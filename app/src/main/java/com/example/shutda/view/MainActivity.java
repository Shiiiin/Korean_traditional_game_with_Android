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
import android.telecom.Call;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shutda.R;
import com.example.shutda.view.Ingame.EventHandler;
import com.example.shutda.view.Ingame.GameThread;
import com.example.shutda.view.Ingame.TaskQueue;
import com.example.shutda.view.Ingame.WinnerChecker;
import com.example.shutda.view.Ingame.gameViewModel;
import com.example.shutda.view.utils.BackPressCloseHandler;
import com.example.shutda.view.data.User;
import com.example.shutda.view.utils.MusicPlayer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Timer;

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
    private ImageView user1check;
    private ImageView user2call;
    private ImageView user2die;
    private ImageView user2half;
    private ImageView user2check;
    private ImageView user3call;
    private ImageView user3die;
    private ImageView user3half;
    private ImageView user3check;

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

    private TextView jokboTextView;
    public TextView turnTextView;

    private GameThread gameThread;

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

        inGame = ViewModelProviders.of(this).get(gameViewModel.class);

        MusicPlayer mp = MusicPlayer.getInstance(this);

        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if(msg.what == 1){

                    cardDummy1.startAnimation(animTransAlpha);

                    System.out.println("!!!!!!!!!1");
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cardDummy1.startAnimation(animTransAlpha);
                            user1Card1.setVisibility(View.VISIBLE);

                            //지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },500);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            user1Card2.setVisibility(View.VISIBLE);
                            TaskQueue.TaskFinishCallback();
//                            지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },1000);
                }
                if(msg.what == 2){
                    System.out.println("!!!!!!!!!2");
                    cardDummy1.startAnimation(animTransLeft);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cardDummy1.startAnimation(animTransLeft);
                            user2Card1.setVisibility(View.VISIBLE);

//                            지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },500);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            user2Card2.setVisibility(View.VISIBLE);
                            TaskQueue.TaskFinishCallback();
//                            지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },1000);


                }
                if(msg.what == 3){
                    System.out.println("!!!!!!!!!3");
                    cardDummy1.startAnimation(animTransRight);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cardDummy1.startAnimation(animTransRight);
                            user3Card1.setVisibility(View.VISIBLE);

                            //지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },500);



                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            user3Card2.setVisibility(View.VISIBLE);
                            TaskQueue.TaskFinishCallback();
//                            지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },1000);
                }

                //두번째 패 나눠주는 부분
                if(msg.what == 4){
                    System.out.println("!!!!!!!!!4");
                    cardDummy1.startAnimation(animTransAlpha);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            user1Card2.setVisibility(View.VISIBLE);
                            TaskQueue.TaskFinishCallback();
                            //지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },500);

                }
                if(msg.what == 5){
                    System.out.println("!!!!!!!!!5");
                    cardDummy1.startAnimation(animTransLeft);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            user2Card2.setVisibility(View.VISIBLE);
                            TaskQueue.TaskFinishCallback();
                            //지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },500);

                }
                if(msg.what == 6){
                    System.out.println("!!!!!!!!!6");
                    cardDummy1.startAnimation(animTransRight);

                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            user3Card2.setVisibility(View.VISIBLE);
                            TaskQueue.TaskFinishCallback();
                            //지연시키길 원하는 밀리초 뒤에 동작
                        }
                    },500);

                }



                if(msg.what == 21){
                    //check
                    mp.checksound();
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("player2 get Check Ani Message");
                            ReactDecision(user2check);
                        }
                    },ReactionSpeed);
                    TaskQueue.TaskFinishCallback();
                }
                if(msg.what == 22){
                    //Call
                    mp.callsound();
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("player2 get Call Ani Message");
                            ReactDecision(user2call);
                        }
                    },ReactionSpeed);

                    TaskQueue.TaskFinishCallback();
                }
                if(msg.what == 23){
                    //Half
                    mp.halfsound();
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("player2 get Half Ani Message");
                            ReactDecision(user2half);
                        }
                    },ReactionSpeed);
                    TaskQueue.TaskFinishCallback();

                }
                if(msg.what == 24){
                    //Die
                    mp.diesound();
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("player2 get Die Ani Message");
                            ReactDecision(user2die);
                        }
                    },ReactionSpeed);
                    TaskQueue.TaskFinishCallback();
                }
                if(msg.what == 31){
                    //check
                    mp.checksound();
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("player3 get Check Ani Message");
                            ReactDecision(user3check);
                        }
                    },ReactionSpeed);
                    TaskQueue.TaskFinishCallback();
                }
                if(msg.what == 32){
                    //Call
                    mp.callsound();
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("player3 get Call Ani Message");
                            ReactDecision(user3call);

                        }
                    },ReactionSpeed);
                    TaskQueue.TaskFinishCallback();

                }
                if(msg.what == 33){
                    //Half
                    mp.halfsound();
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("player3 get Half Ani Message");
                            ReactDecision(user3half);
                        }
                    },ReactionSpeed);
                    TaskQueue.TaskFinishCallback();

                }
                if(msg.what == 34){
                    //Die
                    mp.diesound();
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("player3 get Die Ani Message");
                            ReactDecision(user3die);
                        }
                    },ReactionSpeed);
                    TaskQueue.TaskFinishCallback();

                }
            }
        };

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
        user1check = findViewById(R.id.user1check);

        user2call  = findViewById(R.id.user2call);
        user2die   = findViewById(R.id.user2die);
        user2half  = findViewById(R.id.user2half);
        user2check = findViewById(R.id.user2check);

        user3call  = findViewById(R.id.user3call);
        user3die   = findViewById(R.id.user3die);
        user3half  = findViewById(R.id.user3half);
        user3check = findViewById(R.id.user3check);

        HalfButton = findViewById(R.id.halfbutton);
        CallButton = findViewById(R.id.callbutton);
        DieButton = findViewById(R.id.diebutton);
        Checkbutton = findViewById(R.id.checkbutton);

        jokboTextView = findViewById(R.id.jokboTextView);
        turnTextView = findViewById(R.id.turnTextView);

        cardVisibleInitialize();

        animTransRight = AnimationUtils.loadAnimation(
                this,R.anim.giveright);
        animTransLeft = AnimationUtils.loadAnimation(
                this,R.anim.giveleft);
        animTransAlpha = AnimationUtils.loadAnimation(
                this,R.anim.giveme);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);

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

        decorView = getWindow().getDecorView();
        uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        backPressCloseHandler = new BackPressCloseHandler(this);

        buttonSetting(AllbuttonOFF);

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

                    mp.halfsound();
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReactDecision(user1half);
                        }
                    },ReactionSpeed);

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

                mp.callsound();
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ReactDecision(user1call);
                    }
                },ReactionSpeed);


                buttonSetting(AllbuttonOFF);

            }
        });


        DieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inGame.DieButtonExecute(MainActivity.this, "player1");

                System.out.println("Die Button Click");

                mp.diesound();
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ReactDecision(user1die);
                    }
                },ReactionSpeed);


                //죽었으니까 패 뒤집어주기
                user1Card1.setImageResource(R.drawable.card_back_view);
                user1Card2.setImageResource(R.drawable.card_back_view);

                buttonSetting(AllbuttonOFF);
            }
        });

        Checkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inGame.CheckButtonExecute(MainActivity.this, "player1");

                System.out.println("Check Button Click");
                mp.checksound();
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ReactDecision(user1check);
                    }
                },ReactionSpeed);

                System.out.println("Check Button Click");
                buttonSetting(AllbuttonOFF);
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

                    int player1card1 = inGame.getUsers().getValue().get("player1").getCard1();
                    int player1card2 = inGame.getUsers().getValue().get("player1").getCard2();

                    cardImageChecker(user1Card2, player1card2);

                    //TODO 족보 보일거 여기다가하기~!~!
                    jokbofinder();

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

                        System.out.println(image +" get Ani Message");
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

        if(gameStatus.getValue()){
            Toast.makeText(MainActivity.this, "게임도중에 나갈수 없어요!", Toast.LENGTH_LONG).show();
        }
        if(!gameStatus.getValue()){
            backPressCloseHandler.onBackPressed();
        }

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

                    inGame.setWinner("player1");

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

                        if(aBoolean){

                            if(inGame.getUsers().getValue().get("player1").isAlive()) {
                                if (inGame.getUsers().getValue().get("player2").isAlive() || inGame.getUsers().getValue().get("player3").isAlive()) {
                                    user1Card1.setEnabled(true);
                                    user1Card2.setEnabled(true);
                                    turnTextView.setText("당신 차례 입니다.");
                                    Boolean[] buttons = inGame.getUsers().getValue().get("player1").getButtonClickEnable();
                                    buttonSetting(buttons);
                                }
                            }

                        }else{
                            turnTextView.setText("");
                        }

                    }
                });

                        System.out.println("@@@@ Thread AI 등록 실행 @@@@");

                        System.out.println("@@@@ Thread Run 실행 @@@@");


                CallNumber.observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        System.out.println(inGame.getFirstTurn().getValue());
                        System.out.println("두번째 카드 반응 트리거"+ inGame.getSecondsCardsPollingTrigger().getValue());
                        if(inGame.getCallNumber().getValue() != 0) {

                            if (inGame.getFirstTurn().getValue()) {

                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //여기가 마지막 call 애니메이션보다 먼저 반응함


                                if (DieNumber.getValue() != null && inGame.checkEnd()) {
                                    if (DieNumber.getValue() == 2) {
                                        inGame.getUsers().getValue().get("player1").setAlive(false);
                                        inGame.getUsers().getValue().get("player2").setAlive(false);
                                        inGame.getUsers().getValue().get("player3").setAlive(false);
                                        inGame.setStatus(false);
                                        System.out.println("Callnum 1");
                                    } else {
                                        inGame.getUsers().getValue().get("player1").setEnableClickCheckButton(true);
                                        inGame.setHalfNumber(0);
                                        inGame.setCallNumber(0);
                                        inGame.setFirstTurn(false);
                                        inGame.setEnableCheck(true);
                                        System.out.println("Callnum 2");

                                        SecondCardspolling();
                                    }
                                }
                            } else {
                                if (DieNumber.getValue() != null && inGame.checkEnd()) {
                                    inGame.getUsers().getValue().get("player1").setAlive(false);
                                    inGame.getUsers().getValue().get("player2").setAlive(false);
                                    inGame.getUsers().getValue().get("player3").setAlive(false);
                                    inGame.setStatus(false);
                                    System.out.println("Callnum 3");
                                }
                            }
                        }
                    }
                });
                DieNumber.observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        System.out.println(inGame.getFirstTurn().getValue());
                        if(inGame.getDieNumber().getValue() != 0) {
                            if (inGame.getFirstTurn().getValue()) {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (CallNumber.getValue() != null && inGame.checkEnd()) {
                                    if (DieNumber.getValue() == 2) {
                                        inGame.getUsers().getValue().get("player1").setAlive(false);
                                        inGame.getUsers().getValue().get("player2").setAlive(false);
                                        inGame.getUsers().getValue().get("player3").setAlive(false);
                                        inGame.setStatus(false);
                                        System.out.println("Dienum 1");
                                    } else {
                                        inGame.getUsers().getValue().get("player1").setEnableClickCheckButton(true);
                                        inGame.setHalfNumber(0);
                                        inGame.setCallNumber(0);
                                        inGame.setFirstTurn(false);
                                        inGame.setEnableCheck(true);
                                        System.out.println("Dienum 2");

                                        SecondCardspolling();

                                    }
                                }
                            } else {
                                if (DieNumber.getValue() != null && inGame.checkEnd()) {
                                    inGame.getUsers().getValue().get("player1").setAlive(false);
                                    inGame.getUsers().getValue().get("player2").setAlive(false);
                                    inGame.getUsers().getValue().get("player3").setAlive(false);
                                    inGame.setStatus(false);
                                    System.out.println("Dienum 3");
                                }
                            }
                        }
                    }
                });
                HalfNumber.observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        inGame.setCallNumber(0);
                    }
                });


                //StartGame관련
                gameStatus.observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {

                        if (aBoolean) {

                            //시작버튼 꺼주고
                            cardDummy1.setEnabled(false);

                            inGame.execute(MainActivity.this);
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

                            //사용자가 결과 확인할수 있게 기다려줌
                            mhandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    GameEnd(MainActivity.this);
                                }
                            },5000);
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

        boolean checkbutton = buttonset[0];
        boolean callbutton = buttonset[1];
        boolean halfbutton = buttonset[2];
        boolean diebutton = buttonset[3];

        Checkbutton.setEnabled(checkbutton);
        HalfButton.setEnabled(halfbutton);
        CallButton.setEnabled(callbutton);
        DieButton.setEnabled(diebutton);


        System.out.println("CHECK "+ checkbutton +" , CALL "+ callbutton +" , HALF "+ halfbutton +", DIE "+ diebutton);
    }


    public void GameEnd(Activity view){

        ImageView winlose;
        ImageButton retry;
        ImageButton quitGame;
        retry = PopUpMessage.findViewById(R.id.retryGame);
        quitGame = PopUpMessage.findViewById(R.id.quitGame);
        winlose = PopUpMessage.findViewById(R.id.winlose);

        cardVisibleInitialize();

        gameThread.interrupt();

        inGame.finish();

        System.out.println(inGame.getWinner().getValue());

        String winner = inGame.winnerChecker.WinnerClassifier();
        System.out.println(inGame.winnerChecker.WinnerClassifier());
        System.out.println(winner.matches("^rematch") + "+++++++" + inGame.getWinner().getValue());


        //TODO rematch일때 dialog 생성됨

        if(!winner.matches("^rematch")) {

            if(winner.equals("player1")){
                winlose.setImageResource(R.drawable.win);
            }
            else{
                winlose.setImageResource(R.drawable.lose);
            }

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

    }

    public void start(){

        inGame.setFirstTurn(true);

        inGame.setSecondsCardsPollingTrigger(false);

        inGame.initialize();

        System.out.println("Dummy");

        if (inGame.BaseBettingExecute(MainActivity.this, basedBettingMoney)) {

            System.out.println("Dummy Button Click");

            inGame.setStatus(Boolean.TRUE);

            FirstCardsPolling();
        }
    }

    private void FirstCardsPolling() {

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
                user1Card1.setVisibility(View.VISIBLE);
                user1Card1.setEnabled(true);
                //지연시키길 원하는 밀리초 뒤에 동작
            }
        },1500);
    }

    private void SecondCardspolling(){

        if(inGame.getUsers().getValue().get("player1").isAlive()){

            Message msg = new Message();
            msg.what = 4;
            new EventHandler(msg).addTask();
        }

        if(inGame.getUsers().getValue().get("player2").isAlive()){

            Message msg = new Message();
            msg.what = 5;
            new EventHandler(msg).addTask();
        }

        if(inGame.getUsers().getValue().get("player3").isAlive()){

            Message msg = new Message();
            msg.what = 6;
            new EventHandler(msg).addTask();
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

    private void jokbofinder() {

        int rank = inGame.winnerChecker.carculateCards("player1");

        //TODO 여기 할차례
        if(rank == mangtong){
            jokboTextView.setText("망통");
        }
        if(rank == onegguk){
            jokboTextView.setText("한끗");
        }
        if(rank == twogguk){
            jokboTextView.setText("두끗");
        }
        if(rank == threegguk){
            jokboTextView.setText("세끗");
        }
        if(rank == fourgguk){
            jokboTextView.setText("네끗");
        }
        if(rank == fivegguk){
            jokboTextView.setText("다섯끗");
        }
        if(rank == sixgguk){
            jokboTextView.setText("여섯끗");
        }
        if(rank == sevengguk){
            jokboTextView.setText("일곱끗");
        }
        if(rank == eightgguk){
            jokboTextView.setText("여덟끗");
        }
        if(rank == gapou){
            jokboTextView.setText("갑오");
        }
        if(rank == seryuk){
            jokboTextView.setText("세륙");
        }
        if(rank == jangsa){
            jokboTextView.setText("장사");
        }
        if(rank == jangping){
            jokboTextView.setText("장삥");
        }
        if(rank == guping){
            jokboTextView.setText("구삥");
        }
        if(rank == doksa){
            jokboTextView.setText("독사");
        }
        if(rank == alli){
            jokboTextView.setText("알리");
        }
        if(rank == oneDDang){
            jokboTextView.setText("1땡");
        }
        if(rank == twoDDang){
            jokboTextView.setText("2땡");
        }
        if(rank == threeDDang){
            jokboTextView.setText("3땡");
        }
        if(rank == fourDDang){
            jokboTextView.setText("4땡");
        }
        if(rank == fiveDDang){
            jokboTextView.setText("5땡");
        }
        if(rank == sixDDang){
            jokboTextView.setText("6땡");
        }
        if(rank == sevenDDang){
            jokboTextView.setText("7땡");
        }
        if(rank == eightDDang){
            jokboTextView.setText("8땡");
        }
        if(rank == nineDDang){
            jokboTextView.setText("9땡");
        }
        if(rank == jangDDang){
            jokboTextView.setText("장땡");
        }
        if(rank == gwangDDang13){
            jokboTextView.setText("13광땡");
        }
        if(rank == gwangDDAng18){
            jokboTextView.setText("18광땡");
        }
        if(rank == gwangDDAng38){
            jokboTextView.setText("38광땡");
        }
        if(rank == DDangCatcher){
            jokboTextView.setText("땡잡이 (1땡부터 장땡까지 잡을 수 있습니다.)");
        }
        if(rank == GwangCatcher){
            jokboTextView.setText("암행어사 (13, 18광땡 잡을 수 있습니다.");
        }
        if(rank == rematch){
            jokboTextView.setText("사구 (광땡 없을경우 재경기)");
        }
        if(rank == rematchDumbful){
            jokboTextView.setText("멍텅구리사구 (38광땡 없을 경우 재경기)");
        }
    }

    private void cardVisibleInitialize() {

        cardDummy1.setEnabled(false);

        jokboTextView.setText(" ");
        turnTextView.setText(" ");

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
        user1check.setVisibility(View.GONE);

        user2call.setVisibility(View.GONE);
        user2die.setVisibility(View.GONE);
        user2half.setVisibility(View.GONE);
        user2check.setVisibility(View.GONE);

        user3call.setVisibility(View.GONE);
        user3die.setVisibility(View.GONE);
        user3half.setVisibility(View.GONE);
        user3check.setVisibility(View.GONE);

        Checkbutton.setEnabled(false);
        HalfButton.setEnabled(false);
        DieButton.setEnabled(false);
        CallButton.setEnabled(false);
    }


}

