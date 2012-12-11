package oga.binary.bind.convertor;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import oga.binary.bind.JABBConvertor;
import oga.binary.bind.JABBElement;
import oga.binary.bind.JABBMarshallException;
import oga.binary.bind.JABBUnmarshallException;
import oga.binary.bind.JABBUnsupportedDataTypeException;
import oga.binary.bind.annotation.BinaryTypes;
import oga.binary.bind.support.BCD;

public abstract class AbstractJABBConvertor implements JABBConvertor {
	public static final Character DEFAULT_PADDING_STR = ' ';
	public static final Character DEFAULT_BCD_PADDING_STR = '0';
	protected Character paddingChar = DEFAULT_PADDING_STR;
	public static final String BCD_MATCHER = "^[0-9]*$";
	protected Charset charset = Charset.defaultCharset();
	
	protected abstract Class<?> getSupportedClass();
	protected abstract Class<?> getSupportedPrimitiveClass();
	
	public boolean supports(Class<?> clazz) {
		boolean supported = false;
		if(clazz.isPrimitive()){
			if(getSupportedPrimitiveClass()==clazz)
				supported = true;
		}else{
			Class<?> supportedClass = getSupportedClass();
			if (clazz == supportedClass)
				supported = true;
			else
				supported = supportedClass.isAssignableFrom(clazz);
		}
		return supported;
	}

	/**
	 * BinaryTypes.CHAR 일경우 Object를 String으로 변환한다.
	 * @param obj
	 * @param field
	 * @return
	 * @throws JABBUnsupportedDataTypeException 
	 */
	protected abstract String convertToString(Object obj, JABBElement field) throws JABBUnsupportedDataTypeException;
	/**
	 * Object를 ByteBuffer로 변환한다.
	 * @param obj
	 * @param field
	 * @return
	 * @throws JABBUnsupportedDataTypeException 
	 */
	protected abstract ByteBuffer convertToByteBuffer(Object obj, JABBElement field) throws JABBUnsupportedDataTypeException;
	
	public ByteBuffer convert(Object obj, JABBElement field)
			throws JABBMarshallException, JABBUnsupportedDataTypeException {
		ByteBuffer buffer = ByteBuffer.allocate(field.length());
		switch (field.type()) {
		case BinaryTypes.CHAR:
			//TODO 캐릭터셋을 가져와야함. 
			String str = convertToString(obj, field);
			str = padding(str, field.length(), paddingChar);
			buffer.put(str.getBytes(charset));
			break;
		case BinaryTypes.BCD:
		case BinaryTypes.BCD7:
			String bcdStr = convertToString(obj, field);
			if(!bcdStr.matches(BCD_MATCHER))
				throwJABBMarshallExceptionWrongValue(field.type(), bcdStr);
			bcdStr = padding(bcdStr, field.length()*2, DEFAULT_BCD_PADDING_STR);
			buffer.put(BCD.encode(bcdStr));
			break;
		default:
			buffer.put(convertToByteBuffer(obj,field));
			break;
		}
		buffer.flip();
		return buffer;
	}
	
	/**
	 * BinaryTypes.CHAR일 경우 문자열에 지정된 길이만큼의 패딩문자열을 더한다.
	 * @param str
	 * @param length
	 * @return
	 */
	public static String padding(String str, int length, Character paddingChar) {
		if(str != null){
			if(str.length() > length){
				str = str.substring(0, length);
			}else if(str.length() < length){
				String res = "";
				for(int i=0; i < length - str.length(); i++){
					res += paddingChar;
				}
				str += res;
			}
		}
		return str;
	}

	/**
	 * String을 Object로 변환한다.
	 * @param str
	 * @param field
	 * @return
	 * @throws JABBUnsupportedDataTypeException 
	 */
	protected abstract Object restoreByString(String str, JABBElement field) throws JABBUnsupportedDataTypeException;
	/**
	 * ByteBuffer에서 Object로 변환한다.
	 * @param buffer
	 * @param field
	 * @return
	 * @throws JABBUnsupportedDataTypeException 
	 */
	protected abstract Object restoreByByteBuffer(ByteBuffer buffer, JABBElement field) throws JABBUnsupportedDataTypeException;
	
	public Object restore(ByteBuffer buffer, JABBElement field)
			throws JABBUnmarshallException, JABBUnsupportedDataTypeException {
		if(buffer.remaining() < field.length()){
			throw new JABBUnmarshallException(
					"The contents of the buffer is not enough. BinaryType:"+ field.type() 
					+"remain:"+buffer.remaining()+", require:"+ field.length());
		}
		
		Object value = null;
		
		switch (field.type()) {
		case BinaryTypes.CHAR:
			//TODO 캐릭터셋을 가져와야함. 
			byte[] strBytes = new byte[field.length()];
			buffer.get(strBytes);
			value = restoreByString(new String(strBytes, charset), field);
			break;
		case BinaryTypes.BCD:
		case BinaryTypes.BCD7:
			byte[] bcdBytes = new byte[field.length()];
			buffer.get(bcdBytes);
			String bcdStr = BCD.decode(bcdBytes);
			if(!bcdStr.matches(BCD_MATCHER))
				throwJABBUnMarshallExceptionWrongValue(field.type(), bcdStr);
			value = restoreByString(BCD.decode(bcdBytes), field);
			break;
		default:
			value = restoreByByteBuffer(buffer, field);
			break;
		}
		return value;
	}

	public Character getPaddingChar() {
		return paddingChar;
	}

	public void setPaddingChar(Character paddingChar) {
		this.paddingChar = paddingChar;
	}

	public void throwJABBUnsupportedDataTypeException(int dataType) throws JABBUnsupportedDataTypeException{
		throw new JABBUnsupportedDataTypeException("Unsupported "+BinaryTypes.getName(dataType)+" to "+getSupportedClass().getName());
	}
	
	private void throwJABBMarshallExceptionWrongValue(int dataType, Object value) throws JABBMarshallException {
		throw new JABBMarshallException("wrong value : "+value+",on "+BinaryTypes.getName(dataType)+" to "+getSupportedClass().getName());
	}
	
	private void throwJABBUnMarshallExceptionWrongValue(int dataType, Object value) throws JABBUnmarshallException {
		throw new JABBUnmarshallException("wrong value : "+value+",on "+BinaryTypes.getName(dataType)+" to "+getSupportedClass().getName());
	}
	
	public Charset getCharset() {
		return charset;
	}
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}
