package alvaro.daniel.space_crusade;

/**
 * Created by Dany on 10/12/2017.
 */

public class SpriteResource {
    String id;
    int[] resourcesId;
    float image_speed;
    Vector2 offsetMultiplicator;
    boolean ui;

    public SpriteResource(String id, int[] resourcesId, float image_speed, boolean ui, Vector2 offset){
        this.id = id;
        this.resourcesId = resourcesId;
        this.image_speed = image_speed;
        this.offsetMultiplicator = offset;
    }

    public SpriteResource(String id, int[] resourcesId){
        this(id, resourcesId, 0, false, new Vector2(-0.5f, -0.5f));
    }

    public SpriteResource(String id, int[] resourcesId, float image_speed){
        this(id, resourcesId, image_speed, false, new Vector2(-0.5f, -0.5f));
    }

    public SpriteResource(String id, int[] resourcesId, float image_speed, boolean ui){
        this(id, resourcesId, image_speed, ui, new Vector2(-0.5f, -0.5f));
    }
}
