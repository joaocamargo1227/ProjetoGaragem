import java.util.Random;

public class Carro {
    public String id;
    public String dono;
    public String placa;
    public String modelo;
    public int vaga;

    // Construtor que gera novo id (11 chars alfanuméricos)
    public Carro(String dono, String placa, String modelo, int vaga) {
        this.id = gerarID(); // gera id curto
        this.dono = dono;
        this.placa = placa;
        this.modelo = modelo;
        this.vaga = vaga;
    }

    // Construtor para reconstruir a partir de persistência (preserva id)
    public Carro(String id, String dono, String placa, String modelo, int vaga) {
        this.id = id;
        this.dono = dono;
        this.placa = placa;
        this.modelo = modelo;
        this.vaga = vaga;
    }

    // Gera um ID alfanumérico de 11 caracteres (A-Z, 0-9)
    private String gerarID() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(11);
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Vaga " + vaga +
                " | Placa: " + placa +
                " | Dono: " + dono +
                " | Modelo: " + modelo;
    }
}
