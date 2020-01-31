package i18n.custom.bundle.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.totvs.tjf.core.validation.ValidationInterpolatorService;

@Configuration
//@PropertySource("classpath:META-INF/config.properties")
public class Config {
	
	@Bean
	public I18nService i18nService(MessageSource messageSource) {
		return new I18nCustomFluig(messageSource);
	}

	@Bean
	public ValidationInterpolatorService validationInterpolatorService(I18nService i18nService) {
		return new ValidationInterpolatorCustomFluig(i18nService); 
	}
}