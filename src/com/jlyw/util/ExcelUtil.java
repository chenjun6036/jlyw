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
 * 对原始记录Excel进行操作，采用类反射机制实现
 * 下载委托单的原始记录Excel文件中，写入相关的Excel数据
 * 上传原始记录Excel文件时，从中读取相应信息并写入数据库
 * @author Administrator
 *
 */
public class ExcelUtil {
	private static final Log log = LogFactory.getLog(ExcelUtil.class);
	private static final String FieldClassApplianceStandardName = "com.jlyw.hibernate.ApplianceStandardName";	//器具标准名称
	private static final String FieldClassCommissionSheet = "com.jlyw.hibernate.CommissionSheet";	//委托单
	private static final String FieldClassOriginalRecord = "com.jlyw.hibernate.OriginalRecord";	//原始记录
	private static final String FieldClassSysUser = "com.jlyw.hibernate.SysUser";	//检校人员 或者 核验人员
	private static final String FieldClassSpecification = "com.jlyw.hibernate.Specification";	//技术规范
	private static final String FieldClassStandard = "com.jlyw.hibernate.Standard";	//计量标准
	private static final String FieldClassStandardAppliance = "com.jlyw.hibernate.StandardAppliance";	//标准器具
	private static final String FieldClassCertificate = "com.jlyw.hibernate.Certificate";	//证书
	
	private static final String FieldClassDescVerify = "检校人员";	//检校人员的描述信息
	private static final String FieldClassDescChecker = "核验人员"; //核验人员的描述信息
	
	private static final String WorkType_Jianding = "检定";	//工作类型
	private static final String WorkType_Jiaozhun = "校准";
	private static final String WorkType_Jiance = "检测";
	private static final String WorkType_Jianyan = "检验";
	
