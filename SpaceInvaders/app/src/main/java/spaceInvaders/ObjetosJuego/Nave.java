package spaceInvaders.ObjetosJuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;

import com.spaceInvaders.android.R;
import spaceInvaders.SpaceInvadersJuego;

public class Nave extends ObjetoVisible {

    // object's bitmapDisparar
    Bitmap bitmap;

    // velocity of object
    private float velocidad;

    public enum EstadoNave {Parada, Izquierda, Derecha, Arriba, Abajo}

    //Estado de la nave
    private EstadoNave estadoNave;

    //Dimensiones de la pantalla
    private int screenX;
    private int screenY;

    private final int prob = 400;

    private boolean activo;

    private MediaPlayer sonido;

    public Nave(Context context, int screenX, int screenY, float velocidad, SpaceInvadersJuego sij) {

        this.spaceInvadersJuego = sij;

        sonido = MediaPlayer.create(context, R.raw.teletransporte);

        activo = true;

        // dimensions of object
        int length;
        int height;

        // position of object
        float x;
        float y;

        this.screenX = screenX;
        this.screenY = screenY;

        length = screenX / 15;
        height = screenX / 18;

        x = ((screenX / 2) - (height / 2));
        y = (screenY - ((screenY / 44) * 10));

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.nave1);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);

        setSize(length, height);
        setPosicionInicial(x, y);

        this.velocidad = (velocidad / 7);

        estadoNave = EstadoNave.Parada;
    }

    @Override
    public void update() {

        //Posicion actual
        PointF posicion = getPosition();

        //Nueva posicion
        if (estadoNave == EstadoNave.Izquierda) {
            posicion.x = posicion.x - velocidad;
        }
        if (estadoNave == EstadoNave.Derecha) {
            posicion.x = posicion.x + velocidad;
        }
        if ((estadoNave == EstadoNave.Abajo) && (posicion.y > 0)) {
            posicion.y = posicion.y - velocidad;
        }
        if ((estadoNave == EstadoNave.Arriba) && (posicion.y < (screenY - (screenY / 9) - (2 * getHeight())))) {
            posicion.y = posicion.y + velocidad;
        }
        if (posicion.x < 0 - getLength()){
            posicion.x = screenX;
        }
        if (posicion.x > screenX){
            posicion.x = 0 - getLength();
        }

        //Teletransporte random
        if (((int) (Math.random() * prob)) == ((int) (Math.random() * prob))){
            float random = (float) (Math.random() * screenX);
            while ((Math.abs(posicion.x - random)) < (screenX / 3)) {
                random = (float) (Math.random() * screenX);
            }
            posicion.x = random;
            posicion.y = (screenY - ((screenY / 44) * 10));
            if (!sonido.isPlaying()) {
                sonido.start();
            }
        }

        //Actualizar posicion
        setPosition(posicion.x, posicion.y);

        //Colisiones
        for (int i = 0; (i < spaceInvadersJuego.getNumAliens()) && activo; i++) {
            Alien alien = (Alien) spaceInvadersJuego.getControladorObjetos().get("alien" + i);
            if (alien.activo()) {
                if (RectF.intersects(alien.getBoundingRect(), getBoundingRect())) {
                    setInvisible();
                    spaceInvadersJuego.mostrarPuntuacionFin();
                }
            }
        }
        AlienEspecial alienEspecial = (AlienEspecial) spaceInvadersJuego.getControladorObjetos().get("alienEspecial");
        if (alienEspecial.activo()) {
            if (RectF.intersects(alienEspecial.getBoundingRect(), getBoundingRect())) {
                setInvisible();
                spaceInvadersJuego.mostrarPuntuacionFin();
            }
        }
        for (int i = 0; (i < spaceInvadersJuego.getNumBarrera()) && activo; i++) {
            Barrera barrera = (Barrera) spaceInvadersJuego.getControladorObjetos().get("barrera" + i);
            if (barrera.activo()) {
                if (RectF.intersects(barrera.getBoundingRect(), getBoundingRect())) {
                    setInvisible();
                    spaceInvadersJuego.mostrarPuntuacionFin();
                }
            }
        }

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (activo) {
            canvas.drawBitmap(bitmap, getPosition().x, getPosition().y, paint);
        }
    }


    public void setDirection(EstadoNave state) {
        estadoNave = state;
    }

    public void setInvisible() {
        activo = false;
    }

    @Override
    public boolean activo() {
        return activo;
    }

}

