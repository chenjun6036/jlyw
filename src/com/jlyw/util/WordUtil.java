package com.jlyw.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.jlyw.hibernate.Address;
import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.Standard;
import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.TestLog;
import com.jlyw.manager.TestLogManager;
import com.jlyw.util.xmlHandler.ParseXML;
import com.jlyw.util.xmlHandler.ParseXMLAll;
/**
 * 对证书Word进行操作：生成证书
 * @author Administrator
 *
 */
public class WordUtil {
	private static final Log log = LogFactory.getLog(WordUtil.class);
	/**
	 * 私有构造函数：防止从外部实例化
	 */
	private WordUtil(){	}

	public static final SimpleDateFormat FormaterYear = new SimpleDateFormat("yyyy");
	public static final SimpleDateFormat FormaterMonth = new SimpleDateFormat("MM");
	public static final	SimpleDateFormat FormaterDay = new SimpleDateFormat("dd");
//	private static JacobWordWriter DocWriter = null;
//	private static Object SynBlock = new Object();
	
//	private static JacobWordWriter getDocWriter(){
//		if(DocWriter == null){
//			try {
//				DocWriter = new WordUtil().new JacobWordWriter();
//			} catch (Exception e) {
//				e.printStackTrace();
//				log.error("error in WordUtil->getDocWriter", e);
//			}
//		}
//		return DocWriter;
//	}
	/**
	 * 由原始记录Excel文件生成证书Word文件
	 * @param excel：原始记录Excel文件
	 * @param doc：证书Doc文件
	 * @param excelParser：原始记录字段定义
	 * @param docParser：证书配置
	 * @param verifierPicPath：检验员签名图片文件地址
	 * @param cSheet：委托单对象
	 * @param oRecord：原始记录对象
	 * @param certificate：证书
	 * @param dwAddress：台头地址对象
	 * @param alertString：字数超限时的提示信息
	 * 
	 * @return 返回新文件的路径名称
	 */
	public static boolean MakeCertificateWord(File excel, File doc, File fOutDoc, File fOutPdf, 
			ParseXMLAll excelParser, ParseXMLAll docParser, 
			String verifierPicPath, 
			CommissionSheet cSheet, OriginalRecord oRecord, Certificate certificate, Address dwAddress, 
			List<Specification> speList, List<Standard> stdList, List<StandardAppliance>stdAppList,StringBuilder alertString) throws Exception{
//		synchronized (SynBlock) {
			//初始化com的线程
			ComThread.InitSTA();
			
			JacobWordWriter docWriter = null;//getDocWriter();
//			try{
//				docWriter.openWord(doc.getAbsolutePath());
//			}catch(Exception e){
//				if(docWriter != null){
//					docWriter.releaseResourse();
//					DocWriter = null;
//				}
//				e.printStackTrace();
//				return false;
//			}
			ActiveXComponent xlsApp = null;
			Dispatch xlsWorkbook = null;
			File barCodeTempFile = null;	//证书编号条形码图片文件
			try{
				docWriter = new WordUtil().new JacobWordWriter();
				docWriter.openWord(doc.getAbsolutePath(), true);	//以只读方式打开
				String bookMarkKey = null;
				
				
				//技术规范、计量标准、标准器具对应
				Map<String, Specification> speMap = new HashMap<String, Specification>();	
				Map<String, Standard> stdMap = new HashMap<String, Standard>();
				Map<String, StandardAppliance> stdAppMap = new HashMap<String, StandardAppliance>();
				int curSpeIndex = 0, curStdIndex = 0, curStdAppIndex = 0;	//当前读取的技术规范/计量标准/标准器具的索引号
				
				/***********            台头单位信息                   ************/
				//台头名称（中文）
				for(int i = 0; i < docParser.getQNameCount("DwName"); i++){
					bookMarkKey = docParser.getAttribute("DwName", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getHeadName());
					}
				}
				//台头名称（英文）
				for(int i = 0; i < docParser.getQNameCount("DwNameEn"); i++){
					bookMarkKey = docParser.getAttribute("DwNameEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getHeadNameEn());
					}
				}
				//台头单位对应的证书地址（中文）
				for(int i = 0; i < docParser.getQNameCount("DwAddrName"); i++){
					bookMarkKey = docParser.getAttribute("DwAddrName", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getAddress());
					}
				}
				//台头单位对应的证书地址（英文）
				for(int i = 0; i < docParser.getQNameCount("DwAddrNameEn"); i++){
					bookMarkKey = docParser.getAttribute("DwAddrNameEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getAddressEn()==null?"":dwAddress.getAddressEn());
					}
				}
				//台头单位对应的邮政编码
				for(int i = 0; i < docParser.getQNameCount("DwZipCode"); i++){
					bookMarkKey = docParser.getAttribute("DwZipCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getZipCode()==null?"":dwAddress.getZipCode());
					}
				}
				//台头单位对应的网址
				for(int i = 0; i < docParser.getQNameCount("DwWebSite"); i++){
					bookMarkKey = docParser.getAttribute("DwWebSite", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getWebSite()==null?"":dwAddress.getWebSite());
					}
				}
				//台头单位对应的电话
				for(int i = 0; i < docParser.getQNameCount("DwTel"); i++){
					bookMarkKey = docParser.getAttribute("DwTel", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getTel()==null?"":dwAddress.getTel());
					}
				}
				//台头单位对应的投诉电话
				for(int i = 0; i < docParser.getQNameCount("DwComplainTel"); i++){
					bookMarkKey = docParser.getAttribute("DwComplainTel", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getComplainTel()==null?"":dwAddress.getComplainTel());
					}
				}
				//台头单位对应的传真
				for(int i = 0; i < docParser.getQNameCount("DwFax"); i++){
					bookMarkKey = docParser.getAttribute("DwFax", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getFax()==null?"":dwAddress.getFax());
					}
				}
				//台头单位对应的计量授权声明（中文）
				for(int i = 0; i < docParser.getQNameCount("DwAuthorizationStatement"); i++){
					bookMarkKey = docParser.getAttribute("DwAuthorizationStatement", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getAuthorizationStatement()==null?"":dwAddress.getAuthorizationStatement());
					}
				}
				//台头单位对应的计量授权声明（英文）
				for(int i = 0; i < docParser.getQNameCount("DwAuthorizationStatementEn"); i++){
					bookMarkKey = docParser.getAttribute("DwAuthorizationStatementEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getAuthorizationStatementEn()==null?"":dwAddress.getAuthorizationStatementEn());
					}
				}
				//台头单位对应的实验室认可声明（中文）
				for(int i = 0; i < docParser.getQNameCount("DwCNASStatement"); i++){
					bookMarkKey = docParser.getAttribute("DwCNASStatement", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getCnasstatement()==null?"":dwAddress.getCnasstatement());
					}
				}
				//台头单位对应的实验室认可声明（英文）
				for(int i = 0; i < docParser.getQNameCount("DwCNASStatementEn"); i++){
					bookMarkKey = docParser.getAttribute("DwCNASStatementEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getCnasstatementEn()==null?"":dwAddress.getCnasstatementEn());
					}
				}
				//台头单位对应的溯源声明（中文）
				for(int i = 0; i < docParser.getQNameCount("DwStandardStatement"); i++){
					bookMarkKey = docParser.getAttribute("DwStandardStatement", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getStandardStatement()==null?"":dwAddress.getStandardStatement());
					}
				}
				//台头单位对应的溯源声明（英文）
				for(int i = 0; i < docParser.getQNameCount("DwStandardStatementEn"); i++){
					bookMarkKey = docParser.getAttribute("DwStandardStatementEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getStandardStatementEn()==null?"":dwAddress.getStandardStatementEn());
					}
				}
				
				/***********            安全码信息                   ************/
				String securityCode = LetterUtil.getCertificateSecurityCode(certificate.getCertificateCode(), oRecord.getWorkDate());
				StringBuilder strBuilder = new StringBuilder(securityCode);
				if(strBuilder.toString().length()>28){
					strBuilder.insert(28," ");
				}
				if(strBuilder.toString().length()>24){
					strBuilder.insert(24," ");
				}
				if(strBuilder.toString().length()>20){
					strBuilder.insert(20," ");
				}
				if(strBuilder.toString().length()>16){
					strBuilder.insert(16," ");
				}
				if(strBuilder.toString().length()>12){
					strBuilder.insert(12," ");
				}
				if(strBuilder.toString().length()>8){
					strBuilder.insert(8," ");
				}
				if(strBuilder.toString().length()>4){
					strBuilder.insert(4," ");
				}
				String securityCodeP1 =strBuilder.toString();
				//String securityCodeP1 = securityCode.substring(0, securityCode.length()>16?16:securityCode.length());
				//String securityCodeP2 = securityCode.length()<=16?"":securityCode.substring(16, securityCode.length());
				//安全码（SecurityCodeA1：前一半；SecurityCodeA2：后一半）
				for(int i = 0; i < docParser.getQNameCount("SecurityCodeP1"); i++){
					bookMarkKey = docParser.getAttribute("SecurityCodeP1", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, securityCodeP1);
					}
				}
