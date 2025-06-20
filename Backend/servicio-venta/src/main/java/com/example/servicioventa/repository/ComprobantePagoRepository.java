package com.example.servicioventa.repository;

import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {
    Optional<ComprobantePago> findById(Integer id);
    //List<ComprobantePago> findByTipoComprobante(String tipoComprobante);
    List<ComprobantePago> findByFechaEmisionBetween(LocalDateTime inicio, LocalDateTime fin);
    List<ComprobantePago> findByTipo(ComprobantePago.TipoComprobante tipo);
    List<ComprobantePago> findByVenta(Venta venta);
    Venta venta(Venta venta);
}
