package ken.server.content;

import java.net.MalformedURLException;
import java.util.Map;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

import ken.platform.AppConfig;

public class ContentManager {
	
	private AppConfig config;
	private String appId;
	private JSONObject manifest;
	
	public ContentManager(AppConfig config, String appId, String sid) {
		this.config = config;
		this.appId = appId;
		
		/* get manifest through appid*/
		manifest = null;
	}
	
	public String getContentMap(Map<String, String> userParameter) throws Exception {
		
		/* get source url from manifest using sid & environment*/
		String source = "http://localhost:8080/" + appId + "/";
		HttpAdapter adapter = new HttpAdapter(source, userParameter);
		return adapter.read();
	}
}
