package fr.vodoji.utils.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class HTTPGet {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Members
	///////////////////////////////////////////////////////////////////////////////////////////



	///////////////////////////////////////////////////////////////////////////////////////////
	// Public constructors
	///////////////////////////////////////////////////////////////////////////////////////////

	public String getSync(StringURL url) {
		String result;
		try	{
				/* Send HTTP Get Request */
			URL u = new URL(url.getURLString());
			URLConnection c = u.openConnection();
				/* Get response */
			BufferedReader buf = new BufferedReader(new InputStreamReader(c.getInputStream()));
			StringBuilder s = new StringBuilder();
			String l;
			while ((l = buf.readLine()) != null) {
				s.append(l);
			}
			buf.close();
			result = s.toString();
		} catch (Exception e) {
			result = "";
		}
		return result;
	}

	public void getAsync(StringURL url, HTTPGetListener listener) {
		class GetTask implements Runnable {
			StringURL _url;
			HTTPGetListener _listener;
			public GetTask(StringURL url, HTTPGetListener listener) {
				_url = url;
				_listener = listener;
			}
			@Override
			public void run() {
				String result = getSync(_url);
				if (result.length() > 0) {
					/* Error */
					_listener.onGetDone(_url.toString(), result);
				} else {
					/* Ok */
					_listener.onGetDone(_url.toString(), result);

				}
			}
		}
		new Thread(new GetTask(url, listener)).start();
	}


	
}
