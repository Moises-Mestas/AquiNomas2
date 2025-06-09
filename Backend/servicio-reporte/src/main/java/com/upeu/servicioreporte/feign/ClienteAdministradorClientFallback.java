package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.AdministradorDto;
import com.upeu.servicioreporte.dto.ClienteDto;
import org.springframework.stereotype.Component;

@Component
public class ClienteAdministradorClientFallback implements ClienteAdministradorClient {

    @Override
    public ClienteDto obtenerClientePorId(Integer id) {
        ClienteDto cliente = new ClienteDto();
        cliente.setId(id);
        cliente.setNombre("Servicio Cliente no disponible");
        cliente.setApellido("");
        cliente.setDni("");
        cliente.setTelefono("");
        cliente.setEmail("");
        cliente.setDireccion("");
        cliente.setFechaRegistro(null);
        return cliente;
    }

    @Override
    public AdministradorDto obtenerAdministradorPorId(Integer id) {
        AdministradorDto admin = new AdministradorDto();
        admin.setId(id);
        admin.setNombre("Servicio Administrador no disponible");
        admin.setEmail("");
        admin.setFechaCreacion(null);
        return admin;
    }
}


