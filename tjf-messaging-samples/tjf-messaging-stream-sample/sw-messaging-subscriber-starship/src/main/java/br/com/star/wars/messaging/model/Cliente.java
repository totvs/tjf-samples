package br.com.star.wars.messaging.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by eml on 27/04/17.
 */
@Entity
@Table(name = "CLIENTE")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column
	private String nome;

	Cliente() {
	}

	@Column
	private String rg;

	public UUID getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Cliente(final String pNome, final String pRg) {
		nome = pNome;
		rg = pRg;
	}

	public String getRg() {
		return rg;
	}

	public void setId(final UUID pId) {
		id = pId;
	}

	public void setNome(final String pNome) {
		nome = pNome;
	}

	public void setRg(final String pRg) {
		rg = pRg;
	}

	@Override
	public String toString() {
		return "Cliente{" + "id=" + id + ", nome='" + nome + '\'' + ", rg='" + rg + '\'' + '}';
	}
}
