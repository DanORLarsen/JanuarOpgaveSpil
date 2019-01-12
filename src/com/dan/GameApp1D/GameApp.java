package com.dan.GameApp1D;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;


public class GameApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Burger Collector?");
        settings.setWidth(1600);
        settings.setHeight(1000);
        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);
    }
    private int coinscoreP1;
    private int coinscoreP2;
    private Entity player1;
    private Entity player2;
    public enum EntityType {
        PLAYER, COIN
    }
    //PICS from flaticon
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CoinSpawnerFactory()); //Added factory here to make it work (cant do it in same method apparently)
            Entities.builder()
                    .type(EntityType.COIN)
                    .at(FXGLMath.random()*1500, FXGLMath.random()*900)
                    .viewFromTextureWithBBox("burger.png")
                    .with(new CollidableComponent(true))
                    .buildAndAttach(getGameWorld());
        getAudioPlayer().playSound("bensound-theelevatorbossanova.mp3");
        player1 = Entities.builder()
                .type(EntityType.PLAYER)
                .at(200, 200)
                .with(new CollidableComponent(true))
                .with(new DudeControl())
                .buildAndAttach();
        player2 = Entities.builder()
                .type(EntityType.PLAYER)
                .at(210, 200)
                .with(new CollidableComponent(true))
                .with(new DudeControl())
                .buildAndAttach();
    }
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player1.getComponent(DudeControl.class).moveRight();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player1.getComponent(DudeControl.class).moveLeft();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player1.getComponent(DudeControl.class).moveUp();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player1.getComponent(DudeControl.class).moveDown();
            }
        }, KeyCode.S);

        //Player 2
        getInput().addAction(new UserAction("Right2") {
            @Override
            protected void onAction() {
                player2.getComponent(DudeControl.class).moveRight();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Left2") {
            @Override
            protected void onAction() {
                player2.getComponent(DudeControl.class).moveLeft();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Up2") {
            @Override
            protected void onAction() {
                player2.getComponent(DudeControl.class).moveUp();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Down2") {
            @Override
            protected void onAction() {
                player2.getComponent(DudeControl.class).moveDown();
            }
        }, KeyCode.DOWN);
    }
    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player1, Entity coin) {
                getGameState().increment("coins",+1);

                getAudioPlayer().playSound("roblox-death-sound-effect-opNTQCf4R.mp3");
                coinscoreP1++;
                //coinscoreP2++; + getGameState().increment("coins2", +1);
                if (coinscoreP1 < 10) //Dont spawn more burgers after reach 10 in score
                getGameWorld().spawn("coin");
                coin.removeFromWorld();

                if (coinscoreP1==10)
                {getAudioPlayer().playSound("Ta Da-SoundBible.com-1884170640.wav");
                    getMasterTimer().runOnceAfter(() -> {
                        exit(); //To make game end after score.
                    }, Duration.seconds(2)); // wait (amount) seconds

                }




            }
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("coins", 0);
        vars.put("coins2",0);
    }
    @Override
    protected void initUI() {
        getGameScene().setBackgroundRepeat("NiceGuyBackground.jpg");

        Texture scoreBox1 = getAssetLoader().loadTexture("white.jpg");
        scoreBox1.setTranslateX(37);
        scoreBox1.setTranslateY(80);

        getGameScene().addUINode(scoreBox1);

        Texture scoreBox2 = getAssetLoader().loadTexture("white.jpg");
        scoreBox2.setTranslateX(1487);
        scoreBox2.setTranslateY(80);

        getGameScene().addUINode(scoreBox2);

        Text textPixels1 = new Text();
        textPixels1.setTranslateX(50); // x = 50
        textPixels1.setTranslateY(100); // y = 100
        getGameScene().addUINode(textPixels1); // add to the scene graph

        Text textPixels2 = new Text();
        textPixels2.setTranslateX(1500); // x = 50
        textPixels2.setTranslateY(100); // y = 100
        getGameScene().addUINode(textPixels2);

        textPixels1.textProperty().bind(getGameState().intProperty("coins").asString());
        textPixels2.textProperty().bind(getGameState().intProperty("coins2").asString());
    }

    public static void main(String[] args) {
        launch(args);
    }


}class DudeControl extends Component {

    private int speed = 0;
    private int height = 0;

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalk;

    public DudeControl() {
        animIdle = new AnimationChannel("bob.png", 221, 128, 222, Duration.seconds(1), 1, 1);
        animWalk = new AnimationChannel("bob.png", 221, 128, 222, Duration.seconds(1), 0, 3);

        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded() {
        entity.setViewWithBBox(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateX(speed * tpf);

        if (speed != 0) {

            if (texture.getAnimationChannel() == animIdle) {
                texture.loopAnimationChannel(animWalk);
            }

            speed = (int) (speed * 0.9);

            if (FXGLMath.abs(speed) < 1) {
                speed = 0;
                texture.loopAnimationChannel(animIdle);
            }
        }

        entity.translateY(height * tpf);

        if (height != 0) {

            if (texture.getAnimationChannel() == animIdle) {
                texture.loopAnimationChannel(animWalk);
            }

            height = (int) (height * 0.9);

            if (FXGLMath.abs(height) < 1) {
                height = 0;
                texture.loopAnimationChannel(animIdle);
            }
        }


    }
    public void moveDown() {
        height = 250;

        getEntity().setScaleY(-1);
    }
    public void moveUp() {
        height = -250;

        getEntity().setScaleY(1);
    }

    public void moveRight() {
        speed = 250;

        getEntity().setScaleX(1);
    }

    public void moveLeft() {
        speed = -250;

        getEntity().setScaleX(-1);
    }
}
