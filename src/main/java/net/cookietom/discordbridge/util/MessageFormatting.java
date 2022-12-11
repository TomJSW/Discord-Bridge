package net.cookietom.discordbridge.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class MessageFormatting {

	private static final Map<String, String> CODES = new HashMap<>();

	static {
		CODES.put("§0", "\u001b[30m");
		CODES.put("§1", "\u001b[34m");
		CODES.put("§2", "\u001b[32m");
		CODES.put("§3", "\u001b[36m");
		CODES.put("§4", "\u001b[31m");
		CODES.put("§5", "\u001b[35m");
		CODES.put("§6", "\u001b[33m");
		CODES.put("§7", "\u001b[37m");
		CODES.put("§8", "\u001b[90m");
		CODES.put("§9", "\u001b[94m");
		CODES.put("§a", "\u001b[92m");
		CODES.put("§b", "\u001b[96m");
		CODES.put("§c", "\u001b[91m");
		CODES.put("§d", "\u001b[95m");
		CODES.put("§e", "\u001b[93m");
		CODES.put("§f", "\u001b[97m");
		CODES.put("§l", "\u001b[1m");
		CODES.put("§o", "\u001b[3m");
		CODES.put("§n", "\u001b[4m");
		CODES.put("§m", "\u001b[9m");
		CODES.put("§k", "\u001b[6m");
		CODES.put("§r", "\u001b[0m)");
	}

	//TODO: Add support for hex colors
	// Example hex colour: §x§f§f§f§f§f§f

	public String toAnsi(Component component) {
		return toAnsi(LegacyComponentSerializer.legacySection().serialize(component));
	}

	public String toAnsi(String message) {
		for (Map.Entry<String, String> entry : CODES.entrySet()) {
			message = message.replace(entry.getKey(), entry.getValue());
		}
		return message;
	}

	public String toDiscordAnsi(Component component) {
		return "```ansi\n" + toAnsi(component) + "\n```";
	}

	public String toDiscordPlain(Component component) {
		return PlainTextComponentSerializer.plainText().serialize(component);
	}

}
