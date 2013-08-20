package com.jlyw.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.jlyw.util.UIDUtil;
import com.jlyw.util.xmlHandler.WriteXml;

public class FormatXmlServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(FormatXmlServlet.class);
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.parseInt(req.getParameter("method"));
		switch (method) {
		case 1: // ����ԭʼ��¼������XML�����ļ�
			File downloadFile = null;
			try {
//				String excel1 = req.getParameter("excel1");// ԭʼ��¼����������
				String excel2 = req.getParameter("excel2");// ֤�鹤��������
				String begin = req.getParameter("begin").toUpperCase();// ֤������--��ʼ
				String end = req.getParameter("end").toUpperCase();// ֤������--����
				String excel3 = req.getParameter("excel3");	//������Ϣ������
				String excel3Begin = req.getParameter("excel3Begin");	//������Ϣ����ʼ
				String excel3End = req.getParameter("excel3End");	//������Ϣ�������
				String model = req.getParameter("model");	//�ܼ����߹�����
				String range = req.getParameter("range");
				String accuracy = req.getParameter("accuracy");
				
				String rows = req.getParameter("typerows").trim(); // ��Ҫ��ӵ��ֶ�
				JSONArray typerows = new JSONArray(rows); // ��Ҫ��ӵ��ֶ�
				if (excel2.length() == 0 || begin.length() == 0 || end.length() == 0 ||
						excel3.length() == 0 || excel3Begin.length() == 0 || excel3End.length() == 0) {
					throw new Exception("������Ϣ�п�ֵ");
				}

				downloadFile = File.createTempFile(UIDUtil.get22BitUID(),".xml");
				WriteXml xml = new WriteXml(downloadFile.getAbsolutePath(),
						"original-record");
				HashMap<String, String> map = new HashMap<String, String>();
//				map.put("description", "ԭʼ��¼���ڹ����������;sheetName���幤��������ƣ�");
//				map.put("sheetName", excel1);
//				xml.writeElement(map, "original-record-sheet");

				map.clear();

				/******************** ��ȡ�ֶ�������Ϣ ******************/
				if (typerows != null && typerows.length() > 0) {
					map.put("description",
									"�ֶζ���,��Ҫ��xls�ж�ȡ��д����ֶε�field������Ϊtrue���ֶ���Ϊ��ǩ���������ݿ��ֶ�����Ӧ����type����ֵ��wΪ��д���ļ���rΪ�����ļ�������rwΪд��Ͷ���; fieldClassֵ��Ӧ����д��/�������ݶ�Ӧ�������ƣ���ԭʼ��¼��ί�е���;typeClass:���ݶ�Ӧ���ֶ����ͣ�typeΪrʱ����Ҫ��");
					xml.startElement(map, "field-definition");
					for (int i = 0; i < typerows.length(); i++) {
						JSONObject jsonObj = typerows.getJSONObject(i);

						
						String fieldClass = jsonObj.get("fieldClass").toString(); // ������
						String fieldClassDesc = jsonObj.get("fieldClass").toString(); // ������
						if (fieldClass.equals("ί�е�"))
							fieldClass = "com.jlyw.hibernate.CommissionSheet";
						else if (fieldClass.equals("ԭʼ��¼"))
							fieldClass = "com.jlyw.hibernate.OriginalRecord";
						else if (fieldClass.equals("��У��Ա") || fieldClass.equals("������Ա"))
							fieldClass = "com.jlyw.hibernate.SysUser";
						else if (fieldClass.equals("�����淶"))
							fieldClass = "com.jlyw.hibernate.Specification";
						else if (fieldClass.equals("������׼"))
							fieldClass = "com.jlyw.hibernate.Standard";
						else if (fieldClass.equals("��׼����"))
							fieldClass = "com.jlyw.hibernate.StandardAppliance";
						else if (fieldClass.equals("֤��"))
							fieldClass = "com.jlyw.hibernate.Certificate";
						else if (fieldClass.equals("���߱�׼��"))
							fieldClass = "com.jlyw.hibernate.ApplianceStandardName";
						String attribute = jsonObj.get("attribute").toString(); // ��������
						String sheetName = jsonObj.get("sheetName").toString();	//����������
						String rowIndex = jsonObj.getString("rowIndex"); // ��Ӧ��Ԫ��--row��
						String typeClass = jsonObj.get("typeClass").toString(); // ��������
						String type = jsonObj.get("type").toString(); // ��д����
						String desc = jsonObj.getString("desc"); // ��������
						String indexStr = jsonObj.getString("indexStr");	//˳���:ֻ�Լ����淶��������׼����׼������Ч
					
						int[] a = statrowcol(rowIndex); // a[0]Ϊ�У�a[1]Ϊ��

						map.clear();
						map.put("field", "true");
						map.put("sheetName", sheetName);
						map.put("rowIndex", String.format("%d", a[0]));
						map.put("colIndex", String.format("%d", a[1]));
						map.put("type", type);
						map.put("fieldClass", fieldClass);
						map.put("typeClass", typeClass);
						map.put("desc", desc);
						map.put("indexStr", indexStr);
						map.put("fieldClassDesc", fieldClassDesc);	//��Ҫ�����������У��Ա/������Ա  ������ѡ��
						map.put("cell", rowIndex);	//��Ԫ����A12����Ҫ���ڸ��û���ʾ������Ϣ
						xml.writeElement(map, attribute);

					}
					xml.endElement("field-definition");

				} else {
					throw new Exception("�ֶ���ϢΪ��");
				}
				map.clear();
				map.put("sheetName", excel2);
				map.put("region", String.format("%s:%s", begin, end));
				map.put("description", "�����ԭʼ��¼excel������֤�������;sheetName���幤��������ƣ�region���������磺A1:H10��");
				xml.writeElement(map, "certificate-sheet");
				
				map.clear();
				map.put("model", model==null?"false":"true");
				map.put("range", range==null?"false":"true");
				map.put("accuracy", accuracy==null?"false":"true");
				xml.writeElement(map, "target-appliance-link-to");	//�ܼ����߹�����
				
				map.clear();
				map.put("sheetName", excel3);
				int[] a = statrowcol(excel3Begin); // a[0]Ϊ�У�a[1]Ϊ��
				map.put("regionBeginRowIndex", String.format("%d", a[0]));
				map.put("regionBeginColIndex", String.format("%d", a[1]));
				a = statrowcol(excel3End); // a[0]Ϊ�У�a[1]Ϊ��
				map.put("regionEndRowIndex", String.format("%d", a[0]));
				map.put("regionEndColIndex", String.format("%d", a[1]));
				map.put("description", "�����ԭʼ��¼excel�и�����Ϣ��������Ϣ��-����֤������ʾ��������;sheetName���幤��������ƣ�regionBeginRowIndex��regionBeginColIndex������Ϣ��ʼ�ĵ�Ԫ��regionEndRowIndex��regionEndColIndex������Ϣ�����ĵ�Ԫ��");
				xml.writeElement(map, "addition-info-region");

				xml.endDocument();

				String downloadFileName = "ԭʼ��¼.xml";
				if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) // firefox�����
					downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
				else
					// if(req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) //IE�����
					downloadFileName = URLEncoder.encode(downloadFileName,"UTF-8");

				// �����������Ϣͷ�� ����-����,����;�ļ���=(��������ļ���) URLEncoader.encode() ���ı���
				resp.setHeader("Content-disposition", "attachment;filename=" + downloadFileName);

				InputStream ins = null;
				java.io.OutputStream outStream = resp.getOutputStream();
				try {
					ins = new FileInputStream(downloadFile);
					
					byte[] buf = new byte[2048];
					int iRead = ins.read(buf);
					while (iRead != -1) {
						outStream.write(buf, 0, iRead);
						iRead = ins.read(buf);
					}
				} catch (Exception e) {
					log.debug("exception in FormatXmlServlet-->case 1-->FileDownload", e);
				} finally {
					if (ins != null) {
						ins.close();
						ins = null;
					}
					outStream.close();
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FormatXmlServlet-->case 1", e);
				}else{
					log.error("error in FormatXmlServlet-->case 1", e);
				}
			} finally {
				if (downloadFile != null && downloadFile.exists()) {
					downloadFile.delete();
					downloadFile = null;
				}
			}
			break;
		case 2: // �춨֤��XML�����ļ�
			File downloadFile1 = null;
			try {
				String rows = req.getParameter("typerows").trim(); // ��Ҫ��ӵ��ֶ�
				JSONArray typerows = new JSONArray(rows); // ��Ҫ��ӵ��ֶ�
				
				downloadFile1 = File.createTempFile(UIDUtil.get22BitUID(),".xml");
				WriteXml xml = new WriteXml(downloadFile1.getAbsolutePath(),"original-record");
				HashMap<String, String> map = new HashMap<String, String>();
				map.clear();

				/******************** ��ȡ�ֶ�������Ϣ ******************/
				if (typerows != null && typerows.length() > 0) {

					// xml.startElement(map, "certificate");
					for (int i = 0; i < typerows.length(); i++) {
						JSONObject jsonObj = typerows.getJSONObject(i);

						String tagName = jsonObj.get("tagName").toString(); // ������
						String attribute = jsonObj.get("attribute").toString(); // ��������
						String desc = jsonObj.getString("desc"); // ��������
						String indexStr = jsonObj.getString("indexStr");	//˳���:ֻ�Լ����淶��������׼����׼������Ч

						map.clear();
						map.put("desc", desc);
						map.put("tagName", tagName);
						map.put("indexStr", indexStr);
						xml.writeElement(map, attribute);

					}
					// xml.endElement("certificate");

				} else {
					throw new Exception("�ֶ���ϢΪ��");
				}
				map.clear();

				xml.endDocument();
				String downloadFile1Name = "�춨֤��.xml";
				if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) // firefox�����
					downloadFile1Name = new String(downloadFile1Name.getBytes("UTF-8"), "ISO8859-1");
				else
					// if(req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) //IE�����
					downloadFile1Name = URLEncoder.encode(downloadFile1Name,"UTF-8");

				// �����������Ϣͷ�� ����-����,����;�ļ���=(��������ļ���) URLEncoader.encode() ���ı���
				resp.setHeader("Content-disposition", "attachment;filename=" + downloadFile1Name);

				InputStream ins = null;
				java.io.OutputStream outStream = resp.getOutputStream();
				try {
					ins = new FileInputStream(downloadFile1);
					byte[] buf = new byte[2048];
					int iRead = ins.read(buf);
					while (iRead != -1) {
						outStream.write(buf, 0, iRead);
						iRead = ins.read(buf);
					}
				} catch (Exception e) {
					log.debug("exception in FormatXmlServlet-->case 2-->FileDownload", e);
				} finally {
					if (ins != null) {
						ins.close();
						ins = null;
					}
					outStream.close();
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FormatXmlServlet-->case 2", e);
				}else{
					log.error("error in FormatXmlServlet-->case 2", e);
				}
			} finally {
				if (downloadFile1 != null && downloadFile1.exists()) {
					downloadFile1.delete();
					downloadFile1 = null;
				}
			}
			break;

		}
	}

	public static int[] statrowcol(String a) { // ͳ���С�����
		int[] result = new int[2];
		a = a.toUpperCase(); // Сд��ĸת��д
		char[] ch = a.toCharArray();
		int m = 0, n = 0; // mΪ�У�nΪ��
		int len = a.length();

		for (int i = 0; i < len; i++) {
			if (ch[i] <= 'Z' && ch[i] >= 'A') {
				m = m * 26 + (ch[i] - 'A') + 1;

			}
			if (ch[i] <= '9' && ch[i] >= '0') {
				n = n * 10 + (ch[i] - '0');
			}
		}
		result[0] = n-1;
		result[1] = m-1;

		return result;

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doPost(req, resp);
	}

	public static void main(String[] args) {
		int[] a = statrowcol("aa12");

		System.out.println(a[0]);
		System.out.println(a[1]);
	}

}
