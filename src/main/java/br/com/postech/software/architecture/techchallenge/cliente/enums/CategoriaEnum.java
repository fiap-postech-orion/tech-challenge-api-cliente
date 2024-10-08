package br.com.postech.software.architecture.techchallenge.cliente.enums;

public enum CategoriaEnum {
    DESCONHECIDO(0, "Desconhecida"),
    LANCHE(1, "Lanche"),
    ACOMPANHAMENTO(2, "Acompanhamento"),
    BEBIDA(3, "Bebida"),
    SOBREMESA(4, "Sobremesa");

    private Integer value;
    private String descricao;

    private CategoriaEnum(Integer value, String descricao) {
        this.value = value;
        this.descricao = descricao;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescricao() {
        return descricao;
    }

    public static CategoriaEnum get(Integer value) {
        for (CategoriaEnum categoria : CategoriaEnum.values()) {
            if(categoria.getValue() == value ) {
                return categoria;
            }
        }

        return CategoriaEnum.DESCONHECIDO;
    }
}