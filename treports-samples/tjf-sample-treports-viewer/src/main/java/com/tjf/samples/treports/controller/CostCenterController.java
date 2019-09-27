package com.tjf.samples.treports.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.samples.treports.service.TReportsService;

@RestController
public class CostCenterController {

	private static final String REL_COST_CENTER = "relCostCenters";

	@Autowired
	TReportsService treportsService;

	@GetMapping(path = "/report/costcenters/view")
	public void viewReport(HttpServletResponse response) throws InterruptedException, IOException {

		// Precisa estar logado no TReports
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<object data=\"" + treportsService.view(REL_COST_CENTER)
				+ "\" type=\"text/html\" style=\"height: 700px; width: 100%\"/>");
		out.close();
	}
}