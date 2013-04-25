package ken.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.tofu.SoyTofu;
import com.google.template.soy.tofu.SoyTofu.Renderer;

public class HelloServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		try {
			System.out.println("call my servlet!!!!");
			
			SoyFileSet fileSet = (new SoyFileSet.Builder()).add(new File("template/simple.soy")).build();
			SoyTofu tofu = fileSet.compileToTofu();
			
			Renderer renderer = tofu.newRenderer("examples.simple");
			
			Map<String, String> data = new HashMap<String, String>();
			data.put("content", "This is test from ken!!!");
			renderer.setData(data);
			
			String responseString = renderer.render();
			
			writer.write(responseString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.flush();
			writer.close();
		}
	}
	
}
