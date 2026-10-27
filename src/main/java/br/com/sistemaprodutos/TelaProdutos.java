package br.com.sistemaprodutos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 */
public class TelaProdutos extends JFrame {
    private final CadastroProdutos cadastro = new CadastroProdutos();

    private final JTextField campoCodigo = new JTextField(10);
    private final JTextField campoNome = new JTextField(18);
    private final JTextField campoDescricao = new JTextField(18);
    private final JTextField campoPreco = new JTextField(10);
    private final JTextField campoQuantidade = new JTextField(6);
    private final JTextField campoMovimentacao = new JTextField(6);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Código", "Nome", "Descrição", "Preço", "Qtd.", "Valor em estoque"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; 
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final JLabel rotuloTotal = new JLabel("Valor total do estoque: R$ 0,00");
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public TelaProdutos() {
        super("Sistema de Cadastro e Gerenciamento de Produtos");
        configurarJanela();
        montarInterface();
        atualizarTabela();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setMinimumSize(new Dimension(850, 500));
        setLocationRelativeTo(null);
    }

    private void montarInterface() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(principal);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Dados do produto"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        adicionarCampo(formulario, c, 0, 0, "Código:", campoCodigo);
        adicionarCampo(formulario, c, 2, 0, "Nome:", campoNome);
        adicionarCampo(formulario, c, 0, 1, "Descrição:", campoDescricao);
        adicionarCampo(formulario, c, 2, 1, "Preço (R$):", campoPreco);
        adicionarCampo(formulario, c, 0, 2, "Quantidade inicial:", campoQuantidade);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoCadastrar = new JButton("Cadastrar");
        JButton botaoConsultar = new JButton("Consultar código");
        JButton botaoAtualizar = new JButton("Atualizar dados");
        JButton botaoExcluir = new JButton("Excluir");
        botoes.add(botaoCadastrar);
        botoes.add(botaoConsultar);
        botoes.add(botaoAtualizar);
        botoes.add(botaoExcluir);

