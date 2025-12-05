import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class Relatorio {

    public static void gerar(List<Carro> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("relatorio.txt"))) {

            pw.println("RELATÓRIO ATUAL DA GARAGEM");
            pw.println("Generated: " + java.time.LocalDateTime.now());
            pw.println("----------------------------");
            pw.println();

            for (Carro c : lista) {
                pw.println("ID: " + c.id + " | Vaga " + c.vaga + " | Placa: " + c.placa + " | Dono: " + c.dono + " | Modelo: " + c.modelo);
            }

            pw.println();
            pw.println("----------------------------");
            pw.println("Fim do Relatório");

        } catch (Exception e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
