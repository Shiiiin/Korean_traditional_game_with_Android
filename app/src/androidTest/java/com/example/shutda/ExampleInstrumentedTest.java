package com.example.shutda;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.shutda.view.Ingame.WinnerChecker;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        winnerChecker = new WinnerChecker(100000002, 1001, 10);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(13));
        assertThat(Player2, is(14));
        assertThat(Player3, is(15));
        assertThat(Winner, is("player3"));
    }

    @Test
    public void testCarculateCards38GDD() {
        winnerChecker = new WinnerChecker(10000100, 1000100, 100001001);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(200));
        assertThat(Player2, is(3737));
        assertThat(Player3, is(4949));
        assertThat(Winner, is("player1"));
    }

    @Test
    public void testCarculateCardsDDjab() {
        winnerChecker = new WinnerChecker(100000002, 200000001, 1000101);

        int Player1 = winnerChecker.carculateCards("player1");
        int Player2 = winnerChecker.carculateCards("player2");
        int Player3 = winnerChecker.carculateCards("player3");
        String Winner = winnerChecker.WinnerClassifier();

        assertThat(Player1, is(13));
        assertThat(Player2, is(100));
        assertThat(Player3, is(3737));
        assertThat(Winner, is("player3"));
    }
}
