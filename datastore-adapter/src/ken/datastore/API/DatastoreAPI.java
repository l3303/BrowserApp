package ken.datastore.API;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import ken.datastore.PersistenceAdapter;
import ken.datastore.manifest.ManifestJsonFormator;
import ken.datastore.manifest.ManifestValidator;
import ken.datastore.model.AppConfigurationBE;

public class DatastoreAPI {
	
	public static AppConfigurationBE importManifest(String manifest, boolean overwrite) throws Exception {
		System.out.println("[pManifest=" + manifest + "][pOverwrite=" + overwrite + "]");

		// parse app configuration
		JSONObject manifestJson = ManifestJsonFormator.formatToJson(manifest);
		
		final String appId = manifestJson.getJSONObject("header").getString("id");

		PersistenceAdapter pa = new PersistenceAdapter();
		AppConfigurationBE theFile = pa.getAppConfigurationBE(appId);
		if (theFile != null) {
			if (!overwrite) {
				return theFile;
			}
		} else {
			theFile = pa.createAppConfigurationBE();
		}
		theFile.setAppId(appId);
		// save the app configuration
		theFile.setManifest(manifest.toString());
		pa.makePersistent(theFile);

		return theFile;
	}
}
