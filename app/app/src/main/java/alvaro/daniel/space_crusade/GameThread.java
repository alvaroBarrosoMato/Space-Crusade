package alvaro.daniel.space_crusade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static android.os.SystemClock.elapsedRealtime;

/**
 * Created by Dany on 07/12/2017.
 */

public class GameThread extends Thread {
        boolean mRun;
        Canvas mCanvas;
        SurfaceHolder surfaceHolder;
        Context context;
        MyCanvas mSurface;
        //GameLoop
        double lag, elapsed, gameClock;
        long init, current, previus, fpsTimer;
        int MS_PER_UPDATE;
        Scene myScene;
        float scale;
        int fps = 0;
        int updateFps = 0;
        public static Map<String, Sprite> sprites;
        public static Map<String, Behaviour> behaviours;

    public GameThread(SurfaceHolder sHolder, Context ctx, MyCanvas canvas,int ms_update){
        surfaceHolder = sHolder;
        context = ctx;
        mRun = false;
        mSurface = canvas;
        lag = 0;
        gameClock = 1;
        MS_PER_UPDATE = ms_update;
    }

    public GameThread(SurfaceHolder sHolder, Context ctx, MyCanvas canvas){
        this(sHolder, ctx, canvas, 10);
    }

    void setRunning(boolean bRun){
        this.mRun = bRun;
    }

    public void stopGame(){
        this.mRun = false;
        myScene.stop();
        myScene = null;
    }

    public void restartGame(){
        myScene.start();
    }

    @Override
    public void run(){
        super.run();
        loadScene();
        myScene.start();
        init = elapsedRealtime();
        previus = init;
        fpsTimer = init + 1000;
        GameActivity.input.lateUpdate();
        while(mRun && mSurface != null){
            current = elapsedRealtime();
            elapsed = (current - previus);
            previus = current;
            lag += elapsed;

            //bucle interno, se repite hasta que el tiempo de juego alcanza el real
            while (lag >= MS_PER_UPDATE && mRun && mSurface != null){
                myScene.update();
                gameClock += MS_PER_UPDATE;
                lag -= MS_PER_UPDATE;
                updateFps++;
                GameActivity.input.lateUpdate();
            }
            if(myScene.getState() == Scene.SCENE_STATE.STATE_RUNNING){
                mCanvas = surfaceHolder.lockCanvas();
                if(mCanvas != null){
                    myScene.render(mCanvas);
                    //mSurface.doDraw(mCanvas, myScene);
                    surfaceHolder.unlockCanvasAndPost(mCanvas);
                }
                fps++;
                if(current >= fpsTimer){
                    fpsTimer = current + 1000;
                    SceneEntity e;
                    if((e = myScene.getEntity("fps")) != null){
                        ((Sprite)e.getComponent(Component.COMPONENT_TYPE.SPRITE)).text = "FPS: "+fps+" UFPS: "+updateFps;
                    }
                    fps = 0;
                    updateFps = 0;
                }
            }
            if(interrupted()){
                stopGame();
                return;
            }
        }
    }

    private void showRealClock(){
        double realClock = elapsedRealtime() - init;
        realClock /= 1000;
        int hour = 0, min = 0;
        if(realClock >= 3600) {
            hour = (int) realClock / 3600;
        }
        realClock -= hour * 3600;
        if(realClock >= 60) {
            min = (int) realClock / 60;
        }
        realClock -= min*60;
        Log.i("GameLoop: ", "Elapsed: "+elapsed+" lag: "+lag);
        Log.i("Real Time: ",hour+" : "+ min +" : "+realClock);
    }

    private void showGameClock(){
        double clock = gameClock / 1000.00f;
        int hour = 0, min = 0;
        if(clock >= 3600) {
            hour = (int) clock / 3600;
        }
        clock -= hour * 3600;
        if(clock >= 60) {
            min = (int) clock / 60;
        }
        clock -= min*60;

        Log.i("Game Time: ",hour+" : "+ min +" : "+clock);
    }

