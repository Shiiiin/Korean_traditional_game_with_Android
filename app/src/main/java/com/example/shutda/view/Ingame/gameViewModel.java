package com.example.shutda.view.Ingame;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import android.os.Message;
import android.widget.Toast;

import com.example.shutda.view.data.User;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


import static com.example.shutda.view.MainActivity.mhandler;
import static com.example.shutda.view.data.DummyCards.*;

public class gameViewModel extends ViewModel{

    //Game based
    private List<Integer> ShuffledCards = new ArrayList<>();
    private Queue<Integer> CardsMachine = new LinkedList<>();

    private MutableLiveData<HashMap<String, User>> users = new MutableLiveData<>();
    private MutableLiveData<Boolean> statement = new MutableLiveData<>();
    private MutableLiveData<Boolean []> buttonSet = new MutableLiveData<>();
    private MutableLiveData<Integer> TotalBettingMoney = new MutableLiveData<>();
    private MutableLiveData<Long> player1Score = new MutableLiveData<>();
    private MutableLiveData<Long> player2Score = new MutableLiveData<>();
    private MutableLiveData<Long> player3Score = new MutableLiveData<>();
    private MutableLiveData<Boolean> UserTurn = new MutableLiveData<>();
    private MutableLiveData<Boolean> player2Turn = new MutableLiveData<>();
    private MutableLiveData<Boolean> player3Turn = new MutableLiveData<>();
    private MutableLiveData<Integer> CallNumber = new MutableLiveData<>();
    private MutableLiveData<Integer> DieNumber = new MutableLiveData<>();
    private MutableLiveData<Integer> HalfNumber = new MutableLiveData<>();
    private MutableLiveData<Boolean> FirstTurn = new MutableLiveData<>();
    private MutableLiveData<Boolean> SecondsCardsPollingTrigger = new MutableLiveData<>();
    private MutableLiveData<String> Winner = new MutableLiveData<>();
    private MutableLiveData<Boolean> EnableCheck = new MutableLiveData<>();

    public LiveData<HashMap<String, User>> getUsers(){ return users;}
    public LiveData<Boolean> getIngameStatus(){ return statement; }
    public LiveData<Integer> getTotalBettingMoney(){ return TotalBettingMoney; }
    public LiveData<Boolean[]> getButtonSet() {return buttonSet; }
    public MutableLiveData<Long> getPlayer1Score() { return player1Score;    }
    public MutableLiveData<Long> getPlayer2Score() { return player2Score;    }
    public MutableLiveData<Long> getPlayer3Score() { return player3Score;    }
    public MutableLiveData<Boolean> getUserTurn() { return UserTurn; }
    public MutableLiveData<Boolean> getPlayer2Turn() { return player2Turn;  }
    public MutableLiveData<Boolean> getPlayer3Turn() { return player3Turn;  }
    public MutableLiveData<Integer> getCallNumber() { return CallNumber;  }
    public MutableLiveData<Integer> getDieNumber() { return DieNumber;  }
    public MutableLiveData<Integer> getHalfNumber() { return HalfNumber;  }
    public MutableLiveData<Boolean> getFirstTurn() { return FirstTurn; }
    public MutableLiveData<String> getWinner() { return Winner; }
    public MutableLiveData<Boolean> getSecondsCardsPollingTrigger() { return SecondsCardsPollingTrigger; }

    private Random random = new Random();
    private int MaxPlayerBattingScore = 0;
    public WinnerChecker winnerChecker;
    private int CheckNumber;

    public void setUsers(HashMap<String, User> user){

        users.postValue(user);
        player1Score.postValue(user.get("player1").getScore());
        player2Score.postValue(user.get("player2").getScore());
        player3Score.postValue(user.get("player3").getScore());

    }


    public void setStatus(Boolean status){
        statement.postValue(status);
    }

    public void setCallNumber(int callNumber) { CallNumber.postValue(callNumber); }

    public void setHalfNumber(int halfNumber) { HalfNumber.postValue(halfNumber); }

    public void setFirstTurn(Boolean firstTurn) { FirstTurn.postValue(firstTurn); }

