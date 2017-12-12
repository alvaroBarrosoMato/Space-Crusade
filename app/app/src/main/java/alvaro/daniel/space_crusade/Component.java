package alvaro.daniel.space_crusade;

/**
 * Created by Dany on 08/12/2017.
 */

public class Component {
    public static enum COMPONENT_TYPE{
        SPRITE, TRANSFORM, KINEMATIC, COLLIDER, BEHAVIOUR
    }
    SceneEntity entity;
    COMPONENT_TYPE type;

    public Component(COMPONENT_TYPE type){
        this.type = type;
    }

    public COMPONENT_TYPE getType() {
        return type;
    }

    public SceneEntity getEntity() {
        return entity;
    }

    public void setEntity(SceneEntity entity) {
        this.entity = entity;
    }

    public Component(Component original){
        this.type = original.type;
        this.entity = original.entity.copy();
    }

    public Component copy(){
        return new Component(this);
    }
}
