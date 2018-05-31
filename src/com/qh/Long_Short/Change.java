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
    	System.out.println("请输入原始长链接:");
		Scanner sc = new Scanner(System.in);
		String sLongUrl = sc.nextLine();
		String result=null;
		Date d = new Date();
		System.out.println(d);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String dateNowStr = sdf.format(d);
		try {
			Connection con;
			// 驱动程序名
			Connection con1, con2;
			String driver = "com.mysql.cj.jdbc.Driver";
			// URL指向要访问的数据库名url
			String url = "jdbc:mysql://localhost:3306/url?serverTimezone=UTC&useSSL=false&characterEncoding=utf8";
			// MySQL配置时的用户名
			String user = "root";
			// MySQL配置时的密码
			String password = "123456";
			Class.forName(driver);
			// getConnection()方法，连接MySQL数据库
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			System.out.println("Success connect Mysql server!");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from url_exchange where Long_url like \"%" + sLongUrl + "%\"");
			if (rs.next() == true) {
				System.out.println("该网址已经有对应的短链接，您可以直接使用:" + rs.getString(3));
				System.out.println("该短链接的自定义key值(若不为空的话):" + rs.getString(4));
				int newcount = rs.getInt(1) + 1;// 将该链接对应的计数+1
				System.out.println("您是否要打开当前网页(Y/N):");
				char s = sc.next().charAt(0);
				if (s == 'Y') {
				if(rs.getString(4)==null)
				{
					System.out.println("请输入短链接:");
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
						stmt.executeUpdate(sql2);// 执行SQL语句
						System.out.println("修改数据库成功");// 将访问次数+1
						System.out.println("网页打开成功");
						con1.close();
					}
				}
				
					else {
						System.out.println("请输入短链接对应的key值:");
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
							stmt.executeUpdate(sql2);// 执行SQL语句
							System.out.println("修改数据库成功");// 将访问次数+1
							System.out.println("网页打开成功");
							openURL(rs1.getString(2));
							con1.close();
					}
				}
				}
				else {
					System.out.println("请重新运行程序以便下一次的操作");
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
				String[] aResult = shortUrl(sLongUrl);// 将产生4组6位字符串
				long t = System.currentTimeMillis();// 获得当前时间的毫秒数
				Random random = new Random(t);// 作为种子数传入到Random的构造器中,避免生成伪随机数
				int j = random.nextInt(4);// 产成4以内随机数
				System.out.println("生成的短链接:" + aResult[j]);// 随机取一个作为短链
				System.out.println("您是否要为短链接设置一个自定义的key(Y/N):");
				char s1 = sc.next().charAt(0);
				String key = null;
				if (s1 == 'Y') {
					System.out.println("请输入您自定义的key值:");
					Scanner sc1 = new Scanner(System.in);
					key = sc1.nextLine();
				}
				String sql = "insert into url_exchange values('1','" + sLongUrl + "','" + aResult[j] + "','" + key
						+ "','" + dateNowStr + "')";
				stmt.executeUpdate(sql);
				System.out.println("插入到数据库成功");
				System.out.println("您是否要打开当前网页(Y/N):");
				char s2 = sc.next().charAt(0);
				if (s2 == 'Y') {
					if (s1 == 'Y') {
						
						System.out.println("请输入您刚才设置的短链接Key值:");
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
					
						System.out.println("请输入短链接:");
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
		// 可以自定义生成 MD5 加密字符传前的混合 KEY
		String key = "test";
		// 要使用生成 URL 的字符
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
				"q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
				"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z"

		};
		// 对传入网址进行 MD5 加密
		String hex = md5ByHex(key + url);

		String[] resUrl = new String[4];
		for (int i = 0; i < 4; i++) {

			// 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
			String sTempSubString = hex.substring(i * 8, i * 8 + 8);

			// 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用long ，则会越界
			long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
			String outChars = "";
			for (int j = 0; j < 6; j++) {
				// 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
				long index = 0x0000003D & lHexLong;
				// 把取得的字符相加
				outChars += chars[(int) index];
				// 每次循环按位右移 5 位
				lHexLong = lHexLong >> 5;
			}
			// 把字符串存入对应索引的输出数组
			resUrl[i] = outChars;
		}
		return resUrl;
	}

	/**
	 * MD5加密(32位大写)
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

	// 打开网页
	public static void openURL(String url) {
		try {
			browse(url);
		} catch (Exception e) {
		}
	}

	// 根据不同的操作系统来选择打开对应网页的方式
	private static void browse(String url) throws Exception {
		// 获取操作系统的名字
		String osName = System.getProperty("os.name", "");
		if (osName.startsWith("Mac OS")) {
			// Mac的打开方式
			Method openURL = Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL",
					new Class[] { String.class });
			openURL.invoke(null, new Object[] { url });
		} else if (osName.startsWith("Windows")) {
			// windows的打开方式。
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		} else {
			// Unix or Linux的打开方式
			String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
			String browser = null;
			for (int count = 0; count < browsers.length && browser == null; count++) {
				// 执行代码，在brower有值后跳出，
				// 这里是如果进程创建成功了，==0是表示正常结束。
				if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
					browser = browsers[count];
				if (browser == null)
					throw new Exception("Could not find web browser");
				else
					// 这个值在上面已经成功的得到了一个进程。
					Runtime.getRuntime().exec(new String[] { browser, url });
			}
		}
	}
}