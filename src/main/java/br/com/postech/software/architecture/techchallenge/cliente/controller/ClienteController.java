package br.com.postech.software.architecture.techchallenge.cliente.controller;

import br.com.postech.software.architecture.techchallenge.cliente.dto.ClienteDTO;
import br.com.postech.software.architecture.techchallenge.cliente.dto.ValidaClienteResponseDTO;
import br.com.postech.software.architecture.techchallenge.cliente.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping("/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> clientesAtivos = clienteService.listarClientesAtivos();
        return new ResponseEntity<>(clientesAtivos, HttpStatus.OK);
    }

    @GetMapping(path = "/filter", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<List<ClienteDTO>> listByCpfOrNomeOrEmail(
            @RequestParam(required = false, name = "cpf") String cpf,
            @RequestParam(required = false, name = "nome") String nome,
            @RequestParam(required = false, name = "email") String email
    ) {
        return new ResponseEntity<>(clienteService.findAllByCpfOrNomeOrEmail(cpf, nome, email), HttpStatus.OK);
    }

    @GetMapping(path = "/{idCliente}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ClienteDTO> buscarCliente(@PathVariable("idCliente") Integer idCliente) throws Exception {
        return new ResponseEntity<>(clienteService.findById(idCliente), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ClienteDTO> salvarCliente(@RequestBody @Valid ClienteDTO clienteDTO) throws Exception {
        clienteDTO.sanitiseDTO();
        clienteDTO.setStatus(Boolean.TRUE);
        return new ResponseEntity<>(clienteService.save(clienteDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{idCliente}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ClienteDTO> atualizarCliente(@PathVariable("idCliente") Integer id, @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO updatedClienteDTO = clienteService.atualizarCliente(id, clienteDTO);

        return new ResponseEntity<>(updatedClienteDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/desativar/{id}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ClienteDTO> desativarCliente(@PathVariable Integer id) {
        ClienteDTO clienteDTO = clienteService.desativarCliente(id);

        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/valida", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ValidaClienteResponseDTO> validaCliente(@RequestBody ClienteDTO clienteDTO) {
        return new ResponseEntity<>(clienteService.valideCliente(clienteDTO), HttpStatus.OK);
    }

    @PostMapping(value = "/anonymize/{id}")
    public void anonymize(@PathVariable Integer id) {
        clienteService.anonymize(id);
    }
}