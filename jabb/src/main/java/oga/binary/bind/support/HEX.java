package oga.binary.bind.support;

public class HEX {

	private static final String HEXES = "0123456789ABCDEF";

	public static String toHexString(byte b) {
		return String.format("%s%s", HEXES.charAt((b & 0xF0) >> 4), HEXES.charAt((b & 0x0F)));
	}

	public static String toHexString(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(toHexString(b));
		}
		return hex.toString();
	}

	public static String getFormatedString(byte[] src) {
		StringBuilder sb = new StringBuilder();
		if (src != null) {
			for (int i = 0; i < src.length; i++) {
				if (i % 16 == 0)
					sb.append('\n').append(String.format("0x%08x ", i));
				
				if (i % 4 == 0 && 1 % 16 != 0)
					sb.append(' ');
				if (i % 16 != 0)
					sb.append(' ');
				
				byte b = src[i];
				sb.append(HEX.toHexString(b));
			}
		}
		return sb.toString();
	}

	public static void print(byte[] src) {
		if (src == null)
			return;
		System.out.println(HEX.getFormatedString(src));
	}

}
