package in.prismar.library.spigot.text;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class InteractiveTextBuilder {

    private List<InteractiveText> texts;

    public InteractiveTextBuilder() {
        this.texts = new ArrayList<>();
    }

    public InteractiveTextBuilder(String text) {
        this();
        addText(text);
    }

    public InteractiveTextBuilder addText(InteractiveText text) {
        this.texts.add(text);
        return this;
    }

    public InteractiveTextBuilder addText(String text) {
        return addText(new InteractiveText(text));
    }

    public InteractiveTextBuilder addText(String text, String click) {
        return addText(new InteractiveText(text).click(click));
    }

    public InteractiveTextBuilder addText(String text, String click, String hover) {
        return addText(new InteractiveText(text).click(click).hover(hover));
    }

    public TextComponent build() {
        TextComponent main = new TextComponent();
        for(InteractiveText text : texts) {
            main.addExtra(text.toComponent());
        }
        return main;
    }

    public InteractiveTextBuilder send(Player player) {
        player.spigot().sendMessage(build());
        return this;
    }

    @Getter
    public class InteractiveText {

        private final String text;
        private String clickText;
        private String hoverText;

        public InteractiveText(String text) {
            this.text = text;
        }

        public InteractiveText click(String text) {
            this.clickText = text;
            return this;
        }

        public InteractiveText hover(String text) {
            this.hoverText = text;
            return this;
        }

        public TextComponent toComponent() {
            TextComponent component = new TextComponent(text);
            if(clickText != null) {
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickText));
            }
            if(hoverText != null) {
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));
            }
            return component;
        }

    }


}
