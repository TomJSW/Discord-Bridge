package net.cookietom.discordbridge;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.cookietom.discordbridge.config.DiscordBridgeConfig;
import net.cookietom.discordbridge.listeners.discord.DiscordMessageListener;
import net.cookietom.discordbridge.listeners.minecraft.PlayerChatListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

public final class DiscordBridgePlugin extends JavaPlugin {
	private JDA jda;

	@Getter
	private WebhookClient webhookClient;

	@Getter
	private DiscordBridgeConfig pluginConfig;

	@SneakyThrows
	@Override
	public void onEnable() {
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);

		saveDefaultConfig();

		try {
			this.pluginConfig = DiscordBridgeConfig.fromConfig(config);
		} catch (InvalidConfigurationException ex) {
			this.getLogger().severe("Plugin set-up is not complete yet. " + ex.getMessage());
			this.getPluginLoader().disablePlugin(this);
			return;
		}

		jda = JDABuilder
				.createDefault(config.getString("bot-token"))
				.enableIntents(EnumSet.allOf(GatewayIntent.class))
				.build();

		WebhookClientBuilder webhookClientBuilder = new WebhookClientBuilder(pluginConfig.webhook());
		this.webhookClient = webhookClientBuilder.build();

		jda.awaitReady();

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerChatListener(this), this);
		jda.addEventListener(new DiscordMessageListener(this));
	}

	@Override
	public void onDisable() {
		if (jda != null) jda.shutdownNow();
		if (webhookClient != null) webhookClient.close();
	}
}
