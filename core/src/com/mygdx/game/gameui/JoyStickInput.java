package com.mygdx.game.gameui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class JoyStickInput extends InputListener {
private JoyStick joystick;
public JoyStickInput(JoyStick joystick){
    this.joystick = joystick;
}

    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
    joystick.settouch();
    joystick.changeCursor(x, y);
        return true;
    }

    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
    joystick.setUNtouch();
    }

    public void touchDragged (InputEvent event, float x, float y, int pointer) {
    joystick.changeCursor(x, y);
    if(joystick.isTouchStick()){
        joystick.handleChangedListener();
    }
    }
}
