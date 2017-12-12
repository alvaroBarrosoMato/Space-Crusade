package alvaro.daniel.space_crusade;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Dany on 10/12/2017.
 */

abstract public class MonoBehaviour implements Callable {
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
}
