import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Historico {

    private static final String ARQUIVO = "historico.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void registrarEntrada(Carro c) {
        registrar("ENTRADA", c);
    }

    public static void registrarSaida(Carro c) {
        registrar("SAIDA", c);
    }

    private static void registrar(String tipo, Carro c) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO, true))) {
            String hora = LocalDateTime.now().format(FMT);
            pw.println(tipo + " | " + hora + " | ID: " + c.id + " | Placa: " + c.placa + " | Dono: " + c.dono + " | Vaga " + c.vaga);
        } catch (Exception e) {
            System.out.println("Erro ao registrar histórico: " + e.getMessage());
        }
    }

    // Opcional: registrar tentativa falha
    public static void registrarFalha(String motivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO, true))) {
            String hora = LocalDateTime.now().format(FMT);
            pw.println("FALHA | " + hora + " | " + motivo);
        } catch (Exception e) {
            System.out.println("Erro ao registrar falha no histórico: " + e.getMessage());
        }
    }
}
