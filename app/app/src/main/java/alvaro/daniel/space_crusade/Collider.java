package alvaro.daniel.space_crusade;

import android.util.Log;

/**
 * Created by Dany on 10/12/2017.
 */

public class Collider extends Component {
    public static enum COLLIDER_TYPE{
        RECT_COLLIDER, CIRCLE_COLLIDER
    }

    float width, height;
    Vector2 offset;
    COLLIDER_TYPE colliderType;


    public Collider(float width, float height, Vector2 offset, COLLIDER_TYPE type ){
        super(COMPONENT_TYPE.COLLIDER);
        this.width = width;
        this.height = height;
        this.offset = offset;
        this.colliderType = type;
    }

    public Collider(float width, float height){
        this(width, height, new Vector2());
    }

    public Collider(float width, float height, Vector2 offset){
        this(width, height, offset, COLLIDER_TYPE.RECT_COLLIDER);
    }

    public Collider(Sprite sprite){
        this(sprite.getWidth(), sprite.getHeight(), sprite.offset);
    }

    public Collider(Collider original){
        this(original.width, original.height, original.offset.copy(), original.colliderType);
    }

    public Collider copy(){
        return new Collider(this);
    }

    public float getX(){
        if(entity != null){
            return ((Transform)entity.getComponent(COMPONENT_TYPE.TRANSFORM)).position.x;
        }
        return 0;
    }

    public float getY(){
        if(entity != null){
            return ((Transform)entity.getComponent(COMPONENT_TYPE.TRANSFORM)).position.y;
        }
        return 0;
    }


    //devuelve una copia del vector posicion de la entidad
    public Vector2 getPosition(){
        if(entity != null){
            return ((Transform)entity.getComponent(COMPONENT_TYPE.TRANSFORM)).position.copy();
        }
        return null;
    }

    public static boolean checkStaticCollision(Collider c1, Collider c2){
        Vector2 point = c1.getPosition();
        Collider total = new Collider(c1.width + c2.width, c1.height + c2.height, c1.offset.copy().add(c2.offset));
        total.entity = c2.entity;
        return checkPointCollision(point, total);
    }

    public static boolean checkPointCollision(Vector2 point, Collider col){
        Vector2 minCorner = new Vector2(col.getX() + col.offset.x, col.getY() + col.offset.y );
        Vector2 maxCorner = new Vector2(col.getX() + col.offset.x + col.width, col.getY() + col.offset.y + col.height);
        //Log.i("CollisionCheck", "Point: "+point.toString() + " Collider: "+ col.toString());
        return (point.x >= minCorner.x && point.x <= maxCorner.x && point.y >= minCorner.y && point.y <= maxCorner.y);
    }

    @Override
    public String toString() {
        return "Collider{" +
                "pos x = " + getX()+
                ", pos y = " + getY() +
                ", width = " + width +
                ", height = " + height +
                ", offset = " + offset +
                ", Collider Type = " + colliderType +
                '}';
    }
}
