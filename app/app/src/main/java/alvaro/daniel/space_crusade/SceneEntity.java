package alvaro.daniel.space_crusade;

import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Dany on 08/12/2017.
 */

public class SceneEntity implements Comparable<SceneEntity>{
    String id;
    Scene scene;
    Scene.TAGS tag;
    Map<Component.COMPONENT_TYPE, Component> components;

    public SceneEntity(String id, Scene scene, Scene.TAGS tag, Transform... transform){
        components = new HashMap();
        if(transform.length == 1){
            transform[0].setEntity(this);
            components.put(Component.COMPONENT_TYPE.TRANSFORM, transform[0]);
        }else{
            components.put(Component.COMPONENT_TYPE.TRANSFORM, new Transform());
        }

        this.id = id;
        this.scene = scene;
        this.tag = tag;
    }

    public SceneEntity(String id){
        this(id, null, Scene.TAGS.DEFAULT);
    }

    public SceneEntity(String id, Scene scene){
        this(id, scene, Scene.TAGS.DEFAULT);
    }

    public SceneEntity(SceneEntity original){
        this(original.id, original.scene, original.tag, ((Transform)original.getComponent(Component.COMPONENT_TYPE.TRANSFORM)).copy());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            original.components.forEach((id, e) -> this.components.put(id, e.copy()));
        }else{
            for(Map.Entry<Component.COMPONENT_TYPE, Component> e: original.components.entrySet()){
                this.components.put(e.getKey(), e.getValue().copy());
            }
        }
    }

    public String getId() {
        return id;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene.TAGS getTag() {
        return tag;
    }

    public void addComponent(Component component){
        component.setEntity(this);
        components.put(component.getType(), component);
    }

    //devuelve el componente del tipo determinado, o null si la entidad no tiene ese componente.
    public Component getComponent(Component.COMPONENT_TYPE type){
       return components.get(type);
    }

    //elimina el componente seleccionado, devuelve true si el componente se ha eliminado o false si no existia
    public boolean removeComponent(Component.COMPONENT_TYPE type){
        Component c = components.remove(type);
        return c != null;
    }

    public SceneEntity copy(){
        return new SceneEntity(this);
    }

    public void render(Canvas canvas){
        Sprite spr = (Sprite) components.get(Component.COMPONENT_TYPE.SPRITE);
        if(spr == null)
            return;
        spr.render(canvas);
    }

    public void update(){
        Kinematic kin = (Kinematic) components.get(Component.COMPONENT_TYPE.KINEMATIC);
        if(kin != null){
            kin.update();
        }
        Behaviour beh = (Behaviour) components.get(Component.COMPONENT_TYPE.BEHAVIOUR);
        if(beh != null){
            beh.update();
        }
    }

    public  Map<String, Object> getMemory(){
        Behaviour b = (Behaviour)components.get(Component.COMPONENT_TYPE.BEHAVIOUR);
        if(b != null){
            return b.memory;
        }else
            return null;
    };

    public void destroy(){
        Behaviour b = (Behaviour)components.get(Component.COMPONENT_TYPE.BEHAVIOUR);
        if(b != null && b.onDestroy.size() > 0){
            for (MonoBehaviour mb :b.onDestroy){
                mb.call();
            }
        }
        scene.removeEntity(id);
    }

    @Override
    public boolean equals(Object o) {
       if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SceneEntity that = (SceneEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "SceneEntity{" +
                "id='" + id + '\'' +
                ", scene=" + scene +
                ", tag=" + tag +
                ", components=" + components +
                '}';
    }

    @Override
    public int compareTo(@NonNull SceneEntity sceneEntity) {
        return sceneEntity.id.compareTo(this.id);
    }
}
