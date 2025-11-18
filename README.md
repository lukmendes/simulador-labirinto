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
