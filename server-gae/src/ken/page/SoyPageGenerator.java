package ken.page;

import java.util.HashMap;
import java.util.List;

import ken.datastore.PersistenceAdapter;
import ken.datastore.manifest.ManifestAdapter;
import ken.datastore.model.JavaScriptBE;
import ken.platform.AppConfig;
import ken.server.content.ContentManager;
import ken.server.environment.RuntimeSystem;
import ken.servlet.ImageServlet;
import ken.util.JsonMapConverter;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.SoyFileSet.Builder;
import com.google.template.soy.data.SanitizedContent;
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
	private JSONObject manifest;
	
	public SoyPageGenerator(String appId, String sid, AppConfig config, HashMap<String, String> userParameter, RuntimeSystem runtimeSystem, JSONObject manifest) {
		this.appId = appId;
		this.screenId = sid;
		this.ajax = config.isAjaxMode();
		this.config = config;
		this.userParameter = userParameter;
		this.runtimeSystem = runtimeSystem;
		this.manifest = manifest;
		
		Builder builder = new SoyFileSet.Builder();
		
		if (ajax) {
			ManifestAdapter adapter = new ManifestAdapter(manifest);
			try {
				templateName = adapter.getTemplate(sid);
				System.out.println("start to initialize file: " + templateName);
				builder.add(PageFactory.getTemplateFile(templateName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			builder.add(PageFactory.getMainpageFile(this.getClass()));
			templateName = "appMain_normal";
		}
		
		tofu = builder.build().compileToTofu();
	}
	
	public String generatePage() {
		Renderer renderer = tofu.newRenderer(SOY_NAMESPACE + templateName);
		
		SoyMapData mainPageData = new SoyMapData();
		
		String AbsoluteBaseUrl = runtimeSystem.getProtocol() + runtimeSystem.getBaseUrl();
		SoyMapData serverInfo = new SoyMapData(
				"baseUrl", AbsoluteBaseUrl,
				"imageServlet", AbsoluteBaseUrl + ImageServlet.SERVLET_PATH + "/",
				"contextPath", runtimeSystem.getContextPath(),
				"appServlet", runtimeSystem.getServletPath());
		
		SoyMapData platformInfo = new SoyMapData(
				"platform", config.getPlatform(),
				"environment", config.getEnvironment());
		
		SoyMapData mHeader = null;
		try {
			mHeader = json2SoyMapData(new ManifestAdapter(manifest).getHeader());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		SoyMapData ijData = new SoyMapData(
				"serverInfo", serverInfo,
				"platformInfo", platformInfo,
				"header", mHeader);
		
		if (ajax) {
			String content = generateContent();
			mainPageData.put("content", content);
			renderer.setData(mainPageData);
			renderer.setIjData(ijData);
			String page = renderer.render();
			System.out.println("recognized ajax mode!!");
			String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><response status=\"200\"><![CDATA[";
			String footer = "]]></response>";
			return header+page+footer;
		} else {
			SoyMapData jsData = getJsData();
			System.out.println("js data : " + jsData);
			mainPageData.put("jsData",jsData);
			mainPageData.put("jsFiles", generateJavaScript());
			mainPageData.put("cssFiles", generateCss());
			renderer.setData(mainPageData);
			renderer.setIjData(ijData);
			String renderdd = renderer.render();
			System.out.println("final render : " + renderdd);
			return renderdd;
		}
	}
	
	private SoyMapData getJsData() {
		SoyMapData jsData = new SoyMapData();
		PersistenceAdapter pa = new PersistenceAdapter();
		List<? extends JavaScriptBE> jsFiles = pa.getJavaScriptBEsForApp(appId);
		for (JavaScriptBE jsFile : jsFiles) {
			jsData.put(jsFile.getName(), jsFile.getJsAsString());
		}
		return jsData;
	}
	
	protected String generateContent() {
		ContentManager manager = new ContentManager(runtimeSystem, appId, screenId, manifest);
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
	
	private SoyMapData json2SoyMapData(JSONObject obj) {
		if (obj != null) {
			try {
				return new SoyMapData(new JsonMapConverter(obj).toJSON());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new SoyMapData();
	}
}
