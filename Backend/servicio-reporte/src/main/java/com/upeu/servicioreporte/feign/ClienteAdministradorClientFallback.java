package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.AdministradorDto;
import com.upeu.servicioreporte.dto.ClienteDto;
import org.springframework.stereotype.Component;

@Component
public class ClienteAdministradorClientFallback implements ClienteAdministradorClient {

    @Override
    public ClienteDto obtenerClientePorId(Integer id) {
        System.err.println("Servicio 'servicio-cliente' - Cliente no disponible para id: " + id);
        ClienteDto clienteDto = new ClienteDto();
        // Si tu ClienteDto tiene campo mensaje de error, setéalo aquí, por ejemplo:
        // clienteDto.setMensajeError("Servicio de Cliente no disponible");
        return clienteDto;
    }

    @Override
    public AdministradorDto obtenerAdministradorPorId(Integer id) {
        System.err.println("Servicio 'servicio-cliente' - Administrador no disponible para id: " + id);
        AdministradorDto administradorDto = new AdministradorDto();
        // Si tu AdministradorDto tiene campo mensaje de error, setéalo aquí:
        // administradorDto.setMensajeError("Servicio de Administrador no disponible");
        return administradorDto;
    }
}
