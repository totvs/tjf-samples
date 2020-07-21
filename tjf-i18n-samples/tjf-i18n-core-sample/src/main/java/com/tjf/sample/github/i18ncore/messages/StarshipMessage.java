package com.tjf.sample.github.i18ncore.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.totvs.tjf.i18n.I18nService;

@Component
public class StarshipMessage {

	@Autowired
	private I18nService i18nService;

	public String starshipConfirmLanding(String starshipName) {
		return this.i18nService.getMessage("starship.authorized", starshipName);
	}

	public String starshipDestroy(String starshipName) {
		return this.i18nService.getMessage("starship.destroyed", starshipName);
	}

}
