package ken.controller;

import java.util.HashMap;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

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
	private JSONObject manifest;
	
	public AppController(String appId, String sid, AppConfig appConfig, HashMap<String, String> userParameters, RuntimeSystem runtimeSystem, JSONObject manifest) {
		this.appId =appId;
		this.sid = sid;
		this.appConfig = appConfig;
		this.userParameters = userParameters;
		this.runtimeSystem = runtimeSystem;
		this.manifest = manifest;
	}
	
	public String doGenerate() throws JSONException {
		SoyPageGenerator pageGenerator = SoyPageGeneratorFactory.getGenerator(appConfig, appId, sid, userParameters, runtimeSystem, manifest);
		return pageGenerator.generatePage();
	}
}
