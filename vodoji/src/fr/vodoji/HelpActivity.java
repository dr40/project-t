package fr.vodoji;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
        Button bExit = (Button) findViewById(R.id.buttonExitHelp);
        bExit.setOnClickListener(ecouteurBExit);
        Button bReturn = (Button) findViewById(R.id.buttonReturnHelp);
        bReturn.setOnClickListener(ecouteurBExit);
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
