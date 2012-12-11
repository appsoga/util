package oga.binary.bind.convertor;

import java.nio.ByteBuffer;
import oga.binary.bind.JABBElement;
import oga.binary.bind.annotation.BinaryTypes;

public class JABBStringConvertor extends AbstractJABBConvertor {
	
	@Override
	protected Class<?> getSupportedClass() {
		return String.class;
	}
	
	@Override
	protected Class<?> getSupportedPrimitiveClass() {
		return null;
	}

	@Override
	protected String convertToString(Object obj, JABBElement field) {
		return (String) obj;
	}

	@Override
	protected ByteBuffer convertToByteBuffer(Object obj, JABBElement field) {
		ByteBuffer buffer = null;
		String value = "";
		if(obj != null){
			value = (String)obj;
		}
		buffer = ByteBuffer.allocate(field.length());
		switch (field.type()) {
		case BinaryTypes.INT16:
			buffer.putShort(Short.parseShort(value));
			break;
		case BinaryTypes.INT32:
			buffer.putInt(Integer.parseInt(value));
			break;
		case BinaryTypes.INT64:
			buffer.putLong(Long.parseLong(value));
			break;
		case BinaryTypes.BYTE:
		default:
			buffer.put(value.getBytes(charset));
			break;
		}
		buffer.flip();
		return buffer;
	}

	@Override
	protected Object restoreByString(String str, JABBElement field) {
		return str.trim();
	}

	@Override
	protected Object restoreByByteBuffer(ByteBuffer buffer, JABBElement field) {
		String value = null;
		switch (field.type()) {
		case BinaryTypes.INT16:
			Short shortValue = buffer.getShort();
			value = shortValue.toString();
			break;
		case BinaryTypes.INT32:
			Integer intValue = buffer.getInt();
			value = intValue.toString();
			break;
		case BinaryTypes.INT64:
			Long longValue = buffer.getLong();
			value = longValue.toString();
			break;
		case BinaryTypes.BYTE:
		default:
			byte[] dst = new byte[field.length()];
			buffer.get(dst);
			value = new String(dst,charset);
			break;
		}
		return value;
	}
}
