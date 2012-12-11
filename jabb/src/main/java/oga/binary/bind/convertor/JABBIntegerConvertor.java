package oga.binary.bind.convertor;

import java.nio.ByteBuffer;

import oga.binary.bind.JABBElement;
import oga.binary.bind.annotation.BinaryTypes;

public class JABBIntegerConvertor extends AbstractJABBConvertor {

	@Override
	protected Class<?> getSupportedClass() {
		return Integer.class;
	}

	@Override
	protected Class<?> getSupportedPrimitiveClass() {
		return Integer.TYPE;
	}
	
	@Override
	protected String convertToString(Object obj, JABBElement field) {
		return ((Integer)obj).toString();
	}

	@Override
	protected ByteBuffer convertToByteBuffer(Object obj, JABBElement field) {
		ByteBuffer buffer = null;
		Integer value = 0;
		if(obj != null){
			value = (Integer)obj;
		}
		buffer = ByteBuffer.allocate(field.length());
		switch (field.type()) {
		case BinaryTypes.BYTE:
			getBytesFromObject(buffer, value, field.length());
			break;
		case BinaryTypes.INT16:
			buffer.putShort(value.shortValue());
			break;
		case BinaryTypes.INT64:
			buffer.putLong(value.longValue());
			break;
		case BinaryTypes.INT32:
		default:
			buffer.putInt(value.intValue());
			break;
		}
		buffer.flip();
		return buffer;
	}
	
	private void getBytesFromObject(ByteBuffer buffer, Integer value, int length){
		Integer intVal = value;
		int bitLength = length * 8;
		byte bytePad = 0x00;
		for(int i=0; i < bitLength ; i+=8){
			if(i < Integer.SIZE){
				intVal = value >> i;
				buffer.put(intVal.byteValue());
			} else {
				buffer.put(bytePad);
			}
		}
	}

	@Override
	protected Object restoreByString(String str, JABBElement field) {
		return Integer.parseInt(str);
	}

	@Override
	protected Object restoreByByteBuffer(ByteBuffer buffer, JABBElement field) {
		Integer value = null;
		switch (field.type()) {
		case BinaryTypes.BYTE:
			value = getObjectFromBytes(buffer, field.length());
			break;
		case BinaryTypes.INT16:
			Short shortValue = buffer.getShort();
			value = shortValue.intValue();
			break;
		case BinaryTypes.INT64:
			Long longValue = buffer.getLong();
			value = longValue.intValue();
			break;
		case BinaryTypes.INT32:
		default:
			value = buffer.getInt();
			break;
		}
		return value;
	}

	private Integer getObjectFromBytes(ByteBuffer buffer, int length) {
		int bitLength = length * 8;
		Integer result = 0;
		for(int i=0; i < bitLength ; i+=8){
			if(i < Integer.SIZE){
				result += (int)(buffer.get()&0xFF) << i;
			} else {
				buffer.get();
			}
		}
		return result;
	}


}
