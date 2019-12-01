package nativeimage;

import java.lang.annotation.*;

/**
 * Register the annotated element to be scanned and generate a reflection config file,
 * instead of the current annotated element you can specify
 * {@link #scanPackage()}, {@link #scanClass()} or {@link #scanClassName()} instead
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.PACKAGE})
//@Repeatable(value = RuntimeReflections.class)
public @interface Reflection {

	/**
	 * The package to be scanned to generate reflection config, e.g <code>java.lang</code>
	 */
	String scanPackage() default "";

	/**
	 * The class to be scanned to generate reflection config
	 */
	Class scanClass() default Void.class;

	/**
	 * The class name to be scanned to generate reflection config, e.g <code>java.lang.String</code>
	 */
	String scanClassName() default "";

	/**
	 * {@link #declaredConstructors()}, {@link #publicConstructors()} and &lt;init&gt; together
	 */
	boolean constructors() default false;

	/**
	 * aka allDeclaredConstructors
	 */
	boolean declaredConstructors() default false;

	/**
	 * aka allPublicConstructors
	 */
	boolean publicConstructors() default false;

	/**
	 * aka allDeclaredMethods
	 */
	boolean declaredMethods() default false;

	/**
	 * aka allPublicMethods
	 */
	boolean publicMethods() default false;

	/**
	 * aka allPublicFields
	 */
	boolean publicFields() default false;

	/**
	 * aka allDeclaredFields
	 */
	boolean declaredFields() default false;

}
