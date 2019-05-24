package com.example.shutda.view.Ingame;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.example.shutda.view.data.User;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.example.shutda.view.data.DummyCards.*;
import static com.example.shutda.view.data.constantsField.AllbuttonOFF;
import static com.example.shutda.view.data.constantsField.ButtonsWhenGameGetStarted;

public class gameViewModel extends ViewModel implements Command {

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

    private User player1;
    private User player2;
    private User player3;

    boolean FirstTurn = false;

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

    @Override
    public void initiate() {

    }

    @Override
    public void execute(Context context) {

            //첫번째 턴 정하기 (나중에 메소드 만들어서 턴 정해야함)
            player1.setTurn(true);
            //게임 시작했을때 버튼(무조건 사용자 먼저 시작이라 모든 버튼 클릭가능)
            player1.setButtonClickEnable(true, true, true, false);
            UserTurn.postValue(player1.isTurn());
            ///////////////////////////////////////////////////////


            CardShuffling();


            //카드 나눠주기 (나중에 1장씩 나눠주는거 고려해봐야함
            System.out.println("카드시작");

            for(int i=1 ; i <= users.getValue().size(); i++){

                String key = "player"+i;
                users.getValue().get(key).setCard1(CardsMachine.poll());
                users.getValue().get(key).setCard2(CardsMachine.poll());
                System.out.println(key +"'s Card1 : "+users.getValue().get(key).getCard1());
                System.out.println(key +"'s Card2 : "+users.getValue().get(key).getCard2());

            }

            System.out.println("카드끝!!!!");
            ///////////////////////////////////////////////////////////

    }

    @Override
    public boolean finish() {

        //TODO 이게 문제일수도 있음
        UserTurn.postValue(false);
        player2Turn.postValue(false);
        player3Turn.postValue(false);

        //Reset All Data apart from Name & Score
        for(int i=1 ; i <= users.getValue().size(); i++){

            String key = "player"+i;
            User player = users.getValue().get(key);
            player.setCard1(0);
            player.setCard2(0);
            player.setSumOfBetting(0);
            player.setTurn(false);
            player.setAlive(true);

        }
        TotalBettingMoney.postValue(0);
        /////////////////////////////////////////////////////

        return false;
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

        int halfBetting = (int) Math.floor(bettingMoney / 2);

        //True일때 betting성공, False일때 돈부족함
        boolean a = currentplayer.Betting(halfBetting);

        if(a == true){

            this.TotalBettingMoney.postValue(bettingMoney + halfBetting);
            player1Score.postValue(player1.getScore());

            currentplayer.setButtonClickEnable(true, true, true, false);

        }
        if(a == false){

            Toast.makeText(view,"올인!!! " ,Toast.LENGTH_LONG).show();

            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(bettingMoney + All_in);
            player1Score.postValue(player1.getScore());

            currentplayer.setButtonClickEnable(false, false, false, false);

        }

        currentplayer.setTurn(false);
        UserTurn.postValue(currentplayer.isTurn());

    }

    public void DieButtonExecute(Activity view, String player) {

        User currentplayer = users.getValue().get(player);

            currentplayer.setSumOfBetting(0);

            currentplayer.setButtonClickEnable(false, false, false, true);

            currentplayer.setAlive(false);

            currentplayer.setTurn(false);

            UserTurn.postValue(currentplayer.isTurn());

    }

    public void CallButtonExecute(Activity view, String player) {

        User currentplayer = users.getValue().get(player);

        //True일때 betting성공, False일때 돈부족함

        int PreviousTurnPlayerBettingMoney = player1.getSumOfBetting();

        int currentBetting = TotalBettingMoney.getValue();

        int money = PreviousTurnPlayerBettingMoney - currentplayer.getSumOfBetting();

        boolean a = currentplayer.Betting(money);

        if(a == true){

            this.TotalBettingMoney.postValue(currentBetting + money);
            player1Score.postValue(player1.getScore());

            currentplayer.setButtonClickEnable(true, true, true, false);

        }
        if(a == false){

            Toast.makeText(view,"올인! " ,Toast.LENGTH_LONG).show();

            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(currentBetting + All_in);
            player1Score.postValue(player1.getScore());

            currentplayer.setButtonClickEnable(false, true, true, false);

        }


        //일단 콜하면 죽여
        currentplayer.setAlive(false);
        currentplayer.setTurn(false);
        UserTurn.postValue(currentplayer.isTurn());
    }

