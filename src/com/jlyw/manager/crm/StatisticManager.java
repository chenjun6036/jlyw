/**
 * 
 */
package com.jlyw.manager.crm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.crm.CustomerCareness;
import com.sun.jndi.url.ldaps.ldapsURLContextFactory;

/**
 * @author xx
 *
 */
public class StatisticManager {
	public List<String> formateExcelOfFullYear(Object obj) {
		List<String> result=new ArrayList<String>();
		JSONObject t=(JSONObject)obj;
		try 
		{
			result.add(t.getString("Code")==null?"":t.getString("Code"));
			result.add(t.getString("Pwd")==null?"":t.getString("Pwd"));
			result.add(t.getString("CustomerName")==null?"":t.getString("CustomerName"));
			result.add(t.getString("CommissionDate")==null?"":t.getString("CommissionDate"));
			result.add(t.getString("ApplianceName")==null?"":t.getString("ApplianceName"));
			result.add(t.getString("ApplianceModel")==null?"":t.getString("ApplianceModel"));
			result.add(t.getString("ApplianceSpeciesName")==null?"":t.getString("ApplianceSpeciesName"));
			result.add(t.getString("ApplianceSpeciesNameStatus")==null?"":t.getString("ApplianceSpeciesNameStatus"));
			result.add(t.getString("ApplianceCode")==null?"":t.getString("ApplianceCode"));
			result.add(t.getString("AppManageCode")==null?"":t.getString("AppManageCode"));
			result.add(t.getString("Model")==null?"":t.getString("Model"));
			result.add(t.getString("Range")==null?"":t.getString("Range"));
			result.add(t.getString("Accuracy")==null?"":t.getString("Accuracy"));
			result.add(t.getString("Manufacturer")==null?"":t.getString("Manufacturer"));
			result.add(t.getString("Quantity")==null?"":t.getString("Quantity"));
			result.add(t.getString("MandatoryInspection")==null?"":t.getString("MandatoryInspection"));
			result.add(t.getString("Urgent")==null?"":t.getString("Urgent"));
			result.add(t.getString("Trans")==null?"":t.getString("Trans"));
			result.add(t.getString("SubContractor")==null?"":t.getString("SubContractor"));
			result.add(t.getString("Appearance")==null?"":t.getString("Appearance"));
			result.add(t.getString("Repair")==null?"":t.getString("Repair"));
			result.add(t.getString("ReportType")==null?"":t.getString("ReportType"));
			result.add(t.getString("OtherRequirements")==null?"":t.getString("OtherRequirements"));
			result.add(t.getString("Location")==null?"":t.getString("Location"));
			result.add(t.getString("Allotee")==null?"":t.getString("Allotee"));
			result.add(t.getString("Status")==null?"":t.getString("Status"));
			result.add(t.getString("TestFee")==null?"":t.getString("TestFee"));
			result.add(t.getString("RepairFee")==null?"":t.getString("RepairFee"));
			result.add(t.getString("MaterialFee")==null?"":t.getString("MaterialFee"));
			result.add(t.getString("CarFee")==null?"":t.getString("CarFee"));
			result.add(t.getString("DebugFee")==null?"":t.getString("DebugFee"));
			result.add(t.getString("OtherFee")==null?"":t.getString("OtherFee"));
			result.add(t.getString("TotalFee")==null?"":t.getString("TotalFee"));
			result.add(t.getString("WithdrawQuantity")==null?"":t.getString("WithdrawQuantity"));
			result.add(t.getString("FinishQuantity")==null?"":t.getString("FinishQuantity"));
			result.add(t.getString("EffectQuantity")==null?"":t.getString("EffectQuantity"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> formateTitleOfFullYear() {
		List<String> result=new ArrayList<String>();
		result.add("委托单号");
		result.add("密码");
		result.add("委托单位");
		result.add("委托日期");
		result.add("器具名称");
		result.add("器具型号");
		result.add("器具分类名称");
		result.add("器具分类名称状态");
		result.add("出厂编号");
		result.add("管理编号");
		result.add("型号规格");
		result.add("测量范围");
		result.add("精度等级");
		result.add("制造厂商");
		result.add("台/件数");
		result.add("强制检验");
		result.add("是否加急");
		result.add("是否转包");
		result.add("转包方");
		result.add("外观附件");
		result.add("修理");
		result.add("报告形式");
		result.add("其它要求");
		result.add("存放位置");
		result.add("派定人");
		result.add("委托单状态");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("车费");
		result.add("调试费");
		result.add("其它费");
		result.add("总费用");
		result.add("退样器具数量");
		result.add("完工数量");
		result.add("有效器具数");
		//result.add("");
		return result;
	}
	
	public List<String> formateTitleOfLeast() {
		List<String> result=new ArrayList<String>();
		
		result.add("委托单位");
		result.add("委托日期(起)");
		result.add("委托日期(止)");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("车费");
		result.add("调试费");
		result.add("其它费");
		result.add("总费用");
		result.add("委托单个数");
		result.add("平均费用");
		return result;
	}
	public List<String> formateExcelOfLeast(Object obj) {
		List<String> result=new ArrayList<String>();
		Object[] tmp=(Object[]) obj;
		result.add(tmp[1]==null?"":tmp[1].toString());
		result.add(tmp[3]==null?"":tmp[3].toString());
		result.add(tmp[4]==null?"":tmp[4].toString());
		result.add(tmp[7]==null?"":tmp[7].toString());
		result.add(tmp[8]==null?"":tmp[8].toString());
		result.add(tmp[9]==null?"":tmp[9].toString());
		result.add(tmp[11]==null?"":tmp[11].toString());
		result.add(tmp[10]==null?"":tmp[10].toString());
		result.add(tmp[12]==null?"":tmp[12].toString());
		result.add(tmp[2]==null?"":tmp[2].toString());
		result.add(tmp[5]==null?"":tmp[5].toString());
		result.add(tmp[6]==null?"":String.format("%.2f", tmp[6]));
		return result;
	}
	
	public List<String> formateTitleOfRatio () {
		List<String> result=new ArrayList<String>();
		
		result.add("委托单位");
		result.add("委托单个数(上年)");
		result.add("平均费用(上年)");
		result.add("总费用(上年)");
		result.add("检测费(上年)");
		result.add("修理费(上年)");
		result.add("材料费(上年)");
		result.add("车费(上年)");
		result.add("调试费(上年)");
		result.add("其它费(上年)");
		
		result.add("委托单个数(当年)");
		result.add("平均费用(当年)");
		result.add("总费用(当年)");
		result.add("检测费(当年)");
		result.add("修理费(当年)");
		result.add("材料费(当年)");
		result.add("车费(当年)");
		result.add("调试费(当年)");
		result.add("其它费(当年)");
		result.add("增长额");
		result.add("增长率");
		return result;
	}
	public List<String> formateExcelOfRatio (ResultSet obj) {
		List<String> result=new ArrayList<String>();
		try {
			result.add(obj.getString(2)==null?"":obj.getString(2));
			result.add(obj.getObject(20)==null?"":String.format("%s",obj.getInt(20)));
			result.add(obj.getObject(9)==null?"":String.format("%.2f",obj.getDouble(9)));
			result.add(obj.getObject(10)==null?"":String.format("%.2f",obj.getDouble(10)));
			result.add(obj.getObject(3)==null?"":String.format("%.2f",obj.getDouble(3)));
			result.add(obj.getObject(7)==null?"":String.format("%.2f",obj.getDouble(7)));
			result.add(obj.getObject(5)==null?"":String.format("%.2f",obj.getDouble(5)));
			result.add(obj.getObject(6)==null?"":String.format("%.2f",obj.getDouble(6)));
			result.add(obj.getObject(4)==null?"":String.format("%.2f",obj.getDouble(4)));
			result.add(obj.getObject(8)==null?"":String.format("%.2f",obj.getDouble(8)));
			
			result.add(obj.getObject(21)==null?"":String.format("%s",obj.getInt(21)));
			result.add(obj.getObject(17)==null?"":String.format("%.2f",obj.getDouble(17)));
			result.add(obj.getObject(18)==null?"":String.format("%.2f",obj.getDouble(18)));
			result.add(obj.getObject(11)==null?"":String.format("%.2f",obj.getDouble(11)));
			result.add(obj.getObject(15)==null?"":String.format("%.2f",obj.getDouble(15)));
			result.add(obj.getObject(13)==null?"":String.format("%.2f",obj.getDouble(13)));
			result.add(obj.getObject(14)==null?"":String.format("%.2f",obj.getDouble(14)));
			result.add(obj.getObject(12)==null?"":String.format("%.2f",obj.getDouble(12)));
			result.add(obj.getObject(16)==null?"":String.format("%.2f",obj.getDouble(16)));
			result.add(obj.getObject(19)==null?"":String.format("%.2f",obj.getDouble(19)));
			
			Double db=0.0;
			if(obj.getDouble(10)>0.0)db=new Double(obj.getDouble(19)/obj.getDouble(10))*100;

			result.add(obj.getObject(10)==null?"-":String.format("%.2f",db)+"%");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> formateTitleOf1LevelClassify() {
		List<String> result=new ArrayList<String>();
		
		result.add("器具名称");
		result.add("器具编号");
		result.add("平均费用");
		result.add("总费用");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("调试费");
		result.add("交通费");
		result.add("其它费");
		return result;
	}
	public List<String> formateExcelOf1LevelClassify(ResultSet obj) {
		List<String> result=new ArrayList<String>();
		try {
			result.add(obj.getString(12)==null?"":obj.getString(12));
			result.add(obj.getObject(10)==null?"":String.format("%s",obj.getInt(10)));
			result.add(obj.getObject(8)==null?"":String.format("%.2f",obj.getDouble(8)));
			result.add(obj.getObject(1)==null?"":String.format("%.2f",obj.getDouble(1)));
			result.add(obj.getObject(2)==null?"":String.format("%.2f",obj.getDouble(2)));
			result.add(obj.getObject(6)==null?"":String.format("%.2f",obj.getDouble(6)));
			result.add(obj.getObject(4)==null?"":String.format("%.2f",obj.getDouble(4)));
			result.add(obj.getObject(3)==null?"":String.format("%.2f",obj.getDouble(3)));
			result.add(obj.getObject(5)==null?"":String.format("%.2f",obj.getDouble(5)));
			result.add(obj.getObject(7)==null?"":String.format("%.2f",obj.getDouble(7)));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> formateTitleOfTopLevel() {
		List<String> result=new ArrayList<String>();
		result.add("分类编号");
		result.add("分类名称");
		result.add("总费用");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("调试费");
		result.add("交通费");
		result.add("其它费");
		return result;
	}
	public List<String> formateExcelOfTopLevel(Object obj) {
		List<String> result=new ArrayList<String>();
		JSONObject tmp=(JSONObject)obj;
		try {
			result.add(tmp.getString("Id")==null?"":tmp.getString("Id"));
			result.add(tmp.getString("Name")==null?"":tmp.getString("Name"));
			result.add(tmp.getString("TotalFee")==null?"":tmp.getString("TotalFee"));
			result.add(tmp.getString("TestFee")==null?"":tmp.getString("TestFee"));
			result.add(tmp.getString("RepairFee")==null?"":tmp.getString("RepairFee"));
			result.add(tmp.getString("MaterialFee")==null?"":tmp.getString("MaterialFee"));
			result.add(tmp.getString("DebugFee")==null?"":tmp.getString("DebugFee"));
			result.add(tmp.getString("CarFee")==null?"":tmp.getString("CarFee"));
			result.add(tmp.getString("OtherFee")==null?"":tmp.getString("OtherFee"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> formateTitleOfYearAll() {
		List<String> result=new ArrayList<String>();
		result.add("年份");
		result.add("月份");
		result.add("委托单个数");
		result.add("平均费用");
		result.add("总费用");
		result.add("检测费");
		result.add("修理费");
		result.add("材料费");
		result.add("调试费");
		result.add("交通费");
		result.add("其它费");
		return result;
	}
	public List<String> formateExcelOfYearAll(ResultSet obj) {
		List<String> result=new ArrayList<String>();
		try {
			result.add(obj.getObject(1)==null?"":String.format("%s",obj.getInt(1)));
			result.add(obj.getObject(2)==null?"":String.format("%s",obj.getInt(2)));
			result.add(obj.getObject(3)==null?"":String.format("%s",obj.getInt(3)));
			result.add(obj.getObject(11)==null?"":String.format("%.2f",obj.getDouble(11)));
			result.add(obj.getObject(4)==null?"":String.format("%.2f",obj.getDouble(4)));
			result.add(obj.getObject(5)==null?"":String.format("%.2f",obj.getDouble(5)));
			result.add(obj.getObject(9)==null?"":String.format("%.2f",obj.getDouble(9)));
			result.add(obj.getObject(7)==null?"":String.format("%.2f",obj.getDouble(7)));
			result.add(obj.getObject(6)==null?"":String.format("%.2f",obj.getDouble(6)));
			result.add(obj.getObject(8)==null?"":String.format("%.2f",obj.getDouble(8)));
			result.add(obj.getObject(10)==null?"":String.format("%.2f",obj.getDouble(10)));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> formatTitleOfInsideContactorFee() {
		List<String> result=new ArrayList<String>();
		result.add("委托单位");
		result.add("联系人");
		result.add("分配年份");
		result.add("分配额度");
		result.add("最后编辑日期");
		result.add("最后编辑人");
		result.add("备注");
		return result;
	}
	public List<String> formatExcelOfInsideContactorFee(Object obj) {
		List<String> result=new ArrayList<String>();
		Object []tmp=(Object[])obj;
		result.add(tmp[0]==null?"":tmp[0].toString());
		result.add(tmp[1]==null?"":tmp[1].toString());
		result.add(tmp[2]==null?"":tmp[2].toString());
		result.add(tmp[3]==null?"":tmp[3].toString());
		result.add(tmp[4]==null?"":tmp[4].toString());
		result.add(tmp[5]==null?"":tmp[5].toString());
		result.add(tmp[6]==null?"":tmp[6].toString());
		return result;
	}
	
	public List<String> formatTitleOfCareness() {
		List<String> result=new ArrayList<String>();
		result.add("关怀单位");
		result.add("优先级");
		result.add("关怀时间");
		result.add("创建者");
		result.add("创建时间");
		result.add("关怀方式");
		result.add("关怀负责人");
		result.add("关怀联系人");
		result.add("状态");
		result.add("备注");
		
		return result;
	}
	public List<String> formatExcelOfCareness(Object obj) {
		List<String> result=new ArrayList<String>();
		CustomerCareness tmp=(CustomerCareness)obj;
		result.add(tmp.getCustomer()==null?"":tmp.getCustomer().getName());
		Integer p=tmp.getPriority();
		if(p!=null)
		{
			int pp=p;
			switch (pp) {
			case 0:
				result.add("低");
				break;
			case 1:
				result.add("中");
				break;
			case 2:
				result.add("高");
				break;
			default:
				result.add("/");
				break;
			}
		}
		else result.add("/");
		result.add(tmp.getTime()==null?"":tmp.getTime().toString());
		result.add(tmp.getSysUserByCreateSysUserId()==null?"":tmp.getSysUserByCreateSysUserId().getName());
		result.add(tmp.getCreateTime()==null?"":tmp.getCreateTime().toString());
		Integer w=tmp.getWay();
		if(w!=null)
		{
			int ww=w;
			switch (ww) {
			case 0:
				result.add("电话");
				break;
			case 1:
				result.add("短信");
				break;
			case 2:
				result.add("礼品赠送");
				break;
			case 3:
				result.add("宴请");
				break;
			case 4:
				result.add("其它");
				break;
			default:
				result.add("/");
	
				break;
			}
		}
		else result.add("/");
		result.add(tmp.getSysUserByCareDutySysUserId()==null?"":tmp.getSysUserByCareDutySysUserId().getName());
		result.add(tmp.getCareContactor()==null?"":tmp.getCareContactor());
		Integer s=tmp.getStatus();
		if(s!=null)
		{
			int ss=s;
			switch (ss) {
			case 0:
				result.add("未处理");
				break;
			case 1:
				result.add("进行中");
				break;
			case 2:
				result.add("已处理");
				break;
			case 3:
				result.add("废弃");
				break;
			default:
				result.add("/");
				break;
			}
		}
		else result.add("/");
		result.add(tmp.getRemark()==null?"":tmp.getRemark());
		return result;
	}
	
	public List<String> formatTitleOfCustomerLevel() {
		List<String> result=new ArrayList<String>();
		result.add("编号");
		result.add("单位名称");
		result.add("总产值");
		result.add("平均产值");
		result.add("业务次数");
		result.add("所占百分比");
		result.add("价值等级");
		
		
		return result;
	}
	public List<String> formatExcelOfCustomerLevel(ResultSet obj) {
		List<String> result=new ArrayList<String>();
		try {
			result.add(obj.getObject(1)==null?"":String.format("%s",obj.getInt(1)));
			result.add(obj.getObject(2)==null?"":obj.getString(2));
			result.add(obj.getObject(3)==null?"":String.format("%.2f",obj.getDouble(3)));
			result.add(obj.getObject(4)==null?"":String.format("%.2f", obj.getDouble(4)));
			result.add(obj.getObject(5)==null?"":String.format("%s", obj.getInt(5)));
			result.add(obj.getObject(6)==null?"":String.format("%.4f", obj.getDouble(6)*100)+"%");
			result.add(obj.getObject(7)==null?"":String.format("%s",obj.getInt(7)));
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
}
