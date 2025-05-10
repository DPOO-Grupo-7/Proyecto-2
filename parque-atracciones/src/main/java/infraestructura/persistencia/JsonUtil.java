package infraestructura.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad para leer y escribir objetos en formato JSON.
 */
public class JsonUtil {
    private static final String PARQUE_ROOT_MARKER = "parque-atracciones"; // Directory name to identify the root
    private static final String DATA_DIR_NAME = "data"; // Name of the data directory

    /**
     * Devuelve la ruta absoluta y normalizada a un archivo dentro del directorio de datos.
     * Ejemplo: getDataFilePath("culturales.json") -> <absoluto>/data/culturales.json
     *          getDataFilePath("elementos", "culturales.json") -> <absoluto>/data/elementos/culturales.json
     */
    public static String getDataFilePath(String... pathParts) {
        String base = getParqueAtraccionesRoot();
        java.nio.file.Path dataDir = java.nio.file.Paths.get(base, DATA_DIR_NAME);
        java.nio.file.Path fullPath = dataDir;
        for (String part : pathParts) {
            fullPath = fullPath.resolve(part);
        }
        // Ensure the directory exists before returning the path
        try {
            java.nio.file.Files.createDirectories(fullPath.getParent());
        } catch (IOException e) {
             // Log or handle the exception if directory creation fails, but proceed for now
             System.err.println("Advertencia: No se pudo crear el directorio padre para " + fullPath + ": " + e.getMessage());
        }
        return fullPath.toAbsolutePath().normalize().toString();
    }

    private static String getParqueAtraccionesRoot() {
        // Busca la carpeta que contiene la clase JsonUtil y sube hasta 'parque-atracciones'
        try {
            // Use getResource to find the path relative to the class file
            java.net.URL resource = JsonUtil.class.getResource(JsonUtil.class.getSimpleName() + ".class");
            if (resource == null) {
                 throw new IllegalStateException("No se pudo encontrar el recurso de la clase JsonUtil.");
            }
            java.nio.file.Path path = java.nio.file.Paths.get(resource.toURI());
            // Navigate up until we find the marker directory or reach the root
            while (path != null && !path.getFileName().toString().equals(PARQUE_ROOT_MARKER)) {
                path = path.getParent();
            }
            if (path != null) {
                return path.toString();
            } else {
                 throw new IllegalStateException("No se pudo encontrar el directorio raíz '" + PARQUE_ROOT_MARKER + "' subiendo desde " + resource);
            }
        } catch (java.net.URISyntaxException e) {
            throw new RuntimeException("Error al convertir la URL del recurso a URI: " + e.getMessage(), e);
        } catch (Exception e) {
            // Catch other potential exceptions during path resolution
             throw new RuntimeException("Error inesperado al determinar el directorio raíz del parque: " + e.getMessage(), e);
        }
        // Removed the unreliable user.dir fallback
    }
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            // Add lenient parsing to handle potential minor JSON format issues
            .setLenient()
            .create();

    /**
     * Guarda una lista de objetos en un archivo JSON (ruta absoluta).
     *
     * @param absolutePath Ruta absoluta del archivo.
     * @param objects Lista de objetos a guardar.
     * @param <T> Tipo de los objetos.
     */
    public static <T> void writeToFileAbsolute(String absolutePath, List<T> objects) {
        // Ensure parent directory exists
        java.nio.file.Path filePath = java.nio.file.Paths.get(absolutePath);
        try {
            java.nio.file.Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            System.err.println("Advertencia: No se pudo crear el directorio padre para " + absolutePath + ": " + e.getMessage());
            // Decide if you want to throw or just log
            // throw new RuntimeException("No se pudo crear el directorio padre para " + absolutePath, e);
        }

        try (FileWriter writer = new FileWriter(absolutePath)) {
            gson.toJson(objects, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en el archivo " + absolutePath, e);
        } catch (com.google.gson.JsonIOException e) {
             throw new RuntimeException("Error de E/S de GSON al escribir en el archivo " + absolutePath, e);
        }
    }

    /**
     * Lee una lista de objetos desde un archivo JSON (ruta absoluta).
     * Si el archivo no existe, devuelve una lista vacía.
     *
     * @param absolutePath Ruta absoluta del archivo.
     * @param typeToken TypeToken para el tipo de la lista.
     * @param <T> Tipo de los objetos en la lista.
     * @return Lista de objetos del tipo especificado.
     */
    public static <T> List<T> readFromFileAbsolute(String absolutePath, TypeToken<List<T>> typeToken) {
        File file = new File(absolutePath);
        if (!file.exists()) {
            return new ArrayList<>(); // Return empty list if file doesn't exist
        }
        if (!file.isFile()) {
             System.err.println("[ERROR] La ruta no es un archivo: " + absolutePath);
             return new ArrayList<>();
        }
         if (!file.canRead()) {
             System.err.println("[ERROR] No se puede leer el archivo: " + absolutePath);
             return new ArrayList<>();
         }

        try (FileReader reader = new FileReader(file)) {
            Type type = typeToken.getType();
            List<T> result = gson.fromJson(reader, type);
            return result != null ? result : new ArrayList<>(); // Ensure non-null return
        } catch (IOException e) {
            // Log the error and return an empty list or rethrow as RuntimeException
            System.err.println("Error de E/S al leer el archivo " + absolutePath + ": " + e.getMessage());
            // Consider logging the stack trace: e.printStackTrace();
            return new ArrayList<>(); // Or throw new RuntimeException(...)
        } catch (com.google.gson.JsonSyntaxException e) {
            System.err.println("Error de sintaxis JSON en el archivo " + absolutePath + ": " + e.getMessage());
            // Consider logging the stack trace: e.printStackTrace();
            return new ArrayList<>(); // Or throw new RuntimeException(...)
        } catch (com.google.gson.JsonIOException e) {
             System.err.println("Error de E/S de GSON al leer el archivo " + absolutePath + ": " + e.getMessage());
             return new ArrayList<>(); // Or throw new RuntimeException(...)
        }
    }
}
