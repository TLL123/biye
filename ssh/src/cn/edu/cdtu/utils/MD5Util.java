package cn.edu.cdtu.utils;

import java.security.MessageDigest;
/**
 * Md5����
 * @author Lee
 *
 */
public class MD5Util {
	
	public static String Md5(String src) {
		try {
			char[] number = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'A', 'B', 'C', 'D', 'E', 'F' };
			StringBuffer newbuf = new StringBuffer();
			byte[] s = src.getBytes(); // ����ǰ������
			MessageDigest message = MessageDigest.getInstance("MD5");
			byte[] ed = message.digest(s); // ���ܺ������
			for (byte b : ed) {
				newbuf.append(number[(b >> 4) & 0x0F]); // ��λ����
				newbuf.append(number[b & 0x0F]); // ��λ��������
			}
			return newbuf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	 public static void main(String[] args) {
		System.out.println(Md5("123456"));
	}
}
