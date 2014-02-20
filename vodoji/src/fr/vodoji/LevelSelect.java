package fr.vodoji;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

public class LevelSelect extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
		/* Level 1 */
        Button bLvl1 = (Button) findViewById(R.id.btnLevel1);
        bLvl1.setOnClickListener(btnLevel1Listener);
		/* Level 2 */
        Button bLvl2 = (Button) findViewById(R.id.btnLevel2);
        bLvl2.setOnClickListener(btnLevel2Listener);
		/* Level 3 */
        Button bLvl3 = (Button) findViewById(R.id.btnLevel3);
        bLvl3.setOnClickListener(btnLevel3Listener);
		/* Level 4 */
        Button bLvl4 = (Button) findViewById(R.id.btnLevel4);
        bLvl4.setOnClickListener(btnLevel4Listener);
		/* Return */
        Button bReturn = (Button) findViewById(R.id.btnReturn);
        bReturn.setOnClickListener(retBtnListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_select, menu);
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
    
    
    private OnClickListener btnLevel1Listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
        	GameGlobals.currentLevel = 1;
        	Intent i = new Intent(view.getContext(), GameActivity.class);
        	startActivity(i);
        }
    };
    private OnClickListener btnLevel2Listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
        	GameGlobals.currentLevel = 2;
        	Intent i = new Intent(view.getContext(), GameActivity.class);
        	startActivity(i);
        }
    };
    private OnClickListener btnLevel3Listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
        	GameGlobals.currentLevel = 3;
        	Intent i = new Intent(view.getContext(), GameActivity.class);
        	startActivity(i);
        }
    };
    
    private OnClickListener btnLevel4Listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
        	GameGlobals.currentLevel = 4;
        	Intent i = new Intent(view.getContext(), GameActivity.class);
        	startActivity(i);
        }
    };
    
}
