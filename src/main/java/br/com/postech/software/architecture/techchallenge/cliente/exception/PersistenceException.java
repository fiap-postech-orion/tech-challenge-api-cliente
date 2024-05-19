package br.com.postech.software.architecture.techchallenge.cliente.exception;

public class PersistenceException extends ApplicationException {
	private static final long serialVersionUID = 1L;

    public PersistenceException(String mensagem) {
        super(mensagem);
    }

    public PersistenceException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }

}
