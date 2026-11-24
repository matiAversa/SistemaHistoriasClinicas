package com.example.SistemaHistoriasClinicas.Controllers;

import com.example.SistemaHistoriasClinicas.Alertas.AlertClass;
import com.example.SistemaHistoriasClinicas.Models.DatoLaboratorio;
import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Models.ValorDatoLaboratorio;
import com.example.SistemaHistoriasClinicas.Services.DatoLaboratorioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ValoresLaboratorioController {

    private static final Logger log = LoggerFactory.getLogger(ValoresLaboratorioController.class);
    private static final BigDecimal VALOR_MAXIMO = new BigDecimal("99999.9999");

    private final DatoLaboratorioService datoLaboratorioService;
    private final ApplicationContext applicationContext;

    private Paciente paciente;
    private String datosDeConsulta;
    private String diagnostico;
    private List<ValorDatoLaboratorio> valores = new ArrayList<>();

    @FXML
    private ListView<ValorDatoLaboratorio> valoresLista;

    @FXML
    private ComboBox<DatoLaboratorio> datosLaboratorioCombo;

    @FXML
    private TextField valorInput;

    @FXML
    private Button confirmarButton;

    public ValoresLaboratorioController(
            DatoLaboratorioService datoLaboratorioService,
            ApplicationContext applicationContext) {
        this.datoLaboratorioService = datoLaboratorioService;
        this.applicationContext = applicationContext;
    }

    @FXML
    private void initialize() {
        datosLaboratorioCombo.setButtonCell(crearCeldaDato());
        datosLaboratorioCombo.setCellFactory(lista -> crearCeldaDato());
        datosLaboratorioCombo.getItems().setAll(datoLaboratorioService.listarTodos());
        datosLaboratorioCombo.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, seleccionado) -> {
                    boolean habilitado = seleccionado != null;
                    valorInput.setDisable(!habilitado);
                    confirmarButton.setDisable(!habilitado);
                    if (!habilitado) {
                        valorInput.clear();
                    }
                }
        );

        valoresLista.setCellFactory(lista -> new ListCell<>() {
            private final Label texto = new Label();
            private final Button quitar = new Button("Quitar");
            private final HBox fila = new HBox(10, texto, quitar);

            {
                texto.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(texto, Priority.ALWAYS);
                quitar.getStyleClass().add("boton-quitar");
                fila.setAlignment(Pos.CENTER_LEFT);
                quitar.setOnAction(evento -> eliminarValor(getItem()));
            }

            @Override
            protected void updateItem(ValorDatoLaboratorio valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null || valor.getDatoLaboratorio() == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                texto.setText(valor.getDatoLaboratorio().getNombre() + ": " + valor.getValorIngresado());
                setText(null);
                setGraphic(fila);
            }
        });
    }

    private void eliminarValor(ValorDatoLaboratorio valor) {
        if (valor == null) {
            return;
        }
        valores.remove(valor);
        valoresLista.getItems().setAll(valores);
    }

    public void preparar(
            Paciente paciente,
            String datosDeConsulta,
            String diagnostico,
            List<ValorDatoLaboratorio> valores) {
        this.paciente = paciente;
        this.datosDeConsulta = datosDeConsulta;
        this.diagnostico = diagnostico;
        this.valores = valores == null ? new ArrayList<>() : valores;
        valoresLista.getItems().setAll(this.valores);
    }

    @FXML
    private void confirmarValor() {
        DatoLaboratorio dato = datosLaboratorioCombo.getValue();
        if (dato == null) {
            AlertClass.mostrarMensaje(
                    "Dato requerido",
                    "Seleccioná un dato de laboratorio."
            );
            return;
        }

        String texto = valorInput.getText() == null ? "" : valorInput.getText().trim();
        if (!texto.matches("\\d+(\\.\\d+)?")) {
            AlertClass.mostrarMensaje(
                    "Valor inválido",
                    "Ingresá un entero o un decimal separado por punto. No uses coma."
            );
            return;
        }

        BigDecimal numero = new BigDecimal(texto);
        if (numero.compareTo(BigDecimal.ZERO) < 0 || numero.compareTo(VALOR_MAXIMO) > 0) {
            AlertClass.mostrarMensaje(
                    "Valor fuera de rango",
                    "El valor debe estar entre 0 y 99999.9999."
            );
            return;
        }

        boolean yaAgregado = valores.stream().anyMatch(valor ->
                valor.getDatoLaboratorio() != null
                        && valor.getDatoLaboratorio().getId() != null
                        && valor.getDatoLaboratorio().getId().equals(dato.getId())
        );
        if (yaAgregado) {
            AlertClass.mostrarMensaje(
                    "Dato repetido",
                    "Ese dato de laboratorio ya fue agregado en esta consulta."
            );
            return;
        }

        ValorDatoLaboratorio valor = new ValorDatoLaboratorio();
        valor.setDatoLaboratorio(dato);
        valor.setValor(numero.doubleValue());
        valor.setValorIngresado(texto);
        valores.add(valor);
        valoresLista.getItems().setAll(valores);
        valorInput.clear();
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
            controller.setValoresLaboratorio(valores);

            Stage ventanaActual = (Stage) valorInput
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

    private ListCell<DatoLaboratorio> crearCeldaDato() {
        return new ListCell<>() {
            @Override
            protected void updateItem(DatoLaboratorio dato, boolean empty) {
                super.updateItem(dato, empty);
                setText(empty || dato == null ? null : dato.getNombre());
            }
        };
    }
}
