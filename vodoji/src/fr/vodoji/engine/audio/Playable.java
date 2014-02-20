package fr.vodoji.engine.audio;

import android.content.Context;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public interface Playable {

	public boolean play();
	public boolean play(boolean loopingMode);
	public boolean pause();
	public boolean resume();
	public boolean stop();
	public boolean isPlaying();
	public boolean isPaused();
	public boolean isStopped();
	public float getVolume();
	public boolean setVolume(float volume);
	public int getDuration();
	public int getCurrentPosition();
	public boolean setCurrentPosition(int position);
	public boolean isLooping();
	public boolean setLooping(boolean loopingMode);
	
	public boolean loadFromContext(Context ctx, int id);
	
	
}
