package com.example.servicioventa.service;



import com.example.servicioventa.dto.PromocionDTO;
import com.example.servicioventa.entity.Promocion;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PromocionService {

    // 🆕 Crear nueva promoción
    //PromocionDTO crear(Promocion promocion);

    @Transactional
    PromocionDTO crear(PromocionDTO dto);

    // 🔁 Actualizar una promoción existente
    Optional<Promocion> actualizar(Long id, PromocionDTO dto);

    // 🔎 Obtener una promoción por ID
    Optional<Promocion> obtenerPorId(Long id);

    // 📄 Listar todas las promociones
    List<Promocion> listarTodas(); // o List<PromocionDTO> si prefieres filtrar

    // 🗑️ Eliminar una promoción
    void eliminar(Long id);

    // 🔍 Buscar por nombre
    List<Promocion> buscarPorNombre(String nombre);

    // 📅 Promociones activas para una fecha
    List<Promocion> promocionesActivas(LocalDate fecha);

    // 📌 Validar si una promoción está activa
    boolean estaActiva(Long promocionId, LocalDate fecha);

    // 📦 Promociones con cantidad mínima de platos
    List<Promocion> conCantidadMinimaRequerida();

    // 📊 Buscar por tipo de descuento
    List<Promocion> buscarPorTipo(String tipoDescuento);
}
