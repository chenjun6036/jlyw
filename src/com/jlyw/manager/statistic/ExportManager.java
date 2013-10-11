package com.jlyw.manager.statistic;

import java.util.ArrayList;
import java.util.List;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.manager.UserManager;
import com.jlyw.util.CommissionSheetFlagUtil;
import com.jlyw.util.FlagUtil.CommissionSheetStatus;

public class ExportManager {
	
	/**
	 * �����շ���ҵ��ͳ�ƽ��excel����
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> SFSMission_formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.has("EmpName")?jsonObj.getString("EmpName"):"");
		result.add(jsonObj.has("CommissionTypeName")?jsonObj.getString("CommissionTypeName"):"");
		result.add(jsonObj.has("ChuDanShu")?jsonObj.getString("ChuDanShu"):"");
		result.add(jsonObj.has("ChuDanTaiJianShu")?jsonObj.getString("ChuDanTaiJianShu"):"");
		result.add(jsonObj.has("WanGongDanShu")?jsonObj.getString("WanGongDanShu"):"");
		result.add(jsonObj.has("WanGongTaiJianShu")?jsonObj.getString("WanGongTaiJianShu"):"");
		result.add(jsonObj.has("JieZhangDanShu")?jsonObj.getString("JieZhangDanShu"):"");
		result.add(jsonObj.has("JieZhangTaiJianShu")?jsonObj.getString("JieZhangTaiJianShu"):"");
		result.add(jsonObj.has("ZhuXiaoDanShu")?jsonObj.getString("ZhuXiaoDanShu"):"");
		result.add(jsonObj.has("ZhuXiaoTaiJianShu")?jsonObj.getString("ZhuXiaoTaiJianShu"):"");
		result.add(jsonObj.has("TuiYangDanShu")?jsonObj.getString("TuiYangDanShu"):"");
		result.add(jsonObj.has("TuiYangTaiJianShu")?jsonObj.getString("TuiYangTaiJianShu"):"");
		
		return result;
	}
	
	/**
	 * �����շ���ҵ��ͳ�ƽ��title
	 * @return
	 */
	public List<String> SFSMission_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("Ա��");
		result.add("ί����ʽ");
		result.add("������");
		result.add("����̨����");
		result.add("�깤����");
		result.add("�깤̨����");
		result.add("���˵���");
		result.add("����̨����");
		result.add("ע������");
		result.add("ע��̨����");
		result.add("��������");
		result.add("����̨����");
		
