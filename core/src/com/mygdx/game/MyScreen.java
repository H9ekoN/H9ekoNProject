package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MyScreen implements Screen {
    private World world;
    private TiledMap map;
    private OrthographicCamera camera = new OrthographicCamera();
    private OrthogonalTiledMapRenderer renderer;
    public static final float UNIT_SCALE = 1f / 16f;
    Box2DDebugRenderer box2DDebugRenderer;
    Texture img;
    SpriteBatch batch;
    BitmapFont font;
    private Stage stage = new Stage();
    private Table table;
    Skin skin;
    TextureAtlas atlas;

    public MyScreen(){
        box2DDebugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        img = new Texture("MasterFish.png");
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE);
        camera.setToOrtho(false, 100, 100);
        world = new World(new Vector2(), false);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(delta, 4,4);
        batch.begin();
        batch.draw(img, 0,0, 100, 100);
        batch.end();

        camera.update();
        renderer.setView(camera);
        renderer.render();
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
        img.dispose();
        batch.dispose();
    }
}
