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

                enviarEmail(pedido);
                System.out.println("Email enviado");
            }, nome -> { });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void enviarEmail(String pedido) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare("email", false, false, false, null);

            channel.basicPublish("", "email", null, pedido.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void salvarPedido(JSONObject JSONPedido) {
        Pedido pedido = Pedido.converteDeJSON(JSONPedido);

        Banco banco = new Banco();
        banco.salvarPedido(pedido);
        banco.fecharConexao();
    }
}
