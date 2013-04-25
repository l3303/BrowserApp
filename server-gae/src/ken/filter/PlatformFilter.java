package ken.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import ken.platform.AppConfig;
import ken.platform.PlatformType;

public class PlatformFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain fchain) throws IOException, ServletException {
		System.out.println("enter filter");
		
		String ajax = (String) req.getParameter("ajax");
		System.out.println("filter get ajax : " + ajax);
		boolean ajaxMode = false;
		if ("true".equals(ajax)) {
			ajaxMode = true;
		}
		
		try {
			if (req instanceof HttpServletRequest) {
				HttpServletRequest pReq = (HttpServletRequest)req;
				String userAgent = pReq.getHeader("User-Agent");
				
				AppConfig config = new AppConfig("others", ajaxMode, checkEnvironment(pReq));
				for(PlatformType platformType: PlatformType.values()) {
					if (userAgent.contains(platformType.getPlatFormType())) {
						System.out.println("recognize " + platformType.getPlatFormType());
						config.setPlatform(platformType.getPlatFormType());
					}
				}
				req.setAttribute("appConfig", config);
				
				
//				Enumeration<String> headerNames = pReq.getHeaderNames();
//				while (headerNames.hasMoreElements()) {
//					String header = (String) headerNames.nextElement();
//					System.out.println("requeset header:{" + header + ": " + pReq.getHeader(header) + "}");
//				}
			}
			fchain.doFilter(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	private String checkEnvironment(HttpServletRequest req) {
		String baseUrl = new String(req.getRequestURL());
		if (baseUrl.contains(".appspot.com")) {
			return "gae";
		} else {
			return "local";
		}
	}

}
