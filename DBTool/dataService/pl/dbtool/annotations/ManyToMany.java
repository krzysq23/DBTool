package pl.dbtool.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ManyToMany {

	public enum FetchType {
		LAZY, EAGER
	}
	
	FetchType fetch() default FetchType.LAZY;
	
}
