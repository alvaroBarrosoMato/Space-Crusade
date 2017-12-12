package alvaro.daniel.space_crusade;

/**
 * Created by Dany on 08/12/2017.
 */

public class Vector2 {
    public float x;
    public float y;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2(){
        this(0,0);
    }

    //constructor copia, crea un vector igual al pasado por parametro
    public Vector2(Vector2 original){
        this(original.x, original.y);
    }

    //Devuelve una copia del vector
    public Vector2 copy(){
        return new Vector2(this);
    }

    //copia los valores del vector argumento
    public void copy(Vector2 original){
        this.x = original.x;
        this.y = original.y;
    }

    //copia el componente x del vector argumento
    public void copyX(Vector2 original){
        copy(new Vector2(original.x, y));
    }

    //copia el componente y del vector argumento
    public void copyY(Vector2 original){
        copy(new Vector2(x, original.y));
    }

    //suma un vector al vector actual, cambia los valores del vector actual y lo devuelve
    public Vector2 add(Vector2 op2) {
        this.x += op2.x;
        this.y += op2.y;
        return this;
    }

    //resta dos vectores, devuelve el vector resultante y cambia los valores del vector actual
    public Vector2 substract(Vector2 op2){
        this.x -= op2.x;
        this.y -= op2.y;
        return this;
    }

    //multiplica dos vectores, devuelve el vector resultante y cambia los valores del vector actual
    public Vector2 multiply(Vector2 op2){
        this.x *= op2.x;
        this.y *= op2.y;
        return this;
    }

    //multiplica el vector por un numero escalar, cambia el valor del vector actual
    public Vector2 multiply(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    //divide dos vectores, devuelve el vector resultante y cambia los valores del vector actual
    public Vector2 divide(Vector2 op2){
        this.x /= op2.x;
        this.y /= op2.y;
        return this;
    }

    //divide el vector por un numero escalar, cambia el valor del vector actual
    public Vector2 divide(float scalar){
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }

    //invierte el vector actual
    public Vector2 invert(){
        this.multiply(-1);
        return this;
    }

    //devuelve la longitud del vector
    public float length(){
        return (float)Math.sqrt(x * x + y * y);
    }
    //alias de la funcion length
    public float magnitude(){
        return length();
    }

    //devuelve la longitud al cuadrado, util para comprar la lonitud de 2 vectores sin tener que calcular su raiz cuadrada
    public float squaredLength(){
        return x * x + y * y;
    }

    //normaliza el vector y lo devuelve
    public Vector2 normalize(){
        divide(length());
        return this;
    }

    //alias de la funcion normalize
    public Vector2 norm(){
        return normalize();
    }

    //devuelve un vector igual pero con longitud 1 (no modifica el vector actual)
    public Vector2 unit(){
        return new Vector2(x/length(), x/length());
    }

    //dot product entre dos vectores
    public float dot(Vector2 op2){
        return x * op2.x + y * op2.y;
    }

    //devuelve el componente z del producto cruzado de 2 vectores llevado al 3D
    public float cross(Vector2 op2){
        return x * op2.y - y * op2.x;
    }

    //devuelve el angulo en radianes formado por el vector con el eje X positivo
    public float angle(){
       return (float) Math.toRadians(angleDeg());
    }

    //devuelve el angulo en grados formado por el vector con el eje X positivo
    public float angleDeg(){
        return (float)Math.atan2(y, x);
    }

    //rota el vector el angulo dado, en radianes CounterClockWise desde el eje X positivo
    public Vector2 rotate(float angle){
        return rotateDeg((float)Math.toDegrees(angle));
    }

    //rota el vector el angulo dado, en grados CounterClockWise desde el eje X positivo
    public Vector2 rotateDeg(float deg){
        this.x = (float)(x * Math.cos(deg) - y * Math.sin(deg));
        this.y = (float)(y * Math.sin(deg) + y * Math.cos(deg));
        return this;
    }

    public static Vector2 lerp(Vector2 val, Vector2 target, float factor){
        Vector2 op = target.copy().substract(val).multiply(factor);
        return val.add(op);
    }

    public static Vector2 lerp(Vector2 val, Vector2 target, Vector2 factor){
        Vector2 op = target.copy().substract(val).multiply(factor);
        return val.add(op);
    }

    //refleja el vector en funcion de una normal.
    public Vector2 reflect(Vector2 normal){
        normal.norm();
        Vector2 velN = normal.multiply(this.dot(normal));
        Vector2 velT = this.substract(velN);
        this.copy(velT.substract(velN));
        return this;
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x = " + x +
                ", y = " + y +
                '}';
    }
}
