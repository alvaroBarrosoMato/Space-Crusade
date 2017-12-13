package alvaro.daniel.space_crusade;

import android.util.Log;

/**
 * Created by Dany on 09/12/2017.
 */

public class Kinematic extends Component {
    Vector2 speed;
    Vector2 friction;
    Vector2 acceleration;
    float rotSpeed;
    float rotFriction;
    float rotAcceleration;
    Vector2 slowMove;
    float slowRot;


    public Kinematic(Vector2 speed, Vector2 acc, Vector2 fric, float rotSpeed, float rotAcceleration, float rotFriction){
        super(COMPONENT_TYPE.KINEMATIC);
        this.speed = speed;
        this.friction = fric;
        this.acceleration = acc;
        this.rotSpeed = rotSpeed;
        this.rotAcceleration = rotAcceleration;
        this.rotFriction = rotFriction;
        this.slowMove = new Vector2(1f, 1f);
        this.slowRot = 0;
    }

    public Kinematic(){
        this(new Vector2(), new Vector2(), new Vector2(0,0), 0, 0 ,0);
    }

    public Kinematic(Vector2 speed){
        this(speed, new Vector2(), new Vector2(0,0),0,0,0);
    }

    public Kinematic(Vector2 speed, float rotSpeed){
        this(speed, new Vector2(), new Vector2(0,0),rotSpeed,0,0);
    }

    public Kinematic(Kinematic original){
        this(original.speed.copy(), original.acceleration.copy(), original.friction.copy(), original.rotSpeed, original
        .rotAcceleration, original.rotFriction);
        this.slowMove = original.slowMove.copy();
        this.slowRot = original.slowRot;
    }

    public Kinematic copy(){
        return new Kinematic(this);
    }

    public void update(){
        move();
        applyAcceleration();
        applyFriction();
    }

    public void applyAcceleration(){
        this.speed.add(this.acceleration);
        this.rotSpeed += this.rotAcceleration;
    }

    public void applyFriction(){
        this.speed = Vector2.lerp(this.speed, new Vector2(), this.friction);
        this.rotSpeed = Scene.lerp(this.rotSpeed, 0, this.rotFriction);
    }

    public void move(){
        if(this.speed.magnitude() != 0){
            ((Transform)this.entity.getComponent(COMPONENT_TYPE.TRANSFORM)).translate(this.speed.copy().multiply(this.slowMove));
            /*if(entity.scene.debug && entity.id.equals("space_ship")){
                Log.i("speed", speed.toString());
                Log.i("acc", acceleration.toString());
            }*/
        }
        if(this.rotSpeed != 0){
            ((Transform)this.entity.getComponent(COMPONENT_TYPE.TRANSFORM)).rotate(this.rotSpeed * slowRot);
        }

    }
}
