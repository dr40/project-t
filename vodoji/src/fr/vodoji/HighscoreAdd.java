package fr.vodoji;

import fr.vodoji.highscore.Highscore;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HighscoreAdd extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscore_add);
		
		Button bCancel = (Button) findViewById(R.id.btnMenu);
		bCancel.setOnClickListener(retBtnListener);
		
        Button bSave = (Button) findViewById(R.id.btnSave);
        bSave.setOnClickListener(saveBtnListener);

    	TextView tvScore = (TextView) findViewById(R.id.scoreLabel);
    	tvScore.setText("Votre score: " + GameGlobals.lastGameScore);
    	TextView tvLevel = (TextView) findViewById(R.id.levelLabel);
    	tvLevel.setText("Niveau de difficultés: " + GameGlobals.currentLevel);
        
        

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.highscore_add, menu);
		return true;
	}

	
    private OnClickListener retBtnListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    };

	
    private OnClickListener saveBtnListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
        	TextView tv = (TextView) findViewById(R.id.txtName);
        	Highscore h = new Highscore();
        	h.loadLocalScoreList(arg0.getContext());
        	h.addScore(tv.getText().toString(), GameGlobals.currentLevel, GameGlobals.lastGameScore);
        	h.saveLocalHighscore(arg0.getContext());
        	
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    };
    
    
}
