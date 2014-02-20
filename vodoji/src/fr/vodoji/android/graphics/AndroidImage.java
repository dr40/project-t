package fr.vodoji.android.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import fr.vodoji.engine.drawable.DrawOptions;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.engine.graphics.Screen;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class AndroidImage extends Image {

	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected Bitmap _bitmap;
	protected Rect _srcRect;
	protected Rect _dstRect;
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public AndroidImage() {
		_srcRect = new Rect();
		_dstRect = new Rect();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Draw methods
	///////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void draw(Screen toScreen) {
		if (_bitmap != null) {
			AndroidScreen as = (AndroidScreen)(toScreen);
			as.getCanvas().drawBitmap(_bitmap, 0, 0, null);
		}
	}

	@Override
	public void draw(Screen toScreen, float x, float y) {
		if (_bitmap != null) {
			AndroidScreen as = (AndroidScreen)(toScreen);
			as.getCanvas().drawBitmap(_bitmap, x, y, null);
		}
	}

	@Override
	public void draw(Screen toScreen, DrawOptions opt) {
		if (_bitmap != null) {
			AndroidScreen as = (AndroidScreen)(toScreen);
			opt.getCropRect(_srcRect, _bitmap.getWidth(), _bitmap.getHeight());
			_dstRect.left = (int)(opt.getX());
			_dstRect.top = (int)(opt.getY());
			_dstRect.right = _dstRect.left + _srcRect.width();
			_dstRect.bottom = _dstRect.top + _srcRect.height();
			as.getCanvas().drawBitmap(_bitmap, _srcRect, _dstRect, null);
		}
	}

	@Override
	public int getWidth() {
		if (_bitmap != null) {
			return _bitmap.getWidth();
		} else {
			return 0;
		}
	}

	@Override
	public int getHeight() {
		if (_bitmap != null) {
			return _bitmap.getHeight();
		} else {
			return 0;
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Load methods
	///////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean loadFromRessource(Resources res, int id) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inScaled = false;
		_bitmap = BitmapFactory.decodeResource(res, id, opt);	
		return (_bitmap != null);
	}

}
