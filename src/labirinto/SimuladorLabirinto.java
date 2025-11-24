package labirinto;

import java.util.Scanner;

public class SimuladorLabirinto {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int dificuldade = exibeMenuEscolha(scan);
        int[] config = defineDesafio(scan, dificuldade);

        int movimentos = config[0];
        int qtdVisuMapa = config[1];
        int movimentosIniciais = movimentos;
        int visuIniciais = qtdVisuMapa;

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

            exibeStatus(movimentos, qtdVisuMapa, pos);

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

            Object[] resultado = moveJogador(mapa, pos, direcao);

            boolean movimentoValido = (boolean) resultado[0];
            boolean chegouSaida = (boolean) resultado[2];

            if (!movimentoValido) {
                System.out.println("\n*********** MOVIMENTO INVÁLIDO ***********\n");
                continue;
            }

            pos = (int[]) resultado[1];
            movimentos--;

            // Corrigido: Vitória deve ser verificada ANTES da derrota.
            String mensagem;
            int pontuacao;
            if (chegouSaida) {
                mensagem = """
                        \n=======================================
                                        PARABÉNS
                                Você encontrou a saída!
                        ======================================= """;
                System.out.println(mensagem);

                pontuacao = calculaPontuacao(true, movimentosIniciais, movimentos, visuIniciais, qtdVisuMapa);
                System.out.println("\nSua pontuação final foi: " + pontuacao);
                break;
            }

            if (movimentos <= 0) {
                mensagem = """
                        \n====================================
                                    FIM DE JOGO
                                Movimentos esgotados!
                        ==================================== """;
                System.out.println(mensagem);

                pontuacao = calculaPontuacao(false, movimentosIniciais, movimentos, visuIniciais, qtdVisuMapa);
                System.out.println("\nSua pontuação final foi: " + pontuacao);
                break;
            }
        }

        scan.close();
    }

    // ---------- 1. Menu ----------
    public static int exibeMenuEscolha(Scanner scan) {
        String instrucoes = """
                ====================================
                            INSTRUÇÕES
                ====================================
                Objetivo: alcance o destino (S) antes que seus movimentos acabem.
                
                COMANDOS:
                W - mover para cima
                S - mover para baixo
                A - mover para a esquerda
                D - mover para a direita
                E - exibir o mapa (usa 1 visualização)
                X - sair do jogo

                REGRAS:
                • Cada movimento gasta 1 ponto de movimentação.
                • Você possui um número limitado de visualizações do mapa.
                • Ao zerar as visualizações, a tecla 'E' não terá efeito.
                • Fique atento à sua posição e planeje seus movimentos.
                """;

        System.out.println(instrucoes);

        System.out.println("""
        =======================================
                    ESCOLHA O MAPA
        =======================================
        1 - Fácil   (10x10)
        2 - Médio   (20x20)
        3 - Difícil (30x30) """);

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

    // ---------- 2. Define Desafio ----------
    public static int[] defineDesafio(Scanner scan, int dificuldade) {
        System.out.println("Deseja personalizar o desafio?");
        System.out.println("1 - Sim");
        System.out.println("2 - Não");
        System.out.print("Opção: ");

        int escolha = scan.nextInt();

        int movimentos = 0;
        int visu = 0;

        if (escolha == 1) {
            System.out.print("Digite a quantidade de movimentos: ");
            movimentos = scan.nextInt();
            System.out.print("Digite a quantidade de visualizações: ");
            visu = scan.nextInt();
        } else {
            switch (dificuldade) {
                case 1 : visu = 5; movimentos = 100; break;
                case 2 : visu = 3; movimentos = 240; break;
                case 3 : visu = 1; movimentos = 540; break;
            }
        }

        return new int[]{movimentos, visu};
    }

    // ---------- 3. Encontra P ----------
    public static int[] encontraPosicaoInicial(char[][] mapa) {
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                if (mapa[i][j] == 'P') {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // ---------- 4. Exibe Mapa ----------
    public static void exibeMapa(char[][] mapa) {
        System.out.println();
        for (char[] linha : mapa) {
            for (char c : linha) System.out.print(c + " ");
            System.out.println();
        }
        System.out.println();
    }

    // ---------- 5. Exibe Status ----------
    public static void exibeStatus(int movimentos, int qtdVisuMapa, int[] pos) {
        String mensagemStatus = """
        ==========================================
                    STATUS DO JOGADOR
        ==========================================
        Movimentos restantes : %d
        Visualizações mapa   : %d
        Posição atual        : (%d, %d)
        ------------------------------------------
        Ação [W/A/S/D] | Ver Mapa [E] | Sair [X]: """;
        System.out.printf(mensagemStatus, movimentos, qtdVisuMapa, pos[0], pos[1]);
    }

    // ---------- 6. Move Jogador ----------
    public static Object[] moveJogador(char[][] mapa, int[] pos, char direcao) {

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

    // ---------- 7. Calcula Pontuação ----------
    public static int calculaPontuacao(boolean venceu, int movimentosIniciais, int movimentosRestantes, int visuInicial, int visuRestante) {
        if (venceu) {
            return 2000 + (movimentosRestantes * 10) + (visuRestante * 20);
        } else {
            int movimentosUsados = movimentosIniciais - movimentosRestantes;
            int visuUsada = visuInicial - visuRestante;
            return (movimentosUsados * 3) + (visuUsada * 5);
        }
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