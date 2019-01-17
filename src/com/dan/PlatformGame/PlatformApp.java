package com.dan.PlatformGame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.util.Map;


public class PlatformApp extends GameApplication {
    public enum EntityType {
        PLAYER, COIN, DOOR
    }
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(15*70);
        gameSettings.setHeight(10*70);
    }

    private int coins = 0;
    private Entity player;


    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("left") {
            @Override
            protected void onAction() {
                player.getComponent(player1Control.class).left();
            }
        }, KeyCode.LEFT);
        getInput().addAction(new UserAction("right") {
            @Override
            protected void onAction() {
                player.getComponent(player1Control.class).right();
            }
        }, KeyCode.RIGHT);
        getInput().addAction(new UserAction("jump") {
            @Override
            protected void onAction() {
                player.getComponent(player1Control.class).jump();
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new marioFactory());
        getGameWorld().setLevelFromMap("mario..json");
        player = getGameWorld().spawn("player",50,50);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("coins", 0);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformApp.EntityType.PLAYER, PlatformApp.EntityType.COIN) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity players, Entity coin) {

                coins++;
                getGameState().increment("coins",+1);
                //Til power up player1.getComponent(DudeControl.class).setMoveSpeed(320);
                getAudioPlayer().playSound("roblox-death-sound-effect-opNTQCf4R.mp3");
                coin.removeFromWorld();
    }});
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformApp.EntityType.PLAYER, PlatformApp.EntityType.DOOR) {
            @Override
            protected void onCollisionBegin(Entity player, Entity door) {
                if (coins == 5)
                {
                    getDisplay().showMessageBox("Level Complete!", () -> {
                    System.out.println("Dialog closed!");});
                }
                    else {getDisplay().showMessageBox("Missing Burger(s)");
                    System.out.println("\uD83D\uDE08 - There is no score icon - \uD83D\uDE08");}
                }});}

    @Override
    protected void initUI() {
        Text textPixels1 = new Text();
        textPixels1.setTranslateX(50); // x = 50
        textPixels1.setTranslateY(30); // y = 100
        getGameScene().addUINode(textPixels1); // add to the scene graph

        textPixels1.textProperty().bind(getGameState().intProperty("coins").asString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
