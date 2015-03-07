package com.novicehacks.autobot.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Incomplete annotation is used to mark that the feature or code for that
 * method / class / constructor is not completed. Used to distinguish the
 * features that are not completed.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @since Nov 29, 2014
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Documented
public @interface Incomplete {

}
