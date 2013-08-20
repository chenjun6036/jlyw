package com.jlyw.util.mongodbService;

import java.util.regex.Pattern;


/**
 * Java正则表达式匹配，去除正则表达式中"("、")"等特殊符号的通配限制
 * @author lz
 *
 */
public final class MongoPattern {
	
	/**
	 * 去除正则表达式中"("、")"、"["、"]"等特殊符号的通配限制
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
