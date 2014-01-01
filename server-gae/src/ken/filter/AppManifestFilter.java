package ken.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ken.datastore.PersistenceAdapter;
import ken.datastore.manifest.ManifestJsonFormator;
import ken.datastore.model.AppConfigurationBE;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class AppManifestFilter implements Filter {
	
	/**The common logger.*/
	private static final Logger LOG = Logger.getLogger(AppManifestFilter.class.getName());

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest pReq = (HttpServletRequest) req;
		HttpServletResponse pResp = (HttpServletResponse) resp;
		
		try {
			final String appId = StringUtils.trimToEmpty(pReq.getParameter("appid"));
			PersistenceAdapter pa = new PersistenceAdapter();
			
			final AppConfigurationBE config = pa.getAppConfigurationBE(appId);
			if (config == null || config.getManifest().length() == 0) {
				LOG.info("Couldn't load any app configuration with appId=" + appId);
				return;
			}
			
			final JSONObject manifest = ManifestJsonFormator.formatToJson(config.getManifest());
			LOG.info("Successfully load manifest: " + manifest);
			
			req.setAttribute("manifest", manifest);
			
			chain.doFilter(pReq, pResp);
		} catch (Exception e) {
			LOG.info("error when parser to json object!!!");
			e.printStackTrace();
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
