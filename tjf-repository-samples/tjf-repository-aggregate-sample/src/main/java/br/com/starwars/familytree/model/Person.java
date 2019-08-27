package br.com.starwars.familytree.model;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person extends Human {

	@AggregateIdentifier
	private String id;

}
