package com.jlyw.util;

import java.lang.reflect.Field;

import org.json.me.JSONArray;

import com.jlyw.hibernate.Certificate;
import com.thoughtworks.xstream.core.util.Fields;

/**
 * ��ʶ����ϵͳ��־λ
 * @author Administrator
 *
 */
public class FlagUtil {
	/**
	 * ԭ������
	 * @author Zhan
	 *
	 */
	public class ReasonType{
		public static final int Type_Withdraw = 11;	//����
		public static final int Type_Overdue = 12;	//��������
		public static final int Type_Discount = 13;	//�ۿ�����
		public static final int Type_CancelCommissionSheet = 14;  //ע��ί�е�
		public static final int Type_CancelSubcontractor = 21;	//ע��ת����
		public static final int Type_CancelCustomer = 22; //ע��ί�е�λ
		
	}
	
	public class QualificationType{	//�����Ա��������
		public static final int Type_Jianding = 11;	//�춨
		public static final int Type_Jiaozhun = 12;	//У׼
		public static final int Type_Jianyan = 13;	//����
		public static final int Type_Heyan = 14;	//����
		public static final int Type_Qianzi = 15; //��Ȩǩ��
		public static final int Type_Except = 16; //��������
	}
	
	public static class BaseTypeInfoType{  //������Ϣ����
		public static final int Type_CustomerType = 11;  //��ҵ���࣬û���������б���һ�£���Ӧ��Classification
		public static final int Type_TestLogTester = 12; //�춨��λ
		public static final int Type_SpecificationType = 13; //��̹淶���
		public static final int Type_StandardIssuedBy = 14;  //������׼��֤����
		public static final int Type_StandardSIssuedBy = 15; //������׼���֤��֤����
		public static final int Type_UserPoliticsStatus = 16; //Ա��������ò
		public static final int Type_UserType = 17;    //��Ա����
		public static final int Type_UserJobTitle = 18;   //Ա��ְ��
		public static final int Type_UserEducation = 19;  //Ա��ѧ��
		public static final int Type_UserDegree = 20;  //Ա��ѧλ
		public static final int Type_UserSpecialty = 21;   //Ա����ѧרҵ
		public static final int Type_UserAdministrationPost = 22;  //Ա������ְ��
		public static final int Type_UserPartyPost = 23;   //Ա������ְ��
		public static final int Type_VehicleBrand = 24;    //����Ʒ��
		public static final int Type_VehicleModel = 25;    //�����ͺ�
		public static final int Type_VehicleLicenceType = 26;   //��������
		public static final int Type_UserNation = 27;   //Ա������
		public static final int Type_Insustry = 28;   //��ҵ
		
		public static final int Type_Customer_Type = 29;   //��λ����

		
		public static String getBaseTypeInfo(int type){
			switch(type){
			case Type_CustomerType:
				return "��ҵ����";
			case Type_TestLogTester:
				return "�춨��λ";
			case Type_SpecificationType:
				return "�淶���";
			case Type_StandardIssuedBy:
				return "������׼��֤��λ";
			case Type_StandardSIssuedBy:
				return "������׼���֤��֤��λ";
			case Type_UserPoliticsStatus:
				return "������ò";
			case Type_UserType:
				return "��Ա����";
			case Type_UserJobTitle:
				return "ְ��";
			case Type_UserEducation:
				return "ѧ��";
			case Type_UserDegree:
				return "ѧλ";
			case Type_UserSpecialty:
				return "��ѧרҵ";
			case Type_UserAdministrationPost:
				return "����ְ��";
			case Type_UserPartyPost:
				return "����ְ��";
			case Type_VehicleBrand:
				return "����Ʒ��";
			case Type_VehicleModel:
				return "�����ͺ�";
			case Type_VehicleLicenceType:
				return "��������";
			case Type_UserNation:
				return "����";
			case Type_Insustry:
				return "��ҵ";
			case Type_Customer_Type:
				return "��λ����";
			default:
					return "";
			}
		}
		
		public static String getBaseTypeAttr(int type){
			switch(type){
			case Type_CustomerType:
				return "Customer|Classification";
			case Type_TestLogTester:
				return "TestLog|Tester";
			case Type_SpecificationType:
				return "Specification|Type";
			case Type_StandardIssuedBy:
				return "Standard|IssuedBy";
			case Type_StandardSIssuedBy:
				return "Standard|SIssuedBy";
			case Type_UserPoliticsStatus:
				return "SysUser|PoliticsStatus";
			case Type_UserType:
				return "SysUser|Type";
			case Type_UserJobTitle:
				return "SysUser|JobTitle";
			case Type_UserEducation:
				return "SysUser|Education";
			case Type_UserDegree:
				return "SysUser|Degree";
			case Type_UserSpecialty:
				return "SysUser|Specialty";
			case Type_UserAdministrationPost:
				return "SysUser|AdministrationPost";
			case Type_UserPartyPost:
				return "SysUser|PartyPost";
			case Type_VehicleBrand:
				return "Vehicle|Brand";
			case Type_VehicleModel:
				return "Vehicle|Model";
			case Type_VehicleLicenceType:
				return "Vehicle|LicenceType";
			case Type_UserNation:
				return "SysUser|Nation";
			case Type_Insustry:
				return "Customer|Industry";
			case Type_Customer_Type:
				return "Customer|CustomerType";
			default:
					return "";
			}
		}
		
	}
	
