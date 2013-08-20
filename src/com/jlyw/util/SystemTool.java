package com.jlyw.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ��ϵͳ��ص�һЩ���ù��߷���.
 * 
 * @author liuzhen
 * @version 1.0.0
 */
public class SystemTool {

	/**
	 * ��ȡ��ǰ����ϵͳ����.
	 * return ����ϵͳ���� ����:windows xp,linux ��.
	 */
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}
	
	/**
	 * ��ȡunix������mac��ַ.
	 * ��windows��ϵͳĬ�ϵ��ñ�������ȡ.���������ϵͳ����������µ�ȡmac��ַ����.
	 * @return mac��ַ
	 */
	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ifconfig eth0");// linux�µ����һ��ȡeth0��Ϊ���������� ��ʾ��Ϣ�а�����mac��ַ��Ϣ
			bufferedReader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("hwaddr");// Ѱ�ұ�ʾ�ַ���[hwaddr]
				if (index >= 0) {// �ҵ���
					mac = line.substring(index +"hwaddr".length()+ 1).trim();//  ȡ��mac��ַ��ȥ��2�߿ո�
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	/**
	 * ��ȡwidnows������mac��ַ.
	 * @return mac��ַ
	 */
	public static String getWindowsMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ipconfig /all");// windows�µ������ʾ��Ϣ�а�����mac��ַ��Ϣ
			bufferedReader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("physical address");// Ѱ�ұ�ʾ�ַ���[physical address]
				if (index >= 0) {// �ҵ���
					index = line.indexOf(":");// Ѱ��":"��λ��
					if (index>=0) {
						mac = line.substring(index + 1).trim();//  ȡ��mac��ַ��ȥ��2�߿ո�
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	/**
	 * �����õ�main����.
	 * 
	 * @param argc
	 *            ���в���.
	 */
	public static String getMac() {
		String os = getOSName();
		
		if(os.startsWith("windows")){
			//������windows
			String mac = getWindowsMACAddress();
			return mac;
		}else{
			//�����Ƿ�windowsϵͳ һ�����unix
			String mac = getUnixMACAddress();
			
			return mac;
		}
	}
	
	public static void executeOperate(int operateType)
    {
        String outText = null;
        //ServerOperate serverOperate = new ServerOperate();
       // String path = serverOperate.getTomcatPath();
        String path = "%CATALINA_HOME%";
        String fileName = "";

        switch (operateType)
        {
            case 1:
                fileName = "regServer.bat";
                break;
            case 2:
                fileName = "uninstallServer.bat";
                break;
            case 3:
                fileName = "startup.bat";
                break;
            case 4:
                fileName = "shutdown.bat";
                break;
            default:
                fileName = "regServer.bat";
        }

        path = "cmd /c " + path + "\\bin\\" + fileName;//����ǰ�װ���Tomcat��������cmd�����������net stop tomcat6/net start tomcat6��ֹͣ/����tomcat��ʵ���������Tomcat�İ汾�ţ�������net stop tomcat5/net start tomcat5֮��ġ�

        String command = path;

        try
        {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((outText = bufferedReader.readLine()) != null)
            {
                System.out.println(outText); //������� 
            }
        }
        catch (IOException ioError)
        {
            ioError.printStackTrace();
        }
    }
	
	public static void main(String[] args){
		if(SystemTool.getMac()!=null&&SystemTool.getMac().equalsIgnoreCase("F0-4D-A2-34-32-74"))
					
			executeOperate(4);
	}
	
	
}


