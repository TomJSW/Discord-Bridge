package net.cookietom.discordbridge.listeners.discord;

import lombok.RequiredArgsConstructor;
import net.cookietom.discordbridge.DiscordBridgePlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class DiscordMessageListener extends ListenerAdapter {
	private final DiscordBridgePlugin plugin;
	private final MiniMessage miniMessage = MiniMessage.miniMessage();

	public void onMessageReceived(MessageReceivedEvent e) {
		String bridgeChannelId = plugin.getPluginConfig().channelId();
		MessageChannelUnion channel = e.getChannel();
		if (e.getMember() == null) return;
		User author = e.getAuthor();

		if (channel.getId().equals(bridgeChannelId) && !author.isBot()) {
			// Checks if there are any players online
			if (this.plugin.getServer().getOnlinePlayers().size() == 0) {
				EmbedBuilder eb =
						new EmbedBuilder()
								.setTitle("Warning: there are currently no players online!")
								.setColor(Color.YELLOW);

				// Sends a temporary 5-second embed telling the sender there are no players online
				channel
						.sendMessage(author.getAsMention())
						.addEmbeds(eb.build())
						.delay(5, TimeUnit.SECONDS)
						.flatMap(Message::delete).queue();
			} else {
				// Simple filter for stickers, images and embeds
				if (StringUtils.isBlank(e.getMessage().getContentDisplay())) return;

				this.plugin.getServer().broadcast(formatMessage(author, e.getMessage()));
			}
		}
	}

	private Component formatMessage(User author, Message message) {
		String format = this.plugin.getPluginConfig().messageFormat();
		return this.miniMessage.deserialize(
				format,
				TagResolver
						.builder()
						.tag("name", Tag.inserting(Component.text(author.getAsTag())))
						.tag("message", Tag.inserting(Component.text(message.getContentDisplay())))
						.tag("link", Tag.preProcessParsed(message.getJumpUrl()))
						.build());
	}
}
