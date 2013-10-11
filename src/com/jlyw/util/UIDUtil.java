package com.jlyw.util;

import java.text.SimpleDateFormat;
import java.util.Random;

public class UIDUtil {
	public static final SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static final SimpleDateFormat formater1 = new SimpleDateFormat("yyyyMMdd");
	private static long LastCurrentTimeMillis = 0;	//���һ�λ�ȡ�ַ�����ʱ��
	private static Object MutexObjectOf22BitUID = new Object();		//���ڻ������
	
	/**
	 * ��ȡ22λ��ID�ַ���
	 * ID�ַ�������yyyyMMddHHmmssSSS_XXXX(XXXXΪ��λ�����)
	 * @return 22λ��ID�ַ���
	 */
	public static String get22BitUID(){
		synchronized (MutexObjectOf22BitUID) {
			long currentTimeMillis = System.currentTimeMillis();
			if(currentTimeMillis <= LastCurrentTimeMillis){
				currentTimeMillis = LastCurrentTimeMillis + 10;	//������10ms
			}
			LastCurrentTimeMillis = currentTimeMillis;
			Random rand = new Random(currentTimeMillis);
			int randInt = rand.nextInt(10000);	//����0~9999֮��������
			return String.format("%s_%04d", formater.format(new java.sql.Date(currentTimeMillis)), randInt);
		}
	}
	/**
	 * ��ȡ14λ��ID�ַ���(�嵥��)
	 * ID�ַ�������yyyyMMddXXXXXX(XXXXXXΪ��λ�����)
	 * @return 14λ��ID�ַ���
	 */
	public static String getBillCode(){
		long currentTimeMillis = System.currentTimeMillis();
		if(currentTimeMillis <= LastCurrentTimeMillis){
			currentTimeMillis = LastCurrentTimeMillis + 10;	//������10ms
		}
		LastCurrentTimeMillis = currentTimeMillis;
		Random rand = new Random(currentTimeMillis);
		int randInt = rand.nextInt(1000000);	//����0~999999֮��������
		return String.format("%s%06d", formater1.format(new java.sql.Date(currentTimeMillis)), randInt);
	}
	
	/**
	 * ��ȡ�ļ���
	 * @param prex���ļ���ǰ׺
	 * @return �ļ����ַ�������ǰ׺��_yyyyMMddHHmmssSSS_XXXX
	 */
	public static String getFileNameUID(String prex){
		return String.format("%s_%s", prex==null?"":prex, get22BitUID());
	}
	
	public static void main(String []args){
		System.out.println(UIDUtil.get22BitUID());
		System.out.println(UIDUtil.getFileNameUID("Home"));
		System.out.println(UIDUtil.getBillCode());
	}
	
	/**
	 * ��ȡ֤���ţ�Code����[ί�е���][3λ���к�]-[3λ�汾��]���汾�Ŵ�0��ʼ���ĺ��һ��Ϊ001����汾��Ϊ0������Ҫ����λ�İ汾��
	 * @param commissionSheetCode��ί�е���
	 * @param sequence�����кţ��ڼ���֤�飩
	 * @param version���汾��
	 * @return
	 */
	public static String getCertificateCode(String commissionSheetCode, int sequence, int version){
		if(sequence <= 0){
			sequence = 1;
		}
		if(version < 0){
			version = 0;
		}
		if(version == 0){
			return String.format("%s%03d", commissionSheetCode, sequence);
		}
		return String.format("%s%03d-%03d", commissionSheetCode, sequence, version);
	}
	
}
