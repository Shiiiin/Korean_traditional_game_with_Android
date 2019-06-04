package com.example.shutda.view.Ingame;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.shutda.view.MainActivity;
import com.example.shutda.view.data.User;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.shutda.view.data.constantsField.AITurnPeriod;

public class GameThread extends Thread {

    private static final String TAG = "GameThread";

    private gameViewModel inGame;
    private LiveData<Boolean> gameStatus;

    private LiveData<Boolean> player2Turn;
    private LiveData<Boolean> player3Turn;

    private MainActivity mainActivity;

    public GameThread(gameViewModel viewModel, MainActivity mainActivity) {

        super();

        this.inGame = viewModel;
        this.mainActivity = mainActivity;

        player2Turn = inGame.getPlayer2Turn();
        player3Turn = inGame.getPlayer3Turn();

        gameStatus = inGame.getIngameStatus();

    }

    public void interrupt(){
        super.interrupt();
    }

    @Override
    public synchronized void run() {

        System.out.println("@@@@ Thread Run 실행 @@@@");

        player2Turn.observe(mainActivity, new Observer<Boolean>() {

            @Override
            public void onChanged(@Nullable Boolean aBoolean) {

                if(aBoolean){
                    Timer timer = new Timer();

                    if (inGame.getUsers().getValue().get("player2").isAlive()) {
                        if (inGame.getUsers().getValue().get("player1").isAlive() || inGame.getUsers().getValue().get("player3").isAlive()) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    System.out.println("@@@@ Thread player2 실행 @@@@");
                                    mainActivity.turnTextView.setText("Ai 1 차례 입니다.");
                                    inGame.AiDecisionMakingExecute("player2");
                                }
                            }, AITurnPeriod);
                        }
                    }else{
                        mainActivity.turnTextView.setText("");
                    }
                }
            }
        });

        player3Turn.observe(mainActivity, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {

                if(aBoolean){
                    Timer timer = new Timer();
                    if (inGame.getUsers().getValue().get("player3").isAlive()) {
                        if (inGame.getUsers().getValue().get("player1").isAlive() || inGame.getUsers().getValue().get("player2").isAlive()) {
                            timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                    System.out.println("@@@@ Thread player3 실행 @@@@");
                                    mainActivity.turnTextView.setText("Ai 2 차례 입니다.");
                                    inGame.AiDecisionMakingExecute("player3");
                                    }
                                }, AITurnPeriod);
                            }
                        }
                    }else{
                    mainActivity.turnTextView.setText("");
                }
                }
            });

    }// run End

}
