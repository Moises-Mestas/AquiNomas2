package com.example.servicioventa.mapper;

import com.example.servicioventa.dto.ClienteDTO;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class ClienteMapper {

    public ClienteDTO aDTO(ClienteDTO entidad) {
        if (entidad == null) return null;

        ClienteDTO dto = new ClienteDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setDni(entidad.getDni());
        dto.setDireccion(entidad.getDireccion());
        dto.setTelefono(entidad.getTelefono());
        dto.setEmail(entidad.getEmail());
        dto.setRuc(entidad.getRuc());

        if (entidad.getFechaRegistro() != null) {
            dto.setFechaRegistro(entidad.getFechaRegistro().toInstant().atOffset(ZoneOffset.of("-05:00")));
        }

        return dto;
    }
}