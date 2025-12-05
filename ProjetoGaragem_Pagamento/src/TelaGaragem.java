import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Tela principal do sistema de garagem.
 * Substitua por este arquivo inteiro no TelaGaragem.java.
 */
public class TelaGaragem extends JFrame {

    private static final int TOTAL_VAGAS = 5;

    // Carrega a lista de carros (persistida em arquivo)
    private final List<Carro> carros;
    private final JTextArea areaStatus;

    public TelaGaragem() {
        super("Sistema de Garagem - Pagamento Obrigatório");

        // Inicializações
        this.carros = Persistencia.carregar();
        this.areaStatus = new JTextArea();

        // Configurações da janela
        setSize(700, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Painel de formulário (topo)
        JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Campos de entrada
        JTextField txtDono = new JTextField();
        JTextField txtPlaca = new JTextField();
        JTextField txtModelo = new JTextField();
        JTextField txtVaga = new JTextField();

        // Botões
        JButton btnSalvar = new JButton("Estacionar");
        JButton btnLiberar = new JButton("Liberar Vaga");
        JButton btnRelatorio = new JButton("Gerar Relatório");

        // Adiciona componentes ao formulário (linha por linha para evitar erros)
        form.add(new JLabel("Nome do Dono:"));
        form.add(txtDono);

        form.add(new JLabel("Placa:"));
        form.add(txtPlaca);

        form.add(new JLabel("Modelo:"));
        form.add(txtModelo);

        form.add(new JLabel("Vaga (1 a " + TOTAL_VAGAS + "):"));
        form.add(txtVaga);

        // Botões no formulário
        form.add(btnSalvar);
        form.add(btnLiberar);

        // Adiciona painéis/áreas à janela
        add(form, BorderLayout.NORTH);

        areaStatus.setEditable(false);
        areaStatus.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(areaStatus), BorderLayout.CENTER);

        // Observação: btnRelatorio é adicionado depois para evitar referência antes da declaração
        add(btnRelatorio, BorderLayout.SOUTH);

        // Atualiza visual inicial
        atualizarStatus();

        // ---------- AÇÕES ----------
        // Ação do botão "Estacionar"
        btnSalvar.addActionListener(e -> {
            String dono = txtDono.getText().trim();
            String placa = txtPlaca.getText().trim().toUpperCase();
            String modelo = txtModelo.getText().trim();
            String vagaStr = txtVaga.getText().trim();

            if (dono.isEmpty() || placa.isEmpty() || modelo.isEmpty() || vagaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos para estacionar.");
                return;
            }

            int vaga;
            try {
                vaga = Integer.parseInt(vagaStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número de vaga inválido.");
                return;
            }

            if (vaga < 1 || vaga > TOTAL_VAGAS) {
                JOptionPane.showMessageDialog(this, "Vaga deve ser entre 1 e " + TOTAL_VAGAS + ".");
                return;
            }

            // Verifica vaga ocupada ou placa duplicada
            for (Carro c : carros) {
                if (c.vaga == vaga) {
                    JOptionPane.showMessageDialog(this, "Vaga já ocupada!");
                    return;
                }
                if (c.placa.equalsIgnoreCase(placa)) {
                    JOptionPane.showMessageDialog(this, "Placa já cadastrada!");
                    return;
                }
            }

            // Cria novo carro e garante ID único (tenta até 10 vezes)
            Carro novo = new Carro(dono, placa, modelo, vaga);
            boolean unico = false;
            int tentativas = 0;
            while (!unico && tentativas < 10) {
                unico = true;
                for (Carro c : carros) {
                    if (c.id.equals(novo.id)) {
                        unico = false;
                        break;
                    }
                }
                if (!unico) {
                    // recria para gerar outro id
                    novo = new Carro(dono, placa, modelo, vaga);
                }
                tentativas++;
            }

            if (!unico) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar ID único. Tente novamente.");
                return;
            }

            // Adiciona carro, persiste e registra histórico
            carros.add(novo);
            Persistencia.salvar(carros);
            Historico.registrarEntrada(novo);

            // Atualiza interface e informa ID
            atualizarStatus();
            JOptionPane.showMessageDialog(this, "Carro estacionado!\nID do veículo: " + novo.id);

            // Limpa campos
            txtDono.setText("");
            txtPlaca.setText("");
            txtModelo.setText("");
            txtVaga.setText("");
        });

        // Ação do botão "Liberar Vaga" (somente com pagamento confirmado)
        btnLiberar.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "Informe o ID do veículo para liberação:");
            if (id == null) return; // usuário cancelou
            id = id.trim();

            String placa = JOptionPane.showInputDialog(this, "Informe a placa do veículo:");
            if (placa == null) return;
            placa = placa.trim().toUpperCase();

            if (id.isEmpty() || placa.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID e Placa são obrigatórios para liberar a vaga.");
                return;
            }

            Carro alvo = null;
            for (Carro c : carros) {
                if (c.id.equals(id) && c.placa.equalsIgnoreCase(placa)) {
                    alvo = c;
                    break;
                }
            }

            if (alvo == null) {
                JOptionPane.showMessageDialog(this, "Nenhum veículo encontrado com esse ID e placa.");
                Historico.registrarFalha("Falha liberação - ID/PLACA inválidos. ID=" + id + " Placa=" + placa);
                return;
            }

            int opt = JOptionPane.showConfirmDialog(this,
                    "Confirme o pagamento para liberar o veículo:\n\n" +
                            "ID: " + alvo.id + "\n" +
                            "Placa: " + alvo.placa + "\n" +
                            "Modelo: " + alvo.modelo + "\n" +
                            "Vaga: " + alvo.vaga + "\n\n" +
                            "Pagamento confirmado?",
                    "Confirmação de Pagamento", JOptionPane.YES_NO_OPTION);

            if (opt != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Pagamento não confirmado. Vaga NÃO liberada.");
                Historico.registrarFalha("Pagamento não confirmado: ID=" + alvo.id);
                return;
            }

            // Libera vaga
            carros.remove(alvo);
            Persistencia.salvar(carros);
            Historico.registrarSaida(alvo);
            atualizarStatus();
            JOptionPane.showMessageDialog(this, "Vaga liberada com sucesso!");
        });

        // Ação do botão "Gerar Relatório"
        btnRelatorio.addActionListener(e -> {
            Relatorio.gerar(carros);
            JOptionPane.showMessageDialog(this, "Relatório gerado: relatorio.txt");
        });

        // Centraliza e mostra a janela
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Atualiza a área de status com lista de vagas (livres/ocupadas).
     */
    private void atualizarStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s %-36s %-10s %-20s%n", "Vaga", "ID", "Placa", "Dono"));
        sb.append("--------------------------------------------------------------------------------\n");

        for (int i = 1; i <= TOTAL_VAGAS; i++) {
            Carro encontrado = null;
            for (Carro c : carros) {
                if (c.vaga == i) {
                    encontrado = c;
                    break;
                }
            }

            if (encontrado == null) {
                sb.append(String.format("%-6s %-36s %-10s %-20s%n",
                        "[" + i + "]", "-", "-", "[LIVRE]"));
            } else {
                sb.append(String.format("%-6s %-36s %-10s %-20s%n",
                        "[" + i + "]", encontrado.id, encontrado.placa, encontrado.dono));
            }
        }

        areaStatus.setText(sb.toString());
    }
}
