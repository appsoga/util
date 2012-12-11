package oga.binary.bind;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import oga.binary.bind.annotation.BinaryElement;
import oga.binary.bind.annotation.BinaryObject;

public class JABBContextFinder {

	public static ArrayList<JABBElement> getFieldMap(Class<?> clazz)
			throws JABBUnsupportedDataTypeException {
		ArrayList<JABBElement> elementList = new ArrayList<JABBElement>();
		if (clazz.isAnnotationPresent(BinaryObject.class)) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(BinaryElement.class)) {
					elementList.add(new JABBElement(field));
				}
			}
		}
		Collections.sort(elementList);
		return elementList;
	}

}