		return result;
	}
	
	/**
	 * ������ֵ��ϸexcel
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> Detail_formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.has("Code")?jsonObj.getString("Code"):"");
		result.add(jsonObj.has("CommissionDate")?jsonObj.getString("CommissionDate"):"");
		result.add(jsonObj.has("CustomerName")?jsonObj.getString("CustomerName"):"");
		result.add(jsonObj.has("ApplianceName")?jsonObj.getString("ApplianceName"):"");
		result.add(jsonObj.has("Model")?jsonObj.getString("Model"):"");
		result.add(jsonObj.has("Range")?jsonObj.getString("Range"):"");
		result.add(jsonObj.has("Accuracy")?jsonObj.getString("Accuracy"):"");
		result.add(jsonObj.has("Manufacturer")?jsonObj.getString("Manufacturer"):"");
		result.add(jsonObj.has("Quantity")?jsonObj.getString("Quantity"):"");
		result.add(jsonObj.has("FinishQuantity")?jsonObj.getString("FinishQuantity"):"");
		result.add(jsonObj.has("WithdrawQuantity")?jsonObj.getString("WithdrawQuantity"):"");
		result.add(jsonObj.has("CertificateCode")?jsonObj.getString("CertificateCode"):"");
		result.add(jsonObj.has("TotalFee")?jsonObj.getString("TotalFee"):"");
		result.add(jsonObj.has("TestFee")?jsonObj.getString("TestFee"):"");
		result.add(jsonObj.has("RepairFee")?jsonObj.getString("RepairFee"):"");
		result.add(jsonObj.has("MaterialFee")?jsonObj.getString("MaterialFee"):"");
		result.add(jsonObj.has("CarFee")?jsonObj.getString("CarFee"):"");
		result.add(jsonObj.has("DebugFee")?jsonObj.getString("DebugFee"):"");
		result.add(jsonObj.has("OtherFee")?jsonObj.getString("OtherFee"):"");
		result.add(jsonObj.has("CheckOutStaff")?jsonObj.getString("CheckOutStaff"):"");
		result.add(jsonObj.has("CheckOutTime")?jsonObj.getString("CheckOutTime"):"");
		result.add(jsonObj.has("DetailListCode")?jsonObj.getString("DetailListCode"):"");
		result.add(jsonObj.has("FeeAlloteeName")?jsonObj.getString("FeeAlloteeName"):"");
		
		return result;
	}
	
	
	/**
	 * ��ֵ��ϸtitle
	 * @return
	 */
	public List<String> Detail_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("ί�е���");
		result.add("ί������");
		result.add("ί�е�λ����");
		result.add("��������");
		result.add("�ͺŹ��");
		result.add("������Χ");
		result.add("��ȷ����/׼ȷ�ȵȼ�/����ʲ�");
		result.add("���쳧��");
		result.add("̨����");
		result.add("�깤̨����");
		result.add("����̨����");
		result.add("֤���");
		result.add("��ֵ");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("��ͨ��");
		result.add("���Է�");
		result.add("��������");
		result.add("������");
		result.add("��������");
		result.add("�����嵥��");
		result.add("��ֵ��");
		
		return result;
	}
	
	/**
	 * ��ֵͳ����Ϣexcel
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> OutPut_formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.getString("DeptCode"));
		result.add(jsonObj.getString("DeptName"));
		result.add(jsonObj.getString("UserJobNum"));
		result.add(jsonObj.getString("UserName"));
		result.add(jsonObj.getString("TotalFee"));
		result.add(jsonObj.getString("Quantity"));
		result.add(jsonObj.getString("Withdraw"));
		result.add(jsonObj.getString("Certificate"));
		result.add(jsonObj.getString("TestFee"));
		result.add(jsonObj.getString("RepairFee"));
		result.add(jsonObj.getString("MaterialFee"));
		result.add(jsonObj.getString("CarFee"));
		result.add(jsonObj.getString("DebugFee"));
		result.add(jsonObj.getString("OtherFee"));
		
		return result;
	}
	
	/**
	 * ��ֵͳ����Ϣtitle
	 * @return
	 */
	public List<String> OutPut_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���Ŵ���");
		result.add("��������");
		result.add("Ա������");
		result.add("Ա������");
		result.add("��ֵ");
		result.add("���̨����");
		result.add("����̨����");
		result.add("֤������");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("��ͨ��");
		result.add("���Է�");
		result.add("��������");
		
		return result;
	}
	
	/**
	 * ��ֵͳ����Ϣexcel
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> DetailList_formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.has("CheckOutStaffJobNum")?jsonObj.getString("CheckOutStaffJobNum"):"");
		result.add(jsonObj.has("CheckOutStaff")?jsonObj.getString("CheckOutStaff"):"");
		result.add(jsonObj.has("DetailListCode")?jsonObj.getString("DetailListCode"):"");
		result.add(jsonObj.has("Cash")?jsonObj.getString("Cash"):"");
		result.add(jsonObj.has("Cheque")?jsonObj.getString("Cheque"):"");
		result.add(jsonObj.has("Account")?jsonObj.getString("Account"):"");
		result.add(jsonObj.has("TotalFee")?jsonObj.getString("TotalFee"):"");
		result.add(jsonObj.has("PaidFee")?jsonObj.getString("PaidFee"):"");
		result.add(jsonObj.has("InvoiceCode")?jsonObj.getString("InvoiceCode"):"");
		result.add(jsonObj.has("CheckOutDate")?jsonObj.getString("CheckOutDate"):"");
		result.add(jsonObj.has("CommissionSheetCode")?jsonObj.getString("CommissionSheetCode"):"");
		result.add(jsonObj.has("CommissionDate")?jsonObj.getString("CommissionDate"):"");
		result.add(jsonObj.has("CustomerName")?jsonObj.getString("CustomerName"):"");
		result.add(jsonObj.has("ApplianceName")?jsonObj.getString("ApplianceName"):"");
		result.add(jsonObj.has("Model")?jsonObj.getString("Model"):"");
		result.add(jsonObj.has("Range")?jsonObj.getString("Range"):"");
		result.add(jsonObj.has("Accuracy")?jsonObj.getString("Accuracy"):"");
		result.add(jsonObj.has("Quantity")?jsonObj.getString("Quantity"):"");
		result.add(jsonObj.has("Withdraw")?jsonObj.getString("Withdraw"):"");
		result.add(jsonObj.has("cSheetTotalFee")?jsonObj.getString("cSheetTotalFee"):"");
		result.add(jsonObj.has("cSheetTestFee")?jsonObj.getString("cSheetTestFee"):"");
		result.add(jsonObj.has("cSheetRepairFee")?jsonObj.getString("cSheetRepairFee"):"");
		result.add(jsonObj.has("cSheetMaterialFee")?jsonObj.getString("cSheetMaterialFee"):"");
		result.add(jsonObj.has("cSheetCarFee")?jsonObj.getString("cSheetCarFee"):"");
		result.add(jsonObj.has("cSheetDebugFee")?jsonObj.getString("cSheetDebugFee"):"");
		result.add(jsonObj.has("cSheetOtherFee")?jsonObj.getString("cSheetOtherFee"):"");
				
		return result;
	}
	
	/**
	 * ��ֵͳ����Ϣtitle
	 * @return
	 */
	public List<String> DetailList__formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("����");
		result.add("����Ա");
		result.add("�����嵥��");
		result.add("�ֽ�");
		result.add("֧Ʊ");
		result.add("�˻�Ԥ����");
		result.add("�ܼƷ���");
		result.add("�Ѹ�����");
		result.add("֧Ʊ��");
		result.add("����ʱ��");
		result.add("ί�е���");
		result.add("ί��ʱ��");
		result.add("ί�е�λ");
		result.add("��������");
		result.add("�ͺŹ��");
		result.add("������Χ");
		result.add("׼ȷ��/��ȷ����/����ʲ�");
		result.add("�ͼ�̨����");
		result.add("����̨����");
		result.add("�ܷ���");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("��ͨ��");
		result.add("���Է�");
		result.add("��������");
		
		return result;
	}
	
	/**
	 * ����֤��excel
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> Certificate_formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.has("Code")?jsonObj.getString("Code"):"");
		result.add(jsonObj.has("CommissionSheetCode")?jsonObj.getString("CommissionSheetCode"):"");
		result.add(jsonObj.has("CustomerName")?jsonObj.getString("CustomerName"):"");
		result.add(jsonObj.has("CustomerAddress")?jsonObj.getString("CustomerAddress"):"");
		result.add(jsonObj.has("CustomerZipCode")?jsonObj.getString("CustomerZipCode"):"");
		result.add(jsonObj.has("CustomerTel")?jsonObj.getString("CustomerTel"):"");
		result.add(jsonObj.has("CustomerContactor")?jsonObj.getString("CustomerContactor"):"");
		result.add(jsonObj.has("ApplianceName")?jsonObj.getString("ApplianceName"):"");
		result.add(jsonObj.has("AppManageCode")?jsonObj.getString("AppManageCode"):"");
		result.add(jsonObj.has("ApplianceCode")?jsonObj.getString("ApplianceCode"):"");
		result.add(jsonObj.has("Model")?jsonObj.getString("Model"):"");
		result.add(jsonObj.has("Quantity")?jsonObj.getString("Quantity"):"");
		result.add(jsonObj.has("Validity")?jsonObj.getString("Validity"):"");
		result.add(jsonObj.has("TotalFee")?jsonObj.getString("TotalFee"):"");
		result.add(jsonObj.has("TestFee")?jsonObj.getString("TestFee"):"");
		result.add(jsonObj.has("RepairFee")?jsonObj.getString("RepairFee"):"");
		result.add(jsonObj.has("MaterialFee")?jsonObj.getString("MaterialFee"):"");
		result.add(jsonObj.has("CarFee")?jsonObj.getString("CarFee"):"");
		result.add(jsonObj.has("DebugFee")?jsonObj.getString("DebugFee"):"");
		result.add(jsonObj.has("OtherFee")?jsonObj.getString("OtherFee"):"");
		
		return result;
	}
	
	
	/**
	 * ֤��title
	 * @return
	 */
	public List<String> Certificate_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("֤���");
		result.add("ί�е���");
		result.add("ί�е�λ");
		result.add("ί�е�λ��ַ");
		result.add("ί�е�λ�ʱ�");
		result.add("ί�е�λ�绰");
		result.add("ί�е�λ��ϵ��");
		result.add("��������");
		result.add("������");
		result.add("�������");
		result.add("�ͺŹ��");
		result.add("̨����");
		result.add("֤����Ч��");
		result.add("�ܷ���");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("��ͨ��");
		result.add("���Է�");
		result.add("��������");
		
		return result;
	}
	
	/**
	 * ����ҵ���ѯ���excel
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> Mission_formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		//result.add(jsonObj.has("Code")?jsonObj.getString("Code"):"");
		result.add(jsonObj.has("Code")?jsonObj.getString("Code"):"");
		result.add(jsonObj.has("CustomerName")?jsonObj.getString("CustomerName"):"");
		result.add(jsonObj.has("CommissionDate")?jsonObj.getString("CommissionDate"):"");
		
		result.add(jsonObj.has("CustomerAddress")?jsonObj.getString("CustomerAddress"):"");
		result.add(jsonObj.has("CustomerTel")?jsonObj.getString("CustomerTel"):"");
		result.add(jsonObj.has("CustomerZipCode")?jsonObj.getString("CustomerZipCode"):"");
		result.add(jsonObj.has("CustomerContactor")?jsonObj.getString("CustomerContactor"):"");
		result.add(jsonObj.has("CustomerContactorTel")?jsonObj.getString("CustomerContactorTel"):"");
		
		result.add(jsonObj.has("ApplianceName")?jsonObj.getString("ApplianceName"):"");
		result.add(jsonObj.has("AppManageCode")?jsonObj.getString("AppManageCode"):"");
		result.add(jsonObj.has("ApplianceCode")?jsonObj.getString("ApplianceCode"):"");
		result.add(jsonObj.has("Model")?jsonObj.getString("Model"):"");
		result.add(jsonObj.has("Quantity")?jsonObj.getString("Quantity"):"");
		result.add(jsonObj.has("WithdrawQuantity")?jsonObj.getString("WithdrawQuantity"):"");
		result.add(jsonObj.has("TotalFee")?jsonObj.getString("TotalFee"):"");
		result.add(jsonObj.has("TestFee")?jsonObj.getString("TestFee"):"");
		result.add(jsonObj.has("RepairFee")?jsonObj.getString("RepairFee"):"");
		result.add(jsonObj.has("MaterialFee")?jsonObj.getString("MaterialFee"):"");
		result.add(jsonObj.has("CarFee")?jsonObj.getString("CarFee"):"");
		result.add(jsonObj.has("DebugFee")?jsonObj.getString("DebugFee"):"");
		result.add(jsonObj.has("OtherFee")?jsonObj.getString("OtherFee"):"");
		
		result.add(jsonObj.has("LocaleCommissionCode")?jsonObj.getString("LocaleCommissionCode"):"");
		result.add(jsonObj.has("LocaleStaff")?jsonObj.getString("LocaleStaff"):"");
		
		result.add((jsonObj.has("MandatoryInspection")&&!jsonObj.getString("MandatoryInspection").equals(""))?CommissionSheetFlagUtil.getMandatoryByFlag((jsonObj.getString("WithdrawQuantity").equals("1")?true:false)):"");
		result.add((jsonObj.has("Urgent")&&!jsonObj.getString("Urgent").equals(""))?CommissionSheetFlagUtil.getUrgentByFlag((jsonObj.getString("Urgent").equals("1")?true:false)):"");
		result.add((jsonObj.has("Trans")&&!jsonObj.getString("Trans").equals(""))?CommissionSheetFlagUtil.getSubcontractByFlag((jsonObj.getString("Trans").equals("1")?true:false)):"");
		
		result.add(jsonObj.has("SubContractor")?jsonObj.getString("SubContractor"):"");
		result.add((jsonObj.has("ReportType")&&!jsonObj.getString("ReportType").equals(""))?CommissionSheetFlagUtil.getReportTypeByFlag(Integer.parseInt(jsonObj.getString("ReportType"))):"");
		result.add((jsonObj.has("Status")&&!jsonObj.getString("Status").equals(""))?CommissionSheetStatus.getStatusString(Integer.parseInt(jsonObj.getString("Status"))):"");
		result.add(jsonObj.has("Allotee")?jsonObj.getString("Allotee"):"");
		result.add(jsonObj.has("FinishLocation")?jsonObj.getString("FinishLocation"):"");
		result.add(jsonObj.has("Location")?jsonObj.getString("Location"):"");
		
		return result;
	}
	
	
	/**
	 * ҵ���ѯ���title
	 * @return
	 */
	public List<String> Mission_formatTitle(){
		List<String> result = new ArrayList<String>();
		//result.add("֤���");
		result.add("ί�е���");
		result.add("ί�е�λ");
		result.add("ί������");
		
		result.add("��λ��ַ");
		result.add("��λ�绰");
		result.add("�ʱ�");
		result.add("��ϵ��");
		result.add("��ϵ�˵绰");
		
		result.add("��������");
		result.add("������");
		result.add("�������");
		result.add("�ͺŹ��");
		result.add("̨����");
		result.add("����̨����");
		result.add("�ܷ���");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("��ͨ��");
		result.add("���Է�");
		result.add("��������");
		
		result.add("�ֳ��������");
		result.add("�ֳ�������");
		
		result.add("�Ƿ�ǿ��");
		result.add("�Ƿ�Ӽ�");
		result.add("�Ƿ�ת��");
		
		result.add("ת����");
		result.add("������ʽ");
		result.add("ί�е�״̬");
		result.add("�ɶ���");
		result.add("�깤���λ��");
		result.add("���λ��");
		
		return result;
	}
	
	/**
	 * ͳ����Ϣexcel
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> TransactionCustomerSUM_formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.getString("CustomerName"));
		result.add(jsonObj.getString("TotalFee"));
		result.add(jsonObj.getString("TestFee"));
		result.add(jsonObj.getString("MaterialFee"));
		result.add(jsonObj.getString("CarFee"));
		result.add(jsonObj.getString("RepairFee"));
		result.add(jsonObj.getString("DebugFee"));
		result.add(jsonObj.getString("OtherFee"));
		
		return result;
	}
	
	/**
	 * ͳ����Ϣtitle
	 * @return
	 */
	public List<String> TransactionCustomerSUM_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("ί�е�λ");
		result.add("�ܷ���");
		result.add("����");
		result.add("���Ϸ�");
		result.add("��ͨ��");
		result.add("�����");
		result.add("���Է�");
		result.add("��������");
		
		return result;
	}
	
	/**
	 * ҵ����ͳ����Ϣexcel
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> Transaction_formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.getString("Code"));
		result.add(jsonObj.getString("CommissionDate"));
		result.add(jsonObj.getString("ApplianceName"));
		result.add(jsonObj.getString("Allotee"));
		result.add(jsonObj.getString("CustomerName"));
		result.add(jsonObj.getString("TotalFee"));
		result.add(jsonObj.getString("TestFee"));
		result.add(jsonObj.getString("MaterialFee"));
		result.add(jsonObj.getString("CarFee"));
		result.add(jsonObj.getString("RepairFee"));
		result.add(jsonObj.getString("DebugFee"));
		result.add(jsonObj.getString("OtherFee"));
		
		return result;
	}
	
	/**
	 * ҵ����ͳ����Ϣtitle
	 * @return
	 */
	public List<String> Transaction_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("ί�е���");
		result.add("ί������");
		result.add("��������");
		result.add("������");
		result.add("ί�е�λ");
		result.add("�ܷ���");
		result.add("����");
		result.add("���Ϸ�");
		result.add("��ͨ��");
		result.add("�����");
		result.add("���Է�");
		result.add("��������");
		
		return result;
	}
	
}
