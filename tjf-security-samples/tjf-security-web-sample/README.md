# Exemplo de uso do componente Security Web

## Contexto

Para explicação do componente **Security Web** vamos criar uma API rest para controle de maquinas de uma fábrica e aplicar segurança de autenticação e autorização nos endpoins dessa API.

## Começando com Security Web

Para criação deste exemplo, você vai precisar de um Authorization Service para podermos realizar as validações de segurança, por exemplo o **TOTVS RAC**.

Vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto criado basta acessar o [Spring initializr]([https://start.spring.io/](https://start.spring.io/)) e criar o projeto.

Para fácil entendimento do componente **Security Web** vamos seguir a sequencia a baixo para criação do exemplo.

### Dependências

Primeiramente configure o repositório do TJF em seu `pom.xml`.

```xml
<repositories>
    <repository>
        <id>central-snapshot</id>
        <name>TOTVS Java Framework: Snapshots</name>
        <url>http://maven.engpro.totvs.com.br/artifactory/libs-release</url>
    </repository>
</repositories>
```

Adicione o _parent_ do TJF.

```xml
<parent>
	<groupId>com.totvs.tjf</groupId>
	<artifactId>tjf-boot-starter</artifactId>
	<version>1.11.0-RELEASE</version>
</parent>
```

Para utilização do componente de segurança do TJF é necessário inserir a seguinte dependência em seu arquivo `pom.xml`.

```xml
<dependency>
    <groupId>com.totvs.tjf</groupId>
    <artifactId>tjf-security-web</artifactId>
</dependency>
```

Em nosso exemplo iremos utilizar um serviço web para disponibilizar os endpoints, para isso adicione a seguinte dependência em seu arquivo `pom.xml`.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
### Configurando integração com o *Authorization Service*

No arquivo `application.yml` precisamos informar nas propriedades na nossa aplicação, quais as URI que serão usadas, para que nossa aplicação integre com o *Authorization Service*.

```yml
security:
  access:
    api:
      permissions-uri: <O URI para validação das permissions.>
  oauth2:
    resource:
      id: 'authorization_api'
      jwk:
        key-set-uri: <O URI para obter chaves de verificação para validação do token JWT> 
        
```

> **Propriedade opcional**  
A propriedade `security.access.api.permissions-uri` é necessária apenas se no código for utilizada a anotação `@PreAuthorize` com a função `hasPermission()`. 


### Criando o modelo

No nosso exemplo usaremos o enum `Type`, que define os tipos de máquinas que teremos em nossa fábrica.

```java
public enum Type {
	COLLECTOR,
	CONVEYOR,
	PRESS;
}
```
A class `Machine` será o modelo principal da nossa aplicação, nela teremos os métodos para rodar, parar e verificar se está em execução.

```java
public class Machine {

	private boolean running = false;
	private final Type type;

	public Type getType() {
		return type;
	}

	public Machine(Type type) {
		this.type = type;
	}

	public boolean running() {
		return running;
	}

	public void run() {
		this.running = true;
	}

	public void stop() {
		this.running = false;
	}
}
```

### Criando o componente

Para gerenciar nossas maquinas criaremos o component `MachineManager`, o qual nossos endpoints irão interagir para parar uma maquina, executar determinada maquina ou parar todas as maquinas.
A título de exemplo já instanciaremos a `MachineManager` com 3 maquinas diferentes em seu pool, uma de cada um dos tipo.

```java
@Component
public class MachineManager {

	private List<Machine> machines = new LinkedList<Machine>();

	public MachineManager(){
		machines.add(new Machine(Type.COLLECTOR));
		machines.add(new Machine(Type.CONVEYOR));
		machines.add(new Machine(Type.PRESS));
	}

	public void stop(int id) {
		machines.get(id).stop();
	}

	public void stopAll() {
		for(Machine machine:machines) {
			machine.stop();
		}
	}

	public void run(int id) {
		machines.get(id).run();
	}

	public List<Map<String, Object>> getMachines() {
		List<Map<String, Object>> listMachines = new ArrayList<Map<String, Object>>();
		for(int id = 0; id < machines.size();id++) {
			listMachines.add(getMachine(id));
		}
		return listMachines;
	}

	public HashMap<String, Object> getMachine(int id) {
		Machine machine = machines.get(id);

		HashMap<String, Object> mapMachine = new HashMap<String, Object>();
	    mapMachine.put("id", id);
	    mapMachine.put("type", machine.getType());
	    mapMachine.put("running", machine.running());

		return mapMachine;
	}
}
```

### Criando os endpoints


Para iniciarmos o desenvolvimento dos endpoints primeiramente vamos criar o Controller que ficará responsável receber as requisições e interagir com nosso `MachineManager`.

Nela criaremos 3 endpoints:
* Um sem validação de autorização, irá verificar apenas a autenticação do usuário. Nela usaremos apenas a anotação `@GetMapping`.
* Um com autorização por *role*, que irá verificar se o usuário tem determinada autorização em determinada *role*. Para isso usaremos alem da anotação `@PostMapping` usaremos a anotação `@RolesAllowed`, passando de parâmetro para ela a *role*.
* E por fim dois outros endpoints com autorização por *permission*, que irá verificar se o usuário tem autorização em determinada *permission*. Para isso usaremos alem da anotação `@PostMapping` a anotação `@PreAuthorize`, passando para ela o método `hasPermission` e que receberá qual a *permission* que queremos validar.

> **Lembrando**: O uso de *permission* depende da correta configuração da propriedade `security.access.api.permissions-uri` no arquivo de configurações `application.yml`.

```java
@RestController
@RequestMapping(path = "/api/v1/machine", produces = { APPLICATION_JSON_VALUE })
public class MachineController {

	@Autowired
	private MachineManager machineManager;

	@GetMapping
	public List<Map<String, Object>> machineList() {
		return machineManager.getMachines();
	}

	@PostMapping("stop")
	@RolesAllowed("ROLE_SUPERVISOR")
	public List<Map<String, Object>> stopAll() {
		machineManager.stopAll();
		return machineManager.getMachines();
	}

	@PostMapping("{id}/run")
	@PreAuthorize("hasPermission(\"SampleSecurityWeb.machine.run\")")
	public Map<String, Object> run(@PathVariable int id) {
		machineManager.run(id);
		return machineManager.getMachine(id);
	}
}
```

### Fluxo de validação da autorização

Esse é o fluxo para realizar uma *request* usando o `tjf-security-web` que passará por todas as etapas.

![Diagrama de Sequência](resources/diagrama_sequencia.png)

## Vamos testar?

Para realizarmos o testes, execute a classe de aplicação do nosso exemplo.

Para facilitar nossos testes usaremos o [Postman](https://www.getpostman.com/).

### Endpoint com apenas validação de autenticação

Crie um novo request no Postman do tipo *get* para o endpoint [http://localhost:\<porta da sua aplicação\>/api/v1/machine](http://localhost:8080/api/v1/machine) e clique em `Send` para executar a request.

Como não autenticamos nossa request, o retorno deverá ser o seguinte:
```json
{
    "error": "unauthorized",
    "error_description": "Full authentication is required to access this resource"
}
```
Agora vamos autenticar a request no Postman:
* Vá na aba *Authorization* da request que criamos, em *Type* selecione o `Oauth 2.0` e clique em `Get New Acess Token`.
* Informe um nome ao token que será gerado e o tipo como *Authorization Code*.
* Informe os demais campos conforme o Authorization Service no qual você está integrando.
* Clique em `Request Token`, ele irá solicitar o usuário e senha para autenticar no *Authorization Service*.
* Gerado o token clique em `Use Token`.

Agora que estamos autenticados, execute novamente a request, deveremos conseguir consultar com sucesso todas as maquinas, o retorno deverá ser como esse:
```json
[
    {
        "running": false,
        "id": 0,
        "type": "COLLECTOR"
    },
    {
        "running": false,
        "id": 1,
        "type": "CONVEYOR"
    },
    {
        "running": false,
        "id": 2,
        "type": "PRESS"
    }
]
```

### Endpoint com validação de autorização por *permission*

Agora vamos fazer um teste do endpoint com a validação de autorização por *permission*.

Crie um novo request no Postman do tipo *post* para o endpoint [http://localhost:\<porta da sua aplicação\>/api/v1/machine/\<id da maquina\>/run](http://localhost:8080/api/v1/machine/0/run) e clique em `Send` para executar a request.

Como não autenticamos a request, o retorno deverá ser o seguinte:
```json
{
    "error": "unauthorized",
    "error_description": "Full authentication is required to access this resource"
}
```
Autentique a request, conforme já vimos, mas use um usuário que no *Authorization Service* no qual você está integrando não tem autorização para a *permission* `SampleSecurityWeb.machine.run`.
**Observação:** Talvez você precisará antes excluir os cookies no Postman, para que ele não use o mesmo usuário já informado.

Execute a request, o retorno será conforme abaixo, mostrando que estamos autenticado, mas o usuário não tem permissão:
```json
{
    "error": "access_denied",
    "error_description": "Access denied"
}
```

Agora autentique a request gerando um novo token, mas agora com um usuário que tem autorização para a *permission* `SampleSecurityWeb.machine.run`, que foi a *permission* que definimos para o nosso endpoint.

Vamos executar novamente a request, você deve conseguir iniciar a maquina com sucesso e o retorno deverá ser como este:
```json
{
    "running": true,
    "id": <Id da maquina>,
    "type": <Tipo da maquina>
}
```
### Endpoint com validação de autorização por *role*

Agora vamos fazer um teste do endpoint com a validação de autorização por *role*.

Crie um novo request no Postman do tipo *post* para o endpoint [http://localhost:\<porta da sua aplicação\>/api/v1/machine/stop](http://localhost:8080/api/v1/machine/stop) e clique em `Send` para executar a request.

Como não autenticamos a request, o retorno deverá ser o seguinte:
```json
{
    "error": "unauthorized",
    "error_description": "Full authentication is required to access this resource"
}
```
Autentique a request, conforme já vimos, mas use um usuário que no *Authorization Service* no qual você está integrando não tem autorização para a *role* `SUPERVISOR`.
**Observação:** Talvez você precisará antes excluir os cookies no Postman, para que ele não use o mesmo usuário já informado anteriormente.

Execute a request, o retorno será conforme abaixo, mostrando que estamos autenticado, mas o usuário não tem permissão:
```json
{
    "error": "access_denied",
    "error_description": "Access denied"
}
```

Agora autentique a request gerando um novo token, mas agora com um usuário que tem autorização para a *role* `SUPERVISOR`, que foi a *role* que definimos para o nosso endpoint.

Vamos executar novamente a request, você deve conseguir parar todas maquinas com sucesso e o retorno deverá ser como este:
```json
[
    {
        "running": false,
        "id": 0,
        "type": "COLLECTOR"
    },
    {
        "running": false,
        "id": 1,
        "type": "CONVEYOR"
    },
    {
        "running": false,
        "id": 2,
        "type": "PRESS"
    }
]
```

**Importante:** Os retornos de erros seguirão o padrão do [API Core](https://tjf.totvs.com.br/wiki/tjf-api-core) caso você o use nos endpoints do seu projeto.

## Isso é tudo pessoal!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos recursos proposto pelo componente **Security Web**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-security-web).

E fique a vontade para mandar sugestões e melhorias para o projeto TJF.
