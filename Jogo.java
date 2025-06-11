import Jogador.*;
import Tabuleiro.*;
import java.util.Scanner;

/**
 * responsavel por gerenciar a interacao entre os jogadores e o tabuleiro,
 * garantindo que as regras do jogo sejam seguidas e que o jogo seja jogado corretamente.
 * a classe Jogo é a principal classe em termos de implementacao, e é responsável por iniciar o jogo,
 * gerenciar os turnos e verificar o fim do jogo (e o vencedor dele).
 * @author Jessica
 */

public class Jogo {
    private Jogador jogador1;
    private Jogador jogador2;
    private boolean contraBot;
    private Scanner sc;

    // precisamos ter uma referencia aos tabuleiros para verificar o termino do jogo
    private Tabuleiro tabuleiroDefesaJogador1;
    private Tabuleiro tabuleiroDefesaJogador2;
/**
 * Construtor da classe Jogo.
 * Inicializa o scanner para leitura de entradas do usuário.
 */
    public Jogo() {
        this.sc = new Scanner(System.in); 
    }

    /**
     * método que inicia o jogo, solicitando os nomes dos jogadores e o modo de jogo.
     * O modo de jogo pode ser Jogador vs Bot ou Jogador vs Jogador.
     * Dependendo do modo, o jogo cria os jogadores e seus respectivos tabuleiros de defesa.
     * Em seguida, posiciona os navios de ambos os jogadores.
     */
    public void iniciar() {
        System.out.println("Modo de jogo:");
        System.out.println("1 - Jogador vs Bot");
        System.out.println("2 - Jogador vs Jogador");
        int modo = sc.nextInt();
        sc.nextLine();

        contraBot = modo == 1;

        System.out.print("Nome do Jogador 1: ");
        String nome1 = sc.nextLine();

        // tabuleiros de defesa aqui, antes de criar os jogadores
        this.tabuleiroDefesaJogador1 = new Tabuleiro(); // tabuleiro do jogador 1
        this.tabuleiroDefesaJogador2 = new Tabuleiro(); // tabuleiro do bot ou jogador 2

        // passagem dos tabuleiros para os construtores dos jogadores
        jogador1 = new Jogador(nome1, this.tabuleiroDefesaJogador1, this.tabuleiroDefesaJogador2);

        if (contraBot) {
            //inicia jogo contra bot
            jogador2 = new Bot("Bot Inimigo", this.tabuleiroDefesaJogador2, this.tabuleiroDefesaJogador1);
        } else {
            System.out.print("Nome do Jogador 2: ");
            String nome2 = sc.nextLine();
            jogador2 = new Jogador(nome2, this.tabuleiroDefesaJogador2, this.tabuleiroDefesaJogador1);
        }
        // posicionamento dos navios do jogador e bot/player2
        jogador1.posicionarNavios();
        jogador2.posicionarNavios();
    }
/**
 * método que gerencia o turno de cada jogador.
 * Ele chama o método fazerJogada de cada jogador, permitindo que eles ataquem um ao outro.
 * Após cada jogada, o método verifica se algum dos jogadores perdeu todos os seus navios.
 * Se um jogador perder, o jogo termina e o vencedor é anunciado.
 * Se ambos os jogadores ainda tiverem navios, o turno continua até que um dos jogadores perca.
 */
    public void jogarTurno() {
        System.out.println("\n--- Turno de " + jogador1.getName() + " ---");
        jogador1.fazerJogada(jogador2); // p1 joga contra o p2

        // cooldown dos navios do Jogador1
        jogador1.getTabuleiroDefesa().cooldownNavios();

        if (verificarFimDeJogo()) {
            return; //verifica se o p2 perdeu
        }

        System.out.println("\n--- Turno de " + jogador2.getName() + " ---");
        jogador2.fazerJogada(jogador1); // p2 joga contra o p1

        // cooldown dos navios do Jogador2
        jogador2.getTabuleiroDefesa().cooldownNavios();
        if(verificarFimDeJogo()){
            return; //verifica se o p1 perdeu
        }
    }
/**
 * método que verifica se o jogo chegou ao fim.
 * @return true se pelo menos um dos jogadores perdeu todos os seus navios, false caso contrário.
 */
    public boolean verificarFimDeJogo() {
        return tabuleiroDefesaJogador1.todosNaviosAfundados() || tabuleiroDefesaJogador2.todosNaviosAfundados();
    }

    /**
     * método que verifica se algum dos jogadores venceu o jogo.
     * Ele verifica se todos os navios de um dos jogadores foram afundados.
     * Se ambos os jogadores tiverem todos os navios afundados, é um empate. situação impossivel de ocorrer porem está aqui para
     *  garantir que o jogo termine corretamente.
     * Se apenas um jogador tiver todos os navios afundados, o outro jogador é declarado vencedor.
     */
    public void verificarVencedor() {
        if (tabuleiroDefesaJogador1.todosNaviosAfundados() && tabuleiroDefesaJogador2.todosNaviosAfundados()) {
            System.out.println("Empate!");
        } else if (tabuleiroDefesaJogador1.todosNaviosAfundados()) {
            System.out.println(jogador2.getName() + " venceu!");
        } else if (tabuleiroDefesaJogador2.todosNaviosAfundados()) {
            System.out.println(jogador1.getName() + " venceu!");
        }
    }
}