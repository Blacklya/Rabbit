package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class CriarPedido {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare("pedidos", false, false, false, null);

            String pedido = "Pedido da Maria";

            channel.basicPublish("", "pedidos", null, pedido.getBytes(StandardCharsets.UTF_8));

            System.out.println("[Criador] Pedido enviado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
