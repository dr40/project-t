package fr.vodoji.engine.graphics;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public abstract class Screen {
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Getters
	///////////////////////////////////////////////////////////////////////////////////////////

	public abstract int getWidth();
	public abstract int getHeight();
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Specific Draw Methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public abstract void drawText(String text, float x, float y, Font fnt);
	
	
}
