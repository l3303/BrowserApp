package ken.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceLoader {
	public static String readStream(InputStream is) {
	    try {
	        return new java.util.Scanner(is, "UTF-8").useDelimiter("\\A").next();
	    } catch (java.util.NoSuchElementException e) {
	        return "";
	    }
	}
	
	public static InputStream getResourceAsStream(String resourceName) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
	}
	
	public static String getResourceAsString(String resourceName) {
		return readStream(getResourceAsStream(resourceName));
	}
	
	public static String getFileAsString(File file) throws FileNotFoundException {
		return readStream(new FileInputStream(file));
	}
}
