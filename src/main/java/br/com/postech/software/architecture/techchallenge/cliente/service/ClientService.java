package br.com.postech.software.architecture.techchallenge.cliente.service;

import br.com.postech.software.architecture.techchallenge.cliente.dto.ClienteDTO;
import br.com.postech.software.architecture.techchallenge.cliente.dto.ValidaClienteResponseDTO;
import br.com.postech.software.architecture.techchallenge.cliente.exception.BusinessException;

import java.util.List;

public interface ClientService {

    List<ClienteDTO> listarClientesAtivos();

    ClienteDTO findById(Integer id);

    ClienteDTO save(ClienteDTO clienteDTO);

    ClienteDTO atualizarCliente(Integer id, ClienteDTO clienteDTO);

    ClienteDTO desativarCliente(Integer id);

    List<ClienteDTO> findAllByCpfOrNomeOrEmail(String cpf, String nome, String email) throws BusinessException;

    ValidaClienteResponseDTO valideCliente(ClienteDTO clienteDTO);
}
