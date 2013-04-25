package ken.platform;

public enum PlatformType {
	
	IPHONE("iPhone"),
	IPAD("iPad");
	
	private final String paltform;
	
	private PlatformType(String platform) {
		this.paltform = platform;
	}
	
	public String getPlatFormType() {
		return paltform;
	}
}
