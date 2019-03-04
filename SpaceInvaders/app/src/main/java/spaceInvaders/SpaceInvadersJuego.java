package spaceInvaders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.spaceInvaders.android.R;

import spaceInvaders.ObjetosJuego.Alien;
import spaceInvaders.ObjetosJuego.AlienEspecial;
import spaceInvaders.ObjetosJuego.Barrera;
import spaceInvaders.ObjetosJuego.Botones;
import spaceInvaders.ObjetosJuego.Nave;
import spaceInvaders.Activities.PuntuacionActivity;
import spaceInvaders.ObjetosJuego.Disparo;

import java.util.ArrayList;
import java.util.Iterator;

import static android.app.Activity.RESULT_OK;

public class SpaceInvadersJuego extends SurfaceView implements Runnable {

    private static final int REQUEST_PHOTO_CODE = 13;

    private boolean jugando;

    private Context context;

    private Thread hilo;

    private int screenX;
    private int screenY;

    private final float velocidadGlobal = 80.0f;

    private ControladorObjetos controladorObjetos;

    private SurfaceHolder ourHolder;
    private Canvas canvas;
    private Paint paint;

    private Activity activity;

    private Botones botones;

    private Nave nave;

    private int numDisparo; // id del proximo disparo

    private ArrayList<Alien> aliens = new ArrayList<>();
    private int numAliens;
    private int aliensActivos;

    private AlienEspecial alienEspecial;

    private int filasBarrera;
    private int columnasBarrera;
    private Barrera[] barreras;
    private int numBarrera;

    private boolean mayor;
    private boolean rebotes;

    private String nombre;
    private int puntuacion;

    public int ultimoChoqueBarrera;

    public ArrayList<Alien> getAliens() {
        return aliens;
    }

    private MediaPlayer musica;

    public SpaceInvadersJuego(Context context){
        super(context);
        this.puntuacion = 0;
    }

    public SpaceInvadersJuego(Context context, Point point, boolean mayor, boolean rebotes, Activity activity, String nombre) {

        //Constructor de SurfaceView
        super(context);

        this.activity = activity;
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = point.x;
        screenY = point.y;

        controladorObjetos = new ControladorObjetos(this);

        botones = new Botones(context, screenX, screenY, mayor);

        this.mayor = mayor;
        this.rebotes = rebotes;

        this.nombre = nombre;
        this.puntuacion = 0;

        this.numDisparo = 0;

        filasBarrera = 8;
        columnasBarrera = 1;
        barreras = new Barrera[4 * filasBarrera * columnasBarrera];

        ultimoChoqueBarrera = 0;

        generarNivel();
    }

    private void generarNivel() {

        nave = new Nave(context, screenX, screenY, velocidadGlobal,this);
        controladorObjetos.add("nave", nave);

        aliensActivos = 0;
        numAliens = 0;
        for (int columna = 0; columna < 6; columna++) {
            for (int fila = 0; fila < 5; fila++) {
                Alien al = new Alien(context, fila, columna, screenX, screenY, this, this.mayor, velocidadGlobal);
                aliens.add(al);
                controladorObjetos.add("alien" + numAliens, al);
                numAliens++;
                aliensActivos++;
            }
        }

        alienEspecial = new AlienEspecial(context,screenX,screenY,this, velocidadGlobal, mayor);
        controladorObjetos.add("alienEspecial", alienEspecial);

        numBarrera = 0;
        for (int numBloque = 0; numBloque < 4; numBloque++) {
            for (int columna = 0; columna < columnasBarrera; columna++) {
                for (int fila = 0; fila < filasBarrera; fila++) {
                    barreras[numBarrera] = new Barrera(numBarrera, fila, columna, numBloque, screenX, screenY);
                    controladorObjetos.add("barrera" + numBarrera, barreras[numBarrera]);
                    numBarrera++;
                }
            }
        }

        musica = MediaPlayer.create(context, R.raw.gamev1);
        musica.start();
        musica.setLooping(true);
    }

