package mc.toriset.gloriousAuth.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class AdventureUtils {
    private static final MiniMessage miniMessage = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.color())
                    .resolver(StandardTags.decorations())
                    .resolver(StandardTags.gradient())
                    .resolver(StandardTags.rainbow())
                    .build())
            .build();

    /**
     * Creates a component with hex color
     * @param hex The hexadecimal color code (e.g. "#ff0000" or "ff0000")
     * @param text The text to color
     * @return Colored Component
     */
    public static Component hex(String hex, String text) {
        // Remove # if present
        hex = hex.replace("#", "");
        
        // Validate hex length
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Hex color must be 6 characters long!");
        }
        
        return Component.text(text)
                .color(TextColor.fromHexString("#" + hex))
                .decoration(TextDecoration.ITALIC, false);
    }
    
    /**
     * Creates a component with RGB color
     * @param red Red value (0-255)
     * @param green Green value (0-255)
     * @param blue Blue value (0-255)
     * @param text The text to color
     * @return Colored Component
     */
    public static Component rgb(int red, int green, int blue, String text) {
        // Validate RGB values
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0 and 255!");
        }
        
        return Component.text(text)
                .color(TextColor.color(red, green, blue))
                .decoration(TextDecoration.ITALIC, false);
    }
    
    /**
     * Parse text using MiniMessage format
     * Supports various tags like <color>, <gradient>, <rainbow>, etc.
     * @param text The text to parse with MiniMessage format
     * @return Parsed Component
     */
    public static Component parse(String text) {
        return miniMessage.deserialize(text);
    }
    
    /**
     * Creates a gradient effect between colors
     * @param text The text content
     * @param colors Array of hex colors to gradient between
     * @return Component with gradient effect
     */
    public static Component gradient(String text, String... colors) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("At least 2 colors are required for a gradient!");
        }

        StringBuilder miniMessageFormat = new StringBuilder("<gradient:");
        for (int i = 0; i < colors.length; i++) {
            miniMessageFormat.append("#");
            miniMessageFormat.append( colors[i].replace("#", ""));
            if (i < colors.length - 1) {
                miniMessageFormat.append(":");
            }
        }
        miniMessageFormat.append(">").append(text).append("</gradient>");

        return parse(miniMessageFormat.toString());
    }
    
    /**
     * Creates a rainbow effect
     * @param text The text to apply rainbow to
     * @return Component with rainbow effect
     */
    public static Component rainbow(String text) {
        return miniMessage.deserialize("<rainbow>" + text + "</rainbow>")
                .decoration(TextDecoration.ITALIC, false);
    }
    
    /**
     * Converts legacy color codes to Adventure Components
     * @param legacy Text with legacy color codes (&)
     * @return Adventure Component
     */
    public static Component fromLegacy(String legacy) {
        return LegacyComponentSerializer.legacyAmpersand()
                .deserialize(legacy)
                .decoration(TextDecoration.ITALIC, false);
    }
}