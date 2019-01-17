package com.dan.PlatformGame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.dan.GameApp1D.MyTimerDan;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.util.Map;
//TO DO: Add Camera + lvls and animated sprite

public class PlatformApp extends GameApplication {
    public enum EntityType {
        PLAYER, COIN, DOOR
    }
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setMenuEnabled(false);
        gameSettings.setIntroEnabled(false);
        gameSettings.setWidth(15*70);
        gameSettings.setHeight(10*70);
        MyTimerDan.start();
    }

    private int coins = 0;
    private int notDone = 2;
    private Entity player;
    private boolean cheater = false;


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

        getInput().addAction(new UserAction("lefta") {
            @Override
            protected void onAction() {
                player.getComponent(player1Control.class).left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("rightd") {
            @Override
            protected void onAction() {
                player.getComponent(player1Control.class).right();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("jump") {
            @Override
            protected void onAction() {
                player.getComponent(player1Control.class).jump();
            }
        }, KeyCode.SPACE);
        getInput().addAction(new UserAction("powerUp") {
            @Override
            protected void onAction() {
                player.getComponent(player1Control.class).setD(100);
                System.out.println("Cheat activated");
                cheater = true;
            }
        }, KeyCode.O);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new marioFactory());
        getGameWorld().setLevelFromMap("mario..json");
        player = getGameWorld().spawn("player",50,400);
        getAudioPlayer().setGlobalSoundVolume(0.12);
        getAudioPlayer().playSound("bensound-theelevatorbossanova.mp3");
        System.out.println(getAudioPlayer().getGlobalSoundVolume());
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
                    MyTimerDan.stop();
                    getDisplay().showMessageBox("Level Complete!", () -> {
                    System.out.println("Dialog closed!");});
                    if (cheater == true)
                    {
                        getAudioPlayer().playSound("Trolol sound.mp3");
                        getDisplay().showMessageBox("Cheater");}
                }
                else if (coins != 5) {
                    if (notDone == 2) {
                        getDisplay().showMessageBox("Missing Burger(s)");
                        notDone--;
                    }
                    else if (notDone == 1) {
                        getDisplay().showMessageBox("\uD83D\uDE08 - There is no score icon - \uD83D\uDE08");
                    }
                }}});}

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
