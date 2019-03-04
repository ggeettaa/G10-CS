package spaceInvaders.ObjetosJuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.spaceInvaders.android.R;

import java.util.Vector;


public class Botones {

    public enum Result {Pause, Shoot, Nothing}

    public class ButtonItem{
        public Rect rect;
        public Result action;
    }

    // vector de botones
    private Vector<ButtonItem> botones;

    Bitmap bitmapDisparar;
    Bitmap bitmapFlechas;

    // dimensiones de la pantalla
    private int screenX;
    private int screenY;

    // dimensiones
    private int lengthDisparar;
    private int heightDisparar;
    private int lengthFlechas;
    private int heightFlechas;

    // localizacionDisparar del boton de disparar
    Point localizacionDisparar = new Point();
    Rect rectanguloDisparar = new Rect();
    Point localizacionFlechas = new Point();
    Rect rectanguloFlechas = new Rect();

    ButtonItem botonDisparo = new ButtonItem();
    ButtonItem botonFlechas = new ButtonItem();


    public Botones(Context context, int screenX, int screenY, boolean mayor){

        this.screenX = screenX;
        this.screenY = screenY;

        botones = new Vector<>();

        // Flechas
        lengthFlechas = screenX;
        heightFlechas = screenY / 10;

        localizacionFlechas.x = 0;
        localizacionFlechas.y = screenY - (screenY / 8);

        rectanguloFlechas.set(localizacionFlechas.x, localizacionFlechas.y, localizacionFlechas.x + lengthFlechas,
                localizacionFlechas.y + heightFlechas);

        bitmapFlechas = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrows);

        bitmapFlechas = Bitmap.createScaledBitmap(bitmapFlechas, lengthFlechas, heightFlechas, false);

        botonFlechas.rect = new Rect(rectanguloFlechas);
        botonDisparo.action = Result.Nothing;

        // Boton disparar
        lengthDisparar = screenX / 3;
        heightDisparar = screenY / 10;

        if (mayor) {
            localizacionDisparar.x = screenX / 3;
            localizacionDisparar.y = screenY - (screenY / 8);
        } else {
            localizacionDisparar.x = 2 * screenX;
            localizacionDisparar.y = 2 * screenY;
        }

        rectanguloDisparar.set(localizacionDisparar.x, localizacionDisparar.y, localizacionDisparar.x + lengthDisparar,
                localizacionDisparar.y + heightDisparar);

        bitmapDisparar = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot);

        bitmapDisparar = Bitmap.createScaledBitmap(bitmapDisparar, lengthDisparar, heightDisparar, false);

        // set the botones
        botonDisparo.rect = new Rect(rectanguloDisparar);
        botonDisparo.action = Result.Shoot;

        botones.addElement(botonDisparo);
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bitmapFlechas, localizacionFlechas.x, localizacionFlechas.y, paint);
        canvas.drawBitmap(bitmapDisparar, localizacionDisparar.x, localizacionDisparar.y, paint);
    }

    public Vector getBotones(){
        return botones;
    }


}
