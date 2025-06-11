package Navios;

import Tabuleiro.*;

/**
* Classe que representa um dreadnought.
* @author Jonatan
*/
public class Dreadnought extends Navio {
  /**
  * Construtor do dreadnought.
  * Inicializa o dreadnought com 3 canhões, 5 turnos de cooldown (4 turnos sem atacar),
  * o caractere 'D' no mapa um tamanho de 4 células e o nome "Dreadnought".
  */
  public Dreadnought() {
    super(4, "Dreadnought", 'D', false, 5, 3);
  }

  /**
  * Função para o jogador atirar com o Dreadnought. Inicializa as arrays e chama a função principal com o parâmetro manual como true.
  *
  * @param tabuleiroAtaque tabuleiro do jogador que está atacando
  * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
  *
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
    System.out.println("Dreadnought atirando!\n");
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
        } catch(CoordenadaForaDoTabuleiroException e) {
          System.out.println("Erro: " + e);
        }
      }
    }

    for(int i = 0; i < this.getQtdCanhoes(); i++) { // atira nas coordenadas informadas
      tabuleiroAtaque.afundarNavio(this, x[i], y[i]);
    }
    this.iniciarCooldown(); // inicia o cooldown do Dreadnought
  }
}