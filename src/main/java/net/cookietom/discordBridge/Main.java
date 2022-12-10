package net.cookietom.discordBridge;

import net.cookietom.discordBridge.discord.DiscordToMC;
import net.cookietom.discordBridge.minecraft.MCToDiscord;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;
import java.util.Objects;
import java.util.logging.Level;

public final class Main extends JavaPlugin {


  final FileConfiguration config = getConfig();

  JDA jda;

  @Override
  public void onEnable() {

    // Adds the fields to the config.yml
    config.addDefault("bot-token", " ");
    config.addDefault("webhook", " ");
    config.addDefault("channel-id", " ");

    config.options().copyDefaults(true);

    saveConfig();

    if (Objects.equals(config.getString("bot-token"), " ") ||
        Objects.equals(config.getString("webhook"), " ") ||
        Objects.equals(config.getString("channel-id"), " ")
    ) {
      this.getLogger().log(Level.SEVERE,
          "Plugin set-up is not complete yet. Please enter the appropriate values in config.yml and saved the file.");
      this.getPluginLoader().disablePlugin(this);
    } else {

      jda =
          JDABuilder
              .createDefault(config.getString("bot-token"))
              .enableIntents(EnumSet.allOf(GatewayIntent.class))
              .build();


      try {
        jda.awaitReady();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      PluginManager pm = Bukkit.getServer().getPluginManager();
      pm.registerEvents(new MCToDiscord(this), this);

      jda.addEventListener(new DiscordToMC(this));

    }
  }

  @Override
  public void onDisable() {
    try {
      jda.shutdownNow();
    } catch (NullPointerException ignored) {
    }
  }
}
