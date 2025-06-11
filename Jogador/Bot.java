package Jogador;
import Tabuleiro.*;
import Navios.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *classe que representa um bot que joga o jogo de batalha naval contra um jogador humano.
* a funcionalidade do bot foi pensada de forma com que a logica de posicionamento e ataque 
* sejam baseadas em aleatoriedade, mas respeitando as regras do jogo.
* como a propria classe Navio possui scanner interno, nao importamos o scanner aqui.
* A classe estende a classe Jogador, que contém a lógica básica de um jogador no jogo.
* @author Jessica
*/


public class Bot extends Jogador {

    /**
     * Objeto Random utilizado para gerar números aleatórios.
     * util para posicionamento de navios e jogadas do bot, funciona
     * para as automações do modo PvE de forma geral.
     */
    private Random rand = new Random();

    /**
     * Construtor do Bot.
     * Inicializa o bot com um nome, um tabuleiro de defesa e um tabuleiro de ataque adversário.
     * @param name nome do bot, "Bot Inimigo" por padrão
     * @param meuTabuleiroDefesa tabuleiro de defesa do bot
     * @param tabuleiroAtaqueAdversario tabuleiro de ataque do adversário
     */
    public Bot(String name, Tabuleiro meuTabuleiroDefesa, Tabuleiro tabuleiroAtaqueAdversario) {
        super(name, meuTabuleiroDefesa, tabuleiroAtaqueAdversario);
    }


/**
 * verificacao se a coordenada e a bomba de profundidade estao dentro do tabuleiro.
 * @param x coordenada x do tabuleiro
 * @param y coordenada y do tabuleiro
 * @param bomba boolean que indica se a coordenada e uma bomba de profundidade ou nao
 * @return true se a coordenada e a bomba de profundidade estiverem dentro do tabuleiro, false caso contrario.
 */
    private static boolean conferirCoordenada(int x, int y, boolean bomba) {
        try {
            if (bomba) { 
                for (int i = x; i <= x + 1; i++) { 
                    for (int j = y; j <= y + 1; j++) {
                        Tabuleiro.conferirCoordenada(i, j);
                    }
                }
            }
            else {
                Tabuleiro.conferirCoordenada(x, y); //
            }
            return true;
        }
        catch (CoordenadaForaDoTabuleiroException e) {
            return false;
        }
    }


/**
 * seleciona um preset de navios para o bot posicionar no tabuleiro.
 */
    @Override
    public void posicionarNavios() {
        System.out.println("\n--- " + this.getName() + " está posicionando seus navios... ---");

        List<Navio> naviosParaPosicionar = new ArrayList<Navio>();

        int escolhaPresetBot = rand.nextInt(3) + 1; // random do bot, automação modo PvE
        naviosParaPosicionar = gerarPresetNavios(escolhaPresetBot); 

        for (Navio navio : naviosParaPosicionar) {
            boolean posicionado = false;
            while (!posicionado) {
                int x = rand.nextInt(Tabuleiro.TAMANHO);
                int y = rand.nextInt(Tabuleiro.TAMANHO);

                int direcao = 0; 
                if (navio.getTamanho() > 1) { 
                    direcao = rand.nextInt(4) + 1; 
                }
                posicionado = navio.posicionar(this.getTabuleiroDefesa(), x, y, direcao);

            }
            System.out.println(navio.getTipoNavio() + " do Bot posicionado.");
        }
        System.out.println("Todos os navios do " + this.getName() + " foram posicionados!");
    }


/**
 * metodo que faz o bot realizar uma jogada de forma automatizada
 * @param inimigo alvo para o bot atacar, que é o jogador humano.
 */


