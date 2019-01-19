package com.dan.PlatformGame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.dan.GameApp1D.MyTimerDan;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;
//TO DO: Add Camera + lvls and animated sprite

public class PlatformApp extends GameApplication {
    public enum EntityType {
        PLAYER, COIN, DOOR, WATER, ENEMIES, PLATFORM
    }
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Burger Mario");
        gameSettings.setVersion("0.5");
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
    private int hitWater = 0;
    private Entity Enemy;
    private int jumpCounter = 0;


    @Override
    protected void initInput() { //These dont need explanation (press key does onAction command)
        getInput().addAction(new UserAction("left") {
            @Override
            protected void onAction() {
                player.getComponent(playerControl.class).left();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("right") {
            @Override
            protected void onAction() {
                player.getComponent(playerControl.class).right();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("lefta") {
            @Override
            protected void onAction() {
                player.getComponent(playerControl.class).left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("rightd") {
            @Override
            protected void onAction() {
                player.getComponent(playerControl.class).right();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("jump") {
            @Override
            protected void onActionBegin() {
                //For double/triple jump maybe?  System.out.println(player.isColliding(platform)); (if collide -  jumpcounter = 0;) else only double jump
                if (jumpCounter < 4) {
                    jumpCounter++;
                    player.getComponent(playerControl.class).jump();
                    if (jumpCounter > 3) {
                        player.getComponent(playerControl.class).jump();
                        getMasterTimer().runOnceAfter(() -> {
                            jumpCounter = 0;
                        }, Duration.seconds(0.50));
                    }
                }
        }
        }, KeyCode.SPACE);

        getInput().addAction(new UserAction("powerUp") {
            @Override
            protected void onActionEnd() {
                player.getComponent(playerControl.class).setMoveSpeed();
                System.out.println("Cheat activated");
                cheater = true;
            }
        }, KeyCode.O);
    }

    @Override
    protected void initGame() {
        //Adding my EntityFactory, + map
        getGameWorld().addEntityFactory(new marioFactory());
        getGameWorld().setLevelFromMap("MarioLevel-1.json");
        player = getGameWorld().spawn("player",50,400);
        Enemy = getGameWorld().spawn("enemies",300, 400);
        Enemy.getComponent(enemyControl.class).jump();
        getAudioPlayer().setGlobalSoundVolume(0.12);
        getAudioPlayer().loopBGM("Kevin Macleod Scheming Weasel (faster version).mp3");
        System.out.println(getAudioPlayer().getGlobalSoundVolume());
    }



    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformApp.EntityType.PLAYER, EntityType.ENEMIES) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity players, Entity coin) {
                player.removeFromWorld();
                Enemy.removeFromWorld();
                Enemy = getGameWorld().spawn("enemies",300,600);
                player = getGameWorld().spawn("player",50,500);

            }});
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
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformApp.EntityType.PLAYER, PlatformApp.EntityType.WATER) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity players, Entity coin) {
                player.removeFromWorld();
                hitWater++;
                player = getGameWorld().spawn("player",50,50);
            }});

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformApp.EntityType.PLAYER, PlatformApp.EntityType.DOOR) {
            @Override
            protected void onCollisionBegin(Entity player, Entity door) {
                if (coins == 5) //Check if all burgers are collected
                {
                    MyTimerDan.stop();
                    getDisplay().showMessageBox(MyTimerDan.getStringScore());
                    getDisplay().showMessageBox("Level Complete!", () -> {
                    System.out.println("Dialog closed!");});
                    System.out.println("you hit the water " + hitWater + " Time(s)");

                    if (cheater == true) //If cheats were activated give cheater messsage + playSound
                    {
                        getAudioPlayer().playSound("Trolol sound.mp3");
                        getDisplay().showMessageBox("Cheater");}
                }
                //If not all are collected then do this.
                else if (coins != 5) {
                    if (notDone == 2) {
                        getDisplay().showMessageBox("Missing Burger(s)");
                        notDone--;
                    }
                    //If second time not all are collected..  give this hint
                    else if (notDone == 1) {
                        getDisplay().showMessageBox("\uD83D\uDE08 - There is no score icon - \uD83D\uDE08");
                    }
                }}});}
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("coins", 0);
        vars.put("hints", "O - Activate Cheats");
    }

    @Override
    protected void initUI() {
        getGameScene().setBackgroundRepeat("BackgroundMario.png");
        Text textPixels1 = new Text();
        textPixels1.setTranslateX(50);
        textPixels1.setTranslateY(30);
        getGameScene().addUINode(textPixels1); // add to the scene graph

        Text textPixels2 = new Text();
        textPixels2.setTranslateX(920);
        textPixels2.setTranslateY(20);
        getGameScene().addUINode(textPixels2);

        textPixels2.textProperty().bind(getGameState().stringProperty("hints"));
        textPixels1.textProperty().bind(getGameState().intProperty("coins").asString());
    }

    public static void main(String[] args) {
        launch(args);

    }
}
