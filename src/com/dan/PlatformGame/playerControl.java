package com.dan.PlatformGame;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class playerControl extends Component {
    private PhysicsComponent physics;
    private int speed = 0;
    private int height = 0;

    private AnimatedTexture texture;

    public AnimatedTexture getTexture() {
        return texture;
    }

    private AnimationChannel animIdle, animWalk;

    public playerControl() {
        animIdle = new AnimationChannel("TrumpAfk.png", 55, 55, 50, Duration.seconds(1), 0, 3);
        animWalk = new AnimationChannel("trumpWalk.png", 55, 55, 50, Duration.seconds(1), 0, 3);

        texture = new AnimatedTexture(animIdle);
    }
    public int moveSpeed = 0;

    public int getMoveSpeed() {
        return moveSpeed;
    }
    public void setMoveSpeed() {
        this.moveSpeed = 150;
    }

    @Override
    public void onAdded() {
        entity.setViewWithBBox(texture);
    }

    @Override
    public void onUpdate(double tpf) {


        texture.loopAnimationChannel(isMoving() ? animWalk : animIdle);
    }

    private boolean isMoving() {
        return FXGLMath.abs(physics.getVelocityX()) > 0;
    }

    public void left() {
        getEntity().setScaleX(-1);
        physics.setVelocityX(-150-moveSpeed);
    }

    public void right() {
        getEntity().setScaleX(1);
        physics.setVelocityX(150+moveSpeed);
    }

    public void jump() {
        physics.setVelocityY(-300);
    }
}