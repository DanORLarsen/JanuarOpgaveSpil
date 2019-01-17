package com.dan.PlatformGame;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class player1Control extends Component {
    private PhysicsComponent physics;
    private int d = 0;

    public void setD(int d) {
        this.d = d;
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
    }
    public void left() {
        physics.setVelocityX(-150-d);
    }
    public void right() {
        physics.setVelocityX(150+d);

    }
    public void jump() {
        physics.setVelocityY(-400);
    }
}
