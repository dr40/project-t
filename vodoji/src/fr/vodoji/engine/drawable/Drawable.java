package fr.vodoji.engine.drawable;

import fr.vodoji.engine.graphics.Screen;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public interface Drawable {

	public void draw(Screen toScreen);
	public void draw(Screen toScreen, float x, float y);
	public void draw(Screen toScreen, DrawOptions opt);
	
}
