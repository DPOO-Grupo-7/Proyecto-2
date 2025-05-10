package vista.gui.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase de utilidad para gestionar los permisos de acceso a funcionalidades
 * según el rol del usuario (Administrador, Empleado, Cliente).
 */
public class PermisoUI {
    // Define permission constants
    public static final int PERMISO_VER = 1;
    public static final int PERMISO_EDITAR = 2;
    public static final int PERMISO_ELIMINAR = 4;
    public static final int PERMISO_CREAR = 8;
    
    // Role-based permission maps
    private static final Map<String, Integer> permisosAtracciones = new HashMap<>();
    private static final Map<String, Integer> permisosEmpleados = new HashMap<>();
    private static final Map<String, Integer> permisosClientes = new HashMap<>();
    private static final Map<String, Integer> permisosTiquetes = new HashMap<>();
    private static final Map<String, Integer> permisosReportes = new HashMap<>();
    
    static {
        // Atracciones permissions
        permisosAtracciones.put("Administrador", PERMISO_VER | PERMISO_EDITAR | PERMISO_ELIMINAR | PERMISO_CREAR);
        permisosAtracciones.put("Empleado", PERMISO_VER);
        permisosAtracciones.put("Cliente", PERMISO_VER);
        
        // Empleados permissions
        permisosEmpleados.put("Administrador", PERMISO_VER | PERMISO_EDITAR | PERMISO_ELIMINAR | PERMISO_CREAR);
        permisosEmpleados.put("Empleado", PERMISO_VER);
        permisosEmpleados.put("Cliente", 0); // No permissions
        
        // Clientes permissions
        permisosClientes.put("Administrador", PERMISO_VER | PERMISO_EDITAR | PERMISO_ELIMINAR | PERMISO_CREAR);
        permisosClientes.put("Empleado", PERMISO_VER | PERMISO_CREAR);
        permisosClientes.put("Cliente", PERMISO_VER | PERMISO_EDITAR); // Solo puede ver y editar su propio perfil
        
        // Tiquetes permissions
        permisosTiquetes.put("Administrador", PERMISO_VER | PERMISO_EDITAR | PERMISO_ELIMINAR | PERMISO_CREAR);
        permisosTiquetes.put("Empleado", PERMISO_VER | PERMISO_CREAR);
        permisosTiquetes.put("Cliente", PERMISO_VER | PERMISO_CREAR);
        
        // Reportes permissions
        permisosReportes.put("Administrador", PERMISO_VER | PERMISO_CREAR);
        permisosReportes.put("Empleado", PERMISO_VER);
        permisosReportes.put("Cliente", 0); // No permissions
    }
    
    /**
     * Verifica si un usuario con un rol específico tiene un permiso para un módulo.
     * 
     * @param rol El rol del usuario (Administrador, Empleado, Cliente)
     * @param modulo El módulo al que se intenta acceder
     * @param permiso El permiso que se quiere verificar
     * @return true si el usuario tiene el permiso, false en caso contrario
     */
    public static boolean tienePermiso(String rol, String modulo, int permiso) {
        Map<String, Integer> mapaPermisos;
        
        switch (modulo) {
            case "atracciones":
                mapaPermisos = permisosAtracciones;
                break;
            case "empleados":
                mapaPermisos = permisosEmpleados;
                break;
            case "clientes":
                mapaPermisos = permisosClientes;
                break;
            case "tiquetes":
                mapaPermisos = permisosTiquetes;
                break;
            case "reportes":
                mapaPermisos = permisosReportes;
                break;
            default:
                return false;
        }
        
        Integer permisosRol = mapaPermisos.get(rol);
        return permisosRol != null && (permisosRol & permiso) == permiso;
    }
}
