package oga.binary.bind.annotation;

public class BinaryTypes {
	/**
	 * java.lang.String type, The length must be. (default 1 bytes)
	 */
	public static final int UNDEFINED = 0x00;
	
	/**
	 * java.lang.String type, The length must be. (default 1 bytes)
	 */
	public static final int CHAR = 0x01;
	public static final int BYTE = 0x02;

	/**
	 * int or Integer type (2 bytes), big-endian.
	 */
	public static final int INT16 = 0x03;

	/**
	 * int or java.lang.Integer type (4 bytes), big-endian.
	 */
	public static final int INT32 = 0x04;

	/**
	 * int or java.lang.Integer type (8 bytes), big-endian.
	 */
	public static final int INT64 = 0x05;

	/**
	 * BCD
	 */
	public static final int BCD = 0x06;
	/**
	 * java.util.Date type (BCD7: 7 bytes)
	 */
	public static final int BCD7 = 0x07;

	
	/**
	 * BinaryTypes.type의 byte길이를 얻는다.
	 * 지원하지 않는 type일 경우 -1을 리턴한다.
	 * @param BinaryTypes.type
	 * @return
	 */
	public static int getByteLength(int type) {
		switch (type) {
		case BYTE:
		case CHAR:
			return 1;
		case INT16:
			return 2;
		case INT32:
			return 4;
		case INT64:
			return 8;
		case BCD:
			return 1;
		case BCD7:
			return 7;
		default:
			return -1;
		}
	}


	public static String getName(int type) {
		switch (type) {
		case BYTE:
			return "BYTE";
		case CHAR:
			return "CHAR";
		case INT16:
			return "INT16";
		case INT32:
			return "INT32";
		case INT64:
			return "INT64";
		case BCD:
			return "BCD";
		case BCD7:
			return "BCD7";
		default:
			return "UNDEFINED";
		}
	}

}
