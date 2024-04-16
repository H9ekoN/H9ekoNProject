package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Timer;
import java.util.TimerTask;

public class MobsAtack {
    private Vector2 vector2;
    private int time, index;
    private float width, radius, height;
    private Timer timer= new Timer();
    public Body body;
    public BodyDef def;
    private float x, y;
    private World world;
    public boolean state = true;
    public boolean state2 = false;

    Texture img, imgAfterAtack;
    private float bodyX, bodyY;

    public MobsAtack(int time, float x, float y, float width, float height,float bodyX, float bodyY, Texture imgAtack, World world){
        this.x = x;
        this.y = y;
        this.img = imgAtack;
        this.time = time;
        this.index = index;
        this.width = width;
        this.height = height;
        this.world = world;
        this.bodyX = bodyX;
        this.bodyY = bodyY;
        body = createRectBody(x, y, width, height, imgAtack, world);
    }

    public MobsAtack(int time,float x, float y, float radius, float bodyX, float bodyY, Texture imgAtack, World world){
        this.x = x;
        this.y = y;
        this.img = imgAtack;
        this.time = time;
        this.index = index;
        this.radius = radius;
        this.world = world;
        body = createCircleBody(x, y, radius, imgAtack, world);
    }
    public Body createCircleBody(float x, float y,float radius, Texture imgAtack, World world){
        def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        body = world.createBody(def);

        CircleShape circle = new CircleShape();
        circle.setPosition(new Vector2(0, 0));
        circle.setRadius(radius);
        Fixture f = body.createFixture(circle, 1f);
        circle.dispose();

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;

        body.setTransform(x, y, 0);
        body.createFixture(fixtureDef).setUserData(this);
        return body;
    }
    public Body createRectBody(float x, float y, float width, float height, Texture imgAtack, World world){
        def = new BodyDef();
        def.fixedRotation = true;
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;

        this.body = world.createBody(def);
        this.body.createFixture(fixtureDef).setUserData(this);
        return body;
    }


    public void drawAttack(SpriteBatch batch, MyScreen myScreen){
        Thread task = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(state)body.setLinearVelocity((myScreen.charfirst.getX() - body.getPosition().x)/10, (myScreen.charfirst.getY() - body.getPosition().y)/10);
            }
        };
     batch.draw(img, this.body.getPosition().x - width, this.body.getPosition().y - height, (float)(width * 2), height * 2);
     // timer.schedule(task, time);
        task.start();
    }
    public void delete(){
        state = false;
    }
}