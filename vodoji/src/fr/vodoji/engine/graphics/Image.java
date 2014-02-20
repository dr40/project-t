package fr.vodoji.engine.graphics;

import fr.vodoji.engine.drawable.Drawable;
import android.content.res.Resources;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public abstract class Image implements Drawable {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public abstract int getWidth();
	public abstract int getHeight();

	public abstract boolean loadFromRessource(Resources res, int id);
	
}
