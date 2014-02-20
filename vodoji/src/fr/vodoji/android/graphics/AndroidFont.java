package fr.vodoji.android.graphics;

import android.graphics.Color;
import android.graphics.Paint;
import fr.vodoji.engine.graphics.Font;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class AndroidFont extends Font {
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected Paint _paint;
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructor
	///////////////////////////////////////////////////////////////////////////////////////////

	public AndroidFont() {
		_paint = new Paint();
		_paint.setColor(Color.BLACK);
		_paint.setTextSize(8);
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Getters
	///////////////////////////////////////////////////////////////////////////////////////////

	public Paint getPaint() {
		return _paint;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	///////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int getColor() {
		return _paint.getColor();
	}

	@Override
	public float getSize() {
		return _paint.getTextSize();
	}

	@Override
	public void setColor(int color) {
		_paint.setColor(color);
	}

	@Override
	public void setSize(float size) {
		_paint.setTextSize(size);
	}

}
