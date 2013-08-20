package com.jlyw.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

/**
 * 输入汉字字符,得到他的声母,
 * 英文字母返回对应的大写字母
 * 数字返回自身
 * 其他字符返回自身
 * @author Administrator
 *
 */
public class LetterUtil {
	    //字母Z使用了两个标签，这里有２７个值
	    //i, u, v都不做声母, 跟随前面的字母
	    private static char[] chartable =
	            {
	                '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈',
	                '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然',
	                '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'
	            };
	    private static char[] alphatable =
	            {
	                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
	                'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 
	                'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	            };
	    private static int[] table = new int[27];
	    //初始化
	    static{
	        for (int i = 0; i < 27; ++i) {
	            table[i] = gbValue(chartable[i]);
	        }
	    }

	    private LetterUtil() {
	    }

	    //主函数,输入字符,得到他的声母,
	    //英文字母返回对应的大写字母
	    //数字返回自身
	    //其他返回空字符‘ ’
	    private static char Char2Alpha(char ch) {

	        if (ch >= 'a' && ch <= 'z')
	            return (char) (ch - 'a' + 'A');
	        if (ch >= 'A' && ch <= 'Z')
	            return ch;
	        if (ch >= '0' && ch <= '9')
	        	return ch;//' ';
	        int gb = gbValue(ch);
	        if (gb < table[0])
	            return ch;	//返回空字符
	        int i;
	        for (i = 0; i < 26; ++i) {
	            if (match(i, gb)) break;
	        }
	        if (i >= 26)
	            return ch;//' ';
	        else
	            return alphatable[i];
	    }

	    //根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
	    public static String String2Alpha(String SourceStr){
	    	if(SourceStr == null)
	    		return null;
	        String Result = "";
	        int StrLength = SourceStr.length();
	        int i;
	        try {
	            for (i = 0; i < StrLength; i++) {
	                Result += Char2Alpha(SourceStr.charAt(i));
	            }
	        } catch (Exception e) {
	            Result = "";
	        }
	        return Result;
	    }

	    private static boolean match(int i, int gb) {
	        if (gb < table[i])
	            return false;
	        int j = i + 1;

	        //字母Z使用了两个标签
	        while (j < 26 && (table[j] == table[i])) ++j;
	        if (j == 26)
	            return gb <= table[j];
	        else
	            return gb < table[j];
	    }
	    
	    public static boolean isNumeric(String str){ 
	    	if(str.matches("\\d*")){
	    		return true; 
	    	}else{
	    		return false;
	    	}
	    }

	    //取出汉字的编码
	    private static int gbValue(char ch) {
	        String str = new String();
	        str += ch;
	        try {
	            byte[] bytes = str.getBytes("GB2312");
	            if (bytes.length < 2)	//不是汉字
	                return (int)ch;//0;
	            return (bytes[0] << 8 & 0xff00) + (bytes[1] &
	                    0xff);
	        } catch (Exception e) {
	            return 0;
	        }
	    }
	    
	    /** 
	     * MD5 加密 
	     */  
	    private static String getMD5Str(String str) throws Exception {  
	        MessageDigest messageDigest = null; 
	        try {  
	            messageDigest = MessageDigest.getInstance("MD5");
	            messageDigest.reset();  
	            messageDigest.update(str.getBytes());  
	        } catch (NoSuchAlgorithmException e) { 
	            throw new Exception("找不到MD5算法");
	        } 
	  
	        byte[] byteArray = messageDigest.digest(); 
	        StringBuffer md5StrBuff = new StringBuffer();  
	        for (int i = 0; i < byteArray.length; i++) {              
	            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
	                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
	            else  
	                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
	        }  
	        return md5StrBuff.toString();  
	    }  
	    
	    /**
	     * 获取证书的安全码
	     * @param certificateCode：证书编号
	     * @param workDate：检定日期
	     * @return
	     * @throws Exception
	     */
	    public static String getCertificateSecurityCode(String certificateCode, java.sql.Date workDate) throws Exception{
	    	return getMD5Str(String.format("%s_%s_%s", "czjl&njust", certificateCode==null?"":certificateCode, workDate==null?"":new SimpleDateFormat("yyyyMMdd").format(workDate)));	//安全码
	    }

	    public static void main(String[] args) {
	        String str = "5431324214312";
	        if(isNumeric(str))
	        	System.out.println("yes");
	        else
	        	System.out.println("no");
	        try {
	        	String Code = LetterUtil.getMD5Str("zhanguosheng");
				System.out.println(Code.substring(0, 16));
				System.out.println(Code.substring(16,Code.length()));
				System.out.println(Code);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
}
