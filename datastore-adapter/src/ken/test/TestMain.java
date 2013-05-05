package ken.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import ken.datastore.manifest.ManifestAdapter;
import ken.datastore.manifest.ManifestValidator;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(new File("/Users/liuken3303/Documents/BrowserApp/datastore-adapter/src/manifest/manifest.json")));
			StringBuffer input = new StringBuffer();
			String line = null;
			while ((line = in.readLine()) != null) {
				input.append(line);
			}
			in.close();
			
//			System.out.println("read manifest: " + input.toString());
			
			JSONObject manifest = new JSONObject(input.toString());
			
			ManifestAdapter adapter = new ManifestAdapter(manifest);
			
//			System.out.println("url : " + adapter.getSourceUrl("flights_list", true));
//			System.out.println("main page: " + adapter.getMainPage());
			
			ManifestValidator.validate(manifest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
