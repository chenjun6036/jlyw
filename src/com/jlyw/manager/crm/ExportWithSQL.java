/**
 * 
 */
package com.jlyw.manager.crm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jlyw.hibernate.BaseHibernateDAO;
import com.jlyw.util.UIDUtil;

/**
 * @author xx
 *
 */
public class ExportWithSQL {
	
	/**
	 * 导出到Excel，生成临时文件，返回路径，用于下载
	 * @param queryStr  查询hql语句
	 * @param keys		查询条件
	 * @param myTitle   标题等其他信息，若没有则，传入null或空列表
	 * @param className 对应的Manager类，用于反向获取对应的format函数，控制表格表头和数据对应
	 * @return    临时文件路径，用于下载，若导出错误则返回空字符串
	 * @throws Exception
	 */
	public List<String> format(ResultSet r) {
		List<String> ret=new ArrayList<String>();
		
		return ret;
		
	}
	public static String ExportToExcel(String queryStr, List<Object> keys,String formatTitleFun,String formatExcelFun,Class c) throws Exception{
		
		Method formatExcel,formatTitle;
		Sql sql=new Sql();
		Connection conn=sql.getMyConn();
		PreparedStatement ps1=conn.prepareStatement(queryStr);
		int pi=1;
		File f = null;
		FileOutputStream os = null;
		try 
		{
			formatExcel=c.getDeclaredMethod(formatExcelFun, ResultSet.class);
			formatTitle=c.getDeclaredMethod(formatTitleFun);
			List<String> title = (List<String>) formatTitle.invoke(c.newInstance());

			for(Object a:keys)
			{
				ps1.setString(pi++, a.toString());
			}
			ResultSet rs1=ps1.executeQuery();
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row;
			HSSFCell cell;
			ResultSetMetaData rsmd = rs1.getMetaData() ; 
			int size = rsmd.getColumnCount();
			rs1.getMetaData();
			int k=0;
			//rs1.beforeFirst();
			row = sheet.createRow(k);
			for(int i = 0; i < title.size();i++)
			{
				cell = row.createCell(i);
				cell.setCellValue(title.get(i));
			}
			k++;
			
			while(rs1.next())
			{
				if(k==10000)
				{//数据量大于60000则另新建一个sheet
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
				List<String> excel = (List<String>)formatExcel.invoke(c.newInstance(),rs1 );
				for(int i = 0; i<size; i++)
				{
					cell = row.createCell(i);
					
					try 
					{
						//String tmpString=rs1.getObject(i+1).toString();
						cell.setCellValue(excel.get(i));
						
					} 
					catch (Exception e) {System.out.println("ExportWithSQL.ExportToExcel:"+i);
						// TODO: handle exception
					}
					
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
