package Navios;

import Tabuleiro.*;

/**
* Classe que representa um cruzador.
* @author Jonatan
*/
public class Cruzado extends Navio {

  /**
  * Construtor do cruzador.
  * Inicializa o cruzador com 2 canhões, 3 turnos de cooldown (2 turnos sem atacar),
  * o caractere 'C' no mapa um tamanho de 3 células e o nome "Cruzador".
  */


  public Cruzado() {
    super(3, "Cruzador", 'C', false, 3, 2);
  }

  /**
  * Função para o jogador atirar com o Dreadnought. Inicializa as arrays e chama a função principal com o parâmetro manual como true.
  *
  * @param tabuleiroAtaque tabuleiro do jogador que está atacando
  * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
  * @throws StringIndexOutOfBoundsException indica que a coordenada informada está fora do intervalo esperado.
  */

  @Override
  public void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa) {
    int x[], y[];
    x = new int[this.getQtdCanhoes()];
    y = new int[this.getQtdCanhoes()];
    this.atirar(tabuleiroAtaque, tabuleiroDefesa, x, y, true);
  }

  /**
  * Função para o bot atirar com o Dreadnought. Chama a função principal com o parâmetro manual como false.
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
  * Função principal para atirar com o Dreadnought.
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
    boolean tiroExtra = false; // se o cruzador tem um tiro extra

    System.out.println("Cruzador atirando!\n");
    if(manual) { // se o jogador está jogando manualmente, pede para ele informar as coordenadas dos tiros
      System.out.println("Tabuleiro inimigo: ");
      tabuleiroAtaque.mostrar(true);
      for(int i = 0; i < this.getQtdCanhoes(); i++) {
        mirarAtaque(x, y, i, "canhão " + (i + 1));
      }
    }
    else { // se o jogador não está jogando manualmente, confere as coordenadas dos tiros
      for(int i = 0; i < this.getQtdCanhoes(); i++) {
        try {
          Tabuleiro.conferirCoordenada(x[i], y[i]);
        } catch(CoordenadaForaDoTabuleiroException e) { // se alguma coordenada estiver fora do tabuleiro, lança uma exceção
          System.out.println("Erro: " + e);
        }
      }
    }

    for(int i = 0; i < this.getQtdCanhoes(); i++) { // atira nas coordenadas informadas e verifica se o cruzador tem um tiro extra
      tiroExtra = tabuleiroAtaque.afundarNavio(this, x[i], y[i]) || tiroExtra;
    }

    if(tiroExtra) { // se o cruzador tem um tiro extra (se acertou um navio leve), pede para o jogador informar a coordenada do tiro extra
      System.out.println("Tiro extra!");
      if(manual) { // se o jogador está jogando manualmente, pede para ele informar a coordenada do tiro extra
        mirarAtaque(x, y, 0, "tiro extra");

        tabuleiroAtaque.afundarNavio(this, x[0], y[0]);
      }
      else { // se o jogador não está jogando manualmente, confere a coordenada do tiro extra e atira
        try {
          Tabuleiro.conferirCoordenada(x[this.getQtdCanhoes()], y[this.getQtdCanhoes()]);
        } catch(CoordenadaForaDoTabuleiroException e) { // se a coordenada estiver fora do tabuleiro, lança uma exceção
          System.out.println("Erro: " + e);
        }
        System.out.println("Bot atirou em: atirou em (" + (char)('A' + x[this.getQtdCanhoes()]) + "," + (y[this.getQtdCanhoes()]) + ")"); // mostra a coordenada do tiro extra
        tabuleiroAtaque.afundarNavio(this, x[this.getQtdCanhoes()], y[this.getQtdCanhoes()]);
      }
    }
    this.iniciarCooldown(); // inicia o cooldown do cruzador
  }
}