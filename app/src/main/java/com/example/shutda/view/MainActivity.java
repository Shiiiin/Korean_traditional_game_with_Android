package com.example.shutda.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shutda.R;
import com.example.shutda.view.Ingame.gameViewModel;
import com.example.shutda.view.background.BackPressCloseHandler;
import com.example.shutda.view.data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static com.example.shutda.view.data.constantsField.*;
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    public static boolean isTurn = false;

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


    private gameViewModel inGame;
    private LiveData<Boolean> gameStatus;
    private LiveData<HashMap<String, User>> userlist;
    private LiveData<Integer> TotalBettingMoney;

    //View
    private ImageView cardDummy;
    private ImageView cardDummy2;
    private ImageView cardDummy3;
    private ImageView cardDummy4;
    private ImageView cardDummy5;
    private ImageView cardDummy6;
    private ImageView cardDummy7;
    private ImageView user1Card1;
    private ImageView user1Card2;
    private ImageView user2Card1;
    private ImageView user2Card2;
    private ImageView user3Card1;
    private ImageView user3Card2;

    private LinearLayout buttonLayout;
    private ImageButton HalfButton;
    private ImageButton CallButton;
    private ImageButton DieButton;
    private ImageButton button4;
    private ImageButton button5;
    private ImageButton LeaveButton;

    private TextView player2NameTextView;
    private TextView player3NameTextView;

    private TextView currentBettingMoney;
    private CardView jokbo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainframe = findViewById(R.id.main_frame);

        cardDummy = findViewById(R.id.CardDummy);
        cardDummy2 = findViewById(R.id.cardDummy2);
        cardDummy3 = findViewById(R.id.cardDummy3);
        cardDummy4 = findViewById(R.id.cardDummy4);
        cardDummy5 = findViewById(R.id.cardDummy5);
        cardDummy6 = findViewById(R.id.cardDummy6);
        cardDummy7 = findViewById(R.id.cardDummy7);
        user1Card1 = findViewById(R.id.user1Card1);
        user1Card2 = findViewById(R.id.user1Card2);
        user2Card1 = findViewById(R.id.user2Card1);
        user2Card2 = findViewById(R.id.user2Card2);
        user3Card1 = findViewById(R.id.user3Card1);
        user3Card2 = findViewById(R.id.user3Card2);

        buttonLayout = findViewById(R.id.button_layout);
        HalfButton = findViewById(R.id.halfbutton);
        CallButton = findViewById(R.id.callbutton);
        DieButton = findViewById(R.id.diebutton);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        LeaveButton = findViewById(R.id.leavebutton);

        player2NameTextView = findViewById(R.id.player2Name);
        player3NameTextView = findViewById(R.id.player3Name);

        currentBettingMoney = findViewById(R.id.currentBettingMoneyText);
        jokbo = findViewById(R.id.jokbo);

        mDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        inGame = ViewModelProviders.of(this).get(gameViewModel.class);
        gameStatus = inGame.getIngameStatus();
        userlist = inGame.getUsers();
        TotalBettingMoney = inGame.getTotalBettingMoney();

        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        backPressCloseHandler = new BackPressCloseHandler(this, mAuth);


        if (mAuth.getCurrentUser() == null) {

            sendBack();

        } else {

            mUserId = mAuth.getCurrentUser().getUid();
        }

        mainLoop();


        //TODO START 부분 구현 // 우선 click해야 시작으로 해놨음 -> bool로 바꾸기
        cardDummy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (inGame.BaseBettingExecute(MainActivity.this, basedBettingMoney)) {

                    inGame.setStatus(Boolean.TRUE);

                }

            }
        });

        HalfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int bettingMoney = inGame.getTotalBettingMoney().getValue();

                int halfBetting = (int) Math.floor(bettingMoney / 2);

                inGame.HalfButtonExecute(MainActivity.this, halfBetting, "player1");

                boolean buttonAttr[] = inGame.getUsers().getValue().get("player1").getButtonClickEnable();


            }
        });

        CallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                inGame.CallButtonExecute(MainActivity.this, );

            }
        });

        DieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inGame.DieButtonExecute(MainActivity.this, "player1");

            }
        });

        LeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inGame.setStatus(Boolean.FALSE);

            }
        });


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
        //        super.onBackPressed();
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

                String name = (String) documentSnapshot.get("name");
                long score = (Long) documentSnapshot.get("score");
                String token_id = (String) documentSnapshot.get("token_id");

                //Edit Players
                Me = new User(name, score, token_id, isTurn, false);
                Ai = new User("AI", 100000, "ALPHAGO", isTurn, false);
                Ai2 = new User("AI2", 100000, "ALPHAGO2", isTurn, false);
                userMap.put("player1", Me);
                userMap.put("player2", Ai);
                userMap.put("player3", Ai2);
                inGame.setUsers(userMap);


                userlist.observe(MainActivity.this, new Observer<HashMap<String, User>>() {
                    @Override
                    public void onChanged(@Nullable HashMap<String, User> users) {

                        if (users.containsKey("player2")) {
                            player2NameTextView.setText(users.get("player2").getName());
                        }
                        if (users.containsKey("player3")) {
                            player3NameTextView.setText(users.get("player3").getName());
                        }
                    }

                });

                TotalBettingMoney.observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer money) {

                        currentBettingMoney.setText(String.valueOf(money));

                    }
                });


                //StartGame
                gameStatus.observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {

                        if (aBoolean) {

                            cardDummy.setEnabled(false);
                            LeaveButton.setEnabled(false);
//                                isturnOnView(false);

                            inGame.initiate();

                            inGame.execute();

                            LeaveButton.setEnabled(true);

                        }
                        if (!aBoolean) {
                            //Update player score on Firestore
                            inGame.uploadScoreToFirestore(currentUser, Me, Ai, Ai2);

                            //지금은 이렇게 되어있는데 인게임 밖으로 나가게 만들어야함

                            inGame.finish();

                            cardDummy.setEnabled(true);
                            //
                        }
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


    //TESTSETESTSET
    public void HalfButtonClicked(boolean[] enable) {

        boolean halfbutton = enable[0];
        boolean callbutton = enable[1];
        boolean diebutton = enable[2];
        boolean leavebutton = enable[3];

        HalfButton.setEnabled(halfbutton);
        CallButton.setEnabled(callbutton);
        DieButton.setEnabled(diebutton);
        button4.setEnabled(false);
        button5.setEnabled(false);
        LeaveButton.setEnabled(leavebutton);
    }
}

