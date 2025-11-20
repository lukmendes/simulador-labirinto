package labirinto;

import java.util.Scanner;

public class SimuladorLabirinto {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        int movimentos, qtdVisuMapa;
        int dificuldade = exibeMenuEscolha(scan);

        if (dificuldade == 1) {
            qtdVisuMapa = 5;
            movimentos = 100;
        } else if (dificuldade == 2) {
            qtdVisuMapa = 3;
            movimentos = 240;
        } else {
            qtdVisuMapa = 1;
            movimentos = 540;
        }

        char[][] mapa = criaMapa(dificuldade);
        int[] pos = encontraPosicaoInicial(mapa);

        if (pos == null) {
            System.out.println("Erro: Posição inicial 'P' não encontrada.");
            return;
        }

        mapa[pos[0]][pos[1]] = '@';

        boolean jogoAtivo = true;
        boolean mostrarMapa = true;

        System.out.println("\n====================================");
        System.out.println("           JOGO INICIADO            ");
        System.out.println("====================================");

        while (jogoAtivo) {

            if (mostrarMapa) {
                exibeMapa(mapa);
                mostrarMapa = false;
            }

            exibirStatus(movimentos, qtdVisuMapa, pos);

            String entrada = scan.next();

            if (entrada.equalsIgnoreCase("X")) {
                System.out.println("Jogo encerrado.");
                break;
            }

            if (entrada.equalsIgnoreCase("E")) {
                if (qtdVisuMapa > 0) {
                    qtdVisuMapa--;
                    mostrarMapa = true;
                } else {
                    System.out.println("Você não possui mais visualizações.");
                }
                continue;
            }

            if (entrada.length() != 1) {
                System.out.println("Entrada inválida.");
                continue;
            }

            char direcao = entrada.toUpperCase().charAt(0);

            Object[] resultado = moverJogador(mapa, pos, direcao);

            boolean movimentoValido = (boolean) resultado[0];
            boolean chegouSaida = (boolean) resultado[2];

            if (!movimentoValido) {
                System.out.println("Movimento inválido!");
                continue;
            }

            pos = (int[]) resultado[1];
            movimentos--;

            // Corrigido: Vitória deve ser verificada ANTES da derrota.
            if (chegouSaida) {
                System.out.println("\n====================================");
                System.out.println("           PARABÉNS!");
                System.out.println("       Você encontrou a saída!");
                System.out.println("====================================");
                break;
            }

            if (movimentos <= 0) {
                System.out.println("\n====================================");
                System.out.println("        FIM DE JOGO");
                System.out.println("    Movimentos esgotados");
                System.out.println("====================================");
                break;
            }
        }

