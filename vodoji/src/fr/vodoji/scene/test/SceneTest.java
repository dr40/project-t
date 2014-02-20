package fr.vodoji.scene.test;

import java.util.LinkedList;

import fr.vodoji.R;
import android.graphics.Color;
import android.view.MotionEvent;
import fr.vodoji.engine.graphics.Font;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.engine.graphics.animation.AnimationData;
import fr.vodoji.engine.scene.Scene;

public class SceneTest extends Scene {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected Image _gfxBack;
	protected Image _gfxAlien;
	protected AnimationData _gfxAnimShip;
	protected LinkedList<AlienEntity> _entities;
	protected Font _fnt1;
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public SceneTest() {
		_entities = new LinkedList<AlienEntity>();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Event
	///////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onLoad() {
		_gfxBack = loadImage(R.drawable.background);
		_gfxAlien = loadImage(R.drawable.alien);
		_gfxAnimShip = new AnimationData();
		_gfxAnimShip.addImage(loadImage(R.drawable.alien));
		_gfxAnimShip.loadFromString(loadStringResource(R.raw.anim_ship));
		for(int i = 0; i < 10; i++) {
			AlienEntity e = new AlienEntity();
			e.setImage(_gfxAnimShip);
			_entities.add(e);
		}
		_fnt1 = loadFont(Color.WHITE, 12);
	}

	@Override
	public void onShow(Scene previousScene) {
		System.out.println("Scene test - show");
	}

	@Override
	public void onHide() {
		System.out.println("Scene test - hide");
	}

	@Override
	public void onUpdate(float timeDiff) {
		for(AlienEntity e : _entities) {
			e.update();
		}
	}

	@Override
	public void onRender() {
		draw(_gfxBack);
		for(AlienEntity e : _entities) {
			drawText("Entity", e.getX(), e.getY(), _fnt1);
			draw(e);
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			for(AlienEntity e : _entities) {
				e.setFrame("hit");
				vibrate(500);
			}
			//gotoScene("test2");
		}
	}


}