    private void loadScene(){
        int w = mSurface.getWidth();
        int h = mSurface.getHeight();
        sprites = getSprites(w, h);
        behaviours = getBehaviours();
        myScene = new Scene(context, "Game", w, h*2, w, h);
        //myScene.debug = true;
        //landscape
        float sprW = sprites.get("landscape").width;
        float sprH = sprites.get("landscape").height;
        SceneEntity landscape = new SceneEntity("landscape", myScene, Scene.TAGS.DEFAULT, new Transform(new Vector2(w/2, h-(sprH/2)), -2));
        landscape.addComponent(sprites.get("landscape"));
        landscape.addComponent(new Kinematic(new Vector2(0, 0)));
        myScene.addEntity(landscape);

        //background
        sprW = sprites.get("background").width;
        sprH = sprites.get("background").height;
        SceneEntity bg = new SceneEntity("background", myScene, Scene.TAGS.DEFAULT, new Transform(new Vector2(w/2, h/2), 5));
        Sprite bgSpr = sprites.get("background");
        bgSpr.elemUI = true;
        bg.addComponent(bgSpr);
        myScene.addEntity(bg);

        //landscape clouds
        sprW = sprites.get("landscape_clouds").width;
        sprH = sprites.get("landscape_clouds").height;
        SceneEntity lc = new SceneEntity("landscape_clouds", myScene, Scene.TAGS.DEFAULT, new Transform(new Vector2(-w/5, h/2 + sprH/2), -4));
        lc.addComponent(sprites.get("landscape_clouds"));
        lc.addComponent(new Kinematic(new Vector2(convert(1.0f), 0)));
        myScene.addEntity(lc);

        //landscape bush
        sprW = sprites.get("landscape_bush").width;
        sprH = sprites.get("landscape_bush").height;
        SceneEntity lb = new SceneEntity("landscape_bush", myScene, Scene.TAGS.DEFAULT, new Transform(new Vector2(w/2, h - (sprH/2)), -8));
        lb.addComponent(sprites.get("landscape_bush"));
        lb.addComponent(new Kinematic(new Vector2(0, 0)));
        myScene.addEntity(lb);

        //control center
        sprW = sprites.get("control_center").width;
        sprH = sprites.get("control_center").height;
        //SceneEntity control = new SceneEntity("control_center", myScene, Scene.TAGS.DEFAULT, new Transform(new Vector2((float)w * (410.0f/1080.0f) , (float)h* (1605.0f/1920.0f)), -5));
        SceneEntity control = new SceneEntity("control_center", myScene, Scene.TAGS.DEFAULT, new Transform(new Vector2(convert(430.0f) , h - convert(420.0f)), -6));
        control.addComponent(sprites.get("control_center"));
        control.addComponent(new Kinematic(new Vector2(0, 0)));
        myScene.addEntity(control);

        //Space Ship
        sprW = sprites.get("space_ship").width;
        sprH = sprites.get("space_ship").height;
        //SceneEntity spaceShip = new SceneEntity("space_ship", myScene, Scene.TAGS.PLAYER, new Transform(new Vector2(w/2, h-(sprH)), -10, 90, new Vector2(0.5f,0.5f)));
        SceneEntity spaceShip = new SceneEntity("space_ship", myScene, Scene.TAGS.PLAYER, new Transform(new Vector2(w/2, h-(sprH)), -10));
        spaceShip.addComponent(sprites.get("space_ship"));
        spaceShip.addComponent(new Kinematic(new Vector2(0, 0)));
        spaceShip.addComponent(new Collider(sprW*0.5f, sprH, new Vector2(sprW * 0.5f * -0.5f, sprH * -0.5f)));
        spaceShip.addComponent(behaviours.get("space_ship"));
        myScene.addEntity(spaceShip);

        //Launch button
        sprW = sprites.get("launch_button").width;
        sprH = sprites.get("launch_button").height;
        SceneEntity launchButton = new SceneEntity("launch_button", myScene, Scene.TAGS.DEFAULT, new Transform(new Vector2(w - sprW/2, h - sprH/2), -20));
        launchButton.addComponent(sprites.get("launch_button"));
        //launchButton.addComponent(new Kinematic(new Vector2(0, 0)));
        launchButton.addComponent(behaviours.get("launch_button"));
        launchButton.addComponent(new Collider(convert(150), convert(150), new Vector2(convert(150) * -0.5f, convert(150) * -0.5f)));
        launchButton.addComponent(new Kinematic(new Vector2(0, 0)));
        myScene.addEntity(launchButton);

        //countdown
        SceneEntity countdown = new SceneEntity("countdown", myScene, Scene.TAGS.UI, new Transform(new Vector2(w/2, h/3.2f), -50));
        Sprite countdownSpr = new Sprite(new Bitmap[0], 0, true, new Vector2());
        countdownSpr.textPaint.setColor(Color.parseColor("#a3190f"));
        countdownSpr.textPaint.setTextSize(dpToPx(120));
        countdownSpr.text = "5";
        countdown.addComponent(countdownSpr);
        countdown.addComponent(behaviours.get("countdown"));
        myScene.addEntity(countdown);

        //*****************Hud
        //Base
        sprW = sprites.get("hud_base").width;
        sprH = sprites.get("hud_base").height;
        SceneEntity hudBase = new SceneEntity("hud_base", myScene, Scene.TAGS.UI, new Transform(new Vector2(w-sprW, convert(50)+sprH/2), -49));
        hudBase.addComponent(sprites.get("hud_base"));
        hudBase.addComponent(behaviours.get("hud_controllerSpeed").copy());
        myScene.addEntity(hudBase);
        //hp
        SceneEntity hudHp = new SceneEntity("hud_hp", myScene, Scene.TAGS.UI, new Transform(new Vector2(w-sprW, convert(50)+sprH), -50));
        hudHp.addComponent(sprites.get("hud_hp"));
        hudHp.addComponent(behaviours.get("hud_controllerHP").copy());
        myScene.addEntity(hudHp);
        //fuel
        SceneEntity hudFuel = new SceneEntity("hud_fuel", myScene, Scene.TAGS.UI, new Transform(new Vector2(w-sprW, convert(50)+sprH), -50));
        hudFuel.addComponent(sprites.get("hud_fuel"));
        hudFuel.addComponent(behaviours.get("hud_controllerFuel").copy());
        myScene.addEntity(hudFuel);

        /*//spiner
        SceneEntity spiner = new SceneEntity("spiner", myScene, Scene.TAGS.UI, new Transform(new Vector2(w/2, h/2), -50));
        spiner.addComponent(sprites.get("spiner"));
        myScene.addEntity(spiner);*/

        //GameController
        SceneEntity gameController = new SceneEntity("game_controller", myScene, Scene.TAGS.DEFAULT);
        gameController.addComponent(behaviours.get("game_controller"));
        myScene.addEntity(gameController);

        //FPS counter
        /*SceneEntity fps = new SceneEntity("fps", myScene, Scene.TAGS.UI, new Transform(new Vector2(w - w/5, 40), -9));
        Sprite fpsSpr = new Sprite(new Bitmap[0], 0, true, new Vector2());
        fpsSpr.text = "FPS: 00";
        fps.addComponent(fpsSpr);
        myScene.addEntity(fps);*/
    }

