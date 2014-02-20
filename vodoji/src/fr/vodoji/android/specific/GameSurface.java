package fr.vodoji.android.specific;

import fr.vodoji.android.AndroidEngine;
import fr.vodoji.android.graphics.AndroidScreen;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
	

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	private GameThread gameThread;
	private AndroidEngine _engine;
	private AndroidScreen _screen;

	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public GameSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		gameThread = new GameThread(getHolder(), this);
		setFocusable(true);
	}
	public GameSurface(Context context) {
		super(context);
		getHolder().addCallback(this);
		gameThread = new GameThread(getHolder(), this);
		setFocusable(true);
	}
	
	
	public void init(AndroidEngine engine) {
		_engine = engine;
		_engine.initialize(getContext(), getResources());
		if (_engine != null) {
			_screen = (AndroidScreen)(_engine.getScreen());
		} else {
			_screen = null;
		}
	}
	
	public void terminate() {
		gameThread.setRunning(false);
	}

	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Event methods
	///////////////////////////////////////////////////////////////////////////////////////////

	synchronized public boolean onTouchEvent(MotionEvent event) {
		if (_engine != null) {
			_engine.onTouch(event);
		}
		return true;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Draw methods
	///////////////////////////////////////////////////////////////////////////////////////////

	synchronized public void onDraw(Canvas c) {
		if (_engine != null) {
			_engine.onUpdate();
			_screen.setCanvas(c);
			_engine.onRender();
			_screen.setCanvas(null);
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Create/Change/Destroy methods
	///////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		gameThread.setRunning(true);
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		gameThread.setRunning(false);
		while (retry) {
			try {
				gameThread.join();
				retry = false;
			} catch (InterruptedException e) {
				
			}
		}
	}

	

}
