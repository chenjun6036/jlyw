package com.jlyw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelCompareUtil {

	public static void CompareExcel(HSSFSheet sheet, HSSFSheet sheetOld, String UserName) throws Exception{
	
		int rowLen = sheet.getLastRowNum();
		int rowLenOld = sheetOld.getLastRowNum();
		int rLen = rowLen >= rowLenOld ? rowLen:rowLenOld;
		
		Timestamp today = new Timestamp(System.currentTimeMillis());
				
		for(int i = 0; i < rLen; i++){
			int colLen = sheet.getRow(i).getLastCellNum();
			int colLenOld = sheetOld.getRow(i).getLastCellNum();
			int cLen = colLen >= colLenOld ? colLen: colLenOld;
			for(int j = 0; j < cLen; j++){
				Object obj = getCellValue(sheet, i, j);
				Object objOld = getCellValue(sheetOld, i, j);
				
				String commentStr = "";
				HSSFCell cell = sheet.getRow(i).getCell(j);
				
				if(cell==null)
					continue;
				if(obj==null&&objOld==null){
					continue;
				}else if(obj==null&&objOld!=null){
					commentStr = String.format("%s于%s将值从{%s}修改为<空白>。\n", UserName, today.toLocaleString(), objOld.toString());
				}else if(obj!=null&&objOld==null){
					commentStr = String.format("%s于%s将值从<空白>修改为{%s}。\n", UserName, today.toLocaleString(), obj.toString());
				}else if(!obj.equals(objOld)){
					commentStr = String.format("%s于%s将值从{%s}修改为{%s}。\n", UserName, today.toLocaleString(), objOld.toString(), obj.toString());
				}else{
					continue;
				}
				
				HSSFComment comment = cell.getCellComment();
				if(comment==null){
					HSSFPatriarch p = sheet.createDrawingPatriarch();
					comment = p.createCellComment(new HSSFClientAnchor(0, 0, 0, 0,(short)35, 15, (short)5, 6));
				}

				commentStr = comment.getString().toString() + commentStr;
				comment.setString(new HSSFRichTextString(commentStr));
				comment.setAuthor(UserName);
				
				cell.setCellComment(comment);
			}
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File f = new File("d:\\c.xls");
			InputStream is = new FileInputStream("d:\\a.xls");
			InputStream isOld = new FileInputStream("d:\\b.xls");
			
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			HSSFWorkbook workbookOld = new HSSFWorkbook(isOld);
			
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFSheet sheetOld = workbookOld.getSheetAt(0);
			OutputStream os = new FileOutputStream(f);
			ExcelCompareUtil.CompareExcel(sheet, sheetOld, "徐晓峰");

			workbook.write(os);
			os.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 从Excel表中获取单元格的值
	 * @param sheet
	 * @param attributes
	 * @return
	 * @throws Exception
	 */
	private static Object getCellValue(HSSFSheet sheet, int rowIndex, int colIndex){
		
			HSSFRow row = sheet.getRow(rowIndex);	//获取行
			HSSFCell cell = row.getCell(colIndex);//获取列
			
			if(cell==null)
				return null;
			
			int cellType = cell.getCellType();
			Object cellVal = null;		//获取单元格
			
			if(cellType == HSSFCell.CELL_TYPE_NUMERIC){
				if(HSSFDateUtil.isCellDateFormatted(cell)){	//日期格式
					cellVal = new Timestamp(cell.getDateCellValue().getTime());
				}else{	//普通数字
					cellVal = cell.getNumericCellValue();
				}
			}else if(cellType == HSSFCell.CELL_TYPE_BOOLEAN){
				cellVal = cell.getBooleanCellValue();
			}else if(cellType == HSSFCell.CELL_TYPE_STRING){
				cellVal = cell.getStringCellValue();
			}
			
			return cellVal;
		
	}

}
