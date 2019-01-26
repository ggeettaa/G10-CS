package spaceInvaders.Activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import spaceInvaders.SpaceInvadersJuego;

public class MayorActivityRebotes extends Activity {

    SpaceInvadersJuego spaceInvadersJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Resolución de la pantalla guardada en un Point
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        //La vista será la del juego
        spaceInvadersJuego = new SpaceInvadersJuego(this, point, true, true, this, getIntent().getExtras().getString("nombre"));
        setContentView(spaceInvadersJuego);
    }

    @Override
    protected void onPause(){
        super.onPause();
        spaceInvadersJuego.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        spaceInvadersJuego.resume();
    }

    @Override
    protected void onStop(){
        super.onStop();
        //spaceInvadersJuego.stop();
    }




}
