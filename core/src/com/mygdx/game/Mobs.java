package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class Mobs {
    private String name;
    private Body body;
    private Texture img;
    public BodyDef def;
    public static float RADIUS;
    private float Xpos;
    private float Ypos;
    private boolean live = true;

    public Mobs(String name, float x, float y, float radius, World world, Texture img) {
        RADIUS = radius;
        this.img = img;
        body = createCircleBody(x, y, (float) (RADIUS * MyScreen.UNIT_SCALE * 1.2), world);
        Xpos = body.getPosition().x;
        Ypos = body.getPosition().y;
        this.name = name;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this.img, this.body.getPosition().x - RADIUS * MyScreen.UNIT_SCALE, (float) (body.getPosition().y - RADIUS * MyScreen.UNIT_SCALE + MyScreen.UNIT_SCALE * 0.6),
                RADIUS * MyScreen.UNIT_SCALE * 2, (float) (2.5 * RADIUS * MyScreen.UNIT_SCALE));
    }

    public void addForce(Vector2 v) {
        body.applyForceToCenter(v, true);
    }

    public void setVelocity(float x, float y) {
        body.setLinearVelocity(x, y);
    }

    private Body createCircleBody(float x, float y, float radius, World world) {
        def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;
        Body body = world.createBody(def);

        CircleShape circle = new CircleShape();
        circle.setPosition(new Vector2(0, 0));
        circle.setRadius(radius);
        Fixture f = body.createFixture(circle, 1f);
        circle.dispose();

        body.setTransform(x, y, 0);

        return body;
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public float getRADIUS() {
        return RADIUS;
    }

    public boolean getLive() {
        return live;
    }

    public float getLength() {
        return (float) (Math.sqrt(getX() * getX() + getY() * getY()));
    }

    public String getName() {
        return name;
    }
}
