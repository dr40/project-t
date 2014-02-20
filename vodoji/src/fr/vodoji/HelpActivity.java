package fr.vodoji;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
        Button bExit = (Button) findViewById(R.id.btnReturn);
        bExit.setOnClickListener(ecouteurBExit);
        Button bReturn = (Button) findViewById(R.id.btnOk);
        bReturn.setOnClickListener(ecouteurBExit);
		TextView tv = (TextView) findViewById(R.id.textRules);
		tv.setText("Règles du jeu :\n\n-Touchez l'écran pour détruire les avions avant qu'il n'ateignent votre porte-avions\n-Certains avions sont plus rapide que les autres et sont ainsi plus difficile à être détruits!");
		tv.setKeyListener(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}
	
    private OnClickListener ecouteurBExit = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    };

}
