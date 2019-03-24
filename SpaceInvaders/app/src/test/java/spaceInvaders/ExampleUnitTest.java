package spaceInvaders;

import android.graphics.Point;

import org.junit.Test;

import spaceInvaders.Activities.MainActivity;
import spaceInvaders.Activities.MayorActivity;
import spaceInvaders.ObjetosJuego.Alien;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void alienDies_isCorrect() {

        MayorActivity mayor = new MayorActivity();
        SpaceInvadersJuego spj = new SpaceInvadersJuego(mayor);
        spj.alienMuere(true);
        assertEquals(100, spj.getPuntuacion());
    }

    @Test
    public void comingCloser_isCorrect(){
        Alien alien1 = new Alien(0,0,0,0);
        alien1.comingCloser();
        assertNotEquals(0.0, alien1.getPosition().y);
    }
}