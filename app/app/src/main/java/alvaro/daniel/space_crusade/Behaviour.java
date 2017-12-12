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
        for (MonoBehaviour cmb: onCreateBeh) {
            cmb.behaviourRef = this;
            cmb.m = this.memory;
            cmb.e = this.entity;
            this.onCreate.add(cmb);
        }

        for (MonoBehaviour umb: onUpdateBeh) {
            umb.behaviourRef = this;
            umb.m = this.memory;
            umb.e = this.entity;
            this.onUpdate.add(umb);
        }
        for (MonoBehaviour dmb: onDestroyBeh) {
            dmb.behaviourRef = this;
            dmb.m = this.memory;
            dmb.e = this.entity;
            this.onDestroy.add(dmb);
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
        this((MonoBehaviour[])original.onCreate.toArray(), (MonoBehaviour[])original.onUpdate.toArray(), (MonoBehaviour[])original.onDestroy.toArray(), new HashMap<>(original.memory));
    }

    public Behaviour copy(){
        return new Behaviour(this);
    }

    public void addBehaviour(@NonNull MonoBehaviour mb,@NonNull BEHAVIOUR_TYPE type){
        mb.behaviourRef = this;
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
        if(CREATE_FLAG){
            for (MonoBehaviour r: onCreate) {
                r.call();
            }
        }
        for (MonoBehaviour r: onUpdate) {
            r.call();
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
