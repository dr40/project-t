package fr.vodoji;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import fr.vodoji.android.AndroidEngine;
import fr.vodoji.android.specific.GameSurface;
import fr.vodoji.engine.scene.Scene;
import fr.vodoji.engine.scene.SceneListener;
import fr.vodoji.scene.classic.ClassicGameScene;
import fr.vodoji.scene.game.GameScene;
import fr.vodoji.scene.test.SceneTest;
import fr.vodoji.scene.test.SceneTest2;

public class GameActivity extends Activity {

	GameSurface surface;
	AndroidEngine engine;
	SceneTest scene1;
	SceneTest2 scene2;
	GameScene gameScene;
	ClassicGameScene classicGameScene;
	MediaPlayer musicPlayer;
	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		/* Music */
		if (musicPlayer != null) {
			musicPlayer.release();
		}
		if (GameGlobals.currentLevel == 1) {
			musicPlayer = MediaPlayer.create(getApplicationContext(), R.raw.m1);
		} else if (GameGlobals.currentLevel == 2) {
			musicPlayer = MediaPlayer.create(getApplicationContext(), R.raw.m2);
		} else if (GameGlobals.currentLevel == 3) {
			musicPlayer = MediaPlayer.create(getApplicationContext(), R.raw.m3);
		} else {
			musicPlayer = MediaPlayer.create(getApplicationContext(), R.raw.m4);
		}
		try {
			musicPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		musicPlayer.start();
		if (!GameGlobals.music) {
			musicPlayer.setVolume(0, 0);
		}
		
		/* Init engine and scenes */
		engine = new AndroidEngine();
		surface = (GameSurface)findViewById(R.id.SurfaceView01);
		surface.init(engine);
		scene1 = new SceneTest();
		scene2 = new SceneTest2();
		gameScene = new GameScene(musicPlayer);
		classicGameScene = new ClassicGameScene(musicPlayer);
		engine.addScene("test", scene1);
		engine.addScene("test2", scene2);
		engine.addScene("game", gameScene);
		engine.addScene("basic", classicGameScene);
		if (GameGlobals.gameBasicMode) { 
			engine.gotoScene("basic");
		} else {
			engine.gotoScene("game");
		}
		
		/* Game Listener */
		gameScene.addListener(new SceneListener() {
			
			@Override
			public void onUnloaded(Scene scene) {
				if (GameGlobals.music) {
					musicPlayer.stop();
				}
		        surface.terminate();
	        	Intent i = new Intent(getApplicationContext(), HighscoreAdd.class);
	        	startActivity(i);
		        finish();
			}
			
			@Override
			public void onLoaded(Scene scene) {
				// TODO Auto-generated method stub
				
			}
		});
		classicGameScene.addListener(new SceneListener() {
			
			@Override
			public void onUnloaded(Scene scene) {
				if (GameGlobals.music) {
					musicPlayer.stop();
				}
		        surface.terminate();
	        	Intent i = new Intent(getApplicationContext(), HighscoreAdd.class);
	        	startActivity(i);
		        finish();
			}
			
			@Override
			public void onLoaded(Scene scene) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void onStop () {
		if (GameGlobals.music) {
			musicPlayer.stop();
		}
        surface.terminate();
        super.onStop();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
	}
	@Override
	public void onBackPressed() {
		if (GameGlobals.music) {
			musicPlayer.stop();
		}
        surface.terminate();
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
       
	}
		
}
