package com.tjf.sample.github.domain.jpa.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.totvs.tjf.domain.jpa.customtypes.UUIDWrapper;

public class AccountId extends UUIDWrapper {

	private static final long serialVersionUID = -5861098387336658229L;

    protected AccountId(UUID value) {
        super(value);
    }

    public static AccountId generate() {
        return new AccountId(UUID.randomUUID());
    }

    @JsonCreator
    public static AccountId from(String value) {
        return new AccountId(UUID.fromString(value));
    }
}
