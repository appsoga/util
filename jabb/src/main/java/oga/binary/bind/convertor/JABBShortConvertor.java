package oga.binary.bind.convertor;

import java.nio.ByteBuffer;

import oga.binary.bind.JABBElement;
import oga.binary.bind.annotation.BinaryTypes;

public class JABBShortConvertor extends AbstractJABBConvertor {
	@Override
	protected Class<?> getSupportedClass() {
		return Short.class;
	}
	
	@Override
	protected Class<?> getSupportedPrimitiveClass() {
		return Short.TYPE;
	}

	@Override
	protected String convertToString(Object obj, JABBElement field) {
			return ((Short)obj).toString();
	}

	@Override
	protected ByteBuffer convertToByteBuffer(Object obj, JABBElement field) {
		ByteBuffer buffer = null;
		Short value = 0;
		if(obj != null){
			value = (Short)obj;
		}
		buffer = ByteBuffer.allocate(field.length());
		switch (field.type()) {
		case BinaryTypes.BYTE:
			getBytesFromObject(buffer, value, field.length());
			break;
		case BinaryTypes.INT32:
			buffer.putInt(value.intValue());
			break;
		case BinaryTypes.INT64:
			buffer.putLong(value.longValue());
			break;
		case BinaryTypes.INT16:
		default:
			buffer.putShort(value.shortValue());
			break;
		}
		buffer.flip();
		return buffer;
	}
	
	private void getBytesFromObject(ByteBuffer buffer, Short value, int length){
		Short shortVal = value;
		int bitLength = length * 8;
		byte bytePad = 0x00;
		for(int i=0; i < bitLength ; i+=8){
			if(i < Short.SIZE){
				shortVal = (short) (value >> i);
				buffer.put(shortVal.byteValue());
			} else {
				buffer.put(bytePad);
			}
		}
	}

	@Override
	protected Object restoreByString(String str, JABBElement field) {
		return Short.parseShort(str);
	}

	@Override
	protected Object restoreByByteBuffer(ByteBuffer buffer, JABBElement field) {
		Short value = null;
		switch (field.type()) {
		case BinaryTypes.BYTE:
			value = getObjectFromBytes(buffer, field.length());
			break;
		case BinaryTypes.INT32:
			Integer intValue = buffer.getInt();
			value = intValue.shortValue();
			break;
		case BinaryTypes.INT64:
			Long longValue = buffer.getLong();
			value = longValue.shortValue();
			break;
		case BinaryTypes.INT16:
		default:
			value = buffer.getShort();
			break;
		}
		return value;
	}

	private Short getObjectFromBytes(ByteBuffer buffer, int length) {
		int bitLength = length * 8;
		Short result = 0;
		for(int i=0; i < bitLength ; i+=8){
			if(i < Short.SIZE){
				result = (short) (result + ((short)(buffer.get()&0xFF) << i));
			} else {
				buffer.get();
			}
		}
		return result;
	}

}
