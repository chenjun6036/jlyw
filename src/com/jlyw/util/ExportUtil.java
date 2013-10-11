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
 * 导出Excel
 * @author niming
 *
 */
public class ExportUtil {
	/**
	 * 导出到Excel，生成临时文件，返回路径，用于下载
	 * @param queryStr  查询hql语句
	 * @param keys		查询条件
	 * @param myTitle   标题等其他信息，若没有则，传入null或空列表
	 * @param className 对应的Manager类，用于反向获取对应的format函数，控制表格表头和数据对应
	 * @return    临时文件路径，用于下载，若导出错误则返回空字符串
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
			formatExcel = aClass.getDeclaredMethod(formatExcelFun, Object.class);//反向获取formatExcel方法，数据项
			formatTitle = aClass.getDeclaredMethod(formatTitleFun);//反向获取formatTitle方法,用于生成导出Excel中的表头
			
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
				if(k==60000){//数据量大于60000则另新建一个sheet
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
			throw new Exception(String.format("导出Excel失败！：原因：%s", e.getMessage()==null?"无":e.getMessage()));
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
	 * 根据结果集导出到Excel，生成临时文件，返回路径，用于下载
	 * @param queryStr  查询hql语句
	 * @param keys		查询条件
	 * @param myTitle   标题等其他信息，若没有则，传入null或空列表
	 * @param className 对应的Manager类，用于反向获取对应的format函数，控制表格表头和数据对应
	 * @return    临时文件路径，用于下载，若导出错误则返回空字符串
	 * @throws Exception
	 */
	public static String ExportToExcelByResultSet(List<JSONObject> result, List<String> myTitle, String formatExcelFun, String formatTitleFun, Class className) throws Exception{
		File f = null;
		FileOutputStream os = null;
		Method formatExcel;
		Method formatTitle;
		try {
			Class aClass = className;
			formatExcel = aClass.getDeclaredMethod(formatExcelFun, Object.class);//反向获取formatExcel方法，数据项
			formatTitle = aClass.getDeclaredMethod(formatTitleFun);//反向获取formatTitle方法,用于生成导出Excel中的表头
			
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
				if(k==60000){//数据量大于60000则另新建一个sheet
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
			throw new Exception(String.format("导出Excel失败！：原因：%s", e.getMessage()==null?"无":e.getMessage()));
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
