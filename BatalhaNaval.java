import Jogador.Jogador;
import Navios.Navio;

/** 
 * classe principal do jogo batalha naval.
 * inicia e termina o jogo.
 * @author Jessica
 */

public class BatalhaNaval {
    /**
     * método main que inicia o jogo de batalha naval.
     * @param args argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        Jogo batalha = new Jogo();
        batalha.iniciar();
        while (!batalha.verificarFimDeJogo()) {
            batalha.jogarTurno();
        }
        batalha.verificarVencedor();
        Navio.fecharScanner();
        Jogador.fecharScanner();
    }
}
