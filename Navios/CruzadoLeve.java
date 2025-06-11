package Navios;

import Tabuleiro.*;

/**
* Classe que representa um cruzador leve. 
* @author Jonatan
*/
public class CruzadoLeve extends Navio {
  /**
  * Construtor do cruzador leve.
  * Inicializa o cruzador leve com 1 canhão, 1 turno de cooldown (0 turnos sem atacar),
  * o caractere 'L' no mapa um tamanho de 2 células e o nome "Cruzador Leve".
  */
  public CruzadoLeve() {
    super(2, "Cruzador Leve", 'L', true, 1, 1);
  }

  /**
  * Função para o jogador atirar com o cruzador leve. Inicializa as arrays e chama a função principal com o parâmetro manual como true.
  *
  * @param tabuleiroAtaque tabuleiro do jogador que está atacando
  * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
  *
  * @throws StringIndexOutOfBoundsException indica que a coordenada informada está fora do intervalo esperado.
  */


  @Override
  public void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa) {
    int x[], y[];
    x = new int[1];
    y = new int[1];
    this.atirar(tabuleiroAtaque, tabuleiroDefesa, x, y, true);
  }

  /**
  * Função para o bot atirar com o cruzador leve. Chama a função principal com o parâmetro manual como false.
  *
  * @param tabuleiroAtaque tabuleiro do jogador que está atacando
  * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
  * @param x array de coordenadas x
  * @param y array de coordenadas y
  *
  * @throws StringIndexOutOfBoundsException indica que a coordenada informada está fora do intervalo esperado.
  */
  @Override
  public void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa, int[] x, int[] y) {
    this.atirar(tabuleiroAtaque, tabuleiroDefesa, x, y, false);
  }

  /**
  * Função principal para atirar com o cruzador leve.
  *
  * @param tabuleiroAtaque tabuleiro do jogador que está atacando
  * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
  * @param x array de coordenadas x
  * @param y array de coordenadas y
  * @param manual se true, o jogador está jogando manualmente
  *
  * @throws StringIndexOutOfBoundsException indica que a coordenada informada está fora do intervalo esperado.
  */
  @Override
  public void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa, int[] x, int[] y, boolean manual) {
    boolean tiroExtra = false; // se o cruzador leve tem um tiro extra

    System.out.println("Cruzador Leve atirando!\n");
    if(manual) { // se o jogador está jogando manualmente, pede para ele informar a coordenada do tiro
      System.out.println("Tabuleiro inimigo: ");
      tabuleiroAtaque.mostrar(true);
      mirarAtaque(x, y, 0, "canhão");
    }
    else { // se o jogador não está jogando manualmente, confere a coordenada do tiro
      try {
        Tabuleiro.conferirCoordenada(x[0], y[0]);
      } catch(CoordenadaForaDoTabuleiroException e) { // se a coordenada estiver fora do tabuleiro, lança uma exceção
        System.out.println("Erro: " + e);
      }
    }

    tiroExtra = tabuleiroAtaque.afundarNavio(this, x[0], y[0]); // atira no navio e verifica se o cruzador leve tem um tiro extra

    if(tiroExtra) { // se o cruzador leve tem um tiro extra (se acertou um navio leve), pede para o jogador informar a coordenada do tiro extra
      System.out.println("Tiro extra!");
      if(manual) { // se o jogador está jogando manualmente, pede para ele informar a coordenada do tiro extra
        mirarAtaque(x, y, 0, "tiro extra");

        tabuleiroAtaque.afundarNavio(this, x[0], y[0]);
      }
      else { // se o jogador não está jogando manualmente, confere a coordenada do tiro extra e atira
        try {
          Tabuleiro.conferirCoordenada(x[1], y[1]);
        } catch(CoordenadaForaDoTabuleiroException e) { // se a coordenada estiver fora do tabuleiro, lança uma exceção
          System.out.println("Erro: " + e);
        }
        System.out.println("Bot atirou em: atirou em (" + (char)('A' + x[this.getQtdCanhoes()]) + "," + (y[this.getQtdCanhoes()]) + ")"); // mostra a coordenada do tiro extra
        tabuleiroAtaque.afundarNavio(this, x[1], y[1]);
      }
    }
    this.iniciarCooldown(); // inicia o cooldown do cruzador leve
  }
}