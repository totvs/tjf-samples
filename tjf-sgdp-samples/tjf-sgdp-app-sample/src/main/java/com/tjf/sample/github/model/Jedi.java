package com.tjf.sample.github.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.totvs.sgdp.sdk.annotations.SGDPClassification;
import com.totvs.sgdp.sdk.annotations.SGDPCode;
import com.totvs.sgdp.sdk.annotations.SGDPData;
import com.totvs.sgdp.sdk.annotations.SGDPDescription;
import com.totvs.sgdp.sdk.annotations.SGDPPurpose;
import com.totvs.sgdp.sdk.annotations.SGDPType;
import com.totvs.sgdp.sdk.audit.SGDPSupport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(SGDPSupport.class)
@SGDPDescription("Jedi")
@SGDPCode("Validar o LGPD do TJF, sobre a identificação, auditoria e anonimização de dados pessoais dos Jedi")
public class Jedi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@SGDPDescription("Nome do Jedi")
	private String name;

	@NotNull
	@SGDPData(allowsAnonymization = true, isSensitive = true, type = SGDPType.CPF)
	@SGDPPurpose(classification = SGDPClassification.REGULAR_EXERCISE_OF_LAW, justification = "Numero de identificação do Jedi")
	@SGDPDescription("Identification")
	private int identification;

	@NotNull
	@SGDPData(allowsAnonymization = true, type = SGDPType.EMAIL)
	@SGDPPurpose(classification = SGDPClassification.CONSENTMENT, justification = "Email para contato.")
	@SGDPPurpose(classification = SGDPClassification.CONTRACT_EXECUTION, justification = "Necessário para contato.")
	@SGDPDescription("Email")
	private String email;

	@NotNull
	@SGDPData(isSensitive = true)
	@SGDPDescription("Gender")
	private String gender;
}