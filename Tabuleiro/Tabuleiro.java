package Tabuleiro; //preciso para importar nos navios, pq sem um package ele não reconhece o tipo e não dá pra importar sem o package aqui

import java.util.*;
import Navios.*;

/**
 * classe que representa o tabuleiro do jogo.
 * o tabuleiro foi implementado como uma matriz de celulas que podem ou nao conter navios, alem de
 * conter metodos para posicionar navios e para a lógica dos tiros de cada navio.
 * tamanho do tabuleiro é definido como uma constante TAMANHO.
 * @author Jessica
 */

public class Tabuleiro {
    private Celula[][] mapa;
    public static final int TAMANHO = 7;
    private int naviosRestantes;

    /**
     * Construtor do tabuleiro.
     * Inicializa o tabuleiro como uma matriz de células do tamanho definido por TAMANHO.
     * Cada célula é inicializada como uma nova instância da classe Celula.
     * A quantidade de navios restantes é inicializada como 0.
     */
    public Tabuleiro() {
        this.mapa = new Celula[TAMANHO][TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                this.mapa[i][j] = new Celula();
            }
        }
        this.naviosRestantes = 0;
    }

/**
 * metodo para conferir se uma coordenada está dentro do tabuleiro.
 * @param x coordenada x
 * @param y coordenada y
 * @throws CoordenadaForaDoTabuleiroException indica que a coordenada está fora do tabuleiro.
 */
    public static void conferirCoordenada(int x, int y) throws CoordenadaForaDoTabuleiroException {
        if (x < 0 || x >= TAMANHO || y < 0 || y >= TAMANHO)
            throw new CoordenadaForaDoTabuleiroException("Coordenada fora do tabuleiro!");
    }

    /**
    * confere se alguem perdeu o jogo
    * @return true se todos os navios foram afundados
    */
    public boolean todosNaviosAfundados() {
        return naviosRestantes == 0;
    }

/**
 * metodo para conferir se uma célula está ocupada por um navio.
 *
 * @param x coordenada x
 * @param y coordenada y
 * @return a disponibilidade da celula, true somente se esta ocupada
 */
    public boolean conferirOcupacao(int x, int y) {
        if (this.mapa[x][y].getNavio() != null)
            return true;
        else
            return false;
    }

/**
 * metodo responsavel por afundar um navio para navios com tiros.
 * @param atirador navio que está atirando
 * @param x coordenada x
 * @param y coordenada y
 * @return true se o navio tem um tiro a mais
*/

    public boolean afundarNavio(Navio atirador, int x, int y) {
        return afundarNavio(atirador, x, y, false);
    }


