package ken.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ken.controller.AppController;
import ken.platform.AppConfig;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.tofu.SoyTofu;
import com.google.template.soy.tofu.SoyTofu.Renderer;

public class AppServlet extends HttpServlet {
	
	private static final Logger LOG = Logger.getLogger(AppServlet.class.getName());
	
	private static final String APP_CONFIG = "appConfig";
	private static final String APP_ID = "appid";
	private static final String SCREEN_ID = "sid";
	private static final String USER_PARAMETERS = "userParameters";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		final String appId = (String) req.getParameter(APP_ID);
		final String sid = (String) req.getParameter(SCREEN_ID);
		final AppConfig appConfig = (AppConfig) req.getAttribute(APP_CONFIG);
		final HashMap<String, String> userParameters = (HashMap<String, String>) req.getAttribute(USER_PARAMETERS);
		
		System.out.println("ajax mode in app servlet: " + appConfig.isAjaxMode());
		
		PrintWriter writer = resp.getWriter();
		try {
			AppController controller = new AppController(appId, sid, appConfig, userParameters);
			String page = controller.doGenerate();
			
			
			writer.write(page);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.flush();
			writer.close();
		}
	}

}
