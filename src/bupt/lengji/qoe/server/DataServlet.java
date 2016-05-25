package bupt.lengji.qoe.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import bupt.lengji.qoe.analysis.RecordWriter;

public class DataServlet implements Servlet {

	@Override
	public void init(ServletConfig config) throws ServletException {

	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		InputStream inputStream = request.getInputStream();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int length;
		while ((length = inputStream.read(data)) != -1) {
			outputStream.write(data, 0, length);
		}
		inputStream.close();
		String record = new String(outputStream.toByteArray());
		RecordWriter rw = new RecordWriter("E://QoEData/QoEData.log", record);
		while (!rw.execute());

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter pw = response.getWriter();
		pw.write("successful");
		pw.flush();
		pw.close();
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void destroy() {

	}

}
