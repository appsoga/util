package oga.binary.bind.convertor;

import java.nio.ByteBuffer;

import oga.binary.bind.JABBElement;
import oga.binary.bind.annotation.BinaryTypes;

public class JABBCharacterConvertor extends AbstractJABBConvertor {
	@Override
	protected Class<?> getSupportedPrimitiveClass() {
		return Character.TYPE;
	}
	
	@Override
	protected Class<?> getSupportedClass() {
		return Character.class;
	}

	@Override
	protected String convertToString(Object obj, JABBElement field) {
		return ((Character)obj).toString();
	}

	@Override
	protected ByteBuffer convertToByteBuffer(Object obj, JABBElement field) {
		ByteBuffer buffer = null;
		char value = 0x00;
		if(obj != null){
			value = (Character)obj;
		}
		buffer = ByteBuffer.allocate(field.length());
		switch (field.type()) {
		case BinaryTypes.INT16:
			buffer.putShort((short)value);
			break;
		case BinaryTypes.INT32:
			buffer.putInt((int)value);
			break;
		case BinaryTypes.INT64:
			buffer.putLong((long)value);
			break;
		case BinaryTypes.BYTE:
		default:
			buffer.putChar(value);
			break;
		}
		buffer.flip();
		return buffer;
	}

	@Override
	protected Object restoreByString(String str, JABBElement field) {
		return (char)str.charAt(0);
	}

	@Override
	protected Object restoreByByteBuffer(ByteBuffer buffer, JABBElement field) {
		char value = 0x00;
		switch (field.type()) {
		case BinaryTypes.INT16:
			short shortValue = buffer.getShort();
			value = (char)shortValue;
			break;
		case BinaryTypes.INT32:
			int intValue = buffer.getInt();
			value = (char)intValue;
			break;
		case BinaryTypes.INT64:
			long longValue = buffer.getLong();
			value = (char)longValue;
			break;
		case BinaryTypes.BYTE:
		default:
			value = buffer.getChar();
			break;
		}
		return value;
	}

}
