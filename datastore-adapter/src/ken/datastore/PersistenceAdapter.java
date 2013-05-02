package ken.datastore;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import ken.datastore.model.AppConfigurationBE;

public class PersistenceAdapter {
	
	protected Class<? extends AppConfigurationBE> appConfigurationBEClass;
	
	protected PersistenceManager persistenceManager;
	
	protected PersistenceManagerFactory pmf;
	
	public PersistenceAdapter(
			Class<? extends AppConfigurationBE> appConfigurationBEClass) {
		this.appConfigurationBEClass = appConfigurationBEClass;
		pmf = getPersistenceManagerFactory();
		persistenceManager = pmf.getPersistenceManager();
	}
	
	public PersistenceAdapter() {
		this(AppConfigurationBE.class);
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
	
	public AppConfigurationBE createAppConfigurationBE() {
		return createInstance(this.appConfigurationBEClass);
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
