package ken.datastore;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import ken.datastore.model.AppConfigurationBE;
import ken.datastore.model.ImageBE;
import ken.datastore.model.JavaScriptBE;

public class PersistenceAdapter {
	
	protected Class<? extends AppConfigurationBE> appConfigurationBEClass;
	protected Class<? extends JavaScriptBE> javaScriptBEClass;
	protected Class<? extends ImageBE> imageBEClass;
	
	protected PersistenceManager persistenceManager;
	
	protected PersistenceManagerFactory pmf;
	
	public PersistenceAdapter(
			Class<? extends AppConfigurationBE> appConfigurationBEClass,
			Class<? extends JavaScriptBE> javascriptBEClass,
			Class<? extends ImageBE> imageBEClass) {
		this.appConfigurationBEClass = appConfigurationBEClass;
		this.javaScriptBEClass = javascriptBEClass;
		this.imageBEClass = imageBEClass;
		pmf = getPersistenceManagerFactory();
		persistenceManager = pmf.getPersistenceManager();
	}
	
	public PersistenceAdapter() {
		this(AppConfigurationBE.class, JavaScriptBE.class, ImageBE.class);
	}
	
	protected PersistenceManagerFactory getPersistenceManagerFactory() {
		return JDOHelper.getPersistenceManagerFactory("transactions-optional");
	}
	
	public void makePersistent(Object o) {
		persistenceManager.makePersistent(o);
		persistenceManager.flush();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> query(Class<T> cls, String filter, String paramDecl, Object[] params) {
		PersistenceManager pm = null;
		List<T> ret = null;
		try {
			pm = this.persistenceManager;//PMF.get().getPersistenceManager();
			
			Query query = pm.newQuery(cls);
			if(filter != null)
				query.setFilter(filter);
			if(paramDecl != null)
				query.declareParameters(paramDecl);
			if(params != null)
				ret = (List<T>) query.executeWithArray(params);
			else
				ret = (List<T>) query.execute();
			ret.size(); // actually read all results (circumvents lazy loading)
		} 
		finally {
			if (pm != null) {
				//pm.close();
			}
		}
		return ret;
	}
	
	public <T> T queryOne(Class<T> cls, String filter, String paramDecl, Object[] params) {
		List<T> results = query(cls, filter, paramDecl, params);
		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
	
	public AppConfigurationBE getAppConfigurationBE(String appId, String version) {
		return queryOne(this.appConfigurationBEClass, "appId == appIdentifier && versionId == versionIdentifier", "String appIdentifier, String versionIdentifier", new Object[]{appId, version});
	}
	
	public AppConfigurationBE getAppConfigurationBE(String appId) {
		return queryOne(this.appConfigurationBEClass, "appId == appIdentifier", "String appIdentifier", new Object[]{appId});
	}
	
	public JavaScriptBE getJavaScriptBE(String name, String appId) {
		return queryOne(this.javaScriptBEClass, "name == jsName && appId == appName", "String jsName, String appName", new Object[]{name, appId});
	}
	
	public List<? extends JavaScriptBE> getJavaScriptBEsForApp(String appId) {
		return query(this.javaScriptBEClass, "appId == appName", "String appName", new Object[]{appId});		
	}
	
	public ImageBE getImageBE(String name, String appId) {
		return queryOne(this.imageBEClass, "name == imgName && appId == appName", "String imgName, String appName", new Object[]{name, appId});
	}
	
	public ImageBE getImageBE(String name) {
		return queryOne(this.imageBEClass, "name == imgName", "String imgName", new Object[]{name});
	}
	
	public AppConfigurationBE createAppConfigurationBE() {
		return createInstance(this.appConfigurationBEClass);
	}
	
	public JavaScriptBE createJavaScriptBE() {
		return createInstance(this.javaScriptBEClass);
	}
	
	public ImageBE createImageBE() {
		return createInstance(this.imageBEClass);
	}
	
	protected <T> T createInstance(Class<T> cls) {
		try {
			return cls.getConstructor().newInstance();
		}
		catch(Exception e) {
			return null;
		}		
	}
}
