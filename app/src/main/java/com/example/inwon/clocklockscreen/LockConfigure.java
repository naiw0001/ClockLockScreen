package com.example.inwon.clocklockscreen;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LockConfigure extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private Switch sw;
    Bitmap img_bitmap;
    private int issw;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},1);
            }
        }
        sw = (Switch)findViewById(R.id.setimg);
        pref = getSharedPreferences("locksceen_clock",MODE_PRIVATE);
       int a= pref.getInt("issw",0);
        if(a == 1){
            sw.setChecked(true);
        }else sw.setChecked(false);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true){
                    Toast.makeText(getApplicationContext(),"이미지를 선택하세요",Toast.LENGTH_SHORT).show();
                    issw = 1;
                    sw.setText("내 이미지 사용 중");
                    setimg();
                }else {
                    Toast.makeText(getApplicationContext(),"내 이미지 사용 안함",Toast.LENGTH_SHORT).show();
                    sw.setText("내 이미지 사용");
                    issw=0;
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else {

                }
                return;
        }
    }

    //onClick
    public void lock(View v){
        switch (v.getId()){
            case R.id.start:
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putInt("issw",issw);
                editor.commit();
                if(issw==1){
                    savefile();
                }
                Intent intent = new Intent(this,ScreenService.class);
                startService(intent);
                finish();
                break;
            case R.id.stop:
                Intent intent2 = new Intent(this,ScreenService.class);
                stopService(intent2);
                File file = new File(getFilesDir().getAbsolutePath()+"/back_img");
                file.delete();
                SharedPreferences.Editor ed = pref.edit();
                ed.clear();
                ed.commit();
                Toast.makeText(getApplicationContext(),"잠금화면이 헤재되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private void setimg(){ // gallery img get
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Image Crop
        intent.putExtra("crop","true");
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",300);
        intent.putExtra("outputY",300);
        intent.putExtra("scale",true);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                try {
//                   img_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                    img_bitmap = data.getParcelableExtra("data");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public void savefile(){
        File file = new File(getFilesDir().getAbsolutePath()+"/back_img");
        try {
            file.createNewFile();
            FileOutputStream fos = openFileOutput("back_img",0);
            img_bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