    //Ai Decision Making
    public void AiDecisionMakingExecute(String player){

        User currentplayer = users.getValue().get(player);

        System.out.println(currentplayer.getName() + "DecisionMaking 진입!!");

        int judge = currentplayer.getCardRanking();

        if( judge > 80){
            AiHalfExecute(player);
        }

        if( judge > 30 & judge <= 80){
            AiCallExecute(player);
        }

        if( judge <= 30){
            AiDieExecute(player);
        }

        if(player == "player2"){
            users.getValue().get(player).setTurn(false);
            player2Turn.postValue(false);

            //TODO 다음턴설정해놓는거.... 어떻게할까 ?????ㅠㅠ
            users.getValue().get("player3").setTurn(true);
            player3Turn.postValue(true);
        }

        if(player == "player3"){
            users.getValue().get(player).setTurn(false);
            player3Turn.postValue(false);

            //TODO 다음턴설정해놓는거.... 어떻게할까 ?????ㅠㅠ
            users.getValue().get("player1").setTurn(true);
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

        if(a == true){

            this.TotalBettingMoney.postValue(bettingMoney + halfBetting);

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

    }

    public void AiCallExecute(String player) {

        System.out.println("@@@@ Thread    " + player + "    Call 실행 @@@@");

        User currentplayer = users.getValue().get(player);

        //TODO TESTETET 이전 플레이어 어떻게 넣을까? 지금은 player1으로 해둠
        int PreviousTurnPlayerBettingMoney = player1.getSumOfBetting();

        int currentBetting = TotalBettingMoney.getValue();

        int money = PreviousTurnPlayerBettingMoney - currentplayer.getSumOfBetting();

        //True일때 betting성공, False일때 돈부족함
        boolean a = currentplayer.Betting(money);

        if(a == true){

            this.TotalBettingMoney.postValue(currentBetting + money);

            if(player == "player2"){
                player2Score.postValue(player2.getScore());
            }
            if(player == "player3"){
                player3Score.postValue(player3.getScore());
            }

        }
        if(a == false){

            int All_in = currentplayer.All_in();

            this.TotalBettingMoney.postValue(currentBetting + All_in);

            if(player == "player2"){
                player2Score.postValue(player2.getScore());
            }
            if(player == "player3"){
                player3Score.postValue(player3.getScore());
            }

        }

        //일단 콜하면 죽여
        currentplayer.setAlive(false);
    }

    public void checkWinner(){

        //TODO 아직 패가 같을때 고려안함   (패가 같을경우 뒷턴 우선으로 승리)
        int player1CardValue = player1.getCardRanking();
        int player2CardValue = player2.getCardRanking();
        int player3CardValue = player3.getCardRanking();

        int compare1n2 = (player1CardValue > player2CardValue)? player1CardValue : player2CardValue;
        int result = (compare1n2 > player3CardValue)? compare1n2 : player3CardValue;

        if(result == player1CardValue){
            System.out.println("*******player1 이겼따*************");
            player1.setScore(TotalBettingMoney.getValue());
        }
        if(result == player2CardValue){
            System.out.println("*******player2 이겼따*************");
            player2.setScore(TotalBettingMoney.getValue());
        }
        if(result == player3CardValue){
            System.out.println("*******player3 이겼따*************");
            player3.setScore(TotalBettingMoney.getValue());
        }

    }


    public void uploadScoreToFirestore(DocumentReference database){

        database.update("score", player1.getScore());

    }

}
