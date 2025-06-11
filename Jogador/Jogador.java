package Jogador;

import java.util.*;
import Navios.*;
import Tabuleiro.*;
/**
 * classe que representa um jogador humano no jogo de batalha naval.
 * para a classe jogador, os ataques e posicionamento de navios sao feitos de forma
 * manual, selecionadas pelo usuario que esta jogando a partida.
 * @author Jessica
 */

public class Jogador {
    private String name;
    private Tabuleiro tabuleiroDefesa;
    private Tabuleiro tabuleiroAtaque; // tabuleiro defesa do bot / p2
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Construtor da classe Jogador.
     * @param name nome do jogador, personalizado pelo usuário
     * @param tabuleiroDefesa tabuleiro de defesa do jogador
     * @param tabuleiroAtaque tabuleiro de ataque do jogador (do bot ou outro jogador)
     */

    public Jogador(String name, Tabuleiro tabuleiroDefesa, Tabuleiro tabuleiroAtaque) {
        this.name = name;
        this.tabuleiroDefesa = tabuleiroDefesa;
        this.tabuleiroAtaque = tabuleiroAtaque;
    }

    /**
     * Construtor padrão da classe Jogador.
     * Inicializa o jogador com um nome padrão e cria os tabuleiros de defesa e ataque.
     */
    public Jogador() {
        this.name = "Jogador Padrão";
        this.tabuleiroDefesa = new Tabuleiro();
        this.tabuleiroAtaque = new Tabuleiro();
    }

    /**
     * Obtém o tabuleiro de defesa do jogador.
     * @return tabuleiro de defesa do jogador, onde os navios são posicionados
     */
    
    public Tabuleiro getTabuleiroDefesa() {
        return tabuleiroDefesa;
    }

    /**
     * Define o tabuleiro de defesa do jogador.
     * @param tabuleiroDefesa tabuleiro de defesa do jogador
     */

    public void setTabuleiroDefesa(Tabuleiro tabuleiroDefesa) {
        this.tabuleiroDefesa = tabuleiroDefesa;
    }

    /**
     * Obtém o tabuleiro de ataque do jogador.
     * @return tabuleiro de ataque do jogador, que pode ser o tabuleiro do bot ou de outro jogador
     */

    public Tabuleiro getTabuleiroAtaque() {
        return tabuleiroAtaque;
    }

    /**
     * Define o tabuleiro de ataque do jogador.
     * @param tabuleiroAtaque tabuleiro de ataque do jogador, que pode ser o tabuleiro do bot ou de outro jogador
     */
    public void setTabuleiroAtaque(Tabuleiro tabuleiroAtaque) {
        this.tabuleiroAtaque = tabuleiroAtaque;
    }

    /**
     * Obtém o nome do jogador.
     * @return nome do jogador, personalizado pelo usuário
     */

    public String getName() {
        return name;
    }

