package fr.vodoji.scene.classic;

import java.util.LinkedList;
import java.util.Random;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.view.MotionEvent;
import fr.vodoji.GameGlobals;
import fr.vodoji.R;
import fr.vodoji.data.MusicRhythmData;
import fr.vodoji.engine.entity.Sprite;
import fr.vodoji.engine.entity.Text;
import fr.vodoji.engine.graphics.Font;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.engine.graphics.animation.AnimationData;
import fr.vodoji.engine.scene.Scene;
import fr.vodoji.scene.classic.actor.Ship;
import fr.vodoji.scene.game.actor.Heros;
import fr.vodoji.utils.math.MathUtils;

public class ClassicGameScene extends Scene {
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected Image gfxBack;
	protected Image gfxShip;
	protected AnimationData animShip;

	protected LinkedList<Ship> _ships;
	protected LinkedList<Text> _scores;
	protected Heros _heros;
	protected Font fntScore;
	protected Font fntScoreOk;
	protected Font fntScoreBad;
	
	protected Sprite _target;
	
	protected int _score;

	protected Random _rand;
	
	protected MediaPlayer music1;
	protected MusicRhythmData musicRhythm;
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public ClassicGameScene(MediaPlayer music) {
		music1 = music;
		musicRhythm = new MusicRhythmData(1000);
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Game Logic
	///////////////////////////////////////////////////////////////////////////////////////////

	public void addShip(int type) {
		/* Choose a side */
		float sx = MathUtils.rndInt(getScreenWidth() - 100) + 50;
		float sy  = MathUtils.rndInt(getScreenHeight() - 100) + 50;
		int angle = MathUtils.rndInt(8)*45;
		/* Select a type */
		if (type <= 0) type = 1;
		_ships.add(new Ship(animShip, angle, type, 4000/type, sx, sy, _engine.getTick()));
		
	}
	public void tapOnShip(Ship s) {
		if (s.onTap(_engine.getTick())) {
			/* Add score */
			int scoreAmmount = (s.getStartType()) * 100;
			if (scoreAmmount == 0) scoreAmmount = 100;
			updateScore(scoreAmmount, s.getX(), s.getY());
			if (GameGlobals.vibrator) {
				vibrate(100);
			}
		} else {
			/* Remove score */
			updateScore(-100, s.getX(), s.getY());
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Scores
	///////////////////////////////////////////////////////////////////////////////////////////

	public void updateScore(int ammount, float fromX, float fromY) {
		/* Change score */
		_score += ammount;
		if (_score < 0) _score = 0;
		Text t;
		if (ammount > 0) {
			t = new Text("+" + ammount, fntScoreOk);
		} else {
			t = new Text("-" + ammount, fntScoreBad);
		}
		t.move(fromX, fromY);
		_scores.add(t);
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Events
	///////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onLoad() {
		/* Initialize */
		_ships = new LinkedList<Ship>();
		_scores = new LinkedList<Text>();
		_rand = new Random(SystemClock.elapsedRealtime());
		/* Load Font */
		fntScore = loadFont(Color.WHITE, 12);
		fntScoreOk = loadFont(Color.GREEN, 10);
		fntScoreBad = loadFont(Color.RED, 10);
		/* Load GFX */
		gfxBack = loadImage(R.drawable.background);
		gfxShip = loadImage(R.drawable.monsters);
		/* Load Animation */
		animShip = loadAnimationData(R.raw.anim_monster, gfxShip);
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
		/* Update Ship */
		long tick = _engine.getTick();
		LinkedList<Ship> shipToDelete = new LinkedList<Ship>();
		for(Ship s : _ships) {
			s.update(timeDiff, tick);
			if (s.getMode() >= 3) {
				if (!s.isTapped()) {
					updateScore(-1000, s.getX(), s.getY());
				}
				shipToDelete.add(s);
			}
		}
		for(Ship s : shipToDelete) {
			_ships.remove(s);
		}
		/* Update scores */
		LinkedList<Text> scoreToDelete = new LinkedList<Text>();
		for(Text t : _scores) {
			float y = t.getY() - 60f * timeDiff;
			t.setY(y);
			if (y < 0) {
				scoreToDelete.add(t);
			}
		}
		for(Text t : scoreToDelete) {
			_scores.remove(t);
		}
		int prob = musicRhythm.probPopMonster(music1.getCurrentPosition(), music1.getDuration());
		if (GameGlobals.currentLevel == 1) {
			if (prob > 50) addShip(_rand.nextInt(3));
		} else if (GameGlobals.currentLevel == 2) {
				if (prob > 40) addShip(_rand.nextInt(4));
		} else if (GameGlobals.currentLevel == 3) {
			if (prob > 25) addShip(_rand.nextInt(4));
		} else {
			if (prob > 20) addShip(_rand.nextInt(5));
		}
		if (((music1.getDuration() - music1.getCurrentPosition())/1000) == 0) {
			GameGlobals.lastGameScore = _score;
			unload();
		}
	}

	@Override
	public void onRender() {
		/* Draw back */
		tile(gfxBack, gfxBack.getWidth(), gfxBack.getHeight(), getScreenWidth(), getScreenHeight());
		/* Draw hud */
		drawText("Score: " + _score, 0, 20, fntScore);
		drawText("Temps restant: " + ((music1.getDuration() - music1.getCurrentPosition())/1000) + "s", 0, 54, fntScore);
		/* Draw Monster */
		for(Ship s : _ships) {
			draw(s);
		}
		/* Draw score */
		for(Text t : _scores) {
			draw(t);
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			/* Initialize */
			float x = event.getX();
			float y = event.getY();
			/* Check if tap on Ship */
			for(Ship s : _ships) {
				float mX = s.getX()-32;
				float mY = s.getY()-32;
				if ((x >= mX) && (y >= mY) && (x < mX + 64) && (y <= mY + 64)) {
					/* Delete monster */
					tapOnShip(s);
					return ;
				}
			}
		}
	}

}
