package com.jlyw.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.xml.sax.Attributes;

import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.Standard;
import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.StdTgtApp;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.TargetAppliance;
import com.jlyw.hibernate.TaskAssign;
import com.jlyw.hibernate.TgtAppSpec;
import com.jlyw.hibernate.TgtAppStdApp;
import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.manager.ApplianceAccuracyManager;
import com.jlyw.manager.ApplianceModelManager;
import com.jlyw.manager.ApplianceRangeManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.StdTgtAppManager;
import com.jlyw.manager.TargetApplianceManager;
import com.jlyw.manager.TaskAssignManager;
import com.jlyw.manager.TestLogManager;
import com.jlyw.manager.TgtAppSpecManager;
import com.jlyw.manager.TgtAppStdAppManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.xmlHandler.ParseXMLAll;

/**
 * ��ԭʼ��¼Excel���в����������෴�����ʵ��
 * ����ί�е���ԭʼ��¼Excel�ļ��У�д����ص�Excel����
 * �ϴ�ԭʼ��¼Excel�ļ�ʱ�����ж�ȡ��Ӧ��Ϣ��д�����ݿ�
 * @author Administrator
 *
 */
public class ExcelUtil {
	private static final Log log = LogFactory.getLog(ExcelUtil.class);
	private static final String FieldClassApplianceStandardName = "com.jlyw.hibernate.ApplianceStandardName";	//���߱�׼����
	private static final String FieldClassCommissionSheet = "com.jlyw.hibernate.CommissionSheet";	//ί�е�
	private static final String FieldClassOriginalRecord = "com.jlyw.hibernate.OriginalRecord";	//ԭʼ��¼
	private static final String FieldClassSysUser = "com.jlyw.hibernate.SysUser";	//��У��Ա ���� ������Ա
	private static final String FieldClassSpecification = "com.jlyw.hibernate.Specification";	//�����淶
	private static final String FieldClassStandard = "com.jlyw.hibernate.Standard";	//������׼
	private static final String FieldClassStandardAppliance = "com.jlyw.hibernate.StandardAppliance";	//��׼����
	private static final String FieldClassCertificate = "com.jlyw.hibernate.Certificate";	//֤��
	
	private static final String FieldClassDescVerify = "��У��Ա";	//��У��Ա��������Ϣ
	private static final String FieldClassDescChecker = "������Ա"; //������Ա��������Ϣ
	
	private static final String WorkType_Jianding = "�춨";	//��������
	private static final String WorkType_Jiaozhun = "У׼";
	private static final String WorkType_Jiance = "���";
	private static final String WorkType_Jianyan = "����";
	
	/**
	 * ����ԭʼ��¼Excel�ļ�
	 * @param is ԭʼ��¼ģ���ļ�������
	 * @param os �������ݺ��ԭʼ��¼�ļ������
	 * @param cSheet ί�е�����
	 * @param oRecord ԭʼ��¼����
	 * @param staff ��У��Ա
	 * @param speList �����淶�б�
	 * @param stdList ������׼�б�
	 * @param stdAppList ��׼�����б�
	 * @param certificate ��Ӧ֤��
	 * @param stdName ��׼����
	 * @param verifier ������Ա
	 * @param parser ��Excelģ���ļ���Ӧ���ֶζ���xml�ļ�������
	 * @return
	 * @throws IOException �������ʱ�׳��쳣
	 */
	public static void downloadExcel(InputStream is, OutputStream os,
			CommissionSheet cSheet, OriginalRecord oRecord, SysUser staff, 
			List<Specification> speList, List<Standard> stdList, List<StandardAppliance> stdAppList, 
			Certificate certificate, ApplianceStandardName stdName, SysUser verifier,
			ParseXMLAll parser) throws IOException, Exception{
		// ������Excel�������ļ�������
		HSSFWorkbook workbook = new HSSFWorkbook(is);
				
		//ѭ��д����Ҫ���ֶ�����
		Iterator<String> keyIterator = parser.getKeyIterator();
		Class c = null;
		Method method = null;
		Object retObj = null;
		
		//�����淶��������׼����׼���߶�Ӧ
		Map<String, Specification> speMap = new HashMap<String, Specification>();	
		Map<String, Standard> stdMap = new HashMap<String, Standard>();
		Map<String, StandardAppliance> stdAppMap = new HashMap<String, StandardAppliance>();
		int curSpeIndex = 0, curStdIndex = 0, curStdAppIndex = 0;	//��ǰ��ȡ�ļ����淶/������׼/��׼���ߵ�������
		while(keyIterator.hasNext()){
			String key = keyIterator.next();
			for(int i = 0; i < parser.getQNameCount(key); i++){
				Attributes attr = parser.getAttributes(key, i);
				c = null;
				method = null;
				retObj = null;
				if(attr.getValue("field") != null && attr.getValue("field").equalsIgnoreCase("true")){	//�������ֶ�
					if(attr.getValue("type") != null && attr.getValue("type").toLowerCase().contains("w")){	//�ֶ����ͣ�д��
						// �����Թ���������á�
						HSSFSheet sheet = getSheet(workbook, attr);
						try {
							c = Class.forName(attr.getValue("fieldClass"));	//��ȡ��Ӧ�������
							method = c.getMethod(String.format("get%s", key.toString()));	//get����
							if(c.isInstance(oRecord)){
								retObj = method.invoke(oRecord);
							}else if(c.isInstance(cSheet)){
								retObj = method.invoke(cSheet);
							}else if(c.isInstance(staff)){
								if(attr.getValue("fieldClassDesc") == null || attr.getValue("fieldClassDesc").equals("��У��Ա")){	//��У��Ա
									retObj = method.invoke(staff);
								}else if(attr.getValue("fieldClassDesc").equals("������Ա") && verifier != null){	//������Ա
									retObj = method.invoke(verifier);
								}
							}else if(c.isInstance(certificate)){
								retObj = method.invoke(certificate);
							}else if(c.isInstance(stdName)){
								retObj = method.invoke(stdName);
							}else if(c == Specification.class){	//�����淶
								String indexStr = attr.getValue("indexStr");	//��ȡ˳��ţ��ڼ�����
								Specification spe = null;
								
								if(speMap.containsKey(indexStr)){
									spe = speMap.get(indexStr);
								}else if(curSpeIndex < speList.size()){
									spe = speList.get(curSpeIndex++);
									speMap.put(indexStr, spe);
								}
								if(spe != null){
									retObj = method.invoke(spe);
								}								
							}else if(c == Standard.class){	//������׼
								String indexStr = attr.getValue("indexStr");	//��ȡ˳��ţ��ڼ�����
								Standard std = null;
								if(stdMap.containsKey(indexStr)){
									std = stdMap.get(indexStr);
								}else if(curStdIndex < stdList.size()){
									std = stdList.get(curStdIndex++);
									stdMap.put(indexStr, std);
								}
								if(std != null){
									retObj = method.invoke(std);
								}
							}else if(c == StandardAppliance.class){	//��׼����
								String indexStr = attr.getValue("indexStr");	//��ȡ˳��ţ��ڼ�����
								StandardAppliance stdApp = null;
								if(stdAppMap.containsKey(indexStr)){
									stdApp = stdAppMap.get(indexStr);
								}else if(curStdAppIndex < stdAppList.size()){
									stdApp = stdAppList.get(curStdAppIndex++);
									stdAppMap.put(indexStr, stdApp);
								}
								if(stdApp != null){
									retObj = method.invoke(stdApp);
								}
							}

							if(retObj == null){
								retObj = "";
							}
							
							/**************������ֵд��Excel�ļ���*******************/
							setCellValue(sheet, attr, retObj);
							
						}catch(Exception e){
							e.printStackTrace();
							log.debug("exception in ExcelUtil->downloadExcel", e);
							throw new Exception(String.format("д���ֶ�ֵ:%s(%s) ʧ�ܣ���ȷ��ģ��xls�ļ����ֶζ���xml�ļ��Ƿ���ȷ��", 
									key.toString(),
									attr.getValue("desc")==null?"":attr.getValue("desc")));
						}
					}
				}
			}
		}
		
		//���ļ�д�������
		workbook.write(os);
		os.flush();
	}
	
