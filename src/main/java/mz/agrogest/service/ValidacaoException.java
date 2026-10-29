package mz.agrogest.service;

/** Erro de regra de negocio, com mensagem pronta a mostrar ao utilizador. */
public class ValidacaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
