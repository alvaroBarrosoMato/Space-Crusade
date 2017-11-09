package com.example.alvar.pruebacamara;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    int PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE;
    int PHOTO_RESULT;
    Uri lastPhotoURI;
    Intent takePictureIntent ;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        image = (ImageView) findViewById(R.id.image);
    }
    public void camara(View view){

        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Uri lastPhotoURI = createFileURI();
            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, lastPhotoURI);
            startActivityForResult(takePictureIntent, PHOTO_RESULT);
        }


    }
    /*Uri createFileURI(){
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();
        return Uri.encode(ts);
    }*/
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_RESULT && resultCode == RESULT_OK) {
            image.setImageBitmap(BitmapFactory.decodeFile(lastPhotoURI.getPath()));
        }
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_RESULT && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }

}
