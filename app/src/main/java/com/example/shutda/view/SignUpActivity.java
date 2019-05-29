package com.example.shutda.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shutda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private View mSignupLayout;
    private TextInputEditText mEmailField;
    private TextInputEditText mPasswordField;
    private EditText mNameField;
    private Button mSignupButton;
    private Button mMoveToBackward;
    private String name,email,password;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDB;
    private ProgressBar mProgressBar;
    private InputMethodManager hiddenkeybord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        mAuth = FirebaseAuth.getInstance();
        mDB=FirebaseFirestore.getInstance();

        mSignupLayout =  findViewById(R.id.sign_up_layout);
        mEmailField =  findViewById(R.id.emailEditView);
        mPasswordField=  findViewById(R.id.passwordEditView);
        mNameField = (EditText) findViewById(R.id.nameEditView);
        mSignupButton= (Button) findViewById(R.id.confirmButton);
        mMoveToBackward = (Button) findViewById(R.id.backwardButton);
        mProgressBar = (ProgressBar) findViewById(R.id.signUpProgressBar);

        mSignupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenkeybord = (InputMethodManager)getSystemService(SignUpActivity.this.INPUT_METHOD_SERVICE);
                hiddenkeybord.hideSoftInputFromWindow(mEmailField.getWindowToken(), 0);
                hiddenkeybord.hideSoftInputFromWindow(mPasswordField.getWindowToken(), 0);
                hiddenkeybord.hideSoftInputFromWindow(mNameField.getWindowToken(), 0);
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = mNameField.getText().toString();
                email = mEmailField.getText().toString();
                password = mPasswordField.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmailField.setError("이메일을 입력하세요.");
                    mEmailField.requestFocus();
                }
                else if(!isEmailValid(email) && !TextUtils.isEmpty(email)){
                    mEmailField.setError("올바른 이메일 형태가 아닙니다.");
                    mEmailField.requestFocus();
                }
                else if(!isPasswordValid(password)){
                    mPasswordField.setError("비밀번호는 6자 이상이여야 합니다.");
                    mPasswordField.requestFocus();
                    mPasswordField.setText("");
                }else if(TextUtils.isEmpty(name)){
                    mNameField.setError("이름을 입력하세요.");
                    mNameField.requestFocus();
                }

                if(!TextUtils.isEmpty(name) && isEmailValid(email) && isPasswordValid(password)){

                    mProgressBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String user_id = mAuth.getCurrentUser().getUid();
                                String token_id= FirebaseInstanceId.getInstance().getToken();

                                Map<String, Object> userMap = new HashMap<>();

                                userMap.put("name", name);
                                userMap.put("email", email);
                                userMap.put("score", 1000000);
                                userMap.put("token_id",token_id);


                                mDB.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignUpActivity.this, "등록 완료", Toast.LENGTH_SHORT).show();

                                        sendBack();

                                    }



                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        mPasswordField.setText("");
                                        mNameField.setText("");
                                        Toast.makeText(SignUpActivity.this ,"등록 실패 ", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else{
                                try{
                                    throw task.getException();
                                } catch (FirebaseAuthEmailException e) {
                                    mEmailField.setError("이메일 에러" + e.getMessage());
                                    mEmailField.requestFocus();
                                }catch (FirebaseAuthWeakPasswordException e){
                                    mPasswordField.setError("패스워드 에러"+ e.getReason());
                                    mPasswordField.requestFocus();
                                }catch (FirebaseAuthUserCollisionException e ){
                                    mEmailField.setError("이미 등록된 이메일 입니다.");
                                    mEmailField.requestFocus();
                                }
                                catch (Exception e) {
                                    boolean badformatEmail = e.getMessage().contains("The email address is badly formatted");
                                    boolean invaildUserMessage = e.getMessage().contains("There is no user record");
                                    boolean invalidPassword = e.getMessage().contains("The password is invalid");
                                    boolean blockedProcess = e.getMessage().contains("Try again later");
                                    if(invaildUserMessage){
                                        mEmailField.setError(getString(R.string.error_invalid_email));
                                        mEmailField.requestFocus();
                                    }

                                    if(badformatEmail){
                                        mEmailField.setError(getString(R.string.error_format_email));
                                        mEmailField.requestFocus();
                                    }

                                    if(invalidPassword){
                                        mPasswordField.setError(getString(R.string.error_incorrect_password));
                                        mPasswordField.requestFocus();
                                        mPasswordField.setText("");
                                    }

                                    if(blockedProcess){
                                        mPasswordField.setError("서버 오류, 다시 시도해주세요. (틀린 비밀번호를 여러번 빠르게 로그인하면 생김)");
                                        mPasswordField.requestFocus();
                                        mPasswordField.setText("");
                                    }

                                    Toast.makeText(SignUpActivity.this, "Test : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }



                            }
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

            }
        });

        mMoveToBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void sendBack() {
        Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}
