package spaceInvaders.ObjetosJuego;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Barrera extends ObjetoVisible {

    private int screenX;
    private int screenY;

    private boolean activo;

    private int id;

    public Barrera(int id, int fila, int columna, int numBloque, int screenX, int screenY){

        this.screenX = screenX;
        this.screenY = screenY;

        int width = screenX / 9;
        int height = screenY / 140;

        int padding = 0;

        int separacionBloques = screenX / 9;
        int startHeight = screenY - (screenY / 3);

        activo = true;

        this.id = id;

        setSize(width - 2 * padding, height - 2 * padding);
        setPosicionInicial((columna * width) + padding + (separacionBloques * numBloque) +
                separacionBloques + (separacionBloques * numBloque), fila * height + padding +
                startHeight);
    }

    @Override
    public void update (){}

    @Override
    public void draw(Canvas canvas, Paint paint){
        if(activo) {
            paint.setColor(Color.argb(255, 150, 165, 176));
            canvas.drawRect(getBoundingRect(), paint);
        }
    }

   /* @Override
    public void reset(){
        super.reset();
        activo = true;
    }*/

    // setters
    public void setInvisible(){
        activo = false;
    }

    @Override
    public boolean activo(){
        return activo;
    }

    public int getId() {
        return id;
    }
}
