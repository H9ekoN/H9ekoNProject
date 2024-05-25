package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Timer;

public class Star {
    private int time, index;
    public float radius;
    public float x, y, vx = 0, vy = 0;
    public boolean state = true;
    public boolean state2 = false;
    public float tx[] = new float[20];
    public float ty[] = new float[20];
    public float maxSpeed = 50;
    public float s = 1.5f;

    public Star(int index, float x, float y, float radius){
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.index = index;
        for (int i = 0; i <=9; i++){
            tx[i] = x;
            ty[i] = y;
        }
    }

    public void drawAttack(ShapeRenderer shapeRenderer, MyScreen myScreen) {
        if (state == true) {
            for (int i = 9; i > 0; i--) {
                shapeRenderer.setColor(134 / 255f, 117 / 255f, 81 / 255f, 1);
                shapeRenderer.rectLine(tx[i] * MyScreen.UNIT_SCALE, ty[i] * MyScreen.UNIT_SCALE, tx[i - 1] * MyScreen.UNIT_SCALE, ty[i - 1] * MyScreen.UNIT_SCALE, ((20 - i)) * MyScreen.UNIT_SCALE * 2);
            }
            shapeRenderer.setColor(242 / 255f, 212 / 255f, 85 / 255f, 1);
            shapeRenderer.rect((x - 15 * s) * MyScreen.UNIT_SCALE, (y - 20 * s) * MyScreen.UNIT_SCALE, (30) * MyScreen.UNIT_SCALE * s, (40) * MyScreen.UNIT_SCALE * s);
            shapeRenderer.rect((x - 20 * s) * MyScreen.UNIT_SCALE, (y - 15 * s) * MyScreen.UNIT_SCALE, (40) * MyScreen.UNIT_SCALE * s, (30) * MyScreen.UNIT_SCALE * s);
            shapeRenderer.rect((x - 4 * s) * MyScreen.UNIT_SCALE, (y - 36 * s) * MyScreen.UNIT_SCALE, (8) * MyScreen.UNIT_SCALE * s, (72) * MyScreen.UNIT_SCALE * s);
            shapeRenderer.rect((x - 36 * s) * MyScreen.UNIT_SCALE, (y - 4 * s) * MyScreen.UNIT_SCALE, (72) * MyScreen.UNIT_SCALE * s, (8) * MyScreen.UNIT_SCALE * s);
        }
    }
    public void delete(){
        state = false;
    }
    public void math(MyScreen myScreen, Character character){
            if(state == true && myScreen.Mod == 2) {
                if (vx>maxSpeed) vx = maxSpeed;
                if (vy>maxSpeed) vy = maxSpeed;
                if (vx<-maxSpeed) vx = -maxSpeed;
                if (vy<-maxSpeed) vy = -maxSpeed;
                x += vx;
                y += vy;
                vx += (x - character.tx1/MyScreen.UNIT_SCALE) / -700f + myScreen.random.nextInt(3)-1;
                vy += (y - character.ty1/MyScreen.UNIT_SCALE) / -700f + myScreen.random.nextInt(3)-1;
                for (int i = 9; i > 0; i--) {
                    tx[i] = tx[i - 1];
                    ty[i] = ty[i - 1];
                }
                tx[0] = x;
                ty[0] = y;
                if (hit(x, y, radius, myScreen.charfirst.getX()/MyScreen.UNIT_SCALE, myScreen.charfirst.getY()/MyScreen.UNIT_SCALE, myScreen.charfirst.getRADIUS())) {
                    delete();
                    myScreen.charfirst.die();
                }
            }
        if(state == true && myScreen.Mod == 3) {
            if (vx>maxSpeed) vx = maxSpeed;
            if (vy>maxSpeed) vy = maxSpeed;
            if (vx<-maxSpeed) vx = -maxSpeed;
            if (vy<-maxSpeed) vy = -maxSpeed;
            x += vx;
            y += vy;
            vx += (x - myScreen.charfirst.tx1/MyScreen.UNIT_SCALE) / -700f + myScreen.random.nextInt(3)-1;
            vy += (y - myScreen.charfirst.ty1/MyScreen.UNIT_SCALE) / -700f + myScreen.random.nextInt(3)-1;
            for (int i = 9; i > 0; i--) {
                tx[i] = tx[i - 1];
                ty[i] = ty[i - 1];
            }
            tx[0] = x;
            ty[0] = y;
            if (hit(x, y, radius, myScreen.charfirst.getX()/MyScreen.UNIT_SCALE, myScreen.charfirst.getY()/MyScreen.UNIT_SCALE, myScreen.charfirst.getRADIUS())) {
                delete();
                myScreen.charfirst.die();
            }
        }
    }

    public boolean hit(float x1, float y1, float r1, float x2, float y2, float r2) {
        float dx = Math.abs(x1 - x2);
        float dy = Math.abs(y1 - y2);
        return Math.sqrt(dx * dx + dy * dy) <= r1 + r2;
    }
}