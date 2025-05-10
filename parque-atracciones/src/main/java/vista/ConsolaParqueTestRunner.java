package vista;

import java.io.*;
import java.util.concurrent.*;

/**
 * Simula la interacción de usuario con ConsolaParque para pruebas automatizadas.
 * Esta clase redirige System.in, System.out y System.err
 * para permitir la alimentación automática de entradas y la captura de salidas.
 *
 * Ejecuta esta clase usando "Ejecutar como -> Aplicación Java" en Eclipse.
 * La salida de la consola mostrará el registro de la interacción simulada.
 */
public class ConsolaParqueTestRunner {

    private static final PrintStream salidaOriginal = System.out;
    private static final PrintStream errorOriginal = System.err;
    private static final InputStream entradaOriginal = System.in;

    private static PipedOutputStream alimentadorEntrada;
    private static PipedInputStream entradaApp;
    private static ByteArrayOutputStream capturaSalidaApp;
    private static ByteArrayOutputStream capturaErrorApp;
    private static PrintStream flujoSalidaApp;
    private static PrintStream flujoErrorApp;

    public static void main(String[] args) throws Exception {
        configurarFlujos();

        // Ejecutar ConsolaParque.main en un hilo separado
        ExecutorService ejecutor = Executors.newSingleThreadExecutor();
        Future<?> futuroApp = enviarApp(ejecutor);

        // --- Escenarios de Prueba ---
        try {
            Thread.sleep(500); // Espera para mensaje de bienvenida
            leerYRegistrarSalida("Salida Inicial (Ejecución 1)");

            // Escenario 1: Autenticación fallida de empleado y salir
            simularInteraccion("Autenticación fallida de empleado y salir",
                "2", // Seleccionar rol Empleado
                "usuarioinexistente", // ID
                "contrasenaerronea", // Contraseña
                "3" // Salir del programa principal
            );
            esperarProcesamientoApp(futuroApp, 5);

            // --- Reinicio para Escenario 2 ---
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 1");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 2)");

            // Escenario 2: Autenticación Admin exitosa, consultas básicas y salir
            simularInteraccion("Autenticación Admin y Consultas",
                "2", // Rol Empleado
                "E005", // ID Admin
                "adminpass", // Contraseña
                "1", // Ver Atracciones
                "2", // Ver Empleados
                "15" // Salir (opción admin)
            );
            esperarProcesamientoApp(futuroApp, 10);

            // Escenario 3: Admin Crea un Nuevo Empleado (Cajero)
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 2");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 3)");

            simularInteraccion("Admin Crea Cajero",
                "2", // Rol Empleado
                "E005", "adminpass",
                "5", // Crear Empleado
                "Cajero", // Tipo
                "EMPTEST01", // ID
                "Test Cajero", // Nombre
                "cajero@test.com", // Correo
                "555-1234", // Teléfono
                "cajeroUser", // Usuario
                "cajeroPass", // Contraseña
                "3", // Caja
                "Entrada Principal", // Punto Venta
                "15" // Salir Menú Admin
            );
            esperarProcesamientoApp(futuroApp, 15);

