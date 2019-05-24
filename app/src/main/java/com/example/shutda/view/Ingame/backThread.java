package com.example.shutda.view.Ingame;

import java.util.Timer;
import java.util.TimerTask;

public class backThread extends Thread {

    private gameViewModel inGame;

    backThread(gameViewModel viewModel){

        this.inGame = viewModel;

    }

    public void test1(){

        Timer timer = new Timer();
        if(inGame.getUsers().getValue().get("player2").isTurn() & inGame.getUsers().getValue().get("player2").isAlive()){

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

    }

    public void test2(){

        Timer timer = new Timer();

        if(inGame.getUsers().getValue().get("player3").isTurn() & inGame.getUsers().getValue().get("player3").isAlive()){

            System.out.println("@@@@ Thread player3 실행 @@@@");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    inGame.AiDecisionMakingExecute("player3");
                }
            },3000);
        }    }

    public void test3(){

        //유저만 죽어있고 AI들만 살아있을 경우 둘이 게임함
        if(!inGame.getUsers().getValue().get("player1").isAlive() & (inGame.getUsers().getValue().get("player2").isAlive() & inGame.getUsers().getValue().get("player3").isAlive())){

            inGame.getUsers().getValue().get("player2").setTurn(true);

            run();

        }

    }
}
