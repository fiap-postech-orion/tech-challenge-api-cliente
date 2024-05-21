package br.com.postech.software.architecture.techchallenge.cliente.service.impl;

import br.com.postech.software.architecture.techchallenge.cliente.configuration.ModelMapperConfiguration;
import br.com.postech.software.architecture.techchallenge.cliente.dto.ClienteDTO;
import br.com.postech.software.architecture.techchallenge.cliente.dto.ValidaClienteResponseDTO;
import br.com.postech.software.architecture.techchallenge.cliente.exception.BusinessException;
import br.com.postech.software.architecture.techchallenge.cliente.model.Cliente;
import br.com.postech.software.architecture.techchallenge.cliente.repository.ClienteJpaRepository;
import br.com.postech.software.architecture.techchallenge.cliente.service.ClientService;
import br.com.postech.software.architecture.techchallenge.cliente.util.CpfCnpjUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClientService {

    private final ClienteJpaRepository clienteJpaRepository;
    private static final ModelMapper MAPPER = ModelMapperConfiguration.getModelMapper();

    @Override
    public List<ClienteDTO> listarClientesAtivos() {
        List<Cliente> clientesAtivos = clienteJpaRepository.findByStatus(Boolean.TRUE);
        return clientesAtivos.stream()
                .map(cliente -> MAPPER.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO findById(Integer id) {
        Optional<Cliente> cliente = clienteJpaRepository.findByIdAndStatus(id, Boolean.TRUE);
        if (cliente.isPresent()) {
            return MAPPER.map(cliente.get(), ClienteDTO.class);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
    }

    public boolean isThereClienteById(Integer id) {
        return clienteJpaRepository.findByIdAndStatus(id, Boolean.TRUE).isPresent();
    }

    @Override
    public ClienteDTO save(ClienteDTO clienteDTO) {
        var cliente = MAPPER.map(clienteDTO, Cliente.class);

        if (Objects.nonNull(cliente.getCpf())) {
            cliente.setCpf(CpfCnpjUtil.removeMaskCPFCNPJ(cliente.getCpf()));
        }

        cliente = clienteJpaRepository.save(cliente);

        return MAPPER.map(cliente, ClienteDTO.class);
    }

    @Override
    public ClienteDTO atualizarCliente(Integer id, ClienteDTO clienteDTO) {
        Cliente clienteMappado = clienteJpaRepository.findById(id)
                .map(cliente -> {
                            cliente.setNome(clienteDTO.getNome());
                            cliente.setEmail(clienteDTO.getEmail());
                            cliente.setCpf(clienteDTO.getCpf());
                            return clienteJpaRepository.save(cliente);
                        }
                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        return MAPPER.map(clienteMappado, ClienteDTO.class);
    }

    @Override
    public ClienteDTO desativarCliente(Integer id) {
        Optional<Cliente> clienteOptional = clienteJpaRepository.findById(id);

        if (!clienteOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }

        Cliente cliente = clienteOptional.get();
        cliente.setStatus(Boolean.FALSE);
        cliente = clienteJpaRepository.save(cliente);

        return MAPPER.map(cliente, ClienteDTO.class);
    }

    @Override
    public List<ClienteDTO> findAllByCpfOrNomeOrEmail(String cpf, String nome, String email) throws BusinessException {
        return clienteJpaRepository.findAllByCpfOrNomeOrEmail(cpf, nome, email)
                .stream()
                .map(cliente -> MAPPER.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());

    }

    public ValidaClienteResponseDTO valideCliente(ClienteDTO clienteDTO) {
        if (isThereClienteById(clienteDTO.getId().intValue())) {
            return new ValidaClienteResponseDTO().toBuilder()
                    .setClienteDTO(clienteDTO)
                    .setIsValid(true)
                    .build();
        }

        List<ClienteDTO> clienteDTOs = findAllByCpfOrNomeOrEmail(
                clienteDTO.getCpf(),
                clienteDTO.getNome(),
                clienteDTO.getEmail());

        if (!CollectionUtils.isEmpty(clienteDTOs)) {
            return new ValidaClienteResponseDTO().toBuilder()
                    .setClienteDTO(clienteDTO)
                    .setIsValid(true)
                    .build();
        }

        return new ValidaClienteResponseDTO().toBuilder()
                .setIsValid(false)
                .setErrorMessage("Cliente não encontrado")
                .build();
    }
}
