package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.BodegaDto;
import com.upeu.servicioreporte.dto.InventarioCocinaDto;
import com.upeu.servicioreporte.dto.InventarioBarraDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "servicio-inventario", fallback = InventarioClientFallback.class)
public interface InventarioClient {
    @GetMapping("/bodega")
    List<BodegaDto> obtenerTodasLasBodegas();

    @GetMapping("/inventarios-cocina")
    List<InventarioCocinaDto> obtenerInventariosCocina();

    @GetMapping("/inventarios-barra")
    List<InventarioBarraDto> obtenerInventariosBarra();
}

