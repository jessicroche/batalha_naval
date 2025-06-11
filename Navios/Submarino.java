package Navios;

import Jogador.OpcaoInvalidaException;
import Tabuleiro.*;

/**
* Classe que representa um submarino.
* @author Jonatan
*
*/
public class Submarino extends Navio {
  private int xAtual; // coordenada x atual do submarino
  private int yAtual; // coordenada y atual do submarino
  private final int MOVIMENTO = 3; // movimento máximo do submarino

  /**
  * Construtor do submarino.
  * Inicializa o submarino com 1 torpedo, 2 turnos de cooldown (1 turno sem atacar),
  * o caractere 'S' no mapa um tamanho de 1 célula e o nome "Submarino".
  */
   public Submarino() {
    super(1, "Submarino", 'S', false, 2, 1);
  }

  /**
  * Sobrescrita do método para posicionar o submarino no tabuleiro, salvando a coordenada do submarino.
  *
  * @param tabuleiro tabuleiro onde o submarino será posicionado
  * @param x coordenada x do submarino no tabuleiro
  * @param y coordenada y do submarino no tabuleiro
  * @param direcao direção do submarino no tabuleiro (não é usada)
  * @return true se o submarino foi posicionado com sucesso, false caso contrário
  *
  * @throws OpcaoInvalidaException indica que a opção informada não é o padrão esperado pela aplicação.
  */
  @Override  
  public boolean posicionar(Tabuleiro tabuleiro, int x, int y, int direcao) {
    if (super.posicionar(tabuleiro, x, y, direcao)) {
      this.xAtual = x;
      this.yAtual = y;
      return true;
    }
    return false;
  }

  /**
  * Função para o jogador atirar com o submarino. Inicializa as arrays e chama a função principal com o parâmetro manual como true.
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
  * Função para o bot atirar com o submarino. Chama a função principal com o parâmetro manual como false.
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
  * Função principal para atirar com o submarino.
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
    System.out.println("Submarino atirando!\n");
    if(manual) { // se o jogador está jogando manualmente, pede para ele informar a coordenada do tiro
      System.out.println("Tabuleiro inimigo: ");
      tabuleiroAtaque.mostrar(true);
      mirarAtaque(x, y, 0, "torpedo");
    }
    else { // se o jogador não está jogando manualmente, confere a coordenada do tiro
      try {
        Tabuleiro.conferirCoordenada(x[0], y[0]);
      } catch(CoordenadaForaDoTabuleiroException e) {
        System.out.println("Erro: " + e);
      }
    }

    tabuleiroAtaque.afundarNavio(this, x[0], y[0]); // atira no navio

    if(manual) { // se o jogador está jogando manualmente, mostra o tabuleiro do jogador que está defendendo
      tabuleiroDefesa.mostrar(false);
    }
    tabuleiroDefesa.setSubmarinoAtacou(xAtual, yAtual); // marca a célula como atacada pelo submarino
    moverSubmarino(tabuleiroDefesa, x, y, manual); // move o submarino após o ataque

    if(!tabuleiroAtaque.temDestroyer()) { // se não tiver mais destroyer no tabuleiro, o submarino sobe para a superfície para que não seja imune aos canhões
      this.setEstaNaSuperficie(true);
      System.out.println("\nO submarino está na superfície!");
    }
    this.iniciarCooldown(); // inicia o cooldown do submarino
  }
  
  /**
  * Função para mover o submarino após o ataque.
  *
  * @param tabuleiroDefesa tabuleiro do jogador que está defendendo
  * @param x array de coordenadas x
  * @param y array de coordenadas y
  * @param manual se true, o jogador está jogando manualmente
  *
  * @throws CelulaOcupadaException indica que a célula onde o submarino está tentando se mover já está ocupada por outro navio.
  */
  void moverSubmarino(Tabuleiro tabuleiroDefesa, int[] x, int[] y, boolean manual) {
    if(manual) { // se o jogador está jogando manualmente, pede para ele informar a nova coordenada do submarino
      do {
        System.out.print("\nDigite uma coordenada para mover o submarino (ex: A1): ");
        String posicao = scanner.nextLine().toUpperCase();
        x[0] = posicao.charAt(0) - 'A'; // A=0, B=1...
        y[0] = posicao.charAt(1) - '0'; // converte char para int

        try{
          if(Math.abs(x[0] - xAtual) + Math.abs(y[0] - yAtual) > MOVIMENTO) { // se o submarino estiver se movendo muito longe, lança uma exceção
            throw new SubmarinoMovendoMuitoLongeException("Movimento muito longo!");
          }
        } catch(SubmarinoMovendoMuitoLongeException e) {
            System.out.println("Erro: " + e);
        }
      } while(!posicionar(tabuleiroDefesa, x[0], y[0], 0)); // se o submarino não puder ser posicionado na nova coordenada, pede para o jogador informar novamente
      tabuleiroDefesa.setCelula(xAtual, yAtual, null); // remove o submarino da posição antiga
      tabuleiroDefesa.removerNavio(); // a função posicionar adiciona um navio, então é necessário remover um navio para não contar dois submarinos
      // atualiza a posição atual do submarino
      this.xAtual = x[0];
      this.yAtual = y[0];
    }
    else { // se o jogador não está jogando manualmente,confere move o submarino para a coordenada informada
      try{
        if(Math.abs(x[1] - xAtual) + Math.abs(y[1] - yAtual) > MOVIMENTO) { // se o submarino estiver se movendo muito longe, lança uma exceção
          throw new SubmarinoMovendoMuitoLongeException("Movimento muito longo!");
        }
      } catch(SubmarinoMovendoMuitoLongeException e) {
          System.out.println("Erro: " + e);
      }
      if(!posicionar(tabuleiroDefesa, x[1], y[1], 0)) { // se o submarino não puder ser posicionado na nova coordenada, lança uma exceção
        throw new CelulaOcupadaException("Célula do submarino ocupada");
      }
      else {
        tabuleiroDefesa.setCelula(xAtual, yAtual, null); // remove o submarino da posição antiga
        tabuleiroDefesa.removerNavio(); // a função posicionar adiciona um navio, então é necessário remover um navio para não contar dois submarinos
        // atualiza a posição atual do submarino
        this.xAtual = x[1];
        this.yAtual = y[1];
      }
    }
  }

  /**
  * Sobrescrita do método para decrementar o cooldown do submarino.
  * Se o submarino estiver na superfície, ele mergulha.
  */
  @Override
  public void decrementarCooldown() {
    super.decrementarCooldown();
    if(this.getEstaNaSuperficie()) {
      this.setEstaNaSuperficie(false);
      System.out.println("\nO submarino mergulhou!");
    }
  }

  /**
   * Retorna a coordenada x atual do submarino.
   * @return xAtual, que é a coordenada x atual do submarino.
   */
  public int getXAtual() {
    return xAtual;
  }

  /**
   * Retorna a coordenada y atual do submarino.
   * @return yAtual, que é a coordenada y atual do submarino.
   */
  public int getYAtual() {
    return yAtual;
  }

  /**
   * Retorna o movimento máximo do submarino.
   * @return MOVIMENTO, que é o movimento máximo do submarino.
   */
  public int getMOVIMENTO() {
    return MOVIMENTO;
  }
}