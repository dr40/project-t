package fr.vodoji.engine.entity;

import fr.vodoji.engine.drawable.Drawable;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public abstract class Entity implements Drawable {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////
	
	protected float _x;
	protected float _y;
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructor
	///////////////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////////////
	// Getters
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public float getX() {
		return _x;
	}
	public float getY() {
		return _y;
	}

	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Setters
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public void setX(float x) {
		_x = x;
	}
	public void setY(float y) {
		_y = y;
	}
	public void move(float x, float y) {
		_x = x;
		_y = y;
	}
	
	
}