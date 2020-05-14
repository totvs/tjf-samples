# OSGI Sample

_Sample_ de uma aplicação [Spring Boot][spring-boot] & [TOTVS Java Framework][tjf] sendo executada dentro de um container [OSGI][osgi], mais especificamente o [Apache Karaf][karaf].

## Spring Boot

A inicialização de uma aplicação **Spring** é feita normalmente na execução do método estático **main**, como no exemplo abaixo:

```java
public static void main(String[] args) {
	SpringApplication.run(SwQueryApplication.class, args);
}
```

Já a inicialização de uma aplicação **OSGI** necessita implementar os métodos abaixo da interface __org.osgi.framework.BundleActivator__:

```java
@Override
public void start(BundleContext bundleContext) {
	// Altera o classLoader da Thread corrente para o Spring Boot  
	Thread.currentThread().setContextClassLoader(SwQueryApplication.class.getClassLoader());

	OsgiBundleResourcePatternResolver resourceResolver = new OsgiBundleResourcePatternResolver(bundleContext.getBundle());
	appContext = new SpringApplication(resourceResolver, SwQueryApplication.class).run();
}

@Override
public void stop(BundleContext bundleContext) {
    SpringApplication.exit(appContext, new ExitCodeGenerator() {
		@Override
		public int getExitCode() {
			return 0;
		}
    });
}
```

O módulo **tjf-core-common** do **TJF**, disponibiliza uma classe que implementa a interface acima e que pode ser utiliza conforme o exemplo abaixo:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.totvs.tjf.core.utils.OSGIApplication;

@SpringBootApplication
public class SwQueryApplication extends OSGIApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwQueryApplication.class, args);
	}

}
```

Porém mesmo assim é necessário incluir a dependência abaixo:

```xml
<dependency>
	<groupId>org.osgi</groupId>
	<artifactId>org.osgi.core</artifactId>
	<scope>provided</scope>
</dependency>
```

O endpoint de monitoramento JMX deve ser desabilitado no **application.yml** da aplicação, caso contrário uma segunda aplicação Spring Boot não será iniciada:

```yml
jmx:
	enabled: false
```

## Container WEB

O **Karaf** por padrão utiliza o [Eclipse Jetty][jetty] que já suporta a plataforma OSGI. Quando necessário este deve utilizado no lugar do [Apache Tomcat][tomcat] por meio da configuração abaixo no **pom.xml** da aplicação:

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

## Empacotamento

O **OSGI** trabalha com o conceito de **Bundle**, que nada mais é do que uma forma diferente de empacotar a aplicação.

O _plugin_ **maven-bundle-plugin** foi incorporado ao módulo **tjf-boot-starter** do [TJF][tjf], e para utilizá-lo basta informar o _profile_ **tjf-osgi** na execução do **Maven**.

```script
mvn clean install -Ptjf-osgi
```

Este plugin necessita também de duas informações adicionais que devem ser informadas no __pom.xml__ da aplicação:

```xml
<properties>
	<osgi.package>br.com.star.wars</osgi.package>
	<osgi.activator>SwQueryApplication</osgi.activator>
</properties>
```

Feito isto nossa aplicação pode ser instalada no **Karaf** por meio do comando abaixo:

```karaf
bundle:install mvn:com.totvs.tjf/tjf-osgi-simple/1.0-SNAPSHOT
```

## Persistência

O [Spring Data JPA][spring-data-jpa] (ainda) não funcionada adequadamente dentro um container OSGI, uma vez que utilizada a classe DriverManager do Driver JDBC e existe um conflito de ClassLoaders fazendo com que na criação do EntityManager para a URL configurada não fique disponível.

Já o [Spring Data JDBC][spring-data-jdbc] funciona sem maiores problemas, que permite o acesso ao banco por meio da anotação **@Query (HQL)**, **JDBC Templates** ou **MyBatis Queries**.

## Testando

O [Apache Karak][karaf] pode ser baixado [aqui][karaf-download]. Já instruções para instalar o ActiveMQ (Artemis) no Karaf podem ser encontradas [aqui][artemis-on-karaf].

Uma vez iniciada a aplicação dentro do Karaf a mesma pode ser testada pela URL:

```http
GET /api/v1/droid/getAll HTTP/1.1
Host: localhost:8180
```

Que deve retornar o conteúdo abaixo:

```json
[
{"id":1,"name":"BB Unit","description":"Super droid de batalha B2 modificado"},
{"id":2,"name":"B2-RP Battle Droid","description":"As unidades do BB trabalham incansavelmente para manter as armas e os navios da Resistência"},
{"id":3,"name":"Courier Droid","description":"Droids de correio usados ​​para retransmitir informações de um lugar para outro"},
{"id":4,"name":"Imperial Probe Droid","description":"Especialmente projetados para a exploração e reconhecimento do espaço profundo"},
{"id":5,"name":"Mouse Droid","description":"Realizam tarefas simples a bordo de naves espaciais e em instalações militares e corporativas"}
]
```

## Que a força esteja com você!

Com isso terminamos nosso  _sample_ , fique a vontade para enriquecê-lo utilizando outros recursos do **TJF** e enviar sugestões e melhorias para o [TOTVS Java Framework][tjf].

[spring-boot]: https://spring.io/projects/spring-boot
[tjf]: https://tjf.totvs.com.br
[osgi]: https://www.osgi.org/
[karaf]: https://karaf.apache.org/
[karaf-download]: https://karaf.apache.org/download.html
[jetty]: https://www.eclipse.org/jetty/
[tomcat]: http://tomcat.apache.org/
[spring-data-jpa]: https://spring.io/projects/spring-data-jpa
[spring-data-jdbc]: https://spring.io/projects/spring-data-jdbc
[artemis-on-karaf]: https://activemq.apache.org/components/artemis/documentation/1.3.0/karaf.html
