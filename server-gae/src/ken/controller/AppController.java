package ken.controller;

import java.util.HashMap;

import ken.page.IOSSoyPageGenerator;
import ken.page.SoyPageGenerator;
import ken.page.factory.SoyPageGeneratorFactory;
import ken.platform.AppConfig;
import ken.platform.AppConfig;
import ken.platform.PlatformType;
import ken.server.environment.RuntimeSystem;

public class AppController {

	private String appId;
	private String sid;
	private AppConfig appConfig;
	private HashMap<String, String> userParameters;
	private RuntimeSystem runtimeSystem;
	
	public AppController(String appId, String sid, AppConfig appConfig, HashMap<String, String> userParameters, RuntimeSystem runtimeSystem) {
		this.appId =appId;
		this.sid = sid;
		this.appConfig = appConfig;
		this.userParameters = userParameters;
		this.runtimeSystem = runtimeSystem;
	}
	
	public String doGenerate() {
		SoyPageGenerator pageGenerator = SoyPageGeneratorFactory.getGenerator(appConfig, appId, sid, userParameters, runtimeSystem);
		return pageGenerator.generatePage();
	}
}
