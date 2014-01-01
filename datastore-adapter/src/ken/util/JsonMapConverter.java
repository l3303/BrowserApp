package ken.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class JsonMapConverter {

	/**
	 * The JSON Object to be converted.
	 */
	private final JSONObject mJsonObject;

	/**
	 * The HashMap that stores all the JSON content.
	 */
	private final HashMap<String, Object> mRoot = new HashMap<String, Object>();

	/**
	 * Initialize the converter with data in JSON format.
	 * @param pJsonSource The JSON source code as JSONObject.
	 */
	public JsonMapConverter(final JSONObject pJsonSource) {
		mJsonObject = pJsonSource;
	}

	/**Initializes the converter with data in JSON format.
	 * @param pJsonSource The JSON source code as string.
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	public JsonMapConverter(final String pJsonSource) throws JSONException {

		if (StringUtils.trimToEmpty(pJsonSource).isEmpty()) {
			throw new IllegalArgumentException("The delivered JSON source must not be empty or null!");
		}
		mJsonObject = new JSONObject(pJsonSource);
	}

	/**Converts the given JSON Object into a HashMap.
	 * @return The HashMap which contains the key-value pairs of the given JSON object.
	 * @throws JSONException 
	 */
	public final HashMap<String, Object> toJSON() throws JSONException {
		return toJSON(mRoot, mJsonObject);
	}
	
	/**The JSON Object gets converted into the respective HashMap structure.
	 * @param pHashMap The HashMap which have to filled with the JSON key-value pairs.
	 * @param pJsonObject The JSON Object which contains the data.
	 * @return A HashMap which contains the JSON content.
	 * @throws JSONException 
	 */
	private HashMap<String, Object> toJSON(final HashMap<String, Object> pHashMap,
			final JSONObject pJsonObject) throws JSONException {

		@SuppressWarnings("unchecked")
		Iterator<String> it = pJsonObject.keys();

		while (it.hasNext()) {

			final String key = it.next();

			if (pJsonObject.get(key) instanceof JSONArray) {
				pHashMap.put(key, toJSON(new ArrayList<Object>(), (JSONArray) pJsonObject.get(key)));
			} else if (pJsonObject.get(key) instanceof JSONObject) {
				pHashMap.put(key, toJSON(new HashMap<String, Object>(), (JSONObject) pJsonObject.get(key)));
			} else {
				pHashMap.put(key, pJsonObject.get(key));
			}
		}

		return pHashMap;
	}

	/**
	 * The JSON Array gets converted into the respective ArrayList structure.
	 * @param pArrayList The ArrayList which have to filled with the JSON key-value pairs.
	 * @param pJsonArray The JSON Array which contains the data.
	 * @return A ArrayList which contains the JSON content.
	 * @throws JSONException 
	 */
	private ArrayList<Object> toJSON(final ArrayList<Object> pArrayList, final JSONArray pJsonArray) throws JSONException {

		for (int i = 0; i < pJsonArray.length(); i++) {

			if (pJsonArray.get(i) instanceof JSONArray) {
				pArrayList.add(toJSON(new ArrayList<Object>(), (JSONArray) pJsonArray.get(i)));
			} else if (pJsonArray.get(i) instanceof JSONObject) {
				pArrayList.add(toJSON(new HashMap<String, Object>(), (JSONObject) pJsonArray.get(i)));
			} else {
				pArrayList.add(pJsonArray.get(i));
			}
		}
		return pArrayList;
	}

	@Override
	public final String toString() {
		return "JsonMapConverter [mJsonObject=" + mJsonObject + ", size of mRoot: " + mRoot.size() + ", mRoot="
				+ mRoot + "]";
	}
}