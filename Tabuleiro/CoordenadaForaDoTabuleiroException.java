package Tabuleiro;

/**
* Exceção lançada quando uma coordenada está fora do tabuleiro.
* @author Jonatan
*
*/
public class CoordenadaForaDoTabuleiroException extends Exception{
/**
 * Construtor da exceção CoordenadaForaDoTabuleiroException.
 * Esta exceção é lançada quando uma coordenada informada está fora dos limites TAMANHO do tabuleiro.
 * @param mensagem mensagem de erro a ser exibida
 */
  public CoordenadaForaDoTabuleiroException(String mensagem) {
    super(mensagem);
  }
}