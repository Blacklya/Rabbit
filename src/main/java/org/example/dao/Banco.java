package org.example.dao;

import org.example.entidades.Pedido;
import org.example.entidades.Produto;

import java.sql.*;

public class Banco {
    private final static String URL = "jdbc:mysql://localhost:3306/trabalho_rabbit";
    private final static String USUARIO = "root";
    private final static String SENHA = "admin";

    private Connection conexao;

    public Banco() {
        try {
            this.conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salvarPedido(Pedido pedido) {
        final String sql = "insert into pedido (cliente, email) values (?, ?)";

        try (final PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pedido.cliente);
            stmt.setString(2, pedido.email);

            stmt.executeUpdate();

            System.out.println("[Banco] Pedido Salvo com sucesso");

            int id = 0;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }

            for (Produto produto : pedido.produtos) {
                salvarProduto(id, produto);
            }

            System.out.println("[Banco] Produtos salvos com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void salvarProduto(int idPedido, Produto produto) {
        final String sql = "insert into produtos (id_pedido, nome, valor) values (?, ?, ?)";

        try (final PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            stmt.setString(2, produto.nome);
            stmt.setDouble(3, produto.valor);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fecharConexao() {
        try {
            conexao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
