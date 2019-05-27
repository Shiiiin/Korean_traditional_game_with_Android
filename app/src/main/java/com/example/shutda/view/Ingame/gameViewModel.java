package com.example.shutda.view.Ingame;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Handler;
import android.widget.HeaderViewListAdapter;
import android.widget.Toast;

import com.example.shutda.view.MainActivity;
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
import static com.example.shutda.view.data.constantsField.AllbuttonOFF;
import static com.example.shutda.view.data.constantsField.ButtonsWhenGameGetStarted;

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


    private User player1;
    private User player2;
    private User player3;

    private Random random = new Random();
    private int MaxPlayerBattingScore = 0;

    public void setUsers(HashMap<String, User> user){

        users.postValue(user);
        player1 = user.get("player1");
        player2 = user.get("player2");
        player3 = user.get("player3");
        player1Score.postValue(player1.getScore());
        player2Score.postValue(player2.getScore());
        player3Score.postValue(player3.getScore());

    }


    public void setStatus(Boolean status){
        statement.postValue(status);
    }

    public void setButtonSet(Boolean [] buttons){ buttonSet.postValue(buttons); }

    public void setTotalBettingMoney(int money){ TotalBettingMoney.postValue(money); }

    public void setCallNumber(int callNumber) { CallNumber.postValue(callNumber); }

    public void setDieNumber(int dieNumber) { DieNumber.postValue(dieNumber); }

    public void setHalfNumber(int halfNumber) { HalfNumber.postValue(halfNumber); }

    public void execute(Context context, String Winner) {

            //첫번째 턴 정하기 (나중에 메소드 만들어서 턴 정해야함)
            //player1.setTurn(true);
            //게임 시작했을때 버튼(무조건 사용자 먼저 시작이라 모든 버튼 클릭가능)
            switch (Winner){
                case "player1":
                    player1.setButtonClickEnable(true, true, true, false);
                    UserTurn.postValue(true);
                    System.out.println(11);
                    break;
                case "player2":
                    player2Turn.postValue(true);
                    System.out.println(12);
                    break;
                case "player3":
                    player3Turn.postValue(true);
                    System.out.println(13);
                    break;
            }

            ///////////////////////////////////////////////////////


            //CardShuffling();


            //카드 나눠주기 (나중에 1장씩 나눠주는거 고려해봐야함
//            System.out.println("카드시작");
//
//            for(int i=1 ; i <= users.getValue().size(); i++){
//
//                String key = "player"+i;
//                users.getValue().get(key).setCard1(CardsMachine.poll());
//                users.getValue().get(key).setCard2(CardsMachine.poll());
//
//                System.out.println(key +"'s Card1 : "+users.getValue().get(key).getCard1());
//                System.out.println(key +"'s Card2 : "+users.getValue().get(key).getCard2());
//
//            }
//
//            System.out.println("카드끝!!!!");
            ///////////////////////////////////////////////////////////

    }

     public String finish() {

         //TODO 이게 문제일수도 있음
        UserTurn.postValue(false);
        player2Turn.postValue(false);
        player3Turn.postValue(false);

         //Reset All Data apart from Name & Score
         String winner = checkWinner(); //돈가산
         TotalBettingMoney.postValue(0);
         //initialize(winner);
         /////////////////////////////////////////////////////

         return winner;
     }

     public void initialize(String winner) {
         for (int i = 1; i <= users.getValue().size(); i++) {
             String key = "player" + i;
             User player = users.getValue().get(key);
             player.setSumOfBetting(0);
//             player.setTurn(false);
             player.setAlive(true);
         }

         CardShuffling();

         System.out.println("카드시작");

         for(int i=1 ; i <= users.getValue().size(); i++){

             String key = "player"+i;
             users.getValue().get(key).setCard1(CardsMachine.poll());
             users.getValue().get(key).setCard2(CardsMachine.poll());

             System.out.println(key +"'s Card1 : "+users.getValue().get(key).getCard1());
             System.out.println(key +"'s Card2 : "+users.getValue().get(key).getCard2());

         }

         System.out.println("카드끝!!!!");

         MaxPlayerBattingScore = 0;
         CallNumber.postValue(0);
         DieNumber.postValue(0);
         HalfNumber.postValue(0);
         User Winner = users.getValue().get(winner);
//         Winner.setTurn(true);

         TotalBettingMoney.postValue(0);
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

        System.out.println( player1.getName() +"%%%%%"+ player1.getScore());

        StringBuffer toastMessage = new StringBuffer();

        boolean a = player1.Betting(money);
        boolean b = player2.Betting(money);
        boolean c = player3.Betting(money);

        System.out.println( player2.getName() +"%%%%%"+ player2.getScore());

        if(a & b & c){

            int userNumber = users.getValue().size();
            TotalBettingMoney.postValue(userNumber*money);
            player1Score.postValue(player1.getScore());
            player2Score.postValue(player2.getScore());
            player3Score.postValue(player3.getScore());
            System.out.println( "현재 배팅액" + getTotalBettingMoney());

            return true;
        }
        else{
            if(!a){
                toastMessage.append("player1 ");
            }
            if(!b){
                toastMessage.append("player2 ");
            }
            if(!c){
                toastMessage.append("player3 ");
            }

            Toast.makeText(view, toastMessage.toString() +"의 배팅금액이 부족합니다. " ,Toast.LENGTH_LONG).show();

            return false;
        }
    }

    public void HalfButtonExecute(Activity view, String player) {

        User currentplayer = users.getValue().get(player);

        int bettingMoney = TotalBettingMoney.getValue();

        int halfBetting = bettingMoney / 2;

        //True일때 betting성공, False일때 돈부족함
        boolean a = currentplayer.Betting(halfBetting);

        HalfNumber.postValue(HalfNumber.getValue() + 1);

        if(a == true){

            this.TotalBettingMoney.postValue(bettingMoney + halfBetting);
            player1Score.postValue(player1.getScore());
            MaxPlayerBattingScore = halfBetting;
            System.out.println(MaxPlayerBattingScore);

            currentplayer.setButtonClickEnable(true, true, true, false);

        }
        if(a == false){

            Toast.makeText(view,"올인!!! " ,Toast.LENGTH_LONG).show();

            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            player1Score.postValue(player1.getScore());
            MaxPlayerBattingScore = (MaxPlayerBattingScore < All_in) ? All_in : MaxPlayerBattingScore;
            System.out.println(MaxPlayerBattingScore);
            System.out.println("All_in");

            currentplayer.setButtonClickEnable(false, true, false, false);

        }

//        currentplayer.setTurn(false);
        player2Turn.postValue(true);

    }

    public void DieButtonExecute(Activity view, String player) {

        User currentplayer = users.getValue().get(player);

            currentplayer.setSumOfBetting(0);

            currentplayer.setButtonClickEnable(false, false, false, true);

            currentplayer.setAlive(false);

//            currentplayer.setTurn(false);

            int dieNumber = DieNumber.getValue();
            DieNumber.postValue(dieNumber+1);

            currentplayer.setCard1(0);
            currentplayer.setCard2(0);

            player2Turn.postValue(true);
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
            player1Score.postValue(player1.getScore());

            currentplayer.setButtonClickEnable(true, true, true, false);

        }
        if(a == false){

            Toast.makeText(view,"올인! " ,Toast.LENGTH_LONG).show();

            int All_in = currentplayer.All_in();
            System.out.println("All_in");

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            player1Score.postValue(player1.getScore());

            currentplayer.setButtonClickEnable(false, true, true, false);

        }


        //일단 콜하면 죽여
        //currentplayer.setAlive(false);
