package net.cookietom.discordBridge.minecraft;

import net.cookietom.discordBridge.Main;
import net.cookietom.discordBridge.util.DiscordWebhook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;

public class MCToDiscord implements Listener {

  private final Main plugin;

  public MCToDiscord(Main plugin) {
    this.plugin = plugin;
  }

  @SuppressWarnings("deprecation") // Paper
  @EventHandler
  public void onChat(AsyncPlayerChatEvent e) {

    String message = e.getMessage();
    Player sender = e.getPlayer();
    DiscordWebhook webhook = new DiscordWebhook(plugin.getConfig().getString("webhook"));

    // Ensures mass mentions are not abused
    if (!message.toLowerCase().contains("@everyone") ||
        !message.toLowerCase().contains("@here") ||
        !message.toLowerCase().contains("<@&")) {

      // Constructs the webhook
      webhook.setContent(message);
      webhook.setAvatarUrl("https://crafatar.com/avatars/" + sender.getUniqueId());
      webhook.setUsername(sender.getName());

    }

    // Sends the webhook
    try {
      webhook.execute();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}

