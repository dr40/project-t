package fr.vodoji;

import android.app.Activity;
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
import fr.vodoji.GameGlobals;

public class MyActivity extends Activity {

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
    		tv.setEnabled(false);
    		tv.setKeyListener(null);
    		
    		Button bPlayGame = (Button) findViewById(R.id.btnGame);
    		bPlayGame.setOnClickListener(btnGameListener);
    		Button bBasicGame = (Button) findViewById(R.id.btnBasicGame);
    		bBasicGame.setOnClickListener(btnBasicListener);
    		
    		
            Button bMusic = (Button) findViewById(R.id.btnMusic);
            bMusic.setOnClickListener(ecouteurMusic);
            
            Button bVib = (Button) findViewById(R.id.btnVib);
            bVib.setOnClickListener(ecouteurVib);
            
            Button bHightScores = (Button) findViewById(R.id.btnHS);
            bHightScores.setOnClickListener(ecouteurHS);
        }
    };   
    
    
    private OnClickListener btnGameListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
        	GameGlobals.gameBasicMode = false;
        	Intent i = new Intent(view.getContext(), LevelSelect.class);
        	startActivity(i);
        }
    };
    private OnClickListener btnBasicListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
        	GameGlobals.gameBasicMode = true;
        	Intent i = new Intent(view.getContext(), LevelSelect.class);
        	startActivity(i);
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
    	GameGlobals.music = !GameGlobals.music;
    	showInfo(1);
    }
    public void switchVibration() {
    	GameGlobals.vibrator = !GameGlobals.vibrator;
    	if (GameGlobals.vibrator){
    		((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1000);
    	}
    	showInfo(2);
    }
    
    public void showInfo (int id) {
    	Context context = getApplicationContext();
    	Toast toast = new Toast(context);
        switch(id) {
	        case 1:
	          if (GameGlobals.music )
	        	  toast = Toast.makeText(context, "Musique activ�e", Toast.LENGTH_LONG);
	          else
	        	  toast = Toast.makeText(context, "Musique d�sactiv�e",Toast.LENGTH_LONG);
	          break;
	        case 2:
	          if (GameGlobals.vibrator)
	        	  toast = Toast.makeText(context, "Vibration activ�e",Toast.LENGTH_LONG);
	          else
	        	  toast = Toast.makeText(context, "Vibration d�sactiv�e",Toast.LENGTH_LONG);
	    }
        toast.show();
    }
    
}
