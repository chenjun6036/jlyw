package com.jlyw.util;

public class CommissionSheetFlagUtil {
	
	public static final int CommissionTypeCount = 7;//ί����ʽ����
	
	/**
	 * ί����ʽ��־λ��������˼
	 * @param flag
	 * @return
	 */
	public static String getCommissionTypeByFlag(int flag){
		switch(flag){
		case 1:
			return "�������";
		case 2:
			return "�ֳ����";
		case 3:
			return "��������";
		case 4:
			return "��ʽ����";
		case 5:
			return "����ҵ��";
		case 6:
			return "�Լ�ҵ��";
		case 7:
			return "�ֳ�����";
		default:
			return "";
		}
	}
	
	/**
	 * ������ʽ��־λ��������˼
	 * @param flag
	 * @return
	 */
	public static String getReportTypeByFlag(int flag){
		switch(flag){
		case 1:
			return "�춨";
		case 2:
			return "У׼";
		case 3:
			return "���";
		case 4:
			return "����";
		default:
			return "";
		}
	}
	
	/**
	 * �Ƿ�ǿ���־λ��������˼
	 * @param flag
	 * @return
	 */
	public static String getMandatoryByFlag(boolean flag){
		if(flag){	//��־λΪ1:��ǿ�Ƽ춨
			return "��ǿ�Ƽ춨";
		}else{
			return "ǿ�Ƽ춨";
		}
	}
	
	/**
	 * �Ƿ�Ӽ���־λ��������˼
	 * @param flag
	 * @return
	 */
	public static String getUrgentByFlag(boolean flag){
		if(flag){	//��־λΪ1:���Ӽ�
			return "��";
		}else{
			return "��";
		}
	}
	/**
	 * �Ƿ���Ҫ�����־λ��������˼
	 * @param flag
	 * @return
	 */
	public static String getRepairByFlag(boolean flag){
		if(flag){	//��־λΪ1:��������
			return "��������";
		}else{
			return "��Ҫ����";
		}
	}
	/**
	 * �Ƿ�ת����־λ��������˼
	 * @param flag
	 * @return
	 */
	public static String getSubcontractByFlag(boolean flag){
		if(flag){	//��־λΪ1:��ת��
			return "��";
		}else{
			return "��";
		}
	}
}
