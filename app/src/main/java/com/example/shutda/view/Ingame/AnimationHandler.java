package com.example.shutda.view.Ingame;

import android.os.Handler;
import android.os.Message;

import static com.example.shutda.view.MainActivity.mhandler;

public class AnimationHandler extends TaskQueue {
    private Message msg;

    public AnimationHandler(Message msg) {
        this.msg = msg;
    }

    public void ExecuteTask() {
        System.out.println("In ExecuteTask()");
        mhandler.handleMessage(msg);

    }
}
