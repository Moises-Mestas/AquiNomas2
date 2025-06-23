package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.PromocionDTO;
import com.example.servicioventa.entity.MenuPromocion;
import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.feing.PedidoClient;
import com.example.servicioventa.mapper.PromocionMapper;
import com.example.servicioventa.repository.PromocionRepository;
import com.example.servicioventa.service.PromocionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository promocionRepository;
    private final PedidoClient pedidoClient;

    @Autowired
    public PromocionServiceImpl(PromocionRepository promocionRepository, @Qualifier("com.example.servicioventa.feing.PedidoClient") PedidoClient pedidoClient) {
        this.promocionRepository = promocionRepository;
        this.pedidoClient = pedidoClient;
    }

    @Override
    @Transactional
    public PromocionDTO crear(PromocionDTO dto) {
        // Conversión de DTO a entidad y guardado
        Promocion promocion = PromocionMapper.toEntity(dto);
        Promocion guardada = promocionRepository.save(promocion);
        // Conversión de entidad a DTO enriquecido usando el Feign client
        return PromocionMapper.toDTO(guardada, pedidoClient);
    }

    @Override
    @Transactional
    public PromocionDTO actualizar(PromocionDTO dto, Long id) {
        Promocion promo = promocionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Promoción no encontrada con ID: " + id));

        // Actualiza los campos básicos
        promo.setNombre(dto.getNombre());
        promo.setDescripcion(dto.getDescripcion());
        promo.setTipoDescuento(Promocion.TipoDescuento.valueOf(dto.getTipoDescuento()));
        promo.setValorDescuento(dto.getValorDescuento());
        promo.setCantidadMinima(dto.getCantidadMinima());
        promo.setMontoMinimo(dto.getMontoMinimo());
        promo.setFechaInicio(dto.getFechaInicio());
        promo.setFechaFin(dto.getFechaFin());

        // Actualiza la colección de menús
        promo.getMenu().clear();
        if (dto.getMenuRequerido() != null && !dto.getMenuRequerido().isEmpty()) {
            promo.getMenu().addAll(
                    dto.getMenuRequerido().stream().map(req -> {
                        var mp = new com.example.servicioventa.entity.MenuPromocion();
                        mp.setMenuId(req.getMenuId());
                        mp.setCantidadRequerida(req.getCantidadRequerida());
                        mp.setPromocion(promo);
                        return mp;
                    }).toList()
            );
        }
        Promocion guardada = promocionRepository.save(promo);
        return PromocionMapper.toDTO(guardada, pedidoClient);
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

    @Override
    public PromocionDTO toDTO(Promocion promocion) {
        return PromocionMapper.toDTO(promocion, pedidoClient);
    }

}
