package dominio.usuario;

import dominio.excepciones.DatosInvalidosException;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {
    protected String identificacion;
    protected String nombre;
    protected String email;
    protected String telefono;
    private String username;
    private String password;

    protected List<String> condicionesMedicas = new ArrayList<>();

    public Usuario(String identificacion, String nombre, String email, 
                   String telefono, String username, String password) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new DatosInvalidosException("La identificación no puede ser null o vacía");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede ser null o vacío");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new DatosInvalidosException("El email no puede ser null o vacío");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre de usuario no puede ser null o vacío");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new DatosInvalidosException("La contraseña no puede ser null o vacía");
        }

        this.identificacion = identificacion;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.username = username;
        this.password = password;
    }

    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede ser null o vacío");
        }
        this.nombre = nombre;
    }

    public boolean verificarPassword(String candidatePassword) {
        return password.equals(candidatePassword);
    }

    protected boolean validarCredenciales(String password) {
        return this.password.equals(password);
    }

    public void cambiarPassword(String passwordActual, String nuevaPassword) {
        if (!validarCredenciales(passwordActual)) {
            throw new DatosInvalidosException("Contraseña actual incorrecta");
        }
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            throw new DatosInvalidosException("La nueva contraseña no puede estar vacía");
        }
        this.password = nuevaPassword;
    }

    public void agregarCondicionMedica(String condicion) {
        this.condicionesMedicas.add(condicion);
    }

    public List<String> getCondicionesMedicas() {
        return List.copyOf(condicionesMedicas);
    }

    public boolean cumpleRestriccionesMedicas(List<String> contraindicaciones) {
        for (String condicion : condicionesMedicas) {
            if (contraindicaciones.contains(condicion)) {
                return false;
            }
        }
        return true;
    }
}
