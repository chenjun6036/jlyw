// JavaScript Document
// ��ȡ״̬��Ϣ
function getCommissionSheetStatusInfo(number) {
	try{
		if(number=="0" || number==0)
		{
			return "���ռ�";
		}
		else if(number==1||number=="1")
		{
			return "�ѷ���";
		}
		else if(number==2||number=="2")
		{
			return "ת����";
		}
		else if(number==3||number=="3")
		{
			return "���깤";
		}
		else if(number==4||number=="4")
		{
			return "�ѽ���";
		}
		else if(number==9||number=="9")
		{
			return "�ѽ���";
		}
		else if(number==10||number=="10")
		{
			return "��ע��";
		}
		else if(number==-1||number=="-1")
		{
			return "Ԥ����";
		}
		else
			return "";
	}catch(e){
		return "";
	}
};