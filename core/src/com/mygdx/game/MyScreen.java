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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.KryoNet.client.ClientProgram;
import com.mygdx.game.KryoNet.server.ServerProgram;
import com.mygdx.game.gameui.JoystickArea;

import java.util.Random;

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
    public int Mod = 1;
    public Random random = new Random(12);
    Button buttonleft;
    Button buttonright;
    public int Online = 0;
    ClientProgram client;
    ServerProgram server;
    MyScreen myScreen = this;


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

        atlas = new TextureAtlas("ui/button.atlas");
        skin = new Skin();
        skin.addRegions(atlas);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = skin.getDrawable("button.up");
        buttonStyle.down = skin.getDrawable("button.down");
        buttonStyle.pressedOffsetX = 1;
        buttonStyle.checkedOffsetY = -1;

        buttonleft = new Button(buttonStyle);
        buttonright = new Button(buttonStyle);


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
        joystickAreafirst.setVisible(false);
        joysticlAreaSecond.setVisible(false);

        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        stage.addActor(table);
        stage.addActor(joystickAreafirst);
        stage.addActor(joysticlAreaSecond);


        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        if (Mod == 1) {
            table.add(buttonleft).width(300).height(300).pad(800);
            table.add(buttonright).width(300).height(300).pad(800);
            buttonleft.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Mod = 2;
                    Online = 1;
                    try {
                        server = new ServerProgram(myScreen);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    joystickAreafirst.setVisible(true);
                    table.removeActor(buttonright);
                    table.removeActor(buttonleft);
                }
            });
            buttonright.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Mod = 2;
                    Online = 2;
                    try {
                        client = new ClientProgram(myScreen);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    joysticlAreaSecond.setVisible(true);
                    table.removeActor(buttonleft);
                    table.removeActor(buttonright);
                }
            });
        }
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

        if(Attack[(int) state] == null && Mod == 2) {
            float randomX = (float) random.nextInt(1200) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE;
            if(Math.abs(charfirst.getX()) - Math.abs(randomX) < Math.abs(charsecond.getX() - randomX)) {
                Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(1200) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charfirst.getX() * UNIT_SCALE,charfirst.getY() * UNIT_SCALE, StarAttack, world);
            } else {
                Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(1200) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charsecond.getX() * UNIT_SCALE,charsecond.getY() * UNIT_SCALE, StarAttack, world);
            }
        }

        state = Math.min(state, 99);

        for(int i = 0; i<(int) 100; i++){
            if(Attack[i]!=null && Attack[i].state &&  Attack[i].body!=null) Attack[i].drawAttack(batch, this);
        }
        batch.end();
        stage.act(delta);
        stage.draw();
        box2DDebugRenderer.render(world, camera.combined);

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
        StarAttack.dispose();
        stage.dispose();
        batch.dispose();
    }
}
