package com.example.servicioventa.controller;

import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.service.ComprobantePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comprobantes")
public class ComprobantePagoController {

    @Autowired
    private  ComprobantePagoService comprobantePagoService;

    @GetMapping
    public List<ComprobantePago> listar() {
        return comprobantePagoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprobantePago> listarPorId(@PathVariable Integer id) {
        Optional<ComprobantePago> comprobante = comprobantePagoService.listarPorId(id);
        return comprobante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ComprobantePago guardar(@RequestBody ComprobantePago comprobantePago) {
        return comprobantePagoService.guardar(comprobantePago);
    }

    @PutMapping
    public ComprobantePago actualizar(@RequestBody ComprobantePago comprobantePago) {
        return comprobantePagoService.actualizar(comprobantePago);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        comprobantePagoService.eliminarPorId(id);
    }
}