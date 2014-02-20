package fr.vodoji;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import fr.vodoji.highscore.Highscore;

public class ActHightScores extends ListActivity {

	protected static Highscore highScore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Initialize */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hight_scores);
				
		/* Load highscores table - dynamically */
		highScore = new Highscore();
		//highScore.refresh();
		highScore.loadLocalScoreList(getApplicationContext());
		//HightScores hs = new HightScores(true); // Iinitialisation de l'hightscore avec des scores bidon
		//String[] listHS = HightScores.getListeHS();
		
		
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,highScore.getScoreList());
	       
	    setListAdapter(adapter);
	    
        Button bHightScores = (Button) findViewById(R.id.btnOkHS);
        bHightScores.setOnClickListener(ecouteurOkHS);
	}

    private OnClickListener ecouteurOkHS = new OnClickListener() {
        @Override 
        public void onClick(View view) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hight_scores, menu);
		return true;
	}

}
