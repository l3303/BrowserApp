package ken.server.content;

import java.net.MalformedURLException;
import java.util.Map;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

import ken.platform.AppConfig;
import ken.server.environment.RuntimeSystem;

public class ContentManager {
	
	private RuntimeSystem runtimeSystem;
	private String appId;
	private String sid;
	private JSONObject manifest;
	
	public ContentManager(RuntimeSystem runtimeSystem,String appId, String sid, JSONObject manifest) {
		this.runtimeSystem = runtimeSystem;
		this.appId = appId;
		this.sid = sid;
		this.manifest = manifest;
	}
	
	public String getContentMap(Map<String, String> userParameter) throws Exception {
		
		/* get source url from manifest using sid & environment*/
		String source = "http://localhost:8080/" + appId + "/";
		HttpAdapter adapter = new HttpAdapter(source, userParameter);
		return adapter.read();
	}
}
