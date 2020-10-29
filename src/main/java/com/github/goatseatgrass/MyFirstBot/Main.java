package com.github.goatseatgrass.MyFirstBot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
	public static DiscordApi api;
	public static Server RabidWriters;
	
	public static void main(String[] args) throws IOException {
		String token = "NzIyNDQ2MTY2Njk2MTMyNjE4.XujMbA.8WxhPPW0xPgDTD1Xi2m1Xbxgf94";
		api = new DiscordApiBuilder().setToken(token).login().join();
		System.out.println("Logged in!");
		//Organization.status(new String[]{"xyz", "offline"});

		RabidWriters = api.getServerById("695616904206876774").get();
		//MyServer = api.getServerById("722438697785622629").get();

		api.addMessageCreateListener(event -> {
			try {
				assign(event.getMessageContent(), event.getChannel(), event.getMessageId(), event.getMessageAuthor().asUser().get());
			} catch (InterruptedException | ExecutionException | IOException e) {
			}
		});

		//api.addServerMemberJoinListener(i -> {Organization.autoNick(i.getUser());});

		// Add a listener which answers with Discord name if someone writes "!ping"
		api.addMessageCreateListener(event -> {
			if (event.getMessageContent().contains("You up?")) {
				//User user = api.getYourself();
				//User user = event.getMessageAuthor().asUser().get();
				//event.getChannel().sendMessage(api.createBotInvite());
				//for (int i=0; i<user.getRoles(MyServer).size(); i++)
				//{event.getChannel().sendMessage(user.getRoles(MyServer).get(i).getName());}
				System.out.println("Working");
				//event.getChannel().sendMessage(user.getIdAsString());
				event.getChannel().sendMessage("yeah");
				System.out.println(event.getMessageContent());
				//event.getChannel().sendMessage(event.getReadableMessageContent());
				System.out.println(event.getReadableMessageContent());
				//event.getChannel().sendMessage("Discriminated Name - " + user.getDiscriminatedName());
				//event.getChannel().sendMessage("Discriminator Name - " + user.getDiscriminator());
				//event.getChannel().sendMessage("Mention Tag - " + user.getMentionTag());
				//event.getChannel().sendMessage("Nick Mention Tag - " + user.getNicknameMentionTag());
				//event.getChannel().sendMessage("Name - " + user.getName());
				//event.getChannel().sendMessage("Display Name - " + user.getDisplayName(event.getServer().get()));
			}
		});

	}
	
	public static void assign(String message, TextChannel channel, long ID, User author) throws InterruptedException, ExecutionException, IOException {
		String[] breakdown = message.split(" ");
		if (author.isBot()) {
		} else {
			if (breakdown[0].equalsIgnoreCase("wolf.status")) {
				Organization.status(breakdown);
			}
			if (breakdown[0].equalsIgnoreCase("wolf.announce")) {
				Organization.announce(breakdown, channel, author);
			}
			if (breakdown[0].equalsIgnoreCase("wolf.warn")) {
				Organization.warn(breakdown, channel, author);
			}
			if (breakdown[0].equalsIgnoreCase("wolf.delete")) {
				Organization.delete(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wolf.react")) {
				Organization.react(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wolf.removereactions")) {
				Organization.removeReactions(breakdown, channel, ID, author);
			}
			
			if (breakdown[0].equalsIgnoreCase("wc.add")) {
				DailyWordCount.add(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.set")) {
				DailyWordCount.set(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.check")) {
				DailyWordCount.check(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.total")) {
				DailyWordCount.sum(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.reset")) {
				DailyWordCount.reset(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.undo")) {
				DailyWordCount.undo(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.view")) {
				DailyWordCount.view(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.help")) {
				DailyWordCount.help(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.break")) {
				DailyWordCount.goOnBreak(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.back")) {
				DailyWordCount.isBack(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.resetbreak")) {
				DailyWordCount.resetBreakStats(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.checkstats")) {
				DailyWordCount.checkStats(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.checkmonthlystats")) {
				DailyWordCount.checkMonthlyStats(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.scan")) {
				DailyWordCount.scan(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.checkmembers")) {
				DailyWordCount.memberList(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.scanall")) {
				DailyWordCount.letsGetThatBread(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.showmap")) {
				DailyWordCount.showmap(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.updatemap")) {
				DailyWordCount.updateMap(message, channel, ID, author);
			}
			
		}
	}

	public static String getMessagefromArray(String[] array, int startingPoint) {
		String message = "";
		for (int i = startingPoint; i < array.length; i++) {
			message += array[i] + " ";
		}
		return message.trim();
	}
	
}