/**
 * metodo principal para afundar um navio.
 * @param atirador navio que está atirando
 * @param x coordenada x
 * @param y coordenada y
 * @param bombaProfundidade true se o navio tem bomba de profundidade
 * @return true se o navio tem mais de um tiro
 */


    public boolean afundarNavio(Navio atirador, int x, int y, Boolean bombaProfundidade) {
        Navio navio = this.getCelula(x, y);
        if (navio != null) {
            if (bombaProfundidade) { //logica para afundar submarinos submerso
                this.mapa[x][y].setBombaProfundidade(true);
                if (navio instanceof Submarino) {
                    navio.afundar(this);
                    return true;
                } else {
                    return false;
                }

            } else if (this.mapa[x][y].getFoiAtacado() && !(navio.getEstaNaSuperficie()) && !(this.mapa[x][y].getImunidade())) {
                System.out.println("Alvo repetido!");
                return false;

            } else { // se o navio não foi afundado ainda
                this.mapa[x][y].setFoiAtacado(true); // marca o ataque

                if (navio instanceof Submarino) { // logica para afundar submarino na superficie
                    if (navio.getEstaNaSuperficie()) {
                        navio.afundar(this);
                        return false;
                    } else {
                        System.out.println("Água...");
                        return false;
                    }

                } else if (navio.getNavioLeve()) {
                    navio.afundar(this);
                    if (atirador instanceof Cruzado || atirador instanceof CruzadoLeve) {
                        return true; // retorna que o cruzador tem um tiro a mais
                    }
                    return false;
                } else {
                    if (navio instanceof Dreadnought && atirador.getNavioLeve()) { // imunidade do dreadnought a canhoes leves
                        this.mapa[x][y].setImunidade(true);
                        System.out.println("Acertou um " + navio.getTipoNavio() + "!");
                        System.out.println("Navio imune a canhões leves.");
                        return false;
                    } else { // navio pesado nao imune a canhao leve afunda e verificamos se o atirador recebe um tiro extra
                        navio.afundar(this); 
                        if (atirador instanceof BattleShip) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        } else { // se a célula não tem um navio
            if (bombaProfundidade) { 
                this.mapa[x][y].setBombaProfundidade(true);
            } else {
                System.out.println("Água...");
                this.mapa[x][y].setFoiAtacado(true);
            }
            return false;
        }
    }


/**
 * metodo para mostrar o tabuleiro para o jogador / inimigo.
 * navios sao mostrados ou escondidos com base na logica de
 * descobrimento de posicoes ao longo do jogo
 * @param esconderNavios true se os navios devem ser escondidos ou nao
*/


    public void mostrar(boolean esconderNavios) {
        System.out.print("  0");
        for (int i = 1; i < TAMANHO; i++) { // mostra os números das colunas
            System.out.print(" ");
            System.out.print(i);
        }
        System.out.println();
        for (int i = 0; i < TAMANHO; i++) { // mostra as linhas
            System.out.print((char) ('A' + i) + " "); // mostra as letras das linhas
            for (int j = 0; j < TAMANHO; j++) { // mostra as células da linha
                System.out.print(this.mapa[i][j].mostrarCelula(esconderNavios));
            }
            System.out.println();
        }
    }


/**
 * metodo para verificar se o alvo esta validado para o bot atacar
 * @param x coordenada x
 * @param y coordenada y
 * @return true se o alvo é valido para ataque
 */

    public boolean alvoValidoBot(int x, int y) {
        //caso chegue no final do jogo e nao tenha celulas que nao foram atacadas, mas ainda tenha navios inimigos
        try {
            conferirCoordenada(x, y);
        } catch (CoordenadaForaDoTabuleiroException e) {
            System.out.println("Erro: " + e);
        }
        if(mapa[x][y].getFoiAtacado()) {
            if(mapa[x][y].getSubmarinoAtacou()) {
                return true;
            }
            Navio navio = mapa[x][y].getNavio();
            if(navio != null && !(navio.foiAfundado())) {
                return true;
            }
            return false; // se a célula tem um navio e ele foi afundado retorna false
        } else { // se a celula nao foi atacada retorna true
            return true;
        }
    }


/**
 * metodo para conferir se tem um destroyer no tabuleiro.
 * @return true se tem um destroyer nao afundado no jogo
 */

    public boolean temDestroyer() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                Navio navio = mapa[i][j].getNavio(); 
                if (navio instanceof Destroyer && !navio.foiAfundado()) {
                    return true; //possui destroyer nao afundado
                }
            }
        }
        return false;
    }

/**
 * metodo para aumentar a quantidade de navios restantes no tabuleiro.
 */
    public void adicionarNavio() {
        this.naviosRestantes++;
    }

/**
 * metodo para diminuir a quantidade de navios restantes no tabuleiro.
 */
    public void removerNavio() {
        this.naviosRestantes--;
    }

    /**
    * metodo para decrementar o cooldown de todos os navios no tabuleiro.
    */
    public void cooldownNavios() {
        List<Navio> navios = this.getNavios();
        for (Navio navio : navios) {
            navio.decrementarCooldown();
        }
    }

    /**
     * Método para verificar se um submarino atacou em uma célula específica do tabuleiro.
     * @param x coordenada x do tabuleiro
     * @param y coordenada y do tabuleiro
     */
    public void setSubmarinoAtacou(int x, int y) {
        this.mapa[x][y].setSubmarinoAtacou(true);
    }

    /**
     * Método para obter o navio em uma célula específica do tabuleiro.
     * @param x coordenada x do tabuleiro
     * @param y coordenada y do tabuleiro
     * @return o navio presente na célula especificada, ou null se não houver navio.
     */
    public Navio getCelula(int x, int y) {
        return this.mapa[x][y].getNavio();
    }

    /**
     * Método para definir um navio em uma célula específica do tabuleiro.
     * @param x coordenada x do tabuleiro
     * @param y coordenada y do tabuleiro
     * @param navio  navio a ser colocado na célula
     */
    public void setCelula(int x, int y, Navio navio) {
        this.mapa[x][y].setNavio(navio);
    }
    /**
     * Método para obter a lista de navios únicos no tabuleiro.
     * @return Lista de navios únicos presentes no tabuleiro.
     */
    public List<Navio> getNavios() {
        List<Navio> navios = new ArrayList<Navio>(); // Lista para armazenar os navios únicos
        for (int i = 0; i < TAMANHO; i++) { // Percorre o tabuleiro
            for (int j = 0; j < TAMANHO; j++) {
                Navio navio = this.mapa[i][j].getNavio();
                if (navio != null && !navios.contains(navio)) { // Verifica se o navio não é nulo e não está na lista
                    navios.add(navio); // Adiciona o navio à lista
                }
            }
        }
        return navios;
    }
}