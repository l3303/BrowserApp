package ken.datastore.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ken.datastore.PersistenceAdapter;
import ken.datastore.API.DatastoreAPI;
import ken.datastore.model.AppConfigurationBE;

public class DataImportServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("call do get method");
		if (req.getParameter("action").equals("importManifest")) {
			doPost(req, resp);
		} else {
			PersistenceAdapter pa = new PersistenceAdapter();
			AppConfigurationBE theFile = pa.getAppConfigurationBE("flightStatus");
			System.out.println("get manifest form datastore: " + theFile.getManifest());
//			if (theFile != null) {
//				System.out.println("get manifest form datastore: " + theFile);
//			} else {
//				System.out.println("not find!!!");
//			}
		}
		resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = req.getPathInfo();
		System.out.println("path is : " + path);
		importManifest();
		resp.setStatus(HttpServletResponse.SC_OK);
	}
	
	private void importManifest() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File("src/mainfest/manifest.json")));
			
			StringBuffer input = new StringBuffer();
			String line = null;
			while ((line = in.readLine()) != null) {
				input.append(line);
			}
			in.close();
			
			System.out.println("store manifest: " + input.toString());
			DatastoreAPI.importManifest(input.toString(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
