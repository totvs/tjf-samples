package i18n.custom.bundle.config;

import javax.validation.MessageInterpolator;

import com.totvs.tjf.core.validation.ValidationInterpolatorService;

import static com.totvs.tjf.core.validation.ValidationInterpolatorService.getCode;

public class ValidationInterpolatorCustomFluig implements ValidationInterpolatorService {

	private final I18nService i18nService;
	private MessageInterpolator interpolator;

	public ValidationInterpolatorCustomFluig(I18nService i18nService) {
		this.i18nService = i18nService;
	}

	@Override
	public String translate(String messageTemplate) {
		return i18nService.getMessage(getCode(messageTemplate));
	}

	@Override
	public MessageInterpolator getMessageInterpolator() {
		return interpolator;
	}

	@Override
	public void setMessageInterpolator(MessageInterpolator interpolator) {
		this.interpolator = interpolator;
	}
}