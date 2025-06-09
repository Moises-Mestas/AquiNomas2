package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.VentaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "servicio-venta", fallback = VentaClientFallback.class)
public interface VentaClient {

    @GetMapping("/api/ventas")
    List<VentaDto> obtenerTodasVentas();

    @GetMapping("/api/ventas/por-fecha")
    List<VentaDto> obtenerVentasPorFecha(@RequestParam("inicio") LocalDateTime inicio,
                                         @RequestParam("fin") LocalDateTime fin);
}
