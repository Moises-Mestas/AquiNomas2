package com.example.servicioventa.mapper;

import com.example.servicioventa.dto.MenuDTO;
import com.example.servicioventa.dto.MenuRequeridoDTO;
import com.example.servicioventa.dto.PromocionDTO;
import com.example.servicioventa.entity.MenuPromocion;
import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.feing.PedidoClient;

import java.util.List;
import java.util.stream.Collectors;

public class PromocionMapper {

    // Conversión de DTO a entidad
    public static Promocion toEntity(PromocionDTO dto) {
        Promocion promocion = new Promocion();
        promocion.setNombre(dto.getNombre());
        promocion.setDescripcion(dto.getDescripcion());
        promocion.setTipoDescuento(Promocion.TipoDescuento.valueOf(dto.getTipoDescuento()));
        promocion.setValorDescuento(dto.getValorDescuento());
        promocion.setCantidadMinima(dto.getCantidadMinima());
        promocion.setMontoMinimo(dto.getMontoMinimo());
        promocion.setFechaInicio(dto.getFechaInicio());
        promocion.setFechaFin(dto.getFechaFin());

        if (dto.getMenuRequerido() != null) {
            List<MenuPromocion> menuList = dto.getMenuRequerido().stream().map(req -> {
                MenuPromocion mp = new MenuPromocion();
                mp.setMenuId(req.getMenuId());
                mp.setCantidadRequerida(req.getCantidadRequerida());
                mp.setPromocion(promocion);
                return mp;
            }).collect(Collectors.toList());
            promocion.setMenu(menuList);
        }
        return promocion;
    }

    // Conversión de entidad a DTO enriquecido
    public static PromocionDTO toDTO(Promocion promocion, PedidoClient pedidoClient) {
        PromocionDTO dto = new PromocionDTO();
        dto.setId(promocion.getId());
        dto.setNombre(promocion.getNombre());
        dto.setDescripcion(promocion.getDescripcion());
        dto.setTipoDescuento(promocion.getTipoDescuento().name());
        dto.setValorDescuento(promocion.getValorDescuento());
        dto.setCantidadMinima(promocion.getCantidadMinima());
        dto.setMontoMinimo(promocion.getMontoMinimo());
        dto.setFechaInicio(promocion.getFechaInicio());
        dto.setFechaFin(promocion.getFechaFin());

        if (promocion.getMenu() != null) {
            List<MenuRequeridoDTO> menuDTOs = promocion.getMenu().stream().map(m -> {
                // Depuración: imprime los valores de la entidad
                System.out.println("Mapping MenuPromocion: id=" + m.getId() +
                        ", menuId=" + m.getMenuId() +
                        ", cantidadRequerida=" + m.getCantidadRequerida());

                // Invoca el Feign client para obtener la información completa del menú
                MenuDTO completo = pedidoClient.obtenerMenuPorId(m.getMenuId());

                MenuRequeridoDTO dtoMenu = new MenuRequeridoDTO();
                // Si el campo id de la entidad es Long y en el DTO se define como Integer:
                dtoMenu.setId(m.getId() != null ? m.getId().intValue() : null);

                if (completo != null && completo.getId() != null) {
                    dtoMenu.setMenuId(completo.getId());
                    dtoMenu.setNombre(completo.getNombre());
                    dtoMenu.setDescripcion(completo.getDescripcion());
                    dtoMenu.setPrecio(completo.getPrecio());
                    dtoMenu.setImagen(completo.getImagen());
                } else {
                    // Si no se obtuvo datos, se usa el valor persistido
                    dtoMenu.setMenuId(m.getMenuId());
                }
                dtoMenu.setCantidadRequerida(m.getCantidadRequerida());
                return dtoMenu;
            }).collect(Collectors.toList());
            dto.setMenuRequerido(menuDTOs);
        } else {
            dto.setMenuRequerido(null);
        }
        return dto;
    }
}