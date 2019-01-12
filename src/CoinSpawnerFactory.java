import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.shape.Rectangle;

public class CoinSpawnerFactory implements EntityFactory {

    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        return  Entities.builder()
                .type(GameApp.EntityType.COIN)
                .at(FXGLMath.random()*1500, FXGLMath.random()*900)
                .viewFromTextureWithBBox("burger.png")
                .with(new CollidableComponent(true))
                .build();
    }
}
