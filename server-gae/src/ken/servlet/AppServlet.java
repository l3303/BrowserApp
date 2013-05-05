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

import org.apache.commons.lang3.StringUtils;

import ken.controller.AppController;
import ken.filter.FilterConstants;
import ken.platform.AppConfig;
import ken.server.environment.RuntimeSystem;

import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.tofu.SoyTofu;
import com.google.template.soy.tofu.SoyTofu.Renderer;

public class AppServlet extends HttpServlet {
	
	private static final Logger LOG = Logger.getLogger(AppServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		final String appId = StringUtils.trimToEmpty(req.getParameter(FilterConstants.PARAM_APP_ID));
		final String sid = StringUtils.trimToEmpty(req.getParameter(FilterConstants.PARAM_SCREEN_ID));
		final AppConfig appConfig = (AppConfig) req.getAttribute(FilterConstants.PARAM_APP_CONFIG);
		final JSONObject manifest = (JSONObject)req.getAttribute(FilterConstants.PARAM_MANIFEST);
		final HashMap<String, String> userParameters = (HashMap<String, String>) req.getAttribute(FilterConstants.PARAM_USER_PARAMETERS);
		final RuntimeSystem runtimeSystem = (RuntimeSystem) req.getAttribute(FilterConstants.PARAM_RUNTIME_SYSTEM);
		
//		LOG.info("ajax mode in app servlet: " + appConfig.isAjaxMode());
		LOG.info("get manifest from req: " + manifest);
		
		PrintWriter writer = resp.getWriter();
		try {
			AppController controller = new AppController(appId, sid, appConfig, userParameters, runtimeSystem, manifest);
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
