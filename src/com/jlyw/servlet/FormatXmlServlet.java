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
		case 1: // 生成原始记录工作表XML配置文件
			File downloadFile = null;
			try {
//				String excel1 = req.getParameter("excel1");// 原始记录工作表名称
				String excel2 = req.getParameter("excel2");// 证书工作表名称
				String begin = req.getParameter("begin").toUpperCase();// 证书区域--开始
				String end = req.getParameter("end").toUpperCase();// 证书区域--结束
				String excel3 = req.getParameter("excel3");	//附加信息工作表
				String excel3Begin = req.getParameter("excel3Begin");	//附加信息区域开始
				String excel3End = req.getParameter("excel3End");	//附加信息区域结束
				String model = req.getParameter("model");	//受检器具关联项
				String range = req.getParameter("range");
				String accuracy = req.getParameter("accuracy");
				
				String rows = req.getParameter("typerows").trim(); // 需要添加的字段
				JSONArray typerows = new JSONArray(rows); // 需要添加的字段
				if (excel2.length() == 0 || begin.length() == 0 || end.length() == 0 ||
						excel3.length() == 0 || excel3Begin.length() == 0 || excel3End.length() == 0) {
					throw new Exception("所填信息有空值");
				}

				downloadFile = File.createTempFile(UIDUtil.get22BitUID(),".xml");
				WriteXml xml = new WriteXml(downloadFile.getAbsolutePath(),
						"original-record");
				HashMap<String, String> map = new HashMap<String, String>();
//				map.put("description", "原始记录所在工作表的名称;sheetName定义工作表的名称；");
//				map.put("sheetName", excel1);
//				xml.writeElement(map, "original-record-sheet");

				map.clear();

				/******************** 读取字段配置信息 ******************/
				if (typerows != null && typerows.length() > 0) {
					map.put("description",
									"字段定义,需要从xls中读取或写入的字段的field属性设为true，字段名为标签名（与数据库字段名对应），type属性值：w为仅写入文件，r为仅从文件读出，rw为写入和读出; fieldClass值对应的是写入/读出数据对应的类名称（如原始记录、委托单）;typeClass:数据对应的字段类型（type为r时才需要）");
					xml.startElement(map, "field-definition");
					for (int i = 0; i < typerows.length(); i++) {
						JSONObject jsonObj = typerows.getJSONObject(i);

						
						String fieldClass = jsonObj.get("fieldClass").toString(); // 类名称
						String fieldClassDesc = jsonObj.get("fieldClass").toString(); // 类名称
						if (fieldClass.equals("委托单"))
							fieldClass = "com.jlyw.hibernate.CommissionSheet";
						else if (fieldClass.equals("原始记录"))
							fieldClass = "com.jlyw.hibernate.OriginalRecord";
						else if (fieldClass.equals("检校人员") || fieldClass.equals("核验人员"))
							fieldClass = "com.jlyw.hibernate.SysUser";
						else if (fieldClass.equals("技术规范"))
							fieldClass = "com.jlyw.hibernate.Specification";
						else if (fieldClass.equals("计量标准"))
							fieldClass = "com.jlyw.hibernate.Standard";
						else if (fieldClass.equals("标准器具"))
							fieldClass = "com.jlyw.hibernate.StandardAppliance";
						else if (fieldClass.equals("证书"))
							fieldClass = "com.jlyw.hibernate.Certificate";
						else if (fieldClass.equals("器具标准名"))
							fieldClass = "com.jlyw.hibernate.ApplianceStandardName";
						String attribute = jsonObj.get("attribute").toString(); // 属性名称
						String sheetName = jsonObj.get("sheetName").toString();	//工作表名称
						String rowIndex = jsonObj.getString("rowIndex"); // 对应单元格--row行
						String typeClass = jsonObj.get("typeClass").toString(); // 属性类型
						String type = jsonObj.get("type").toString(); // 读写类型
						String desc = jsonObj.getString("desc"); // 属性描述
						String indexStr = jsonObj.getString("indexStr");	//顺序号:只对技术规范、计量标准、标准器具有效
					
						int[] a = statrowcol(rowIndex); // a[0]为行，a[1]为列

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
						map.put("fieldClassDesc", fieldClassDesc);	//主要是用于区别检校人员/核验人员  的配置选项
						map.put("cell", rowIndex);	//单元格：如A12，主要用于给用户提示错误信息
						xml.writeElement(map, attribute);

					}
					xml.endElement("field-definition");

				} else {
					throw new Exception("字段信息为空");
				}
				map.clear();
				map.put("sheetName", excel2);
				map.put("region", String.format("%s:%s", begin, end));
				map.put("description", "定义该原始记录excel中用于证书的区域;sheetName定义工作表的名称；region定义区域，如：A1:H10；");
				xml.writeElement(map, "certificate-sheet");
				
				map.clear();
				map.put("model", model==null?"false":"true");
				map.put("range", range==null?"false":"true");
				map.put("accuracy", accuracy==null?"false":"true");
				xml.writeElement(map, "target-appliance-link-to");	//受检器具关联项
				
				map.clear();
				map.put("sheetName", excel3);
				int[] a = statrowcol(excel3Begin); // a[0]为行，a[1]为列
				map.put("regionBeginRowIndex", String.format("%d", a[0]));
				map.put("regionBeginColIndex", String.format("%d", a[1]));
				a = statrowcol(excel3End); // a[0]为行，a[1]为列
				map.put("regionEndRowIndex", String.format("%d", a[0]));
				map.put("regionEndColIndex", String.format("%d", a[1]));
				map.put("description", "定义该原始记录excel中附加信息（费用信息等-不在证书上显示）的区域;sheetName定义工作表的名称；regionBeginRowIndex、regionBeginColIndex附加信息开始的单元格，regionEndRowIndex、regionEndColIndex附加信息结束的单元格");
				xml.writeElement(map, "addition-info-region");

				xml.endDocument();

				String downloadFileName = "原始记录.xml";
				if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) // firefox浏览器
					downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
				else
					// if(req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) //IE浏览器
					downloadFileName = URLEncoder.encode(downloadFileName,"UTF-8");

				// 向浏览器发消息头： 内容-设置,附件;文件名=(如果中文文件名) URLEncoader.encode() 中文编码
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
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
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
		case 2: // 检定证书XML配置文件
			File downloadFile1 = null;
			try {
				String rows = req.getParameter("typerows").trim(); // 需要添加的字段
				JSONArray typerows = new JSONArray(rows); // 需要添加的字段
				
				downloadFile1 = File.createTempFile(UIDUtil.get22BitUID(),".xml");
				WriteXml xml = new WriteXml(downloadFile1.getAbsolutePath(),"original-record");
				HashMap<String, String> map = new HashMap<String, String>();
				map.clear();

				/******************** 读取字段配置信息 ******************/
				if (typerows != null && typerows.length() > 0) {

					// xml.startElement(map, "certificate");
					for (int i = 0; i < typerows.length(); i++) {
						JSONObject jsonObj = typerows.getJSONObject(i);

						String tagName = jsonObj.get("tagName").toString(); // 类名称
						String attribute = jsonObj.get("attribute").toString(); // 属性名称
						String desc = jsonObj.getString("desc"); // 属性描述
						String indexStr = jsonObj.getString("indexStr");	//顺序号:只对技术规范、计量标准、标准器具有效

						map.clear();
						map.put("desc", desc);
						map.put("tagName", tagName);
						map.put("indexStr", indexStr);
						xml.writeElement(map, attribute);

					}
					// xml.endElement("certificate");

				} else {
					throw new Exception("字段信息为空");
				}
				map.clear();

				xml.endDocument();
				String downloadFile1Name = "检定证书.xml";
				if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) // firefox浏览器
					downloadFile1Name = new String(downloadFile1Name.getBytes("UTF-8"), "ISO8859-1");
				else
					// if(req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) //IE浏览器
					downloadFile1Name = URLEncoder.encode(downloadFile1Name,"UTF-8");

				// 向浏览器发消息头： 内容-设置,附件;文件名=(如果中文文件名) URLEncoader.encode() 中文编码
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
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
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

	public static int[] statrowcol(String a) { // 统计行、列数
		int[] result = new int[2];
		a = a.toUpperCase(); // 小写字母转大写
		char[] ch = a.toCharArray();
		int m = 0, n = 0; // m为列，n为行
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
