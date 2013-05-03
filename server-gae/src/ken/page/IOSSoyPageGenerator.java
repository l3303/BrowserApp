package ken.page;

import java.util.HashMap;

import ken.platform.AppConfig;
import ken.server.environment.RuntimeSystem;

import com.google.template.soy.data.SoyMapData;

public class IOSSoyPageGenerator extends SoyPageGenerator {

	public IOSSoyPageGenerator(String appId, String sid, AppConfig config, HashMap<String, String> userParameter, RuntimeSystem runtimeSystem) {
		super(appId, sid, config, userParameter, runtimeSystem);
	}

	@Override
	protected String generateContent() {
		return "IOS PLATFORM, NOT START YET!!!";
	}
}