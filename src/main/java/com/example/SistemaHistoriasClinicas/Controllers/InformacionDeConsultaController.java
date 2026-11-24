package com.example.SistemaHistoriasClinicas.Controllers;

import com.example.SistemaHistoriasClinicas.Models.Consulta;
import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Models.ValorDatoLaboratorio;
import com.example.SistemaHistoriasClinicas.Services.ValorDatoLaboratorioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class InformacionDeConsultaController {

    private static final Logger log = LoggerFactory.getLogger(InformacionDeConsultaController.class);

    private final ValorDatoLaboratorioService valorDatoLaboratorioService;
    private final ApplicationContext applicationContext;

    private Paciente paciente;

    @FXML
    private Label datosDeConsultaValor;

    @FXML
    private Label diagnosticoValor;

    @FXML
    private ListView<ValorDatoLaboratorio> valoresLista;

    public InformacionDeConsultaController(
            ValorDatoLaboratorioService valorDatoLaboratorioService,
            ApplicationContext applicationContext) {
        this.valorDatoLaboratorioService = valorDatoLaboratorioService;
        this.applicationContext = applicationContext;
    }

    @FXML
    private void initialize() {
        valoresLista.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(ValorDatoLaboratorio valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null || valor.getDatoLaboratorio() == null) {
                    setText(null);
                    return;
                }
                String nombre = valor.getDatoLaboratorio().getNombre() == null
                        ? ""
                        : valor.getDatoLaboratorio().getNombre();
                setText(nombre + ": " + formatearValor(valor.getValor()));
            }
        });
    }

    public void setConsulta(Consulta consulta, Paciente paciente) {
        this.paciente = paciente;
        datosDeConsultaValor.setText(textoOVacio(consulta.getDatosDeConsulta()));
        diagnosticoValor.setText(textoOVacio(consulta.getDiagnostico()));
        valoresLista.getItems().setAll(valorDatoLaboratorioService.listarPorConsulta(consulta.getId()));
    }

    @FXML
    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/InformacionPaciente.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent informacionPaciente = loader.load();

            InformacionPacienteController controller = loader.getController();
            controller.setPaciente(paciente);

            Stage ventanaActual = (Stage) datosDeConsultaValor
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(informacionPaciente));
            ventanaActual.setTitle("Información del paciente");
            ventanaActual.setWidth(1220);
            ventanaActual.setHeight(860);
            ventanaActual.setMinWidth(1100);
            ventanaActual.setMinHeight(820);
            ventanaActual.centerOnScreen();
            ventanaActual.show();

        } catch (IOException e) {
            log.error("No se pudo volver a la información del paciente", e);
        }
    }

    private String textoOVacio(String valor) {
        return valor == null ? "" : valor;
    }

    private String formatearValor(Double valor) {
        if (valor == null) {
            return "";
        }
        return BigDecimal.valueOf(valor).stripTrailingZeros().toPlainString();
    }
}
