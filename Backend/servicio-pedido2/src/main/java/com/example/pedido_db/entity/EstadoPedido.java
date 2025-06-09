package com.example.pedido_db.entity;

public enum EstadoPedido {
    CANCELADO, COMPLETADO, INICIADO, PENDIENTE;

    //GET /pedidos/filter?estadoPedido=INICIADO
    //GET /pedidos/filter?estadoPedido=CANCELADO
    //GET /pedidos/filter?estadoPedido=COMPLETADO
    //GET /pedidos/filter?estadoPedido=PENDIENTE



    //GET /menu/filterByPriceRange?minPrecio=10.00&maxPrecio=50.00

}
