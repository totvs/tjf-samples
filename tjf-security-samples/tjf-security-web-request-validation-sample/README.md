# Exemplo de uso do recurso Request Validation

## Contexto

Para exemplificar o uso do recurso **Request Validation** vamos utilizar um exemplo simples com uma API REST para validação das organizações permitidas.

## Começando

Para criação deste exemplo, você vai precisar de um _Authorization Service_ para podermos realizar a validação de solicitação, por exemplo o **TOTVS RAC**.

Vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto basta acessar o [Spring initializr](https://start.spring.io/) e criar o projeto.

### Dependências

Para utilização do recurso será necessário alterar o _parent_ e inserir a seguinte dependência em seu arquivo `pom.xml`:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>4.26.0-RELEASE</version>
</parent>
```

```xml
<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-security-webmvc</artifactId>
</dependency>
```

### Configurando o recurso de Validação de Solicitação

No arquivo `application.yml` precisamos habilitar nas propriedades da nossa aplicação o recurso de Request Validation.

```yml
tjf:
  security:
    api:
      request-validation:
        enable: true
```

### Criando a implementação da validação

Vamos implementar a nossa regra que valida a organização conforme o parâmetro da requisição, tomando como base a implementação do `OrganizationsAllowed` que recupera as organizações permitidas do contexto de segurança ao se autenticar na aplicação.

O método `validate()` realiza a implementação da regra de validação da organização.

```java
public class OrganizationValidator implements Validator {

  @Override
  public ValidationResult validate(HttpServletRequest request) {

    String organization = request.getParameter("organization");
    if (organization == null) {
      return ValidationResult.valid();
    }

    if (Arrays.asList(OrganizationsAllowed.value()).contains(organization)) {
      return ValidationResult.valid();
    }

    return ValidationResult.invalid("Acesso negado para a organização");
  }
}
```

### Criando a configuração

Para configurar a nossa implementação de validação no recurso, criaremos a classe `ValidationConfiguration` sinalizada com a anotação `@Configuration`.

O bean `Validator` define a nossa implementação de validação de organização.

```java
@Configuration
public class ValidationConfiguration { 

  @Bean
  Validator validator() {
    return new OrganizationValidator();
  }
}
```

### Criando os _endpoints_

Vamos criar o _controller_ que ficará responsável por receber as requisições.

```java
@RestController
@RequestMapping(path = "/api/v1/validation")
public class ValidationController {

  @GetMapping("/param")
  ResponseEntity<String> param(@RequestParam(required = false) String organization) {
    return ResponseEntity.ok("Param");
  }
}
```

## Vamos testar?

Assim que o aplicativo iniciar, faça uma requisição `GET` para o endpoint `http://localhost:8080/api/v1/validation/param?organization=123`.  

Se a validação da organização obteve um resultado malsucedido, será apresentado o código de status `403 Forbidden` no retorno da resposta.

## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **Security Web**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-security-webmvc) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).
