package com.example.shutda;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.shutda.view.Ingame.WinnerChecker;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.example.shutda.view.data.DummyCards.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ExampleInstrumentedTest {
    private WinnerChecker winnerChecker;


    @Test
    public void testCarculateCardsDDang() {
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
    public void testCarculateCardsSJJ() {
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
    public void testCarculateCardsGDA() {

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
    public void testCarculateCards38GDD() {

        winnerChecker = new WinnerChecker(August1+March1, January1+March1, September1+April1);


        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(gwangDDAng38));
        assertThat(Player2, is(gwangDDang13));
        assertThat(Player3, is(rematchDumbful));
        assertThat(Winner, is("player1"));
    }

    @Test
    public void testCarculateCardsDDjab() {
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
    public void testCarculateCardsRematch() {
        winnerChecker = new WinnerChecker(January1+March1, September1+April1, July1+July2);

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
    public void testCarculateCardsNoGwangCatcher() {
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
    public void testCarculateCardsGwangCatcher() {
        winnerChecker = new WinnerChecker(April1+July1, January1+August1, October2+January1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(GwangCatcher));
        assertThat(Player2, is(gwangDDAng18));
        assertThat(Player3, is(jangping));
        assertThat(Winner, is("player2"));
    }
}
