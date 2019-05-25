package com.example.shutda.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shutda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Intent jogboIntent = new Intent(this, JogboActivity.class);
        final Intent mainIntent = new Intent(this, MainActivity.class);
        final Intent loginIntent = new Intent(this, LoginActivity.class);

        SetOnClickListener(R.id.play_button, mainIntent);
        SetOnClickListener(R.id.rank_button, jogboIntent);
        SetOnClickListener(R.id.rule_button, jogboIntent);
        SetOnClickListener(R.id.answer_button, jogboIntent);
        SetLogoutListener(R.id.logout_button, loginIntent);

    }


    private void SetOnClickListener(int button_id, final Intent intent) {
        findViewById(button_id).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intent);
                    }
                }
        );
    }

    private void SetLogoutListener(final int button_id, final Intent intent) {
        final FirebaseDatabase mDB = FirebaseDatabase.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String mUserId = mAuth.getCurrentUser().getUid();
        mDB.getReference("/Users").child(mUserId).child("token_id").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuth.signOut();
                SetOnClickListener(button_id, intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MenuActivity.this, "로그아웃 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
