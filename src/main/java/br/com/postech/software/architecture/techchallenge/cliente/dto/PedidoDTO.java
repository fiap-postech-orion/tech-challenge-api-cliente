package br.com.postech.software.architecture.techchallenge.cliente.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class PedidoDTO {

	private Long numeroPedido;
    private ClienteDTO cliente;
    private String dataPedido;
    private Integer statusPedido;
    private String qrCode;
    @NotEmpty
    private List<PedidoProdutoDTO> produtos;
}
