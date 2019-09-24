# TJF Process Service Sample

_Sample_ de utilização do componente [__TJF Process Service__][tjf-process-service] do [__TOTVS Java Framework__][tjf].

# Definições

O [__TJF Process Service__][tjf-process-service] é um orquestrador pronto para uso que não necessita de codificação. No nosso exemplo existem definições padrões são fornecidas ao mesmo por meio da pasta "definitions", e que é referenciada no docker-compose abaixo:

docker-compose.yml
```docker
version: '3'

services:

  rabbitmq:
    image: "rabbitmq:3-management"
    environment:
      RABBITMQ_DEFAULT_USER: "guest"
      RABBITMQ_DEFAULT_PASS: "guest"
    ports:
      - "9996:15672"
    volumes:
      - rabbitmq-volume:/var/lib/rabbitmq

  postgres:
    image: "postgres"
    environment:
      POSTGRES_DB: "processdb"
      POSTGRES_USER: "admin"
      POSTGRES_PASSWORD: "admin"
    ports:
      - "9997:5432"

  process:
    image: "docker.totvs.io/tjf/process-service-discriminator"
    depends_on:
      - postgres
      - rabbitmq
    volumes:
      - ./definitions:/definitions
    ports:
      - "9998:8080"
    environment:
      serviceDiscriminatorDomainExchange: "domain-exchange"
      serviceDiscriminatorDBHost: "postgres/processdb"
      serviceDiscriminatorDBUser: "admin"
      serviceDiscriminatorDBPass: "admin"
      serviceDiscriminatorRabbitHost: "rabbitmq"
      serviceDiscriminatorRabbitUsername: "guest"
      serviceDiscriminatorRabbitPassword: "guest"

volumes:
  rabbitmq-volume:
```

# Executando

Na pasta principal do exemplo execute os seguintes comando:

```cmd
sudo docker-compose up
```

Execute agora um dos exemplos abaixo:

1. [__tjf-process-domain-java-sample__][tjf-process-domain-java-sample]
2. [__tjf-process-domain-node-sample__][tjf-process-domain-node-sample]

Se tudo correr bem você verá uma sequencia de mensagens que representam as atividades de nossa orquestração. Verifique também o log de execução da aplicação.

# Que a força esteja com você!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pela biblioteca [__TJF Process Service__][tjf-process-service] e enviar sugestões e melhorias para o [__TOTVS Java Framework__][tjf].

[tjf]: https://tjf.totvs.com.br
[tjf-process-service]: https://tjf.totvs.com.br/wiki/tjf-process-service
[tjf-process-domain-java-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-process/tjf-process-domain-java-sample
[tjf-process-domain-node-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-process/tjf-process-domain-node-sample
