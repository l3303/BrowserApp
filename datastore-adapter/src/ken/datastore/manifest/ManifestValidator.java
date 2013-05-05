package ken.datastore.manifest;

import java.util.Iterator;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class ManifestValidator {
	
	public static void validate(JSONObject manifest) throws Exception {
		JSONObject header = manifest.getJSONObject(ManifestConstants.KEY_HEDAER);
		JSONObject screens = manifest.getJSONObject(ManifestConstants.KEY_SCREENS);
		validateHeader(header);
		Iterator<String> iter = screens.keys();
		while (iter.hasNext()) {
			String id = (String) iter.next();
			if (id.isEmpty()) {
				throw new Exception("Screen id cannot be empty");
			}
			JSONObject screen = screens.getJSONObject(id);
			validateScreen(screen, id);
		}
	}
	
	private static void validateHeader(JSONObject header) throws Exception {
		Iterator<String> iter = header.keys();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (!key.equals(ManifestConstants.KEY_APP_ID) && !key.equals(ManifestConstants.KEY_MAIN_PAGE) && !key.equals(ManifestConstants.KEY_ICON)) {
				throw new Exception("Element [" + key + "] is not allowed in header!");
			}
			if (header.getString(key).isEmpty()) {
				throw new Exception("Element + [" + key + "] cannot be empty");
			}
		}
		System.out.println("validate header : " + header);
	}
	
	private static void validateScreen(JSONObject screen, String id) throws Exception {
		Iterator<String> iter = screen.keys();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (!key.equals(ManifestConstants.KEY_TEMPLATE) && !key.equals(ManifestConstants.KEY_PRODUCTION) && !key.equals(ManifestConstants.KEY_INTEGRATION)) {
				throw new Exception("Element [" + key + "] is not allowed in " + id + "!");
			}
			if (key.equals(ManifestConstants.KEY_TEMPLATE)) {
				if (screen.getString(key).isEmpty()) {
					throw new Exception("Element + [" + key + "] cannot be empty");
				}
			} else if (key.equals(ManifestConstants.KEY_PRODUCTION) || key.equals(ManifestConstants.KEY_INTEGRATION)) {
				validateLink(screen.getJSONObject(key));
			}
		}
		System.out.println("validate screen : " + screen);
	}
	
	private static void validateLink(JSONObject link) throws Exception {
		Iterator<String> iter = link.keys();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (!key.equals(ManifestConstants.KEY_SOURCE) && !key.equals(ManifestConstants.KEY_PARAMETER)) {
				throw new Exception("Element [" + key + "] is not allowed!");
			}
			if (key.equals(ManifestConstants.KEY_SOURCE)) {
				if (link.getString(key).isEmpty()) {
					throw new Exception("Element + [" + key + "] cannot be empty");
				}
			} else if (key.equals(ManifestConstants.KEY_PARAMETER)) {
				
			}
		}
	}
}
