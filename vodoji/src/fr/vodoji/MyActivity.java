package fr.vodoji;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View;
import android.os.Handler;

public class MyActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	private static boolean music = true;
	private static boolean vibration = true;
	

    private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            setContentView(R.layout.main);
            Button bExit = (Button) findViewById(R.id.buttonExit);
            bExit.setOnClickListener(ecouteurBExit);

            Button bHelp = (Button) findViewById(R.id.buttonHelp);
            bHelp.setOnClickListener(ecouteurBHelp);
            
            TextView tv = (TextView) findViewById(R.id.nameAppli);
    		tv.setText("VODOJI");
    		tv.setKeyListener(null);
    		
            Button bMusic = (Button) findViewById(R.id.btnMusic);
            bMusic.setOnClickListener(ecouteurMusic);
            
            Button bVib = (Button) findViewById(R.id.btnVib);
            bVib.setOnClickListener(ecouteurVib);
            
            Button bHightScores = (Button) findViewById(R.id.btnHS);
            bHightScores.setOnClickListener(ecouteurHS);
        }
    };   
    
    private OnClickListener ecouteurVib = new OnClickListener() {
        @Override 
        public void onClick(View view) {
        	switchVibration();
        }
    };
    
    private OnClickListener ecouteurMusic = new OnClickListener() {
        @Override 
        public void onClick(View view) {
        	switchMusic();
        }
    };
    
    private OnClickListener ecouteurBExit = new OnClickListener() {
        @Override 
        public void onClick(View arg0) {
            System.exit(RESULT_OK);
        }
    };
    
    private OnClickListener ecouteurHS = new OnClickListener() {
        @Override 
        public void onClick(View view) {
        	Intent i = new Intent(view.getContext(), ActHightScores.class);
        	startActivity(i);
        }
    };
    
    private OnClickListener ecouteurBHelp = new OnClickListener() {
        @Override
        public void onClick(View view) {
        	Intent i = new Intent(view.getContext(), HelpActivity.class);
        	startActivity(i);
        }
    };
    
    public void switchMusic() {
    	music = !music;
    	showInfo(1);
    }
    public void switchVibration() {
    	vibration = !vibration;
    	if (vibration){
    		((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1000);
    	}
    	showInfo(2);
    }
    
    public void showInfo (int id) {
    	Context context = getApplicationContext();
    	Toast toast = new Toast(context);
        switch(id) {
	        case 1:
	          if (music)
	        	  toast = Toast.makeText(context, "Musique activée", Toast.LENGTH_LONG);
	          else
	        	  toast = Toast.makeText(context, "Musique désactivée",Toast.LENGTH_LONG);
	          break;
	        case 2:
	          if (vibration)
	        	  toast = Toast.makeText(context, "Vibration activée",Toast.LENGTH_LONG);
	          else
	        	  toast = Toast.makeText(context, "Vibration désactivée",Toast.LENGTH_LONG);
	    }
        toast.show();
    }
    
}
