package alvaro.daniel.space_crusade;

/**
 * Created by Dany on 10/12/2017.
 */

public class CollisionPoint {
    Collider other;
    Collider self;
    Vector2 normal;

    public CollisionPoint(Collider other, Collider self, Vector2 normal) {
        this.other = other;
        this.self = self;
        this.normal = normal;
    }

    public CollisionPoint(Collider other) {
        this.other = other;
    }
}
