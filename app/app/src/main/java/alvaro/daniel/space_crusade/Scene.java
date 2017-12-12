package alvaro.daniel.space_crusade;

/**
 * Created by Dany on 08/12/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Scene {
    public static enum TAGS{
            DEFAULT, PLAYER, ENEMY, SOLID, UI
    }
    public static enum SCENE_STATE{
        STATE_PAUSE, STATE_RUNNING, STATE_STEP, STATE_STOPED
    }
    Map<String, SceneEntity> originalEntities;
    Map<String, SceneEntity> runningEntities;
    //SortedMap<String, SceneEntity> runningEntities;
    Map<String, SceneEntity> collisionables;
    String name;
    SCENE_STATE state = SCENE_STATE.STATE_STOPED;
    Boolean doPause = false;
    Context ctx;
    int width;
    int height;
    int view_x;
    int view_y;
    int canvas_width;
    int canvas_height;
    long frame = 0;
    long uframe = 0;
    boolean debug = false;
    private long idNumber = 0;
    Typeface spaceFont;
    SceneEntity cam;

    public Scene(Context ctx, String name, int width, int height, int canvas_width, int canvas_height) {
        this.ctx = ctx;
        this.name = name;
        this.width = width;
        this.height = height;
        this.canvas_width = canvas_width;
        this.canvas_height = canvas_height;
        this.originalEntities = new HashMap();
        this.collisionables = new HashMap();
        this.runningEntities = new HashMap();
        /*this.runningEntities = new TreeMap<String, SceneEntity>((a, b) -> {

            Log.i("comparando", a.toString() +" : "+b.toString());
            float d1 = ((Transform)this.runningEntities.get(a).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
            float d2 =((Transform)this.runningEntities.get(b).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
            //float d1 = ((Transform)this.originalEntities.get(a).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
            //float d2 =((Transform)this.originalEntities.get(b).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
            int compare = Float.compare(d1, d2) * -1;
            if(compare == 0){
                compare = a.compareTo(b);
            }
            return compare;
        });*/

        //spaceFont = Typeface.createFromAsset(ctx.getAssets(), "fonts/radiospacebold.ttf");
        spaceFont = BaseActivity.spaceFont;
    }

    public void addEntity(SceneEntity entity){
        entity.scene = this;
        if(state == SCENE_STATE.STATE_STOPED){
            originalEntities.put(entity.id, entity);
        }else{
            addToRun(entity);
        }
    }

    private void addToRun(SceneEntity entity){
        runningEntities.put(entity.id, entity);
        if(entity.getComponent(Component.COMPONENT_TYPE.COLLIDER) != null){
            collisionables.put(entity.id, entity);
        }
    }

    public boolean removeEntity(String id){
        if(state == SCENE_STATE.STATE_STOPED){
           return originalEntities.remove(id) != null;
        }else{
            return removeToRun(id);
        }
    }

    public boolean removeToRun(String id){
       return  runningEntities.remove(id) != null;
    }

    public void SetCamera(SceneEntity c){
        this.cam = c;
    }

    public boolean pause(){
        if(state != SCENE_STATE.STATE_STOPED && state != SCENE_STATE.STATE_PAUSE) {
            state = SCENE_STATE.STATE_PAUSE;
            return true;
        }
        return false;
    }

    public boolean play(){
        if(state != SCENE_STATE.STATE_STOPED && state != SCENE_STATE.STATE_RUNNING) {
            state = SCENE_STATE.STATE_RUNNING;
            return true;
        }
        return false;
    }

    public void stop(){
        runningEntities.clear();
        state = SCENE_STATE.STATE_STOPED;
    }

    public SceneEntity getEntity(String id){
        return runningEntities.get(id);
    }

    public long getIdNumber(){
        return idNumber++;
    }

    public SCENE_STATE getState(){
        return state;
    }

    public void start(){
        stop();
        frame = 0;
        uframe = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            originalEntities.forEach((id, e) -> {
                addToRun(e.copy());
            });
        }else{
            for(SceneEntity e : originalEntities.values()){
                addToRun(e.copy());
            }
        }
        /*if(debug){
            for (SceneEntity e: runningEntities.values()) {
                Log.i("Entidad inicial", e.toString());
            }
        }*/

        this.state = SCENE_STATE.STATE_RUNNING;
    }

    public void update(){
        if(state == SCENE_STATE.STATE_RUNNING){
            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    runningEntities.forEach((id, e) -> e.update());
                }else{
                    for(SceneEntity e : runningEntities.values()){
                        e.update();
                    }
                }
            }catch (ConcurrentModificationException e){
                Log.i("List modified", "");
            }
            uframe++;
        }
    }

    public void render(Canvas canvas){
        if(state == SCENE_STATE.STATE_RUNNING){
            canvas.drawColor(Color.BLACK);
            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    entriesSortedByValues(runningEntities).forEach((e) -> e.getValue().render(canvas));
                    //runningEntities.forEach((id, e) -> e.render(canvas));
                }else{
                    for(Map.Entry<String, SceneEntity> e : entriesSortedByValues(runningEntities)){
                        e.getValue().render(canvas);
                    }
                }
            }catch(ConcurrentModificationException e){
                Log.i("List modified", "");
            }
            frame++;
        }

    }

    public static float clamp(float val, float min, float max){
        return Math.max(min, Math.min(val, max));
    }

    public static int clampInt( int val, int min, int max){
        return Math.max(min, Math.min(val, max));
    }

    public static float lerp(float val,float target,float factor){
        return val + (target - val) * factor;
    }

    public static float shortAngleDist(float a0, float a1) {
        float max = 360;
        float da = (a1 - a0) % max;
        return 2*da % max - da;
    }

    public static float lerpAngle(float val, float target, float factor){
        return val + shortAngleDist(val, target) * factor;
    }

    static SortedSet<Map.Entry<String, SceneEntity>> entriesSortedByValues(Map<String, SceneEntity> map) {
        SortedSet<Map.Entry<String,SceneEntity>> sortedEntries = new TreeSet<Map.Entry<String, SceneEntity>>(
                new Comparator<Map.Entry<String, SceneEntity>>() {
                    @Override public int compare(Map.Entry<String, SceneEntity> e1, Map.Entry<String, SceneEntity> e2) {
                        float d1 = ((Transform)e1.getValue().getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
                        float d2 = ((Transform)e2.getValue().getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
                        //float d1 = ((Transform)this.runningEntities.get(a).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
                        //float d2 =((Transform)this.runningEntities.get(b).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
                        int compare = Float.compare(d1, d2) * -1;
                        if(compare == 0){
                            compare = ((String)e1.getKey()).compareTo((String)e2.getKey());
                        }
                        return compare;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
    /*
    static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        float d1 = ((Transform)((SceneEntity)e1.getValue()).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
                        float d2 = ((Transform)((SceneEntity)e2.getValue()).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
                        //float d1 = ((Transform)this.runningEntities.get(a).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
                       //float d2 =((Transform)this.runningEntities.get(b).getComponent(Component.COMPONENT_TYPE.TRANSFORM)).depth;
                        int compare = Float.compare(d1, d2) * -1;
                        if(compare == 0){
                            compare = ((String)e1.getKey()).compareTo((String)e2.getKey());
                        }
                        return compare;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }*/
}
