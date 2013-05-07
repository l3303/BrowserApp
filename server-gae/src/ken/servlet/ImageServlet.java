package ken.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ken.datastore.PersistenceAdapter;
import ken.datastore.model.ImageBE;

import org.apache.commons.lang3.StringUtils;

public class ImageServlet extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(ImageServlet.class.getName());
	private static final String SERVLET_NAME = "image";
	private static final String SERVLET_PATH = "services/" + SERVLET_NAME;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String imageName = StringUtils.trimToEmpty(req.getParameter("img"));
		String appId = StringUtils.trimToEmpty(req.getParameter("appId"));
		
		if (!imageName.isEmpty()) {
			PersistenceAdapter pm = new PersistenceAdapter();
			ImageBE imgBE = null;
			if (appId.isEmpty()) {
				imgBE = pm.getImageBE(imageName);
			} else {
				imgBE = pm.getImageBE(imageName, appId);
			}
			if (imgBE != null) {
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.setHeader("Cache-Control", "no-cache, must-revalidate");
				resp.setContentType(getServletContext().getMimeType(imageName));
				byte[] image = imgBE.getImageData();
				resp.getOutputStream().write(image);
				resp.getOutputStream().flush();
				resp.getOutputStream().close();
			} else {
				LOG.info("Sorry, fail to get image with name: " + imageName + " and appId: " + appId);
			}
		} else {
			LOG.warning("no image name provided.");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}
	
}
