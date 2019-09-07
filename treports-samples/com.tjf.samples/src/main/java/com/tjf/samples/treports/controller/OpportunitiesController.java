package com.tjf.samples.treports.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/crm/v1/opportunities", produces = "application/json")
public class OpportunitiesController {

	@GetMapping
	public String getEmployees() {
		
		System.out.println("TREPORT");
		
		return "{\n" + 
				"    \"total\": 20,\n" + 
				"    \"hasNext\": true,\n" + 
				"    \"items\": [\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"# teste voltar\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 185,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 117,\n" + 
				"            \"Seller\": 73,\n" + 
				"            \"Code\": 3675,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Glauber José Lopes\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"#camisa nota 10\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 188,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 117,\n" + 
				"            \"Seller\": 73,\n" + 
				"            \"Code\": 3672,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Glauber José Lopes\",\n" + 
				"            \"Notes\": \"ste\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"#camisa nota 20\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 185,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 117,\n" + 
				"            \"Seller\": 73,\n" + 
				"            \"Code\": 3673,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Glauber José Lopes\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 2,\n" + 
				"            \"Description\": \"#fechada direto\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 502,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": \"2016-01-15\",\n" + 
				"            \"Process\": 117,\n" + 
				"            \"Seller\": 74,\n" + 
				"            \"Code\": 3677,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super usuario\",\n" + 
				"            \"Notes\": \"a1\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 2,\n" + 
				"            \"Description\": \"#voltar\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 480,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": \"2015-11-11\",\n" + 
				"            \"Process\": 117,\n" + 
				"            \"Seller\": 73,\n" + 
				"            \"Code\": 3676,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Glauber José Lopes\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"1\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 398,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 2653,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"teste\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"1\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 221,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 151,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 2674,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"1026\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 479,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 117,\n" + 
				"            \"Seller\": 2,\n" + 
				"            \"Code\": 1026,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Marcos Pedro da Silveira\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"1028\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 479,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 117,\n" + 
				"            \"Seller\": 2,\n" + 
				"            \"Code\": 1028,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Marcos Pedro da Silveira\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"107 - NATAL\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 397,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 107,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"109 - FIM DO ALFA \\\\O/ \\\\O/ \\\\O/ \\\\O/\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 398,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 109,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"110 - SEXTA-FEIRA\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 398,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 110,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"111 - USUÁRIO 13\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 397,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 111,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 2,\n" + 
				"            \"Description\": \"12.1.16 - 2\",\n" + 
				"            \"Currency\": 0,\n" + 
				"            \"Stage\": 501,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": \"2017-03-07\",\n" + 
				"            \"Process\": 350,\n" + 
				"            \"Seller\": 495,\n" + 
				"            \"Code\": 4353,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Marcio Floriano\",\n" + 
				"            \"Notes\": \"JIhuih ioioasfh shd fuihsud fhusd ghfuoisdfg uysd gfids.\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"123\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 398,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 112,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"123456\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 397,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 313,\n" + 
				"            \"Code\": 2638,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"andreia.pbw\",\n" + 
				"            \"Notes\": \"\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 1,\n" + 
				"            \"Description\": \"1322\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 498,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": null,\n" + 
				"            \"Process\": 350,\n" + 
				"            \"Seller\": 73,\n" + 
				"            \"Code\": 3772,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Glauber José Lopes\",\n" + 
				"            \"Notes\": \"Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq rst uvw xyz Abc defg hi j k lmn opq,\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 2,\n" + 
				"            \"Description\": \"2 PIRROLIDONA SOLUPHOR P/1000\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 401,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": \"2013-06-26\",\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 597,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"Oportunidade criada automaticamente por Seguro de Transportes, Demais Seguros ou Ramos\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 2,\n" + 
				"            \"Description\": \"20051/1000\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 401,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": \"2013-06-26\",\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 588,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"Oportunidade criada automaticamente por Seguro de Transportes, Demais Seguros ou Ramos\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"Status\": 2,\n" + 
				"            \"Description\": \"20061/1000\",\n" + 
				"            \"Currency\": 1,\n" + 
				"            \"Stage\": 401,\n" + 
				"            \"ListofConsumer\": [],\n" + 
				"            \"ClosingDate\": \"2013-06-26\",\n" + 
				"            \"Process\": 270,\n" + 
				"            \"Seller\": 153,\n" + 
				"            \"Code\": 593,\n" + 
				"            \"_expandables\": [\n" + 
				"                \"ListofConsumer\"\n" + 
				"            ],\n" + 
				"            \"SellerName\": \"Super Usuario GP\",\n" + 
				"            \"Notes\": \"Oportunidade criada automaticamente por Seguro de Transportes, Demais Seguros ou Ramos\"\n" + 
				"        }\n" + 
				"    ]\n" + 
				"}";
	}
}