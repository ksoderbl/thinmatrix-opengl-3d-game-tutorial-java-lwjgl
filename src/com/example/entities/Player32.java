package com.example.entities;

// import org.lwjgl.input.Keyboard;
import org.joml.Vector3f;

import com.example.models.TexturedModel;
import com.example.renderEngine.DisplayManager;
import com.example.terrains.World;

public class Player32 extends Entity {

    public static final float RUN_SPEED = 40;   // units / second
    public static final float TURN_SPEED = 160; // degrees / second
    public static final float GRAVITY = -50;
    public static final float JUMP_POWER = 30;
    
    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;

    public Player32(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(World world) {
        checkInputs();
        
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);
        
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        
        float terrainHeight = world.getHeightOfTerrain(getPosition().x, getPosition().z);
        if (super.getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
        
        // assume all low places are filled with water
        // make player swim so the head stays above surface
        float playerHeight = 5;
        float waterHeight = world.getHeightOfWater(getPosition().x, getPosition().z);
        if (super.getPosition().y < waterHeight - playerHeight) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = waterHeight - playerHeight;
        }
    }

    // private void jump() {
    //     if (!isInAir) {
    //         this.upwardsSpeed = JUMP_POWER;
    //         isInAir = true;
    //     }
    // }

    private void checkInputs() {
        // TODO
        // if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
        //     this.currentSpeed = RUN_SPEED;
        // } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
        //     this.currentSpeed = -RUN_SPEED;
        // } else {
        //     this.currentSpeed = 0;
        // }

        // if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
        //     this.currentTurnSpeed = -TURN_SPEED;
        // } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
        //     this.currentTurnSpeed = TURN_SPEED;
        // } else {
        //     this.currentTurnSpeed = 0;
        // }

        // if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
        //     jump();
        // }
    }
}
