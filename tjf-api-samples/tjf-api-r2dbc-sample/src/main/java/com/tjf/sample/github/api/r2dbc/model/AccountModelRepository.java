package com.tjf.sample.github.api.r2dbc.model;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

//import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;

public interface AccountModelRepository
		extends ReactiveCrudRepository<AccountModel, Integer>

//, ApiJpaRepository<AccountModel> 

{
}
