package ken.platform;

public class AppConfig {
	
	private String platform;
	private boolean isAjaxMode;
	/*
	 * environment:
	 * "gae" running on Google App Engine
	 * "local" running on localhost
	 * */
	private String environment;
	
	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public AppConfig(String platform, boolean isAjaxMode, String environment) {
		this.platform = platform;
		this.isAjaxMode = isAjaxMode;
		this.environment = environment;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public boolean isAjaxMode() {
		return isAjaxMode;
	}

	public void setAjaxMode(boolean isAjaxMode) {
		this.isAjaxMode = isAjaxMode;
	}
}
