package ken.datastore.manifest;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class ManifestAdapter {
	
	private JSONObject manifest;
	
	public ManifestAdapter(JSONObject manifest) {
		this.manifest = manifest;
	}
	
	public String getSourceUrl(String sid, boolean isLocal) throws Exception {
		String environment = ManifestConstants.KEY_INTEGRATION;
		if (!isLocal) {
			environment = ManifestConstants.KEY_PRODUCTION;
		}
		JSONObject content = getScreen().getJSONObject(sid);
		if (content != null) {
			StringBuffer url = new StringBuffer();
			url.append(content.getJSONObject(environment).getString(ManifestConstants.KEY_SOURCE));
			if (!url.toString().endsWith("/")) {
				url.append("/");
			}
			url.append("?action=");
			url.append(content.getJSONObject(environment).getJSONObject(ManifestConstants.KEY_PARAMETER).getString(ManifestConstants.KEY_ACTION));
			return url.toString();
		} else {
			throw new Exception("Cannot find " + sid + "in manifest!!!");
		}
	}
	
	public String getTemplate(String sid) throws Exception {
		JSONObject screen = getScreen().getJSONObject(sid);
		if (screen != null) {
			return screen.getString(ManifestConstants.KEY_TEMPLATE);
		} else {
			throw new Exception("Cannot find " + sid + "in manifest!!!");
		}
	}
	
	public String getMainPage() throws JSONException {
		return getHeader().getString(ManifestConstants.KEY_MAIN_PAGE);
	}
	
	public JSONObject getHeader() throws JSONException {
		return manifest.getJSONObject(ManifestConstants.KEY_HEDAER);
	}
	
	public JSONObject getScreen() throws JSONException {
		return manifest.getJSONObject(ManifestConstants.KEY_SCREENS);
	}
}
