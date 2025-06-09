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
        // puedes dejar otros campos nulos o con valores por defecto
        return cliente;
    }

    @Override
    public AdministradorDto obtenerAdministradorPorId(Integer id) {
        AdministradorDto admin = new AdministradorDto();
        admin.setId(id);
        admin.setNombre("Servicio Administrador no disponible");
        // otros campos nulos o por defecto
        return admin;
    }
}


