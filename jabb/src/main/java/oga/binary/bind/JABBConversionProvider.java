package oga.binary.bind;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import oga.binary.bind.convertor.JABBCharacterConvertor;
import oga.binary.bind.convertor.JABBDateConvertor;
import oga.binary.bind.convertor.JABBIntegerConvertor;
import oga.binary.bind.convertor.JABBLongConvertor;
import oga.binary.bind.convertor.JABBShortConvertor;
import oga.binary.bind.convertor.JABBStringConvertor;

public class JABBConversionProvider {

	private Charset charset;

	private java.util.List<JABBConvertor> convertors;

	public JABBConversionProvider() {
		super();
		this.charset = Charset.defaultCharset();
		this.convertors = getDefaultConvertors();
	}

	public JABBConversionProvider(List<JABBConvertor> convertors) {
		super();
		this.charset = Charset.defaultCharset();
		this.convertors = convertors;
	}

	public ByteBuffer toByte(Object object, JABBElement field)
			throws JABBUnsupportedDataTypeException, JABBException {

		if (object == null)
			return ByteBuffer.allocate(field.length());

		ByteBuffer buffer = null;
		boolean supports = false;
		for (JABBConvertor c : getConvertors()) {
			if (c.supports(object.getClass())) {
				supports = true;
				buffer = c.convert(object, field);
				break;
			}
		}

		if (supports == false) {
			String msg = String.format("object class: %s", object.getClass());
			throw new JABBUnsupportedDataTypeException(msg);
		}

		return buffer;
	}

	public Object toObject(ByteBuffer buffer, JABBElement field)
			throws JABBException, InstantiationException,
			IllegalAccessException {
		Class<?> fieldClassType = field.classType();
		
		for (JABBConvertor c : getConvertors()) {
			if (c.supports(fieldClassType)) {
				return c.restore(buffer, field);
			} 
		}
		return null;
	}

	public java.util.List<JABBConvertor> getConvertors() {
		if (convertors == null) {
			convertors = getDefaultConvertors();
		}
		return convertors;
	}

	public void setConvertors(java.util.List<JABBConvertor> convertors) {
		this.convertors = convertors;
	}

	private java.util.List<JABBConvertor> getDefaultConvertors() {
		java.util.List<JABBConvertor> convs = new java.util.ArrayList<JABBConvertor>();
		convs.add(new JABBShortConvertor());
		convs.add(new JABBIntegerConvertor());
		convs.add(new JABBLongConvertor());
		convs.add(new JABBDateConvertor());
		convs.add(new JABBCharacterConvertor());
		convs.add(new JABBStringConvertor());
		return convs;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}
