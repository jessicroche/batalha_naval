package Navios;

import java.util.*;
import Tabuleiro.*;
import Jogador.*;

/**
* Classe abstrata que representa um navio.
* @author Jonatan
*
*/

public abstract class Navio {
    private int tamanho; // tamanho do navio
    private int turnosRestantesCooldown = 0;    // turnos restantes para o navio poder atirar novamente

    private final String tipoNavio; // nome do tipo do navio
    private final boolean navioLeve; // se o navio é leve ou não (feito para os canhões e imunidade do Dreadnought)
    private final char caractereMapa; // caractere maiusculo que representa o navio no mapa
    private final int turnosCooldown; // turnos de cooldown depois do navio atirar
    private final int qtdCanhoes; // quantidade de canhões do navio

    protected static Scanner scanner = new Scanner(System.in); // scanner para ler as entradas do usuário
    private boolean estaNaSuperficie = false; // se o navio está na superfície ou não (feito para o submarino)

    /**
    * Construtor do navio
    * Tudo é informado pelos construtores das classes filhas
    *
    * @param tamanho tamanho do navio
    * @param tipoNavio nome do tipo do navio
    * @param navioLeve se o navio é leve ou não
    * @param caractereMapa caractere maiusculo que representa o navio no mapa
    * @param turnosCooldown turnos de cooldown depois do navio atirar
    * @param qtdCanhoes quantidade de canhões do navio
    */
    public Navio(int tamanho, String tipoNavio, char caractereMapa, boolean navioLeve, int turnosCooldown, int qtdCanhoes) { 
        this.tamanho = tamanho;
        this.tipoNavio = tipoNavio;
        this.navioLeve = navioLeve;
        this.caractereMapa = caractereMapa;
        this.turnosCooldown = turnosCooldown;
        this.qtdCanhoes = qtdCanhoes;
    }

