# Simulador de Labirinto (Projeto Acadêmico)

Este projeto faz parte da disciplina **Lógica de Programação e Algoritmos**, ministrada por **Patrícia Dourado** na **UCSAL** em **2/2025**. Foi desenvolvido apenas em **métodos** e **arrays bidimensionais**.

O objetivo geral é criar um programa de console onde o jogador percorre um labirinto, representado por uma matriz, até alcançar a saída em um limite de movimentos e visualizações.

---

# 👤 Parte desenvolvida por: **Lucas Mendes**

Minha responsabilidade no grupo é implementar toda a parte relacionada à **Representação e Manipulação do Labirinto**, incluindo:

* Criação dos mapas que o jogo usará.
* Seleção da dificuldade (funcionalidade extra).
* Exibição dos mapas no console.
* Identificação da posição inicial (`P`).
* Preparação para integração com os módulos dos colegas.

---

## ✔ Funcionalidades criadas (Lucas)

### 1. Criação dos Mapas por Dificuldade (Extra implementado)

Implementei três mapas em matrizes bidimensionais, um para cada nível de dificuldade, atendendo ao requisito opcional de Mapas por Dificuldade:

* **Fácil:** 10×10
* **Médio:** 20×20
* **Difícil:** 30×30

Os mapas são construídos usando `char[][]` com os seguintes símbolos:

* `'P'` → Posição inicial do jogador.
* `'S'` → Saída do labirinto.
* `'#'` → Parede/Obstáculo.
* `'.'` → Caminho livre.

Os mapas são criados e retornados pelo método:

```java
public static char[][] criaMapa(int dificuldade);
````

### 2. Exibição do Menu de Escolha de Mapa

Antes de carregar o labirinto, o método `exibeMenuEscolha` exibe as opções de tamanho/dificuldade e captura a escolha do usuário:

```java
public static int exibeMenuEscolha(Scanner scan);
```

### 3. Exibição do Labirinto no Console

O método `exibeMapa` é responsável por imprimir o estado atual do labirinto no console. Este método será chamado pela lógica principal de Pedro, mas é funcionalmente completo neste módulo:

```java
public static void exibeMapa(char[][] mapa);
```

Estrutura: Utiliza for aninhado para percorrer e exibir a matriz.

### 4. Localização da Posição Inicial

A função crucial para a próxima etapa (Movimentação) é a localização da coordenada inicial do jogador (P).

* **Busca:** Percorre o mapa para encontrar o caractere 'P'.

* **Retorno:** Retorna um array de inteiros com a linha e coluna de P.

```java
public static int[] encontraPosicaoInicial(char[][] mapa);
```

---

# 👤 Parte desenvolvida por: **Jilson**

Minha responsabilidade no grupo é implementar toda a parte relacionada à **Movimentação do jogador**, incluindo:


 Criar a função responsável por mover o jogador (W, A, S, D).
 
 Impedir movimentos inválidos (paredes, fora do mapa).
 
 Atualizar a posição na matriz ( @ ).
 
 Detectar chegada na saída S.
 
 Detectar itens (caso existam) e retornar o efeito



---

# 👤 Parte desenvolvida por: **Pedro**

Implementei o **Fluxo de Jogo aprimorado no `SimuladorLabirinto.java`**, integrando movimentação, limites, mensagens claras e controle completo da execução do labirinto.

Minhas entregas incluem:

### ✔ 1. Limite de Movimentos por Dificuldade

Cada nível de dificuldade agora define um número máximo de passos que o jogador pode realizar.
Ao longo da partida, cada movimento reduz esse total, e o jogo termina automaticamente ao atingir zero.

### ✔ 2. Limite de Visualizações do Mapa

Incluí a variável `qtdVisuMapa`, que controla quantas vezes o jogador pode pressionar **E** para exibir o mapa completo.
Quando esse limite chega a zero, o jogo informa claramente ao jogador e impede novas visualizações.

### ✔ 3. Exibição de Status Clara e Organizada

Toda ação do jogador exibe um painel de status com:

* Movimentos restantes
* Visualizações restantes
* Posição atual do jogador
* Comandos disponíveis

Isso orienta o jogador a cada turno.

### ✔ 4. Fluxo Principal do Jogo

Implementei a lógica completa do loop principal do labirinto, incluindo:

* Interpretação dos comandos
* Chamadas ao método de movimentação (criado por Jilson)
* Atualização da posição do jogador
* Verificação da chegada à saída
* Controle de erros e comandos inválidos
* Mensagens informativas e consistentes

O jogo agora apresenta uma experiência estruturada, clara e com feedback imediato ao usuário.

### ✔ 5. Sistema de Exibição do Mapa com Tratamento de Erros

Ao tentar exibir o mapa sem visualizações disponíveis, o jogo trata o erro de forma controlada e amigável, evitando falhas e mantendo a integridade da partida.

---