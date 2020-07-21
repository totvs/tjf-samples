package com.tjf.sample.github.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.model.Client;
import com.tjf.sample.github.model.Product;
import com.tjf.sample.github.repository.ClientRepository;
import com.tjf.sample.github.repository.ProductRepository;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.stereotype.openapi.MessageDocumentationApi;
import com.totvs.tjf.api.context.stereotype.openapi.ProductInformationApi;
import com.totvs.tjf.api.context.stereotype.openapi.ProductInformationEndPoint;
import com.totvs.tjf.api.context.stereotype.openapi.XTOTVSApi;
import com.totvs.tjf.api.context.stereotype.openapi.XTOTVSEndPoint;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;

@RestController
@RequestMapping(path = "/api/saude/v1/product", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V1)
@OpenAPIDefinition(info = @Info(title = "API de Produtos", 
								description = "Controle de Produtos", 
								version = "3.004", 
								contact = @Contact(name = "", url = "", email = "")), 
					servers = @Server(url = "{{host}}/api/saude/v1/product", 
									  description = "Descrição do Server", 
									  variables = {
											  @ServerVariable(name = "serverUrl", defaultValue = "localhost"),
											  @ServerVariable(name = "serverHttpPort", defaultValue = "8080") }))
@XTOTVSApi(messageDocumentation = @MessageDocumentationApi(name = "Product", description = "Produto", segment = "Saúde"), 
		   productInformation = @ProductInformationApi(product = "OpenApiSample", contact = "tjf@totvs.com.br", description = "Open Api Sample", helpUrl = "http://tjf.totvs.com.br"))
public class ProductController {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ClientRepository clientRepository;

	@Operation(summary = "Buscar Produtos", 
			   description = "Buscar lista de Produtos", 
			   operationId = "produtoGet", 
			   tags = { "client" }, 
			   responses = { @ApiResponse(responseCode = "200", 
			   							  description = "Lista de Produtos retornada."),
					   		 @ApiResponse(responseCode = "400", 
					   		 			  description = "Erro ao retornar a lista de Produtos.")})
	@GetMapping(path = "/list")
	@XTOTVSEndPoint(productInformation = @ProductInformationEndPoint(minimalVersion = "2.0", product = "OpenApiSample"))
	public ApiCollectionResponse<Product> findAll() {
		return ApiCollectionResponse.of(productRepository.findAll());
	}

	@Operation(summary = "Criar Produto", 
			   description = "Criação de Produto", 
			   operationId = "produtoPost", 
			   tags = { "client" }, 
			   responses = { @ApiResponse(responseCode = "201", 
			   							  description = "Produto criado com sucesso."),
					   		 @ApiResponse(responseCode = "400", 
					   		 			  description = "Erro ao criar o Produto.")})
	@PostMapping(path = "/create")
	public ResponseEntity<Void> create(@RequestBody Product product) {
		productRepository.save(product);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Operation(summary = "Vender Produto", 
			   description = "Venda de Produto", 
			   operationId = "vendaProdutoPost", 
			   tags = { "client" }, 
			   responses = { @ApiResponse(responseCode = "201", 
			   							  description = "Produto vendido com sucesso."),
					   		 @ApiResponse(responseCode = "400", 
					   		 			  description = "Erro ao vender o Produto.")})
	@PostMapping(path = "/sell/{productId}/{clientId}")
	public ResponseEntity<String> sell(@PathVariable("productId") int productId, @PathVariable("clientId") int clientId) {
		Client client = clientRepository.getOne(clientId);
		
		productRepository.deleteById(productId);
		return ResponseEntity.status(HttpStatus.CREATED).body("Produto vendido com sucesso para o cliente " + client.getName());
	}
	
}
