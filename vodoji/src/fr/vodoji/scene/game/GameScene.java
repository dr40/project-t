package fr.vodoji.scene.game;

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
import fr.vodoji.scene.game.actor.Heros;
import fr.vodoji.scene.game.actor.Monster;
import fr.vodoji.utils.math.MathUtils;

public class GameScene extends Scene {
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected Image gfxBack;
	protected Image gfxMonster;
	protected Image gfxHeros;
	protected Image gfxStuff;
	protected Image gfxTarget;
	protected AnimationData animBack;
	protected AnimationData animMonster;
	protected AnimationData animHeros;
	protected AnimationData animStuff;
	
	protected LinkedList<Monster> _monsters;
	protected LinkedList<Text> _scores;
	protected Heros _heros;
	protected Font fntScore;
	protected Font fntScoreOk;
	protected Font fntScoreBad;
	protected Font fntLives;
	
	protected Sprite _target;
	
	protected int _score;
	protected int _lives;
	protected Random _rand;
	protected MediaPlayer music1;
	protected MusicRhythmData musicRhythm;
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public GameScene(MediaPlayer music) {
		music1 = music;
		musicRhythm = new MusicRhythmData(500);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Game Logic
	///////////////////////////////////////////////////////////////////////////////////////////

	public void addMonster(int type) {
		/* Choose a side */
		int r = _rand.nextInt(4000);
		float sx;
		float sy;
		if (r < 1000) {
			/* Left */
			sx = -32;
			sy = MathUtils.rndInt(getScreenHeight()+64)-32;
		} else if (r < 2000) {
			/* Top */
			sx = MathUtils.rndInt(getScreenWidth()+64)-32;
			sy = -32;
		} else if (r < 3000) {
			/* Right */
			sx = getScreenWidth();
			sy = MathUtils.rndInt(getScreenHeight()+64)-32;
		} else {
			/* Bottom */
			sx = MathUtils.rndInt(getScreenWidth()+64)-32;
			sy = getScreenHeight();
		}
		/* Select a type */
		_monsters.add(new Monster(animMonster, type, 1, sx, sy, 20f * (type+1)));
		
	}
	public void tapOnMonster(Monster monster) {
		monster.decHealth();
		if (monster.getHealth() <= 0) {
			monster.setFrame("small-destroy");
			monster.setLoopingCount(0);
			/* Add score */
			int scoreAmmount = (monster.getStartType()) * 100;
			if (scoreAmmount == 0) scoreAmmount = 100;
			updateScore(scoreAmmount, monster.getX(), monster.getY());
			if (GameGlobals.vibrator) {
				vibrate(100);
			}
		}
	}
	public void destroyMonster(Monster monster) {
		_monsters.remove(monster);
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
		_monsters = new LinkedList<Monster>();
		_scores = new LinkedList<Text>();
		_rand = new Random(SystemClock.elapsedRealtime());
		_lives = 5;
		/* Load Font */
		fntScore = loadFont(Color.WHITE, 12);
		fntScoreOk = loadFont(Color.GREEN, 10);
		fntScoreBad = loadFont(Color.RED, 10);
		fntLives = loadFont(Color.WHITE, 12);
		/* Load GFX */
		gfxBack = loadImage(R.drawable.fnd);
		gfxMonster = loadImage(R.drawable.monsters);
		gfxHeros = loadImage(R.drawable.heros);
		gfxTarget = loadImage(R.drawable.target);
		/* Load Animation */
		animMonster = loadAnimationData(R.raw.anim_monster, gfxMonster);
		animHeros = loadAnimationData(R.raw.anim_heros, gfxHeros);
		/* Load actor */
		_heros = new Heros();
		_heros.setImage(animHeros, "idle");
		_heros.setCenterMode(false);
		_target = new Sprite(gfxTarget);
		_target.setCenterMode(false);
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
		/* Update monsters */
		float centerX = getScreenWidth()/2 + 16;
		float centerY = getScreenHeight()/2 + 16;
		
		for(Monster m : _monsters) {
			if (m.getHealth() > 0) {
				m.update(timeDiff, centerX, centerY);
				double dist = MathUtils.dist(m.getX(), m.getY(), centerX, centerY);
				m._dist = dist;
				if (dist < 32) {
					m.setHealth(0);
					m.setFrame("destroy");
					m.setLoopingCount(0);

					_heros.setFrame("hit");
					_heros.setLoopingCount(0);
				} else if ((dist > 50) && (dist < 140)) {
					m.resumeAnimation();
				} else {
					m.pauseAnimation();
				}
			}
			if ((m.getHealth() <= 0) && (m.isAnimationStopped())) {
				destroyMonster(m);
				break;
			}
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
		/* Update heros */
		if (_heros.isAnimationStopped()) {
			_heros.setLoopingCount(-1);
			_heros.setFrame("idle");
			/* Add score */
			updateScore(-1000, centerX, centerY);
			if (GameGlobals.vibrator) {
				vibrate(1000);
			}
		}
		int prob = musicRhythm.probPopMonster(music1.getCurrentPosition(), music1.getDuration());
		if (GameGlobals.currentLevel == 1) {
			if (prob > 50) addMonster(_rand.nextInt(3));
		} else if (GameGlobals.currentLevel == 2) {
				if (prob > 40) addMonster(_rand.nextInt(4));
		} else if (GameGlobals.currentLevel == 3) {
			if (prob > 25) addMonster(_rand.nextInt(4));
		} else {
			if (prob > 20) addMonster(_rand.nextInt(5));
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
		drawText("Temps restant: " + ((music1.getDuration() - music1.getCurrentPosition())/1000) + "s", 0, 54, fntLives);
		/* Draw heros */
		float posX = getScreenWidth()/2 ;
		float posY = getScreenHeight()/2;
		draw(_target, posX - _target.getImage().getWidth()/2, posY - _target.getImage().getHeight()/2);
		draw(_heros, posX, posY);
		/* Draw Monster */
		for(Monster m : _monsters) {
			draw(m);
			//drawText("Debug:" + m._dist, m.getX(), m.getY(), fntScore);
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
			float centerX = getScreenWidth()/2 + 16;
			float centerY = getScreenHeight()/2 + 16;
			/* Check if tap on Monster */
			for(Monster m : _monsters) {
				float mX = m.getX()-32;
				float mY = m.getY()-32;
				if ((m.getHealth() > 0) && (x >= mX) && (y >= mY) && (x < mX + 64) && (y <= mY + 64)) {
					double dist = MathUtils.dist(m.getX(), m.getY(), centerX, centerY);
					if ((dist > 50) && (dist < 140)) {
						/* Delete monster */
						tapOnMonster(m);
					} else {
						/* Reduce score */
						updateScore(-100, mX, mY);
					}
					return ;
				}
			}
		}
	}

}
