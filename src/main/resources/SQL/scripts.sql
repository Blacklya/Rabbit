create database trabalho_rabbit;

use trabalho_rabbit;

create table pedido (
	id int primary key not null auto_increment,
    cliente varchar(20),
    email varchar(50)
);

create table produtos (
	id int primary key not null auto_increment,
    id_pedido int not null,
    nome varchar(30),
    valor double,
    constraint fk_pedido foreign key (id_pedido)
    references pedido(id)
);

select * from pedido;
select * from produtos;