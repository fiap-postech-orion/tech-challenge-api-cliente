package br.com.postech.software.architecture.techchallenge.cliente.consumer;

import br.com.postech.software.architecture.techchallenge.cliente.dto.PedidoDTO;
import br.com.postech.software.architecture.techchallenge.cliente.dto.ProdutoDTO;
import br.com.postech.software.architecture.techchallenge.cliente.dto.ValidaProdutoRequestDTO;
import br.com.postech.software.architecture.techchallenge.cliente.producer.RabbitMQProducer;
import br.com.postech.software.architecture.techchallenge.cliente.service.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ValidaClienteConsumer {

    private ClienteService clienteService;
    private RabbitMQProducer rabbitMQProducer;

    @RabbitListener (queues = {"${valida.cliente.queue}"})
    public void consume(PedidoDTO pedidoDTO) {

        clienteService.valideCliente(pedidoDTO.getCliente());

        List<ProdutoDTO> produtos = pedidoDTO.getProdutos().stream()
                .map(pedidoProduto -> pedidoProduto.getProduto()).collect(Collectors.toList());
        rabbitMQProducer.sendToValidaProdutosQueue(new ValidaProdutoRequestDTO(produtos));
    }
}
