package org.example.entidades;

import org.json.JSONObject;

public class Produto {
    final public String nome;
    final public double valor;

    public Produto(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public JSONObject toJSON() {
        return new JSONObject()
            .put("nome", nome)
            .put("valor", valor);
    }
}
