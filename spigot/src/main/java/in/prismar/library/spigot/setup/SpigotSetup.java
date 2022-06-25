package in.prismar.library.spigot.setup;

import com.google.common.collect.Lists;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.command.spigot.SpigotCommandListener;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.List;

@Getter
public class SpigotSetup {

    private final String prefix;
    private final Plugin plugin;
    private final List<Command> commands;
    private final List<Listener> listeners;

    /**
     * Constructor
     * @param plugin = the main class that extends JavaPlugin
     */
    public SpigotSetup(@NonNull Plugin plugin, String prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
        this.commands = Lists.newArrayList();
        this.listeners = Lists.newArrayList();
    }

    /**
     * Add a command to list
     *
     * WARNING: It is not the default CommandExecutor
     * @param command = bukkit command
     */
    public void addCommand(@NonNull Command command){
        this.commands.add(command);
    }

    /**
     * Add a listener to list
     * @param listener = bukkit listener
     */
    public void addListener(@NonNull Listener listener){
        this.listeners.add(listener);
    }

    /**
     * Add a command based on the command util
     *
     * @param command
     */
    public void addCommand(@NonNull SpigotCommand<? extends CommandSender> command) {
        addCommand(new SpigotCommandListener<>(command));
    }

    /**
     * Register command & listener
     * Without putting the command into the plugin.yml
     */
    public void register(){
        this.commands.forEach((command -> {
            registerCommand(command);
        }));
        this.listeners.forEach((listener -> {
            getPlugin().getServer().getPluginManager().registerEvents(listener, getPlugin());
        }));
    }


    @SneakyThrows
    private void registerCommand(Command command) {
        Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        field.setAccessible(true);
        CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer());
        commandMap.register(this.prefix == null ? "plugin" : this.prefix, command);
    }
}