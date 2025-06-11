package Jogador;
/** 
 * Exceção personalizada para indicar que uma opção inválida foi selecionada.
 * Esta exceção é lançada quando o usuário escolhe uma opção que não está disponível
 * ou que não é reconhecida pelo sistema.
 * @author Jonatan
 */
public class OpcaoInvalidaException extends RuntimeException {
    public OpcaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}