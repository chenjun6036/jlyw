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
		result.add("ί�е���");
		result.add("����");
		result.add("ί�е�λ");
		result.add("ί������");
		result.add("��������");
		result.add("�����ͺ�");
		result.add("���߷�������");
		result.add("���߷�������״̬");
		result.add("�������");
		result.add("������");
		result.add("�ͺŹ��");
		result.add("������Χ");
		result.add("���ȵȼ�");
		result.add("���쳧��");
		result.add("̨/����");
		result.add("ǿ�Ƽ���");
		result.add("�Ƿ�Ӽ�");
		result.add("�Ƿ�ת��");
		result.add("ת����");
		result.add("��۸���");
		result.add("����");
		result.add("������ʽ");
		result.add("����Ҫ��");
		result.add("���λ��");
		result.add("�ɶ���");
		result.add("ί�е�״̬");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("����");
		result.add("���Է�");
		result.add("������");
		result.add("�ܷ���");
		result.add("������������");
		result.add("�깤����");
		result.add("��Ч������");
		//result.add("");
		return result;
	}
	
	public List<String> formateTitleOfLeast() {
		List<String> result=new ArrayList<String>();
		
		result.add("ί�е�λ");
		result.add("ί������(��)");
		result.add("ί������(ֹ)");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("����");
		result.add("���Է�");
		result.add("������");
		result.add("�ܷ���");
		result.add("ί�е�����");
		result.add("ƽ������");
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
		
		result.add("ί�е�λ");
		result.add("ί�е�����(����)");
		result.add("ƽ������(����)");
		result.add("�ܷ���(����)");
		result.add("����(����)");
		result.add("�����(����)");
		result.add("���Ϸ�(����)");
		result.add("����(����)");
		result.add("���Է�(����)");
		result.add("������(����)");
		
		result.add("ί�е�����(����)");
		result.add("ƽ������(����)");
		result.add("�ܷ���(����)");
		result.add("����(����)");
		result.add("�����(����)");
		result.add("���Ϸ�(����)");
		result.add("����(����)");
		result.add("���Է�(����)");
		result.add("������(����)");
		result.add("������");
		result.add("������");
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
		
		result.add("��������");
		result.add("���߱��");
		result.add("ƽ������");
		result.add("�ܷ���");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("���Է�");
		result.add("��ͨ��");
		result.add("������");
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
		result.add("������");
		result.add("��������");
		result.add("�ܷ���");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("���Է�");
		result.add("��ͨ��");
		result.add("������");
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
		result.add("���");
		result.add("�·�");
		result.add("ί�е�����");
		result.add("ƽ������");
		result.add("�ܷ���");
		result.add("����");
		result.add("�����");
		result.add("���Ϸ�");
		result.add("���Է�");
		result.add("��ͨ��");
		result.add("������");
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
		result.add("ί�е�λ");
		result.add("��ϵ��");
		result.add("�������");
		result.add("������");
		result.add("���༭����");
		result.add("���༭��");
		result.add("��ע");
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
		result.add("�ػ���λ");
		result.add("���ȼ�");
		result.add("�ػ�ʱ��");
		result.add("������");
		result.add("����ʱ��");
		result.add("�ػ���ʽ");
		result.add("�ػ�������");
		result.add("�ػ���ϵ��");
		result.add("״̬");
		result.add("��ע");
		
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
				result.add("��");
				break;
			case 1:
				result.add("��");
				break;
			case 2:
				result.add("��");
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
				result.add("�绰");
				break;
			case 1:
				result.add("����");
				break;
			case 2:
				result.add("��Ʒ����");
				break;
			case 3:
				result.add("����");
				break;
			case 4:
				result.add("����");
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
				result.add("δ����");
				break;
			case 1:
				result.add("������");
				break;
			case 2:
				result.add("�Ѵ���");
				break;
			case 3:
				result.add("����");
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
		result.add("���");
		result.add("��λ����");
		result.add("�ܲ�ֵ");
		result.add("ƽ����ֵ");
		result.add("ҵ�����");
		result.add("��ռ�ٷֱ�");
		result.add("��ֵ�ȼ�");
		
		
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
