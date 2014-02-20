package fr.vodoji.engine.graphics.animation;

import android.graphics.Rect;
import fr.vodoji.utils.json.JSON;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class AnimationFrameItem {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected int _imageId;
	protected Rect _crop;
	protected int _delay;

	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructor
	///////////////////////////////////////////////////////////////////////////////////////////

	public AnimationFrameItem(JSON json) {
		_imageId = json.getChildIntValue("image", 0);
		_crop = new Rect();
		_crop.left = json.getChildIntValue("crop-x");
		_crop.top = json.getChildIntValue("crop-y");
		_crop.right = _crop.left + json.getChildIntValue("crop-w");
		_crop.bottom = _crop.top + json.getChildIntValue("crop-h");
		_delay = json.getChildIntValue("delay");
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Getters
	///////////////////////////////////////////////////////////////////////////////////////////

	public final int getCropX() {
		return _crop.left;
	}
	public final int getCropY() {
		return _crop.top;
	}
	public final int getCropW() {
		return _crop.right - _crop.left;
	}
	public final int getCropH() {
		return _crop.bottom - _crop.top;
	}
	public final Rect getCrop() {
		return _crop;
	}
	public final int getImageId() {
		return _imageId;
	}
	public final int getDelay() {
		return _delay;
	}
	
}
