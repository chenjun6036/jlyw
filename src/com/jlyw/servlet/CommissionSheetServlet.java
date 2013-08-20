package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.Address;
import com.jlyw.hibernate.ApplianceManufacturer;
import com.jlyw.hibernate.AppliancePopularName;
import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.LocaleApplianceItem;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SubContractor;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularName;
import com.jlyw.manager.AddressManager;
import com.jlyw.manager.ApplianceManufacturerManager;
import com.jlyw.manager.AppliancePopularNameManager;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.CustomerContactorManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.ReasonManager;
import com.jlyw.manager.SubContractManager;
import com.jlyw.manager.SubContractorManager;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.quotation.QuotationManager;
import com.jlyw.manager.vehicle.LocaleApplianceItemManager;
import com.jlyw.manager.view.ViewAppSpeStandardPopularNameManager;
import com.jlyw.util.CommissionSheetFlagUtil;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UIDUtil;
import com.jlyw.util.FlagUtil.CommissionSheetStatus;
import com.jlyw.util.SysStringUtil.ResponseContentType;


public class CommissionSheetServlet extends HttpServlet{
	private static Log log = LogFactory.getLog(CommissionSheetServlet.class);
	private static Object MutexObjectOfNewCommissionSheet = new Object();		//���ڻ�������½�ί�е�
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doPost(req, resp);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
			Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
			CommissionSheetManager cSheetMgr = new CommissionSheetManager();
			switch (method) {
			case 0: // ����ί�е�
				try {
					String QuotationId = req.getParameter("QuotationId");	//���۵���
					String CommissionDate = req.getParameter("CommissionDate");		//ί������
					String PromiseDate = req.getParameter("PromiseDate").trim();			//��ŵ����
					String CommissionType = req.getParameter("CommissionType");		//ί����ʽ
					String CustomerName  = req.getParameter("CustomerName").trim();		//ί�е�λ
					String CustomerTel = req.getParameter("CustomerTel").trim();
					String CustomerAddress = req.getParameter("CustomerAddress").trim();
					String CustomerZipCode = req.getParameter("CustomerZipCode").trim();
					String ContactPerson = req.getParameter("ContactPerson").trim();
					String ContactorTel = req.getParameter("ContactorTel").trim();
					String SampleFrom = req.getParameter("SampleFrom");	//֤�鵥λ
					String BillingTo = req.getParameter("BillingTo");	//��Ʊ��λ
					
					String CustomerHandler = req.getParameter("CustomerHandler");	//ί����
//					String ReceiverName = req.getParameter("ReceiverName").trim();	//������
					
					String HeadNameId = req.getParameter("HeadName").trim();	//̨ͷ����ID
					String RecipientAddress = req.getParameter("RecipientAddress");	//������ַ
					String PickupAddress = req.getParameter("PickupAddress");		//ȡ����ַ
					
					String Appliances = req.getParameter("Appliances").trim();	//���������
					JSONArray appliancesArray = new JSONArray(Appliances);	//��������
					String LocaleCommissionCode = null;	//�ֳ�ί�е���
					Timestamp LocaleCommissionDate = null;	//�ֳ����ʱ��
					Integer LocaleStaffId = null;	//�ֳ���⸺����ID
					
					if(QuotationId != null){
						QuotationId = QuotationId.trim();
					}else{
						QuotationId = "";
					}
					if(CommissionType.length() == 0 || CustomerName.length() == 0 || appliancesArray.length() == 0){
						throw new Exception("ί�е���Ϣ��������Ϣ¼�벻������");
					}
					if((CommissionDate == null || CommissionDate.length() == 0) && !CommissionType.equals("2")){
						throw new Exception("ί������Ϊ�գ�");
					}
					if(SampleFrom.length() == 0){
						SampleFrom = CustomerName;
					}
					if(BillingTo.length() == 0){
						BillingTo = CustomerName;
					}
					if(RecipientAddress == null || RecipientAddress.length() == 0){
						RecipientAddress = null;
					}
					if(PickupAddress == null || PickupAddress.length() == 0){
						PickupAddress = null;
					}
					
					Timestamp today = new Timestamp(System.currentTimeMillis());
					CustomerManager cusMgr = new CustomerManager();		//�ͻ�����Mgr
					Integer CustomerId;
					List<Customer> cusList = cusMgr.findByVarProperty(new KeyValueWithOperator("name",CustomerName,"="), 
							new KeyValueWithOperator("status", 1, "<>"));	//����ί�е�λ��ID
					if(cusList != null && cusList.size() == 1){
						CustomerId = cusList.get(0).getId();
					}else if(cusList != null && cusList.size() > 1){
						throw new Exception("���ݿ����ҵ����������ͬ��ί�е�λ:"+CustomerName+", �뵽��ί�е�λ��Ϣ���������޸ģ�");
					}else{
						throw new Exception("ί�е�λ�����ڣ��¿ͻ������½�ί�е�λ��");
					}
					
					if(QuotationId.length() > 0){
						if(new QuotationManager().findById(QuotationId) == null){
							throw new Exception(String.format("���۵���:%s ��Ч��", QuotationId));
						}
					}
					if(CommissionType.equals("2")){	//�ֳ����
						LocaleCommissionCode = req.getParameter("LocaleCommissionCode");	//�ֳ�ί�е���
						String LocaleCommissionDateStr = req.getParameter("LocaleCommissionDate");	//�ֳ����ʱ��
						String LocaleStaffName = req.getParameter("LocaleStaffId");	//�ֳ�����������
						if(LocaleCommissionCode == null || LocaleCommissionCode.length() == 0 ||
								LocaleCommissionDateStr == null || LocaleCommissionDateStr.length() == 0 ||
								LocaleStaffName == null || LocaleStaffName.length() == 0){
							throw new Exception("�ֳ������Ϣ��д��������");
						}
						CommissionDate = LocaleCommissionDateStr;	//ί�����ڼ�Ϊ�ֳ��������
						LocaleCommissionDate = new Timestamp(DateTimeFormatUtil.DateFormat.parse(LocaleCommissionDateStr).getTime());
						UserManager userMgr = new UserManager();
						List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name", LocaleStaffName, "="), new KeyValueWithOperator("status", 0, "="));
						if(userList != null && userList.size() > 0){
							LocaleStaffId = userList.get(0).getId();
						}else{
							throw new Exception(String.format("�ֳ���⸺���� '%s' �����ڣ�������ѡ��", LocaleStaffName));
						}
					}
										
//					Integer ReceiverId2 = null;	//������
//					if(ReceiverName == null || ReceiverName.trim().length() == 0){
//						ReceiverName = null;
//						ReceiverId2 = null;
//					}else{
//						UserManager userMgr = new UserManager();
//						List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",ReceiverName.trim(),"="));
//						if(userList != null && userList.size() > 0){
//							ReceiverName = userList.get(0).getName();
//							ReceiverId2 = userList.get(0).getId();
//						}else{
//							throw new Exception(String.format("������ '%s' �����ڣ�������ѡ��", ReceiverName));
//						}
//					}

					
					
					ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//���߷������Mgr
					ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//���߱�׼���ƹ���Mgr
					AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//���߳������ƹ���Mgr
					SubContractorManager subConMgr = new SubContractorManager();	//ת��������Mgr
					ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//���쳧����Mgr
					
					AddressManager addrMgr = new AddressManager();
					Address HeadNameAddr = new AddressManager().findById(Integer.parseInt(HeadNameId));	//̨ͷ���Ƶĵ�λ
					
					QualificationManager qualMgr = new QualificationManager();	//�����Ա���ʹ���Mgr
					List<Integer> qualList = new ArrayList<Integer>();
					qualList.add(FlagUtil.QualificationType.Type_Jianding);
					qualList.add(FlagUtil.QualificationType.Type_Jianyan);
					qualList.add(FlagUtil.QualificationType.Type_Jiaozhun);
					
					List<CommissionSheet> comList = new ArrayList<CommissionSheet>();	//ί�е��б�
					List<Integer> idList = new ArrayList<Integer>();	//ί�е��б�����Ŀ��Ӧǰ̨����¼��ID
					List<SubContractor> subConList = new ArrayList<SubContractor>();	//ί�е��б��Ӧ��ת��������ί�е�û��ת��������Ϊnull
					List<SysUser> alloteeList = new ArrayList<SysUser>();	//ί�е��б��Ӧ���ɶ��ˣ���ί�е�û���ɶ��ˣ���Ϊnull
					Timestamp commissionDate = new Timestamp(Date.valueOf(CommissionDate).getTime());	//ί������
					LocaleApplianceItemManager locAppItemMgr = new LocaleApplianceItemManager();	//�ֳ����������Ŀ��Mgr
					
					synchronized(MutexObjectOfNewCommissionSheet) {
						//begin-��ѯ��ί�е���������ί�е���
						String queryCode = String.format("%d%d", today.getYear()+1900,Integer.parseInt(CommissionType));		//��ѯί�е�����ʽ������ί����ʽ��ͬ��־λ��ͬ
						String queryString = "select max(model.code) from CommissionSheet as model where model.code like ?";
						List<Object> retList = cSheetMgr.findByHQL(queryString, queryCode+"%");
						Integer codeBeginInt = Integer.parseInt("000001");	//ί�е����
						if(retList.size() > 0 && retList.get(0) != null){
							codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(5)) + 1;
						}
						//end-��ѯ��ί�е���������ί�е���
						
						/********************   ��ί�е�    ******************/
						for(int i = 0; i < appliancesArray.length(); i++){
							JSONObject jsonObj = appliancesArray.getJSONObject(i);
							idList.add(jsonObj.getInt("Id"));		//ǰ��ҳ�����¼��ID
							CommissionSheet comSheet = new CommissionSheet();
							comSheet.setCommissionDate(commissionDate);	//ί������
							if(PromiseDate.length() > 0){
								comSheet.setPromiseDate(Date.valueOf(PromiseDate));	//��ŵ����
							}
							if(QuotationId.length() == 0){			//���۵���
								comSheet.setQuotationId(null);
							}else{
								comSheet.setQuotationId(QuotationId);
							}
							
							comSheet.setCommissionType(Integer.parseInt(CommissionType));//ί����ʽ
							comSheet.setCustomerId(CustomerId);	//ί�е�λID
							comSheet.setCustomerName(CustomerName);
							comSheet.setCustomerTel(CustomerTel);
							comSheet.setCustomerAddress(CustomerAddress);
							comSheet.setCustomerZipCode(CustomerZipCode);
							comSheet.setCustomerContactor(ContactPerson);
							comSheet.setCustomerContactorTel(ContactorTel);
							comSheet.setSampleFrom(SampleFrom);
							comSheet.setBillingTo(BillingTo);
							comSheet.setCustomerHandler(CustomerHandler);	//ί����
							comSheet.setReceiverId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//������ID
							comSheet.setReceiverName(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//����������
							
							comSheet.setHeadNameId(HeadNameAddr.getId());	//̨ͷ����ID
							comSheet.setHeadName(HeadNameAddr.getHeadName());	//̨ͷ����
							comSheet.setHeadNameEn(HeadNameAddr.getHeadNameEn()==null?"":HeadNameAddr.getHeadNameEn());	//̨ͷ����Ӣ��
							comSheet.setSampleAddress(RecipientAddress==null?null:Integer.parseInt(RecipientAddress));//��Ʒ���յص�
							comSheet.setReportAddress(PickupAddress==null?null:Integer.parseInt(PickupAddress));	//ȡ����ȡ����ص�
							
							comSheet.setLocaleCommissionCode(LocaleCommissionCode);	//�ֳ�ί�����
							comSheet.setLocaleCommissionDate(LocaleCommissionDate);	//�ֳ����ʱ��
							comSheet.setLocaleStaffId(LocaleStaffId);	//�ֳ�������ID
							
							//ί�е�������Ϣ��������Ϣ��
							comSheet.setCode(queryCode+String.format("%06d", codeBeginInt++));	//ί�е����
							int pwd =  (int) (Math.random()*9000+1000);	//���룺��λ�������1000~9999��
							comSheet.setPwd(new Integer(pwd).toString());
							comSheet.setCreatorId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//ί�е�������ID
							comSheet.setCreateDate(today);	//ί�е�����ʱ��
							comSheet.setStatus(0);	//ί�е�״̬�����ռ�
							comSheet.setAttachment(UIDUtil.get22BitUID());	//����������
							
							/**********************   �ֳ�ҵ����Ϣ   ************************/
							if(CommissionType.equals("2")){
								if(!jsonObj.has("LocaleApplianceId") || jsonObj.get("LocaleApplianceId").toString().length() == 0){	//�ֳ������ĿId
									throw new Exception("�ֳ������Ϣ���������ֳ������ĿIDΪ�գ�");
								}
								String LocaleApplianceId = jsonObj.getString("LocaleApplianceId").toString();	//�ֳ����������Ŀ��ID
								LocaleApplianceItem locAppItem = locAppItemMgr.findById(Integer.parseInt(LocaleApplianceId));
								if(locAppItem == null){
									throw new Exception("�Ҳ����ֳ������ĿIDΪ��"+LocaleApplianceId+"���ļ�¼��");
								}else if(!LocaleCommissionCode.equalsIgnoreCase(locAppItem.getLocaleMission().getCode())){
									throw new Exception("�ֳ�ί����ţ�"+LocaleCommissionCode+" ���Ҳ��������ĿIDΪ��"+LocaleApplianceId+"���ļ�¼��");								
								}else if(!CustomerId.equals(locAppItem.getLocaleMission().getCustomer().getId())){
									throw new Exception("�ֳ�ί����ţ�"+LocaleCommissionCode+" ����Ӧ��ί�е�λID�������ί�е�λID��һ�£�");
								}
								//�ж�ί�е����ݿ����Ƿ��Ѿ����ڸü����ĿID��Ӧ��ί�е�
								if(cSheetMgr.getTotalCount(new KeyValueWithOperator("status", 10, "<>"),
										new KeyValueWithOperator("localeApplianceItemId", locAppItem.getId(), "=")) > 0){
									throw new Exception("�ֳ������ĿIDΪ��"+LocaleApplianceId+"���ļ�¼�Ѿ�����ί�е��ˣ������ظ����ɣ�");
								}
								comSheet.setLocaleApplianceItemId(locAppItem.getId());							
							}
							/**********************   ���������Ϣ    ************************/
							String SpeciesType = jsonObj.get("SpeciesType").toString();	//���߷�������
							String ApplianceSpeciesId = jsonObj.get("ApplianceSpeciesId").toString();	//�������ID/��׼����ID
							String ApplianceName = jsonObj.getString("ApplianceName");	//��������
							String Manufacturer= jsonObj.getString("Manufacturer");	//���쳧
							
							if(Integer.parseInt(SpeciesType) == 0){	//0:��׼���ƣ�1����������
								comSheet.setSpeciesType(false);	
								String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
								if(ApplianceName == null || ApplianceName.trim().length() == 0){
									ApplianceName = stdName;	//��������δ��д����Ĭ��Ϊ��׼���ƻ��������
								}else{	//�������д���ж��Ƿ���ڱ�׼���ƣ���������ڱ�׼���ƣ�����볣�����Ʊ���
									if(!stdName.equalsIgnoreCase(ApplianceName.trim())){
										List<AppliancePopularName> popRetList = popNameMgr.findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("popularName", ApplianceName.trim(), "="));
										if(popRetList != null && popRetList.size() == 0){
											ApplianceStandardName sNameTemp = new ApplianceStandardName();
											sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
											AppliancePopularName popNameTemp = new AppliancePopularName();
											popNameTemp.setApplianceStandardName(sNameTemp);
											popNameTemp.setPopularName(ApplianceName);
											popNameTemp.setBrief(LetterUtil.String2Alpha(ApplianceName.trim()));
											popNameTemp.setStatus(0);
											popNameMgr.save(popNameTemp);
										}
									}
								}
								
								//�����������쳧
								if(Manufacturer != null && Manufacturer.trim().length() > 0){
									int intRet = mafMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("manufacturer", Manufacturer.trim(), "="));
									if(intRet == 0){
										ApplianceStandardName sNameTemp = new ApplianceStandardName();
										sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
										ApplianceManufacturer maf = new ApplianceManufacturer();
										maf.setApplianceStandardName(sNameTemp);
										maf.setManufacturer(Manufacturer.trim());
										maf.setBrief(LetterUtil.String2Alpha(Manufacturer.trim()));
										maf.setStatus(0);
										mafMgr.save(maf);
									}
								}
							}else{
								comSheet.setSpeciesType(true);	
								if(ApplianceName == null || ApplianceName.trim().length() == 0){
									ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//��������δ��д����Ĭ��Ϊ��׼���ƻ��������
								}
							}
							comSheet.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));
							
