package i18n.custom.bundle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.core.validation.ValidatorService;

import i18n.custom.bundle.dtos.AppDto;
import i18n.custom.bundle.exceptions.AppConstraintException;

@RestController("PlansAppDefinitionControllerV1")
@RequestMapping(path = RestPath.BASE_PATH + "/apps", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@ApiGuideline(ApiGuidelineVersion.v1)
public class AppController {

	@Autowired
	private ValidatorService validator;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public AppDto create(@RequestBody AppDto appDto) {

		validator.validate(appDto).ifPresent(violations -> {
			throw new AppConstraintException(violations);
		});

		return appDto;
	}
}