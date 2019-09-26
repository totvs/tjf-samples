package com.tjf.samples.treports.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.samples.treports.service.TReportsService;

@RestController
public class CostCenterController {

	private static final String REL_COST_CENTER = "relCostCenters";

	@Autowired
	TReportsService treportsService;

	@GetMapping(path = "/report/costcenters")
	public void getReport(HttpServletResponse response) throws InterruptedException, IOException {

		setFileReponse(treportsService.execute(REL_COST_CENTER), response);
	}

	private void setFileReponse(File file, HttpServletResponse response) throws IOException {

		response.setContentType("application/pdf");
		InputStream is = new FileInputStream(file);
		IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
	}
}