    @Override
    public void run() {
        while (jugando) {
            if ((this.aliensActivos == 0) && (!alienEspecial.activo())) {
                this.mostrarPuntuacionFin();
            }
            try {
                ultimoChoqueBarrera++;
                controladorObjetos.updateAll();
                controladorObjetos.drawAll(ourHolder, canvas, paint);
            } catch (Exception e){
                activity.finish();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // El jugador toca la pantalla
            case MotionEvent.ACTION_DOWN:
                float xPos = motionEvent.getX();
                float yPos = motionEvent.getY();
                boolean buttonPress = false;

                Iterator itr = botones.getBotones().iterator();
                while (itr.hasNext()) {
                    Botones.ButtonItem button = (Botones.ButtonItem) itr.next();
                    if (xPos > button.rect.left && xPos < button.rect.right &&
                            yPos > button.rect.top && yPos < button.rect.bottom) {
                        if (button.action == Botones.Result.Shoot) {
                            disparar(nave.getPosition().x + (nave.getLength() / 2), nave.getPosition().y, true);
                            buttonPress = true;
                            break;
                        }
                    }
                }
                //Mover nave
                if (!buttonPress) {
                    realizarMovimiento(motionEvent);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (jugando) {
                    //Parar nave
                    nave.setDirection(Nave.EstadoNave.Parada);
                }
                break;
        }
        return true;
    }

    public void realizarMovimiento(MotionEvent motionEvent){
        if ((motionEvent.getX() < (screenX / 6)) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
            nave.setDirection(Nave.EstadoNave.Izquierda);
        } else if ((motionEvent.getX() > (screenX - (screenX / 6))) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
            nave.setDirection(Nave.EstadoNave.Abajo);
        } else if ((motionEvent.getX() < (screenX / 3)) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
            nave.setDirection(Nave.EstadoNave.Derecha);
        } else if ((motionEvent.getX() > (2 * (screenX / 3))) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
            nave.setDirection(Nave.EstadoNave.Arriba);
        }
    }

    public void drawButtons(Canvas c, Paint p){
        botones.draw(c, p);
    }

    public int getNumAliens() { return numAliens; }

    public int getPuntuacion(){ return this.puntuacion; }

    public void alienMuere(boolean especial) {
        this.puntuacion += 100;
        if (!especial) {
            aliensActivos--;
        }
    }

    public void generarAlienEspecial() {
        alienEspecial.regenerar();
    }

    public void disparar(float posX, float posY, boolean nave) {
        numDisparo++;
        Disparo.Direction d;
        if (nave) {
            d = Disparo.Direction.Arriba;
        } else {
            d = Disparo.Direction.Abajo;
        }
        controladorObjetos.add("disparo" + numDisparo, new Disparo(numDisparo, posX, posY, screenX, screenY, this, d, nave, velocidadGlobal, rebotes));
    }

    public void eliminarDisparo(int id) {
        controladorObjetos.remove("disparo" + id);
    }

    public void mostrarPuntuacionFin() {
        if (this.jugando) {
            musica.stop();
            this.jugando = false;
            Intent intent = new Intent(activity, PuntuacionActivity.class);
            intent.putExtra("puntos", String.valueOf(puntuacion));
            intent.putExtra("nombre", nombre);
            if (mayor) {
                if (rebotes) {
                    intent.putExtra("tipoJuego", "mayorRebotes");
                } else {
                    intent.putExtra("tipoJuego", "mayor");
                }
            } else {
                intent.putExtra("tipoJuego", "menor");
            }
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public ControladorObjetos getControladorObjetos() {
        return controladorObjetos;
    }

    public int getNumBarrera() {
        return numBarrera;
    }

    public void pause() {
        jugando = false;
    }

    public void resume() {
        jugando = true;
        hilo = new Thread(this);
        hilo.start();

    }

    public int getNumDisparo(){
        return this.numDisparo;
    }

    public void setPuntuacion(int num){
        this.puntuacion = num;
    }
}
