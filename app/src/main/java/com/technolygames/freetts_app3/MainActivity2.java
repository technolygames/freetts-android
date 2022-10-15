package com.technolygames.freetts_app3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity2 extends androidx.appcompat.app.AppCompatActivity{
    private static final int PICKFILE_REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        verifyStoragePermissions(MainActivity2.this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                i=Intent.createChooser(i,"Escoje un archivo");
                startActivityForResult(i,PICKFILE_REQUEST_CODE);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    Socket s=new Socket("192.168.0.12",2389);
                    EditText et=(EditText)findViewById(R.id.textField);
                    File f=new File(et.getText().toString());
                    byte[] flujo=new byte[(int)f.length()];
                    InputStream is=new FileInputStream(f);
                    BufferedInputStream bis=new BufferedInputStream(is);
                    DataInputStream dis=new DataInputStream(bis);
                    dis.readFully(flujo,0,flujo.length);

                    OutputStream os=s.getOutputStream();
                    DataOutputStream dos=new DataOutputStream(os);
                    System.out.println(f.getName());
                    dos.writeUTF(f.getName());
                    dos.writeLong(flujo.length);
                    dos.write(flujo,0,flujo.length);

                    new Thread(new thread(is,os)).start();

                    os.flush();
                    dos.flush();
                }catch(IOException e){
                    showNotification(e);
                    e.printStackTrace();
                }
            }
        });
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

 // /storage/emulated/0/Pictures
    NotificationCompat.Builder builder;

    protected void showNotification(Throwable throwable){
        NotificationManagerCompat nmc=NotificationManagerCompat.from(this);
        builder=new NotificationCompat.Builder(getApplicationContext(),NotificationUtils.ANDROID_CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("TTS dice").setContentText(throwable.getMessage()).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        nmc.notify(NotificationCompat.PRIORITY_DEFAULT,builder.build());
        new NotificationUtils(getApplicationContext()).createChannels();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==PICKFILE_REQUEST_CODE){
            Uri uri=data.getData();
            String src1=new PathUtils(getApplicationContext()).getPath(uri);
            EditText et=(EditText)findViewById(R.id.textField);
            et.setText(src1);
            return;
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}