package ken.page.factory;

import java.util.HashMap;

import ken.page.IOSSoyPageGenerator;
import ken.page.SoyPageGenerator;
import ken.platform.AppConfig;
import ken.platform.PlatformType;

public class SoyPageGeneratorFactory {
	public static SoyPageGenerator getGenerator(AppConfig config, String appId, String sid, HashMap<String, String> userParameters) {
		String platform = config.getPlatform();
		if (platform.equals(PlatformType.IPHONE) || platform.equals(PlatformType.IPAD)) {
			return new IOSSoyPageGenerator(appId, sid, config, userParameters);
		} else {
			return new SoyPageGenerator(appId, sid, config, userParameters);
		}
	}
}
