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
        winnerChecker = new WinnerChecker(2000000001, 200000001, 20000001);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(110));
        assertThat(Player2, is(100));
        assertThat(Player3, is(90));
        assertThat(Winner, is("player1"));
    }

    @Test
    public void testCarculateCardsSJJ() {
        winnerChecker = new WinnerChecker(101001, 1000001002, 1000000000);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(10));
        assertThat(Player2, is(11));
        assertThat(Player3, is(12));
        assertThat(Winner, is("player3"));
    }

    @Test
    public void testCarculateCardsGDA() {
        winnerChecker = new WinnerChecker(August2+January2, January1+April2, January1+February2);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(gapou));
        assertThat(Player2, is(doksa));
        assertThat(Player3, is(alli));
        assertThat(Winner, is("player3"));
    }

    @Test
    public void testCarculateCards38GDD() {
        winnerChecker = new WinnerChecker(March1 + August1, March1 + July1, April1 + September1);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(gwangDDAng38));
        assertThat(Player2, is(DDangCatcher));
        assertThat(Player3, is(rematchDumbful));
        assertThat(Winner, is("player1"));
    }

    @Test
    public void testCarculateCardsShin() {
        winnerChecker = new WinnerChecker(August1+September1, April1+April2, -2);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(sevengguk));
        assertThat(Player2, is(fourDDang));
        assertThat(Winner, is("player2"));
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
}
