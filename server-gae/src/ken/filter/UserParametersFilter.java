package ken.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserParametersFilter implements Filter{
	
	private static final String PRIFIX_USER_PARAMETER = "_";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest pReq, ServletResponse pResp,
			FilterChain pChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) pReq;
		HttpServletResponse resp = (HttpServletResponse) pResp;
		
		HashMap<String, String> userParameters = new HashMap<String, String>();
		final StringBuffer logMessages = new StringBuffer();
		
		Enumeration<String> paraNames = req.getParameterNames();
		while (paraNames.hasMoreElements()) {
			String name = (String) paraNames.nextElement();
			if (req.getParameter(name).startsWith(PRIFIX_USER_PARAMETER)) {
				userParameters.put(name, req.getParameter(name));
				logMessages.append("[" + name + "=" + req.getParameter(name) + "]");
			}
		}
		if (logMessages.length() == 0) {
			logMessages.append("Delivered user parameters: No user parameters available.");
		}

		System.out.println("Delivered user parameters: " + logMessages.toString());
		
		req.setAttribute("userParameters", userParameters);
		pChain.doFilter(req, pResp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
