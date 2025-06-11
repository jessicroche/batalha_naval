package Navios;

import Tabuleiro.*;

/**
* Classe que representa um destroyer.
* @author Jonatan
*
*/
public class Destroyer extends Navio {
  /**
  * Construtor do destroyer.
  * Inicializa o destroyer com 1 canhão, 2 turnos de cooldown (1 turno sem atacar),
  * o caractere 'T' no mapa um tamanho de 2 células e o nome (em português) "ContraTorpedeiro".
  */
  public Destroyer() {
    super(2, "ContraTorpedeiro", 'T', true, 2, 1);
  }

  /**
  * Função para o jogador atirar com o destroyer. Inicializa as arrays e chama a função principal com o parâmetro manual como true.
  *
  * @param tabuleiroAtaque tabuleiro do jogador que está atacando
  * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
  *
  * @throws StringIndexOutOfBoundsException indica que a coordenada informada está fora do intervalo esperado.
  */
  @Override
  public void atirar(Tabuleiro tabuleiroAtaque, Tabuleiro tabuleiroDefesa) {
    int x[], y[];
    x = new int[2];
    y = new int[2];
    this.atirar(tabuleiroAtaque, tabuleiroDefesa, x, y, true);
  }

  /**
  * Função para o bot atirar com o destroyer. Chama a função principal com o parâmetro manual como false.
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
  * Função principal para atirar com o destroyer.
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
    System.out.println("ContraTorpedeiro atirando!\n");
    if(manual) { // se o jogador está jogando manualmente, pede para ele informar as coordenadas dos tiros
      System.out.println("Tabuleiro inimigo: ");
      tabuleiroAtaque.mostrar(true);
      mirarAtaque(x, y, 0, "canhão"); // canhão

      do { // bomba de profundidade
        mirarAtaque(x, y, 1, "bomba de profundidade");
        try {
          for (int i = x[1]; i <= x[1] + 1; i++) { // conferindo se toda a bomba de profundidade está dentro do tabuleiro
            for (int j = y[1]; j <= y[1] + 1; j++) {
              Tabuleiro.conferirCoordenada(i, j);
            }
          }
          break;
        } catch(CoordenadaForaDoTabuleiroException e) { // se a bomba de profundidade estiver fora do tabuleiro, pede para o jogador informar novamente
          System.out.println("Erro: " + e);
          continue;
        }
      } while(true);
    }
    else { // se o jogador não está jogando manualmente, confere as coordenadas dos tiros
      try {
        Tabuleiro.conferirCoordenada(x[0], y[0]); // canhão
        for (int i = x[1]; i <= x[1] + 1; i++) { // conferindo se toda a bomba de profundidade está dentro do tabuleiro
          for (int j = y[1]; j <= y[1] + 1; j++) {
            Tabuleiro.conferirCoordenada(i, j);
          }
        }
      } catch(CoordenadaForaDoTabuleiroException e) { // se a bomba de profundidade ou o canhão estiver fora do tabuleiro, pede para o jogador informar novamente
        System.out.println("Erro: " + e);
      }
    }

    tabuleiroAtaque.afundarNavio(this, x[0], y[0]); // atira com o canhão

    boolean acertouBomba = false;

    for (int i = x[1]; i <= x[1] + 1; i++) { // atira com a bomba de profundidade
      for (int j = y[1]; j <= y[1] + 1; j++) {
        acertouBomba = tabuleiroAtaque.afundarNavio(this, i, j, true) || acertouBomba;
      }
    }
    if(acertouBomba) { // se a bomba de profundidade acertou um submarino, mostra a mensagem
      System.out.println("Bomba de profundidade acertou um submarino!");
    } 
    else { // se a bomba de profundidade não acertou nada, mostra a mensagem
      System.out.println("Bomba de profundidade não acertou nada.");
    }
    this.iniciarCooldown(); // inicia o cooldown do destroyer
  }
}