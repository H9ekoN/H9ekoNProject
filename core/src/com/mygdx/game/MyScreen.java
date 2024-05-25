package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.mygdx.game.KryoNet.client.ClientProgram;
import com.mygdx.game.KryoNet.server.ServerProgram;
import com.mygdx.game.gameui.JoystickArea;

import java.util.Random;


public class MyScreen extends Game implements Screen{
    private World world;
    private Texture RedEndScreen, PurpleEndScreen;
    private float blasterLineWidthX1, blasterLineWidthX2, blasterLineWidthY1, blasterLineWidthY2;
    public static OrthographicCamera camera = new OrthographicCamera();
    public static final float UNIT_SCALE = 1f / 16f;
    public Texture imgfirst, imgsecond, StarAttack, MobsTexture, Blaster, imgfirst1, imgfirst2, imgsecond1, imgsecond2;
    private SpriteBatch batch;
    private Stage stage = new Stage();
    private boolean raz = true;
    private Table table;
    private Skin skin;
    private TextureAtlas hostatlas, joinatlas, soloatlas;
    public Character charfirst, charsecond;
    private JoystickArea joystickAreafirst, joysticlAreaSecond;
    private BodyDef groundBodyDef = new BodyDef();
    private RectangleForMyGame bord1, bord2, bord3, bord4, line;
    private Star[] Attack = new Star[7];
    public float state = 0;
    private boolean stateMod = false;
    int Time;
    public Vector2 v1 = new Vector2(), v2 = new Vector2();
    public int Mod = 1;
    public ShapeRenderer shapeRenderer = new ShapeRenderer();
    public Random random = new Random();
    private Button buttonleft;
    private Button buttonright;
    private Button buttoncenter;
    private Button RestartButton;
    public int Online = 0;
    private ClientProgram clientP;
    private TextureRegion RegionBlaster;
    public ServerProgram serverP;
    private MyScreen myScreen = this;
    private int blasterID = 0;
    private BlasterAttack[] blasterAttacks = new BlasterAttack[3];
    private final float W;
    private final float H;
    private Texture[] MobsTextures = new Texture[6];
    private int AnimationStateRight = -1;
    private int AnimationStateLeft = -1;
    private Sound soundtouch, blaster, star;
    private TextureAtlas restaratlas;


    public boolean hit(float x1, float y1, float r1, float x2, float y2, float r2) {
        float dx = Math.abs(x1 - x2);
        float dy = Math.abs(y1 - y2);
        return Math.sqrt(dx * dx + dy * dy) <= r1 + r2;
    }

