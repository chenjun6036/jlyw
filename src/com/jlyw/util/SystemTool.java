package com.jlyw.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 与系统相关的一些常用工具方法.
 * 
 * @author liuzhen
 * @version 1.0.0
 */
public class SystemTool {

	/**
	 * 获取当前操作系统名称.
	 * return 操作系统名称 例如:windows xp,linux 等.
	 */
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}
	
	/**
	 * 获取unix网卡的mac地址.
	 * 非windows的系统默认调用本方法获取.如果有特殊系统请继续扩充新的取mac地址方法.
	 * @return mac地址
	 */
	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ifconfig eth0");// linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
			bufferedReader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("hwaddr");// 寻找标示字符串[hwaddr]
				if (index >= 0) {// 找到了
					mac = line.substring(index +"hwaddr".length()+ 1).trim();//  取出mac地址并去除2边空格
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
	 * 获取widnows网卡的mac地址.
	 * @return mac地址
	 */
	public static String getWindowsMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ipconfig /all");// windows下的命令，显示信息中包含有mac地址信息
			bufferedReader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("physical address");// 寻找标示字符串[physical address]
				if (index >= 0) {// 找到了
					index = line.indexOf(":");// 寻找":"的位置
					if (index>=0) {
						mac = line.substring(index + 1).trim();//  取出mac地址并去除2边空格
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
	 * 测试用的main方法.
	 * 
	 * @param argc
	 *            运行参数.
	 */
	public static String getMac() {
		String os = getOSName();
		
		if(os.startsWith("windows")){
			//本地是windows
			String mac = getWindowsMACAddress();
			return mac;
		}else{
			//本地是非windows系统 一般就是unix
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

        path = "cmd /c " + path + "\\bin\\" + fileName;//如果是安装版的Tomcat，可以在cmd命令窗口中输入net stop tomcat6/net start tomcat6来停止/启动tomcat。实际情况根据Tomcat的版本号，可以是net stop tomcat5/net start tomcat5之类的。

        String command = path;

        try
        {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((outText = bufferedReader.readLine()) != null)
            {
                System.out.println(outText); //输出测试 
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


