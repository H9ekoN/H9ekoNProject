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
    public Texture imgfirst, imgsecond, StarAttack, MobsTexture;
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
    Button buttoncenter;
    public int Online = 0;
    ClientProgram clientP;
    public ServerProgram serverP;
    MyScreen myScreen = this;


    public MyScreen() {

        box2DDebugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();

        imgfirst = new Texture("HeartRed.png");
        imgsecond = new Texture("HeartPurple.png");
        MobsTexture = new Texture("monster1stay1.png");

        StarAttack = new Texture("Star.png");

        map = new TmxMapLoader().load("space.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE);
        camera.setToOrtho(false, 200, 150);

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
        buttoncenter = new Button(buttonStyle);


        charfirst = new Character("Character1", 600 * UNIT_SCALE, 900 * UNIT_SCALE, 90, world, imgfirst);
        charsecond = new Character("Character2", 2600 * UNIT_SCALE, 900 * UNIT_SCALE, 90, world, imgsecond);
        mobsfirst = new Mobs("mob", 1600 * UNIT_SCALE, 2100 * UNIT_SCALE, 200, world, MobsTexture);

        bord1 = new RectangleForMyGame(-1 * UNIT_SCALE, -1 * UNIT_SCALE, 5800 * UNIT_SCALE, 1 * UNIT_SCALE, 1, world, null);
        bord2 = new RectangleForMyGame(220 * UNIT_SCALE, 2 * UNIT_SCALE, 1 * UNIT_SCALE, 3500 * UNIT_SCALE, 1, world, null);
        bord3 = new RectangleForMyGame(3000 * UNIT_SCALE, 2 * UNIT_SCALE, 1 * UNIT_SCALE, 3500 * UNIT_SCALE, 1, world, null);
        bord4 = new RectangleForMyGame(-7 * UNIT_SCALE, 2400 * UNIT_SCALE, 5800 * UNIT_SCALE, 1 * UNIT_SCALE, 0, world, null);

        line = new RectangleForMyGame(0, 1800 * UNIT_SCALE, 5800 * UNIT_SCALE, 20 * UNIT_SCALE, 0, world, new Texture("whiteline.png"));

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
            table.add(buttonleft).width(300).height(300).pad(300);
            table.add(buttoncenter).width(300).height(300).pad(300);
            table.add(buttonright).width(300).height(300).pad(300);
            buttoncenter.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Online = 3;
                    world.destroyBody(charsecond.body);
                    charsecond = null;
                    joystickAreafirst.setVisible(true);
                    table.removeActor(buttonright);
                    table.removeActor(buttonleft);
                    table.removeActor(buttoncenter);
                    Mod = 3;

                }
            });
            buttonleft.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Online = 1;
                    Thread thread = new Thread(() -> {
                        try {
                            serverP = new ServerProgram(myScreen);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    });
                    thread.start();
                    joystickAreafirst.setVisible(true);
                    table.removeActor(buttonright);
                    table.removeActor(buttonleft);
                    table.removeActor(buttoncenter);
                }
            });
            buttonright.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Online = 2;
                    Thread thread = new Thread(() -> {
                        try {
                            clientP = new ClientProgram(myScreen);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();
                    joysticlAreaSecond.setVisible(true);
                    table.removeActor(buttonleft);
                    table.removeActor(buttonright);
                    table.removeActor(buttoncenter);
                }
            });
        }
        Thread thread = new Thread(){
            @Override
            public void run() {
                for(;true;){
                    world.step(1, 4, 4);
                    state += 0.1f;
                    state = Math.min(state, 99);
                    if(Attack[(int) state] == null && Mod == 3 && charsecond!=null){
                        float randomX = (float) random.nextInt(2800) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE;
                        if(Math.abs(charfirst.getX()) - Math.abs(randomX) < Math.abs(charsecond.getX() - randomX)) {
                            Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(2800) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charfirst.getX() * UNIT_SCALE,charfirst.getY() * UNIT_SCALE, StarAttack, world);
                        }
                    }
                    if(Attack[(int) state] == null && Mod == 2) {
                        float randomX = (float) random.nextInt(2800) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE;
                        if(Math.abs(charfirst.getX()) - Math.abs(randomX) < Math.abs(charsecond.getX() - randomX)) {
                            Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(2800) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charfirst.getX() * UNIT_SCALE,charfirst.getY() * UNIT_SCALE, StarAttack, world);
                        } else {
                            Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(2800) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charsecond.getX() * UNIT_SCALE,charsecond.getY() * UNIT_SCALE, StarAttack, world);
                        }
                    } else if (Attack[(int) state] == null && Mod == 3) {
                        float randomX = (float) random.nextInt(2800) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE;
                            Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(2800) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charfirst.getX() * UNIT_SCALE,charfirst.getY() * UNIT_SCALE, StarAttack, world);
                    }
                    for(int i = 0; i<(int) 100; i++){
                        if(Attack[i]!=null && Attack[i].state &&  Attack[i].body!=null) {
                            if(Attack[i].state)Attack[i].body.setLinearVelocity((myScreen.charfirst.getX() - Attack[i].body.getPosition().x)/60, (myScreen.charfirst.getY() - Attack[i].body.getPosition().y)/60);
                        }
                    }
                    for(int i = 0; i<(int) 100; i++){
                        if(Attack[i] != null && !Attack[i].state && Attack[i].body!=null && !Attack[i].state2){
                            world.destroyBody(Attack[i].body);
                            Attack[i].state2 = true;
                        }
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (joystickAreafirst.isTouchStick()) {
            float x = joystickAreafirst.getValueX() * 50;
            float y = joystickAreafirst.getValueY() * 50;
            if (charfirst.getLive()) {
                charfirst.setVelocity(x, y);
            }
        }
        if (!joystickAreafirst.isTouchStick()) {
            charfirst.setVelocity(0, 0);
        }

        if (joysticlAreaSecond.isTouchStick()) {
            float x = joysticlAreaSecond.getValueX() * 50;
            float y = joysticlAreaSecond.getValueY() * 50;
            if (charsecond != null) {
                if(charsecond.getLive()) {
                    charsecond.setVelocity(x, y);
                }
            }
        }
        if (charsecond != null) {
            if (!joysticlAreaSecond.isTouchStick()) {
                charsecond.setVelocity(0, 0);
            }
        }
        camera.update();
        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mobsfirst.draw(batch);
        charfirst.draw(batch);

        if (charsecond != null) {
            charsecond.draw(batch);
        }

        for(int i = 0; i<(int) 100; i++){
            if(Attack[i]!=null && Attack[i].state &&  Attack[i].body!=null) Attack[i].drawAttack(batch, this);
        }
        batch.end();
        stage.act(delta);
        stage.draw();
        box2DDebugRenderer.render(world, camera.combined);
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