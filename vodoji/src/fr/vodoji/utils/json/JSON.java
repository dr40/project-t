package fr.vodoji.utils.json;

import java.util.ArrayList;


/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class JSON {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Protected Members
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Private list of JSON childs.
	 * Concern only JSON Object and JSON Array, since other type cannot have named child (cf. json specification)
	 */
	protected ArrayList<JSON> _childs;
	/**
	 * Parent of this JSON object. Can be null.
	 */
	protected JSON _parent;
	/**
	 * Type of this JSON Object (cf. JSONType, json specification)
	 */
	protected JSONType _type;
	/**
	 * Name of this JSON object
	 */
	protected String _name;
	/**
	 * String value of this JSON object
	 */
	protected String _value;


	///////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Construct an empty JSON Object
	 */
	public JSON() {
		_childs = new ArrayList<JSON>();
		_parent = null;
		_type = JSONType.EMPTY;
		_name = "";
		_value = "";
	}

	public JSON(String jsonString) {
		_childs = new ArrayList<JSON>();
		_parent = null;
		_type = JSONType.EMPTY;
		_name = "";
		_value = "";
		JSONParser parser = new JSONParser();
		parser.doParse(this, jsonString);

	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Properties set (used principally by JSONParser)
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Set the parent of this JSON Object
	 * @param parent JSON object desired
	 */
	public void setParent(JSON parent) {
		_parent = parent;
	}
	/**
	 * Set the name of this JSON object
	 * @param name Name desired
	 */
	public void setName(String name) {
		_name = name;
	}
	/**
	 * Set the value of this JSON object
	 * @param value Value desired
	 */
	public void setValue(String value) {
		_value = value;
	}
	/**
	 * Set the type of this JSON object
	 * @param type Type desired
	 */
	public void setType(JSONType type) {
		_type = type;
	}


	///////////////////////////////////////////////////////////////////////////////////////////
	// Create Update Delete Childs methods (used principally by JSONParser)
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Remove all JSON child objects
	 */
	public void clear() {
		_childs.clear();
	}
	/**
	 * Add an JSON child object
	 * @param json JSON object desired
	 */
	public void addChild(JSON json) {
		_childs.add(json);
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Properties
	///////////////////////////////////////////////////////////////////////////////////////////

	public JSON getParent() {
		return _parent;
	}

	public String getName() {
		return _name;
	}

	public JSONType getType() {
		return _type;
	}

	public String getValue() {
		return _value;
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Childs
	///////////////////////////////////////////////////////////////////////////////////////////

	public JSON get(int index) {
		if ((index >= 0) && (index < _childs.size())) {
			return _childs.get(index);
		} else {
			return null;
		}
	}

	public JSON get(String name) {
		for(JSON o : _childs) {
			if (o.getName().compareTo(name) == 0) {
				return o;
			}
		}
		return null;
	}

	public JSON getByPath(String path) {
		return getByPath(path, ".");
	}

	public JSON getByPath(String path, String pathDelimiter) {
		if (path.length() == 0) {
			return this;
		} else {
			int i = path.indexOf(pathDelimiter);
			if (i == -1) {
				return get(path);
			} else {
				JSON child = get(path.substring(0, i));
				if (child != null) {
					return child.getByPath(path.substring(i+pathDelimiter.length()), pathDelimiter);
				} else {
					return null;
				}
			}
		}
	}

	public boolean contains(String name) {
		return (get(name) != null);
	}

	public int size() {
		return _childs.size();
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Value
	///////////////////////////////////////////////////////////////////////////////////////////

	public boolean getBoolValue() {
		return getBoolValue(false);
	}

	public boolean getBoolValue(boolean defaultValue) {
		if (_type != JSONType.EMPTY) {
			return ((_value.compareToIgnoreCase("true") == 0) || (_value.compareToIgnoreCase("1") == 0));
		} else {
			return defaultValue;
		}
	}


	public int getIntValue() {
		return getIntValue(0);
	}

	public int getIntValue(int defaultValue) {
		if (_type != JSONType.EMPTY) {
			return stringToInt(_value);
		} else {
			return defaultValue;
		}
	}

	public double getNumberValue() {
		return getNumberValue(0.0);
	}

	public double getNumberValue(double defaultValue) {
		if (_type != JSONType.EMPTY) {
			return stringToDouble(_value);
		} else {
			return defaultValue;
		}
	}

	public String getStringValue() {
		return getStringValue("");
	}

	public String getStringValue(String defaultValue) {
		if (_type != JSONType.EMPTY) {
			return _value;
		} else {
			return defaultValue;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Value - helper methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public boolean getChildBoolValue(String childName) {
		return getChildBoolValue(childName, false);
	}
	public boolean getChildBoolValue(int childIndex) {
		return getChildBoolValue(childIndex, false);
	}
	public boolean getChildBoolValue(String childName, boolean defaultValue) {
		JSON c = get(childName);
		return ((c != null) ? c.getBoolValue(defaultValue) : defaultValue);
	}
	public boolean getChildBoolValue(int childIndex, boolean defaultValue) {
		JSON c = get(childIndex);
		return ((c != null) ? c.getBoolValue(defaultValue) : defaultValue);
	}

	public int getChildIntValue(String childName) {
		return getChildIntValue(childName, 0);
	}
	public int getChildIntValue(int childIndex) {
		return getChildIntValue(childIndex, 0);
	}
	public int getChildIntValue(String childName, int defaultValue) {
		JSON c = get(childName);
		return ((c != null) ? c.getIntValue(defaultValue) : defaultValue);
	}
	public int getChildIntValue(int childIndex, int defaultValue) {
		JSON c = get(childIndex);
		return ((c != null) ? c.getIntValue(defaultValue) : defaultValue);
	}

	public double getChildNumberValue(String childName) {
		return getChildNumberValue(childName, 0);
	}
	public double getChildNumberValue(int childIndex) {
		return getChildNumberValue(childIndex, 0);
	}
	public double getChildNumberValue(String childName, double defaultValue) {
		JSON c = get(childName);
		return ((c != null) ? c.getNumberValue(defaultValue) : defaultValue);
	}
	public double getChildNumberValue(int childIndex, double defaultValue) {
		JSON c = get(childIndex);
		return ((c != null) ? c.getNumberValue(defaultValue) : defaultValue);
	}

	public String getChildStringValue(String childName) {
		return getChildStringValue(childName, "");
	}
	public String getChildStringValue(int childIndex) {
		return getChildStringValue(childIndex, "");
	}
	public String getChildStringValue(String childName, String defaultValue) {
		JSON c = get(childName);
		return ((c != null) ? c.getStringValue(defaultValue) : defaultValue);
	}
	public String getChildStringValue(int childIndex, String defaultValue) {
		JSON c = get(childIndex);
		return ((c != null) ? c.getStringValue(defaultValue) : defaultValue);
	}


	///////////////////////////////////////////////////////////////////////////////////////////
	// ToString
	///////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString() {
		return toString("");
	}
	public String toString(String tabIndent) {
		String tmp = "";
        /* Add name */
		if ((_parent != null) && (_parent.getType() != JSONType.ARRAY)) {
			tmp += "\"" + _name + "\"";
			tmp += ": ";
		}
        /* Add value */
		if (_type == JSONType.STRING) {
			tmp += "\"" + _value + "\"";
		} else if ((_type == JSONType.NUMBER) || (_type == JSONType.BOOLEAN)) {
			tmp += _value;
		}
        /* Begin Child */
		if (_childs.size() > 0) {
			String childTabIndent = tabIndent + "\t";
			if (_type == JSONType.OBJECT) {
				tmp += "{\n" + childTabIndent;
			} else if (_type == JSONType.ARRAY) {
				tmp += "[\n" + childTabIndent;
			}

            /* Add Child */
			int childId = 0;
			for (JSON c : _childs) {
				if (childId > 0) {
					tmp += ",\n" + childTabIndent;
				}
				tmp += c.toString(childTabIndent);
				childId++;
			}
            /* End Child */
			if (_type == JSONType.OBJECT) {
				tmp += "\n" + tabIndent + "}";
			} else if (_type == JSONType.ARRAY) {
				tmp += "\n" + tabIndent + "]";
			}
		}
        /* Return result */
		return tmp;
	}


	///////////////////////////////////////////////////////////////////////////////////////////
	// Tools - functions
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Convert a string into a double (take in account '.' and ',')
	 * @param str String desired
	 * @return Double number of the string
	 */
	protected double stringToDouble(String str) {
		double d1, d2;
		try {
			d1 = Double.parseDouble(str.replace(',', '.'));
		} catch (Exception e) {
			d1 = 0;
		}
		try {
			d2 = Double.parseDouble(str.replace('.', ','));
		} catch (Exception e) {
			d2 = 0;
		}
		if (d1 == d2) {
			return d1;
		} else if (d1 != 0) {
			return d1;
		} else {
			return d2;
		}
	}
	/**
	 * Convert a string into an integer
	 * @param str String desired
	 * @return Integer number of the string
	 */
	protected int stringToInt(String str) {
		return (int)(stringToDouble(str));
	}

}
