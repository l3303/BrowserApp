package ken.page.factory;

import java.util.HashMap;

import ken.page.IOSSoyPageGenerator;
import ken.page.SoyPageGenerator;
import ken.platform.AppConfig;
import ken.platform.PlatformType;
import ken.server.environment.RuntimeSystem;

public class SoyPageGeneratorFactory {
	public static SoyPageGenerator getGenerator(AppConfig config, String appId, String sid, HashMap<String, String> userParameters, RuntimeSystem runtimeSystem) {
		String platform = config.getPlatform();
		if (platform.equals(PlatformType.IPHONE) || platform.equals(PlatformType.IPAD)) {
			return new IOSSoyPageGenerator(appId, sid, config, userParameters, runtimeSystem);
		} else {
			return new SoyPageGenerator(appId, sid, config, userParameters, runtimeSystem);
		}
	}
}
