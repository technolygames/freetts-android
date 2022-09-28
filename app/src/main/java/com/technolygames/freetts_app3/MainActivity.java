package com.technolygames.freetts_app3;
//clases
import com.technolygames.freetts_app3.databinding.ActivityMainBinding;
//librerías
import com.google.android.material.snackbar.Snackbar;
//java
import java.util.Locale;
//android
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.navigation.Navigation;
import androidx.navigation.NavController;
//extensión larga
import android.speech.tts.TextToSpeech;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends androidx.appcompat.app.AppCompatActivity{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private NotificationCompat.Builder builder;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        NotificationManagerCompat nmc=NotificationManagerCompat.from(this);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController=Navigation.findNavController(this,R.id.nav_host_fragment_content_main);
        appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);

        tts=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int i){
                if(i!=TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                }
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view,"Presiona para escuchar",Snackbar.LENGTH_LONG).setAction("Leer texto",new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        TextView ta=(TextView)findViewById(R.id.edit_text);
                        tts.setLanguage(new Locale("es","mx"));
                        builder=new NotificationCompat.Builder(getApplicationContext(),NotificationUtils.ANDROID_CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("TTS dice").setContentText("Content text").setStyle(new NotificationCompat.BigTextStyle().bigText(ta.getText())).setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        nmc.notify(NotificationCompat.PRIORITY_DEFAULT,builder.build());
                        new NotificationUtils(getApplicationContext()).createChannels();
                        tts.speak(ta.getText().toString(), TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, null);
                    }
                }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id=item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        NavController navController=Navigation.findNavController(this,R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController,appBarConfiguration)||super.onSupportNavigateUp();
    }

    protected void onPause(){
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
}