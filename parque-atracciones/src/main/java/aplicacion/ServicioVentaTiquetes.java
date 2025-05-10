package aplicacion;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import dominio.tiquete.*;
import dominio.usuario.Usuario;
import dominio.usuario.Cliente;
import dominio.elementoparque.Atraccion;
import dominio.elementoparque.Espectaculo;
import dominio.elementoparque.NivelExclusividad;
import dominio.empleado.Empleado;
import dominio.excepciones.DatosInvalidosException;
import dominio.excepciones.TiqueteInvalidoException;
import dominio.elementoparque.ElementoParque;
import infraestructura.persistencia.TiqueteRepositoryJson;

public class ServicioVentaTiquetes {

    private static final double DESCUENTO_EMPLEADO = 0.50;
    private final TiqueteRepositoryJson tiqueteRepository;
    private final Map<String, Tiquete> tiquetesVendidos = new HashMap<>();

    public ServicioVentaTiquetes(TiqueteRepositoryJson tiqueteRepository, java.util.function.Function<String, Atraccion> atraccionResolver) {
        this.tiqueteRepository = tiqueteRepository;
        for (Tiquete t : tiqueteRepository.cargarTiquetes(atraccionResolver)) {
            tiquetesVendidos.put(t.getCodigo(), t);
        }
    }

    private double calcularPrecioFinal(Usuario comprador, double precioBase) {
        if (comprador instanceof Empleado) {
            return precioBase * (1 - DESCUENTO_EMPLEADO);
        }
        return precioBase;
    }

    public TiqueteGeneral venderTiqueteGeneral(Usuario comprador, CategoriaTiquete categoria, double precioBase) {
        if (comprador == null || categoria == null) {
            throw new DatosInvalidosException("Comprador y categoría no pueden ser nulos.");
        }
        String codigo = generarCodigoUnico();
        boolean esEmpleado = comprador instanceof Empleado;
        double precioFinal = calcularPrecioFinal(comprador, precioBase);
        TiqueteGeneral tg = new TiqueteGeneral(codigo, LocalDateTime.now(), precioFinal,
                comprador.getIdentificacion(), comprador.getNombre(),
                esEmpleado, categoria, comprador);
        tiquetesVendidos.put(codigo, tg);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        return tg;
    }

    public TiqueteTemporada venderTiqueteTemporada(Usuario comprador, CategoriaTiquete categoria,
                                                    LocalDateTime fechaInicio, LocalDateTime fechaFin,
                                                    double precioBase) {
        if (comprador == null || categoria == null || fechaInicio == null || fechaFin == null) {
            throw new DatosInvalidosException("Comprador, categoría y fechas no pueden ser nulos.");
        }
        // Validación de categoría: no se permite BASICO
        if (categoria == CategoriaTiquete.BASICO) {
            throw new DatosInvalidosException("No se permite vender tiquetes de temporada con categoría BASICO.");
        }
        String codigo = generarCodigoUnico();
        boolean esEmpleado = comprador instanceof Empleado;
        double precioFinal = calcularPrecioFinal(comprador, precioBase);
        TiqueteTemporada tt = new TiqueteTemporada(codigo, LocalDateTime.now(), precioFinal,
                comprador.getIdentificacion(), comprador.getNombre(),
                esEmpleado, categoria, fechaInicio, fechaFin, comprador);
        tiquetesVendidos.put(codigo, tt);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        return tt;
    }

    public EntradaIndividual venderEntradaIndividual(Usuario comprador, Atraccion atraccion, double precioBase) {
        if (comprador == null || atraccion == null) {
            throw new DatosInvalidosException("Comprador y atracción no pueden ser nulos.");
        }
        String codigo = generarCodigoUnico();
        boolean esEmpleado = comprador instanceof Empleado;
        double precioFinal = calcularPrecioFinal(comprador, precioBase);
        EntradaIndividual ei = new EntradaIndividual(codigo, LocalDateTime.now(), precioFinal,
                comprador.getIdentificacion(), comprador.getNombre(),
                esEmpleado, atraccion, comprador);
        tiquetesVendidos.put(codigo, ei);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        return ei;
    }

    public FastPass venderFastPass(Usuario comprador, LocalDateTime fechaValida, double precioBase) {
        if (comprador == null || fechaValida == null) {
            throw new DatosInvalidosException("Comprador y fecha de validez no pueden ser nulos.");
        }
        String codigo = generarCodigoUnico();
        boolean esEmpleado = comprador instanceof Empleado;
        double precioFinal = calcularPrecioFinal(comprador, precioBase);
        FastPass fp = new FastPass(codigo, LocalDateTime.now(), fechaValida, precioFinal,
                comprador.getIdentificacion(), comprador.getNombre(), esEmpleado, comprador);
        tiquetesVendidos.put(codigo, fp);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        return fp;
    }

    private String generarCodigoUnico() {
        return UUID.randomUUID().toString();
    }

    public boolean validarTiquete(Tiquete tiquete, LocalDateTime fechaUso) {
        if (tiquete == null || fechaUso == null) {
            throw new DatosInvalidosException("Tiquete y fecha de uso no pueden ser nulos.");
        }
        return tiquete.esValidoParaFecha(fechaUso);
    }

