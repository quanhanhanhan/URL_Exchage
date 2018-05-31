package com.qh.Long_Short;

import java.security.MessageDigest;
import java.util.Random;
import java.util.Scanner;

import com.mysql.cj.exceptions.RSAException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.reflect.Method;

public class Change {
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */

	public static void main(String[] args) {
		process_change();
	}
    public static String process_change() {
    	System.out.println("������ԭʼ������:");
		Scanner sc = new Scanner(System.in);
		String sLongUrl = sc.nextLine();
		String result=null;
		Date d = new Date();
		System.out.println(d);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String dateNowStr = sdf.format(d);
		try {
			Connection con;
			// ����������
			Connection con1, con2;
			String driver = "com.mysql.cj.jdbc.Driver";
			// URLָ��Ҫ���ʵ����ݿ���url
			String url = "jdbc:mysql://localhost:3306/url?serverTimezone=UTC&useSSL=false&characterEncoding=utf8";
			// MySQL����ʱ���û���
			String user = "root";
			// MySQL����ʱ������
			String password = "123456";
			Class.forName(driver);
			// getConnection()����������MySQL���ݿ�
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			System.out.println("Success connect Mysql server!");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from url_exchange where Long_url like \"%" + sLongUrl + "%\"");
			if (rs.next() == true) {
				System.out.println("����ַ�Ѿ��ж�Ӧ�Ķ����ӣ�������ֱ��ʹ��:" + rs.getString(3));
				System.out.println("�ö����ӵ��Զ���keyֵ(����Ϊ�յĻ�):" + rs.getString(4));
				int newcount = rs.getInt(1) + 1;// �������Ӷ�Ӧ�ļ���+1
				System.out.println("���Ƿ�Ҫ�򿪵�ǰ��ҳ(Y/N):");
				char s = sc.next().charAt(0);
				if (s == 'Y') {
				if(rs.getString(4)==null)
				{
					System.out.println("�����������:");
					Scanner sc1 = new Scanner(System.in);
					String sShortUrl = sc1.nextLine();
					//sc1.close();
					con1 = DriverManager.getConnection(url, user, password);
					Statement stmt1 = con1.createStatement();
					ResultSet rs1 = stmt1
							.executeQuery("select * from url_exchange where Short_url = '" + sShortUrl + "'");
					if (rs1.next() == true) {
						openURL(rs1.getString(2));
						result = rs1.getString(2);
						String sql2 = "update url_exchange set count='" + newcount + "' where Long_url = '" + sLongUrl
								+ "'";
						stmt.executeUpdate(sql2);// ִ��SQL���
						System.out.println("�޸����ݿ�ɹ�");// �����ʴ���+1
						System.out.println("��ҳ�򿪳ɹ�");
						con1.close();
					}
				}
				
					else {
						System.out.println("����������Ӷ�Ӧ��keyֵ:");
						Scanner sc1 = new Scanner(System.in);
						String key1 = sc1.nextLine();
						//sc1.close();
						con1 = DriverManager.getConnection(url, user, password);
						Statement stmt1 = con1.createStatement();
						ResultSet rs1 = stmt1
								.executeQuery("select * from url_exchange where Key_Value = '" + key1 + "'");
						if (rs1.next() == true) {
							result = rs1.getString(2);
							System.out.println("result:"+result);
							String sql2 = "update url_exchange set count='" + newcount + "' where Long_url = '" + sLongUrl
									+ "'";
							stmt.executeUpdate(sql2);// ִ��SQL���
							System.out.println("�޸����ݿ�ɹ�");// �����ʴ���+1
							System.out.println("��ҳ�򿪳ɹ�");
							openURL(rs1.getString(2));
							con1.close();
					}
				}
				}
				else {
					System.out.println("���������г����Ա���һ�εĲ���");
				}
				
			} 
			else {
				boolean b1 = sLongUrl.startsWith("www");
				if (b1 == false) {
					sLongUrl = "www." + sLongUrl;
					boolean b2 = sLongUrl.startsWith("http");
					boolean b3 = sLongUrl.startsWith("https");
					boolean b4 = sLongUrl.endsWith("com");
					boolean b5 = sLongUrl.endsWith("net");
					if (b2 == false && b3 == false)
						sLongUrl = "http://" + sLongUrl;
					if (b4 == false&&b5==false)
						sLongUrl = sLongUrl + ".com";
					    
				}
				String[] aResult = shortUrl(sLongUrl);// ������4��6λ�ַ���
				long t = System.currentTimeMillis();// ��õ�ǰʱ��ĺ�����
				Random random = new Random(t);// ��Ϊ���������뵽Random�Ĺ�������,��������α�����
				int j = random.nextInt(4);// ����4���������
				System.out.println("���ɵĶ�����:" + aResult[j]);// ���ȡһ����Ϊ����
				System.out.println("���Ƿ�ҪΪ����������һ���Զ����key(Y/N):");
				char s1 = sc.next().charAt(0);
				String key = null;
				if (s1 == 'Y') {
					System.out.println("���������Զ����keyֵ:");
					Scanner sc1 = new Scanner(System.in);
					key = sc1.nextLine();
				}
				String sql = "insert into url_exchange values('1','" + sLongUrl + "','" + aResult[j] + "','" + key
						+ "','" + dateNowStr + "')";
				stmt.executeUpdate(sql);
				System.out.println("���뵽���ݿ�ɹ�");
				System.out.println("���Ƿ�Ҫ�򿪵�ǰ��ҳ(Y/N):");
				char s2 = sc.next().charAt(0);
				if (s2 == 'Y') {
					if (s1 == 'Y') {
						
						System.out.println("���������ղ����õĶ�����Keyֵ:");
						Scanner sc2 = new Scanner(System.in);
						String own_key = sc2.nextLine();
						con2 = DriverManager.getConnection(url, user, password);
						Statement stmt2 = con2.createStatement();
						String ss = "select * from url_exchange where Key_Value = '" + own_key + "'";
						ResultSet rs2 = stmt2.executeQuery(ss);
						if (rs2.next() == true) {
							result = rs2.getString(2);
							//System.out.println(result+"hah");
							openURL(rs2.getString(2));
						}
						
					} else {
					
						System.out.println("�����������:");
						Scanner sc3 = new Scanner(System.in);
						String sShortUrl1 = sc3.nextLine();
						sc3.close();
						con2 = DriverManager.getConnection(url, user, password);
						Statement stmt2 = con2.createStatement();
						ResultSet rs2 = stmt2
								.executeQuery("select * from url_exchange where Short_url = '" + sShortUrl1 + "'");
						if (rs2.next() == true) {
							result = rs2.getString(2);
							openURL(rs2.getString(2));
						}
						
					}
				}
				
			}
			con.close();
		} catch (Exception e) {
			System.out.print("get data errorr!");
			e.printStackTrace();
		}
		sc.close();
		if(sLongUrl.startsWith("http")==true)
		return sLongUrl;
		else {
			System.out.println(result);
			return result;
		}
			
	}
	public static String[] shortUrl(String url) {
		// �����Զ������� MD5 �����ַ���ǰ�Ļ�� KEY
		String key = "test";
		// Ҫʹ������ URL ���ַ�
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
				"q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
				"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z"

		};
		// �Դ�����ַ���� MD5 ����
		String hex = md5ByHex(key + url);

		String[] resUrl = new String[4];
		for (int i = 0; i < 4; i++) {

			// �Ѽ����ַ����� 8 λһ�� 16 ������ 0x3FFFFFFF ����λ������
			String sTempSubString = hex.substring(i * 8, i * 8 + 8);

			// ������Ҫʹ�� long ����ת������Ϊ Inteper .parseInt() ֻ�ܴ��� 31 λ , ��λΪ����λ , �������long �����Խ��
			long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
			String outChars = "";
			for (int j = 0; j < 6; j++) {
				// �ѵõ���ֵ�� 0x0000003D ����λ�����㣬ȡ���ַ����� chars ����
				long index = 0x0000003D & lHexLong;
				// ��ȡ�õ��ַ����
				outChars += chars[(int) index];
				// ÿ��ѭ����λ���� 5 λ
				lHexLong = lHexLong >> 5;
			}
			// ���ַ��������Ӧ�������������
			resUrl[i] = outChars;
		}
		return resUrl;
	}

	/**
	 * MD5����(32λ��д)
	 * 
	 * @param src
	 * @return
	 */
	public static String md5ByHex(String src) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = src.getBytes();
			md.reset();
			md.update(b);
			byte[] hash = md.digest();
			String hs = "";
			String stmp = "";
			for (int i = 0; i < hash.length; i++) {
				stmp = Integer.toHexString(hash[i] & 0xFF);
				if (stmp.length() == 1)
					hs = hs + "0" + stmp;
				else {
					hs = hs + stmp;
				}
			}
			return hs.toUpperCase();
		} catch (Exception e) {
			return "";
		}
	}

	// ����ҳ
	public static void openURL(String url) {
		try {
			browse(url);
		} catch (Exception e) {
		}
	}

	// ���ݲ�ͬ�Ĳ���ϵͳ��ѡ��򿪶�Ӧ��ҳ�ķ�ʽ
	private static void browse(String url) throws Exception {
		// ��ȡ����ϵͳ������
		String osName = System.getProperty("os.name", "");
		if (osName.startsWith("Mac OS")) {
			// Mac�Ĵ򿪷�ʽ
			Method openURL = Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL",
					new Class[] { String.class });
			openURL.invoke(null, new Object[] { url });
		} else if (osName.startsWith("Windows")) {
			// windows�Ĵ򿪷�ʽ��
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		} else {
			// Unix or Linux�Ĵ򿪷�ʽ
			String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
			String browser = null;
			for (int count = 0; count < browsers.length && browser == null; count++) {
				// ִ�д��룬��brower��ֵ��������
				// ������������̴����ɹ��ˣ�==0�Ǳ�ʾ����������
				if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
					browser = browsers[count];
				if (browser == null)
					throw new Exception("Could not find web browser");
				else
					// ���ֵ�������Ѿ��ɹ��ĵõ���һ�����̡�
					Runtime.getRuntime().exec(new String[] { browser, url });
			}
		}
	}
}