    public void setSecondsCardsPollingTrigger(Boolean trigger) { SecondsCardsPollingTrigger.postValue(trigger);}

    public void setWinner(String winner) { Winner.postValue(winner); }

    public void setEnableCheck(Boolean enableCheck) { EnableCheck.postValue(enableCheck); }

    public void execute(Context context) {

        switch (Winner.getValue()){
            case "player1":
                UserTurn.postValue(true);
                break;
            case "player2":
                player2Turn.postValue(true);
                break;
            case "player3":
                player3Turn.postValue(true);
                break;
        }
    }

     public void finish() {

         System.out.println("finish");

         UserTurn.postValue(false);
         player2Turn.postValue(false);
         player3Turn.postValue(false);
         System.out.println(UserTurn.getValue());
         System.out.println(player2Turn.getValue());
         System.out.println(player3Turn.getValue());

         //Reset All Data apart from Name & Score
         checkWinner(); //돈가산

     }


     public void initialize() {

         CardShuffling();
         System.out.println("카드시작");
         EnableCheck.postValue(true);
         CheckNumber = 0;

         for (int i = 1; i <= users.getValue().size(); i++) {
             String key = "player" + i;
             User player = users.getValue().get(key);
             player.setSumOfBetting(0);

             player.setAlive(true);

             player.setCard1(CardsMachine.poll());
             player.setCard2(CardsMachine.poll());

             System.out.println(key +"'s Card1 : "+player.getCard1());
             System.out.println(key +"'s Card2 : "+player.getCard2());
         }

         System.out.println("카드끝!!!!");

         //버튼 초기화
         users.getValue().get("player1").setButtonClickEnable(true, false, true, true);

         int player1CardValue = users.getValue().get("player1").getCardValues();
         int player2CardValue = users.getValue().get("player2").getCardValues();
         int player3CardValue = users.getValue().get("player3").getCardValues();
         if(winnerChecker == null) {
             winnerChecker = new WinnerChecker(player1CardValue, player2CardValue, player3CardValue);
         }
         else {
             winnerChecker.setPlayersMap(player1CardValue, player2CardValue, player3CardValue);
         }

         MaxPlayerBattingScore = 0;
//         CallNumber.postValue(0);
         HalfNumber.postValue(0);
         DieNumber.postValue(0);

     }

    public void CardShuffling() {

        if(!ShuffledCards.isEmpty()){
            CardsMachine.clear();
            ShuffledCards.clear();
        }


        for(int i=0; i<TheNumberofCard; i++) {

            ShuffledCards.add(Cards[i]);

        }

        Collections.shuffle(ShuffledCards);

        CardsMachine.addAll(ShuffledCards);

    }

