package ken.page;

import java.io.File;

public class MainpageFactory {
	
	private static final String TEMPLATE_PATH = "src/template/";
	private static final String DEFAULT_MAIN_PAGE = "appMain_normal";
	private static final String IOS_MAIN_PAGE = "appMain_ios";
	
	
	public static File getMainpageFile(Class<?> classType) {
		if (classType == IOSSoyPageGenerator.class) {
			return new File(TEMPLATE_PATH + IOS_MAIN_PAGE + ".soy");
		}
		return new File(TEMPLATE_PATH + DEFAULT_MAIN_PAGE + ".soy");
	}
}
