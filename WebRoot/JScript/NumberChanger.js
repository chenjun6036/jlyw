// JavaScript Document
// 数值转换器
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
//获取整数——四舍五入
function getIntByRound(number){
	return Math.round(getFloat(number));
}