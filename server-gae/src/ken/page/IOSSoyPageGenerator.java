package ken.page;

import java.util.HashMap;

import ken.platform.AppConfig;
import ken.server.environment.RuntimeSystem;

import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.template.soy.data.SoyMapData;

public class IOSSoyPageGenerator extends SoyPageGenerator {

	public IOSSoyPageGenerator(String appId, String sid, AppConfig config, HashMap<String, String> userParameter, RuntimeSystem runtimeSystem, JSONObject manifest) {
		super(appId, sid, config, userParameter, runtimeSystem, manifest);
	}

	@Override
	protected String generateContent() {
		return "IOS PLATFORM, NOT START YET!!!";
	}
}