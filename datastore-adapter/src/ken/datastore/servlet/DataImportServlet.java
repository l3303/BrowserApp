package ken.datastore.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ken.datastore.PersistenceAdapter;
import ken.datastore.API.DatastoreAPI;
import ken.datastore.controller.DataImportController;
import ken.datastore.model.AppConfigurationBE;
import ken.datastore.model.JavaScriptBE;

public class DataImportServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("call do get method");
		if (req.getParameter("action").equals("importManifest")) {
			doPost(req, resp);
		} else {
			PersistenceAdapter pa = new PersistenceAdapter();
//			AppConfigurationBE theFile = pa.getAppConfigurationBE("dummy_content");
//			System.out.println("get manifest form datastore: " + theFile.getManifest());
//			
//			if (theFile != null) {
//				System.out.println("get manifest form datastore: " + theFile);
//			} else {
//				System.out.println("not find!!!");
//			}
			
			List<? extends JavaScriptBE> jsFiles = pa.getJavaScriptBEsForApp("dummy_content");
			System.out.println("count of js get for app: " + jsFiles.size());
			for (JavaScriptBE jsFile : jsFiles) {
				System.out.println("js content: " + jsFile.getJsAsString());
			}
		}
		resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = req.getPathInfo();
		System.out.println("path is : " + path);
		DataImportController controller = new DataImportController();
		controller.importApp("src/manifest/");
		resp.setStatus(HttpServletResponse.SC_OK);
	}
}
