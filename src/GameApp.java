import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;


public class GameApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1800);
        settings.setHeight(1600);
        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);
    }
    private Entity player;
    private Entity player2;

    @Override
    protected void initGame() {
        player = Entities.builder()
                .at(200, 200)
                .with(new DudeControl())
                .buildAndAttach();
        player2 = Entities.builder()
                .at(200, 200)
                .with(new DudeControl())
                .buildAndAttach();
    }
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(DudeControl.class).moveRight();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(DudeControl.class).moveLeft();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player.getComponent(DudeControl.class).moveUp();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(DudeControl.class).moveDown();
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

    public static void main(String[] args) {
        launch(args);
    }


}class DudeControl extends Component {

    private int speed = 0;
    private int height = 0;

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalk;

    public DudeControl() {
        animIdle = new AnimationChannel("newdude.png", 4, 32, 42, Duration.seconds(1), 1, 1);
        animWalk = new AnimationChannel("newdude.png", 4, 32, 42, Duration.seconds(1), 0, 3);

        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded() {
        entity.setView(texture);
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
        height = 150;

        getEntity().setScaleY(-1);
    }
    public void moveUp() {
        height = -150;

        getEntity().setScaleY(1);
    }

    public void moveRight() {
        speed = 150;

        getEntity().setScaleX(1);
    }

    public void moveLeft() {
        speed = -150;

        getEntity().setScaleX(-1);
    }
}
