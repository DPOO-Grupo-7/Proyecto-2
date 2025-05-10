package vista.gui.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla con soporte para paginación de datos.
 * Útil para mostrar grandes conjuntos de datos en tablas.
 */
public class ModeloTablaPaginado extends AbstractTableModel {
    
    private static final long serialVersionUID = 1L;
    
    private List<?> datosTotales;
    private List<?> datosPagina;
    private String[] columnas;
    private int filasPorPagina = 10;
    private int paginaActual = 0;
    private int totalPaginas;
    
    /**
     * Constructor para modelo con paginación.
     * 
     * @param columnas Nombres de las columnas
     * @param datos Lista completa de datos
     * @param filasPorPagina Número de filas por página
     */
    public ModeloTablaPaginado(String[] columnas, List<?> datos, int filasPorPagina) {
        this.columnas = columnas;
        this.datosTotales = datos;
        this.filasPorPagina = filasPorPagina;
        
        calcularTotalPaginas();
        cargarDatosPagina();
    }
    
    /**
     * Calcula el número total de páginas según los datos y filas por página.
     */
    private void calcularTotalPaginas() {
        if (datosTotales.isEmpty()) {
            totalPaginas = 1;
        } else {
            totalPaginas = (int) Math.ceil((double) datosTotales.size() / filasPorPagina);
        }
        
        // Asegurar que la página actual está en rango válido
        if (paginaActual >= totalPaginas) {
            paginaActual = totalPaginas - 1;
        }
    }
    
    /**
     * Carga los datos correspondientes a la página actual.
     */
    private void cargarDatosPagina() {
        int desde = paginaActual * filasPorPagina;
        int hasta = Math.min(desde + filasPorPagina, datosTotales.size());
        
        if (datosTotales.isEmpty()) {
            datosPagina = new ArrayList<>();
        } else {
            datosPagina = datosTotales.subList(desde, hasta);
        }
    }
    
    /**
     * Cambia a la página especificada.
     * 
     * @param pagina Número de página (0-indexed)
     */
    public void setPaginaActual(int pagina) {
        int nuevaPagina = Math.min(Math.max(0, pagina), totalPaginas - 1);
        if (nuevaPagina != paginaActual) {
            paginaActual = nuevaPagina;
            cargarDatosPagina();
            fireTableDataChanged();
        }
    }
    
    /**
     * Avanza a la siguiente página si es posible.
     */
    public void siguientePagina() {
        if (paginaActual < totalPaginas - 1) {
            paginaActual++;
            cargarDatosPagina();
            fireTableDataChanged();
        }
    }
    
    /**
     * Retrocede a la página anterior si es posible.
     */
    public void anteriorPagina() {
        if (paginaActual > 0) {
            paginaActual--;
            cargarDatosPagina();
            fireTableDataChanged();
        }
    }
    
    /**
     * Actualiza los datos del modelo.
     * 
     * @param nuevosDatos Nueva lista completa de datos
     */
    public void actualizarDatos(List<?> nuevosDatos) {
        datosTotales = nuevosDatos;
        calcularTotalPaginas();
        cargarDatosPagina();
        fireTableDataChanged();
    }
    
    /**
     * Obtiene el total de páginas.
     * 
     * @return Número total de páginas
     */
    public int getTotalPaginas() {
        return totalPaginas;
    }
    
    /**
     * Obtiene la página actual.
     * 
     * @return Número de página actual (0-indexed)
     */
    public int getPaginaActual() {
        return paginaActual;
    }
    
    /**
     * Obtiene el número de filas por página.
     * 
     * @return Filas por página
     */
    public int getFilasPorPagina() {
        return filasPorPagina;
    }
    
    /**
     * Obtiene el objeto en la fila especificada (dentro de la página actual).
     * 
     * @param rowIndex Índice de fila
     * @return Objeto en esa fila
     */
    public Object getObjectAtRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < datosPagina.size()) {
            return datosPagina.get(rowIndex);
        }
        return null;
    }
    
    /**
     * Obtiene el índice absoluto (en toda la lista) de una fila en la página actual.
     * 
     * @param rowIndex Índice de fila en la página
     * @return Índice absoluto en toda la lista
     */
    public int getAbsoluteRowIndex(int rowIndex) {
        return paginaActual * filasPorPagina + rowIndex;
    }
    
    // Implementación de métodos requeridos por AbstractTableModel
    
    @Override
    public int getRowCount() {
        return datosPagina.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Este método debe ser sobreescrito por las clases hijas
        // para extraer los valores específicos de los objetos
        return null;
    }
    
    /**
     * Crea un panel de control de paginación para una tabla.
     * 
     * @param tabla JTable a controlar
     * @return Panel con controles de paginación
     */
    public JPanel crearPanelPaginacion(JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton btnPrimero = new JButton("<<");
        JButton btnAnterior = new JButton("<");
        JLabel lblPagina = new JLabel(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
        JButton btnSiguiente = new JButton(">");
        JButton btnUltimo = new JButton(">>");
        
        btnPrimero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPaginaActual(0);
                lblPagina.setText(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
            }
        });
        
        btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anteriorPagina();
                lblPagina.setText(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
            }
        });
        
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siguientePagina();
                lblPagina.setText(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
            }
        });
        
        btnUltimo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPaginaActual(totalPaginas - 1);
                lblPagina.setText(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
            }
        });
        
        panelControles.add(btnPrimero);
        panelControles.add(btnAnterior);
        panelControles.add(lblPagina);
        panelControles.add(btnSiguiente);
        panelControles.add(btnUltimo);
        
        panel.add(panelControles, BorderLayout.CENTER);
        
        return panel;
    }
}
