package oga.binary.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface BinaryElement {

	int order() default Integer.MAX_VALUE;

	int type() default BinaryTypes.UNDEFINED;

	int length() default 0;
	
	/**
	 * (Optional)using for java.text.SimpleDateFormat
	 * @return
	 */
	String datepattern() default "";
	
	//TODO 실수 지원
}
