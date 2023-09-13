# SGDP - Sistema de Gestão de Dados Pessoais

:warning: **Biblioteca depreciada, em breve não estará disponível. Caso prefira um exemplo mais atualizado utilizar o [sgdp-service-sample-test](https://totvstfs.visualstudio.com/TOTVSApps-SupportElements/_git/sgdp-service-sample-test).**

## Contexto

Este _sample_ tem o objetivo demonstrar o componente **SGDP Core** do **TOTVS Java Framework**.

Ele é composto por um exemplo de uma aplicação detentora de dados pessoais e de um serviço de platafaforma SGDP responsável pelo gerenciamento destes dados pessoais no que diz respeito aos direitos do titular de dados.

## Ambiente

Como estes exemplo fazem uso do **Apache Kafka** e do **Rabbit** este ambiente deve ser provisionado executando o comando abaixo na pasta raiz do tjf-sample-sgdp:

```cmd
docker-compose up
```

## Execução dos Samples

Os samples deve ser instanciados por meio do comando abaixo na raiz de suas respetivas pastas:

```cmd
mvn spring-boot:run
```

## Hora de testar

Agora é só seguir as orientações das documentações na seguinte ordem:

- tjf-sgdp-app-sample
- tjf-sgdp-service-sample

## Isso é tudo pessoal!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pelo componente [SGDP][tjf-sgdp] e enviar sugestões e melhorias para o projeto **TOTVS Java Framework**.

[tjf-api-jpa-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-api-samples/tjf-api-jpa-sample
[h2]: https://www.h2database.com
[tjf-sgdp]: https://tjf.totvs.com.br/wiki/tjf-sgdp-core
