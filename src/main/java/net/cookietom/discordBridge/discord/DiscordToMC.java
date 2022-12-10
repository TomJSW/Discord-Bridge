package net.cookietom.discordBridge.discord;

import net.cookietom.discordBridge.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class DiscordToMC extends ListenerAdapter {

  private final Main plugin;

  public DiscordToMC(Main plugin) {
    this.plugin = plugin;
  }

  public void onMessageReceived(MessageReceivedEvent e) {

    String bridgeChannelID = plugin.getConfig().getString("channel-id");
    MessageChannelUnion channel = e.getChannel();
    User author = e.getAuthor();

    if (channel.getId().equals(bridgeChannelID) &&
        !author.isBot()) {

      // Checks if there are any players online
      if (Bukkit.getOnlinePlayers().size() == 0) {

        EmbedBuilder eb =
            new EmbedBuilder()
                .setTitle("Warning: there are currently no players online!")
                .setColor(Color.YELLOW);

        // Sends a temporary 5-second embed telling the sender there are no players online
        channel
            .sendMessage("<@" + author.getId() + ">")
            .addEmbeds(eb.build())
            .delay(5, TimeUnit.SECONDS)
            .flatMap(Message::delete).queue();

      } else {
        // Ensures member isn't null as getMember() is marked as @Nullable
        if (e.getMember() != null) {
          // Broadcasts the message on the server
          Bukkit.broadcast(Component.text("§3Bridge> §f" + e.getMember().getEffectiveName() + ": " +
              e.getMessage().getContentDisplay()));
        }
      }
    }
  }
}
