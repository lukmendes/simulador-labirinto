package labirinto;

import java.util.Scanner;

public class SimuladorLabirinto {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int dificuldade = exibeMenuEscolha(scan);
        char[][] mapa = criaMapa(dificuldade);
        int[] pos = encontraPosicaoInicial(mapa);
        
        // Verifica se a posição inicial foi encontrada
        if (pos == null) {
             System.out.println("Erro: Posição inicial 'P' não encontrada no mapa.");
             return; 
        }

        // Atualiza a posição inicial 'P' para o símbolo de jogador '@'
        mapa[pos[0]][pos[1]] = '@';

        int movimentos = 0;
        boolean jogoAtivo = true;

        System.out.println("\n--- Jogo Iniciado ---");

        while (jogoAtivo) {
            exibeMapa(mapa);
            System.out.println("Movimentos: " + movimentos);
            System.out.println("Posição atual: (" + pos[0] + ", " + pos[1] + ")");
            System.out.print("Mover (W/A/S/D) ou Sair (X): ");
            
            // Lógica para consumir a linha (caso tenha sobrado algo) e ler a entrada
            String entrada = scan.next();

            if (entrada.equalsIgnoreCase("X")) {
                jogoAtivo = false;
                System.out.println("\nJogo encerrado por desistência.");
                break;
            }

            if (entrada.length() != 1) {
                System.out.println("Entrada inválida. Use apenas uma letra (W, A, S, D, X).");
                continue;
            }

            char direcao = entrada.charAt(0);
            
            // Chama a função de movimentação do jogador
            Object[] resultadoMovimento = moverJogador(mapa, pos, direcao);
            
            boolean movimentoValido = (boolean) resultadoMovimento[0];
            int[] novaPos = (int[]) resultadoMovimento[1];
            // boolean encontrouItem = (boolean) resultadoMovimento[2]; // Sempre false no momento
            boolean chegouSaida = (boolean) resultadoMovimento[3];

            if (movimentoValido) {
                pos = novaPos; // Atualiza a posição
                movimentos++;
                System.out.println("Movimento válido!");

                if (chegouSaida) {
                    System.out.println("\n🎉 PARABÉNS! Você encontrou a saída 'S'!");
                    System.out.println("Total de movimentos: " + movimentos);
                    jogoAtivo = false;
                }
            } else {
                System.out.println("Movimento inválido! Você bateu na parede ou saiu do mapa.");
            }
            System.out.println("-------------------------");
        }
        scan.close();
    }

    /**
     * Tenta mover o jogador no mapa.
     */
    public static Object[] moverJogador(char[][] mapa, int[] posAtual, char direcao) {
        int linhaAtual = posAtual[0];
        int colunaAtual = posAtual[1];
        int novaLinha = linhaAtual;
        int novaColuna = colunaAtual;

        // 1. Determina a nova posição
        switch (Character.toUpperCase(direcao)) {
            case 'W': // Cima
                novaLinha--;
                break;
            case 'S': // Baixo
                novaLinha++;
                break;
            case 'A': // Esquerda
                novaColuna--;
                break;
            case 'D': // Direita
                novaColuna++;
                break;
            default:
                // Retorna movimento inválido se a direção não for reconhecida
                return new Object[]{false, posAtual, false, false};
        }

        // 2. Verifica se a nova posição é válida (fora do mapa ou parede '#')
        int linhas = mapa.length;
        int colunas = mapa[0].length;

        boolean foraDoMapa = (novaLinha < 0 || novaLinha >= linhas || novaColuna < 0 || novaColuna >= colunas);
        if (foraDoMapa) {
            return new Object[]{false, posAtual, false, false}; // Movimento inválido: fora do mapa
        }

        char destino = mapa[novaLinha][novaColuna];

        // 3. Verifica se bateu na parede
        if (destino == '#') {
            return new Object[]{false, posAtual, false, false}; // Movimento inválido: parede
        }
        
        // Se chegou aqui, o movimento é válido (caminho '.' ou saída 'S')

        // 4. Se o destino é a saída 'S'
        boolean chegouSaida = (destino == 'S');
        
        // 5. Atualiza o mapa: marca a nova posição com '@' e a antiga com '.'
        mapa[novaLinha][novaColuna] = '@';
        mapa[linhaAtual][colunaAtual] = '.'; 

        // Retorna o resultado: [movimento válido, nova posição, encontrou item, chegou saída]
        return new Object[]{true, new int[]{novaLinha, novaColuna}, false, chegouSaida};
    }

    public static int exibeMenuEscolha(Scanner scan) {
        String menu = """
                Escolha o tamanho do mapa:
                1 - Fácil (10x10);
                2 - Médio (20x20);
                3 - Difícil (30x30); """;
        System.out.println(menu);
        int escolha = 0;
        while (escolha < 1 || escolha > 3) {
            System.out.print("Digite 1, 2 ou 3: ");
            if (scan.hasNextInt()) {
                escolha = scan.nextInt();
            } else {
                System.out.println("Entrada inválida. Digite um número.");
                scan.next(); // Limpa entrada inválida
            }
        }
        return escolha;
    }

    public static void exibeMapa(char[][] mapa) {
        System.out.println();
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                System.out.print(mapa[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int[] encontraPosicaoInicial(char[][] mapa) {
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                // Procura por 'P'
                if (mapa[i][j] == 'P') {
                    return new int[] {i, j};
                }
            }
        }
        return null; // Retorna nulo se 'P' não for encontrado
    }

    public static char[][] criaMapa(int dificuldade) {
        switch (dificuldade) {
            case 1:
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
                // O seu mapa 2 tinha 21 linhas. Corrigi o tamanho da última linha para 20 colunas.
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
                 // O seu mapa 3 tinha 31 linhas. Corrigi o tamanho da última linha para 30 colunas.
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