    private Map<String, Sprite>  getSprites(int w, int h){
        Map<String, Sprite> sprites = new HashMap();
        Bitmap landScape = BitmapFactory.decodeResource(context.getResources(), R.drawable.landscape);
        scale = (w) / (float)landScape.getWidth();
        //Log.i("Bitmap size", "width: "+landScape.getWidth() + ", height: "+ landScape.getHeight()+", density: "+landScape.getDensity());
        //Log.i("Canvas size", "width: "+w + ", height: "+h+", scale: "+scale);

        //landscape
        landScape = landScape.createScaledBitmap(landScape, (int)(landScape.getWidth() * scale), (int)(landScape.getHeight()*scale), false);
        sprites.put("landscape", new Sprite(new Bitmap[]{landScape}, 0, false, new Vector2(-landScape.getWidth()/2,-landScape.getHeight()/2)));

        SpriteResource[] resources = {new SpriteResource("landscape_clouds", new int[]{R.drawable.clouds_landscape}),
                new SpriteResource("landscape_bush", new int[]{R.drawable.landscape_bush1}),
                new SpriteResource("control_center", new int[]{R.drawable.control_center}),
                new SpriteResource("space_ship", new int[]{R.drawable.space_ship}),
                new SpriteResource("launch_button", new int[]{R.drawable.launch_button_rest, R.drawable.launch_button_presed}),
                new SpriteResource("background", new int[]{R.drawable.background_game1, R.drawable.background_game2, R.drawable.background_game3}),
                new SpriteResource("hud_base", new int[]{R.drawable.hud_base}, 0, true),
                new SpriteResource("hud_hp", new int[]{R.drawable.hud_hp}, 0, true, new Vector2(-0.5f, -1.0f)),
                new SpriteResource("hud_fuel", new int[]{R.drawable.hud_fuel}, 0, true, new Vector2(-0.5f, -1.0f)),
                new SpriteResource("fire_flicker", new int[]{R.drawable.fire_flicker1, R.drawable.fire_flicker2, R.drawable.fire_flicker3, R.drawable.fire_flicker4, R.drawable.fire_flicker5},
                        0.4f, false, new Vector2(-0.5f, 1.1f )),
                new SpriteResource("explosion", new int[]{R.drawable.explosion1, R.drawable.explosion2, R.drawable.explosion3, R.drawable.explosion4, R.drawable.explosion5, R.drawable.explosion6
                        , R.drawable.explosion7, R.drawable.explosion8, R.drawable.explosion9, R.drawable.explosion10, R.drawable.explosion11, R.drawable.explosion12, R.drawable.explosion13
                        , R.drawable.explosion14, R.drawable.explosion15, R.drawable.explosion16},0.4f, false, new Vector2(-0.5f, -0.5f )),
                new SpriteResource("fuel_barrel", new int[]{R.drawable.speaker}),
                /*new SpriteResource("spiner", new int[]{R.drawable.spiner1, R.drawable.spiner2, R.drawable.spiner3, R.drawable.spiner4, R.drawable.spiner5, R.drawable.spiner6, R.drawable.spiner7, R.drawable.spiner8},
                        0.1f, true, new Vector2(-0.5f, -0.5f )),*/
        };

        //int[] sprIds = {R.drawable.clouds_landscape, R.drawable.space_ship, R.drawable.launch_button_rest};

        for(SpriteResource r : resources){
            Bitmap[] b = new Bitmap[r.resourcesId.length];
            for(int i = 0; i < r.resourcesId.length; i++){
                b[i] = BitmapFactory.decodeResource(context.getResources(), r.resourcesId[i]);
                b[i] = b[i].createScaledBitmap(b[i], (int)(b[i].getWidth() * scale), (int)(b[i].getHeight() * scale), false);
            }
            sprites.put(r.id, new Sprite(b, r.image_speed, r.ui, new Vector2(b[0].getWidth(),b[0].getHeight()).multiply(r.offsetMultiplicator)));
        }

       /* //landscape clouds
        Bitmap lc = BitmapFactory.decodeResource(context.getResources(), R.drawable.clouds_landscape);
        landScape = landScape.createScaledBitmap(landScape, (int)(landScape.getWidth() * scale), (int)(landScape.getHeight()*scale), false);
        //Space Ship
        Bitmap spaceShip = BitmapFactory.decodeResource(context.getResources(), R.drawable.space_ship);
        spaceShip = spaceShip.createScaledBitmap(spaceShip, (int)(spaceShip.getWidth()*scale), (int)(spaceShip.getHeight()*scale), false);
        sprites.put("space_ship", new Sprite(new Bitmap[]{spaceShip}, 0, new Vector2(-spaceShip.getWidth()/2,-spaceShip.getHeight()/2)));
        //Launch button
        Bitmap[] launchButton = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(), R.drawable.launch_button_rest),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.launch_button_presed)};
        for(int i = 0; i < launchButton.length; i++){
            Bitmap b = launchButton[i];
            launchButton[i] = b.createScaledBitmap(b, (int)(b.getWidth()*scale), (int)(b.getHeight()*scale), false);
        }
        sprites.put("launch_button", new Sprite(launchButton, 0, new Vector2(-launchButton[0].getWidth()/2,-launchButton[0].getHeight()/2)));*/
        return sprites;
    }

