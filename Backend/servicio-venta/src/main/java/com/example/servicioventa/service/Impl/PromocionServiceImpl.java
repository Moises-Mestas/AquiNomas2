package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.PromocionDTO;
import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.repository.PromocionRepository;
import com.example.servicioventa.service.PromocionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository promocionRepository;

    @Autowired
    public PromocionServiceImpl(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    @Transactional
    @Override
    public PromocionDTO crear(PromocionDTO dto) {
        Promocion promocion = new Promocion();

        promocion.setNombre(dto.getNombre());
        promocion.setDescripcion(dto.getDescripcion());
        promocion.setTipoDescuento(Promocion.TipoDescuento.valueOf(dto.getTipoDescuento()));
        promocion.setValorDescuento(dto.getValorDescuento());
        promocion.setCantidadMinima(dto.getCantidadMinima());
        promocion.setMontoMinimo(dto.getMontoMinimo());
        promocion.setFechaInicio(dto.getFechaInicio());
        promocion.setFechaFin(dto.getFechaFin());

        // 👇 Si el DTO ya trae los MenuPromocion listos
        if (dto.getMenu() != null) {
            dto.getMenu().forEach(m -> m.setPromocion(promocion)); // asegúrate que se setee la relación inversa
            promocion.setMenu(dto.getMenu());
        }

        Promocion guardada = promocionRepository.save(promocion);

        // 🔁 Puedes devolver el mismo DTO o actualizar el ID si quieres:
        dto.setId(guardada.getId());
        return dto;
    }


    @Override
    @Transactional
    public Optional<Promocion> actualizar(Long id, PromocionDTO dto) {
        return promocionRepository.findById(id).map(promo -> {
            promo.setNombre(dto.getNombre());
            promo.setDescripcion(dto.getDescripcion());
            promo.setTipoDescuento(Promocion.TipoDescuento.valueOf(dto.getTipoDescuento()));
            promo.setValorDescuento(dto.getValorDescuento());
            promo.setCantidadMinima(dto.getCantidadMinima());
            promo.setMontoMinimo(dto.getMontoMinimo());
            promo.setFechaInicio(dto.getFechaInicio());
            promo.setFechaFin(dto.getFechaFin());

            promo.getMenu().clear();
            promo.getMenu().addAll(dto.getMenu());

            return promocionRepository.save(promo);
        });
    }

    @Override
    public Optional<Promocion> obtenerPorId(Long id) {
        return promocionRepository.findById(id);
    }

    @Override
    public List<Promocion> listarTodas() {
        return promocionRepository.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (!promocionRepository.existsById(id)) {
            throw new NoSuchElementException("Promoción no encontrada con ID: " + id);
        }
        promocionRepository.deleteById(id);
    }

    @Override
    public List<Promocion> buscarPorNombre(String nombre) {
        return promocionRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Promocion> promocionesActivas(LocalDate fecha) {
        return promocionRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(fecha, fecha);
    }

    @Override
    public boolean estaActiva(Long promocionId, LocalDate fecha) {
        Optional<Promocion> promo = promocionRepository.findById(promocionId);
        return promo.filter(p ->
                !fecha.isBefore(p.getFechaInicio()) && !fecha.isAfter(p.getFechaFin())
        ).isPresent();
    }

    @Override
    public List<Promocion> conCantidadMinimaRequerida() {
        return promocionRepository.findAll().stream()
                .filter(p -> p.getCantidadMinima() != null && p.getCantidadMinima() > 0)
                .toList();
    }

    @Override
    public List<Promocion> buscarPorTipo(String tipoDescuento) {
        return promocionRepository.findAll().stream()
                .filter(p -> p.getTipoDescuento().name().equalsIgnoreCase(tipoDescuento))
                .toList();
    }
}