    public MyScreen() {

        batch = new SpriteBatch();

        imgfirst = new Texture("HeartRed_2.png");
        imgfirst1 = new Texture("HeartRed_1.png");
        imgfirst2 = new Texture("HeartRed_0.png");

        imgsecond = new Texture("HeartPurple.png");
        imgsecond1 = new Texture("HeartPurple.png");
        imgsecond2 = new Texture("HeartPurple.png");

        MobsTextures[0] = new Texture("f1.png");
        MobsTextures[1] = new Texture("f2.png");
        MobsTextures[2] = new Texture("f3.png");
        MobsTextures[3] = new Texture("f4.png");
        MobsTextures[4] = new Texture("f5.png");
        MobsTextures[5] = new Texture("f6.png");

        RedEndScreen = new Texture("endscreenRed.png");
        PurpleEndScreen = new Texture("endscreenPurple.png");


        MobsTexture = new Texture("monster1stay1.png");
        Blaster = new Texture("blaster.png");
        RegionBlaster = new TextureRegion(Blaster);

        StarAttack = new Texture("Star.png");
        camera.setToOrtho(false, 200, 150);

        world = new World(new Vector2(0, 0), true);

        for (int i = 0; i<Attack.length; i++){
            Attack[i] = new Star(0,0,0,2);
            Attack[i].state = false;
        }

        world.setContactListener(new MyContactListener());

        hostatlas = new TextureAtlas("ui/hostbutton.atlas");
        joinatlas = new TextureAtlas("ui/joinbutton.atlas");
        soloatlas = new TextureAtlas("ui/solobutton.atlas");
        restaratlas = new TextureAtlas("ui/RestartAtlas.atlas");

        skin = new Skin();

        skin.addRegions(hostatlas);
        skin.addRegions(joinatlas);
        skin.addRegions(soloatlas);
        skin.addRegions(restaratlas);

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

        Button.ButtonStyle buttonrestartStyle = new Button.ButtonStyle();
        buttonrestartStyle.up = skin.getDrawable("restartbutton");
        buttonrestartStyle.pressedOffsetX = 1;
        buttonrestartStyle.checkedOffsetY = -1;
        RestartButton = new Button(buttonrestartStyle);



        charfirst = new Character("Character1", 600 * UNIT_SCALE, 900 * UNIT_SCALE, 90, world, imgfirst, imgfirst1, imgfirst2);
        charsecond = new Character("Character2", 2600 * UNIT_SCALE, 900 * UNIT_SCALE, 90, world, imgsecond, imgsecond1, imgsecond2);


        bord1 = new RectangleForMyGame(-1 * UNIT_SCALE, -1 * UNIT_SCALE, 5800 * UNIT_SCALE, 1 * UNIT_SCALE, 1, world, null);
        bord2 = new RectangleForMyGame(220 * UNIT_SCALE, 2 * UNIT_SCALE, 1 * UNIT_SCALE, 3500 * UNIT_SCALE, 1, world, null);
        bord3 = new RectangleForMyGame(3000 * UNIT_SCALE, 2 * UNIT_SCALE, 1 * UNIT_SCALE, 3500 * UNIT_SCALE, 1, world, null);
        bord4 = new RectangleForMyGame(-7 * UNIT_SCALE, 2400 * UNIT_SCALE, 5800 * UNIT_SCALE, 1 * UNIT_SCALE, 0, world, null);

        line = new RectangleForMyGame(0, 1800 * UNIT_SCALE, 5800 * UNIT_SCALE, 20 * UNIT_SCALE, 0, world, new Texture("whiteline.png"));

        soundtouch = Gdx.audio.newSound(Gdx.files.internal("sounds/touch.mp3"));
        blaster = Gdx.audio.newSound(Gdx.files.internal("sounds/blaster.mp3"));

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
                    soundtouch.play();
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
                    soundtouch.play();
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
                    soundtouch.play();
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
                    charfirst.tx2 = charfirst.tx1;
                    charfirst.ty2 = charfirst.ty1;
                    charfirst.tx1 = charfirst.getX();
                    charfirst.ty1 = charfirst.getY();
                    world.step(1, 4, 4);
                    state += 0.5f;
                    if (state > 16) {
                        state = 0;
                    }
                    //if(random.nextInt(10) == 0){

                    if(Mod != 1 && random.nextInt(10) == 0 && AnimationStateLeft == 0) {
                        AnimationStateLeft = 1;
                        }
                    if(Mod != 1 && AnimationStateLeft != 0 && state%1==0){
                        AnimationStateLeft+=1;
                    }
                    if(Mod != 1 && AnimationStateLeft == 7){
                        AnimationStateLeft = 0;
                        setStar(-1);
                    }
                    if(Mod != 1 && random.nextInt(10) == 0 && AnimationStateRight == 0) {
                        AnimationStateRight = 1;
                    }
                    if(Mod != 1 && AnimationStateRight != 0 && state%1==0){
                        AnimationStateRight+=1;
                    }
                    if(Mod != 1 && AnimationStateRight == 7){
                        AnimationStateRight = 0;
                        setStar(1);
                    }
                    for (int i = 0; i < Attack.length; i++) {
                        if (Attack[i] != null && Attack[i].state) {

                            Attack[i].math(myScreen, charsecond);
                        }
                    }
                    for (int i = 0; i < Attack.length; i++) {
                        if (Attack[i] != null && !Attack[i].state  && !Attack[i].state2) {
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
                            Thread Threadsoundblaster = new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(320);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    blaster.play();
                                }
                            };
                            Threadsoundblaster.start();
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

    private void setStar(int a) {
        int index = -1;
        for(int i = 0; i<Attack.length; i++){
            if (Attack[i] != null && !Attack[i].state){
                index = i;
                break;
            }
        }

        if (index != -1){
            Attack[index] = new Star(index, W/2+W/4*a, H *3 +50, 20);
        }
    }

    public void set(){
        RestartScreen restartScreen = new RestartScreen();
        this.setScreen(restartScreen);
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (Mod == 4){
            batch.begin();
            batch.draw(MobsTexture, 0, 0, 160 * UNIT_SCALE, 90 * UNIT_SCALE);
            batch.end();
        }
        if (Mod == 5){
            batch.begin();
            batch.draw(PurpleEndScreen, 0, 0, 160 * UNIT_SCALE, 90 * UNIT_SCALE);
            batch.end();
        }

        if(Mod != 1 && Mod != 4 && Mod != 5){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(19/255f, 17/255f, 49/255f, 1);
            shapeRenderer.rect(0,0,4000 * UNIT_SCALE,2500 * UNIT_SCALE);
            shapeRenderer.setColor(26/255f, 23/255f , 78/255f, 1);
            shapeRenderer.rect(0,0,4000 * UNIT_SCALE,1800 * UNIT_SCALE);
            shapeRenderer.end();

            batch.begin();
            batch.draw(MobsTexture, (W/2) * UNIT_SCALE + 13, (H+727) * UNIT_SCALE, 35 * 0.85f, 35f);
            if(AnimationStateLeft>0) {
                batch.draw(MobsTextures[AnimationStateLeft - 1], (W / 2 + 15) * UNIT_SCALE + 13 - MobsTextures[AnimationStateLeft-1].getWidth()  *0.64f, (H + 727 + 230) * UNIT_SCALE, MobsTextures[AnimationStateLeft-1].getWidth() *0.64f, MobsTextures[AnimationStateLeft-1].getHeight() * 0.64f);
            }
            if(AnimationStateRight>0) {
                batch.draw(MobsTextures[AnimationStateRight - 1], (W / 2 + 15) * UNIT_SCALE + 28 + 13 + MobsTextures[AnimationStateRight-1].getWidth()  *0.64f, (H + 727 + 230) * UNIT_SCALE, -MobsTextures[AnimationStateRight-1].getWidth() *0.64f, MobsTextures[AnimationStateRight-1].getHeight() * 0.64f);
            }
            batch.end();
        } else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0,0,0,1);
            shapeRenderer.rect(0,0,4000 * UNIT_SCALE,2500 * UNIT_SCALE);
            shapeRenderer.end();
        }


