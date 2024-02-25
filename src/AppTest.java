
import server.BacarratEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class AppTest {
    /**
     * Cristiano Test SUI
     */

    @Test
    public void login(){
        String[] loginDetails = {"login" , "Charan", "100"};
        String result = BacarratEngine.login(loginDetails);
        assertEquals("Login Successful", result);
    }

    @Test
    public void bankerResult(){
        List<Double> shuffledCards = BacarratEngine.shuffle(4);
        assertNotNull(shuffledCards);
    }
}