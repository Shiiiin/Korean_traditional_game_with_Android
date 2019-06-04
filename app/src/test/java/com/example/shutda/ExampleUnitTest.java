package com.example.shutda;


import com.example.shutda.view.Ingame.WinnerChecker;

import org.junit.Test;

import static com.example.shutda.view.data.DummyCards.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    private WinnerChecker winnerChecker;

    @Test
    public void testCarculateCardsDDang() { //땡체크
        winnerChecker = new WinnerChecker(October1+October2, September1+September2, August2+August1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(jangDDang));
        assertThat(Player2, is(nineDDang));
        assertThat(Player3, is(eightDDang));
        assertThat(Winner, is("player1"));
    }

    @Test
    public void testCarculateCardsSJJ() { //세륙, 장사, 장삥
        winnerChecker = new WinnerChecker(June1+April2, April1+October2, October1+January1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(seryuk));
        assertThat(Player2, is(jangsa));
        assertThat(Player3, is(jangping));
        assertThat(Winner, is("player3"));
    }

    @Test
    public void testCarculateCardsGDA() { //구삥, 독사, 알리

        winnerChecker = new WinnerChecker(September1+January2, April1+January2, February1+January1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(guping));
        assertThat(Player2, is(doksa));
        assertThat(Player3, is(alli));
        assertThat(Winner, is("player3"));
    }

    @Test
    public void testCarculateCards38GDD() { //38광땡 vs 멍텅구리94

        winnerChecker = new WinnerChecker(August1+March1, May1+May2, September1+April1);


        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(gwangDDAng38));
        assertThat(Player2, is(fiveDDang));
        assertThat(Player3, is(rematchDumbful));
        assertThat(Winner, is("player1"));
    }

    @Test
    public void testCarculateCards13GDD() { //13광땡 vs 멍텅구리94

        winnerChecker = new WinnerChecker(May1+May2, January1+March1, September1+April1);


        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(fiveDDang));
        assertThat(Player2, is(gwangDDang13));
        assertThat(Player3, is(rematchDumbful));
        assertThat(Winner, is("rematch"));
    }

    @Test
    public void testCarculateCardsDDjab() { //땡잡이
        winnerChecker = new WinnerChecker(September1+January2, September1+September2, March1+July1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(guping));
        assertThat(Player2, is(nineDDang));
        assertThat(Player3, is(DDangCatcher));
        assertThat(Winner, is("player3"));
    }

    @Test
    public void testCarculateCardsDDjabGwang() { //땡잡이 vs 광땡
        winnerChecker = new WinnerChecker(January1+August1, September1+September2, March1+July1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(gwangDDAng18));
        assertThat(Player2, is(nineDDang));
        assertThat(Player3, is(DDangCatcher));
        assertThat(Winner, is("player3"));
    }

    @Test
    public void testCarculateCardsRematch() { //광땡 vs 재경기
        winnerChecker = new WinnerChecker(January1+March1, September2+April1, July1+July2);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(gwangDDang13));
        assertThat(Player2, is(rematch));
        assertThat(Player3, is(sevenDDang));
        assertThat(Winner, is("player1"));
    }

    @Test
    public void testCarculateCardsNoGwangCatcher() { //암행어사 X
        winnerChecker = new WinnerChecker(April1+July1, August1+August2, October2+January1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(GwangCatcher));
        assertThat(Player2, is(eightDDang));
        assertThat(Player3, is(jangping));
        assertThat(Winner, is("player2"));
    }

    @Test
    public void testCarculateCardsGwangCatcher() { //암행어사
        winnerChecker = new WinnerChecker(April1+July1, January1+August1, October2+January1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(GwangCatcher));
        assertThat(Player2, is(gwangDDAng18));
        assertThat(Player3, is(jangping));
        assertThat(Winner, is("player1"));
    }

    @Test
    public void testCarculateCards38GwangCatcher() { //38광땡 vs 암행어사
        winnerChecker = new WinnerChecker(April1+July1, February1+February2, March1+August1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(GwangCatcher));
        assertThat(Player2, is(twoDDang));
        assertThat(Player3, is(gwangDDAng38));
        assertThat(Winner, is("player3"));
    }

    @Test
    public void testCarculateCardsNoDDangCatcher() {//땡잡이 X
        winnerChecker = new WinnerChecker(April2+July2, August1+February2, March1+July1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(onegguk));
        assertThat(Player2, is(mangtong));
        assertThat(Player3, is(DDangCatcher));
        assertThat(Winner, is("player1"));
    }
}