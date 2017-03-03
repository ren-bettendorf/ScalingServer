package cs455.scaling.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class HashingFunction {

	private static final HashingFunction instance = new HashingFunction();

	private HashingFunction() {  }

	public static HashingFunction getInstance() {
		return instance;
	}

	public String SHA1FromBytes(byte[] data) throws NoSuchAlgorithmException{
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		byte[] hash = digest.digest(data);
		BigInteger hashInt = new BigInteger(1, hash);
		String hashcode = hashInt.toString(16);
		// Ensure message has size 40 so clients don't hang
		while(hashcode.length() < 40) {
			hashcode = "0" + hashcode;
		}
		return hashcode;
	}
}
