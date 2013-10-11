package com.jlyw.util;

import java.lang.reflect.Field;

import org.json.me.JSONArray;

import com.jlyw.hibernate.Certificate;
import com.thoughtworks.xstream.core.util.Fields;

/**
 * 标识所有系统标志位
 * @author Administrator
 *
 */
public class FlagUtil {
	/**
	 * 原因类型
	 * @author Zhan
	 *
	 */
	public class ReasonType{
		public static final int Type_Withdraw = 11;	//退样
		public static final int Type_Overdue = 12;	//超期申请
		public static final int Type_Discount = 13;	//折扣申请
		public static final int Type_CancelCommissionSheet = 14;  //注销委托单
		public static final int Type_CancelSubcontractor = 21;	//注销转包方
		public static final int Type_CancelCustomer = 22; //注销委托单位
		
	}
	
	public class QualificationType{	//检测人员资质类型
		public static final int Type_Jianding = 11;	//检定
		public static final int Type_Jiaozhun = 12;	//校准
		public static final int Type_Jianyan = 13;	//检验
		public static final int Type_Heyan = 14;	//核验
		public static final int Type_Qianzi = 15; //授权签字
		public static final int Type_Except = 16; //检验排外
	}
	
	public static class BaseTypeInfoType{  //基础信息类型
		public static final int Type_CustomerType = 11;  //企业分类，没有与数据中保持一致，本应是Classification
		public static final int Type_TestLogTester = 12; //检定单位
		public static final int Type_SpecificationType = 13; //规程规范类别
		public static final int Type_StandardIssuedBy = 14;  //计量标准发证机关
		public static final int Type_StandardSIssuedBy = 15; //计量标准社会证发证机关
		public static final int Type_UserPoliticsStatus = 16; //员工政治面貌
		public static final int Type_UserType = 17;    //人员性质
		public static final int Type_UserJobTitle = 18;   //员工职称
		public static final int Type_UserEducation = 19;  //员工学历
		public static final int Type_UserDegree = 20;  //员工学位
		public static final int Type_UserSpecialty = 21;   //员工所学专业
		public static final int Type_UserAdministrationPost = 22;  //员工行政职务
		public static final int Type_UserPartyPost = 23;   //员工党内职务
		public static final int Type_VehicleBrand = 24;    //车辆品牌
		public static final int Type_VehicleModel = 25;    //车辆型号
		public static final int Type_VehicleLicenceType = 26;   //车牌类型
		public static final int Type_UserNation = 27;   //员工民族
		public static final int Type_Insustry = 28;   //行业
		
