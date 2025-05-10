package vista.gui.util;

import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 * Clase de utilidad para validar entradas de usuario en formularios.
 */
public class Validador {
    
    /**
     * Valida que un campo de texto no esté vacío.
     * 
     * @param campo El campo de texto a validar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @param errores StringBuilder donde se acumularán los errores
     * @return true si el campo es válido, false en caso contrario
     */
    public static boolean validarCampoNoVacio(JTextComponent campo, String nombreCampo, StringBuilder errores) {
        if (campo.getText().trim().isEmpty()) {
            errores.append("El campo ").append(nombreCampo).append(" es obligatorio.\n");
            return false;
        }
        return true;
    }
    
    /**
     * Valida que un campo de contraseña no esté vacío.
     * 
     * @param campo El campo de contraseña a validar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @param errores StringBuilder donde se acumularán los errores
     * @return true si el campo es válido, false en caso contrario
     */
    public static boolean validarPasswordNoVacia(JPasswordField campo, String nombreCampo, StringBuilder errores) {
        if (new String(campo.getPassword()).trim().isEmpty()) {
            errores.append("El campo ").append(nombreCampo).append(" es obligatorio.\n");
            return false;
        }
        return true;
    }
    
    /**
     * Valida que un ComboBox tenga un elemento seleccionado (distinto del primer elemento).
     * 
     * @param combo El ComboBox a validar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @param errores StringBuilder donde se acumularán los errores
     * @return true si la selección es válida, false en caso contrario
     */
    public static boolean validarComboBoxSeleccionado(JComboBox<?> combo, String nombreCampo, StringBuilder errores) {
        if (combo.getSelectedIndex() <= 0) { // Asumiendo que el índice 0 es una opción tipo "Seleccione..."
            errores.append("Debe seleccionar un ").append(nombreCampo).append(".\n");
            return false;
        }
        return true;
    }
    
    /**
     * Valida que un campo de texto contenga un email con formato válido.
     * 
     * @param campo El campo de texto a validar
     * @param errores StringBuilder donde se acumularán los errores
     * @return true si el email es válido, false en caso contrario
     */
    public static boolean validarEmail(JTextField campo, StringBuilder errores) {
        String email = campo.getText().trim();
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email.isEmpty()) {
            return true; // Si es opcional, no validamos formato
        }
        if (!email.matches(regex)) {
            errores.append("El email no tiene un formato válido.\n");
            return false;
        }
        return true;
    }
    
    /**
     * Valida que un campo de texto contenga un número de teléfono con formato válido.
     * 
     * @param campo El campo de texto a validar
     * @param errores StringBuilder donde se acumularán los errores
     * @return true si el teléfono es válido, false en caso contrario
     */
    public static boolean validarTelefono(JTextField campo, StringBuilder errores) {
        String telefono = campo.getText().trim();
        if (telefono.isEmpty()) {
            return true; // Si es opcional, no validamos formato
        }
        if (!telefono.matches("\\d{10}")) {
            errores.append("El teléfono debe contener 10 dígitos.\n");
            return false;
        }
        return true;
    }
    
    /**
     * Valida que un campo de texto contenga solo números.
     * 
     * @param campo El campo de texto a validar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @param errores StringBuilder donde se acumularán los errores
     * @return true si el campo contiene solo números, false en caso contrario
     */
    public static boolean validarNumerico(JTextField campo, String nombreCampo, StringBuilder errores) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            return true; // Si es opcional, no validamos
        }
        try {
            Double.parseDouble(texto);
            return true;
        } catch (NumberFormatException e) {
            errores.append("El campo ").append(nombreCampo).append(" debe contener solo números.\n");
            return false;
        }
    }
    
    /**
     * Valida que un campo de texto contenga solo números enteros.
     * 
     * @param campo El campo de texto a validar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @param errores StringBuilder donde se acumularán los errores
     * @return true si el campo contiene solo números enteros, false en caso contrario
     */
    public static boolean validarNumeroEntero(JTextField campo, String nombreCampo, StringBuilder errores) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            return true; // Si es opcional, no validamos
        }
        try {
            Integer.parseInt(texto);
            return true;
        } catch (NumberFormatException e) {
            errores.append("El campo ").append(nombreCampo).append(" debe contener solo números enteros.\n");
            return false;
        }
    }
}
