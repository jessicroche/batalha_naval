package Tabuleiro;
import Navios.*;

/**
* Classe que representa uma célula do tabuleiro.
* Cada célula pode conter um navio, e pode ter sido atacada ou não.
* Nessa classe é cuidado do caractere que será mostrado no mapa.
* @author Jonatan
*/

public class Celula {
  private Navio navio; // navio que está na célula
  private boolean foiAtacado; // se a célula foi atacada ou não
  private boolean bombaProfundidade; // se a célula foi atacada com uma bomba de profundidade
  private boolean submarinoAtacou; // se o submarino que está na célula atacou
  private boolean imunidade; // se o navio que está na célula tem imunidade ao canhão que o atingiu
  private int contador; // contador para os estados temporário da célula

  /**
  * Construtor da célula.
  * Inicializa todos os atributos como false ou null.
  */
  public Celula() {
    this.navio = null;
    this.foiAtacado = false;
    this.bombaProfundidade = false;
    this.submarinoAtacou = false;
    this.imunidade = false;
    this.contador = 0;
  }

  /**
  * Função que retorna o caractere que será mostrado no mapa.
  *
  * @param esconderNavio se true, esconde o navio.
  * @return o caractere que será mostrado no mapa.
  */
  public String mostrarCelula(boolean esconderNavio) {
    if(this.foiAtacado) { // se a célula foi atacada, mostra o navio ou um símbolo de erro
      if(this.navio == null) { // se a célula não tem um navio, mostra o símbolo de erro
        return "x ";
      }
      else if(this.navio instanceof Submarino) { // se a célula tem um submarino
        if(bombaProfundidade) { // se o submarino atingido por uma bomba de profundidade, mostra o símbolo de submarino afundado
          return "s ";
        }
        else {  // se o submarino foi atingido por um canhão, mostra o símbolo de erro
          return "x ";
        }
      }
      else if(this.imunidade) {  // se o navio tem imunidade ao canhão que o atingiu, mostra o símbolo de navio imune para o jogador inimigo
        return this.navio.getCaractereMapa() + " ";
      }
      else { // se o navio não tem imunidade ao canhão que o atingiu, mostra o símbolo de navio atingido para o jogador inimigo
        return Character.toLowerCase(this.navio.getCaractereMapa()) + " ";
      }
    } 
    if(this.contador > 0) { // se a célula tem um estado temporário, mostra o símbolo correspondente
      if(bombaProfundidade) { 
        if(this.navio instanceof Submarino) { // se a célula tem um submarino, mostra o símbolo de submarino afundado, sem mudar o contador, pois o submarino afundado sempre vai ser mostrado
          return "s ";
        } else { // se a célula não tem um submarino, mostra o símbolo de bomba de profundidade e decrementa o contador
          this.contador--;
          return "o ";
        }
      } else if(submarinoAtacou) { // se a célula tem um submarino atacando, mostra o símbolo de submarino atacando e decrementa o contador
        this.contador--;
        return "S ";
      }
    }
    else { // se a célula não foi atacada e não tem um estado temporário, mostra o navio ou a água
      bombaProfundidade = false;
      submarinoAtacou = false;
      if((esconderNavio && !imunidade) || this.navio == null) { // se o navio deve ser escondido ou a célula não tem um navio, mostra a água
        return "~ ";
      }
      else {
        return this.navio.getCaractereMapa() + " ";
      }
    }
    return "x "; //nunca deve chegar aqui, mas o compilador pede um return
  }

  /**
   * Função para obter o navio que está na célula.
   * @return o navio que está na célula.
   */
  public Navio getNavio() {
    return navio;
  }

  /**
   * Função para setar o navio na célula.
   * @param navio
   */
  public void setNavio(Navio navio) {
    this.navio = navio;
  }

  /**
   * Função para verificar se a célula foi atacada.
   * @return true se a célula foi atacada, false caso contrário.
   */
  public boolean getFoiAtacado() {
  	return foiAtacado;
  }

  /**
   * Função para setar o estado da célula como atacada.
   * @param foiAtacado indica se a célula foi atacada ou não.
   */
  public void setFoiAtacado(boolean foiAtacado) {
  	this.foiAtacado = foiAtacado;
  }

  /**
   * Função para setar o estado temporário da célula.
   * @param imunidade indica se a célula tem imunidade ao canhão que a atingiu
   */
  public void setImunidade(boolean imunidade) {
    this.imunidade = imunidade;
  }

  /**
   * Função para verificar se a célula tem imunidade ao canhão que a atingiu.
   * @return true se a célula tem imunidade ao canhão que a atingiu, false caso contrário.
   */
  public boolean getImunidade() {
    return imunidade;
  }

  /**
   * Função para verificar se a célula foi atacada com uma bomba de profundidade.
   * @return true se a célula foi atacada com uma bomba de profundidade, false caso contrário.
   */
  public boolean getSubmarinoAtacou() {
    return submarinoAtacou;
  }

  /**
  * Funções para setar o estado temporário da célula.
  * Define o contador para 2, pois o estado temporário dura 2 turnos.
  * @param bombaProfundidade indica se a célula foi atacada com uma bomba de profundidade
  */
  public void setBombaProfundidade(boolean bombaProfundidade) {
    this.bombaProfundidade = bombaProfundidade;
    this.contador = 2;
  }

  /**
   * Função para setar o estado temporário do submarino atacando.
   * Define o contador para 2, pois o submarino ataca por 2 turnos.
   * @param submarinoAtacou
   */
  public void setSubmarinoAtacou(boolean submarinoAtacou) {
    this.submarinoAtacou = submarinoAtacou;
    this.contador = 2;
  }
}