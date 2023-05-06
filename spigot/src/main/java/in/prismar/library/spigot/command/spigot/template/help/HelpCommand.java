package in.prismar.library.spigot.command.spigot.template.help;

import in.prismar.library.spigot.command.CommandChildNode;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import lombok.Setter;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Setter
public abstract class HelpCommand<T extends CommandSender> extends SpigotCommand<T> {

    private String category;
    private String baseColor;
    private String upperBorder;
    private String lowerBorder;
    private String arrow;

    private int maxEntriesPerPage;

    public HelpCommand(String name, String category) {
        super(name);
        this.maxEntriesPerPage = 5;
        this.category = category;
        this.baseColor = "§a";
        this.upperBorder = "§8╔═══════════════════════╗";
        this.lowerBorder = "§8╚═══════════════════════╝";
        this.arrow = "§8» ";
    }

    @Override
    public boolean send(T player, SpigotArguments arguments) throws CommandException {
        if(!raw(player, arguments)) {
            return true;
        }
        int page = 0;
        if(arguments.getLength() >= 1) {
            try {
                page = Integer.valueOf(arguments.getString(0)) - 1;
            }catch (NumberFormatException exception) {

            }
        }
        if(page < 0) {
            page = 0;
        }
        int maxPage = getChildren().size() / maxEntriesPerPage + ((getChildren().size() % maxEntriesPerPage == 0) ? 0 : 1);
        if(maxPage <= 0) {
            maxPage++;
        }
        sendHelp(player, page, maxPage);
        return true;
    }

    public boolean raw(T player, SpigotArguments arguments) {
        return true;
    }

    public void sendHelp(T sender, int page, int maxPage) {
        sender.sendMessage(upperBorder);
        sender.sendMessage("§c");
        sender.sendMessage(" " + arrow + baseColor + category + " §8- " + baseColor + "Usage");
        sender.sendMessage("§c");

        int start = page * maxEntriesPerPage;
        int end = (page + 1) * maxEntriesPerPage;
        for (int i = start; i < end; i++) {
            if(getChildren().size() > i) {
                CommandChildNode<T, SpigotArguments> child = getChildren().get(i);
                if(child != null) {
                    if(child instanceof HelpSubCommand<T> sub) {
                        String usage = generateUsage(sub);
                        String description = generateDescription(sub);
                        sender.sendMessage(" " + arrow + "§8/" + baseColor + getName() + " " + child.getName() + " " + usage + description);
                    }
                }
            }
        }
        sender.sendMessage("§c");
        sender.sendMessage(" " + arrow + "§8/" + baseColor + getName() + " §8<" + baseColor + (page+1) + "§8/"+ baseColor + maxPage+"§8>");
        sender.sendMessage(lowerBorder);
    }

    private String generateDescription(HelpSubCommand<T> sub) {
        String description = sub.getDescription().isEmpty() ? "" : "§8│ §7" + sub.getDescription();
        return description;
    }

    private String generateUsage(HelpSubCommand<T> sub) {
        String usage = "";
        String[] usageEntries = sub.getUsage().split(" ");
        if(usageEntries.length >= 1) {
            for(String single : usageEntries) {
                usage += single.replace("<", "§8<" + baseColor).replace(">", "§8>") + " ";
            }
        } else {
            usage += " ";
        }
        return usage;
    }
}