	public static class CommissionSheetStatus{	//ί�е�״̬
		public static final int Status_YiShouJian = 0;	//���ռ�
		public static final int Status_YiFenPei = 1;	//�ѷ���
		public static final int Status_ZhuanBaoZhong = 2;	//ת����
		public static final int Status_YiWanGong = 3;	//���깤
		public static final int Status_YiJieZhang = 4; //�ѽ���
		public static final int Status_YiJieShu = 9; //�ѽ���/��ȡ��
		public static final int Status_YiZhuXiao = 10; //��ע��
		public static final int Status_YuLiuZhong = -1;//Ԥ����
		
		public static String getStatusString(int status){
			switch(status){
			case Status_YiShouJian:
				return "���ռ�";
			case Status_YiFenPei:
				return "�ѷ���";
			case Status_ZhuanBaoZhong:
				return "ת����";
			case Status_YiWanGong:
				return "���깤";
			case Status_YiJieZhang:
				return "�ѽ���";
			case Status_YiJieShu:
				return "�ѽ���";
			case Status_YiZhuXiao:
				return "��ע��";
			case Status_YuLiuZhong:
				return "Ԥ����";
			default:
				return "";
			}
		}
		/**
		 * �ж�һ��ί�е�״̬�Ƿ����깤
		 * @param status
		 * @return ί�е����깤����ע�����򷵻�true
		 */
		public static boolean checkCommissionSheetFinished(int status){
			switch(status){
			case Status_YiWanGong:
			case Status_YiJieZhang:
			case Status_YiJieShu:
				return true;
			default:
				return false;
			}
		}
		
		/**
		 * �ж�һ��ί�е�״̬�Ƿ���ע��
		 * @param status
		 * @return
		 */
		public static boolean checkCommissionSheetInvalid(int status){
			switch(status){
			case Status_YiZhuXiao:
				return true;
			default:
				return false;
			}
		}
		/**
		 * �ж�һ��ί�е�״̬�Ƿ����깤���Ƿ���ע��
		 * @param status
		 * @return
		 */
		public static boolean checkCommissionSheetFinishedOrInvalid(int status){
			return checkCommissionSheetFinished(status) || checkCommissionSheetInvalid(status);
		}
	}
	
	/**
	 * ���źͼ�ʱ��Ϣ������
	 * @author Zhan
	 *
	 */
	public static class SmsAndInfomationType{
		public static final int Type_Other = 10;	//����
		public static final int Type_TaskReceived = 11;	//�յ���������
		public static final int Type_OverdueApproveTask = 12;//������������
		public static final int Type_WithdrawApproveTask = 13;//������������
		public static final int Type_DiscountApproveTask = 14;//�ۿ�������������
		public static final int Type_DriverTask = 15;	//�ֳ�����������˾����
		public static final int Type_LocaleTask = 16;	//�ֳ��������
		public static final int Type_OriginalAndCertificateVerify = 17;	//ԭʼ��¼��֤���������
		public static final int Type_OriginalAndCertificateAuthorize = 18;	//ԭʼ��¼��֤�����Ȩǩ��
		
		public static final int Type_TaskCancel = 19;	//ȡ������ļ�������
		
		public static final int Type_RemakeCertificate = 20;	//���±���֤��
		public static final int Type_RemakeCertificateApprove = 21;	//���±���֤�������
		
		public static final int Type_OriginalAndCertificateWorkStaffVerify = 22;	//�춨Ա�˶���֤�鲻�ɼ춨Ա�Լ��ϴ���ԭʼ��¼��֤��
		
		
		public static final String Url_TaskReceived = "/jlyw/TaskManage/TaskTime.jsp";	//���������������
		public static final String Url_OriginalAndCertificateVerify = "/jlyw/TaskManage/VerifyTask.jsp";	//ԭʼ��¼��֤�������������
		public static final String Url_OriginalAndCertificateAuthorize = "/jlyw/TaskManage/AuthorizeTask.jsp";	//ԭʼ��¼��֤��ǩ����������
		public static String getMsgTypeInfo(int type){
			switch(type){
			case Type_TaskReceived:
				return "��Ʒ����/�춨";
			case Type_OverdueApproveTask:
				return "��������";
			case Type_WithdrawApproveTask:
				return "��������";
			case Type_DiscountApproveTask:
				return "�ۿ���������";
			case Type_DriverTask:
				return "��������";
			case Type_LocaleTask:
				return "�ֳ��������";
			case Type_OriginalAndCertificateVerify:
				return "ԭʼ��¼/֤�����";
			case Type_OriginalAndCertificateAuthorize:
				return "֤����Ȩǩ��";
			case Type_TaskCancel:
				return "ȡ����������";
			case Type_RemakeCertificate:
				return "���±���֤��";
			case Type_RemakeCertificateApprove:
				return "���±���֤�����";
			case Type_OriginalAndCertificateWorkStaffVerify:
				return "�춨Ա�˶�";
			case Type_Other:
			default:
				return "����";
			}
		}
	}
	
	public static class CertificateFlag{
		/**
		 * �ж�һ��֤���Ƿ�Ϊ��ʽ�汾
		 * @param certificate ��Ҫ�жϵ�֤��
		 * @return true����֤��֤��Ϊ��ʽ�棬false����֤��֤�鲻����ʽ��
		 */
		public static boolean isCertificateOfficial(Certificate certificate) throws Exception{
			if(certificate == null || certificate.getPdf() != null){
				return true;
			}else{
				return false;
			}
		}
	}
}
