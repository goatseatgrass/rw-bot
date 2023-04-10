package com.github.goatseatgrass.MyFirstBot;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

public class Main {
	public static DiscordApi api;
	//public static Server RabidWriters;
	//public static User Me;
	
	public static void main(String[] args) throws IOException {
		String token = "Insert token here";
		api = new DiscordApiBuilder().setToken(token).login().join();
		System.out.println("Logged in!");
		//MyServer = api.getServerById("722438697785622629").get();


		api.addMessageCreateListener(event -> {
			try {System.out.println("In try block");
			if (event.isPrivateMessage())
				{PMAssign(event.getMessageContent(), event.getChannel(), event.getMessageId(), event.getMessageAuthor().asUser().get());}
			else
				{assign(event.getMessageContent(), event.getChannel(), event.getMessageId(), event.getMessageAuthor().asUser().get());}
			} catch (InterruptedException | ExecutionException | IOException e) {
			}
		});

		//api.addServerMemberJoinListener(i -> {Organization.autoNick(i.getUser());});
	}
	
	public static void assign(String message, TextChannel channel, long ID, User author) throws InterruptedException, ExecutionException, IOException {
		String[] breakdown = message.split(" ");
        System.out.println("In assign");
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
			if (breakdown[0].equalsIgnoreCase("wc.remove")) {
				DailyWordCount.add(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.view")) {
                System.out.println("In view");
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
			if (breakdown[0].equalsIgnoreCase("wc.checkstatus")) {
				DailyWordCount.status(breakdown, channel, ID, author);
			}
			if (breakdown[0].equalsIgnoreCase("wc.updatemap")) {
				DailyWordCount.updateMap(message, channel, ID, author);
			}
			
		}
	}

	public static void PMAssign(String message, TextChannel channel, long ID, User author) throws ExecutionException, InterruptedException {
	TextChannel logroom = api.getTextChannelById("704993381641748542").get();
	if (author.isBot()) {
		}
	else if (message.contains("#C")){
		logroom.sendMessage("We have a new complaint" + System.lineSeparator() + "C#" + channel.getId() + " - " + message.split("-")[1]);
		channel.getMessageById(ID).get().addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
		api.getYourself().sendMessage(author.getDiscriminatedName() + " " + message + System.lineSeparator() + LocalDateTime.now().toString());
		channel.sendMessage("If your message has been reacted to, that means that your message has been sent to the staff anonymously, and they will get back to you using the bot. If not, wait for some time and try again");
	}
	else if (message.contains("#S")){
		logroom.sendMessage("We have a new suggestion" + System.lineSeparator() + "S#" + channel.getId() + " - " + message.split("-")[1]);
		channel.getMessageById(ID).get().addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
		api.getYourself().sendMessage(author.getDiscriminatedName() + " " + message + System.lineSeparator() + LocalDateTime.now().toString());
		channel.sendMessage("If your message has been reacted to, that means that your message has been sent to the staff anonymously, and they will get back to you using the bot. If not, wait for some time and try again");
	}

	else if (message.contains("complain")){
		channel.sendMessage("Type in the complaint/concern now in the following format" + System.lineSeparator() + "#C - \"Enter concern/suggestion here\"");
	}
	else if (message.contains("suggestion")){
		channel.sendMessage("Type in the suggestion now in the following format" + System.lineSeparator() + "#S - \"Enter suggestion here\"");
	}
	else {
		channel.sendMessage("Thanks for PMing me, I'm glad I can be of use." + System.lineSeparator() + "If you have a complaint or concern, type \"I have a complaint\" and follow the steps indicated by the bot" + System.lineSeparator() + "If you have a suggestion, type \"I have a suggestion\" and follow the steps indicated by the bot");
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



