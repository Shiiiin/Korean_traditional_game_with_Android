package com.example.shutda.view.Ingame;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shutda.R;
import com.example.shutda.view.data.User;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


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
    private MutableLiveData<String[]> Locked = new MutableLiveData<>();


    private ImageView user2Card1;
    private ImageView user2Card2;
    private ImageView user3Card1;
    private ImageView user3Card2;
    public ImageView user2call;
    public ImageView user2die;
    public ImageView user2half;
    public ImageView user3call;
    public ImageView user3die;
    public ImageView user3half;

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
    public MutableLiveData<String[]> getLocked() { return Locked; }


    private Random random = new Random();
    private int MaxPlayerBattingScore = 0;
    private WinnerChecker winnerChecker;
    private String[] LockArray;

    public void setUsers(HashMap<String, User> user){

        users.postValue(user);
        player1Score.postValue(user.get("player1").getScore());
        player2Score.postValue(user.get("player2").getScore());
        player3Score.postValue(user.get("player3").getScore());

    }


    public void setStatus(Boolean status){
        statement.postValue(status);
    }

    public void setButtonSet(Boolean [] buttons){ buttonSet.postValue(buttons); }

    public void setTotalBettingMoney(int money){ TotalBettingMoney.postValue(money); }

    public void setCallNumber(int callNumber) { CallNumber.postValue(callNumber); }

    public void setDieNumber(int dieNumber) { DieNumber.postValue(dieNumber); }

    public void setHalfNumber(int halfNumber) { HalfNumber.postValue(halfNumber); }

    public void setUserTurn(Boolean userTurn) { UserTurn.postValue(userTurn); }

    public void setFirstTurn(Boolean firstTurn) { FirstTurn.postValue(firstTurn); }

    public void setLocked(String[] locked) { Locked.postValue(locked); }

    public void execute(Context context, String Winner) {

        //첫번째 턴 정하기 (나중에 메소드 만들어서 턴 정해야함)
        //player1.setTurn(true);
        //게임 시작했을때 버튼(무조건 사용자 먼저 시작이라 모든 버튼 클릭가능)
        switch (Winner){
            case "player1":
                users.getValue().get("player1").setTurn(true);
                UserTurn.postValue(true);
                break;
            case "player2":
                users.getValue().get("player2").setTurn(true);
                player2Turn.postValue(true);
                break;
            case "player3":
                users.getValue().get("player3").setTurn(true);
                player3Turn.postValue(true);
                break;
        }
        ///////////////////////////////////////////////////////
    }

     public String finish() {

         //TODO 이게 문제일수도 있음
         System.out.println("finish");
//         FirstTurn.postValue(true);

         for (int i = 1; i <= users.getValue().size(); i++) {
             String key = "player" + i;
             User player = users.getValue().get(key);
             player.setAlive(false);
         }

         UserTurn.postValue(false);
         player2Turn.postValue(false);
         player3Turn.postValue(false);
         System.out.println(UserTurn.getValue());
         System.out.println(player2Turn.getValue());
         System.out.println(player3Turn.getValue());

         //Reset All Data apart from Name & Score
         String winner = checkWinner(); //돈가산

         return winner;
     }


     public void initialize() {

         CardShuffling();
         System.out.println("카드시작");

         for (int i = 1; i <= users.getValue().size(); i++) {
             String key = "player" + i;
             User player = users.getValue().get(key);
             player.setSumOfBetting(0);
//             player.setTurn(false);
             player.setAlive(true);

             player.setCard1(CardsMachine.poll());
             player.setCard2(CardsMachine.poll());

             System.out.println(key +"'s Card1 : "+player.getCard1());
             System.out.println(key +"'s Card2 : "+player.getCard2());
         }

         System.out.println("카드끝!!!!");

         //버튼 초기화
         users.getValue().get("player1").setButtonClickEnable(true, true, true, false);

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
         CallNumber.postValue(0);
         DieNumber.postValue(0);
         HalfNumber.postValue(0);

//         users.getValue().get(winner).(true);

//         TotalBettingMoney.postValue(0);
             //TODO 시작 할 때 모든 이미지 제거 (이런식으로)
         /*user2Card1.setVisibility(View.INVISIBLE);
        user3Card1.setVisibility(View.GONE);
        user2Card2.setVisibility(View.GONE);
        user3Card2.setVisibility(View.INVISIBLE);
        user2call.setVisibility(View.GONE);
        user2die.setVisibility(View.GONE);
        user2half.setVisibility(View.GONE);
        user3call.setVisibility(View.GONE);
        user3die.setVisibility(View.GONE);
        user3half.setVisibility(View.GONE);
        */

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
//            if(!b){
//                toastMessage.append("player2 ");
//            }
//            if(!c){
//                toastMessage.append("player3 ");
//            }
//
            Toast.makeText(view, toastMessage.toString() +"의 배팅금액이 부족합니다. " ,Toast.LENGTH_LONG).show();
//
            return false;
        }
    }

    public void HalfButtonExecute(Activity view, String player) {
//        LockArray = new String[] {"true", player};
//        Locked.postValue(LockArray);
//        FirstTurn.postValue(false);


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

            currentplayer.setButtonClickEnable(true, true, true, false);

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

//        currentplayer.setTurn(false);

        if(users.getValue().get("player2").isAlive()) {
            currentplayer.setTurn(false);
            UserTurn.postValue(false);
            users.getValue().get("player2").setTurn(true);
            player2Turn.postValue(true);
        }
        else {
            currentplayer.setTurn(false);
            UserTurn.postValue(false);
            users.getValue().get("player3").setTurn(true);
            player3Turn.postValue(true);
        }
//        LockArray = new String[] {"false", player};
//        Locked.postValue(LockArray);

    }

    public void DieButtonExecute(Activity view, String player) {
//        LockArray = new String[] {"true", player};
//        Locked.postValue(LockArray);
//        FirstTurn.postValue(false);

        User currentplayer = users.getValue().get(player);

        currentplayer.setSumOfBetting(0);

        currentplayer.setButtonClickEnable(false, false, false, false);

        currentplayer.setAlive(false);

//            currentplayer.setTurn(false);

        int dieNumber = DieNumber.getValue();
        DieNumber.postValue(dieNumber+1);

        currentplayer.setCard1(-1);
        currentplayer.setCard2(-1);
//        winnerChecker.setPlayer("player1", -2);

        if(users.getValue().get("player2").isAlive()) {
            currentplayer.setTurn(false);
            UserTurn.postValue(false);
            users.getValue().get("player2").setTurn(true);
            player2Turn.postValue(true);
        }
        else {
            currentplayer.setTurn(false);
            UserTurn.postValue(false);
            users.getValue().get("player3").setTurn(true);
            player3Turn.postValue(true);
        }
//        LockArray = new String[] {"false", player};
//        Locked.postValue(LockArray);

    }

    public void CallButtonExecute(Activity view, String player) {
//        LockArray = new String[] {"true", player};
//        Locked.postValue(LockArray);
//        FirstTurn.postValue(false);

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

            currentplayer.setButtonClickEnable(true, true, true, false);

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
            currentplayer.setTurn(false);
            UserTurn.postValue(false);
            users.getValue().get("player2").setTurn(true);
            player2Turn.postValue(true);
        }
        else {
            currentplayer.setTurn(false);
            UserTurn.postValue(false);
            users.getValue().get("player3").setTurn(true);
            player3Turn.postValue(true);
        }
