# SGDP - Sistema de Gestão de Dados Pessoais

# Contexto

Para exemplificar a biblioteca __SGDP__ vamos utilizar o mesmo projeto criado para o _sample_ da biblioteca __API JPA__ disponível em nosso [GitHub][tjf-api-jpa-sample].

# Começando com SGDP

Com o projeto _sample_ __API JPA__ em mãos, vamos alterar alguns campos no banco de dados e incluir as anotações disponibilizadas pelo SGDP na classe de entidade.

> Como _engine_ de banco de dados utilizaremos o [H2][h2].

## Dependências

Incluir a biblioteca como dependência no `pom.xml` da aplicação:

```xml
<dependency>
    <groupId>com.totvs.tjf</groupId>
    <artifactId>tjf-sgdp-core</artifactId>
</dependency>
```

Além das propriedades existentes no `application.yml` da aplicação, será necessário incluir novas propriedades. Conforme o exemplo:


## Criando as tabelas

Para que seja possível demonstrar de forma mais efetiva algumas das anotações disponibilizadas no SGDP, precisaremos alterar nossa classe `Jedi.java` e adicionar os campos `identification` (algo como o CPF dos Jedis :D) e `Email`.

```Java
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getIdentification() {
		return globalIdentification;
	}

	public void setIdentification(int identification) {
		this.identification = identification;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
```
Para melhor entendimento, o tutorial será dividido em tópicos, são eles: 

* __Identificação__ - Uso de anotações para identificar dados pessoais;

* __Auditoria__ - Auditoria dos tratamentos realizados em dados pessoais; e 

* __Metadado__ - Extração de relatório do metadado da aplicação.


## Identificação

Agora vamos a inclusão das anotações SGDP, vamos começar com a anotação `@SGDPData` que incluiremos nos campos com dados pessoais. Além disso, vamos incluir a `@SGDPPurpose` que define um dos propósitos do tratamento de um determinado dado pessoal e o `@SGDPDescription` que torna mais claro a descrição da entidade ou atributo durante a geração do metadado.

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

## Auditoria



## Metadado

O SGDP conta com um serviço para extração do metadado do banco de dados da aplicação e as identificações feitas com o as anotações SGDP.

Para extrair o metadado conforme descrito na documentação do SGDP, basta realizar um GET no endpoint `/sgdp/metadata`, tendo como resultado o Json abaixo:

```json
{
    "tables": {
        "br.com.star.wars.model.Jedi": {
            "sgdpSupport": true,
            "description": "Jedi",
            "fields": {
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
        "br.com.star.wars.model.Jedi": {
            "description": "Validar o LGPD do TJF, sobre a identificação, auditoria e anonimização de dados pessoais dos Jedi"
        }
    },
    "package": "br.com.star.wars"
}
```

# Isso é tudo pessoal!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pelo componente [SGDP][tjf-sgdp] e enviar sugestões e melhorias para o projeto __TOTVS Java Framework__.

[tjf-api-jpa-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-api-samples/tjf-api-jpa-sample
[h2]: https://www.h2database.com
[tjf-sgdp]: https://tjf.totvs.com.br/wiki/tjf-sgdp