    /**
    * Verifica se o navio foi afundado
    *
    * @return true se o navio foi afundado, false caso contrário
    */
    public boolean foiAfundado() {
        // navio atingido e afundado
        if(tamanho == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
    * Afunda uma parte do navio, informando seu tipo e se ele foi afundado completamente
    @param tabuleiro tabuleiro onde o navio está posicionado
    */
    public void afundar(Tabuleiro tabuleiro) {
        System.out.println("Acertou um " + this.tipoNavio + "!"); 
        tamanho--;
        if(this.tamanho == 0) { // se o navio foi afundado completamente
            System.out.println(this.tipoNavio + " afundado!!!"); 
            tabuleiro.removerNavio(); // remove o navio do tabuleiro
        }
    }

    /**
    * Posiciona o navio no tabuleiro
    *
    * @param tabuleiro tabuleiro onde o navio será posicionado
    * @param x coordenada x do navio no tabuleiro
    * @param y coordenada y do navio no tabuleiro
    * @param direcao direção do navio no tabuleiro (1 = cima, 2 = baixo, 3 = esquerda, 4 = direita)
    * @return true se o navio foi posicionado com sucesso, false caso contrário
    * @throws OpcaoInvalidaException indica que a opção informada não é o padrão esperado pela aplicação.
    */
    public boolean posicionar(Tabuleiro tabuleiro, int x, int y, int direcao) {
        try {
            Tabuleiro.conferirCoordenada(x, y); // verifica se a coordenada está dentro do tabuleiro
            if(tamanho > 1) {
                if(direcao == 0) { // se a direção não for informada, pede para o usuário informar
                    System.out.println("Tamanho navio: " + this.tamanho);
                    System.out.printf("Para que lado " + this.tipoNavio + " deve apontar? (Cima = 1, Baixo = 2, Esquerda = 3, Direita = 4): ");
                    direcao = Integer.parseInt(scanner.nextLine()); 
                }
                boolean ocupado = false; // verifica se as posições estão ocupadas
                switch(direcao) {
                    case 1: // cima
                        for(int i = x; i > x - this.tamanho; i--) { // verifica se as posições são válidas e se estão ocupadas
                            Tabuleiro.conferirCoordenada(i, y);
                            ocupado = ocupado || tabuleiro.conferirOcupacao(i, y);
                        }
                        if(!ocupado) {
                            for(int i = x; i > x - this.tamanho; i--) // posiciona o navio nas posições informadas
                                tabuleiro.setCelula(i, y, this); 
                        }
                        else
                            return false;
                        break;

                    case 2: // baixo
                        for(int i = x; i < x + this.tamanho; i++) { // verifica se as posições são válidas e se estão ocupadas
                            Tabuleiro.conferirCoordenada(i, y);
                            ocupado = ocupado || tabuleiro.conferirOcupacao(i, y);
                        }
                        if(!ocupado) {
                            for(int i = x; i < x + this.tamanho; i++) // posiciona o navio nas posições informadas
                                tabuleiro.setCelula(i, y, this);
                        }
                        else
                            return false;
                        break;
                        
                    case 3: // esquerda
                        for(int i = y; i > y - this.tamanho; i--) { // verifica se as posições são válidas e se estão ocupadas
                            Tabuleiro.conferirCoordenada(x, i);
                            ocupado = ocupado || tabuleiro.conferirOcupacao(x, i);
                        }
                        if(!ocupado) {
                            for(int i = y; i > y - this.tamanho; i--) // posiciona o navio nas posições informadas
                                tabuleiro.setCelula(x, i, this);
                        }
                        else
                            return false;  
                        break;

                    case 4: // direita
                        for(int i = y; i < y + this.tamanho; i++) { // verifica se as posições são válidas e se estão ocupadas
                            Tabuleiro.conferirCoordenada(x, i);
                            ocupado = ocupado || tabuleiro.conferirOcupacao(x, i);
                        }
                        if(!ocupado) {
                            for(int i = y; i < y + this.tamanho; i++) // posiciona o navio nas posições informadas
                                tabuleiro.setCelula(x, i, this);
                        }
                        else
                            return false;
                        break;
                    default:
                        throw new OpcaoInvalidaException("Opção inválida!"); // se a direção for inválida, lança uma exceção
                }
            }
            else { // se o navio tiver tamanho 1, posiciona na coordenada informada
                if(!tabuleiro.conferirOcupacao(x, y) || tabuleiro.getCelula(x, y) == this) // se a posição estiver ocupada, retorna false
                    tabuleiro.setCelula(x, y, this); // posiciona o navio na coordenada informada
                else
                    return false;
            }
            System.out.println(this.tipoNavio + " posicionado com sucesso!");
            tabuleiro.adicionarNavio(); // adiciona o navio no tabuleiro
            return true; // se o navio foi posicionado com sucesso, retorna true
        } catch(CoordenadaForaDoTabuleiroException e) { // se a coordenada estiver fora do tabuleiro, lança uma exceção e retorna false, já que o navio não foi posicionado
            System.out.println("Erro: " + e);
            return false;
        } catch(OpcaoInvalidaException e) { // se a opção for inválida, lança uma exceção e retorna false, já que o navio não foi posicionado
            System.out.println("Erro: " + e);
            return false;
        }
    }

    /**
    * Função para o jogador informar a coordenada para atirar
    *
    * @param x array de coordenadas x
    * @param y array de coordenadas y
    * @param indice índice do array
    * @param arma nome da arma que está atirando
    * @throws StringIndexOutOfBoundsException indica que a coordenada informada está fora do intervalo esperado.
    */
    public void mirarAtaque(int[] x, int[] y, int indice, String arma) {
        while(true) {
            System.out.print("\nDigite a coordenada para atirar " + arma + "(ex: A1): ");
            String posicao;
            posicao = scanner.nextLine().toUpperCase();

            try {
                x[indice] = posicao.charAt(0) - 'A'; // A=0, B=1...
                y[indice] = posicao.charAt(1) - '0'; // converte char para int
                
                Tabuleiro.conferirCoordenada(x[indice], y[indice]); // verifica se a coordenada está dentro do tabuleiro
                break; 
            } catch (CoordenadaForaDoTabuleiroException e) { // se a coordenada estiver fora do tabuleiro, lança uma exceção e pede para o usuário informar novamente
                System.out.println("Erro: " + e);
                continue;
            } catch (StringIndexOutOfBoundsException e) { // se a coordenada digitada for inválida, lança uma exceção e pede para o usuário informar novamente
                System.out.println("Coordenada inválida!");
                continue;    
            }
        }
    }

    /**
    * função para atirar com o bot.
    *
    * @param tabuleiroAtaque tabuleiro do jogador que está atacando
    * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
    * @param x array de coordenadas x
    * @param y array de coordenadas y
    */
    public abstract void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa, int[] x, int[] y); //função para o bot

    /**
    * 
    * Função para atirar com o jogador. Chama a função principal com o parâmetro x e y como null.
    * @param tabuleiroAtaque tabuleiro do jogador que está atacando
    * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
    */
    
    public abstract void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa); //função para o jogador


