package com.mygdx.game.gameui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


public class JoystickArea extends Group {
    private JoyStick joystick;
    private JoyStickInput jlistener;
    private final Vector2 tmp = new Vector2();
    public JoystickArea(Texture circle, Texture curJoystick, int setX, int setY) {
        joystick = new JoyStick(circle, curJoystick);
        addActor(joystick);

        addListener(new AreaListener());

        setX(setX);
        setY(setY);
        setWidth(550);
        setHeight(500);

    }

    private class AreaListener extends InputListener{

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            joystick.parentToLocalCoordinates(tmp.set(x,y));
            return super.touchDown(event, tmp.x, tmp.y, pointer, button);
        }
        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            joystick.parentToLocalCoordinates(tmp.set(x,y));
        }
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            joystick.parentToLocalCoordinates(tmp.set(x,y));
        }

    }


    public float getValueX(){
        return joystick.getValueX();
    }
    public float getValueY(){
        return joystick.getValueY();
    }
    public boolean isTouchStick(){
        return joystick.isTouchStick();
    }
}
