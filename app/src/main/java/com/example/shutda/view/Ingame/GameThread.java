package com.example.shutda.view.Ingame;

import android.arch.lifecycle.LiveData;

import com.example.shutda.view.data.User;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameThread extends Thread {

    private static final String TAG = "GameThread";

    private gameViewModel inGame;
    private LiveData<Boolean> gameStatus;

    private User player1;
    private User player2;
    private User player3;

    public GameThread(gameViewModel viewModel) {

        super();

        this.inGame = viewModel;

        gameStatus = inGame.getIngameStatus();


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

        System.out.println("@@@@ Thread Start 실행 @@@@");


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
    public void run() {
        System.out.println("@@@@ Thread AI 등록 실행 @@@@");
        player1 = inGame.getUsers().getValue().get("player1");
        player2 = inGame.getUsers().getValue().get("player2");
        player3 = inGame.getUsers().getValue().get("player3");

        System.out.println("@@@@ Thread Run 실행 @@@@");

        Timer timer = new Timer();


                //우선, 다 죽어있는지 확인
                if(!player1.isAlive() & !player2.isAlive() & !player3.isAlive()){

                    System.out.println("&&&&&&& Thread에서 게임끝!~! &&&&&&&&&&&&");
                    inGame.checkWinner();
                    inGame.setStatus(false);

                }//////////////죽은거 확인끝

        ExecutorService es = Executors.newSingleThreadExecutor();

                if(player2.isTurn() & player2.isAlive()){

                    System.out.println("@@@@ Thread player2 실행 @@@@");

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            inGame.AiDecisionMakingExecute("player2");
                        }
                    },3000);
                }else{
                    inGame.getUsers().getValue().get("player3").setTurn(true);
                }

                if(player3.isTurn() & player3.isAlive()){

                    System.out.println("@@@@ Thread player3 실행 @@@@");

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            inGame.AiDecisionMakingExecute("player3");
                        }
                    },3000);
                }

                //유저만 죽어있고 AI들만 살아있을 경우 둘이 게임함
                if(!player1.isAlive() & (player2.isAlive() & player3.isAlive())){

                    inGame.getUsers().getValue().get("player2").setTurn(true);

                    run();

                }


    }// run End



}
