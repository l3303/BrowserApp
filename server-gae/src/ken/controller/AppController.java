package ken.controller;

import java.util.HashMap;

import ken.page.IOSSoyPageGenerator;
import ken.page.SoyPageGenerator;
import ken.platform.AppConfig;
import ken.platform.AppConfig;
import ken.platform.PlatformType;

public class AppController {
	
//	 private SoyPageGenerator pageGenerator;
	private String appId;
	private String sid;
	private AppConfig appConfig;
	private HashMap<String, String> userParameters;
	
//	private static final String NORMAL_TEMPLATE_MAIN = "appMain_normal";
//	private static final String IOS_TEMPLATE_MAIN = "appMain_ios";

	public AppController(String appId, String sid, AppConfig appConfig, HashMap<String, String> userParameters) {
		this.appId =appId;
		this.sid = sid;
		this.appConfig = appConfig;
		this.userParameters = userParameters;
		
//		if (appConfig.getPlatform().equals(PlatformType.IPHONE.getPlatFormType())) {
//			
//			pageGenerator = new IOSSoyPageGenerator(IOS_TEMPLATE_MAIN);
//			System.out.print("place holder 1");
//			//to do
//		} else {
//			System.out.print("No matched paltform, define as normal browser!");
//			pageGenerator = new SoyPageGenerator(NORMAL_TEMPLATE_MAIN);
//		}
	}
	
	public String doGenerate() {
		SoyPageGenerator pageGenerator;
		System.out.println("platform : " + PlatformType.IPHONE);
		if (appConfig.getPlatform().equals(PlatformType.IPHONE.getPlatFormType())) {
			//temperary method
			pageGenerator = new IOSSoyPageGenerator(appId, sid, appConfig.isAjaxMode(), appConfig, userParameters);
			System.out.print("place holder 1");
		} else {
			System.out.println("No matched paltform, define as normal browser!");
			pageGenerator = new SoyPageGenerator(appId, sid, appConfig.isAjaxMode(), appConfig, userParameters);
		}
		return pageGenerator.generatePage();
	}
}
