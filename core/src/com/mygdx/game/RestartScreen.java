package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RestartScreen extends Game implements Screen {
    private Button restartbutton;

    private Skin skin;
    private Table table;
    private Button button;
    private MyScreen myScreen;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public RestartScreen(){



    }
    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0,0,4000 * MyScreen.UNIT_SCALE,4000 * MyScreen.UNIT_SCALE);
        shapeRenderer.end();
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int i, int i1) {

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

    }
}
