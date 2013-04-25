package ken.page;

import java.util.HashMap;

import ken.platform.AppConfig;

import com.google.template.soy.data.SoyMapData;

public class IOSSoyPageGenerator extends SoyPageGenerator {

	public IOSSoyPageGenerator(String appId, String sid, boolean ajax, AppConfig config, HashMap<String, String> userParameter) {
		super(appId, sid, ajax, config, userParameter);
	}

	@Override
	protected String generateContent() {
		return "IOS PLATFORM, NOT START YET!!!";
	}
}