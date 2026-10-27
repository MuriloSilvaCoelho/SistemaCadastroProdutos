package br.com.sistemaprodutos;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Representa um produto do estoque.
 * Os atributos são privados para aplicar encapsulamento.
 */
public class Produto {
    private final String codigo;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidade;

    // Construtor: cria um produto já com seus dados iniciais.
    public Produto(String codigo, String nome, String descricao, BigDecimal preco, int quantidade) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código é obrigatório.");
        }
        validarTexto(nome, "O nome");
        validarPreco(preco);
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        }

        this.codigo = codigo.trim();
        this.nome = nome.trim();
        this.descricao = descricao == null ? "" : descricao.trim();
        this.preco = preco.setScale(2, RoundingMode.HALF_UP);
        this.quantidade = quantidade;
    }

    private static void validarTexto(String texto, String mensagem) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(mensagem + " é obrigatório.");
        }
    }

    private static void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }

   
    public void setNome(String nome) {
        validarTexto(nome, "O nome");
        this.nome = nome.trim();
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao == null ? "" : descricao.trim();
    }

    public void setPreco(BigDecimal preco) {
        validarPreco(preco);
        this.preco = preco.setScale(2, RoundingMode.HALF_UP);
    }

    
    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A entrada deve ser maior que zero.");
        }
        this.quantidade += quantidade;
    }

    
    public void retirarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A retirada deve ser maior que zero.");
        }
        if (quantidade > this.quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente. Disponível: " + this.quantidade);
        }
        this.quantidade -= quantidade;
    }

    
    public BigDecimal calcularValorEstoque() {
        return preco.multiply(BigDecimal.valueOf(quantidade));
    }
}
