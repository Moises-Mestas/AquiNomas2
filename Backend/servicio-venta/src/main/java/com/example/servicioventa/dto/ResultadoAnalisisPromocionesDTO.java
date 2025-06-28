package com.example.servicioventa.dto;

import com.example.servicioventa.entity.Promocion;

import java.util.List;

public class ResultadoAnalisisPromocionesDTO {
    private List<Promocion> promocionesAplicables;
    private List<String> promocionesRechazadas;

    public ResultadoAnalisisPromocionesDTO(List<Promocion> aplicables, List<String> rechazadas) {
        this.promocionesAplicables = aplicables;
        this.promocionesRechazadas = rechazadas;
    }

    public boolean isVacio() {
        return (promocionesAplicables == null || promocionesAplicables.isEmpty()) &&
                (promocionesRechazadas == null || promocionesRechazadas.isEmpty());
    }

    public String getMensajeResumen() {
        if (isVacio()) return "⚠️ No se encontraron promociones aplicables ni rechazadas.";
        if (!promocionesAplicables.isEmpty() && promocionesRechazadas.isEmpty())
            return "🎉 Todas las promociones se aplican.";
        if (!promocionesRechazadas.isEmpty() && promocionesAplicables.isEmpty())
            return "❌ Ninguna promoción aplica a este pedido.";
        return "ℹ️ Algunas promociones aplican, otras fueron rechazadas.";
    }

    // getters y setters

    public List<Promocion> getPromocionesAplicables() {
        return promocionesAplicables;
    }

    public void setPromocionesAplicables(List<Promocion> promocionesAplicables) {
        this.promocionesAplicables = promocionesAplicables;
    }

    public List<String> getPromocionesRechazadas() {
        return promocionesRechazadas;
    }

    public void setPromocionesRechazadas(List<String> promocionesRechazadas) {
        this.promocionesRechazadas = promocionesRechazadas;
    }
}

