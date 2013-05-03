package ken.page;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import ken.platform.AppConfig;
import ken.server.content.ContentManager;
import ken.server.environment.RuntimeSystem;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.SoyFileSet.Builder;
import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import com.google.template.soy.tofu.SoyTofu.Renderer;

public class SoyPageGenerator {
	
	private static final String RESOURCE_PATH = "src/lib/";
	private static final String SOY_NAMESPACE = "ken.cloudsdk.";
	
	private String appId;
	private boolean ajax;
	private String screenId;
	private String templateName;
	private SoyTofu tofu;
	private AppConfig config;
	private HashMap<String, String> userParameter;
	private RuntimeSystem runtimeSystem;
	
	public SoyPageGenerator(String appId, String sid, AppConfig config, HashMap<String, String> userParameter, RuntimeSystem runtimeSystem) {
		this.appId = appId;
		this.screenId = sid;
		this.ajax = config.isAjaxMode();
		this.config = config;
		this.userParameter = userParameter;
		this.runtimeSystem = runtimeSystem;
		
		Builder builder = new SoyFileSet.Builder();
		builder.add(MainpageFactory.getMainpageFile(this.getClass()));
		
		/*
		 * TODO:
		 * get templateName according appId & screenId
		 * */
		
		if (ajax) {
			
		} else {
			templateName = "appMain_normal";
		}
		
		tofu = builder.build().compileToTofu();
	}
	
	public String generatePage() {
		Renderer renderer = tofu.newRenderer(SOY_NAMESPACE + templateName);
		
		SoyMapData mainPageData = new SoyMapData();
		
		if (ajax) {
			String content = generateContent();
			mainPageData.put("content", content);
			renderer.setData(mainPageData);
			String page = renderer.render();
			System.out.println("recognized ajax mode!!");
			String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><response status=\"200\"><![CDATA[";
			String footer = "]]></response>";
			return header+page+footer;
		} else {
			mainPageData.put("jsFiles", generateJavaScript());
			mainPageData.put("cssFiles", generateCss());
			renderer.setData(mainPageData);
			return renderer.render();
		}
	}
	
	protected String generateContent() {
//		return new SoyMapData("content", "normal soy template content!!!!by ken!!!");
		ContentManager manager = new ContentManager(config, appId, screenId);
		try {
			return manager.getContentMap(userParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	protected SoyListData generateJavaScript() {
		SoyListData jsFiles = new SoyListData();
		jsFiles.add(getResourcePath() + "javascript/jquery.min.js",
					getResourcePath() + "javascript/nbt.js",
					getResourcePath() + "javascript/logger.js",
					getResourcePath() + "javascript/nessieNbtUtils_v1.js");
		return jsFiles;
	}
	
	protected SoyListData generateCss() {
		SoyListData cssFiles = new SoyListData();
		cssFiles.add(getResourcePath() + "css/nbt.css",
					getResourcePath() + "css/simulator.css",
					getResourcePath() + "css/default_bon.css",
					getResourcePath() + "css/nbt_1024.css");
		return cssFiles;
	}
	
	private String getBaseUrl() {
		return runtimeSystem.getProtocol() + runtimeSystem.getBaseUrl();
	}
	
	private String getResourcePath() {
		return getBaseUrl() + "/" + RESOURCE_PATH;
	}
}
