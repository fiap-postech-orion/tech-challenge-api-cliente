package br.com.postech.software.architecture.techchallenge.cliente.repository;

import br.com.postech.software.architecture.techchallenge.cliente.model.Cliente;
import br.com.postech.software.architecture.techchallenge.cliente.exception.PersistenceException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteJpaRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByStatus(Boolean c);

    Optional<Cliente> findByIdAndStatus(Integer id, Boolean status);

    Optional<Cliente> findById(Integer id);

    List<Cliente> findAllByCpfOrNomeOrEmail(String cpf, String nome, String email) throws PersistenceException;
}
