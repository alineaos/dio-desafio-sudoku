import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;

public class Main {
    public static final Scanner sc = new Scanner(System.in);
    private static Jogo jogo;
    private final static int LIMITE_TABULEIRO = 9;

    public static void main(String[] args) {
        final Map<String, String> posicao = Stream.of(args)
                .collect(toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));
        int opcao;
        do {
            System.out.println("JOGO DO SUDOKU");
            System.out.println("======================");
            System.out.println("Escolha uma opção");
            System.out.println("[1] Novo jogo");
            System.out.println("[2] Colocar um número");
            System.out.println("[3] Remover um número");
            System.out.println("[4] Visualizar tabuleiro");
            System.out.println("[5] Verificar status do jogo");
            System.out.println("[6] Limpar");
            System.out.println("[7] Finalizar");
            System.out.println("[0] Sair");

            while (true) {
                try {
                    opcao = sc.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Informe um número válido\n");
                    sc.next();
                }
            }

            switch (opcao) {
                case 1 -> novoJogo(posicao);
                case 2 -> inserirNumero();
                case 3 -> removerNumero();
                case 4 -> visualizarJogo();
                case 5 -> statusJogo();
                case 6 -> limpar();
                case 7 -> finalizar();
                case 0 -> System.exit(0);
                default -> System.out.println("Opção inválida.");
            }
        } while (true);
    }

    public static void novoJogo(Map<String, String> posicao) {
        if (nonNull(jogo)) {
            System.out.println("O jogo já foi iniciado");
            return;
        }


        List<List<Posicoes>> posicoes = new ArrayList<>();
        for (int i = 0; i < LIMITE_TABULEIRO; i++) {
            posicoes.add(new ArrayList<>());
            for (int j = 0; j < LIMITE_TABULEIRO; j++) {
                String posicaoDesejada = posicao.get("%s,%s".formatted(i, j));
                int esperado = Integer.parseInt(posicaoDesejada.split(",")[0]);
                boolean fixo = Boolean.parseBoolean(posicaoDesejada.split(",")[1]);
                Posicoes posicaoAtual = new Posicoes(esperado, fixo);
                posicoes.get(i).add(posicaoAtual);
            }
        }

        jogo = new Jogo(posicoes);
        System.out.println("O jogo está pronto para ser iniciado");
    }

    public static void inserirNumero() {
        if (isNull(jogo)) {
            System.out.println("Inicie o jogo para continuar");
            return;
        }

        System.out.println("Informe a coluna: ");
        int col = numeroValido(0, 8);
        System.out.println("Informe a linha: ");
        int lin = numeroValido(0, 8);
        System.out.printf("Informe o número que será inserido na posição %d, %d\n", col, lin);
        int valor = numeroValido(1, 9);
        if (!jogo.mudarValor(col, lin, valor)) {
            System.out.printf("A posição [%d, %d] possui um valor fixo.\n", col, lin);
        }
    }

    public static void removerNumero() {
        if (isNull(jogo)) {
            System.out.println("Inicie o jogo para continuar");
            return;
        }

        System.out.println("Informe a coluna: ");
        int col = numeroValido(0, 8);
        System.out.println("Informe a linha: ");
        int lin = numeroValido(0, 8);
        if (!jogo.limparValor(col, lin)) {
            System.out.printf("A posição [%d, %d] possui um valor fixo.\n", col, lin);
        }
    }

    public static void visualizarJogo() {
        if (isNull(jogo)) {
            System.out.println("Inicie o jogo para continuar");
            return;
        }

        Object[] args = new Object[81];
        int argPos = 0;
        for (int i = 0; i < LIMITE_TABULEIRO; i++) {
            for (var col : jogo.getPosicoes()) {
                args[argPos++] = " " + ((isNull(col.get(i).getAtual())) ? " " : col.get(i).getAtual());
            }
        }

        System.out.println("Seu jogo se encontra da seguinte forma: ");
        System.out.printf((Tabuleiro.TEMPLATE) + "\n", args);
    }

    public static void statusJogo() {
        if (isNull(jogo)) {
            System.out.println("Inicie o jogo para continuar");
            return;
        }

        System.out.printf("Status do jogo: %s\n", jogo.getStatus().getLegenda());

        if (jogo.erros()) {
            System.out.println("O jogo contém erros.");
        } else {
            System.out.println("O jogo não contém erros.");
        }
    }

    public static void limpar() {
        if (isNull(jogo)) {
            System.out.println("Inicie o jogo para continuar");
            return;
        }

        System.out.println("Tem certeza que deseja limpar o jogo? Você perderá todo o seu progresso. [Digite 'sim' ou 'não']");
        String confirmar = sc.next();
        while (!confirmar.equalsIgnoreCase("sim") && !confirmar.equalsIgnoreCase("não")) {
            System.out.println("Digite 'sim' ou 'não'");
            confirmar = sc.next();
        }

        if (confirmar.equalsIgnoreCase("sim")) {
            jogo.resetar();
        }

    }

    public static void finalizar() {
        if (isNull(jogo)) {
            System.out.println("Inicie o jogo para continuar");
            return;
        }

        if (jogo.jogoFinalizado()) {
            System.out.println("Parabéns, você finalizou o jogo!");
            visualizarJogo();
            jogo = null;
        } else if (jogo.erros()) {
            System.out.println("Seu jogo contém erros. Verifique o tabuleiro e corrija-os.");
        } else {
            System.out.println("Seu jogo possui espaços em branco. Verifique o tabuleiro e preencha-os.");
        }
    }

    public static int numeroValido(final int min, final int max) {
        int numerovalido;

        while (true) {
            try {
                int numero = sc.nextInt();
                if (numero >= min && numero <= max) {
                    numerovalido = numero;
                    break;
                } else {
                   System.out.printf("Informe um número entre %d e %d\n", min, max);
                }
            } catch (InputMismatchException e) {
                System.out.printf("Informe um número entre %d e %d\n", min, max);
                sc.next();
            }
        }
        return numerovalido;
    }
}


