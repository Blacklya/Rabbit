package org.example.funcoes;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.dao.Banco;
import org.example.entidades.Pedido;
import org.example.entidades.Produto;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class EnviarEmail {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare("email", false, false, false, null);

            System.out.println("Aguardando emails");

            channel.basicConsume("email", true, (nome, envio) -> {
                String pedido = new String(envio.getBody(), StandardCharsets.UTF_8);

                enviar(new JSONObject(pedido));
            }, nome -> { });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void enviar(JSONObject JSONPedido) {
        Pedido pedido = Pedido.converteDeJSON(JSONPedido);

        StringBuilder conteudo = new StringBuilder("Caro cliente " + pedido.cliente + ",\n"
                + "Seu pedido foi recebido com sucesso\n"
                + "O valor total é de " + String.format("%.2f", pedido.totalPedido()) + "\n\n"
                + "---- Itens ----\n");

        for (Produto produto : pedido.produtos) {
            conteudo.append(produto.nome).append(": ").append(produto.valor).append("\n");
        }

        System.out.println("Email enviado com sucesso para o endereço: " + pedido.email);
        System.out.println("Conteudo: " + conteudo);
    }
}
