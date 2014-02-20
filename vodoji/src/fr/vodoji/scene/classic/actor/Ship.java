package fr.vodoji.scene.classic.actor;

import fr.vodoji.engine.entity.Sprite;
import fr.vodoji.engine.graphics.animation.AnimationData;

public class Ship extends Sprite {
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected int _type;
	protected int _startType;
	protected int _startAngle;
	protected long _lifeTime; ///< Life time in 'ms' indicate when to destroy the ship
	protected long _lastTick;
	protected boolean _tapped;
	protected int _mode; //< 0 = LifeTime running, 1 = WaitToBeDestroyed (2s), 2 = WaitEndOfDestroyAnim, 3 = Wait to be deleted
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public Ship(AnimationData anim, int startAngle, int type, long lifeTime, float x, float y, long tick) {
		setImage(anim);
		if (startAngle < 0) startAngle = 0;
		if (startAngle > 315) startAngle = 315;
		_startAngle = startAngle;
		setFrame("ship" + type + ":" + _startAngle);
		pauseAnimation();
		
		_startType = type;
		_type = type;
		_lifeTime = lifeTime;
		_x = x;
		_y = y;
		_lastTick = tick;
		_tapped = false;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Update
	///////////////////////////////////////////////////////////////////////////////////////////

	public void update(float timeDiff, long tick) {
		/* Update animation */
		if (_mode == 0) {
			if (tick - _lastTick >= _lifeTime) {
				_mode = 1;
				resumeAnimation();
				_lastTick = tick;
			}
		} else if (_mode == 1) {
			if (tick - _lastTick > 2000) {
				_mode = 2;
				setLoopingCount(0);
				setFrame("destroy");
				_lastTick = tick;
			}
		} else if (_mode == 2) {
			if (isAnimationStopped()) {
				/* Destroyed */
				_mode = 3;
			}
		}
	}
	
	public boolean onTap(long tick) {
		if (_mode == 1) {
			_mode = 2;
			setLoopingCount(0);
			setFrame("small-destroy");
			_lastTick = tick;
			_tapped = true;
			return true;
		} else {
			return false;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Getters
	///////////////////////////////////////////////////////////////////////////////////////////

	public int getType() {
		return _type;
	}
	public int getStartType() {
		return _startType;
	}

	public int getMode() {
		return _mode;
	}
	public boolean isTapped() {
		return _tapped;
	}

	
}
