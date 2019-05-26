package com.example.shutda.view.background;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class BackPressCloseHandler {

    private long backKeyClickTime = 0;
    private Activity activity;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    public BackPressCloseHandler(Activity activity, FirebaseAuth mAuth)
    {
        this.activity = activity;
        this.mAuth = mAuth;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyClickTime + 2000) { backKeyClickTime = System.currentTimeMillis();
            showToast();
            return; }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000) {

//            mDB.collection("Users").document(mAuth.getCurrentUser().getUid()).update("token_id", "");

//            mAuth.signOut();

            activity.finish();
        }
    }

    public void showToast() {
        Toast.makeText(activity, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show(); } }
