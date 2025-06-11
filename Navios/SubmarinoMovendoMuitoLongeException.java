package Navios;
/**
 * Exceção lançada quando um submarino tenta se mover muito longe (mais de 3 células).
 * Esta exceção é usada para garantir que o submarino não se mova além de seu alcance permitido
 * conforme as regras descritas no manual do jogo.
 * @author Jonatan
 */
public class SubmarinoMovendoMuitoLongeException extends Exception {
  /**
   * Construtor da exceção SubmarinoMovendoMuitoLongeException.
   * Esta exceção é lançada quando um submarino tenta se mover mais de 3 células.
   * @param mensagem mensagem de erro a ser exibida
   */
  public SubmarinoMovendoMuitoLongeException(String mensagem) {
    super(mensagem);
  }
}