package com.example.particles;

import org.joml.Vector3f;

import com.example.entities.Player;
import com.example.renderEngine.DisplayManager;

public class Particle34 {
    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;
    
    private float elapsedTime = 0;

    public Particle34(Vector3f position, Vector3f velocity, float gravityEffect,
            float lifeLength, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        ParticleMaster34.addParticle(this);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public boolean update() {
        velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);
        change.mul(DisplayManager.getFrameTimeSeconds());
        position.add(change);
        elapsedTime += DisplayManager.getFrameTimeSeconds();
        return elapsedTime < lifeLength;
    }
}
