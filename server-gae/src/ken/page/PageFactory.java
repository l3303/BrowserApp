package ken.page;

import java.io.File;

public class PageFactory {
	
	private static final String TEMPLATE_PATH = "src/template/";
	private static final String TEMPLATE_PATH_MAIN = TEMPLATE_PATH + "main/";
	private static final String DEFAULT_MAIN_PAGE = "appMain_normal";
	private static final String IOS_MAIN_PAGE = "appMain_ios";
	
	
	public static File getMainpageFile(Class<?> classType) {
		if (classType == IOSSoyPageGenerator.class) {
			return new File(TEMPLATE_PATH_MAIN + IOS_MAIN_PAGE + ".soy");
		}
		return new File(TEMPLATE_PATH_MAIN + DEFAULT_MAIN_PAGE + ".soy");
	}
	
	public static File getTemplateFile(String template) {
		return new File(TEMPLATE_PATH + template + ".soy");
	}
}
