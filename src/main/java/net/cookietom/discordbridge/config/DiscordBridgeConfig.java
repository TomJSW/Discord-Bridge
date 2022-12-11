package net.cookietom.discordbridge.config;


import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public record DiscordBridgeConfig(
		String botToken,
		String webhook,
		String channelId,
		String messageFormat,
		boolean ansi
) {
	private static void validate(boolean condition, String message) throws InvalidConfigurationException {
		if (!condition) {
			throw new InvalidConfigurationException(message);
		}
	}

	private static void requireNonBlank(String value, String field) throws InvalidConfigurationException {
		validate(StringUtils.isNotBlank(value), field + " cannot be blank or empty in config.");
	}

	public static DiscordBridgeConfig fromConfig(ConfigurationSection config) throws InvalidConfigurationException {
		String botToken = config.getString("bot-token");
		String webhook = config.getString("webhook");
		String channelId = config.getString("channel-id");
		String messageFormat = config.getString("message-format");

		requireNonBlank(botToken, "bot-token");
		requireNonBlank(webhook, "webhook");
		requireNonBlank(channelId, "channel-id");
		requireNonBlank(messageFormat, "message-format");
		validate(config.contains("ansi"), "ansi is missing from config.");
		boolean ansi = config.getBoolean("ansi");

		return new DiscordBridgeConfig(
				botToken,
				webhook,
				channelId,
				messageFormat,
				ansi
		);
	}
}
