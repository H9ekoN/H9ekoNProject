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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
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
    private float blasterLineWidthX1, blasterLineWidthX2, blasterLineWidthY1, blasterLineWidthY2;
    public static OrthographicCamera camera = new OrthographicCamera();
    private OrthogonalTiledMapRenderer renderer;
    public static final float UNIT_SCALE = 1f / 16f;
    private Box2DDebugRenderer box2DDebugRenderer;
    public Texture imgfirst, imgsecond, StarAttack, MobsTexture, Blaster;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage = new Stage();
    private Table table;
    private Skin skin;
    private TextureAtlas hostatlas, joinatlas, soloatlas;
    public Character charfirst, charsecond;
    private Mobs mobsfirst;
    private JoystickArea joystickAreafirst, joysticlAreaSecond;
    private BodyDef groundBodyDef = new BodyDef();
    private RectangleForMyGame bord1, bord2, bord3, bord4, line;
    private MobsAtack[] Attack = new MobsAtack[60];
    public float state = 0;
    public float gravitationstate = 0;
    public int Bossid = 0;
    int Bossstate = 0;
    int Time;
    public Vector2 v1 = new Vector2(), v2 = new Vector2();
    public int Mod = 1;
    public ShapeRenderer shapeRenderer = new ShapeRenderer();
    public Random random = new Random();
    private Button buttonleft;
    private Button buttonright;
    private Button buttoncenter;
    public int Online = 0;
    private ClientProgram clientP;
    private TextureRegion RegionBlaster;
    public ServerProgram serverP;
    private MyScreen myScreen = this;
    private int blasterID = 0;
    private BlasterAttack[] blasterAttacks = new BlasterAttack[3];
    private float W;
    private float H;
    public RayCastCallback callback = (fixture, point, normal, fraction) -> {
        if (fixture.getUserData() instanceof Character) {
            Character tbb = (Character) (fixture.getUserData());
            tbb.die();
        }
        return 1;
    };

    public boolean hit(float x1, float y1, float r1, float x2, float y2, float r2) {
        float dx = Math.abs(x1 - x2);
        float dy = Math.abs(y1 - y2);
        return Math.sqrt(dx * dx + dy * dy) <= r1 + r2;
    }

    public MyScreen() {

        box2DDebugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();

        imgfirst = new Texture("HeartRed.png");
        imgsecond = new Texture("HeartPurple.png");
        MobsTexture = new Texture("monster1stay1.png");
        Blaster = new Texture("blaster.png");
        RegionBlaster = new TextureRegion(Blaster);

        StarAttack = new Texture("Star.png");

        map = new TmxMapLoader().load("space.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE);
        camera.setToOrtho(false, 200, 150);

        world = new World(new Vector2(0, 0), true);

        world.setContactListener(new MyContactListener());

        hostatlas = new TextureAtlas("ui/hostbutton.atlas");
        joinatlas = new TextureAtlas("ui/joinbutton.atlas");
        soloatlas = new TextureAtlas("ui/solobutton.atlas");

        skin = new Skin();

        skin.addRegions(hostatlas);
        skin.addRegions(joinatlas);
        skin.addRegions(soloatlas);

        W = Gdx.graphics.getWidth();
        H = Gdx.graphics.getHeight();

        Button.ButtonStyle buttonhostStyle = new Button.ButtonStyle();
        buttonhostStyle.up = skin.getDrawable("blaster");
        buttonhostStyle.down = skin.getDrawable("blaster2");
        buttonhostStyle.pressedOffsetX = 1;
        buttonhostStyle.checkedOffsetY = -1;
        buttonleft = new Button(buttonhostStyle);

        Button.ButtonStyle buttonjoinStyle = new Button.ButtonStyle();
        buttonjoinStyle.up = skin.getDrawable("blaster1");
        buttonjoinStyle.down = skin.getDrawable("blaster2");
        buttonjoinStyle.pressedOffsetX = 1;
        buttonjoinStyle.checkedOffsetY = -1;
        buttonright = new Button(buttonjoinStyle);

        Button.ButtonStyle buttonsoloStyle = new Button.ButtonStyle();
        buttonsoloStyle.up = skin.getDrawable("blaster3");
        buttonsoloStyle.down = skin.getDrawable("blaster2");
        buttonsoloStyle.pressedOffsetX = 1;
        buttonsoloStyle.checkedOffsetY = -1;
        buttoncenter = new Button(buttonsoloStyle);

        charfirst = new Character("Character1", 600 * UNIT_SCALE, 900 * UNIT_SCALE, 90, world, imgfirst);
        charsecond = new Character("Character2", 2600 * UNIT_SCALE, 900 * UNIT_SCALE, 90, world, imgsecond);
        mobsfirst = new Mobs("mob", 1600 * UNIT_SCALE, 2070 * UNIT_SCALE, 220, world, MobsTexture);


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
            table.add(buttonleft).width(600).height(128).pad(80);
            table.add(buttoncenter).width(600).height(128).pad(80);
            table.add(buttonright).width(600).height(128).pad(80);
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
            for (int i = 0; i < blasterAttacks.length; i++) {
                blasterAttacks[i] = new BlasterAttack(0, 0, 0);
            }
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                for (; true; ) {
                    world.step(1, 4, 4);
                    state += 0.1f;
                    state = Math.min(state, 16);
                    if (Attack[(int) state] == null && Mod == 3 && charsecond != null) {
                        float randomX = (float) random.nextInt(2400) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE;
                        if (Math.abs(charfirst.getX()) - Math.abs(randomX) < Math.abs(charsecond.getX() - randomX)) {
                            Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(2400) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charfirst.getX() * UNIT_SCALE, charfirst.getY() * UNIT_SCALE, StarAttack, world);
                        }
                    }


                    if (Attack[(int) state] == null && Mod == 2) {
                        float randomX = (float) random.nextInt(2400) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE;
                        if (Math.abs(charfirst.getX()) - Math.abs(randomX) < Math.abs(charsecond.getX() - randomX)) {
                            Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(2400) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charfirst.getX() * UNIT_SCALE, charfirst.getY() * UNIT_SCALE, StarAttack, world);
                        } else {
                            Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(2400) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charsecond.getX() * UNIT_SCALE, charsecond.getY() * UNIT_SCALE, StarAttack, world);
                        }
                    } else if (Attack[(int) state] == null && Mod == 3) {
                        float randomX = (float) random.nextInt(2400) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE;
                        Attack[(int) state] = new MobsAtack(2000, (float) random.nextInt(2400) * UNIT_SCALE + 200 * UNIT_SCALE + 50 * UNIT_SCALE, 1400 * UNIT_SCALE, 50 * UNIT_SCALE, 50 * UNIT_SCALE, charfirst.getX() * UNIT_SCALE, charfirst.getY() * UNIT_SCALE, StarAttack, world);
                    }
                    for (int i = 0; i < 16; i++) {
                        if (Attack[i] != null && Attack[i].state && Attack[i].body != null) {
                            if (Attack[i].state)
                                Attack[i].body.setLinearVelocity((myScreen.charfirst.getX() - Attack[i].body.getPosition().x) / 130, (myScreen.charfirst.getY() - Attack[i].body.getPosition().y) / 130);
                        }
                    }
                    for (int i = 0; i < 16; i++) {
                        if (Attack[i] != null && !Attack[i].state && Attack[i].body != null && !Attack[i].state2) {
                            world.destroyBody(Attack[i].body);
                            Attack[i].state2 = true;
                        }
                    }
                    if (random.nextInt(10) == 0 && (Mod == 2 || Mod == 3)) {
                        blasterID++;
                        if (blasterID >= blasterAttacks.length) {
                            blasterID = 0;
                        }
                        if (!blasterAttacks[blasterID].live) {
                            blasterAttacks[blasterID].live = true;
                            blasterAttacks[blasterID].mode = false;
                            blasterAttacks[blasterID].BlusterX = random.nextInt((int) W);
                            blasterAttacks[blasterID].BlusterY = random.nextInt((int) H);
                            blasterAttacks[blasterID].rotate = random.nextInt(360);
                        }
                    }

                    for (BlasterAttack blasterAttack : blasterAttacks) {
                        if (blasterAttack.live && (Mod == 2 || Mod == 3)) {
                            blasterAttack.math();
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
                if (charsecond.getLive()) {
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
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.begin();
        mobsfirst.draw(batch);
        charfirst.draw(batch);

        if (charsecond != null) {
            charsecond.draw(batch);
        }

        for (int i = 0; i < (int) Attack.length; i++) {
            if (Attack[i] != null && Attack[i].state && Attack[i].body != null)
                Attack[i].drawAttack(batch, this);
        }
        for (BlasterAttack attack : blasterAttacks) {
            if (attack.live) {
                float vx = (float) Math.sin(attack.rotate * Math.PI / 180f), vy = (float) Math.cos(attack.rotate * Math.PI / 180f);
                batch.draw(RegionBlaster, (attack.BlusterX + (-vx * (1 - attack.state)) * 100) * UNIT_SCALE, (attack.BlusterY + (-vy * (1 - attack.state)) * 100) * UNIT_SCALE, UNIT_SCALE * 128, UNIT_SCALE * 128, UNIT_SCALE * 256, UNIT_SCALE * 256, 1, 1, 360 - attack.rotate - 50);
            }
        }

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if ((Mod == 2) || (Mod == 3)) for (BlasterAttack blasterAttack : blasterAttacks) {
            // shapeRenderer.rect(blasterAttack.BlusterX, blasterAttack.BlusterY, 200 * blasterAttack.state + 1, 200 * blasterAttack.state + 1);
            if (blasterAttack.live) {
                float vx = (float) Math.sin(blasterAttack.rotate * Math.PI / 180f), vy = (float) Math.cos(blasterAttack.rotate * Math.PI / 180f);
                shapeRenderer.setColor(0.7f * 0.7f, 0.7f * 0.7f, 0.9f * 0.9f, 1);
                if (blasterAttack.state > 0.8) shapeRenderer.setColor(0.9f * 0.7f, 0.7f * 0.2f, 0.7f * 0.2f, 1);
                shapeRenderer.circle((blasterAttack.BlusterX + 128 + (vx * 140) + (-vx * (1 - blasterAttack.state)) * 100) * UNIT_SCALE, (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100) * UNIT_SCALE, (blasterAttack.state * 64) * UNIT_SCALE);

                blasterLineWidthX1 = (blasterAttack.BlusterX + 128 + vx * 140 + (-vx * (1 - blasterAttack.state)) * 100) * UNIT_SCALE;
                blasterLineWidthX2 = (blasterAttack.BlusterX + 128 + vx * 140 + (-vx * (1 - blasterAttack.state)) * 100 + vx * W * 2) * UNIT_SCALE;
                blasterLineWidthY1 = (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100) * UNIT_SCALE;
                blasterLineWidthY2 = (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100 + vy * W * 2) * UNIT_SCALE;

                v1.set(blasterLineWidthX1, blasterLineWidthY1);
                v2.set(blasterLineWidthX2, blasterLineWidthY2);
                float x = blasterLineWidthX1, y = blasterLineWidthY1;

                //world.rayCast(callback, v1, v2);
                for (int i = 0; i < 100; i++) {
                    x += vx * 10;
                    y += vy * 10;
                    if (blasterAttack.state > 0.8) {
                        if (Mod == 3) {
                            if (hit(x, y, 5 * UNIT_SCALE, charfirst.getX(), charfirst.getY(), charfirst.getRADIUS() * UNIT_SCALE)) {
                                charfirst.die();
                            }

                        } else if (Mod == 2) {
                            if (hit(x, y, 5 * UNIT_SCALE, charfirst.getX(), charfirst.getY(), charfirst.getRADIUS() * UNIT_SCALE)) {
                                charfirst.die();
                            }
                            if (hit(x, y, 5 * UNIT_SCALE, charsecond.getX(), charsecond.getY(), charsecond.getRADIUS() * UNIT_SCALE)) {
                                charsecond.die();
                            }
                        }
                    }
                }

                shapeRenderer.rectLine(blasterLineWidthX1, blasterLineWidthY1, blasterLineWidthX2, blasterLineWidthY2, (blasterAttack.state * 32) * UNIT_SCALE);
                shapeRenderer.setColor(0.8f * 0.7f, 0.8f * 0.7f, 0.9f * 0.7f, 1);
                if (blasterAttack.state > 0.8) shapeRenderer.setColor(0.9f * 0.7f, 0.2f * 0.7f, 0.2f * 0.7f, 1);
                shapeRenderer.circle((blasterAttack.BlusterX + 128 + vx * 140 + (-vx * (1 - blasterAttack.state)) * 100) * UNIT_SCALE, (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100) * UNIT_SCALE, (blasterAttack.state * 32) * UNIT_SCALE);
                shapeRenderer.rectLine((blasterAttack.BlusterX + 128 + vx * 140 + (-vx * (1 - blasterAttack.state)) * 100) * UNIT_SCALE, (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100) * UNIT_SCALE, (blasterAttack.BlusterX + 128 + vx * 140 + (-vx * (1 - blasterAttack.state)) * 100 + vx * W * 2) * UNIT_SCALE, (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100 + vy * W * 2) * UNIT_SCALE, (blasterAttack.state * 16) * UNIT_SCALE);
            }
        }
        shapeRenderer.end();
        batch.begin();
        batch.end();
        stage.act(delta);
        stage.draw();
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
        MobsTexture.dispose();
        hostatlas.dispose();
        joinatlas.dispose();
        soloatlas.dispose();
        Blaster.dispose();
        StarAttack.dispose();
        shapeRenderer.dispose();
    }
}