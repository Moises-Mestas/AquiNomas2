package com.example.servicioventa.service;


import com.example.servicioventa.dto.VentaDTO;
import com.example.servicioventa.entity.Venta;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaService {
    @Transactional
    ResponseEntity<?> guardarVenta(Venta venta);
    //Venta guardarVenta(Venta venta);
    public List<VentaDTO> listar();
//    public Venta guardarVenta(Venta venta);
    //public Venta actualizar(Venta venta);

    @Transactional
    ResponseEntity<?> actualizar(Long id, Venta venta);

    public List<Venta> buscarPorNombreCliente(String nombreCliente);
    public List<Venta> buscarPorFecha(LocalDateTime inicio, LocalDateTime fin);
    public Optional<Venta> listarPorId(Long id);
    public void eliminarPorId(Long id);
}