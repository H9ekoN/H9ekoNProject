package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class RectangleForMyGame {
    private Body body;
    private Texture img;
    public BodyDef def;
    private float Xpos;
    private float Ypos;
    float width;
    float height;
    public int material;

    public RectangleForMyGame(float x, float y, float width, float height, int material, World world, Texture img) {
        this.img = img;
        this.width = width;
        this.height = height;
        body = createRectangularBody(x, y, width, height, material, world);
        Xpos = body.getPosition().x;
        Ypos = body.getPosition().y;
        this.material = material;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(img, this.body.getPosition().x - width * MyScreen.UNIT_SCALE, (float) (body.getPosition().y - height * MyScreen.UNIT_SCALE + MyScreen.UNIT_SCALE * 0.6),
                width * MyScreen.UNIT_SCALE * 2, (float) (2.5 * height * MyScreen.UNIT_SCALE));
    }

    public void setVelocity(float x, float y) {
        body.setLinearVelocity(x, y);
    }

    private Body createRectangularBody(float posx, float posy, float width, float height, int material, World world) {
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = true;

        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width / 2, height / 2);
        boxBody.createFixture(poly, material).setUserData(this);
        poly.dispose();

        return boxBody;
    }
    public float getY(){
        return Ypos;
    }
}
