// JavaScript Document
// 获取状态信息
function getCommissionSheetStatusInfo(number) {
	try{
		if(number=="0" || number==0)
		{
			return "已收件";
		}
		else if(number==1||number=="1")
		{
			return "已分配";
		}
		else if(number==2||number=="2")
		{
			return "转包中";
		}
		else if(number==3||number=="3")
		{
			return "已完工";
		}
		else if(number==4||number=="4")
		{
			return "已结账";
		}
		else if(number==9||number=="9")
		{
			return "已结束";
		}
		else if(number==10||number=="10")
		{
			return "已注销";
		}
		else if(number==-1||number=="-1")
		{
			return "预留中";
		}
		else
			return "";
	}catch(e){
		return "";
	}
};