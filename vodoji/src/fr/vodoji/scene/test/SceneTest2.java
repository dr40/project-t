package fr.vodoji.scene.test;

import android.graphics.Color;
import android.view.MotionEvent;
import fr.vodoji.R;
import fr.vodoji.engine.entity.Text;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.engine.scene.Scene;

public class SceneTest2 extends Scene {

	protected Image _gfxBack;
	protected Text txt;
	
	@Override
	public void onLoad() {
		_gfxBack = loadImage(R.drawable.background);
		txt = new Text("Scene 2", loadFont(Color.RED, 12));
		txt.move(100, 50);
	}

	@Override
	public void onShow(Scene previousScene) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(float timeDiff) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRender() {
		draw(_gfxBack);
		draw(txt);
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			gotoScene("test");
		}
	}

}
