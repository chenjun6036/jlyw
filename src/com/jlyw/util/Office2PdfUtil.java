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
 * ִ��ǰ��������openoffice����
 * ����$OO_HOME\program��
 * ִ��soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard 
 */
public class Office2PdfUtil {
	public static int ServicePort = SystemCfgUtil.PdfConvertServerPort;	//OpenOfficeת������Ķ˿�(Ĭ��8100)
	
	private static final Log log = LogFactory.getLog(Office2PdfUtil.class);
	/**
	 * �ĵ���Excel��Word��ת��ΪPDF��ʽ(ͨ��openofficeת������ʧЧ)
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
	    	
	    	//ת��
		    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		    converter.convert(inputFile, outputFile);
	    }catch(ConnectException cex){
	    	throw new Exception(String.format("����PDFת��������ʧ�ܣ�����ϵ����Ա��msg��%s", cex.getMessage()==null?"��":cex.getMessage()));
	    }finally{
		    // close the connection
	    	if(connection != null){
	    		connection.disconnect();
	    		connection = null;
	    	}
	    }
	}
	
	/**
	 * �ĵ���Excel��Word��ת��ΪPDF��ʽ(ͨ��MicroSoft Officeʵ��)
	 * @param inputFile
	 * @param outputFile
	 * @throws Exception
	 */
	public static void docToPdf(File inputFile, File outputFile) throws Exception{
		//��ʼ��com���߳�
		ComThread.InitSTA();
		
		JacobWriter docWriter = null;
		try{
			if(inputFile.getAbsolutePath().endsWith(".doc") || inputFile.getAbsolutePath().endsWith(".docx")){
				docWriter = new Office2PdfUtil().new JacobWriter(true);
			} else if(inputFile.getAbsolutePath().endsWith(".xls") || inputFile.getAbsolutePath().endsWith(".xlsx")){
				docWriter = new Office2PdfUtil().new JacobWriter(false);
			} else{
				throw new Exception("����Excel����Word�ĵ���ת��PDFʧ�ܣ�");
			}
			
			docWriter.openWord(inputFile.getAbsolutePath(), true);	//��ֻ��д��ʽ��
			if(outputFile.exists())
				outputFile.delete();
			docWriter.saveAsPdf(outputFile.getCanonicalPath());	//���ΪPdf
			
			docWriter.closeWord(false);
			docWriter.releaseResourse();
			docWriter = null;
		}catch(Exception e){
			throw e;
		}finally{
			try{
				//�ر�com���߳�
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
	 * �ڲ��ࣺʹ��Jacobת��Excel��Word
	 * @author Administrator
	 *
	 */
	public class JacobWriter {
		private ActiveXComponent app;
		private Dispatch documents = null;
		private Dispatch doc = null;
		private String filepath = null;
		private boolean isWord = true;	//true����Word��flase����Excel
		
		public JacobWriter(boolean isWord) throws Exception{
			this.isWord = isWord;
			if(isWord){
				app = new ActiveXComponent("Word.Application");
				app.setProperty("Visible", new Variant(false));   //����word���ɼ�
				documents = app.getProperty("Documents").toDispatch();
			}else{
				app = new ActiveXComponent("Excel.Application");
				app.setProperty("Visible", new Variant(false));   //����word���ɼ�
				documents = app.getProperty("Workbooks").toDispatch();
			}
			
			
		}
		
		/**
		 * ��Word�ĵ�
		 * @param docFilePath
		 * @param readOnly �Ƿ���ֻ����ʽ��
		 * @throws Exception
		 */
		public void openWord(String docFilePath, boolean readOnly) throws Exception{
			try{
				filepath = docFilePath;
//				doc = Dispatch.call(documents, "Open", filepath).toDispatch();//Ҳ���滻Ϊdoc = Dispatch.invoke(documents, "Open", Dispatch.Method, new Object[]{inFile, new Variant(false), new Variant(false)}, new int[1]).toDispatch();   //��word�ļ���ע���������������Ҫ��Ϊfalse�����������ʾ�Ƿ���ֻ����ʽ�򿪣���Ϊ����Ҫ����ԭ�ļ��������Կ�д��ʽ�򿪡�
				doc = Dispatch.invoke(	//�򿪲���ȡExcel�ĵ�
						documents,
						"Open",
						Dispatch.Method,
						new Object[] {filepath, 
								new Variant(false),
								new Variant(readOnly) //�Ƿ���ֻ����ʽ��:�ǡ���ֻ����ʽ��
						},
						new int[1]).toDispatch();
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * ���ΪPdf
		 * @param filePath: ���Ϊ��·��
		 */
		public void saveAsPdf(String filePath){
			if(isWord){
				if(doc != null){
					Dispatch.invoke(doc, "SaveAs", Dispatch.Method, 
							new Object[] {filePath, new Variant(17) }, new int[1]); 	//17��ʾ���ΪPDF��ʽ
				}
			}else{
				if(doc != null){		//ת��Excel��ǰ�ҳΪPDF
					Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
							filePath, new Variant(57), new Variant(false),
							new Variant(57), new Variant(57), new Variant(false),
							new Variant(true), new Variant(57), new Variant(true),
							new Variant(true), new Variant(true) }, new int[1]);
				}
			}
			
		}
		
		/**
		 * �ر��ĵ�
		 * @param f��Ϊtrueʱ�����޸ĵ��ļ����˳���Ϊfalseʱ�������޸ĵ��ļ����˳�
		 */
		public void closeWord(boolean f){
			if(doc != null){
				Dispatch.call(doc, "Close", new Variant(f));
				doc = null;
			}
		}
		
		/**
		 * �ͷ���Դ
		 */
		public void releaseResourse(){
			documents = null;
			
			// �ͷ� Com ��Դ
			if (app != null) {
				app.invoke("Quit", new Variant[] {});//����ʹ�÷�����Dispatch.call(app, "Quit");
				app = null;
			}
		}		
	}	
	
}
