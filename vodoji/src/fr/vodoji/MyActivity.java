package fr.vodoji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.os.Handler;

public class MyActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */

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
        }
    };

    private OnClickListener ecouteurBExit = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            System.exit(RESULT_OK);
        }
    };
    
    private OnClickListener ecouteurBHelp = new OnClickListener() {
        @Override
        public void onClick(View view) {
        	Intent i = new Intent(view.getContext(), HelpActivity.class);
        	startActivity(i);
        }
    };

}
