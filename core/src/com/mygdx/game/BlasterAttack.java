package com.mygdx.game;


public class BlasterAttack {
    public float BlusterX, BlusterY, rotate;
    public float state = 0;
    public boolean mode = false;
    public boolean live = false;
    public BlasterAttack(float BlusterX,float BlusterY, float rotate){
        this.BlusterX = BlusterX;
        this.BlusterY = BlusterY;
        this.rotate = rotate;
    }
    public void math(){
        if (!mode){
            state+=(1-state)/30f;
            if (state >0.95f){
                mode = true;
            }
        } else {
            state+=(-state)/3f;
            if (state < 0.05f){
                live = false;
                mode = false;
            }
        }
    }
}
