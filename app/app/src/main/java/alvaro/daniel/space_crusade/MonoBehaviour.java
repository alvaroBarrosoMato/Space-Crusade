package alvaro.daniel.space_crusade;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Dany on 10/12/2017.
 */


public class MonoBehaviour{
    Behaviour behaviourRef;
    Map<String, Object> m;
    SceneEntity e;
    Callable script;

    public MonoBehaviour(Map<String, Object> memory, Callable c){
        this.behaviourRef = null;
        this.e = null;
        this.m = memory;
        this.script = c;
    }

    public MonoBehaviour(Callable c){
        this(new HashMap<String, Object>(), c);
    }

    public MonoBehaviour(MonoBehaviour original){
        this(original.script);
        this.behaviourRef = null;
        this.e = null;
        m = new HashMap<String, Object>();
    }

    public MonoBehaviour copy(){
        return new MonoBehaviour(this);
    }

    public Object execute(){
        try {
            return script.call();
        } catch (Exception e1) {
            Log.e("Monobehaviour Error ", "error executin callable");
            e1.printStackTrace();
        }
        return null;
    }
}

/*abstract public class MonoBehaviour implements Callable {
    Behaviour behaviourRef;
    Map<String, Object> m;
    SceneEntity e;

    public MonoBehaviour(Behaviour ref){
        this.behaviourRef = ref;
    }

    public MonoBehaviour(){
        behaviourRef = null;
        m = new HashMap<>();
        e = null;
    }

    @Override
    abstract public Object call();

    public MonoBehaviour copy(){
        return new MonoBehaviour() {
            @Override
            public Object call() {
                return null;
            }
        }
    }
}*/
