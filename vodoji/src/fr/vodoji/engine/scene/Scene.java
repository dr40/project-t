package fr.vodoji.engine.scene;



import java.util.LinkedList;

import android.view.MotionEvent;
import fr.vodoji.engine.audio.Audio;
import fr.vodoji.engine.audio.Music;
import fr.vodoji.engine.audio.Sound;
import fr.vodoji.engine.drawable.DrawOptions;
import fr.vodoji.engine.drawable.Drawable;
import fr.vodoji.engine.graphics.Font;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.engine.graphics.Screen;
import fr.vodoji.engine.graphics.animation.AnimationData;
import fr.vodoji.engine.Engine;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public abstract class Scene {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////
	
	protected Engine _engine;
	protected LinkedList<SceneListener> _listeners;
	protected boolean _activated;
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public Scene() {
		_engine = null;
		_listeners = new LinkedList<SceneListener>();
		_activated = false;
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////
	// Init methods
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public void init(Engine engine) {
		_engine = engine;
		_activated = true;
		onLoad();
		for(SceneListener s : _listeners) {
			s.onLoaded(this);
		}
	}
	
	public Engine getEngine() {
		return _engine;
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Actions/Listeners
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public void addListener(SceneListener listener) {
		_listeners.add(listener);
	}
	public void removeListener(SceneListener listener) {
		_listeners.remove(listener);
	}
	
	public void unload() {
		_activated = false;
		for(SceneListener s : _listeners) {
			s.onUnloaded(this);
		}
	}
	public void activate() {
		_activated = true;
	}
	public boolean isActivated() {
		return _activated;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Events to override
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public abstract void onLoad();
	public abstract void onShow(Scene previousScene);
	public abstract void onHide();
	public abstract void onUpdate(float timeDiff);
	public abstract void onRender();
	public abstract void onTouch(MotionEvent event);
	

	///////////////////////////////////////////////////////////////////////////////////////////
	// Draw methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public void draw(Drawable drawable) {
		if (_engine != null) {
			drawable.draw(_engine.getScreen());
		}
	}
	public void draw(Drawable drawable, float x, float y) {
		if (_engine != null) {
			drawable.draw(_engine.getScreen(), x, y);
		}
	}
	public void draw(Drawable drawable, DrawOptions opts) {
		if (_engine != null) {
			drawable.draw(_engine.getScreen(), opts);
		}
	}
	public void drawText(String text, float x, float y, Font fnt) {
		if (_engine != null) {
			_engine.getScreen().drawText(text, x, y, fnt);
		}
	}
	
	public void tile(Drawable drawable, int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
		if (_engine != null) {
			Screen screen = _engine.getScreen();
			int x = 0;
			int y;
			while (x <= dstWidth) {
				y = 0;
				while (y <= dstHeight) {
					drawable.draw(screen, x, y);
					y += srcHeight;
				}
				x += srcWidth;
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Graphics - helper methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public int getScreenWidth() {
		if (_engine != null) {
			return _engine.getScreenWidth();
		} else {
			return 0;
		}
	}
	public int getScreenHeight() {
		if (_engine != null) {
			return _engine.getScreenHeight();
		} else {
			return 0;
		}
	}
	
	public String loadStringResource(int resId) {
		if (_engine != null) {
			return _engine.loadStringResource(resId);
		} else {
			return "";
		}
	}
	
	public Image loadImage(int id) {
		if (_engine != null) {
			return _engine.loadImage(id);
		} else {
			return null;
		}
	}
	public AnimationData loadAnimationData(int resId, Image img) {
		if (_engine != null) {
			return _engine.loadAnimationData(resId, img);
		} else {
			return null;
		}
	}
	public Font loadFont(float size) {
		if (_engine != null) {
			return _engine.loadFont(size);
		} else {
			return null;
		}
	}
	public Font loadFont(int color) {
		if (_engine != null) {
			return _engine.loadFont(color);
		} else {
			return null;
		}
	}
	public Font loadFont(int color, float size) {
		if (_engine != null) {
			return _engine.loadFont(color, size);
		} else {
			return null;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Other engine - helper methods
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean vibrate(int delay) {
		if (_engine != null) {
			return _engine.vibrate(delay);
		} else {
			return false;
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Audio - helper methods
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public Audio loadAudio(int resId) {
		if (_engine != null) {
			return _engine.loadAudio(resId);
		} else {
			return null;
		}
	}
	public Sound loadSound(int resId) {
		if (_engine != null) {
			return _engine.loadSound(resId);
		} else {
			return null;
		}
	}
	public Music loadMusic(int resId) {
		if (_engine != null) {
			return _engine.loadMusic(resId);
		} else {
			return null;
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Scene - helper methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public boolean gotoScene(String name) {
		if (_engine != null) {
			return _engine.gotoScene(name);
		} else {
			return false;
		}
	}
	public boolean showScene(String name) {
		if (_engine != null) {
			return _engine.showScene(name);
		} else {
			return false;
		}
	}
}
