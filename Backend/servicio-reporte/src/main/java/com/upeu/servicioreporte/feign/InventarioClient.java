package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.InventarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "servicio-inventario", fallback = InventarioClientFallback.class)
public interface InventarioClient {

    @GetMapping("/inventarios")
    List<InventarioDto> obtenerTodosInventarios();
}
