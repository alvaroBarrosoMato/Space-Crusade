package alvaro.daniel.space_crusade;

import android.hardware.Sensor;

/**
 * Created by Dany on 10/12/2017.
 */

public class Input {
    boolean touchUp;
    Vector2 touchPos;
    float[] orientations;

    public Input(){
        this.touchUp = false;
        this.touchPos = new Vector2(-1, -1);
        orientations = new float[3];
    }

    public void lateUpdate(){
        touchUp = false;
        touchPos = new Vector2(-1, -1);
    }
}
