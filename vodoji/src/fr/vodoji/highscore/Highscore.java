package fr.vodoji.highscore;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;

import fr.vodoji.Constants;
import fr.vodoji.utils.http.HTTPGet;
import fr.vodoji.utils.http.StringURL;
import fr.vodoji.utils.json.JSON;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class Highscore {


	///////////////////////////////////////////////////////////////////////////////////////////
	// Public enums
	///////////////////////////////////////////////////////////////////////////////////////////

	public enum State {
		NOT_A_RECORD,
		RECORD_BEAT,
		NEW_RECORD
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected ArrayList<HighscoreItem> _items;
	protected HashSet<HighscoreListener> _listeners;
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Public constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public Highscore() {
		_items = new ArrayList<HighscoreItem>();
		_listeners = new HashSet<HighscoreListener>();
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Get methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public int getCount() {
		return _items.size();
	}
	public HighscoreItem getItem(int index) {
		if ((index >= 0) && (index <= _items.size())) {
			return _items.get(index);
		} else {
			return null;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Listener methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public boolean removeListener(HighscoreListener listener) {
		if (_listeners.contains(listener)) {
			_listeners.remove(listener);
			return true;
		} else {
			return false;
		}
	}

	public boolean addListener(HighscoreListener listener) {
		if (!_listeners.contains(listener)) {
			_listeners.add(listener);
			return true;
		} else {
			return false;
		}
	}


	///////////////////////////////////////////////////////////////////////////////////////////
	// Save methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public void addScore(String username, int level, int score) {
		addScore(username, level, score, null);
	}
	public void addScore(String username, int level, int score, HighscoreListener listener) {
		/* Inner thread class */
		class AddScoreTask implements Runnable {
			Highscore _parent;
			HighscoreItem _item;
			HighscoreListener _listener;
			public AddScoreTask(Highscore parent, HighscoreItem item, HighscoreListener listener) {
				_parent = parent;
				_item = item;
				_listener = listener;
			}
			@Override
			public void run() {
				/* Compose URL */
				StringURL url = new StringURL(Constants.SCORE_URL);
				url.setParameter("secure_code", Constants.SECURE_CODE);
				url.setParameter("action", "addScore");
				url.setParameter("username", _item.getUserName());
				url.setParameter("score", String.valueOf(_item.getScore()));
				url.setParameter("level", String.valueOf(_item.getLevel()));
				/* Get URL data */
				HTTPGet http = new HTTPGet();
				String result = http.getSync(url);
				/* Treat response */
				if (result.length() == 0) {
					/* Error */
					if (_listener != null) {
						_listener.onScoreSaveError(_parent, _item);
					}
					for(HighscoreListener h : _listeners) {
						h.onScoreSaveError(_parent, _item);
					}
				} else {
					/* OK */
					JSON json = new JSON(result);
					String msg = json.getChildStringValue("message");
					Highscore.State state = Highscore.State.NOT_A_RECORD;	
					if (msg.compareToIgnoreCase("NEW") == 0) {
						state = Highscore.State.NEW_RECORD;
					} else if (msg.compareToIgnoreCase("BEAT") == 0) {
						state = Highscore.State.RECORD_BEAT;
					} else {
						state = Highscore.State.NOT_A_RECORD;
					}
					if (_listener != null) {
						_listener.onScoreSaved(_parent, _item, state);
					}
					for(HighscoreListener h : _listeners) {
						h.onScoreSaved(_parent, _item, state);
					}
				}
			}
		}
		/* Create score */
		HighscoreItem item = new HighscoreItem(username, level, score);
		/* Save in local */
		_items.add(item);		
//		/* Compose URL */
//		StringURL url = new StringURL(Constants.SCORE_URL);
//		url.setParameter("secure_code", Constants.SECURE_CODE);
//		url.setParameter("action", "addScore");
//		url.setParameter("username", item.getUserName());
//		url.setParameter("score", String.valueOf(item.getScore()));
//		url.setParameter("level", String.valueOf(item.getLevel()));
//		/* Get URL data */
//		HTTPGet http = new HTTPGet();
//		String result = http.getSync(url);
//		/* Treat response */
//		if (result.length() == 0) {
//			/* Error */
//			if (listener != null) {
//				listener.onScoreSaveError(this, item);
//			}
//			for(HighscoreListener h : _listeners) {
//				h.onScoreSaveError(this, item);
//			}
//		} else {
//			/* OK */
//			JSON json = new JSON(result);
//			String msg = json.getChildStringValue("message");
//			Highscore.State state = Highscore.State.NOT_A_RECORD;	
//			if (msg.compareToIgnoreCase("NEW") == 0) {
//				state = Highscore.State.NEW_RECORD;
//			} else if (msg.compareToIgnoreCase("BEAT") == 0) {
//				state = Highscore.State.RECORD_BEAT;
//			} else {
//				state = Highscore.State.NOT_A_RECORD;
//			}
//			if (listener != null) {
//				listener.onScoreSaved(this, item, state);
//			}
//			for(HighscoreListener h : _listeners) {
//				h.onScoreSaved(this, item, state);
//			}
//		}
		new Thread(new AddScoreTask(this, item, listener)).start();

	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Refresh methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public void refresh() {
		refresh(null);
	}
	public void refresh(HighscoreListener listener) {
		/* Refresh - Inner thread class */
		/*class RefreshScoreTask implements Runnable {
			Highscore _parent;
			HighscoreListener _listener;*/
		// TODO
		/* Compose URL */
		StringURL url = new StringURL(Constants.SCORE_URL);
		url.setParameter("secure_code", Constants.SECURE_CODE);
		url.setParameter("action", "listScore");
		/* Get URL data */
		HTTPGet http = new HTTPGet();
		String result = http.getSync(url);
		/* Treat response */
		if (result.length() == 0) {
			/* Load highscore */
			JSON json = new JSON(result);
			_items.clear();
			for(int i = 0, max = json.size(); i < max; i++) {
				JSON jsonItem = json.get(i);
				HighscoreItem item = new HighscoreItem(jsonItem.getChildStringValue("username"), 
						jsonItem.getChildIntValue("level"), 
						jsonItem.getChildIntValue("score"), 
						(long)(jsonItem.getChildNumberValue("score_timestamp")));
				_items.add(item);
			}
		}
		/* Prevent listeners */
		if (listener != null) {
			listener.onScoreRefreshed(this);
		}
		for(HighscoreListener h : _listeners) {
			h.onScoreRefreshed(this);
		}
	}

	public String[] getScoreList() {
		String[] lstStr = new String[_items.size()];
		for(int i = 0, max = lstStr.length; i < max; i++) {
			lstStr[i] = _items.get(i).getUserName() + " | " + _items.get(i).getLevel() + " | " + _items.get(i).getScore();
		}
		return lstStr;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Local methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public void loadLocalScoreList(Context ctx) {
        String jsonScore = "";
        try {
            InputStream inputStream = ctx.openFileInput("score.json");
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                 
                inputStream.close();
                jsonScore = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
        	;
        } catch (IOException e) {
        	;
        }
        /* Load Items */
        JSON mainJSON = new JSON("{\"score\":" + jsonScore + "}");
        JSON json = mainJSON.get("score");
		_items.clear();
		for(int i = 0, max = json.size(); i < max; i++) {
			JSON jsonItem = json.get(i);
			HighscoreItem item = new HighscoreItem(jsonItem.getChildStringValue("username"), 
					jsonItem.getChildIntValue("level"), 
					jsonItem.getChildIntValue("score"), 
					(long)(jsonItem.getChildNumberValue("score_timestamp")));
			_items.add(item);
		}
	}
	
	public void saveLocalHighscore(Context ctx) {
		/* Generate JSON */
		String jsonCode = "[";
		int index = 0;
		for(HighscoreItem item : _items) {
			if (index > 0) {
				jsonCode = jsonCode + ",";
			}
			index++;
			jsonCode = jsonCode + "{";
			jsonCode = jsonCode + "\"username\":\"" + item.getUserName().replace("\"", "\\\"") + "\",";
			jsonCode = jsonCode + "\"level\":" + item.getLevel() + ",";
			jsonCode = jsonCode + "\"score\":" + item.getScore() + ",";
			jsonCode = jsonCode + "\"score_timestamp\":" + 0 + "}";
		}
		jsonCode = jsonCode + "]";
		/* Save JSON */
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ctx.openFileOutput("score.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(jsonCode);
            outputStreamWriter.close();
        }
        catch (IOException e) {
        	;
        } 
	}
	
	
}
