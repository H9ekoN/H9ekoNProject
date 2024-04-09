package com.mygdx.game;


import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if(a == null || b == null) return;
        if (a.getUserData() == null || b.getUserData() == null) return;

        if(a.getUserData() instanceof Character && b.getUserData() instanceof MobsAtack) {
            Character tba = (Character) a.getUserData();
            tba.die();
        }else if (b.getUserData() instanceof Character  && a.getUserData() instanceof  MobsAtack){
            Character tbb = (Character) b.getUserData();
            tbb.die();
        }

        if (b.getUserData() instanceof RectangleForMyGame  && a.getUserData() instanceof  MobsAtack){
            MobsAtack tba = (MobsAtack) a.getUserData();
            RectangleForMyGame tbb = (RectangleForMyGame) b.getUserData();
            if (tba.body.getPosition().y < tbb.getY() || (tba.body.getPosition().y < 50 * MyScreen.UNIT_SCALE))tba.delete();

        } else if (b.getUserData() instanceof MobsAtack && a.getUserData() instanceof RectangleForMyGame){
            MobsAtack tbb = (MobsAtack) b.getUserData();
            RectangleForMyGame tba = (RectangleForMyGame) a.getUserData();
            if (tbb.body.getPosition().y < tba.getY() || (tbb.body.getPosition().y < 50 * MyScreen.UNIT_SCALE))tbb.delete();
        }
    }
    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if(a == null || b == null) return;
        if (a.getUserData() == null || b.getUserData() == null) return;

        if (b.getUserData() instanceof RectangleForMyGame  && a.getUserData() instanceof  MobsAtack){
            RectangleForMyGame tbb = (RectangleForMyGame) b.getUserData();
            if (tbb.material == 0) contact.setEnabled(false);

        } else if (b.getUserData() instanceof MobsAtack && a.getUserData() instanceof RectangleForMyGame){
            RectangleForMyGame tba = (RectangleForMyGame) a.getUserData();
            if (tba.material == 0) contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
    }
}
