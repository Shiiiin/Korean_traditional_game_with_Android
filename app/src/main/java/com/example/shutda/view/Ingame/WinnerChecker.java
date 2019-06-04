package com.example.shutda.view.Ingame;

import java.util.HashMap;

import static com.example.shutda.view.data.DummyCards.*;

public class WinnerChecker {

    private HashMap<String, Integer> playersMap;

    public WinnerChecker(int player1Cards, int player2Cards, int player3Cards) {

        this.playersMap = new HashMap<>();
        playersMap.put("player1", player1Cards);
        playersMap.put("player2", player2Cards);
        playersMap.put("player3", player3Cards);
        System.out.println("player1카드" + player1Cards);
        System.out.println("player2카드" + player2Cards);
        System.out.println("player3카드" + player3Cards);
    }

    public void setPlayer(String key, int playerCards) {
        playersMap.put(key, playerCards);
    }

    public void setPlayersMap(int player1Cards, int player2Cards, int player3Cards) {
        playersMap.put("player1", player1Cards);
        playersMap.put("player2", player2Cards);
        playersMap.put("player3", player3Cards);
    }


    public String WinnerClassifier() {

        String winner="";

        int player1rank = carculateCards("player1");
        int player2rank = carculateCards("player2");
        int player3rank = carculateCards("player3");


        System.out.println("player1랭크@@@" + player1rank);
        System.out.println("player2랭크@@@" + player2rank);
        System.out.println("player3랭크@@@@" + player3rank);

        if (player1rank == DDangCatcher) {

            if ((player2rank >= oneDDang & player2rank <= jangDDang) || (player3rank >= oneDDang & player3rank <= jangDDang)) {
                System.out.println("여긴가1");
                winner = "player1";
                return winner;
            }else{
                player1rank = mangtong;
            }

        }else if (player1rank == GwangCatcher) {

            if ((player2rank >= gwangDDang13 & player2rank <= gwangDDAng18) || (player3rank >= gwangDDang13 & player3rank <= gwangDDAng18)) {
                System.out.println("여긴가2");
                winner = "player1";
                return winner;
            }else{
                player1rank = onegguk;
            }

        }else if (player1rank == rematch) {
            if ((player2rank <= jangDDang) & (player3rank <= jangDDang)) {
                winner = "rematch";
                return winner;
            }else{
                player1rank = threegguk;
            }
        }else if (player1rank == rematchDumbful) {
            if ((player2rank <= gwangDDAng18) & (player3rank <= gwangDDAng18)) {
                winner = "rematch";
                return winner;
            }else{
                player1rank = threegguk;
            }
        }


        if (player2rank == DDangCatcher) {

            if ((player1rank >= oneDDang & player1rank <= jangDDang) || (player3rank >= oneDDang & player3rank <= jangDDang)) {
                winner = "player2";
                return winner;
            }else{
                player2rank = mangtong;
            }

        }else if (player2rank == GwangCatcher) {

            if ((player1rank >= gwangDDang13 & player1rank <= gwangDDAng18) || (player3rank >= gwangDDang13 & player3rank <= gwangDDAng18)) {
                winner = "player2";
                return winner;
            }else{
                player2rank = onegguk;
            }

        }else if (player2rank == rematch) {
            if ((player1rank <= jangDDang) & (player3rank <= jangDDang)) {
                winner = "rematch";
                return winner;
            }else{
                player2rank = threegguk;
            }
        }else if (player2rank == rematchDumbful) {
            if ((player1rank <= gwangDDAng18) & (player3rank <= gwangDDAng18)) {
                winner = "rematch";
                return winner;
            }else{
                player2rank = threegguk;
            }
        }

        if (player3rank == DDangCatcher) {

            if ((player2rank >= oneDDang & player2rank <= jangDDang) || (player1rank >= oneDDang & player1rank <= jangDDang)) {
                winner = "player3";
                return winner;
            }else{
                player3rank = mangtong;
            }
        }else if (player3rank == GwangCatcher) {

            if ((player2rank >= gwangDDang13 & player2rank <= gwangDDAng18) || (player1rank >= gwangDDang13 & player1rank <= gwangDDAng18)) {
                winner = "player3";
                return winner;
            }else{
                player3rank = onegguk;
            }

        } else if (player3rank == rematch) {
            if ((player2rank <= oneDDang) & (player1rank <= oneDDang)) {
                winner = "rematch";
                return winner;
            }else{
                player3rank = threegguk;
            }
        }else if (player3rank == rematchDumbful) {
            if ((player2rank <= gwangDDAng18) & (player1rank <= gwangDDAng18)) {
                winner = "rematch";
                return winner;
            }else{
                player3rank = threegguk;
            }
        }


        if( player1rank == player2rank & player1rank > player3rank){
            winner = "rematch12";
            return winner;
        }
        else if(player2rank == player3rank & player2rank > player1rank){
            winner = "rematch23";
            return winner;
        }
        else if(player3rank == player1rank & player3rank > player2rank){
            winner = "rematch31";
            return winner;
        }else if(player1rank == player2rank & player2rank == player3rank){
            winner = "rematch";
            return winner;
        }
        else{
            //우선 player1이랑 player2랑 비교
            String Compare1n2 = (player1rank > player2rank) ? "player1" : "player2";

            //player1이랑 player2랑 비교승자랑 player3이랑 비교
            if(Compare1n2.equals("player1")){

                System.out.println("player1 compare");
                if( player1rank > player3rank){
                    winner = "player1";
                }else{
                    winner = "player3";
                }

                return winner;

            }
            if(Compare1n2.equals("player2")){
                System.out.println("player2 compare");
                if( player2rank > player3rank){
                    winner = "player2";
                }else{
                    winner = "player3";
                }
                return winner;
            }

            System.out.println("여긴가3");
            winner = "player3";
            System.out.println(winner);
            return winner;
        }


    }


//    망통 : 0점
//    1끗 : 1점
//    2끗: 2점
//    3끗: 3점
//    4끗 : 4점
//    5끗: 5점
//    6끗: 6점
//    7끗: 7점
//    8끗: 8점
//    갑오: 9점

//    세륙 : 10점
//    장사 : 11점
//    장삥 : 12점
//    구삥 : 13점
//    독사 : 14점
//    알리 : 15점

//     1땡 : 20점
//     2땡 : 30점
//     3땡 : 40점
//     4땡 : 50점
//     5땡 : 60점
//     6땡 : 70점
//     7땡 : 80점
//     8땡 : 90점
//     9땡 : 100점
//     장땡 : 110점

//     13광땡: 130점
//     18광땡: 140점
//     38광땡: 200점

//     특수패
//     땡잡이: 17점
//     94 : 18점
//     멍텅구리94: 19점
    // 암행어사 : 16점

