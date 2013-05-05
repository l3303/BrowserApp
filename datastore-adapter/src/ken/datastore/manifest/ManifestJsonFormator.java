package ken.datastore.manifest;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class ManifestJsonFormator {
	
	public static JSONObject formatToJson(String strManifest) throws Exception {
		JSONObject manifest = new JSONObject(strManifest);
		ManifestValidator.validate(manifest);
		return manifest;
	}
}
