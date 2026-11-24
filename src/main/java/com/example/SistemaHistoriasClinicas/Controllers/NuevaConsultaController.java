package com.example.SistemaHistoriasClinicas.Controllers;

import com.example.SistemaHistoriasClinicas.Alertas.AlertClass;
import com.example.SistemaHistoriasClinicas.Models.Consulta;
import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Models.ValorDatoLaboratorio;
import com.example.SistemaHistoriasClinicas.Services.ConsultaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class NuevaConsultaController {

    private static final Logger log = LoggerFactory.getLogger(NuevaConsultaController.class);
    private static final int MAXIMO_CARACTERES_TEXTO = 9999;
    private static final String TEXTO_INICIAL = "Sin Datos";

    private final ConsultaService consultaService;
    private final ApplicationContext applicationContext;
    private Paciente paciente;
    private List<ValorDatoLaboratorio> valoresLaboratorio = new ArrayList<>();

    @FXML
    private TextArea datosDeConsultaInput;

    @FXML
    private TextArea diagnosticoInput;

    public NuevaConsultaController(ConsultaService consultaService, ApplicationContext applicationContext) {
        this.consultaService = consultaService;
        this.applicationContext = applicationContext;
    }

    @FXML
    private void initialize() {
        configurarLimiteDeCaracteres(datosDeConsultaInput);
        configurarLimiteDeCaracteres(diagnosticoInput);
        datosDeConsultaInput.setText(TEXTO_INICIAL);
        diagnosticoInput.setText(TEXTO_INICIAL);
    }

    public void setTextosConsulta(String datosDeConsulta, String diagnostico) {
        datosDeConsultaInput.setText(datosDeConsulta);
        diagnosticoInput.setText(diagnostico);
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void setValoresLaboratorio(List<ValorDatoLaboratorio> valoresLaboratorio) {
        this.valoresLaboratorio = valoresLaboratorio == null
                ? new ArrayList<>()
                : valoresLaboratorio;
    }

    @FXML
    private void aceptarConsulta() {
        if (paciente == null || paciente.getId() == null) {
            AlertClass.mostrarMensaje(
                    "Paciente no disponible",
                    "No hay un paciente asociado para registrar la consulta."
            );
            return;
        }

        if (datosDeConsultaInput.getText().isBlank() || diagnosticoInput.getText().isBlank()) {
            AlertClass.mostrarMensaje(
                    "Campos incompletos",
                    "Completá los datos de la consulta y el diagnóstico."
            );
            return;
        }

        Consulta consulta = new Consulta(
                paciente,
                datosDeConsultaInput.getText().trim(),
                LocalDate.now(),
                diagnosticoInput.getText().trim()
        );

        Consulta guardada = consultaService.almacenarNuevaConsulta(consulta, valoresLaboratorio);
        guardada.setPaciente(paciente);

        if (paciente.getConsultas() == null) {
            paciente.setConsultas(new ArrayList<>());
        }
        paciente.getConsultas().add(guardada);

        volverAInformacionPaciente();
    }

    @FXML
    private void abrirValoresLaboratorio() {
        if (paciente == null || paciente.getId() == null) {
            AlertClass.mostrarMensaje(
                    "Paciente no disponible",
                    "No hay un paciente asociado para cargar datos de laboratorio."
            );
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/ValoresLaboratorio.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent valoresLaboratorioVista = loader.load();

            ValoresLaboratorioController controller = loader.getController();
            controller.preparar(
                    paciente,
                    datosDeConsultaInput.getText(),
                    diagnosticoInput.getText(),
                    valoresLaboratorio
            );

            Stage ventanaActual = (Stage) datosDeConsultaInput
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(valoresLaboratorioVista));
            ventanaActual.setTitle("Datos de laboratorio");
            ventanaActual.setWidth(1180);
            ventanaActual.setHeight(760);
            ventanaActual.setMinWidth(1050);
            ventanaActual.setMinHeight(700);
            ventanaActual.centerOnScreen();
            ventanaActual.show();

        } catch (IOException e) {
            log.error("No se pudo abrir la pantalla de datos de laboratorio", e);
            AlertClass.mostrarMensaje(
                    "Error",
                    "No se pudo abrir la pantalla de datos de laboratorio."
            );
        }
    }

    @FXML
    private void editarInformacionPaciente() {
        if (paciente == null || paciente.getId() == null) {
            AlertClass.mostrarMensaje(
                    "Paciente no disponible",
                    "No hay un paciente asociado para editar."
            );
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/EditarInformacionPaciente.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent editarInformacion = loader.load();

            EditarInformacionPacienteController controller = loader.getController();
            controller.preparar(
                    paciente,
                    datosDeConsultaInput.getText(),
                    diagnosticoInput.getText(),
                    valoresLaboratorio
            );

            Stage ventanaActual = (Stage) datosDeConsultaInput
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(editarInformacion));
            ventanaActual.setTitle("Editar información del paciente");
            ventanaActual.setWidth(850);
            ventanaActual.setHeight(700);
            ventanaActual.setMinWidth(850);
            ventanaActual.setMinHeight(700);
            ventanaActual.centerOnScreen();
            ventanaActual.show();

        } catch (IOException e) {
            log.error("No se pudo abrir la edición de la información del paciente", e);
            AlertClass.mostrarMensaje(
                    "Error",
                    "No se pudo abrir la edición de la información del paciente."
            );
        }
    }

    private void configurarLimiteDeCaracteres(TextArea campo) {
        campo.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= MAXIMO_CARACTERES_TEXTO
                        ? change
                        : null
        ));
    }

    @FXML
    private void volverAInformacionPaciente() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/InformacionPaciente.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent informacionPaciente = loader.load();

            InformacionPacienteController controller = loader.getController();
            controller.setPaciente(paciente);

            Stage ventanaActual = (Stage) datosDeConsultaInput
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
            AlertClass.mostrarMensaje(
                    "Error",
                    "No se pudo volver a la información del paciente."
            );
        }
    }
}
