package oga.binary.bind;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

import oga.binary.bind.annotation.BinaryElement;
import oga.binary.bind.annotation.BinaryObject;
import oga.binary.bind.annotation.BinaryTypes;

public class JABBElement implements Comparable<JABBElement> {

	private Field field;

	private int index;
	
	private int type;

	private int length = 0;
	
	private String dateformat = "";
	
	public JABBElement(Field field) throws JABBUnsupportedDataTypeException {
		super();
		this.field = field;
		this.field.setAccessible(true);

		BinaryElement element = field.getAnnotation(BinaryElement.class);
		this.index = element.order();
		
		//type 지정
		this.type = element.type();
		if(this.type == BinaryTypes.UNDEFINED){
			Type type = field.getType();
			if(type.getClass().equals(Short.class)){
				this.type = BinaryTypes.INT16;
			}else if(type.getClass().equals(Integer.class)){
				this.type = BinaryTypes.INT32;
			} else if(type.getClass().equals(Long.class)){
				this.type = BinaryTypes.INT64;
			} else if(type.getClass().equals(String.class)){
				this.type = BinaryTypes.CHAR;
			} else if(type.getClass().equals(Character.class)){
				this.type = BinaryTypes.CHAR;
			}
		}
		int typeMinLength = BinaryTypes.getByteLength(this.type);
		if(field.getType().isAnnotationPresent(BinaryObject.class)){
			ArrayList<JABBElement> elementList = JABBContextFinder.getFieldMap(field.getType());
			if(elementList != null){
				for(JABBElement jabbElement : elementList)
					this.length += jabbElement.length();
			}
		}else{
			this.length = (element.length() < typeMinLength ? typeMinLength : element.length());
		}
	}

	public int compareTo(JABBElement o) {
		Integer order1 = this.order();
		Integer order2 = o.order();
		return order1.compareTo(order2);
	}

	/**
	 * 클래스 필드의 타입(java.lang.String, java.lang.Integer, ...)을 반환한다.
	 * 
	 * @return
	 */
	public Class<?> classType() {
		return this.field.getType();
	}

	/**
	 * 바이너리를 처리하는 순서(스트림에서 데이터의 처리 순서).
	 * 
	 * @return
	 */
	public int order() {
		return index;
	}

	/**
	 * 바이너리를 처리하기위한 기본형 타입(int, char, long, ...)
	 * 
	 * @return
	 */
	public int type() {
		return type;
	}

	/**
	 * 바이너리에서 처리할 바이트수.
	 * 
	 * @return
	 */
	public int length() {
		return length;
	}

	/**
	 * Date포맷
	 * @return
	 */
	public String dateformat(){
		return dateformat;
	}
	
	public Class<?> getDeclaringClass() {
		return this.field.getDeclaringClass();
	}

	/**
	 * 객체데 해당하는 엘리먼의 값을 설정한다.
	 * 
	 * @since JDK1.0
	 * @See java.lang.Thread.setPriority(int)
	 * 
	 * @param obj
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void set(Object obj, Object value) throws IllegalArgumentException,
			IllegalAccessException {
		this.field.set(obj, value);
	}

	/**
	 * 객체에서 해당하는 엘리먼트의 값을 가져온다.
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object get(Object obj) throws IllegalArgumentException,
			IllegalAccessException {
		return this.field.get(obj);
	}

	@Override
	public String toString() {
		return "JABBElement [index=" + index + ", type=" + type + ", length="
				+ length + ", field=" + field + "]";
	}
}