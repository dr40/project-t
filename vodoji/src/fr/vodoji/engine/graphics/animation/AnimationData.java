package fr.vodoji.engine.graphics.animation;

import java.util.ArrayList;
import java.util.HashMap;
import fr.vodoji.engine.graphics.Image;
import fr.vodoji.utils.json.JSON;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class AnimationData {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////
	
	private ArrayList<Image> _images;
	private HashMap<String, AnimationFrame> _frames;
	private String _defaultFrame;
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public AnimationData() {
		_images = new ArrayList<Image>();
		_defaultFrame = "";
		_frames = new HashMap<String, AnimationFrame>();
	}

	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Image methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public void addImage(Image img) {
		_images.add(img);
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Load methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public boolean loadFromString(String str) {
		return loadFromJSON(new JSON(str));
	}
	public boolean loadFromJSON(JSON json) {
		/* Load default frame */
		_defaultFrame = json.getChildStringValue("main");
		/* Load frames */
		_frames.clear();
		JSON jsonFrame = json.get("frame");
		if (jsonFrame == null) return false;
		for(int i = 0, max = jsonFrame.size(); i < max; i++) {
			JSON jsonChild = jsonFrame.get(i);
			AnimationFrame frm = new AnimationFrame(jsonChild);
			_frames.put(jsonChild.getName(), frm);
		}
		/* Done: return true */
		return true;
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Getters
	///////////////////////////////////////////////////////////////////////////////////////////

	public String getDefaultFrameName() {
		return _defaultFrame;
	}
	

	public AnimationFrame getFrame(String name) {
		if (_frames.containsKey(name)) {
			return _frames.get(name);
		} else {
			return null;
		}
	}
	public Image getImage(int index) {
		if ((index >= 0) && (index < _images.size())) {
			return _images.get(index);
		} else {
			return null;
		}
	}
	
	
}
