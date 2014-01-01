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

import ken.server.environment.RuntimeSystem;
import ken.server.environment.ServerProtocol;

public class EnvironmentFilter implements Filter {
	
	private static final Logger LOG = Logger.getLogger(RuntimeSystem.class.getName());

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest pReq = (HttpServletRequest) req;
		HttpServletResponse pResp = (HttpServletResponse) resp;
		pReq.setAttribute("runtimeSystem", getRuntimeSystem(pReq));
		chain.doFilter(pReq, pResp);
	}
	
	private RuntimeSystem getRuntimeSystem(HttpServletRequest req) {
		RuntimeSystem runtimeSystem = new RuntimeSystem();
		
		if (req.getProtocol().contains("HTTPS")) {
			runtimeSystem.setProtocol(ServerProtocol.HTTPS);
		} else {
			runtimeSystem.setProtocol(ServerProtocol.HTTP);
		}
		
		if (req.getServerName().contains("localhost")) {
			runtimeSystem.setBaseUrl(req.getServerName() + ":" + req.getServerPort());
		} else {
			runtimeSystem.setBaseUrl(req.getServerName());
		}
		runtimeSystem.setContextPath(req.getContextPath());
		runtimeSystem.setServletPath(req.getServletPath());
		LOG.info("environment generate runtime system : " + runtimeSystem.toString());
		return runtimeSystem;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
