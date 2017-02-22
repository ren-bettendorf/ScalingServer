package cs455.scaling.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingFunction {

	private static final HashingFunction instance = new HashingFunction();

	private HashingFunction() {  }

	public static HashingFunction getInstance() {
		return instance;
	}

	public String SHA1FromBytes(byte[] data) throws NoSuchAlgorithmException{
		MessageDigest digest = MessageDigest.getInstance("SHA1");
		byte[] hash = digest.digest(data);
		BigInteger hashInt = new BigInteger(1, hash);

		return hashInt.toString(16);
	}
}