		public static final int Type_Customer_Type = 29;   //单位类型

		
		public static String getBaseTypeInfo(int type){
			switch(type){
			case Type_CustomerType:
				return "企业分类";
			case Type_TestLogTester:
				return "检定单位";
			case Type_SpecificationType:
				return "规范类别";
			case Type_StandardIssuedBy:
				return "计量标准发证单位";
			case Type_StandardSIssuedBy:
				return "计量标准社会证发证单位";
			case Type_UserPoliticsStatus:
				return "政治面貌";
			case Type_UserType:
				return "人员性质";
			case Type_UserJobTitle:
				return "职称";
			case Type_UserEducation:
				return "学历";
			case Type_UserDegree:
				return "学位";
			case Type_UserSpecialty:
				return "所学专业";
			case Type_UserAdministrationPost:
				return "行政职务";
			case Type_UserPartyPost:
				return "党内职务";
			case Type_VehicleBrand:
				return "车辆品牌";
			case Type_VehicleModel:
				return "车辆型号";
			case Type_VehicleLicenceType:
				return "车牌类型";
			case Type_UserNation:
				return "民族";
			case Type_Insustry:
				return "行业";
			case Type_Customer_Type:
				return "单位类型";
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
	
	public static class CommissionSheetStatus{	//委托单状态
		public static final int Status_YiShouJian = 0;	//已收件
		public static final int Status_YiFenPei = 1;	//已分配
		public static final int Status_ZhuanBaoZhong = 2;	//转包中
		public static final int Status_YiWanGong = 3;	//已完工
		public static final int Status_YiJieZhang = 4; //已结帐
		public static final int Status_YiJieShu = 9; //已结束/已取样
		public static final int Status_YiZhuXiao = 10; //已注销
		public static final int Status_YuLiuZhong = -1;//预留中
		
		public static String getStatusString(int status){
			switch(status){
			case Status_YiShouJian:
				return "已收件";
			case Status_YiFenPei:
				return "已分配";
			case Status_ZhuanBaoZhong:
				return "转包中";
			case Status_YiWanGong:
				return "已完工";
			case Status_YiJieZhang:
				return "已结账";
			case Status_YiJieShu:
				return "已结束";
			case Status_YiZhuXiao:
				return "已注销";
			case Status_YuLiuZhong:
				return "预留中";
			default:
				return "";
			}
		}
		/**
		 * 判断一个委托单状态是否已完工
		 * @param status
		 * @return 委托单已完工或已注销，则返回true
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
		 * 判断一个委托单状态是否已注销
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
		 * 判断一个委托单状态是否已完工或是否已注销
		 * @param status
		 * @return
		 */
		public static boolean checkCommissionSheetFinishedOrInvalid(int status){
			return checkCommissionSheetFinished(status) || checkCommissionSheetInvalid(status);
		}
	}
	
	/**
	 * 短信和即时消息的类型
	 * @author Zhan
	 *
	 */
	public static class SmsAndInfomationType{
		public static final int Type_Other = 10;	//其他
		public static final int Type_TaskReceived = 11;	//收到检验任务
		public static final int Type_OverdueApproveTask = 12;//超期审批任务
		public static final int Type_WithdrawApproveTask = 13;//退样审批任务
		public static final int Type_DiscountApproveTask = 14;//折扣申请审批任务
		public static final int Type_DriverTask = 15;	//现场检测出车任务（司机）
		public static final int Type_LocaleTask = 16;	//现场检测任务
		public static final int Type_OriginalAndCertificateVerify = 17;	//原始记录和证书核验任务
		public static final int Type_OriginalAndCertificateAuthorize = 18;	//原始记录和证书的授权签字
		
		public static final int Type_TaskCancel = 19;	//取消分配的检验任务
		
		public static final int Type_RemakeCertificate = 20;	//重新编制证书
		public static final int Type_RemakeCertificateApprove = 21;	//重新编制证书结果审核
		
		public static final int Type_OriginalAndCertificateWorkStaffVerify = 22;	//检定员核定（证书不由检定员自己上传）原始记录和证书
		
		
		public static final String Url_TaskReceived = "/jlyw/TaskManage/TaskTime.jsp";	//检验任务相关链接
		public static final String Url_OriginalAndCertificateVerify = "/jlyw/TaskManage/VerifyTask.jsp";	//原始记录和证书核验任务链接
		public static final String Url_OriginalAndCertificateAuthorize = "/jlyw/TaskManage/AuthorizeTask.jsp";	//原始记录和证书签字任务链接
		public static String getMsgTypeInfo(int type){
			switch(type){
			case Type_TaskReceived:
				return "样品检验/检定";
			case Type_OverdueApproveTask:
				return "延期审批";
			case Type_WithdrawApproveTask:
				return "退样审批";
			case Type_DiscountApproveTask:
				return "折扣申请审批";
			case Type_DriverTask:
				return "出车任务";
			case Type_LocaleTask:
				return "现场检测任务";
			case Type_OriginalAndCertificateVerify:
				return "原始记录/证书核验";
			case Type_OriginalAndCertificateAuthorize:
				return "证书授权签字";
			case Type_TaskCancel:
				return "取消检验任务";
			case Type_RemakeCertificate:
				return "重新编制证书";
			case Type_RemakeCertificateApprove:
				return "重新编制证书审核";
			case Type_OriginalAndCertificateWorkStaffVerify:
				return "检定员核定";
			case Type_Other:
			default:
				return "其它";
			}
		}
	}
	
	public static class CertificateFlag{
		/**
		 * 判断一个证书是否为正式版本
		 * @param certificate ：要判断的证书
		 * @return true：验证的证书为正式版，false：验证的证书不是正式版
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
