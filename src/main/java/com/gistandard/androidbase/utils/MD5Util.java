/*
 * 文件名：MD5Util.java 版权：Copyright ArcherMind Technology.Co.,Ltd. All Rights
 * Reserved. 2013-3-13 下午12:06:07
 */
package com.gistandard.androidbase.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util {
	
	/**
	 * 123456加密后是：123456:E10ADC3949BA59ABBE56E057F20F883E
	 */
	

	/** * 16进制字符集 */
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/** * 指定算法为MD5的MessageDigest */
	private static MessageDigest messageDigest = null;

	/** * 初始化messageDigest的加密算法为MD5 */
	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * * 获取文件的MD5值
	 * 
	 * @param file
	 *            目标文件
	 * @return MD5字符串
	 */
	public synchronized static String getFileMD5String (File file) throws OutOfMemoryError, IOException{
		String ret = "";
		FileInputStream in = null;
		FileChannel ch = null;
		try {
			in = new FileInputStream(file);
			ch = in.getChannel();
			ByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
					file.length());
			
			messageDigest.update(byteBuffer);
			ret = bytesToHex(messageDigest.digest());
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ch != null) {
				try {
					ch.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
	
	
    /**
     * 给文件进行md5加密
     * 
     * @param file 要加密的文件
     * @return 文件加密好后形成的字符串
     */
    public static String getMD5(File file)
    {
        FileInputStream fis = null;
        byte[] buffer = new byte[8192];
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            int length = -1;
            while ((length = fis.read(buffer)) != -1)
            {
                md.update(buffer, 0, length);
            }
            return toHexString(md.digest());
        }
        catch (IOException ex)
        {
            return null;
        }
        catch (NoSuchAlgorithmException ex)
        {
            return null;
        }
        finally
        {
            buffer = null;
            try
            {
                if (fis != null)
                {
                    fis.close();
                }
            }
            catch (IOException ex)
            {
            }
        }
    }

	/**
	 * * 获取文件的MD5值
	 * 
	 * @param fileName
	 *            目标文件的完整名称
	 * @return MD5字符串
	 */
	public static String getFileMD5String(String fileName) {
		try {
			return getFileMD5String(new File(fileName));
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * * MD5加密字符串
	 * 
	 * @param str
	 *            目标字符串
	 * @return MD5加密后的字符串
	 */

	public static String getMD5String(String str) {

		return getMD5String(str.getBytes());
	}

	/**
	 * * MD5加密以byte数组表示的字符串
	 * 
	 * @param bytes
	 *            目标byte数组
	 * @return MD5加密后的字符串
	 */

	public static String getMD5String(byte[] bytes) {
		messageDigest.update(bytes);
		return bytesToHex(messageDigest.digest());
	}

	/**
	 * * 校验密码与其MD5是否一致
	 * 
	 * @param pwd
	 *            密码字符串
	 * @param md5
	 *            基准MD5值
	 * @return 检验结果
	 */
	public static boolean checkPassword(String pwd, String md5) {
		return getMD5String(pwd).equalsIgnoreCase(md5);
	}

	/**
	 * * 校验密码与其MD5是否一致
	 * 
	 * @param pwd
	 *            以字符数组表示的密码
	 * @param md5
	 *            基准MD5值
	 * @return 检验结果
	 */
	public static boolean checkPassword(char[] pwd, String md5) {
		return checkPassword(new String(pwd), md5);

	}

	/**
	 * * 检验文件的MD5值
	 * 
	 * @param file
	 *            目标文件
	 * @param md5
	 *            基准MD5值
	 * @return 检验结果
	 */
	public static boolean checkFileMD5(File file, String md5) {
		try {
			return getFileMD5String(file).equalsIgnoreCase(md5);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * * 检验文件的MD5值
	 * 
	 * @param fileName
	 *            目标文件的完整名称
	 * @param md5
	 *            基准MD5值
	 * @return 检验结果
	 */
	public static boolean checkFileMD5(String fileName, String md5) {
		return checkFileMD5(new File(fileName), md5);

	}

	/**
	 * * 将字节数组转换成16进制字符串
	 * 
	 * @param bytes
	 *            目标字节数组
	 * @return 转换结果
	 */
	public static String bytesToHex(byte bytes[]) {
		return bytesToHex(bytes, 0, bytes.length);

	}

	/**
	 * * 将字节数组中指定区间的子数组转换成16进制字符串
	 * 
	 * @param bytes
	 *            目标字节数组
	 * @param start
	 *            起始位置（包括该位置）
	 * @param end
	 *            结束位置（不包括该位置）
	 * @return 转换结果
	 */
	public static String bytesToHex(byte bytes[], int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < start + end; i++) {
			sb.append(byteToHex(bytes[i]));
		}
		return sb.toString();

	}

	/**
	 * * 将单个字节码转换成16进制字符串
	 * 
	 * @param bt
	 *            目标字节
	 * @return 转换结果
	 */
	public static String byteToHex(byte bt) {
		return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];

	}

	// 可逆的加密算法
	public static String getEncryption(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

	// 可逆的解密算法
	public static String getDeciphering(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;

	}
	
	public static String getStreamHash(InputStream stream)    
    {    
        String hash=null;    
        byte[] buffer = new byte[1024];    
        BufferedInputStream in=null;    
        try   
        {    
            in=new BufferedInputStream(stream);    
            int numRead = 0;      
            while ((numRead = in.read(buffer)) > 0)     
            {      
            	messageDigest.update(buffer, 0, numRead);      
            }      
            in.close();     
            hash=toHexString(messageDigest.digest());      
        }catch (Exception e)     
        {    
            if(in!=null)try{in.close();}catch (Exception ex) {ex.printStackTrace();}    
            e.printStackTrace();    
        }    
        return hash;    
    }
	
	private static String toHexString(byte[] b) {      
        StringBuilder sb = new StringBuilder(b.length * 2);      
        for (int i = 0; i < b.length; i++) {      
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);      
            sb.append(hexChar[b[i] & 0x0f]);      
        }      
        return sb.toString();      
    }
	
	private static char[] hexChar = {'0', '1', '2', '3',      
	            '4', '5', '6', '7',      
	            '8', '9', 'a', 'b',      
	            'c', 'd', 'e', 'f'};
	
	// ajax请求头信息加密用
	public static String toHexStrign(byte[] buf) {
		char[] hex = "0123456789abcdef".toCharArray();
		int nbyte = buf.length;
		char[] result = new char[nbyte * 2];
		for (int i = 0; i < nbyte; i++) {

			result[i * 2] = hex[(0xf0 & buf[i]) >>> 4];
			result[i * 2 + 1] = hex[(0x0f & buf[i])];
		}
		return new String(result);
	}
	// ajax请求头信息加密用
	public static String calMd5(String src) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(src.getBytes("utf8"));
			byte[] re = md.digest();
			return toHexStrign(re);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}



    /**
     * 
     * 功能描述: <br>
     * 文件sha1值的计算 适用于上G大的文件 〈功能详细描述〉
     * 
     * @param path
     * @return
     * @throws OutOfMemoryError
     * @throws IOException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String getFileSha1(String path) throws OutOfMemoryError, IOException {
        File file = new File(path);
        FileInputStream in = new FileInputStream(file);
        MessageDigest messagedigest;
        try {
            messagedigest = MessageDigest.getInstance("SHA-1");

            byte[] buffer = new byte[1024 * 1024 * 10];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                // 该对象通过使用 update（）方法处理数据
                messagedigest.update(buffer, 0, len);
            }
            // 对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。
            return byte2hex(messagedigest.digest());
        } catch (NoSuchAlgorithmException e) {
            // NQLog.e("getFileSha1->NoSuchAlgorithmException###", e.toString());
            // e.printStackTrace();
        } catch (OutOfMemoryError e) {
            // NQLog.e("getFileSha1->OutOfMemoryError###", e.toString());
            // e.printStackTrace();
            throw e;
        } finally {
            in.close();
        }
        return null;
    }

    /**
     * 
     * 功能描述: <br>
     * byte数组转String 〈功能详细描述〉
     * 
     * @param b
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp);
            } else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }
    
    public static void main(String[] args) {
    	String[] aa = {"pcia","gzkj","wlss","hubsystem"};
    	for(int i = 0; i < aa.length; i++){
    		System.out.println(MD5Util.calMd5(String.valueOf(aa[i])));
    	}
	}
}
