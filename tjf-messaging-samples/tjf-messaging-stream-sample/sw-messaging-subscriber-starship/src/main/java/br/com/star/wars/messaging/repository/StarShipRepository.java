package br.com.star.wars.messaging.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.star.wars.messaging.model.Cliente;
import br.com.star.wars.messaging.model.StarShip;

@Repository
public interface StarShipRepository extends JpaRepository<StarShip, UUID> {
}
