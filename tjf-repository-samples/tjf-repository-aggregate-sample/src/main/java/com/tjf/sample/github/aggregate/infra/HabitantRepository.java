package com.tjf.sample.github.aggregate.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HabitantRepository {
	
	@Lazy
	@Autowired
	private ConversionService conversionService;
	
	@Autowired
	private HabitantModelRepository habitantModelRepository;
	
	@Transactional
	public void store(final Habitant habitant) {
		habitantModelRepository.insert(conversionService.convert(habitant, HabitantModel.class));
	}

}
