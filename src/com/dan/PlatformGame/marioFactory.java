package com.dan.PlatformGame;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;


public class marioFactory implements EntityFactory {
    @Spawns("platform")
public Entity newPlatfrom(SpawnData data) {
    return Entities.builder()
            .from(data)
            .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),data.<Integer>get("height"))))
            .with(new PhysicsComponent())
            .build();
}
    @Spawns("water")
    public Entity newWater(SpawnData data) {
        return Entities.builder()
                .type(PlatformApp.EntityType.WATER)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }
/*
    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        return Entities.builder()
                .from(data)
                .viewFromNodeWithBBox(new Circle(data.<Integer>get("width")/2, Color.GOLD))
                .build();
    }
    */
    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        return  Entities.builder()
                .type(PlatformApp.EntityType.COIN)
                .from(data)
                .viewFromTextureWithBBox("burger.png")
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("door")
    public Entity newDoor(SpawnData data) {
        return  Entities.builder()
                .type(PlatformApp.EntityType.DOOR)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        return Entities.builder()
                .type(PlatformApp.EntityType.PLAYER)
                .from(data)
                .viewFromTexture("newdude.png")
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new playerControl())
                .build();
    }

    @Spawns("enemies")
    public Entity newEnemies(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        return Entities.builder()
                .type(PlatformApp.EntityType.ENEMIES)
                .from(data)
                .viewFromTexture("Thwomp.png")
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new enemyControl())
                .build();
    }

}
