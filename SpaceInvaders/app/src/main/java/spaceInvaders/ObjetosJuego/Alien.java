package spaceInvaders.ObjetosJuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;

import com.spaceInvaders.android.R;
import spaceInvaders.SpaceInvadersJuego;


public class Alien extends ObjetoVisible {

    public enum Estado {Izquierda, Derecha}

    private Bitmap bitmap;
    private Paint alienPaint;

    // Dimensiones de la pantalla
    private int screenX;
    private int screenY;

    private Estado estado;
    private boolean activo;

    private float velocidad;

    private int columna;
    private int padding;

    private int length;
    private int height;
    private String color;
    private boolean colorRandom;

    private boolean mayor;
    private Context context;

    private final int prob = 3000; //Probabilidad de disparo random


    public Alien(int fila, int columna, int screenX, int screenY){
        this.columna = columna;
        this.padding = screenX / 20;

        this.screenX = screenX;
        this.screenY = screenY;

        length = screenX / 15;
        height = screenY / 28;

        float x = columna * (length + padding);
        float y = fila * (length + padding / 4);

        setPosicionInicial(x, y);
    }

    public Alien(Context context, int fila, int columna, int screenX, int screenY, SpaceInvadersJuego sij, boolean mayor, float velocidad){

        this.mayor = mayor;
        this.context = context;
        this.columna = columna;
        this.padding = screenX / 20;

        this.spaceInvadersJuego = sij;

        this.screenX = screenX;
        this.screenY = screenY;

        length = screenX / 15;
        height = screenY / 28;

        float x = columna * (length + padding);
        float y = fila * (length + padding / 4);

        setSize(length, height);
        setPosicionInicial(x, y);

        activo = true;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);

        alienPaint = new Paint();
        alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.verde), PorterDuff.Mode.SRC_IN));


        color = "Verde";
        colorRandom = false;

        this.velocidad = (velocidad / 50);

        estado = Estado.Derecha;

    }

    @Override //ObjetoVisible
    public void update (){
        if (activo) {

            PointF loc = getPosition();
            if (estado == Estado.Izquierda) {
                loc.x = loc.x - velocidad;
            } else if (estado == Estado.Derecha) {
                loc.x = loc.x + velocidad;
            }

            setPosition(loc.x, loc.y);
            loc = getPosition();

            Nave nave = (Nave) spaceInvadersJuego.getControladorObjetos().get("nave");

            //teletransporteRandom
            comingCloser();

            // disparo random
            if ((this.mayor) && (((int) (Math.random() * prob)) == ((int) (Math.random() * prob))) && (activo)) {
                spaceInvadersJuego.disparar(loc.x + (getLength() / 2), loc.y, false);
            }

            // aliens llegan abajo
            if (this.activo) {
                for (int i = 0; i < spaceInvadersJuego.getNumBarrera(); i++) {
                    Barrera barrera = (Barrera) spaceInvadersJuego.getControladorObjetos().get("barrera" + i);
                    if (barrera.activo()) {
                        if (RectF.intersects(barrera.getBoundingRect(), getBoundingRect())) {
                            barrera.setInvisible();
                        }
                    }
                }
                if (loc.y > (screenY - ((screenY / 34) * 10))) {
                    // spaceInvadersJuego.reiniciar();
                    spaceInvadersJuego.mostrarPuntuacionFin();

                }
            }

            // Borde de la pantalla
            if ((loc.x > (screenX - ((getLength() + padding) * (6 - columna)))) || (loc.x < ((getLength() + padding) * columna))) {
                mediaVuelta();
            }
        }

    }

    @Override
    public void draw (Canvas canvas, Paint paint){
        if (activo){
            PointF loc = getPosition();
            canvas.drawBitmap(bitmap, loc.x, loc.y, alienPaint);
        }
    }

    public void comingCloser(){
        if (((int) (Math.random() * prob)) == ((int) (Math.random() * prob))){
            int random = (int) (Math.random() * screenX);
            while ((Math.abs(padding - random)) < (screenX / 3)) {
                random = (int) (Math.random() * screenX);
            }
           setPosition(getPosition().x ,getPosition().y +(getHeight()*2));
        }
    }

    public void mediaVuelta(){
        if (estado == Estado.Izquierda){
            estado = Estado.Derecha;
            spaceInvadersJuego.generarAlienEspecial();
        } else {
            estado = Estado.Izquierda;
        }
        PointF loc = getPosition();
        loc.y = loc.y + getHeight();
        setPosition(loc.x, loc.y);
    }

    public void setInvisible(){
        this.spaceInvadersJuego.alienMuere(false);
        activo = false;
    }

    @Override
    public boolean activo(){
        return activo;
    }

    public void cambiarColor(){
        if (colorRandom) {
            colorRandom = false;
            alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.verde), PorterDuff.Mode.SRC_IN));
            color="Verde";
        } else {
            switch (this.color) {
                case "Verde":
                    alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.morado), PorterDuff.Mode.SRC_IN));
                    color = "Morado";
                    break;
                case "Morado":
                    alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.amarillo), PorterDuff.Mode.SRC_IN));
                    color = "Amarillo";
                    break;
                case "Amarillo":
                    alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.azul), PorterDuff.Mode.SRC_IN));
                    color = "Azul";
                    break;
                case "Azul":
                    alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.rosa), PorterDuff.Mode.SRC_IN));
                    color = "Rosa";
                    break;
                case "Rosa":
                    alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.blanco), PorterDuff.Mode.SRC_IN));
                    color = "Blanco";
                    break;
                case "Blanco":
                    alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.naranja), PorterDuff.Mode.SRC_IN));
                    color = "Naranja";
                    break;
                case "Naranja":
                    alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.verde), PorterDuff.Mode.SRC_IN));
                    color = "Verde";
                    break;
            }
        }
    }

    public void cambiarColorRandom(){
        colorRandom = true;
        int c = (int)(Math.random() * 7);
        switch (c){
            case 0:
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.morado), PorterDuff.Mode.SRC_IN));
                color="Morado";
                break;
            case 1:
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.amarillo), PorterDuff.Mode.SRC_IN));
                color="Amarillo";
                break;
            case 2:
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.azul), PorterDuff.Mode.SRC_IN));
                color="Azul";
                break;
            case 3:
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.rosa), PorterDuff.Mode.SRC_IN));
                color="Rosa";
                break;
            case 4:
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.blanco), PorterDuff.Mode.SRC_IN));
                color="Blanco";
                break;
            case 5:
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.naranja), PorterDuff.Mode.SRC_IN));
                color="Naranja";
                break;
            default:
                alienPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.verde), PorterDuff.Mode.SRC_IN));
                color="Verde";
                break;
        }
    }


}
