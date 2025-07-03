package org.example.funcoes;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.entidades.Pedido;
import org.example.entidades.Produto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EnviarPedidos {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        List<Pedido> pedidos = criarPedidos();

        for (Pedido pedido : pedidos) {
            try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
                channel.queueDeclare("pedidos", false, false, false, null);

                channel.basicPublish("", "pedidos", null, pedido.toString().getBytes(StandardCharsets.UTF_8));

                System.out.println("Pedidos enviados");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Pedido> criarPedidos() {
        List<Pedido> pedidos = new ArrayList<>();

        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("Caneta", 12.99));
        produtos.add(new Produto("Lápis", 5.5));
        produtos.add(new Produto("Borracha", 10));

        pedidos.add(new Pedido("Maria", "maria.pn@aluno.ifsc.edu.br", produtos));

        List<Produto> produtos2 = new ArrayList<>();
        produtos2.add(new Produto("Calça", 99.99));
        produtos2.add(new Produto("Camisa", 40));
        produtos2.add(new Produto("Tênis", 120.5));

        pedidos.add(new Pedido("Fulano", "maria.pn@aluno.ifsc.edu.br", produtos2));

        return pedidos;
    }
}
