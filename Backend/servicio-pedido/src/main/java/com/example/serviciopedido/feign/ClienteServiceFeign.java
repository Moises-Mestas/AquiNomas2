package com.example.serviciopedido.feign;

import com.example.serviciopedido.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service")  // Reemplaza la URL con la de tu microservicio
public interface ClienteServiceFeign {

    @GetMapping("/{id}")
    ClienteDTO getClienteById(@PathVariable("id") Integer id);
}
