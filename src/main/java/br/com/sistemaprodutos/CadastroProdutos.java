package br.com.sistemaprodutos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 
 */
public class CadastroProdutos {
    private final ArrayList<Produto> produtos = new ArrayList<>();

    public void cadastrar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto inválido.");
        }
        if (buscarPorCodigo(produto.getCodigo()) != null) {
            throw new IllegalArgumentException("Já existe um produto com esse código.");
        }
        produtos.add(produto);
    }

    public Produto buscarPorCodigo(String codigo) {
        if (codigo == null) return null;
        for (Produto produto : produtos) {
            if (produto.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return produto;
            }
        }
        return null;
    }

    public List<Produto> listar() {
 
        return Collections.unmodifiableList(new ArrayList<>(produtos));
    }

    public void atualizar(String codigo, String nome, String descricao, BigDecimal preco) {
        Produto produto = exigirProduto(codigo);
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
    }

    public void excluir(String codigo) {
        Produto produto = exigirProduto(codigo);
        produtos.remove(produto);
    }

    public void adicionarEstoque(String codigo, int quantidade) {
        exigirProduto(codigo).adicionarEstoque(quantidade);
    }

    public void retirarEstoque(String codigo, int quantidade) {
        exigirProduto(codigo).retirarEstoque(quantidade);
    }

    public BigDecimal calcularValorTotalEstoque() {
        BigDecimal total = BigDecimal.ZERO;
        for (Produto produto : produtos) {
            total = total.add(produto.calcularValorEstoque());
        }
        return total;
    }

    private Produto exigirProduto(String codigo) {
        Produto produto = buscarPorCodigo(codigo);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }
        return produto;
    }
}
