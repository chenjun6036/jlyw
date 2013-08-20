package com.jlyw.util;

import java.io.File;
import java.net.ConnectException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.jlyw.util.WordUtil.JacobWordWriter;
/**
 * 执行前，请启动openoffice服务
 * 进入$OO_HOME\program下
 * 执行soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard 
 */
public class Office2PdfUtil {
	public static int ServicePort = SystemCfgUtil.PdfConvertServerPort;	//OpenOffice转换服务的端口(默认8100)
	
	private static final Log log = LogFactory.getLog(Office2PdfUtil.class);
	/**
	 * 文档（Excel或Word）转换为PDF格式(通过openoffice转换，已失效)
	 * @param inputFile
	 * @param outputFile
	 * @throws Exception
	 */
	public static void docToPdfByOpenOffice(File inputFile, File outputFile) throws Exception {
		 // connect to an OpenOffice.org instance running on port 8100
	    OpenOfficeConnection connection = null;
	    try{
	    	connection = new SocketOpenOfficeConnection(ServicePort);
	    	connection.connect();
	    	
	    	//转换
		    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		    converter.convert(inputFile, outputFile);
	    }catch(ConnectException cex){
	    	throw new Exception(String.format("连接PDF转换服务器失败，请联系管理员！msg：%s", cex.getMessage()==null?"无":cex.getMessage()));
	    }finally{
		    // close the connection
	    	if(connection != null){
	    		connection.disconnect();
	    		connection = null;
	    	}
	    }
	}
	
	/**
	 * 文档（Excel或Word）转换为PDF格式(通过MicroSoft Office实现)
	 * @param inputFile
	 * @param outputFile
	 * @throws Exception
	 */
	public static void docToPdf(File inputFile, File outputFile) throws Exception{
		//初始化com的线程
		ComThread.InitSTA();
		
		JacobWriter docWriter = null;
		try{
			if(inputFile.getAbsolutePath().endsWith(".doc") || inputFile.getAbsolutePath().endsWith(".docx")){
				docWriter = new Office2PdfUtil().new JacobWriter(true);
			} else if(inputFile.getAbsolutePath().endsWith(".xls") || inputFile.getAbsolutePath().endsWith(".xlsx")){
				docWriter = new Office2PdfUtil().new JacobWriter(false);
			} else{
				throw new Exception("不是Excel或者Word文档，转换PDF失败！");
			}
			
			docWriter.openWord(inputFile.getAbsolutePath(), true);	//以只读写方式打开
			if(outputFile.exists())
				outputFile.delete();
			docWriter.saveAsPdf(outputFile.getCanonicalPath());	//另存为Pdf
			
			docWriter.closeWord(false);
			docWriter.releaseResourse();
			docWriter = null;
		}catch(Exception e){
			throw e;
		}finally{
			try{
				//关闭com的线程
				ComThread.Release();
			}catch(Exception ex){
				log.error("error in Office2PdfUtil-->MakeCertificateWord", ex);
			}
		}
	}
	
	public static void main(String []args){
		File input = new File("D:/ttt.xls");
		File output = new File("D:/ttt.pdf");
		try {
			Office2PdfUtil.docToPdf(input, output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 内部类：使用Jacob转换Excel或Word
	 * @author Administrator
	 *
	 */
	public class JacobWriter {
		private ActiveXComponent app;
		private Dispatch documents = null;
		private Dispatch doc = null;
		private String filepath = null;
		private boolean isWord = true;	//true：是Word；flase：是Excel
		
		public JacobWriter(boolean isWord) throws Exception{
			this.isWord = isWord;
			if(isWord){
				app = new ActiveXComponent("Word.Application");
				app.setProperty("Visible", new Variant(false));   //设置word不可见
				documents = app.getProperty("Documents").toDispatch();
			}else{
				app = new ActiveXComponent("Excel.Application");
				app.setProperty("Visible", new Variant(false));   //设置word不可见
				documents = app.getProperty("Workbooks").toDispatch();
			}
			
			
		}
		
		/**
		 * 打开Word文档
		 * @param docFilePath
		 * @param readOnly 是否以只读方式打开
		 * @throws Exception
		 */
		public void openWord(String docFilePath, boolean readOnly) throws Exception{
			try{
				filepath = docFilePath;
//				doc = Dispatch.call(documents, "Open", filepath).toDispatch();//也可替换为doc = Dispatch.invoke(documents, "Open", Dispatch.Method, new Object[]{inFile, new Variant(false), new Variant(false)}, new int[1]).toDispatch();   //打开word文件，注意这里第三个参数要设为false，这个参数表示是否以只读方式打开，因为我们要保存原文件，所以以可写方式打开。
				doc = Dispatch.invoke(	//打开并获取Excel文档
						documents,
						"Open",
						Dispatch.Method,
						new Object[] {filepath, 
								new Variant(false),
								new Variant(readOnly) //是否以只读方式打开:是——只读方式打开
						},
						new int[1]).toDispatch();
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * 另存为Pdf
		 * @param filePath: 另存为的路径
		 */
		public void saveAsPdf(String filePath){
			if(isWord){
				if(doc != null){
					Dispatch.invoke(doc, "SaveAs", Dispatch.Method, 
							new Object[] {filePath, new Variant(17) }, new int[1]); 	//17表示另存为PDF格式
				}
			}else{
				if(doc != null){		//转换Excel当前活动页为PDF
					Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
							filePath, new Variant(57), new Variant(false),
							new Variant(57), new Variant(57), new Variant(false),
							new Variant(true), new Variant(57), new Variant(true),
							new Variant(true), new Variant(true) }, new int[1]);
				}
			}
			
		}
		
		/**
		 * 关闭文档
		 * @param f：为true时保存修改的文件并退出；为false时不保存修改的文件并退出
		 */
		public void closeWord(boolean f){
			if(doc != null){
				Dispatch.call(doc, "Close", new Variant(f));
				doc = null;
			}
		}
		
		/**
		 * 释放资源
		 */
		public void releaseResourse(){
			documents = null;
			
			// 释放 Com 资源
			if (app != null) {
				app.invoke("Quit", new Variant[] {});//或者使用方法：Dispatch.call(app, "Quit");
				app = null;
			}
		}		
	}	
	
}
