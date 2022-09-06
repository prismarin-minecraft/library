package in.prismar.library.spigot.file;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.prismar.library.spigot.location.LocationUtil;
import org.bukkit.Location;

import java.io.IOException;

public class GsonLocationAdapter extends TypeAdapter<Location> {

    public void write(JsonWriter jsonWriter, Location location) throws IOException {
        jsonWriter.value(LocationUtil.locationToString(location));
    }

    public Location read(JsonReader jsonReader) throws IOException {
        return LocationUtil.stringToLocation(jsonReader.nextString());
    }
}