    @Override
    public void fazerJogada(Jogador inimigo) {
        System.out.println("\n--- Turno de " + this.getName() + " ---");

        Navio navioEscolhido = null;
        List<Navio> meusNavios = getTabuleiroDefesa().getNavios();
        for(Navio navio : meusNavios) {
            if (!navio.foiAfundado() && navio.podeAtirar()) {
                navioEscolhido = navio;
                break;
            }
        }

        if (navioEscolhido == null) {
            System.out.println(getName() + ": Nenhum de meus navios pode atirar neste turno ou todos afundados. Passando o turno.");
            return;
        }
        System.out.println(getName() + " irá usar o " + navioEscolhido.getTipoNavio() + ".");

        int xAlvo, yAlvo;
        do {
            xAlvo = rand.nextInt(Tabuleiro.TAMANHO);
            yAlvo = rand.nextInt(Tabuleiro.TAMANHO);
        } while (!getTabuleiroAtaque().alvoValidoBot(xAlvo, yAlvo) || !conferirCoordenada(xAlvo, yAlvo, false)); 

        int[] coordsX;
        int[] coordsY;

        if (navioEscolhido instanceof Submarino) {
            int moveX, moveY;
            Submarino navioTemp = (Submarino) navioEscolhido;
            while(true) {
                moveX = rand.nextInt(navioTemp.getMOVIMENTO() * 2);//fazer random com o tamanho do movimento do submarino e acrescentar no x e y atual
                moveY = rand.nextInt(navioTemp.getMOVIMENTO() * 2);
                moveX = navioTemp.getXAtual() + moveX - navioTemp.getMOVIMENTO(); 
                moveY = navioTemp.getXAtual() + moveY - navioTemp.getMOVIMENTO(); 
                if(!conferirCoordenada(moveX, moveY, false)) {
                    continue;
                }
                else if(getTabuleiroDefesa().conferirOcupacao(moveX, moveY)) {
                    if(!(getTabuleiroDefesa().getCelula(moveX, moveY) == navioEscolhido)) {
                        continue;
                    }
                }
                else if(Math.abs(moveX - navioTemp.getXAtual()) + Math.abs(moveY - navioTemp.getYAtual()) > navioTemp.getMOVIMENTO()) {
                    continue;
                }
                break;
            }
            coordsX = new int[]{xAlvo, moveX};
            coordsY = new int[]{yAlvo, moveY};
        } else if (navioEscolhido instanceof Destroyer) {
            int bombaX, bombaY;
            do {
                bombaX = rand.nextInt(Tabuleiro.TAMANHO);
                bombaY = rand.nextInt(Tabuleiro.TAMANHO);
            } while (!conferirCoordenada(bombaX, bombaY, true));
            coordsX = new int[]{xAlvo, bombaX};
            coordsY = new int[]{yAlvo, bombaY};
        } 
        else {

            int numCanhoes;
            try {
                numCanhoes = navioEscolhido.getQtdCanhoes(); 
            } catch (NoSuchMethodError e) { 
                System.err.println("Aviso: Navio.getQtdCanhoes() não encontrado. Usando turnosCooldown como proxy para quantidade de canhões.");
                numCanhoes = navioEscolhido.getTurnosRestantesCooldown(); 
            } catch (AbstractMethodError e) {
                System.err.println("Aviso: Navio.getQtdCanhoes() não encontrado. Usando turnosCooldown como proxy para quantidade de canhões.");
                numCanhoes = navioEscolhido.getTurnosRestantesCooldown();
            }

            coordsX = new int[numCanhoes + 1]; 
            coordsY = new int[numCanhoes + 1];

            coordsX[0] = xAlvo; 
            coordsY[0] = yAlvo;

            for (int i = 1; i < numCanhoes; i++) {
                int tempX, tempY;
                do {
                    tempX = rand.nextInt(Tabuleiro.TAMANHO);
                    tempY = rand.nextInt(Tabuleiro.TAMANHO);
                } while (!getTabuleiroAtaque().alvoValidoBot(tempX, tempY) || !conferirCoordenada(tempX, tempY, false)); 

                coordsX[i] = tempX;
                coordsY[i] = tempY;
            }
            if (numCanhoes < coordsX.length) { 
                int tempX, tempY;
                do {
                    tempX = rand.nextInt(Tabuleiro.TAMANHO);
                    tempY = rand.nextInt(Tabuleiro.TAMANHO);
                } while (!getTabuleiroAtaque().alvoValidoBot(tempX, tempY) || !conferirCoordenada(tempX, tempY, false));
                coordsX[numCanhoes] = tempX; 
                coordsY[numCanhoes] = tempY;
            }

        }

        navioEscolhido.atirar(getTabuleiroAtaque(), getTabuleiroDefesa(), coordsX, coordsY);

        System.out.println(getName() + " atirou em (" + (char)('A'+xAlvo) + "," + (yAlvo) + ") com " + navioEscolhido.getTipoNavio() + ".");
    }
}