							comSheet.setApplianceName(ApplianceName);	//����������
							comSheet.setAppFactoryCode(jsonObj.getString("ApplianceCode"));		//�������
							comSheet.setAppManageCode(jsonObj.getString("AppManageCode"));		//������
							comSheet.setApplianceModel(jsonObj.getString("Model"));		//�ͺŹ��
							comSheet.setRange(jsonObj.getString("Range"));		//������Χ
							comSheet.setAccuracy(jsonObj.getString("Accuracy"));	//���ȵȼ�
							comSheet.setManufacturer(jsonObj.getString("Manufacturer"));		//���쳧��
							comSheet.setQuantity(Integer.parseInt(jsonObj.get("Quantity").toString()));		//̨����
							comSheet.setMandatory(jsonObj.getInt("MandatoryInspection")==0?false:true);	//ǿ�Ƽ���
							comSheet.setUrgent(jsonObj.getInt("Urgent")==0?false:true);		//�Ƿ�Ӽ�
							comSheet.setSubcontract(jsonObj.getInt("Trans")==0?false:true);		//�Ƿ�ת����0��ת����1:����Ҫת����
							String SubContractor = jsonObj.getString("SubContractor");
							if(jsonObj.getInt("Trans")==0 && SubContractor!= null && SubContractor.trim().length() > 0){	//ת��
								List<SubContractor> subConRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
								if(subConRetList != null && subConRetList.size() > 0){
									subConList.add(subConRetList.get(0));
								}else{
									subConList.add(null);
								}
							}else{
								subConList.add(null);
							}
							comSheet.setAppearance(jsonObj.getString("Appearance"));//��۸���
							comSheet.setRepair(jsonObj.getInt("Repair")==0?false:true);		//�������
							comSheet.setReportType(jsonObj.getInt("ReportType"));	//������ʽ
							comSheet.setOtherRequirements(jsonObj.getString("OtherRequirements"));	//����Ҫ��
							comSheet.setLocation(jsonObj.getString("Location"));		//���λ��						
										
