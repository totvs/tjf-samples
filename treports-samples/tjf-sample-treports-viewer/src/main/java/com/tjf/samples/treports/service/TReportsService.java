package com.tjf.samples.treports.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import com.tjf.samples.treports.api.ApiCollectionReportReponse;
import com.tjf.samples.treports.api.ApiExecuteInfoResponse;
import com.tjf.samples.treports.command.ExecuteCommand;
import com.tjf.samples.treports.model.Report;
import com.tjf.samples.treports.model.Request;

@Service
public class TReportsService {

	private static final String FINISHED = "Finalizado";

	@Autowired
	AuthService authService;

	private HttpHeaders generateHeaders() {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authService.getToken().getValue());
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		return headers;
	}

	private List<Report> getReports() {

		RestTemplate restTemplate = new RestTemplate();

		HttpEntity entity = new HttpEntity(generateHeaders());

		ResponseEntity<ApiCollectionReportReponse> response = restTemplate.exchange("http://localhost:7017/api/trep/v1/reports/",
				HttpMethod.GET, entity, ApiCollectionReportReponse.class);

		System.out.println("Status: " + response.getStatusCodeValue());
		System.out.println("Reports length: " + response.getBody().getItems().length);
		System.out.println("#### REPORTS #####");

		return List.of(response.getBody().getItems());
	}

	private Report getReport(String code) {

		List<Report> reports = getReports();

		for (Report report : reports) {
			if (report.getCode().equalsIgnoreCase(code)) {
				return report;
			}
		}

		return null;
	}

	private ApiExecuteInfoResponse executeReport(String reportUId) {

		RestTemplate restTemplate = new RestTemplate();
		ExecuteCommand reportRequest = new ExecuteCommand();

		HttpEntity<ExecuteCommand> request = new HttpEntity<>(reportRequest, generateHeaders());

		ResponseEntity<ApiExecuteInfoResponse> response = restTemplate.postForEntity(
				"http://localhost:7017/api/trep/v1/reports/" + reportUId + "/execute", request, ApiExecuteInfoResponse.class);

		System.out.println("Status: " + response.getStatusCodeValue());
		System.out.println("uId request: " + response.getBody().getuIdRequest());
		System.out.println("Schedule date: " + response.getBody().getScheduleDate());
		System.out.println("#### EXECUTE INFO #####");

		return response.getBody();
	}

	private Request getRequestInfo(String uIdRequest) {

		RestTemplate restTemplate = new RestTemplate();

		HttpEntity entity = new HttpEntity(generateHeaders());

		ResponseEntity<Request> response = restTemplate.exchange(
				"http://localhost:7017/api/trep/v1/generateds/" + uIdRequest + "/info", HttpMethod.GET, entity,
				Request.class);

		System.out.println("Status: " + response.getStatusCodeValue());
		System.out.println("Id: " + response.getBody().getId());
		System.out.println("Request Status: " + response.getBody().getStatus());
		System.out.println("#### REQUEST INFO #####");

		return response.getBody();
	}

	private File getFile(int idRequest) {

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:7017/api/trep/v1/generateds/" + idRequest + "/file?exportFileType=0";

		System.out.println("Download file: " + url);
		System.out.println("#### DONWLOAD #####");

		return restTemplate.execute(url, HttpMethod.GET,
				clientHttpRequest -> clientHttpRequest.getHeaders().setBearerAuth(authService.getToken().getValue()),
				clientHttpResponse -> {
					File file = File.createTempFile("download", ".pdf");
					StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(file));
					return file;
				});
	}

	public File execute(String code) throws InterruptedException {

		ApiExecuteInfoResponse executeInfo = executeReport(getReport(code).getuId());
		Request requestInfo = wait(executeInfo.getuIdRequest());

		return getFile(requestInfo.getId());
	}

	private Request wait(String uIdRequest) throws InterruptedException {

		Request info = null;
		String status = "";

		while (!status.equalsIgnoreCase(FINISHED)) {
			info = getRequestInfo(uIdRequest);
			status = info.getStatus();
			Thread.sleep(3000);
		}

		return info;
	}

	public String view(String code) throws InterruptedException {

		ApiExecuteInfoResponse executeInfo = executeReport(getReport(code).getuId());
		wait(executeInfo.getuIdRequest());
		
		return "http://localhost:7017/reportsviewer/external/" + executeInfo.getuIdRequest();
	}
}