package com.example.pedido_db.entity;

public enum EstadoPedido {
    CANCELADO, COMPLETADO, INICIADO, PENDIENTE;

    //GET
    //GET /pedidos/filter?estadoPedido=CANCELADO
    //GET /pedidos/filter?estadoPedido=COMPLETADO
    //GET /pedidos/filter?estadoPedido=PENDIENTE

    //GET http://localhost:9000/menus/filterByPriceRange?minPrecio=10.00&maxPrecio=20.00
    //GET http://localhost:9000/pedidos/filter?estadoPedido=INICIADO

    //GET /pedidos/historyByCliente?clienteId=123


}
