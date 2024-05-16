package com.mygdx.game.gameui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class JoyStick extends Actor {
    private Texture circle, curStick;
    private boolean isTouchStick = false;
    private float rad = 275;
    private static final float currad = 175;
    private float curX = 0;
    private float curY = 0;
    private float inverseRad;
    private float valueX = 0;
    private float valueY = 0;
    public void setCenterPosition(float x, float y){
        setPosition(x - rad, y - rad);
    }
    public float getValueX(){
        return valueX;
    }
    public float getValueY(){
        return valueY;
    }
    public boolean isTouchStick(){
        return isTouchStick;
    }
    private List<JoystickChangedListener> listeners = new ArrayList<>();
    public void addJoystickChangedListeners(){listeners.clear();}
    public void removeJoystickChangedListener(JoystickChangedListener listener){listeners.remove(listener);}
    public void clearJoystickChangedListener(JoystickChangedListener listener){
        listeners.clear();
    }
    public void handleChangedListener(){
        for(JoystickChangedListener listener: listeners){
            listener.changed(valueX,valueY);
        }
    }

    public JoyStick(Texture circle, Texture curStick){
        this.circle = circle;
        this.curStick = curStick;
        setDefaultWH();
        setDefaultXY();
        addListener(new JoyStickInput(this));
    }
    public void setDefaultWH(){
        rad = 205;
        setWidth(rad*2);
        setHeight(rad*2);
    }

    public void setWidth(float w){
        super.setWidth(w);
        super.setHeight(w);
        rad = w/2;
        inverseRad = 1 / rad;
    }
    public void setHeight(float h) {
        super.setWidth(h);
        super.setHeight(h);
        rad = h/2;
        inverseRad = 1/rad;
    }

    public void setrad(float rad){
        rad/=2;
        setWidth(rad*2);
        setHeight(rad*2);
    }

    public void setUNtouch(){
        isTouchStick = false;
    }
    public void settouch(){
        isTouchStick = true;
    }

    public void resetCur() {
        curX = 0;
        curY = 0;
    }

    public void setDefaultXY(){
        setX(30);
        setY(30);
    }
    public void changeCursor(float x, float y){
        float dx= x - rad;
        float dy= y - rad;
        float length = (float)Math.sqrt(dx * dx + dy*dy);
        if (length <= rad) {
            this.curX = dx;
            this.curY = dy;
        } else {
            float k  = rad / length;
            this.curX = dx * k;
            this.curY = dy * k;
        }
        valueX = curX * inverseRad;
        valueY = curY * inverseRad;
    }

    public Actor hit(float x, float y, boolean isTouchStick){
        Actor actor = super.hit(x,y,isTouchStick);
        if (actor == null) return null;
        else {
            float dx = x - rad;
            float dy = y - rad;
            return (dx * dx + dy *dy <= rad*rad)? this:null;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(circle, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if(isTouchStick){
            batch.draw(curStick, this.getX() + rad - currad + curX, this.getY() + rad - currad + curY, 2*currad, 2*currad);
        } else {
            batch.draw(curStick, this.getX() + rad - currad, this.getY() + rad - currad, 2*currad, 2*currad);

        }
    }
}