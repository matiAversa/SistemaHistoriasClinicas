package com.example.SistemaHistoriasClinicas.Controllers;

import com.example.SistemaHistoriasClinicas.Alertas.AlertClass;
import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Models.ValorDatoLaboratorio;
import com.example.SistemaHistoriasClinicas.Services.PacienteService;
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
import java.util.List;

@Component
public class EditarInformacionPacienteController {

    private static final Logger log = LoggerFactory.getLogger(EditarInformacionPacienteController.class);
    private static final int MAXIMO_CARACTERES_TEXTO = 9999;

    private final PacienteService pacienteService;
    private final ApplicationContext applicationContext;

    private Paciente paciente;
    private String datosDeConsulta;
    private String diagnostico;
    private List<ValorDatoLaboratorio> valoresLaboratorio;

    @FXML
    private TextArea medicacionInput;

    @FXML
    private TextArea actividadFisicaInput;

    public EditarInformacionPacienteController(
            PacienteService pacienteService,
            ApplicationContext applicationContext) {
        this.pacienteService = pacienteService;
        this.applicationContext = applicationContext;
    }

    @FXML
    private void initialize() {
        configurarLimiteDeCaracteres(medicacionInput);
        configurarLimiteDeCaracteres(actividadFisicaInput);
    }

    public void preparar(
            Paciente paciente,
            String datosDeConsulta,
            String diagnostico,
            List<ValorDatoLaboratorio> valoresLaboratorio) {
        this.paciente = paciente;
        this.datosDeConsulta = datosDeConsulta;
        this.diagnostico = diagnostico;
        this.valoresLaboratorio = valoresLaboratorio;
        medicacionInput.setText(textoOVacio(paciente.getMedicacion()));
        actividadFisicaInput.setText(textoOVacio(paciente.getActFisica()));
    }

    @FXML
    private void guardarInformacion() {
        if (paciente == null || paciente.getId() == null) {
            AlertClass.mostrarMensaje(
                    "Paciente no disponible",
                    "No hay un paciente asociado para actualizar."
            );
            return;
        }

        String medicacion = medicacionInput.getText().trim();
        String actFisica = actividadFisicaInput.getText().trim();

        pacienteService.actualizarMedicacionYActividad(paciente.getId(), medicacion, actFisica);
        paciente.setMedicacion(medicacion);
        paciente.setActFisica(actFisica);

        volverANuevaConsulta();
    }

    @FXML
    private void volverANuevaConsulta() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/NuevaConsulta.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent nuevaConsulta = loader.load();

            NuevaConsultaController controller = loader.getController();
            controller.setPaciente(paciente);
            controller.setTextosConsulta(datosDeConsulta, diagnostico);
            controller.setValoresLaboratorio(valoresLaboratorio);

            Stage ventanaActual = (Stage) medicacionInput
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(nuevaConsulta));
            ventanaActual.setTitle("Nueva consulta");
            ventanaActual.setWidth(850);
            ventanaActual.setHeight(900);
            ventanaActual.setMinWidth(850);
            ventanaActual.setMinHeight(860);
            ventanaActual.centerOnScreen();
            ventanaActual.show();

        } catch (IOException e) {
            log.error("No se pudo volver a la nueva consulta", e);
            AlertClass.mostrarMensaje(
                    "Error",
                    "No se pudo volver a la nueva consulta."
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

    private String textoOVacio(String valor) {
        return valor == null ? "" : valor;
    }
}
