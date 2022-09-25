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
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import androidx.navigation.Navigation;
import androidx.navigation.NavController;
//extensión larga
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.ui.AppBarConfiguration;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

public class MainActivity extends androidx.appcompat.app.AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController=Navigation.findNavController(this,R.id.nav_host_fragment_content_main);
        appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view,"Replace with your own action",Snackbar.LENGTH_LONG).setAction("Action",null).show();
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

    protected void synthesizer(){
        try {
            System.setProperty("freetts.voices","com.sun.speech.freetts.en.us"+".cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts"+".jsapi.FreeTTSEngineCentral");
            Synthesizer synth=Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synth.allocate();
            synth.resume();
            synth.speakPlainText("", null);
            synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
            synth.deallocate();
        }catch(AudioException e){
            e.printStackTrace();
        }catch(EngineException x){
            x.printStackTrace();
        }catch(InterruptedException n){
            n.printStackTrace();
        }
    }
}