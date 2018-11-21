package cipher;

public class CipherEncrypter {
	
    public static final String DEFAULT_KEY = "046c73498a53cbb732248168a3ced0ad";
	private String key;
	
	public CipherEncrypter(String key){
		this.key = key;
	}
	public String getEncodingKey(String from, String to) {
    	String key;
    	if (from.compareTo(to) > 0 || from.compareTo(to) == 0) {
    		key = from + to;
    	} else {
    		key = to + from;
    	}
    	return key;
    }

	public String encryptXor(String message) {
		String cipherMessage = "";
		int keyCharNum = 0;
		char[] charMessage = message.toCharArray();
		for (char c: charMessage){
			int charNum = (c^key.charAt(keyCharNum))%256;
			c = (char)charNum;
			cipherMessage+=c;
			keyCharNum = (keyCharNum + 1)% key.length();
		}
		return cipherMessage;
	}
	public String encryptCeasar(String message) {
		char[] charMessage = message.toCharArray();
		String ceasarMessage = "";
		int keyCharNum = 0;
		for (char c: charMessage){
			c = (char)(c + key.charAt(keyCharNum));
			//System.out.println("C = " + c + "int(C) = " + (int)c);
			//System.out.println((int)key.charAt(keyCharNum) + " to jest:" + key.charAt(keyCharNum));
			
			c %= 256;
			//System.out.println("POOOOO C = " + c + "int(C) = " + (int)c);
			keyCharNum = (keyCharNum + 1)% key.length();
			ceasarMessage += c;
		}
		return ceasarMessage;
	}
	public String decryptCeasar(String message) {
		char[] charMessage = message.toCharArray();
		String ceasarMessage = "";
		int keyCharNum = 0;
		for (char c: charMessage){
			int move = (int) key.charAt(keyCharNum);
			
			//System.out.println("C = " + c + "int(C) = " + (int)c);
			//System.out.println((int)key.charAt(keyCharNum) + " to jest:" + key.charAt(keyCharNum));
			move = ((c - move) % 256 + 256) % 256;
			c = (char) move;
			//System.out.println("POOOOO C = " + c + "int(C) = " + (int)c);
			keyCharNum = (keyCharNum + 1)% key.length();
			ceasarMessage += c;
		}
		return ceasarMessage;
	}

	public String encrypt(String message, String cipherKey) {
		if (cipherKey == "") {
			key = DEFAULT_KEY;
		}
		else {
			this.key = cipherKey;
		}
		MatrixCipher cipher = new MatrixCipher();
		message = encryptCeasar(message);
		message = encryptXor(message);
		message = cipher.encryptMatrixCipher(message);
		
		return message;
	}
	public String decrypt(String message, String cipherKey) {
		if (cipherKey == "") {
			key = DEFAULT_KEY;
		}
		else {
			this.key = cipherKey;
		}
		MatrixCipher cipher = new MatrixCipher();
		
		message = cipher.decryptMatrixCipher(message);
		message = encryptXor(message);
		message = decryptCeasar(message);
		
		return message;
	}
	
}
