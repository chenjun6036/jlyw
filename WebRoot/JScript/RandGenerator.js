// JavaScript Document
// �����������
rnd.today=new Date();
rnd.seed=rnd.today.getTime();
//��ȡ0��1֮��������
function rnd() {
	rnd.seed = (rnd.seed*9301+49297) % 233280;
	return rnd.seed/(233280.0);
};
//��ȡָ��λ�������������������
//��numberΪ100���򷵻�[0~99]֮��������
function rand(number) {
	return Math.ceil(rnd()*number);
};