    public Map<String, Behaviour> getBehaviours(){
        Map<String, Behaviour> behaviours = new HashMap();

        //Launch button
        MonoBehaviour launchButton = new MonoBehaviour() {
            @Override
            public Object call(){
                if(behaviourRef == null || e == null)
                    return null;
                boolean pressed = (boolean)m.get("pressed");
                if(!pressed){
                    Input i = GameActivity.input;
                    if(i.touchUp){
                        //Log.i("Touch Detectado", "posicion: "+i.touchPos.x +", "+i.touchPos.y);
                        //Log.i("Update", "update desde la entidad: "+behaviourRef.entity.getId() + ", prueba de memoria: "+behaviourRef.memory.get(("msg")));
                        if(Collider.checkPointCollision(i.touchPos, (Collider)e.getComponent(Component.COMPONENT_TYPE.COLLIDER))){
                            //Log.i("Boton pulsado", "lanzamiento");
                            ((Sprite)e.getComponent(Component.COMPONENT_TYPE.SPRITE)).setImageIndex(1);
                            m.put("pressed", true);
                            Behaviour gc = (Behaviour)e.scene.getEntity("game_controller").getComponent(Component.COMPONENT_TYPE.BEHAVIOUR);

                            if(gc != null){
                                gc.memory.put("start", true);
                            }
                        }
                    }
                }
                return null;
            }
        };
        Behaviour launch = new Behaviour(new MonoBehaviour[]{launchButton});
        launch.memory.put("pressed", false);
        behaviours.put("launch_button", launch);

        //Countdown
        MonoBehaviour countdownMB = new MonoBehaviour() {
            @Override
            public Object call(){
                if(behaviourRef == null || e == null)
                    return null;
                boolean cd = (boolean)m.get("cd");
                if(cd){
                    m.put("timer", elapsedRealtime());
                    m.put("cd", false);
                    m.put("time_target_refresh", elapsedRealtime());
                }
                if(m.containsKey("timer")){
                    long timer = (long)m.get("timer");
                    long timer_refresh = (long)m.get("time_target_refresh");
                    int time_target = (int)m.get("time_target");
                    m.put("time_target", (int)(time_target-(elapsedRealtime() - timer_refresh)));
                    m.put("time_target_refresh", elapsedRealtime());

                    if(elapsedRealtime() >= timer+1000){
                        m.put("cd", true);
                        Sprite s = (Sprite)e.getComponent(Component.COMPONENT_TYPE.SPRITE);
                        int count = (int)m.get("count") -1;
                        m.put("count", count);
                        s.text = Integer.toString(count);
                        if(count < 0){
                            e.scene.getEntity("game_controller").getMemory().put("start", true);
                            e.scene.getEntity("launch_button").getMemory().put("pressed", true);
                            e.destroy();
                        }
                    }
                }
                return null;
            }
        };
        Behaviour countdown = new Behaviour(new MonoBehaviour[]{countdownMB});
        countdown.memory.put("cd", true);
        countdown.memory.put("count", 5);
        countdown.memory.put("time_target", 5000);
        countdown.memory.put("time_target_refresh", (long)0);
        behaviours.put("countdown", countdown);

        //Game controller
        MonoBehaviour gcMB = new MonoBehaviour() {
            @Override
            public Object call(){
                if(behaviourRef == null || e == null)
                    return null;

                SceneEntity ss = e.scene.getEntity("space_ship");
                int hp = (int)ss.getMemory().get("hp");
                float fuel = (float)ss.getMemory().get("fuel");
                //game over
                if(hp <= -1 || fuel <= -1){
                    myScene.pause();
                    float distance = (float)m.get("distance");

                    // Get a handler that can be used to post to the main thread
                    Handler mainHandler = new Handler(context.getMainLooper());

                    Runnable gameOverRunnable = new Runnable() {
                        @Override
                        public void run() {
                            ((GameActivity)context).toggleGameOver(true, (int)distance);
                        } // This is your code
                    };
                    mainHandler.post(gameOverRunnable);
                    return null;
                }

                boolean start = (boolean)m.get("start");
                if(start){
                    SceneEntity countdown =  e.scene.getEntity("countdown");
                    int launch_error = 0;
                    if(countdown != null){
                        launch_error = (int)countdown.getMemory().get("time_target");
                    }else{
                        launch_error = 6000;
                    }
                    m.put("launch_error", launch_error);
                   if(countdown != null)
                       countdown.destroy();
                   //lanzamos el cohete
                    if(ss != null) {
                        //ss.getMemory().put("launch", true);
                        ss.getMemory().put("launch_state", 1);
                        ss.getMemory().put("launch_error", launch_error);
                    }
                    m.put("start", false);
                }
                boolean launched = (boolean)m.get("launched");
                if(launched){
                    Kinematic ssk = (Kinematic)ss.getComponent(Component.COMPONENT_TYPE.KINEMATIC);
                    Kinematic landscape = (Kinematic)e.scene.getEntity("landscape").getComponent(Component.COMPONENT_TYPE.KINEMATIC);
                    Kinematic landscapeClouds = (Kinematic)e.scene.getEntity("landscape_clouds").getComponent(Component.COMPONENT_TYPE.KINEMATIC);
                    Kinematic button = (Kinematic)e.scene.getEntity("launch_button").getComponent(Component.COMPONENT_TYPE.KINEMATIC);
                    Kinematic bush = (Kinematic)e.scene.getEntity("landscape_bush").getComponent(Component.COMPONENT_TYPE.KINEMATIC);
                    Kinematic controlCenter = (Kinematic)e.scene.getEntity("control_center").getComponent(Component.COMPONENT_TYPE.KINEMATIC);
                    landscape.speed.y = -ssk.speed.y;
                    landscapeClouds.speed.y = -ssk.speed.y/2;
                    button.speed.y = -ssk.speed.y * 2f;
                    bush.speed.y = -ssk.speed.y * 1.5f;
                    controlCenter.speed.y = -ssk.speed.y *1.2f;
                    ssk.slowMove = new Vector2(1, 0);
                    ssk.friction = new Vector2( 0.07f,0.01f);
                    //ssk.acceleration.y = -0.5f;
                    m.put("launched", false);
                    m.put("state", 1);
                }

                int state = (int)m.get("state");
                if(state > 0){
                    if(e.scene.uframe % 400 == 0){
                        e.scene.addEntity(createBarrel(new Vector2(e.scene.width * (float)Math.random(), -100f), -9));
                    }
                }
                return null;
            }
        };
        Behaviour gc = new Behaviour(new MonoBehaviour[]{gcMB});
        gc.memory.put("start", false);
        gc.memory.put("launched", false);
        gc.memory.put("distance", 0.0f);
        gc.memory.put("ss_speed", 0.0f);
        gc.memory.put("state", 0);
        behaviours.put("game_controller", gc);

        //SpaceShip
        MonoBehaviour ssMB = new MonoBehaviour() {
            @Override
            public Object call(){
                if(behaviourRef == null || e == null)
                    return null;
                Kinematic ssKin = (Kinematic)e.getComponent(Component.COMPONENT_TYPE.KINEMATIC);
                Transform ssTran = (Transform)e.getComponent(Component.COMPONENT_TYPE.TRANSFORM);
                int hp = (int)m.get("hp");
                float fuel = (float)m.get("fuel");

                //boolean launch = (boolean)behaviourRef.memory.get("launch");
                int launchState = (int)behaviourRef.memory.get("launch_state");
                if(launchState == 1){
                    int launch_error = (int)m.get("launch_error");
                    if(ssKin != null){
                        ssKin.acceleration.y = -0.005f;
                        ssKin.friction.y = 0.01f;
                    }
                    if(launch_error > 3000){
                        m.put("destroying", true);
                        m.put("acc_timer", 300);
                    }else if(launch_error > 1500){
                        m.put("acc_timer", 200);
                    }else if(launch_error > 500){
                        m.put("acc_timer", 100);
                    }else{
                        m.put("acc_timer", 20);
                    }
                    m.put("launch_state", 2);

                    /*if(launchState >= 6){
                        //no se ha lanzado antes de la cuenta atras, el cohete explota
                        //ssKin.rotSpeed = 1;
                        SceneEntity explosion = new SceneEntity("explosion", myScene, Scene.TAGS.DEFAULT, new Transform(ssTran.position, -11, 0, new Vector2(2.5f ,2.5f)));
                        explosion.addComponent(sprites.get("explosion"));
                        e.scene.addEntity(explosion);
                    }*/
                    SceneEntity fire = new SceneEntity("fire", myScene, Scene.TAGS.DEFAULT, new Transform(ssTran.position.copy(), -9));
                    fire.addComponent(sprites.get("fire_flicker").copy());
                    Behaviour follow = behaviours.get("follow").copy();
                    follow.memory.put("target", e.id);
                    follow.memory.put("pos_smoothing", 0.9f);
                    follow.memory.put("rot_smoothing", 0.7f);
                    fire.addComponent(follow);
                    e.scene.addEntity(fire);
                    //m.put("launch", false);
                }

                if(launchState == 2){
                    int acc_timer = (int)m.get("acc_timer");
                    if(acc_timer == 0){
                        ssKin.acceleration.y = -0.1f;
                        m.put("launch_state", 3);
                    }else{
                        m.put("acc_timer", acc_timer-1);
                    }
                }

                if(launchState == 3){
                    if(ssTran.position.y <= e.scene.canvas_height/2){
                        e.scene.getEntity("game_controller").getMemory().put("launched", true);
                        ssKin.acceleration.y = -0.3f;
                        m.put("launch_state", 4);
                    }
                }

                if(launchState >= 4){
                    Input i = GameActivity.input;
                    //Log.i("gyroscope", i.orientations[2]+" derecha " + (i.orientations[2] > 20f) + " izda " +(i.orientations[2] < 20f));
                    if(i.orientations[2] > 20f){
                        ssKin.acceleration.x = Scene.clamp(ssKin.acceleration.x +0.05f, 0, 0.2f);
                        //ssKin.rotAcceleration = 0.5f;
                        Log.i("rotacion", ssTran.rotation +" angleDeg "+ssKin.speed.angleDeg());
                        //ssTran.rotation = 90;
                    }else if(i.orientations[2] < -20f){
                        ssKin.acceleration.x = Scene.clamp(ssKin.acceleration.x -0.05f, -0.2f, 0);
                        //ssKin.acceleration.x -= 0.002f;
                        Log.i("rotacion", ssTran.rotation +" angleDeg "+ssKin.speed.angleDeg());
                       // ssKin.rotAcceleration = -0.5f;
                    }else{
                        ssKin.acceleration.x = Scene.lerp(ssKin.acceleration.x, 0, 0.02f);
                    }

                    //ssKin.speed.x = Scene.lerp(ssKin.speed.x, 0f, 0.02f);
                    ssTran.rotation = Scene.lerpAngle(ssTran.rotation, 0, 0.03f);
                    ssTran.rotation = Scene.lerpAngle(ssTran.rotation, ssTran.rotation + ssKin.speed.x, 0.1f);
                    Sprite spr = (Sprite)e.getComponent(Component.COMPONENT_TYPE.SPRITE);
                    if((ssTran.position.x < spr.width/2 && ssKin.speed.x < 0f) || (ssTran.position.x > (e.scene.width - spr.width/2) && ssKin.speed.x > 0)){
                        //ssKin.acceleration.x = 0;
                        ssKin.speed.x = 0;
                    }
                    if(e.scene.frame % 6 == 0){
                        m.put("fuel", (float)fuel-0.1f);
                    }
                }

                boolean destroying = (boolean)m.get("destroying");
                if(destroying){
                    if(e.scene.frame % 6 == 0){
                       m.put("hp", hp-1);
                    }
                    if(e.scene.uframe % 80 == 0){
                        e.scene.addEntity(createExplosion(ssTran.position.copy(), -11, true));
                    }
                }
                return null;
            }
        };
        Behaviour ss = new Behaviour(new MonoBehaviour[]{ssMB});
        ss.memory.put("launch", false);
        ss.memory.put("hp", 100);
        ss.memory.put("fuel", 100.0f);
        ss.memory.put("launch_state", 0);
        ss.memory.put("destroying", false);
        behaviours.put("space_ship", ss);

        // hud controller
        MonoBehaviour hudControllerHPMB = new MonoBehaviour() {
            @Override
            public Object call() {
                if (behaviourRef == null || e == null)
                    return null;
                SceneEntity ss = e.scene.getEntity("space_ship");
                Sprite spr = (Sprite)e.getComponent(Component.COMPONENT_TYPE.SPRITE);
                Transform t = (Transform)e.getComponent(Component.COMPONENT_TYPE.TRANSFORM);
               // SceneEntity gcB = e.scene.getEntity("game_controller");

                int actualHp = (int)ss.getMemory().get("hp");
                int savedHp = (int)m.get("hp");
                if(actualHp != savedHp && actualHp >= 0) {
                    t.scale.y = (float) actualHp / 100.0f;
                    m.put("hp", actualHp);
                }

                return null;
            }
        };
        Behaviour hudControllerHP = new Behaviour(new MonoBehaviour[]{hudControllerHPMB});
        hudControllerHP.memory.put("hp", 100);
        behaviours.put("hud_controllerHP", hudControllerHP);

        MonoBehaviour hudControllerFuelMB = new MonoBehaviour() {
            @Override
            public Object call() {
                if (behaviourRef == null || e == null)
                    return null;
                SceneEntity ss = e.scene.getEntity("space_ship");
                Sprite spr = (Sprite)e.getComponent(Component.COMPONENT_TYPE.SPRITE);
                Transform t = (Transform)e.getComponent(Component.COMPONENT_TYPE.TRANSFORM);
                // SceneEntity gcB = e.scene.getEntity("game_controller");

                float actualFuel = (float)ss.getMemory().get("fuel");
                float savedFuel = (float)m.get("fuel");
                if(actualFuel != savedFuel && actualFuel >= 0) {
                    t.scale.y = actualFuel / 100.0f;
                    m.put("fuel", actualFuel);
                }
                return null;
            }
        };
        Behaviour hudControllerFuel= new Behaviour(new MonoBehaviour[]{hudControllerFuelMB});
        hudControllerFuel.memory.put("fuel", 100.0f);
        behaviours.put("hud_controllerFuel", hudControllerFuel);

        MonoBehaviour hudControllerSpeedMB = new MonoBehaviour() {
            @Override
            public Object call() {
                if (behaviourRef == null || e == null)
                    return null;
                SceneEntity ss = e.scene.getEntity("space_ship");
                Sprite spr = (Sprite)e.getComponent(Component.COMPONENT_TYPE.SPRITE);
                // SceneEntity gcB = e.scene.getEntity("game_controller");

                if(spr.textOffset.magnitude() == 0){
                    spr.textOffset.y = (float)spr.height+dpToPx(20);
                    spr.textPaint.setColor(Color.BLACK);
                    spr.textPaint.setTextAlign(Paint.Align.CENTER);
                }
                spr.text = (int)((Kinematic)ss.getComponent(Component.COMPONENT_TYPE.KINEMATIC)).speed.magnitude()*40f + " Km/H";
                return null;
            }
        };
        Behaviour hudControllerSpeed= new Behaviour(new MonoBehaviour[]{hudControllerSpeedMB});
        behaviours.put("hud_controllerSpeed", hudControllerSpeed);

        //Barrel
        MonoBehaviour barrelMB = new MonoBehaviour() {
            @Override
            public Object call(){
                if(behaviourRef == null || e == null)
                    return null;
                SceneEntity ss =  e.scene.getEntity((String)m.get("space_ship"));
                Collider c = (Collider)e.getComponent(Component.COMPONENT_TYPE.COLLIDER);
                if(ss == null)
                    return null;

                if(Collider.checkStaticCollision(c, (Collider)ss.getComponent(Component.COMPONENT_TYPE.COLLIDER))){
                    float fuel = (float)ss.getMemory().get("fuel");
                    ss.getMemory().put("fuel", Scene.clamp(fuel+20, 0, 100f));
                    e.destroy();
                }
                return null;
            }
        };
        Behaviour barrel= new Behaviour(new MonoBehaviour[]{barrelMB});
        behaviours.put("fuel_barrel", barrel);

        //Follow
        MonoBehaviour followMBcreate = new MonoBehaviour() {
            @Override
            public Object call(){
                if(behaviourRef == null || e == null)
                    return null;
                m.put("target", "");
                m.put("pos_smoothing", 1.0f);
                m.put("rot_smoothing", 1.0f);
                return null;
            }
        };

        MonoBehaviour followMBupdate = new MonoBehaviour() {
            @Override
            public Object call(){
                if(behaviourRef == null || e == null)
                    return null;
                SceneEntity target =  e.scene.getEntity((String)m.get("target"));
                if(target == null)
                    return null;

                float pos_smoot = (float)m.get("pos_smoothing");
                float rot_smoot = (float)m.get("rot_smoothing");
                Transform t = (Transform)e.getComponent(Component.COMPONENT_TYPE.TRANSFORM);

                Vector2 target_pos = ((Transform)target.getComponent(Component.COMPONENT_TYPE.TRANSFORM)).position;
                float target_rot = ((Transform)target.getComponent(Component.COMPONENT_TYPE.TRANSFORM)).rotation;

                t.position = Vector2.lerp(t.position, target_pos, pos_smoot);
                t.rotation = Scene.lerpAngle(t.rotation, target_rot, rot_smoot);
                return null;
            }
        };
        Behaviour follow = new Behaviour(new MonoBehaviour[]{followMBcreate}, new MonoBehaviour[]{followMBupdate});
        behaviours.put("follow", follow);

        //EndAnim
        MonoBehaviour endAnimMB = new MonoBehaviour() {
            @Override
            public Object call(){
                if(behaviourRef == null || e == null)
                    return null;
                Sprite s = (Sprite)e.getComponent(Component.COMPONENT_TYPE.SPRITE);
                if(s != null){
                    if(s.getImage_index() >= s.getImageLength()-1)
                        e.destroy();
                }
                return null;
            }
        };
        Behaviour endAnim = new Behaviour(new MonoBehaviour[]{endAnimMB});
        behaviours.put("end_anim", endAnim);

        return behaviours;
    }

