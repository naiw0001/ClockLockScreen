package com.example.inwon.clocklockscreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LockConfigure extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_BOOT_COMPLETED)){

            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},1);
            }
        }
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
                Intent intent = new Intent(this,ScreenService.class);
                startService(intent);
                finish();
                break;
            case R.id.stop:
                Intent intent2 = new Intent(this,ScreenService.class);
                stopService(intent2);
                Toast.makeText(getApplicationContext(),"잠금화면이 헤재되었습니다.",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
