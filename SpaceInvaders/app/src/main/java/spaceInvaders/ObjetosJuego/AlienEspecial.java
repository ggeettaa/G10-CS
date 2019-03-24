package spaceInvaders.ObjetosJuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.spaceInvaders.android.R;
import spaceInvaders.SpaceInvadersJuego;

public class AlienEspecial extends ObjetoVisible {
    Bitmap bitmap;

    // Dimensiones de la pantalla
    private int screenX;
    private int screenY;

    private Alien.Estado estado;
    private boolean activo;

    private float velocidad;

    private int xInicial;
    private int y;

    private int length;
    private int height;

    private Context context;

    private SpaceInvadersJuego spaceInvadersJuego;

    private boolean mayor;

    private final int prob = 90; //Probabilidad de disparo random

    public AlienEspecial(Context context,int screenX, int screenY, SpaceInvadersJuego sij, float velocidad, boolean mayor){
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;

        length = screenX / 12;
        height = screenY / 28;

        this.mayor = mayor;

        xInicial = length * (-1);
        y = (height / 2);

        setSize(length, height);
        setPosicionInicial(xInicial, y);

        setSize(length, height);
        setPosicionInicial(xInicial, y);

        activo = false;

        spaceInvadersJuego = sij;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien10);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);

        this.velocidad = (velocidad / 20);

    }

    public void regenerar(){
        this.activo = true;
        setPosicionInicial(xInicial, y);
    }

    @Override
    public void update() {
        if(this.activo){

            PointF loc = getPosition();

            // disparo random
            if ((this.mayor) && (((int) (Math.random() * prob)) == ((int) (Math.random() * prob))) && (activo)) {
                spaceInvadersJuego.disparar(loc.x + (getLength() / 2), loc.y, false);
            }

            loc.x = loc.x + velocidad;
            this.setPosition(loc.x, loc.y);
            if (loc.x > (screenX + length)){
                this.activo = false;
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (activo) {
            PointF loc = getPosition();
            canvas.drawBitmap(bitmap, loc.x, loc.y, paint);
        }
    }

    @Override
    public boolean activo(){
        return activo;
    }

    public void setInvisible(){
        this.spaceInvadersJuego.alienMuere(true);
        activo = false;
    }


}
