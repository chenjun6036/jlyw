package com.jlyw.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Export extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = req.getParameter("filePath");
		InputStream fis = null;
		try{
			File file = new File(path);
			String filename = file.getName();
			fis = new FileInputStream(path);
			OutputStream toClient = resp.getOutputStream();
			resp.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			resp.addHeader("Content-Length", "" + file.length());
			resp.setContentType("application/octet-stream");
			byte[] buffer = new byte[2048];
			int iRead = fis.read(buffer);
			while(iRead != -1){
				toClient.write(buffer, 0, iRead);
				iRead = fis.read(buffer);
			}
			
			toClient.flush();
			toClient.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fis != null){
				fis.close();
				fis = null;
			}
		}
	}

}
