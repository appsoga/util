package oga.binary.bind;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import oga.binary.bind.annotation.BinaryObject;

/**
 * 자바 객체를 파이트배열로 변환할 수 있다.
 * 
 * @author ohsangmok
 * 
 */
public class JABBMarshaller {
	private static final int DEFAULT_BUFSIZ = 1024;
	
	private JABBConversionProvider conversionProvider = new JABBConversionProvider();

	public JABBMarshaller() {
		super();
	}

	public JABBMarshaller(JABBConversionProvider jabbConversionProvider) {
		super();
		this.conversionProvider = jabbConversionProvider;
	}
	
	public void setConversionProvider(JABBConversionProvider conversionProvider) {
		this.conversionProvider = conversionProvider;
	}
	
	public JABBConversionProvider getConversionProvider() {
		if(conversionProvider == null)
			conversionProvider = new JABBConversionProvider();
		return conversionProvider;
	}

	public byte[] marshall(Object source) throws JABBMarshallException,
			JABBUnsupportedDataTypeException, IllegalArgumentException {

		if (source == null)
			throw new IllegalArgumentException("source, must not be null");

		BinaryObject ann = source.getClass().getAnnotation(BinaryObject.class);
		if (ann == null)
			throw new JABBUnsupportedDataTypeException(
					"annotation kr.co.pmgrow.jabb.JABBObject");

		ArrayList<JABBElement> elementList = JABBContextFinder.getFieldMap(source.getClass());
		ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFSIZ);
		try {
			for (JABBElement field : elementList) {
				Object value = field.get(source);
				byte[] bytes = null;
				if(field.classType().isAnnotationPresent(BinaryObject.class)){
					bytes = marshall(value);
					buffer = checkBuffer(buffer, bytes.length);
					buffer.put(bytes);
				}else{
					ByteBuffer fieldBuffer = getConversionProvider().toByte(value, field);
					buffer = checkBuffer(buffer, fieldBuffer.limit());
					buffer.put(fieldBuffer);
				}
			}
			buffer.flip();
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] bytes = new byte[buffer.limit()];
		buffer.get(bytes);
		return bytes;
	}

	/**
	 * buffer 크기를 확인해서 작을경우 DEFAULT_BUFSIZ 만큼 늘린다. 
	 * @param buffer
	 * @param addLength
	 * @return
	 */
	private ByteBuffer checkBuffer(ByteBuffer buffer, int addLength) {
		if((buffer.limit() - buffer.position()) < addLength){
			ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() + addLength + DEFAULT_BUFSIZ);
			newBuffer.put(buffer);
			buffer = null;
			buffer = newBuffer;
		}
		return buffer;
	}
}
