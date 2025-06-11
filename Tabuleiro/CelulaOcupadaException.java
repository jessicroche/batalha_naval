package Tabuleiro;

/**
* Exceção lançada quando uma célula do tabuleiro já está ocupada por um navio.
* @author Jonatan
*
*/
public class CelulaOcupadaException extends RuntimeException {
    /**
     * Construtor da exceção CelulaOcupadaException.
     * Esta exceção é lançada quando uma célula informada já está ocupada por um navio.
     * @param mensagem mensagem de erro a ser exibida
     */
    public CelulaOcupadaException(String mensagem) {
        super(mensagem);
    }
}