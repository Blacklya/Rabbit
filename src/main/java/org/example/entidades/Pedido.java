package org.example.entidades;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Pedido {
    final public String cliente;
    final public String email;
    final public List<Produto> produtos;

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

    public static Pedido converteDeJSON(JSONObject JSONPedido) {
        List<Produto> produtos = new ArrayList<>();

        JSONArray JSONProdutos = JSONPedido.getJSONArray("produtos");
        for (int i = 0; i < JSONProdutos.length(); i++) {
            produtos.add(new Produto(
                    JSONProdutos.getJSONObject(i).getString("nome"),
                    JSONProdutos.getJSONObject(i).getDouble("valor")
            ));
        }

        return new Pedido(
                JSONPedido.getString("cliente"),
                JSONPedido.getString("email"),
                produtos
        );
    }

    public double totalPedido() {
        double total = 0;
        for (Produto produto : produtos) {
            total += produto.valor;
        }
        return total;
    }

}
