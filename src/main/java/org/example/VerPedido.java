package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class VerPedido {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare("pedidos", false, false, false, null);

            System.out.println("[Visualizador] Aguardando pedidos");

            channel.basicConsume("pedidos", true, (nome, envio) -> {
                String pedido = new String(envio.getBody(), StandardCharsets.UTF_8);
                System.out.println("[Visualizador] Pedido recebido:\n" + pedido);
            }, nome -> { });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
