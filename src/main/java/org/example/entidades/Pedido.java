package org.example.entidades;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Pedido {
    final public String cliente;
    final public String email;
    final List<Produto> produtos;

    public Pedido(String cliente, String email, List<Produto> produtos) {
        this.cliente = cliente;
        this.email = email;
        this.produtos = produtos;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("cliente", cliente)
                .put("email", email)
                .put("produtos", JSONProdutos())
                .toString();
    }

    private JSONArray JSONProdutos() {
        JSONArray produtos = new JSONArray();
        for (Produto produto : this.produtos) {
            produtos.put(produto.toJSON());
        }
        return produtos;
    }

}
