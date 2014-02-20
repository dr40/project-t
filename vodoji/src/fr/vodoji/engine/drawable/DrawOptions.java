package fr.vodoji.engine.drawable;

import android.graphics.Rect;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class DrawOptions {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	private float _x;
	private float _y;
	private Rect _crop;

	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public DrawOptions() {
		_x = 0;
		_y = 0;
		_crop = new Rect(0,0,0,0);
	}
	
	public DrawOptions(float x, float y) {
		_x = x;
		_y = y;
		_crop = new Rect(0,0,0,0);
	}

	public DrawOptions(float x, float y, Rect crop) {
		_x = x;
		_y = y;
		if (crop == null) {
			_crop = new Rect(0,0,0,0);
		} else {
			_crop = crop;
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Compute methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public final void getCropRect(Rect dstRect, int fromWidth, int fromHeight) {
		if ((_crop.left == 0) && (_crop.top == 0) && (_crop.right == 0) && (_crop.bottom == 0)) {
			dstRect.left = 0;
			dstRect.top = 0;
			dstRect.right = fromWidth;
			dstRect.bottom = fromHeight;
		} else {
			if (_crop.left < 0) {
				dstRect.left = 0;
			} else {
				dstRect.left = _crop.left;
			}
			if (_crop.top < 0) {
				dstRect.top = 0;
			} else {
				dstRect.top = _crop.top;
			}
			if (_crop.right < _crop.left) {
				dstRect.right = 0;
			} else if (_crop.right > fromWidth) {
				dstRect.right = fromWidth;
			} else {
				dstRect.right = _crop.right;
			}
			if (_crop.bottom < _crop.top) {
				dstRect.bottom = 0;
			} else if (_crop.bottom > fromHeight) {
				dstRect.bottom = fromHeight;
			} else {
				dstRect.bottom = _crop.bottom;
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Getters
	///////////////////////////////////////////////////////////////////////////////////////////

	public final float getX() {
		return _x;
	}
	
	public final float getY() {
		return _y;
	}
	
	public final Rect getCrop() {
		return _crop;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Setters
	///////////////////////////////////////////////////////////////////////////////////////////

	public DrawOptions setX(float x) {
		_x = x;
		return this;
	}
	public DrawOptions setY(float y) {
		_y = y;
		return this;
	}
	public DrawOptions move(float x, float y) {
		_x = x;
		_y = y;
		return this;
	}

	public DrawOptions clearCrop() {
		return crop(0,0,0,0);
	}
	public DrawOptions crop(int cropX, int cropY, int cropW, int cropH) {
		_crop.left = cropX;
		_crop.top = cropY;
		_crop.right = cropX + cropW;
		_crop.bottom = cropY + cropH;
		return this;
	}
	public DrawOptions crop(Rect rect) {
		if (rect == null) {
			return clearCrop();
		} else {
			return crop(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
		}
	}
	
	
}
