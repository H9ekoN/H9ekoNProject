package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.gameui.JoystickArea;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

public class MyScreen implements Screen {
    private World world;
    private TiledMap map;
    public static OrthographicCamera camera = new OrthographicCamera();
    private OrthogonalTiledMapRenderer renderer;
    public static final float UNIT_SCALE = 1f / 16f;
    private Box2DDebugRenderer box2DDebugRenderer;
    public Texture imgfirst, imgsecond, StarAttack;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage = new Stage();
    private Table table;
    private Skin skin;
    private TextureAtlas atlas;
    public Character charfirst, charsecond;
    private Mobs mobsfirst;
    private JoystickArea joystickAreafirst, joysticlAreaSecond;
    private BodyDef groundBodyDef = new BodyDef();
    private RectangleForMyGame bord1, bord2, bord3, bord4, line;
    private MobsAtack[] Attack = new MobsAtack[100];
    public float state = 0;
    public int Bossid = 0;
    int Bossstate = 0;
    int Time;



    public MyScreen() {
        box2DDebugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();

        imgfirst = new Texture("MasterFish.png");
        imgsecond = new Texture("monster1stay1.png");
        StarAttack = new Texture("Star.png");

        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE);
        camera.setToOrtho(false, 100, 100);

        world = new World(new Vector2(0, 0), true);

        world.setContactListener(new MyContactListener());


        charfirst = new Character("Character1", 250 * UNIT_SCALE, 900 * UNIT_SCALE, 80, world, imgfirst);
        charsecond = new Character("Character2", 1400 * UNIT_SCALE, 900 * UNIT_SCALE, 80, world, imgfirst);
        mobsfirst = new Mobs("mob", 784 * UNIT_SCALE, 1280 * UNIT_SCALE, 150, world, imgsecond);

        bord1 = new RectangleForMyGame(-1 * UNIT_SCALE, -1 * UNIT_SCALE, 3000 * UNIT_SCALE, 1 * UNIT_SCALE, 1, world, null);
        bord2 = new RectangleForMyGame(-5 * UNIT_SCALE, 2 * UNIT_SCALE, 1 * UNIT_SCALE, 3000 * UNIT_SCALE, 1, world, null);
        bord3 = new RectangleForMyGame(1600 * UNIT_SCALE, 2 * UNIT_SCALE, 1 * UNIT_SCALE, 3000 * UNIT_SCALE, 1, world, null);
        bord4 = new RectangleForMyGame(-7 * UNIT_SCALE, 1500 * UNIT_SCALE, 3000 * UNIT_SCALE, 1 * UNIT_SCALE, 0, world, null);

        line = new RectangleForMyGame(0, 1100 * UNIT_SCALE, 3300 * UNIT_SCALE, 20 * UNIT_SCALE, 0, world, new Texture("whiteline.png"));

        Texture circle = new Texture("JoyStick/circle.png");
        Texture curCircle = new Texture("JoyStick/stick.png");
        joystickAreafirst = new JoystickArea(circle, curCircle, 10, 10);
        joysticlAreaSecond = new JoystickArea(circle, curCircle, 1700, 10);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(table);
        stage.addActor(joystickAreafirst);
        stage.addActor(joysticlAreaSecond);


        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (joystickAreafirst.isTouchStick()) {
            float x = joystickAreafirst.getValueX() * 30;
            float y = joystickAreafirst.getValueY() * 30;
            if (charfirst.getLive()) {
                charfirst.setVelocity(x, y);
            }
        }

        if (joystickAreafirst.isTouchStick() == false) {
            charfirst.setVelocity(0, 0);
        }

        if (joysticlAreaSecond.isTouchStick()) {
            float x = joysticlAreaSecond.getValueX() * 30;
            float y = joysticlAreaSecond.getValueY() * 30;
            if (charsecond.getLive()) {
                charsecond.setVelocity(x, y);
            }
        }

        if (joysticlAreaSecond.isTouchStick() == false) {
            charsecond.setVelocity(0, 0);
        }

        camera.update();
        renderer.setView(camera);
        renderer.render();

        world.step(delta, 4, 4);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mobsfirst.draw(batch);

        charfirst.draw(batch);
        charsecond.draw(batch);
        state += 0.1f;

        if(Attack[(int) state] == null) {
            Attack[(int) state] = new MobsAtack(2000, (float) Math.random() * 1200 * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE,  1400 * UNIT_SCALE, 0, -200 * UNIT_SCALE, 30 * UNIT_SCALE, 30 * UNIT_SCALE, StarAttack, world);

        }

        state = Math.min(state, 99);

        for(int i = 0; i<(int) 100; i++){
            if(Attack[i]!=null && Attack[i].state &&  Attack[i].body!=null) Attack[i].drawAttack(batch);
        }
        batch.end();

        box2DDebugRenderer.render(world, camera.combined);

        stage.act(delta);
        stage.draw();
        for(int i = 0; i<(int) 100; i++){
            if(Attack[i] != null && Attack[i].state == false  && Attack[i].body!=null && Attack[i].state2 == false){
                    world.destroyBody(Attack[i].body);
                    Attack[i].state2 = true;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        world.dispose();
        imgfirst.dispose();
        imgsecond.dispose();
        batch.dispose();
    }
}
