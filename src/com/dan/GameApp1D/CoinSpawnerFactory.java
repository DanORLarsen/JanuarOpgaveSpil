package com.dan.GameApp1D;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;

public class CoinSpawnerFactory implements EntityFactory {

    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        return  Entities.builder()
                .type(BCMapp.EntityType.COIN)
                .at(FXGLMath.random()*1500, FXGLMath.random()*900)
                .viewFromTextureWithBBox("burger.png")
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("powerup")
    public Entity newPowerup(SpawnData data) {
        return  Entities.builder()
                .type(BCMapp.EntityType.POWERUP)
                .at(FXGLMath.random()*1500, FXGLMath.random()*900)
                .viewFromTextureWithBBox("powerup.png")
                .with(new CollidableComponent(true))
                .build();
    }

}
