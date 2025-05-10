package aplicacion;
    import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dominio.usuario.Cliente;
import dominio.usuario.Usuario;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class ServicioGestionClientes {

/**
 * ServicioGestionClientes maneja la autenticación de usuarios (instancias de Usuario)
 * con persistencia en un archivo JSON.
 */

    private static final String FILE_PATH = "parque-atracciones/data/users.json";
    private Map<String, Cliente> users = new HashMap<>();
    private Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            @Override public void write(JsonWriter out, LocalDate value) throws IOException {
                out.value(value != null ? value.format(formatter) : null);
            }
            @Override public LocalDate read(JsonReader in) throws IOException {
                String date = in.nextString();
                return date != null ? LocalDate.parse(date, formatter) : null;
            }
        })
        // Adaptador para LocalDateTime
        .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            @Override public void write(JsonWriter out, LocalDateTime value) throws IOException {
                out.value(value != null ? value.format(formatter) : null);
            }
            @Override public LocalDateTime read(JsonReader in) throws IOException {
                String dateTime = in.nextString();
                return dateTime != null ? LocalDateTime.parse(dateTime, formatter) : null;
            }
        })
        .create();

    public ServicioGestionClientes() {
        loadUsers();
    }

    /**
     * Carga los usuarios desde el archivo JSON.
     */

    private void loadUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (Reader reader = new FileReader(file)) {
            Map<String, Cliente> loadedUsers = gson.fromJson(reader,
                new TypeToken<Map<String, Cliente>>(){}.getType());
            if (loadedUsers != null) {
                users.putAll(loadedUsers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda los usuarios en formato JSON.
     */

    private void saveUsers() {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public boolean isPasswordCorrect(String username, String password) {
        Usuario u = users.get(username);
        return u != null && u.verificarPassword(password);
    }

    public boolean addUser(Cliente user) {
        String username = user.getUsername();
        if (userExists(username)) return false;
        users.put(username, user);
        saveUsers();
        return true;
    }

    public boolean removeUser(Cliente user) {
        return removeUser(user.getUsername());
    }

    public boolean removeUser(String username) {
        if (!userExists(username)) return false;
        users.remove(username);
        saveUsers();
        return true;
    }
    public Usuario getUser(String username) {
        return users.get(username);
    }

    public boolean changePassword(Usuario user, String oldPassword, String newPassword) {
        if (!userExists(user.getUsername())) return false;
        try {
            user.cambiarPassword(oldPassword, newPassword);
            saveUsers();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> listUsers() {
        return new ArrayList<>(users.keySet());
    }
}


