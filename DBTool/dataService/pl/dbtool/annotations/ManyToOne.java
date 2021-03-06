package pl.dbtool.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import pl.dbtool.annotations.ManyToMany.CascadeType;
import pl.dbtool.annotations.ManyToMany.FetchType;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ManyToOne {

	FetchType fetch() default FetchType.LAZY;
	CascadeType cascade() default CascadeType.ALL;
}
