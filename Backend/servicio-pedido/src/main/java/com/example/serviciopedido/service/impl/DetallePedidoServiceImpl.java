package com.example.serviciopedido.service.impl;

import com.example.serviciopedido.dto.ClienteDTO;
import com.example.serviciopedido.dto.DetallePedidoDTO;
import com.example.serviciopedido.feign.ClienteFeignClient;
import com.example.serviciopedido.repository.DetallePedidoRepository;
import com.example.serviciopedido.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final ClienteFeignClient clienteFeignClient;

    @Autowired
    public DetallePedidoServiceImpl(DetallePedidoRepository detallePedidoRepository, ClienteFeignClient clienteFeignClient) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.clienteFeignClient = clienteFeignClient;
    }

    @Override
    public List<DetallePedidoDTO> listar() {
        // Obtener todos los detalles de pedido desde la base de datos
        List<DetallePedidoDTO> detalles = detallePedidoRepository.findAll().stream()
                .map(detalle -> {
                    // Usar Feign para obtener el cliente asociado a este detalle
                    ClienteDTO cliente = clienteFeignClient.getClienteById(detalle.getCliente().getId());
                    // Convertir a DTO y agregar el cliente
                    DetallePedidoDTO detalleDTO = new DetallePedidoDTO(
                            detalle.getId(),
                            cliente,
                            detalle.getMenu(),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario()
                    );
                    return detalleDTO;
                })
                .collect(Collectors.toList());

        return detalles;
    }

    @Override
    public Optional<DetallePedidoDTO> listarPorId(Integer id) {
        Optional<DetallePedidoDTO> detalleOpt = detallePedidoRepository.findById(id).map(detalle -> {
            // Usar Feign para obtener el cliente asociado al detalle
            ClienteDTO cliente = clienteFeignClient.getClienteById(detalle.getCliente().getId());
            // Convertir a DTO y agregar el cliente
            DetallePedidoDTO detalleDTO = new DetallePedidoDTO(
                    detalle.getId(),
                    cliente,
                    detalle.getMenu(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario()
            );
            return detalleDTO;
        });

        return detalleOpt;
    }

    @Override
    public DetallePedidoDTO guardar(DetallePedidoDTO detallePedidoDTO) {
        // Convertir DTO a entidad para guardarlo en la base de datos
        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setCliente(detallePedidoDTO.getCliente());
        detallePedido.setMenu(detallePedidoDTO.getMenu());
        detallePedido.setCantidad(detallePedidoDTO.getCantidad());
        detallePedido.setPrecioUnitario(detallePedidoDTO.getPrecioUnitario());

        DetallePedido savedDetalle = detallePedidoRepository.save(detallePedido);

        // Convertir la entidad guardada nuevamente a DTO para devolver
        return new DetallePedidoDTO(
                savedDetalle.getId(),
                savedDetalle.getCliente(),
                savedDetalle.getMenu(),
                savedDetalle.getCantidad(),
                savedDetalle.getPrecioUnitario()
        );
    }

    @Override
    public DetallePedidoDTO actualizar(DetallePedidoDTO detallePedidoDTO) {
        // Convertir DTO a entidad para actualizarlo en la base de datos
        DetallePedido detallePedido = detallePedidoRepository.findById(detallePedidoDTO.getId())
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado"));
        detallePedido.setCliente(detallePedidoDTO.getCliente());
        detallePedido.setMenu(detallePedidoDTO.getMenu());
        detallePedido.setCantidad(detallePedidoDTO.getCantidad());
        detallePedido.setPrecioUnitario(detallePedidoDTO.getPrecioUnitario());

        DetallePedido updatedDetalle = detallePedidoRepository.save(detallePedido);

        // Convertir la entidad actualizada a DTO para devolver
        return new DetallePedidoDTO(
                updatedDetalle.getId(),
                updatedDetalle.getCliente(),
                updatedDetalle.getMenu(),
                updatedDetalle.getCantidad(),
                updatedDetalle.getPrecioUnitario()
        );
    }

    @Override
    public void eliminar(Integer id) {
        // Eliminar el detalle de pedido por ID
        detallePedidoRepository.deleteById(id);
    }
}
