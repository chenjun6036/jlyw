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
 * ��֤��Word���в���������֤��
 * @author Administrator
 *
 */
public class WordUtil {
	private static final Log log = LogFactory.getLog(WordUtil.class);
	/**
	 * ˽�й��캯������ֹ���ⲿʵ����
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
	 * ��ԭʼ��¼Excel�ļ�����֤��Word�ļ�
	 * @param excel��ԭʼ��¼Excel�ļ�
	 * @param doc��֤��Doc�ļ�
	 * @param excelParser��ԭʼ��¼�ֶζ���
	 * @param docParser��֤������
	 * @param verifierPicPath������Աǩ��ͼƬ�ļ���ַ
	 * @param cSheet��ί�е�����
	 * @param oRecord��ԭʼ��¼����
	 * @param certificate��֤��
	 * @param dwAddress��̨ͷ��ַ����
	 * @param alertString����������ʱ����ʾ��Ϣ
	 * 
	 * @return �������ļ���·������
	 */
	public static boolean MakeCertificateWord(File excel, File doc, File fOutDoc, File fOutPdf, 
			ParseXMLAll excelParser, ParseXMLAll docParser, 
			String verifierPicPath, 
			CommissionSheet cSheet, OriginalRecord oRecord, Certificate certificate, Address dwAddress, 
			List<Specification> speList, List<Standard> stdList, List<StandardAppliance>stdAppList,StringBuilder alertString) throws Exception{
//		synchronized (SynBlock) {
			//��ʼ��com���߳�
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
			File barCodeTempFile = null;	//֤����������ͼƬ�ļ�
			try{
				docWriter = new WordUtil().new JacobWordWriter();
				docWriter.openWord(doc.getAbsolutePath(), true);	//��ֻ����ʽ��
				String bookMarkKey = null;
				
				
				//�����淶��������׼����׼���߶�Ӧ
				Map<String, Specification> speMap = new HashMap<String, Specification>();	
				Map<String, Standard> stdMap = new HashMap<String, Standard>();
				Map<String, StandardAppliance> stdAppMap = new HashMap<String, StandardAppliance>();
				int curSpeIndex = 0, curStdIndex = 0, curStdAppIndex = 0;	//��ǰ��ȡ�ļ����淶/������׼/��׼���ߵ�������
				
				/***********            ̨ͷ��λ��Ϣ                   ************/
				//̨ͷ���ƣ����ģ�
				for(int i = 0; i < docParser.getQNameCount("DwName"); i++){
					bookMarkKey = docParser.getAttribute("DwName", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getHeadName());
					}
				}
				//̨ͷ���ƣ�Ӣ�ģ�
				for(int i = 0; i < docParser.getQNameCount("DwNameEn"); i++){
					bookMarkKey = docParser.getAttribute("DwNameEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getHeadNameEn());
					}
				}
				//̨ͷ��λ��Ӧ��֤���ַ�����ģ�
				for(int i = 0; i < docParser.getQNameCount("DwAddrName"); i++){
					bookMarkKey = docParser.getAttribute("DwAddrName", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getAddress());
					}
				}
				//̨ͷ��λ��Ӧ��֤���ַ��Ӣ�ģ�
				for(int i = 0; i < docParser.getQNameCount("DwAddrNameEn"); i++){
					bookMarkKey = docParser.getAttribute("DwAddrNameEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getAddressEn()==null?"":dwAddress.getAddressEn());
					}
				}
				//̨ͷ��λ��Ӧ����������
				for(int i = 0; i < docParser.getQNameCount("DwZipCode"); i++){
					bookMarkKey = docParser.getAttribute("DwZipCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getZipCode()==null?"":dwAddress.getZipCode());
					}
				}
				//̨ͷ��λ��Ӧ����ַ
				for(int i = 0; i < docParser.getQNameCount("DwWebSite"); i++){
					bookMarkKey = docParser.getAttribute("DwWebSite", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getWebSite()==null?"":dwAddress.getWebSite());
					}
				}
				//̨ͷ��λ��Ӧ�ĵ绰
				for(int i = 0; i < docParser.getQNameCount("DwTel"); i++){
					bookMarkKey = docParser.getAttribute("DwTel", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getTel()==null?"":dwAddress.getTel());
					}
				}
				//̨ͷ��λ��Ӧ��Ͷ�ߵ绰
				for(int i = 0; i < docParser.getQNameCount("DwComplainTel"); i++){
					bookMarkKey = docParser.getAttribute("DwComplainTel", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getComplainTel()==null?"":dwAddress.getComplainTel());
					}
				}
				//̨ͷ��λ��Ӧ�Ĵ���
				for(int i = 0; i < docParser.getQNameCount("DwFax"); i++){
					bookMarkKey = docParser.getAttribute("DwFax", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getFax()==null?"":dwAddress.getFax());
					}
				}
				//̨ͷ��λ��Ӧ�ļ�����Ȩ���������ģ�
				for(int i = 0; i < docParser.getQNameCount("DwAuthorizationStatement"); i++){
					bookMarkKey = docParser.getAttribute("DwAuthorizationStatement", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getAuthorizationStatement()==null?"":dwAddress.getAuthorizationStatement());
					}
				}
				//̨ͷ��λ��Ӧ�ļ�����Ȩ������Ӣ�ģ�
				for(int i = 0; i < docParser.getQNameCount("DwAuthorizationStatementEn"); i++){
					bookMarkKey = docParser.getAttribute("DwAuthorizationStatementEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getAuthorizationStatementEn()==null?"":dwAddress.getAuthorizationStatementEn());
					}
				}
				//̨ͷ��λ��Ӧ��ʵ�����Ͽ����������ģ�
				for(int i = 0; i < docParser.getQNameCount("DwCNASStatement"); i++){
					bookMarkKey = docParser.getAttribute("DwCNASStatement", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getCnasstatement()==null?"":dwAddress.getCnasstatement());
					}
				}
				//̨ͷ��λ��Ӧ��ʵ�����Ͽ�������Ӣ�ģ�
				for(int i = 0; i < docParser.getQNameCount("DwCNASStatementEn"); i++){
					bookMarkKey = docParser.getAttribute("DwCNASStatementEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getCnasstatementEn()==null?"":dwAddress.getCnasstatementEn());
					}
				}
				//̨ͷ��λ��Ӧ����Դ���������ģ�
				for(int i = 0; i < docParser.getQNameCount("DwStandardStatement"); i++){
					bookMarkKey = docParser.getAttribute("DwStandardStatement", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getStandardStatement()==null?"":dwAddress.getStandardStatement());
					}
				}
				//̨ͷ��λ��Ӧ����Դ������Ӣ�ģ�
				for(int i = 0; i < docParser.getQNameCount("DwStandardStatementEn"); i++){
					bookMarkKey = docParser.getAttribute("DwStandardStatementEn", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, dwAddress.getStandardStatementEn()==null?"":dwAddress.getStandardStatementEn());
					}
				}
				
				/***********            ��ȫ����Ϣ                   ************/
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
				//��ȫ�루SecurityCodeA1��ǰһ�룻SecurityCodeA2����һ�룩
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
				//֤����
				for(int i = 0; i < docParser.getQNameCount("CertificateCode"); i++){
					bookMarkKey = docParser.getAttribute("CertificateCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, certificate.getCertificateCode());
					}
				}
				//֤����������
				for(int i = 0; i < docParser.getQNameCount("CertificateBarCode"); i++){
					bookMarkKey = docParser.getAttribute("CertificateBarCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						if(barCodeTempFile == null || !barCodeTempFile.exists() || barCodeTempFile.length() == 0){
							barCodeTempFile = File.createTempFile(UIDUtil.get22BitUID(), ".jpg");	//֤������������ʱ�ļ�
							try{
								BarCodeUtil.GenerateBarcode(certificate.getCertificateCode(), barCodeTempFile);
							}catch(Exception e){
								throw new Exception(String.format("֤������������ʧ�ܣ�ԭ��%s", (e==null||e.getMessage()==null)?"��":e.getMessage()));
							}
						}
						if(barCodeTempFile != null && barCodeTempFile.exists() && barCodeTempFile.length() > 0){
							docWriter.addPicByBookMark(bookMarkKey, barCodeTempFile.getAbsolutePath());
						}
					}
				}
				
				//�ͼ쵥λ
				for(int i = 0; i < docParser.getQNameCount("Customer"); i++){
					bookMarkKey = docParser.getAttribute("Customer", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getSampleFrom()==null?cSheet.getCustomerName():cSheet.getSampleFrom());
					}
				}
				//�ͼ쵥λ����ַ��
				for(int i = 0; i < docParser.getQNameCount("CustomerAddress"); i++){
					bookMarkKey = docParser.getAttribute("CustomerAddress", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getCustomerAddress());
					}
				}
				
				//������������
				for(int i = 0; i < docParser.getQNameCount("ApplianceName"); i++){
					bookMarkKey = docParser.getAttribute("ApplianceName", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, (oRecord.getApplianceName()==null||oRecord.getApplianceName().trim().length()==0)?oRecord.getTargetAppliance().getApplianceStandardName().getName():oRecord.getApplianceName());
					}
				}
				
				//�ͺ�/���
				for(int i = 0; i < docParser.getQNameCount("Model"); i++){
					bookMarkKey = docParser.getAttribute("Model", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String tempModel = oRecord.getModel()==null?"":oRecord.getModel().trim();
						String tempRange = oRecord.getRange()==null?"":oRecord.getRange().trim();
						String temp = null;
						if(tempModel.length() > 0 && tempRange.length() > 0){
							temp = tempModel + " �� " + tempRange;
						}else if(tempModel.length() > 0 || tempRange.length() > 0){
							temp = (tempModel.length() > 0)?tempModel:tempRange;						
						}else{
							temp = " �� ";
						}
						docWriter.setValueByBookMark(bookMarkKey, temp);
					}
				}
				
				//�������
				for(int i = 0; i < docParser.getQNameCount("FactoryCode"); i++){
					bookMarkKey = docParser.getAttribute("FactoryCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, String.format("%s%s", oRecord.getApplianceCode(), (oRecord.getManageCode()==null||oRecord.getManageCode().length()==0)?"":"["+oRecord.getManageCode()+"]"));
					}
				}
				
				//���쵥λ
				for(int i = 0; i < docParser.getQNameCount("Manufacturer"); i++){
					bookMarkKey = docParser.getAttribute("Manufacturer", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getManufacturer());
					}
				}
				
				/****************  �춨���ݣ������淶��   **********************/			
				//�춨���ݣ���ҳ/���棩
				String technicalDocsAll = "";	//�춨����
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
				//�춨���ݣ������淶��--���
				for(int i = 0; i < docParser.getQNameCount("TechnicalDocs-Code"); i++){
					bookMarkKey = docParser.getAttribute("TechnicalDocs-Code", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("TechnicalDocs-Code", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
				//�춨���ݣ������淶��--����
				for(int i = 0; i < docParser.getQNameCount("TechnicalDocs-Name"); i++){
					bookMarkKey = docParser.getAttribute("TechnicalDocs-Name", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("TechnicalDocs-Name", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
						Specification spe = null;
						if(speMap.containsKey(indexStr)){
							spe = speMap.get(indexStr);
						}else if(curSpeIndex < speList.size()){
							spe = speList.get(curSpeIndex++);
							speMap.put(indexStr, spe);
						}
						if(spe != null){
							docWriter.setValueByBookMark(bookMarkKey, "��"+spe.getNameCn()+"��");
						}
					}
				}
				
				
				/****************  ������׼   **********************/				
				//������׼--����
				for(int i = 0; i < docParser.getQNameCount("Standard-Name"); i++){
					bookMarkKey = docParser.getAttribute("Standard-Name", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-Name", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
						Standard std = null;
						if(stdMap.containsKey(indexStr)){
							std = stdMap.get(indexStr);
						}else if(curStdIndex < stdList.size()){
							int temp = curStdIndex;
							for(; temp < stdList.size(); temp++){
								if(!SystemCfgUtil.checkStandardVirtual(stdList.get(temp).getName())){	//��������ļ�����׼������֤������ʾ�����򣬲���֤������ʾ
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
								alertString.append("'������׼--����'������������30������������" + std.getName().length() + "��;");
							}
						}
					}
				}
				//������׼--֤����
				for(int i = 0; i < docParser.getQNameCount("Standard-CertificateCode"); i++){
					bookMarkKey = docParser.getAttribute("Standard-CertificateCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-CertificateCode", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'������׼--֤����'������������30������������" + std.getCertificateCode().length() + "��;");
							}
						}
					}
				}
				//������׼--������Χ
				for(int i = 0; i < docParser.getQNameCount("Standard-Range"); i++){
					bookMarkKey = docParser.getAttribute("Standard-Range", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-Range", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'������׼--������Χ'������������30������������" + std.getRange().length() + "��;");
							}
						}
					}
				}
				//������׼--��ȷ����
				for(int i = 0; i < docParser.getQNameCount("Standard-Uncertain"); i++){
					bookMarkKey = docParser.getAttribute("Standard-Uncertain", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-Uncertain", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'������׼--��ȷ����'������������30������������" + std.getUncertain().length() + "��;");
							}
						}
					}
				}
				//������׼--��Ч��
				for(int i = 0; i < docParser.getQNameCount("Standard-ValidDate"); i++){
					bookMarkKey = docParser.getAttribute("Standard-ValidDate", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("Standard-ValidDate", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'������׼--��Ч��'������������30������������" + validate.length() + "��;");
							}
						}
					}
				}

				/****************  ��׼����   **********************/	
				//��׼����--����
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-Name"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-Name", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-Name", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'��׼����--����'������������30������������" + stdApp.getName().length() + "��;");
							}
						}
					}
				}
				//��׼����--������Χ
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-Range"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-Range", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-Range", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'��׼����--������Χ'������������30������������" + stdApp.getRange().length() + "��;");
							}
						}
					}
				}
				//��׼����--�ͺŹ��
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-Model"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-Model", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-Model", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'��׼����--�ͺŹ��'������������30������������" + stdApp.getModel().length() + "��;");
							}
						}
					}
				}
				//��׼����--��ȷ����
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-Uncertain"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-Uncertain", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-Uncertain", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'��׼����--��ȷ����'������������30������������" + stdApp.getUncertain().length() + "��;");
							}
						}
					}
				}
				//��׼����--֤����
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-SeriaNumber"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-SeriaNumber", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-SeriaNumber", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'��׼����--֤����'������������30������������" + stdApp.getSeriaNumber().length() + "��;");
							}
						}
					}
				}
				//��׼����--���ڱ��
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-LocaleCode"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-LocaleCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-LocaleCode", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'��׼����--���ڱ��'������������30������������" + stdApp.getLocaleCode().length() + "��;");
							}
						}
					}
				}
				//��׼����--��Ч��
				for(int i = 0; i < docParser.getQNameCount("StandardAppliance-ValidDate"); i++){
					bookMarkKey = docParser.getAttribute("StandardAppliance-ValidDate", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						String indexStr = docParser.getAttribute("StandardAppliance-ValidDate", "indexStr", i);	//��ȡ˳��ţ��ڼ�����
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
								alertString.append("'��׼����--��Ч��'������������30������������" + validate.length() + "��;");
							}
						}
					}
				}
				
				
				//�춨����
				for(int i = 0; i < docParser.getQNameCount("Conclusion"); i++){
					bookMarkKey = docParser.getAttribute("Conclusion", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getConclusion()==null?"":oRecord.getConclusion());
					}
				}


				/*
				//��׼��
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
				
				//������
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
				
				//�춨Ա
				if(verifierPicPath != null && verifierPicPath.length() > 0){
					for(int i = 0; i < docParser.getQNameCount("Verifier"); i++){
						bookMarkKey = docParser.getAttribute("Verifier", "tagName", i);
						if(bookMarkKey != null && bookMarkKey.length() > 0){
							docWriter.addPicByBookMark(bookMarkKey, verifierPicPath);
						}
					}
				}
				
				//�춨Ա����ӡ��
				for(int i = 0; i < docParser.getQNameCount("VerifierPrinter"); i++){
					bookMarkKey = docParser.getAttribute("VerifierPrinter", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getSysUserByStaffId().getName());
					}
				}
				
				//�춨����-��
				for(int i = 0; i < docParser.getQNameCount("VerifyYear"); i++){
					bookMarkKey = docParser.getAttribute("VerifyYear", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getWorkDate() == null?"":FormaterYear.format(oRecord.getWorkDate()));
					}
				}
				
				//�춨����-��
				for(int i = 0; i < docParser.getQNameCount("VerifyMonth"); i++){
					bookMarkKey = docParser.getAttribute("VerifyMonth", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getWorkDate() == null?"":FormaterMonth.format(oRecord.getWorkDate()));
					}
				}
				
				//�춨����-��
				for(int i = 0; i < docParser.getQNameCount("VerifyDay"); i++){
					bookMarkKey = docParser.getAttribute("VerifyDay", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getWorkDate() == null?"":FormaterDay.format(oRecord.getWorkDate()));
					}
				}
				
				//��Ч����-��
				for(int i = 0; i < docParser.getQNameCount("ValidYear"); i++){
					bookMarkKey = docParser.getAttribute("ValidYear", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getValidity() == null?"":FormaterYear.format(oRecord.getValidity()));
					}
				}
				
				//��Ч����-��
				for(int i = 0; i < docParser.getQNameCount("ValidMonth"); i++){
					bookMarkKey = docParser.getAttribute("ValidMonth", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey,  oRecord.getValidity() == null?"":FormaterMonth.format(oRecord.getValidity()));
					}
				}
				
				//��Ч����-��
				for(int i = 0; i < docParser.getQNameCount("ValidDay"); i++){
					bookMarkKey = docParser.getAttribute("ValidDay", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey,  oRecord.getValidity() == null?"":FormaterDay.format(oRecord.getValidity()));
					}
				}
				
				java.sql.Timestamp sampleRecvDate = null;	//��Ʒ����ʱ��
				if(cSheet.getCommissionType() == 2&&cSheet.getLocaleCommissionDate()!=null){	//�ֳ���⣺�ֳ�����ʱ��
					sampleRecvDate = cSheet.getLocaleCommissionDate();
				}else{
					sampleRecvDate = cSheet.getCommissionDate();
				}
				//��Ʒ��������-�꣨���У׼֤�飩
				for(int i = 0; i < docParser.getQNameCount("SampleRecvYear"); i++){
					bookMarkKey = docParser.getAttribute("SampleRecvYear", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, sampleRecvDate == null?"":FormaterYear.format(sampleRecvDate));
					}
				}
				
				//��Ʒ��������-�£����У׼֤�飩
				for(int i = 0; i < docParser.getQNameCount("SampleRecvMonth"); i++){
					bookMarkKey = docParser.getAttribute("SampleRecvMonth", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey,  sampleRecvDate == null?"":FormaterMonth.format(sampleRecvDate));
					}
				}
				
				//��Ʒ��������-�գ����У׼֤�飩
				for(int i = 0; i < docParser.getQNameCount("SampleRecvDay"); i++){
					bookMarkKey = docParser.getAttribute("SampleRecvDay", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey,  sampleRecvDate == null?"":FormaterDay.format(sampleRecvDate));
					}
				}
				
				//�����ص�
				for(int i = 0; i < docParser.getQNameCount("WorkLocation"); i++){
					bookMarkKey = docParser.getAttribute("WorkLocation", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getWorkLocation() == null?"":oRecord.getWorkLocation());
					}
				}
				
				//�¶�
				for(int i = 0; i < docParser.getQNameCount("Temp"); i++){
					bookMarkKey = docParser.getAttribute("Temp", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getTemp()==null?"":oRecord.getTemp().toString());
					}
				}
				
				//���ʪ��
				for(int i = 0; i < docParser.getQNameCount("Humidity"); i++){
					bookMarkKey = docParser.getAttribute("Humidity", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getHumidity() == null?"":oRecord.getHumidity().toString());
					}
				}
				
				//����ѹ
				for(int i = 0; i < docParser.getQNameCount("Pressure"); i++){
					bookMarkKey = docParser.getAttribute("Pressure", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getPressure()==null?"":oRecord.getPressure().toString());
					}
				}
				
				
				
				if(excel != null){
					//��ȡExcel��֤��Ĺ��������ƺ�����
					String certificateSheetName = excelParser.getFirstAttribute("certificate-sheet", "sheetName");
					String certificateRegion = excelParser.getFirstAttribute("certificate-sheet", "region");
					bookMarkKey = docParser.getFirstAttribute("DataPage", "tagName");	//֤������ҳ��ǩ
					if(certificateSheetName != null && certificateRegion != null && bookMarkKey != null){
						/*************����Excel������ҳ��֤���ָ��λ��*************/
						//begin:ʹ��Jacob��Excel
						xlsApp = new ActiveXComponent("Excel.Application");
						xlsApp.setProperty("Visible", new Variant(false));	//����ʾ��
						Dispatch workbooks = xlsApp.getProperty("Workbooks").toDispatch();
						xlsWorkbook = Dispatch.invoke(	//�򿪲���ȡExcel�ĵ�
								workbooks,
								"Open",
								Dispatch.Method,
								new Object[] {excel.getAbsolutePath(), 
										new Variant(false),
										new Variant(true) //�Ƿ���ֻ����ʽ��:�ǡ���ֻ����ʽ��
								},
								new int[1]).toDispatch();
						//end:ʹ��Jacob��Excel
						
						docWriter.PasteExcelTable(xlsWorkbook, certificateSheetName, certificateRegion, bookMarkKey);//��������ҳ
						
						xlsApp.setProperty("CutCopyMode", false);	//����Excel���������а����Ϣ
					}
				}

				docWriter.saveAsWord(fOutDoc.getCanonicalPath()); 	//���ΪWord��ԭģ���ļ��������˳�
				if(fOutPdf != null){
					docWriter.saveAsPdf(fOutPdf.getCanonicalPath());	//���ΪPDF
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
						Dispatch.call(xlsWorkbook, "Close", new Variant(false));	//�������˳�
						xlsWorkbook = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					if(xlsApp != null){	//�ͷ�com��Դ
						xlsApp.invoke("Quit", new Variant[] {});//����ʹ�÷�����Dispatch.call(app, "Quit");
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
					//�ر�com���߳�
					ComThread.Release();
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				
				if(barCodeTempFile != null && barCodeTempFile.exists()){	//ɾ����ʱ�ļ�
					barCodeTempFile.delete();
					barCodeTempFile = null;
				}
			}
//		}
		
	}
	
	/**
	 * ���ϴ���֤���ļ����д�������֤���ź�֤�������룩
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
			//��ʼ��com���߳�
			ComThread.InitSTA();
			
			JacobWordWriter docWriter = null;//getDocWriter();
			ActiveXComponent xlsApp = null;
			Dispatch xlsWorkbook = null;
			File barCodeTempFile = null;	//֤����������ͼƬ�ļ�
			try{
				docWriter = new WordUtil().new JacobWordWriter();
				docWriter.openWord(doc.getAbsolutePath(), true);	//��ֻ����ʽ��
				String bookMarkKey = null;
				
				/***********            ��ȫ����Ϣ                   ************/
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
				//��ȫ�루SecurityCodeA1��ǰһ�룻SecurityCodeA2����һ�룩
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
				
				//֤����
				for(int i = 0; i < docParser.getQNameCount("CertificateCode"); i++){
					bookMarkKey = docParser.getAttribute("CertificateCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, certificate.getCertificateCode());
					}
				}
				//֤����������
				for(int i = 0; i < docParser.getQNameCount("CertificateBarCode"); i++){
					bookMarkKey = docParser.getAttribute("CertificateBarCode", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						if(barCodeTempFile == null || !barCodeTempFile.exists() || barCodeTempFile.length() == 0){
							barCodeTempFile = File.createTempFile(UIDUtil.get22BitUID(), ".jpg");	//֤������������ʱ�ļ�
							try{
								BarCodeUtil.GenerateBarcode(certificate.getCertificateCode(), barCodeTempFile);
							}catch(Exception e){
								throw new Exception(String.format("֤������������ʧ�ܣ�ԭ��%s", (e==null||e.getMessage()==null)?"��":e.getMessage()));
							}
						}
						if(barCodeTempFile != null && barCodeTempFile.exists() && barCodeTempFile.length() > 0){
							docWriter.replacePicByBookMark(bookMarkKey, barCodeTempFile.getAbsolutePath());
						}
					}
				}
				
				//�ͼ쵥λ
				for(int i = 0; i < docParser.getQNameCount("Customer"); i++){
					bookMarkKey = docParser.getAttribute("Customer", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						CommissionSheet cSheet = oRecord.getCommissionSheet();
						docWriter.setValueByBookMark(bookMarkKey, cSheet.getSampleFrom()==null?cSheet.getCustomerName():cSheet.getSampleFrom());
					}
				}
				//�ͼ쵥λ����ַ��
				for(int i = 0; i < docParser.getQNameCount("CustomerAddress"); i++){
					bookMarkKey = docParser.getAttribute("CustomerAddress", "tagName", i);
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						docWriter.setValueByBookMark(bookMarkKey, oRecord.getCommissionSheet().getCustomerAddress());
					}
				}
				
				docWriter.saveAsWord(fOutDoc.getCanonicalPath()); 	//���ΪWord��ԭģ���ļ��������˳�
				if(fOutPdf != null){
					docWriter.saveAsPdf(fOutPdf.getCanonicalPath());	//���ΪPDF
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
						Dispatch.call(xlsWorkbook, "Close", new Variant(false));	//�������˳�
						xlsWorkbook = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					if(xlsApp != null){	//�ͷ�com��Դ
						xlsApp.invoke("Quit", new Variant[] {});//����ʹ�÷�����Dispatch.call(app, "Quit");
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
					//�ر�com���߳�
					ComThread.Release();
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				
				if(barCodeTempFile != null && barCodeTempFile.exists()){	//ɾ����ʱ�ļ�
					barCodeTempFile.delete();
					barCodeTempFile = null;
				}
			}
//		}
		
	}
	/**
	 * ��Ӻ������Ȩǩ���˵�ǩ��
	 * @param doc
	 * @param docParser
	 * @param checkerPicPath
	 * @param authorizerPicPath
	 * @param oRecord
	 * @return
	 */
	public static boolean AddCertificateSignature(File doc, File fOutDoc, File fOutPdf, ParseXML docParser,String checkerPicPath, String authorizerPicPath, OriginalRecord oRecord) throws Exception{
//		synchronized (SynBlock) {
			//��ʼ��com���߳�
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
				docWriter.openWord(doc.getAbsolutePath(), true);	//��ֻ��д��ʽ��
				String bookMarkKey = null;
				//��׼��
				if(authorizerPicPath != null && authorizerPicPath.length() > 0){
					bookMarkKey = docParser.getAttribute("Authorizer", "tagName");
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						//docWriter.addPicByBookMark(bookMarkKey, authorizerPicPath);
						docWriter.replacePicByBookMark(bookMarkKey, authorizerPicPath);
					}
				}
				
				//��׼�ˣ���ӡ��
				bookMarkKey = docParser.getAttribute("AuthorizerPrinter", "tagName");
				if(bookMarkKey != null && bookMarkKey.length() > 0){
					docWriter.setValueByBookMark(bookMarkKey, oRecord.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
				}
				
				
				//��׼��ְ��
				bookMarkKey = docParser.getAttribute("AuthorizerPosition", "tagName");
				if(bookMarkKey != null && bookMarkKey.length() > 0){
					docWriter.setValueByBookMark(bookMarkKey, oRecord.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getAdministrationPost());
				}
				
				//������
				if(checkerPicPath != null && checkerPicPath.length() > 0){
					bookMarkKey = docParser.getAttribute("Checker", "tagName");
					if(bookMarkKey != null && bookMarkKey.length() > 0){
						//docWriter.addPicByBookMark(bookMarkKey, checkerPicPath);
						docWriter.replacePicByBookMark(bookMarkKey, checkerPicPath);
					}
				}
				
				//�����ˣ���ӡ��
				bookMarkKey = docParser.getAttribute("CheckerPrinter", "tagName");
				if(bookMarkKey != null && bookMarkKey.length() > 0){
					docWriter.setValueByBookMark(bookMarkKey, oRecord.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
				}
				
				docWriter.saveAsWord(fOutDoc.getCanonicalPath()); 	//���ΪWord��ԭģ���ļ��������˳�
				docWriter.saveAsPdf(fOutPdf.getCanonicalPath());	//���ΪPdf
				
				docWriter.closeWord(false);
				docWriter.releaseResourse();
				docWriter = null;
				
				return true;
				
			}catch(Exception ex){
				throw ex;
			}finally{
				
				try{
					if(xlsWorkbook != null){
						Dispatch.call(xlsWorkbook, "Close", new Variant(false));	//�������˳�
						xlsWorkbook = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					if(xlsApp != null){	//�ͷ�com��Դ
						xlsApp.invoke("Quit", new Variant[] {});//����ʹ�÷�����Dispatch.call(app, "Quit");
						xlsApp = null;
					}
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}
				try{
					//�ر�com���߳�
					ComThread.Release();
				}catch(Exception ex){
					log.error("error in WordUtil-->MakeCertificateWord", ex);
				}					
			}
//		}
	}
	
	
	
	/**
	 * �ڲ��ࣺʹ��Jacob��������Word
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
			app.setProperty("Visible", new Variant(false));   //����word���ɼ�
			documents = app.getProperty("Documents").toDispatch();
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
				
				activeDocument = app.getProperty("ActiveDocument").toDispatch();
				bookMarks = Dispatch.call(activeDocument, "Bookmarks").toDispatch();//����bookMarks = app.call(activeDocument, "Bookmarks").toDispatch();
			}catch(Exception e){
				throw e;
			}
		}
		
		
		/**
		 * ���沢�ر�Word
		 * @param f��Ϊtrueʱ�����޸ĵ��ļ����˳���Ϊfalseʱ�������޸ĵ��ļ����˳� 
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
		 * ���ΪWord
		 * @param filePath: ���Ϊ��·��
		 */
		public void saveAsWord(String filePath){
			if(doc != null){
				Dispatch.call(doc, "SaveAs", new Variant(filePath));
			}
		}
		
		/**
		 * ��沢�ر�Word
		 * @param f��Ϊtrueʱ�����޸ĵ��ļ����˳���Ϊfalseʱ�������޸ĵ��ļ����˳� 
		 * @param filePath: ���Ϊ��·��
		 */
		public void saveAsAndCloseWord(boolean f, String filePath){
			if(doc != null){
				saveAsWord(filePath);
				closeWord(f);
			}
		}
		
		/**
		 * ���ΪPdf
		 * @param filePath: ���Ϊ��·��
		 */
		public void saveAsPdf(String filePath){
			if(doc != null){
				Dispatch.invoke(doc, "SaveAs", Dispatch.Method, 
						new Object[] {filePath, new Variant(17) }, new int[1]); 	//17��ʾ���ΪPDF��ʽ
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
		
		/**
		 * ����ǩλ�ò�������
		 * @param bookMarkKey����ǩ��
		 * @param value�������ֵ
		 * @return ����ɹ�����true��ʧ�ܣ�û���ҵ���ǩ������false
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
		 * ����ǩλ�ò�������,���������С����Ӧ
		 * @param bookMarkKey����ǩ��
		 * @param value�������ֵ
		 * @return ����ɹ�����true��ʧ�ܣ�û���ҵ���ǩ������false
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
						alertString.append("����������30������������" + value.length() + "��;");
					}
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * ����ǩλ�ò�������,���������С����Ӧ
		 * @param bookMarkKey����ǩ��
		 * @param value�������ֵ
		 * @return ����ɹ�����true��ʧ�ܣ�û���ҵ���ǩ������false
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
		 * ����Excel���ָ����ǩλ�ô�
		 * @param xlWorkbook��Excel��
		 * @param sheetName��Excel�����������
		 * @param xlsRange�����Ƶ������磺A1:H10��
		 * @param bookMarkKey��Word��ǩ������
		 * @return ���Ƴɹ�����true,����ʧ��(�Ҳ���ָ����ǩ)����false
		 * @throws Exception
		 */
		public boolean PasteExcelTable(Dispatch xlWorkbook, String sheetName, String xlsRange, String bookMarkKey) throws Exception{
			try{
				boolean isBookMarkExist = Dispatch.call(bookMarks, "Exists",bookMarkKey).changeType(Variant.VariantBoolean).getBoolean();
				if (isBookMarkExist == true) {
					Dispatch xlsSheet = null;
					try{
						xlsSheet = Dispatch.call(xlWorkbook, "Sheets", new Object[]{sheetName}).toDispatch();	//Excel ������
					}catch(Exception e){
						throw new Exception(String.format("δ�ҵ�ԭʼ��¼Excel�ļ��ж�Ӧ֤������Ĺ�����%s", sheetName));
					}
					Boolean bVisible = Dispatch.get(xlsSheet, "Visible").changeType(Variant.VariantBoolean).getBoolean();
					if(!bVisible){	//���صĹ�����ȡ������
						Dispatch.put(xlsSheet, "Visible", new Variant(true));
					}
					Dispatch Range = Dispatch.call(xlsSheet, "Range", new Object[] { xlsRange }).toDispatch();	//���Ƶ�����
					Dispatch.call(Range, "Copy");	//����
					
					Dispatch docRangeItem = Dispatch.call(bookMarks, "Item", bookMarkKey).toDispatch();
					Dispatch docRange = Dispatch.call(docRangeItem, "Range").toDispatch();
					Dispatch.call(docRange, "PasteExcelTable", new Variant(false), new Variant(false), new Variant(false));	//����Excel��񣺰�Excel����ĸ�ʽ
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * ����ǩ�����ͼƬ
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
					Dispatch.call(picInlineShapes, "AddPicture", picPath, new Variant(false), new Variant(true)).toDispatch();	//������������LinkToFile�����Ƿ����ӵ��ļ������ĸ�������SaveWithDocument�������ļ��б���ͼƬ
					
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
		
		/**
		 * ����ǩ���滻ͼƬ
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
					
					Dispatch.put(range, "Text", "");	//�����ԭ�е�ͼƬ������
					
					Dispatch picInlineShapes = Dispatch.get(range, "InlineShapes").toDispatch();
					Dispatch.call(picInlineShapes, "AddPicture", picPath, new Variant(false), new Variant(true)).toDispatch();	//������������LinkToFile�����Ƿ����ӵ��ļ������ĸ�������SaveWithDocument�������ļ��б���ͼƬ
					
					return true;
				}
				return false;
			}catch(Exception e){
				throw e;
			}
		}
	}
}
