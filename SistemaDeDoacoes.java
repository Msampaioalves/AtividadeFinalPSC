import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SistemaDeDoacoes {
    private List<Doacao> doacoes;
    private static final String ARQUIVO = "doacoes.txt";

    public SistemaDeDoacoes() {
        doacoes = new ArrayList<>();
        carregarDoacoes();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SistemaDeDoacoes::criarInterface);
    }

    private void adicionarDoacao(Doacao doacao) {
        doacoes.add(doacao);
        salvarDoacoes();
    }

    private double calcularTotalDoacoes() {
        return doacoes.stream().mapToDouble(Doacao::getQuantidade).sum();
    }

    private void salvarDoacoes() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ARQUIVO))) {
            for (Doacao doacao : doacoes) {
                writer.write(doacao.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarDoacoes() {
        doacoes.clear();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                doacoes.add(Doacao.fromString(linha));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void criarInterface() {
        JFrame frame = new JFrame("Sistema de Gerenciamento de Doações");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        SistemaDeDoacoes sistema = new SistemaDeDoacoes();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel lblTipo = new JLabel("Tipo de Doação:");
        JTextField txtTipo = new JTextField();
        JLabel lblQuantidade = new JLabel("Quantidade:");
        JTextField txtQuantidade = new JTextField();
        JLabel lblData = new JLabel("Data (yyyy-mm-dd):");
        JTextField txtData = new JTextField();

        JButton btnAdicionar = new JButton("Adicionar Doação");
        JButton btnTotal = new JButton("Calcular Total");

        panel.add(lblTipo);
        panel.add(txtTipo);
        panel.add(lblQuantidade);
        panel.add(txtQuantidade);
        panel.add(lblData);
        panel.add(txtData);
        panel.add(btnAdicionar);
        panel.add(btnTotal);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

        btnAdicionar.addActionListener((ActionEvent e) -> {
            try {
                String tipo = txtTipo.getText();
                double quantidade = Double.parseDouble(txtQuantidade.getText());
                LocalDate data = LocalDate.parse(txtData.getText());
                Doacao doacao = new Doacao(tipo, quantidade, data);
                sistema.adicionarDoacao(doacao);
                JOptionPane.showMessageDialog(frame, "Doação adicionada com sucesso!");
                txtTipo.setText("");
                txtQuantidade.setText("");
                txtData.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao adicionar doação: " + ex.getMessage());
            }
        });

        btnTotal.addActionListener((ActionEvent e) -> {
            double total = sistema.calcularTotalDoacoes();
            JOptionPane.showMessageDialog(frame, "Total de Doações: " + total);
        });
    }

    // Classe interna para representar uma Doação
    static class Doacao {
        private String tipo;
        private double quantidade;
        private LocalDate data;

        public Doacao(String tipo, double quantidade, LocalDate data) {
            this.tipo = tipo;
            this.quantidade = quantidade;
            this.data = data;
        }

        public String getTipo() {
            return tipo;
        }

        public double getQuantidade() {
            return quantidade;
        }

        public LocalDate getData() {
            return data;
        }

        @Override
        public String toString() {
            return tipo + ";" + quantidade + ";" + data;
        }

        public static Doacao fromString(String linha) {
            String[] partes = linha.split(";");
            String tipo = partes[0];
            double quantidade = Double.parseDouble(partes[1]);
            LocalDate data = LocalDate.parse(partes[2]);
            return new Doacao(tipo, quantidade, data);
        }
    }
}
