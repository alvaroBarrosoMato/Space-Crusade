package alvaro.daniel.space_crusade;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dany on 10/12/2017.
 */

public class Behaviour extends Component {
    public static enum BEHAVIOUR_TYPE{
        BEHAVIOUR_ONCREATE, BEHAVIOUR_ONUPDATE, BEHAVIOUR_ONDESTROY
    }
    List<MonoBehaviour> onCreate;
    List<MonoBehaviour> onUpdate;
    List<MonoBehaviour> onDestroy;
    boolean CREATE_FLAG = false;

    Map<String, Object> memory;

    public Behaviour(MonoBehaviour[] onCreateBeh, MonoBehaviour[] onUpdateBeh, MonoBehaviour[] onDestroyBeh, Map<String, Object>... memory){
        super(COMPONENT_TYPE.BEHAVIOUR);
        if(memory.length == 1){
            this.memory = memory[0];
        }else{
            this.memory = new HashMap();
        }
        this.onCreate = new ArrayList<>();
        this.onUpdate = new ArrayList<>();
        this.onDestroy = new ArrayList<>();
        for (MonoBehaviour mb: onCreateBeh) {
            addBehaviour(mb, BEHAVIOUR_TYPE.BEHAVIOUR_ONCREATE);
        }

        for (MonoBehaviour mb: onUpdateBeh) {
            addBehaviour(mb, BEHAVIOUR_TYPE.BEHAVIOUR_ONUPDATE);
        }
        for (MonoBehaviour mb: onDestroyBeh) {
            addBehaviour(mb, BEHAVIOUR_TYPE.BEHAVIOUR_ONDESTROY);
        }
    }

    public Behaviour(){
        this(new MonoBehaviour[0], new MonoBehaviour[0], new MonoBehaviour[0]);
    }

    public Behaviour(MonoBehaviour[] onUpdate, Map<String, Object> memory){
            this(new MonoBehaviour[0], onUpdate, new MonoBehaviour[0], memory);
    }

    public Behaviour(MonoBehaviour[] onUpdate){
        this(new MonoBehaviour[0], onUpdate, new MonoBehaviour[0]);
    }

    public Behaviour(MonoBehaviour[] onCreate, MonoBehaviour[] onUpdate){
        this(onCreate, onUpdate, new MonoBehaviour[0]);
    }

    public Behaviour(MonoBehaviour[] onCreate, MonoBehaviour[] onUpdate, Map<String, Object> memory){
        this(onCreate, onUpdate, new MonoBehaviour[0], memory);
    }

    public Behaviour(Behaviour original){
        this(original.onCreate.toArray(new MonoBehaviour[original.onCreate.size()]), original.onUpdate.toArray(new MonoBehaviour[original.onUpdate.size()]), original.onDestroy.toArray(new MonoBehaviour[original.onDestroy.size()]));
        /*for(MonoBehaviour mb : original.onCreate){
            //this.onCreate.add(mb.clone());
            //this.memory.put(e.getKey(), e.getValue())
        }*/
        //this.memory = new HashMap<>();
        this.CREATE_FLAG = false;
        this.memory.putAll(original.memory);
    }

    public Behaviour copy(){
        return new Behaviour(this);
    }

    public void addBehaviour(@NonNull MonoBehaviour mb,@NonNull BEHAVIOUR_TYPE type){
        mb.behaviourRef = this;
        mb.m = this.memory;
        mb.e = this.entity;
        switch(type){
            case BEHAVIOUR_ONCREATE:
                this.onCreate.add(mb);
                break;
            case BEHAVIOUR_ONUPDATE:
                this.onUpdate.add(mb);
                break;

            case BEHAVIOUR_ONDESTROY:
                this.onDestroy.add(mb);
                break;
        }
    }

    public void update(){
        if(!CREATE_FLAG){
            for (MonoBehaviour r: onCreate) {
                r.call();
            }
            CREATE_FLAG = true;
        }else {
            for (MonoBehaviour r : onUpdate) {
                r.call();
            }
        }
    }

    public void destroy(){
        for (MonoBehaviour r: onDestroy) {
            r.call();
        }
    }

    @Override
    public void setEntity(SceneEntity e){
        super.setEntity(e);
        for (MonoBehaviour mb: this.onCreate) {
            mb.e = this.entity;
        }
        for (MonoBehaviour mb: this.onUpdate) {
            mb.e = this.entity;
        }
        for (MonoBehaviour mb: this.onUpdate) {
            mb.e = this.entity;
        }
    }
}
