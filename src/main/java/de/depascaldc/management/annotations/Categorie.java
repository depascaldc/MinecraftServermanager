package de.depascaldc.management.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Categorie {
	/**
	 * Custom display name for the annotated class or method.
	 *
	 * @return String as displayname
	 */
	String value();
}
