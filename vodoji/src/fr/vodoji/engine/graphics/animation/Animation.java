package fr.vodoji.engine.graphics.animation;

import android.graphics.Rect;
import android.os.SystemClock;
import fr.vodoji.engine.drawable.DrawOptions;
import fr.vodoji.engine.drawable.Drawable;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.engine.graphics.Screen;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class Animation implements Drawable {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////
	
	private AnimationData _currentAnimationData;
	private AnimationFrame _currentFrame;
	private Image _currentImage;
	private AnimationFrameItem _currentFrameItem;
	private long _lastTick;
	private boolean _paused;
	private int _currentFrameItemId;
	private DrawOptions _drawOptions;
	public boolean centerMode;
	private int _loopingCount; /// -1 = Infinite, 0 = No repeat, 1 = once, ....
	private int _loopDone;
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructor
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public Animation() {
		_drawOptions = new DrawOptions();
		_lastTick = 0;
		_paused = false;
		_currentFrameItemId = -1;
		centerMode = true;
		_loopingCount = -1;
		_loopDone = 0;
	}
	


	///////////////////////////////////////////////////////////////////////////////////////////
	// Animation methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public void setAnimationData(AnimationData data) {
		_currentAnimationData = data;
		start();
	}
	public AnimationData getAnimationData() {
		return _currentAnimationData;
	}
	
	public boolean start() {
		if (_currentAnimationData != null) {
			if (_currentFrame == null) {
				return start(_currentAnimationData.getDefaultFrameName());
			} else {
				resume();
				return true;
			}
		} else {
			return false;
		}
	}
	public boolean start(String name) {
		if (_currentAnimationData != null) {
			/* Seek frame */
			AnimationFrame frm = _currentAnimationData.getFrame(name);
			if (frm != null) {
				_paused = false;
				_currentFrame = frm;
				_currentFrameItemId = -1;
				_loopDone = 0;
				return true;
			}
		}
		return false;
	}
	public void pause() {
		_paused = true;
	}
	public void resume() {
		if (_paused) {
			_paused = false;
			_lastTick = SystemClock.elapsedRealtime();
		}
	}
	public void stop() {
		_paused = true;
		_currentFrameItemId = -1;
		_loopDone = 0;
	}
	public void reset() {
		_currentFrameItemId = -1;
		_loopDone = 0;
	}
	public boolean isPlaying() {
		return ((!_paused) && (_currentFrameItemId >= 0));
	}
	public boolean isPaused() {
		return _paused;
	}
	public boolean isStopped() {
		return ((_paused) && (_currentFrameItemId < 0));
	}
	public int getLoopingCount() {
		return _loopingCount;
	}
	public void setLoopingCount(int looping) {
		_loopingCount = looping;
		_loopDone = 0;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Draw/Update
	///////////////////////////////////////////////////////////////////////////////////////////

	protected void update() {
		/* Initialize */
		if (_currentAnimationData == null) return ;
		if (_currentFrame == null) return ;
		long tick = SystemClock.elapsedRealtime();
		/* Check if initialized */
		if (_currentFrameItemId < 0) {
			/* Init */
			if (_currentFrame.size() > 0) {
				_currentFrameItemId = 0;
				_currentFrameItem = _currentFrame.get(_currentFrameItemId);
				_currentImage = _currentAnimationData.getImage(_currentFrameItem.getImageId());
				_drawOptions.crop(_currentFrameItem.getCrop());
				_lastTick = tick;
			}
		}
		/* Compute CurrentImage */
		if ((!_paused) && (_currentFrameItemId >= 0)) {
			/* Check if need to step */
			if (tick - _lastTick >= _currentFrameItem.getDelay()) {
				_currentFrameItemId++;
				if (_currentFrameItemId >= _currentFrame.size()) {
					_loopDone++;
					if ((_loopingCount >= 0) && (_loopDone > _loopingCount)) {
						_paused = true;
						_currentFrameItemId = -1;
						return ;
					} else {
						_currentFrameItemId = 0;
					}
				}
				_currentFrameItem = _currentFrame.get(_currentFrameItemId);
				_currentImage = _currentAnimationData.getImage(_currentFrameItem.getImageId());
				_drawOptions.crop(_currentFrameItem.getCrop());
				_lastTick = tick;
			}
		}
	}
	public void draw(Screen toScreen) {
		draw(toScreen, 0, 0);
	}
	public void draw(Screen toScreen, float x, float y) {
		update();
		if (_currentImage != null) {
			if (centerMode) {
				Rect r = _drawOptions.getCrop();
				x -= r.width()/2;
				y -= r.height()/2;
			}
			_drawOptions.move(x, y);
			_currentImage.draw(toScreen, _drawOptions);
		}
	}
	public void draw(Screen toScreen, DrawOptions opt) {
		draw(toScreen, opt.getX(), opt.getY());
	}
	
}