    public boolean validarAccesoAtraccion(Tiquete tiquete, ElementoParque elemento) {
        if (tiquete == null || elemento == null) {
            throw new DatosInvalidosException("Tiquete y elemento del parque no pueden ser nulos para validar acceso.");
        }

        if (elemento instanceof Espectaculo) return true;

        if (!(elemento instanceof Atraccion)) {
            throw new TiqueteInvalidoException("El elemento '" + elemento.getNombre() + "' no es una atracción válida.");
        }

        Atraccion atraccion = (Atraccion) elemento;
        NivelExclusividad nivelReq = atraccion.getNivelExclusividad();

        if (nivelReq == null) {
            throw new TiqueteInvalidoException("La atracción '" + atraccion.getNombre() + "' no tiene nivel de exclusividad definido.");
        }

        // 💡 VALIDACIÓN NUEVA: condiciones médicas
        if (tiquete instanceof TiqueteGeneral || tiquete instanceof TiqueteTemporada || tiquete instanceof EntradaIndividual) {
            if (tiquete.getIdentificacionComprador() != null) {
                Usuario comprador = tiquete.getComprador();
                if (comprador instanceof Cliente cliente) {
                    if (!cliente.cumpleRestriccionesAtraccion(
                            atraccion.getAlturaMinima(), atraccion.getAlturaMaxima(),
                            atraccion.getPesoMinimo(), atraccion.getPesoMaximo(),
                            atraccion.getRestriccionesMedicas())) {
                        throw new TiqueteInvalidoException("El cliente no cumple las restricciones médicas o físicas para esta atracción.");
                    }
                }
            }
        }

        boolean accesoPermitido = false;
        CategoriaTiquete categoriaTiquete = null;

        if (tiquete instanceof TiqueteGeneral) {
            categoriaTiquete = ((TiqueteGeneral) tiquete).getCategoria();
            accesoPermitido = nivelReq.permiteAccesoConCategoria(categoriaTiquete);
        } else if (tiquete instanceof TiqueteTemporada) {
            categoriaTiquete = ((TiqueteTemporada) tiquete).getCategoria();
            accesoPermitido = nivelReq.permiteAccesoConCategoria(categoriaTiquete);
        } else if (tiquete instanceof EntradaIndividual) {
            accesoPermitido = ((EntradaIndividual) tiquete).esValidoParaAtraccion(atraccion);
            if (!accesoPermitido) {
                throw new TiqueteInvalidoException("Esta Entrada Individual no es válida para la atracción '" + atraccion.getNombre() + "'.");
            }
        } else if (tiquete instanceof FastPass) {
            throw new TiqueteInvalidoException("Un FastPass por sí solo no otorga acceso a la atracción. Se requiere un tiquete base válido.");
        }

        if (!accesoPermitido && categoriaTiquete != null) {
            throw new TiqueteInvalidoException("El tiquete categoría '" + categoriaTiquete + "' no permite acceso a '" + atraccion.getNombre() + "' (Nivel: " + nivelReq + ").");
        }

        return true;
    }

    public void registrarUsoTiquete(Tiquete tiquete) {
        if (tiquete == null) {
            throw new DatosInvalidosException("El tiquete a registrar no puede ser nulo.");
        }
        tiquete.marcarComoUtilizado();
        tiquetesVendidos.put(tiquete.getCodigo(), tiquete);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        System.out.println("Uso registrado para tiquete: " + tiquete.getCodigo());
    }

    public List<Tiquete> consultarTiquetesPorUsuario(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new DatosInvalidosException("La identificación no puede ser nula ni vacía.");
        }
        return tiquetesVendidos.values().stream()
                .filter(t -> identificacion.equals(t.getIdentificacionComprador()))
                .toList();
    }

    public java.util.List<Tiquete> consultarTiquetesPorFecha(java.time.LocalDate fecha) {
        if (fecha == null) {
            throw new DatosInvalidosException("La fecha no puede ser nula.");
        }
        return tiquetesVendidos.values().stream()
                .filter(t -> t.getFechaHoraEmision().toLocalDate().equals(fecha))
                .toList();
    }

    public <T extends Tiquete> java.util.List<T> consultarTiquetesPorTipo(Class<T> tipo) {
        if (tipo == null) {
            throw new DatosInvalidosException("El tipo de tiquete no puede ser nulo.");
        }
        return tiquetesVendidos.values().stream()
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .toList();
    }

    public void eliminarTiquetesPorUsuario(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new DatosInvalidosException("La identificación no puede ser nula ni vacía.");
        }
        List<String> codigosAEliminar = tiquetesVendidos.values().stream()
                .filter(t -> identificacion.equals(t.getIdentificacionComprador()))
                .map(Tiquete::getCodigo)
                .toList();
        for (String codigo : codigosAEliminar) {
            tiquetesVendidos.remove(codigo);
        }
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
    }

    public <T extends Tiquete> void eliminarTiquetesPorTipo(Class<T> tipo) {
        if (tipo == null) {
            throw new DatosInvalidosException("El tipo de tiquete no puede ser nulo.");
        }
        List<String> codigosAEliminar = tiquetesVendidos.values().stream()
                .filter(tipo::isInstance)
                .map(Tiquete::getCodigo)
                .toList();
        for (String codigo : codigosAEliminar) {
            tiquetesVendidos.remove(codigo);
        }
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
    }
}
