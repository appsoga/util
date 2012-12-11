package oga.binary.bind.convertor;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import oga.binary.bind.JABBElement;
import oga.binary.bind.JABBUnsupportedDataTypeException;
import oga.binary.bind.annotation.BinaryTypes;

public class JABBDateConvertor extends AbstractJABBConvertor {
	private static final String defaultDateformat = "yyyyMMddhhmmss";

	@Override
	protected Class<?> getSupportedClass() {
		return java.util.Date.class;
	}

	@Override
	protected Class<?> getSupportedPrimitiveClass() {
		return null;
	}
	
	@Override
	protected String convertToString(Object obj, JABBElement field) throws JABBUnsupportedDataTypeException {
		String dateformat = field.dateformat();
		if(dateformat == null || dateformat.isEmpty()){
			if(!isDefaultType(field))
				throwJABBUnsupportedDataTypeException(field.type());
			dateformat = defaultDateformat;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
			return sdf.format(obj);
		} catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected ByteBuffer convertToByteBuffer(Object obj, JABBElement field) throws JABBUnsupportedDataTypeException {
		throwJABBUnsupportedDataTypeException(field.type());
		return null;
	}

	@Override
	protected Object restoreByString(String str, JABBElement field) throws JABBUnsupportedDataTypeException {
		String dateformat = field.dateformat();
		if(dateformat == null || dateformat.isEmpty()){
			if(!isDefaultType(field))
				throwJABBUnsupportedDataTypeException(field.type());
			dateformat = defaultDateformat;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * default type으로 converting가능한지 확인한다
	 * @param field
	 * @return
	 */
	private static boolean isDefaultType(JABBElement field){
		int length = defaultDateformat.length();
		int bcdLen = length/2;
		switch (field.type()) {
		case BinaryTypes.BCD:
		case BinaryTypes.BCD7:
			if(field.length() == bcdLen)
				return true;
			break;
		case BinaryTypes.CHAR:
			if(field.length() == length)
				return true;
			break;
		default:
			break;
		}
		return false;
	}
	
	@Override
	protected Object restoreByByteBuffer(ByteBuffer buffer, JABBElement field) throws JABBUnsupportedDataTypeException {
		throwJABBUnsupportedDataTypeException(field.type());
		return null;
	}

}
