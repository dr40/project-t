package fr.vodoji.utils.json;


/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public enum JSONType {
	/**
	 *  Empty JSON Object (cf. json.org :: value empty)
	 */
	EMPTY,
	/**
	 *  String JSON Object: "..." (cf. json.org :: value string)
	 */
	STRING,
	/**
	 *  Number JSON Object (cf. json.org :: value number)
	 */
	NUMBER,
	/**
	 *  JSON Object: {...} (cf. json.org :: value object)
	 */
	OBJECT,
	/**
	 *  Array JSON Object: [...] (cf. json.org :: value array)
	 */
	ARRAY,
	/**
	 * Boolean JSON Object: true or false (cf. json.org :: value true, false)
	 */
	BOOLEAN
}
