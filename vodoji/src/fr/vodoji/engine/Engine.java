package fr.vodoji.engine;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Stack;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.MotionEvent;

import fr.vodoji.engine.audio.Audio;
import fr.vodoji.engine.audio.AudioPlayer;
import fr.vodoji.engine.audio.Music;
import fr.vodoji.engine.audio.Sound;
import fr.vodoji.engine.graphics.Font;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.engine.graphics.Screen;
import fr.vodoji.engine.graphics.animation.AnimationData;
import fr.vodoji.engine.scene.Scene;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public abstract class Engine {
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////
	
	protected HashMap<String, Scene> _scenes;
	protected Stack<Scene> _sceneStack;
	protected Resources _res;
	protected Context _context;
	protected AudioPlayer _player;
	protected long _lastTick;
	protected long _currentTick;
	protected float _timeDiff;
	protected int _fps;
	protected int _fpsInOneSecond;
	protected long _fpsLastTick;	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructor
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public Engine() {
		_scenes = new HashMap<String, Scene>();
		_sceneStack = new Stack<Scene>();
		_currentTick = SystemClock.elapsedRealtime();
		_fpsLastTick = _currentTick;
		_timeDiff = ((float)(_currentTick - _lastTick) / 1000.0f);
		_lastTick = _currentTick;
		_fpsInOneSecond = 0;
		_fps = 0;
	}
	
	public void initialize(Context ctx, Resources res) {
		_context = ctx;
		_res = res;
	}
	
	public void setContext(Context ctx) {
		_context = ctx;
	}
	public Context getContext() {
		return _context;
	}
	public void setResources(Resources res) {
		_res = res;
	}
	public Resources getResources() {
		return _res;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Scene management
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public void addScene(String name, Scene scene) {
		scene.init(this);
		_scenes.put(name,  scene);
	}
	public boolean gotoScene(String name) {
		if (_scenes.containsKey(name)) {
			/* Get current scene */
			Scene currentScene = ((_sceneStack.size() > 0) ? _sceneStack.firstElement() : null);
			/* Seek if scene already shown */
			Scene sceneToShow = _scenes.get(name);
			int sceneCountBeforeSceneToShow = 0;
			boolean sceneFound = false;
			for(Scene s : _sceneStack) {
				if (s == sceneToShow) {
					sceneFound = true;
					break;
				} else {
					sceneCountBeforeSceneToShow++;
				}
			}
			/* Show current scene */
			if (sceneFound) {
				/* Remove all above scene */
				for(int i = 0; i < sceneCountBeforeSceneToShow; i++) {
					hideScene();
				}
				/* Call show event */
				sceneToShow.onShow(currentScene);
			} else {
				/* Call show event */
				_sceneStack.add(0, sceneToShow);
				sceneToShow.onShow(currentScene);
			}
			return true;
		} else {
			return false;
		}
	}
	public boolean showScene(String name) {
		if (_scenes.containsKey(name)) {
			/* Get current scene */
			Scene currentScene = ((_sceneStack.size() > 0) ? _sceneStack.firstElement() : null);
			/* Show scene */
			Scene sceneToShow = _scenes.get(name);
			sceneToShow.onShow(currentScene);
			_sceneStack.add(0, sceneToShow);
			return true;
		} else {
			return false;
		}
	}
	public void hideScene() {
		/* Pop scene */
		if (_sceneStack.size() > 0) {
			Scene s = _sceneStack.remove(0);
			s.onHide();
		}
	}
	public Scene getCurrentScene() {
		if (_sceneStack.size() > 0) {
			return _sceneStack.firstElement();
		} else {
			return null;
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Event management
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public void onTouch(MotionEvent event) {
		/* Call current scene event */
		if (_sceneStack.size() > 0) {
			if (_sceneStack.firstElement().isActivated()) {
				_sceneStack.firstElement().onTouch(event);
			}
		}
	}	
	public void onRender() {
		/* Call current scene event */
		if (_sceneStack.size() > 0) {
			if (_sceneStack.firstElement().isActivated()) {
				_sceneStack.firstElement().onRender();
			}
		}
	}
	public void onUpdate() {
		/* Update FPS/TimeDiff */
		_currentTick = SystemClock.elapsedRealtime();
		if (_fpsLastTick + 1000 < _currentTick) {
			_fps = _fpsInOneSecond;
			_fpsLastTick = _currentTick;
			_fpsInOneSecond = 0;
		}
		_fpsInOneSecond++;
		_timeDiff = ((float)(_currentTick - _lastTick) / 1000.0f);
		_lastTick = _currentTick;
		/* Call current scene event */
		if (_sceneStack.size() > 0) {
			_sceneStack.firstElement().onUpdate(_timeDiff);
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Graphics
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public abstract Screen getScreen();
	public abstract Image createImage();
	public abstract Font createFont();

	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Audio
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public AudioPlayer getAudioPlayer() {
		return new AudioPlayer();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Frame infos
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public long getTick() {
		return _currentTick;
	}
	public float getTimeDiff() {
		return _timeDiff;
	}
	public int getFPS() {
		return _fps;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Others
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean vibrate(int delay) {
		try {
			Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(delay);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Graphics - helper methods
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public int getScreenWidth() {
		Screen s = getScreen();
		if (s != null) {
			return s.getWidth();
		} else {
			return 0;
		}
	}
	public int getScreenHeight() {
		Screen s = getScreen();
		if (s != null) {
			return s.getHeight();
		} else {
			return 0;
		}
	}
	
	
	public String loadStringResource(int resId) {
		try {
			InputStream reader = getResources().openRawResource(resId);
			byte[] b = new byte[reader.available()];
			reader.read(b);
			return new String(b);
		} catch (Exception e) {
			return "";
		}
	}
	
	public Image loadImage(int resId) {
		Image img = createImage();
		if ((img != null) && (_res != null)) {
			img.loadFromRessource(_res, resId);
		}
		return img;
	}
	
	public AnimationData loadAnimationData(int resId, Image img) {
		AnimationData anim = new AnimationData();
		anim.addImage(img);
		anim.loadFromString(loadStringResource(resId));
		return anim;
	}
	
	public Font loadFont(float size) {
		Font fnt = createFont();
		if (fnt != null) {
			fnt.setSize(size);
		}
		return fnt;
	}
	public Font loadFont(int color) {
		Font fnt = createFont();
		if (fnt != null) {
			fnt.setColor(color);
		}
		return fnt;
	}
	public Font loadFont(int color, float size) {
		Font fnt = createFont();
		if (fnt != null) {
			fnt.setColor(color);
			fnt.setSize(size);
		}
		return fnt;
	}
	public Audio loadAudio(int resId) {
		if (_context != null) {
			Audio a = new Audio();
			if (a.loadFromContext(_context, resId)) {
				return a;
			}
		}
		return null;
	}
	public Sound loadSound(int resId) {
		if (_context != null) {
			Sound s = new Sound();
			if (s.loadFromContext(_context, resId)) {
				return s;
			}
		}
		return null;
	}
	public Music loadMusic(int resId) {
		if (_context != null) {
			Music m = new Music();
			if (m.loadFromContext(_context, resId)) {
				return m;
			}
		}
		return null;
	}
	
}
