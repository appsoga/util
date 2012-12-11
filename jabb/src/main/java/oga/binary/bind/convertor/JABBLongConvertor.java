package oga.binary.bind.convertor;

import java.nio.ByteBuffer;

import oga.binary.bind.JABBElement;
import oga.binary.bind.annotation.BinaryTypes;

public class JABBLongConvertor extends AbstractJABBConvertor {
	@Override
	protected Class<?> getSupportedClass() {
		return Long.class;
	}
	
	@Override
	protected Class<?> getSupportedPrimitiveClass() {
		return Long.TYPE;
	}

	@Override
	protected String convertToString(Object obj, JABBElement field) {
		return ((Long)obj).toString();
	}

	@Override
	protected ByteBuffer convertToByteBuffer(Object obj, JABBElement field) {
		ByteBuffer buffer = null;
		Long value = 0L;
		if(obj != null){
			value = (Long)obj;
		}
		buffer = ByteBuffer.allocate(field.length());
		switch (field.type()) {
		case BinaryTypes.BYTE:
			getBytesFromObject(buffer, value, field.length());
			break;
		case BinaryTypes.INT16:
			buffer.putShort(value.shortValue());
			break;
		case BinaryTypes.INT32:
			buffer.putInt(value.intValue());
			break;
		case BinaryTypes.INT64:
		default:
			buffer.putLong(value.longValue());
			break;
		}
		buffer.flip();
		return buffer;
	}

	private void getBytesFromObject(ByteBuffer buffer, Long value, int length){
		Long longVal = value;
		int bitLength = length * 8;
		byte bytePad = 0x00;
		for(int i=0; i < bitLength ; i+=8){
			if(i < Long.SIZE){
				longVal = value >> i;
				buffer.put(longVal.byteValue());
			} else {
				buffer.put(bytePad);
			}
		}
	}

	@Override
	protected Object restoreByString(String str, JABBElement field) {
		return Long.parseLong(str);
	}

	@Override
	protected Object restoreByByteBuffer(ByteBuffer buffer, JABBElement field) {
		Long value = null;
		switch (field.type()) {
		case BinaryTypes.BYTE:
			value = getObjectFromBytes(buffer, field.length());
			break;
		case BinaryTypes.INT16:
			Short shortValue = buffer.getShort();
			value = shortValue.longValue();
			break;
		case BinaryTypes.INT32:
			Integer intValue = buffer.getInt();
			value = intValue.longValue();
			break;
		case BinaryTypes.INT64:
		default:
			value = buffer.getLong();
			break;
		}
		return value;
	}

	private Long getObjectFromBytes(ByteBuffer buffer, int length) {
		int bitLength = length * 8;
		Long result = 0L;
		for(int i=0; i < bitLength ; i+=8){
			if(i < Long.SIZE){
				result += (long)(buffer.get()&0xFF) << i;
			} else {
				buffer.get();
			}
		}
		return result;
	}
}