            // Escenario 4: Admin Crea Atracción Cultural
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 3");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 4)");

            simularInteraccion("Admin Crea Atracción Cultural",
                "2", // Rol Empleado
                "E005", "adminpass",
                "6", // Crear Atracción Cultural
                "ACULT01", // ID
                "Museo Test", // Nombre
                "Zona Cultural", // Ubicación
                "50", // Cupo
                "2", // Min Emp
                "6", // Edad Min
                "FAMILIAR", // Nivel Exclusividad
                "15" // Salir Menú Admin
            );
            esperarProcesamientoApp(futuroApp, 15);

            // Escenario 5: Admin Crea Atracción Mecánica
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 4");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 5)");

            simularInteraccion("Admin Crea Atracción Mecánica",
                "2", // Rol Empleado
                "E005", "adminpass",
                "7", // Crear Atracción Mecánica
                "AMEC01", // ID
                "Montaña Rusa Test", // Nombre
                "Zona Adrenalina", // Ubicación
                "24", // Cupo
                "3", // Min Emp
                "ORO", // Nivel Exclusividad
                "ALTO", // Nivel Riesgo
                "1.4", // Altura Min
                "2.0", // Altura Max
                "40", // Peso Min
                "120", // Peso Max
                "Problemas cardiacos", // Contraindicaciones
                "No objetos sueltos", // Restricciones
                "15" // Salir Menú Admin
            );
            esperarProcesamientoApp(futuroApp, 20);

            // Escenario 6: Admin Crea Espectáculo
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 5");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 6)");

            simularInteraccion("Admin Crea Espectáculo",
                "2", // Rol Empleado
                "E005", "adminpass",
                "8", // Crear Espectáculo
                "ESPEC01", // ID
                "Show Nocturno Test", // Nombre
                "Teatro Principal", // Ubicación
                "200", // Cupo
                "Show de luces y sonido", // Descripción
                "1", // Cantidad Horarios
                "2025-07-15T20:00:00", // Inicio Horario 1
                "2025-07-15T21:00:00", // Fin Horario 1
                "15" // Salir Menú Admin
            );
            esperarProcesamientoApp(futuroApp, 15);

            // Escenario 7: Admin Actualiza y Elimina Atracción
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 6");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 7)");

            simularInteraccion("Admin Actualiza y Elimina Atracción",
                "2", // Rol Empleado
                "E005", "adminpass",
                "9", // Actualizar Atracción
                "ACULT01", // ID a actualizar
                "Museo Test Actualizado", // Nuevo Nombre
                "10", // Eliminar Atracción
                "ACULT01", // ID a eliminar
                "15" // Salir Menú Admin
            );
            esperarProcesamientoApp(futuroApp, 15);

            // Escenario 8: Admin Actualiza y Elimina Empleado
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 7");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 8)");

            simularInteraccion("Admin Actualiza y Elimina Empleado",
                "2", // Rol Empleado
                "E005", "adminpass",
                "11", // Actualizar Empleado
                "EMPTEST01", // ID a actualizar
                "Test Cajero Actualizado", // Nuevo Nombre
                "12", // Eliminar Empleado
                "EMPTEST01", // ID a eliminar
                "15" // Salir Menú Admin
            );
            esperarProcesamientoApp(futuroApp, 15);

            // Escenario 9: Admin Asigna Turno
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 8");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 9)");

            simularInteraccion("Admin Asigna Turno",
                "2", // Rol Empleado
                "E005", "adminpass",
                "13", // Asignar Turno
                "E001", // ID Empleado
                "2025-08-01", // Fecha
                "APERTURA", // Turno
                "AMEC01", // Lugar Trabajo
                "15" // Salir Menú Admin
            );
            esperarProcesamientoApp(futuroApp, 15);

            // Escenario 10: Consultas Avanzadas Admin
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 9");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 10)");

            simularInteraccion("Consultas Avanzadas Admin",
                "2", // Rol Empleado
                "E005", "adminpass",
                "14", // Consultas Avanzadas
                "1", // Consulta por Exclusividad
                "ORO", // Nivel
                "14", // Consultas Avanzadas
                "2", // Consulta Mecánicas por Riesgo
                "ALTO", // Nivel Riesgo
                "15" // Salir Menú Admin
            );
            esperarProcesamientoApp(futuroApp, 15);

            // Escenario 11: Acciones Empleado Regular (No Admin)
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 10");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 11)");

            simularInteraccion("Acciones Empleado Regular",
                "2", // Rol Empleado
                "E001", // ID Empleado
                "juanpass", // Contraseña
                "1", // Ver Atracciones
                "2", // Ver Empleados
                "3", // Vender Tiquete
                "CUST001", // ID Comprador
                "Test Customer", // Nombre Comprador
                "general", // Tipo Tiquete
                "ORO", // Categoría
                "150.0", // Precio
                "4", // Consultar Tiquetes por Usuario
                "CUST001", // ID Usuario
                "5" // Salir Menú Empleado
            );
            esperarProcesamientoApp(futuroApp, 20);
            
            // Escenario 12: Interacción Cliente (ConsolaCliente)
            reiniciarYRestablecer(ejecutor, futuroApp, "Escenario 11");
            ejecutor = Executors.newSingleThreadExecutor();
            futuroApp = enviarApp(ejecutor);
            Thread.sleep(500);
            leerYRegistrarSalida("Salida Inicial (Ejecución 12)");

            simularInteraccion("Interacción Cliente",
                "1", // Rol Cliente
                "1", // Ver Atracciones
                "2", // Comprar tiquete
                "CUST002", // ID Cliente
                "Cliente Test", // Nombre
                "cliente2@test.com", // Email
                "1", // Tipo de tiquete: General
                "FAMILIAR", // Categoría
                "100.0", // Precio
                "3", // Ver mis tiquetes
                "CUST002", // ID Cliente para consulta
                "5" // Salir Menú Cliente
            );
            esperarProcesamientoApp(futuroApp, 20);

        } finally {
            reiniciarYRestablecer(ejecutor, futuroApp, "Limpieza Final");
            restaurarFlujos();
            salidaOriginal.println("\n--- Pruebas Finalizadas ---");
        }
    }

    /**
     * Configura los flujos de entrada y salida para la simulación.
     */
    private static void configurarFlujos() throws IOException {
        alimentadorEntrada = new PipedOutputStream();
        entradaApp = new PipedInputStream(alimentadorEntrada);
        System.setIn(entradaApp);

        capturaSalidaApp = new ByteArrayOutputStream();
        flujoSalidaApp = new PrintStream(capturaSalidaApp, true);
        System.setOut(flujoSalidaApp);

        capturaErrorApp = new ByteArrayOutputStream();
        flujoErrorApp = new PrintStream(capturaErrorApp, true);
        System.setErr(flujoErrorApp);
    }

    /**
     * Restaura los flujos originales del sistema.
     */
    private static void restaurarFlujos() {
        System.setOut(salidaOriginal);
        System.setErr(errorOriginal);
        System.setIn(entradaOriginal);
        cerrarSilencioso(alimentadorEntrada);
        cerrarSilencioso(entradaApp);
        cerrarSilencioso(flujoSalidaApp);
        cerrarSilencioso(flujoErrorApp);
        cerrarSilencioso(capturaSalidaApp);
        cerrarSilencioso(capturaErrorApp);
    }

    /**
     * Simula una secuencia de entradas de usuario y registra la interacción.
     * @param nombreEscenario Nombre para el registro.
     * @param entradas Secuencia de cadenas representando las entradas del usuario.
     */
    private static void simularInteraccion(String nombreEscenario, String... entradas) throws IOException, InterruptedException {
        salidaOriginal.println("\n--- Ejecutando Escenario: " + nombreEscenario + " ---");
        for (String entrada : entradas) {
            salidaOriginal.println(">>> Entrada: " + entrada);
            alimentadorEntrada.write((entrada + System.lineSeparator()).getBytes());
            alimentadorEntrada.flush();
            Thread.sleep(500); // Espera para procesar
            leerYRegistrarSalida("Salida tras '" + entrada + "'");
        }
        salidaOriginal.println("--- Escenario finalizado: " + nombreEscenario + " ---");
    }

    /**
     * Lee la salida y error capturados y los imprime en la consola original.
     * @param contexto Etiqueta para el momento de captura.
     */
    private static void leerYRegistrarSalida(String contexto) {
        String salida = capturaSalidaApp.toString();
        String error = capturaErrorApp.toString();

        if (!salida.isEmpty()) {
            salidaOriginal.println("--- Salida Capturada (" + contexto + ") ---");
            salidaOriginal.print(salida);
            salidaOriginal.println("------------------------------------");
            capturaSalidaApp.reset();
        }
        if (!error.isEmpty()) {
            errorOriginal.println("--- Error Capturado (" + contexto + ") ---");
            errorOriginal.print(error);
            errorOriginal.println("------------------------------------");
            capturaErrorApp.reset();
        }
        if (salida.isEmpty() && error.isEmpty()) {
            salidaOriginal.println("--- No hay nueva salida capturada (" + contexto + ") ---");
        }
    }

    /**
     * Espera a que el hilo de la aplicación termine, con tiempo máximo.
     * Lee la salida final.
     * @param futuro El Future del hilo de la aplicación.
     * @param segundosTimeout Tiempo máximo de espera.
     */
    private static void esperarProcesamientoApp(Future<?> futuro, int segundosTimeout) throws InterruptedException {
        salidaOriginal.println("\n--- Esperando procesamiento de la app (Tiempo máximo: " + segundosTimeout + "s) ---");
        try {
            futuro.get(segundosTimeout, TimeUnit.SECONDS);
            salidaOriginal.println("--- Hilo de la app finalizó normalmente. ---");
        } catch (TimeoutException e) {
            salidaOriginal.println("--- Hilo de la app excedió el tiempo. ---");
        } catch (ExecutionException e) {
            errorOriginal.println("--- Hilo de la app lanzó una excepción: " + e.getCause() + " ---");
            e.getCause().printStackTrace(errorOriginal);
        } catch (CancellationException e) {
            salidaOriginal.println("--- Hilo de la app fue cancelado. ---");
        } finally {
            leerYRegistrarSalida("Salida Final");
        }
    }

    /** Envía ConsolaParque.main a un ejecutor */
    private static Future<?> enviarApp(ExecutorService ejecutor) {
        return ejecutor.submit(() -> {
            try {
                ConsolaParque.main(new String[]{});
            } catch (Exception e) {
                errorOriginal.println("Excepción en el hilo de ConsolaParque: " + e.getMessage());
                e.printStackTrace(errorOriginal);
            } finally {
                cerrarSilencioso(entradaApp);
            }
        });
    }

    /** Apaga el ejecutor y restablece los flujos */
    private static void reiniciarYRestablecer(ExecutorService ejecutor, Future<?> futuroApp, String contexto) throws IOException, InterruptedException {
        salidaOriginal.println("\n--- Reiniciando tras " + contexto + " ---");
        if (ejecutor != null && !ejecutor.isTerminated()) {
            if (futuroApp != null && !futuroApp.isDone()) {
                salidaOriginal.println("--- La app no salió limpiamente, interrumpiendo. ---");
                futuroApp.cancel(true);
                try {
                    futuroApp.get(1, TimeUnit.SECONDS);
                } catch (Exception e) { /* Ignorar */ }
            }
            ejecutor.shutdownNow();
            if (!ejecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                errorOriginal.println("--- El ejecutor no terminó tras shutdownNow. ---");
            } else {
                salidaOriginal.println("--- Ejecutar terminado. ---");
            }
        }
        restaurarFlujos();
        configurarFlujos();
        salidaOriginal.println("--- Flujos restablecidos para el siguiente escenario ---");
    }

    /** Cierra flujos de forma silenciosa */
    private static void cerrarSilencioso(Closeable cerrable) {
        if (cerrable != null) {
            try {
                cerrable.close();
            } catch (IOException e) {
                // Ignorar
            }
        }
    }
}
