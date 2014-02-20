package fr.vodoji.utils.http;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public interface HTTPGetListener {

	public void onGetDone(String url, String result);
	public void onGetError(String url);

}
