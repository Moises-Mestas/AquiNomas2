package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.ClienteDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-cliente", fallback = ClienteClientFallback.class)
public interface ClienteClient {

    @GetMapping("/clientes/{id}")
    ClienteDto obtenerClientePorId(@PathVariable Integer id);
}