    /**
    * Função para atirar com o jogador manualmente.
    *
    * @param tabuleiroAtaque tabuleiro do jogador que está atacando
    * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
    * @param x array de coordenadas x
    * @param y array de coordenadas y
    * @param manual se true, o jogador está jogando manualmente
    */

    abstract void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa, int[] x, int[] y, boolean manual); //função onde vai ser implementado a lógica de atirar

    
    /**
    * Função para iniciar o cooldown do navio
    */
    public void iniciarCooldown() {
        this.turnosRestantesCooldown = this.turnosCooldown;
    }

    /**
    * Função para decrementar o cooldown do navio
    */
    public void decrementarCooldown() {
        if(this.turnosRestantesCooldown != 0)
            this.turnosRestantesCooldown--;
    }

    /**
    * Função para verificar se o navio pode atirar
    *
    * @return true se o navio pode atirar, false caso contrário
    */
    public boolean podeAtirar() {
        return this.turnosRestantesCooldown == 0;
    }

    /**
    * Função para fechar o scanner
    */
    public static void fecharScanner() {
        scanner.close();
    }

    /**
     * Função para obter o tamanho do navio.
     * @return o tamanho do navio
     */
    public int getTamanho() {
        return this.tamanho;
    }

    /**
     * Função para obter o tipo do navio.
     * @return o tipo do navio
     */
    public String getTipoNavio() {
        return this.tipoNavio;
    }

    /**
     * Função para obter o caractere que representa o navio no mapa.
     * @return o caractere que representa o navio no mapa
     */
    public char getCaractereMapa() {
        return this.caractereMapa;   
    }

    /**
     * Função para obter se o navio é leve ou não.
     * @return true se o navio é leve, false caso contrário
     */
    public boolean getNavioLeve() {
        return this.navioLeve;
    }

    /**
     * Função para obter os turnos restantes de cooldown do navio.
     * @return o número de turnos restantes de cooldown
     */
    public int getTurnosRestantesCooldown() {
        return this.turnosRestantesCooldown;
    }

    /**
     * Função para obter se o navio está na superfície ou não.
     * @return true se o navio está na superfície, false caso contrário
     */
    public boolean getEstaNaSuperficie() {
        return this.estaNaSuperficie;
    }

    /**
     * Função para definir se o navio está na superfície ou não.
     * @param estaNaSuperficie indica se o navio está na superfície ou não
     */
    public void setEstaNaSuperficie(boolean estaNaSuperficie) {
        this.estaNaSuperficie = estaNaSuperficie;
    }

    /**
     * Função para obter a quantidade de canhões do navio.
     * @return a quantidade de canhões do navio
     */
    public int getQtdCanhoes() {
        return this.qtdCanhoes;
    }
}