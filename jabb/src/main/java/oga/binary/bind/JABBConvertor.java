package oga.binary.bind;

import java.nio.ByteBuffer;

public interface JABBConvertor {

	boolean supports(Class<?> clazz);

	ByteBuffer convert(Object obj, JABBElement element) throws JABBException;

	Object restore(ByteBuffer buffer, JABBElement element) throws JABBException;

}
