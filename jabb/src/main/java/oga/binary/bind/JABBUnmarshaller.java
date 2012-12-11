package oga.binary.bind;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import oga.binary.bind.annotation.BinaryObject;

/**
 * 바이트배열에서 데이터를 읽어 자바객체에 매핑하여 준다.
 * 
 * @author ohsangmok
 * 
 */
public class JABBUnmarshaller {

	private JABBConversionProvider conversionProvider;

	public JABBUnmarshaller() {
		super();
	}

	public JABBUnmarshaller(JABBConversionProvider jabbConversionProvider) {
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

	public <T> T unmarshall(byte[] source, Class<T> clazz) throws JABBException,
			JABBUnsupportedDataTypeException, IllegalArgumentException {

		BinaryObject ann = clazz.getAnnotation(BinaryObject.class);
		if (ann == null)
			throw new JABBUnsupportedDataTypeException(
					"annotation kr.co.pmgrow.jabb.JABBObject");

		if (source == null)
			throw new IllegalArgumentException("srouce is null.");

		try {
			T obj = clazz.newInstance();
			ArrayList<JABBElement> elementList = JABBContextFinder.getFieldMap(clazz);
			
			int offset = 0;
			for (JABBElement field : elementList) {
				Object value = null;
				if(field.classType().isAnnotationPresent(BinaryObject.class)){
					ByteBuffer buffer = ByteBuffer.wrap(source, offset, source.length - offset);
					byte[] newSource = new byte[field.length()];
					buffer.get(newSource);
					value = unmarshall(newSource, field.classType());
				}else{
					ByteBuffer buffer = ByteBuffer.wrap(source, offset, field.length());
					value = getConversionProvider().toObject(buffer, field);
				}
				if (value != null)
					field.set(obj, value);
				offset += field.length();
			}
			
			if (source.length != offset) {
				System.out.println(String.format("%d = %d", source.length, offset));
				throw new JABBException("Message does not fit the size of the buffer.");
			}
			
			return obj;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
}
