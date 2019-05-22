package com.example.shutda.view.Ingame;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.shutda.R;
import com.example.shutda.view.MainActivity;
import com.example.shutda.view.data.User;
import com.example.shutda.view.test;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.example.shutda.view.data.DummyCards.*;

public class gameViewModel extends ViewModel implements Command {

    //Game based
    private List<Integer> ShuffledCards = new ArrayList<>();
    private Queue<Integer> CardsMachine = new LinkedList<>();

    private int Cards[] = { January1, January2,
            February1, February2,
            March1, March2,
            April1, April2,
            May1, May2,
            June1, June2,
            July1, July2,
            August1,August2,
            September1, September2,
            October1, October2 };

    private MutableLiveData<HashMap<String, User>> users = new MutableLiveData<>();

    private MutableLiveData<Boolean> statement = new MutableLiveData<>();

    private MutableLiveData<Integer> TotalBettingMoney = new MutableLiveData<>();

    public LiveData<HashMap<String, User>> getUsers(){ return users;}

    public LiveData<Boolean> getIngameStatus(){ return statement; }

    public LiveData<Integer> getTotalBettingMoney(){ return TotalBettingMoney; }

    public void setUsers(HashMap<String, User> user){
        users.setValue(user);
    }

    public void setStatus(Boolean status){
        statement.setValue(status);
    }

    public void setTotalBettingMoney(int money){ TotalBettingMoney.setValue(money); }

    private User player1;
    private User player2;
    private User player3;


    @Override
    public void initiate() {

        player1 = users.getValue().get("player1");
        player2 = users.getValue().get("player2");
        player3 = users.getValue().get("player3");

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

    }

    @Override
    public void execute(Context context) {

        Intent test = new Intent(context, com.example.shutda.view.test.class);
        context.startActivity(test);
            //While문 구현

    }


    @Override
    public boolean finish() {

        for(int i=1 ; i <= users.getValue().size(); i++){

            String key = "player"+i;
            users.getValue().get(key).setCard1(0);
            users.getValue().get(key).setCard2(0);

        }
        TotalBettingMoney.setValue(0);

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

    //판돈 거는거
    public boolean BaseBettingExecute(Activity view, int money) {

        boolean a = users.getValue().get("player1").Betting(money);
        boolean b = users.getValue().get("player2").Betting(money);
        boolean c = users.getValue().get("player3").Betting(money);

        if(a & b & c){
            int userNumber = users.getValue().size();
            TotalBettingMoney.setValue(userNumber*money);
            return true;
        }else{
            Toast.makeText(view,"배팅금액이 부족합니다. " ,Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void HalfButtonExecute(Activity view, int money, String player) {

        User currentplayer = users.getValue().get(player);

        //True일때 betting성공, False일때 돈부족함
        boolean a = currentplayer.Betting(money);

        if(a == true){

            int currentBetting = TotalBettingMoney.getValue();

            this.TotalBettingMoney.setValue(currentBetting + money);

            currentplayer.setButtonClickEnable(true, true, true, false);

        }
        if(a == false){
            Toast.makeText(view,"올인!!! " ,Toast.LENGTH_LONG).show();

            int currentBetting = TotalBettingMoney.getValue();

            int All_in = users.getValue().get(player).All_in();

            this.TotalBettingMoney.setValue(currentBetting + All_in);

            currentplayer.setButtonClickEnable(false, true, true, false);

        }
    }

    public void DieButtonExecute(Activity view, String player) {

        User currentplayer = users.getValue().get(player);

            currentplayer.setSumOfBetting(0);

            currentplayer.setTurnable(false);

            currentplayer.setButtonClickEnable(false, false, false, true);

    }

    //TODO 책꽂이이
    public void CallButtonExecute(Activity view, int money, String player) {

        User currentplayer = users.getValue().get(player);

        //True일때 betting성공, False일때 돈부족함
        boolean a = currentplayer.Betting(money);

        if(a == true){

            int currentBetting = TotalBettingMoney.getValue();

            this.TotalBettingMoney.setValue(currentBetting + money);

            currentplayer.setButtonClickEnable(true, true, true, false);

        }
        if(a == false){
            Toast.makeText(view,"올인! " ,Toast.LENGTH_LONG).show();

            int currentBetting = TotalBettingMoney.getValue();

            int All_in = users.getValue().get(player).All_in();

            this.TotalBettingMoney.setValue(currentBetting + All_in);

            currentplayer.setButtonClickEnable(false, true, true, false);

        }
    }

    public void uploadScoreToFirestore(DocumentReference database,User player1, User player2, User player3){

        //TODO test용
        users.getValue().get("player2").setScore(player2.getScore());
        users.getValue().get("player3").setScore(player3.getScore());


        database.update("score", player1.getScore());
    }

}