        botaoCadastrar.addActionListener(e -> cadastrarProduto());
        botaoConsultar.addActionListener(e -> consultarProduto());
        botaoAtualizar.addActionListener(e -> atualizarProduto());
        botaoExcluir.addActionListener(e -> excluirProduto());

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(formulario, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);
        principal.add(topo, BorderLayout.NORTH);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setAutoCreateRowSorter(true);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                preencherCamposPelaSelecao();
            }
        });
        principal.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout(8, 8));
        JPanel estoque = new JPanel(new FlowLayout(FlowLayout.LEFT));
        estoque.setBorder(BorderFactory.createTitledBorder("Movimentação de estoque"));
        estoque.add(new JLabel("Quantidade:"));
        estoque.add(campoMovimentacao);
        JButton botaoEntrada = new JButton("Registrar entrada");
        JButton botaoSaida = new JButton("Registrar retirada");
        estoque.add(botaoEntrada);
        estoque.add(botaoSaida);
        botaoEntrada.addActionListener(e -> movimentarEstoque(true));
        botaoSaida.addActionListener(e -> movimentarEstoque(false));

        rotuloTotal.setFont(rotuloTotal.getFont().deriveFont(Font.BOLD, 15f));
        rodape.add(estoque, BorderLayout.CENTER);
        rodape.add(rotuloTotal, BorderLayout.SOUTH);
        principal.add(rodape, BorderLayout.SOUTH);
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints c, int x, int y,
                                String rotulo, JTextField campo) {
        c.gridx = x;
        c.gridy = y;
        c.weightx = 0;
        painel.add(new JLabel(rotulo), c);
        c.gridx = x + 1;
        c.weightx = 1;
        painel.add(campo, c);
    }

    private void cadastrarProduto() {
        try {
            Produto produto = new Produto(
                    campoCodigo.getText(),
                    campoNome.getText(),
                    campoDescricao.getText(),
                    lerPreco(),
                    lerQuantidadeInicial()
            );
            cadastro.cadastrar(produto);
            atualizarTabela();
            limparCampos();
            mensagem("Produto cadastrado com sucesso.");
        } catch (IllegalArgumentException ex) {
            erro(ex.getMessage());
        }
    }

    private void consultarProduto() {
        Produto produto = cadastro.buscarPorCodigo(campoCodigo.getText());
        if (produto == null) {
            erro("Produto não encontrado.");
            return;
        }
        preencherCampos(produto);
        selecionarLinhaPorCodigo(produto.getCodigo());
    }

    private void atualizarProduto() {
        try {
            cadastro.atualizar(campoCodigo.getText(), campoNome.getText(),
                    campoDescricao.getText(), lerPreco());
            atualizarTabela();
            mensagem("Dados do produto atualizados.");
        } catch (IllegalArgumentException ex) {
            erro(ex.getMessage());
        }
    }

    private void excluirProduto() {
        try {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente excluir o produto " + campoCodigo.getText() + "?",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
            if (resposta != JOptionPane.YES_OPTION) return;
            cadastro.excluir(campoCodigo.getText());
            atualizarTabela();
            limparCampos();
            mensagem("Produto excluído.");
        } catch (IllegalArgumentException ex) {
            erro(ex.getMessage());
        }
    }

    private void movimentarEstoque(boolean entrada) {
        try {
            int quantidade = Integer.parseInt(campoMovimentacao.getText().trim());
            if (entrada) {
                cadastro.adicionarEstoque(campoCodigo.getText(), quantidade);
            } else {
                cadastro.retirarEstoque(campoCodigo.getText(), quantidade);
            }
            atualizarTabela();
            campoMovimentacao.setText("");
            mensagem(entrada ? "Entrada registrada." : "Retirada registrada.");
        } catch (NumberFormatException ex) {
            erro("Informe um número inteiro válido para a movimentação.");
        } catch (IllegalArgumentException ex) {
            erro(ex.getMessage());
        }
    }

    private BigDecimal lerPreco() {
        String texto = campoPreco.getText().trim().replace("R$", "").replace(" ", "");
        if (texto.contains(",") && texto.contains(".")) {
            texto = texto.replace(".", "").replace(",", ".");
        } else {
            texto = texto.replace(",", ".");
        }
        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Informe um preço válido, por exemplo 12,50.");
        }
    }

    private int lerQuantidadeInicial() {
        try {
            return Integer.parseInt(campoQuantidade.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Informe uma quantidade inteira válida.");
        }
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        for (Produto p : cadastro.listar()) {
            modelo.addRow(new Object[]{
                    p.getCodigo(), p.getNome(), p.getDescricao(),
                    moeda.format(p.getPreco()), p.getQuantidade(),
                    moeda.format(p.calcularValorEstoque())
            });
        }
        rotuloTotal.setText("Valor total do estoque: " + moeda.format(cadastro.calcularValorTotalEstoque()));
    }

    private void preencherCamposPelaSelecao() {
        int linhaVisual = tabela.getSelectedRow();
        if (linhaVisual < 0) return;
        int linhaModelo = tabela.convertRowIndexToModel(linhaVisual);
        String codigo = modelo.getValueAt(linhaModelo, 0).toString();
        Produto produto = cadastro.buscarPorCodigo(codigo);
        if (produto != null) preencherCampos(produto);
    }

    private void preencherCampos(Produto p) {
        campoCodigo.setText(p.getCodigo());
        campoNome.setText(p.getNome());
        campoDescricao.setText(p.getDescricao());
        campoPreco.setText(p.getPreco().toPlainString().replace(".", ","));
        campoQuantidade.setText(String.valueOf(p.getQuantidade()));
    }

    private void selecionarLinhaPorCodigo(String codigo) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).toString().equalsIgnoreCase(codigo)) {
                int linhaVisual = tabela.convertRowIndexToView(i);
                tabela.setRowSelectionInterval(linhaVisual, linhaVisual);
                return;
            }
        }
    }

    private void limparCampos() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoDescricao.setText("");
        campoPreco.setText("");
        campoQuantidade.setText("");
        campoMovimentacao.setText("");
        tabela.clearSelection();
        campoCodigo.requestFocus();
    }

    private void mensagem(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Sistema de produtos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void erro(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Atenção", JOptionPane.ERROR_MESSAGE);
    }
}
