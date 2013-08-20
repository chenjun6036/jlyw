package com.jlyw.util;

public class CommissionSheetFlagUtil {
	
	public static final int CommissionTypeCount = 7;//委托形式种类
	
	/**
	 * 委托形式标志位的中文意思
	 * @param flag
	 * @return
	 */
	public static String getCommissionTypeByFlag(int flag){
		switch(flag){
		case 1:
			return "送样检测";
		case 2:
			return "现场检测";
		case 3:
			return "公正计量";
		case 4:
			return "型式评价";
		case 5:
			return "其它业务";
		case 6:
			return "自检业务";
		case 7:
			return "现场带回";
		default:
			return "";
		}
	}
	
	/**
	 * 报告形式标志位的中文意思
	 * @param flag
	 * @return
	 */
	public static String getReportTypeByFlag(int flag){
		switch(flag){
		case 1:
			return "检定";
		case 2:
			return "校准";
		case 3:
			return "检测";
		case 4:
			return "检验";
		default:
			return "";
		}
	}
	
	/**
	 * 是否强检标志位的中文意思
	 * @param flag
	 * @return
	 */
	public static String getMandatoryByFlag(boolean flag){
		if(flag){	//标志位为1:非强制检定
			return "非强制检定";
		}else{
			return "强制检定";
		}
	}
	
	/**
	 * 是否加急标志位的中文意思
	 * @param flag
	 * @return
	 */
	public static String getUrgentByFlag(boolean flag){
		if(flag){	//标志位为1:不加急
			return "否";
		}else{
			return "是";
		}
	}
	/**
	 * 是否需要修理标志位的中文意思
	 * @param flag
	 * @return
	 */
	public static String getRepairByFlag(boolean flag){
		if(flag){	//标志位为1:无需修理
			return "无需修理";
		}else{
			return "需要修理";
		}
	}
	/**
	 * 是否转包标志位的中文意思
	 * @param flag
	 * @return
	 */
	public static String getSubcontractByFlag(boolean flag){
		if(flag){	//标志位为1:不转包
			return "否";
		}else{
			return "是";
		}
	}
}
