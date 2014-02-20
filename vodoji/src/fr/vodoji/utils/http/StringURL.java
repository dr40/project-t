package fr.vodoji.utils.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class StringURL {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////

	protected HashMap<String, String> _parameters;
	protected String _url;
	

	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public StringURL() {
		_parameters = new HashMap<String, String>();
		_url = "";
	}
	public StringURL(String baseURL) {
		_parameters = new HashMap<String, String>();
		_url = baseURL;
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////
	// Parameters management
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public void setParameter(String name, String value) {
		_parameters.put(name, value);
	}
	public boolean removeParameter(String name) {
		if (_parameters.containsKey(name)) {
			_parameters.remove(name);
			return true;
		} else {
			return false;
		}
	}
	public void clearParameter() {
		_parameters.clear();
	}
	public boolean haveParameter(String name) {
		return (_parameters.containsKey(name));
	}
	public int getParameterCount() {
		return _parameters.size();
	}
	public String getParameter(String name) {
		if (_parameters.containsKey(name)) {
			return _parameters.get(name);
		} else {
			return null;
		}
	}
	public String getParameter(int index) {
		if ((index >= 0) && (index < _parameters.size())) {
			for(Map.Entry<String, String> entry : _parameters.entrySet()) {
				if (index == 0) {
					return entry.getValue();
				}
				index--;
			}
		}
		return null;
	}
	public String getParameterName(int index) {
		if ((index >= 0) && (index < _parameters.size())) {
			for(Map.Entry<String, String> entry : _parameters.entrySet()) {
				if (index == 0) {
					return entry.getKey();
				}
				index--;
			}
		}
		return null;
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Main Methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public String getURLString() {
		String m = _url;
		if (m == null) m = "";
		int index = 0;
		for(Map.Entry<String, String> entry : _parameters.entrySet()) {
			/* Initialize */
			if (index == 0) {
				m = m + "?";
			} else {
				m = m + "&";
			}
			index++;
			/* Put key */
			String v = encode(entry.getKey());
			if (v != null) {
				m = m + v + "=";
				/* Put value */
				v = encode(entry.getValue());
				if (v != null) {
					m = m + v;
				}
			}
			
		}
		return m;
	}
	
	@Override
	public String toString() {
		return getURLString();
	}


	///////////////////////////////////////////////////////////////////////////////////////////
	// Tools Methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public static String encode(String url) {
		try {
			return URLEncoder.encode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
}
