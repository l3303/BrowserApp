package ken.controller;

import java.util.HashMap;

import ken.page.IOSSoyPageGenerator;
import ken.page.SoyPageGenerator;
import ken.page.factory.SoyPageGeneratorFactory;
import ken.platform.AppConfig;
import ken.platform.AppConfig;
import ken.platform.PlatformType;

public class AppController {

	private String appId;
	private String sid;
	private AppConfig appConfig;
	private HashMap<String, String> userParameters;

	public AppController(String appId, String sid, AppConfig appConfig, HashMap<String, String> userParameters) {
		this.appId =appId;
		this.sid = sid;
		this.appConfig = appConfig;
		this.userParameters = userParameters;
		
	}
	
	public String doGenerate() {
		SoyPageGenerator pageGenerator = SoyPageGeneratorFactory.getGenerator(appConfig, appId, sid, userParameters);
		return pageGenerator.generatePage();
	}
}
