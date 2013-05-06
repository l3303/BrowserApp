package ken.util;

import com.google.javascript.jscomp.CompilationLevel;
import com.googlecode.htmlcompressor.compressor.ClosureJavaScriptCompressor;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class WebPageCompressor {

	/**Default Ctor.*/
	private WebPageCompressor() {
	}
	
	/**
	 * This method configures the HTML compressor and starts all the optimizations.
	 * @param pUncompressedHtml The uncompressed HTML with inline CSS and JS as String. 
	 * @return The compressed HTML page.
	 */
	public static String compressHtml(final String pUncompressedHtml) {

		HtmlCompressor compressor = new HtmlCompressor();

//		sLog.info("Setting ALL settings for the code compressor...");
		compressor.setEnabled(true);                   //if false all compression is off (default is true)
		compressor.setRemoveComments(true);            //if false keeps HTML comments (default is true)
		compressor.setRemoveMultiSpaces(true);         //if false keeps multiple whitespace characters (default is true)
		compressor.setRemoveIntertagSpaces(true);      //removes iter-tag whitespace characters
		compressor.setRemoveQuotes(false);              //removes unnecessary tag attribute quotes
		compressor.setSimpleDoctype(false);             //simplify existing doctype
		compressor.setRemoveScriptAttributes(false);    //remove optional attributes from script tags
		compressor.setRemoveStyleAttributes(false);     //remove optional attributes from style tags
		compressor.setRemoveLinkAttributes(false);      //remove optional attributes from link tags
		compressor.setRemoveFormAttributes(false);      //remove optional attributes from form tags
		compressor.setRemoveInputAttributes(false);     //remove optional attributes from input tags
		compressor.setSimpleBooleanAttributes(true);   //remove values from boolean tag attributes
		compressor.setRemoveJavaScriptProtocol(true);  //remove "javascript:" from inline event handlers
		compressor.setRemoveHttpProtocol(false);        //replace "http://" with "//" inside tag attributes
		compressor.setRemoveHttpsProtocol(false);       //replace "https://" with "//" inside tag attributes

		compressor.setCompressCss(true);               //compress inline css 
		compressor.setCompressJavaScript(true);        //compress inline javascript
		
		compressor.setGenerateStatistics(true);			

		//use Google Closure Compiler for javascript compression
		ClosureJavaScriptCompressor cjsc = new ClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS);
		//cjsc.setLoggingLevel(Level.FINEST);

		compressor.setJavaScriptCompressor(cjsc);

		// start compression				
		/*String compressedHtml = compressor.compress(pUncompressedHtml);
		LOG.info(System.getProperty("line.separator") + "Original metrics: " + compressor.getStatistics().getOriginalMetrics()
				+ System.getProperty("line.separator") + "Compressed metrics: " + compressor.getStatistics().getCompressedMetrics()
				+ System.getProperty("line.separator")
				+ "Size of skipped blocks (not modified): " + compressor.getStatistics().getPreservedSize() + " bytes."
				+ System.getProperty("line.separator")
				+ "Time: " + compressor.getStatistics().getTime() + " ms.");*/

		// TODO reenable compression
		String compressedHtml = pUncompressedHtml;

		return compressedHtml;
	}
}
