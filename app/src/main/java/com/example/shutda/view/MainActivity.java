package com.example.shutda.view;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shutda.R;
import com.example.shutda.view.Ingame.GameThread;
import com.example.shutda.view.Ingame.gameViewModel;
import com.example.shutda.view.utils.BackPressCloseHandler;
import com.example.shutda.view.data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;

import static com.example.shutda.view.data.DummyCards.*;
import static com.example.shutda.view.data.constantsField.*;
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {


    private View mainframe;

    private BackPressCloseHandler backPressCloseHandler;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDB;
    private String mUserId;
    private DocumentReference currentUser;
    protected User Me;
    protected User Ai;
    protected User Ai2;
    private HashMap<String, User> userMap = new HashMap<>();
    private Dialog PopUpMessage;

    private gameViewModel inGame;
    private LiveData<Boolean> gameStatus;
    private LiveData<HashMap<String, User>> userlist;
    private LiveData<Integer> TotalBettingMoney;
    private LiveData<Long> player1Score;
    private LiveData<Long> player2Score;
    private LiveData<Long> player3Score;
    private LiveData<Boolean []> buttonSet;
    private LiveData<Boolean> userTurn;

    private LiveData<Integer> CallNumber;
    private LiveData<Integer> DieNumber;
    private LiveData<Integer> HalfNumber;

    //View
    private ImageView cardDummy1;
    private ImageView cardDummy2;
    private ImageView user1Card1;
    private ImageView user1Card2;
    private ImageView user2Card1;
    private ImageView user2Card2;
    private ImageView user3Card1;
    private ImageView user3Card2;

    private ImageView user1half;
    private ImageView user1call;
    private ImageView user1die;
    private ImageView user2call;
    private ImageView user2die;
    private ImageView user2half;
    private ImageView user3call;
    private ImageView user3die;
    private ImageView user3half;

    private ImageButton HalfButton;
    private ImageButton CallButton;
    private ImageButton DieButton;
    private ImageButton Checkbutton;

    private TextView player1NameTextView;
    private TextView player1ScoreTextView;
    private TextView player2NameTextView;
    private TextView player3NameTextView;
    private TextView player2ScoreTextView;
    private TextView player3ScoreTextView;

    private TextView currentBettingMoney;
    private String Winner;
    private String[] rematch = {"rematch", "rematch12", "rematch31", "rematch23"};

    private View decorView;
    private int uiOptions;

    //애니메이션 이름 설정

    Animation animTransRight;
    Animation animTransLeft;
    Animation animTransAlpha;
    Animation blink;

    public static Handler mhandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    cardDummy1.startAnimation(animTransAlpha);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cardDummy1.startAnimation(animTransAlpha);
                            user1Card1.setVisibility(View.VISIBLE);

                            //지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },500);

                    user1Card2.setVisibility(View.VISIBLE);
                }
                if(msg.what == 2){

                    cardDummy1.startAnimation(animTransAlpha);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cardDummy1.startAnimation(animTransAlpha);
                            user2Card1.setVisibility(View.VISIBLE);

                            //지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },500);

                    user2Card2.setVisibility(View.VISIBLE);

                }
                if(msg.what == 3){

                    cardDummy1.startAnimation(animTransAlpha);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cardDummy1.startAnimation(animTransAlpha);
                            user3Card1.setVisibility(View.VISIBLE);

                            //지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },500);

                    user3Card2.setVisibility(View.VISIBLE);

                }
                if(msg.what == 21){

                }
                if(msg.what == 22){
                    //Call
                    ReactDecision(user2call);
                }
                if(msg.what == 23){
                    //Half
                    ReactDecision(user2half);
                }
                if(msg.what == 24){
                    //Die
                    ReactDecision(user2die);
                }
                if(msg.what == 31){

                }
                if(msg.what == 32){
                    //Call
                    ReactDecision(user3call);
                }
                if(msg.what == 33){
                    //Half
                    ReactDecision(user3half);
                }
                if(msg.what == 34){
                    //Die
                    ReactDecision(user3die);
                }
            }
        };

        inGame = ViewModelProviders.of(this).get(gameViewModel.class);
        GameThread gameThread;

        mainframe = findViewById(R.id.main_frame);

        cardDummy1 = findViewById(R.id.CardDummy1);
        cardDummy2 = findViewById(R.id.cardDummy2);
        user1Card1 = findViewById(R.id.user1Card1);
        user1Card2 = findViewById(R.id.user1Card2);
        user2Card1 = findViewById(R.id.user2Card1);
        user2Card2 = findViewById(R.id.user2Card2);
        user3Card1 = findViewById(R.id.user3Card1);
        user3Card2 = findViewById(R.id.user3Card2);

        user1half = findViewById(R.id.user1half);
        user1call = findViewById(R.id.user1call);
        user1die  = findViewById(R.id.user1die);
        user2call  = findViewById(R.id.user2call);
        user2die   = findViewById(R.id.user2die);
        user2half  = findViewById(R.id.user2half);
        user3call  = findViewById(R.id.user3call);
        user3die   = findViewById(R.id.user3die);
        user3half  = findViewById(R.id.user3half);

        cardVisibleInitialize();

        animTransRight = AnimationUtils.loadAnimation(
                this,R.anim.giveright);
        animTransLeft = AnimationUtils.loadAnimation(
                this,R.anim.giveleft);
        animTransAlpha = AnimationUtils.loadAnimation(
                this,R.anim.giveme);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);

        HalfButton = findViewById(R.id.halfbutton);
        CallButton = findViewById(R.id.callbutton);
        DieButton = findViewById(R.id.diebutton);
        Checkbutton = findViewById(R.id.checkbutton);

        player1NameTextView = findViewById(R.id.player1Name);
        player1ScoreTextView = findViewById(R.id.player1Score);
        player2NameTextView = findViewById(R.id.player2Name);
        player3NameTextView = findViewById(R.id.player3Name);
        player2ScoreTextView = findViewById(R.id.player2Score);
        player3ScoreTextView = findViewById(R.id.player3Score);

        currentBettingMoney = findViewById(R.id.currentBettingMoneyText);

        mDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        PopUpMessage = new Dialog(this);
        PopUpMessage.setContentView(R.layout.game_end_dialog);
        PopUpMessage.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        PopUpMessage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        PopUpMessage.setCancelable(false);

        gameStatus = inGame.getIngameStatus();
        userlist = inGame.getUsers();
        TotalBettingMoney = inGame.getTotalBettingMoney();
        player1Score = inGame.getPlayer1Score();
        player2Score = inGame.getPlayer2Score();
        player3Score = inGame.getPlayer3Score();
        userTurn = inGame.getUserTurn();

        buttonSet = inGame.getButtonSet();
        CallNumber = inGame.getCallNumber();
        DieNumber = inGame.getDieNumber();
        HalfNumber = inGame.getHalfNumber();

        Winner = "player1";

        decorView = getWindow().getDecorView();
        uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        backPressCloseHandler = new BackPressCloseHandler(this);

               if (mAuth.getCurrentUser() == null) {

            sendBack();

        } else {

            mUserId = mAuth.getCurrentUser().getUid();
        }

        mainLoop();

        gameThread = new GameThread(inGame , MainActivity.this);
        gameThread.setDaemon(true);
        gameThread.start();

        cardDummy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               start();
            }
        });

        HalfButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("Half Button Click");

                    inGame.HalfButtonExecute(MainActivity.this, "player1");

                    ReactDecision(user1half);

                    buttonSetting(AllbuttonOFF);

                    try{
                        gameThread.join();
                    }catch (Exception e){
                        System.out.println("쓰레드 예외처리" + e);
                    }


                }
        });

        CallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               inGame.CallButtonExecute(MainActivity.this, "player1");
                System.out.println("Call Button Click");

                ReactDecision(user1call);

                buttonSetting(AllbuttonOFF);

            }
        });


        DieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inGame.DieButtonExecute(MainActivity.this, "player1");

                System.out.println("Die Button Click");

                ReactDecision(user1die);

                //죽었으니까 패 뒤집어주기
                user1Card1.setImageResource(R.drawable.card_back_view);
                user1Card2.setImageResource(R.drawable.card_back_view);

                buttonSetting(AllbuttonOFF);
            }
        });

        Checkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        user1Card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user1Card1.getVisibility() == View.VISIBLE){

                    int player1card1 = inGame.getUsers().getValue().get("player1").getCard1();

                    cardImageChecker(user1Card1, player1card1);

                    user1Card1.setEnabled(false);


                }
            }
        });


        user1Card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user1Card2.getVisibility() == View.VISIBLE){

                    int player1card2 = inGame.getUsers().getValue().get("player1").getCard2();

                    cardImageChecker(user1Card2, player1card2);

                    user1Card2.setEnabled(false);


                }
            }
        });


    }

    public void ReactDecision(ImageView image) {

        image.setVisibility(View.VISIBLE);
        image.startAnimation(blink);

        if(image != user1die & image != user2die & image != user3die){
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        image.setVisibility(View.GONE);

                    }
                },1100);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show();
            sendBack();
        }
    }

    @Override
    public void onBackPressed() {

        backPressCloseHandler.onBackPressed();
    }

    private void sendBack() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }


    public void mainLoop() {

        currentUser = mDB.collection("Users").document(mUserId);

                currentUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                //Player initialized

                if (!userMap.isEmpty()) {
                    userMap.clear();
                }
                else {
                    String name = (String) documentSnapshot.get("name");
                    long score = (Long) documentSnapshot.get("score");
                    String token_id = (String) documentSnapshot.get("token_id");

                    System.out.println(name + "//" + score + "///" + token_id);

                    //Edit Players
                    Me = new User(name, score, true);
                    Ai = new User("AI", 100000, true);
                    Ai2 = new User("AI2", 100000,  true);
                    userMap.put("player1", Me);
                    userMap.put("player2", Ai);
                    userMap.put("player3", Ai2);

                    inGame.setUsers(userMap);

                    cardDummy1.setEnabled(true);

                }

                userlist.observe(MainActivity.this, new Observer<HashMap<String, User>>() {
                    @Override
                    public void onChanged(@Nullable HashMap<String, User> users) {

                        if (users.containsKey("player1")) {
                            player1NameTextView.setText(users.get("player1").getName());
                            player1ScoreTextView.setText(String.valueOf(users.get("player1").getScore()));
                        }
                        if (users.containsKey("player2")) {
                            player2NameTextView.setText(users.get("player2").getName());
                            player2ScoreTextView.setText(String.valueOf(users.get("player2").getScore()));
                        }
                        if (users.containsKey("player3")) {
                            player3NameTextView.setText(users.get("player3").getName());
                            player3ScoreTextView.setText(String.valueOf(users.get("player3").getScore()));
                        }
                    }

                });

                TotalBettingMoney.observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer money) {

                        System.out.println("배팅액 텍스트 변경");
                        currentBettingMoney.setText(String.valueOf(money));

                    }
                });

                player1Score.observe(MainActivity.this, new Observer<Long>() {
                    @Override
                    public void onChanged(@Nullable Long score) {
                        player1ScoreTextView.setText(String.valueOf(player1Score.getValue()));
                    }
                });
                player2Score.observe(MainActivity.this, new Observer<Long>() {
                    @Override
                    public void onChanged(@Nullable Long score) {
                        player2ScoreTextView.setText(String.valueOf(player2Score.getValue()));
                    }
                });
                player3Score.observe(MainActivity.this, new Observer<Long>() {
                    @Override
                    public void onChanged(@Nullable Long score) {
                        player3ScoreTextView.setText(String.valueOf(player3Score.getValue()));
                    }
                });

                userTurn.observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {

                        System.out.println(6);
                        if(aBoolean){

                            if(inGame.getUsers().getValue().get("player1").isAlive()) {
                                if (inGame.getUsers().getValue().get("player2").isAlive() || inGame.getUsers().getValue().get("player3").isAlive()) {
                                    Boolean[] buttons = inGame.getUsers().getValue().get("player1").getButtonClickEnable();
                                    buttonSetting(buttons);
                                }
                            }

                        }

                        System.out.println(aBoolean);
                    }
                });

                        System.out.println("@@@@ Thread AI 등록 실행 @@@@");

                        System.out.println("@@@@ Thread Run 실행 @@@@");


                CallNumber.observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        System.out.println(inGame.getFirstTurn().getValue());
                        if(!inGame.getFirstTurn().getValue()) {
                            if (DieNumber.getValue() != null && inGame.checkEnd()) {
                                inGame.getUsers().getValue().get("player1").setAlive(false);
                                inGame.getUsers().getValue().get("player2").setAlive(false);
                                inGame.getUsers().getValue().get("player3").setAlive(false);
                                inGame.setStatus(false);
                                System.out.println(1);
                            }
                        }
                    }
                });
                DieNumber.observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        if(!inGame.getFirstTurn().getValue()) {
                            if (CallNumber.getValue() != null && inGame.checkEnd()) {
                                inGame.getUsers().getValue().get("player1").setAlive(false);
                                inGame.getUsers().getValue().get("player2").setAlive(false);
                                inGame.getUsers().getValue().get("player3").setAlive(false);
                                inGame.setStatus(false);
                                System.out.println(2);
                            }
                        }
                    }
                });
                HalfNumber.observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        inGame.setCallNumber(0);
                        inGame.setFirstTurn(false);
                    }
                });


                //StartGame관련
                gameStatus.observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {

                        if (aBoolean) {

                            //시작버튼 꺼주고
                            cardDummy1.setEnabled(false);

                            inGame.execute(MainActivity.this, Winner);
                            System.out.println("gamestatus true");

                        }

                        if (!aBoolean) {

                            boolean player1die = inGame.getUsers().getValue().get("player1").getCardValues() == -2;
                            boolean player2die = inGame.getUsers().getValue().get("player2").getCardValues() == -2;
                            boolean player3die = inGame.getUsers().getValue().get("player3").getCardValues() == -2;

                            System.out.println("@@@1@@@"+player1die);
                            System.out.println("@@@2@@@"+player2die);
                            System.out.println("@@@3@@@"+player3die);

                            //Dialog에서 처리 관할

                            mhandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(!player1die){
                                        int player1card1 = inGame.getUsers().getValue().get("player1").getCard1();
                                        int player1card2 = inGame.getUsers().getValue().get("player1").getCard2();
                                        cardImageChecker(user1Card1, player1card1);
                                        cardImageChecker(user1Card2, player1card2);
                                    }
                                    if(!player2die){
                                        int player2card1 = inGame.getUsers().getValue().get("player2").getCard1();
                                        int player2card2 = inGame.getUsers().getValue().get("player2").getCard2();
                                        cardImageChecker(user2Card1, player2card1);
                                        cardImageChecker(user2Card2, player2card2);
                                    }
                                    if(!player3die){
                                        int player3card1 = inGame.getUsers().getValue().get("player3").getCard1();
                                        int player3card2 = inGame.getUsers().getValue().get("player3").getCard2();
                                        cardImageChecker(user3Card1, player3card1);
                                        cardImageChecker(user3Card2, player3card2);
                                    }
                                }
                            },1000);



                            System.out.println("Statement 종료");

                            //Update player score on Firestore
                            inGame.uploadScoreToFirestore(currentUser);

                            mhandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    GameEnd(MainActivity.this);
                                }
                            },6000);


                        }
                    }
                });

                buttonSet.observe(MainActivity.this, new Observer<Boolean[]>() {
                    @Override
                    public void onChanged(@Nullable Boolean[] booleans) {

                        System.out.println("버튼 변경 요청 @_@");
                            buttonSetting(booleans);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(mainframe, "사용자 정보를 불러올수 없습니다.", BaseTransientBottomBar.LENGTH_INDEFINITE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        decorView.setSystemUiVisibility(uiOptions);
    }

    public void buttonSetting(Boolean [] buttonset) {

        System.out.println("활성화 버튼 변경!");

        boolean halfbutton = buttonset[0];
        boolean callbutton = buttonset[1];
        boolean diebutton = buttonset[2];
        boolean leavebutton = buttonset[3];

        HalfButton.setEnabled(halfbutton);
        CallButton.setEnabled(callbutton);
        DieButton.setEnabled(diebutton);
        Checkbutton.setEnabled(false);

        System.out.println("HALF "+ halfbutton +" , CALL "+callbutton + " , DIE "+diebutton+ ", LEAVE "+leavebutton);
    }


    public void GameEnd(Activity view){

        Button retry;
        Button quitGame;
        retry = PopUpMessage.findViewById(R.id.retryGame);
        quitGame = PopUpMessage.findViewById(R.id.quitGame);

        Winner = inGame.finish();

        cardVisibleInitialize();

        System.out.println(Winner);

        System.out.println(rematch + "+++++++" + Winner);

        if(Arrays.binarySearch(rematch, Winner) <= 0) {

            PopUpMessage.show();

            retry.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    PopUpMessage.dismiss();

                    decorView.setSystemUiVisibility(uiOptions);
                    start();

                }
            });

            quitGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopUpMessage.dismiss();
                    finish();

                }
            });
        }

        else{


        }

    }

    public void start(){

        inGame.setFirstTurn(true);

        inGame.initialize();

        System.out.println("Dummy");

        if (inGame.BaseBettingExecute(MainActivity.this, basedBettingMoney)) {

            System.out.println("Dummy Button Click");

            inGame.setStatus(Boolean.TRUE);

            //애니메이션 시작
            cardDummy1.startAnimation(animTransLeft);

            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardDummy1.startAnimation(animTransRight);
                    user2Card1.setVisibility(View.VISIBLE);

                    //지연시키길 원하는 밀리초 뒤에 동작
                }
            },500);

            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardDummy1.startAnimation(animTransAlpha);
                    user3Card1.setVisibility(View.VISIBLE);
                    //지연시키길 원하는 밀리초 뒤에 동작
                }
            },1000);

            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardDummy1.startAnimation(animTransLeft);
                    user1Card1.setVisibility(View.VISIBLE);
                    user1Card1.setEnabled(true);

                    //지연시키길 원하는 밀리초 뒤에 동작
                }
            },1500);

            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardDummy1.startAnimation(animTransRight);
                    user2Card2.setVisibility(View.VISIBLE);


                    //지연시키길 원하는 밀리초 뒤에 동작
                }
            },2000);

            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardDummy1.startAnimation(animTransAlpha);
                    user3Card2.setVisibility(View.VISIBLE);
                    //지연시키길 원하는 밀리초 뒤에 동작
                }
            },2500);

            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    user1Card2.setVisibility(View.VISIBLE);
                    user1Card2.setEnabled(true);
                    //지연시키길 원하는 밀리초 뒤에 동작
                }
            },3000);

        }
    }

    private void cardImageChecker(ImageView card, int cardnumber) {

        if(cardnumber == January1){
            card.setImageResource(R.drawable.card011);
        }
        if(cardnumber == January2){
            card.setImageResource(R.drawable.card012);
        }
        if(cardnumber == February1){
            card.setImageResource(R.drawable.card021);
        }
        if(cardnumber == February2){
            card.setImageResource(R.drawable.card022);
        }
        if(cardnumber == March1){
            card.setImageResource(R.drawable.card031);
        }
        if(cardnumber == March2){
            card.setImageResource(R.drawable.card032);
        }
        if(cardnumber == April1){
            card.setImageResource(R.drawable.card041);
        }
        if(cardnumber == April2){
            card.setImageResource(R.drawable.card042);
        }
        if(cardnumber == May1){
            card.setImageResource(R.drawable.card051);
        }
        if(cardnumber == May2){
            card.setImageResource(R.drawable.card052);
        }
        if(cardnumber == June1){
            card.setImageResource(R.drawable.card061);
        }
        if(cardnumber == June2){
            card.setImageResource(R.drawable.card062);
        }
        if(cardnumber == July1){
            card.setImageResource(R.drawable.card071);
        }
        if(cardnumber == July2){
            card.setImageResource(R.drawable.card072);
        }
        if(cardnumber == August1){
            card.setImageResource(R.drawable.card081);
        }
        if(cardnumber == August2){
            card.setImageResource(R.drawable.card082);
        }
        if(cardnumber == September1){
            card.setImageResource(R.drawable.card091);
        }
        if(cardnumber == September2){
            card.setImageResource(R.drawable.card092);
        }
        if(cardnumber == October1){
            card.setImageResource(R.drawable.card101);
        }
        if(cardnumber == October2){
            card.setImageResource(R.drawable.card102);
        }
    }

    private void cardVisibleInitialize() {

        cardDummy1.setEnabled(false);

        //처음에 카드 안보이는 용도
        user1Card1.setImageResource(R.drawable.card_back_view);
        user1Card1.setEnabled(false);
        user1Card1.setVisibility(View.INVISIBLE);

        user1Card2.setImageResource(R.drawable.card_back_view);
        user1Card2.setEnabled(false);
        user1Card2.setVisibility(View.GONE);

        user2Card1.setImageResource(R.drawable.card_back_view);
        user2Card1.setVisibility(View.INVISIBLE);
        user2Card2.setImageResource(R.drawable.card_back_view);
        user2Card2.setVisibility(View.GONE);

        user3Card1.setImageResource(R.drawable.card_back_view);
        user3Card1.setVisibility(View.GONE);
        user3Card2.setImageResource(R.drawable.card_back_view);
        user3Card2.setVisibility(View.INVISIBLE);

        user1call.setVisibility(View.GONE);
        user1die.setVisibility(View.GONE);
        user1half.setVisibility(View.GONE);

        user2call.setVisibility(View.GONE);
        user2die.setVisibility(View.GONE);
        user2half.setVisibility(View.GONE);

        user3call.setVisibility(View.GONE);
        user3die.setVisibility(View.GONE);
        user3half.setVisibility(View.GONE);
    }


}

