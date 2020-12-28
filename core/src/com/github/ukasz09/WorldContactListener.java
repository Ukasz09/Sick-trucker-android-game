package com.github.ukasz09;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.github.ukasz09.map.mapObjects.DangerZone;
import com.github.ukasz09.map.mapObjects.Ground;
import com.github.ukasz09.player.Player;

public class WorldContactListener implements ContactListener {

    public static final double DEGREES_TO_RADIANS = (double) (Math.PI / 180);
    private World world;

    public WorldContactListener(World world) {
        this.world = world;
    }

    @Override
    public void beginContact(Contact cntct) {
        Fixture fa = cntct.getFixtureA();
        Fixture fb = cntct.getFixtureB();
        System.out.println(fa + "," + fb);
        if (fb == null) return;
        if (fa == null) {
            return;
        }
        com.github.ukasz09.player.Player player = (com.github.ukasz09.player.Player) fb.getUserData();
        if (fa.getUserData() == null) {
            return;
        }
        if (fb.getUserData() == null) return;
        if (isGroundContact(fa, fb)) {
//            if (player.isJumping()) {
//                System.out.println("app");
//                player.getBody().applyForceToCenter(0, Player.JUMP_FORCE, false);
            player.setJumping(false);
//            }
//            player.setJumping(false);
//            player.getBody().setAngularVelocity(1);
            player.getBody().setAngularVelocity(0);

        }
        if (isDangerContact(fa, fb)) {
            player.setJumping(false);
            player.hit();
        }
    }

    @Override
    public void endContact(Contact cntct) {
        Fixture fa = cntct.getFixtureA();
        Fixture fb = cntct.getFixtureB();
        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;
        if (isGroundContact(fa, fb)) {
            com.github.ukasz09.player.Player player = (com.github.ukasz09.player.Player) fb.getUserData();
            player.setJumping(true);
        }
        if (isDangerContact(fa, fb)) {
            com.github.ukasz09.player.Player player = (com.github.ukasz09.player.Player) fb.getUserData();
            player.setJumping(true);
        }
    }

    private boolean isDangerContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof DangerZone && b.getUserData() instanceof com.github.ukasz09.player.Player);
    }

    private boolean isGroundContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Ground && b.getUserData() instanceof com.github.ukasz09.player.Player);
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {
    }
}