//				for(int i = 0; i < docParser.getQNameCount("SecurityCodeP2"); i++){
//					bookMarkKey = docParser.getAttribute("SecurityCodeP2", "tagName", i);
//					if(bookMarkKey != null && bookMarkKey.length() > 0){
//						docWriter.setValueByBookMark(bookMarkKey, securityCodeP2);
//					}
//				}
//				
				//证书编号
				for(int i = 0; i < docParser.getQNameCount("CertificateCode"); i++){
					bookMarkKey = docParser.getAttribute("CertificateCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, certificate.getCertificateCode());
					}
				}
				//证书编号条形码
				for(int i = 0; i < docParser.getQNameCount("CertificateBarCode"); i++){
					bookMarkKey = docParser.getAttribute("CertificateBarCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						if(barCodeTempFile == null || !barCodeTempFile.exists() || barCodeTempFile.length() == 0){
							barCodeTempFile = File.createTempFile(UIDUtil.get22BitUID(), ".jpg");	//证书编号条形码临时文件
							try{
								BarCodeUtil.GenerateBarcode(certificate.getCertificateCode(), barCodeTempFile);
							}catch(Exception e){
								throw new Exception(String.format("证书条形码生成失败！原因：%s", (e==null||e.getMessage()==null)?"无":e.getMessage()));
							}
						}
						if(barCodeTempFile != null && barCodeTempFile.exists() && barCodeTempFile.length() > 0){
							docWriter.addPicByBookMark(bookMarkKey, barCodeTempFile.getAbsolutePath());
						}
					}
				}
				
				//送检单位
				for(int i = 0; i < docParser.getQNameCount("Customer"); i++){
					bookMarkKey = docParser.getAttribute("Customer", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getSampleFrom()==null?cSheet.getCustomerName():cSheet.getSampleFrom());
					}
				}
				//送检单位（地址）
				for(int i = 0; i < docParser.getQNameCount("CustomerAddress"); i++){
					bookMarkKey = docParser.getAttribute("CustomerAddress", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getCustomerAddress());
					}
				}
				
				//计量器具名称
				for(int i = 0; i < docParser.getQNameCount("ApplianceName"); i++){
					bookMarkKey = docParser.getAttribute("ApplianceName", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, (oRecord.getApplianceName()==null||oRecord.getApplianceName().trim().length()==0)?oRecord.getTargetAppliance().getApplianceStandardName().getName():oRecord.getApplianceName());
					}
				}
				
				//型号/规格
				for(int i = 0; i < docParser.getQNameCount("Model"); i++){
					bookMarkKey = docParser.getAttribute("Model", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String tempModel = oRecord.getModel()==null?"":oRecord.getModel().trim();
						String tempRange = oRecord.getRange()==null?"":oRecord.getRange().trim();
						String temp = null;
						if(tempModel.length() > 0 && tempRange.length() > 0){
							temp = tempModel + " ／ " + tempRange;
						}else if(tempModel.length() > 0 || tempRange.length() > 0){
							temp = (tempModel.length() > 0)?tempModel:tempRange;						
						}else{
							temp = " ／ ";
						}
						docWriter.setValueByBookMark(bookMarkKey, temp);
					}
				}
				
				//出厂编号
				for(int i = 0; i < docParser.getQNameCount("FactoryCode"); i++){
					bookMarkKey = docParser.getAttribute("FactoryCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, String.format("%s%s", oRecord.getApplianceCode(), (oRecord.getManageCode()==null||oRecord.getManageCode().length()==0)?"":"["+oRecord.getManageCode()+"]"));
					}
				}
				
				//制造单位
				for(int i = 0; i < docParser.getQNameCount("Manufacturer"); i++){
					bookMarkKey = docParser.getAttribute("Manufacturer", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getManufacturer());
					}
				}
				
				/****************  检定依据（技术规范）   **********************/			
				//检定依据（首页/封面）
				String technicalDocsAll = "";	//检定依据
				for(int i = 0; i < docParser.getQNameCount("TechnicalDocs-All"); i++){
					bookMarkKey = docParser.getAttribute("TechnicalDocs-All", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						if(technicalDocsAll == null || technicalDocsAll.length() == 0){
							for(int j = 0; j < speList.size(); j++){
								technicalDocsAll += speList.get(j).getSpecificationCode();
								if(j < speList.size() - 1){
									technicalDocsAll += ",";
								}
							}
						}
						docWriter.setValueByBookMark(bookMarkKey, technicalDocsAll);
					}
				}
				//检定依据（技术规范）--编号
				for(int i = 0; i < docParser.getQNameCount("TechnicalDocs-Code"); i++){
					bookMarkKey = docParser.getAttribute("TechnicalDocs-Code", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("TechnicalDocs-Code", "indexStr", i);	//获取顺序号（第几个）
						Specification spe = null;
						if(speMap.containsKey(indexStr)){
							spe = speMap.get(indexStr);
						}else if(curSpeIndex < speList.size()){
							spe = speList.get(curSpeIndex++);
							speMap.put(indexStr, spe);
						}
						if(spe != null){
							docWriter.setValueByBookMark(bookMarkKey, spe.getSpecificationCode());
						}
					}
				}
				//检定依据（技术规范）--名称
				for(int i = 0; i < docParser.getQNameCount("TechnicalDocs-Name"); i++){
					bookMarkKey = docParser.getAttribute("TechnicalDocs-Name", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("TechnicalDocs-Name", "indexStr", i);	//获取顺序号（第几个）
						Specification spe = null;
						if(speMap.containsKey(indexStr)){
							spe = speMap.get(indexStr);
						}else if(curSpeIndex < speList.size()){
							spe = speList.get(curSpeIndex++);
							speMap.put(indexStr, spe);
						}
						if(spe != null){
							docWriter.setValueByBookMark(bookMarkKey, "《"+spe.getNameCn()+"》");
						}
					}
				}
				
				
				/****************  计量标准   **********************/				
				//计量标准--名称
				for(int i = 0; i < docParser.getQNameCount("Standard-Name"); i++){
					bookMarkKey = docParser.getAttribute("Standard-Name", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-Name", "indexStr", i);	//获取顺序号（第几个）
						Standard std = null;
						if(stdMap.containsKey(indexStr)){
							std = stdMap.get(indexStr);
						}else if(curStdIndex < stdList.size()){
							int temp = curStdIndex;
							for(; temp < stdList.size(); temp++){
								if(!SystemCfgUtil.checkStandardVirtual(stdList.get(temp).getName())){	//不是虚拟的计量标准，则在证书上显示，否则，不在证书上显示
									std = stdList.get(temp);
									stdMap.put(indexStr, std);
									break;
								}
							}
							curStdIndex = temp + 1;
						}
						if(std != null){
							if(std.getName().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, std.getName(),10);
							}else if(std.getName().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, std.getName(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, std.getName(),5);
								alertString.append("'计量标准--名称'的字数超过了30个，现字数是" + std.getName().length() + "个;");
							}
						}
					}
				}
				//计量标准--证书编号
				for(int i = 0; i < docParser.getQNameCount("Standard-CertificateCode"); i++){
					bookMarkKey = docParser.getAttribute("Standard-CertificateCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-CertificateCode", "indexStr", i);	//获取顺序号（第几个）
						Standard std = null;
						if(stdMap.containsKey(indexStr)){
							std = stdMap.get(indexStr);
						}else if(curStdIndex < stdList.size()){
							std = stdList.get(curStdIndex++);
							stdMap.put(indexStr, std);
						}
						if(std != null){
							if(std.getCertificateCode().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, std.getCertificateCode(),10);
							}else if(std.getCertificateCode().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, std.getCertificateCode(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, std.getCertificateCode(),5);
								alertString.append("'计量标准--证书编号'的字数超过了30个，现字数是" + std.getCertificateCode().length() + "个;");
							}
						}
					}
				}
				//计量标准--测量范围
				for(int i = 0; i < docParser.getQNameCount("Standard-Range"); i++){
					bookMarkKey = docParser.getAttribute("Standard-Range", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-Range", "indexStr", i);	//获取顺序号（第几个）
						Standard std = null;
						if(stdMap.containsKey(indexStr)){
							std = stdMap.get(indexStr);
						}else if(curStdIndex < stdList.size()){
							std = stdList.get(curStdIndex++);
							stdMap.put(indexStr, std);
						}
						if(std != null){
							if(std.getRange().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, std.getRange(),10);
							}else if(std.getRange().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, std.getRange(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, std.getRange(),5);
								alertString.append("'计量标准--测量范围'的字数超过了30个，现字数是" + std.getRange().length() + "个;");
							}
						}
					}
				}
				//计量标准--不确定度
				for(int i = 0; i < docParser.getQNameCount("Standard-Uncertain"); i++){
					bookMarkKey = docParser.getAttribute("Standard-Uncertain", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-Uncertain", "indexStr", i);	//获取顺序号（第几个）
						Standard std = null;
						if(stdMap.containsKey(indexStr)){
							std = stdMap.get(indexStr);
						}else if(curStdIndex < stdList.size()){
							std = stdList.get(curStdIndex++);
							stdMap.put(indexStr, std);
						}
						if(std != null){
							if(std.getUncertain().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, std.getUncertain(),10);
							}else if(std.getUncertain().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, std.getUncertain(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, std.getUncertain(),5);
								alertString.append("'计量标准--不确定度'的字数超过了30个，现字数是" + std.getUncertain().length() + "个;");
							}
						}
					}
				}
				//计量标准--有效期
				for(int i = 0; i < docParser.getQNameCount("Standard-ValidDate"); i++){
					bookMarkKey = docParser.getAttribute("Standard-ValidDate", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-ValidDate", "indexStr", i);	//获取顺序号（第几个）
						Standard std = null;
						if(stdMap.containsKey(indexStr)){
							std = stdMap.get(indexStr);
						}else if(curStdIndex < stdList.size()){
							std = stdList.get(curStdIndex++);
							stdMap.put(indexStr, std);
						}
						if(std != null){
							String validate = (std.getValidDate()==null?"":DateTimeFormatUtil.DateFormat.format(std.getValidDate()));
							if(validate.length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, validate,10);
							}else if(validate.length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, validate,6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, validate,5);
								alertString.append("'计量标准--有效期'的字数超过了30个，现字数是" + validate.length() + "个;");
							}
						}
					}
				}

				/****************  标准器具   **********************/	
				//标准器具--名称
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-Name"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-Name", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-Name", "indexStr", i);	//获取顺序号（第几个）
						StandardAppliance stdApp = null;
						if(stdAppMap.containsKey(indexStr)){
							stdApp = stdAppMap.get(indexStr);
						}else if(curStdAppIndex < stdAppList.size()){
							stdApp = stdAppList.get(curStdAppIndex++);
							stdAppMap.put(indexStr, stdApp);
						}
						if(stdApp != null){
							if(stdApp.getName().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getName(),10);
							}else if(stdApp.getName().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getName(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getName(),5);
								alertString.append("'标准器具--名称'的字数超过了30个，现字数是" + stdApp.getName().length() + "个;");
							}
						}
					}
				}
				//标准器具--测量范围
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-Range"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-Range", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-Range", "indexStr", i);	//获取顺序号（第几个）
						StandardAppliance stdApp = null;
						if(stdAppMap.containsKey(indexStr)){
							stdApp = stdAppMap.get(indexStr);
						}else if(curStdAppIndex < stdAppList.size()){
							stdApp = stdAppList.get(curStdAppIndex++);
							stdAppMap.put(indexStr, stdApp);
						}
						if(stdApp != null){
							if(stdApp.getRange().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getRange(),10);
							}else if(stdApp.getRange().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getRange(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getRange(),5);
								alertString.append("'标准器具--测量范围'的字数超过了30个，现字数是" + stdApp.getRange().length() + "个;");
							}
						}
					}
				}
				//标准器具--型号规格
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-Model"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-Model", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-Model", "indexStr", i);	//获取顺序号（第几个）
						StandardAppliance stdApp = null;
						if(stdAppMap.containsKey(indexStr)){
							stdApp = stdAppMap.get(indexStr);
						}else if(curStdAppIndex < stdAppList.size()){
							stdApp = stdAppList.get(curStdAppIndex++);
							stdAppMap.put(indexStr, stdApp);
						}
						if(stdApp != null){
							if(stdApp.getModel().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getModel(),10);
							}else if(stdApp.getModel().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getModel(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getModel(),5);
								alertString.append("'标准器具--型号规格'的字数超过了30个，现字数是" + stdApp.getModel().length() + "个;");
							}
						}
					}
				}
				//标准器具--不确定度
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-Uncertain"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-Uncertain", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-Uncertain", "indexStr", i);	//获取顺序号（第几个）
						StandardAppliance stdApp = null;
						if(stdAppMap.containsKey(indexStr)){
							stdApp = stdAppMap.get(indexStr);
						}else if(curStdAppIndex < stdAppList.size()){
							stdApp = stdAppList.get(curStdAppIndex++);
							stdAppMap.put(indexStr, stdApp);
						}
						if(stdApp != null){
							if(stdApp.getUncertain().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getUncertain(),10);
							}else if(stdApp.getName().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getUncertain(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getUncertain(),5);
								alertString.append("'标准器具--不确定度'的字数超过了30个，现字数是" + stdApp.getUncertain().length() + "个;");
							}
						}
					}
				}
				//标准器具--证书编号
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-SeriaNumber"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-SeriaNumber", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-SeriaNumber", "indexStr", i);	//获取顺序号（第几个）
						StandardAppliance stdApp = null;
						if(stdAppMap.containsKey(indexStr)){
							stdApp = stdAppMap.get(indexStr);
						}else if(curStdAppIndex < stdAppList.size()){
							stdApp = stdAppList.get(curStdAppIndex++);
							stdAppMap.put(indexStr, stdApp);
						}
						if(stdApp != null){
							if(stdApp.getSeriaNumber().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getSeriaNumber(),10);
							}else if(stdApp.getSeriaNumber().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getSeriaNumber(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getSeriaNumber(),5);
								alertString.append("'标准器具--证书编号'的字数超过了30个，现字数是" + stdApp.getSeriaNumber().length() + "个;");
							}
						}
					}
				}
				//标准器具--所内编号
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-LocaleCode"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-LocaleCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-LocaleCode", "indexStr", i);	//获取顺序号（第几个）
						StandardAppliance stdApp = null;
						if(stdAppMap.containsKey(indexStr)){
							stdApp = stdAppMap.get(indexStr);
						}else if(curStdAppIndex < stdAppList.size()){
							stdApp = stdAppList.get(curStdAppIndex++);
							stdAppMap.put(indexStr, stdApp);
						}
						if(stdApp != null){
							if(stdApp.getLocaleCode().length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getLocaleCode(),10);
							}else if(stdApp.getLocaleCode().length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getLocaleCode(),6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, stdApp.getLocaleCode(),5);
								alertString.append("'标准器具--所内编号'的字数超过了30个，现字数是" + stdApp.getLocaleCode().length() + "个;");
							}
						}
					}
				}
				//标准器具--有效期
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-ValidDate"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-ValidDate", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-ValidDate", "indexStr", i);	//获取顺序号（第几个）
						StandardAppliance stdApp = null;
						if(stdAppMap.containsKey(indexStr)){
							stdApp = stdAppMap.get(indexStr);
						}else if(curStdAppIndex < stdAppList.size()){
							stdApp = stdAppList.get(curStdAppIndex++);
							stdAppMap.put(indexStr, stdApp);
						}
						if(stdApp != null){
							String validate = (stdApp.getValidDate()==null?"":DateTimeFormatUtil.DateFormat.format(stdApp.getValidDate()));
							if(validate.length()<=18){
								docWriter.setValueByBookMark(bookMarkKey, validate,10);
							}else if(validate.length()<=30){
								docWriter.setValueByBookMark(bookMarkKey, validate,6);
							}else{
								docWriter.setValueByBookMark(bookMarkKey, validate,5);
								alertString.append("'标准器具--有效期'的字数超过了30个，现字数是" + validate.length() + "个;");
							}
						}
					}
				}
				
				
				//检定结论
				for(int i = 0; i < docParser.getQNameCount("Conclusion"); i++){
					bookMarkKey = docParser.getAttribute("Conclusion", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getConclusion()==null?"":oRecord.getConclusion());
					}
				}


				/*
				//批准人
				if(authorizerPicPath != null && authorizerPicPath.length() > 0){
					bookMarkKey = docParser.getAttribute("Authorizer", "tagName");
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.addPicByBookMark(bookMarkKey, authorizerPicPath);
					}
				}else{
					bookMarkKey = docParser.getAttribute("Authorizer", "tagName");
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
					}
				}
				
				//核验人
				if(checkerPicPath != null && checkerPicPath.length() > 0){
					bookMarkKey = docParser.getAttribute("Checker", "tagName");
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.addPicByBookMark(bookMarkKey, checkerPicPath);
					}
				}else{
					bookMarkKey = docParser.getAttribute("Checker", "tagName");
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
					}
				}
				*/
				
				//检定员
				if(verifierPicPath != null && verifierPicPath.length() > 0){
					for(int i = 0; i < docParser.getQNameCount("Verifier"); i++){
						bookMarkKey = docParser.getAttribute("Verifier", "tagName", i);
						if(bookMarkKey != null && bookMarkKey.length() > 0){
							docWriter.addPicByBookMark(bookMarkKey, verifierPicPath);
						}
					}
				}
				
				//检定员（打印）
				for(int i = 0; i < docParser.getQNameCount("VerifierPrinter"); i++){
					bookMarkKey = docParser.getAttribute("VerifierPrinter", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getSysUserByStaffId().getName());
					}
				}
				
				//检定日期-年
				for(int i = 0; i < docParser.getQNameCount("VerifyYear"); i++){
					bookMarkKey = docParser.getAttribute("VerifyYear", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getWorkDate() == null?"":FormaterYear.format(oRecord.getWorkDate()));
					}
				}
				
				//检定日期-月
				for(int i = 0; i < docParser.getQNameCount("VerifyMonth"); i++){
					bookMarkKey = docParser.getAttribute("VerifyMonth", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getWorkDate() == null?"":FormaterMonth.format(oRecord.getWorkDate()));
					}
				}
				
				//检定日期-日
				for(int i = 0; i < docParser.getQNameCount("VerifyDay"); i++){
					bookMarkKey = docParser.getAttribute("VerifyDay", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getWorkDate() == null?"":FormaterDay.format(oRecord.getWorkDate()));
					}
				}
				
				//有效日期-年
				for(int i = 0; i < docParser.getQNameCount("ValidYear"); i++){
					bookMarkKey = docParser.getAttribute("ValidYear", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getValidity() == null?"":FormaterYear.format(oRecord.getValidity()));
					}
				}
				
				//有效日期-月
				for(int i = 0; i < docParser.getQNameCount("ValidMonth"); i++){
					bookMarkKey = docParser.getAttribute("ValidMonth", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey,  oRecord.getValidity() == null?"":FormaterMonth.format(oRecord.getValidity()));
					}
				}
				
				//有效日期-日
				for(int i = 0; i < docParser.getQNameCount("ValidDay"); i++){
					bookMarkKey = docParser.getAttribute("ValidDay", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey,  oRecord.getValidity() == null?"":FormaterDay.format(oRecord.getValidity()));
					}
				}
				
				java.sql.Timestamp sampleRecvDate = null;	//样品接收时间
				if(cSheet.getCommissionType() == 2&&cSheet.getLocaleCommissionDate()!=null){	//现场检测：现场检测的时间
					sampleRecvDate = cSheet.getLocaleCommissionDate();
				}else{
					sampleRecvDate = cSheet.getCommissionDate();
				}
				//样品接收日期-年（针对校准证书）
				for(int i = 0; i < docParser.getQNameCount("SampleRecvYear"); i++){
					bookMarkKey = docParser.getAttribute("SampleRecvYear", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, sampleRecvDate == null?"":FormaterYear.format(sampleRecvDate));
					}
				}
				
				//样品接收日期-月（针对校准证书）
				for(int i = 0; i < docParser.getQNameCount("SampleRecvMonth"); i++){
					bookMarkKey = docParser.getAttribute("SampleRecvMonth", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey,  sampleRecvDate == null?"":FormaterMonth.format(sampleRecvDate));
					}
				}
				
				//样品接收日期-日（针对校准证书）
				for(int i = 0; i < docParser.getQNameCount("SampleRecvDay"); i++){
					bookMarkKey = docParser.getAttribute("SampleRecvDay", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey,  sampleRecvDate == null?"":FormaterDay.format(sampleRecvDate));
					}
				}
				
				//工作地点
				for(int i = 0; i < docParser.getQNameCount("WorkLocation"); i++){
					bookMarkKey = docParser.getAttribute("WorkLocation", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getWorkLocation() == null?"":oRecord.getWorkLocation());
					}
				}
				
				//温度
				for(int i = 0; i < docParser.getQNameCount("Temp"); i++){
					bookMarkKey = docParser.getAttribute("Temp", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getTemp()==null?"":oRecord.getTemp().toString());
					}
				}
				
				//相对湿度
				for(int i = 0; i < docParser.getQNameCount("Humidity"); i++){
					bookMarkKey = docParser.getAttribute("Humidity", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getHumidity() == null?"":oRecord.getHumidity().toString());
					}
				}
				
				//大气压
				for(int i = 0; i < docParser.getQNameCount("Pressure"); i++){
					bookMarkKey = docParser.getAttribute("Pressure", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getPressure()==null?"":oRecord.getPressure().toString());
					}
				}
				
				
				
				if(excel != null){
					//获取Excel中证书的工作表名称和区域
					String certificateSheetName = excelParser.getFirstAttribute("certificate-sheet", "sheetName");
					String certificateRegion = excelParser.getFirstAttribute("certificate-sheet", "region");
					bookMarkKey = docParser.getFirstAttribute("DataPage", "tagName");	//证书数据页书签
					if(certificateSheetName != null && certificateRegion != null && bookMarkKey != null){
						/*************拷贝Excel的数据页至证书的指定位置*************/
						//begin:使用Jacob打开Excel
						xlsApp = new ActiveXComponent("Excel.Application");
						xlsApp.setProperty("Visible", new Variant(false));	//不显示打开
						Dispatch workbooks = xlsApp.getProperty("Workbooks").toDispatch();
						xlsWorkbook = Dispatch.invoke(	//打开并获取Excel文档
								workbooks,
								"Open",
								Dispatch.Method,
								new Object[] {excel.getAbsolutePath(), 
										new Variant(false),
										new Variant(true) //是否以只读方式打开:是——只读方式打开
								},
								new int[1]).toDispatch();
						//end:使用Jacob打开Excel
						
						docWriter.PasteExcelTable(xlsWorkbook, certificateSheetName, certificateRegion, bookMarkKey);//拷贝数据页
						
						xlsApp.setProperty("CutCopyMode", false);	//设置Excel不保留剪切板的信息
					}
				}

				docWriter.saveAsWord(fOutDoc.getCanonicalPath()); 	//另存为Word，原模板文件不保存退出
				if(fOutPdf != null){
					docWriter.saveAsPdf(fOutPdf.getCanonicalPath());	//另存为PDF
				}