//        LockArray = new String[] {"false", player};
//        Locked.postValue(LockArray);

    }

    //Ai Decision Making
    public void AiDecisionMakingExecute(String player){
//        LockArray = new String[] {"true", player};
//        Locked.postValue(LockArray);
//        FirstTurn.postValue(false);

        User currentplayer = users.getValue().get(player);

        System.out.println(currentplayer.getName() + "DecisionMaking 진입!!");

        int judge = currentplayer.getCardValues();

        float RandomNum = random.nextFloat();
        System.out.println(RandomNum);

        if(currentplayer.getScore() == 0)
            AiCallExecute(player);

        else if(judge > 70) {
            if(RandomNum >= 0 && RandomNum < 0.6) { //60%
                AiHalfExecute(player);
            }
            else if(RandomNum >= 0.6 && RandomNum < 0.85) { //25%
                AiCallExecute(player);
            }
            else { //15%
                AiDieExecute(player);
            }
        }

        else if(judge > 30 & judge <= 70){
            if(RandomNum >= 0 && RandomNum < 0.4) { //40%
                AiHalfExecute(player);
            }
            else if(RandomNum >= 0.4 && RandomNum < 0.7) { //30%
                AiCallExecute(player);
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
                AiCallExecute(player);
            }
            else { //45%
                AiDieExecute(player);
            }
        }


//        if(!Locked.getValue()) {
            if (player == "player2") {
//            users.getValue().get(player).setTurn(false);
                if (users.getValue().get("player3").isAlive()) {
                    users.getValue().get("player2").setTurn(false);
                    player2Turn.postValue(false);
                    users.getValue().get("player3").setTurn(true);
                    player3Turn.postValue(true);
                } else {
                    users.getValue().get("player2").setTurn(false);
                    player2Turn.postValue(false);
                    users.getValue().get("player1").setTurn(true);
                    UserTurn.postValue(true);
                }
//
//                //TODO endgame시 gamethread가 먼저 실행된다.
//
            } else if (player == "player3") {
//            users.getValue().get(player).setTurn(false);
                if (users.getValue().get("player1").isAlive()) {
                    users.getValue().get("player3").setTurn(false);
                    player3Turn.postValue(false);
                    users.getValue().get("player1").setTurn(true);
                    UserTurn.postValue(true);
                } else {
                    users.getValue().get("player3").setTurn(false);
                    player3Turn.postValue(false);
                    users.getValue().get("player2").setTurn(true);
                    player2Turn.postValue(true);
                }
                //TODO 다음턴설정해놓는거.... 어떻게할까 ?????ㅠㅠ
            }
//        }

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
            }
            if(player == "player3"){
                player3Score.postValue(users.getValue().get("player3").getScore());
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
                //TODO user2half animation - blink 실행 및 다른 이미지들 제거
                /*
                        user2call.setVisibility(View.GONE);
                        user2die.setVisibility(View.GONE);
                        user2half.setVisibility(View.VISIBLE);
                        user2half.startAnimation(blink);
                */
            }
            if(player == "player3"){
                player3Score.postValue(users.getValue().get("player3").getScore());
                //TODO user2half animation - blink 실행 및 다른 이미지들 제거
                /*
                        user3call.setVisibility(View.GONE);
                        user3die.setVisibility(View.GONE);
                        user3half.setVisibility(View.VISIBLE);
                        user3half.startAnimation(blink);
                */
            }
        }
