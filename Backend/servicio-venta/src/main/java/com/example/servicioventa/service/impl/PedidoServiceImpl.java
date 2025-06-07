package com.example.servicioventa.service.impl;

import com.example.servicioventa.dto.Pedido;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PedidoServiceImpl {

    @Autowired
    private LoadBalancerClient loadBalancerClient; // 🔄 Obtiene instancia dinámica

    @Autowired
    private RestTemplate restTemplate;

    private static final String CIRCUIT_BREAKER_NAME = "pedidoCircuitBreaker";

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackObtenerPedidoPorId")
    public Pedido obtenerPedidoPorId(Integer pedido_id) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("servicio-pedido"); // 🌎 Obtiene instancia de Eureka

        if (serviceInstance == null) {
            System.out.println("⚠️ No se encontraron instancias de servicio-pedido en Eureka.");
            return fallbackObtenerPedidoPorId(pedido_id, new RuntimeException("Servicio pedido no disponible"));
        }

        String url = serviceInstance.getUri() + "/pedidos/" + pedido_id;
        System.out.println("🔍 URL dinámica generada: " + url);

        try {
            return restTemplate.getForObject(url, Pedido.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener pedido con ID " + pedido_id + ": " + e.getMessage());
        }
    }

    // Metodo Fallback cuando el servicio no está disponible
    public Pedido fallbackObtenerPedidoPorId(Integer pedido_id, Throwable t) {
        System.out.println("⚠️ Fallback activado - Pedido ID: " + pedido_id + " - Causa: " + t.getMessage());

        Pedido pedido = new Pedido();
        pedido.setId(pedido_id);
        pedido.setEstadoPedido(Pedido.EstadoPedido.valueOf("Información no disponible"));
        return pedido;
    }


}