    //처음 판돈 거는거
    public boolean BaseBettingExecute(Activity view, int money) {

        System.out.println( users.getValue().get("player1").getName() +"%%%%%"+ users.getValue().get("player1").getScore());

        StringBuffer toastMessage = new StringBuffer();

        boolean a = users.getValue().get("player1").Betting(money);
        boolean b = users.getValue().get("player2").Betting(money);
        boolean c = users.getValue().get("player3").Betting(money);

        if(!b) {
            users.getValue().get("player2").setScore(100000);
            b = users.getValue().get("player2").Betting(money);
            Toast.makeText(view, "AI1 DIE!!" ,Toast.LENGTH_LONG).show();
        }
        if(!c) {
            users.getValue().get("player3").setScore(100000);
            c = users.getValue().get("player3").Betting(money);
            Toast.makeText(view, "AI3 DIE!!" ,Toast.LENGTH_LONG).show();
        }

        if(a & b & c){

            int userNumber = users.getValue().size();
            TotalBettingMoney.postValue(userNumber*money);
            player1Score.postValue(users.getValue().get("player1").getScore());
            player2Score.postValue(users.getValue().get("player2").getScore());
            player3Score.postValue(users.getValue().get("player3").getScore());
            System.out.println( "현재 배팅액" + getTotalBettingMoney().getValue());

            return true;
        }
        else{
            if(!a){
                toastMessage.append("player1 ");
            }

            Toast.makeText(view, toastMessage.toString() +"의 배팅금액이 부족합니다. " ,Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void HalfButtonExecute(Activity view, String player) {
        EnableCheck.postValue(false);

        User currentplayer = users.getValue().get(player);

        int bettingMoney = TotalBettingMoney.getValue();

        int halfBetting = bettingMoney / 2;

        //True일때 betting성공, False일때 돈부족함
        boolean a = currentplayer.Betting(halfBetting);

        HalfNumber.postValue(HalfNumber.getValue() + 1);

        if(a == true){

            this.TotalBettingMoney.postValue(bettingMoney + halfBetting);
            player1Score.postValue(users.getValue().get("player1").getScore());
            MaxPlayerBattingScore = halfBetting;
            System.out.println(MaxPlayerBattingScore);

            currentplayer.setButtonClickEnable(false, true, true, true);

        }
        if(a == false){

            Toast.makeText(view,"올인!!! " ,Toast.LENGTH_LONG).show();

            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            player1Score.postValue(users.getValue().get("player1").getScore());
            MaxPlayerBattingScore = (MaxPlayerBattingScore < All_in) ? All_in : MaxPlayerBattingScore;
            System.out.println(MaxPlayerBattingScore);
            System.out.println("All_in");

            currentplayer.setButtonClickEnable(false, true, false, false);

        }

        if(users.getValue().get("player2").isAlive()) {

            UserTurn.postValue(false);

            player2Turn.postValue(true);
        }
        else {

            UserTurn.postValue(false);

            player3Turn.postValue(true);
        }

    }

    public void DieButtonExecute(Activity view, String player) {
        EnableCheck.postValue(false);

        User currentplayer = users.getValue().get(player);

        currentplayer.setSumOfBetting(0);

        currentplayer.setButtonClickEnable(false, false, false, false);

        currentplayer.setAlive(false);

        int dieNumber = DieNumber.getValue();
        DieNumber.postValue(dieNumber+1);

        currentplayer.setCard1(-1);
        currentplayer.setCard2(-1);
        winnerChecker.setPlayer("player1", -2);

        if(users.getValue().get("player2").isAlive()) {

            UserTurn.postValue(false);
            player2Turn.postValue(true);
        }
        else {

            UserTurn.postValue(false);
            player3Turn.postValue(true);
        }

    }

    public void CallButtonExecute(Activity view, String player) {

        User currentplayer = users.getValue().get(player);

        //True일때 betting성공, False일때 돈부족함

        int HalfTurnPlayerBettingMoney = MaxPlayerBattingScore;
        System.out.println(HalfTurnPlayerBettingMoney);

        int bettingMoney = TotalBettingMoney.getValue();

        int money = HalfTurnPlayerBettingMoney - currentplayer.getSumOfBetting();
        money = (money < 0) ? 0 : money;

        boolean a = currentplayer.Betting(money);

        int callNumber = CallNumber.getValue();
        CallNumber.postValue(callNumber+1);

        if(a == true){

            this.TotalBettingMoney.postValue(bettingMoney + money);
            player1Score.postValue(users.getValue().get("player1").getScore());

            currentplayer.setButtonClickEnable(false, true, true, true);

        }
        if(a == false){

            Toast.makeText(view,"올인! " ,Toast.LENGTH_LONG).show();

            int All_in = currentplayer.All_in();
            System.out.println("All_in");

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            player1Score.postValue(users.getValue().get("player1").getScore());

            currentplayer.setButtonClickEnable(false, true, false, false);

        }

        if(users.getValue().get("player2").isAlive()) {

            UserTurn.postValue(false);
            player2Turn.postValue(true);
        }
        else {

            UserTurn.postValue(false);
            player3Turn.postValue(true);
        }

    }

    public void CheckButtonExecute(Activity view, String player) {
        EnableCheck.postValue(false);
        CheckNumber = 1;

        User currentplayer = users.getValue().get(player);
        currentplayer.setButtonClickEnable(false, true, true, true);

        if(users.getValue().get("player2").isAlive()) {

            UserTurn.postValue(false);
            player2Turn.postValue(true);
        }
        else {

            UserTurn.postValue(false);
            player3Turn.postValue(true);
        }
    }


        //Ai Decision Making
    public void AiDecisionMakingExecute(String player){
        if(EnableCheck.getValue()) {
            users.getValue().get("player1").setEnableClickCheckButton(false);
            users.getValue().get("player1").setEnableClickCallButton(true);
        }

        User currentplayer = users.getValue().get(player);

        System.out.println(currentplayer.getName() + "DecisionMaking 진입!!");

        int judge = winnerChecker.carculateCards(player);

        float RandomNum = random.nextFloat();

        System.out.println(RandomNum);

        if(currentplayer.getScore() == 0)
            AiCallExecute(player);

        else if(judge > 9) {
            if(RandomNum >= 0 && RandomNum < 0.6) { //60%
                AiHalfExecute(player);
            }
            else if(RandomNum >= 0.6 && RandomNum < 0.85) { //25%
                if(EnableCheck.getValue()){
                    AiCheckExecute(player);
                }
                else {
                    AiCallExecute(player);
                }
            }
            else { //15%
                AiDieExecute(player);
            }
        }

        else if(judge > 6 & judge <= 9){
            if(RandomNum >= 0 && RandomNum < 0.4) { //40%
                AiHalfExecute(player);
            }
            else if(RandomNum >= 0.4 && RandomNum < 0.7) { //30%
                if(EnableCheck.getValue()){
                    AiCheckExecute(player);
                }
                else {
                    AiCallExecute(player);
                }
            }
            else { //30%
                AiDieExecute(player);
            }
        }

        else {
            if(RandomNum >= 0 && RandomNum < 0.2) { //20%
                AiHalfExecute(player);
            }
            else if(RandomNum >= 0.2 && RandomNum < 0.55) { //35%
                if(EnableCheck.getValue()){
                    AiCheckExecute(player);
                }
                else {
                    AiCallExecute(player);
                }            }
            else { //45%
                AiDieExecute(player);
            }
        }


        if (player == "player2") {

            if (users.getValue().get("player3").isAlive()) {

                player2Turn.postValue(false);
                player3Turn.postValue(true);
            } else {

                player2Turn.postValue(false);
                UserTurn.postValue(true);
            }

        } else if (player == "player3") {
            if (users.getValue().get("player1").isAlive()) {

                player3Turn.postValue(false);
                UserTurn.postValue(true);
            } else {
                player3Turn.postValue(false);

                player2Turn.postValue(true);
            }
        }

        EnableCheck.postValue(false);
    }

    public void AiHalfExecute(String player) {

        System.out.println("@@@@ Thread  " + player + "  Half 실행 @@@@");

        User currentplayer = users.getValue().get(player);

        //True일때 betting성공, False일때 돈부족함

        int bettingMoney = TotalBettingMoney.getValue();

        int halfBetting = (int) Math.floor(bettingMoney / 2);

        boolean a = currentplayer.Betting(halfBetting);

        HalfNumber.postValue(HalfNumber.getValue() + 1);

        if(a == true){

            this.TotalBettingMoney.postValue(bettingMoney + halfBetting);
            MaxPlayerBattingScore = halfBetting;
            System.out.println(MaxPlayerBattingScore);

            if(player == "player2"){
                player2Score.postValue(users.getValue().get("player2").getScore());
                Message msg = new Message();
                msg.what = 23;
                new EventHandler(msg).addTask();
            }
            if(player == "player3"){
                player3Score.postValue(users.getValue().get("player3").getScore());
                Message msg = new Message();
                msg.what = 33;
                new EventHandler(msg).addTask();
            }

        }
        if(a == false){

            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            MaxPlayerBattingScore = (MaxPlayerBattingScore < All_in) ? All_in : MaxPlayerBattingScore;
            System.out.println(MaxPlayerBattingScore);
            System.out.println("All_in");

            if(player == "player2"){
                player2Score.postValue(users.getValue().get("player2").getScore());
                Message msg = new Message();
                msg.what = 23;
                new EventHandler(msg).addTask();

            }
            if(player == "player3"){
                player3Score.postValue(users.getValue().get("player3").getScore());
                Message msg = new Message();
                msg.what = 33;
                new EventHandler(msg).addTask();

            }
        }

    }

    public void AiDieExecute(String player) {

        int dieNumber = DieNumber.getValue();
        DieNumber.postValue(dieNumber+1);

        System.out.println("@@@@ Thread    " + player + "    Die 실행 @@@@");

        User currentplayer = users.getValue().get(player);

        currentplayer.setSumOfBetting(0);

        currentplayer.setAlive(false);

        currentplayer.setCard1(-1);
        currentplayer.setCard2(-1);
        winnerChecker.setPlayer(player, -2);

        if(player == "player2"){
            Message msg = new Message();
            msg.what = 24;
            new EventHandler(msg).addTask();
//            mhandler.sendEmptyMessage(24);
        }
        if(player == "player3"){
            Message msg = new Message();
            msg.what = 34;
            new EventHandler(msg).addTask();
//            mhandler.sendEmptyMessage(34);
        }
    }

    public void AiCallExecute(String player) {

        int callNumber = CallNumber.getValue();
        CallNumber.postValue(callNumber+1);

        System.out.println("@@@@ Thread    " + player + "    Call 실행 @@@@");

        User currentplayer = users.getValue().get(player);

        int HalfTurnPlayerBettingMoney = MaxPlayerBattingScore;
        System.out.println(HalfTurnPlayerBettingMoney);

        int bettingMoney = TotalBettingMoney.getValue();

        int money = HalfTurnPlayerBettingMoney - currentplayer.getSumOfBetting();
        money = (money < 0) ? 0 : money;

        //True일때 betting성공, False일때 돈부족함
        boolean a = currentplayer.Betting(money);

        if(a == true){

            this.TotalBettingMoney.postValue(bettingMoney + money);

            if(player == "player2"){

                    player2Score.postValue(users.getValue().get("player2").getScore());
                    Message msg = new Message();
                    msg.what = 22;
                    new EventHandler(msg).addTask();
//                      mhandler.sendEmptyMessage(22);

            }
            if(player == "player3"){

                    player3Score.postValue(users.getValue().get("player3").getScore());
                    Message msg = new Message();
                    msg.what = 32;
                    new EventHandler(msg).addTask();
//                      mhandler.sendEmptyMessage(32);

            }

        }
        if(a == false){
            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            System.out.println("All_in");

            if(player == "player2"){
                    player2Score.postValue(users.getValue().get("player2").getScore());
                    Message msg = new Message();
                    msg.what = 22;
                    new EventHandler(msg).addTask();
//                      mhandler.sendEmptyMessage(22);
            }
            if(player == "player3"){

                    player3Score.postValue(users.getValue().get("player3").getScore());
                    Message msg = new Message();
                    msg.what = 32;
                    new EventHandler(msg).addTask();
//                mhandler.sendEmptyMessage(32);

            }

        }

    }

    public void AiCheckExecute(String player) {
        System.out.println("@@@@ Thread    " + player + "    Check 실행 @@@@");
        CheckNumber = 1;

        if(player == "player2"){
            Message msg = new Message();
            msg.what = 21;
            new EventHandler(msg).addTask();
//
        }
        if(player == "player3"){

            Message msg = new Message();
            msg.what = 31;
            new EventHandler(msg).addTask();
//

        }
    }


        public void checkWinner() {

        System.out.println("checkwinner");

        Winner.setValue(winnerChecker.WinnerClassifier());

        getWinner().setValue(winnerChecker.WinnerClassifier());

        String winner = winnerChecker.WinnerClassifier();

        switch(winner) {
            case "player1":
                System.out.println("*******player1 이겼따*************");
                users.getValue().get("player1").setScore(users.getValue().get("player1").getScore() + TotalBettingMoney.getValue());
                player1Score.postValue(users.getValue().get("player1").getScore());
                TotalBettingMoney.postValue(0);
                break;

            case "player2":
                System.out.println("*******player2 이겼따*************");
                users.getValue().get("player2").setScore(users.getValue().get("player2").getScore() + TotalBettingMoney.getValue());
                player2Score.postValue(users.getValue().get("player2").getScore());
                TotalBettingMoney.postValue(0);
                break;

            case "player3":
                System.out.println("*******player3 이겼따*************");
                users.getValue().get("player3").setScore(users.getValue().get("player3").getScore() + TotalBettingMoney.getValue());
                player3Score.postValue(users.getValue().get("player3").getScore());
                TotalBettingMoney.postValue(0);
                break;

            case "rematch":
                System.out.println("*******rematch*************");
                ArrayList<Integer> list = new ArrayList<>();
                list.add(1);
                list.add(2);
                list.add(3);
                rematchPlayers(list);
                break;

            case "rematch12":
                System.out.println("*******rematch12*************");
                ArrayList<Integer> list1 = new ArrayList<>();
                list1.add(1);
                list1.add(2);
                rematchPlayers(list1);
                break;

            case "rematch31":
                System.out.println("*******rematch31*************");
                ArrayList<Integer> list2 = new ArrayList<>();
                list2.add(1);
                list2.add(3);
                rematchPlayers(list2);
                break;

            case "rematch23":
                System.out.println("*******rematch23*************");
                ArrayList<Integer> list3 = new ArrayList<>();
                list3.add(2);
                list3.add(3);
                rematchPlayers(list3);
                break;
        }

    }

    public void uploadScoreToFirestore(DocumentReference database){

        database.update("score", users.getValue().get("player1").getScore());

    }

    public Boolean checkEnd() {

        if (HalfNumber.getValue() != null) {
            if (HalfNumber.getValue() > 0) {
                if (2 == CallNumber.getValue() + DieNumber.getValue()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if(2 == DieNumber.getValue()) {
                    return true;
                }
                else if (3 == CallNumber.getValue() + DieNumber.getValue() + CheckNumber) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }

    public void rematchPlayers(ArrayList<Integer> Keys) {

        CardShuffling();

        System.out.println("카드시작");

        for (int i = 0; i < Keys.size(); i++) {

            String playername = "player" + Keys.get(i);
            User player = users.getValue().get(playername);
            player.setSumOfBetting(0);
            player.setAlive(true);

            player.setCard1(CardsMachine.poll());
            player.setCard2(CardsMachine.poll());

            System.out.println(playername + "'s Card1 : " + player.getCard1());
            System.out.println(playername + "'s Card2 : " + player.getCard2());

            if (Keys.get(i) == 1) {
                System.out.println("&&&&&&&&&&&&&&1");
                Message m = new Message();
                m.what = 1;
                new EventHandler(m).addTask();
            }
            if (Keys.get(i) == 2) {
                System.out.println("&&&&&&&&&&&&&&2");
                Message m = new Message();
                m.what = 2;
                new EventHandler(m).addTask();
            }
            if (Keys.get(i) == 3) {
                System.out.println("&&&&&&&&&&&&&&3");
                Message m = new Message();
                m.what = 3;
                new EventHandler(m).addTask();
            }
        }
        System.out.println("카드끝!!!!");

        //버튼 초기화
        if(users.getValue().get("player1").isAlive())
            users.getValue().get("player1").setButtonClickEnable(true, false, true, true);

        int player1CardValue = users.getValue().get("player1").getCardValues();
        int player2CardValue = users.getValue().get("player2").getCardValues();
        int player3CardValue = users.getValue().get("player3").getCardValues();
        if(winnerChecker == null) {
            winnerChecker = new WinnerChecker(player1CardValue, player2CardValue, player3CardValue);
        }
        else {
            winnerChecker.setPlayersMap(player1CardValue, player2CardValue, player3CardValue);
        }

        FirstTurn.postValue(true);
        HalfNumber.postValue(0);


        if(users.getValue().get("player1").isAlive())
            UserTurn.postValue(true);
        else
            player2Turn.postValue(true);
    }
}
