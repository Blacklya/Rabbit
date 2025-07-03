package org.example.funcoes;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.dao.Banco;
import org.example.entidades.Pedido;
import org.example.entidades.Produto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SalvarPedidos {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare("pedidos", false, false, false, null);

            System.out.println("Aguardando pedidos");

            channel.basicConsume("pedidos", true, (nome, envio) -> {
                String pedido = new String(envio.getBody(), StandardCharsets.UTF_8);
                System.out.println("Pedido recebido");

                salvarPedido(new JSONObject(pedido));

                System.out.println("Pedido salvo");
            }, nome -> { });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void salvarPedido(JSONObject JSONPedido) {
        Pedido pedido = converteJSONPedido(JSONPedido);

        Banco banco = new Banco();
        banco.salvarPedido(pedido);
        banco.fecharConexao();
    }

    private static Pedido converteJSONPedido(JSONObject JSONPedido) {
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
}
