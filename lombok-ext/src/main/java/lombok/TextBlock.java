package lombok;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that RSL is supposed to inject the variable javadoc on it's value
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface TextBlock {

	/**
	 * Removes vertical and horizontal white space margins from around the
	 * essential body of a multi-line string, while preserving relative
	 * indentation.
	 *
	 * <p>This feature is not supported yet</p>
	 */
	boolean align() default false;
}
