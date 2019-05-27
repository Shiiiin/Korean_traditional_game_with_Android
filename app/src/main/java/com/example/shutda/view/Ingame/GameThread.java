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

    private User player1;
    private User player2;
    private User player3;

    public GameThread(gameViewModel viewModel, MainActivity mainActivity) {

        super();

        this.inGame = viewModel;
        this.mainActivity = mainActivity;

        gameStatus = inGame.getIngameStatus();
        player2Turn = inGame.getPlayer2Turn();
        player3Turn = inGame.getPlayer3Turn();


    }

//    public synchronized void RegistPlayers(){
//
//        player1 = inGame.getUsers().getValue().get("player1");
//        player2 = inGame.getUsers().getValue().get("player2");
//        player3 = inGame.getUsers().getValue().get("player3");
//
//
//    }


    @Override
    public synchronized void start() {

        //이 메소드 사용금지

        super.start();

        System.out.println("@@@@ Thread start 실행 @@@@");


    }

    @Override
    public State getState() {
        return super.getState();
    }

    @Override
    public void interrupt() {
        super.interrupt();

        System.out.println("@@@@ Thread Interrupt 실행 @@@@");
    }

    @Override
     public synchronized void run() {
            System.out.println("@@@@ Thread AI 등록 실행 @@@@");

            System.out.println("@@@@ Thread Run 실행 @@@@");

            //TODO 한바퀴 돌리는데는 성공 이제 모두콜 모두 죽었을때 구현해야함

            player2Turn.observe(mainActivity, new Observer<Boolean>() {
                        @Override
                        public void onChanged(@Nullable Boolean aBoolean) {

                            if(aBoolean){
                                Timer timer = new Timer();

                                if(inGame.getUsers().getValue().get("player2").isAlive()){

                                    System.out.println("@@@@ Thread player2 실행 @@@@");

                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            inGame.AiDecisionMakingExecute("player2");
                                        }
                                    },AITurnPeriod);
                                }else{
//                                    inGame.getUsers().getValue().get("player3").setTurn(true);
                                    inGame.getPlayer3Turn().postValue(true);
                                }
                            }

                        }
                    });

            player3Turn.observe(mainActivity, new Observer<Boolean>() {
                        @Override
                        public void onChanged(@Nullable Boolean aBoolean) {

                            Timer timer = new Timer();

                            if(aBoolean){

                                if(inGame.getUsers().getValue().get("player3").isAlive()){

                                    System.out.println("@@@@ Thread player3 실행 @@@@");

                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            inGame.AiDecisionMakingExecute("player3");
                                        }
                                    },AITurnPeriod);
                                }else {
//                                    inGame.getUsers().getValue().get("player1").setTurn(true);
                                    inGame.getUserTurn().postValue(true);
                                }
                            }

                        }
                    });



        }// run End


}
