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
	 * 导出收发室业务统计结果excel内容
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
	 * 导出收发室业务统计结果title
	 * @return
	 */
	public List<String> SFSMission_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("员工");
		result.add("委托形式");
		result.add("出单数");
		result.add("出单台件数");
		result.add("完工单数");
		result.add("完工台件数");
		result.add("结账单数");
		result.add("结账台件数");
		result.add("注销单数");
		result.add("注销台件数");
		result.add("退样单数");
		result.add("退样台件数");
		
		return result;
	}
	
	/**
	 * 导出产值明细excel
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
	 * 产值明细title
	 * @return
	 */
	public List<String> Detail_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("委托单号");
		result.add("委托日期");
		result.add("委托单位名称");
		result.add("器具名称");
		result.add("型号规格");
		result.add("测量范围");
		result.add("不确定度/准确度等级/最大允差");
		result.add("制造厂商");
		result.add("台件数");
		result.add("完工台件数");
		result.add("退样台件数");
		result.add("证书号");
		result.add("产值");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("交通费");
		result.add("调试费");
		result.add("其他费用");
		result.add("结账人");
		result.add("结账日期");
		result.add("结账清单号");
		result.add("产值人");
		
		return result;
	}
	
	/**
	 * 产值统计信息excel
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
	 * 产值统计信息title
	 * @return
	 */
	public List<String> OutPut_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("部门代码");
		result.add("部门名称");
		result.add("员工工号");
		result.add("员工名称");
		result.add("产值");
		result.add("检测台件数");
		result.add("退样台件数");
		result.add("证书数量");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("交通费");
		result.add("调试费");
		result.add("其他费用");
		
		return result;
	}
	
	/**
	 * 产值统计信息excel
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
	 * 产值统计信息title
	 * @return
	 */
	public List<String> DetailList__formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("工号");
		result.add("结账员");
		result.add("结账清单号");
		result.add("现金");
		result.add("支票");
		result.add("账户预付款");
		result.add("总计费用");
		result.add("已付费用");
		result.add("支票号");
		result.add("结账时间");
		result.add("委托单号");
		result.add("委托时间");
		result.add("委托单位");
		result.add("器具名称");
		result.add("型号规格");
		result.add("测量范围");
		result.add("准确度/不确定度/最大允差");
		result.add("送检台件数");
		result.add("退样台件数");
		result.add("总费用");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("交通费");
		result.add("调试费");
		result.add("其他费用");
		
		return result;
	}
	
	/**
	 * 导出证书excel
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
	 * 证书title
	 * @return
	 */
	public List<String> Certificate_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("证书号");
		result.add("委托单号");
		result.add("委托单位");
		result.add("委托单位地址");
		result.add("委托单位邮编");
		result.add("委托单位电话");
		result.add("委托单位联系人");
		result.add("器具名称");
		result.add("管理编号");
		result.add("出厂编号");
		result.add("型号规格");
		result.add("台件数");
		result.add("证书有效期");
		result.add("总费用");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("交通费");
		result.add("调试费");
		result.add("其他费用");
		
		return result;
	}
	
	/**
	 * 导出业务查询结果excel
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
	 * 业务查询结果title
	 * @return
	 */
	public List<String> Mission_formatTitle(){
		List<String> result = new ArrayList<String>();
		//result.add("证书号");
		result.add("委托单号");
		result.add("委托单位");
		result.add("委托日期");
		
		result.add("单位地址");
		result.add("单位电话");
		result.add("邮编");
		result.add("联系人");
		result.add("联系人电话");
		
		result.add("器具名称");
		result.add("管理编号");
		result.add("出厂编号");
		result.add("型号规格");
		result.add("台件数");
		result.add("退样台件数");
		result.add("总费用");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("交通费");
		result.add("调试费");
		result.add("其他费用");
		
		result.add("现场任务书号");
		result.add("现场负责人");
		
		result.add("是否强检");
		result.add("是否加急");
		result.add("是否转包");
		
		result.add("转包方");
		result.add("报告形式");
		result.add("委托单状态");
		result.add("派定人");
		result.add("完工存放位置");
		result.add("存放位置");
		
		return result;
	}
	
	/**
	 * 统计信息excel
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
	 * 统计信息title
	 * @return
	 */
	public List<String> TransactionCustomerSUM_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("委托单位");
		result.add("总费用");
		result.add("检测费");
		result.add("材料费");
		result.add("交通费");
		result.add("修理费");
		result.add("调试费");
		result.add("其他费用");
		
		return result;
	}
	
	/**
	 * 业务量统计信息excel
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
	 * 业务量统计信息title
	 * @return
	 */
	public List<String> Transaction_formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("委托单号");
		result.add("委托日期");
		result.add("器具名称");
		result.add("分配人");
		result.add("委托单位");
		result.add("总费用");
		result.add("检测费");
		result.add("材料费");
		result.add("交通费");
		result.add("修理费");
		result.add("调试费");
		result.add("其他费用");
		
		return result;
	}
	
}