    public SceneEntity createExplosion(Vector2 pos, float depth, boolean random){
        Transform t = new Transform(pos, depth,0, new Vector2(2f,2f));
        if(random){
            t.position.add(new Vector2(((float)Math.random() - 0.5f) * 100, ((float)Math.random() - 0.5f) * 170));
            float scaleRnd = (float)Math.random() * 4.0f;
            t.scale = new Vector2(Scene.clamp( scaleRnd,1, 2.5f), Scene.clamp(scaleRnd, 1,2.5f));
            t.rotation = (float)Math.random()*360;
        }
        SceneEntity explosion = new SceneEntity("explosion#"+myScene.getIdNumber(), myScene, Scene.TAGS.DEFAULT, t);
        explosion.addComponent(sprites.get("explosion").copy());
        explosion.addComponent(behaviours.get("end_anim").copy());
        return explosion;
    }

    public SceneEntity createBarrel(Vector2 pos, float depth){
        Transform t = new Transform(pos, depth);
        Kinematic k = new Kinematic(new Vector2(0f, 1f));
        k.rotSpeed = 2f;


        SceneEntity barrel = new SceneEntity("barrel#"+myScene.getIdNumber(), myScene, Scene.TAGS.DEFAULT, t);
        Sprite spr = sprites.get("fuel_barrel").copy();
        barrel.addComponent(spr);
        barrel.addComponent(behaviours.get("fuel_barrel").copy());
        barrel.addComponent(k.copy());
        barrel.addComponent(new Collider(spr.width, spr.height, new Vector2(-spr.width/2, -spr.height/2)));
        return barrel;
    }

    public int dpToPx(float dp){
        return (int)((dp * context.getResources().getDisplayMetrics().density) + 0.5);
    }

    public int pxToDp(float px){
        return (int)((px/context.getResources().getDisplayMetrics().density) + 0.5);
    }

    public int convert(float px){
        float dp = px/context.getResources().getDisplayMetrics().density;
        return (int) ((dp * context.getResources().getDisplayMetrics().density) + 0.5);
    }
}