//				docWriter.closeWord(false);
//				docWriter.releaseResourse();
//				docWriter = null;
				
				return true;
				
			}catch(Exception e){
				throw e;
			}finally{
				try{
					if(xlsWorkbook != null){
						Dispatch.call(xlsWorkbook, "Close", new Variant(false));	//不保存退出
						xlsWorkbook = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					if(xlsApp != null){	//释放com资源
						xlsApp.invoke("Quit", new Variant[] {});//或者使用方法：Dispatch.call(app, "Quit");
						xlsApp = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				
				try{
					if(docWriter != null){
						docWriter.closeWord(false);
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					if(docWriter != null){
						docWriter.releaseResourse();
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				docWriter = null;
				try{
					//关闭com的线程
					ComThread.Release();
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				
				if(barCodeTempFile != null && barCodeTempFile.exists()){	//删除临时文件
					barCodeTempFile.delete();
					barCodeTempFile = null;
				}
			}
//		}
		
	}
	
	/**
	 * 对上传的证书文件进行处理（更新证书编号和证书条形码）
	 * @param doc
	 * @param fOutDoc
	 * @param fOutPdf
	 * @param docParser
	 * @param oRecord
	 * @param certificate
	 * @return
	 * @throws Exception
	 */
	public static boolean HandleUploadedCertificateWord(File doc, File fOutDoc, File fOutPdf, 
			ParseXMLAll docParser, OriginalRecord oRecord, Certificate certificate) throws Exception{
//		synchronized (SynBlock) {
			//初始化com的线程
			ComThread.InitSTA();
			
			JacobWordWriter docWriter = null;//getDocWriter();
			ActiveXComponent xlsApp = null;
			Dispatch xlsWorkbook = null;
			File barCodeTempFile = null;	//证书编号条形码图片文件
			try{
				docWriter = new WordUtil().new JacobWordWriter();
				docWriter.openWord(doc.getAbsolutePath(), true);	//以只读方式打开
				String bookMarkKey = null;
				
				/***********            安全码信息                   ************/
				String securityCode = LetterUtil.getCertificateSecurityCode(certificate.getCertificateCode(), oRecord.getWorkDate());
				StringBuilder strBuilder = new StringBuilder(securityCode);
				if(strBuilder.toString().length()>28){
					strBuilder.insert(28," ");
				}
				if(strBuilder.toString().length()>24){
					strBuilder.insert(24," ");
				}
				if(strBuilder.toString().length()>20){
					strBuilder.insert(20," ");
				}
				if(strBuilder.toString().length()>16){
					strBuilder.insert(16," ");
				}
				if(strBuilder.toString().length()>12){
					strBuilder.insert(12," ");
				}
				if(strBuilder.toString().length()>8){
					strBuilder.insert(8," ");
				}
				if(strBuilder.toString().length()>4){
					strBuilder.insert(4," ");
				}
				String securityCodeP1 =strBuilder.toString();
				//String securityCodeP1 = securityCode.substring(0, securityCode.length()>16?16:securityCode.length());
				//String securityCodeP2 = securityCode.length()<=16?"":securityCode.substring(16, securityCode.length());
				//安全码（SecurityCodeA1：前一半；SecurityCodeA2：后一半）
				for(int i = 0; i < docParser.getQNameCount("SecurityCodeP1"); i++){
					bookMarkKey = docParser.getAttribute("SecurityCodeP1", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, securityCodeP1);
					}
				}
//				for(int i = 0; i < docParser.getQNameCount("SecurityCodeP2"); i++){
//					bookMarkKey = docParser.getAttribute("SecurityCodeP2", "tagName", i);
//					if(bookMarkKey != null && bookMarkKey.length() > 0){
//						docWriter.setValueByBookMark(bookMarkKey, securityCodeP2);
//					}
//				}
				
				//证书编号
				for(int i = 0; i < docParser.getQNameCount("CertificateCode"); i++){
					bookMarkKey = docParser.getAttribute("CertificateCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, certificate.getCertificateCode());
					}
				}
				//证书编号条形码
				for(int i = 0; i < docParser.getQNameCount("CertificateBarCode"); i++){
					bookMarkKey = docParser.getAttribute("CertificateBarCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						if(barCodeTempFile == null || !barCodeTempFile.exists() || barCodeTempFile.length() == 0){
							barCodeTempFile = File.createTempFile(UIDUtil.get22BitUID(), ".jpg");	//证书编号条形码临时文件
							try{
								BarCodeUtil.GenerateBarcode(certificate.getCertificateCode(), barCodeTempFile);
							}catch(Exception e){
								throw new Exception(String.format("证书条形码生成失败！原因：%s", (e==null||e.getMessage()==null)?"无":e.getMessage()));
							}
						}
						if(barCodeTempFile != null && barCodeTempFile.exists() && barCodeTempFile.length() > 0){
							docWriter.replacePicByBookMark(bookMarkKey, barCodeTempFile.getAbsolutePath());
						}
					}
				}
				
				//送检单位
				for(int i = 0; i < docParser.getQNameCount("Customer"); i++){
					bookMarkKey = docParser.getAttribute("Customer", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						CommissionSheet cSheet = oRecord.getCommissionSheet();
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getSampleFrom()==null?cSheet.getCustomerName():cSheet.getSampleFrom());
					}
				}
				//送检单位（地址）
				for(int i = 0; i < docParser.getQNameCount("CustomerAddress"); i++){
					bookMarkKey = docParser.getAttribute("CustomerAddress", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getCommissionSheet().getCustomerAddress());
					}
				}
				
				docWriter.saveAsWord(fOutDoc.getCanonicalPath()); 	//另存为Word，原模板文件不保存退出
				if(fOutPdf != null){
					docWriter.saveAsPdf(fOutPdf.getCanonicalPath());	//另存为PDF
				}
//				docWriter.closeWord(false);
//				docWriter.releaseResourse();
//				docWriter = null;
				
				return true;
				
			}catch(Exception e){
				throw e;
			}finally{
				try{
					if(xlsWorkbook != null){
						Dispatch.call(xlsWorkbook, "Close", new Variant(false));	//不保存退出
						xlsWorkbook = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					if(xlsApp != null){	//释放com资源
						xlsApp.invoke("Quit", new Variant[] {});//或者使用方法：Dispatch.call(app, "Quit");
						xlsApp = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				
				try{
					if(docWriter != null){
						docWriter.closeWord(false);
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					if(docWriter != null){
						docWriter.releaseResourse();
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				docWriter = null;
				try{
					//关闭com的线程
					ComThread.Release();
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				
				if(barCodeTempFile != null && barCodeTempFile.exists()){	//删除临时文件
					barCodeTempFile.delete();
					barCodeTempFile = null;
				}
			}
//		}
		
	}
	/**
	 * 添加核验和授权签字人的签名
	 * @param doc
	 * @param docParser
	 * @param checkerPicPath
	 * @param authorizerPicPath
	 * @param oRecord
	 * @return
	 */
	public static boolean AddCertificateSignature(File doc, File fOutDoc, File fOutPdf, ParseXML docParser,String checkerPicPath, String authorizerPicPath, OriginalRecord oRecord) throws Exception{
//		synchronized (SynBlock) {
			//初始化com的线程
			ComThread.InitSTA();
			
			JacobWordWriter docWriter = null;
//			try{
//				docWriter.openWord(doc.getAbsolutePath());
//			}catch(Exception e){
//				if(docWriter != null){
//					docWriter.releaseResourse();
//					DocWriter = null;
//				}
//				e.printStackTrace();
//				return false;
//			}
			ActiveXComponent xlsApp = null;
			Dispatch xlsWorkbook = null;
			try{
				docWriter = new WordUtil().new JacobWordWriter();
				docWriter.openWord(doc.getAbsolutePath(), true);	//以只读写方式打开
				String bookMarkKey = null;
				//批准人
				if(authorizerPicPath != null && authorizerPicPath.length() > 0){
					bookMarkKey = docParser.getAttribute("Authorizer", "tagName");
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						//docWriter.addPicByBookMark(bookMarkKey, authorizerPicPath);
						docWriter.replacePicByBookMark(bookMarkKey, authorizerPicPath);
					}
				}
				
				//批准人（打印）
				bookMarkKey = docParser.getAttribute("AuthorizerPrinter", "tagName");
				if(bookMarkKey != null && bookMarkKey.length() > 0){
					docWriter.setValueByBookMark(bookMarkKey, oRecord.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
				}
				
				
				//批准人职务
				bookMarkKey = docParser.getAttribute("AuthorizerPosition", "tagName");
				if(bookMarkKey != null && bookMarkKey.length() > 0){
					docWriter.setValueByBookMark(bookMarkKey, oRecord.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getAdministrationPost());
				}
				
				//核验人
				if(checkerPicPath != null && checkerPicPath.length() > 0){
					bookMarkKey = docParser.getAttribute("Checker", "tagName");
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						//docWriter.addPicByBookMark(bookMarkKey, checkerPicPath);
						docWriter.replacePicByBookMark(bookMarkKey, checkerPicPath);
					}
				}
				
				//核验人（打印）
				bookMarkKey = docParser.getAttribute("CheckerPrinter", "tagName");
				if(bookMarkKey != null && bookMarkKey.length() > 0){
					docWriter.setValueByBookMark(bookMarkKey, oRecord.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
				}
				
				docWriter.saveAsWord(fOutDoc.getCanonicalPath()); 	//另存为Word，原模板文件不保存退出
				docWriter.saveAsPdf(fOutPdf.getCanonicalPath());	//另存为Pdf
				
				docWriter.closeWord(false);
				docWriter.releaseResourse();
				docWriter = null;
				
				return true;
				
			}catch(Exception ex){
				throw ex;
			}finally{
				
				try{
					if(xlsWorkbook != null){
						Dispatch.call(xlsWorkbook, "Close", new Variant(false));	//不保存退出
						xlsWorkbook = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					if(xlsApp != null){	//释放com资源
						xlsApp.invoke("Quit", new Variant[] {});//或者使用方法：Dispatch.call(app, "Quit");
						xlsApp = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					//关闭com的线程
					ComThread.Release();
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}					
			}
//		}
	}
	
	
	
	/**
	 * 内部类：使用Jacob操作生成Word
	 * @author Administrator
	 *
	 */
	public class JacobWordWriter {
		private ActiveXComponent app;
		private Dispatch documents = null;
		private Dispatch doc = null;
		private Dispatch activeDocument = null;
		private Dispatch bookMarks = null;
		private String filepath = null;
		
		public JacobWordWriter() throws Exception{
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", new Variant(false));   //设置word不可见
			documents = app.getProperty("Documents").toDispatch();
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
				
				activeDocument = app.getProperty("ActiveDocument").toDispatch();
				bookMarks = Dispatch.call(activeDocument, "Bookmarks").toDispatch();//或者bookMarks = app.call(activeDocument, "Bookmarks").toDispatch();
			}catch(Exception e){
				throw e;
			}
		}
		
		
		/**
		 * 保存并关闭Word
		 * @param f：为true时保存修改的文件并退出；为false时不保存修改的文件并退出 
		 */
		public void saveAndCloseWord(boolean f){
			if(doc != null){
				if(f){
					Dispatch.call(doc, "Save");
				}
				closeWord(f);
			}
			
		}
		/**
		 * 另存为Word
		 * @param filePath: 另存为的路径
		 */
		public void saveAsWord(String filePath){
			if(doc != null){
				Dispatch.call(doc, "SaveAs", new Variant(filePath));
			}
		}
		
		/**
		 * 另存并关闭Word
		 * @param f：为true时保存修改的文件并退出；为false时不保存修改的文件并退出 
		 * @param filePath: 另存为的路径
		 */
		public void saveAsAndCloseWord(boolean f, String filePath){
			if(doc != null){
				saveAsWord(filePath);
				closeWord(f);
			}
		}
		
		/**
		 * 另存为Pdf
		 * @param filePath: 另存为的路径
		 */
		public void saveAsPdf(String filePath){
			if(doc != null){
				Dispatch.invoke(doc, "SaveAs", Dispatch.Method, 
						new Object[] {filePath, new Variant(17) }, new int[1]); 	//17表示另存为PDF格式
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
		
		/**
		 * 在书签位置插入数据
		 * @param bookMarkKey：书签名
		 * @param value：插入的值
		 * @return 插入成功返回true，失败（没有找到书签）返回false
		 * @throws Exception
		 */
		public boolean setValueByBookMark(String bookMarkKey, String value) throws Exception{
			try{
				boolean isBookMarkExist = Dispatch.call(bookMarks, "Exists",bookMarkKey).changeType(Variant.VariantBoolean).getBoolean();
				if (isBookMarkExist == true) {
					Dispatch rangeItem = Dispatch.call(bookMarks, "Item", bookMarkKey).toDispatch();
					Dispatch range = Dispatch.call(rangeItem, "Range").toDispatch();
					Dispatch.put(range, "Text", value == null?"":value);
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * 在书签位置插入数据,根据字体大小自适应
		 * @param bookMarkKey：书签名
		 * @param value：插入的值
		 * @return 插入成功返回true，失败（没有找到书签）返回false
		 * @throws Exception
		 */
		public boolean setValueByBookMarkFontsize(String bookMarkKey, String value, StringBuilder alertString) throws Exception{
			try{
				boolean isBookMarkExist = Dispatch.call(bookMarks, "Exists",bookMarkKey).changeType(Variant.VariantBoolean).getBoolean();
				if (isBookMarkExist == true) {
					Dispatch rangeItem = Dispatch.call(bookMarks, "Item", bookMarkKey).toDispatch();
					Dispatch range = Dispatch.call(rangeItem, "Range").toDispatch();

					Dispatch.put(range, "Text", value == null?"":value);
					
					Dispatch font = Dispatch.get(range, "Font").toDispatch(); 
					if(value.length()<=18){
						Dispatch.put(font, "Size", 10);
					}else if(value.length()<=30){
						Dispatch.put(font, "Size", 6);
					}else{
						Dispatch.put(font, "Size", 5);
						//alertString=new StringBuilder();
						alertString.append("字数超过了30个，现字数是" + value.length() + "个;");
					}
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * 在书签位置插入数据,根据字体大小自适应
		 * @param bookMarkKey：书签名
		 * @param value：插入的值
		 * @return 插入成功返回true，失败（没有找到书签）返回false
		 * @throws Exception
		 */
		public boolean setValueByBookMark(String bookMarkKey, String value, int fontsize) throws Exception{
			try{
				boolean isBookMarkExist = Dispatch.call(bookMarks, "Exists",bookMarkKey).changeType(Variant.VariantBoolean).getBoolean();
				if (isBookMarkExist == true) {
					Dispatch rangeItem = Dispatch.call(bookMarks, "Item", bookMarkKey).toDispatch();
					Dispatch range = Dispatch.call(rangeItem, "Range").toDispatch();
					Dispatch.put(range, "Text", value == null?"":value);					
					Dispatch font = Dispatch.get(range, "Font").toDispatch(); 					
					Dispatch.put(font, "Size", fontsize);					
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * 复制Excel表格到指定标签位置处
		 * @param xlWorkbook：Excel表
		 * @param sheetName：Excel工作表的名称
		 * @param xlsRange：复制的区域（如：A1:H10）
		 * @param bookMarkKey：Word标签的名称
		 * @return 复制成功返回true,复制失败(找不到指定标签)返回false
		 * @throws Exception
		 */
		public boolean PasteExcelTable(Dispatch xlWorkbook, String sheetName, String xlsRange, String bookMarkKey) throws Exception{
			try{
				boolean isBookMarkExist = Dispatch.call(bookMarks, "Exists",bookMarkKey).changeType(Variant.VariantBoolean).getBoolean();
				if (isBookMarkExist == true) {
					Dispatch xlsSheet = null;
					try{
						xlsSheet = Dispatch.call(xlWorkbook, "Sheets", new Object[]{sheetName}).toDispatch();	//Excel 工作表
					}catch(Exception e){
						throw new Exception(String.format("未找到原始记录Excel文件中对应证书区域的工作表：%s", sheetName));
					}
					Boolean bVisible = Dispatch.get(xlsSheet, "Visible").changeType(Variant.VariantBoolean).getBoolean();
					if(!bVisible){	//隐藏的工作表：取消隐藏
						Dispatch.put(xlsSheet, "Visible", new Variant(true));
					}
					Dispatch Range = Dispatch.call(xlsSheet, "Range", new Object[] { xlsRange }).toDispatch();	//复制的区域
					Dispatch.call(Range, "Copy");	//复制
					
					Dispatch docRangeItem = Dispatch.call(bookMarks, "Item", bookMarkKey).toDispatch();
					Dispatch docRange = Dispatch.call(docRangeItem, "Range").toDispatch();
					Dispatch.call(docRange, "PasteExcelTable", new Variant(false), new Variant(false), new Variant(false));	//复制Excel表格：按Excel定义的格式
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * 在书签处添加图片
		 * @param bookMarkKey
		 * @param picPath
		 * @return
		 * @throws Exception
		 */
		public boolean addPicByBookMark(String bookMarkKey, String picPath) throws Exception{
			try{
				boolean isBookMarkExist = Dispatch.call(bookMarks, "Exists",bookMarkKey).changeType(Variant.VariantBoolean).getBoolean();
				if (isBookMarkExist == true) {
					Dispatch rangeItem = Dispatch.call(bookMarks, "Item", bookMarkKey).toDispatch();
					Dispatch range = Dispatch.call(rangeItem, "Range").toDispatch();
					
					Dispatch picInlineShapes = Dispatch.get(range, "InlineShapes").toDispatch();
					Dispatch.call(picInlineShapes, "AddPicture", picPath, new Variant(false), new Variant(true)).toDispatch();	//第三个参数（LinkToFile）：是否连接到文件；第四个参数（SaveWithDocument）：在文件中保存图片
					
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * 在书签处替换图片
		 * @param bookMarkKey
		 * @param picPath
		 * @return
		 * @throws Exception
		 */
		public boolean replacePicByBookMark(String bookMarkKey, String picPath) throws Exception{
			try{
				boolean isBookMarkExist = Dispatch.call(bookMarks, "Exists",bookMarkKey).changeType(Variant.VariantBoolean).getBoolean();
				if (isBookMarkExist == true) {
					Dispatch rangeItem = Dispatch.call(bookMarks, "Item", bookMarkKey).toDispatch();
					Dispatch range = Dispatch.call(rangeItem, "Range").toDispatch();
					
					Dispatch.put(range, "Text", "");	//先清空原有的图片等数据
					
					Dispatch picInlineShapes = Dispatch.get(range, "InlineShapes").toDispatch();
					Dispatch.call(picInlineShapes, "AddPicture", picPath, new Variant(false), new Variant(true)).toDispatch();	//第三个参数（LinkToFile）：是否连接到文件；第四个参数（SaveWithDocument）：在文件中保存图片
					
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
	}
}
