package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.AdministradorDto;
import com.upeu.servicioreporte.dto.ClienteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-cliente", fallback = ClienteAdministradorClientFallback.class)
public interface ClienteAdministradorClient {

    @GetMapping("/clientes/{id}")
    ClienteDto obtenerClientePorId(@PathVariable Integer id);

    @GetMapping("/administradores/{id}")
    AdministradorDto obtenerAdministradorPorId(@PathVariable Integer id);
}