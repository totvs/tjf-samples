package com.tjf.samples.treports.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.samples.treports.model.CostCenter;
import com.tjf.samples.treports.model.CostCenterRepository;
import com.totvs.tjf.api.context.v1.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;

@RestController
public class CostCentersController {
    
	@Autowired
	CostCenterRepository costCenterRepository;

	@GetMapping(path = "/api/ctb/v1/costcenters", produces = "application/json")
	public ApiCollectionResponse<CostCenter> getCostCenters(ApiFieldRequest fieldRequest, ApiPageRequest pageRequest,
			ApiSortRequest sortRequest) {
		return costCenterRepository.findAllProjected(fieldRequest, pageRequest, sortRequest);
	}

	@GetMapping(path = "/report/costcenters")
	public void getReport(HttpServletResponse response) {
		
		try {
			response.setContentType("application/pdf");
			// get your file as InputStream
			InputStream is = new ClassPathResource("sample.pdf").getInputStream();
			// copy it to response's OutputStream
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			// log.info("Error writing file to output stream. Filename was '{}'", fileName,

			System.out.println(ex);

			throw new RuntimeException("IOError writing file to output stream");
		}
	}
}
