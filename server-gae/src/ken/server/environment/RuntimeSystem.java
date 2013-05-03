package ken.server.environment;

public final class RuntimeSystem {
	
	private ServerProtocol protocol;
	private String baseUrl;
	private String contextPath;
	private String servletPath;
	
	public RuntimeSystem() {
		super();
	}
	
	public ServerProtocol getProtocol() {
		return protocol;
	}
	
	public void setProtocol(ServerProtocol protocol) {
		this.protocol = protocol;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String getContextPath() {
		return contextPath;
	}
	
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public String getServletPath() {
		return servletPath;
	}
	
	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}
	
	public boolean isLocal() {
		if (baseUrl.contains("localhost")) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getAbsoluteUrl() {
		return protocol.getProtocol() + baseUrl + contextPath + servletPath;
	}
	
	public String toString() {
		return "[protocol=" + protocol.getProtocol() + "][baseUrl=" + baseUrl + "][contextPath=" + contextPath + "][servletPath=" + servletPath + "]";
	}
}
