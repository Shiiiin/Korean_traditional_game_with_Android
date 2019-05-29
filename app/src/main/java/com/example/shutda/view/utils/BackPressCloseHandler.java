package com.example.shutda.view.utils;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class BackPressCloseHandler {

    private long backKeyClickTime = 0;
    private Activity activity;

    public BackPressCloseHandler(Activity activity)
    {
        this.activity = activity;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyClickTime + 2000) { backKeyClickTime = System.currentTimeMillis();
            showToast();
            return; }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000) {


            activity.finish();
        }
    }

    public void showToast() {
        Toast.makeText(activity, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show(); } }
