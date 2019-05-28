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

    public String WinnerClassifier() {

        String winner="";
        int player1rank = carculateCards("player1");
        int player2rank = carculateCards("player2");
        int player3rank = carculateCards("player3");

        System.out.println("player1랭크@@@" + player1rank);
        System.out.println("player2랭크@@@" + player2rank);
        System.out.println("player3랭크@@@@" + player3rank);

        if (player1rank == DDangCatcher) {

            if ((player2rank >= oneDDang & player2rank <= jangDDang) | (player3rank >= oneDDang & player3rank <= jangDDang)) {
                winner = "player1";
            }else{
                player1rank = mangtong;
            }

        }else if (player1rank == rematch) {
            if ((player2rank <= jangDDang) & (player3rank <= jangDDang)) {
                winner = "rematch";
            }else{
                player1rank = onegguk;
            }
        }else if (player1rank == rematchDumbful) {
            if ((player2rank <= gwangDDAng18) & (player3rank <= gwangDDAng18)) {
                winner = "rematch";
            }else{
                player1rank = onegguk;
            }
        }


        if (player2rank == DDangCatcher) {

            if ((player1rank >= oneDDang & player1rank <= jangDDang) | (player3rank >= oneDDang & player3rank <= jangDDang)) {
                winner = "player2";
            }else{
                player2rank = mangtong;
            }

        }else if (player2rank == rematch) {
            if ((player1rank <= jangDDang) & (player3rank <= jangDDang)) {
                winner = "rematch";
            }else{
                player2rank = onegguk;
            }
        }else if (player2rank == rematchDumbful) {
            if ((player1rank <= gwangDDAng18) & (player3rank <= gwangDDAng18)) {
                winner = "rematch";
            }else{
                player2rank = onegguk;
            }
        }

        if (player3rank == DDangCatcher) {

            if ((player2rank >= oneDDang & player2rank <= jangDDang) | (player1rank >= oneDDang & player1rank <= jangDDang)) {
                winner = "player3";
            }else{
                player3rank = mangtong;
            }
        }else if (player3rank == rematch) {
            if ((player2rank <= oneDDang) & (player1rank <= oneDDang)) {
                winner = "rematch";
            }else{
                player3rank = onegguk;
            }
        }else if (player3rank == rematchDumbful) {
            if ((player2rank <= gwangDDAng18) & (player1rank <= gwangDDAng18)) {
                winner = "rematch";
            }else{
                player3rank = onegguk;
            }
        }


        if( player1rank == player2rank & player1rank > player3rank){
            winner = "rematch12";
        }
        else if(player2rank == player3rank & player2rank > player1rank){
            winner = "rematch23";
        }
        else if(player3rank == player1rank & player3rank > player2rank){
            winner = "rematch31";
        }
        else{

            //우선 player1이랑 player2랑 비교
            String Compare1n2 = (player1rank > player2rank) ? "player1" : "player2";


            //player1이랑 player2랑 비교승자랑 player3이랑 비교
            if(Compare1n2 == "player1"){

                winner = (player1rank > player3rank) ? "player1" : "player3";

            }else if(Compare1n2 == "player2"){

                winner = (player2rank > player3rank) ? "player2" : "player3";

            }

        }

        return winner;
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
//     땡잡이: 3737점
//     94 : 4949점
//     멍텅구리94: 9494점

    public int carculateCards(String player){

        System.out.print(player +"패 계산중....");

        int sumOfCards = playersMap.get(player);
        int rank = -5;


        //땡부분
        if(sumOfCards == (January1+January2)){

            rank = oneDDang;
        }
        else if(sumOfCards == (February1+February2)){

            rank = twoDDang;
        }
        else if(sumOfCards == (March1+March2)){

            rank = threeDDang;
        }
        else if(sumOfCards == (April1+April2)){

            rank = fourDDang;
        }
        else if(sumOfCards == (March1+March2)){

            rank = fiveDDang;
        }
        else if(sumOfCards == (June1+June2)){

            rank = sixDDang;
        }
        else if(sumOfCards == (July1+July2)){

            rank = sevenDDang;
        }
        else if(sumOfCards == (August1+August2)){

            rank = eightDDang;
        }
        else if(sumOfCards == (September1+September2)){

            rank = nineDDang;
        }
        else if(sumOfCards == (October1+October2)){

            rank = jangDDang;
        }

        //족보부분
        else if((sumOfCards == (April1+June1)) || (sumOfCards == (April1+June2)) ||
                (sumOfCards == (April2+June1)) || (sumOfCards == (April2+June2))){

            rank = seryuk;
        }
        else if((sumOfCards == (October1+April1)) | (sumOfCards == (October1+April2)) |
                (sumOfCards == (October2+April1)) | (sumOfCards == (October2+April2))){
            rank = jangsa;
        }
        else if((sumOfCards == (October1+January1)) | (sumOfCards == (October1+January2)) |
                (sumOfCards == (October2+January1)) | (sumOfCards == (October2+January2))){

            rank = jangping;
        }
        else if((sumOfCards == (September1+January1)) | (sumOfCards == (September1+January2)) |
                (sumOfCards == (September2+January1)) | (sumOfCards == (September2+January2))){

            rank = guping;
        }
        else if((sumOfCards == (April1+January1)) | (sumOfCards == (April1+January2)) |
                (sumOfCards == (April2+January1)) | (sumOfCards == (April2+January2))){

            rank = doksa;
        }
        else if((sumOfCards == (January1+February1)) | (sumOfCards == (January1+February2)) |
                (sumOfCards == (January2+February1)) | (sumOfCards == (January2+February1))){

            rank = alli;
        }

        //광땡부분
        else if(sumOfCards == (January1+March1)){

            rank = gwangDDang13;
        }
        else if(sumOfCards == (January1+August1)){

            rank = gwangDDAng18;
        }
        else if(sumOfCards == (March1+August1)){

            rank = gwangDDAng38;
        }

        //땡잡이
        else if(sumOfCards == (July1+March1) | sumOfCards == (July1+March2) | sumOfCards == (July2+March1) | sumOfCards == (July2+March2) ){

            rank = DDangCatcher;
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
            if(thirdOneLocation != 0){
                value1 = secondOneLocation;
                value2 = thirdOneLocation;
            }
            if(thirdOneLocation == 0){
                value1 = firstOneLocation;
                value2 = secondOneLocation;
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
