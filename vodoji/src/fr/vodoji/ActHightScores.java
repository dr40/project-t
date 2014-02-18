package fr.vodoji;

import fr.vodoji.model.HightScores;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class ActHightScores extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hight_scores);
				
		HightScores hs = new HightScores(true); // Iinitialisation de l'hightscore avec des scores bidon
		String[] listHS = HightScores.getListeHS();
		
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listHS);
	       
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
