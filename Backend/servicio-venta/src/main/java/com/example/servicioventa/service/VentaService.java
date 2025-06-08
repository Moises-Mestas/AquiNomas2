package com.example.servicioventa.service;


import com.example.servicioventa.entity.Venta;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaService {
    public List<Venta> listar();
    public Venta guardarVenta(Venta venta);
    public Venta actualizar(Venta venta);
    public List<Venta> buscarPorNombreCliente(String nombreCliente);
    public List<Venta> buscarPorFecha(LocalDateTime inicio, LocalDateTime fin);
    public Optional<Venta> listarPorId(Long id);
    public void eliminarPorId(Long id);
}