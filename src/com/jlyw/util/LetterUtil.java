package com.jlyw.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

/**
 * ���뺺���ַ�,�õ�������ĸ,
 * Ӣ����ĸ���ض�Ӧ�Ĵ�д��ĸ
 * ���ַ�������
 * �����ַ���������
 * @author Administrator
 *
 */
public class LetterUtil {
	    //��ĸZʹ����������ǩ�������У�����ֵ
	    //i, u, v��������ĸ, ����ǰ�����ĸ
	    private static char[] chartable =
	            {
	                '��', '��', '��', '��', '��', '��', '��', '��', '��',
	                '��', '��', '��', '��', '��', 'Ŷ', 'ž', '��', 'Ȼ',
	                '��', '��', '��', '��', '��', '��', 'ѹ', '��', '��'
	            };
	    private static char[] alphatable =
	            {
	                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
	                'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 
	                'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	            };
	    private static int[] table = new int[27];
	    //��ʼ��
	    static{
	        for (int i = 0; i < 27; ++i) {
	            table[i] = gbValue(chartable[i]);
	        }
	    }

	    private LetterUtil() {
	    }

	    //������,�����ַ�,�õ�������ĸ,
	    //Ӣ����ĸ���ض�Ӧ�Ĵ�д��ĸ
	    //���ַ�������
	    //�������ؿ��ַ��� ��
	    private static char Char2Alpha(char ch) {

	        if (ch >= 'a' && ch <= 'z')
	            return (char) (ch - 'a' + 'A');
	        if (ch >= 'A' && ch <= 'Z')
	            return ch;
	        if (ch >= '0' && ch <= '9')
	        	return ch;//' ';
	        int gb = gbValue(ch);
	        if (gb < table[0])
	            return ch;	//���ؿ��ַ�
	        int i;
	        for (i = 0; i < 26; ++i) {
	            if (match(i, gb)) break;
	        }
	        if (i >= 26)
	            return ch;//' ';
	        else
	            return alphatable[i];
	    }

	    //����һ���������ֵ��ַ�������һ������ƴ������ĸ���ַ���
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

	        //��ĸZʹ����������ǩ
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

	    //ȡ�����ֵı���
	    private static int gbValue(char ch) {
	        String str = new String();
	        str += ch;
	        try {
	            byte[] bytes = str.getBytes("GB2312");
	            if (bytes.length < 2)	//���Ǻ���
	                return (int)ch;//0;
	            return (bytes[0] << 8 & 0xff00) + (bytes[1] &
	                    0xff);
	        } catch (Exception e) {
	            return 0;
	        }
	    }
	    
	    /** 
	     * MD5 ���� 
	     */  
	    private static String getMD5Str(String str) throws Exception {  
	        MessageDigest messageDigest = null; 
	        try {  
	            messageDigest = MessageDigest.getInstance("MD5");
	            messageDigest.reset();  
	            messageDigest.update(str.getBytes());  
	        } catch (NoSuchAlgorithmException e) { 
	            throw new Exception("�Ҳ���MD5�㷨");
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
	     * ��ȡ֤��İ�ȫ��
	     * @param certificateCode��֤����
	     * @param workDate���춨����
	     * @return
	     * @throws Exception
	     */
	    public static String getCertificateSecurityCode(String certificateCode, java.sql.Date workDate) throws Exception{
	    	return getMD5Str(String.format("%s_%s_%s", "czjl&njust", certificateCode==null?"":certificateCode, workDate==null?"":new SimpleDateFormat("yyyyMMdd").format(workDate)));	//��ȫ��
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
