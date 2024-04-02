package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Timer;
import java.util.TimerTask;

public class MobsAtack {
    private Vector2 vector2;
    private int time;
    private Timer timer= new Timer();
    private float x, y;
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            drawAfterAttack(new SpriteBatch());
        }
    };

    Texture img, imgAfterAtack;
    public MobsAtack(int atack,float x, float y, Texture imgAtack, Texture imgAfterAtack){
        this.x = x;
        this.y = y;
        this.img = imgAtack;
        this.imgAfterAtack = imgAfterAtack;
        this.time = atack;
    }

    public void drawAttack(SpriteBatch batch){
     batch.draw(img, x, y);
     timer.schedule(task, time);
    }
    public void  drawAfterAttack(SpriteBatch batch){
        batch.draw(imgAfterAtack, x,y);
    }
}
