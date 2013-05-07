package ken.datastore.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

import ken.datastore.API.DatastoreAPI;
import ken.datastore.model.AppConfigurationBE;
import ken.datastore.model.ImageBE;
import ken.datastore.model.JavaScriptBE;

public class DataImportController {
	
	private String appId;
	
	public void importApp(String path) {
		if (!path.endsWith("/")) {
			path += "/";
		}
		File manifest = new File(path + "manifest.json");
		if (manifest != null) {
			importManifest(manifest);
		}
		if (appId != null && !appId.isEmpty()) {
			File dir = new File(path);
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile() && file.getName().endsWith(".js")) {
					importJS(file);
				} else if (file.isFile() && file.getName().endsWith(".png")) {
					importImage(file);
				}
			}
		}
	}
	
	private void importManifest(File manifest) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(manifest));
			
			StringBuffer input = new StringBuffer();
			String line = null;
			while ((line = in.readLine()) != null) {
				input.append(line);
			}
			in.close();
			
			System.out.println("store manifest: " + input.toString());
			AppConfigurationBE config = DatastoreAPI.importManifest(input.toString(), true);
			appId = config.getAppId();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void importJS(File jsFile) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			InputStream in = new FileInputStream(jsFile);
			final int bufferSize = 1024;
			final byte[] buffer = new byte[bufferSize];
			
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				System.out.println("file length: " + len);
				if (len < bufferSize) {
					for (int i = 0; i < len; i++) {
						out.write(buffer[i]);
					}
				} else {
					out.write(buffer);
				}
			}
			
			in.close();
			out.close();
						
			JavaScriptBE js = DatastoreAPI.importJavaScript(jsFile.getName(), appId, new String(out.toByteArray(), "UTF-8"), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void importImage(File imageFile) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			InputStream in = new FileInputStream(imageFile);
			final int bufferSize = 1024;
			final byte[] buffer = new byte[bufferSize];
			
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				System.out.println("file length: " + len);
				if (len < bufferSize) {
					for (int i = 0; i < len; i++) {
						out.write(buffer[i]);
					}
				} else {
					out.write(buffer);
				}
			}
			
			in.close();
			out.close();
						
			ImageBE js = DatastoreAPI.importImage(imageFile.getName(), appId, out.toByteArray(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
