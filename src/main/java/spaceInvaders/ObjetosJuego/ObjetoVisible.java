package spaceInvaders.ObjetosJuego;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import spaceInvaders.SpaceInvadersJuego;

public class ObjetoVisible {

    private RectF rect = new RectF();

    protected SpaceInvadersJuego spaceInvadersJuego;

    //Tamaño
    private float length;
    private float height;

    //Coordenadas
    private float x;
    private float y;

    //Coordenadas iniciales
    private float xInicial = 0;
    private float yInicial = 0;


    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(rect, paint);
    }

    /*public void reset() {
        setSize(length, height);
        setPosition(initialX, initialY);
    }*/

    public void update() {}

    public boolean activo() {
        return true;
    }

    // setters
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;

        RectF temp = new RectF(x, y, x + length, y + height);
        setBoundingRect(temp);
    }

    public void setPosicionInicial(float x, float y) {
        this.x = x;
        xInicial = x;
        this.y = y;
        yInicial = y;
        RectF temp = new RectF(x, y, x + length, y + height);
        setBoundingRect(temp);
    }

    public void setSize(float length, float height) {
        this.length = length;
        this.height = height;
    }

    public void setBoundingRect(RectF r) {
        rect.left = r.left;
        rect.right = r.right;
        rect.top = r.top;
        rect.bottom = r.bottom;
    }

    // getters
    public PointF getPosition() {
        PointF loc = new PointF();
        loc.x = x;
        loc.y = y;
        return loc;
    }

    public float getHeight() {
        return height;
    }

    public float getLength() {
        return length;
    }

    public RectF getBoundingRect() {
        return rect;
    }

}