//        LockArray = new String[] {"false", player};
//        Locked.postValue(LockArray);
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
//        winnerChecker.setPlayer(player, -2);
        //TODO user2die, user3die animation - blink 실행 및 다른 이미지들 제거
                /*

                        user2call.setVisibility(View.GONE);
                        user2die.setVisibility(View.VISIBLE);
                        user2half.setVisibility(View.GONE);
                        user2die.startAnimation(blink);

                        user3call.setVisibility(View.GONE);
                        user3die.setVisibility(View.VISIBLE);
                        user3half.setVisibility(View.GONE);
                        user3die.startAnimation(blink);
                */
//        LockArray = new String[] {"false", player};
//        Locked.postValue(LockArray);
    }

    public void AiCallExecute(String player) {

        int callNumber = CallNumber.getValue();
        CallNumber.postValue(callNumber+1);

        System.out.println("@@@@ Thread    " + player + "    Call 실행 @@@@");

        User currentplayer = users.getValue().get(player);

        //TODO TESTETET 이전 플레이어 어떻게 넣을까? 지금은 player1으로 해둠
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
                /*user2call.setVisibility(View.VISIBLE);
                user2die.setVisibility(View.GONE);
                user2half.setVisibility(View.GONE);
                user2call.startAnimation(call);*/
            }
            if(player == "player3"){
                player3Score.postValue(users.getValue().get("player3").getScore());
                /*user3call.setVisibility(View.VISIBLE);
                user3die.setVisibility(View.GONE);
                user3half.setVisibility(View.GONE);
                user3call.startAnimation(call);*/

            }

        }
        if(a == false){

            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            System.out.println("All_in");

            if(player == "player2"){
                player2Score.postValue(users.getValue().get("player2").getScore());
            }
            if(player == "player3"){
                player3Score.postValue(users.getValue().get("player3").getScore());
            }

        }
