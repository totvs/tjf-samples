package i18n.custom.bundle.config;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.totvs.tjf.core.validation.ValidationInterpolatorService;
import com.totvs.tjf.i18n.core.StandardI18nService;

@Component
public class I18nCustomFluig implements I18nService {

	private final MessageSource messageSource;
	
	public I18nCustomFluig(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public String translate(String s, Object... objects) {

		System.out.println(s);
		List.of(objects).forEach(System.out::println);
			
		return new StandardI18nService(messageSource).translate(s, objects);
	}

	@Override
	public <T> String getMessage(String code, Collection<T> arguments) {

		System.out.println(code);
		arguments.forEach(System.out::println);

		return new StandardI18nService(messageSource).getMessage(code, arguments);
	}
}