							/**********************  �ж��ɶ����Ƿ���ڼ���Ч�������뵽alloteeList   ****************************/
							String Allotee = jsonObj.getString("Allotee");
							if(Allotee != null && Allotee.trim().length() > 0){
								Allotee = Allotee.trim();
								comSheet.setAllotee(Allotee);		//�ɶ���
								List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, qualList);
								if(qualRetList != null && qualRetList.size() > 0){
									boolean alloteeChecked = false;
									for(Object[] objArray : qualRetList){
										if(!qualMgr.checkUserQualify((Integer)objArray[0], comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//û�иü�����Ŀ�ļ�����������
											alloteeChecked = true;
											SysUser tempUser = new SysUser();
											tempUser.setId((Integer)objArray[0]);
											tempUser.setName((String)objArray[1]);
											
											alloteeList.add(tempUser);
											comSheet.setStatus(1);	//����ί�е�״̬���ѷ���
											break;
										}
									}
									
									if(!alloteeChecked){
										throw new Exception(String.format("�ɶ��� '%s' �����ڻ�û�����ʼ�����Ŀ��%s��������ѡ��", Allotee, comSheet.getApplianceName()));
									}
								}else{
									throw new Exception(String.format("�ɶ��� '%s' �����ڻ�û�����ʼ�����Ŀ��%s��������ѡ��", Allotee, comSheet.getApplianceName()));
								}
							}else{
								comSheet.setAllotee(null);		//�ɶ���
								alloteeList.add(null);
							}
							comList.add(comSheet);
						}
						//���Ϊ�ֳ���⣬�жϱ��μ���������Ŀ�Ƿ����ֳ�ί�����������Ŀ���
						if(CommissionType.equals("2")){
							int totalLocAppItem = locAppItemMgr.getTotalCount(new KeyValueWithOperator("localeMission.code", LocaleCommissionCode, "="));
							if(totalLocAppItem != comList.size()){
								throw new Exception("�ֳ�ί����ţ�"+LocaleCommissionCode+" �µ�������Ŀ�����뱾���ύ��������Ŀ������һ�£�");
							}
						}
						
						if(cSheetMgr.saveByBatch(comList,subConList,alloteeList,(SysUser)req.getSession().getAttribute("LOGIN_USER"),(SysUser)req.getSession().getAttribute("LOGIN_USER"),today)){
							try{
								/************  ���»�����ί�е�λ��ϵ��   *************/
								if(ContactPerson.length() > 0){
									CustomerContactorManager cusConMgr = new CustomerContactorManager();
									List<CustomerContactor> cusConList = cusConMgr.findByVarProperty(new KeyValueWithOperator("customerId", CustomerId, "="), new KeyValueWithOperator("name", ContactPerson, "="));
									if(cusConList != null){
										if(cusConList.size() > 0){
											CustomerContactor c = cusConList.get(0);
											if(ContactorTel.length() > 0){
												if(!ContactorTel.equalsIgnoreCase(c.getCellphone1()) && (c.getCellphone2() == null || c.getCellphone2().length() == 0)){
													c.setCellphone2(c.getCellphone1());
												}
												c.setCellphone1(ContactorTel);
											}
											c.setLastUse(today);
											c.setCount(c.getCount()+1);
											cusConMgr.update(c);
										}else{
											CustomerContactor c = new CustomerContactor();
											///////////////////////////////////////
											Customer a=new Customer();
											a.setId(CustomerId);
											c.setCustomer(a);
											//////////////////////////////////////
											//c.setCustomerId(CustomerId);
											c.setName(ContactPerson);
											c.setCellphone1(ContactorTel);
											c.setLastUse(today);
											c.setCount(1);
											cusConMgr.save(c);
										}
									}
								}
							}catch(Exception e){ }
							JSONObject retObj=new JSONObject();
							retObj.put("IsOK", true);
							JSONArray retArray = new JSONArray();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
	//						UserManager userMgr = new UserManager();
	//						String ReceiverName = ReceiverId.length()>0?userMgr.findById(Integer.parseInt(ReceiverId)).getName():"";//����������
							
							Address RecipientAddressObj = RecipientAddress==null?null:addrMgr.findById(Integer.parseInt(RecipientAddress)); //��Ʒ���յص�
							Address PickupAddressObj = PickupAddress==null?null:addrMgr.findById(Integer.parseInt(PickupAddress));	//ȡ����ȡ����ص�
							UserManager uMgr = new UserManager();
							for(int i = 0; i < comList.size(); i++){
								CommissionSheet comSheet = comList.get(i);
								JSONObject record = new JSONObject();
								record.put("Id", idList.get(i));
								record.put("CommissionNumber", comSheet.getCode());		//ί�е���
								JSONObject printRecord = new JSONObject();
								printRecord.put("Code", comSheet.getCode());	//ί�е���
								printRecord.put("Pwd", comSheet.getPwd());	//ί�е�����
								printRecord.put("CommissionDate", sdf.format(comSheet.getCommissionDate()));	//ί������
								printRecord.put("CustomerName", comSheet.getCustomerName());	//ί�е�λ����
								printRecord.put("CustomerTel", comSheet.getCustomerTel());	//ί�е�λ�绰
								printRecord.put("CustomerAddress", comSheet.getCustomerAddress());	//ί�е�λ��ַ
								printRecord.put("CustomerZipCode", comSheet.getCustomerZipCode());	//ί�е�λ��������
								printRecord.put("SampleFrom", comSheet.getSampleFrom());	//֤�鵥λ����
								printRecord.put("BillingTo", comSheet.getBillingTo());	//��Ʊ��λ����
								
								printRecord.put("ApplianceName", comSheet.getApplianceName());	//��������
								printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", comSheet.getApplianceModel(),comSheet.getRange(),comSheet.getAccuracy(),comSheet.getManufacturer()));//������Ϣ
								printRecord.put("ApplianceNumber",String.format("%s[%s]", comSheet.getAppFactoryCode(),comSheet.getAppManageCode()));//���߱��
								printRecord.put("Quantity",comSheet.getQuantity().toString());//̨����
								printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(comSheet.getMandatory()));//ǿ�Ƽ���
								printRecord.put("Appearance", comSheet.getAppearance());//��۸���
								printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(comSheet.getRepair()));//�������
								printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(comSheet.getReportType()));//������ʽ
								printRecord.put("OtherRequirements", comSheet.getOtherRequirements());//����Ҫ��
								printRecord.put("Location", comSheet.getLocation());	//���λ��
								printRecord.put("Allotee", comSheet.getAllotee());	//�ɶ���
								printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//����������
								
								printRecord.put("HeadName",HeadNameAddr.getHeadName());	//̨ͷ����
								printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//������ַ
								printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
								printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//ȡ����ַ
								printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
								
								
								SysUser allotee = alloteeList.get(i);
								if(allotee != null){
									 SysUser tempUser = uMgr.findById(allotee.getId());
									printRecord.put("AlloteeJobNum",tempUser.getJobNum());	//Ա������
								}else{
									printRecord.put("AlloteeJobNum","");	//Ա������
								}
								
								record.put("PrintObj", printRecord);
								retArray.put(record);
							}
							retObj.put("CommissionSheetList", retArray);

							resp.setContentType("text/html;charset=utf-8");
							resp.getWriter().write(retObj.toString());
						}else{
							throw new Exception("����ί�е���Ϣʧ�ܣ�");
						}
					} //end of synchronized
				} catch(NumberFormatException e){	//�ַ���תInteger����
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("����ʧ�ܣ�������Ϣ���������벻�������ʽ����"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.NumberFormatException.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 0", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 0", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}catch (Exception e){
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("����ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 0", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 0", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}
				break;
			case 1: //����ί�е�λ����ʷ�ͼ�����
				JSONObject retJSON = new JSONObject();
				int totalSize = 0;
				try {
					JSONArray jsonArray = new JSONArray();
					int page = 0;	//��ǰҳ��
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;	//ҳ���С
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
					String CustomerName  = req.getParameter("CustomerName");
					if(CustomerName != null && CustomerName.trim().length() > 0){
						String ApplianceName = req.getParameter("ApplianceName");
						String BeginDate = req.getParameter("BeginDate");
						String EndDate = req.getParameter("EndDate");
						String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //���jquery����������������
						
						List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
						condList.add(new KeyValueWithOperator("status", 10, "<>"));//ѡ��ί�е���δע����
						condList.add(new KeyValueWithOperator("customerName",cusName,"="));
						if(BeginDate != null && BeginDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(BeginDate).getTime()), ">="));
						}
						if(EndDate != null && EndDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(EndDate).getTime()), "<"));
						}
						if(ApplianceName != null && ApplianceName.trim().length() > 0 ){
							String appName = URLDecoder.decode(ApplianceName.trim(), "UTF-8");
							condList.add(new KeyValueWithOperator("applianceName", "%"+appName+"%", "like"));
						}
						totalSize = cSheetMgr.getTotalCount(condList);
						List<CommissionSheet> retList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, condList);
						if(retList != null && retList.size() > 0){
							ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();
							ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
							SubContractManager subConMgr = new SubContractManager();	//ת����¼Mgr
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							for(CommissionSheet cSheet : retList){
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("CustomerName", cSheet.getCustomerName());	//ί�е�λ
								jsonObj.put("CommissionCode", cSheet.getCode());	//ί�е���
								jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//���߷���Id(�����߱�׼����ID)
								if(cSheet.getSpeciesType()){	//������Ȩ�����ࣩ����
									jsonObj.put("SpeciesType", 1);	//���߷�������
									ApplianceSpecies spe = speciesMgr.findById(cSheet.getApplianceSpeciesId());
									if(spe != null){
										jsonObj.put("ApplianceSpeciesName", spe.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
									}else{
										continue;
									}
								}else{	//���߱�׼����
									jsonObj.put("SpeciesType", 0);
									ApplianceStandardName stName = standardNameMgr.findById(cSheet.getApplianceSpeciesId());
									if(stName != null){
										jsonObj.put("ApplianceSpeciesName", stName.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
									}else{
										continue;
									}
								}
								jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//�������ƣ��������ƣ�
								jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//�������
								jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//������
								jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//�ͺŹ��
								jsonObj.put("Range", cSheet.getRange());		//������Χ
								jsonObj.put("Accuracy", cSheet.getAccuracy());	//���ȵȼ�
								jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//���쳧��
								jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//̨/����
								jsonObj.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//ǿ�Ƽ���
								jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//�Ӽ�
								jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//ת��
								if(!cSheet.getSubcontract()){	//0��ת��
									List<SubContract> subRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
									if(subRetList != null && subRetList.size() > 0){
										jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//ת����
									}else{
										jsonObj.put("SubContractor", "");	//ת����
									}
								}else{
									jsonObj.put("SubContractor", "");
								}
								jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//��۸���
								jsonObj.put("Repair", cSheet.getRepair()?1:0);	//����
								jsonObj.put("ReportType", cSheet.getReportType());	//������ʽ
								jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//����Ҫ��
								jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//���λ��
								jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//�ɶ���
								jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//ί������
								jsonArray.put(jsonObj);	
							}
						}
					}
					retJSON.put("total", totalSize);
					retJSON.put("rows", jsonArray);
				} catch (Exception e) {
					
					try {
						retJSON.put("total", 0);
						retJSON.put("rows", new JSONArray());
					} catch (JSONException e1) {
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 1", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 1", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON.toString());
				}
				break;
			case 2:	//ע��ί�е�(�������½�ί�е��Ľ�����ע��������ע��ί�е��Ĺ�����case 12)
				JSONObject retJSON2 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//ί�е���
					String Pwd = req.getParameter("Pwd");	//ί�е�����
					
					if(Code == null || Code.trim().length() == 0 || Pwd == null || Pwd.trim().length() == 0){
						throw new Exception("ί�е��Ż�����Ϊ�գ�");
					}
					List<CommissionSheet> cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="),new KeyValueWithOperator("pwd", Pwd, "="));
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						CommissionSheet cSheet = cSheetRetList.get(0);
						if(cSheet.getStatus() != FlagUtil.CommissionSheetStatus.Status_YiZhuXiao){	//ί�е���δע��
							cSheet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiZhuXiao);
							//����ע��������Ϣ
							cSheet.setCancelDate(new Timestamp(System.currentTimeMillis()));
							cSheet.setCancelExecuterId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId()); //ע������ִ����
							if(!cSheetMgr.update(cSheet)){
								throw new Exception("�ύ������ʧ�ܣ�");
							}
						}
						retJSON2.put("IsOK", true);
					}else{
						throw new Exception("ί�е��Ż��������");
					}
				} catch (Exception e){
					
					try {
						retJSON2.put("IsOK", false);
						retJSON2.put("msg", String.format("ע��ί�е�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 2", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 2", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON2.toString());
				}
				break;
			case 3:	//����ί�е�����Ϣ
				JSONObject retJSON3 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//ί�е���
					String Pwd = req.getParameter("Pwd");	//ί�е�����
					
					if(Code == null || Code.trim().length() == 0 || Pwd == null || Pwd.trim().length() == 0){
						throw new Exception("ί�е��Ż�����Ϊ�գ�");
					}
					ViewAppSpeStandardPopularNameManager mainMgr = new ViewAppSpeStandardPopularNameManager();
					List<CommissionSheet> cSheetRetList=new ArrayList<CommissionSheet>();
					if(req.getParameter("Type")==null){
						cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="),new KeyValueWithOperator("pwd", Pwd, "="));
					}else{
						cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="),new KeyValueWithOperator("pwd", Pwd, "="),new KeyValueWithOperator("status", -1, "="));
					}
						
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						CommissionSheet cSheet = cSheetRetList.get(0);
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						retJSON3.put("IsOK", true);
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CommissionId", cSheet.getId());	//ί�е�ID
						jsonObj.put("CommissionCode", cSheet.getCode());
						jsonObj.put("CommissionPwd", cSheet.getPwd());
						jsonObj.put("CommissionStatus", cSheet.getStatus());	//ί�е�״̬
						jsonObj.put("CommissionType", cSheet.getCommissionType());
						jsonObj.put("CommissionDate", sf.format((cSheet.getCommissionType()==2 && cSheet.getLocaleCommissionDate()!=null)?cSheet.getLocaleCommissionDate():cSheet.getCommissionDate()));
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("CustomerTel", cSheet.getCustomerTel());
						jsonObj.put("CustomerAddress", cSheet.getCustomerAddress());
						jsonObj.put("CustomerZipCode", cSheet.getCustomerZipCode());
						jsonObj.put("ContactPerson", cSheet.getCustomerContactor());
						jsonObj.put("ContactorTel", cSheet.getCustomerContactorTel());
						jsonObj.put("SampleFrom", cSheet.getSampleFrom());
						jsonObj.put("CustomerHandler", cSheet.getCustomerHandler());//ί����
						jsonObj.put("BillingTo", cSheet.getBillingTo());
						jsonObj.put("ApplianceName", cSheet.getApplianceName());	//��������	
						if(cSheet.getSpeciesType()!=null){
							jsonObj.put("SpeciesType", cSheet.getSpeciesType()?1:0);
							jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());
							String queryString = "from ViewApplianceSpecialStandardNamePopularName as model " +
								" where model.id.id = ? and model.id.type = ? ";
							List<ViewApplianceSpecialStandardNamePopularName> vRetList = mainMgr.findPageAllByHQL(queryString, 1, 30, cSheet.getApplianceSpeciesId(), jsonObj.getInt("SpeciesType"));
							String ApplianceSpeciesName="";
							
							if(vRetList!=null&&vRetList.size()>0){
								ApplianceSpeciesName=vRetList.get(0).getName();
							}
							
							jsonObj.put("ApplianceSpeciesName", ApplianceSpeciesName);
						}
						jsonObj.put("HeadName", cSheet.getHeadNameId());
						jsonObj.put("RecipientAddress", cSheet.getSampleAddress());	//������ַ
						jsonObj.put("PickupAddress", cSheet.getReportAddress());	//ȡ����ַ
						
						jsonObj.put("Model", cSheet.getApplianceModel());
						jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//�������
						jsonObj.put("ApplianceManageCode", cSheet.getAppManageCode());	//������
						jsonObj.put("Manufacturer", cSheet.getManufacturer());
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("Mandatory", cSheet.getMandatory()?1:0);	//ǿ�Ƽ춨
						jsonObj.put("Ness", cSheet.getUrgent()?1:0);			//�Ƿ�Ӽ�
						jsonObj.put("Repair", cSheet.getRepair()?1:0);			//�Ƿ�����
						jsonObj.put("Trans", cSheet.getSubcontract()?1:0);			//�Ƿ�ת��
						jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());//�ɶ���
						jsonObj.put("Appearance", cSheet.getAppearance());	//��۸���
						jsonObj.put("OtherRequirements", cSheet.getOtherRequirements());	//����Ҫ��
						jsonObj.put("Status", cSheet.getStatus());	//ί�е�״̬
						jsonObj.put("ReportType", cSheet.getReportType());	//������ʽ
						jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//���λ��
						jsonObj.put("TotalFee", cSheet.getTotalFee());	//ί�е��ܼƷ���
						jsonObj.put("Attachment", cSheet.getAttachment());
						jsonObj.put("Remark", cSheet.getRemark());
						jsonObj.put("AlloteeRule", SystemCfgUtil.getTaskAllotRule()==0?0:1);
						retJSON3.put("CommissionObj", jsonObj);
					}else{	
						if(req.getParameter("Type")==null){
							throw new Exception("ί�е��Ż��������"+req.getParameter("Type"));
						}else{
							throw new Exception("ί�е��Ż�������󣬻��߸�ί�е�����Ԥ��ί�е���"+req.getParameter("Type"));
						}
					}
				} catch (Exception e){
					
					try {
						retJSON3.put("IsOK", false);
						retJSON3.put("msg", String.format("��ѯί�е�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 3", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 3", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retJSON3.toString());
				}
				break;
			case 4://����ί�е��š�ί�е�λ�Ȳ�ѯί�е���Ϣ
				JSONObject retJSON4 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//ί�е���
					String CustomerId = req.getParameter("CustomerId");//ί�е�λ
					String CustomerName = req.getParameter("CustomerName");//ί�е�λ����
					String DateFrom = req.getParameter("DateFrom");
					String DateEnd = req.getParameter("DateEnd");
					String Status = req.getParameter("Status");
					int page = 1;
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
					
					List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
					if(Code != null && Code.length() > 0){
						keys.add(new KeyValueWithOperator("code", "%"+Code+"%", "like"));
					}
					if(CustomerId!=null && CustomerId.length() > 0){
						keys.add(new KeyValueWithOperator("customerId", Integer.valueOf(CustomerId), "="));
					}
					if(CustomerName!=null && CustomerName.length() > 0){
						String CustomerNameStr = URLDecoder.decode(CustomerName, "utf-8");
						keys.add(new KeyValueWithOperator("customerName", "%"+CustomerNameStr+"%", "like"));
					}
					if(DateFrom != null && DateFrom.length() > 0){
						Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", Start, ">="));
					}
					if(DateEnd != null && DateEnd.length() > 0){
						Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", End, "<="));
					}
					if(Status != null && Status.length() > 0 ){
						keys.add(new KeyValueWithOperator("status", Integer.valueOf(Status), "="));
					}
					
					List<CommissionSheet> cSheetRetList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, keys);
					int total = cSheetMgr.getTotalCount(keys);
					JSONArray options = new JSONArray();
					CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						for(CommissionSheet cSheet : cSheetRetList)
						{
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("Id", cSheet.getId());
							jsonObj.put("DetailListCode", cSheet.getDetailListCode());
							jsonObj.put("Code", cSheet.getCode());
							jsonObj.put("Pwd", cSheet.getPwd());
							jsonObj.put("CustomerId", cSheet.getCustomerId());
							jsonObj.put("CustomerName", cSheet.getCustomerName());
							jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//���߷���Id(�����߱�׼����ID)
							if(cSheet.getSpeciesType()){	//������Ȩ�����ࣩ����
								jsonObj.put("SpeciesType", 1);	//���߷�������
								ApplianceSpecies spe = (new ApplianceSpeciesManager()).findById(cSheet.getApplianceSpeciesId());
								if(spe != null){
									jsonObj.put("ApplianceSpeciesName", spe.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
								}else{
									continue;
								}
							}else{	//���߱�׼����
								jsonObj.put("SpeciesType", 0);
								ApplianceStandardName stName = (new ApplianceStandardNameManager()).findById(cSheet.getApplianceSpeciesId());
								if(stName != null){
									jsonObj.put("ApplianceSpeciesName", stName.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
								}else{
									continue;
								}
							}
							jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//�������ƣ��������ƣ�
							jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//�������
							jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//������
							jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//�ͺŹ��
							jsonObj.put("Range", cSheet.getRange());		//������Χ
							jsonObj.put("Accuracy", cSheet.getAccuracy());	//���ȵȼ�
							jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//���쳧��
							jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//̨/����
							jsonObj.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//ǿ�Ƽ���
							jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//�Ӽ�
							jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//ת��
							if(!cSheet.getSubcontract()){	//0��ת��
								List<SubContract> subRetList = (new SubContractManager()).findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
								if(subRetList != null && subRetList.size() > 0){
									jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//ת����
								}else{
									jsonObj.put("SubContractor", "");	//ת����
								}
							}else{
								jsonObj.put("SubContractor", "");
							}
							jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//��۸���
							jsonObj.put("Repair", cSheet.getRepair()?1:0);	//����
							jsonObj.put("ReportType", cSheet.getReportType());	//������ʽ
							jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//����Ҫ��
							jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//���λ��
							jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//�ɶ���
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//ί������
							jsonObj.put("Status", cSheet.getStatus());	//ί�е�״̬
							
							jsonObj.put("Attachment", cSheet.getAttachment());
							
							/***********��CertificateFeeAssign(ԭʼ��¼֤����÷���)�в���ί�е��ķ�������***********/
							List<Object[]> FList=feeMgr.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByCommissionSheetId, cSheet.getId());		
																	
							if(FList.isEmpty()){
						    	jsonObj.put("TestFee", 0.0);
								jsonObj.put("RepairFee", 0.0);
								jsonObj.put("MaterialFee", 0.0);
								jsonObj.put("CarFee", 0.0);
								jsonObj.put("DebugFee", 0.0);
								jsonObj.put("OtherFee", 0.0);
								jsonObj.put("TotalFee", 0.0);
								jsonObj.put("OldTestFee", 0.0);
								jsonObj.put("OldRepairFee", 0.0);
								jsonObj.put("OldMaterialFee", 0.0);
								jsonObj.put("OldCarFee", 0.0);
								jsonObj.put("OldDebugFee", 0.0);
								jsonObj.put("OldOtherFee", 0.0);
								jsonObj.put("OldTotalFee", 0.0);
						    }else{
							    for(Object[] fee:FList){							    	
									jsonObj.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
									jsonObj.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
									jsonObj.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
									jsonObj.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
									jsonObj.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
									jsonObj.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
									jsonObj.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
									jsonObj.put("OldTestFee", (Double)fee[7]==null?0.0:(Double)fee[7]);
									jsonObj.put("OldRepairFee", (Double)fee[8]==null?0.0:(Double)fee[8]);
									jsonObj.put("OldMaterialFee", (Double)fee[9]==null?0.0:(Double)fee[9]);
									jsonObj.put("OldCarFee", (Double)fee[10]==null?0.0:(Double)fee[10]);
									jsonObj.put("OldDebugFee", (Double)fee[11]==null?0.0:(Double)fee[11]);
									jsonObj.put("OldOtherFee", (Double)fee[12]==null?0.0:(Double)fee[12]);
									jsonObj.put("OldTotalFee", (Double)fee[13]==null?0.0:(Double)fee[13]);
							   }
						    }
							
							options.put(jsonObj);
						}
					}
					retJSON4.put("total", total);
					retJSON4.put("rows", options);
				} catch (Exception e){
					try {
						retJSON4.put("total", 0);
						retJSON4.put("rows", new JSONArray());
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 4", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 4", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON4.toString());
				}
				break;
			case 5: // �깤ȷ��   --����
				JSONObject retObj5=new JSONObject();
				try {
					String CommissionSheetIds = req.getParameter("CommissionSheetId");	//���ί�е�֮���á�;���ָ�
					if(CommissionSheetIds == null || CommissionSheetIds.trim().length() == 0){
						throw new Exception("������������");
					}
					
					String[] cSheetIdArray = CommissionSheetIds.split(";");
					if(cSheetIdArray == null || cSheetIdArray.length == 0){
						throw new Exception("������������");
					}
					
					Timestamp today = new Timestamp(System.currentTimeMillis());// ��ǰʱ��
					SysUser Handler = (SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);// ��ǰ�����û�
					
					
					String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//ǩ��ͨ����ԭʼ��¼����������
					String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//����׼��������������
					String hqlQueryString_SubContract = "select count(*) from SubContract as model where model.commissionSheet.id=? and model.status<>1 and model.receiveDate is not null";
					
					
					int doneSuccessed = 0;	//�깤ȷ�ϳɹ���ί�е�����
					for(String CommissionSheetId : cSheetIdArray){
						int CommissionId = Integer.parseInt(CommissionSheetId.trim());	//ί�е�ID
						
						String FinishLocation = req.getParameter("FinishLocation");
						CommissionSheet cSheetRet = cSheetMgr.findById(CommissionId);
						if(cSheetRet.getStatus() == 3 || 	//�깤ȷ�ϣ����깤��
								cSheetRet.getStatus() == 4 || //�ѽ���
								cSheetRet.getStatus() == 9 || //�ѽ���
								cSheetRet.getStatus() == 10){	//��ע��
							if(cSheetIdArray.length == 1){
								throw new Exception("��ί�е��������ٴ��깤ȷ�ϣ�");
							}else{
								continue;
							}
						}else{
							//��ѯ�깤�������������������������Լ��Ƿ�ת��
							List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheetRet.getId(), true);	//�깤��������
							int finishQuantity = 0;
							if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
								finishQuantity = fQuantityList.get(0).intValue();
							}
							List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheetRet.getId(), true);	//������������
							int wQuatity = cSheetRet.getQuantity();
							if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
								wQuatity = cSheetRet.getQuantity() - ((Long)wQuantityList.get(0)).intValue();  //��Ч��������
							}
							int iSubContract = cSheetMgr.getTotalCountByHQL(hqlQueryString_SubContract, cSheetRet.getId());
							if(iSubContract > 0 || cSheetRet.getCommissionType() == 5 ){	//��ί�е���ת�����ҵ��Ϊ����ҵ�����깤ȷ��ʱ����Ҫ�жϡ��깤���������Ƿ���ڵ�����Ч����������;
								//�޲���
							}else{
								if(finishQuantity < wQuatity){
									if(cSheetIdArray.length == 1){
										throw new Exception(String.format("��ί�е������깤���깤��������'%d'����ί�е�����Ч��������'%d'��", finishQuantity, wQuatity));
									}else{
										continue;
									}
								}else if(finishQuantity > wQuatity){
									if(cSheetIdArray.length == 1){
										throw new Exception(String.format("��ί�е������깤���깤��������'%d'����ί�е�����Ч��������'%d'��", finishQuantity, wQuatity));
									}else{
										continue;
									}
								}
							}
							try{
								cSheetMgr.commissionSheetFinishConfirm(cSheetRet, Handler, FinishLocation, today, wQuatity);
								doneSuccessed++;
							}catch(Exception e){
								if(cSheetIdArray.length == 1){
									throw e;
								}else{
									continue;
								}
							}
						}	
					}
					if(doneSuccessed == 0){
						throw new Exception("�ޡ��볢�ԶԵ���ί�е������깤ȷ�ϣ�");
					}else{
						retObj5.put("IsOK", true);
						retObj5.put("msg", String.format("�깤ȷ�ϳɹ�!�����깤ȷ�ϵ�ί�е�����%d", doneSuccessed));
					}
				}catch (Exception e){
					
					try {
						retObj5.put("IsOK", false);
						retObj5.put("msg", String.format("�깤ȷ��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 5", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 5", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj5.toString());
				}
				break;
			case 6:	//����ί�е�����Ϣ(ί�е��Ų�ѯ,���ڲ���ί�е�)
				JSONObject retJSON6 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//ί�е���
					String Pwd = req.getParameter("Pwd");	//ί�е�����
					
					if(Code == null || Code.trim().length() == 0 || Pwd == null || Pwd.trim().length() == 0){
						throw new Exception("ί�е��Ż�����Ϊ�գ�");
					}
					AddressManager addrMgr = new AddressManager();
					
					List<CommissionSheet> cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="),new KeyValueWithOperator("pwd", Pwd, "="));
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						CommissionSheet cSheet = cSheetRetList.get(0);
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						retJSON6.put("IsOK", true);
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CommissionId", cSheet.getId());	//ί�е�ID
						jsonObj.put("CommissionCode", cSheet.getCode());
						jsonObj.put("CommissionPwd", cSheet.getPwd());
						jsonObj.put("CommissionStatus", cSheet.getStatus());	//ί�е�״̬
						jsonObj.put("CommissionType", cSheet.getCommissionType());
						jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("CustomerTel", cSheet.getCustomerTel());
						jsonObj.put("CustomerAddress", cSheet.getCustomerAddress());
						jsonObj.put("CustomerZipCode", cSheet.getCustomerZipCode());
						jsonObj.put("ContactPerson", cSheet.getCustomerContactor());
						jsonObj.put("ContactorTel", cSheet.getCustomerContactorTel());
						jsonObj.put("SampleFrom", cSheet.getSampleFrom());
						jsonObj.put("BillingTo", cSheet.getBillingTo());
						jsonObj.put("ApplianceName", cSheet.getApplianceName());	//��������
						jsonObj.put("Model", cSheet.getApplianceModel());
						jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//�������
						jsonObj.put("ApplianceManageCode", cSheet.getAppManageCode());	//������
						jsonObj.put("Manufacturer", cSheet.getManufacturer());
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("Mandatory", cSheet.getMandatory()?1:0);	//ǿ�Ƽ춨
						jsonObj.put("Ness", cSheet.getUrgent()?1:0);			//�Ƿ�Ӽ�
						jsonObj.put("Appearance", cSheet.getAppearance());	//��۸���
						jsonObj.put("OtherRequirements", cSheet.getOtherRequirements());	//����Ҫ��
						jsonObj.put("Status", cSheet.getStatus());	//ί�е�״̬
						
						jsonObj.put("TotalFee", cSheet.getTotalFee());	//ί�е��ܼƷ���
						
						retJSON6.put("CommissionObj", jsonObj);
						JSONObject printRecord = new JSONObject();
						printRecord.put("Code", cSheet.getCode());	//ί�е���
						printRecord.put("Pwd", cSheet.getPwd());	//ί�е�����
						printRecord.put("CommissionDate", sf.format(cSheet.getCommissionDate()));	//ί������
						printRecord.put("CustomerName", cSheet.getCustomerName());	//ί�е�λ����
						printRecord.put("CustomerTel", cSheet.getCustomerTel());	//ί�е�λ�绰
						printRecord.put("CustomerAddress", cSheet.getCustomerAddress());	//ί�е�λ��ַ
						printRecord.put("CustomerZipCode", cSheet.getCustomerZipCode());	//ί�е�λ��������
						printRecord.put("SampleFrom", cSheet.getSampleFrom());	//֤�鵥λ����
						printRecord.put("BillingTo", cSheet.getBillingTo());	//��Ʊ��λ����
						
						printRecord.put("ApplianceName", cSheet.getApplianceName());	//��������
						printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", cSheet.getApplianceModel(),cSheet.getRange(),cSheet.getAccuracy(),cSheet.getManufacturer()));//������Ϣ
						printRecord.put("ApplianceNumber",String.format("%s[%s]", cSheet.getAppFactoryCode(),cSheet.getAppManageCode()));//���߱��
						printRecord.put("Quantity",cSheet.getQuantity().toString());//̨����
						printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(cSheet.getMandatory()));//ǿ�Ƽ���
						printRecord.put("Appearance", cSheet.getAppearance());//��۸���
						printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(cSheet.getRepair()));//�������
						printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));//������ʽ
						printRecord.put("OtherRequirements", cSheet.getOtherRequirements());//����Ҫ��
						printRecord.put("Location", cSheet.getLocation());	//���λ��
						printRecord.put("Allotee", cSheet.getAllotee());	//�ɶ���
						printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//����������
						
						Address RecipientAddressObj = cSheet.getSampleAddress()==null?null:addrMgr.findById(cSheet.getSampleAddress()); //��Ʒ���յص�
						Address PickupAddressObj = cSheet.getReportAddress()==null?null:addrMgr.findById(cSheet.getReportAddress());	//ȡ����ȡ����ص�
						printRecord.put("HeadName",cSheet.getHeadName());	//̨ͷ����
						printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//���յص�
						printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
						printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//ȡ����ַ
						printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
						
						retJSON6.put("PrintObj", printRecord);
					}else{
						throw new Exception("ί�е��Ż��������");
						
					}
				} catch (Exception e){
					
					try {
						retJSON6.put("IsOK", false);
						retJSON6.put("msg", String.format("��ѯί�е�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 6", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 6", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retJSON6.toString());
				}
				break;
			case 7: // �ͻ�ȡ��   --����
				JSONObject retObj7=new JSONObject();
				try {
					int CommissionId = Integer.parseInt(req.getParameter("CommissionSheetId"));	//ί�е�ID
					
					CommissionSheet cSheetRet = cSheetMgr.findById(CommissionId);
					if(cSheetRet.getCheckOutDate() == null){
						throw new Exception("��ί�е���δ���ˣ�����ȡ����");
					}
					if(cSheetRet.getStatus() == 9){
						throw new Exception("��ί�е���ȡ���������ظ�������");
					}else{
						cSheetRet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiJieShu);     //9�ͻ�ȡ��������״̬				
//						SysUser Handler = (SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);// ��ǰ�����û�
//						cSheetRet.setFinishStaffId(Handler.getId()); //����ί�е�
	
//						cSheetRet.setFinishDate(today);
						boolean yes = cSheetMgr.update(cSheetRet);
						if(!yes){     //����ί�е�
							throw new Exception("ί�е�����ʧ�ܣ�");
						} 
						retObj7.put("IsOK", true);
						retObj7.put("msg", "�ͻ�ȡ��ȷ�ϳɹ���");
					}	
				}catch (Exception e){
					
					try {
						retObj7.put("IsOK", false);
						retObj7.put("msg", String.format("�ͻ�ȡ��ȷ��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 7", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 7", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj7.toString());
				}
				break;
			case 8:	//����������ί�е�����Ϣ������ӡ��Ϣ�������ڲ���ί�е�
				JSONObject retJSON8 = new JSONObject();
				int totalSize8 = 0;
				try {
					JSONArray jsonArray = new JSONArray();
					int page = 0;	//��ǰҳ��
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;	//ҳ���С
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
						String CustomerName  = req.getParameter("CustomerName");
					
						String ApplianceName = req.getParameter("ApplianceName");
						String BeginDate = req.getParameter("BeginDate");
						String EndDate = req.getParameter("EndDate");
						String Code = req.getParameter("Code");	//ί�е���
						
						
						List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
						condList.add(new KeyValueWithOperator("status", 10, "<>"));//ѡ��ί�е���δע����
						if(CustomerName != null && CustomerName.length() > 0){
							String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //���jquery����������������
							condList.add(new KeyValueWithOperator("customerName",cusName,"="));
						}
						if(Code != null && Code.length() > 0){
							condList.add(new KeyValueWithOperator("code", Code, "="));
						}
						if(BeginDate != null && BeginDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(BeginDate).getTime()), ">="));
						}
						if(EndDate != null && EndDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(EndDate).getTime()), "<"));
						}
						if(ApplianceName != null && ApplianceName.trim().length() > 0 ){
							String appName = URLDecoder.decode(ApplianceName.trim(), "UTF-8");
							condList.add(new KeyValueWithOperator("applianceName", "%"+appName+"%", "like"));
						}
						totalSize8 = cSheetMgr.getTotalCount(condList);
						AddressManager addrMgr = new AddressManager();
						List<CommissionSheet> retList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, condList);
						if(retList != null && retList.size() > 0){
							ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();
							ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
							SubContractManager subConMgr = new SubContractManager();	//ת����¼Mgr
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							for(CommissionSheet cSheet : retList){
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("Id", cSheet.getId());
								jsonObj.put("Code", cSheet.getCode());
								jsonObj.put("Pwd", cSheet.getPwd());
								jsonObj.put("CustomerName", cSheet.getCustomerName());
								jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//���߷���Id(�����߱�׼����ID)
								if(cSheet.getSpeciesType()){	//������Ȩ�����ࣩ����
									jsonObj.put("SpeciesType", 1);	//���߷�������
									ApplianceSpecies spe = speciesMgr.findById(cSheet.getApplianceSpeciesId());
									if(spe != null){
										jsonObj.put("ApplianceSpeciesName", spe.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
									}else{
										continue;
									}
								}else{	//���߱�׼����
									jsonObj.put("SpeciesType", 0);
									ApplianceStandardName stName = standardNameMgr.findById(cSheet.getApplianceSpeciesId());
									if(stName != null){
										jsonObj.put("ApplianceSpeciesName", stName.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
									}else{
										continue;
									}
								}
								jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//�������ƣ��������ƣ�
								jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//�������
								jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//������
								jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//�ͺŹ��
								jsonObj.put("Range", cSheet.getRange());		//������Χ
								jsonObj.put("Accuracy", cSheet.getAccuracy());	//���ȵȼ�
								jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//���쳧��
								jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//̨/����
								jsonObj.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//ǿ�Ƽ���
								jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//�Ӽ�
								jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//ת��
								if(!cSheet.getSubcontract()){	//0��ת��
									List<SubContract> subRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
									if(subRetList != null && subRetList.size() > 0){
										jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//ת����
									}else{
										jsonObj.put("SubContractor", "");	//ת����
									}
								}else{
									jsonObj.put("SubContractor", "");
								}
								jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//��۸���
								jsonObj.put("Repair", cSheet.getRepair()?1:0);	//����
								jsonObj.put("ReportType", cSheet.getReportType());	//������ʽ
								jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//����Ҫ��
								jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//���λ��
								jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//�ɶ���
								jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//ί������
								jsonObj.put("Status", cSheet.getStatus());	//ί�е�״̬
								
								JSONObject printRecord = new JSONObject();
								printRecord.put("Code", cSheet.getCode());	//ί�е���
								printRecord.put("Pwd", cSheet.getPwd());	//ί�е�����
								printRecord.put("CommissionDate", sf.format(cSheet.getCommissionDate()));	//ί������
								printRecord.put("CustomerName", cSheet.getCustomerName());	//ί�е�λ����
								printRecord.put("CustomerTel", cSheet.getCustomerTel());	//ί�е�λ�绰
								printRecord.put("CustomerAddress", cSheet.getCustomerAddress());	//ί�е�λ��ַ
								printRecord.put("CustomerZipCode", cSheet.getCustomerZipCode());	//ί�е�λ��������
								printRecord.put("SampleFrom", cSheet.getSampleFrom());	//֤�鵥λ����
								printRecord.put("BillingTo", cSheet.getBillingTo());	//��Ʊ��λ����
								
								printRecord.put("ApplianceName", cSheet.getApplianceName());	//��������
								printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", cSheet.getApplianceModel(),cSheet.getRange(),cSheet.getAccuracy(),cSheet.getManufacturer()));//������Ϣ
								printRecord.put("ApplianceNumber",String.format("%s[%s]", cSheet.getAppFactoryCode(),cSheet.getAppManageCode()));//���߱��
								printRecord.put("Quantity",cSheet.getQuantity().toString());//̨����
								printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(cSheet.getMandatory()));//ǿ�Ƽ���
								printRecord.put("Appearance", cSheet.getAppearance());//��۸���
								printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(cSheet.getRepair()));//�������
								printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));//������ʽ
								printRecord.put("OtherRequirements", cSheet.getOtherRequirements());//����Ҫ��
								printRecord.put("Location", cSheet.getLocation());	//���λ��
								printRecord.put("Allotee", cSheet.getAllotee());	//�ɶ���
								printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//����������
								
								Address RecipientAddressObj = cSheet.getSampleAddress()==null?null:addrMgr.findById(cSheet.getSampleAddress()); //��Ʒ���յص�
								Address PickupAddressObj = cSheet.getReportAddress()==null?null:addrMgr.findById(cSheet.getReportAddress());	//ȡ����ȡ����ص�
								printRecord.put("HeadName",cSheet.getHeadName());	//̨ͷ����
								printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//���յص�
								printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
								printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//ȡ����ַ
								printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
								
								jsonObj.put("PrintObj", printRecord);
								
								jsonArray.put(jsonObj);	
							}
						}
					
					retJSON8.put("total", totalSize8);
					retJSON8.put("rows", jsonArray);
				} catch (Exception e) {
					
					try {
						retJSON8.put("total", 0);
						retJSON8.put("rows", new JSONArray());
					} catch (JSONException e1) {
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 8", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 8", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON8.toString());
				}
				
				break;
			case 9:	//����������ί�е�����Ϣ��������ӡ��Ϣ��:�����깤ȷ�ϵ�
				JSONObject retJSON9 = new JSONObject();
				int totalSize9 = 0;
				try {
					JSONArray jsonArray = new JSONArray();
					int page = 0;	//��ǰҳ��
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;	//ҳ���С
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
						String CustomerName  = req.getParameter("CustomerName");
					
						String ApplianceName = req.getParameter("ApplianceName");
						String BeginDate = req.getParameter("BeginDate");
						String EndDate = req.getParameter("EndDate");
						String Code = req.getParameter("Code");	//ί�е���
						
						String CommissionStatus = req.getParameter("CommissionStatus");	//ί�е�״̬�����ַ���Ϊȫ����1Ϊδ�깤��2Ϊ���깤ȷ�ϵ�
						
						List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
						condList.add(new KeyValueWithOperator("status", 10, "<>"));//ѡ��ί�е���δע����
						if(CustomerName != null && CustomerName.length() > 0){
							String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //���jquery����������������
							condList.add(new KeyValueWithOperator("customerName",cusName,"="));
						}
						if(Code != null && Code.length() > 0){
							condList.add(new KeyValueWithOperator("code", "%"+Code+"%", "like"));
						}
						if(BeginDate != null && BeginDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(BeginDate).getTime()), ">="));
						}
						if(EndDate != null && EndDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(EndDate).getTime()), "<="));
						}
						if(ApplianceName != null && ApplianceName.trim().length() > 0 ){
							String appName = URLDecoder.decode(ApplianceName.trim(), "UTF-8");
							condList.add(new KeyValueWithOperator("applianceName", "%"+appName+"%", "like"));
						}
						if(CommissionStatus != null && CommissionStatus.length() > 0){
							if(CommissionStatus.equals("1")){
								condList.add(new KeyValueWithOperator("status", 3, "<"));//δ�깤
							}else if(CommissionStatus.equals("2")){
								condList.add(new KeyValueWithOperator("status", 3, ">="));//���깤ȷ��
							}
						}
						totalSize9 = cSheetMgr.getTotalCount(condList);
						List<CommissionSheet> retList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, condList);
						if(retList != null && retList.size() > 0){
							ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();
							ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
							SubContractManager subConMgr = new SubContractManager();	//ת����¼Mgr
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							
							String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//ǩ��ͨ����ԭʼ��¼����������(ǩ����ͨ���Ҳ������ں�ִ̨��)
							String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//����׼��������������
							String hqlQueryString_SubContract = "select count(*) from SubContract as model where model.commissionSheet.id=? and model.status<>1 and model.receiveDate is not null";
							for(CommissionSheet cSheet : retList){
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("Id", cSheet.getId());
								jsonObj.put("Code", cSheet.getCode());
								jsonObj.put("Pwd", cSheet.getPwd());
								jsonObj.put("CustomerName", cSheet.getCustomerName());
								jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//���߷���Id(�����߱�׼����ID)
								if(cSheet.getSpeciesType()){	//������Ȩ�����ࣩ����
									jsonObj.put("SpeciesType", 1);	//���߷�������
									ApplianceSpecies spe = speciesMgr.findById(cSheet.getApplianceSpeciesId());
									if(spe != null){
										jsonObj.put("ApplianceSpeciesName", spe.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
									}else{
										continue;
									}
								}else{	//���߱�׼����
									jsonObj.put("SpeciesType", 0);
									ApplianceStandardName stName = standardNameMgr.findById(cSheet.getApplianceSpeciesId());
									if(stName != null){
										jsonObj.put("ApplianceSpeciesName", stName.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
									}else{
										continue;
									}
								}
								jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//�������ƣ��������ƣ�
								jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//�������
								jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//������
								jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//�ͺŹ��
								jsonObj.put("Range", cSheet.getRange());		//������Χ
								jsonObj.put("Accuracy", cSheet.getAccuracy());	//���ȵȼ�
								jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//���쳧��
								jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//̨/����
								jsonObj.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//ǿ�Ƽ���
								jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//�Ӽ�
								jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//ת��
								if(!cSheet.getSubcontract()){	//0��ת��
									List<SubContract> subRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
									if(subRetList != null && subRetList.size() > 0){
										jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//ת����
									}else{
										jsonObj.put("SubContractor", "");	//ת����
									}
								}else{
									jsonObj.put("SubContractor", "");
								}
								jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//��۸���
								jsonObj.put("Repair", cSheet.getRepair()?1:0);	//����
								jsonObj.put("ReportType", cSheet.getReportType());	//������ʽ
								jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//����Ҫ��
								jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//���λ��
								jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//�ɶ���
								jsonObj.put("CommissionDate", sf.format((cSheet.getCommissionType()==2 && cSheet.getLocaleCommissionDate()!=null)?cSheet.getLocaleCommissionDate():cSheet.getCommissionDate()));		//ί������								
								jsonObj.put("Remark", cSheet.getRemark()==null?"":cSheet.getRemark());	//��ע
								//������Ϣ
								jsonObj.put("TestFee", cSheet.getTestFee()==null?0:cSheet.getTestFee());
								jsonObj.put("RepairFee", cSheet.getRepairFee()==null?0:cSheet.getRepairFee());
								jsonObj.put("MaterialFee", cSheet.getMaterialFee()==null?0:cSheet.getMaterialFee());
								jsonObj.put("CarFee", cSheet.getCarFee()==null?0:cSheet.getCarFee());
								jsonObj.put("DebugFee", cSheet.getDebugFee()==null?0:cSheet.getDebugFee());
								jsonObj.put("OtherFee", cSheet.getOtherFee()==null?0:cSheet.getOtherFee());
								jsonObj.put("TotalFee", cSheet.getTotalFee()==null?0:cSheet.getTotalFee());
								
								jsonObj.put("Status", cSheet.getStatus());
								jsonObj.put("CommissionType", cSheet.getCommissionType());	//ί����ʽ
								jsonObj.put("CommissionTypeName", CommissionSheetFlagUtil.getCommissionTypeByFlag(cSheet.getCommissionType()));
								
								//��ѯ�깤�������������������������Լ��Ƿ�ת��
								List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId(), true);	//�깤��������
								if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
									jsonObj.put("FinishQuantity", fQuantityList.get(0));	//�깤��������
								}else{
									jsonObj.put("FinishQuantity", 0);
								}
								List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//������������
								if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
									jsonObj.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//��Ч��������
								}else{
									jsonObj.put("EffectQuantity", cSheet.getQuantity());
								}
								int iSubContract = cSheetMgr.getTotalCountByHQL(hqlQueryString_SubContract, cSheet.getId());
								if(iSubContract > 0 || cSheet.getCommissionType() == 5){	//��ί�е���ת��(���ί�е�Ϊ����ҵ��)�����깤ȷ��ʱ����Ҫ�жϡ��깤���������Ƿ���ڵ�����Ч����������;
									jsonObj.put("IsSubContract", true);
								}else{
									jsonObj.put("IsSubContract", false);
								}
								//��ѯֱ������֤���֤������
								int ZGCerCount=0;
								List<Long> ZGCerCountList = new OriginalRecordManager().findByHQL("select count(*) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 " +
										" and model.originalRecordExcel.doc='' and model.certificate.pdf is not null ", cSheet.getId());
								if(ZGCerCountList.get(0) != null){
									ZGCerCount = ZGCerCountList.get(0).intValue();
								}
								jsonObj.put("ZGCerCount", ZGCerCount);
								
								jsonArray.put(jsonObj);	
							}
						}
					
					retJSON9.put("total", totalSize9);
					retJSON9.put("rows", jsonArray);
				} catch (Exception e) {
					
					try {
						retJSON9.put("total", 0);
						retJSON9.put("rows", new JSONArray());
					} catch (JSONException e1) {
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 9", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 9", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON9.toString());
				}
				break;
			case 10://������ί�е��Ų�ѯί�е���Ϣ������ҵ���ѯ��
				JSONObject retJSON10 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//ί�е���
					
					if(Code == null || Code.trim().length() == 0){
						throw new Exception("ί�е���Ϊ�գ�");
					}
					List<CommissionSheet> cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="));
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						CommissionSheet cSheet = cSheetRetList.get(0);
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						retJSON10.put("IsOK", true);
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CommissionId", cSheet.getId());	//ί�е�ID
						jsonObj.put("CommissionCode", cSheet.getCode());
						jsonObj.put("CommissionPwd", cSheet.getPwd());
						jsonObj.put("CommissionStatus", cSheet.getStatus());	//ί�е�״̬
						jsonObj.put("CommissionType", cSheet.getCommissionType());
						jsonObj.put("ReportType", cSheet.getReportType());
						jsonObj.put("CommissionDate", sf.format((cSheet.getCommissionType()==2 && cSheet.getLocaleCommissionDate()!=null)?cSheet.getLocaleCommissionDate():cSheet.getCommissionDate()));
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("CustomerTel", cSheet.getCustomerTel());
						jsonObj.put("CustomerAddress", cSheet.getCustomerAddress());
						jsonObj.put("CustomerZipCode", cSheet.getCustomerZipCode());
						jsonObj.put("ContactPerson", cSheet.getCustomerContactor());
						jsonObj.put("ContactorTel", cSheet.getCustomerContactorTel());
						jsonObj.put("SampleFrom", cSheet.getSampleFrom());
						jsonObj.put("BillingTo", cSheet.getBillingTo());
						jsonObj.put("ApplianceName", cSheet.getApplianceName());	//��������
						jsonObj.put("Model", cSheet.getApplianceModel());
						jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//�������
						jsonObj.put("ApplianceManageCode", cSheet.getAppManageCode());	//������
						jsonObj.put("Manufacturer", cSheet.getManufacturer());
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("Mandatory", cSheet.getMandatory()?1:0);	//ǿ�Ƽ춨
						jsonObj.put("Ness", cSheet.getUrgent()?1:0);			//�Ƿ�Ӽ�
						jsonObj.put("Appearance", cSheet.getAppearance());	//��۸���
						jsonObj.put("OtherRequirements", cSheet.getOtherRequirements());	//����Ҫ��
						jsonObj.put("Status", cSheet.getStatus());	//ί�е�״̬
						jsonObj.put("Receiver", cSheet.getReceiverName());
						jsonObj.put("FinishStaff", cSheet.getFinishStaffId()==null?"":(new UserManager()).findById(cSheet.getFinishStaffId()).getName());
						jsonObj.put("FinishDate", cSheet.getFinishDate()==null?"":cSheet.getFinishDate());
						jsonObj.put("CustomerClassification", (new CustomerManager()).findById(cSheet.getCustomerId()).getClassification());
						jsonObj.put("LocaleCommissionCode", cSheet.getLocaleCommissionCode());
						jsonObj.put("LocaleCommissionDate", cSheet.getLocaleCommissionDate());
						jsonObj.put("CheckOutStaffId", cSheet.getCheckOutStaffId());
						jsonObj.put("CheckOutStaff", cSheet.getCheckOutStaffId()==null?"":(new UserManager()).findById(cSheet.getCheckOutStaffId()).getName());
						jsonObj.put("CheckOutTime", cSheet.getCheckOutDate()==null?"":cSheet.getCheckOutDate());