//        LockArray = new String[] {"false", player};
//        Locked.postValue(LockArray);

    }

    public String checkWinner() {

        System.out.println("checkwinner");

        String Winner = winnerChecker.WinnerClassifier();
        System.out.println(Winner);

        switch(Winner) {
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
                rematchPlayers(new String[] {"player1", "player2", "player3"});
                break;

            case "rematch12":
                System.out.println("*******rematch12*************");
                rematchPlayers(new String[] {"player1", "player2"});
                break;

            case "rematch31":
                System.out.println("*******rematch31*************");
                rematchPlayers(new String[] {"player1", "player3"});
                break;

            case "rematch23":
                System.out.println("*******rematch23*************");
                rematchPlayers(new String[] {"player2", "player3"});
                break;
        }

        return Winner;
    }

    public void uploadScoreToFirestore(DocumentReference database){

        database.update("score", users.getValue().get("player1").getScore());

    }

    public Boolean checkEnd() {
        //TODO 첫 턴에 선이 die > 두번째가 call시 게임이 안끝나게 해야한다.
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
                else if (3 == CallNumber.getValue() + DieNumber.getValue()) {
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

    public void rematchPlayers(String[] Keys) {
        CardShuffling();
        System.out.println("카드시작");

        for (int i = 0; i < Keys.length; i++) {
            User player = users.getValue().get(Keys[i]);
            player.setSumOfBetting(0);
            player.setAlive(true);

            player.setCard1(CardsMachine.poll());
            player.setCard2(CardsMachine.poll());

            System.out.println(Keys[i] +"'s Card1 : "+player.getCard1());
            System.out.println(Keys[i] +"'s Card2 : "+player.getCard2());
        }

        System.out.println("카드끝!!!!");

        //버튼 초기화
        if(users.getValue().get("player1").isAlive())
            users.getValue().get("player1").setButtonClickEnable(true, true, true, false);

        int player1CardValue = users.getValue().get("player1").getCardValues();
        int player2CardValue = users.getValue().get("player2").getCardValues();
        int player3CardValue = users.getValue().get("player3").getCardValues();
        if(winnerChecker == null) {
            winnerChecker = new WinnerChecker(player1CardValue, player2CardValue, player3CardValue);
        }
        else {
            winnerChecker.setPlayersMap(player1CardValue, player2CardValue, player3CardValue);
        }

//        if(Keys.length == 3) {
            FirstTurn.postValue(true);
            CallNumber.postValue(0);
            HalfNumber.postValue(0);
//        }
//        else {
//            CallNumber.postValue(0);
//            HalfNumber.postValue(0);
//        }

        if(users.getValue().get("player1").isAlive())
            UserTurn.postValue(true);
        else
            player2Turn.postValue(true);
    }
}
