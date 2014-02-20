package fr.vodoji.engine.audio;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class Audio implements Playable {
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////
	
	protected MediaPlayer _player;
	protected float _volume;
	protected boolean _isPrepared;
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public Audio() {
		_volume = 1.0f;
		_isPrepared = false;
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	///////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean play() {
		if (_player != null) {
			try {
				if (!_isPrepared) {
					_player.prepare();
					_isPrepared = true;
				}
				_player.start();
				return true;
			} catch (Exception e) {
				;
			}
		}
		return false;
	}
	@Override
	public boolean play(boolean loopingMode) {
		if (_player != null) {
			try {
				if (!_isPrepared) {
					_player.prepare();
					_isPrepared = true;
				}
				_player.setLooping(loopingMode);
				_player.start();
				return true;
			} catch (Exception e) {
				;
			}
		}
		return false;
	}

	@Override
	public boolean pause() {
		if (_player != null) {
			try {
				_player.pause();
				return true;
			} catch (Exception e) {
				;
			}
		}
		return false;
	}

	@Override
	public boolean resume() {
		if (_player != null) {
			try {
				_player.start();
				return true;
			} catch (Exception e) {
				;
			}
		}
		return false;
	}

	@Override
	public boolean stop() {
		if (_player != null) {
			try {
				_player.stop();
				return true;
			} catch (Exception e) {
				;
			}
		}
		return false;
	}

	@Override
	public boolean isPlaying() {
		if (_player != null) {
			try {
				return _player.isPlaying();
			} catch (Exception e) {
				;
			}
		}
		return false;
	}

	@Override
	public boolean isPaused() {
		return (!isPlaying());
	}

	@Override
	public boolean isStopped() {
		return ((!isPlaying()) && (_player.getCurrentPosition() == 0));
	}

	@Override
	public float getVolume() {
		return _volume;
	}
	@Override
	public boolean setVolume(float volume) {
		if (_player != null) {
			try {
				if (volume < 0) volume = 0;
				if (volume > 1.0f) volume = 1.0f;
				_player.setVolume(volume, volume);
				_volume = volume;
				return true;
			} catch (Exception e) {
				;
			}
		}
		return false;
	}
	@Override
	public int getDuration() {
		if (_player != null) {
			try {
				return _player.getDuration();
			} catch (Exception e) {
				;
			}
		}
		return 0;
	}
	@Override
	public int getCurrentPosition() {
		if (_player != null) {
			try {
				return _player.getCurrentPosition();
			} catch (Exception e) {
				;
			}
		}
		return 0;
	}
	@Override
	public boolean setCurrentPosition(int position) {
		if (_player != null) {
			try {
				int len = _player.getDuration();
				if (position < 0) position = 0;
				if (position > len) position = len;
				_player.seekTo(position);
				return true;
			} catch (Exception e) {
				;
			}
		}
		return false;
	}
	@Override
	public boolean isLooping() {
		if (_player != null) {
			try {
				return _player.isLooping();
			} catch (Exception e) {
				;
			}
		}
		return false;
	}
	@Override
	public boolean setLooping(boolean loopingMode) {
		if (_player != null) {
			try {
				_player.setLooping(loopingMode);
				return true;
			} catch (Exception e) {
				;
			}
		}
		return false;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Load method
	///////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean loadFromContext(Context ctx, int id) {
		try {
			_isPrepared = false;
			_player = MediaPlayer.create(ctx, id);
		} catch (Exception e) {            
			return false;
		}
		try {
			if (_player != null) {
				_player.prepare();
				_isPrepared = true;
			}
		} catch (Exception e) {    
			;
		}
		return true;
	}
	
	
}
