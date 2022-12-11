package net.cookietom.discordbridge.listeners.minecraft;

import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.cookietom.discordbridge.DiscordBridgePlugin;
import net.cookietom.discordbridge.util.MessageFormatting;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class PlayerChatListener implements Listener {
	private final DiscordBridgePlugin plugin;

	@EventHandler
	public void onChat(AsyncChatEvent e) {
		Component message = e.message();
		Player sender = e.getPlayer();
		String raw = PlainTextComponentSerializer.plainText().serialize(message);

		if (!raw.toLowerCase().contains("@everyone") ||
				!raw.toLowerCase().contains("@here") ||
				!raw.toLowerCase().contains("<@&")) {
			WebhookMessage webhookMessage = new WebhookMessageBuilder()
					.setContent(this.plugin.getPluginConfig().ansi() ? MessageFormatting.toDiscordAnsi(message) : MessageFormatting.toDiscordPlain(message))
					.setAvatarUrl("https://crafatar.com/avatars/" + sender.getUniqueId())
					.setUsername(sender.getName())
					.build();

			this.plugin.getWebhookClient().send(webhookMessage).exceptionally(throwable -> {
				this.plugin.getLogger().warning(() -> "Failed to send message to Discord. Full stack trace: " + throwable);
				return null;
			});
		}
	}
}