    /**
     * Define o nome do jogador.
     * @param name nome do jogador, personalizado pelo usuário
     */
    public void setName(String name) {
        this.name = name;
    }

/**
 * gera uma lista de navios com base na escolha do preset do usuario (escolha manual)
 * cada preset possui uma estrategia diferente de navios para posicionamento.
 * @param escolha número do preset escolhido (1, 2 ou 3)
 * @return lista de navios a serem posicionados
 */
    protected List<Navio> gerarPresetNavios (int escolha){
        List<Navio> naviosParaPosicionar = new ArrayList<Navio>();
        switch (escolha) {
            case 1: // mix de pesados e leves para versatilidade
                naviosParaPosicionar.add(new Navios.Dreadnought());     
                naviosParaPosicionar.add(new Navios.BattleShip());      
                naviosParaPosicionar.add(new Navios.Cruzado());
                naviosParaPosicionar.add(new Navios.Destroyer());       
                naviosParaPosicionar.add(new Navios.Destroyer());       
                naviosParaPosicionar.add(new Navios.CruzadoLeve());     
                naviosParaPosicionar.add(new Navios.CruzadoLeve());     
                naviosParaPosicionar.add(new Navios.Submarino());      
                naviosParaPosicionar.add(new Navios.Submarino());      
                break; 
            case 2: // ofensivo: foco em dano pesado
                naviosParaPosicionar.add(new Navios.Dreadnought());     
                naviosParaPosicionar.add(new Navios.Dreadnought());    
                naviosParaPosicionar.add(new Navios.BattleShip());     
                naviosParaPosicionar.add(new Navios.BattleShip());     
                naviosParaPosicionar.add(new Navios.Cruzado());        
                naviosParaPosicionar.add(new Navios.Cruzado());        
                naviosParaPosicionar.add(new Navios.Destroyer());       
                break; 
            case 3: // tático: foco em controle e contra-ataque a leves
                naviosParaPosicionar.add(new Navios.Cruzado());       
                naviosParaPosicionar.add(new Navios.Cruzado());
                naviosParaPosicionar.add(new Navios.Destroyer());      
                naviosParaPosicionar.add(new Navios.Destroyer());      
                naviosParaPosicionar.add(new Navios.CruzadoLeve());    
                naviosParaPosicionar.add(new Navios.CruzadoLeve());    
                naviosParaPosicionar.add(new Navios.CruzadoLeve());    
                naviosParaPosicionar.add(new Navios.Submarino());    
                naviosParaPosicionar.add(new Navios.Submarino());      
                naviosParaPosicionar.add(new Navios.Submarino());    
                naviosParaPosicionar.add(new Navios.Submarino());    
                break; 
            default: // caso inválido, retorna um preset equilibrado por padrão
                System.out.println("Escolha de preset inválida. Usando preset Equilibrado por padrão.");
                naviosParaPosicionar.add(new Navios.Dreadnought());     
                naviosParaPosicionar.add(new Navios.BattleShip());      
                naviosParaPosicionar.add(new Navios.Cruzado());
                naviosParaPosicionar.add(new Navios.Destroyer());       
                naviosParaPosicionar.add(new Navios.Destroyer());       
                naviosParaPosicionar.add(new Navios.CruzadoLeve());     
                naviosParaPosicionar.add(new Navios.CruzadoLeve());     
                naviosParaPosicionar.add(new Navios.Submarino());      
                naviosParaPosicionar.add(new Navios.Submarino());      
                break;
        }
        return naviosParaPosicionar;      
    }

/**
 * posiciona os navios no tabuleiro de defesa do jogador manualmente
 * @throws StringIndexOutOfBoundsException indica que a coordenada digitada pelo usuario esta fora do padrao esperado (ex: A1)
 * @throws NumberFormatException indica que a coordenada digitada pelo usuario nao pode ser convertida para um numero inteiro (ex: A1)
 * @throws OpcaoInvalidaException indica que a direcao escolhida pelo usuario para posicionar o navio é invalida (ex: 0, 1, 2, 3)
 */
    public void posicionarNavios() {
        System.out.println("\n--- " + this.name + ", posicionando navios! ---");

        List<Navio> naviosParaPosicionar = new ArrayList<Navio>();

        int escolhaPreset = 0;
        boolean presetValido = false;
        do {
            try {
                System.out.println("Escolha um preset de navios:");
                System.out.println("[1] - Equilibrado: Versátil, com boa defesa e ataque geral.");
                System.out.println("        (Dreadnought, BattleShip, Cruzador, 2x Destroyer, 2x Cruzador Leve e 2x Submarino)");
                System.out.println("[2] - Ofensivo: Foco em dano pesado, com força bruta superior, fraco contra submarinos.");
                System.out.println("        (2x Dreadnought, 2x BattleShip, 2x Cruzador, Destroyer)");
                System.out.println("[3] - Tático: Mais leves, focado em caçar submarinos e flanquear.");
                System.out.println("        (2x Cruzador, 2x Destroyer, 3x Cruzador Leve, 4x Submarino)");

                System.out.print("Digite o número do preset que deseja usar: ");
                escolhaPreset = scanner.nextInt();
                scanner.nextLine(); 

                naviosParaPosicionar = gerarPresetNavios(escolhaPreset);
                if (!naviosParaPosicionar.isEmpty()) {
                    presetValido = true;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Entrada inválida. Digite um NÚMERO para o preset.");
            }
        } while (!presetValido);


        for (Navio navio : naviosParaPosicionar) {
            boolean posicionado = false;
            while (!posicionado) {
                System.out.println("\nPosicionando: " + navio.getTipoNavio() + " (Tamanho: " + navio.getTamanho() + ")");
                this.tabuleiroDefesa.mostrar(false); // Mostra o tabuleiro atual do jogador

                System.out.print("Digite a coordenada inicial (ex: A1): ");
                String posicaoStr = scanner.nextLine().toUpperCase().trim(); 

                if (posicaoStr.isEmpty() || posicaoStr.length() < 2) {
                    System.out.println("Formato de coordenada inválido. Tente novamente.");
                    continue; 
                }

                try {
                    int x = posicaoStr.charAt(0) - 'A';
                    int y = Integer.parseInt(posicaoStr.substring(1)); 

                    try {
                        //posicionado = this.tabuleiroDefesa.adicionarNavio(navio, x, y, 0); 
                        posicionado = navio.posicionar(this.tabuleiroDefesa, x, y, 0);
                    } catch (OpcaoInvalidaException e) { 
                        System.out.println("Erro de direção: " + e.getMessage());
                        posicionado = false; 
                        continue; 
                    }

                } catch (StringIndexOutOfBoundsException e) { 
                    System.out.println("Entrada de coordenada inválida: " + e.getMessage() + ". Tente novamente (ex: A1).");
                } catch (NumberFormatException e) {
                    System.out.println("Entrada de coordenada inválida: " + e.getMessage() + ". Tente novamente (ex: A1).");
                }
                catch (Exception e) { 
                    System.out.println("Ocorreu um erro inesperado: " + e.getMessage() + ". Tente novamente.");
                }
            }
        }
        System.out.println("\nTodos os seus navios foram posicionados!");
        this.tabuleiroDefesa.mostrar(false);
    }

/**
 * ataque do jogador contra o alvo (bot ou outro jogador)
 * @param inimigo alvo para o jogador atacar, que é o bot ou outro jogador.
 */

    public void fazerJogada(Jogador inimigo) {
        //System.out.println("\n--- Turno de " + this.name + " ---");
        System.out.println("\nSeu Tabuleiro:");
        this.tabuleiroDefesa.mostrar(false);

        System.out.println("\nTabuleiro do Inimigo:");
        this.tabuleiroAtaque.mostrar(true);

        Navio navioEscolhido = null;
        boolean escolhaValida = false;

        do {
            System.out.println("\nNavios disponíveis para " + this.name + ":");
            //Map<Integer, Navio> meusNavios = tabuleiroDefesa.getNavios();
            List<Navio> meusNavios = tabuleiroDefesa.getNavios();

            if (meusNavios.isEmpty()) {
                System.out.println("Você não tem navios para atirar!"); //(isso não deve acontecer se o jogo esta ativo)
                return;
            }

            System.out.println("------------------------------------");
            System.out.printf("%-5s %-20s %-10s\n", "ID", "Tipo de Navio", "Cooldown");
            System.out.println("------------------------------------");

            boolean temNavioAtiravel = false;
            int idNavio = 1;
            for (Navio navio : meusNavios) {
                if (!navio.foiAfundado()) {
                    System.out.printf("%-5d %-20s %-10s\n", 
                                      idNavio, 
                                      navio.getTipoNavio(), 
                                      navio.podeAtirar() ? "Pronto!" : navio.getTurnosRestantesCooldown() + " turnos");
                    idNavio++;
                    if (navio.podeAtirar()) {
                        temNavioAtiravel = true;
                    }
                }
            }
            System.out.println("------------------------------------");

            if (!temNavioAtiravel) {
                System.out.println("Todos os seus navios estão afundados ou em cooldown. Passando o turno.");
                return;
            }

            System.out.print("Escolha o ID do navio para atirar: ");
            try {
                int idEscolha = scanner.nextInt();
                scanner.nextLine();

                idNavio = 1;
                for (Navio navio : meusNavios) {
                    if (idEscolha == idNavio) {
                        navioEscolhido = navio;
                        break;
                    }
                    idNavio++;
                }
                //navioEscolhido = tabuleiroDefesa.buscarNavio(idEscolha);

                if (navioEscolhido == null) {
                    System.out.println("ID de navio inválido. Tente novamente.");
                } else if (navioEscolhido.foiAfundado()) {
                    System.out.println("Este navio já foi afundado. Escolha outro.");
                    navioEscolhido = null;
                } else if (!navioEscolhido.podeAtirar()) {
                    System.out.println("Este navio está em cooldown. Escolha outro.");
                    navioEscolhido = null;
                } else {
                    escolhaValida = true;
                }
            } catch (OpcaoInvalidaException e) {
                System.out.println("Entrada inválida. Digite um número para o ID do navio.");
                scanner.nextLine();
            }

        } while (!escolhaValida);

        navioEscolhido.atirar(this.tabuleiroAtaque, this.tabuleiroDefesa); 
    }

    /**
     * Verifica se todos os navios do jogador foram afundados.
     * @return true se todos os navios foram afundados, false caso contrário.
     */
    public boolean todosNaviosAfundados() {
        return tabuleiroDefesa.todosNaviosAfundados();
    }

    /**
     * Fecha o scanner utilizado para entrada de dados.
     */
    public static void fecharScanner() {
        scanner.close();
    }
}