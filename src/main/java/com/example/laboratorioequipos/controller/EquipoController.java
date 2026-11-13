package com.example.laboratorioequipos.controller;

import com.example.laboratorioequipos.model.Equipo;
import com.example.laboratorioequipos.model.TipoEquipo;
import com.example.laboratorioequipos.dao.EquipoDAO;
import com.example.laboratorioequipos.dao.TipoEquipoDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.Optional;

public class EquipoController {

    @FXML private TextField txtCodigo, txtNombre, txtMarca, txtCantidad, txtBuscarNombre;
    @FXML private ComboBox<TipoEquipo> cmbTipo, cmbFiltroTipo;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TableView<Equipo> tblEquipos;
    @FXML private TableColumn<Equipo, String> colCodigo, colNombre, colMarca, colEstado, colTipo;
    @FXML private TableColumn<Equipo, Integer> colCantidad;

    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final TipoEquipoDAO tipoDAO = new TipoEquipoDAO();
    private final ObservableList<Equipo> listaEquipos = FXCollections.observableArrayList();
    private Equipo equipoSeleccionado = null;

    private static final List<String> ESTADOS_PERMITIDOS = List.of(
            "Disponible", "En mantenimiento", "Fuera de servicio"
    );

    @FXML
    public void initialize() {
        cmbEstado.setItems(FXCollections.observableArrayList(ESTADOS_PERMITIDOS));
        cargarTipos();
        configurarTabla();
        cargarDatosTabla(null, null);

        tblEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                equipoSeleccionado = newSel;
                cargarFormulario(newSel);
            }
        });
    }

    private void cargarTipos() {
        List<TipoEquipo> tipos = tipoDAO.listarTodos();
        cmbTipo.setItems(FXCollections.observableArrayList(tipos));

        ObservableList<TipoEquipo> tiposFiltro = FXCollections.observableArrayList();
        tiposFiltro.add(null);
        tiposFiltro.addAll(tipos);
        cmbFiltroTipo.setItems(tiposFiltro);
        cmbFiltroTipo.setButtonCell(crearCeldaFiltroTipo());
        cmbFiltroTipo.setCellFactory(lv -> crearCeldaFiltroTipo());
    }

    private ListCell<TipoEquipo> crearCeldaFiltroTipo() {
        return new ListCell<>() {
            @Override
            protected void updateItem(TipoEquipo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Todos los tipos" : item.getNombre());
            }
        };
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipoEquipo().getNombre())
        );
        tblEquipos.setItems(listaEquipos);
    }

    private void cargarDatosTabla(String nombre, TipoEquipo tipo) {
        listaEquipos.setAll(equipoDAO.buscarConFiltros(nombre, tipo));
    }

    private void cargarFormulario(Equipo e) {
        txtCodigo.setText(e.getCodigo());
        txtNombre.setText(e.getNombre());
        txtMarca.setText(e.getMarca());
        txtCantidad.setText(String.valueOf(e.getCantidad()));
        cmbEstado.setValue(e.getEstado());

        cmbTipo.getItems().stream()
                .filter(t -> t.getIdTipo().equals(e.getTipoEquipo().getIdTipo()))
                .findFirst()
                .ifPresent(cmbTipo::setValue);
    }

    @FXML
    private void onGuardar() {
        if (!validarFormulario()) return;

        try {
            Equipo nuevo = new Equipo();
            mapearFormularioAEntidad(nuevo);
            equipoDAO.guardar(nuevo);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Equipo registrado correctamente.");
            onLimpiar();
            onBuscarFiltrar();
        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Persistencia",
                    "No se pudo guardar el equipo. Verifique que el código no esté duplicado.\nDetalle: " + obtenerMensajeCausa(ex));
        }
    }

    @FXML
    private void onActualizar() {
        if (equipoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Seleccione un equipo de la tabla para actualizar.");
            return;
        }
        if (!validarFormulario()) return;

        try {
            mapearFormularioAEntidad(equipoSeleccionado);
            equipoDAO.actualizar(equipoSeleccionado);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Equipo actualizado correctamente.");
            onLimpiar();
            onBuscarFiltrar();
        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Actualizar",
                    "No se pudo actualizar el registro (posible código duplicado).\nDetalle: " + obtenerMensajeCausa(ex));
        }
    }

    @FXML
    private void onEliminar() {
        if (equipoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Seleccione un equipo de la tabla para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Eliminar equipo " + equipoSeleccionado.getCodigo() + "?");
        confirm.setContentText("Esta acción eliminará el registro permanentemente de PostgreSQL.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                equipoDAO.eliminar(equipoSeleccionado.getIdEquipo());
                mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Equipo eliminado del inventario.");
                onLimpiar();
                onBuscarFiltrar();
            } catch (Exception ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Eliminar", obtenerMensajeCausa(ex));
            }
        }
    }

    @FXML
    private void onBuscarFiltrar() {
        cargarDatosTabla(txtBuscarNombre.getText(), cmbFiltroTipo.getValue());
    }

    @FXML
    private void onLimpiar() {
        equipoSeleccionado = null;
        txtCodigo.clear();
        txtNombre.clear();
        txtMarca.clear();
        txtCantidad.clear();
        cmbEstado.setValue(null);
        cmbTipo.setValue(null);
        tblEquipos.getSelectionModel().clearSelection();
    }

    private void mapearFormularioAEntidad(Equipo e) {
        e.setCodigo(txtCodigo.getText().trim());
        e.setNombre(txtNombre.getText().trim());
        e.setMarca(txtMarca.getText() != null ? txtMarca.getText().trim() : "");
        e.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
        e.setEstado(cmbEstado.getValue());
        e.setTipoEquipo(cmbTipo.getValue());
    }

    private boolean validarFormulario() {
        if (txtCodigo.getText() == null || txtCodigo.getText().isBlank() ||
                txtNombre.getText() == null || txtNombre.getText().isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El código y el nombre son obligatorios.");
            return false;
        }
        if (cmbTipo.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Debe seleccionar un tipo de equipo.");
            return false;
        }
        if (cmbEstado.getValue() == null || !ESTADOS_PERMITIDOS.contains(cmbEstado.getValue())) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione un estado válido.");
            return false;
        }
        try {
            int cant = Integer.parseInt(txtCantidad.getText().trim());
            if (cant < 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "La cantidad no puede ser negativa.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "La cantidad debe ser un número entero válido.");
            return false;
        }
        return true;
    }

    private String obtenerMensajeCausa(Throwable t) {
        while (t.getCause() != null) t = t.getCause();
        return t.getMessage();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}