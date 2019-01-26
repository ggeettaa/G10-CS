package spaceInvaders.ObjetosJuego;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import spaceInvaders.SpaceInvadersJuego;

public class Disparo extends ObjetoVisible {

    public int id;

    public enum Direction{SinDireccion, Arriba, Abajo}

    public enum Tipo {Nave, Alien, General}

    private boolean activo; // si el disparo esta activo

    private Direction direccion; // direccion del disparo

    private float velocidad;
    private boolean rebotes;
    private float inclinacion;
    private long contadorRebotes;

    private int screenY;
    private int screenX;

    private Tipo tipo;


    public Disparo(int id, float startX, float startY, int screenX, int screenY, SpaceInvadersJuego sij, Direction direccion, boolean nave, float velocidad, boolean rebotes){

        this.id = id;

        if (nave) {
            this.tipo = Tipo.Nave;
        } else {
            this.tipo = Tipo.Alien;
        }

        this.spaceInvadersJuego = sij;

        this.screenX = screenX;
        this.screenY = screenY;
        int height = screenY / 80;
        int length = screenY / 80;
        setSize(length, height);

        this.velocidad = (velocidad / 4);

        this.rebotes = rebotes;
        inclinacion = 0;
        contadorRebotes = 0;

        // get current position
        PointF loc = getPosition();

        // set new position
        loc.x = startX - (length / 2);
        loc.y = startY;
        setPosicionInicial(loc.x, loc.y);
        inclinacion = 0;

        this.direccion = direccion;

        activo = true;
        if ((startX < getLength()) || (startX > (screenX - getLength()))) {
            setInactive();
        }

    }


    @Override
    public void update(){
        if(activo) {
            PointF posicion = getPosition(); //Posicion actual

            //Nueva posicion
            if (direccion == Direction.Arriba) {
                posicion.y -= velocidad;
            } else {
                posicion.y += velocidad;
            }
            if (inclinacion != 0) {
                posicion.x -= (inclinacion);
            }

            //Actualizar posicion
            setPosition(posicion.x, posicion.y);

            //Colision disparo nave - alien
            if ((tipo == Tipo.Nave) || (tipo == Tipo.General)) {
                for (int i = 0; (i < spaceInvadersJuego.getNumAliens()) && activo; i++) {
                    Alien alien = (Alien) spaceInvadersJuego.getControladorObjetos().get("alien" + i);
                    if (alien.activo()) {
                        if (RectF.intersects(alien.getBoundingRect(), getBoundingRect())) {
                            alien.setInvisible();
                            setInactive();
                        }
                    }
                    AlienEspecial alienEspecial = (AlienEspecial) spaceInvadersJuego.getControladorObjetos().get("alienEspecial");
                    if (alienEspecial.activo()) {
                        if (RectF.intersects(alienEspecial.getBoundingRect(), getBoundingRect())) {
                            alienEspecial.setInvisible();
                            setInactive();
                        }
                    }
                }
            }

            //Colision disparo alien - nave
            if ((tipo == Tipo.Alien) || (tipo == Tipo.General)) {
                Nave nave = (Nave) spaceInvadersJuego.getControladorObjetos().get("nave");
                if (RectF.intersects(nave.getBoundingRect(), getBoundingRect())) {
                    setInactive();
                    nave.setInvisible();
                    spaceInvadersJuego.mostrarPuntuacionFin();
                }
            }

            //Colision disparo - barrera
            boolean cambiarColor = false;
            for (int i = 0; (i < spaceInvadersJuego.getNumBarrera()) && activo; i++) {
                Barrera barrera = (Barrera) spaceInvadersJuego.getControladorObjetos().get("barrera" + i);
                if (barrera.activo()) {
                    if (RectF.intersects(barrera.getBoundingRect(), getBoundingRect())) {
                        if (direccion == Direction.Arriba) {
                            try {
                                Barrera barreraAbajo = (Barrera) spaceInvadersJuego.getControladorObjetos().get("barrera" + Integer.toString(i + 1));
                                if (barreraAbajo.activo() && RectF.intersects(barreraAbajo.getBoundingRect(), getBoundingRect())) {
                                    barrera = barreraAbajo;
                                }
                            } catch (Exception e) {}
                        }
                        barrera.setInvisible();
                        setInactive();
                        cambiarColor = true;
                    }
                }
            }
            if (cambiarColor) {
                if (spaceInvadersJuego.ultimoChoqueBarrera < 5) {
                    for (Alien alien : spaceInvadersJuego.getAliens()) {
                        alien.cambiarColorRandom();
                    }
                } else {
                    for (Alien alien : spaceInvadersJuego.getAliens()) {
                        alien.cambiarColor();
                    }
                }
                spaceInvadersJuego.ultimoChoqueBarrera = 0;

            }

            if (rebotes) {
                //Rebote arriba o abajo
                if ((getImpactPointY() - getHeight()) > (screenY - (screenY / 9) - (2 * getHeight()))) {
                    direccion = Direction.Arriba;
                    tipo = Tipo.General;
                    inclinacion = (float) Math.random() * 30 - 15;
                } else if (getImpactPointY() < 0) {
                    direccion = Direction.Abajo;
                    tipo = Tipo.General;
                    inclinacion = (float) Math.random() * 30 - 15;
                }
                //Rebote lateral
                if ((getImpactPointX() > (screenX - getLength())) || (getImpactPointX() < getLength())) {
                    inclinacion = inclinacion * -1;
                }
            } else {
                //Parte superior o inferior de la pantalla
                if (getImpactPointY() < 0 || getImpactPointY() > (screenY - (screenY / 9) - getHeight())) {
                    setInactive();
                }
            }

        }
    }

    @Override
    public void draw (Canvas canvas, Paint paint) {
        if(activo){
            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawRoundRect(getBoundingRect(), (screenX / 100), (screenX / 100), paint);
        }
    }

    public void setInactive(){
        activo = false;
        spaceInvadersJuego.eliminarDisparo(id);
    }

    public float getImpactPointY(){
        if(direccion == Direction.Abajo){
            return getPosition().y + getHeight();
        }
        else{
            return getPosition().y;
        }
    }

    public float getImpactPointX(){
        return getPosition().x + (getLength() / 2);
    }

    @Override
    public boolean activo() {
        return activo;
    }

}
