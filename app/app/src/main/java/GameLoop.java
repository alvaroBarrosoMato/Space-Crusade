import alvaro.daniel.space_crusade.Scene;

import static android.os.SystemClock.elapsedRealtime;

/**
 * Created by Dany on 08/12/2017.
 */

public class GameLoop {
    boolean running;
    double lag, elapsed;
    long init, current, previus;
    int MS_PER_UPDATE, gameClock;
    Scene myScene;

    public GameLoop(int ms_update){
        running = false;
        lag = 0;
        gameClock = 1;
        init = elapsedRealtime();
        previus = init;
        MS_PER_UPDATE = ms_update;
    }

    public GameLoop(){
        this(60);
    }

    public void start(Scene myScene){
        this.myScene = myScene;
        running = true;
        while(running){

        }
    }

}
