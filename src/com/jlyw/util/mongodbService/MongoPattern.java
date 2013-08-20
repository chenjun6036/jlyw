package com.jlyw.util.mongodbService;

import java.util.regex.Pattern;


/**
 * Java������ʽƥ�䣬ȥ��������ʽ��"("��")"��������ŵ�ͨ������
 * @author lz
 *
 */
public final class MongoPattern {
	
	/**
	 * ȥ��������ʽ��"("��")"��"["��"]"��������ŵ�ͨ������
	 * @param regex
	 * @param flags
	 * @return
	 */
	public static Pattern compile(String regex, int flags){
		String newRegex = regex.replace("(", "\\(").replace(")", "\\)").replace("[", "\\[").replace("]", "\\]");
		return Pattern.compile(newRegex, flags);
	}
	private MongoPattern(){}
}
