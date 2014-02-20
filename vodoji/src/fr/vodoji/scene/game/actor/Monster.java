package fr.vodoji.scene.game.actor;

import fr.vodoji.engine.entity.Sprite;
import fr.vodoji.engine.graphics.animation.AnimationData;
import fr.vodoji.utils.math.MathUtils;

/**
 * @author Dorian
 *
 */
public class Monster extends Sprite {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected int _type;
	protected int _startType;
	protected int _animAngle;
	protected float _speed;
	protected int _health;
	public double _dist;
	

	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public Monster(AnimationData anim, int type, int health, float x, float y, float speed) {
		setImage(anim);
		_startType = type;
		_type = type;
		_health = health;
		_animAngle = -1;
		_x = x;
		_y = y;
		_speed = speed;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Update
	///////////////////////////////////////////////////////////////////////////////////////////

	public void update(float timeDiff, float centerX, float centerY) {
		/* Update angle */
		double angle = MathUtils.findRotation(_x + 16, _y + 16, centerX, centerY);
		double angleToDeg = Math.toDegrees(angle);
		int newAnimAngle = Math.round((int)(angleToDeg) % 360);
		if (newAnimAngle < 0) newAnimAngle = 0;
		else if (newAnimAngle > 360) newAnimAngle = 360;
		newAnimAngle = (int)(newAnimAngle / 45) * 45;
		if (_animAngle != newAnimAngle) {
			/* Change frame */
			setFrame("ship" + _type + ":" + newAnimAngle);
			_animAngle = newAnimAngle;
		}
		/* Move */
		_x = _x - (float)(Math.sin(angle))*(_speed*timeDiff);
		_y = _y - (float)(Math.cos(angle))*(_speed*timeDiff);
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

	public float getSpeed() {
		return _speed;
	}

	public int getHealth() {
		return _health;
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Setters
	///////////////////////////////////////////////////////////////////////////////////////////

	public void decHealth() {
		if (_health == 0) return ;
		_health--;
		/* Change frame */
		//_type--;
		//setFrame("ship" + _type + ":" + _animAngle);
	}
	public void setHealth(int health) {
		_health = health;
	}
}
