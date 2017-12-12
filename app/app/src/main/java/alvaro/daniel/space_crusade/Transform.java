package alvaro.daniel.space_crusade;

/**
 * Created by Dany on 08/12/2017.
 */

public class Transform extends Component{
    Vector2 position;
    float rotation;
    Vector2 scale;
    float depth;

    public Transform(Vector2 position, float depth, float rot, Vector2 scale){
        super(COMPONENT_TYPE.TRANSFORM);
        this.position = position;
        this.rotation = rot%360;
        this.scale = scale;
        this.depth = depth;
    }

    public Transform(){
        this(new Vector2(0,0),0, 0, new Vector2(1,1));
    }

    public Transform(Vector2 position){
        this(position, 0, 0, new Vector2(1, 1));
    }

    public Transform (Vector2 position, float depth){
        this(position, depth, 0, new Vector2(1,1));
    }

    public Transform(Transform original){
        this(original.position.copy(), original.depth, original.rotation, original.scale.copy());
    }

    public Transform copy(){
        return new Transform(this);
    }

    public void translate(Vector2 move){
        this.position.add(move);
    }

    public void rotate(float deg){
        this.rotation += deg;
        this.rotation %= 360;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation%360;
    }

    public void addRotation(float rotation){
        this.rotation += rotation;
        this.rotation = this.rotation%360;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }
}
