package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.AdministradorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-cliente", fallback =AdministradorClientFallback.class)
public interface AdministradorClient {

    @GetMapping("/administradores/{id}")
    AdministradorDto obtenerAdministradorPorId(@PathVariable Integer id);
}
