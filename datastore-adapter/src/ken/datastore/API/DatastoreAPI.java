package ken.datastore.API;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import ken.datastore.PersistenceAdapter;
import ken.datastore.manifest.ManifestJsonFormator;
import ken.datastore.manifest.ManifestValidator;
import ken.datastore.model.AppConfigurationBE;
import ken.datastore.model.ImageBE;
import ken.datastore.model.JavaScriptBE;

public class DatastoreAPI {
	
	static final Logger LOG = Logger.getLogger(DatastoreAPI.class.getName());
	
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
	
	public static JavaScriptBE importJavaScript(String jsName,
			String appId,
			String jsString,
			boolean overwrite) {
		LOG.info("[jsName=" + jsName
				+ "][appId=" + appId
				+ "][overwrite=" + overwrite
				+ "]");
		
		jsName = StringUtils.trimToEmpty(jsName);
		if (jsName.length() == 0) {
			throw new IllegalArgumentException("The js name wasn't valid. [jsName=" + jsName + "]");
		}
		appId = StringUtils.trimToEmpty(appId);
		if (appId.length() == 0) {
			throw new IllegalArgumentException("The app id wasn't valid. [appId=" + appId + "]");
		}
		
		PersistenceAdapter pa = new PersistenceAdapter();
		JavaScriptBE theJs = pa.getJavaScriptBE(jsName, appId);
		if (theJs != null) {
			LOG.info("Existing js entity found.");
			if (!overwrite) {
				LOG.info("Not overwriting existing entity");
				return null;
			}
		} else {
			LOG.info("Creating new js entity");
			theJs = pa.createJavaScriptBE();
		}
		theJs.setName(jsName);
		theJs.setAppId(appId);
		theJs.setJs(jsString);
		pa.makePersistent(theJs);
		
		return theJs;
	}
	
	public final ImageBE importImage(final String imgName, final String appId,
			final byte[] imgData,
			final boolean overwrite) {

		LOG.info("[imgName=" + imgName
				+ "][appId=" + appId
				+ "][imgData=" + imgData
				+ "][overwrite=" + overwrite
				+ "]");

		PersistenceAdapter pa = new PersistenceAdapter();
		ImageBE theImage = pa.getImageBE(imgName, appId);
		if (theImage != null) {
			if (!overwrite) {
				return null;
			}
		} else {
			theImage = pa.createImageBE();
		}
		theImage.setName(imgName);
		theImage.setAppId(appId);
		theImage.setImageData(imgData);
		pa.makePersistent(theImage);
		
		return theImage;
	}
}
