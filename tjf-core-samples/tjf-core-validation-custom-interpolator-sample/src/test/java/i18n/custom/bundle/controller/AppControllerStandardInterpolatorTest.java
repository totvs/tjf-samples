package i18n.custom.bundle.controller;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.api.context.v1.response.ApiErrorResponse;
import com.totvs.tjf.api.context.v1.response.ApiErrorResponseDetail;
import com.totvs.tjf.core.validation.StandardValidationInterpolatorService;
import com.totvs.tjf.core.validation.ValidationInterpolatorService;

import i18n.custom.bundle.config.I18nService;
import i18n.custom.bundle.config.ValidationInterpolatorCustomFluig;
import i18n.custom.bundle.dtos.AppDto;
import i18n.custom.test.i18nCustomApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {i18nCustomApplication.class, AppController.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AppControllerStandardInterpolatorTest {

	private final ObjectMapper mapper = new ObjectMapper();
    
	@Autowired
	TestRestTemplate rest;

	@Test
	public void testValidationCreateAppDtoSucess() throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		AppDto appDto = new AppDto();
		appDto.setId("1");
		appDto.setName("n");
		appDto.setCode("c");

		HttpEntity<AppDto> request = new HttpEntity<>(appDto, headers);

		ResponseEntity<AppDto> response = rest.postForEntity(RestPath.BASE_PATH + "/apps", request, AppDto.class);

		assertEquals(201, response.getStatusCodeValue());
		assertEquals(toJsonString(appDto), toJsonString(response.getBody()));
	}

	@Test
	public void testValidationCreateAppDtoErrorCodeNotBlank() throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		AppDto appDto = new AppDto();
		appDto.setName("n");

		HttpEntity<AppDto> request = new HttpEntity<>(appDto, headers);

		ResponseEntity<ApiErrorResponse> response = rest.postForEntity(RestPath.BASE_PATH + "/apps", request,
				ApiErrorResponse.class);

		Collection<ApiErrorResponseDetail> details = List.of(new ApiErrorResponseDetail("AppDto.code.NotBlank",
				"validation bundle:O atributo code não pode ser vazio", "code: null", null));

		ApiErrorResponse erro = new ApiErrorResponse("FLUIG_VALIDATION", "i18n bundle:Violação encontrada",
				"i18n bundle:Uma ou mais validações não foram atendidas", null, details);

		assertEquals(400, response.getStatusCodeValue());
		assertEquals(toJsonString(erro), toJsonString(response.getBody()));
	}

	@Test
	public void testValidationCreateAppDtoErrorNameNotBlank() throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		AppDto appDto = new AppDto();
		appDto.setCode("c");

		HttpEntity<AppDto> request = new HttpEntity<>(appDto, headers);

		ResponseEntity<ApiErrorResponse> response = rest.postForEntity(RestPath.BASE_PATH + "/apps", request,
				ApiErrorResponse.class);

		Collection<ApiErrorResponseDetail> details = List.of(new ApiErrorResponseDetail("AppDto.name.NotBlank",
				"validation bundle:O atributo name não pode ser vazio", "name: null", null));

		ApiErrorResponse erro = new ApiErrorResponse("FLUIG_VALIDATION", "i18n bundle:Violação encontrada",
				"i18n bundle:Uma ou mais validações não foram atendidas", null, details);

		assertEquals(400, response.getStatusCodeValue());
		assertEquals(toJsonString(erro), toJsonString(response.getBody()));
	}

	@Test
	public void testValidationCreateAppDtoErrorCodeSize() throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		AppDto appDto = new AppDto();
		appDto.setName("n");
		appDto.setCode("123456789.123456789.123456789.123456789.123456789.1");

		HttpEntity<AppDto> request = new HttpEntity<>(appDto, headers);

		ResponseEntity<ApiErrorResponse> response = rest.postForEntity(RestPath.BASE_PATH + "/apps", request,
				ApiErrorResponse.class);

		Collection<ApiErrorResponseDetail> details = List
				.of(new ApiErrorResponseDetail("javax.validation.constraints.Size.message",
						"tamanho deve estar entre 0 e 50",
						"code: 123456789.123456789.123456789.123456789.123456789.1", null));

		ApiErrorResponse erro = new ApiErrorResponse("FLUIG_VALIDATION", "i18n bundle:Violação encontrada",
				"i18n bundle:Uma ou mais validações não foram atendidas", null, details);

		assertEquals(400, response.getStatusCodeValue());
		assertEquals(toJsonString(erro), toJsonString(response.getBody()));
	}

	@Test
	public void testValidationCreateAppDtoErrorNameSize() throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		AppDto appDto = new AppDto();
		appDto.setName(
				"123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.1");
		appDto.setCode("c");

		HttpEntity<AppDto> request = new HttpEntity<>(appDto, headers);

		ResponseEntity<ApiErrorResponse> response = rest.postForEntity(RestPath.BASE_PATH + "/apps", request,
				ApiErrorResponse.class);

		Collection<ApiErrorResponseDetail> details = List.of(new ApiErrorResponseDetail("AppDto.name.Size",
				"validation bundle:O atributo name não pode ser maior que 100 caracteres",
				"name: 123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.1",
				null));

		ApiErrorResponse erro = new ApiErrorResponse("FLUIG_VALIDATION", "i18n bundle:Violação encontrada",
				"i18n bundle:Uma ou mais validações não foram atendidas", null, details);

		assertEquals(400, response.getStatusCodeValue());
		assertEquals(toJsonString(erro), toJsonString(response.getBody()));
	}

	@Test
	public void testValidationCreateAppDtoErrorEN() throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAcceptLanguageAsLocales(List.of(Locale.ENGLISH));

		AppDto appDto = new AppDto();
		appDto.setName(
				"123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.1");
		appDto.setCode("c");

		HttpEntity<AppDto> request = new HttpEntity<>(appDto, headers);

		ResponseEntity<ApiErrorResponse> response = rest.postForEntity(RestPath.BASE_PATH + "/apps", request,
				ApiErrorResponse.class);

		Collection<ApiErrorResponseDetail> details = List.of(new ApiErrorResponseDetail("AppDto.name.Size",
				"validation bundle EN:O atributo name não pode ser maior que 100 caracteres",
				"name: 123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.123456789.1",
				null));

		ApiErrorResponse erro = new ApiErrorResponse("FLUIG_VALIDATION", "i18n bundle EN:Violação encontrada",
				"i18n bundle EN:Uma ou mais validações não foram atendidas", null, details);

		assertEquals(400, response.getStatusCodeValue());
		assertEquals(toJsonString(erro), toJsonString(response.getBody()));
	}

	public String toJsonString(Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
}