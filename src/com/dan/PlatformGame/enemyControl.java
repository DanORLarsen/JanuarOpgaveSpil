package com.dan.PlatformGame;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

public class enemyControl extends Component {
    private PhysicsComponent physics;
    private int speed = 0;
    private int height = 0;
    private LocalTimer jumpTimer;

    private AnimatedTexture texture;

    public AnimatedTexture getTexture() {
        return texture;
    }

    private AnimationChannel animIdle, animWalk;

    public enemyControl() {
        animIdle = new AnimationChannel("newdude.png", 4, 32, 42, Duration.seconds(1), 1, 1);
        animWalk = new AnimationChannel("newdude.png", 4, 32, 42, Duration.seconds(1), 0, 3);

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
        jumpTimer = FXGL.newLocalTimer();
        jumpTimer.capture();

    }

    @Override
    public void onUpdate(double tpf) {
        texture.loopAnimationChannel(isMoving() ? animWalk : animIdle);
        if (jumpTimer.elapsed(Duration.seconds(3)))
        {jump();
        jumpTimer.capture();}
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
        physics.setVelocityY(-800);
    }
}