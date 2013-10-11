package com.jlyw.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.me.JSONObject;

import com.jlyw.hibernate.BaseHibernateDAO;

/**
 * ����Excel
 * @author niming
 *
 */
public class ExportUtil {
	/**
	 * ������Excel��������ʱ�ļ�������·������������
	 * @param queryStr  ��ѯhql���
	 * @param keys		��ѯ����
	 * @param myTitle   �����������Ϣ����û���򣬴���null����б�
	 * @param className ��Ӧ��Manager�࣬���ڷ����ȡ��Ӧ��format���������Ʊ���ͷ�����ݶ�Ӧ
	 * @return    ��ʱ�ļ�·�����������أ������������򷵻ؿ��ַ���
	 * @throws Exception
	 */
	public static String ExportToExcel(String queryStr, List<Object> keys, List<String> myTitle, String formatExcelFun, String formatTitleFun, Class className) throws Exception{
		BaseHibernateDAO dao = new BaseHibernateDAO();
		List<Object> result = dao.findByHQL(queryStr,keys);
		File f = null;
		FileOutputStream os = null;
		Method formatExcel;
		Method formatTitle;
		try {
			Class aClass = className;
			formatExcel = aClass.getDeclaredMethod(formatExcelFun, Object.class);//�����ȡformatExcel������������
			formatTitle = aClass.getDeclaredMethod(formatTitleFun);//�����ȡformatTitle����,�������ɵ���Excel�еı�ͷ
			
			List<String> title = (List<String>) formatTitle.invoke(aClass.newInstance());
			
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row;
			HSSFCell cell;
			int k = 0;
			if(myTitle!=null&&myTitle.size()>0)
			{
				row = sheet.createRow(k);
				for(int i = 0;i<myTitle.size();i++)
				{
					cell = row.createCell(i);
					cell.setCellValue(myTitle.get(i));
				}
				k++;
			}
			row = sheet.createRow(k);
			for(int i = 0; i < title.size();i++)
			{
				cell = row.createCell(i);
				cell.setCellValue(title.get(i));
			}
			k++;
			for(int j = 0;j<result.size();j++)
			{	
				if(k==60000){//����������60000�����½�һ��sheet
					sheet = wb.createSheet();
					k=0;
					row = sheet.createRow(k);
					for(int i = 0; i < title.size();i++)
					{
						cell = row.createCell(i);
						cell.setCellValue(title.get(i));
					}
					k++;
				}
				row = sheet.createRow(k);
				List<String> excel = (List<String>) formatExcel.invoke(aClass.newInstance(), result.get(j));
				for(int i = 0; i<excel.size(); i++)
				{
					cell = row.createCell(i);
					//System.out.println(excel.get(i)==null?"fff":excel.get(i).toString());					
					cell.setCellValue(excel.get(i).toString());
				}
				k++;
			}
			
			f = File.createTempFile(UIDUtil.get22BitUID(), ".xls");
			os = new FileOutputStream(f);
			wb.write(os);
			os.flush();
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(String.format("����Excelʧ�ܣ���ԭ��%s", e.getMessage()==null?"��":e.getMessage()));
		}finally{
			if(os!=null){
				try {
					os.close();
					os = null;
				} catch (IOException e) {}
			}			
		}
		return f.length()>0?f.getAbsolutePath():"";
	}
	
	/**
	 * ���ݽ����������Excel��������ʱ�ļ�������·������������
	 * @param queryStr  ��ѯhql���
	 * @param keys		��ѯ����
	 * @param myTitle   �����������Ϣ����û���򣬴���null����б�
	 * @param className ��Ӧ��Manager�࣬���ڷ����ȡ��Ӧ��format���������Ʊ���ͷ�����ݶ�Ӧ
	 * @return    ��ʱ�ļ�·�����������أ������������򷵻ؿ��ַ���
	 * @throws Exception
	 */
	public static String ExportToExcelByResultSet(List<JSONObject> result, List<String> myTitle, String formatExcelFun, String formatTitleFun, Class className) throws Exception{
		File f = null;
		FileOutputStream os = null;
		Method formatExcel;
		Method formatTitle;
		try {
			Class aClass = className;
			formatExcel = aClass.getDeclaredMethod(formatExcelFun, Object.class);//�����ȡformatExcel������������
			formatTitle = aClass.getDeclaredMethod(formatTitleFun);//�����ȡformatTitle����,�������ɵ���Excel�еı�ͷ
			
			List<String> title = (List<String>) formatTitle.invoke(aClass.newInstance());
			
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row;
			HSSFCell cell;
			int k = 0;
			if(myTitle!=null&&myTitle.size()>0)
			{
				row = sheet.createRow(k);
				for(int i = 0;i<myTitle.size();i++)
				{
					cell = row.createCell(i);
					cell.setCellValue(myTitle.get(i));
				}
				k++;
			}
			row = sheet.createRow(k);
			for(int i = 0; i < title.size();i++)
			{
				cell = row.createCell(i);
				cell.setCellValue(title.get(i));
			}
			k++;
			for(int j = 0;j<result.size();j++)
			{	
				if(k==60000){//����������60000�����½�һ��sheet
					sheet = wb.createSheet();
					k=0;
					row = sheet.createRow(k);
					for(int i = 0; i < title.size();i++)
					{
						cell = row.createCell(i);
						cell.setCellValue(title.get(i));
					}
					k++;
				}
				row = sheet.createRow(k);
				List<String> excel = (List<String>) formatExcel.invoke(aClass.newInstance(), result.get(j));
				for(int i = 0; i<excel.size(); i++)
				{
					cell = row.createCell(i);
					cell.setCellValue(excel.get(i).toString());
				}
				k++;
			}
			f = File.createTempFile(UIDUtil.get22BitUID(), ".xls");
			os = new FileOutputStream(f);
			wb.write(os);
			os.flush();
		}
		catch(Exception e){
			e.printStackTrace();
			throw new Exception(String.format("����Excelʧ�ܣ���ԭ��%s", e.getMessage()==null?"��":e.getMessage()));
		}finally{
			if(os!=null){
				try {
					os.close();
					os = null;
				} catch (IOException e) {}
			}			
		}
		return f.length()>0?f.getAbsolutePath():"";
	}
	
}