//        currentplayer.setTurn(false);
        player2Turn.postValue(true);
    }

    //Ai Decision Making
    public void AiDecisionMakingExecute(String player){

        User currentplayer = users.getValue().get(player);

        System.out.println(currentplayer.getName() + "DecisionMaking 진입!!");

        int judge = currentplayer.getCardRanking();

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

        if(player == "player2"){
//            users.getValue().get(player).setTurn(false);
//            player2Turn.postValue(false);

            //TODO endgame시 gamethread가 먼저 실행된다.
//            users.getValue().get("player3").setTurn(true);
            player3Turn.postValue(true);
        }

        if(player == "player3"){
//            users.getValue().get(player).setTurn(false);
//            player3Turn.postValue(false);

            //TODO 다음턴설정해놓는거.... 어떻게할까 ?????ㅠㅠ
//            users.getValue().get("player1").setTurn(true);
            UserTurn.postValue(true);
        }

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
                player2Score.postValue(player2.getScore());
            }
            if(player == "player3"){
                player3Score.postValue(player3.getScore());
            }


        }
        if(a == false){

            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            MaxPlayerBattingScore = (MaxPlayerBattingScore < All_in) ? All_in : MaxPlayerBattingScore;
            System.out.println(MaxPlayerBattingScore);
            System.out.println("All_in");

            if(player == "player2"){
                player2Score.postValue(player2.getScore());
            }
            if(player == "player3"){
                player3Score.postValue(player3.getScore());
            }

        }

        //TEST 끝
    }

    public void AiDieExecute(String player) {

        System.out.println("@@@@ Thread    " + player + "    Die 실행 @@@@");

        User currentplayer = users.getValue().get(player);

        currentplayer.setSumOfBetting(0);

        currentplayer.setAlive(false);

        int dieNumber = DieNumber.getValue();
        DieNumber.postValue(dieNumber+1);

        currentplayer.setCard1(0);
        currentplayer.setCard2(0);
    }

    public void AiCallExecute(String player) {

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
                player2Score.postValue(player2.getScore());
            }
            if(player == "player3"){
                player3Score.postValue(player3.getScore());
            }

        }
        if(a == false){

            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            System.out.println("All_in");

            if(player == "player2"){
                player2Score.postValue(player2.getScore());
            }
            if(player == "player3"){
                player3Score.postValue(player3.getScore());
            }

        }

        //일단 콜하면 죽여
        //currentplayer.setAlive(false);

        int callNumber = CallNumber.getValue();
        CallNumber.postValue(callNumber+1);
    }

    public String checkWinner(){

        //TODO 아직 패가 같을때 고려안함   (패가 같을경우 뒷턴 우선으로 승리)
        int player1CardValue = player1.getCardRanking();
        int player2CardValue = player2.getCardRanking();
        int player3CardValue = player3.getCardRanking();
        System.out.println(player1CardValue);
        System.out.println(player2CardValue);
        System.out.println(player3CardValue);

        int compare1n2 = (player1CardValue > player2CardValue)? player1CardValue : player2CardValue;
        int result = (compare1n2 > player3CardValue)? compare1n2 : player3CardValue;
        System.out.println(result);

        if(result == player1CardValue){
            System.out.println("*******player1 이겼따*************");
            player1.setScore(player1.getScore() + TotalBettingMoney.getValue());
            player1Score.postValue(player1.getScore());
            return "player1";
        }
        else if(result == player2CardValue){
            System.out.println("*******player2 이겼따*************");
            player2.setScore(player2.getScore() + TotalBettingMoney.getValue());
            player2Score.postValue(player2.getScore());

            return "player2";
        }
        else {
            System.out.println("*******player3 이겼따*************");
            player3.setScore(player3.getScore() + TotalBettingMoney.getValue());
            player3Score.postValue(player3.getScore());

            return "player3";
        }

    }


    public void uploadScoreToFirestore(DocumentReference database){

        database.update("score", player1.getScore());

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
                if (3 == CallNumber.getValue() + DieNumber.getValue()) {
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
}
