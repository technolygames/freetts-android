package com.technolygames.freetts_app3;
//clases
import com.technolygames.freetts_app3.databinding.ActivityMainBinding;
//librerías
import com.google.android.material.snackbar.Snackbar;
//java
import java.util.Locale;
import javax.speech.Engine;
import javax.speech.Central;
import javax.speech.AudioException;
import javax.speech.EngineException;
//android
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.navigation.NavController;
//extensión larga
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.core.app.NotificationCompat;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

public class MainActivity extends androidx.appcompat.app.AppCompatActivity{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private TextToSpeech tts;

    private String CHANNEL_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

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

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TextView ta=(TextView)findViewById(R.id.edit_text);
                Toast.makeText(getApplicationContext(), ta.getText().toString(), Toast.LENGTH_SHORT).show();
                tts.setLanguage(new Locale("es","mx"));
                tts.speak(ta.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view,"Lee el texto del área de texto",Snackbar.LENGTH_LONG).setAction("Acción",new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Test").setContentText("Content text").setStyle(new NotificationCompat.BigTextStyle().bigText("big style text")).setPriority(NotificationCompat.PRIORITY_DEFAULT);
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