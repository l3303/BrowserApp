package ken.server.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class HttpAdapter {
	
	private URL url;
	private static final int CONNECTION_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 8000;
	private static final String DEFAULT_CHARSET = "UTF-8";
	
	public HttpAdapter(String httpUrl, Map<String, String> userParameter) throws MalformedURLException {
		StringBuffer urlBuffer = new StringBuffer(httpUrl);
		if (userParameter != null && userParameter.size() > 0) {
			if (!httpUrl.endsWith("?")) {
				if (httpUrl.endsWith("/")) {
					urlBuffer.append("/");
				}
				urlBuffer.append("?");
			}
			Iterator<String> iter = userParameter.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				urlBuffer.append(key);
				urlBuffer.append("=");
				try {
					urlBuffer.append(URLEncoder.encode(userParameter.get(key), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				urlBuffer.append("&");
			}
		}
		System.out.println("start to fetch content with url: " + urlBuffer);
		url = new URL(urlBuffer.toString());
	}
	
	public final String read() throws Exception {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				final StringBuilder responseText = new StringBuilder();
				String inputLine = null;

				// determine charset and use default charset if no charset is set
				String charset = DEFAULT_CHARSET;
				System.out.println("connection : " + connection.getContentEncoding());
				if (connection.getContentEncoding() != null && !connection.getContentEncoding().isEmpty()) {
					charset = connection.getContentEncoding();
				}

				System.out.println("Read content with charset=" + charset);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), charset));

				while ((inputLine = in.readLine()) != null) {
					responseText.append(inputLine);
				}
				in.close();
				return responseText.toString();
			} else {
				throw new Exception("connection error!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
