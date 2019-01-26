package spaceInvaders.Activities;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import spaceInvaders.SpaceInvadersJuego;

public class MenorActivity extends AppCompatActivity {

    SpaceInvadersJuego spaceInvadersJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Resolución de la pantalla guardada en un Point
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        //La vista será la del juego
        spaceInvadersJuego = new SpaceInvadersJuego(this, point, false, false, this, getIntent().getExtras().getString("nombre"));
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
