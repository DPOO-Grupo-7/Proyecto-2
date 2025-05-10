package dominio.elementoparque;
// Manuel Villaveces (◣ ◢) KickAss

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;

import dominio.empleado.Capacitacion;
import dominio.empleado.Empleado;
import dominio.trabajo.LugarTrabajo;
import dominio.excepciones.DatosInvalidosException;
import dominio.excepciones.AsignacionInvalidaException;

/**
 * Representa una atracción del parque de diversiones.
 */
public abstract class Atraccion extends ElementoParque implements LugarTrabajo {
    private String ubicacion;
    private int empleadosMinimos;
    private NivelExclusividad nivelExclusividad;
    private List<Empleado> empleadosAsignados;
    private Set<Capacitacion> capacitacionesRequeridas;

    // ✅ NUEVOS CAMPOS: restricciones físicas
    private double alturaMinima;
    private double alturaMaxima;
    private double pesoMinimo;
    private double pesoMaximo;

    // ✅ Campo existente: restricciones médicas
    private List<String> restriccionesMedicas = new ArrayList<>();

    public Atraccion(String id, String nombre, String ubicacion, int cupoMaximo, int empleadosMinimos) {
        super(id, nombre, cupoMaximo);

        if (empleadosMinimos < 0) {
            throw new DatosInvalidosException("El número mínimo de empleados no puede ser negativo.");
        }
        this.ubicacion = ubicacion;
        this.empleadosMinimos = empleadosMinimos;
        this.empleadosAsignados = new ArrayList<>();
        this.capacitacionesRequeridas = new HashSet<>();
    }

    @Override
    public String getNombreLugar() {
        return getNombre();
    }

    @Override
    public void asignarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new DatosInvalidosException("El empleado a asignar no puede ser nulo.");
        }
        if (!puedeAsignarEmpleado(empleado)) {
            throw new AsignacionInvalidaException("El empleado '" + empleado.getNombre() + "' no puede ser asignado a la atracción '" + getNombre() + "'.");
        }
        if (!empleadosAsignados.contains(empleado)) {
            empleadosAsignados.add(empleado);
        }
    }

    @Override
    public boolean cumpleRequisitosPersonal() {
        if (empleadosAsignados.size() < empleadosMinimos) {
            return false;
        }
        return empleadosAsignados.stream()
                .allMatch(e -> e.cumpleCapacitaciones(capacitacionesRequeridas));
    }

    @Override
    public Set<Capacitacion> getCapacitacionesRequeridas() {
        return new HashSet<>(capacitacionesRequeridas);
    }

    protected void setCapacitacionesRequeridas(Set<Capacitacion> capacitaciones) {
        this.capacitacionesRequeridas = new HashSet<>(capacitaciones);
    }

    public abstract boolean puedeAsignarEmpleado(Empleado empleado);

    public String getUbicacion() {
        return ubicacion;
    }

    public int getEmpleadosMinimos() {
        return empleadosMinimos;
    }

    public List<Empleado> getEmpleadosAsignados() {
        return List.copyOf(empleadosAsignados);
    }

    public NivelExclusividad getNivelExclusividad() {
        return nivelExclusividad;
    }

    public void setNivelExclusividad(NivelExclusividad nivelExclusividad) {
        this.nivelExclusividad = nivelExclusividad;
    }

    @Override
    public boolean estaDisponibleEnFecha(LocalDateTime fecha) {
        return super.estaDisponibleEnFecha(fecha);
    }

    public String getId() {
        return super.getId();
    }

    // ✅ NUEVOS GETTERS DE RESTRICCIONES FÍSICAS
    public double getAlturaMinima() {
        return alturaMinima;
    }

    public double getAlturaMaxima() {
        return alturaMaxima;
    }

    public double getPesoMinimo() {
        return pesoMinimo;
    }

    public double getPesoMaximo() {
        return pesoMaximo;
    }

    // ✅ SETTER OPCIONAL PARA RESTRICCIONES FÍSICAS
    public void setRestriccionesFisicas(double alturaMinima, double alturaMaxima, double pesoMinimo, double pesoMaximo) {
        this.alturaMinima = alturaMinima;
        this.alturaMaxima = alturaMaxima;
        this.pesoMinimo = pesoMinimo;
        this.pesoMaximo = pesoMaximo;
    }

    // 🩺 MÉTODOS PARA RESTRICCIONES MÉDICAS
    public List<String> getRestriccionesMedicas() {
        return List.copyOf(restriccionesMedicas);
    }

    public void setRestriccionesMedicas(List<String> restricciones) {
        if (restricciones == null) {
            throw new DatosInvalidosException("La lista de restricciones médicas no puede ser nula.");
        }
        this.restriccionesMedicas = new ArrayList<>(restricciones);
    }
}
