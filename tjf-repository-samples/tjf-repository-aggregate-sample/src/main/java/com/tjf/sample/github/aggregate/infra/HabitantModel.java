package com.tjf.sample.github.aggregate.infra;

import javax.validation.constraints.NotNull;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

@Aggregate
public class HabitantModel {

    @AggregateIdentifier
	private String id;

	@NotNull
	private String name;

	@NotNull
	private String gender;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
	
    public HabitantModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.gender = builder.gender;
    }
    
    @SuppressWarnings("unused")
    private HabitantModel() {
        this.id = null;
        this.name = null;
        this.gender = null;
    }
    
    public static class Builder {

        private String id;

        private String name;
        
        private String gender;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }


        public HabitantModel build() {
            return new HabitantModel(this);
        }

    }

}