    public int carculateCards(String player){

        System.out.print(player +"패 계산중....");

        int sumOfCards = playersMap.get(player);
        int rank = -5;

        if(sumOfCards == -2)
            return -2;

        //땡부분
        if(sumOfCards == (January1+January2)){

            rank = oneDDang;
            System.out.println("1땡");
        }
        else if(sumOfCards == (February1+February2)){

            rank = twoDDang;
            System.out.println("2땡");

        }
        else if(sumOfCards == (March1+March2)){

            rank = threeDDang;
            System.out.println("3땡");

        }
        else if(sumOfCards == (April1+April2)){

            rank = fourDDang;
            System.out.println("4땡");

        }
        else if(sumOfCards == (May1+May2)){

            rank = fiveDDang;
            System.out.println("5땡");

        }
        else if(sumOfCards == (June1+June2)){

            rank = sixDDang;
            System.out.println("6땡");

        }
        else if(sumOfCards == (July1+July2)){

            rank = sevenDDang;
            System.out.println("7땡");

        }
        else if(sumOfCards == (August1+August2)){

            rank = eightDDang;
            System.out.println("8땡");

        }
        else if(sumOfCards == (September1+September2)){

            rank = nineDDang;
            System.out.println("9땡");

        }
        else if(sumOfCards == (October1+October2)){

            rank = jangDDang;
            System.out.println("10땡");

        }

        //족보부분
        else if((sumOfCards == (April1+June1)) || (sumOfCards == (April1+June2)) ||
                (sumOfCards == (April2+June1)) || (sumOfCards == (April2+June2))){

            rank = seryuk;
            System.out.println("세륙");

        }
        else if((sumOfCards == (October1+April1)) | (sumOfCards == (October1+April2)) |
                (sumOfCards == (October2+April1)) | (sumOfCards == (October2+April2))){
            rank = jangsa;
            System.out.println("장사");

        }
        else if((sumOfCards == (October1+January1)) | (sumOfCards == (October1+January2)) |
                (sumOfCards == (October2+January1)) | (sumOfCards == (October2+January2))){

            rank = jangping;
            System.out.println("장삥");

        }
        else if((sumOfCards == (September1+January1)) | (sumOfCards == (September1+January2)) |
                (sumOfCards == (September2+January1)) | (sumOfCards == (September2+January2))){

            rank = guping;
            System.out.println("구삥");

        }
        else if((sumOfCards == (April1+January1)) | (sumOfCards == (April1+January2)) |
                (sumOfCards == (April2+January1)) | (sumOfCards == (April2+January2))){

            rank = doksa;
            System.out.println("독사");

        }
        else if((sumOfCards == (January1+February1)) | (sumOfCards == (January1+February2)) |
                (sumOfCards == (January2+February1)) | (sumOfCards == (January2+February1))){

            rank = alli;
            System.out.println("알리");

        }

        //광땡부분
        else if(sumOfCards == (January1+March1)){

            rank = gwangDDang13;
            System.out.println("13광땡");

        }
        else if(sumOfCards == (January1+August1)){

            rank = gwangDDAng18;
            System.out.println("18광땡");

        }
        else if(sumOfCards == (March1+August1)){

            rank = gwangDDAng38;
            System.out.println("38광땡");

        }

        //땡잡이
        else if(sumOfCards == (July1+March1) ){

            rank = DDangCatcher;
            System.out.println("땡잡이");

        }
        else if(sumOfCards == (April1 + July1)){

            rank = GwangCatcher;
            System.out.println("암행어사");
        }
        //구사
        else if(sumOfCards == (September1+April2) | sumOfCards == (September2+April1) | sumOfCards == (September2+April2)){

            rank = rematch;
        }
        //멍텅구리구사
        else if(sumOfCards == (September1+April1)){

            rank = rematchDumbful;
        }

        //끗 판별
        else{
            int theNumberOfZero = 1;
            int firstOneLocation = 0;
            int secondOneLocation = 0;
            int thirdOneLocation = 0;
            int value1=0;
            int value2=0;

            while(sumOfCards != 0){
                if(sumOfCards % 10 == 2){
                    firstOneLocation = theNumberOfZero;
                    theNumberOfZero++;
                    sumOfCards = sumOfCards/10;
                }
                else if(sumOfCards % 10 == 0){
                    theNumberOfZero++;
                    sumOfCards = sumOfCards/10;
                    System.out.println("한번나눠준거"+sumOfCards);
                }
                else if ( firstOneLocation == 0 & sumOfCards % 10 == 1){
                    firstOneLocation = theNumberOfZero;
                    theNumberOfZero++;
                    sumOfCards = sumOfCards/10;
                    System.out.println("첫번째위치"+firstOneLocation);
                }else if ( (firstOneLocation != 0 & sumOfCards % 10 == 1) & secondOneLocation == 0){
                    secondOneLocation = theNumberOfZero;
                    theNumberOfZero++;
                    sumOfCards = sumOfCards/10;
                    System.out.println("두번째위치"+secondOneLocation);
                }else if( (secondOneLocation != 0) & ((sumOfCards % 10) == 1)){
                    thirdOneLocation = theNumberOfZero;
                    theNumberOfZero++;
                    sumOfCards = sumOfCards/10;
                    System.out.println("세번째위치"+thirdOneLocation);
                }
            }
            if(secondOneLocation == 0 & thirdOneLocation == 0){
                value1 = 1;
                value2 = firstOneLocation;
            }
            if(thirdOneLocation == 0 & secondOneLocation != 0){
                value1 = firstOneLocation;
                value2 = secondOneLocation;
            }
            if(thirdOneLocation != 0){
                value1 = secondOneLocation;
                value2 = thirdOneLocation;
            }
            System.out.println("value1///"+value1);
            System.out.println("value2///"+value2);

            if(0 == (value1 + value2) %10 ){
                rank = mangtong;
            }
            if( 1 == (value1 + value2) %10){
                rank = onegguk;
            }
            if( 2 == (value1 + value2) %10){
                rank = twogguk;
            }
            if( 3 == (value1 + value2) %10){
                rank = threegguk;
            }
            if( 4 == (value1 + value2) %10){
                rank = fourgguk;
            }
            if( 5 == (value1 + value2) %10){
                rank = fivegguk;
            }
            if( 6 == (value1 + value2) %10){
                rank = sixgguk;
            }
            if( 7 == (value1 + value2) %10){
                rank = sevengguk;
            }
            if( 8 == (value1 + value2) %10){
                rank = eightgguk;
            }
            if( 9 == (value1 + value2) %10){
                rank = gapou;
            }

        }

        System.out.print(player +"패 계산끝...!!!");

        return rank;

    }

}
