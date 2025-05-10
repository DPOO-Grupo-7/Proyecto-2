package infraestructura.persistencia;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dominio.elementoparque.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ElementoParqueRepositoryJson implements ElementoParqueRepository {

    private final String atraccionesMecanicasPath;
    private final String atraccionesCulturalesPath;
    private final String espectaculosPath;

    private List<AtraccionMecanica> atraccionesMecanicas;
    private List<AtraccionCultural> atraccionesCulturales;
    private List<Espectaculo> espectaculos;
    /**
     * Constructor for ElementoParqueRepositoryJson.
     * @param atraccionesDirName The base directory name under 'data' containing attraction type files (e.g., "elementos").
     * @param espectaculosFileName The base file name under 'data' for espectaculos (e.g., "espectaculos.json").
     */
    public ElementoParqueRepositoryJson(String atraccionesDirName, String espectaculosFileName) {
        this.atraccionesMecanicasPath = JsonUtil.getDataFilePath(atraccionesDirName, "mecanicas.json");
        this.atraccionesCulturalesPath = JsonUtil.getDataFilePath(atraccionesDirName, "culturales.json");
        this.espectaculosPath = JsonUtil.getDataFilePath(espectaculosFileName);

        new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
            .setLenient()
            .create();
        cargarDatos();
    }

    private void cargarDatos() {
        atraccionesMecanicas = cargarElementos(atraccionesMecanicasPath, new TypeToken<List<AtraccionMecanica>>() {});
        atraccionesCulturales = cargarElementos(atraccionesCulturalesPath, new TypeToken<List<AtraccionCultural>>() {});
        espectaculos = cargarElementos(espectaculosPath, new TypeToken<List<Espectaculo>>() {});
    }

    private <T> List<T> cargarElementos(String rutaArchivoAbsoluta, TypeToken<List<T>> typeToken) {
        return JsonUtil.readFromFileAbsolute(rutaArchivoAbsoluta, typeToken);
    }

    @Override
    public Atraccion save(Atraccion atraccion) {
        if (atraccion instanceof AtraccionMecanica) {
            guardarAtraccionMecanica((AtraccionMecanica) atraccion);
        } else if (atraccion instanceof AtraccionCultural) {
            guardarAtraccionCultural((AtraccionCultural) atraccion);
        }
        return atraccion;
    }

    @Override
    public Espectaculo save(Espectaculo espectaculo) {
        guardarEspectaculo(espectaculo);
        return espectaculo;
    }

    private void guardarAtraccionMecanica(AtraccionMecanica atraccion) {
        boolean found = false;
        for (int i = 0; i < atraccionesMecanicas.size(); i++) {
            if (atraccionesMecanicas.get(i).getId().equals(atraccion.getId())) {
                atraccionesMecanicas.set(i, atraccion);
                found = true;
                break;
            }
        }
        if (!found) {
            atraccionesMecanicas.add(atraccion);
        }
        JsonUtil.writeToFileAbsolute(atraccionesMecanicasPath, atraccionesMecanicas);
    }

    private void guardarAtraccionCultural(AtraccionCultural atraccion) {
        boolean found = false;
        for (int i = 0; i < atraccionesCulturales.size(); i++) {
            if (atraccionesCulturales.get(i).getId().equals(atraccion.getId())) {
                atraccionesCulturales.set(i, atraccion);
                found = true;
                break;
            }
        }
        if (!found) {
            atraccionesCulturales.add(atraccion);
        }
        JsonUtil.writeToFileAbsolute(atraccionesCulturalesPath, atraccionesCulturales);
    }

    private void guardarEspectaculo(Espectaculo espectaculo) {
        boolean found = false;
        for (int i = 0; i < espectaculos.size(); i++) {
            if (espectaculos.get(i).getId().equals(espectaculo.getId())) {
                espectaculos.set(i, espectaculo);
                found = true;
                break;
            }
        }
        if (!found) {
            espectaculos.add(espectaculo);
        }
        JsonUtil.writeToFileAbsolute(espectaculosPath, espectaculos);
    }

    @Override
    public Optional<Atraccion> findAtraccionById(String id) {
        for (AtraccionMecanica atraccion : atraccionesMecanicas) {
            if (atraccion.getId().equals(id)) {
                return Optional.of(atraccion);
            }
        }
        for (AtraccionCultural atraccion : atraccionesCulturales) {
            if (atraccion.getId().equals(id)) {
                return Optional.of(atraccion);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Espectaculo> findEspectaculoById(String id) {
        for (Espectaculo espectaculo : espectaculos) {
            if (espectaculo.getId().equals(id)) {
                return Optional.of(espectaculo);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ElementoParque> findById(String id) {
        Optional<Atraccion> atraccionOpt = findAtraccionById(id);
        if (atraccionOpt.isPresent()) {
            return Optional.of(atraccionOpt.get());
        }

        Optional<Espectaculo> espectaculoOpt = findEspectaculoById(id);
        if (espectaculoOpt.isPresent()) {
            return Optional.of(espectaculoOpt.get());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Atraccion> findAtraccionByNombre(String nombre) {
        for (AtraccionMecanica atraccion : atraccionesMecanicas) {
            if (atraccion.getNombre().equalsIgnoreCase(nombre)) {
                return Optional.of(atraccion);
            }
        }
        for (AtraccionCultural atraccion : atraccionesCulturales) {
            if (atraccion.getNombre().equalsIgnoreCase(nombre)) {
                return Optional.of(atraccion);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Espectaculo> findEspectaculoByNombre(String nombre) {
        for (Espectaculo espectaculo : espectaculos) {
            if (espectaculo.getNombre().equalsIgnoreCase(nombre)) {
                return Optional.of(espectaculo);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ElementoParque> findByNombre(String nombre) {
        Optional<Atraccion> atraccionOpt = findAtraccionByNombre(nombre);
        if (atraccionOpt.isPresent()) {
            return Optional.of(atraccionOpt.get());
        }

        Optional<Espectaculo> espectaculoOpt = findEspectaculoByNombre(nombre);
        if (espectaculoOpt.isPresent()) {
            return Optional.of(espectaculoOpt.get());
        }

        return Optional.empty();
    }

    @Override
    public List<Atraccion> findAllAtracciones() {
        List<Atraccion> todasLasAtracciones = new ArrayList<>();
        todasLasAtracciones.addAll(atraccionesMecanicas);
        todasLasAtracciones.addAll(atraccionesCulturales);
        return todasLasAtracciones;
    }

    @Override
    public List<ElementoParque> findAll() {
        List<ElementoParque> todosLosElementos = new ArrayList<>();
        todosLosElementos.addAll(atraccionesMecanicas);
        todosLosElementos.addAll(atraccionesCulturales);
        todosLosElementos.addAll(espectaculos);
        return todosLosElementos;
    }

    @Override
    public void deleteById(String id) {
        boolean removed = atraccionesMecanicas.removeIf(a -> a.getId().equals(id));
        if (removed) {
            JsonUtil.writeToFileAbsolute(atraccionesMecanicasPath, atraccionesMecanicas);
            return;
        }

        removed = atraccionesCulturales.removeIf(a -> a.getId().equals(id));
        if (removed) {
            JsonUtil.writeToFileAbsolute(atraccionesCulturalesPath, atraccionesCulturales);
            return;
        }

        removed = espectaculos.removeIf(e -> e.getId().equals(id));
        if (removed) {
            JsonUtil.writeToFileAbsolute(espectaculosPath, espectaculos);
            return;
        }
    }

    public void guardarDatos() {
        JsonUtil.writeToFileAbsolute(atraccionesMecanicasPath, atraccionesMecanicas);
        JsonUtil.writeToFileAbsolute(atraccionesCulturalesPath, atraccionesCulturales);
        JsonUtil.writeToFileAbsolute(espectaculosPath, espectaculos);
    }

    @Override
    public List<AtraccionMecanica> findAllMecanicas() {
        return new ArrayList<>(atraccionesMecanicas);
    }

    @Override
    public List<AtraccionCultural> findAllCulturales() {
        return new ArrayList<>(atraccionesCulturales);
    }

    @Override
    public List<Espectaculo> findAllEspectaculos() {
        return new ArrayList<>(espectaculos);
    }
}