package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.BodegaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "servicio-bodega", fallback = BodegaClientFallback.class)
public interface BodegaClient {

    @GetMapping("/bodegas")
    List<BodegaDto> obtenerTodasLasBodegas();
}