//						String detailqueryStr = "select model.sysUser.name,model.lastEditTime from DetailList as model,DetailListCom as model1 where model.status = ? and model.id = model1.detailList.id and model1.commissionSheet.id = ?";
//						
//						List<Object[]> detail = (new DetailListManager()).findByHQL(detailqueryStr, new Integer(2), cSheet.getId());
//						if(detail!=null&detail.size()>0){
//							Object[] temp = detail.get(0);
//							jsonObj.put("CheckOutStaff", temp[0].toString());
//							jsonObj.put("CheckOutTime", temp[1].toString());
//						}
//						else{
//							jsonObj.put("CheckOutStaff", "");
//							jsonObj.put("CheckOutTime", "");
//						}
						jsonObj.put("Remark", cSheet.getRemark());
						jsonObj.put("TotalFee", cSheet.getTotalFee());	//ί�е��ܼƷ���
						JSONObject printRecord = new JSONObject();
						printRecord.put("Code", cSheet.getCode());	//ί�е���
						printRecord.put("Pwd", cSheet.getPwd());	//ί�е�����
						printRecord.put("CommissionDate", sf.format(cSheet.getCommissionDate()));	//ί������
						printRecord.put("CustomerName", cSheet.getCustomerName());	//ί�е�λ����
						printRecord.put("CustomerTel", cSheet.getCustomerTel());	//ί�е�λ�绰
						printRecord.put("CustomerAddress", cSheet.getCustomerAddress());	//ί�е�λ��ַ
						printRecord.put("CustomerZipCode", cSheet.getCustomerZipCode());	//ί�е�λ��������
						printRecord.put("SampleFrom", cSheet.getSampleFrom());	//֤�鵥λ����
						printRecord.put("BillingTo", cSheet.getBillingTo());	//��Ʊ��λ����
						
						printRecord.put("ApplianceName", cSheet.getApplianceName());	//��������
						printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", cSheet.getApplianceModel(),cSheet.getRange(),cSheet.getAccuracy(),cSheet.getManufacturer()));//������Ϣ
						printRecord.put("ApplianceNumber",String.format("%s[%s]", cSheet.getAppFactoryCode(),cSheet.getAppManageCode()));//���߱��
						printRecord.put("Quantity",cSheet.getQuantity().toString());//̨����
						printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(cSheet.getMandatory()));//ǿ�Ƽ���
						printRecord.put("Appearance", cSheet.getAppearance());//��۸���
						printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(cSheet.getRepair()));//�������
						printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));//������ʽ
						printRecord.put("OtherRequirements", cSheet.getOtherRequirements());//����Ҫ��
						printRecord.put("Location", cSheet.getLocation());	//���λ��
						printRecord.put("Allotee", cSheet.getAllotee());	//�ɶ���
						printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//����������
						
						Address RecipientAddressObj = cSheet.getSampleAddress()==null?null:(new AddressManager()).findById(cSheet.getSampleAddress()); //��Ʒ���յص�
						Address PickupAddressObj = cSheet.getReportAddress()==null?null:(new AddressManager()).findById(cSheet.getReportAddress());	//ȡ����ȡ����ص�
						printRecord.put("HeadName",cSheet.getHeadName());	//̨ͷ����
						printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//���յص�
						printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
						printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//ȡ����ַ
						printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
						
						retJSON10.put("PrintObj", printRecord);
						retJSON10.put("CommissionObj", jsonObj);
					}else{
						throw new Exception("ί�е��Ŵ���");
					}
				} catch (Exception e){
					
					try {
						retJSON10.put("IsOK", false);
						retJSON10.put("msg", String.format("��ѯί�е�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 10", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 10", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retJSON10.toString());
				}
				break;
			case 11://�޸�ί�е���ע
				JSONObject retObj11 = new JSONObject();
				try{
					String CommissionId = req.getParameter("CommissionId");
					String Remark = req.getParameter("Remark");
					CommissionSheet cSheet = cSheetMgr.findById(Integer.valueOf(CommissionId));
					cSheet.setRemark(Remark);
					boolean res = cSheetMgr.save(cSheet);
					retObj11.put("IsOK", res);
					retObj11.put("msg", res?"�޸ĳɹ���":"�޸�ʧ�ܣ��������޸ģ�");
				}catch(Exception e){
					try {
						retObj11.put("IsOK", false);
						retObj11.put("msg", String.format("�޸�ί�е���עʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 11", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 11", e);
					}
				}finally{
					resp.setContentType(ResponseContentType.Type_FormSubmit);
					resp.getWriter().write(retObj11.toString());
				}
				break;
			case 12://ע��ί�е�
				JSONObject retObj12 = new JSONObject();
				Timestamp today = new Timestamp(System.currentTimeMillis());
				try{
					String CommissionId = req.getParameter("CommissionId");
					String Reason = req.getParameter("Reason");
					CommissionSheet cSheet = cSheetMgr.findById(Integer.valueOf(CommissionId));
					if(cSheet.getCheckOutDate() != null &&
							(cSheet.getStatus() == FlagUtil.CommissionSheetStatus.Status_YiJieZhang || cSheet.getStatus() == FlagUtil.CommissionSheetStatus.Status_YiJieShu))
					{
						throw new Exception("��ί�е��ѽ��ˣ�����ע����");
					}
					if(cSheet.getStatus() == FlagUtil.CommissionSheetStatus.Status_YiZhuXiao)
					{
						throw new Exception("��ί�е���ע���������ظ�ע����");
					}
					cSheet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiZhuXiao);
					cSheet.setCancelDate(today);
					cSheet.setCancelExecuterId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());
					ReasonManager rMgr = new ReasonManager();
					List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", FlagUtil.ReasonType.Type_CancelCommissionSheet, "="));//����ע��ԭ��
					if(rList.size() > 0){	//����ԭ��
						Reason reason = rList.get(0);
						reason.setCount(reason.getCount()+1);
						reason.setLastUse(today);
						rMgr.update(reason);
						cSheet.setCancelReason(reason.getId());	//ע��ԭ��
					}else{	//�½�ԭ��
						Reason reason = new Reason();
						reason.setCount(1);
						reason.setLastUse(today);
						reason.setReason(Reason.trim());
						reason.setStatus(0);
						reason.setType(FlagUtil.ReasonType.Type_CancelCommissionSheet);	//ע���ͻ�
						rMgr.save(reason);
						cSheet.setCancelReason(reason.getId());	//ע��ԭ��
					}
					boolean res1 = cSheetMgr.cancel(cSheet);
					retObj12.put("IsOK", res1);
					retObj12.put("msg", res1?"ע���ɹ���":"ע��ʧ�ܣ�������ע����");
				}catch(Exception e){
					try {
						retObj12.put("IsOK", false);
						retObj12.put("msg", String.format("ע��ί�е�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 12", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 12", e);
					}
				}finally{
					resp.setContentType(ResponseContentType.Type_FormSubmit);
					resp.getWriter().write(retObj12.toString());
				}
				break;
			case 13://����ί�е���ִ�н��˵���
				JSONObject retObj13 = new JSONObject();
				try{
					String CommissionId = req.getParameter("CommissionId");
					if(CommissionId == null || CommissionId.length() == 0){
						throw new Exception("����δָ����");
					}
					CommissionSheet cSheet = cSheetMgr.findById(Integer.valueOf(CommissionId));
					if(cSheet == null){
						throw new Exception("�Ҳ���ָ����ί�е���");
					}
					if(cSheet.getCheckOutDate() == null){
						throw new Exception("��ί�е���δ���ˣ�����ִ�н��˵��أ�");
					}
					SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
					cSheetMgr.checkOutReject(cSheet, loginUser);
					retObj13.put("IsOK", true);
					retObj13.put("msg", "���سɹ���");
				}catch(Exception e){
					try {
						retObj13.put("IsOK", false);
						retObj13.put("msg", String.format("����ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 13", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 13", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj13.toString());
				}
				break;
			case 14://����ί�е��Ž�ί�е��ӡ�ע�������������ռ���״̬
				JSONObject retObj14 = new JSONObject();
				try{
					String CommissionId = req.getParameter("CommissionId");
					if(CommissionId == null || CommissionId.length() == 0){
						throw new Exception("����δָ����");
					}
					CommissionSheet cSheet = cSheetMgr.findById(Integer.valueOf(CommissionId));
					if(cSheet == null){
						throw new Exception("�Ҳ���ָ����ί�е���");
					}
					if(cSheet.getCancelDate() == null){
						throw new Exception("��ί�е���δע��������ִ��ע�����أ�");
					}
					if(cSheet.getCommissionType()==2&&cSheet.getLocaleApplianceItemId()!=null){
						List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
						keys.add(new KeyValueWithOperator("localeApplianceItemId", cSheet.getLocaleApplianceItemId(), "="));
						keys.add(new KeyValueWithOperator("status", new Integer(10), "<>"));
						List<CommissionSheet> check = cSheetMgr.findByVarProperty(keys);
						if(check!=null&&check.size()>0){
							throw new Exception("�Ѵ�����ͬ�����Ŀ��δע�����ֳ����ί�е�������ִ��ע�����أ�");
						}
					}
					cSheet.setFinishLocation(null);
					cSheet.setFinishStaffId(null);
					cSheet.setFinishDate(null);
					cSheet.setCancelDate(null);
					cSheet.setCancelExecuterId(null);
					cSheet.setCancelReason(null);
					cSheet.setCancelRequesterId(null);
					cSheet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiShouJian);
					cSheetMgr.update(cSheet);
					retObj14.put("IsOK", true);
					retObj14.put("msg", "���سɹ���");
				}catch(Exception e){
					try {
						retObj14.put("IsOK", false);
						retObj14.put("msg", String.format("����ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 13", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 13", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj14.toString());
				}
				break;
			case 15://����ί�е��š�ί�е�λ�Ȳ�ѯί�е���Ϣ(��ӡ)
				JSONObject retJSON15 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//ί�е���
					String CustomerId = req.getParameter("CustomerId");//ί�е�λ
					String CustomerName = req.getParameter("CustomerName");//ί�е�λ����
					String DateFrom = req.getParameter("DateFrom");
					String DateEnd = req.getParameter("DateEnd");
					String Status = req.getParameter("Status");
					int page = 1;
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
					
					List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
					if(Code != null && Code.length() > 0){
						keys.add(new KeyValueWithOperator("code", "%"+Code+"%", "like"));
					}
					if(CustomerId!=null && CustomerId.length() > 0){
						keys.add(new KeyValueWithOperator("customerId", Integer.valueOf(CustomerId), "="));
					}
					if(CustomerName!=null && CustomerName.length() > 0){
						String CustomerNameStr = URLDecoder.decode(CustomerName, "utf-8");
						keys.add(new KeyValueWithOperator("customerName", "%"+CustomerNameStr+"%", "like"));
					}
					if(DateFrom != null && DateFrom.length() > 0){
						Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", Start, ">="));
					}
					if(DateEnd != null && DateEnd.length() > 0){
						Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", End, "<="));
					}
					if(Status != null && Status.length() > 0 ){
						keys.add(new KeyValueWithOperator("status", Integer.valueOf(Status), "="));
					}
					
					List<CommissionSheet> cSheetRetList = cSheetMgr.findByPropertyBySort("commissionDate", false, keys);
					int total = cSheetMgr.getTotalCount(keys);
					JSONArray options = new JSONArray();
					CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						for(CommissionSheet cSheet : cSheetRetList)
						{
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("Id", cSheet.getId());
							jsonObj.put("DetailListCode", cSheet.getDetailListCode());
							jsonObj.put("Code", cSheet.getCode());
							jsonObj.put("Pwd", cSheet.getPwd());
							jsonObj.put("CustomerId", cSheet.getCustomerId());
							jsonObj.put("CustomerName", cSheet.getCustomerName());
							jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//���߷���Id(�����߱�׼����ID)
							if(cSheet.getSpeciesType()){	//������Ȩ�����ࣩ����
								jsonObj.put("SpeciesType", 1);	//���߷�������
								ApplianceSpecies spe = (new ApplianceSpeciesManager()).findById(cSheet.getApplianceSpeciesId());
								if(spe != null){
									jsonObj.put("ApplianceSpeciesName", spe.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
								}else{
									continue;
								}
							}else{	//���߱�׼����
								jsonObj.put("SpeciesType", 0);
								ApplianceStandardName stName = (new ApplianceStandardNameManager()).findById(cSheet.getApplianceSpeciesId());
								if(stName != null){
									jsonObj.put("ApplianceSpeciesName", stName.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
								}else{
									continue;
								}
							}
							jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//�������ƣ��������ƣ�
							jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//�������
							jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//������
							jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//�ͺŹ��
							jsonObj.put("Range", cSheet.getRange());		//������Χ
							jsonObj.put("Accuracy", cSheet.getAccuracy());	//���ȵȼ�
							jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//���쳧��
							jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//̨/����
							jsonObj.put("MandatoryInspection", cSheet.getMandatory()?"��ǿ�Ƽ춨":"ǿ�Ƽ춨");	//ǿ�Ƽ���
							jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//�Ӽ�
							jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//ת��
							if(!cSheet.getSubcontract()){	//0��ת��
								List<SubContract> subRetList = (new SubContractManager()).findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
								if(subRetList != null && subRetList.size() > 0){
									jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//ת����
								}else{
									jsonObj.put("SubContractor", "");	//ת����
								}
							}else{
								jsonObj.put("SubContractor", "");
							}
							jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//��۸���
							jsonObj.put("Repair", cSheet.getRepair()?1:0);	//����
							
							jsonObj.put("ReportType", CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));	//������ʽ
							
							jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//����Ҫ��
							jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//���λ��
							jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//�ɶ���
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//ί������
							String status="";
							if(cSheet.getStatus()==0){
								status="���ռ�";
							}else if(cSheet.getStatus()==1){
								status="�ѷ���";
							}else if(cSheet.getStatus()==2){
								status="ת����";
							}else if(cSheet.getStatus()==3){
								status="���깤";
							}else if(cSheet.getStatus()==4){
								status="�ѽ���";
							}else if(cSheet.getStatus()==9){
								status="�ѽ���";
							}else if(cSheet.getStatus()==10){
								status="��ע��";
							}
							jsonObj.put("Status",status);	//ί�е�״̬
							
							jsonObj.put("Attachment", cSheet.getAttachment());
							
							/***********��CertificateFeeAssign(ԭʼ��¼֤����÷���)�в���ί�е��ķ�������***********/
							List<Object[]> FList=feeMgr.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByCommissionSheetId, cSheet.getId());		
																	
							if(FList.isEmpty()){
						    	jsonObj.put("TestFee", 0.0);
								jsonObj.put("RepairFee", 0.0);
								jsonObj.put("MaterialFee", 0.0);
								jsonObj.put("CarFee", 0.0);
								jsonObj.put("DebugFee", 0.0);
								jsonObj.put("OtherFee", 0.0);
								jsonObj.put("TotalFee", 0.0);
								jsonObj.put("OldTestFee", 0.0);
								jsonObj.put("OldRepairFee", 0.0);
								jsonObj.put("OldMaterialFee", 0.0);
								jsonObj.put("OldCarFee", 0.0);
								jsonObj.put("OldDebugFee", 0.0);
								jsonObj.put("OldOtherFee", 0.0);
								jsonObj.put("OldTotalFee", 0.0);
						    }else{
							    for(Object[] fee:FList){							    	
									jsonObj.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
									jsonObj.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
									jsonObj.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
									jsonObj.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
									jsonObj.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
									jsonObj.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
									jsonObj.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
									jsonObj.put("OldTestFee", (Double)fee[7]==null?0.0:(Double)fee[7]);
									jsonObj.put("OldRepairFee", (Double)fee[8]==null?0.0:(Double)fee[8]);
									jsonObj.put("OldMaterialFee", (Double)fee[9]==null?0.0:(Double)fee[9]);
									jsonObj.put("OldCarFee", (Double)fee[10]==null?0.0:(Double)fee[10]);
									jsonObj.put("OldDebugFee", (Double)fee[11]==null?0.0:(Double)fee[11]);
									jsonObj.put("OldOtherFee", (Double)fee[12]==null?0.0:(Double)fee[12]);
									jsonObj.put("OldTotalFee", (Double)fee[13]==null?0.0:(Double)fee[13]);
							   }
						    }
							
							options.put(jsonObj);
						}
					}
					retJSON15.put("total", total);
					retJSON15.put("rows", options);
					
					
					retJSON15.put("IsOK", true);
					req.getSession().setAttribute("MissionLookList", retJSON15);
					
					resp.sendRedirect("/jlyw/StatisticLook/MissionLookPrint.jsp");
				}catch(Exception e){
					
					try{
						retJSON15.put("total", 0);
						retJSON15.put("rows", new JSONArray());
						retJSON15.put("IsOK", false);
						req.getSession().setAttribute("MissionLookList", retJSON15);
						resp.sendRedirect("/jlyw/StatisticLook/MissionLookPrint.jsp");
					}catch(Exception e1){}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 15", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 15", e);
					}
				}finally{
					
				}
				break;
			case 16: // Ԥ��ί�е�
				try {
					String QuotationId = req.getParameter("QuotationId");	//���۵���
					String CommissionDate = req.getParameter("CommissionDate");		//ί������
					String PromiseDate = req.getParameter("PromiseDate").trim();			//��ŵ����
					String CommissionType = req.getParameter("CommissionType");		//ί����ʽ
					String CustomerName  = req.getParameter("CustomerName").trim();		//ί�е�λ
					String CustomerTel = req.getParameter("CustomerTel").trim();
					String CustomerAddress = req.getParameter("CustomerAddress").trim();
					String CustomerZipCode = req.getParameter("CustomerZipCode").trim();
					String ContactPerson = req.getParameter("ContactPerson").trim();
					String ContactorTel = req.getParameter("ContactorTel").trim();
					String SampleFrom = req.getParameter("SampleFrom");	//֤�鵥λ
					String BillingTo = req.getParameter("BillingTo");	//��Ʊ��λ
					
					String CustomerHandler = req.getParameter("CustomerHandler");	//ί����
//					String ReceiverName = req.getParameter("ReceiverName").trim();	//������
					
					String HeadNameId = req.getParameter("HeadName").trim();	//̨ͷ����ID
					String RecipientAddress = req.getParameter("RecipientAddress");	//������ַ
					String PickupAddress = req.getParameter("PickupAddress");		//ȡ����ַ					
					String Appliances = req.getParameter("Appliances").trim();	//���������

					String LocaleCommissionCode = null;	//�ֳ�ί�е���
					Timestamp LocaleCommissionDate = null;	//�ֳ����ʱ��
					Integer LocaleStaffId = null;	//�ֳ���⸺����ID
					
					if(QuotationId != null){
						QuotationId = QuotationId.trim();
					}else{
						QuotationId = "";
					}
					
					if((CommissionDate == null || CommissionDate.length() == 0) && !CommissionType.equals("2")){
						throw new Exception("ί������Ϊ�գ�");
					}
					if(SampleFrom.length() == 0){
						SampleFrom = CustomerName;
					}
					if(BillingTo.length() == 0){
						BillingTo = CustomerName;
					}
					if(RecipientAddress == null || RecipientAddress.length() == 0){
						RecipientAddress = null;
					}
					if(PickupAddress == null || PickupAddress.length() == 0){
						PickupAddress = null;
					}
					
					Timestamp now = new Timestamp(System.currentTimeMillis());
					CustomerManager cusMgr = new CustomerManager();		//�ͻ�����Mgr
					Integer CustomerId;
					List<Customer> cusList = cusMgr.findByVarProperty(new KeyValueWithOperator("name",CustomerName,"="), 
							new KeyValueWithOperator("status", 1, "<>"));	//����ί�е�λ��ID
					if(cusList != null && cusList.size() == 1){
						CustomerId = cusList.get(0).getId();
					}else if(cusList != null && cusList.size() > 1){
						throw new Exception("���ݿ����ҵ����������ͬ��ί�е�λ:"+CustomerName+", �뵽��ί�е�λ��Ϣ���������޸ģ�");
					}else{
						throw new Exception("ί�е�λ�����ڣ��¿ͻ������½�ί�е�λ��");
					}
					
					if(QuotationId.length() > 0){
						if(new QuotationManager().findById(QuotationId) == null){
							throw new Exception(String.format("���۵���:%s ��Ч��", QuotationId));
						}
					}

					//begin-��ѯ��ί�е���������ί�е���
					String queryCode = String.format("%d%d", now.getYear()+1900,Integer.parseInt(CommissionType));		//��ѯί�е�����ʽ������ί����ʽ��ͬ��־λ��ͬ
					String queryString = "select max(model.code) from CommissionSheet as model where model.code like ?";
					List<Object> retList = cSheetMgr.findByHQL(queryString, queryCode+"%");
					Integer codeBeginInt = Integer.parseInt("000001");	//ί�е����
					if(retList.size() > 0 && retList.get(0) != null){
						codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(5)) + 1;
					}
					//end-��ѯ��ί�е���������ί�е���
					
					ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//���߷������Mgr
					ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//���߱�׼���ƹ���Mgr
					AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//���߳������ƹ���Mgr
					SubContractorManager subConMgr = new SubContractorManager();	//ת��������Mgr
					ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//���쳧����Mgr
					
					AddressManager addrMgr = new AddressManager();
					Address HeadNameAddr = new AddressManager().findById(Integer.parseInt(HeadNameId));	//̨ͷ���Ƶĵ�λ
					
					QualificationManager qualMgr = new QualificationManager();	//�����Ա���ʹ���Mgr
					List<Integer> qualList = new ArrayList<Integer>();
					qualList.add(FlagUtil.QualificationType.Type_Jianding);
					qualList.add(FlagUtil.QualificationType.Type_Jianyan);
					qualList.add(FlagUtil.QualificationType.Type_Jiaozhun);
					
					List<CommissionSheet> comList = new ArrayList<CommissionSheet>();	//ί�е��б�
					
					List<SubContractor> subConList = new ArrayList<SubContractor>();	//ί�е��б��Ӧ��ת��������ί�е�û��ת��������Ϊnull
					List<SysUser> alloteeList = new ArrayList<SysUser>();	//ί�е��б��Ӧ���ɶ��ˣ���ί�е�û���ɶ��ˣ���Ϊnull
					Timestamp commissionDate = new Timestamp(Date.valueOf(CommissionDate).getTime());	//ί������
					LocaleApplianceItemManager locAppItemMgr = new LocaleApplianceItemManager();	//�ֳ����������Ŀ��Mgr
					/********************   ��ί�е�    ******************/
					for(int i=0;i<Integer.parseInt(req.getParameter("YLNumber"));i++){//������Ԥ����Ŀ��ȵ�ί�е�
						//JSONObject jsonObj = appliancesArray.getJSONObject(i);
						//idList.add(jsonObj.getInt("Id"));		//ǰ��ҳ�����¼��ID
						
						CommissionSheet comSheet = new CommissionSheet();
						comSheet.setCommissionDate(commissionDate);	//ί������
						if(PromiseDate.length() > 0){
							comSheet.setPromiseDate(Date.valueOf(PromiseDate));	//��ŵ����
						}
						if(QuotationId.length() == 0){			//���۵���
							comSheet.setQuotationId(null);
						}else{
							comSheet.setQuotationId(QuotationId);
						}
						
						comSheet.setCommissionType(Integer.parseInt(CommissionType));//ί����ʽ
						comSheet.setCustomerId(CustomerId);	//ί�е�λID
						comSheet.setCustomerName(CustomerName);
						comSheet.setCustomerTel(CustomerTel);
						comSheet.setCustomerAddress(CustomerAddress);
						comSheet.setCustomerZipCode(CustomerZipCode);
						comSheet.setCustomerContactor(ContactPerson);
						comSheet.setCustomerContactorTel(ContactorTel);
						comSheet.setSampleFrom(SampleFrom);
						comSheet.setBillingTo(BillingTo);
						comSheet.setCustomerHandler(CustomerHandler);	//ί����
						comSheet.setReceiverId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//������ID
						comSheet.setReceiverName(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//����������
						
						comSheet.setHeadNameId(HeadNameAddr.getId());	//̨ͷ����ID
						comSheet.setHeadName(HeadNameAddr.getHeadName());	//̨ͷ����
						comSheet.setHeadNameEn(HeadNameAddr.getHeadNameEn()==null?"":HeadNameAddr.getHeadNameEn());	//̨ͷ����Ӣ��
						comSheet.setSampleAddress(RecipientAddress==null?null:Integer.parseInt(RecipientAddress));//��Ʒ���յص�
						comSheet.setReportAddress(PickupAddress==null?null:Integer.parseInt(PickupAddress));	//ȡ����ȡ����ص�
						
						comSheet.setLocaleCommissionCode(LocaleCommissionCode);	//�ֳ�ί�����
						comSheet.setLocaleCommissionDate(LocaleCommissionDate);	//�ֳ����ʱ��
						comSheet.setLocaleStaffId(LocaleStaffId);	//�ֳ�������ID
						
						//ί�е�������Ϣ��������Ϣ��
						comSheet.setCode(queryCode+String.format("%06d", codeBeginInt++));	//ί�е����
						int pwd =  (int) (Math.random()*9000+1000);	//���룺��λ�������1000~9999��
						comSheet.setPwd(new Integer(pwd).toString());
						comSheet.setCreatorId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//ί�е�������ID
						comSheet.setCreateDate(now);	//ί�е�����ʱ��
						comSheet.setStatus(0);	//ί�е�״̬�����ռ�
						comSheet.setAttachment(UIDUtil.get22BitUID());	//����������
						
						
						/**********************   ���������Ϣ    ************************/
						String SpeciesType = req.getParameter("SpeciesType");	//���߷�������
						String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//�������ID/��׼����ID
						String ApplianceName = req.getParameter("ApplianceName").trim();	//��������
						String Manufacturer= req.getParameter("Manufacturer");	//���쳧
						
						if(Integer.parseInt(SpeciesType) == 0){	//0:��׼���ƣ�1����������
							comSheet.setSpeciesType(false);	
							String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = stdName;	//��������δ��д����Ĭ��Ϊ��׼���ƻ��������
							}else{	//�������д���ж��Ƿ���ڱ�׼���ƣ���������ڱ�׼���ƣ�����볣�����Ʊ���
								if(!stdName.equalsIgnoreCase(ApplianceName.trim())){
									List<AppliancePopularName> popRetList = popNameMgr.findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("popularName", ApplianceName.trim(), "="));
									if(popRetList != null && popRetList.size() == 0){
										ApplianceStandardName sNameTemp = new ApplianceStandardName();
										sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
										AppliancePopularName popNameTemp = new AppliancePopularName();
										popNameTemp.setApplianceStandardName(sNameTemp);
										popNameTemp.setPopularName(ApplianceName);
										popNameTemp.setBrief(LetterUtil.String2Alpha(ApplianceName.trim()));
										popNameTemp.setStatus(0);
										popNameMgr.save(popNameTemp);
									}
								}
							}
							
							//�����������쳧
							if(Manufacturer != null && Manufacturer.trim().length() > 0){
								int intRet = mafMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("manufacturer", Manufacturer.trim(), "="));
								if(intRet == 0){
									ApplianceStandardName sNameTemp = new ApplianceStandardName();
									sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
									ApplianceManufacturer maf = new ApplianceManufacturer();
									maf.setApplianceStandardName(sNameTemp);
									maf.setManufacturer(Manufacturer.trim());
									maf.setBrief(LetterUtil.String2Alpha(Manufacturer.trim()));
									maf.setStatus(0);
									mafMgr.save(maf);
								}
							}
						}else{
							comSheet.setSpeciesType(true);	
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//��������δ��д����Ĭ��Ϊ��׼���ƻ��������
							}
						}
						comSheet.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));
						
						comSheet.setApplianceName(ApplianceName);	//����������
						comSheet.setAppFactoryCode(req.getParameter("ApplianceCode").trim());		//�������
						comSheet.setAppManageCode(req.getParameter("AppManageCode").trim());		//������
						comSheet.setApplianceModel(req.getParameter("Model"));		//�ͺŹ��
						comSheet.setRange(req.getParameter("Range"));		//������Χ
						comSheet.setAccuracy(req.getParameter("Accuracy"));	//���ȵȼ�
						comSheet.setManufacturer(req.getParameter("Manufacturer"));		//���쳧��
						comSheet.setQuantity(Integer.parseInt(req.getParameter("Quantity")));		//̨����
						
						comSheet.setMandatory(Integer.parseInt(req.getParameter("Mandatory"))==0?false:true);	//ǿ�Ƽ���
						comSheet.setUrgent(req.getParameter("Ness")==null?true:false);		//�Ƿ�Ӽ�
						comSheet.setSubcontract(req.getParameter("Trans")==null?true:false);		//�Ƿ�ת����0��ת����1:����Ҫת����
						
						String SubContractor = req.getParameter("SubContractor");
						if(req.getParameter("Trans")!=null && SubContractor!= null && SubContractor.trim().length() > 0){	//ת��
							List<SubContractor> subConRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
							if(subConRetList != null && subConRetList.size() > 0){
								subConList.add(subConRetList.get(0));
							}else{
								subConList.add(null);
							}
						}else{
							subConList.add(null);
						}
						comSheet.setAppearance(req.getParameter("Appearance"));//��۸���
						comSheet.setRepair(Integer.parseInt(req.getParameter("Repair"))==0?false:true);		//�������
						comSheet.setReportType(Integer.parseInt(req.getParameter("ReportType")));	//������ʽ
						comSheet.setOtherRequirements(req.getParameter("OtherRequirements"));	//����Ҫ��
						comSheet.setLocation(req.getParameter("Location"));		//���λ��						
						comSheet.setStatus(-1);//��Ԥ��			
						/**********************  �ж��ɶ����Ƿ���ڼ���Ч�������뵽alloteeList   ****************************/
						String Allotee = req.getParameter("Allotee");
						if(Allotee != null && Allotee.trim().length() > 0){
							Allotee = Allotee.trim();
							comSheet.setAllotee(Allotee);		//�ɶ���
							List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, qualList);
							if(qualRetList != null && qualRetList.size() > 0){
								boolean alloteeChecked = false;
								for(Object[] objArray : qualRetList){
									if(!qualMgr.checkUserQualify((Integer)objArray[0], comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//û�иü�����Ŀ�ļ�����������
										alloteeChecked = true;
										SysUser tempUser = new SysUser();
										tempUser.setId((Integer)objArray[0]);
										tempUser.setName((String)objArray[1]);
										
										alloteeList.add(tempUser);
										comSheet.setStatus(1);	//����ί�е�״̬���ѷ���
										break;
									}
								}
								
								if(!alloteeChecked){
									throw new Exception(String.format("�ɶ��� '%s' �����ڻ�û�����ʼ�����Ŀ��%s��������ѡ��", Allotee, comSheet.getApplianceName()));
								}
							}else{
								throw new Exception(String.format("�ɶ��� '%s' �����ڻ�û�����ʼ�����Ŀ��%s��������ѡ��", Allotee, comSheet.getApplianceName()));
							}
						}else{
							comSheet.setAllotee(null);		//�ɶ���
							alloteeList.add(null);
						}
			
						comList.add(comSheet);					
					}//ί�е�����ѭ����ӽ���
					
				
					if(cSheetMgr.saveByBatchYL(comList,(SysUser)req.getSession().getAttribute("LOGIN_USER"),now)){
						try{
							/************  ���»�����ί�е�λ��ϵ��   *************/
							if(ContactPerson.length() > 0){
								CustomerContactorManager cusConMgr = new CustomerContactorManager();
								List<CustomerContactor> cusConList = cusConMgr.findByVarProperty(new KeyValueWithOperator("customerId", CustomerId, "="), new KeyValueWithOperator("name", ContactPerson, "="));
								if(cusConList != null){
									if(cusConList.size() > 0){
										CustomerContactor c = cusConList.get(0);
										if(ContactorTel.length() > 0){
											if(!ContactorTel.equalsIgnoreCase(c.getCellphone1()) && (c.getCellphone2() == null || c.getCellphone2().length() == 0)){
												c.setCellphone2(c.getCellphone1());
											}
											c.setCellphone1(ContactorTel);
										}
										c.setLastUse(now);
										c.setCount(c.getCount()+1);
										cusConMgr.update(c);
									}else{
										CustomerContactor c = new CustomerContactor();
										///////////////////////////////////////
										Customer a=new Customer();
										a.setId(CustomerId);
										c.setCustomer(a);
										//////////////////////////////////////
										//c.setCustomerId(CustomerId);
										c.setName(ContactPerson);
										c.setCellphone1(ContactorTel);
										c.setLastUse(now);
										c.setCount(1);
										cusConMgr.save(c);
									}
								}
							}
						}catch(Exception e){ }
						JSONObject retObj=new JSONObject();
						retObj.put("IsOK", true);
						JSONArray retArray = new JSONArray();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
//						UserManager userMgr = new UserManager();
//						String ReceiverName = ReceiverId.length()>0?userMgr.findById(Integer.parseInt(ReceiverId)).getName():"";//����������
						
						Address RecipientAddressObj = RecipientAddress==null?null:addrMgr.findById(Integer.parseInt(RecipientAddress)); //��Ʒ���յص�
						Address PickupAddressObj = PickupAddress==null?null:addrMgr.findById(Integer.parseInt(PickupAddress));	//ȡ����ȡ����ص�
						UserManager uMgr = new UserManager();
						for(int i = 0; i < comList.size(); i++){
							CommissionSheet cSheet = comList.get(i);
							JSONObject record = new JSONObject();
							record.put("Id", i+1);
							record.put("CommissionNumber", cSheet.getCode());		//ί�е���
							
							record.put("Code", cSheet.getCode());	//ί�е���
							record.put("Pwd", cSheet.getPwd());	//ί�е�����
						
							retArray.put(record);
						}
						retObj.put("CommissionSheetList", retArray);
    
						resp.setContentType("text/html;charset=utf-8");
						resp.getWriter().write(retObj.toString());
					}else{
						throw new Exception("����ί�е���Ϣʧ�ܣ�");
					}
					
				} catch(NumberFormatException e){	//�ַ���תInteger����
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("����ʧ�ܣ�������Ϣ���������벻�������ʽ����"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.NumberFormatException.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 16", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 16", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}catch (Exception e){
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("����ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 16", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 16", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}
				break;
			case 17: // �޸�Ԥ��ί�е�
				try {
					String QuotationId = req.getParameter("QuotationId");	//���۵���
					String CommissionDate = req.getParameter("CommissionDate");		//ί������
					String PromiseDate = req.getParameter("PromiseDate").trim();			//��ŵ����
					String CommissionType = req.getParameter("CommissionType");		//ί����ʽ
					String CustomerName  = req.getParameter("CustomerName").trim();		//ί�е�λ
					String CustomerTel = req.getParameter("CustomerTel").trim();
					String CustomerAddress = req.getParameter("CustomerAddress").trim();
					String CustomerZipCode = req.getParameter("CustomerZipCode").trim();
					String ContactPerson = req.getParameter("ContactPerson").trim();
					String ContactorTel = req.getParameter("ContactorTel").trim();
					String SampleFrom = req.getParameter("SampleFrom");	//֤�鵥λ
					String BillingTo = req.getParameter("BillingTo");	//��Ʊ��λ
					
					String CustomerHandler = req.getParameter("CustomerHandler");	//ί����
//					String ReceiverName = req.getParameter("ReceiverName").trim();	//������
					
					String HeadNameId = req.getParameter("HeadName").trim();	//̨ͷ����ID
					String RecipientAddress = req.getParameter("RecipientAddress");	//������ַ
					String PickupAddress = req.getParameter("PickupAddress");		//ȡ����ַ					
					
					String comSheetCode = req.getParameter("comSheetCode").trim();	//���������
					
					String LocaleCommissionCode = null;	//�ֳ�ί�е���
					Timestamp LocaleCommissionDate = null;	//�ֳ����ʱ��
					Integer LocaleStaffId = null;	//�ֳ���⸺����ID
					
					if(QuotationId != null){
						QuotationId = QuotationId.trim();
					}else{
						QuotationId = "";
					}
					
					if((CommissionDate == null || CommissionDate.length() == 0) && !CommissionType.equals("2")){
						throw new Exception("ί������Ϊ�գ�");
					}
					if(SampleFrom.length() == 0){
						SampleFrom = CustomerName;
					}
					if(BillingTo.length() == 0){
						BillingTo = CustomerName;
					}
					if(RecipientAddress == null || RecipientAddress.length() == 0){
						RecipientAddress = null;
					}
					if(PickupAddress == null || PickupAddress.length() == 0){
						PickupAddress = null;
					}
					
					Timestamp now = new Timestamp(System.currentTimeMillis());
					CustomerManager cusMgr = new CustomerManager();		//�ͻ�����Mgr
					Integer CustomerId;
					List<Customer> cusList = cusMgr.findByVarProperty(new KeyValueWithOperator("name",CustomerName,"="), 
							new KeyValueWithOperator("status", 1, "<>"));	//����ί�е�λ��ID
					if(cusList != null && cusList.size() == 1){
						CustomerId = cusList.get(0).getId();
					}else if(cusList != null && cusList.size() > 1){
						throw new Exception("���ݿ����ҵ����������ͬ��ί�е�λ:"+CustomerName+", �뵽��ί�е�λ��Ϣ���������޸ģ�");
					}else{
						throw new Exception("ί�е�λ�����ڣ��¿ͻ������½�ί�е�λ��");
					}
					
					if(QuotationId.length() > 0){
						if(new QuotationManager().findById(QuotationId) == null){
							throw new Exception(String.format("���۵���:%s ��Ч��", QuotationId));
						}
					}
					//begin-��ѯ��ί�е���������ί�е���
					String queryCode = String.format("%d%d", now.getYear()+1900,Integer.parseInt(CommissionType));		//��ѯί�е�����ʽ������ί����ʽ��ͬ��־λ��ͬ
					String queryString = "select max(model.code) from CommissionSheet as model where model.code like ?";
					List<Object> retList = cSheetMgr.findByHQL(queryString, queryCode+"%");
					Integer codeBeginInt = Integer.parseInt("000001");	//ί�е����
					if(retList.size() > 0 && retList.get(0) != null){
						codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(5)) + 1;
					}
					//end-��ѯ��ί�е���������ί�е���
					
					ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//���߷������Mgr
					ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//���߱�׼���ƹ���Mgr
					AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//���߳������ƹ���Mgr
					SubContractorManager subConMgr = new SubContractorManager();	//ת��������Mgr
					ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//���쳧����Mgr
					
					AddressManager addrMgr = new AddressManager();
					Address HeadNameAddr = new AddressManager().findById(Integer.parseInt(HeadNameId));	//̨ͷ���Ƶĵ�λ
					
					QualificationManager qualMgr = new QualificationManager();	//�����Ա���ʹ���Mgr
					List<Integer> qualList = new ArrayList<Integer>();
					qualList.add(FlagUtil.QualificationType.Type_Jianding);
					qualList.add(FlagUtil.QualificationType.Type_Jianyan);
					qualList.add(FlagUtil.QualificationType.Type_Jiaozhun);
					
					List<CommissionSheet> comList = new ArrayList<CommissionSheet>();	//ί�е��б�
					
					List<SubContractor> subConList = new ArrayList<SubContractor>();	//ί�е��б��Ӧ��ת��������ί�е�û��ת��������Ϊnull
					List<SysUser> alloteeList = new ArrayList<SysUser>();	//ί�е��б��Ӧ���ɶ��ˣ���ί�е�û���ɶ��ˣ���Ϊnull
					Timestamp commissionDate = new Timestamp(Date.valueOf(CommissionDate).getTime());	//ί������
					LocaleApplianceItemManager locAppItemMgr = new LocaleApplianceItemManager();	//�ֳ����������Ŀ��Mgr
					/********************   ��ί�е�    ******************/
			
					CommissionSheet comSheet = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", comSheetCode, "=")).get(0);
					
					comSheet.setCommissionDate(commissionDate);	//ί������
					if(PromiseDate.length() > 0){
						comSheet.setPromiseDate(Date.valueOf(PromiseDate));	//��ŵ����
					}
					if(QuotationId.length() == 0){			//���۵���
						comSheet.setQuotationId(null);
					}else{
						comSheet.setQuotationId(QuotationId);
					}
					
					comSheet.setCommissionType(Integer.parseInt(CommissionType));//ί����ʽ
					comSheet.setCustomerId(CustomerId);	//ί�е�λID
					comSheet.setCustomerName(CustomerName);
					comSheet.setCustomerTel(CustomerTel);
					comSheet.setCustomerAddress(CustomerAddress);
					comSheet.setCustomerZipCode(CustomerZipCode);
					comSheet.setCustomerContactor(ContactPerson);
					comSheet.setCustomerContactorTel(ContactorTel);
					comSheet.setSampleFrom(SampleFrom);
					comSheet.setBillingTo(BillingTo);
					comSheet.setCustomerHandler(CustomerHandler);	//ί����
					comSheet.setReceiverId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//������ID
					comSheet.setReceiverName(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//����������
					
					comSheet.setHeadNameId(HeadNameAddr.getId());	//̨ͷ����ID
					comSheet.setHeadName(HeadNameAddr.getHeadName());	//̨ͷ����
					comSheet.setHeadNameEn(HeadNameAddr.getHeadNameEn()==null?"":HeadNameAddr.getHeadNameEn());	//̨ͷ����Ӣ��
					comSheet.setSampleAddress(RecipientAddress==null?null:Integer.parseInt(RecipientAddress));//��Ʒ���յص�
					comSheet.setReportAddress(PickupAddress==null?null:Integer.parseInt(PickupAddress));	//ȡ����ȡ����ص�
					
					comSheet.setLocaleCommissionCode(LocaleCommissionCode);	//�ֳ�ί�����
					comSheet.setLocaleCommissionDate(LocaleCommissionDate);	//�ֳ����ʱ��
					comSheet.setLocaleStaffId(LocaleStaffId);	//�ֳ�������ID
					
					//ί�е�������Ϣ��������Ϣ��				
					/**********************   ���������Ϣ    ************************/
					
					String SpeciesType = req.getParameter("SpeciesType");	//���߷�������
					String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//�������ID/��׼����ID
					String ApplianceName = req.getParameter("ApplianceName").trim();	//��������
					String Manufacturer= req.getParameter("Manufacturer");	//���쳧
					//System.out.println("SpeciesType:"+SpeciesType+"ApplianceSpeciesId:"+ApplianceSpeciesId);
					if(Integer.parseInt(SpeciesType) == 0){	//0:��׼���ƣ�1����������
						comSheet.setSpeciesType(false);	
						String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
						if(ApplianceName == null || ApplianceName.trim().length() == 0){
							ApplianceName = stdName;	//��������δ��д����Ĭ��Ϊ��׼���ƻ��������
						}else{	//�������д���ж��Ƿ���ڱ�׼���ƣ���������ڱ�׼���ƣ�����볣�����Ʊ���
							if(!stdName.equalsIgnoreCase(ApplianceName.trim())){
								List<AppliancePopularName> popRetList = popNameMgr.findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("popularName", ApplianceName.trim(), "="));
								if(popRetList != null && popRetList.size() == 0){
									ApplianceStandardName sNameTemp = new ApplianceStandardName();
									sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
									AppliancePopularName popNameTemp = new AppliancePopularName();
									popNameTemp.setApplianceStandardName(sNameTemp);
									popNameTemp.setPopularName(ApplianceName);
									popNameTemp.setBrief(LetterUtil.String2Alpha(ApplianceName.trim()));
									popNameTemp.setStatus(0);
									popNameMgr.save(popNameTemp);
								}
							}
						}
						
						//�����������쳧
						if(Manufacturer != null && Manufacturer.trim().length() > 0){
							int intRet = mafMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("manufacturer", Manufacturer.trim(), "="));
							if(intRet == 0){
								ApplianceStandardName sNameTemp = new ApplianceStandardName();
								sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
								ApplianceManufacturer maf = new ApplianceManufacturer();
								maf.setApplianceStandardName(sNameTemp);
								maf.setManufacturer(Manufacturer.trim());
								maf.setBrief(LetterUtil.String2Alpha(Manufacturer.trim()));
								maf.setStatus(0);
								mafMgr.save(maf);
							}
						}
					}else{
						comSheet.setSpeciesType(true);	
						if(ApplianceName == null || ApplianceName.trim().length() == 0){
							ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//��������δ��д����Ĭ��Ϊ��׼���ƻ��������
						}
					}
					comSheet.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));
					
					comSheet.setApplianceName(ApplianceName);	//����������
					comSheet.setAppFactoryCode(req.getParameter("ApplianceCode").trim());		//�������
					comSheet.setAppManageCode(req.getParameter("AppManageCode").trim());		//������
					comSheet.setApplianceModel(req.getParameter("Model"));		//�ͺŹ��
					comSheet.setRange(req.getParameter("Range"));		//������Χ
					comSheet.setAccuracy(req.getParameter("Accuracy"));	//���ȵȼ�
					comSheet.setManufacturer(req.getParameter("Manufacturer"));		//���쳧��
					comSheet.setQuantity(Integer.parseInt(req.getParameter("Quantity")));		//̨����
					//System.out.println("Mandatory:"+req.getParameter("Mandatory"));
					comSheet.setMandatory(Integer.parseInt(req.getParameter("Mandatory").trim())==0?false:true);	//ǿ�Ƽ���
					comSheet.setUrgent(req.getParameter("Ness")==null?true:false);		//�Ƿ�Ӽ�
					comSheet.setSubcontract(req.getParameter("Trans")==null?true:false);		//�Ƿ�ת����0��ת����1:����Ҫת����
					
					String SubContractor = req.getParameter("SubContractor");
					if(req.getParameter("Trans")!=null && SubContractor!= null && SubContractor.trim().length() > 0){	//ת��
						List<SubContractor> subConRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
						if(subConRetList != null && subConRetList.size() > 0){
							subConList.add(subConRetList.get(0));
						}else{
							subConList.add(null);
						}
					}else{
						subConList.add(null);
					}
					comSheet.setAppearance(req.getParameter("Appearance"));//��۸���
					comSheet.setRepair(Integer.parseInt(req.getParameter("Repair"))==0?false:true);		//�������
					comSheet.setReportType(Integer.parseInt(req.getParameter("ReportType")));	//������ʽ
					comSheet.setOtherRequirements(req.getParameter("OtherRequirements"));	//����Ҫ��
					comSheet.setLocation(req.getParameter("Location"));		//���λ��						
					
					comSheet.setStatus(0);//���ռ�	
					
					/**********************  �ж��ɶ����Ƿ���ڼ���Ч�������뵽alloteeList   ****************************/
					String Allotee = req.getParameter("Allotee");
					if(Allotee != null && Allotee.trim().length() > 0){
						Allotee = Allotee.trim();
						comSheet.setAllotee(Allotee);		//�ɶ���
						List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, qualList);
						if(qualRetList != null && qualRetList.size() > 0){
							boolean alloteeChecked = false;
							for(Object[] objArray : qualRetList){
								if(!qualMgr.checkUserQualify((Integer)objArray[0], comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//û�иü�����Ŀ�ļ�����������
									alloteeChecked = true;
									SysUser tempUser = new SysUser();
									tempUser.setId((Integer)objArray[0]);
									tempUser.setName((String)objArray[1]);
									
									alloteeList.add(tempUser);
									comSheet.setStatus(1);	//����ί�е�״̬���ѷ���
									break;
								}
							}
							
							if(!alloteeChecked){
								throw new Exception(String.format("�ɶ��� '%s' �����ڻ�û�����ʼ�����Ŀ��%s��������ѡ��", Allotee, comSheet.getApplianceName()));
							}
						}else{
							throw new Exception(String.format("�ɶ��� '%s' �����ڻ�û�����ʼ�����Ŀ��%s��������ѡ��", Allotee, comSheet.getApplianceName()));
						}
					}else{
						comSheet.setAllotee(null);		//�ɶ���
						alloteeList.add(null);
					}
		
					comList.add(comSheet);					
					//ί�е�������ӽ���
			
					if(cSheetMgr.updateByBatch(comList,subConList,alloteeList,(SysUser)req.getSession().getAttribute("LOGIN_USER"),(SysUser)req.getSession().getAttribute("LOGIN_USER"),now)){
						try{
							/************  ���»�����ί�е�λ��ϵ��   *************/
							if(ContactPerson.length() > 0){
								CustomerContactorManager cusConMgr = new CustomerContactorManager();
								List<CustomerContactor> cusConList = cusConMgr.findByVarProperty(new KeyValueWithOperator("customerId", CustomerId, "="), new KeyValueWithOperator("name", ContactPerson, "="));
								if(cusConList != null){
									if(cusConList.size() > 0){
										CustomerContactor c = cusConList.get(0);
										if(ContactorTel.length() > 0){
											if(!ContactorTel.equalsIgnoreCase(c.getCellphone1()) && (c.getCellphone2() == null || c.getCellphone2().length() == 0)){
												c.setCellphone2(c.getCellphone1());
											}
											c.setCellphone1(ContactorTel);
										}
										c.setLastUse(now);
										c.setCount(c.getCount()+1);
										cusConMgr.update(c);
									}else{
										CustomerContactor c = new CustomerContactor();
										///////////////////////////////////////
										Customer a=new Customer();
										a.setId(CustomerId);
										c.setCustomer(a);
										//////////////////////////////////////
										//c.setCustomerId(CustomerId);
										c.setName(ContactPerson);
										c.setCellphone1(ContactorTel);
										c.setLastUse(now);
										c.setCount(1);
										cusConMgr.save(c);
									}
								}
							}
						}catch(Exception e){ }
						JSONObject retObj=new JSONObject();
						retObj.put("IsOK", true);
						JSONArray retArray = new JSONArray();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
//						UserManager userMgr = new UserManager();
//						String ReceiverName = ReceiverId.length()>0?userMgr.findById(Integer.parseInt(ReceiverId)).getName():"";//����������
						
						Address RecipientAddressObj = RecipientAddress==null?null:addrMgr.findById(Integer.parseInt(RecipientAddress)); //��Ʒ���յص�
						Address PickupAddressObj = PickupAddress==null?null:addrMgr.findById(Integer.parseInt(PickupAddress));	//ȡ����ȡ����ص�
						UserManager uMgr = new UserManager();
					
						
						JSONObject record = new JSONObject();
						record.put("Id", 1);
						record.put("CommissionNumber", comSheet.getCode());		//ί�е���
						JSONObject printRecord = new JSONObject();
						printRecord.put("Code", comSheet.getCode());	//ί�е���
						printRecord.put("Pwd", comSheet.getPwd());	//ί�е�����
						printRecord.put("CommissionDate", sdf.format(comSheet.getCommissionDate()));	//ί������
						printRecord.put("CustomerName", comSheet.getCustomerName());	//ί�е�λ����
						printRecord.put("CustomerTel", comSheet.getCustomerTel());	//ί�е�λ�绰
						printRecord.put("CustomerAddress", comSheet.getCustomerAddress());	//ί�е�λ��ַ
						printRecord.put("CustomerZipCode", comSheet.getCustomerZipCode());	//ί�е�λ��������
						printRecord.put("SampleFrom", comSheet.getSampleFrom());	//֤�鵥λ����
						printRecord.put("BillingTo", comSheet.getBillingTo());	//��Ʊ��λ����
						
						printRecord.put("ApplianceName", comSheet.getApplianceName());	//��������
						printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", comSheet.getApplianceModel(),comSheet.getRange(),comSheet.getAccuracy(),comSheet.getManufacturer()));//������Ϣ
						printRecord.put("ApplianceNumber",String.format("%s[%s]", comSheet.getAppFactoryCode(),comSheet.getAppManageCode()));//���߱��
						printRecord.put("Quantity",comSheet.getQuantity().toString());//̨����
						printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(comSheet.getMandatory()));//ǿ�Ƽ���
						printRecord.put("Appearance", comSheet.getAppearance());//��۸���
						printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(comSheet.getRepair()));//�������
						printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(comSheet.getReportType()));//������ʽ
						printRecord.put("OtherRequirements", comSheet.getOtherRequirements());//����Ҫ��
						printRecord.put("Location", comSheet.getLocation());	//���λ��
						printRecord.put("Allotee", comSheet.getAllotee());	//�ɶ���
						printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//����������
						
						printRecord.put("HeadName",HeadNameAddr.getHeadName());	//̨ͷ����
						printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//������ַ
						printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
						printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//ȡ����ַ
						printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
						
						SysUser allotee = alloteeList.get(0);
						if(allotee != null){
							 SysUser tempUser = uMgr.findById(allotee.getId());
							printRecord.put("AlloteeJobNum",tempUser.getJobNum());	//Ա������
						}else{
							printRecord.put("AlloteeJobNum","");	//Ա������
						}
						
						//record.put("PrintObj", printRecord);
						//retArray.put(record);
						
						//retObj.put("CommissionSheetList", retArray);
						retObj.put("PrintObj", printRecord);
    
						resp.setContentType("text/html;charset=utf-8");
						resp.getWriter().write(retObj.toString());
					}else{
						throw new Exception("����ί�е���Ϣʧ�ܣ�");
					}
					
				} catch(NumberFormatException e){	//�ַ���תInteger����
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("����ʧ�ܣ�������Ϣ���������벻�������ʽ����"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.NumberFormatException.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 17", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 17", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}catch (Exception e){
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("����ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 17", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 17", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}
				break;
			case 18://��ѯδʹ�õ�Ԥ��ί�е���
				JSONObject retJSON18 = new JSONObject();
				try{
					
					String DateFrom = req.getParameter("DateFrom");
					String DateEnd = req.getParameter("DateEnd");
					
					int page = 1;
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
					
					List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
					
					if(DateFrom != null && DateFrom.length() > 0){
						Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", Start, ">="));
					}
					if(DateEnd != null && DateEnd.length() > 0){
						Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", End, "<="));
					}
					
					keys.add(new KeyValueWithOperator("status", CommissionSheetStatus.Status_YuLiuZhong, "="));
					List<CommissionSheet> retList = cSheetMgr.findPagedAllBySort(page, rows, "code", true, keys);
					int total = cSheetMgr.getTotalCount(keys);
					JSONArray options = new JSONArray();
					for(CommissionSheet cSheet : retList){
						JSONObject option = new JSONObject();
						option.put("Code", cSheet.getCode());
						option.put("Pwd", cSheet.getPwd());
						option.put("CommissionDate", cSheet.getCommissionDate());
						
						options.put(option);
					}
					
					retJSON18.put("total", total);
					retJSON18.put("rows", options);
				}catch(Exception e){
					try{
						retJSON18.put("total", 0);
						retJSON18.put("rows", new JSONArray());
					}catch(Exception e1){}
					if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
						log.debug("exception in CommissionSheetServlet-->case 18", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 18", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON18.toString());
				}
				break;
			}
		
		
	}

}
