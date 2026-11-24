package com.example.SistemaHistoriasClinicas;

import com.example.SistemaHistoriasClinicas.Services.DatoLaboratorioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CargaInicialDatoLaboratorio implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CargaInicialDatoLaboratorio.class);

    private final DatoLaboratorioService datoLaboratorioService;

    public CargaInicialDatoLaboratorio(DatoLaboratorioService datoLaboratorioService) {
        this.datoLaboratorioService = datoLaboratorioService;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            datoLaboratorioService.cargarDatosInicialesSiLaTablaEstaVacia();
        } catch (RuntimeException e) {
            log.error("Falló la carga inicial de datos de laboratorio", e);
            throw e;
        }
    }
}
