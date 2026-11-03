package com.example.SistemaHistoriasClinicas.Controllers;

import com.example.SistemaHistoriasClinicas.Models.Consulta;
import com.example.SistemaHistoriasClinicas.Models.Paciente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class InformacionPacienteController {

    private static final DateTimeFormatter FECHA_CONSULTA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ApplicationContext applicationContext;

    @FXML
    private Label nombreValor;

    @FXML
    private Label apellidoValor;

    @FXML
    private Label telefonoValor;

    @FXML
    private Label documentoValor;

    @FXML
    private Label afiliadoValor;

    @FXML
    private Label fechaNacimientoValor;

    @FXML
    private Label medicacionValor;

    @FXML
    private Label actividadFisicaValor;

    @FXML
    private ListView<Consulta> consultasLista;

    private Paciente paciente;

    public InformacionPacienteController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @FXML
    private void initialize() {
        consultasLista.setCellFactory(lista -> {
            ListCell<Consulta> celda = new ListCell<>() {
                @Override
                protected void updateItem(Consulta consulta, boolean empty) {
                    super.updateItem(consulta, empty);
                    if (empty || consulta == null || consulta.getFecha() == null) {
                        setText(null);
                        return;
                    }
                    setText(consulta.getFecha().format(FECHA_CONSULTA));
                }
            };

            celda.setOnMouseClicked(evento -> {
                if (!celda.isEmpty() && celda.getItem() != null) {
                    abrirInformacionDeConsulta(celda.getItem());
                }
            });

            return celda;
        });
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        nombreValor.setText(textoOVacio(paciente.getNombre()));
        apellidoValor.setText(textoOVacio(paciente.getApellido()));
        telefonoValor.setText(textoOVacio(paciente.getTelefono()));
        documentoValor.setText(textoOVacio(paciente.getDocumento()));
        afiliadoValor.setText(textoOVacio(paciente.getAfiliado()));
        fechaNacimientoValor.setText(formatearFecha(
                paciente.getDia(),
                paciente.getMes(),
                paciente.getAno()
        ));
        medicacionValor.setText(textoOVacio(paciente.getMedicacion()));
        actividadFisicaValor.setText(textoOVacio(paciente.getActFisica()));
        cargarConsultas();
    }

    private void cargarConsultas() {
        List<Consulta> consultas = paciente.getConsultas() == null
                ? new ArrayList<>()
                : new ArrayList<>(paciente.getConsultas());

        consultas.sort(Comparator
                .comparing(Consulta::getFecha, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Consulta::getId, Comparator.nullsLast(Comparator.reverseOrder())));

        consultasLista.getItems().setAll(consultas);
    }

    private void abrirInformacionDeConsulta(Consulta consulta) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/InformacionDeConsulta.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent informacionConsulta = loader.load();

            InformacionDeConsultaController controller = loader.getController();
            controller.setConsulta(consulta, paciente);

            Stage ventanaActual = (Stage) nombreValor
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(informacionConsulta));
            ventanaActual.setTitle("Información de la consulta");
            ventanaActual.setWidth(850);
            ventanaActual.setHeight(700);
            ventanaActual.setMinWidth(850);
            ventanaActual.setMinHeight(700);
            ventanaActual.centerOnScreen();
            ventanaActual.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void nuevaConsulta() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/NuevaConsulta.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent nuevaConsulta = loader.load();

            NuevaConsultaController controller = loader.getController();
            controller.setPaciente(paciente);

            Stage ventanaActual = (Stage) nombreValor
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(nuevaConsulta));
            ventanaActual.setTitle("Nueva consulta");
            ventanaActual.setWidth(850);
            ventanaActual.setHeight(700);
            ventanaActual.setMinWidth(850);
            ventanaActual.setMinHeight(700);
            ventanaActual.centerOnScreen();
            ventanaActual.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void volverAlInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/Inicio.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent inicio = loader.load();

            Stage ventanaActual = (Stage) nombreValor
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(inicio, 600, 500));
            ventanaActual.setTitle("Sistema de Historias Clínicas");
            ventanaActual.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String textoOVacio(String valor) {
        return valor == null ? "" : valor;
    }

    private String formatearFecha(String dia, String mes, String ano) {
        if (estaVacio(dia) && estaVacio(mes) && estaVacio(ano)) {
            return "";
        }

        return textoOVacio(dia) + "/" + textoOVacio(mes) + "/" + textoOVacio(ano);
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
