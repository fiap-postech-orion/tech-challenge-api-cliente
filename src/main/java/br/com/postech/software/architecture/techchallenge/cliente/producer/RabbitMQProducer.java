package br.com.postech.software.architecture.techchallenge.cliente.producer;

import br.com.postech.software.architecture.techchallenge.cliente.dto.ErroResponseDTO;
import br.com.postech.software.architecture.techchallenge.cliente.dto.ValidaClienteResponseDTO;
import br.com.postech.software.architecture.techchallenge.cliente.dto.ValidaProdutoRequestDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Component
public class RabbitMQProducer {

    private static final String exchange = "api";
    private static final String validaProdutosRoutingKey = "validaProdutos";

    private static final String erroValidacaoRoutingKey = "erroValidacao";

    private RabbitTemplate template;

    public void sendToValidaProdutosQueue(ValidaProdutoRequestDTO validaProduto) {
        log.info("Message send: [{}]", validaProduto.toString());
        template.convertAndSend(exchange, validaProdutosRoutingKey, validaProduto);
    }

    public void sendToErroValidacaoQueue(ValidaClienteResponseDTO valida, Integer numeroPedido) {
        log.info("Message send: [{}]", valida.toString());
        template.convertAndSend(exchange, erroValidacaoRoutingKey,
                new ErroResponseDTO(numeroPedido, false, valida.getErrorMessage()));
    }
}
