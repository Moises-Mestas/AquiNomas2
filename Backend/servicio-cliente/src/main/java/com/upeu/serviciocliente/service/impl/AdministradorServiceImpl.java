package com.upeu.serviciocliente.service.impl;

import com.upeu.serviciocliente.entity.Administrador;
import com.upeu.serviciocliente.repository.AdministradorRepository;
import com.upeu.serviciocliente.service.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;

    @Override
    public Administrador saveAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    @Override
    public Administrador updateAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    @Override
    public void deleteAdministrador(Integer id) {
        administradorRepository.deleteById(id);
    }

    @Override
    public Optional<Administrador> getAdministradorById(Integer id) {
        return administradorRepository.findById(id);
    }

    @Override
    public List<Administrador> getAllAdministradores() {
        return administradorRepository.findAll();
    }

    @Override
    public List<Administrador> findByNombre(String nombre) {
        return administradorRepository.findByNombreContainingIgnoreCase(nombre);
    }
}