	/**
	 * 下载原始记录Excel文件
	 * @param is 原始记录模板文件输入流
	 * @param os 生成内容后的原始记录文件输出流
	 * @param cSheet 委托单对象
	 * @param oRecord 原始记录对象
	 * @param staff 检校人员
	 * @param speList 技术规范列表
	 * @param stdList 计量标准列表
	 * @param stdAppList 标准器具列表
	 * @param certificate 对应证书
	 * @param stdName 标准名称
	 * @param verifier 核验人员
	 * @param parser 与Excel模板文件对应的字段定义xml文件解析者
	 * @return
	 * @throws IOException 处理出错时抛出异常
	 */
	public static void downloadExcel(InputStream is, OutputStream os,
			CommissionSheet cSheet, OriginalRecord oRecord, SysUser staff, 
			List<Specification> speList, List<Standard> stdList, List<StandardAppliance> stdAppList, 
			Certificate certificate, ApplianceStandardName stdName, SysUser verifier,
			ParseXMLAll parser) throws IOException, Exception{
		// 创建对Excel工作簿文件的引用
		HSSFWorkbook workbook = new HSSFWorkbook(is);
				
		//循环写入需要的字段内容
		Iterator<String> keyIterator = parser.getKeyIterator();
		Class c = null;
		Method method = null;
		Object retObj = null;
		
		//技术规范、计量标准、标准器具对应
		Map<String, Specification> speMap = new HashMap<String, Specification>();	
		Map<String, Standard> stdMap = new HashMap<String, Standard>();
		Map<String, StandardAppliance> stdAppMap = new HashMap<String, StandardAppliance>();
		int curSpeIndex = 0, curStdIndex = 0, curStdAppIndex = 0;	//当前读取的技术规范/计量标准/标准器具的索引号
		while(keyIterator.hasNext()){
			String key = keyIterator.next();
			for(int i = 0; i < parser.getQNameCount(key); i++){
				Attributes attr = parser.getAttributes(key, i);
				c = null;
				method = null;
				retObj = null;
				if(attr.getValue("field") != null && attr.getValue("field").equalsIgnoreCase("true")){	//交互的字段
					if(attr.getValue("type") != null && attr.getValue("type").toLowerCase().contains("w")){	//字段类型：写入
						// 创建对工作表的引用。
						HSSFSheet sheet = getSheet(workbook, attr);
						try {
							c = Class.forName(attr.getValue("fieldClass"));	//获取对应的类对象
							method = c.getMethod(String.format("get%s", key.toString()));	//get方法
							if(c.isInstance(oRecord)){
								retObj = method.invoke(oRecord);
							}else if(c.isInstance(cSheet)){
								retObj = method.invoke(cSheet);
							}else if(c.isInstance(staff)){
								if(attr.getValue("fieldClassDesc") == null || attr.getValue("fieldClassDesc").equals("检校人员")){	//检校人员
									retObj = method.invoke(staff);
								}else if(attr.getValue("fieldClassDesc").equals("核验人员") && verifier != null){	//核验人员
									retObj = method.invoke(verifier);
								}
							}else if(c.isInstance(certificate)){
								retObj = method.invoke(certificate);
							}else if(c.isInstance(stdName)){
								retObj = method.invoke(stdName);
							}else if(c == Specification.class){	//技术规范
								String indexStr = attr.getValue("indexStr");	//获取顺序号（第几个）
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
							}else if(c == Standard.class){	//计量标准
								String indexStr = attr.getValue("indexStr");	//获取顺序号（第几个）
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
							}else if(c == StandardAppliance.class){	//标准器具
								String indexStr = attr.getValue("indexStr");	//获取顺序号（第几个）
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
							
							/**************将返回值写入Excel文件中*******************/
							setCellValue(sheet, attr, retObj);
							
						}catch(Exception e){
							e.printStackTrace();
							log.debug("exception in ExcelUtil->downloadExcel", e);
							throw new Exception(String.format("写入字段值:%s(%s) 失败，请确认模板xls文件和字段定义xml文件是否正确！", 
									key.toString(),
									attr.getValue("desc")==null?"":attr.getValue("desc")));
						}
					}
				}
			}
		}
		
		//将文件写入输出流
		workbook.write(os);
		os.flush();
	}
	
	/**
	 * 上传原始记录Excel文件
	 * @param is
	 * @param os
	 * @param cSheet
	 * @param oRecord
	 * @param workStaff 检/校人员
	 * @param certificate 对应证书
	 * @param stdName
	 * @param speList
	 * @param stdList
	 * @param stdAppList
	 * @param veriAndAuth ：核验和授权签字，用于带出方法里的数据
	 * @param certificateModFileName ：模板文件的名称，用于带出方法里的数据
	 * @param xlsParser
	 * @throws IOException
	 * @throws Exception
	 */
	public static void uploadExcel(InputStream is, OutputStream os, 
			CommissionSheet cSheet, OriginalRecord oRecord, SysUser workStaff, Certificate certificate, ApplianceStandardName stdName, 
			List<Specification> speList, List<Standard> stdList, List<StandardAppliance> stdAppList,
			VerifyAndAuthorize veriAndAuth, StringBuffer certificateModFileName,
			ParseXMLAll xlsParser) throws IOException, Exception{
		
		// 创建对Excel工作簿文件的引用
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		
		List<Attributes> handledAttrList = new ArrayList<Attributes>();	//已处理过的属性列表
		
		//校验委托单位
		List<Attributes> attrList = xlsParser.getAttributesByPropertyValue("CustomerName", "fieldClass", FieldClassCommissionSheet);	//验证委托单单位名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘委托单位’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || !obj.toString().equals(cSheet.getCustomerName())){
				throw new Exception(String.format("Excel中的委托单位:'%s'(单元格:%s)与委托单上的委托单位:'%s'不匹配！", 
						obj==null?"":obj.toString(), 
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						cSheet.getCustomerName()));
			}
		}
		//校验标准名称
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassApplianceStandardName);	//验证器具标准名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘标准名称’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || !obj.toString().equals(stdName.getName())){
				throw new Exception(String.format("Excel中的标准名称:'%s'(单元格:%s)与模板文件的标准名称:'%s'不匹配！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						stdName.getName()));
			}
		}
		//获取型号规格、测量范围、准确度等级（查找受检器具）
		if(xlsParser.getQNameCount("target-appliance-link-to") == 0){
			throw new Exception(String.format("配置文件出错：未配置受检器具关联项(%s)，请联系模板文件管理员！", "target-appliance-link-to"));
		}
		Attributes tAppLinkAttr = xlsParser.getAttributes("target-appliance-link-to", 0);
		boolean bModelLinked = (tAppLinkAttr.getValue("model") != null && tAppLinkAttr.getValue("model").equalsIgnoreCase("true"))?true:false;	//受检器具是否与型号规格有关
		boolean bRangeLinked = (tAppLinkAttr.getValue("range") != null && tAppLinkAttr.getValue("range").equalsIgnoreCase("true"))?true:false;	//受检器具是否与测量范围有关
		boolean bAccuracyLinked = (tAppLinkAttr.getValue("accuracy") != null && tAppLinkAttr.getValue("accuracy").equalsIgnoreCase("true"))?true:false;	//受检器具是否与不确定度有关
		String modelOld = oRecord.getModel(), rangeOld = oRecord.getRange(), accuracyOld = oRecord.getAccuracy();
		String model = null, range = null, accuracy = null;
		attrList = xlsParser.getAttributesByPropertyValue("Model", "fieldClass", FieldClassOriginalRecord);	//型号规格
		if(attrList.size() == 0 && bModelLinked){
			throw new Exception("‘型号规格’是该受检器具的关联项，但在Excel文件中找不到‘型号规格’！");
		}
		ApplianceModelManager modelMgr = new ApplianceModelManager();
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(bModelLinked){	//型号规格是该器具的关联项
				if((modelOld == null && obj != null && obj.length() > 0) ||
						(modelOld != null && (obj == null || !modelOld.equalsIgnoreCase(obj))) ){	//与之前的型号规格不相同
					//判断该受检器具是否含有该型号规格
					int iTempRet = modelMgr.getTotalCount(new KeyValueWithOperator("targetAppliance.id", oRecord.getTargetAppliance().getId(), "="),
							new KeyValueWithOperator("model", obj==null?"":obj, "="));
					if(iTempRet <= 0){
						throw new Exception(String.format("‘型号规格’是该受检器具的关联项，Excel中的‘型号规格’:'%s'(单元格:%s) 与所选定的受检器具(标准名称：%s,受检器具名称：%s)的型号规格:'%s'不一致！",
								obj==null?"":obj,
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								stdName.getName(),
								oRecord.getTargetAppliance().getName(),
								modelOld==null?"":modelOld));
					}
				}
				if(model != null && !model.equalsIgnoreCase(obj==null?"":obj)){
					throw new Exception(String.format("Excel中的有多处‘型号规格’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
							obj==null?"":obj,
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							model));
				}
				model = (obj==null)?"":obj;
			}else{
				model = (obj==null)?"":obj;
			}
		}
		attrList = xlsParser.getAttributesByPropertyValue("Range", "fieldClass", FieldClassOriginalRecord);	//测量范围
		if(attrList.size() == 0 && bRangeLinked){
			throw new Exception("‘测量范围’是该受检器具的关联项，但在Excel文件中找不到‘测量范围’！");
		}
		ApplianceRangeManager rangeMgr = new ApplianceRangeManager();
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(bRangeLinked){	//型号规格是该器具的关联项
				if((rangeOld == null && obj != null && obj.length() > 0) ||
						(rangeOld != null && (obj == null || !rangeOld.equalsIgnoreCase(obj))) ){
					//判断该受检器具是否含有该型号规格
					int iTempRet = rangeMgr.getTotalCount(new KeyValueWithOperator("targetAppliance.id", oRecord.getTargetAppliance().getId(), "="),
							new KeyValueWithOperator("range", obj==null?"":obj, "="));
					if(iTempRet <= 0){
						throw new Exception(String.format("‘测量范围’是该受检器具的关联项，Excel中的‘测量范围’:'%s'(单元格:%s) 与所选定的受检器具(标准名称：%s,受检器具名称：%s)的测量范围:'%s'不一致！",
								obj==null?"":obj,
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								stdName.getName(),
								oRecord.getTargetAppliance().getName(),
								rangeOld==null?"":rangeOld));
					}
					
				}
				if(range != null && !range.equalsIgnoreCase(obj==null?"":obj)){
					throw new Exception(String.format("Excel中的有多处‘测量范围’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
							obj==null?"":obj,
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							range));
				}				
				range = (obj==null)?"":obj;
			}else{
				range = (obj==null)?"":obj;
			}
		}
		attrList = xlsParser.getAttributesByPropertyValue("Accuracy", "fieldClass", FieldClassOriginalRecord);	//不确定度
		if(attrList.size() == 0 && bAccuracyLinked){
			throw new Exception("‘准确度等级’是该受检器具的关联项，但在Excel文件中找不到‘准确度等级’！");
		}
		ApplianceAccuracyManager accuracyMgr = new ApplianceAccuracyManager();
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(bAccuracyLinked){	//准确度等级是该器具的关联项
				if((accuracyOld == null && obj != null && obj.length() > 0) ||
						(accuracyOld != null && (obj == null || !accuracyOld.equalsIgnoreCase(obj))) ){
					//判断该受检器具是否含有该型号规格
					int iTempRet = accuracyMgr.getTotalCount(new KeyValueWithOperator("targetAppliance.id", oRecord.getTargetAppliance().getId(), "="),
							new KeyValueWithOperator("accuracy", obj==null?"":obj, "="));
					if(iTempRet <= 0){
						throw new Exception(String.format("‘准确度等级’是该受检器具的关联项，Excel中的‘准确度等级’:'%s'(单元格:%s) 与所选定的受检器具(标准名称：%s,受检器具名称：%s)的准确度等级:'%s'不一致！",
								obj==null?"":obj,
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								stdName.getName(),
								oRecord.getTargetAppliance().getName(),
								accuracyOld==null?"":accuracyOld));
					}
					
				}
				if(accuracy != null && !accuracy.equalsIgnoreCase(obj==null?"":obj)){
					throw new Exception(String.format("Excel中的有多处‘准确度等级’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
							obj==null?"":obj,
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							accuracy));
				}				
				accuracy = (obj==null)?"":obj;
			}else{
				accuracy = (obj==null)?"":obj;
			}
		}
		
		//获取工作性质
		String workType = null;
		attrList = xlsParser.getAttributesByPropertyValue("WorkType", "fieldClass", FieldClassOriginalRecord);	//验证工作性质
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘工作性质’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || (!obj.equals(WorkType_Jianding) && !obj.equals(WorkType_Jiaozhun) && !obj.equals(WorkType_Jiance) && !obj.equals(WorkType_Jianyan))){
				throw new Exception(String.format("Excel中的‘工作性质’无效:'%s'(单元格:%s)，该值必须为“检定/校准/检测/检验”中的一种",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(workType != null && !workType.equals(obj)){
				throw new Exception(String.format("Excel中的有多处‘工作性质’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						workType));
			}
			workType = obj.toString();
		}
		//获取检定/校准日期
		Date workDate = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date nowDate = new Date(calendar.getTimeInMillis());	//当前日期
		attrList = xlsParser.getAttributesByPropertyValue("WorkDate", "fieldClass", FieldClassOriginalRecord);	//获取检定/校准日期
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘检定/校准日期’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null){
				if(workDate == null || workDate.compareTo(nowDate) == 0){
					workDate = nowDate;
					setCellValue(sheet, attr, workDate);	//将值写入Excel文件中
				}else{
					throw new Exception(String.format("Excel中有多处‘检定/校准日期’且值不一致(若为空，则默认为今天):'%s'(单元格:%s) 与  '%s' 不一致！",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(workDate)));
				}
			}else if(workDate != null && workDate.compareTo((Date)obj) != 0){
				throw new Exception(String.format("Excel中有多处‘检定/校准日期’且值不一致(若为空，则默认为今天):'%s'(单元格:%s) 与  '%s'不一致！",
						obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						DateTimeFormatUtil.DateFormat.format(workDate)));
			}else{
				workDate = (Date)obj;
			}
		}
		//检验有效日期(若填写，则判断不可比检定日期早且不能比正常的有效期晚，若没有填写，则反填)
		Date validateDate = null, validateDateMax = null;
		if(oRecord.getTargetAppliance().getTestCycle() != null){	//计算最大的有效日期
			calendar.setTime(workDate);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + oRecord.getTargetAppliance().getTestCycle());
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	//有效期=检定日期+检定周期-1天
			validateDateMax = new java.sql.Date(calendar.getTimeInMillis());  	//有效期
		}
		attrList = xlsParser.getAttributesByPropertyValue("Validity", "fieldClass", FieldClassOriginalRecord);	//获取有效日期
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘有效日期’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null){	//没有填写：反填
				if(validateDate != null && (validateDateMax == null || validateDate.compareTo(validateDateMax) != 0)){
					throw new Exception(String.format("Excel中有多处‘有效日期’且值不一致(若为空，则默认为检定日期+检定周期):'%s'(单元格:%s) 与  '%s'不一致！",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(validateDate)));
				}
				validateDate = validateDateMax;
				setCellValue(sheet, attr, validateDate);	//将值写入Excel文件中
				
			}else{
				if(workDate.after((Date)obj)){
					throw new Exception(String.format("‘有效日期’:'%s'(单元格:%s) 不能早于 ‘检定/校准日期’:'%s' ！",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(workDate)));
				}
//				if(validateDateMax != null && validateDateMax.before((Date)obj)){
//					throw new Exception(String.format("‘有效日期’:'%s'(单元格:%s) 不能晚于 最大的有效期:'%s'！",
//							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
//							attr.getValue("cell")==null?"":attr.getValue("cell"),
//							DateTimeFormatUtil.DateFormat.format(validateDateMax)));
//				}
				if(validateDate != null && validateDate.compareTo((Date)obj) != 0){
					throw new Exception(String.format("Excel中有多处‘有效日期’且值不一致(若为空，则默认为检定日期+检定周期):'%s'(单元格:%s) 与  '%s'不一致！",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(validateDate)));
				}else{
					validateDate = (Date)obj;
				}
			} 
		}
		
		//检验依据的技术文件
		attrList = xlsParser.getAttributesByPropertyValue("SpecificationCode", "fieldClass", FieldClassSpecification);	//技术规范：编号
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘依据技术文件（技术规范）的编号’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj != null && obj.length() > 0){
				Specification spe = getSpecificationInList(obj, speList);
				if(spe == null){
					throw new Exception(String.format("找不到本次检定/检验 依据的技术文件 %s(单元格:%s)", 
							obj==null?"":obj,
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
			}
		}
		//检验计量标准
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassStandard);	//计量标准：名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘计量标准的名称’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj != null && obj.toString().length() > 0){
				String stdNameValue = obj, stdRange = null, stdAccuracy = null, stdCertificateCode = null;
				Date stdValidate = null;
				
				boolean isStdVirtual = SystemCfgUtil.checkStandardVirtual(stdNameValue);	//该计量标准是否为虚拟的计量标准
				if(isStdVirtual){
					setCellValue(sheet, attr, "");	//写入单元格为空字符串
				}
				
				String indexStr = attr.getValue("indexStr");
				if(indexStr == null){
					throw new Exception(String.format("模板定义文件错误：没有定义计量标准名称的顺序号(单元格：%s)", attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				List<Attributes> attrTempList = xlsParser.getAttributesByPropertyValues("Range", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的计量标准的测量范围！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdRange = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//写入单元格为空字符串
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("Uncertain", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的计量标准的准确度等级（或最大允许误差或不确定度）！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAccuracy = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//写入单元格为空字符串
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("CertificateCode", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的计量标准的证书编号！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdCertificateCode = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//写入单元格为空字符串
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的计量标准的‘有效期至’！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdValidate = (objTemp==null)?null:(Date)objTemp;
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//写入单元格为空字符串
					}
				}
				Standard std = getStandardInList(stdNameValue, true, 
						stdRange, stdRange==null?false:true, 
						stdAccuracy, stdAccuracy==null?false:true, 
						stdCertificateCode, stdCertificateCode==null?false:true, 
						stdValidate, stdValidate==null?false:true, 
						stdList);
				if(std == null){
					throw new Exception(String.format("找不到本次检定/检验 所使用的计量标准(名称:%s%s%s%s%s)(单元格:%s)", 
							stdNameValue,
							stdRange==null?"":",测量范围:"+stdRange,
							stdAccuracy==null?"":",不确定度:"+stdAccuracy,
							stdCertificateCode==null?"":",证书编号:"+stdCertificateCode,
							stdValidate==null?"":",有效期至："+DateTimeFormatUtil.DateFormat.format(stdValidate),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				if(stdValidate == null){	//有效期为空，则反填至Excel文件
					stdValidate = std.getValidDate();
					attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandard, "indexStr", indexStr);
					for(Attributes attrTemp : attrTempList){
						HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
						setCellValue(sheetTemp, attrTemp, stdValidate);
					}
				}
				//判断有效期是否在检定日期之后
				if(stdValidate != null && workDate.after(stdValidate)){
					throw new Exception(String.format("'检定/校准日期(若为空，则默认为今天)：'%s' 不能在所使用的计量标准(名称:%s%s%s%s%s)的有效期之后'", 
							DateTimeFormatUtil.DateFormat.format(workDate),
							stdNameValue,
							stdRange==null?"":",测量范围:"+stdRange,
							stdAccuracy==null?"":",不确定度:"+stdAccuracy,
							stdCertificateCode==null?"":",证书编号:"+stdCertificateCode,
							stdValidate==null?"":",有效期至："+DateTimeFormatUtil.DateFormat.format(stdValidate)));
				}
			}
		}
		//检验标准器具
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassStandardAppliance);	//标准器具：名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘主要标准器具的名称’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj != null && obj.length() > 0){
				String stdAppName = obj, stdAppRange = null, stdAppAccuracy = null, stdAppSeriaNumber = null;
				Date stdAppValidate = null;
				
				String indexStr = attr.getValue("indexStr");
				if(indexStr == null){
					throw new Exception(String.format("模板定义文件错误：没有定义‘主要标准器具’的顺序号(单元格：%s)", attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				List<Attributes> attrTempList = xlsParser.getAttributesByPropertyValues("Range", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的‘主要标准器具’的测量范围！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppRange = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("Uncertain", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的‘主要标准器具’的准确度等级（或最大允许误差或不确定度）！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppAccuracy = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("SeriaNumber", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的‘主要标准器具’的证书编号！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppSeriaNumber = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的‘主要标准器具’的‘有效期至’！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
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
					throw new Exception(String.format("找不到本次检定/检验 所使用的‘主要标准器具’(名称:%s%s%s%s%s)(单元格:%s)！",
							stdAppName,
							stdAppRange==null?"":",测量范围:"+stdAppRange,
							stdAppAccuracy==null?"":",不确定度:"+stdAppAccuracy,
							stdAppSeriaNumber==null?"":",证书编号:"+stdAppSeriaNumber,
							stdAppValidate==null?"":",有效期至："+DateTimeFormatUtil.DateFormat.format(stdAppValidate),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				if(stdAppValidate != null){	//验证TestLog是否有该有效期的溯源记录
					TestLogManager tlMgr = new TestLogManager();
					int iTlRet = tlMgr.getTotalCount(new KeyValueWithOperator("standardAppliance.id", stdApp.getId(), "="),
							new KeyValueWithOperator("status", 1, "<>"),
							new KeyValueWithOperator("validDate", stdAppValidate, "="));
					if(iTlRet == 0){
						throw new Exception(String.format("本次检定/检验 所使用的‘主要标准器具’(名称:%s%s%s%s)(单元格:%s)没有有效期为 %s 的溯源记录！",
								stdAppName,
								stdAppRange==null?"":",测量范围:"+stdAppRange,
								stdAppAccuracy==null?"":",不确定度:"+stdAppAccuracy,
								stdAppSeriaNumber==null?"":",证书编号:"+stdAppSeriaNumber,								
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								stdAppValidate==null?"":DateTimeFormatUtil.DateFormat.format(stdAppValidate) ));
					}
				}else if(stdAppValidate == null && stdApp.getValidDate() != null){	//标准器具的有效期为空：则默认为该标准器具的最新有效期，并反填至Excel文件
					stdAppValidate = stdApp.getValidDate();
					attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
					for(Attributes attrTemp : attrTempList){
						HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
						setCellValue(sheetTemp, attrTemp, stdAppValidate);
					}
				}
				//判断有效期是否在检定日期之后
				if(stdAppValidate != null && workDate.after(stdAppValidate)){
					throw new Exception(String.format("'检定/校准日期(若为空，则默认为今天)：'%s' 不能在所使用的‘主要标准器具’(名称:%s%s%s%s%s)的有效期之后'", 
							DateTimeFormatUtil.DateFormat.format(workDate),
							stdAppName,
							stdAppRange==null?"":",测量范围:"+stdAppRange,
							stdAppAccuracy==null?"":",不确定度:"+stdAppAccuracy,
							stdAppSeriaNumber==null?"":",证书编号:"+stdAppSeriaNumber,
							stdAppValidate==null?"":",有效期至："+DateTimeFormatUtil.DateFormat.format(stdAppValidate)));
				}
			}
		}
		
		//核对：检定/校准人员（确定检定/校准人员 和 任务分配记录）
		attrList = xlsParser.getAttributesByPropertyValues("Name", "fieldClass", FieldClassSysUser, "fieldClassDesc", FieldClassDescVerify);	//验证检校人员
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘检定/校准人员’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || !workStaff.getName().equalsIgnoreCase(obj)){
				throw new Exception(String.format("Excel中的‘检定/校准人员’:'%s'(单元格:%s)与实际检定/校准人员:'%s'不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						workStaff.getName()));
			}
		}
		//核对：核验人员
		SysUser checkStaff = null;
		QualificationManager qfMgr = new QualificationManager();
		UserManager userMgr = new UserManager();
		List<Integer> qfTypeList = new ArrayList<Integer>();
		qfTypeList.add(FlagUtil.QualificationType.Type_Heyan);
		attrList = xlsParser.getAttributesByPropertyValues("Name", "fieldClass", FieldClassSysUser, "fieldClassDesc", FieldClassDescChecker);	//验证核验人员
		if(attrList.size() == 0){
			throw new Exception("在Excel的配置文件中找不到‘核验人员’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.length() == 0) && checkStaff != null){
				throw new Exception(String.format("Excel中的有多个‘核验人员’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						checkStaff.getName()));
			}
			if(obj != null && checkStaff != null && !checkStaff.getName().equalsIgnoreCase(obj)){
				throw new Exception(String.format("Excel中的有多个‘核验人员’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						checkStaff.getName()));
			}
			if(checkStaff == null && obj != null && obj.length() > 0){
				//检查是否有核验的资质
				List<Object[]> qfRetList = qfMgr.getQualifyUsers(obj.toString(), stdName.getId(), 0, qfTypeList);
				if(qfRetList.size() == 0){
					throw new Exception(String.format("Excel中的指定‘核验人员’:'%s'(单元格:%s) 没有器具标准名称'%s'的 核验资质！",
							obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							stdName.getName()));
				}
				Object[] userObj = qfRetList.get(0);
				checkStaff = userMgr.findById((Integer)userObj[0]);
				if(checkStaff != null && workStaff.getId().equals(checkStaff.getId())){
					throw new Exception(String.format("Excel中的指定‘核验人员’:'%s'(单元格:%s) 与 ‘检定/校准人员’:'%s'不能为同一个人！",
							obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							workStaff.getName()));
				}
			}
//			setCellValue(sheet, attr, "");	//将Excel中的核验人员置为""，等授权签字通过以后统一再填入（以省去网页上每次选择核验人员后需要更新Excel）
		}
		//获取授权签字人员
		SysUser authStaff = null;	//签字人
		if(checkStaff != null){ //核验人员不为空
			List<Object[]> qfRetList = qfMgr.getVerifyOrAuthorizeQualifyUsers(stdName.getId(), 0, FlagUtil.QualificationType.Type_Qianzi);
			if(qfRetList.size() == 0){
				throw new Exception(String.format("找不到器具标准名称'%s'对应的授权签字人员！请联系基础数据管理员！", stdName.getName()));
			}
			Object[] userObj = qfRetList.get(0);
			authStaff = userMgr.findById((Integer)userObj[0]);
		}
		
		//获取结论、证书模板文件名
		String conclusion = null;	//结论
		attrList = xlsParser.getAttributesByPropertyValue("Conclusion", "fieldClass", FieldClassOriginalRecord);	//获取结论
		if(attrList.size() == 0 && workType.equals(WorkType_Jianding)){
			throw new Exception("在Excel文件中找不到‘结论’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.trim().length() == 0) && workType.equals(WorkType_Jianding)){
				throw new Exception(String.format("Excel中的‘结论’值无效:'%s'(单元格:%s)，请输入一个有效值！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(conclusion != null && !conclusion.equals(obj)){
				throw new Exception(String.format("Excel中的有多处‘结论’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						conclusion));
			}
			conclusion = obj.toString();
		}
		certificateModFileName.append(getCertificateModFileName(workType, conclusion));	//获取证书模板文件名称
//		certificate.setFileName(getCertificateModFileName(workType, conclusion));	//获取证书模板文件名称
		
		//获取器具名称(常用名称)
		String applianceName = null;
		attrList = xlsParser.getAttributesByPropertyValue("ApplianceName", "fieldClass", FieldClassOriginalRecord);	//获取器具名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘器具名称（常用名称）’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				if(applianceName != null && !applianceName.equals(stdName.getName())){
					throw new Exception(String.format("Excel中有多个器具名称（若为空，则默认为标准名称）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							applianceName));
				}else{
					setCellValue(sheet, attr, stdName.getName());
					applianceName = stdName.getName();
				}
			}else if(applianceName != null && !applianceName.equals(obj)){
				throw new Exception(String.format("Excel中有多个器具名称（若为空，则默认为标准名称）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						applianceName));
			}else{
				applianceName = obj;
			}
		}
		
		//获取器具数量
		Integer applianceQuantity = null;
		attrList = xlsParser.getAttributesByPropertyValue("Quantity", "fieldClass", FieldClassOriginalRecord);	//验证器具数量
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Integer obj = (Integer)getCellValue(sheet, attr);
			if(obj == null){
				throw new Exception(String.format("Excel中的器具数量:'%s'(单元格:%s)为空！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(obj <= 0){
				throw new Exception(String.format("Excel中的器具数量无效:'%s'(单元格:%s)为空！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(applianceQuantity != null && !applianceQuantity.equals(obj)){
				throw new Exception(String.format("Excel中有多个器具数量且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						applianceQuantity.toString()));
			}
			//判断有效器具数量是否大于委托单的总器具数量
			Integer existedNumber = 0;
			List<Long> existedCountList = new OriginalRecordManager().findByHQL("select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 ", 
					cSheet.getId());
			if(existedCountList.get(0) != null){
				existedNumber = existedCountList.get(0).intValue();
				if(oRecord.getQuantity() != null){	//减去该原始记录之前已有的数量
					existedNumber -= oRecord.getQuantity();
				}
			}
			if(obj + existedNumber > cSheet.getQuantity()){
				throw new Exception(String.format("Excel中的器具数量'%s'(单元格:%s)无效:委托单下所有原始记录的器具总数'%s'不得超过委托单的器具数量'%s'！",
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
		/****************        处理费用信息          ******************/
		//检定费(如果没填，则默认收费标准；否则，进行校验。如果收费标准为null或0，则以用户输入为准)
		Double testFee = null;
		Double testFeeStandard = oRecord.getTargetAppliance().getFee()==null?0:oRecord.getTargetAppliance().getFee()*applianceQuantity;	//收费标准(待修改：后面要添加判断是否存在报价单)
		Double testFeeOld = oRecord.getTestFee();
		attrList = xlsParser.getAttributesByPropertyValue("TestFee", "fieldClass", FieldClassOriginalRecord);
		if(attrList.size() == 0){
			throw new Exception("在Excel配置文件中找不到‘检定费’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//委托单已完工或者已注销:不允许修改费用
				testFee = testFeeOld;
				if(obj == null){	//将费用反填
					setCellValue(sheet, attr, testFee);
				}else{
					if(!obj.equals(testFeeOld==null?new Double(0):testFeeOld)){
						throw new Exception(String.format("该委托单已完工或已注销，不能修改检定费！Excel中检定费:'%s'(单元格:%s) 与 原费用('%s') 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								testFeeOld==null?0:testFeeOld));
					}
				}
			}else{	//委托单尚未完工，可以修改费用
				if(obj == null){
					if(testFee != null && !testFee.equals(testFeeStandard)){
						throw new Exception(String.format("Excel中有多个检定费（若为空，则默认为标准费用）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								testFee));
					}else{
						testFee = testFeeStandard;
						setCellValue(sheet, attr, testFee);
					}
				}else if(testFee != null && !testFee.equals(obj)){
					throw new Exception(String.format("Excel中有多个检定费（若为空，则默认为标准费用）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							testFee));
				}else{
					if(testFeeStandard > 0 && !testFeeStandard.equals(obj)){
						throw new Exception(String.format("Excel中检定费与收费标准不一致:'%s'(单元格:%s) 与 标准费用('%s') 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								testFeeStandard));
					}
					testFee = obj;
				}
			}
		}
		//修理级别、修理费（根据修理级别来判断修理费标准）
		String repairLevel = null;
		String repairLevelOld = oRecord.getRepairLevel();
		attrList = xlsParser.getAttributesByPropertyValue("RepairLevel", "fieldClass", FieldClassOriginalRecord);
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘修理级别’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//委托单已完工或者已注销
				repairLevel = repairLevelOld;
				if(obj == null){
					setCellValue(sheet, attr, repairLevel);
				}else{
					if(!obj.equalsIgnoreCase(repairLevelOld==null?"":repairLevelOld)){
						throw new Exception(String.format("该委托单已完工或已注销，不能修改修理级别！Excel中修理级别:'%s'(单元格:%s) 与 原修理级别('%s') 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairLevelOld==null?"":repairLevelOld));
					}
				}
			}else{	//委托单未完工
				if(obj == null || obj.length() == 0){
					if(repairLevel != null && !repairLevel.equals("")){
						throw new Exception(String.format("Excel中有多个修理级别且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairLevel));
					}else{
						repairLevel = "";
					}
				}else if(repairLevel != null && !repairLevel.equals(obj)){
					throw new Exception(String.format("Excel中有多个修理级别且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairLevel));
				}else{
					repairLevel = obj;
				}
			}
		}
		Double repairFee = null;
		Double repairFeeStandard = 0.0;		//收费标准
		Double repairFeeOld = oRecord.getRepairFee();
		if(repairLevel != null && repairLevel.equals("小")){
			repairFeeStandard = oRecord.getTargetAppliance().getSrfee()==null?0:oRecord.getTargetAppliance().getSrfee()*applianceQuantity;
		}else if(repairLevel != null && repairLevel.equals("中")){
			repairFeeStandard = oRecord.getTargetAppliance().getMrfee()==null?0:oRecord.getTargetAppliance().getMrfee()*applianceQuantity;
		}else if(repairLevel != null && repairLevel.equals("大")){
			repairFeeStandard = oRecord.getTargetAppliance().getLrfee()==null?0:oRecord.getTargetAppliance().getLrfee()*applianceQuantity;
		}
		attrList = xlsParser.getAttributesByPropertyValue("RepairFee", "fieldClass", FieldClassOriginalRecord);	//修理费
		if(attrList.size() == 0){
			throw new Exception("在Excel配置文件中找不到‘修理费’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//委托单已完工或者已注销
				repairFee = repairFeeOld;
				if(obj == null){
					setCellValue(sheet, attr, repairFee);
				}else{
					if(!obj.equals(repairFeeOld==null?new Double(0):repairFeeOld)){
						throw new Exception(String.format("该委托单已完工或已注销，不能修改修理费用！Excel中修理费用:'%s'(单元格:%s) 与 原修理费用('%s') 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairFeeOld==null?"":repairFeeOld.toString()));
					}
				}
			}else{	//委托单未完工
				if(obj == null){
					if(repairFee != null && !repairFee.equals(repairFeeStandard)){
						throw new Exception(String.format("Excel中有多个修理费（若为空，则默认为标准费用）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairFee));
					}else{
						repairFee = repairFeeStandard;
						setCellValue(sheet, attr, repairFee);
					}
				}else if(repairFee != null && !repairFee.equals(obj)){
					throw new Exception(String.format("Excel中有多个修理费（若为空，则默认为标准费用）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairFee));
				}else{
					if(repairFeeStandard > 0 && !repairFeeStandard.equals(obj)){
						throw new Exception(String.format("Excel中修理费与收费标准不一致:'%s'(单元格:%s) 与 标准费用('%s') 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairFeeStandard));
					}
					repairFee = obj;
				}
			}
		}
		//配件费、配件明细：当有配件费时，配件明细不能为空！
		Double materialFee = null;
		Double materialFeeOld = oRecord.getMaterialFee();
		attrList = xlsParser.getAttributesByPropertyValue("MaterialFee", "fieldClass", FieldClassOriginalRecord);	//配件费
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘配件费’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//委托单已完工或已注销
				materialFee = materialFeeOld;
				if(obj == null){
					setCellValue(sheet, attr, materialFee);
				}else{
					if(!obj.equals(materialFeeOld==null?new Double(0):materialFeeOld)){
						throw new Exception(String.format("该委托单已完工或已注销，不能修改配件费！Excel中配件费:'%s'(单元格:%s) 与 原配件费('%s') 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								repairFeeOld==null?"":repairFeeOld.toString()));
					}
				}
			}else{	//委托单未完工
				if(obj == null){
					if(materialFee != null && materialFee != 0){
						throw new Exception(String.format("Excel中有多个配件费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								materialFee));
					}else{
						materialFee = 0.0;
					}
				}else if(materialFee != null && !materialFee.equals(obj)){
					throw new Exception(String.format("Excel中有多个配件费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							materialFee));
				}else{
					materialFee = obj;
				}
			}
		}
		String materialDetail = null;
		attrList = xlsParser.getAttributesByPropertyValue("MaterialDetail", "fieldClass", FieldClassOriginalRecord);	//配件明细
		if(attrList.size() == 0 && materialFee != null && materialFee > 0){
			throw new Exception("在Excel文件中找不到‘配件明细’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.trim().length() == 0) && materialFee != null && materialFee > 0){
				throw new Exception(String.format("Excel中配件明细为空:'%s'(单元格:%s)！请输入有效内容(有配件费'%s'，则配件明细必填)！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						materialFee));
			}else if(materialDetail != null && !materialDetail.equals(obj)){
				throw new Exception(String.format("Excel中有多个配件明细且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						materialDetail));
			}else{
				materialDetail = obj;
			}
		}
		//交通费：现场才有交通费
		Double carFee = null;
		Double carFeeOld = oRecord.getCarFee();
		attrList = xlsParser.getAttributesByPropertyValue("CarFee", "fieldClass", FieldClassOriginalRecord);	//交通费
		if(attrList.size() == 0 && cSheet.getCommissionType() == 2){	//现场检测
			throw new Exception("在Excel文件中找不到‘交通费’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//委托单已完工或已注销
				carFee = carFeeOld;
				if(obj == null){
					setCellValue(sheet, attr, carFee);
				}else{
					if(!obj.equals(carFeeOld==null?new Double(0):carFeeOld)){
						throw new Exception(String.format("该委托单已完工或已注销，不能修改交通费！Excel中交通费:'%s'(单元格:%s) 与 原交通费('%s') 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								carFeeOld==null?"":carFeeOld.toString()));
					}
				}
			}else{	//委托单未完工
				if(obj == null){
					if(carFee != null && carFee != 0){
						throw new Exception(String.format("Excel中有多个交通费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								carFee));
					}else{
						carFee = 0.0;
					}
				}else if(carFee != null && !carFee.equals(obj)){
					throw new Exception(String.format("Excel中有多个交通费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							carFee));
				}else{
					if(obj > 0 && cSheet.getCommissionType() != 2){	//交通费不为0，且不是现场检测
						throw new Exception(String.format("该委托单的委托形式不是现场检测，不能有交通费'%s'(单元格:%s)！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell")));
					}
					carFee = obj;
				}
			}
		}
		//其他费
		Double otherFee = null;
		Double otherFeeOld = oRecord.getOtherFee();
		if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){  //委托单已完工或已注销：其他费不可改动
			otherFee = otherFeeOld;
		}
		attrList = xlsParser.getAttributesByPropertyValue("OtherFee", "fieldClass", FieldClassOriginalRecord);	//其他费
//		if(attrList.size() == 0){	//现场检测
//			throw new Exception("在Excel文件中找不到‘其他费’！");
//		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){	//委托单已完工或已注销
				otherFee = otherFeeOld;
				if(obj == null){
					setCellValue(sheet, attr, otherFee);
				}else{
					if(!obj.equals(otherFeeOld==null?new Double(0):otherFeeOld)){
						throw new Exception(String.format("该委托单已完工或已注销，不能修改其他费！Excel中其他费:'%s'(单元格:%s) 与 原其他费('%s') 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								otherFeeOld==null?"":otherFeeOld.toString()));
					}
				}
			}else{	//委托单未完工
				if(obj == null){
					if(otherFee != null && otherFee != 0){
						throw new Exception(String.format("Excel中有多个其他费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
								obj==null?"":obj.toString(),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								otherFee));
					}else{
						otherFee = 0.0;
					}
				}else if(otherFee != null && !otherFee.equals(obj)){
					throw new Exception(String.format("Excel中有多个其他费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							otherFee));
				}else{
					otherFee = obj;
				}
			}
		}
		//反填：工作地点
		String workLocation = cSheet.getCommissionType()==2?"被测仪器使用现场":"本所实验室";
		attrList = xlsParser.getAttributesByPropertyValue("WorkLocation", "fieldClass", FieldClassOriginalRecord);
//		if(attrList.size() == 0){
//			throw new Exception("在Excel文件中找不到‘工作地点’！");
//		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			setCellValue(sheet, attr, workLocation);
		}
		
		
		//更新oRecord
		oRecord.setAccuracy(accuracy);
		oRecord.setConclusion(conclusion);
		oRecord.setModel(model);
		oRecord.setRange(range);
//		oRecord.setSysUserByStaffId(workStaff);
//		oRecord.setTargetAppliance(tApp);
//		oRecord.setTaskAssign(tAssign);
		oRecord.setValidity(validateDate);
		oRecord.setWorkDate(workDate);
		oRecord.setWorkLocation(workLocation);	//工作地点
		oRecord.setWorkType(workType);
		oRecord.setQuantity(applianceQuantity);	//器具数量
		oRecord.setApplianceName(applianceName);
		
		oRecord.setTestFee(testFee);
		oRecord.setRepairLevel(repairLevel);
		oRecord.setRepairFee(repairFee);
		oRecord.setMaterialFee(materialFee);
		oRecord.setMaterialDetail(materialDetail);
		oRecord.setCarFee(carFee);
		oRecord.setOtherFee(otherFee);
		
		//更新veriAndAuth
		veriAndAuth.setSysUserByVerifierId(checkStaff);
		veriAndAuth.setSysUserByAuthorizerId(authStaff);
		
		//处理其它的信息
		uploadExcelWithOtherInfo(workbook, cSheet, oRecord, stdName, certificate, xlsParser, handledAttrList);
		
		//输入总费用
		boolean bTotalFee = false;	//总费用是否存在
		Double totalFee = new Double(0.0);
		if(oRecord.getTestFee() != null){	//检测费
			bTotalFee = true;
			if(oRecord.getTestFee().intValue() != oRecord.getTestFee()){	//有小数点
				oRecord.setTestFee(new Double(Math.round(oRecord.getTestFee())));
			}
			totalFee += oRecord.getTestFee();
		}
		if(oRecord.getRepairFee() != null){	//修理费
			bTotalFee = true;
			if(oRecord.getRepairFee().intValue() != oRecord.getRepairFee()){	//有小数点
				oRecord.setRepairFee(new Double(Math.round(oRecord.getRepairFee())));
			}
			totalFee += oRecord.getRepairFee();
		}
		if(oRecord.getMaterialFee() != null){	//材料费
			bTotalFee = true;
			if(oRecord.getMaterialFee().intValue() != oRecord.getMaterialFee()){	//有小数点
				oRecord.setMaterialFee(new Double(Math.round(oRecord.getMaterialFee())));
			}
			totalFee += oRecord.getMaterialFee();
		}
		if(oRecord.getCarFee() != null){	//交通费
			bTotalFee = true;
			if(oRecord.getCarFee().intValue() != oRecord.getCarFee()){	//有小数点
				oRecord.setCarFee(new Double(Math.round(oRecord.getCarFee())));
			}
			totalFee += oRecord.getCarFee();
		}
		if(oRecord.getDebugFee() != null){	//调试费
			bTotalFee = true;
			if(oRecord.getDebugFee().intValue() != oRecord.getDebugFee()){	//有小数点
				oRecord.setDebugFee(new Double(Math.round(oRecord.getDebugFee())));
			}
			totalFee += oRecord.getDebugFee();
		}if(oRecord.getOtherFee() != null){	//其他费
			bTotalFee = true;
			if(oRecord.getOtherFee().intValue() != oRecord.getOtherFee()){	//有小数点
				oRecord.setOtherFee(new Double(Math.round(oRecord.getOtherFee())));
			}
			totalFee += oRecord.getOtherFee();
		}
		if(bTotalFee){
			oRecord.setTotalFee(totalFee);
		}else{
			oRecord.setTotalFee(null);
		}
		
		//将文件写入输出流
		workbook.write(os);
		os.flush();
	}
	
	/**
	 * 上传原始记录Excel文件
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
	 * 上传原始记录Excel文件
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
	 * 从技术规范列表中获取相关证书编号的技术规范
	 * @param specificationCode：技术规范
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
	 * 从计量标准列表中获取相关计量标准
	 * @param stdName
	 * @param bStdNameCheck 为true时检验计量标准的名称；为false时不需要检验计量标准的名称
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
	 * 从标准器具列表中获取相关标准器具
	 * @param stdAppName
	 * @param bStdAppNameCheck 为true时检验标准器具的名称；为false时不需要检验标准器具的名称
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
	 * 更新原始记录中的证书编号字段
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
			
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			
//			// 创建对工作表的引用。
//			String sheetName = parser.getFirstAttribute("original-record-sheet", "sheetName");
//			if(sheetName == null){
//				throw new Exception("原始记录XML配置文件未指定原始记录工作表表名：sheetName");
//			}
//			HSSFSheet sheet = workbook.getSheet(sheetName);
			
			//循环写入证书编号的字段内容
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
				
				// 创建对工作表的引用。
				HSSFSheet sheet = getSheet(workbook, attr);
								
				//交互的字段
				if( attr.getValue("type").contains("w")){	//字段类型：写入文件
					try {
						c = Class.forName(attr.getValue("fieldClass"));	//获取对应的类对象
						method = c.getMethod(String.format("get%s", CertificateCodeKey.toString()));	//get方法
						if(c.isInstance(certificate)){
							retObj = method.invoke(certificate);
						}else {
							continue;
						}
					
						if(retObj == null){	//没有找到对应的get方法或值为null
							retObj = "";
						}
						/**************将返回值写入Excel文件中*******************/
						setCellValue(sheet, attr, retObj);
					}catch(Exception e){
						e.printStackTrace();
						throw new Exception(String.format("更新原始记录Excel的证书编号字段值失败！写入字段值:%s(%s) 失败，请确认xls文件和字段定义xml文件是否匹配！", 
								CertificateCodeKey.toString(),
								parser.getAttribute(CertificateCodeKey, "desc", i)==null?"":parser.getAttribute(CertificateCodeKey, "desc", i)));
					}
				}
			}
			
			//将文件写入输出流
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
	 * 移除原始记录上的多余信息：证书页以及费用等信息（用于生成PDF）
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
			
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			
			//移除证书的表单：用于打印不显示证书区域
			try{
				String certificateSheetName = parser.getFirstAttribute("certificate-sheet", "sheetName");	//获取证书的标签页名称
				workbook.removeSheetAt(workbook.getSheetIndex(certificateSheetName));
			}catch(Exception exx){
				log.debug("exception in ExcelUtil->removeAdditionalInfo->移除证书的表单", exx);
			}
			
			//删除原始记录上的附加信息区域：用于打印不显示附加信息区域
			try{
				String additionInfoSheetName = parser.getFirstAttribute("addition-info-region", "sheetName");	//附加信息区域的标签页名称
				String regionBeginColIndex = parser.getFirstAttribute("addition-info-region", "regionBeginColIndex");
				String regionEndColIndex = parser.getFirstAttribute("addition-info-region", "regionEndColIndex");
				if(additionInfoSheetName != null && regionBeginColIndex != null && regionEndColIndex != null){
					HSSFSheet sheet = workbook.getSheet(additionInfoSheetName);
					for(int i = Integer.parseInt(regionBeginColIndex); i <= Integer.parseInt(regionEndColIndex); i++){
						sheet.setColumnHidden(i, true);	//设置列隐藏
					}
				}
			}catch(Exception exx){
				log.debug("exception in ExcelUtil->removeAdditionalInfo->隐藏附加信息区域", exx);
			}
			
			//将文件写入输出流
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
	 * 在原始记录Excel中插入检/校人员和核验人员的签名图片（在证书授权签字后插入）
	 * 程序调用后判断如果    (outFile.exists()==false || outFile.length() == 0)，则说明没有插入图片，否则说明已插入图片重新生成一份原始记录Excel文件
	 * @param inFile
	 * @param outFile
	 * @param fImgVerify ：检定人员签名图片文件
	 * @param fImgChecker:核验人员签名图片文件
	 * @param parser
	 */
	public static void insertWorkerSignatureImgToExcel(File inFile, File outFile, File fImgVerify, File fImgChecker, ParseXMLAll parser) throws Exception{
		InputStream in = null;
		OutputStream os = null;
		BufferedImage bufferImg = null;
		ByteArrayOutputStream byteArrayVerify = null, byteArrayChecker = null;
		
		boolean isInserted = false;	//是否已经签名
		final String keyName = "Name";	//标签属性名称（检校人员：姓名）
		final String fieldClass = "com.jlyw.hibernate.SysUser";	//类对象
		final String VerifyClassDesc = "检校人员";
		final String CheckerClassDesc = "核验人员";
		double imgHeightVerify = 0.0, imgHeightChecker = 0.0;	//检/校人员、核定人员签名图片的高度(以磅为单位，3磅=4像素)
		try{
			if(fImgVerify != null && fImgVerify.exists() && fImgVerify.length() > 0){
				//先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray  
				byteArrayVerify = new ByteArrayOutputStream();
				bufferImg = ImageIO.read(fImgVerify);
				imgHeightVerify = bufferImg.getHeight() * 0.75;	//转换为以磅为单位
				ImageIO.write(bufferImg, "png", byteArrayVerify);
			}
			
			if(fImgChecker != null && fImgChecker.exists() && fImgChecker.length() > 0){
				//先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray  
				byteArrayChecker = new ByteArrayOutputStream();
				bufferImg = ImageIO.read(fImgChecker);
				imgHeightChecker = bufferImg.getHeight() * 0.75; //转换为以磅为单位
				ImageIO.write(bufferImg, "png", byteArrayChecker);
			}
			
			in = new FileInputStream(inFile);
			os = new FileOutputStream(outFile);
			
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			
			//循环判断是否有检校人员或核验人员 ‘姓名’ 的字段内容

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
				
				// 创建对工作表的引用。
				HSSFSheet sheet = getSheet(workbook, attr);
				HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
				
				//交互的字段
				if(attr.getValue("type").contains("w")){	//字段类型：写入文件
					try {
						if(attr.getValue("fieldClassDesc").equals(VerifyClassDesc) && byteArrayVerify != null){
							retObj = "";
							//先将检校人员的对应位置写入空字符（去掉打印的 名字）
							setCellValue(sheet, attr, retObj);
													
							//插入图片
							int rowIndex = Integer.parseInt(attr.getValue("rowIndex"));
							int colIndex = Integer.parseInt(attr.getValue("colIndex"));
							HSSFRow row = sheet.getRow(rowIndex);	//获取行
							double rowHeight = row.getHeight() / 20.0;	//行高(以磅为单位)
							double imgZoomScale = (rowHeight <= 0 || imgHeightVerify <= 0)?1:rowHeight*1.0/imgHeightVerify;
							HSSFClientAnchor anchor = new HSSFClientAnchor((int)rowHeight,0,0,0,(short)colIndex,rowIndex,(short)(colIndex+1),rowIndex+1);;       
//							patriarch.createPicture(anchor, workbook.addPicture(byteArrayVerify.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize(1);	//保持保持原始的图片大小，图片显示的终了cell位置就不起作用了
							patriarch.createPicture(anchor, workbook.addPicture(byteArrayVerify.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize(imgZoomScale);
							isInserted = true;
							
						}else if(attr.getValue("fieldClassDesc").equals(CheckerClassDesc) && byteArrayChecker != null){
							retObj = "";
							//先将核验人员的对应位置写入空字符（去掉打印的 名字）
							setCellValue(sheet, attr, retObj);
							
							//插入图片
							int rowIndex = Integer.parseInt(attr.getValue("rowIndex"));
							int colIndex = Integer.parseInt(attr.getValue("colIndex"));
							HSSFRow row = sheet.getRow(rowIndex);	//获取行
							double rowHeight = row.getHeight() / 20.0;	//行高(以磅为单位)
							double imgZoomScale = (rowHeight <= 0 || imgHeightChecker <= 0)?1:rowHeight*1.0/imgHeightChecker;							
							HSSFClientAnchor anchor = new HSSFClientAnchor((int)rowHeight,0,0,0,(short)colIndex,rowIndex,(short)(colIndex+1),rowIndex+1);;       
//							patriarch.createPicture(anchor, workbook.addPicture(byteArrayChecker.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize(1);	//保持保持原始的图片大小，图片显示的终了cell位置就不起作用了
							patriarch.createPicture(anchor, workbook.addPicture(byteArrayChecker.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize(imgZoomScale);
							isInserted = true;
						}
					}catch(Exception e){
						e.printStackTrace();
						throw new Exception(String.format("在原始记录Excel中插入签名图片错误！错误信息：%s",e.getMessage()==null?"无":e.getMessage()));
					}
				}
			}
			if(isInserted){				
				//移除证书的表单：用于打印不显示证书区域
				try{
					String certificateSheetName = parser.getFirstAttribute("certificate-sheet", "sheetName");	//获取证书的标签页名称
					workbook.removeSheetAt(workbook.getSheetIndex(certificateSheetName));
					isInserted = true;
				}catch(Exception exx){
					log.debug("exception in ExcelUtil->insertWorkerSignatureImgToExcel->移除证书的表单", exx);
				}
				//删除原始记录上的附加信息区域：用于打印不显示附加信息区域
				try{
					String additionInfoSheetName = parser.getFirstAttribute("addition-info-region", "sheetName");	//附加信息区域的标签页名称
					String regionBeginColIndex = parser.getFirstAttribute("addition-info-region", "regionBeginColIndex");
					String regionEndColIndex = parser.getFirstAttribute("addition-info-region", "regionEndColIndex");
					if(additionInfoSheetName != null && regionBeginColIndex != null && regionEndColIndex != null){
						HSSFSheet sheet = workbook.getSheet(additionInfoSheetName);
						for(int i = Integer.parseInt(regionBeginColIndex); i <= Integer.parseInt(regionEndColIndex); i++){
							sheet.setColumnHidden(i, true);	//设置列隐藏
						}
						isInserted = true;
					}
				}catch(Exception exx){
					log.debug("exception in ExcelUtil->insertWorkerSignatureImgToExcel->隐藏附加信息区域", exx);
				}
				
				//将文件写入输出流
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
//			workbook.removeSheetAt(workbook.getSheetIndex("证书"));
//			HSSFSheet sheet = workbook.getSheet("记录");
//			for(int i = 0; i < 10; i++){
//				sheet.setColumnHidden(i, true);	//设置列隐藏
//			}
//			workbook.write(new FileOutputStream("D:\\TestOut.xls"));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}
	
	
	/**
	 * 批量上传原始记录Excel文件
	 * 对于OriginalRecord对应的字段，仅从文件中读出（不写入Excel文件）
	 * 对于委托单、检校人员对应的字段，根据需要仅写入Excel文件(不从Excel文件中读取写入数据库)
	 * @param is
	 * @param os
	 * @param cSheet
	 * @param oRecord
	 * @param staff 检/校人员
	 * @param certificate 对应证书
	 * @param xlsParser
	 * @param oRecord:原始记录（用于带出相关信息，方法初始执行时oRecord的ID为null）
	 * @param certificate:证书（用于预留证书编号以及将证书的模板文件名称从方法执行后带回，方法初始执行时certificate的ID为null）
	 * @param verAndAuth:核验和 授权签字记录（用于带出相关信息，方法初始执行时verAndAuth的ID为null）
	 * @param specMap：技术规范Map（用于带出相关数据，Integer存放的为Specification的ID）
	 * @param stdMap:计量标准Map（用于带出相关数据，Integer存放的为Standard的ID）
	 * @param stdAppMap：标准器具Map（用于带出相关数据，Integer存放的为StandardAppliance的ID）
	 * @return 
	 * @throws IOException 处理出错时抛出异常
	 */
	public static void uploadExcelByBatch(InputStream is, OutputStream os, 
			CommissionSheet cSheet, ApplianceStandardName stdName, 
			ParseXMLAll xlsParser, 
			OriginalRecord oRecord, Certificate certificate, VerifyAndAuthorize veriAndAuth, 
			Map<Integer, Specification> specMap, Map<Integer, Standard> stdMap, Map<Integer, StandardAppliance> stdAppMap) throws Exception{
		
		// 创建对Excel工作簿文件的引用
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		
		List<Attributes> handledAttrList = new ArrayList<Attributes>();	//已处理过的属性列表
		
		//校验委托单位
		List<Attributes> attrList = xlsParser.getAttributesByPropertyValue("CustomerName", "fieldClass", FieldClassCommissionSheet);	//验证委托单单位名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘委托单位’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || !obj.toString().equals(cSheet.getCustomerName())){
				throw new Exception(String.format("Excel中的委托单位:'%s'(单元格:%s)与委托单上的委托单位:'%s'不匹配！", 
						obj==null?"":obj.toString(), 
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						cSheet.getCustomerName()));
			}
		}
		//校验标准名称
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassApplianceStandardName);	//验证器具标准名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘标准名称’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || !obj.toString().equals(stdName.getName())){
				throw new Exception(String.format("Excel中的标准名称:'%s'(单元格:%s)与模板文件的标准名称:'%s'不匹配！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						stdName.getName()));
			}
		}
		//获取型号规格、测量范围、准确度等级（查找受检器具）
		if(xlsParser.getQNameCount("target-appliance-link-to") == 0){
			throw new Exception(String.format("配置文件出错：未配置受检器具关联项(%s)，请联系模板文件管理员！", "target-appliance-link-to"));
		}
		Attributes tAppLinkAttr = xlsParser.getAttributes("target-appliance-link-to", 0);
		boolean bModelLinked = (tAppLinkAttr.getValue("model") != null && tAppLinkAttr.getValue("model").equalsIgnoreCase("true"))?true:false;	//受检器具是否与型号规格有关
		boolean bRangeLinked = (tAppLinkAttr.getValue("range") != null && tAppLinkAttr.getValue("range").equalsIgnoreCase("true"))?true:false;	//受检器具是否与测量范围有关
		boolean bAccuracyLinked = (tAppLinkAttr.getValue("accuracy") != null && tAppLinkAttr.getValue("accuracy").equalsIgnoreCase("true"))?true:false;	//受检器具是否与不确定度有关
		String model = null, range = null, accuracy = null;
		attrList = xlsParser.getAttributesByPropertyValue("Model", "fieldClass", FieldClassOriginalRecord);	//型号规格
		if(attrList.size() == 0 && bModelLinked){
			throw new Exception("‘型号规格’是该受检器具的关联项，但在Excel文件中找不到‘型号规格’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && model != null && !model.equals(obj)){
				throw new Exception(String.format("Excel中的有多处型号规格且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						model));
			}else{
				model = (obj==null)?null:obj.toString();
			}
		}
		attrList = xlsParser.getAttributesByPropertyValue("Range", "fieldClass", FieldClassOriginalRecord);	//测量范围
		if(attrList.size() == 0 && bRangeLinked){
			throw new Exception("‘测量范围’是该受检器具的关联项，但在Excel文件中找不到‘测量范围’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && range != null && !range.equals(obj)){
				throw new Exception(String.format("Excel中的有多处测量范围且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						range));
			}else{
				range = (obj==null)?null:obj.toString();
			}
		}
		attrList = xlsParser.getAttributesByPropertyValue("Accuracy", "fieldClass", FieldClassOriginalRecord);	//不确定度
		if(attrList.size() == 0 && bAccuracyLinked){
			throw new Exception("‘准确度等级’是该受检器具的关联项，但在Excel文件中找不到‘准确度等级’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && accuracy != null && !accuracy.equals(obj)){
				throw new Exception(String.format("Excel中的有多处准确度等级且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
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
			throw new Exception(String.format("找不到符合条件的受检器具！标准名称:%s%s%s%s", 
					stdName.getName(),
					bModelLinked?",型号规格:"+(model==null?"":model):"",
					bRangeLinked?",测量范围:"+(range==null?"":range):"",
					bAccuracyLinked?",准确度等级:"+(accuracy==null?"":accuracy):""));
		}
		if(tAppList.size() > 1){
			throw new Exception(String.format("找到 %s 条符合条件的受检器具！标准名称:%s%s%s%s", 
					tAppList.size(),
					stdName.getName(),
					bModelLinked?",型号规格:"+(model==null?"":model):"",
					bRangeLinked?",测量范围:"+(range==null?"":range):"",
					bAccuracyLinked?",准确度等级:"+(accuracy==null?"":accuracy):""));
		}
		TargetAppliance tApp = tAppList.get(0);	//受检器具
		
		//获取工作性质
		String workType = null;
		attrList = xlsParser.getAttributesByPropertyValue("WorkType", "fieldClass", FieldClassOriginalRecord);	//验证工作性质
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘工作性质’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null || (!obj.toString().equals(WorkType_Jianding) && !obj.toString().equals(WorkType_Jiaozhun) && !obj.toString().equals(WorkType_Jiance) && !obj.toString().equals(WorkType_Jianyan))){
				throw new Exception(String.format("Excel中的‘工作性质’无效:'%s'(单元格:%s)，该值必须为“检定/校准/检测/检验”中的一种",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(workType != null && !workType.equals(obj)){
				throw new Exception(String.format("Excel中的有多处‘工作性质’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						workType));
			}
			workType = obj.toString();
		}
		//获取检定/校准日期
		Date workDate = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date nowDate = new Date(calendar.getTimeInMillis());	//当前日期
		attrList = xlsParser.getAttributesByPropertyValue("WorkDate", "fieldClass", FieldClassOriginalRecord);	//获取检定/校准日期
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘检定/校准日期’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null){
				if(workDate == null || workDate.compareTo(nowDate) == 0){
					workDate = nowDate;
					setCellValue(sheet, attr, workDate);	//将值写入Excel文件中
				}else{
					throw new Exception(String.format("Excel中有多处‘检定/校准日期’且值不一致(若为空，则默认为今天):'%s'(单元格:%s) 与  '%s' 不一致！",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(workDate)));
				}
			}else if(workDate != null && workDate.compareTo((Date)obj) != 0){
				throw new Exception(String.format("Excel中有多处‘检定/校准日期’且值不一致(若为空，则默认为今天):'%s'(单元格:%s) 与  '%s'不一致！",
						obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						DateTimeFormatUtil.DateFormat.format(workDate)));
			}else{
				workDate = (Date)obj;
			}
		}
		//检验有效日期(若填写，则判断不可比检定日期早且不能比正常的有效期晚，若没有填写，则反填)
		Date validateDate = null, validateDateMax = null;
		if(tApp.getTestCycle() != null){	//计算最大的有效日期
			calendar.setTime(workDate);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + tApp.getTestCycle());
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	//有效期=检定日期+检定周期-1天
			validateDateMax = new java.sql.Date(calendar.getTimeInMillis());  	//有效期
		}
		attrList = xlsParser.getAttributesByPropertyValue("Validity", "fieldClass", FieldClassOriginalRecord);	//获取有效日期
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘有效日期’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj == null){	//没有填写：反填
				if(validateDate != null && (validateDateMax == null || validateDate.compareTo(validateDateMax) != 0)){
					throw new Exception(String.format("Excel中有多处‘有效日期’且值不一致(若为空，则默认为检定日期+检定周期):'%s'(单元格:%s) 与  '%s'不一致！",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(validateDate)));
				}
				validateDate = validateDateMax;
				setCellValue(sheet, attr, validateDate);	//将值写入Excel文件中
				
			}else{
				if(workDate.after((Date)obj)){
					throw new Exception(String.format("‘有效日期’:'%s'(单元格:%s) 不能早于 ‘检定/校准日期’:'%s' ！",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(workDate)));
				}
//				if(validateDateMax != null && validateDateMax.before((Date)obj)){
//					throw new Exception(String.format("‘有效日期’:'%s'(单元格:%s) 不能晚于 最大的有效期:'%s'！",
//							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
//							attr.getValue("cell")==null?"":attr.getValue("cell"),
//							DateTimeFormatUtil.DateFormat.format(validateDateMax)));
//				}
				if(validateDate != null && validateDate.compareTo((Date)obj) != 0){
					throw new Exception(String.format("Excel中有多处‘有效日期’且值不一致(若为空，则默认为检定日期+检定周期):'%s'(单元格:%s) 与  '%s'不一致！",
							obj==null?"":DateTimeFormatUtil.DateFormat.format((Date)obj),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							DateTimeFormatUtil.DateFormat.format(validateDate)));
				}else{
					validateDate = (Date)obj;
				}
			} 
		}
		
		//检验依据的技术文件
		TgtAppSpecManager tsMgr = new TgtAppSpecManager();
		attrList = xlsParser.getAttributesByPropertyValue("SpecificationCode", "fieldClass", FieldClassSpecification);	//技术规范：编号
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘依据技术文件（技术规范）的编号’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && obj.toString().length() > 0){
				List<TgtAppSpec> retList = tsMgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id", tApp.getId(), "="),
						new KeyValueWithOperator("specification.status", 1, "<>"),
						new KeyValueWithOperator("specification.specificationCode", obj.toString(), "="));
				if(retList.size() == 0){
					throw new Exception(String.format("找不到受检器具（标准名称:%s%s%s%s）所依据的技术文件 %s(单元格:%s)", 
							stdName.getName(),
							model==null?"":",型号规格:"+model,
							range==null?"":",测量范围:"+range,
							accuracy==null?"":",准确度等级:"+accuracy,
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}else{
					for(TgtAppSpec tas : retList){
						specMap.put(tas.getSpecification().getId(), tas.getSpecification());
					}	
				}
			}
		}
		//检验计量标准
		StdTgtAppManager stMgr = new StdTgtAppManager();
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassStandard);	//计量标准：名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘计量标准的名称’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && obj.toString().length() > 0){
				String stdNameValue = obj.toString(), stdRange = null, stdAccuracy = null, stdCertificateCode = null;
				Date stdValidate = null;
				
				boolean isStdVirtual = SystemCfgUtil.checkStandardVirtual(stdNameValue);	//是否为虚拟的计量标准
				if(isStdVirtual){
					setCellValue(sheet, attr, "");	//写入单元格为空字符串
				}
				
				String indexStr = attr.getValue("indexStr");
				if(indexStr == null){
					throw new Exception(String.format("模板定义文件错误：没有定义计量标准名称的顺序号(单元格：%s)", attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				List<Attributes> attrTempList = xlsParser.getAttributesByPropertyValues("Range", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的计量标准的测量范围！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdRange = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//写入单元格为空字符串
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("Uncertain", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的计量标准的准确度等级（或最大允许误差或不确定度）！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAccuracy = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//写入单元格为空字符串
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("CertificateCode", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的计量标准的证书编号！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdCertificateCode = (objTemp==null)?null:objTemp.toString();
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//写入单元格为空字符串
					}
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandard, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的计量标准的‘有效期至’！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdValidate = (objTemp==null)?null:(Date)objTemp;
					if(isStdVirtual){
						setCellValue(sheet, attrTemp, "");	//写入单元格为空字符串
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
					throw new Exception(String.format("找不到受检器具（标准名称:%s%s%s%s）所使用的计量标准(名称:%s%s%s%s%s)(单元格:%s)", 
							stdName.getName(),
							model==null?"":",型号规格:"+model,
							range==null?"":",测量范围:"+range,
							accuracy==null?"":",准确度等级:"+accuracy,
							stdNameValue,
							stdRange==null?"":",测量范围:"+stdRange,
							stdAccuracy==null?"":",不确定度:"+stdAccuracy,
							stdCertificateCode==null?"":",证书编号:"+stdCertificateCode,
							stdValidate==null?"":",有效期至："+DateTimeFormatUtil.DateFormat.format(stdValidate),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}else{
					stdMap.put(retList.get(0).getStandard().getId(), retList.get(0).getStandard());	//取第一条（因为若有效期没填，则取有效期最迟的那条记录）
					if(stdValidate == null){	//有效期为空，则反填至Excel文件
						stdValidate = retList.get(0).getStandard().getValidDate();
						attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandard, "indexStr", indexStr);
						for(Attributes attrTemp : attrTempList){
							HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
							setCellValue(sheetTemp, attrTemp, stdValidate);
						}
					}
					//判断有效期是否在检定日期之后
					if(stdValidate != null && workDate.after(stdValidate)){
						throw new Exception(String.format("'检定/校准日期(若为空，则默认为今天)：'%s' 不能在所使用的计量标准(名称:%s%s%s%s%s)的有效期之后'", 
								DateTimeFormatUtil.DateFormat.format(workDate),
								stdNameValue,
								stdRange==null?"":",测量范围:"+stdRange,
								stdAccuracy==null?"":",不确定度:"+stdAccuracy,
								stdCertificateCode==null?"":",证书编号:"+stdCertificateCode,
								stdValidate==null?"":",有效期至："+DateTimeFormatUtil.DateFormat.format(stdValidate)));
					}
				}
			}
		}
		//检验标准器具
		TgtAppStdAppManager tsAppMgr = new TgtAppStdAppManager();
		attrList = xlsParser.getAttributesByPropertyValue("Name", "fieldClass", FieldClassStandardAppliance);	//标准器具：名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘主要标准器具的名称’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Object obj = getCellValue(sheet, attr);
			if(obj != null && obj.toString().length() > 0){
				String stdAppName = obj.toString(), stdAppRange = null, stdAppAccuracy = null, stdAppSeriaNumber = null;
				Date stdAppValidate = null;
				
				String indexStr = attr.getValue("indexStr");
				if(indexStr == null){
					throw new Exception(String.format("模板定义文件错误：没有定义‘主要标准器具’的顺序号(单元格：%s)", attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				List<Attributes> attrTempList = xlsParser.getAttributesByPropertyValues("Range", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的‘主要标准器具’的测量范围！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppRange = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("Uncertain", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的‘主要标准器具’的准确度等级（或最大允许误差或不确定度）！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppAccuracy = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("SeriaNumber", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的‘主要标准器具’的证书编号！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppSeriaNumber = (objTemp==null)?null:objTemp.toString();
				}
				attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
				if(attrTempList.size() > 1){
					throw new Exception(String.format("模板定义文件错误：有%s个顺序号相同(%s)的‘主要标准器具’的‘有效期至’！", attrTempList.size(), indexStr));
				}else if(attrTempList.size() == 1){
					Attributes attrTemp = attrTempList.get(0);
					handledAttrList.add(attrTemp);	//添加已处理属性列表
					HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
					Object objTemp = getCellValue(sheetTemp, attrTemp);
					stdAppValidate = (objTemp==null)?null:(Date)objTemp;
				}
				if(stdMap.size() == 0){
					throw new Exception(String.format("找不到受检器具（标准名称:%s%s%s%s）所使用的‘主要标准器具’(名称:%s%s%s%s%s)(单元格:%s),原因：未指定计量标准！", 
							stdName.getName(),
							model==null?"":",型号规格:"+model,
							range==null?"":",测量范围:"+range,
							accuracy==null?"":",准确度等级:"+accuracy,
							stdAppName,
							stdAppRange==null?"":",测量范围:"+stdAppRange,
							stdAppAccuracy==null?"":",不确定度:"+stdAppAccuracy,
							stdAppSeriaNumber==null?"":",证书编号:"+stdAppSeriaNumber,
							stdAppValidate==null?"":",有效期至："+DateTimeFormatUtil.DateFormat.format(stdAppValidate),
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
					throw new Exception(String.format("找不到受检器具（标准名称:%s%s%s%s）在指定计量标准下所使用的‘主要标准器具’(名称:%s%s%s%s%s)(单元格:%s)", 
							stdName.getName(),
							model==null?"":",型号规格:"+model,
							range==null?"":",测量范围:"+range,
							accuracy==null?"":",准确度等级:"+accuracy,
							stdAppName,
							stdAppRange==null?"":",测量范围:"+stdAppRange,
							stdAppAccuracy==null?"":",不确定度:"+stdAppAccuracy,
							stdAppSeriaNumber==null?"":",证书编号:"+stdAppSeriaNumber,
							stdAppValidate==null?"":",有效期至："+DateTimeFormatUtil.DateFormat.format(stdAppValidate),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}else{
					stdAppMap.put(retList.get(0).getStandardAppliance().getId(), retList.get(0).getStandardAppliance());	//取第一条
					if(stdAppValidate == null && retList.get(0).getStandardAppliance().getValidDate() != null){	//有效期为空，则反填至Excel文件
						stdAppValidate = retList.get(0).getStandardAppliance().getValidDate();
						attrTempList = xlsParser.getAttributesByPropertyValues("ValidDate", "fieldClass", FieldClassStandardAppliance, "indexStr", indexStr);
						for(Attributes attrTemp : attrTempList){
							HSSFSheet sheetTemp = getSheet(workbook, attrTemp);
							setCellValue(sheetTemp, attrTemp, stdAppValidate);
						}
					}
					//判断有效期是否在检定日期之后
					if(stdAppValidate != null && workDate.after(stdAppValidate)){
						throw new Exception(String.format("'检定/校准日期(若为空，则默认为今天)：'%s' 不能在所使用的‘主要标准器具’(名称:%s%s%s%s%s)的有效期之后'", 
								DateTimeFormatUtil.DateFormat.format(workDate),
								stdAppName,
								stdAppRange==null?"":",测量范围:"+stdAppRange,
								stdAppAccuracy==null?"":",不确定度:"+stdAppAccuracy,
								stdAppSeriaNumber==null?"":",证书编号:"+stdAppSeriaNumber,
								stdAppValidate==null?"":",有效期至："+DateTimeFormatUtil.DateFormat.format(stdAppValidate)));
					}
				}
			}
		}
		
		//核对：检定/校准人员（确定检定/校准人员 和 任务分配记录）
		SysUser workStaff = null;
		TaskAssign tAssign = null;	//任务分配记录
		TaskAssignManager tAssignMgr = new TaskAssignManager();
		attrList = xlsParser.getAttributesByPropertyValues("Name", "fieldClass", FieldClassSysUser, "fieldClassDesc", FieldClassDescVerify);	//验证检校人员
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘检定/校准人员’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				throw new Exception(String.format("Excel中的‘检定/校准人员’无效:'%s'(单元格:%s)！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(workStaff != null && !workStaff.getName().equalsIgnoreCase(obj.toString())){
				throw new Exception(String.format("Excel中的有多个‘检定/校准人员’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						workStaff.getName()));
			}
			if(workStaff == null){
				//查找所属的任务分配记录
				List<TaskAssign> tAssignList = tAssignMgr.findByVarProperty(new KeyValueWithOperator("sysUserByAlloteeId.name", obj.toString(), "="),
						new KeyValueWithOperator("commissionSheet.id", cSheet.getId(), "="),
						new KeyValueWithOperator("status", 1, "<>"));
				if(tAssignList.size() == 0){
					throw new Exception(String.format("该委托单没有'%s'的任务分配记录，如要继续请先为'%s'添加任务！", obj.toString(), obj.toString()));
				}
				tAssign = tAssignList.get(0);
				workStaff = tAssignList.get(0).getSysUserByAlloteeId();
			}
		}
		//核对：核验人员
		SysUser checkStaff = null;
		QualificationManager qfMgr = new QualificationManager();
		UserManager userMgr = new UserManager();
		List<Integer> qfTypeList = new ArrayList<Integer>();
		qfTypeList.add(FlagUtil.QualificationType.Type_Heyan);
		attrList = xlsParser.getAttributesByPropertyValues("Name", "fieldClass", FieldClassSysUser, "fieldClassDesc", FieldClassDescChecker);	//验证核验人员
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘核验人员’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				throw new Exception(String.format("Excel中的‘核验人员’未指定:'%s'(单元格:%s)！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(checkStaff != null && !checkStaff.getName().equalsIgnoreCase(obj.toString())){
				throw new Exception(String.format("Excel中的有多个‘核验人员’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						checkStaff.getName()));
			}
			if(checkStaff == null){
				//检查是否有核验的资质
				List<Object[]> qfRetList = qfMgr.getQualifyUsers(obj.toString(), stdName.getId(), 0, qfTypeList);
				if(qfRetList.size() == 0){
					throw new Exception(String.format("Excel中的指定‘核验人员’:'%s'(单元格:%s) 没有器具标准名称'%s'的 核验资质！",
							obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							stdName.getName()));
				}
				Object[] userObj = qfRetList.get(0);
				checkStaff = userMgr.findById((Integer)userObj[0]);
				if(checkStaff != null && workStaff.getId().equals(checkStaff.getId())){
					throw new Exception(String.format("Excel中的指定‘核验人员’:'%s'(单元格:%s) 与 ‘检定/校准人员’:'%s'不能为同一个人！",
							obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							workStaff.getName()));
				}
			}
//			setCellValue(sheet, attr, "");	//将Excel中的核验人员置为""，等授权签字通过以后统一再填入（以省去网页上每次选择核验人员后需要更新Excel）
		}
		//获取授权签字人员
		SysUser authStaff = null;	//签字人
		List<Object[]> qfRetList = qfMgr.getVerifyOrAuthorizeQualifyUsers(stdName.getId(), 0, FlagUtil.QualificationType.Type_Qianzi);
		if(qfRetList.size() == 0){
			throw new Exception(String.format("找不到器具标准名称'%s'对应的授权签字人员！请联系基础数据管理员！", stdName.getName()));
		}
		Object[] userObj = qfRetList.get(0);
		authStaff = userMgr.findById((Integer)userObj[0]);
		
		//获取结论、证书模板文件名
		String conclusion = null;	//结论
		attrList = xlsParser.getAttributesByPropertyValue("Conclusion", "fieldClass", FieldClassOriginalRecord);	//获取结论
		if(attrList.size() == 0 && workType.equals(WorkType_Jianding)){
			throw new Exception("在Excel文件中找不到‘结论’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.trim().length() == 0) && workType.equals(WorkType_Jianding)){
				throw new Exception(String.format("Excel中的‘结论’值无效:'%s'(单元格:%s)，请输入一个有效值！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(conclusion != null && !conclusion.equals(obj)){
				throw new Exception(String.format("Excel中的有多处‘结论’且值不一致:'%s'(单元格:%s) 与  '%s'不一致！",
						obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						conclusion));
			}
			conclusion = obj.toString();
		}
		certificate.setFileName(getCertificateModFileName(workType, conclusion));	//获取证书模板文件名称
		
		//获取器具名称(常用名称)
		String applianceName = null;
		attrList = xlsParser.getAttributesByPropertyValue("ApplianceName", "fieldClass", FieldClassOriginalRecord);	//获取器具名称
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘器具名称（常用名称）’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				if(applianceName != null && !applianceName.equals(stdName.getName())){
					throw new Exception(String.format("Excel中有多个器具名称（若为空，则默认为标准名称）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							applianceName));
				}else{
					setCellValue(sheet, attr, stdName.getName());
					applianceName = stdName.getName();
				}
			}else if(applianceName != null && !applianceName.equals(obj)){
				throw new Exception(String.format("Excel中有多个器具名称（若为空，则默认为标准名称）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						applianceName));
			}else{
				applianceName = obj;
			}
		}
		
		//获取器具数量
		Integer applianceQuantity = null;
		attrList = xlsParser.getAttributesByPropertyValue("Quantity", "fieldClass", FieldClassOriginalRecord);	//验证器具数量
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Integer obj = (Integer)getCellValue(sheet, attr);
			if(obj == null){
				throw new Exception(String.format("Excel中的器具数量:'%s'(单元格:%s)为空！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(obj <= 0){
				throw new Exception(String.format("Excel中的器具数量无效:'%s'(单元格:%s)为空！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}
			if(applianceQuantity != null && !applianceQuantity.equals(obj)){
				throw new Exception(String.format("Excel中有多个器具数量且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						applianceQuantity.toString()));
			}
			//判断有效器具数量是否大于委托单的总器具数量
			Integer existedNumber = 0;
			List<Long> existedCountList = new OriginalRecordManager().findByHQL("select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 ", 
					cSheet.getId());
			if(existedCountList.get(0) != null){
				existedNumber = existedCountList.get(0).intValue();
			}
			if(obj + existedNumber > cSheet.getQuantity()){
				throw new Exception(String.format("Excel中的器具数量'%s'(单元格:%s)无效:委托单下所有原始记录的器具总数'%s'不得超过委托单的器具数量'%s'！",
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
		
		/****************        处理费用信息          ******************/
		//检定费(如果没填，则默认收费标准；否则，进行校验。如果收费标准为null或0，则以用户输入为准)
		Double testFee = null;
		Double testFeeStandard = tApp.getFee()==null?0:tApp.getFee()*applianceQuantity;	//收费标准(待修改：后面要添加判断是否存在报价单)
		attrList = xlsParser.getAttributesByPropertyValue("TestFee", "fieldClass", FieldClassOriginalRecord);
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘检定费’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(obj == null){
				if(testFee != null && !testFee.equals(testFeeStandard)){
					throw new Exception(String.format("Excel中有多个检定费（若为空，则默认为标准费用）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							testFee));
				}else{
					testFee = testFeeStandard;
					setCellValue(sheet, attr, testFee);
				}
			}else if(testFee != null && !testFee.equals(obj)){
				throw new Exception(String.format("Excel中有多个检定费（若为空，则默认为标准费用）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						testFee));
			}else{
				if(testFeeStandard > 0 && !testFeeStandard.equals(obj)){
					throw new Exception(String.format("Excel中检定费与收费标准不一致:'%s'(单元格:%s) 与 标准费用('%s') 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							testFeeStandard));
				}
				testFee = obj;
			}
		}
		//修理级别、修理费（根据修理级别来判断修理费标准）
		String repairLevel = null;
		attrList = xlsParser.getAttributesByPropertyValue("RepairLevel", "fieldClass", FieldClassOriginalRecord);
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘修理级别’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if(obj == null || obj.trim().length() == 0){
				if(repairLevel != null && !repairLevel.equals("")){
					throw new Exception(String.format("Excel中有多个修理级别且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairLevel));
				}else{
					repairLevel = "";
				}
			}else if(repairLevel != null && !repairLevel.equals(obj)){
				throw new Exception(String.format("Excel中有多个修理级别且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						repairLevel));
			}else{
				repairLevel = obj;
			}
		}
		Double repairFee = null;
		Double repairFeeStandard = 0.0;		//收费标准
		if(repairLevel != null && repairLevel.equals("小")){
			repairFeeStandard = tApp.getSrfee()==null?0:tApp.getSrfee()*applianceQuantity;
		}else if(repairLevel != null && repairLevel.equals("中")){
			repairFeeStandard = tApp.getMrfee()==null?0:tApp.getMrfee()*applianceQuantity;
		}else if(repairLevel != null && repairLevel.equals("大")){
			repairFeeStandard = tApp.getLrfee()==null?0:tApp.getLrfee()*applianceQuantity;
		}
		attrList = xlsParser.getAttributesByPropertyValue("RepairFee", "fieldClass", FieldClassOriginalRecord);	//修理费
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘修理费’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(obj == null){
				if(repairFee != null && !repairFee.equals(repairFeeStandard)){
					throw new Exception(String.format("Excel中有多个修理费（若为空，则默认为标准费用）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairFee));
				}else{
					repairFee = repairFeeStandard;
					setCellValue(sheet, attr, repairFee);
				}
			}else if(repairFee != null && !repairFee.equals(obj)){
				throw new Exception(String.format("Excel中有多个修理费（若为空，则默认为标准费用）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						repairFee));
			}else{
				if(repairFeeStandard > 0 && !repairFeeStandard.equals(obj)){
					throw new Exception(String.format("Excel中修理费与收费标准不一致:'%s'(单元格:%s) 与 标准费用('%s') 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							repairFeeStandard));
				}
				repairFee = obj;
			}
		}
		//配件费、配件明细：当有配件费时，配件明细不能为空！
		Double materialFee = null;
		attrList = xlsParser.getAttributesByPropertyValue("MaterialFee", "fieldClass", FieldClassOriginalRecord);	//配件费
		if(attrList.size() == 0){
			throw new Exception("在Excel文件中找不到‘配件费’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(obj == null){
				if(materialFee != null && materialFee != 0){
					throw new Exception(String.format("Excel中有多个配件费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							materialFee));
				}else{
					materialFee = 0.0;
				}
			}else if(materialFee != null && !materialFee.equals(obj)){
				throw new Exception(String.format("Excel中有多个配件费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						materialFee));
			}else{
				materialFee = obj;
			}
		}
		String materialDetail = null;
		attrList = xlsParser.getAttributesByPropertyValue("MaterialDetail", "fieldClass", FieldClassOriginalRecord);	//配件明细
		if(attrList.size() == 0 && materialFee != null && materialFee > 0){
			throw new Exception("在Excel文件中找不到‘配件明细’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			String obj = (String)getCellValue(sheet, attr);
			if((obj == null || obj.trim().length() == 0) && materialFee != null && materialFee > 0){
				throw new Exception(String.format("Excel中配件明细为空:'%s'(单元格:%s)！请输入有效内容！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell")));
			}else if(materialDetail != null && !materialDetail.equals(obj)){
				throw new Exception(String.format("Excel中有多个配件明细且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						materialDetail));
			}else{
				materialDetail = obj;
			}
		}
		//交通费：现场才有交通费
		Double carFee = null;
		attrList = xlsParser.getAttributesByPropertyValue("CarFee", "fieldClass", FieldClassOriginalRecord);	//交通费
		if(attrList.size() == 0 && cSheet.getCommissionType() == 2){	//现场检测
			throw new Exception("在Excel文件中找不到‘交通费’！");
		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			Double obj = (Double)getCellValue(sheet, attr);
			if(obj == null){
				if(carFee != null && carFee != 0){
					throw new Exception(String.format("Excel中有多个交通费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell"),
							carFee));
				}else{
					carFee = 0.0;
				}
			}else if(carFee != null && !carFee.equals(obj)){
				throw new Exception(String.format("Excel中有多个交通费（若为空，则默认为0）且值不一致:'%s'(单元格:%s) 与 '%s' 不一致！",
						obj==null?"":obj.toString(),
						attr.getValue("cell")==null?"":attr.getValue("cell"),
						carFee));
			}else{
				if(obj > 0 && cSheet.getCommissionType() != 2){	//交通费不为0，且不是现场检测
					throw new Exception(String.format("该委托单的委托形式不是现场检测，不能有交通费'%s'(单元格:%s)！",
							obj==null?"":obj.toString(),
							attr.getValue("cell")==null?"":attr.getValue("cell")));
				}
				carFee = obj;
			}
		}
		//反填：工作地点
		String workLocation = cSheet.getCommissionType()==2?"被测仪器使用现场":"本所实验室";
		attrList = xlsParser.getAttributesByPropertyValue("WorkLocation", "fieldClass", FieldClassOriginalRecord);
//		if(attrList.size() == 0){
//			throw new Exception("在Excel文件中找不到‘工作地点’！");
//		}
		for(Attributes attr : attrList){
			handledAttrList.add(attr);	//添加已处理属性列表
			HSSFSheet sheet = getSheet(workbook, attr);
			setCellValue(sheet, attr, workLocation);
		}
		
		
		//更新oRecord
		oRecord.setAccuracy(accuracy);
		oRecord.setConclusion(conclusion);
		oRecord.setModel(model);
		oRecord.setRange(range);
		oRecord.setSysUserByStaffId(workStaff);
		oRecord.setTargetAppliance(tApp);
		oRecord.setTaskAssign(tAssign);
		oRecord.setValidity(validateDate);
		oRecord.setWorkDate(workDate);
		oRecord.setWorkLocation(workLocation);	//工作地点
		oRecord.setWorkType(workType);
		oRecord.setQuantity(applianceQuantity);	//器具数量
		oRecord.setApplianceName(applianceName);
		
		oRecord.setTestFee(testFee);
		oRecord.setRepairLevel(repairLevel);
		oRecord.setRepairFee(repairFee);
		oRecord.setMaterialFee(materialFee);
		oRecord.setMaterialDetail(materialDetail);
		oRecord.setCarFee(carFee);
		
		//更新veriAndAuth
		veriAndAuth.setSysUserByVerifierId(checkStaff);
		veriAndAuth.setSysUserByAuthorizerId(authStaff);
		
		//处理其它的信息
		uploadExcelWithOtherInfo(workbook, cSheet, oRecord, stdName, certificate, xlsParser, handledAttrList);
		
		//输入总费用
		boolean bTotalFee = false;	//总费用是否存在
		Double totalFee = new Double(0.0);
		if(oRecord.getTestFee() != null){	//检测费
			bTotalFee = true;
			if(oRecord.getTestFee().intValue() != oRecord.getTestFee()){	//有小数点
				oRecord.setTestFee(new Double(Math.round(oRecord.getTestFee())));
			}
			totalFee += oRecord.getTestFee();
		}
		if(oRecord.getRepairFee() != null){	//修理费
			bTotalFee = true;
			if(oRecord.getRepairFee().intValue() != oRecord.getRepairFee()){	//有小数点
				oRecord.setRepairFee(new Double(Math.round(oRecord.getRepairFee())));
			}
			totalFee += oRecord.getRepairFee();
		}
		if(oRecord.getMaterialFee() != null){	//材料费
			bTotalFee = true;
			if(oRecord.getMaterialFee().intValue() != oRecord.getMaterialFee()){	//有小数点
				oRecord.setMaterialFee(new Double(Math.round(oRecord.getMaterialFee())));
			}
			totalFee += oRecord.getMaterialFee();
		}
		if(oRecord.getCarFee() != null){	//交通费
			bTotalFee = true;
			if(oRecord.getCarFee().intValue() != oRecord.getCarFee()){	//有小数点
				oRecord.setCarFee(new Double(Math.round(oRecord.getCarFee())));
			}
			totalFee += oRecord.getCarFee();
		}
		if(oRecord.getDebugFee() != null){	//调试费
			bTotalFee = true;
			if(oRecord.getDebugFee().intValue() != oRecord.getDebugFee()){	//有小数点
				oRecord.setDebugFee(new Double(Math.round(oRecord.getDebugFee())));
			}
			totalFee += oRecord.getDebugFee();
		}if(oRecord.getOtherFee() != null){	//其他费
			bTotalFee = true;
			if(oRecord.getOtherFee().intValue() != oRecord.getOtherFee()){	//有小数点
				oRecord.setOtherFee(new Double(Math.round(oRecord.getOtherFee())));
			}
			totalFee += oRecord.getOtherFee();
		}
		if(bTotalFee){
			oRecord.setTotalFee(totalFee);
		}else{
			oRecord.setTotalFee(null);
		}
		
		//将文件写入输出流
		workbook.write(os);
		os.flush();
	}
	
	/**
	 * 批量上传原始记录Excel文件
	 * 对于OriginalRecord对应的字段，仅从文件中读出（不写入Excel文件）
	 * 对于委托单、检校人员对应的字段，根据需要仅写入Excel文件(不从Excel文件中读取写入数据库)
	 * @param in
	 * @param osFile
	 * @param cSheet
	 * @param oRecord
	 * @param staff 检/校人员
	 * @param certificate 对应证书
	 * @param xlsParser
	 * @param oRecord:原始记录（用于带出相关信息，方法初始执行时oRecord的ID为null）
	 * @param certificate:证书（用于预留证书编号以及将证书的模板文件名称从方法执行后带回，方法初始执行时certificate的ID为null）
	 * @param verAndAuth:核验和 授权签字记录（用于带出相关信息，方法初始执行时verAndAuth的ID为null）
	 * @param specMap：技术规范Map（用于带出相关数据，Integer存放的为Specification的ID）
	 * @param stdMap:计量标准Map（用于带出相关数据，Integer存放的为Standard的ID）
	 * @param stdAppMap：标准器具Map（用于带出相关数据，Integer存放的为StandardAppliance的ID）
	 * @return 
	 * @throws IOException 处理出错时抛出异常
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
	 * 获取工作表
	 * @param workbook
	 * @param Attributes attributes
	 * @return
	 * @throws Exception
	 */
	private static HSSFSheet getSheet(HSSFWorkbook workbook, Attributes attributes) throws Exception{
		// 创建对工作表的引用。
		String sheetName = attributes.getValue("sheetName");
		if(sheetName == null){
			throw new Exception(String.format("读写入字段值:%s(单元格:%s)失败!原因：配置文件未指定工作表名称：sheetName", 
					attributes.getValue("desc")==null?"":attributes.getValue("desc"),
					attributes.getValue("cell")==null?"":attributes.getValue("cell")));
		}
		return workbook.getSheet(sheetName);
	}
	
	/**
	 * 从Excel表中获取单元格的值
	 * @param sheet
	 * @param attributes
	 * @return
	 * @throws Exception
	 */
	private static Object getCellValue(HSSFSheet sheet, Attributes attributes) throws Exception{
		try{
			HSSFRow row = sheet.getRow(Integer.parseInt(attributes.getValue("rowIndex")));	//获取行
			HSSFCell cell = row.getCell(Integer.parseInt(attributes.getValue("colIndex")));//获取列
			
			int cellType = cell.getCellType();
			String cellVal = null;		//获取单元格字符串
	//		cell.setCellType(HSSFCell.CELL_TYPE_STRING);	//设置单元格类型为字符串类型
	//		String cellVal = cell.getStringCellValue();		//获取单元格字符串
			
			if(cellType == HSSFCell.CELL_TYPE_NUMERIC){
				if(HSSFDateUtil.isCellDateFormatted(cell)){	//日期格式
					cellVal = String.format("%f", cell.getNumericCellValue());
				}else{	//普通数字
					cellVal = String.valueOf(cell.getNumericCellValue());
				}
			}else if(cellType == HSSFCell.CELL_TYPE_BOOLEAN){
				cellVal = cell.getBooleanCellValue()?"true":"false";
			}else if(cellType == HSSFCell.CELL_TYPE_STRING){
				cellVal = cell.getStringCellValue();
			}
			
			Object args = null;
			
			//判断参数类型
			Class paramClass = Class.forName(attributes.getValue("typeClass"));	//参数类型
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
						throw new Exception("值必须为浮点数！");
					}
				}
			}else if(Integer.class == paramClass){
				if(cellVal == null || cellVal.length() == 0){
					args = null;
				}else{
					try{
						args = (int)Double.parseDouble(cellVal);
					}catch(Exception ex){
						throw new Exception("值必须为整数！");
					}
				}
			}else if(java.sql.Date.class == paramClass){	//格式为yyyy-MM-dd
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
						throw new Exception("值必须为日期格式：yyyy-MM-dd[如2012-01-01]");
					}
				}
			}else if(java.sql.Timestamp.class == paramClass){	//格式为yyyy-MM-dd HH:mm:ss
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
						throw new Exception("值必须为时间格式：yyyy-MM-dd HH:mm:ss[如2012-01-01 08:00:01]");
					}
				}
			}else{
				args = cellVal;
			}
			
			return args;
		}catch(Exception e){
			throw new Exception(String.format("从Excel中读出字段值:%s(单元格:%s) 失败。%s", 
						attributes.getValue("desc")==null?"":attributes.getValue("desc"),
						attributes.getValue("cell")==null?"":attributes.getValue("cell"),
						e.getMessage() == null?"":String.format("(原因：%s)", e.getMessage())));
		}
	}
	/**
	 * 往Excel中写入字段值
	 * @param sheet
	 * @param attributes
	 * @param objValue
	 * @throws Exception
	 */
	private static void setCellValue(HSSFSheet sheet, Attributes attributes, Object objValue) throws Exception{
		try{
			HSSFRow row = sheet.getRow(Integer.parseInt(attributes.getValue("rowIndex")));	//获取行
			HSSFCell cell = row.getCell(Integer.parseInt(attributes.getValue("colIndex")));//获取列
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);	//设置单元格类型为字符串类型
			//判断参数类型
			Class typeClass = Class.forName(attributes.getValue("typeClass"));	//值类型
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
			throw new Exception(String.format("往Excel中写入字段值:%s(单元格:%s) 失败。%s", 
					attributes.getValue("desc")==null?"":attributes.getValue("desc"),
					attributes.getValue("cell")==null?"":attributes.getValue("cell"),
					e.getMessage() == null?"":String.format("(原因：%s)", e.getMessage())));
		}
	}
	
//	/**
//	 * 根据 工作性质 返回需要的资质类型列表
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
	 * 获取证书模板文件名称
	 * @param workType
	 * @param conclusion
	 * @return
	 */
	public static String getCertificateModFileName(String workType, String conclusion) throws Exception{
		if(WorkType_Jianding.equals(workType)){
			if(conclusion != null && conclusion.contains("不")){
				return "检定结果通知书.doc";
			}else{
				return "检定证书.doc";
			}
		}
		if(WorkType_Jiaozhun.equals(workType)){
			return "校准证书.doc";
		}
		if(WorkType_Jiance.equals(workType)){
			return "检测报告.doc";
		}
		if(WorkType_Jianyan.equals(workType)){
			return "检验报告.doc";
		}
		return null;
	}
	
	/**
	 * 上传Excel时处理不需要校验 的字段
	 * @param workbook
	 * @param cSheet
	 * @param oRecord
	 * @param stdName
	 * @param certificate
	 * @param xlsParser
	 * @param handledAttrsList
	 */
	private static void uploadExcelWithOtherInfo(HSSFWorkbook workbook, CommissionSheet cSheet, OriginalRecord oRecord, ApplianceStandardName stdName, Certificate certificate, ParseXMLAll xlsParser, List<Attributes> handledAttrsList) throws Exception{
		//循环写入需要的字段内容
		Iterator<String> keyIterator = xlsParser.getKeyIterator();
		Class c = null;
		Method method = null;
		Object retObj = null;
		Class paramClass = null;	//参数Class
		while(keyIterator.hasNext()){
			String key = keyIterator.next();
			
			for(int i = 0; i < xlsParser.getQNameCount(key); i++){
				c = null;
				method = null;
				retObj = null;
				paramClass = null;
				
				Attributes attr = xlsParser.getAttributes(key, i);
				if(handledAttrsList.contains(attr)){
					continue;	//已处理过的不再处理
				}
				if(attr.getValue("field") == null || 
						!attr.getValue("field").equalsIgnoreCase("true") ||
						attr.getValue("type") == null){
					continue;
				}
				HSSFSheet sheet = getSheet(workbook, attr);	//获取工作表
				
				//交互的字段
				if(attr.getValue("type").toLowerCase().contains("w")){	//字段类型：写入文件
					try {
						boolean bWriteToFile = true;
						c = Class.forName(attr.getValue("fieldClass"));	//获取对应的类对象
						method = c.getMethod(String.format("get%s", key.toString()));	//get方法
						if(c.isInstance(oRecord)){
							bWriteToFile = false;
							
//							retObj = method.invoke(oRecord);
//							//判断委托单是否为已完工，若已完工，则费用不允许修改！
//							if(cSheet.getStatus() >= 3){	//已完工确认或注销 :费用不允许修改
//								if(key.equals("TestFee") || key.equals("RepairFee") || key.equals("MaterialFee") || key.equals("CarFee") || key.equals("DebugFee") || key.equals("OtherFee") || key.equals("TotalFee")){
//									retObj = method.invoke(oRecord);
//								}else{
//									bWriteToFile = false;	//原始记录：以上传的Excel文件中的值为准
//								}
//							}else{
//								bWriteToFile = false;	//原始记录：以上传的Excel文件中的值为准
//							}
						}else if(c.isInstance(cSheet)){
							retObj = method.invoke(cSheet);
						}else if(c == SysUser.class){	//人员（检校人员和核验人员）：不处理（之前已经校验过）
							continue;
						}else if(c.isInstance(certificate)){
							retObj = method.invoke(certificate);
						}else if(c.isInstance(stdName)){	//标准名称:不处理（之前已经校验过）
							continue;
						}else if(c == Specification.class){	//技术规范:不处理
							continue;
						}else if(c == Standard.class){	//计量标准：不处理
							continue;
						}else if(c == StandardAppliance.class){	//标准器具：不处理
							continue;
						}
					
						if(retObj == null){	//没有找到对应的get方法或值为null
							retObj = "";
						}
						/**************将返回值写入Excel文件中*******************/
						if(bWriteToFile){
							setCellValue(sheet, attr, retObj);
						}
					}catch(Exception e){
						e.printStackTrace();
						log.debug("exception in ExcelUtil->uploadExcelWithOtherInfo", e);
						throw new Exception(String.format("写入字段值:%s(%s,单元格:%s) 失败！%s ", 
								key.toString(),
								attr.getValue("desc")==null?"":attr.getValue("desc"),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								e.getMessage() == null?"":"原因："+e.getMessage()));
					}
				}
				if(attr.getValue("type").toLowerCase().contains("r")){	//字段类型：从文件中读出，写入数据库
					try {
						c = Class.forName(attr.getValue("fieldClass"));	//获取对应的类对象
						paramClass = Class.forName(attr.getValue("typeClass"));
						method = c.getMethod(String.format("set%s", key.toString()), paramClass);	//set方法
						
						/*******只有原始记录对象可写入*************/
						if(c.isInstance(oRecord) && method != null){	//从Excel中取值，并写入数据库中
														
							//判断委托单是否为已完工，若已完工，则费用不允许修改！
							if(cSheet.getStatus() >= 3){	//已完工确认或注销 :费用不允许修改
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
						throw new Exception(String.format("读出字段值:%s(%s,单元格:%s) 失败！%s ", 
								key.toString(),
								attr.getValue("desc")==null?"":attr.getValue("desc"),
								attr.getValue("cell")==null?"":attr.getValue("cell"),
								e.getMessage() == null?"":"原因：" + e.getMessage()));
					}
				}
			}
		}
	}
	
	

}
