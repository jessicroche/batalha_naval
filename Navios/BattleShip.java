package Navios;

import Tabuleiro.*;

/**
* Classe que representa um battleship.
* @author Jonatan
*/
public class BattleShip extends Navio {
  /**
  * Construtor do battleship.
  * Inicializa o battleship com 3 canhões, 4 turnos de cooldown (3 turnos sem atacar),
  * o caractere 'G' no mapa um tamanho de 3 células e o nome "Navio de Guerra".
  */
  public BattleShip() {
    super(3, "Navio de Guerra", 'G', false, 4, 3);
  }

  /**
  * Função para o jogador atirar com o battleship. Inicializa as arrays e chama a função principal com o parâmetro manual como true.
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
  * Função para o bot atirar com o battleship. Chama a função principal com o parâmetro manual como false.
  *
  * @param tabuleiroAtaque tabuleiro do jogador que está atacando
  * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
  * @param x array de coordenadas x
  * @param y array de coordenadas y
  * @throws StringIndexOutOfBoundsException indica que a coordenada informada está fora do intervalo esperado.
  */
  @Override
  public void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa, int[] x, int[] y) {
    this.atirar(tabuleiroAtaque, tabuleiroDefesa, x, y, false);
  }


  /**
  * Função principal para atirar com o battleship.
  *
  * @param tabuleiroAtaque tabuleiro do jogador que está atacando
  * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
  * @param x array de coordenadas x
  * @param y array de coordenadas y
  * @param manual se true, o jogador está jogando manualmente
  * @throws StringIndexOutOfBoundsException indica que a coordenada informada está fora do intervalo esperado.
  */


  @Override
  public void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa, int[] x, int[] y, boolean manual) {
    boolean tiroExtra = false; // se o battleship tem um tiro extra

    System.out.println("Navio de Guerra atirando!\n");
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
    
    for(int i = 0; i < this.getQtdCanhoes(); i++) { // atira nas coordenadas informadas e verifica se o battleship tem um tiro extra
      tiroExtra = tabuleiroAtaque.afundarNavio(this, x[i], y[i]) || tiroExtra;
    }

    if(tiroExtra) { // se o battleship tem um tiro extra (se acertou um navio pesado), pede para o jogador informar a coordenada do tiro extra
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
    this.iniciarCooldown(); // inicia o cooldown do battleship
  }
}