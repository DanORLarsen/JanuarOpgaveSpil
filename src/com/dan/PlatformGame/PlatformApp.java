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

//--module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.graphics,javafx.media,javafx.fxml

import java.io.IOException;
import java.util.Map;
//TO DO: Export game to JAR

public class PlatformApp extends GameApplication {
    public enum EntityType {
        PLAYER, COIN, DOOR, WATER, ENEMIES, PLATFORM
    }
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Burger Mario");
        gameSettings.setVersion("1.0");
        gameSettings.setMenuEnabled(false);
        gameSettings.setIntroEnabled(false);
        gameSettings.setWidth(15*70);
        gameSettings.setHeight(10*70);
        gameSettings.setAppIcon("burger.png");
        MyTimerDan.start();


    }
    private int coins = 0;
    private int notDone = 2;
    private Entity Player1;
    private Entity player2;
    private boolean cheater = false;
    private int hitWater = 0;
    private Entity Enemy;
    private int jumpCounter = 0;
    private int lvlsComplete = 0;


    @Override
    protected void initInput() { //These dont need explanation (press key does onAction command)
        getInput().addAction(new UserAction("left") {
            @Override
            protected void onAction() {
                Player1.getComponent(playerControl.class).left();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("right") {
            @Override
            protected void onAction() {
                Player1.getComponent(playerControl.class).right();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("leftA") {
            @Override
            protected void onAction() {
                Player1.getComponent(playerControl.class).left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("rightD") {
            @Override
            protected void onAction() {
                Player1.getComponent(playerControl.class).right();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("jump") {
            @Override
            protected void onActionBegin() {
                //For double/triple jump maybe?  System.out.println(Player1.isColliding(platform)); (if collide -  jumpcounter = 0;) else only double jump
                d();
            }
        }, KeyCode.SPACE);

        getInput().addAction(new UserAction("jumpW") {
            @Override
            protected void onActionBegin() {
                //For double/triple jump maybe?  System.out.println(Player1.isColliding(platform)); (if collide -  jumpcounter = 0;) else only double jump
                d();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("jumpUp") {
            @Override
            protected void onActionBegin() {
                //For double/triple jump maybe?  System.out.println(Player1.isColliding(platform)); (if collide -  jumpcounter = 0;) else only double jump
                d();
            }
        }, KeyCode.UP);


        getInput().addAction(new UserAction("powerUp") {
            @Override
            protected void onActionEnd() {
                Player1.getComponent(playerControl.class).setMoveSpeed();
                System.out.println("Cheat activated");
                cheater = true;
            }
        }, KeyCode.O);
    }

    private void d() {
        if (jumpCounter < 4) {
            jumpCounter++;
            Player1.getComponent(playerControl.class).jump();
            if (jumpCounter > 3) {
                Player1.getComponent(playerControl.class).jump();
                getMasterTimer().runOnceAfter(() -> {
                    jumpCounter = 0;
                }, Duration.seconds(0.50));
            }
        }
    }

    @Override
    protected void initGame() {
        //Adding my EntityFactory, + map
        getGameWorld().addEntityFactory(new marioFactory());
        getGameWorld().setLevelFromMap("MarioLevel-1.json");
        Player1 = getGameWorld().spawn("player",50,400);
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
                Player1.removeFromWorld();
                Enemy.removeFromWorld();
                if (lvlsComplete == 2) {
                Enemy = getGameWorld().spawn("enemies",50,400);
                Player1 = getGameWorld().spawn("player",50,500);
                }
                if (lvlsComplete==1){
                    Player1 = getGameWorld().spawn("player",50,400);
                    Enemy = getGameWorld().spawn("enemies",735, 400);
                }
                else {Enemy = getGameWorld().spawn("enemies",300,600);
                    Player1 = getGameWorld().spawn("player",50,500);}


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
                Player1.removeFromWorld();
                hitWater++;
                if (lvlsComplete == 0 || lvlsComplete == 1)
                {Player1 = getGameWorld().spawn("player",50,400);}
                else {
                Player1 = getGameWorld().spawn("player",50,50);}
            }});

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformApp.EntityType.PLAYER, PlatformApp.EntityType.DOOR) {
            @Override
            protected void onCollisionBegin(Entity player, Entity door) {
                if (coins == 5) //Check if all burgers are collected
                {
                    lvlsComplete++; //Need for lvl 3 later. when switch to lvl 2 works
                    MyTimerDan.stop();
                    Player1.removeFromWorld();
                    if (lvlsComplete == 3)
                {getDisplay().showMessageBox("Game Complete!", () -> {
                    System.out.println("Dialog closed!");
                    exit();
                });}
                    getDisplay().showMessageBox(MyTimerDan.getStringScore());
                    getDisplay().showMessageBox("Level Complete!", () -> {
                    System.out.println("Dialog closed!");
                    });
                    if (lvlsComplete == 1){
                    getGameWorld().setLevelFromMap("MarioLevel-2.json");
                    coins = 0;
                    getGameState().increment("coins",0);
                    notDone = 0;
                    Player1 = getGameWorld().spawn("player",50,400);
                    Enemy = getGameWorld().spawn("enemies",735, 400);
                    Player1.getComponent(playerControl.class);
                    System.out.println("you hit the water " + hitWater + " Time(s)");

                    Enemy.getComponent(enemyControl.class).jump();}
                    else if (lvlsComplete == 2)
                    { getGameWorld().setLevelFromMap("MarioLevel-3.json");
                        coins = 0;
                        notDone = 0;
                        Player1 = getGameWorld().spawn("player",50,50);
                        Player1.getComponent(playerControl.class);
                        System.out.println("you hit the water " + hitWater + " Time(s)");
                        Enemy = getGameWorld().spawn("enemies",60, 400);
                        Enemy.getComponent(enemyControl.class).jump();}



                    if (cheater == true) //If cheats were activated give cheater messsage + playSound
                    {
                        getAudioPlayer().playSound("Trolol sound.mp3");
                        getDisplay().showMessageBox("Cheater");}}

                //If not all are collected then do this.
                 if (coins != 5) {
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
/*
        if (args.length == 0) {
            try {
                // re-launch the app itselft with VM option passed
                Runtime.getRuntime().exec(new String[] {"java", "-Xmx1024m", "-jar", "JanuarOpgaveSpil.jar", "test"});
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            System.exit(0);
        }
*/
    }
}
