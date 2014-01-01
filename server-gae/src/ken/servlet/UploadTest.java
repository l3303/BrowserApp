package ken.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadTest extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		
		try {
			List<FileItem> items = upload.parseRequest(req);
			
			for (FileItem item : items) {
				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					String value = item.getString();
					System.out.println("field name: " + fieldName + " &value: " + value);
				} else {
					String fileName = item.getName();
					String fileType = item.getContentType();
					System.out.println("file name: " + fileName + " &type: " + fileType);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
