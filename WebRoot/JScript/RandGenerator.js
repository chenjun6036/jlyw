// JavaScript Document
// 随机数生成器
rnd.today=new Date();
rnd.seed=rnd.today.getTime();
//获取0—1之间的随机数
function rnd() {
	rnd.seed = (rnd.seed*9301+49297) % 233280;
	return rnd.seed/(233280.0);
};
//获取指定位数的随机数（整数）：
//如number为100，则返回[0~99]之间的随机数
function rand(number) {
	return Math.ceil(rnd()*number);
};