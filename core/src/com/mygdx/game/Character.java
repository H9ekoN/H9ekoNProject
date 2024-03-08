package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;


public class Character {
    private Body body;
    private Texture img;
    public BodyDef def;
    public static float RADIUS;

    public Character(float x, float y, float radius, World world, Texture img){
        RADIUS = radius;
        this.img = img;
        body = createRectBody(x, y, (float) (RADIUS * MyScreen.UNIT_SCALE * 1.2), world);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(img, this.body.getPosition().x - RADIUS * MyScreen.UNIT_SCALE, (float) (body.getPosition().y - RADIUS * MyScreen.UNIT_SCALE + MyScreen.UNIT_SCALE*0.6),
                RADIUS * MyScreen.UNIT_SCALE * 2, (float) (2.5 * RADIUS * MyScreen.UNIT_SCALE));
    }

    public void addForce(Vector2 v){
        body.applyForceToCenter(v, true);
    }

    public void setVelocity(float x, float y){
        body.setLinearVelocity(x, y);
    }

    private Body createRectBody(float x, float y, float radius,  World world){
        def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        Body body = world.createBody(def);

        CircleShape circle = new CircleShape();
        circle.setPosition(new Vector2(0,0));
        circle.setRadius(radius);
        Fixture f = body.createFixture(circle, 1f);
        circle.dispose();

        body.setTransform(x,y,0);

        return body;
    }

}
