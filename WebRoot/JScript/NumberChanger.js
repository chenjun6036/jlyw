// JavaScript Document
// ��ֵת����
function getFloat(number) {
	try{
		 var numberStr = String(number);
		 if(isNaN(parseFloat(numberStr))){
		 	return parseFloat('0.0');
		 }else{
		 	return parseFloat(numberStr);
		 }
	}catch(e){
		return parseFloat('0.0');
	}
};
function getInt(number){
	try{
		var numberStr = String(number);
		if(isNaN(parseInt(numberStr))){
		 	return parseInt('0');
		 }else{
		 	return parseInt(numberStr);
		 }
	}catch(e){
		return parseInt('0');
	}
}
//��ȡ����������������
function getIntByRound(number){
	return Math.round(getFloat(number));
}