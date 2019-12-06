# Diferentes Maneiras de Realizar uma Busca de Dados

Neste exemplo vamos mostrar diferentes técnicas para se fazer uma seleção dos seus dados. Para isso usaremos 3 técnicas de busca, sendo elas **NamedQuery**, **Criteria** e **Specification**. 

> Durante o exemplo não será detalhado o projeto, apenas citado os fontes para a busca dos códigos.

O projeto foi criado baseado no Spring e não utiliza componentes providos pelo framework TJF, pois o objetivo é mostrar as técnicas de seleção.

## O projeto

Ao abrir o projeto em sua IDE de preferência será possível ver que o mesmo possui uma arquitetura simples, contendo apenas um pacote para o modelo dos objetos, um pacote para o repositório e outro para o controller, além do pacote padrão de configuração do projeto.

## Queries

Como citado anteriormente o projeto contém 3 técnicas de query, desta forma iremos explorar um pouco de cada técnica, contendo um retorno com informação de tempo das buscas, facilitando a sua escolha na hora de optar entre as técnicas de busca.

### NamedQuery

Sendo uma das técnicas mais utilizadas, por possuir uma fácil construção e alto controle no desenvolvimento, a mesma é criada na anotação `@Query` sob o método de busca criado. Em nosso projeto sua declaração está no método `DroidRepository` no pacote `br.com.star.wars.repository`. Como exemplo:

```java
@Query("SELECT d FROM droid d WHERE d.name LIKE CONCAT('%', :name, '%')")
List<Droid> getByName(@Param("name") String name);
```

### Criteria

Uma técnica muito utilizada por frameworks de consulta de dados, por ser simples e maleavel, pois é possível montar complexas consultas de forma rápida, simples e segura. Em nosso projeto é possível ver sua utilização no pacote `DroidController` nos métodos `criteriaGetByName`, `criteriaGetByDescription` e `criteriaGetByNameAndDescription`. Como exemplo de sua utilização:

```java
CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
CriteriaQuery<Droid> criteriaQuery = criteriaBuilder.createQuery(Droid.class);

Root<Droid> droid = criteriaQuery.from(Droid.class);
Predicate filterByDescription = criteriaBuilder.like(droid.get("description"), "%" + description + "%");
criteriaQuery.where(filterByDescription);
```

### Specification

Specification é uma técnica presente na utilização de JPA, sendo visivel no framework Spring e possuindo uma montagem de clausulas de busca simples. Muito similar ao criteria, entretanto não exige tantas definições para seu uso. Em nosso projeto a classe `DroidSpecification` do pacote `br.com.star.wars.model` possui as definições de filtro. Como exemplo:

```java
public static Specification<Droid> nameLike(String name) {
	return (root, query, cb) -> cb.like(cb.upper(root.get("name")), DroiSpecification.likeConstructor(name));
}
```

## Resultados

Para visualização dos resultados, é possível importar a collection *Sample Query.json* localizada na pasta `resources` do projeto. Ao iniciar o projeto execute as requisições da collection, dessa forma os valores da tabela filtrada serão retornados, assim como valores de informação da query executada e seu tempo.

## Conclusão

Este projeto serve como base para auxiliar na escolha da técnica ideal para seu projeto, observando cada uma delas possui benefícios de uso, então vale utilizar a qual melhor se adequa ao seu projeto.

Lembre-se de acessar o portal [TJF](https://tjf.totvs.com.br/) para acompanhar o projeto e fique a vontade para ler outros Guias sobre tecnologias utilizadas no TJF.
