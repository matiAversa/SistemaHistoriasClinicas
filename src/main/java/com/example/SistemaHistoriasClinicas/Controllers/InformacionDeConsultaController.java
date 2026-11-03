package com.example.SistemaHistoriasClinicas.Controllers;

import com.example.SistemaHistoriasClinicas.Models.Consulta;
import com.example.SistemaHistoriasClinicas.Models.Paciente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class InformacionDeConsultaController {

    private final ApplicationContext applicationContext;

    private Paciente paciente;

    @FXML
    private Label datosDeConsultaValor;

    @FXML
    private Label diagnosticoValor;

    public InformacionDeConsultaController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setConsulta(Consulta consulta, Paciente paciente) {
        this.paciente = paciente;
        datosDeConsultaValor.setText(textoOVacio(consulta.getDatosDeConsulta()));
        diagnosticoValor.setText(textoOVacio(consulta.getDiagnostico()));
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
            e.printStackTrace();
        }
    }

    private String textoOVacio(String valor) {
        return valor == null ? "" : valor;
    }
}
