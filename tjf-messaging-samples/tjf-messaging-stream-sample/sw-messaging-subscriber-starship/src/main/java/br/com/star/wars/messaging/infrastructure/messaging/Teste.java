package br.com.star.wars.messaging.infrastructure.messaging;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.annotation.AliasFor;
import org.springframework.messaging.handler.annotation.MessageMapping;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@MessageMapping
@Documented
@StreamListener
public @interface Teste {
	/**
	 * The name of the binding target (e.g. channel) that the method subscribes to.
	 * @return the name of the binding target.
	 */
	@AliasFor(annotation=StreamListener.class, attribute="target")
	String value() default "";

	/**
	 * The name of the binding target (e.g. channel) that the method subscribes to.
	 * @return the name of the binding target.
	 */
	@AliasFor(annotation=StreamListener.class, attribute="target")
	String target()  default "";


	/**
	 * A condition that must be met by all items that are dispatched to this method.
	 * @return a SpEL expression that must evaluate to a {@code boolean} value.
	 */
	@AliasFor(annotation=StreamListener.class, attribute="condition")
//	String condition() default "headers['type']== new br.com.star.wars.messaging.model.StarShip(#starShip).getName()";
	String condition() default "headers['type']== new br.com.star.wars.messaging.model.StarShip(Teste.name()).getName()";
	
	String name() default "arrivedStarShip";
}
