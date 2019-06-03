package com.example.shutda.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.shutda.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int RC_SIGN_IN = 9001;
    /**
     * A dummy authentication store containing known user names and passwords.
     */
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private FirebaseFirestore mDB;

    // UI references.
    private TextInputLayout textInputLayout1;
    private TextInputLayout textInputLayout2;
    private TextInputEditText mEmailView;
    private TextInputEditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mLayoutView;
    private InputMethodManager hiddenkeybord;

    private Button SiginButtonwithEmail;
    private SignInButton SigninButtonwithGoogle;
    private Button SignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        // Configure Google Sign In

        // Button listeners
        SigninButtonwithGoogle = (SignInButton) findViewById(R.id.google_sign_in_button);

        SigninButtonwithGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();

        // Set up the login form.

        mLayoutView = (ConstraintLayout) findViewById(R.id.login_layout);

        mLayoutView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenkeybord = (InputMethodManager)getSystemService(LoginActivity.this.INPUT_METHOD_SERVICE);
                hiddenkeybord.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
                hiddenkeybord.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
            }
        });
        textInputLayout1 = findViewById(R.id.textInputLayout);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        SiginButtonwithEmail = (Button) findViewById(R.id.email_sign_in_button);
        SignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        SignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newUserRegisteration();
            }
        });
        SiginButtonwithEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(isNetworkConnected() == false){

            mAuth.signOut();

            SignUpButton.setEnabled(false);
            SiginButtonwithEmail.setEnabled(false);
            SigninButtonwithGoogle.setEnabled(false);
            mEmailView.setEnabled(false);
            mPasswordView.setEnabled(false);

            Snackbar.make(mLayoutView,"네트워크에 연결되어있지 않습니다..ㅋㅋ;",Snackbar.LENGTH_INDEFINITE).show();

        }
        if(currentUser != null && isNetworkConnected() == true){
            sendToMenu();
        }

    }

    private boolean isNetworkConnected(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void sendToMenu() {
        Intent loginIntent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(loginIntent);
    }

    private void newUserRegisteration() {
        Intent SignUpIntent = new Intent ( LoginActivity.this, SignUpActivity.class);
        startActivity(SignUpIntent);
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if(TextUtils.isEmpty(email)){
            mEmailView.setError("이메일을 입력하세요.");
            mEmailView.requestFocus();
        }
        else if(isEmailValid(email) == false && !TextUtils.isEmpty(email)){
            mEmailView.setError("올바른 이메일 형태가 아닙니다.");
            mEmailView.requestFocus();
        }
        else if(isPasswordValid(password) == false){
            mPasswordView.setError("비밀번호는 6자 이상이여야 합니다.");
            mPasswordView.requestFocus();
            mPasswordView.setText("");
        }

        if (isEmailValid(email) && isPasswordValid(password)) {
            //둘다 있을때
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String token_id = FirebaseInstanceId.getInstance().getToken();
                            String current_id = mAuth.getCurrentUser().getUid();

                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("token_id", token_id);

                            mDB.collection("Users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    showProgress(true);
                                    sendToMenu();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("EFERERER", e.getMessage() );
                                }
                            });

                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                boolean badformatEmail = e.getMessage().contains("The email address is badly formatted");
                                boolean invaildUserMessage = e.getMessage().contains("There is no user record");
                                boolean invalidPassword = e.getMessage().contains("The password is invalid");
                                boolean blockedProcess = e.getMessage().contains("Try again later");
                                if(invaildUserMessage){
                                    mEmailView.setError(getString(R.string.error_invalid_email));
                                    mEmailView.requestFocus();
                                }

                                if(badformatEmail){
                                    mEmailView.setError(getString(R.string.error_format_email));
                                    mEmailView.requestFocus();
                                }

                                if(invalidPassword){
                                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                                    mPasswordView.requestFocus();
                                    mPasswordView.setText("");
                                }

                                if(blockedProcess){
                                    mPasswordView.setError("서버 오류, 다시 시도해주세요. (틀린 비밀번호를 여러번 빠르게 로그인하면 생김)");
                                    mPasswordView.requestFocus();
                                    mPasswordView.setText("");
                                }

                                Toast.makeText(LoginActivity.this, "Test : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                        showProgress(false);
                    }
                });
            }


        }
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() >= 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, wich allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            textInputLayout1.setVisibility(show ? View.GONE : View.VISIBLE);
            textInputLayout1.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    textInputLayout1.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            textInputLayout2.setVisibility(show ? View.GONE : View.VISIBLE);
            textInputLayout2.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    textInputLayout2.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mEmailView.setVisibility(show ? View.GONE : View.VISIBLE);
            mEmailView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEmailView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mPasswordView.setVisibility(show ? View.GONE : View.VISIBLE);
            mPasswordView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPasswordView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            SignUpButton.setVisibility(show ? View.GONE : View.VISIBLE);
            SignUpButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    SignUpButton.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            SiginButtonwithEmail.setVisibility(show ? View.GONE : View.VISIBLE);
            SiginButtonwithEmail.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    SiginButtonwithEmail.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            SigninButtonwithGoogle.setVisibility(show ? View.GONE : View.VISIBLE);
            SigninButtonwithGoogle.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    SigninButtonwithGoogle.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mEmailView.setVisibility(show ? View.GONE : View.VISIBLE);
            mPasswordView.setVisibility(show ? View.GONE : View.VISIBLE);
            SigninButtonwithGoogle.setVisibility(show ? View.GONE : View.VISIBLE);
            SiginButtonwithEmail.setVisibility(show ? View.GONE : View.VISIBLE);
            SignUpButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("fail to login with google", "Google sign in failed", e);
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Google SignIn", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgress(true);
        // [END_EXCLUDE]

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            final String token_id = FirebaseInstanceId.getInstance().getToken();
                            final String current_id = mAuth.getUid();
                            final String name = mAuth.getCurrentUser().getDisplayName();
                            final String email = mAuth.getCurrentUser().getEmail();

                            final int score = 1000000;


                            mDB.collection("Users").document(current_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if(email.equals(documentSnapshot.getString("email"))){


                                        Map<String, Object> tokenMap2 = new HashMap<>();

                                        tokenMap2.put("token_id", token_id);

                                        mDB.collection("Users").document(current_id).update(tokenMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                sendToMenu();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("EFERERER", e.getMessage() );
                                            }
                                        });


                                    }else {

                                        Map<String, Object> tokenMap = new HashMap<>();

                                        tokenMap.put("email", email);
                                        tokenMap.put("name", name);
                                        tokenMap.put("score", score);
                                        tokenMap.put("token_id", token_id);

                                        mDB.collection("Users").document(current_id).set(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                sendToMenu();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("EFERERER", e.getMessage());
                                            }
                                        });
                                    }
                                }
                            });






                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.login_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        showProgress(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]



    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]




}

