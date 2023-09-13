# SGDP - Sistema de Gestão de Dados Pessoais

**Biblioteca depreciada, em breve não estará disponível. Caso prefira um exemplo mais atualizado utilizar o [sgdp-service-sample-test](https://totvstfs.visualstudio.com/TOTVSApps-SupportElements/_git/sgdp-service-sample-test?path=/README.md).**

## Contexto

Para exemplificar a biblioteca **SGDP** vamos utilizar o mesmo projeto criado para o  _sample_  da biblioteca **API JPA** disponível em nosso [GitHub][tjf-api-jpa-sample].

## Começando com SGDP

Com o projeto  _sample_  **API JPA** em mãos, vamos alterar alguns campos no banco de dados e incluir as anotações disponibilizadas pelo SGDP na classe de entidade.

> Como _engine_ de banco de dados utilizaremos o [H2][h2].

## Dependências

Incluir a biblioteca como dependência no `pom.xml` da aplicação:

```xml
<dependency>
	<groupId>com.totvs.sgdp.sdk</groupId>
	<artifactId>sgdp</artifactId>
	<version>{versão}</version>
</dependency>
```

Além das propriedades existentes no `application.yml` da aplicação, será necessário incluir novas propriedades. Conforme o exemplo:

```yml
  cloud:
    stream:
      defaultBinder: rabbit1
      binders:
        rabbit1:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: localhost

      rabbit:
        bindings:
          sgdp-input:
            consumer:
              exchangeType: headers
          sgdp-audit:
            producer:
              exchangeType: headers
          sgdp-output:
            producer:
              exchangeType: headers

      bindings:
        sgdp-audit:
          destination: sgdp-audit

        sgdp-input:
          destination: sgdp-commands
          group: sw-sgdp-app

        sgdp-output:
          destination: sgdp-responses
```

## Criando as tabelas

Para que seja possível demonstrar de forma mais efetiva algumas das anotações disponibilizadas no SGDP, precisaremos alterar nossa classe `Jedi.java` e adicionar os campos `identification` (algo como o CPF dos Jedis :D) e `Email`.

```Java
@Getter
@Setter
@Entity
public class Jedi {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private String name;
	
	@NotNull
	private int identification;
	
	@NotNull
	private String email;

	@NotNull
	private String gender;
}
```

Para melhor entendimento, o tutorial será dividido em tópicos, são eles:

* **Identificação** - Uso de anotações para identificar dados pessoais;
* **Auditoria** - Auditoria dos tratamentos realizados em dados pessoais;
* **Metadado** - Extração de relatório do metadado da aplicação.

### Identificação

Agora vamos a inclusão das anotações SGDP, vamos começar com a anotação `@SGDPData` que incluiremos nos campos com dados pessoais.

Além disso, vamos incluir a `@SGDPPurpose` que define os propósitos do tratamento de um determinado dado pessoal e o `@SGDPDescription` que torna mais claro a descrição da entidade ou atributo durante a geração do metadado.

```java
@SGDPData(allowsAnonymization = true, isSensitive = true, type = SGDPType.CPF)
@SGDPPurpose(classification = SGDPClassification.REGULAR_EXERCISE_OF_LAW, justification = "Numero de identificação do Jedi")
@SGDPDescription("Identification")
private int identification;

@NotNull
@SGDPData(allowsAnonymization = true, type = SGDPType.EMAIL)
@SGDPPurpose(classification = SGDPClassification.CONSENTMENT, justification = "Email para contato.")
@SGDPPurpose(classification = SGDPClassification.CONTRACT_EXECUTION, justification = "Necessário para contato.")
@SGDPDescription("Email")
private String email;
```

> A anotação `@SGDPPurpose` pode ser incluída mais de uma vez por atributo.

### Auditoria

Para realizar a auditoria das informações, precisaremos incluir a anotação `@EntityListeners` na classe `Jedi.java`:

```java
@Entity
@EntityListeners (SGDPSupport.class)
@SGDPDescription("Jedi")
@SGDPCode("Validar o LGPD do TJF, sobre a identificação, auditoria e anonimização de dados pessoais dos Jedi")
public class Jedi {
...
}
```

## Consulta dos Dados Pessoais

O SGDP conta com um serviço para extração dos dados do titular de dados. As aplicações devem responder ao comando emitido pelo serviço para esta finalidade. Este comando contêm as possíveis identificações do titular de dados, e com base nestas identificações a aplicação deve responder com os dados que ela possui referente ao titular em questão.

A aplicação deve fornecer um componente que implementa a interface com.totvs.tjf.sgdp.services.data.SGDPDataService, conforme exemplo abaixo:

```java
@Component
@Transactional
public class SWDataService extends SGDPDataService {

	@Autowired
	private JediRepository jediRepository;

	@Override
	public void execute(SGDPDataCommand command, SGDPMetadata metadata) {
		int identification = Integer.parseInt(command.getIdentifiers().get("identification"));
		List<Jedi> list = jediRepository.findByIdentificationEquals(identification);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("***** DATA COMMAND *****");
		try {
			System.out.println(mapper.writeValueAsString(list));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		addData(list, metadata);
	}
}
```

## Anonimização dos Dados Pessoais

O SGDP conta com um serviço para anonimização dos dados do titular de dados. As aplicações devem responder ao comando emitido pelo serviço para esta finalidade. Este comando contêm as possíveis identificações do titular de dados, e com base nestas identificações a aplicação deve anonimizar os dados que ela possui referente ao titular em questão.

A aplicação deve fornecer um componente que implementa a interface com.totvs.tjf.sgdp.services.mask.SGDPMaskService, conforme exemplo abaixo:

```java
@Component
@Transactional
public class SWMaskService extends SGDPMaskService {

	@Autowired
	private JediRepository jediRepository;

	@Override
	public void execute(SGDPMaskCommand command, SGDPMetadata metadata) {
		int identification = Integer.parseInt(command.getIdentifiers().get("identification"));
		List<Jedi> list = jediRepository.findByIdentificationEquals(identification);
		ObjectMapper mapper = new ObjectMapper();

		System.out.println("***** MASK COMMAND *****");
		try {
			System.out.println(mapper.writeValueAsString(list));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		list.forEach((jedi) -> {
			try {
				mask(jedi, metadata, command.getToVerify());
				try {
					System.out.println(mapper.writeValueAsString(jedi));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				jediRepository.save(jedi);
			} catch (SGDPMaskException e) {
				e.printStackTrace();
			}
		});
	}
}
```

## Hora de testar

> Os endpoints podem ser testados também via **Swagger-UI** pela url `localhost:8180/swagger-ui.html`.

**`Lista de Jedis`**

Para verificar a lista dos Jedis deve ser requisitada a url:

```http
GET /jedi/v1/jedis HTTP/1.1
Host: localhost:8180
```

```json
{
	"hasNext":false,
	"items":[
		{"name":"Qui-Gon Jinn","identification":7777,"email":"jinn@space.com","gender":"male"},
		{"name":"Obi-Wan Kenobi","identification":123,"email":"obi@jedi.com","gender":"male"},
		{"name":"Anakin Skywalker","identification":6666,"email":"darkin@space.com","gender":"male"},
		{"name":"Yoda","identification":1,"email":"yoda@space.com","gender":"male"},
		{"name":"Mace Windu","identification":2341,"email":"mace_windu@jedi.com","gender":"male"},
		{"name":"Count Dooku","identification":2431,"email":"dooku@dark.com","gender":"male"},
		{"name":"Luke Skywalker","identification":8543,"email":"luke@jedi.com","gender":"male"},
		{"name":"Rey","identification":3421,"email":"rey@space.com","gender":"female"}
	]
}
```

**`SGDP Metadata`**

Para extrair o metadado conforme descrito na documentação do SGDP Core, basta requisitar a url:

```http
GET /sgdp/metadata HTTP/1.1
Host: localhost:8180
```

```json
{
  "models": {
    "com.tjf.sample.github.model.Jedi": {
      "sgdpSupport": true,
      "description": "Jedi",
      "attributes": {
        "identification": {
          "type": "int",
          "description": "Identification",
          "sgdpData": {
            "sensitive": true,
            "type": "CPF",
            "allowsAnonymization": true
          },
          "sgdpPurposes": [
            {
              "classification": "REGULAR_EXERCISE_OF_LAW",
              "justification": "Numero de identificação do Jedi"
            }
          ]
        },
        "gender": {
          "type": "String",
          "description": "Gender",
          "sgdpData": {
            "sensitive": true,
            "type": "EMPTY",
            "allowsAnonymization": false
          },
          "sgdpPurposes": []
        },
        "name": {
          "type": "String",
          "description": "Nome do Jedi",
          "sgdpData": null,
          "sgdpPurposes": []
        },
        "id": {
          "type": "int",
          "description": null,
          "sgdpData": null,
          "sgdpPurposes": []
        },
        "email": {
          "type": "String",
          "description": "Email",
          "sgdpData": {
            "sensitive": false,
            "type": "EMAIL",
            "allowsAnonymization": true
          },
          "sgdpPurposes": [
            {
              "classification": "CONSENTMENT",
              "justification": "Email para contato."
            },
            {
              "classification": "CONTRACT_EXECUTION",
              "justification": "Necessário para contato."
            }
          ]
        }
      },
      "usedAt": []
    }
  },
  "codes": {
    "com.tjf.sample.github.model.Jedi": {
      "description": "Validar o LGPD do TJF, sobre a identificação, auditoria e anonimização de dados pessoais dos Jedi"
    }
  },
  "package": "com.tjf.sample.github"
}
```

## Isso é tudo pessoal!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pelo componente SGDP e enviar sugestões e melhorias para o projeto **TOTVS Java Framework**.

[tjf-api-jpa-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-api-samples/tjf-api-jpa-sample
[h2]: https://www.h2database.com
