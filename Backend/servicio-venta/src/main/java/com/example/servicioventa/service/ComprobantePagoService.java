package com.example.servicioventa.service;


import com.example.servicioventa.entity.ComprobantePago;

import java.util.List;
import java.util.Optional;

public interface ComprobantePagoService {
    public List<ComprobantePago> listar();
    public ComprobantePago guardarComprobante(ComprobantePago comprobantePago);

    ComprobantePago guardar(ComprobantePago comprobantePago);

    public ComprobantePago actualizar(ComprobantePago comprobantePago);
    public Optional<ComprobantePago> listarPorId(Integer id);
    public void eliminarPorId(Integer id);
}