        scan.close();
    }

    // ---------- Exibir Status ----------
    public static void exibirStatus(int movimentos, int qtdVisuMapa, int[] pos) {
        System.out.println("====================================");
        System.out.println("            STATUS DO JOGADOR       ");
        System.out.println("====================================");
        System.out.println("Movimentos restantes : " + movimentos);
        System.out.println("Visualizações mapa   : " + qtdVisuMapa);
        System.out.println("Posição atual        : (" + pos[0] + ", " + pos[1] + ")");
        System.out.println("------------------------------------");
        System.out.print("Ação [W/A/S/D] | Ver Mapa [E] | Sair [X]: ");
    }

    // ---------- Mover Jogador ----------
    public static Object[] moverJogador(char[][] mapa, int[] pos, char direcao) {

        int r = pos[0], c = pos[1];
        int nr = r, nc = c;

        switch (direcao) {
            case 'W': nr--; break;
            case 'S': nr++; break;
            case 'A': nc--; break;
            case 'D': nc++; break;
            default:  return new Object[]{false, pos, false};
        }

        if (nr < 0 || nr >= mapa.length || nc < 0 || nc >= mapa[0].length)
            return new Object[]{false, pos, false};

        char destino = mapa[nr][nc];

        if (destino == '#')
            return new Object[]{false, pos, false};

        boolean chegouSaida = (destino == 'S');

        mapa[nr][nc] = '@';
        mapa[r][c] = '.';

        return new Object[]{true, new int[]{nr, nc}, chegouSaida};
    }

    // ---------- Menu ----------
    public static int exibeMenuEscolha(Scanner scan) {
        System.out.println("====================================");
        System.out.println("             INSTRUÇÕES              ");
        System.out.println("====================================");
        System.out.println("Objetivo: alcance o destino (S) antes que seus movimentos acabem.");
        System.out.println();
        System.out.println("COMANDOS:");
        System.out.println("  W - mover para cima");
        System.out.println("  S - mover para baixo");
        System.out.println("  A - mover para a esquerda");
        System.out.println("  D - mover para a direita");
        System.out.println("  E - exibir o mapa (usa 1 visualização)");
        System.out.println("  X - sair do jogo");
        System.out.println();
        System.out.println("REGRAS:");
        System.out.println("  • Cada movimento gasta 1 ponto de movimentação.");
        System.out.println("  • Você possui um número limitado de visualizações do mapa.");
        System.out.println("  • Ao zerar as visualizações, a tecla 'E' não terá efeito.");
        System.out.println("  • Fique atento à sua posição e planeje seus movimentos.");

        System.out.println("""
        ====================================
                  ESCOLHA O MAPA
        ====================================
        1 - Fácil   (10x10)
        2 - Médio   (20x20)
        3 - Difícil (30x30)
        """);

        int op = 0;
        while (op < 1 || op > 3) {
            System.out.print("Opção: ");
            if (scan.hasNextInt()) {
                op = scan.nextInt();
            } else {
                scan.next();
            }
        }
        return op;
    }

    // ---------- Encontrar P ----------
    public static int[] encontraPosicaoInicial(char[][] mapa) {
        for (int i = 0; i < mapa.length; i++)
            for (int j = 0; j < mapa[i].length; j++)
                if (mapa[i][j] == 'P')
                    return new int[]{i, j};
        return null;
    }

    // ---------- Exibir Mapa ----------
    public static void exibeMapa(char[][] mapa) {
        System.out.println();
        for (char[] linha : mapa) {
            for (char c : linha) System.out.print(c + " ");
            System.out.println();
        }
        System.out.println();
    }

    // ---------- Mapas ----------
    public static char[][] criaMapa(int dificuldade) {
        switch (dificuldade) {
            case 1:
                // Mapa Fácil 10x10
                return new char[][]{
                        {'P', '.', '.', '#', '#', '.', '.', '.', '.', '.'},
                        {'#', '#', '.', '#', '.', '.', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.'},
                        {'.', '#', '#', '.', '#', '#', '#', '.', '.', '#'},
                        {'.', '#', '.', '.', '.', '.', '.', '.', '#', '#'},
                        {'.', '#', '.', '#', '#', '#', '.', '#', '#', '.'},
                        {'.', '.', '.', '.', '#', '.', '.', '.', '.', '.'},
                        {'#', '#', '.', '.', '.', '#', '#', '.', '#', '.'},
                        {'.', '.', '#', '#', '.', '.', '#', '.', '.', '.'},
                        {'.', '.', '.', '.', '#', '#', '#', '#', '#', 'S'}
                };
            case 2:
                // Mapa Médio 20x20
                return new char[][]{
                        {'P', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '#', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '.', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.'},
                        {'#', '#', '#', '#', '#', '.', '#', '.', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', 'S'}
                };
            case 3:
                 // Mapa Díficil 30x30
                return new char[][]{
                        {'P', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '.', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'#', '#', '#', '#', '#', '.', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.'},
                        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                        {'.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', 'S'}
                };
            default:
                System.out.println("Opção não encontrada. Retornando opção 'fácil'. ");
                return criaMapa(1);
        }
    }
}