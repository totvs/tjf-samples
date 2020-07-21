package com.tjf.sample.github.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.model.Client;
import com.tjf.sample.github.repository.ClientRepository;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.stereotype.openapi.MessageDocumentationApi;
import com.totvs.tjf.api.context.stereotype.openapi.ProductInformationApi;
import com.totvs.tjf.api.context.stereotype.openapi.XTOTVSApi;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;

@RestController
@RequestMapping(path = "/api/saude/v1/client", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V1)
@OpenAPIDefinition(info = @Info(title = "API de Clientes", 
								description = "Controle de Clientes", 
								version = "2.003", 
								contact = @Contact(name = "", url = "", email = "")), 
				   servers = @Server(url = "{{host}}/api/saude/v1/client", 
				   		description = "Descrição do Server", 
				   		variables = {
				   				@ServerVariable(name = "serverUrl", defaultValue = "localhost"),
                                @ServerVariable(name = "serverHttpPort", defaultValue = "8080")}))
@XTOTVSApi(messageDocumentation = @MessageDocumentationApi(name = "Client", description = "Cliente", segment = "Saúde"), 
			productInformation = @ProductInformationApi(product = "OpenApiSample", contact = "tjf@totvs.com.br", description = "Open Api Sample", helpUrl = "http://tjf.totvs.com.br"))
public class ClientController {

	@Autowired
	private ClientRepository clientRepository;

	@Operation(summary = "Buscar Clientes", 
			   description = "Buscar lista de Clientes", 
			   operationId = "clienteGet", 
			   tags = { "client" }, 
			   responses = { @ApiResponse(responseCode = "200", 
			   							  description = "Lista de Clientes retornada."),
					   		 @ApiResponse(responseCode = "400", 
					   		 			  description = "Erro ao retornar a lista de Clientes.")})
	@GetMapping(path = "/list")
	public ApiCollectionResponse<Client> findAll() {
		return ApiCollectionResponse.of(clientRepository.findAll());
	}
	
	@Operation(summary = "Criar Cliente", 
			   description = "Criação de Cliente", 
			   operationId = "clientePost", 
			   tags = { "client" }, 
			   responses = { @ApiResponse(responseCode = "201", 
			   							  description = "Cliente criado com sucesso."),
					   		 @ApiResponse(responseCode = "400", 
					   		 			  description = "Erro ao criar o Cliente.")})
	@PostMapping(path = "/create")
	public ResponseEntity<Void> create(@RequestBody Client client) {
		clientRepository.save(client);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}