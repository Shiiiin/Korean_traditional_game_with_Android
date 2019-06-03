package com.example.shutda.view.Ingame;

import android.os.Handler;
import android.os.Message;

import java.util.LinkedList;
import java.util.Queue;

import static com.example.shutda.view.MainActivity.mhandler;

public abstract class TaskQueue {

    static Queue<TaskQueue> messageQueue = new LinkedList<>();
    static boolean isTurnOn = true;

    public void addTask(){
        messageQueue.add(this);

        //처음 들어오면 풀링하는거 막아줌
        if(isTurnOn == true){
            isTurnOn = false;
            polling();
        }
    }

    // ExecuteTask의 끝에는 항상 TaskFinishCallback을호출해야함
    public abstract void ExecuteTask();

    public static void TaskFinishCallback(){
        System.out.println("TaskFinishCallback!");
        polling();
    }

    public synchronized static void polling(){
        if(!messageQueue.isEmpty()){
            messageQueue.poll().ExecuteTask();
        }else{
            isTurnOn=true;
        }

    }
}
