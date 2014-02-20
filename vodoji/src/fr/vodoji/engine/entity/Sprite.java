package fr.vodoji.engine.entity;


import fr.vodoji.engine.drawable.DrawOptions;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.engine.graphics.Screen;
import fr.vodoji.engine.graphics.animation.Animation;
import fr.vodoji.engine.graphics.animation.AnimationData;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class Sprite extends Entity {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected Image _img;
	protected Animation _anim;
	protected boolean centerMode;
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructor
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public Sprite() {
		_anim = new Animation();
		centerMode = true;
	}
	public Sprite(Image img) {
		_img = img;
		centerMode = true;
	}
	public Sprite(AnimationData anim) {
		_anim.setAnimationData(anim);
		centerMode = true;
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////
	// Getters
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isAnimation() {
		return (_anim != null);
	}
	public Image getImage() {
		return _img;
	}
	public AnimationData getAnimationData() {
		return _anim.getAnimationData();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Setters
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public void setImage(Image img) {
		_img = img;
		_anim.setAnimationData(null);
	}
	public void setImage(AnimationData anim) {
		_img = null;
		_anim.setAnimationData(anim);
	}
	public void setImage(AnimationData anim, String frame) {
		_img = null;
		_anim.setAnimationData(anim);
		_anim.start(frame);
	}
	public void pauseAnimation() {
		_anim.pause();
	}
	public void resumeAnimation() {
		_anim.resume();
	}
	public void stopAnimation() {
		_anim.stop();
	}
	public void resetAnimation() {
		_anim.reset();
	}
	public boolean isAnimationPlaying() {
		return _anim.isPlaying();
	}
	public boolean isAnimationPaused() {
		return _anim.isPaused();
	}
	public boolean isAnimationStopped() {
		return _anim.isStopped();
	}
	public void setFrame(String frameName) {
		_anim.start(frameName);
	}

	public int getLoopingCount() {
		return _anim.getLoopingCount();
	}
	public void setLoopingCount(int looping) {
		_anim.setLoopingCount(looping);
	}

	public void setCenterMode(boolean centered) {
		centerMode = centered;
	}
	public boolean isCenterMode() {
		return centerMode;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Draw
	///////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void draw(Screen toScreen) {
		float x = _x;
		float y = _y;
		if (_img != null) {
			if (centerMode) {
				x -= (_img.getWidth()/2);
				y -= (_img.getHeight()/2);
			}
			_img.draw(toScreen, x, y);
		} else {
			_anim.centerMode = centerMode;
			_anim.draw(toScreen, x, y);
		}
	}

	@Override
	public void draw(Screen toScreen, float x, float y) {
		if (_img != null) {
			_img.draw(toScreen, x, y);
		} else {
			_anim.draw(toScreen, x, y);
		}
	}

	@Override
	public void draw(Screen toScreen, DrawOptions opt) {
		if (_img != null) {
			_img.draw(toScreen, opt);
		} else {
			_anim.draw(toScreen, opt);
		}
	}
	
	
}
