package com.iproov.androidapiclient.javaretrofit;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * The use of these apis in a client is not for production, only for demonstrations.
 * Your ApiService Client Secret should never be embedded in client code.
 */
@Documented
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
public @interface DemonstrationPurposeOnly {

    String value() default "The use of these apis in a client is not for production, only for demonstrations. Your ApiService Client Secret should never be embedded in client code.";

    String message() default "The use of these apis in a client is not for production, only for demonstrations. Your ApiService Client Secret should never be embedded in client code.";
}
