package alvaro.daniel.space_crusade;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dany on 08/12/2017.
 */

public class Sprite extends Component {
    private List<Bitmap> images;
    private float anim_controller;
    private int image_index;

    Vector2 offset;
    float image_speed;
    boolean elemUI = false;
    String text = "";
    Paint textPaint;
    float width;
    float height;
    
    public Sprite(Bitmap[] images, float image_speed, boolean ui, Vector2 ... offset){
        super(COMPONENT_TYPE.SPRITE);
        this.images = new ArrayList();
        if(offset.length == 1){
            this.offset = offset[0];
        }else{
            this.offset = new Vector2(0,0);
        }
        for(Bitmap b : images){
            this.images.add(b);
        }
        this.image_index = 0;
        this.image_speed = image_speed;
        this.elemUI = ui;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(40);
        textPaint.setColor(Color.WHITE);
        this.width = images.length > 0? images[0].getWidth() : 0;
        this.height = images.length > 0? images[0].getHeight() : 0;
    }

    public Sprite(){
        this(new Bitmap[0], 0, false);
    }

    public Sprite(Bitmap[] images){
        this(images, 0, false);
    }

    public Sprite(Sprite original){
        this((Bitmap[])original.images.toArray(), original.image_speed, original.elemUI, original.offset.copy());
    }

    public Sprite clone(){
        return new Sprite(this);
    }

    public void setImageIndex(int index){
        anim_controller = 0;
        image_index = index % images.size();
    }

    public int getImage_index() {
        return image_index;
    }

    public int getImageLength(){
        return images.size();
    }

    public void render(Canvas canvas){
        //Log.i("Sprite: ", "rendering sprite of "+entity.id);
        Transform t = (Transform)entity.getComponent(COMPONENT_TYPE.TRANSFORM);
        //canvas.drawColor(Color.BLUE);
        Vector2 pos;
        if(!elemUI){
            pos = t.position.copy().add(offset);
        }else {
            pos = t.position.copy().add(offset);
        }
        //calculamos la image_index correspondiente.
        calculeImageIndex();
        //modificamos el canvas para dibujar la imagen rotada
        canvas.save();
        //canvas.translate(t.position.x, t.position.y);
        canvas.rotate(t.rotation, t.position.x, t.position.y);
        canvas.scale(t.scale.x, t.scale.y, t.position.x, t.position.y);
        //dibujamos el sprite si lo tiene
        if(images.size() > 0){
            canvas.drawBitmap(images.get(image_index),pos.x, pos.y, null);
            /*int w = images.get(image_index).getWidth();
            int h = images.get(image_index).getHeight();
            RectF dst = new RectF(pos.x - (w/2 * t.scale.x), pos.y - (h/2) * t.scale.y, pos.x + (w/2 * t.scale.x), pos.y + (h/2) * t.scale.y);
            canvas.drawBitmap(images.get(image_index), null, dst, null);*/
        }
        //dibujamos el texto si lo tiene
        if(!text.isEmpty()){
            if(entity.scene.spaceFont != null){
                textPaint.setTypeface(entity.scene.spaceFont);
            }
            canvas.drawText(text, pos.x, pos.y, textPaint);
        }
        if(entity.scene.debug){
            Collider c = (Collider)entity.getComponent(COMPONENT_TYPE.COLLIDER);
            if(c != null){
                Paint p = new Paint();
                p.setStyle(Paint.Style.STROKE);
                p.setColor(Color.RED);
                p.setStrokeWidth(3);
                canvas.drawRect(new RectF(t.position.x + c.offset.x, t.position.y + c.offset.y, t.position.x + c.offset.x + c.width, t.position.y + c.offset.y + c.height), p);
            }
        }
        //canvas.drawBitmap(images.get(image_index),0, 0, null);
        canvas.restore();
    }

    public void calculeImageIndex(){
        this.anim_controller += this.image_speed;
        if(this.anim_controller >= 1){
            this.image_index++;
            if(this.image_index >= images.size()){
                this.image_index = 0;
            }
            this.anim_controller %= 1;
        }else if(this.anim_controller <= -1){
            this.image_index--;
            if(this.image_index <= -1){
                this.image_index = images.size()-1;
            }
            this.anim_controller %= 1;
        }
    }
}
