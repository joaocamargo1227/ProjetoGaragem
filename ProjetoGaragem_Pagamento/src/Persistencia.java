import java.io.*;
import java.util.*;

public class Persistencia {

    private static final String ARQUIVO = "garagem.txt";

    public static void salvar(List<Carro> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) {
            for (Carro c : lista) {
                // id;dono;placa;modelo;vaga
                pw.println(c.id + ";" + c.dono + ";" + c.placa + ";" + c.modelo + ";" + c.vaga);
            }
        } catch (Exception e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public static List<Carro> carregar() {
        List<Carro> lista = new ArrayList<>();

        File file = new File(ARQUIVO);
        if (!file.exists()) return lista;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(";", -1);
                if (p.length < 5) continue;
                // reconstrói Carro preservando id
                String id = p[0];
                String dono = p[1];
                String placa = p[2];
                String modelo = p[3];
                int vaga = Integer.parseInt(p[4]);
                Carro c = new Carro(id, dono, placa, modelo, vaga);
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
        return lista;
    }
}
