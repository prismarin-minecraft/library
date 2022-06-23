package in.prismar.library.file.yaml;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simplify yml file creation
 */
public class YamlConfig {

    private static final Yaml YAML = new Yaml();

    private File file;
    private File directory;

    private Map<String, Object> entity;

    /**
     * Load or create file with given path and file name
     *
     * @param path
     * @param fileName
     */
    public YamlConfig(String path, String fileName) {
        this.directory = new File(path);
        this.directory.mkdirs();
        this.file = new File(path.concat(fileName).concat(".yml"));
        load();
    }

    /**
     * Delete file
     */
    public void delete() {
        if(file.exists()) {
            file.delete();
        }
    }

    /**
     * Set key and value into entity
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        this.entity.put(key, value);
    }

    /**
     * Get value from entity without casting it to the desired object
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getGeneric(String key) {
        return (T) this.get(key);
    }

    /**
     * Get value from entity
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return this.entity.get(key);
    }

    /**
     * Check if key exists
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return this.entity.containsKey(key);
    }

    /**
     * Remove key
     *
     * @param key
     * @return = true if the key was successfully removed from the entity
     */
    public boolean remove(String key) {
        if(contains(key)) {
            this.entity.remove(key);
            return true;
        }
        return false;
    }

    /**
     * Load or create file with desired default values
     * It can be used to reload a file too.
     */
    public void load() {
        try {
            if(file.exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                this.entity = YAML.load(reader);
                reader.close();
            } else {
                 this.entity = new LinkedHashMap<>();
                 this.writeDefaults();
                 this.save();
            }
        }catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Error while trying to load ("+file.getName()+") file.");
        }
    }

    /**
     * Save entity into file
     */
    public void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
            writer.write(YAML.dumpAsMap(entity));
            writer.flush();

            writer.close();
            fileOutputStream.close();
        }catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Error while trying to save ("+file.getName()+") file.");
        }
    }

    /**
     * If the file was created for the first time
     *
     * For override purpose
     */
    public void writeDefaults() {}
}
