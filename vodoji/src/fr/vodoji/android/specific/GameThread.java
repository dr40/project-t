package fr.vodoji.android.specific;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	private SurfaceHolder _holder;
	private GameSurface _gameSurface;
	private boolean _isRunning = false;


	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public GameThread(SurfaceHolder holder, GameSurface gameSurface) {
		_holder = holder;
		_gameSurface = gameSurface;
	}


	///////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public void setRunning(boolean run) {
		_isRunning = run;
	}
	
	@SuppressLint("WrongCall")
	@Override
	public void run() {
		Canvas c;
		while (_isRunning) {
			c = null;
			try {
				c = _holder.lockCanvas();
				synchronized (_holder) {
					_gameSurface.onDraw(c);
				}
			} finally {
				if (c != null) {
					_holder.unlockCanvasAndPost(c);
				}
			}
		};
	}
	
	

}