        if (joystickAreafirst.isTouchStick() && Mod != 1 && Mod != 4 && Mod != 5) {
            float x = joystickAreafirst.getValueX() * 50;
            float y = joystickAreafirst.getValueY() * 50;
            if (charfirst.getLive()) {
                charfirst.setVelocity(x, y);
            }
        }

        if (!joystickAreafirst.isTouchStick() && Mod != 1 && Mod != 4 && Mod != 5) {
            charfirst.setVelocity(0, 0);
        }

        if (joysticlAreaSecond.isTouchStick() && Mod != 1 && Mod != 4 && Mod != 5) {
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

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.begin();
        charfirst.draw(batch);

        if (Mod == 2) {
            charsecond.draw(batch);
        }
            batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < (int) Attack.length; i++) {
            if (Attack[i] != null && Attack[i].state && Mod != 1 && Mod != 4 && Mod != 5)
                Attack[i].drawAttack(shapeRenderer,this);
        }
        shapeRenderer.end();
        batch.begin();
        for (BlasterAttack attack : blasterAttacks) {
            if (attack.live && Mod != 1 && Mod != 4 && Mod != 5) {
                float vx = (float) Math.sin(attack.rotate * Math.PI / 180f), vy = (float) Math.cos(attack.rotate * Math.PI / 180f);
                batch.draw(RegionBlaster, (attack.BlusterX + (-vx * (1 - attack.state)) * 100) * UNIT_SCALE, (attack.BlusterY + (-vy * (1 - attack.state)) * 100) * UNIT_SCALE, UNIT_SCALE * 128, UNIT_SCALE * 128, UNIT_SCALE * 256, UNIT_SCALE * 256, 1, 1, 360 - attack.rotate - 45);
            }
        }

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if ((Mod == 2) || (Mod == 3)) for (BlasterAttack blasterAttack : blasterAttacks) {
            // shapeRenderer.rect(blasterAttack.BlusterX, blasterAttack.BlusterY, 200 * blasterAttack.state + 1, 200 * blasterAttack.state + 1);
            if (blasterAttack.live) {
                float vx = (float) Math.sin(blasterAttack.rotate * Math.PI / 180f), vy = (float) Math.cos(blasterAttack.rotate * Math.PI / 180f);
                shapeRenderer.setColor(242/255f, 212/255f, 85/255f, 1);
                //if (blasterAttack.state > 0.8) shapeRenderer.setColor(0.9f * 0.7f, 0.7f * 0.2f, 0.7f * 0.2f, 1);
                // shapeRenderer.circle((blasterAttack.BlusterX + 128 + (vx * 140) + (-vx * (1 - blasterAttack.state)) * 100) * UNIT_SCALE, (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100) * UNIT_SCALE, (blasterAttack.state * 64) * UNIT_SCALE);

                blasterLineWidthX1 = (blasterAttack.BlusterX + 128 + vx * 140 + (-vx * (1 - blasterAttack.state)) * 100) * UNIT_SCALE;
                blasterLineWidthX2 = (blasterAttack.BlusterX + 128 + vx * 140 + (-vx * (1 - blasterAttack.state)) * 100 + vx * W * 2) * UNIT_SCALE;
                blasterLineWidthY1 = (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100) * UNIT_SCALE;
                blasterLineWidthY2 = (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100 + vy * W * 2) * UNIT_SCALE;

                v1.set(blasterLineWidthX1, blasterLineWidthY1);
                v2.set(blasterLineWidthX2, blasterLineWidthY2);
                float x = blasterLineWidthX1, y = blasterLineWidthY1;

                float nx = 5;
                float ny = 5;
                //world.rayCast(callback, v1, v2);
                for (int i = 0; i < 200; i++) {
                    //nx += random.nextInt(3)-1;
                    if (i%2==0){
                        nx = 2;
                        ny = 7;
                    } else {
                        nx = 7;
                        ny = 2;
                    }
                    x += vx * nx;
                    y += vy * ny;
                    if (blasterAttack.state > 0.8) {
                        if (Mod == 3 && raz) {
                            if (hit(x, y, 5 * UNIT_SCALE, charfirst.getX(), charfirst.getY(), charfirst.getRADIUS() * UNIT_SCALE)) {
                                charfirst.die();
                                stateMod = true;
                                table.add(RestartButton);
                                RestartButton.addListener(new ClickListener(){
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        myScreen = new MyScreen();
                                        setScreen(myScreen);
                                        table.removeActor(RestartButton);
                                        Mod = 1;
                                    }
                                });
                                table.add(RestartButton).width(600).height(128);
                                raz = false;
                            }

                        } else if (Mod == 2) {
                            if (hit(x, y, 5 * UNIT_SCALE, charfirst.getX(), charfirst.getY(), charfirst.getRADIUS() * UNIT_SCALE)) {
                                charfirst.die();
                                stateMod = true;
                                table.add(RestartButton);
                                RestartButton.addListener(new ClickListener(){
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        myScreen = new MyScreen();
                                        setScreen(myScreen);
                                        table.removeActor(RestartButton);
                                        Mod = 1;
                                    }
                                });
                                table.add(RestartButton).width(600).height(128);
                                raz = false;
                            }
                            if (hit(x, y, 5 * UNIT_SCALE, charsecond.getX(), charsecond.getY(), charsecond.getRADIUS() * UNIT_SCALE)) {
                                charsecond.die();
                                stateMod = true;
                                table.add(RestartButton);
                                RestartButton.addListener(new ClickListener(){
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        myScreen = new MyScreen();
                                        setScreen(myScreen);
                                        table.removeActor(RestartButton);
                                        Mod = 1;
                                    }
                                });
                                table.add(RestartButton).width(600).height(128);
                                raz = false;
                            }
                        }
                        shapeRenderer.rectLine(x, y, x - vx * nx, y - vy * ny, (blasterAttack.state * 32 + random.nextInt(15)) * UNIT_SCALE);
                    } else {
                            shapeRenderer.rectLine(x, y, x - vx * nx/5*3, y - vy * ny/5*3, (blasterAttack.state * 8) * UNIT_SCALE);
                    }
                }

                shapeRenderer.setColor(242/255f, 212/255f, 85/255f, 1);
                shapeRenderer.rect((blasterAttack.BlusterX + 128 + vx * 140 + (-vx * (1 - blasterAttack.state)) * 100) * UNIT_SCALE - (blasterAttack.state * 32) * UNIT_SCALE/2f, (blasterAttack.BlusterY + 128 + vy * 140 + (-vy * (1 - blasterAttack.state)) * 100) * UNIT_SCALE - (blasterAttack.state * 32) * UNIT_SCALE/2, (blasterAttack.state * 32) * UNIT_SCALE, (blasterAttack.state * 32) * UNIT_SCALE);
            }
        }
        shapeRenderer.end();
        batch.begin();
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void create() {

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
        soundtouch.dispose();
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