package br.com.postech.software.architecture.techchallenge.cliente;

import br.com.postech.software.architecture.techchallenge.cliente.dto.ClienteDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClienteDTOTests {

    @Test
    void contextLoads() {
    }
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testEmailValido() {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setEmail("email@example.com");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty(), "Não houve violações de restrição");
    }

    @Test
    public void testEmailInvalido() {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setEmail("email_invalido.com");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        assertEquals(2, violations.size(), "Esperado uma violação de restrição");
    }

    @Test
    public void testCPFValido() {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setCpf("820.484.952-49");
        cliente.setSenha("123456789");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        assertTrue(violations.isEmpty(), "Esperado que não haja violações de restrição");
    }

    @Test
    public void testCPFInvalido() {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setCpf("12345678900");
        cliente.setSenha("123456789");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        assertEquals(1, violations.size(), "Esperado uma violação de restrição");
        assertEquals("CPF inválido, digite novamente", violations.iterator().next().getMessage());
    }

    @Test
    public void testSenhaValida() {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setSenha("senha123");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        assertTrue(violations.isEmpty(), "Esperado que não haja violações de restrição");
    }

    @Test
    public void testSenhaInvalida() {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setSenha("123");

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        assertEquals(1, violations.size(), "Esperado uma violação de restrição");
        assertEquals("tamanho deve ser entre 6 e 20", violations.iterator().next().getMessage());
    }

    @Test
    public void testSenhaNula() {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setSenha(null);

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        assertEquals(1, violations.size(), "Esperado uma violação de restrição");
        assertEquals("não deve ser nulo", violations.iterator().next().getMessage());
    }
}
