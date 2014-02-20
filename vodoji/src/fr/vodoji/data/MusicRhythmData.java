package fr.vodoji.data;

import java.util.ArrayList;
import java.util.Random;

import android.os.SystemClock;

import fr.vodoji.utils.json.JSON;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class MusicRhythmData {

	protected int _currentPos;
	protected ArrayList<Integer> _rythms;
	protected Random _rand;
	protected long _lastGeneratedTick;
	protected long _interval;
	
	public MusicRhythmData(int interval) {
		_currentPos = 0;
		_rythms = new ArrayList<Integer>();
		_rand = new Random(SystemClock.elapsedRealtime());
		_lastGeneratedTick = 0;
		_interval = interval;
	}
	
	public void loadData(String jsonString) {
		loadData(new JSON(jsonString));
	}
	public void loadData(JSON json) {
		_rythms = new ArrayList<Integer>(json.size());
		for(int i = 0, max = json.size(); i < max; i++) {
			_rythms.add(json.getChildIntValue(i));
		}
	}
	
	public void reset() {
		_currentPos = 0;
	}
	
	public int probPopMonster(int currentTime, int totalTime) {
		int prob = 0;
		long tick = SystemClock.elapsedRealtime();
		if (tick - _lastGeneratedTick > _interval) {
			prob = (int)((double)(currentTime) / (double)(totalTime)) * 100 + _rand.nextInt(75);
			_lastGeneratedTick = tick;
		}
		return prob;
	}
	
	
}
