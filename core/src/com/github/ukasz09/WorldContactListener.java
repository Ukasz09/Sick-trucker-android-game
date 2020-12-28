package com.github.ukasz09;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.github.ukasz09.map.mapObjects.DangerZone;
import com.github.ukasz09.player.Player;

public class WorldContactListener implements ContactListener {

    public WorldContactListener() {
    }

    @Override
    public void beginContact(Contact cntct) {
        Fixture fa = cntct.getFixtureA();
        Fixture fb = cntct.getFixtureB();
        if (fb == null || fa == null || fa.getUserData() == null || fb.getUserData() == null)
            return;
        Player player = (Player) fb.getUserData();
        if (isDangerContact(fa, fb)) {
            player.destroy();
        }
    }

    @Override
    public void endContact(Contact cntct) {
        // nothing to do
    }

    private boolean isDangerContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof DangerZone && b.getUserData() instanceof Player);
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {
        // nothing to do
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {
        // nothing to do
    }
}
