package fr.vodoji.android;

import fr.vodoji.android.graphics.AndroidFont;
import fr.vodoji.android.graphics.AndroidImage;
import fr.vodoji.android.graphics.AndroidScreen;
import fr.vodoji.engine.Engine;
import fr.vodoji.engine.graphics.Font;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.engine.graphics.Screen;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class AndroidEngine extends Engine {
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////
	
	protected AndroidScreen _screen;
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public AndroidEngine() {
		_screen = new AndroidScreen();
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	///////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public Screen getScreen() {
		return _screen;
	}

	@Override
	public Image createImage() {
		return new AndroidImage();
	}

	@Override
	public Font createFont() {
		return new AndroidFont();
	}

}
