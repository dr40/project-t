package fr.vodoji.android.graphics;

import android.graphics.Canvas;
import fr.vodoji.engine.graphics.Font;
import fr.vodoji.engine.graphics.Screen;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class AndroidScreen extends Screen {
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	private Canvas _drawCanvas;
	private int _lastWidth;
	private int _lastHeight;
	

	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public AndroidScreen() {
		_lastWidth = 0;
		_lastHeight = 0;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Canvas management
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public Canvas getCanvas() {
		return _drawCanvas;
	}
	public void setCanvas(Canvas c) {
		_drawCanvas = c;
		if (c != null) {
			_lastWidth = c.getWidth();
			_lastHeight = c.getHeight();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Event methods
	///////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void drawText(String text, float x, float y, Font fnt) {
		if ((_drawCanvas != null) && (fnt != null) && (fnt instanceof AndroidFont)) {
			AndroidFont androidFont = (AndroidFont)(fnt);
			_drawCanvas.drawText(text, x, y, androidFont.getPaint());
		}
	}
	@Override
	public int getWidth() {
		return _lastWidth;
	}
	@Override
	public int getHeight() {
		return _lastHeight;
	}

}
