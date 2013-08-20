package com.jlyw.util;

/**
 * ϵͳ��һЩ�����ַ�����������
 * @author zhan
 *
 */
public class SysStringUtil {
	/**
	 * ��̨����ǰ̨������Ϣ���ص�ContentType������
	 * @author zhan
	 *
	 */
	public static class ResponseContentType{
		public static final String Type_FormSubmit = "text/html;charset=utf-8";	//Form�ύʱ������Ϣ��ContentType����response.setContentType("text/html;charset=utf-8");
		public static final String Type_AjaxSubmit = "text/json;charset=utf-8";	//Ajax�ύ����ǰ̨Ajax��Ҫ���÷��صĸ�ʽΪjson
		public static final String Type_EasyUIDatagrid = "text/json;charset=utf-8";	//ʹ��easyui datagrid��������
		public static final String Type_EasyUICombobox = "text/json;charset=gbk";	//ʹ��easyui combobox��������
	}
	
	public static class ReasonString {
		public static final String Reason_Discount_LocalMission = "���ֳ������Э��۸�";	//����ֳ�������е�����ϵͳ�Զ������ۿ۵�ԭ������
		public static final String Reason_Discount_Deal = "���Ѱ�Э��۸���д��ۡ�";	//���Э���е�����ϵͳ�Զ������ۿ۵�ԭ������
	}
}