	/**
	 * �ϴ�ԭʼ��¼Excel�ļ�
	 * @param is
	 * @param os
	 * @param cSheet
	 * @param oRecord
	 * @param workStaff ��/У��Ա
	 * @param certificate ��Ӧ֤��
	 * @param stdName
	 * @param speList
	 * @param stdList
	 * @param stdAppList
	 * @param veriAndAuth ���������Ȩǩ�֣����ڴ��������������
	 * @param certificateModFileName ��ģ���ļ������ƣ����ڴ��������������
	 * @param xlsParser
	 * @throws IOException
	 * @throws Exception
	 */
	public static void uploadExcel(InputStream is, OutputStream os, 
			CommissionSheet cSheet, OriginalRecord oRecord, SysUser workStaff, Certificate certificate, ApplianceStandardName stdName, 
			List<Specification> speList, List<Standard> stdList, List<StandardAppliance> stdAppList,
			VerifyAndAuthorize veriAndAuth, StringBuffer certificateModFileName,
			ParseXMLAll xlsParser) throws IOException, Exception{
		
		// ������Excel�������ļ�������
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		
		List<Attributes> handledAttrList = new ArrayList<Attributes>();	//�Ѵ�����������б�
		
		//У��ί�е�λ
		List<Attributes> attrList = xlsParser.getAttributesByPropertyValue("CustomerName", "fieldClass", FieldClassCommissionSheet);	//��֤ί�е���λ����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�����ί�е�λ����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || !obj.toString().equals(cSheet.getCustomerName())){
				throw new Exception(String.format("Excel�е�ί�е�λ:'%s'(��Ԫ��:%s)��ί�е��ϵ�ί�е�λ:'%s'��ƥ�䣡", 
						obj==null?"":obj.toString(), 
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						cSheet.getCustomerName()));
			}
		}
		//У���׼����
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassApplianceStandardName);	//��֤���߱�׼����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�������׼���ơ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || !obj.toString().equals(stdName.getName())){
				throw new Exception(String.format("Excel�еı�׼����:'%s'(��Ԫ��:%s)��ģ���ļ��ı�׼����:'%s'��ƥ�䣡",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						stdName.getName()));
			}
		}
		//��ȡ�ͺŹ�񡢲�����Χ��׼ȷ�ȵȼ��������ܼ����ߣ�
		if(xlsParser.getQNameCount("target-appliance-link-to") == 0){
			throw new Exception(String.format("�����ļ�����δ�����ܼ����߹�����(%s)������ϵģ���ļ�����Ա��", "target-appliance-link-to"));
		}
		Attributes tAppLinkAttr = xlsParser.getAttributes("target-appliance-link-to", 0);
		boolean bModelLinked = (tAppLinkAttr.getValue("model") != null && tAppLinkAttr.getValue("model").equalsIgnoreCase("true"))?true:false;	//�ܼ������Ƿ����ͺŹ���й�
		boolean bRangeLinked = (tAppLinkAttr.getValue("range") != null && tAppLinkAttr.getValue("range").equalsIgnoreCase("true"))?true:false;	//�ܼ������Ƿ��������Χ�й�
		boolean bAccuracyLinked = (tAppLinkAttr.getValue("accuracy") != null && tAppLinkAttr.getValue("accuracy").equalsIgnoreCase("true"))?true:false;	//�ܼ������Ƿ��벻ȷ�����й�
		String modelOld = oRecord.getModel(), rangeOld = oRecord.getRange(), accuracyOld = oRecord.getAccuracy();
		String model = null, range = null, accuracy = null;
		attrList = xlsParser.getAttributesByPropertyValue("Model", "fieldClass", FieldClassOriginalRecord);	//�ͺŹ��
		if(attrList.size() == 0 && bModelLinked){
			throw new Exception("���ͺŹ���Ǹ��ܼ����ߵĹ��������Excel�ļ����Ҳ������ͺŹ�񡯣�");
		}
		ApplianceModelManager modelMgr = new ApplianceModelManager();
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(bModelLinked){	//�ͺŹ���Ǹ����ߵĹ�����
				if((modelOld == null && obj != null && obj.length() > 0) ||
						(modelOld != null && (obj == null || !modelOld.equalsIgnoreCase(obj))) ){	//��֮ǰ���ͺŹ����ͬ
					//�жϸ��ܼ������Ƿ��и��ͺŹ��
					int iTempRet = modelMgr.getTotalCount(new KeyValueWithOperator("targetAppliance.id", oRecord.getTargetAppliance().getId(), "="),
							new KeyValueWithOperator("model", obj==null?"":obj, "="));
					if(iTempRet <= 0){
						throw new Exception(String.format("���ͺŹ���Ǹ��ܼ����ߵĹ����Excel�еġ��ͺŹ��:'%s'(��Ԫ��:%s) ����ѡ�����ܼ�����(��׼���ƣ�%s,�ܼ��������ƣ�%s)���ͺŹ��:'%s'��һ�£�",
								obj==null?"":obj,
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								stdName.getName(),
								oRecord.getTargetAppliance().getName(),
								modelOld==null?"":modelOld));
					}
				}
				if(model != null && !model.equalsIgnoreCase(obj==null?"":obj)){
					throw new Exception(String.format("Excel�е��жദ���ͺŹ����ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
							obj==null?"":obj,
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							model));
				}
				model = (obj==null)?"":obj;
			}else{
				model = (obj==null)?"":obj;
			}
		}
		attrList = xlsParser.getAttributesByPropertyValue("Range", "fieldClass", FieldClassOriginalRecord);	//������Χ
		if(attrList.size() == 0 && bRangeLinked){
			throw new Exception("��������Χ���Ǹ��ܼ����ߵĹ��������Excel�ļ����Ҳ�����������Χ����");
		}
		ApplianceRangeManager rangeMgr = new ApplianceRangeManager();
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(bRangeLinked){	//�ͺŹ���Ǹ����ߵĹ�����
				if((rangeOld == null && obj != null && obj.length() > 0) ||
						(rangeOld != null && (obj == null || !rangeOld.equalsIgnoreCase(obj))) ){
					//�жϸ��ܼ������Ƿ��и��ͺŹ��
					int iTempRet = rangeMgr.getTotalCount(new KeyValueWithOperator("targetAppliance.id", oRecord.getTargetAppliance().getId(), "="),
							new KeyValueWithOperator("range", obj==null?"":obj, "="));
					if(iTempRet <= 0){
						throw new Exception(String.format("��������Χ���Ǹ��ܼ����ߵĹ����Excel�еġ�������Χ��:'%s'(��Ԫ��:%s) ����ѡ�����ܼ�����(��׼���ƣ�%s,�ܼ��������ƣ�%s)�Ĳ�����Χ:'%s'��һ�£�",
								obj==null?"":obj,
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								stdName.getName(),
								oRecord.getTargetAppliance().getName(),
								rangeOld==null?"":rangeOld));
					}
					
				}
				if(range != null && !range.equalsIgnoreCase(obj==null?"":obj)){
					throw new Exception(String.format("Excel�е��жദ��������Χ����ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
							obj==null?"":obj,
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							range));
				}				
				range = (obj==null)?"":obj;
			}else{
				range = (obj==null)?"":obj;
			}
		}
		attrList = xlsParser.getAttributesByPropertyValue("Accuracy", "fieldClass", FieldClassOriginalRecord);	//��ȷ����
		if(attrList.size() == 0 && bAccuracyLinked){
			throw new Exception("��׼ȷ�ȵȼ����Ǹ��ܼ����ߵĹ��������Excel�ļ����Ҳ�����׼ȷ�ȵȼ�����");
		}
		ApplianceAccuracyManager accuracyMgr = new ApplianceAccuracyManager();
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(bAccuracyLinked){	//׼ȷ�ȵȼ��Ǹ����ߵĹ�����
				if((accuracyOld == null && obj != null && obj.length() > 0) ||
						(accuracyOld != null && (obj == null || !accuracyOld.equalsIgnoreCase(obj))) ){
					//�жϸ��ܼ������Ƿ��и��ͺŹ��
					int iTempRet = accuracyMgr.getTotalCount(new KeyValueWithOperator("targetAppliance.id", oRecord.getTargetAppliance().getId(), "="),
							new KeyValueWithOperator("accuracy", obj==null?"":obj, "="));
					if(iTempRet <= 0){
						throw new Exception(String.format("��׼ȷ�ȵȼ����Ǹ��ܼ����ߵĹ����Excel�еġ�׼ȷ�ȵȼ���:'%s'(��Ԫ��:%s) ����ѡ�����ܼ�����(��׼���ƣ�%s,�ܼ��������ƣ�%s)��׼ȷ�ȵȼ�:'%s'��һ�£�",
								obj==null?"":obj,
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								stdName.getName(),
								oRecord.getTargetAppliance().getName(),
								accuracyOld==null?"":accuracyOld));
					}
					
				}
				if(accuracy != null && !accuracy.equalsIgnoreCase(obj==null?"":obj)){
					throw new Exception(String.format("Excel�е��жദ��׼ȷ�ȵȼ�����ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
							obj==null?"":obj,
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							accuracy));
				}				
				accuracy = (obj==null)?"":obj;
			}else{
				accuracy = (obj==null)?"":obj;
			}
		}
		
		//��ȡ��������
		String workType = null;
		attrList = xlsParser.getAttributesByPropertyValue("WorkType", "fieldClass", FieldClassOriginalRecord);	//��֤��������
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ������������ʡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || (!obj.equals(WorkType_Jianding) && !obj.equals(WorkType_Jiaozhun) && !obj.equals(WorkType_Jiance) && !obj.equals(WorkType_Jianyan))){
				throw new Exception(String.format("Excel�еġ��������ʡ���Ч:'%s'(��Ԫ��:%s)����ֵ����Ϊ���춨/У׼/���/���顱�е�һ��",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(workType != null && !workType.equals(obj)){
				throw new Exception(String.format("Excel�е��жദ���������ʡ���ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						workType));
			}
			workType = obj.toString();
		}
		//��ȡ�춨/У׼����
		Date workDate = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date nowDate = new Date(calendar.getTimeInMillis());	//��ǰ����
		attrList = xlsParser.getAttributesByPropertyValue("WorkDate", "fieldClass", FieldClassOriginalRecord);	//��ȡ�춨/У׼����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ������춨/У׼���ڡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null){
				if(workDate == null || workDate.compareTo(nowDate) == 0){
					workDate = nowDate;
					setCellValue(sheet, attr, workDate);	//��ֵд��Excel�ļ���
				}else{
					throw new Exception(String.format("Excel���жദ���춨/У׼���ڡ���ֵ��һ��(��Ϊ�գ���Ĭ��Ϊ����):'%s'(��Ԫ��:%s) ��  '%s' ��һ�£�",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(workDate)));
				}
			}else if(workDate != null && workDate.compareTo((Date)obj) != 0){
				throw new Exception(String.format("Excel���жദ���춨/У׼���ڡ���ֵ��һ��(��Ϊ�գ���Ĭ��Ϊ����):'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						DateTimeFormatUtil.DateFormat.format(workDate)));
			}else{
				workDate = (Date)obj;
			}
		}
		//������Ч����(����д�����жϲ��ɱȼ춨�������Ҳ��ܱ���������Ч������û����д������)
		Date validateDate = null, validateDateMax = null;
		if(oRecord.getTargetAppliance().getTestCycle() != null){	//����������Ч����
			calendar.setTime(workDate);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + oRecord.getTargetAppliance().getTestCycle());
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	//��Ч��=�춨����+�춨����-1��
			validateDateMax = new java.sql.Date(calendar.getTimeInMillis());  	//��Ч��
		}
		attrList = xlsParser.getAttributesByPropertyValue("Validity", "fieldClass", FieldClassOriginalRecord);	//��ȡ��Ч����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�������Ч���ڡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null){	//û����д������
				if(validateDate != null && (validateDateMax == null || validateDate.compareTo(validateDateMax) != 0)){
					throw new Exception(String.format("Excel���жദ����Ч���ڡ���ֵ��һ��(��Ϊ�գ���Ĭ��Ϊ�춨����+�춨����):'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(validateDate)));
				}
				validateDate = validateDateMax;
				setCellValue(sheet, attr, validateDate);	//��ֵд��Excel�ļ���
				
			}else{
				if(workDate.after((Date)obj)){
					throw new Exception(String.format("����Ч���ڡ�:'%s'(��Ԫ��:%s) �������� ���춨/У׼���ڡ�:'%s' ��",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(workDate)));
				}
//				if(validateDateMax != null && validateDateMax.before((Date)obj)){
//					throw new Exception(String.format("����Ч���ڡ�:'%s'(��Ԫ��:%s) �������� ������Ч��:'%s'��",
//							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
//							attr.getValue("cell")==null?"":attr.getValue("cell"),
//							DateTimeFormatUtil.DateFormat.format(validateDateMax)));
//				}
				if(validateDate != null && validateDate.compareTo((Date)obj) != 0){
					throw new Exception(String.format("Excel���жദ����Ч���ڡ���ֵ��һ��(��Ϊ�գ���Ĭ��Ϊ�춨����+�춨����):'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(validateDate)));
				}else{
					validateDate = (Date)obj;
				}
			} 
		}
		
		//�������ݵļ����ļ�
		attrList = xlsParser.getAttributesByPropertyValue("SpecificationCode", "fieldClass", FieldClassSpecification);	//�����淶�����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ��������ݼ����ļ��������淶���ı�š���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj != null && obj.length() > 0){
				Specification spe = getSpecificationInList(obj, speList);
				if(spe == null){
					throw new Exception(String.format("�Ҳ������μ춨/���� ���ݵļ����ļ� %s(��Ԫ��:%s)", 
							obj==null?"":obj,
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
			}
		}
		//���������׼
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassStandard);	//������׼������
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�����������׼�����ơ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj != null && obj.toString().length() > 0){
				String stdNameValue = obj, stdRange = null, stdAccuracy = null, stdCertificateCode = null;
				Date stdValidate = null;
				
				boolean isStdVirtual = SystemCfgUtil.checkStandardVirtual(stdNameValue);	//�ü�����׼�Ƿ�Ϊ����ļ�����׼
				if(isStdVirtual){
					setCellValue(sheet, attr, "");	//д�뵥Ԫ��Ϊ���ַ���
				}
				
				String indexStr = attr.getValue("indexStr");
				if(indexStr == null){
					throw new Exception(String.format("ģ�嶨���ļ�����û�ж��������׼���Ƶ�˳���(��Ԫ��%s)", attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				List<Attributes> attrTempList = xlsParser.getAttributesByPropertyValues("Range", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ļ�����׼�Ĳ�����Χ��", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdRange = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//д�뵥Ԫ��Ϊ���ַ���
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("Uncertain", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ļ�����׼��׼ȷ�ȵȼ����������������ȷ���ȣ���", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAccuracy = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//д�뵥Ԫ��Ϊ���ַ���
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("CertificateCode", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ļ�����׼��֤���ţ�", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdCertificateCode = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//д�뵥Ԫ��Ϊ���ַ���
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ļ�����׼�ġ���Ч��������", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdValidate = (objTemp==null)?null:(Date)objTemp;
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//д�뵥Ԫ��Ϊ���ַ���
					}
				}
				Standard std = getStandardInList(stdNameValue, true, 
						stdRange, stdRange==null?false:true, 
						stdAccuracy, stdAccuracy==null?false:true, 
						stdCertificateCode, stdCertificateCode==null?false:true, 
						stdValidate, stdValidate==null?false:true, 
						stdList);
				if(std == null){
					throw new Exception(String.format("�Ҳ������μ춨/���� ��ʹ�õļ�����׼(����:%s%s%s%s%s)(��Ԫ��:%s)", 
							stdNameValue,
							stdRange==null?"":",������Χ:"+stdRange,
							stdAccuracy==null?"":",��ȷ����:"+stdAccuracy,
							stdCertificateCode==null?"":",֤����:"+stdCertificateCode,
							stdValidate==null?"":",��Ч������"+DateTimeFormatUtil.DateFormat.format(stdValidate),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				if(stdValidate == null){	//��Ч��Ϊ�գ�������Excel�ļ�
					stdValidate = std.getValidDate();
					attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandard, "indexStr", indexStr);
					for(Attributes attrTemp : attrTempList){
						HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
						setCellValue(sheetTemp, attrTemp, stdValidate);
					}
				}
				//�ж���Ч���Ƿ��ڼ춨����֮��
				if(stdValidate != null && workDate.after(stdValidate)){
					throw new Exception(String.format("'�춨/У׼����(��Ϊ�գ���Ĭ��Ϊ����)��'%s' ��������ʹ�õļ�����׼(����:%s%s%s%s%s)����Ч��֮��'", 
							DateTimeFormatUtil.DateFormat.format(workDate),
							stdNameValue,
							stdRange==null?"":",������Χ:"+stdRange,
							stdAccuracy==null?"":",��ȷ����:"+stdAccuracy,
							stdCertificateCode==null?"":",֤����:"+stdCertificateCode,
							stdValidate==null?"":",��Ч������"+DateTimeFormatUtil.DateFormat.format(stdValidate)));
				}
			}
		}
		//�����׼����
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassStandardAppliance);	//��׼���ߣ�����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�������Ҫ��׼���ߵ����ơ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj != null && obj.length() > 0){
				String stdAppName = obj, stdAppRange = null, stdAppAccuracy = null, stdAppSeriaNumber = null;
				Date stdAppValidate = null;
				
				String indexStr = attr.getValue("indexStr");
				if(indexStr == null){
					throw new Exception(String.format("ģ�嶨���ļ�����û�ж��塮��Ҫ��׼���ߡ���˳���(��Ԫ��%s)", attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				List<Attributes> attrTempList = xlsParser.getAttributesByPropertyValues("Range", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ġ���Ҫ��׼���ߡ��Ĳ�����Χ��", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppRange = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("Uncertain", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ġ���Ҫ��׼���ߡ���׼ȷ�ȵȼ����������������ȷ���ȣ���", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppAccuracy = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("SeriaNumber", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ġ���Ҫ��׼���ߡ���֤���ţ�", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppSeriaNumber = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ġ���Ҫ��׼���ߡ��ġ���Ч��������", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppValidate = (objTemp==null)?null:(Date)objTemp;
				}
				StandardAppliance stdApp = getStandardApplianceInList(stdAppName, true, 
						stdAppRange, stdAppRange==null?false:true, 
						stdAppAccuracy, stdAppAccuracy==null?false:true, 
						stdAppSeriaNumber, stdAppSeriaNumber==null?false:true,
						stdAppList);
				if(stdApp == null){
					throw new Exception(String.format("�Ҳ������μ춨/���� ��ʹ�õġ���Ҫ��׼���ߡ�(����:%s%s%s%s%s)(��Ԫ��:%s)��",
							stdAppName,
							stdAppRange==null?"":",������Χ:"+stdAppRange,
							stdAppAccuracy==null?"":",��ȷ����:"+stdAppAccuracy,
							stdAppSeriaNumber==null?"":",֤����:"+stdAppSeriaNumber,
							stdAppValidate==null?"":",��Ч������"+DateTimeFormatUtil.DateFormat.format(stdAppValidate),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				if(stdAppValidate != null){	//��֤TestLog�Ƿ��и���Ч�ڵ���Դ��¼
					TestLogManager tlMgr = new TestLogManager();
					int iTlRet = tlMgr.getTotalCount(new KeyValueWithOperator("standardAppliance.id", stdApp.getId(), "="),
							new KeyValueWithOperator("status", 1, "<>"),
							new KeyValueWithOperator("validDate", stdAppValidate, "="));
					if(iTlRet == 0){
						throw new Exception(String.format("���μ춨/���� ��ʹ�õġ���Ҫ��׼���ߡ�(����:%s%s%s%s)(��Ԫ��:%s)û����Ч��Ϊ %s ����Դ��¼��",
								stdAppName,
								stdAppRange==null?"":",������Χ:"+stdAppRange,
								stdAppAccuracy==null?"":",��ȷ����:"+stdAppAccuracy,
								stdAppSeriaNumber==null?"":",֤����:"+stdAppSeriaNumber,								
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								stdAppValidate==null?"":DateTimeFormatUtil.DateFormat.format(stdAppValidate) ));
					}
				}else if(stdAppValidate == null && stdApp.getValidDate() != null){	//��׼���ߵ���Ч��Ϊ�գ���Ĭ��Ϊ�ñ�׼���ߵ�������Ч�ڣ���������Excel�ļ�
					stdAppValidate = stdApp.getValidDate();
					attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
					for(Attributes attrTemp : attrTempList){
						HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
						setCellValue(sheetTemp, attrTemp, stdAppValidate);
					}
				}
				//�ж���Ч���Ƿ��ڼ춨����֮��
				if(stdAppValidate != null && workDate.after(stdAppValidate)){
					throw new Exception(String.format("'�춨/У׼����(��Ϊ�գ���Ĭ��Ϊ����)��'%s' ��������ʹ�õġ���Ҫ��׼���ߡ�(����:%s%s%s%s%s)����Ч��֮��'", 
							DateTimeFormatUtil.DateFormat.format(workDate),
							stdAppName,
							stdAppRange==null?"":",������Χ:"+stdAppRange,
							stdAppAccuracy==null?"":",��ȷ����:"+stdAppAccuracy,
							stdAppSeriaNumber==null?"":",֤����:"+stdAppSeriaNumber,
							stdAppValidate==null?"":",��Ч������"+DateTimeFormatUtil.DateFormat.format(stdAppValidate)));
				}
			}
		}
		
		//�˶ԣ��춨/У׼��Ա��ȷ���춨/У׼��Ա �� ��������¼��
		attrList = xlsParser.getAttributesByPropertyValues("Name", "fieldClass", FieldClassSysUser, "fieldClassDesc", FieldClassDescVerify);	//��֤��У��Ա
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ������춨/У׼��Ա����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || !workStaff.getName().equalsIgnoreCase(obj)){
				throw new Exception(String.format("Excel�еġ��춨/У׼��Ա��:'%s'(��Ԫ��:%s)��ʵ�ʼ춨/У׼��Ա:'%s'��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						workStaff.getName()));
			}
		}
		//�˶ԣ�������Ա
		SysUser checkStaff = null;
		QualificationManager qfMgr = new QualificationManager();
		UserManager userMgr = new UserManager();
		List<Integer> qfTypeList = new ArrayList<Integer>();
		qfTypeList.add(FlagUtil.QualificationType.Type_Heyan);
		attrList = xlsParser.getAttributesByPropertyValues("Name", "fieldClass", FieldClassSysUser, "fieldClassDesc", FieldClassDescChecker);	//��֤������Ա
		if(attrList.size() == 0){
			throw new Exception("��Excel�������ļ����Ҳ�����������Ա����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.length() == 0) && checkStaff != null){
				throw new Exception(String.format("Excel�е��ж����������Ա����ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						checkStaff.getName()));
			}
			if(obj != null && checkStaff != null && !checkStaff.getName().equalsIgnoreCase(obj)){
				throw new Exception(String.format("Excel�е��ж����������Ա����ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						checkStaff.getName()));
			}
			if(checkStaff == null && obj != null && obj.length() > 0){
				//����Ƿ��к��������
				List<Object[]> qfRetList = qfMgr.getQualifyUsers(obj.toString(), stdName.getId(), 0, qfTypeList);
				if(qfRetList.size() == 0){
					throw new Exception(String.format("Excel�е�ָ����������Ա��:'%s'(��Ԫ��:%s) û�����߱�׼����'%s'�� �������ʣ�",
							obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							stdName.getName()));
				}
				Object[] userObj = qfRetList.get(0);
				checkStaff = userMgr.findById((Integer)userObj[0]);
				if(checkStaff != null && workStaff.getId().equals(checkStaff.getId())){
					throw new Exception(String.format("Excel�е�ָ����������Ա��:'%s'(��Ԫ��:%s) �� ���춨/У׼��Ա��:'%s'����Ϊͬһ���ˣ�",
							obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							workStaff.getName()));
				}
			}
//			setCellValue(sheet, attr, "");	//��Excel�еĺ�����Ա��Ϊ""������Ȩǩ��ͨ���Ժ�ͳһ�����루��ʡȥ��ҳ��ÿ��ѡ�������Ա����Ҫ����Excel��
		}
		//��ȡ��Ȩǩ����Ա
		SysUser authStaff = null;	//ǩ����
		if(checkStaff != null){ //������Ա��Ϊ��
			List<Object[]> qfRetList = qfMgr.getVerifyOrAuthorizeQualifyUsers(stdName.getId(), 0, FlagUtil.QualificationType.Type_Qianzi);
			if(qfRetList.size() == 0){
				throw new Exception(String.format("�Ҳ������߱�׼����'%s'��Ӧ����Ȩǩ����Ա������ϵ�������ݹ���Ա��", stdName.getName()));
			}
			Object[] userObj = qfRetList.get(0);
			authStaff = userMgr.findById((Integer)userObj[0]);
		}
		
		//��ȡ���ۡ�֤��ģ���ļ���
		String conclusion = null;	//����
		attrList = xlsParser.getAttributesByPropertyValue("Conclusion", "fieldClass", FieldClassOriginalRecord);	//��ȡ����
		if(attrList.size() == 0 && workType.equals(WorkType_Jianding)){
			throw new Exception("��Excel�ļ����Ҳ��������ۡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.trim().length() == 0) && workType.equals(WorkType_Jianding)){
				throw new Exception(String.format("Excel�еġ����ۡ�ֵ��Ч:'%s'(��Ԫ��:%s)��������һ����Чֵ��",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(conclusion != null && !conclusion.equals(obj)){
				throw new Exception(String.format("Excel�е��жദ�����ۡ���ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						conclusion));
			}
			conclusion = obj.toString();
		}
		certificateModFileName.append(getCertificateModFileName(workType, conclusion));	//��ȡ֤��ģ���ļ�����
//		certificate.setFileName(getCertificateModFileName(workType, conclusion));	//��ȡ֤��ģ���ļ�����
		
		//��ȡ��������(��������)
		String applianceName = null;
		attrList = xlsParser.getAttributesByPropertyValue("ApplianceName", "fieldClass", FieldClassOriginalRecord);	//��ȡ��������
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ������������ƣ��������ƣ�����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				if(applianceName != null && !applianceName.equals(stdName.getName())){
					throw new Exception(String.format("Excel���ж���������ƣ���Ϊ�գ���Ĭ��Ϊ��׼���ƣ���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							applianceName));
				}else{
					setCellValue(sheet, attr, stdName.getName());
					applianceName = stdName.getName();
				}
			}else if(applianceName != null && !applianceName.equals(obj)){
				throw new Exception(String.format("Excel���ж���������ƣ���Ϊ�գ���Ĭ��Ϊ��׼���ƣ���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						applianceName));
			}else{
				applianceName = obj;
			}
		}
		
		//��ȡ��������
		Integer applianceQuantity = null;
		attrList = xlsParser.getAttributesByPropertyValue("Quantity", "fieldClass", FieldClassOriginalRecord);	//��֤��������
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Integer obj = (Integer)getCellValue(sheet, attr);
			if(obj == null){
				throw new Exception(String.format("Excel�е���������:'%s'(��Ԫ��:%s)Ϊ�գ�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(obj <= 0){
				throw new Exception(String.format("Excel�е�����������Ч:'%s'(��Ԫ��:%s)Ϊ�գ�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(applianceQuantity != null && !applianceQuantity.equals(obj)){
				throw new Exception(String.format("Excel���ж������������ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						applianceQuantity.toString()));
			}
			//�ж���Ч���������Ƿ����ί�е�������������
			Integer existedNumber = 0;
			List<Long> existedCountList = new OriginalRecordManager().findByHQL("select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 ", 
					cSheet.getId());
			if(existedCountList.get(0) != null){
				existedNumber = existedCountList.get(0).intValue();
				if(oRecord.getQuantity() != null){	//��ȥ��ԭʼ��¼֮ǰ���е�����
					existedNumber -= oRecord.getQuantity();
				}
			}
			if(obj + existedNumber > cSheet.getQuantity()){
				throw new Exception(String.format("Excel�е���������'%s'(��Ԫ��:%s)��Ч:ί�е�������ԭʼ��¼����������'%s'���ó���ί�е�����������'%s'��",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						obj + existedNumber,
						cSheet.getQuantity()));
			}
			applianceQuantity = obj;
		}
		if(applianceQuantity == null){
			applianceQuantity = 1;
		}
		/****************        ���������Ϣ          ******************/
		//�춨��(���û���Ĭ���շѱ�׼�����򣬽���У�顣����շѱ�׼Ϊnull��0�������û�����Ϊ׼)
		Double testFee = null;
		Double testFeeStandard = oRecord.getTargetAppliance().getFee()==null?0:oRecord.getTargetAppliance().getFee()*applianceQuantity;	//�շѱ�׼(���޸ģ�����Ҫ����ж��Ƿ���ڱ��۵�)
		Double testFeeOld = oRecord.getTestFee();
		attrList = xlsParser.getAttributesByPropertyValue("TestFee", "fieldClass", FieldClassOriginalRecord);
		if(attrList.size() == 0){
			throw new Exception("��Excel�����ļ����Ҳ������춨�ѡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//ί�е����깤������ע��:�������޸ķ���
				testFee = testFeeOld;
				if(obj == null){	//�����÷���
					setCellValue(sheet, attr, testFee);
				}else{
					if(!obj.equals(testFeeOld==null?new Double(0):testFeeOld)){
						throw new Exception(String.format("��ί�е����깤����ע���������޸ļ춨�ѣ�Excel�м춨��:'%s'(��Ԫ��:%s) �� ԭ����('%s') ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								testFeeOld==null?0:testFeeOld));
					}
				}
			}else{	//ί�е���δ�깤�������޸ķ���
				if(obj == null){
					if(testFee != null && !testFee.equals(testFeeStandard)){
						throw new Exception(String.format("Excel���ж���춨�ѣ���Ϊ�գ���Ĭ��Ϊ��׼���ã���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								testFee));
					}else{
						testFee = testFeeStandard;
						setCellValue(sheet, attr, testFee);
					}
				}else if(testFee != null && !testFee.equals(obj)){
					throw new Exception(String.format("Excel���ж���춨�ѣ���Ϊ�գ���Ĭ��Ϊ��׼���ã���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							testFee));
				}else{
					if(testFeeStandard > 0 && !testFeeStandard.equals(obj)){
						throw new Exception(String.format("Excel�м춨�����շѱ�׼��һ��:'%s'(��Ԫ��:%s) �� ��׼����('%s') ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								testFeeStandard));
					}
					testFee = obj;
				}
			}
		}
		//����������ѣ��������������ж�����ѱ�׼��
		String repairLevel = null;
		String repairLevelOld = oRecord.getRepairLevel();
		attrList = xlsParser.getAttributesByPropertyValue("RepairLevel", "fieldClass", FieldClassOriginalRecord);
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ����������𡯣�");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//ί�е����깤������ע��
				repairLevel = repairLevelOld;
				if(obj == null){
					setCellValue(sheet, attr, repairLevel);
				}else{
					if(!obj.equalsIgnoreCase(repairLevelOld==null?"":repairLevelOld)){
						throw new Exception(String.format("��ί�е����깤����ע���������޸�������Excel��������:'%s'(��Ԫ��:%s) �� ԭ������('%s') ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairLevelOld==null?"":repairLevelOld));
					}
				}
			}else{	//ί�е�δ�깤
				if(obj == null || obj.length() == 0){
					if(repairLevel != null && !repairLevel.equals("")){
						throw new Exception(String.format("Excel���ж����������ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairLevel));
					}else{
						repairLevel = "";
					}
				}else if(repairLevel != null && !repairLevel.equals(obj)){
					throw new Exception(String.format("Excel���ж����������ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairLevel));
				}else{
					repairLevel = obj;
				}
			}
		}
		Double repairFee = null;
		Double repairFeeStandard = 0.0;		//�շѱ�׼
		Double repairFeeOld = oRecord.getRepairFee();
		if(repairLevel != null && repairLevel.equals("С")){
			repairFeeStandard = oRecord.getTargetAppliance().getSrfee()==null?0:oRecord.getTargetAppliance().getSrfee()*applianceQuantity;
		}else if(repairLevel != null && repairLevel.equals("��")){
			repairFeeStandard = oRecord.getTargetAppliance().getMrfee()==null?0:oRecord.getTargetAppliance().getMrfee()*applianceQuantity;
		}else if(repairLevel != null && repairLevel.equals("��")){
			repairFeeStandard = oRecord.getTargetAppliance().getLrfee()==null?0:oRecord.getTargetAppliance().getLrfee()*applianceQuantity;
		}
		attrList = xlsParser.getAttributesByPropertyValue("RepairFee", "fieldClass", FieldClassOriginalRecord);	//�����
		if(attrList.size() == 0){
			throw new Exception("��Excel�����ļ����Ҳ���������ѡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//ί�е����깤������ע��
				repairFee = repairFeeOld;
				if(obj == null){
					setCellValue(sheet, attr, repairFee);
				}else{
					if(!obj.equals(repairFeeOld==null?new Double(0):repairFeeOld)){
						throw new Exception(String.format("��ί�е����깤����ע���������޸�������ã�Excel���������:'%s'(��Ԫ��:%s) �� ԭ�������('%s') ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairFeeOld==null?"":repairFeeOld.toString()));
					}
				}
			}else{	//ί�е�δ�깤
				if(obj == null){
					if(repairFee != null && !repairFee.equals(repairFeeStandard)){
						throw new Exception(String.format("Excel���ж������ѣ���Ϊ�գ���Ĭ��Ϊ��׼���ã���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairFee));
					}else{
						repairFee = repairFeeStandard;
						setCellValue(sheet, attr, repairFee);
					}
				}else if(repairFee != null && !repairFee.equals(obj)){
					throw new Exception(String.format("Excel���ж������ѣ���Ϊ�գ���Ĭ��Ϊ��׼���ã���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairFee));
				}else{
					if(repairFeeStandard > 0 && !repairFeeStandard.equals(obj)){
						throw new Exception(String.format("Excel����������շѱ�׼��һ��:'%s'(��Ԫ��:%s) �� ��׼����('%s') ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairFeeStandard));
					}
					repairFee = obj;
				}
			}
		}
		//����ѡ������ϸ�����������ʱ�������ϸ����Ϊ�գ�
		Double materialFee = null;
		Double materialFeeOld = oRecord.getMaterialFee();
		attrList = xlsParser.getAttributesByPropertyValue("MaterialFee", "fieldClass", FieldClassOriginalRecord);	//�����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ���������ѡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//ί�е����깤����ע��
				materialFee = materialFeeOld;
				if(obj == null){
					setCellValue(sheet, attr, materialFee);
				}else{
					if(!obj.equals(materialFeeOld==null?new Double(0):materialFeeOld)){
						throw new Exception(String.format("��ί�е����깤����ע���������޸�����ѣ�Excel�������:'%s'(��Ԫ��:%s) �� ԭ�����('%s') ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairFeeOld==null?"":repairFeeOld.toString()));
					}
				}
			}else{	//ί�е�δ�깤
				if(obj == null){
					if(materialFee != null && materialFee != 0){
						throw new Exception(String.format("Excel���ж������ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								materialFee));
					}else{
						materialFee = 0.0;
					}
				}else if(materialFee != null && !materialFee.equals(obj)){
					throw new Exception(String.format("Excel���ж������ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							materialFee));
				}else{
					materialFee = obj;
				}
			}
		}
		String materialDetail = null;
		attrList = xlsParser.getAttributesByPropertyValue("MaterialDetail", "fieldClass", FieldClassOriginalRecord);	//�����ϸ
		if(attrList.size() == 0 && materialFee != null && materialFee > 0){
			throw new Exception("��Excel�ļ����Ҳ����������ϸ����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.trim().length() == 0) && materialFee != null && materialFee > 0){
				throw new Exception(String.format("Excel�������ϸΪ��:'%s'(��Ԫ��:%s)����������Ч����(�������'%s'���������ϸ����)��",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						materialFee));
			}else if(materialDetail != null && !materialDetail.equals(obj)){
				throw new Exception(String.format("Excel���ж�������ϸ��ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						materialDetail));
			}else{
				materialDetail = obj;
			}
		}
		//��ͨ�ѣ��ֳ����н�ͨ��
		Double carFee = null;
		Double carFeeOld = oRecord.getCarFee();
		attrList = xlsParser.getAttributesByPropertyValue("CarFee", "fieldClass", FieldClassOriginalRecord);	//��ͨ��
		if(attrList.size() == 0 && cSheet.getCommissionType() == 2){	//�ֳ����
			throw new Exception("��Excel�ļ����Ҳ�������ͨ�ѡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//ί�е����깤����ע��
				carFee = carFeeOld;
				if(obj == null){
					setCellValue(sheet, attr, carFee);
				}else{
					if(!obj.equals(carFeeOld==null?new Double(0):carFeeOld)){
						throw new Exception(String.format("��ί�е����깤����ע���������޸Ľ�ͨ�ѣ�Excel�н�ͨ��:'%s'(��Ԫ��:%s) �� ԭ��ͨ��('%s') ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								carFeeOld==null?"":carFeeOld.toString()));
					}
				}
			}else{	//ί�е�δ�깤
				if(obj == null){
					if(carFee != null && carFee != 0){
						throw new Exception(String.format("Excel���ж����ͨ�ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								carFee));
					}else{
						carFee = 0.0;
					}
				}else if(carFee != null && !carFee.equals(obj)){
					throw new Exception(String.format("Excel���ж����ͨ�ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							carFee));
				}else{
					if(obj > 0 && cSheet.getCommissionType() != 2){	//��ͨ�Ѳ�Ϊ0���Ҳ����ֳ����
						throw new Exception(String.format("��ί�е���ί����ʽ�����ֳ���⣬�����н�ͨ��'%s'(��Ԫ��:%s)��",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell")));
					}
					carFee = obj;
				}
			}
		}
		//������
		Double otherFee = null;
		Double otherFeeOld = oRecord.getOtherFee();
		if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){  //ί�е����깤����ע���������Ѳ��ɸĶ�
			otherFee = otherFeeOld;
		}
		attrList = xlsParser.getAttributesByPropertyValue("OtherFee", "fieldClass", FieldClassOriginalRecord);	//������
//		if(attrList.size() == 0){	//�ֳ����
//			throw new Exception("��Excel�ļ����Ҳ����������ѡ���");
//		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//ί�е����깤����ע��
				otherFee = otherFeeOld;
				if(obj == null){
					setCellValue(sheet, attr, otherFee);
				}else{
					if(!obj.equals(otherFeeOld==null?new Double(0):otherFeeOld)){
						throw new Exception(String.format("��ί�е����깤����ע���������޸������ѣ�Excel��������:'%s'(��Ԫ��:%s) �� ԭ������('%s') ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								otherFeeOld==null?"":otherFeeOld.toString()));
					}
				}
			}else{	//ί�е�δ�깤
				if(obj == null){
					if(otherFee != null && otherFee != 0){
						throw new Exception(String.format("Excel���ж�������ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								otherFee));
					}else{
						otherFee = 0.0;
					}
				}else if(otherFee != null && !otherFee.equals(obj)){
					throw new Exception(String.format("Excel���ж�������ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							otherFee));
				}else{
					otherFee = obj;
				}
			}
		}
		//��������ص�
		String workLocation = cSheet.getCommissionType()==2?"��������ʹ���ֳ�":"����ʵ����";
		attrList = xlsParser.getAttributesByPropertyValue("WorkLocation", "fieldClass", FieldClassOriginalRecord);
//		if(attrList.size() == 0){
//			throw new Exception("��Excel�ļ����Ҳ����������ص㡯��");
//		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			setCellValue(sheet, attr, workLocation);
		}
		
		
		//����oRecord
		oRecord.setAccuracy(accuracy);
		oRecord.setConclusion(conclusion);
		oRecord.setModel(model);
		oRecord.setRange(range);
//		oRecord.setSysUserByStaffId(workStaff);
//		oRecord.setTargetAppliance(tApp);
//		oRecord.setTaskAssign(tAssign);
		oRecord.setValidity(validateDate);
		oRecord.setWorkDate(workDate);
		oRecord.setWorkLocation(workLocation);	//�����ص�
		oRecord.setWorkType(workType);
		oRecord.setQuantity(applianceQuantity);	//��������
		oRecord.setApplianceName(applianceName);
		
		oRecord.setTestFee(testFee);
		oRecord.setRepairLevel(repairLevel);
		oRecord.setRepairFee(repairFee);
		oRecord.setMaterialFee(materialFee);
		oRecord.setMaterialDetail(materialDetail);
		oRecord.setCarFee(carFee);
		oRecord.setOtherFee(otherFee);
		
		//����veriAndAuth
		veriAndAuth.setSysUserByVerifierId(checkStaff);
		veriAndAuth.setSysUserByAuthorizerId(authStaff);
		
		//������������Ϣ
		uploadExcelWithOtherInfo(workbook, cSheet, oRecord, stdName, certificate, xlsParser, handledAttrList);
		
		//�����ܷ���
		boolean bTotalFee = false;	//�ܷ����Ƿ����
		Double totalFee = new Double(0.0);
		if(oRecord.getTestFee() != null){	//����
			bTotalFee = true;
			if(oRecord.getTestFee().intValue() != oRecord.getTestFee()){	//��С����
				oRecord.setTestFee(new Double(Math.round(oRecord.getTestFee())));
			}
			totalFee += oRecord.getTestFee();
		}
		if(oRecord.getRepairFee() != null){	//�����
			bTotalFee = true;
			if(oRecord.getRepairFee().intValue() != oRecord.getRepairFee()){	//��С����
				oRecord.setRepairFee(new Double(Math.round(oRecord.getRepairFee())));
			}
			totalFee += oRecord.getRepairFee();
		}
		if(oRecord.getMaterialFee() != null){	//���Ϸ�
			bTotalFee = true;
			if(oRecord.getMaterialFee().intValue() != oRecord.getMaterialFee()){	//��С����
				oRecord.setMaterialFee(new Double(Math.round(oRecord.getMaterialFee())));
			}
			totalFee += oRecord.getMaterialFee();
		}
		if(oRecord.getCarFee() != null){	//��ͨ��
			bTotalFee = true;
			if(oRecord.getCarFee().intValue() != oRecord.getCarFee()){	//��С����
				oRecord.setCarFee(new Double(Math.round(oRecord.getCarFee())));
			}
			totalFee += oRecord.getCarFee();
		}
		if(oRecord.getDebugFee() != null){	//���Է�
			bTotalFee = true;
			if(oRecord.getDebugFee().intValue() != oRecord.getDebugFee()){	//��С����
				oRecord.setDebugFee(new Double(Math.round(oRecord.getDebugFee())));
			}
			totalFee += oRecord.getDebugFee();
		}if(oRecord.getOtherFee() != null){	//������
			bTotalFee = true;
			if(oRecord.getOtherFee().intValue() != oRecord.getOtherFee()){	//��С����
				oRecord.setOtherFee(new Double(Math.round(oRecord.getOtherFee())));
			}
			totalFee += oRecord.getOtherFee();
		}
		if(bTotalFee){
			oRecord.setTotalFee(totalFee);
		}else{
			oRecord.setTotalFee(null);
		}
		
		//���ļ�д�������
		workbook.write(os);
		os.flush();
	}
	
	/**
	 * �ϴ�ԭʼ��¼Excel�ļ�
	 * @param in
	 * @param osFile
	 * @param cSheet
	 * @param oRecord
	 * @param workStaff
	 * @param certificate
	 * @param stdName
	 * @param speList
	 * @param stdList
	 * @param stdAppList
	 * @param veriAndAuth
	 * @param certificateModFileName
	 * @param xlsParser
	 * @throws IOException
	 * @throws Exception
	 */
	public static void uploadExcel(InputStream in, File osFile, 
			CommissionSheet cSheet, OriginalRecord oRecord, SysUser workStaff, Certificate certificate, ApplianceStandardName stdName, 
			List<Specification> speList, List<Standard> stdList, List<StandardAppliance> stdAppList,
			VerifyAndAuthorize veriAndAuth, StringBuffer certificateModFileName,
			ParseXMLAll xlsParser) throws IOException, Exception{
		OutputStream os = null;
		try{
			os = new FileOutputStream(osFile);
			uploadExcel(in, os, cSheet, oRecord, workStaff, certificate, stdName, speList, stdList, stdAppList, veriAndAuth, certificateModFileName, xlsParser);
		}catch(Exception e){
			throw e;
		}finally{
			if(os != null){
				os.close();
				os = null;
			}
		}
		
	}
	
	/**
	 * �ϴ�ԭʼ��¼Excel�ļ�
	 * @param inFile
	 * @param osFile
	 * @param cSheet
	 * @param oRecord
	 * @param workStaff
	 * @param certificate
	 * @param stdName
	 * @param speList
	 * @param stdList
	 * @param stdAppList
	 * @param veriAndAuth
	 * @param certificateModFileName
	 * @param xlsParser
	 * @throws IOException
	 * @throws Exception
	 */
	public static void uploadExcel(File inFile, File osFile, 
			CommissionSheet cSheet, OriginalRecord oRecord, SysUser workStaff, Certificate certificate, ApplianceStandardName stdName, 
			List<Specification> speList, List<Standard> stdList, List<StandardAppliance> stdAppList,
			VerifyAndAuthorize veriAndAuth, StringBuffer certificateModFileName,
			ParseXMLAll xlsParser) throws IOException, Exception{
		InputStream in = null;
		OutputStream os = null;
		try{
			in = new FileInputStream(inFile);
			os = new FileOutputStream(osFile);
			uploadExcel(in, os, cSheet, oRecord, workStaff, certificate, stdName, speList, stdList, stdAppList, veriAndAuth, certificateModFileName, xlsParser);
		}catch(Exception e){
			throw e;
		}finally{
			if(in != null){
				in.close();
				in = null;
			}
			if(os != null){
				os.close();
				os = null;
			}
		}
		
	}
	
	/**
	 * �Ӽ����淶�б��л�ȡ���֤���ŵļ����淶
	 * @param specificationCode�������淶
	 * @param speList
	 * @return
	 * @throws Exception
	 */
	private static Specification getSpecificationInList(String specificationCode, List<Specification> speList) throws Exception{
		for(Specification spe : speList){
			if(spe.getSpecificationCode().equalsIgnoreCase(specificationCode)){
				return spe;
			}
		}
		return null;
	}
	
	/**
	 * �Ӽ�����׼�б��л�ȡ��ؼ�����׼
	 * @param stdName
	 * @param bStdNameCheck Ϊtrueʱ���������׼�����ƣ�Ϊfalseʱ����Ҫ���������׼������
	 * @param range
	 * @param bRangeCheck
	 * @param uncertain
	 * @param bUncertainCheck
	 * @param CertificateCode
	 * @param bCertificateCodeCheck
	 * @param validDate
	 * @param bValidDateCheck
	 * @param stdList
	 * @return
	 * @throws Exception
	 */
	private static Standard getStandardInList(String stdName, boolean bStdNameCheck, 
			String range, boolean bRangeCheck,
			String uncertain, boolean bUncertainCheck, 
			String CertificateCode, boolean bCertificateCodeCheck,
			Date validDate, boolean bValidDateCheck,
			List<Standard> stdList) throws Exception{
		for(Standard std : stdList){
			if((!bStdNameCheck || (bStdNameCheck && std.getName().equalsIgnoreCase(stdName))) && 
					(!bRangeCheck || (bRangeCheck && std.getRange().equalsIgnoreCase(range))) &&
					(!bUncertainCheck || (bUncertainCheck && std.getUncertain().equalsIgnoreCase(uncertain))) &&
					(!bCertificateCodeCheck || (bCertificateCodeCheck && std.getCertificateCode().equalsIgnoreCase(CertificateCode))) &&
					(!bValidDateCheck || (bValidDateCheck && validDate != null && std.getValidDate().compareTo(validDate) == 0)) ){
				return std;
			}
		}
		return null;
	}
	/**
	 * �ӱ�׼�����б��л�ȡ��ر�׼����
	 * @param stdAppName
	 * @param bStdAppNameCheck Ϊtrueʱ�����׼���ߵ����ƣ�Ϊfalseʱ����Ҫ�����׼���ߵ�����
	 * @param range
	 * @param bRangeCheck
	 * @param uncertain
	 * @param bUncertainCheck
	 * @param CertificateCode
	 * @param bCertificateCodeCheck
	 * @param stdAppList
	 * @return
	 * @throws Exception
	 */
	private static StandardAppliance getStandardApplianceInList(String stdAppName, boolean bStdAppNameCheck, 
			String range, boolean bRangeCheck,
			String uncertain, boolean bUncertainCheck, 
			String CertificateCode, boolean bCertificateCodeCheck, 
			List<StandardAppliance> stdAppList) throws Exception{
		
		for(StandardAppliance stdApp : stdAppList){
			if((!bStdAppNameCheck || (bStdAppNameCheck && stdApp.getName().equalsIgnoreCase(stdAppName))) && 
					(!bRangeCheck || (bRangeCheck && stdApp.getRange().equalsIgnoreCase(range))) &&
					(!bUncertainCheck || (bUncertainCheck && stdApp.getUncertain().equalsIgnoreCase(uncertain))) &&
					(!bCertificateCodeCheck || (bCertificateCodeCheck && stdApp.getSeriaNumber().equalsIgnoreCase(CertificateCode))) ){
				return stdApp;
			}
		}
		return null;
	}
	
	/**
	 * ����ԭʼ��¼�е�֤�����ֶ�
	 * @param inFile
	 * @param osFile
	 * @param certificate
	 * @param parser
	 */
	public static void updateExcelWithCertificateCode(File inFile, File osFile, Certificate certificate, ParseXMLAll parser) throws Exception{
		InputStream in = null;
		OutputStream os = null;
		String CertificateCodeKey = "CertificateCode";
		try{
			in = new FileInputStream(inFile);
			os = new FileOutputStream(osFile);
			
			// ������Excel�������ļ�������
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			
//			// �����Թ���������á�
//			String sheetName = parser.getFirstAttribute("original-record-sheet", "sheetName");
//			if(sheetName == null){
//				throw new Exception("ԭʼ��¼XML�����ļ�δָ��ԭʼ��¼�����������sheetName");
//			}
//			HSSFSheet sheet = workbook.getSheet(sheetName);
			
			//ѭ��д��֤���ŵ��ֶ�����
			Class c = null;
			Method method = null;
			Object retObj = null;
			
			for(int i = 0; i < parser.getQNameCount(CertificateCodeKey); i++){
				c = null;
				method = null;
				retObj = null;
				
				Attributes attr = parser.getAttributes(CertificateCodeKey, i);
				
				if(attr.getValue("field") == null || 
						!attr.getValue("field").equalsIgnoreCase("true") ||
						attr.getValue("type") == null ||
						attr.getValue("fieldClass") == null ||
						!attr.getValue("fieldClass").equals("com.jlyw.hibernate.Certificate")){
					continue;
				}
				
				// �����Թ���������á�
				HSSFSheet sheet = getSheet(workbook, attr);
								
				//�������ֶ�
				if( attr.getValue("type").contains("w")){	//�ֶ����ͣ�д���ļ�
					try {
						c = Class.forName(attr.getValue("fieldClass"));	//��ȡ��Ӧ�������
						method = c.getMethod(String.format("get%s", CertificateCodeKey.toString()));	//get����
						if(c.isInstance(certificate)){
							retObj = method.invoke(certificate);
						}else {
							continue;
						}
					
						if(retObj == null){	//û���ҵ���Ӧ��get������ֵΪnull
							retObj = "";
						}
						/**************������ֵд��Excel�ļ���*******************/
						setCellValue(sheet, attr, retObj);
					}catch(Exception e){
						e.printStackTrace();
						throw new Exception(String.format("����ԭʼ��¼Excel��֤�����ֶ�ֵʧ�ܣ�д���ֶ�ֵ:%s(%s) ʧ�ܣ���ȷ��xls�ļ����ֶζ���xml�ļ��Ƿ�ƥ�䣡", 
								CertificateCodeKey.toString(),
								parser.getAttribute(CertificateCodeKey, "desc", i)==null?"":parser.getAttribute(CertificateCodeKey, "desc", i)));
					}
				}
			}
			
			//���ļ�д�������
			workbook.write(os);
			os.flush();
		}catch(Exception e){
			throw e;
		}finally{
			if(in != null){
				in.close();
				in = null;
			}
			if(os != null){
				os.close();
				os = null;
			}
		}
	}
	
	/**
	 * �Ƴ�ԭʼ��¼�ϵĶ�����Ϣ��֤��ҳ�Լ����õ���Ϣ����������PDF��
	 * @param inFile
	 * @param outFile
	 * @param parser
	 */
	public static void removeAdditionalInfo(File inFile, File outFile, ParseXMLAll parser) throws Exception{
		InputStream in = null;
		OutputStream os = null;
		
		try{
			in = new FileInputStream(inFile);
			os = new FileOutputStream(outFile);
			
			// ������Excel�������ļ�������
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			
			//�Ƴ�֤��ı������ڴ�ӡ����ʾ֤������
			try{
				String certificateSheetName = parser.getFirstAttribute("certificate-sheet", "sheetName");	//��ȡ֤��ı�ǩҳ����
				workbook.removeSheetAt(workbook.getSheetIndex(certificateSheetName));
			}catch(Exception exx){
				log.debug("exception in ExcelUtil->removeAdditionalInfo->�Ƴ�֤��ı�", exx);
			}
			
			//ɾ��ԭʼ��¼�ϵĸ�����Ϣ�������ڴ�ӡ����ʾ������Ϣ����
			try{
				String additionInfoSheetName = parser.getFirstAttribute("addition-info-region", "sheetName");	//������Ϣ����ı�ǩҳ����
				String regionBeginColIndex = parser.getFirstAttribute("addition-info-region", "regionBeginColIndex");
				String regionEndColIndex = parser.getFirstAttribute("addition-info-region", "regionEndColIndex");
				if(additionInfoSheetName != null && regionBeginColIndex != null && regionEndColIndex != null){
					HSSFSheet sheet = workbook.getSheet(additionInfoSheetName);
					for(int i = Integer.parseInt(regionBeginColIndex); i <= Integer.parseInt(regionEndColIndex); i++){
						sheet.setColumnHidden(i, true);	//����������
					}
				}
			}catch(Exception exx){
				log.debug("exception in ExcelUtil->removeAdditionalInfo->���ظ�����Ϣ����", exx);
			}
			
			//���ļ�д�������
			workbook.write(os);
			os.flush();
		}catch(Exception e){
			throw e;
		}finally{
			if(in != null){
				in.close();
				in = null;
			}
			if(os != null){
				os.close();
				os = null;
			}
		}
	}
	
	/**
	 * ��ԭʼ��¼Excel�в����/У��Ա�ͺ�����Ա��ǩ��ͼƬ����֤����Ȩǩ�ֺ���룩
	 * ������ú��ж����    (outFile.exists()==false || outFile.length() == 0)����˵��û�в���ͼƬ������˵���Ѳ���ͼƬ��������һ��ԭʼ��¼Excel�ļ�
	 * @param inFile
	 * @param outFile
	 * @param fImgVerify ���춨��Աǩ��ͼƬ�ļ�
	 * @param fImgChecker:������Աǩ��ͼƬ�ļ�
	 * @param parser
	 */
	public static void insertWorkerSignatureImgToExcel(File inFile, File outFile, File fImgVerify, File fImgChecker, ParseXMLAll parser) throws Exception{
		InputStream in = null;
		OutputStream os = null;
		BufferedImage bufferImg = null;
		ByteArrayOutputStream byteArrayVerify = null, byteArrayChecker = null;
		
		boolean isInserted = false;	//�Ƿ��Ѿ�ǩ��
		final String keyName = "Name";	//��ǩ�������ƣ���У��Ա��������
		final String fieldClass = "com.jlyw.hibernate.SysUser";	//�����
		final String VerifyClassDesc = "��У��Ա";
		final String CheckerClassDesc = "������Ա";
		double imgHeightVerify = 0.0, imgHeightChecker = 0.0;	//��/У��Ա���˶���Աǩ��ͼƬ�ĸ߶�(�԰�Ϊ��λ��3��=4����)
		try{
			if(fImgVerify != null && fImgVerify.exists() && fImgVerify.length() > 0){
				//�ȰѶ�������ͼƬ�ŵ�һ��ByteArrayOutputStream�У��Ա����ByteArray  
				byteArrayVerify = new ByteArrayOutputStream();
				bufferImg = ImageIO.read(fImgVerify);
				imgHeightVerify = bufferImg.getHeight() * 0.75;	//ת��Ϊ�԰�Ϊ��λ
				ImageIO.write(bufferImg, "png", byteArrayVerify);
			}
			
			if(fImgChecker != null && fImgChecker.exists() && fImgChecker.length() > 0){
				//�ȰѶ�������ͼƬ�ŵ�һ��ByteArrayOutputStream�У��Ա����ByteArray  
				byteArrayChecker = new ByteArrayOutputStream();
				bufferImg = ImageIO.read(fImgChecker);
				imgHeightChecker = bufferImg.getHeight() * 0.75; //ת��Ϊ�԰�Ϊ��λ
				ImageIO.write(bufferImg, "png", byteArrayChecker);
			}
			
			in = new FileInputStream(inFile);
			os = new FileOutputStream(outFile);
			
			// ������Excel�������ļ�������
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			
			//ѭ���ж��Ƿ��м�У��Ա�������Ա �������� ���ֶ�����

			Object retObj = null;
			for(int i = 0; i < parser.getQNameCount(keyName); i++){
				retObj = null;
				
				Attributes attr = parser.getAttributes(keyName, i);
				
				if(attr.getValue("field") == null || 
						!attr.getValue("field").equalsIgnoreCase("true") ||
						attr.getValue("type") == null ||
						attr.getValue("fieldClass") == null ||
						!attr.getValue("fieldClass").equals(fieldClass) ||
						attr.getValue("fieldClassDesc") == null ||
						(!attr.getValue("fieldClassDesc").equals(CheckerClassDesc) && !attr.getValue("fieldClassDesc").equals(VerifyClassDesc))){
					continue;
				}
				
				// �����Թ���������á�
				HSSFSheet sheet = getSheet(workbook, attr);
				HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
				
				//�������ֶ�
				if(attr.getValue("type").contains("w")){	//�ֶ����ͣ�д���ļ�
					try {
						if(attr.getValue("fieldClassDesc").equals(VerifyClassDesc) && byteArrayVerify != null){
							retObj = "";
							//�Ƚ���У��Ա�Ķ�Ӧλ��д����ַ���ȥ����ӡ�� ���֣�
							setCellValue(sheet, attr, retObj);
													
							//����ͼƬ
							int rowIndex = Integer.parseInt(attr.getValue("rowIndex"));
							int colIndex = Integer.parseInt(attr.getValue("colIndex"));
							HSSFRow row = sheet.getRow(rowIndex);	//��ȡ��
							double rowHeight = row.getHeight() / 20.0;	//�и�(�԰�Ϊ��λ)
							double imgZoomScale = (rowHeight <= 0 || imgHeightVerify <= 0)?1:rowHeight*1.0/imgHeightVerify;
							HSSFClientAnchor anchor = new HSSFClientAnchor((int)rowHeight,0,0,0,(short)colIndex,rowIndex,(short)(colIndex+1),rowIndex+1);;       
//							patriarch.createPicture(anchor, workbook.addPicture(byteArrayVerify.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize(1);	//���ֱ���ԭʼ��ͼƬ��С��ͼƬ��ʾ������cellλ�þͲ���������
							patriarch.createPicture(anchor, workbook.addPicture(byteArrayVerify.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize(imgZoomScale);
							isInserted = true;
							
						}else if(attr.getValue("fieldClassDesc").equals(CheckerClassDesc) && byteArrayChecker != null){
							retObj = "";
							//�Ƚ�������Ա�Ķ�Ӧλ��д����ַ���ȥ����ӡ�� ���֣�
							setCellValue(sheet, attr, retObj);
							
							//����ͼƬ
							int rowIndex = Integer.parseInt(attr.getValue("rowIndex"));
							int colIndex = Integer.parseInt(attr.getValue("colIndex"));
							HSSFRow row = sheet.getRow(rowIndex);	//��ȡ��
							double rowHeight = row.getHeight() / 20.0;	//�и�(�԰�Ϊ��λ)
							double imgZoomScale = (rowHeight <= 0 || imgHeightChecker <= 0)?1:rowHeight*1.0/imgHeightChecker;							
							HSSFClientAnchor anchor = new HSSFClientAnchor((int)rowHeight,0,0,0,(short)colIndex,rowIndex,(short)(colIndex+1),rowIndex+1);;       
//							patriarch.createPicture(anchor, workbook.addPicture(byteArrayChecker.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize(1);	//���ֱ���ԭʼ��ͼƬ��С��ͼƬ��ʾ������cellλ�þͲ���������
							patriarch.createPicture(anchor, workbook.addPicture(byteArrayChecker.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize(imgZoomScale);
							isInserted = true;
						}
					}catch(Exception e){
						e.printStackTrace();
						throw new Exception(String.format("��ԭʼ��¼Excel�в���ǩ��ͼƬ���󣡴�����Ϣ��%s",e.getMessage()==null?"��":e.getMessage()));
					}
				}
			}
			if(isInserted){				
				//�Ƴ�֤��ı������ڴ�ӡ����ʾ֤������
				try{
					String certificateSheetName = parser.getFirstAttribute("certificate-sheet", "sheetName");	//��ȡ֤��ı�ǩҳ����
					workbook.removeSheetAt(workbook.getSheetIndex(certificateSheetName));
					isInserted = true;
				}catch(Exception exx){
					log.debug("exception in ExcelUtil->insertWorkerSignatureImgToExcel->�Ƴ�֤��ı�", exx);
				}
				//ɾ��ԭʼ��¼�ϵĸ�����Ϣ�������ڴ�ӡ����ʾ������Ϣ����
				try{
					String additionInfoSheetName = parser.getFirstAttribute("addition-info-region", "sheetName");	//������Ϣ����ı�ǩҳ����
					String regionBeginColIndex = parser.getFirstAttribute("addition-info-region", "regionBeginColIndex");
					String regionEndColIndex = parser.getFirstAttribute("addition-info-region", "regionEndColIndex");
					if(additionInfoSheetName != null && regionBeginColIndex != null && regionEndColIndex != null){
						HSSFSheet sheet = workbook.getSheet(additionInfoSheetName);
						for(int i = Integer.parseInt(regionBeginColIndex); i <= Integer.parseInt(regionEndColIndex); i++){
							sheet.setColumnHidden(i, true);	//����������
						}
						isInserted = true;
					}
				}catch(Exception exx){
					log.debug("exception in ExcelUtil->insertWorkerSignatureImgToExcel->���ظ�����Ϣ����", exx);
				}
				
				//���ļ�д�������
				workbook.write(os);
				os.flush();				
				
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(in != null){
				in.close();
				in = null;
			}
			if(os != null){
				os.close();
				os = null;
			}
			if(byteArrayVerify != null){
				byteArrayVerify.close();
				byteArrayVerify = null;
			}
			if(byteArrayChecker != null){
				byteArrayChecker.close();
				byteArrayChecker = null;
			}
		}
	}
	
//	public static void main(String[] args){
//		try{
//			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("D:\\Test.xls"));
//			workbook.removeSheetAt(workbook.getSheetIndex("֤��"));
//			HSSFSheet sheet = workbook.getSheet("��¼");
//			for(int i = 0; i < 10; i++){
//				sheet.setColumnHidden(i, true);	//����������
//			}
//			workbook.write(new FileOutputStream("D:\\TestOut.xls"));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}
	
	
	/**
	 * �����ϴ�ԭʼ��¼Excel�ļ�
	 * ����OriginalRecord��Ӧ���ֶΣ������ļ��ж�������д��Excel�ļ���
	 * ����ί�е�����У��Ա��Ӧ���ֶΣ�������Ҫ��д��Excel�ļ�(����Excel�ļ��ж�ȡд�����ݿ�)
	 * @param is
	 * @param os
	 * @param cSheet
	 * @param oRecord
	 * @param staff ��/У��Ա
	 * @param certificate ��Ӧ֤��
	 * @param xlsParser
	 * @param oRecord:ԭʼ��¼�����ڴ��������Ϣ��������ʼִ��ʱoRecord��IDΪnull��
	 * @param certificate:֤�飨����Ԥ��֤�����Լ���֤���ģ���ļ����ƴӷ���ִ�к���أ�������ʼִ��ʱcertificate��IDΪnull��
	 * @param verAndAuth:����� ��Ȩǩ�ּ�¼�����ڴ��������Ϣ��������ʼִ��ʱverAndAuth��IDΪnull��
	 * @param specMap�������淶Map�����ڴ���������ݣ�Integer��ŵ�ΪSpecification��ID��
	 * @param stdMap:������׼Map�����ڴ���������ݣ�Integer��ŵ�ΪStandard��ID��
	 * @param stdAppMap����׼����Map�����ڴ���������ݣ�Integer��ŵ�ΪStandardAppliance��ID��
	 * @return 
	 * @throws IOException �������ʱ�׳��쳣
	 */
	public static void uploadExcelByBatch(InputStream is, OutputStream os, 
			CommissionSheet cSheet, ApplianceStandardName stdName, 
			ParseXMLAll xlsParser, 
			OriginalRecord oRecord, Certificate certificate, VerifyAndAuthorize veriAndAuth, 
			Map<Integer, Specification> specMap, Map<Integer, Standard> stdMap, Map<Integer, StandardAppliance> stdAppMap) throws Exception{
		
		// ������Excel�������ļ�������
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		
		List<Attributes> handledAttrList = new ArrayList<Attributes>();	//�Ѵ�����������б�
		
		//У��ί�е�λ
		List<Attributes> attrList = xlsParser.getAttributesByPropertyValue("CustomerName", "fieldClass", FieldClassCommissionSheet);	//��֤ί�е���λ����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�����ί�е�λ����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || !obj.toString().equals(cSheet.getCustomerName())){
				throw new Exception(String.format("Excel�е�ί�е�λ:'%s'(��Ԫ��:%s)��ί�е��ϵ�ί�е�λ:'%s'��ƥ�䣡", 
						obj==null?"":obj.toString(), 
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						cSheet.getCustomerName()));
			}
		}
		//У���׼����
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassApplianceStandardName);	//��֤���߱�׼����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�������׼���ơ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || !obj.toString().equals(stdName.getName())){
				throw new Exception(String.format("Excel�еı�׼����:'%s'(��Ԫ��:%s)��ģ���ļ��ı�׼����:'%s'��ƥ�䣡",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						stdName.getName()));
			}
		}
		//��ȡ�ͺŹ�񡢲�����Χ��׼ȷ�ȵȼ��������ܼ����ߣ�
		if(xlsParser.getQNameCount("target-appliance-link-to") == 0){
			throw new Exception(String.format("�����ļ�����δ�����ܼ����߹�����(%s)������ϵģ���ļ�����Ա��", "target-appliance-link-to"));
		}
		Attributes tAppLinkAttr = xlsParser.getAttributes("target-appliance-link-to", 0);
		boolean bModelLinked = (tAppLinkAttr.getValue("model") != null && tAppLinkAttr.getValue("model").equalsIgnoreCase("true"))?true:false;	//�ܼ������Ƿ����ͺŹ���й�
		boolean bRangeLinked = (tAppLinkAttr.getValue("range") != null && tAppLinkAttr.getValue("range").equalsIgnoreCase("true"))?true:false;	//�ܼ������Ƿ��������Χ�й�
		boolean bAccuracyLinked = (tAppLinkAttr.getValue("accuracy") != null && tAppLinkAttr.getValue("accuracy").equalsIgnoreCase("true"))?true:false;	//�ܼ������Ƿ��벻ȷ�����й�
		String model = null, range = null, accuracy = null;
		attrList = xlsParser.getAttributesByPropertyValue("Model", "fieldClass", FieldClassOriginalRecord);	//�ͺŹ��
		if(attrList.size() == 0 && bModelLinked){
			throw new Exception("���ͺŹ���Ǹ��ܼ����ߵĹ��������Excel�ļ����Ҳ������ͺŹ�񡯣�");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && model != null && !model.equals(obj)){
				throw new Exception(String.format("Excel�е��жദ�ͺŹ����ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						model));
			}else{
				model = (obj==null)?null:obj.toString();
			}
		}
		attrList = xlsParser.getAttributesByPropertyValue("Range", "fieldClass", FieldClassOriginalRecord);	//������Χ
		if(attrList.size() == 0 && bRangeLinked){
			throw new Exception("��������Χ���Ǹ��ܼ����ߵĹ��������Excel�ļ����Ҳ�����������Χ����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && range != null && !range.equals(obj)){
				throw new Exception(String.format("Excel�е��жദ������Χ��ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						range));
			}else{
				range = (obj==null)?null:obj.toString();
			}
		}
		attrList = xlsParser.getAttributesByPropertyValue("Accuracy", "fieldClass", FieldClassOriginalRecord);	//��ȷ����
		if(attrList.size() == 0 && bAccuracyLinked){
			throw new Exception("��׼ȷ�ȵȼ����Ǹ��ܼ����ߵĹ��������Excel�ļ����Ҳ�����׼ȷ�ȵȼ�����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && accuracy != null && !accuracy.equals(obj)){
				throw new Exception(String.format("Excel�е��жദ׼ȷ�ȵȼ���ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						accuracy));
			}else{
				accuracy = (obj==null)?null:obj.toString();
			}
		}
		TargetApplianceManager tAppMgr = new TargetApplianceManager();
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(stdName.getId());
		String queryString = "from TargetAppliance as model where model.applianceStandardName.id=? ";
		if(bModelLinked){
			queryString += " and model.id in (select m.targetAppliance.id from ApplianceModel as m where model.id=m.targetAppliance.id and m.model=?) ";
			paramList.add(model);
		}
		if(bRangeLinked){
			queryString += " and model.id in (select r.targetAppliance.id from ApplianceRange as r where model.id=r.targetAppliance.id and r.range=?) ";
			paramList.add(range);
		}
		if(bAccuracyLinked){
			queryString += " and model.id in (select a.targetAppliance.id from ApplianceAccuracy as a where model.id=a.targetAppliance.id and a.accuracy=?) ";
			paramList.add(accuracy);
		}
		List<TargetAppliance> tAppList = tAppMgr.findPageAllByHQL(queryString, 1, 5, paramList);
		if(tAppList.size() == 0){
			throw new Exception(String.format("�Ҳ��������������ܼ����ߣ���׼����:%s%s%s%s", 
					stdName.getName(),
					bModelLinked?",�ͺŹ��:"+(model==null?"":model):"",
					bRangeLinked?",������Χ:"+(range==null?"":range):"",
					bAccuracyLinked?",׼ȷ�ȵȼ�:"+(accuracy==null?"":accuracy):""));
		}
		if(tAppList.size() > 1){
			throw new Exception(String.format("�ҵ� %s �������������ܼ����ߣ���׼����:%s%s%s%s", 
					tAppList.size(),
					stdName.getName(),
					bModelLinked?",�ͺŹ��:"+(model==null?"":model):"",
					bRangeLinked?",������Χ:"+(range==null?"":range):"",
					bAccuracyLinked?",׼ȷ�ȵȼ�:"+(accuracy==null?"":accuracy):""));
		}
		TargetAppliance tApp = tAppList.get(0);	//�ܼ�����
		
		//��ȡ��������
		String workType = null;
		attrList = xlsParser.getAttributesByPropertyValue("WorkType", "fieldClass", FieldClassOriginalRecord);	//��֤��������
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ������������ʡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || (!obj.toString().equals(WorkType_Jianding) && !obj.toString().equals(WorkType_Jiaozhun) && !obj.toString().equals(WorkType_Jiance) && !obj.toString().equals(WorkType_Jianyan))){
				throw new Exception(String.format("Excel�еġ��������ʡ���Ч:'%s'(��Ԫ��:%s)����ֵ����Ϊ���춨/У׼/���/���顱�е�һ��",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(workType != null && !workType.equals(obj)){
				throw new Exception(String.format("Excel�е��жദ���������ʡ���ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						workType));
			}
			workType = obj.toString();
		}
		//��ȡ�춨/У׼����
		Date workDate = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date nowDate = new Date(calendar.getTimeInMillis());	//��ǰ����
		attrList = xlsParser.getAttributesByPropertyValue("WorkDate", "fieldClass", FieldClassOriginalRecord);	//��ȡ�춨/У׼����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ������춨/У׼���ڡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null){
				if(workDate == null || workDate.compareTo(nowDate) == 0){
					workDate = nowDate;
					setCellValue(sheet, attr, workDate);	//��ֵд��Excel�ļ���
				}else{
					throw new Exception(String.format("Excel���жദ���춨/У׼���ڡ���ֵ��һ��(��Ϊ�գ���Ĭ��Ϊ����):'%s'(��Ԫ��:%s) ��  '%s' ��һ�£�",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(workDate)));
				}
			}else if(workDate != null && workDate.compareTo((Date)obj) != 0){
				throw new Exception(String.format("Excel���жദ���춨/У׼���ڡ���ֵ��һ��(��Ϊ�գ���Ĭ��Ϊ����):'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						DateTimeFormatUtil.DateFormat.format(workDate)));
			}else{
				workDate = (Date)obj;
			}
		}
		//������Ч����(����д�����жϲ��ɱȼ춨�������Ҳ��ܱ���������Ч������û����д������)
		Date validateDate = null, validateDateMax = null;
		if(tApp.getTestCycle() != null){	//����������Ч����
			calendar.setTime(workDate);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + tApp.getTestCycle());
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	//��Ч��=�춨����+�춨����-1��
			validateDateMax = new java.sql.Date(calendar.getTimeInMillis());  	//��Ч��
		}
		attrList = xlsParser.getAttributesByPropertyValue("Validity", "fieldClass", FieldClassOriginalRecord);	//��ȡ��Ч����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�������Ч���ڡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null){	//û����д������
				if(validateDate != null && (validateDateMax == null || validateDate.compareTo(validateDateMax) != 0)){
					throw new Exception(String.format("Excel���жദ����Ч���ڡ���ֵ��һ��(��Ϊ�գ���Ĭ��Ϊ�춨����+�춨����):'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(validateDate)));
				}
				validateDate = validateDateMax;
				setCellValue(sheet, attr, validateDate);	//��ֵд��Excel�ļ���
				
			}else{
				if(workDate.after((Date)obj)){
					throw new Exception(String.format("����Ч���ڡ�:'%s'(��Ԫ��:%s) �������� ���춨/У׼���ڡ�:'%s' ��",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(workDate)));
				}
//				if(validateDateMax != null && validateDateMax.before((Date)obj)){
//					throw new Exception(String.format("����Ч���ڡ�:'%s'(��Ԫ��:%s) �������� ������Ч��:'%s'��",
//							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
//							attr.getValue("cell")==null?"":attr.getValue("cell"),
//							DateTimeFormatUtil.DateFormat.format(validateDateMax)));
//				}
				if(validateDate != null && validateDate.compareTo((Date)obj) != 0){
					throw new Exception(String.format("Excel���жദ����Ч���ڡ���ֵ��һ��(��Ϊ�գ���Ĭ��Ϊ�춨����+�춨����):'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(validateDate)));
				}else{
					validateDate = (Date)obj;
				}
			} 
		}
		
		//�������ݵļ����ļ�
		TgtAppSpecManager tsMgr = new TgtAppSpecManager();
		attrList = xlsParser.getAttributesByPropertyValue("SpecificationCode", "fieldClass", FieldClassSpecification);	//�����淶�����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ��������ݼ����ļ��������淶���ı�š���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && obj.toString().length() > 0){
				List<TgtAppSpec> retList = tsMgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id", tApp.getId(), "="),
						new KeyValueWithOperator("specification.status", 1, "<>"),
						new KeyValueWithOperator("specification.specificationCode", obj.toString(), "="));
				if(retList.size() == 0){
					throw new Exception(String.format("�Ҳ����ܼ����ߣ���׼����:%s%s%s%s�������ݵļ����ļ� %s(��Ԫ��:%s)", 
							stdName.getName(),
							model==null?"":",�ͺŹ��:"+model,
							range==null?"":",������Χ:"+range,
							accuracy==null?"":",׼ȷ�ȵȼ�:"+accuracy,
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}else{
					for(TgtAppSpec tas : retList){
						specMap.put(tas.getSpecification().getId(), tas.getSpecification());
					}	
				}
			}
		}
		//���������׼
		StdTgtAppManager stMgr = new StdTgtAppManager();
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassStandard);	//������׼������
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�����������׼�����ơ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && obj.toString().length() > 0){
				String stdNameValue = obj.toString(), stdRange = null, stdAccuracy = null, stdCertificateCode = null;
				Date stdValidate = null;
				
				boolean isStdVirtual = SystemCfgUtil.checkStandardVirtual(stdNameValue);	//�Ƿ�Ϊ����ļ�����׼
				if(isStdVirtual){
					setCellValue(sheet, attr, "");	//д�뵥Ԫ��Ϊ���ַ���
				}
				
				String indexStr = attr.getValue("indexStr");
				if(indexStr == null){
					throw new Exception(String.format("ģ�嶨���ļ�����û�ж��������׼���Ƶ�˳���(��Ԫ��%s)", attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				List<Attributes> attrTempList = xlsParser.getAttributesByPropertyValues("Range", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ļ�����׼�Ĳ�����Χ��", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdRange = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//д�뵥Ԫ��Ϊ���ַ���
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("Uncertain", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ļ�����׼��׼ȷ�ȵȼ����������������ȷ���ȣ���", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAccuracy = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//д�뵥Ԫ��Ϊ���ַ���
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("CertificateCode", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ļ�����׼��֤���ţ�", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdCertificateCode = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//д�뵥Ԫ��Ϊ���ַ���
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ļ�����׼�ġ���Ч��������", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdValidate = (objTemp==null)?null:(Date)objTemp;
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//д�뵥Ԫ��Ϊ���ַ���
					}
				}
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				condList.add(new KeyValueWithOperator("targetAppliance.id", tApp.getId(), "="));
				condList.add(new KeyValueWithOperator("standard.name", stdNameValue, "="));
				if(stdRange != null){
					condList.add(new KeyValueWithOperator("standard.range", stdRange, "="));
				}
				if(stdAccuracy != null){
					condList.add(new KeyValueWithOperator("standard.uncertain", stdAccuracy, "="));
				}
				if(stdCertificateCode != null){
					condList.add(new KeyValueWithOperator("standard.certificateCode", stdCertificateCode, "="));
				}
				if(stdValidate != null){
					condList.add(new KeyValueWithOperator("standard.validDate", stdValidate, "="));
				}else{
					condList.add(new KeyValueWithOperator("standard.status", 1, "<>"));
				}
				List<StdTgtApp> retList = stMgr.findPagedAllBySort(1, 5, "standard.validDate", false, condList);
				if(retList.size() == 0){
					throw new Exception(String.format("�Ҳ����ܼ����ߣ���׼����:%s%s%s%s����ʹ�õļ�����׼(����:%s%s%s%s%s)(��Ԫ��:%s)", 
							stdName.getName(),
							model==null?"":",�ͺŹ��:"+model,
							range==null?"":",������Χ:"+range,
							accuracy==null?"":",׼ȷ�ȵȼ�:"+accuracy,
							stdNameValue,
							stdRange==null?"":",������Χ:"+stdRange,
							stdAccuracy==null?"":",��ȷ����:"+stdAccuracy,
							stdCertificateCode==null?"":",֤����:"+stdCertificateCode,
							stdValidate==null?"":",��Ч������"+DateTimeFormatUtil.DateFormat.format(stdValidate),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}else{
					stdMap.put(retList.get(0).getStandard().getId(), retList.get(0).getStandard());	//ȡ��һ������Ϊ����Ч��û���ȡ��Ч����ٵ�������¼��
					if(stdValidate == null){	//��Ч��Ϊ�գ�������Excel�ļ�
						stdValidate = retList.get(0).getStandard().getValidDate();
						attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandard, "indexStr", indexStr);
						for(Attributes attrTemp : attrTempList){
							HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
							setCellValue(sheetTemp, attrTemp, stdValidate);
						}
					}
					//�ж���Ч���Ƿ��ڼ춨����֮��
					if(stdValidate != null && workDate.after(stdValidate)){
						throw new Exception(String.format("'�춨/У׼����(��Ϊ�գ���Ĭ��Ϊ����)��'%s' ��������ʹ�õļ�����׼(����:%s%s%s%s%s)����Ч��֮��'", 
								DateTimeFormatUtil.DateFormat.format(workDate),
								stdNameValue,
								stdRange==null?"":",������Χ:"+stdRange,
								stdAccuracy==null?"":",��ȷ����:"+stdAccuracy,
								stdCertificateCode==null?"":",֤����:"+stdCertificateCode,
								stdValidate==null?"":",��Ч������"+DateTimeFormatUtil.DateFormat.format(stdValidate)));
					}
				}
			}
		}
		//�����׼����
		TgtAppStdAppManager tsAppMgr = new TgtAppStdAppManager();
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassStandardAppliance);	//��׼���ߣ�����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�������Ҫ��׼���ߵ����ơ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && obj.toString().length() > 0){
				String stdAppName = obj.toString(), stdAppRange = null, stdAppAccuracy = null, stdAppSeriaNumber = null;
				Date stdAppValidate = null;
				
				String indexStr = attr.getValue("indexStr");
				if(indexStr == null){
					throw new Exception(String.format("ģ�嶨���ļ�����û�ж��塮��Ҫ��׼���ߡ���˳���(��Ԫ��%s)", attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				List<Attributes> attrTempList = xlsParser.getAttributesByPropertyValues("Range", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ġ���Ҫ��׼���ߡ��Ĳ�����Χ��", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppRange = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("Uncertain", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ġ���Ҫ��׼���ߡ���׼ȷ�ȵȼ����������������ȷ���ȣ���", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppAccuracy = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("SeriaNumber", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ġ���Ҫ��׼���ߡ���֤���ţ�", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppSeriaNumber = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("ģ�嶨���ļ�������%s��˳�����ͬ(%s)�ġ���Ҫ��׼���ߡ��ġ���Ч��������", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//����Ѵ��������б�
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppValidate = (objTemp==null)?null:(Date)objTemp;
				}
				if(stdMap.size() == 0){
					throw new Exception(String.format("�Ҳ����ܼ����ߣ���׼����:%s%s%s%s����ʹ�õġ���Ҫ��׼���ߡ�(����:%s%s%s%s%s)(��Ԫ��:%s),ԭ��δָ��������׼��", 
							stdName.getName(),
							model==null?"":",�ͺŹ��:"+model,
							range==null?"":",������Χ:"+range,
							accuracy==null?"":",׼ȷ�ȵȼ�:"+accuracy,
							stdAppName,
							stdAppRange==null?"":",������Χ:"+stdAppRange,
							stdAppAccuracy==null?"":",��ȷ����:"+stdAppAccuracy,
							stdAppSeriaNumber==null?"":",֤����:"+stdAppSeriaNumber,
							stdAppValidate==null?"":",��Ч������"+DateTimeFormatUtil.DateFormat.format(stdAppValidate),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
//				String queryStringStdApp = "from StdStdApp as a where a.standard.status<>1 and a.standardAppliance.status<>1 " +
//					" and a.standard.id in (select b.standard.id from StdTgtApp as b where b.targetAppliance.id=?) " +
//					" and a.standardAppliance.name=? ";
//				List<Object> paramListStdApp = new ArrayList<Object>();
//				paramListStdApp.add(tApp.getId());
//				paramListStdApp.add(stdAppName);
//				if(stdAppRange != null){
//					queryStringStdApp += " and a.standardAppliance.range=? ";
//					paramListStdApp.add(stdAppRange);
//				}
//				if(stdAppAccuracy != null){
//					queryStringStdApp += " and a.standardAppliance.uncertain=? ";
//					paramListStdApp.add(stdAppAccuracy);
//				}
//				if(stdAppSeriaNumber != null){
//					queryStringStdApp += " and a.standardAppliance.seriaNumber=? ";
//					paramListStdApp.add(stdAppSeriaNumber);
//				}
//				if(stdAppValidate != null){
//					queryStringStdApp += " and a.standardAppliance.id in (select tl.standardAppliance.id from TestLog as tl where tl.standardAppliance.id=a.standardAppliance.id and tl.status<>1 and tl.validDate=?) ";
//					paramListStdApp.add(stdAppValidate);
//				}
//				queryStringStdApp += " and a.standard.id in ( ";
//				Iterator<Entry<Integer, Standard>> iterStd = stdMap.entrySet().iterator();
//				while(iterStd.hasNext()){
//					Entry<Integer, Standard> entry = iterStd.next();
//					Standard std = entry.getValue();
//					queryStringStdApp += "?";
//					paramListStdApp.add(std.getId());
//					if(iterStd.hasNext()){
//						queryStringStdApp += ",";
//					}
//				}
//				queryStringStdApp += " ) order by a.isMain desc, a.standard.id asc, a.sequence asc ";
//				
//				List<StdStdApp> retList = ssMgr.findPageAllByHQL(queryStringStdApp, 1, 10, paramListStdApp);
				
				String queryStringStdApp = "from TgtAppStdApp as a where a.targetAppliance.status<>1 and a.standardAppliance.status<>1 " +
					" and a.targetAppliance.id=? " +
					" and a.standardAppliance.name=? ";
				List<Object> paramListStdApp = new ArrayList<Object>();
				paramListStdApp.add(tApp.getId());
				paramListStdApp.add(stdAppName);
				if(stdAppRange != null){
					queryStringStdApp += " and a.standardAppliance.range=? ";
					paramListStdApp.add(stdAppRange);
				}
				if(stdAppAccuracy != null){
					queryStringStdApp += " and a.standardAppliance.uncertain=? ";
					paramListStdApp.add(stdAppAccuracy);
				}
				if(stdAppSeriaNumber != null){
					queryStringStdApp += " and a.standardAppliance.seriaNumber=? ";
					paramListStdApp.add(stdAppSeriaNumber);
				}
				if(stdAppValidate != null){
					queryStringStdApp += " and a.standardAppliance.id in (select tl.standardAppliance.id from TestLog as tl where tl.standardAppliance.id=a.standardAppliance.id and tl.status<>1 and tl.validDate=?) ";
					paramListStdApp.add(stdAppValidate);
				}
				queryStringStdApp += " order by a.id desc ";
				List<TgtAppStdApp> retList = tsAppMgr.findPageAllByHQL(queryStringStdApp, 1, 10, paramListStdApp);
				if(retList.size() == 0){
					throw new Exception(String.format("�Ҳ����ܼ����ߣ���׼����:%s%s%s%s����ָ��������׼����ʹ�õġ���Ҫ��׼���ߡ�(����:%s%s%s%s%s)(��Ԫ��:%s)", 
							stdName.getName(),
							model==null?"":",�ͺŹ��:"+model,
							range==null?"":",������Χ:"+range,
							accuracy==null?"":",׼ȷ�ȵȼ�:"+accuracy,
							stdAppName,
							stdAppRange==null?"":",������Χ:"+stdAppRange,
							stdAppAccuracy==null?"":",��ȷ����:"+stdAppAccuracy,
							stdAppSeriaNumber==null?"":",֤����:"+stdAppSeriaNumber,
							stdAppValidate==null?"":",��Ч������"+DateTimeFormatUtil.DateFormat.format(stdAppValidate),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}else{
					stdAppMap.put(retList.get(0).getStandardAppliance().getId(), retList.get(0).getStandardAppliance());	//ȡ��һ��
					if(stdAppValidate == null && retList.get(0).getStandardAppliance().getValidDate() != null){	//��Ч��Ϊ�գ�������Excel�ļ�
						stdAppValidate = retList.get(0).getStandardAppliance().getValidDate();
						attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
						for(Attributes attrTemp : attrTempList){
							HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
							setCellValue(sheetTemp, attrTemp, stdAppValidate);
						}
					}
					//�ж���Ч���Ƿ��ڼ춨����֮��
					if(stdAppValidate != null && workDate.after(stdAppValidate)){
						throw new Exception(String.format("'�춨/У׼����(��Ϊ�գ���Ĭ��Ϊ����)��'%s' ��������ʹ�õġ���Ҫ��׼���ߡ�(����:%s%s%s%s%s)����Ч��֮��'", 
								DateTimeFormatUtil.DateFormat.format(workDate),
								stdAppName,
								stdAppRange==null?"":",������Χ:"+stdAppRange,
								stdAppAccuracy==null?"":",��ȷ����:"+stdAppAccuracy,
								stdAppSeriaNumber==null?"":",֤����:"+stdAppSeriaNumber,
								stdAppValidate==null?"":",��Ч������"+DateTimeFormatUtil.DateFormat.format(stdAppValidate)));
					}
				}
			}
		}
		
		//�˶ԣ��춨/У׼��Ա��ȷ���춨/У׼��Ա �� ��������¼��
		SysUser workStaff = null;
		TaskAssign tAssign = null;	//��������¼
		TaskAssignManager tAssignMgr = new TaskAssignManager();
		attrList = xlsParser.getAttributesByPropertyValues("Name", "fieldClass", FieldClassSysUser, "fieldClassDesc", FieldClassDescVerify);	//��֤��У��Ա
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ������춨/У׼��Ա����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				throw new Exception(String.format("Excel�еġ��춨/У׼��Ա����Ч:'%s'(��Ԫ��:%s)��",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(workStaff != null && !workStaff.getName().equalsIgnoreCase(obj.toString())){
				throw new Exception(String.format("Excel�е��ж�����춨/У׼��Ա����ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						workStaff.getName()));
			}
			if(workStaff == null){
				//������������������¼
				List<TaskAssign> tAssignList = tAssignMgr.findByVarProperty(new KeyValueWithOperator("sysUserByAlloteeId.name", obj.toString(), "="),
						new KeyValueWithOperator("commissionSheet.id", cSheet.getId(), "="),
						new KeyValueWithOperator("status", 1, "<>"));
				if(tAssignList.size() == 0){
					throw new Exception(String.format("��ί�е�û��'%s'����������¼����Ҫ��������Ϊ'%s'�������", obj.toString(), obj.toString()));
				}
				tAssign = tAssignList.get(0);
				workStaff = tAssignList.get(0).getSysUserByAlloteeId();
			}
		}
		//�˶ԣ�������Ա
		SysUser checkStaff = null;
		QualificationManager qfMgr = new QualificationManager();
		UserManager userMgr = new UserManager();
		List<Integer> qfTypeList = new ArrayList<Integer>();
		qfTypeList.add(FlagUtil.QualificationType.Type_Heyan);
		attrList = xlsParser.getAttributesByPropertyValues("Name", "fieldClass", FieldClassSysUser, "fieldClassDesc", FieldClassDescChecker);	//��֤������Ա
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ�����������Ա����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				throw new Exception(String.format("Excel�еġ�������Ա��δָ��:'%s'(��Ԫ��:%s)��",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(checkStaff != null && !checkStaff.getName().equalsIgnoreCase(obj.toString())){
				throw new Exception(String.format("Excel�е��ж����������Ա����ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						checkStaff.getName()));
			}
			if(checkStaff == null){
				//����Ƿ��к��������
				List<Object[]> qfRetList = qfMgr.getQualifyUsers(obj.toString(), stdName.getId(), 0, qfTypeList);
				if(qfRetList.size() == 0){
					throw new Exception(String.format("Excel�е�ָ����������Ա��:'%s'(��Ԫ��:%s) û�����߱�׼����'%s'�� �������ʣ�",
							obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							stdName.getName()));
				}
				Object[] userObj = qfRetList.get(0);
				checkStaff = userMgr.findById((Integer)userObj[0]);
				if(checkStaff != null && workStaff.getId().equals(checkStaff.getId())){
					throw new Exception(String.format("Excel�е�ָ����������Ա��:'%s'(��Ԫ��:%s) �� ���춨/У׼��Ա��:'%s'����Ϊͬһ���ˣ�",
							obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							workStaff.getName()));
				}
			}
//			setCellValue(sheet, attr, "");	//��Excel�еĺ�����Ա��Ϊ""������Ȩǩ��ͨ���Ժ�ͳһ�����루��ʡȥ��ҳ��ÿ��ѡ�������Ա����Ҫ����Excel��
		}
		//��ȡ��Ȩǩ����Ա
		SysUser authStaff = null;	//ǩ����
		List<Object[]> qfRetList = qfMgr.getVerifyOrAuthorizeQualifyUsers(stdName.getId(), 0, FlagUtil.QualificationType.Type_Qianzi);
		if(qfRetList.size() == 0){
			throw new Exception(String.format("�Ҳ������߱�׼����'%s'��Ӧ����Ȩǩ����Ա������ϵ�������ݹ���Ա��", stdName.getName()));
		}
		Object[] userObj = qfRetList.get(0);
		authStaff = userMgr.findById((Integer)userObj[0]);
		
		//��ȡ���ۡ�֤��ģ���ļ���
		String conclusion = null;	//����
		attrList = xlsParser.getAttributesByPropertyValue("Conclusion", "fieldClass", FieldClassOriginalRecord);	//��ȡ����
		if(attrList.size() == 0 && workType.equals(WorkType_Jianding)){
			throw new Exception("��Excel�ļ����Ҳ��������ۡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.trim().length() == 0) && workType.equals(WorkType_Jianding)){
				throw new Exception(String.format("Excel�еġ����ۡ�ֵ��Ч:'%s'(��Ԫ��:%s)��������һ����Чֵ��",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(conclusion != null && !conclusion.equals(obj)){
				throw new Exception(String.format("Excel�е��жദ�����ۡ���ֵ��һ��:'%s'(��Ԫ��:%s) ��  '%s'��һ�£�",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						conclusion));
			}
			conclusion = obj.toString();
		}
		certificate.setFileName(getCertificateModFileName(workType, conclusion));	//��ȡ֤��ģ���ļ�����
		
		//��ȡ��������(��������)
		String applianceName = null;
		attrList = xlsParser.getAttributesByPropertyValue("ApplianceName", "fieldClass", FieldClassOriginalRecord);	//��ȡ��������
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ������������ƣ��������ƣ�����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				if(applianceName != null && !applianceName.equals(stdName.getName())){
					throw new Exception(String.format("Excel���ж���������ƣ���Ϊ�գ���Ĭ��Ϊ��׼���ƣ���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							applianceName));
				}else{
					setCellValue(sheet, attr, stdName.getName());
					applianceName = stdName.getName();
				}
			}else if(applianceName != null && !applianceName.equals(obj)){
				throw new Exception(String.format("Excel���ж���������ƣ���Ϊ�գ���Ĭ��Ϊ��׼���ƣ���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						applianceName));
			}else{
				applianceName = obj;
			}
		}
		
		//��ȡ��������
		Integer applianceQuantity = null;
		attrList = xlsParser.getAttributesByPropertyValue("Quantity", "fieldClass", FieldClassOriginalRecord);	//��֤��������
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Integer obj = (Integer)getCellValue(sheet, attr);
			if(obj == null){
				throw new Exception(String.format("Excel�е���������:'%s'(��Ԫ��:%s)Ϊ�գ�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(obj <= 0){
				throw new Exception(String.format("Excel�е�����������Ч:'%s'(��Ԫ��:%s)Ϊ�գ�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(applianceQuantity != null && !applianceQuantity.equals(obj)){
				throw new Exception(String.format("Excel���ж������������ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						applianceQuantity.toString()));
			}
			//�ж���Ч���������Ƿ����ί�е�������������
			Integer existedNumber = 0;
			List<Long> existedCountList = new OriginalRecordManager().findByHQL("select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 ", 
					cSheet.getId());
			if(existedCountList.get(0) != null){
				existedNumber = existedCountList.get(0).intValue();
			}
			if(obj + existedNumber > cSheet.getQuantity()){
				throw new Exception(String.format("Excel�е���������'%s'(��Ԫ��:%s)��Ч:ί�е�������ԭʼ��¼����������'%s'���ó���ί�е�����������'%s'��",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						obj + existedNumber,
						cSheet.getQuantity()));
			}
			
			applianceQuantity = obj;
		}
		if(applianceQuantity == null){
			applianceQuantity = 1;
		}
		
		/****************        ���������Ϣ          ******************/
		//�춨��(���û���Ĭ���շѱ�׼�����򣬽���У�顣����շѱ�׼Ϊnull��0�������û�����Ϊ׼)
		Double testFee = null;
		Double testFeeStandard = tApp.getFee()==null?0:tApp.getFee()*applianceQuantity;	//�շѱ�׼(���޸ģ�����Ҫ����ж��Ƿ���ڱ��۵�)
		attrList = xlsParser.getAttributesByPropertyValue("TestFee", "fieldClass", FieldClassOriginalRecord);
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ������춨�ѡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(obj == null){
				if(testFee != null && !testFee.equals(testFeeStandard)){
					throw new Exception(String.format("Excel���ж���춨�ѣ���Ϊ�գ���Ĭ��Ϊ��׼���ã���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							testFee));
				}else{
					testFee = testFeeStandard;
					setCellValue(sheet, attr, testFee);
				}
			}else if(testFee != null && !testFee.equals(obj)){
				throw new Exception(String.format("Excel���ж���춨�ѣ���Ϊ�գ���Ĭ��Ϊ��׼���ã���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						testFee));
			}else{
				if(testFeeStandard > 0 && !testFeeStandard.equals(obj)){
					throw new Exception(String.format("Excel�м춨�����շѱ�׼��һ��:'%s'(��Ԫ��:%s) �� ��׼����('%s') ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							testFeeStandard));
				}
				testFee = obj;
			}
		}
		//����������ѣ��������������ж�����ѱ�׼��
		String repairLevel = null;
		attrList = xlsParser.getAttributesByPropertyValue("RepairLevel", "fieldClass", FieldClassOriginalRecord);
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ����������𡯣�");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				if(repairLevel != null && !repairLevel.equals("")){
					throw new Exception(String.format("Excel���ж����������ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairLevel));
				}else{
					repairLevel = "";
				}
			}else if(repairLevel != null && !repairLevel.equals(obj)){
				throw new Exception(String.format("Excel���ж����������ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						repairLevel));
			}else{
				repairLevel = obj;
			}
		}
		Double repairFee = null;
		Double repairFeeStandard = 0.0;		//�շѱ�׼
		if(repairLevel != null && repairLevel.equals("С")){
			repairFeeStandard = tApp.getSrfee()==null?0:tApp.getSrfee()*applianceQuantity;
		}else if(repairLevel != null && repairLevel.equals("��")){
			repairFeeStandard = tApp.getMrfee()==null?0:tApp.getMrfee()*applianceQuantity;
		}else if(repairLevel != null && repairLevel.equals("��")){
			repairFeeStandard = tApp.getLrfee()==null?0:tApp.getLrfee()*applianceQuantity;
		}
		attrList = xlsParser.getAttributesByPropertyValue("RepairFee", "fieldClass", FieldClassOriginalRecord);	//�����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ���������ѡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(obj == null){
				if(repairFee != null && !repairFee.equals(repairFeeStandard)){
					throw new Exception(String.format("Excel���ж������ѣ���Ϊ�գ���Ĭ��Ϊ��׼���ã���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairFee));
				}else{
					repairFee = repairFeeStandard;
					setCellValue(sheet, attr, repairFee);
				}
			}else if(repairFee != null && !repairFee.equals(obj)){
				throw new Exception(String.format("Excel���ж������ѣ���Ϊ�գ���Ĭ��Ϊ��׼���ã���ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						repairFee));
			}else{
				if(repairFeeStandard > 0 && !repairFeeStandard.equals(obj)){
					throw new Exception(String.format("Excel����������շѱ�׼��һ��:'%s'(��Ԫ��:%s) �� ��׼����('%s') ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairFeeStandard));
				}
				repairFee = obj;
			}
		}
		//����ѡ������ϸ�����������ʱ�������ϸ����Ϊ�գ�
		Double materialFee = null;
		attrList = xlsParser.getAttributesByPropertyValue("MaterialFee", "fieldClass", FieldClassOriginalRecord);	//�����
		if(attrList.size() == 0){
			throw new Exception("��Excel�ļ����Ҳ���������ѡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(obj == null){
				if(materialFee != null && materialFee != 0){
					throw new Exception(String.format("Excel���ж������ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							materialFee));
				}else{
					materialFee = 0.0;
				}
			}else if(materialFee != null && !materialFee.equals(obj)){
				throw new Exception(String.format("Excel���ж������ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						materialFee));
			}else{
				materialFee = obj;
			}
		}
		String materialDetail = null;
		attrList = xlsParser.getAttributesByPropertyValue("MaterialDetail", "fieldClass", FieldClassOriginalRecord);	//�����ϸ
		if(attrList.size() == 0 && materialFee != null && materialFee > 0){
			throw new Exception("��Excel�ļ����Ҳ����������ϸ����");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.trim().length() == 0) && materialFee != null && materialFee > 0){
				throw new Exception(String.format("Excel�������ϸΪ��:'%s'(��Ԫ��:%s)����������Ч���ݣ�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}else if(materialDetail != null && !materialDetail.equals(obj)){
				throw new Exception(String.format("Excel���ж�������ϸ��ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						materialDetail));
			}else{
				materialDetail = obj;
			}
		}
		//��ͨ�ѣ��ֳ����н�ͨ��
		Double carFee = null;
		attrList = xlsParser.getAttributesByPropertyValue("CarFee", "fieldClass", FieldClassOriginalRecord);	//��ͨ��
		if(attrList.size() == 0 && cSheet.getCommissionType() == 2){	//�ֳ����
			throw new Exception("��Excel�ļ����Ҳ�������ͨ�ѡ���");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(obj == null){
				if(carFee != null && carFee != 0){
					throw new Exception(String.format("Excel���ж����ͨ�ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							carFee));
				}else{
					carFee = 0.0;
				}
			}else if(carFee != null && !carFee.equals(obj)){
				throw new Exception(String.format("Excel���ж����ͨ�ѣ���Ϊ�գ���Ĭ��Ϊ0����ֵ��һ��:'%s'(��Ԫ��:%s) �� '%s' ��һ�£�",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						carFee));
			}else{
				if(obj > 0 && cSheet.getCommissionType() != 2){	//��ͨ�Ѳ�Ϊ0���Ҳ����ֳ����
					throw new Exception(String.format("��ί�е���ί����ʽ�����ֳ���⣬�����н�ͨ��'%s'(��Ԫ��:%s)��",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				carFee = obj;
			}
		}
		//��������ص�
		String workLocation = cSheet.getCommissionType()==2?"��������ʹ���ֳ�":"����ʵ����";
		attrList = xlsParser.getAttributesByPropertyValue("WorkLocation", "fieldClass", FieldClassOriginalRecord);
//		if(attrList.size() == 0){
//			throw new Exception("��Excel�ļ����Ҳ����������ص㡯��");
//		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//����Ѵ��������б�
			HSSFSheet sheet = getSheet(workbook, attr);
			setCellValue(sheet, attr, workLocation);
		}
		
		
		//����oRecord
		oRecord.setAccuracy(accuracy);
		oRecord.setConclusion(conclusion);
		oRecord.setModel(model);
		oRecord.setRange(range);
		oRecord.setSysUserByStaffId(workStaff);
		oRecord.setTargetAppliance(tApp);
		oRecord.setTaskAssign(tAssign);
		oRecord.setValidity(validateDate);
		oRecord.setWorkDate(workDate);
		oRecord.setWorkLocation(workLocation);	//�����ص�
		oRecord.setWorkType(workType);
		oRecord.setQuantity(applianceQuantity);	//��������
		oRecord.setApplianceName(applianceName);
		
		oRecord.setTestFee(testFee);
		oRecord.setRepairLevel(repairLevel);
		oRecord.setRepairFee(repairFee);
		oRecord.setMaterialFee(materialFee);
		oRecord.setMaterialDetail(materialDetail);
		oRecord.setCarFee(carFee);
		
		//����veriAndAuth
		veriAndAuth.setSysUserByVerifierId(checkStaff);
		veriAndAuth.setSysUserByAuthorizerId(authStaff);
		
		//������������Ϣ
		uploadExcelWithOtherInfo(workbook, cSheet, oRecord, stdName, certificate, xlsParser, handledAttrList);
		
		//�����ܷ���
		boolean bTotalFee = false;	//�ܷ����Ƿ����
		Double totalFee = new Double(0.0);
		if(oRecord.getTestFee() != null){	//����
			bTotalFee = true;
			if(oRecord.getTestFee().intValue() != oRecord.getTestFee()){	//��С����
				oRecord.setTestFee(new Double(Math.round(oRecord.getTestFee())));
			}
			totalFee += oRecord.getTestFee();
		}
		if(oRecord.getRepairFee() != null){	//�����
			bTotalFee = true;
			if(oRecord.getRepairFee().intValue() != oRecord.getRepairFee()){	//��С����
				oRecord.setRepairFee(new Double(Math.round(oRecord.getRepairFee())));
			}
			totalFee += oRecord.getRepairFee();
		}
		if(oRecord.getMaterialFee() != null){	//���Ϸ�
			bTotalFee = true;
			if(oRecord.getMaterialFee().intValue() != oRecord.getMaterialFee()){	//��С����
				oRecord.setMaterialFee(new Double(Math.round(oRecord.getMaterialFee())));
			}
			totalFee += oRecord.getMaterialFee();
		}
		if(oRecord.getCarFee() != null){	//��ͨ��
			bTotalFee = true;
			if(oRecord.getCarFee().intValue() != oRecord.getCarFee()){	//��С����
				oRecord.setCarFee(new Double(Math.round(oRecord.getCarFee())));
			}
			totalFee += oRecord.getCarFee();
		}
		if(oRecord.getDebugFee() != null){	//���Է�
			bTotalFee = true;
			if(oRecord.getDebugFee().intValue() != oRecord.getDebugFee()){	//��С����
				oRecord.setDebugFee(new Double(Math.round(oRecord.getDebugFee())));
			}
			totalFee += oRecord.getDebugFee();
		}if(oRecord.getOtherFee() != null){	//������
			bTotalFee = true;
			if(oRecord.getOtherFee().intValue() != oRecord.getOtherFee()){	//��С����
				oRecord.setOtherFee(new Double(Math.round(oRecord.getOtherFee())));
			}
			totalFee += oRecord.getOtherFee();
		}
		if(bTotalFee){
			oRecord.setTotalFee(totalFee);
		}else{
			oRecord.setTotalFee(null);
		}
		
		//���ļ�д�������
		workbook.write(os);
		os.flush();
	}
	
	/**
	 * �����ϴ�ԭʼ��¼Excel�ļ�
	 * ����OriginalRecord��Ӧ���ֶΣ������ļ��ж�������д��Excel�ļ���
	 * ����ί�е�����У��Ա��Ӧ���ֶΣ�������Ҫ��д��Excel�ļ�(����Excel�ļ��ж�ȡд�����ݿ�)
	 * @param in
	 * @param osFile
	 * @param cSheet
	 * @param oRecord
	 * @param staff ��/У��Ա
	 * @param certificate ��Ӧ֤��
	 * @param xlsParser
	 * @param oRecord:ԭʼ��¼�����ڴ��������Ϣ��������ʼִ��ʱoRecord��IDΪnull��
	 * @param certificate:֤�飨����Ԥ��֤�����Լ���֤���ģ���ļ����ƴӷ���ִ�к���أ�������ʼִ��ʱcertificate��IDΪnull��
	 * @param verAndAuth:����� ��Ȩǩ�ּ�¼�����ڴ��������Ϣ��������ʼִ��ʱverAndAuth��IDΪnull��
	 * @param specMap�������淶Map�����ڴ���������ݣ�Integer��ŵ�ΪSpecification��ID��
	 * @param stdMap:������׼Map�����ڴ���������ݣ�Integer��ŵ�ΪStandard��ID��
	 * @param stdAppMap����׼����Map�����ڴ���������ݣ�Integer��ŵ�ΪStandardAppliance��ID��
	 * @return 
	 * @throws IOException �������ʱ�׳��쳣
	 */
	public static void uploadExcelByBatch(InputStream in, File osFile, 
			CommissionSheet cSheet, ApplianceStandardName stdName, 
			ParseXMLAll xlsParser, 
			OriginalRecord oRecord, Certificate certificate, VerifyAndAuthorize veriAndAuth, 
			Map<Integer, Specification> specMap, Map<Integer, Standard> stdMap, Map<Integer, StandardAppliance> stdAppMap) throws Exception{
		OutputStream os = null;
		try{
			os = new FileOutputStream(osFile);
			uploadExcelByBatch(in, os, cSheet, stdName, xlsParser, oRecord, certificate, veriAndAuth, specMap, stdMap, stdAppMap);
		}catch(Exception e){
			throw e;
		}finally{
			if(os != null){
				os.close();
				os = null;
			}
		}
		
	}
	
	/**
	 * ��ȡ������
	 * @param workbook
	 * @param Attributes attributes
	 * @return
	 * @throws Exception
	 */
	private static HSSFSheet getSheet(HSSFWorkbook workbook, Attributes attributes) throws Exception{
		// �����Թ���������á�
		String sheetName = attributes.getValue("sheetName");
		if(sheetName == null){
			throw new Exception(String.format("��д���ֶ�ֵ:%s(��Ԫ��:%s)ʧ��!ԭ�������ļ�δָ�����������ƣ�sheetName", 
					attributes.getValue("desc")==null?"":attributes.getValue("desc"),
					attributes.getValue("cell")==null?"":attributes.getValue("cell")));
		}
		return workbook.getSheet(sheetName);
	}
	
	/**
	 * ��Excel���л�ȡ��Ԫ���ֵ
	 * @param sheet
	 * @param attributes
	 * @return
	 * @throws Exception
	 */
	private static Object getCellValue(HSSFSheet sheet, Attributes attributes) throws Exception{
		try{
			HSSFRow row = sheet.getRow(Integer.parseInt(attributes.getValue("rowIndex")));	//��ȡ��
			HSSFCell cell = row.getCell(Integer.parseInt(attributes.getValue("colIndex")));//��ȡ��
			
			int cellType = cell.getCellType();
			String cellVal = null;		//��ȡ��Ԫ���ַ���
	//		cell.setCellType(HSSFCell.CELL_TYPE_STRING);	//���õ�Ԫ������Ϊ�ַ�������
	//		String cellVal = cell.getStringCellValue();		//��ȡ��Ԫ���ַ���
			
			if(cellType == HSSFCell.CELL_TYPE_NUMERIC){
				if(HSSFDateUtil.isCellDateFormatted(cell)){	//���ڸ�ʽ
					cellVal = String.format("%f", cell.getNumericCellValue());
				}else{	//��ͨ����
					cellVal = String.valueOf(cell.getNumericCellValue());
				}
			}else if(cellType == HSSFCell.CELL_TYPE_BOOLEAN){
				cellVal = cell.getBooleanCellValue()?"true":"false";
			}else if(cellType == HSSFCell.CELL_TYPE_STRING){
				cellVal = cell.getStringCellValue();
			}
			
			Object args = null;
			
			//�жϲ�������
			Class paramClass = Class.forName(attributes.getValue("typeClass"));	//��������
			if(String.class == paramClass){
				args = cellVal;
			}else if(Boolean.class == paramClass){
				if(cellVal == null || cellVal.length() == 0){
					args = null;
				}else if(cellVal.equalsIgnoreCase(attributes.getValue("trueValue"))){
					args = new Boolean(true);
				}else{
					args = new Boolean(false);
				}
			}else if(Double.class == paramClass){
				if(cellVal == null || cellVal.length() == 0){
					args = null;
				}else{
					try{
						args = Double.parseDouble(cellVal);
					}catch(Exception ex){
						throw new Exception("ֵ����Ϊ��������");
					}
				}
			}else if(Integer.class == paramClass){
				if(cellVal == null || cellVal.length() == 0){
					args = null;
				}else{
					try{
						args = (int)Double.parseDouble(cellVal);
					}catch(Exception ex){
						throw new Exception("ֵ����Ϊ������");
					}
				}
			}else if(java.sql.Date.class == paramClass){	//��ʽΪyyyy-MM-dd
				if(cellVal == null || cellVal.length() == 0){
					args = null;
				}else{
					try{
						if(cellType == HSSFCell.CELL_TYPE_NUMERIC){
							args = new java.sql.Date(cell.getDateCellValue().getTime());
						}else{
							args = new java.sql.Date(DateTimeFormatUtil.DateFormat.parse(cellVal).getTime());
						}
					}catch(Exception ex){
						throw new Exception("ֵ����Ϊ���ڸ�ʽ��yyyy-MM-dd[��2012-01-01]");
					}
				}
			}else if(java.sql.Timestamp.class == paramClass){	//��ʽΪyyyy-MM-dd HH:mm:ss
				if(cellVal == null || cellVal.length() == 0){
					args = null;
				}else{
					try{
						if(cellType == HSSFCell.CELL_TYPE_NUMERIC){
							args = new java.sql.Timestamp(cell.getDateCellValue().getTime());
						}else{
							args = new java.sql.Timestamp(DateTimeFormatUtil.DateTimeFormat.parse(cellVal).getTime());
						}
					}catch(Exception ex){
						throw new Exception("ֵ����Ϊʱ���ʽ��yyyy-MM-dd HH:mm:ss[��2012-01-01 08:00:01]");
					}
				}
			}else{
				args = cellVal;
			}
			
			return args;
		}catch(Exception e){
			throw new Exception(String.format("��Excel�ж����ֶ�ֵ:%s(��Ԫ��:%s) ʧ�ܡ�%s", 
						attributes.getValue("desc")==null?"":attributes.getValue("desc"),
						attributes.getValue("cell")==null?"":attributes.getValue("cell"),
						e.getMessage() == null?"":String.format("(ԭ��%s)", e.getMessage())));
		}
	}
	/**
	 * ��Excel��д���ֶ�ֵ
	 * @param sheet
	 * @param attributes
	 * @param objValue
	 * @throws Exception
	 */
	private static void setCellValue(HSSFSheet sheet, Attributes attributes, Object objValue) throws Exception{
		try{
			HSSFRow row = sheet.getRow(Integer.parseInt(attributes.getValue("rowIndex")));	//��ȡ��
			HSSFCell cell = row.getCell(Integer.parseInt(attributes.getValue("colIndex")));//��ȡ��
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);	//���õ�Ԫ������Ϊ�ַ�������
			//�жϲ�������
			Class typeClass = Class.forName(attributes.getValue("typeClass"));	//ֵ����
			if(objValue == null){
				objValue = "";
			}
			if(objValue instanceof java.lang.String){
				cell.setCellValue(objValue.toString());
			}else if(objValue instanceof java.sql.Date){
				cell.setCellValue(DateTimeFormatUtil.DateFormat.format(objValue));
			}else if(objValue instanceof java.sql.Timestamp){
				if(typeClass != null && typeClass.equals("java.sql.Date")){
					cell.setCellValue(DateTimeFormatUtil.DateFormat.format(objValue));
				}else{
					cell.setCellValue(DateTimeFormatUtil.DateTimeFormat.format(objValue));
				}
			}else if(objValue instanceof java.lang.Boolean){
				if(true == (Boolean)objValue){
					cell.setCellValue(attributes.getValue("trueValue"));
				}else{
					cell.setCellValue(attributes.getValue("falseValue"));
				}
			}else{
				cell.setCellValue(objValue.toString());
			}
		}catch(Exception e){
			throw new Exception(String.format("��Excel��д���ֶ�ֵ:%s(��Ԫ��:%s) ʧ�ܡ�%s", 
					attributes.getValue("desc")==null?"":attributes.getValue("desc"),
					attributes.getValue("cell")==null?"":attributes.getValue("cell"),
					e.getMessage() == null?"":String.format("(ԭ��%s)", e.getMessage())));
		}
	}
	
//	/**
//	 * ���� �������� ������Ҫ�����������б�
//	 * @param workType
//	 * @return
//	 * @throws Exception
//	 */
//	private static List<Integer> getQualificationTypeList(String workType) throws Exception{
//		List<Integer> typeList = new ArrayList<Integer>();
//		if(workType.equals(WorkType_Jianding)){
//			typeList.add(FlagUtil.QualificationType.Type_Jianding);
//		}else if(workType.equals(WorkType_Jiaozhun)){
//			typeList.add(FlagUtil.QualificationType.Type_Jiaozhun);
//		}else if(workType.equals(WorkType_Jiance)){
//			
//		}else if(workType.equals(WorkType_Jianyan)){
//			typeList.add(FlagUtil.QualificationType.Type_Jianyan);
//		}
//		return typeList;
//	}
	
	/**
	 * ��ȡ֤��ģ���ļ�����
	 * @param workType
	 * @param conclusion
	 * @return
	 */
	public static String getCertificateModFileName(String workType, String conclusion) throws Exception{
		if(WorkType_Jianding.equals(workType)){
			if(conclusion != null && conclusion.contains("��")){
				return "�춨���֪ͨ��.doc";
			}else{
				return "�춨֤��.doc";
			}
		}
		if(WorkType_Jiaozhun.equals(workType)){
			return "У׼֤��.doc";
		}
		if(WorkType_Jiance.equals(workType)){
			return "��ⱨ��.doc";
		}
		if(WorkType_Jianyan.equals(workType)){
			return "���鱨��.doc";
		}
		return null;
	}
	
	/**
	 * �ϴ�Excelʱ������ҪУ�� ���ֶ�
	 * @param workbook
	 * @param cSheet
	 * @param oRecord
	 * @param stdName
	 * @param certificate
	 * @param xlsParser
	 * @param handledAttrsList
	 */
	private static void uploadExcelWithOtherInfo(HSSFWorkbook workbook, CommissionSheet cSheet, OriginalRecord oRecord, ApplianceStandardName stdName, Certificate certificate, ParseXMLAll xlsParser, List<Attributes> handledAttrsList) throws Exception{
		//ѭ��д����Ҫ���ֶ�����
		Iterator<String> keyIterator = xlsParser.getKeyIterator();
		Class c = null;
		Method method = null;
		Object retObj = null;
		Class paramClass = null;	//����Class
		while(keyIterator.hasNext()){
			String key = keyIterator.next();
			
			for(int i = 0; i < xlsParser.getQNameCount(key); i++){
				c = null;
				method = null;
				retObj = null;
				paramClass = null;
				
				Attributes attr = xlsParser.getAttributes(key, i);
				if(handledAttrsList.contains(attr)){
					continue;	//�Ѵ�����Ĳ��ٴ���
				}
				if(attr.getValue("field") == null || 
						!attr.getValue("field").equalsIgnoreCase("true") ||
						attr.getValue("type") == null){
					continue;
				}
				HSSFSheet sheet = getSheet(workbook, attr);	//��ȡ������
				
				//�������ֶ�
				if(attr.getValue("type").toLowerCase().contains("w")){	//�ֶ����ͣ�д���ļ�
					try {
						boolean bWriteToFile = true;
						c = Class.forName(attr.getValue("fieldClass"));	//��ȡ��Ӧ�������
						method = c.getMethod(String.format("get%s", key.toString()));	//get����
						if(c.isInstance(oRecord)){
							bWriteToFile = false;
							
//							retObj = method.invoke(oRecord);
//							//�ж�ί�е��Ƿ�Ϊ���깤�������깤������ò������޸ģ�
//							if(cSheet.getStatus() >= 3){	//���깤ȷ�ϻ�ע�� :���ò������޸�
//								if(key.equals("TestFee") || key.equals("RepairFee") || key.equals("MaterialFee") || key.equals("CarFee") || key.equals("DebugFee") || key.equals("OtherFee") || key.equals("TotalFee")){
//									retObj = method.invoke(oRecord);
//								}else{
//									bWriteToFile = false;	//ԭʼ��¼�����ϴ���Excel�ļ��е�ֵΪ׼
//								}
//							}else{
//								bWriteToFile = false;	//ԭʼ��¼�����ϴ���Excel�ļ��е�ֵΪ׼
//							}
						}else if(c.isInstance(cSheet)){
							retObj = method.invoke(cSheet);
						}else if(c == SysUser.class){	//��Ա����У��Ա�ͺ�����Ա����������֮ǰ�Ѿ�У�����
							continue;
						}else if(c.isInstance(certificate)){
							retObj = method.invoke(certificate);
						}else if(c.isInstance(stdName)){	//��׼����:������֮ǰ�Ѿ�У�����
							continue;
						}else if(c == Specification.class){	//�����淶:������
							continue;
						}else if(c == Standard.class){	//������׼��������
							continue;
						}else if(c == StandardAppliance.class){	//��׼���ߣ�������
							continue;
						}
					
						if(retObj == null){	//û���ҵ���Ӧ��get������ֵΪnull
							retObj = "";
						}
						/**************������ֵд��Excel�ļ���*******************/
						if(bWriteToFile){
							setCellValue(sheet, attr, retObj);
						}
					}catch(Exception e){
						e.printStackTrace();
						log.debug("exception in ExcelUtil->uploadExcelWithOtherInfo", e);
						throw new Exception(String.format("д���ֶ�ֵ:%s(%s,��Ԫ��:%s) ʧ�ܣ�%s ", 
								key.toString(),
								attr.getValue("desc")==null?"":attr.getValue("desc"),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								e.getMessage() == null?"":"ԭ��"+e.getMessage()));
					}
				}
				if(attr.getValue("type").toLowerCase().contains("r")){	//�ֶ����ͣ����ļ��ж�����д�����ݿ�
					try {
						c = Class.forName(attr.getValue("fieldClass"));	//��ȡ��Ӧ�������
						paramClass = Class.forName(attr.getValue("typeClass"));
						method = c.getMethod(String.format("set%s", key.toString()), paramClass);	//set����
						
						/*******ֻ��ԭʼ��¼�����д��*************/
						if(c.isInstance(oRecord) && method != null){	//��Excel��ȡֵ����д�����ݿ���
														
							//�ж�ί�е��Ƿ�Ϊ���깤�������깤������ò������޸ģ�
							if(cSheet.getStatus() >= 3){	//���깤ȷ�ϻ�ע�� :���ò������޸�
								if(key.equals("TestFee") || key.equals("RepairFee") || key.equals("MaterialFee") || key.equals("CarFee") || key.equals("DebugFee") || key.equals("OtherFee") || key.equals("TotalFee")){
									continue;
								}
							}
							
							Object args = getCellValue(sheet, attr);
							method.invoke(oRecord, args);
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.debug("exception in ExcelUtil->uploadExcelWithOtherInfo", e);
						throw new Exception(String.format("�����ֶ�ֵ:%s(%s,��Ԫ��:%s) ʧ�ܣ�%s ", 
								key.toString(),
								attr.getValue("desc")==null?"":attr.getValue("desc"),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								e.getMessage() == null?"":"ԭ��" + e.getMessage()));
					}
				}
			}
		}
	}
	
	

}
