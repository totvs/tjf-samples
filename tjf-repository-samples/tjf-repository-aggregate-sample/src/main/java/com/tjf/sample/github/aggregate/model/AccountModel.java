package com.tjf.sample.github.aggregate.model;

import java.io.Serializable;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

import lombok.Getter;

@Getter
@Aggregate
public class AccountModel implements Serializable {

	private static final long serialVersionUID = 3181541174652364912L;

	@AggregateIdentifier
	private Integer accountId;

	private String name;
	
	private String address;
	
	private Double balance;
	
}
