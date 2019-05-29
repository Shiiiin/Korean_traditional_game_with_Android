package com.example.shutda.view.data;

public class User {
    private String name;
    private long score;
    private int Card1 = 0;
    private int Card2 = 0;
    private int SumOfBetting =0;
//    private boolean isTurn;
    private boolean isAlive;
    private boolean EnableClickHalfButton;
    private boolean EnableClickCallButton;
    private boolean EnableClickDieButton;
    private boolean EnableClickLeaveButton;


    public User (String name, long score, boolean life){
        this.name = name;
        this.score = score;
        this.isAlive = life;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getCard1() {
        return Card1;
    }

    public void setCard1(int card1) {
        Card1 = card1;
    }

    public int getCard2() {
        return Card2;
    }

    public void setCard2(int card2) {
        Card2 = card2;
    }

    public int getSumOfBetting() {
        return SumOfBetting;
    }

    public void setSumOfBetting(int sumOfBetting) {
        SumOfBetting = sumOfBetting;
    }

    public void setButtonClickEnable(boolean halfbutton, boolean callbutton, boolean diebutton, boolean leavebutton){

        this.EnableClickHalfButton = halfbutton;
        this.EnableClickCallButton = callbutton;
        this.EnableClickDieButton = diebutton;
        this.EnableClickLeaveButton = leavebutton;
    }

    public Boolean[] getButtonClickEnable(){

        Boolean groupbutton[] = {EnableClickHalfButton, EnableClickCallButton,
                                  EnableClickDieButton, EnableClickLeaveButton};

        return groupbutton;
    }

    public int getCardValues() {
        return Card1+ Card2;
    }

    public boolean Betting(int money){

        long remainMoney = this.score - money;

        if(remainMoney >=0){
            this.score = remainMoney;
            SumOfBetting = SumOfBetting + money;
            return true;
        }else{
            return false;
        }
    }

    public int All_in(){
        //Should Execute this method when only Halfbutton is clicked
        int bettingMoney = (int) score;

        SumOfBetting =  SumOfBetting + bettingMoney;

        this.score = 0;

        return bettingMoney;
    }
}
