package com.github.goatseatgrass.MyFirstBot;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DailyWordCount {
	
private static final Preferences prefs = Preferences.userNodeForPackage(DailyWordCount.class);
/* Names of keys
 * 1. author.getIdAsString() book1/book2 name - String book name 
 * 2. author.getIdAsString() book1/book2 number - Long word count
 * 3. author.getIdAsString() book1/book2 - Boolean book set or not
 * 4. author.getIdAsString() onbreak - Boolean on break
 * 5. author.getIdAsString() break localdatetime - String from
 * 6. author.getIdAsString() stats break - from to
 * 7. author.getIdAsString() stats break - from to
 */
static Map<MessageAuthor, List<Message>> map;

public static void help(String[] message, TextChannel currentChannel, long ID, User author) throws ExecutionException {
	//wc.set book1 name or wc.set book2 name
MessageBuilder mb = new MessageBuilder();
EmbedBuilder eb = new EmbedBuilder();
eb.setColor(Color.WHITE);
eb.setAuthor("List of Commands");
eb.addField("1. wc.set book1/book2 <Name of the book>", "Adds the specified book as your first/second book. You can also use this command to rename your book");
eb.addField("2. wc.add book1/book2 <Number of words>", "Adds the amount of words to your first/second book. To remove words from your book, use negative numbers.");
//eb.addField("3. wc.remove book1/book2", "Removes the amount of words from your first/second book.");
eb.addField("3. wc.check book1/book2 <Number> ", "Shows the specified number of word count entries of your first/second book. If no number is specified, it shows the last 10 entries");
eb.addField("4. wc.total book1/book2", "Shows the total amount of words written for your first/second book.");
eb.addField("5. wc.reset book1/book2", "Resets all progress of your first/second book.");
eb.addField("6. wc.view book1/book2", "Shows the name of your first/second book.");
//eb.addField("7. wc.break", "Puts you on break effective immediately.");
//eb.addField("8. wc.back", "Ends your break");
//eb.addField("9. wc.checkstats", "Shows you dem statistics");
//eb.addField("11. wc.checkmonthlystats <Month>", "Shows you dem monthly statistics");
currentChannel.sendMessage(eb);

}

public static void set(String[] message, TextChannel currentChannel, long ID, User author) throws InterruptedException, ExecutionException {
	//wc.set book1 name or wc.set book2 name
	memberAdd(message, currentChannel, ID, author);
	String book = message[1];
	if (prefs.getBoolean(author.getIdAsString() + "isOnBreak", false)) 
	{currentChannel.sendMessage("You're on a break. Use wc.back to join the rat race");}
	
	else if (book.equalsIgnoreCase("book1")||book.equalsIgnoreCase("book2")) {
		prefs.put(author.getIdAsString() + book + " name", Main.getMessagefromArray(message, 2));
		prefs.putBoolean(author.getIdAsString() + book, true);
		currentChannel.getMessageById(ID).get().addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
		Organization.deleteInTime(currentChannel, ID, 1);
	}

	else
	{currentChannel.sendMessage("Use wc.set book1/book2 followed by the name of the book to set it.");}
}

public static void view(String[] message, TextChannel currentChannel, long ID, User author) throws InterruptedException, ExecutionException {
	//wc.view book1 or wc.view book2 
	String book = message[1];
	if (book.equalsIgnoreCase("book1")||book.equalsIgnoreCase("book2")) {
		if (prefs.get(author.getIdAsString() + book + " name", "no book").equalsIgnoreCase("no book"))
			{currentChannel.sendMessage("You do not have a book atm. Use \"wc.set " + book + " <name of your book without these thingies at the end>\" to first set a book");}
		else
			{	currentChannel.getMessageById(ID).get().addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
				Organization.deleteInTime(currentChannel, ID, 1);
				currentChannel.sendMessage("You currently have \"" + prefs.get(author.getIdAsString() + book + " name", "no book") + "\" set as your first book");}
	}
}

public static void add(String[] message, TextChannel currentChannel, long ID, User author) throws InterruptedException, ExecutionException {
	//wc.add book1 number or wc.add book2 number
	String book = message[1];
	boolean validEntry = true;
	
	if (prefs.getBoolean(author.getIdAsString() + "isOnBreak", false)) 
	{currentChannel.sendMessage("You're on a break. Use wc.back to join the rat race");}
	
	else if (book.equalsIgnoreCase("book1")||book.equalsIgnoreCase("book2")) {
		if (prefs.get(author.getIdAsString() + book, "no book").equalsIgnoreCase("no book"))
			{currentChannel.sendMessage("You do not have a book atm. Use \"wc.set book1 <name of your book without these thingies at the end>\" to first set a book");}
		else
		{String wc = prefs.get(author.getIdAsString() + book + " wc", "");
		try {
			int words = Integer.parseInt(message[2]);
		}
		catch(NumberFormatException e) {
			currentChannel.sendMessage("Please use a valid number");
			validEntry = false;
		}
		
		if(validEntry) {
		wc+= message[2];
		wc+=" ";
		prefs.put(author.getIdAsString() + book + " wc", wc);
		prefs.put(author.getIdAsString() + " update", LocalDate.now().toString() + " ");}
		currentChannel.getMessageById(ID).get().addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
		Organization.deleteInTime(currentChannel, ID, 5);
		}
	}
}

public static void undo(String[] message, TextChannel currentChannel, long ID, User author) throws InterruptedException, ExecutionException {
	//wc.add book1 number or wc.add book2 number
	String book = message[1];
	if (prefs.getBoolean(author.getIdAsString() + "isOnBreak", false)) 
	{currentChannel.sendMessage("You're on a break. Use wc.back to join the rat race");}
	
	else if (book.equalsIgnoreCase("book1")||book.equalsIgnoreCase("book2")) {
		String wc = prefs.get(author.getIdAsString() + book + " wc", "");
		String newwc = "";
		String months = prefs.get(author.getIdAsString() + " update", "");
		String newmonths = "";
		for (int i=0; i<wc.split(" ").length-1; i++)
			{newwc+=wc.split(" ")[i];
			newwc+=" ";}

		for (int i=0; i<months.split(" ").length-1; i++)
		{newmonths+=months.split(" ")[i];
			newmonths+=" ";}
		prefs.put(author.getIdAsString() + book + " wc", newwc);
		prefs.put(author.getIdAsString() + " update", newmonths);
		currentChannel.getMessageById(ID).get().addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
	}
}

public static void check(String[] message, TextChannel currentChannel, long ID, User author) {
	//wc.check book1 month or wc.check book2 month
	String book = message[1];
	if (prefs.getBoolean(author.getIdAsString() + "isOnBreak", false)) 
	{currentChannel.sendMessage("You're already on a break. Use wc.back to join the rat race");}
	
	else if (book.equalsIgnoreCase("book1")||book.equalsIgnoreCase("book2")) {
		if (prefs.getBoolean(author.getIdAsString() + book, false)) {
			String words = "Words written - ";
			String[]temp = prefs.get(author.getIdAsString() + book + " wc", "0").split(" ");
			for (int i=0; i<temp.length; i++) {
				if (i==temp.length-1)
					{words+=temp[i];}
				else
					{words+= temp[i] + ", ";}
			}
			currentChannel.sendMessage(words);
		}
	}
}

public static int total(String[] message, TextChannel currentChannel, long ID, User author, String month) {
	String book = message[1];
    int sum = prefs.getInt(author.getIdAsString() + " sum " + book, 0);
    if (month.equalsIgnoreCase("total")) {
		if (prefs.getBoolean(author.getIdAsString() + book, false)) {
			String allWords = prefs.get(author.getIdAsString() + book + " wc", "0");
			String[] words = allWords.split(" ");
			for (int i = 0; i < words.length; i++) {
				//System.out.println(words[i]);
				if (!words[i].chars().allMatch(Character::isWhitespace)) {
					String temp = words[i].replace(" ", "");
					sum += Long.parseLong(temp);
				}
			}
		}
	}
	else {
		int counter = 0;
		boolean validMonth = true;
		int start = -1;
		boolean startSet = false;
		String[] dates = prefs.get(author.getIdAsString() + " update", "").split(" ");
		String[] words = prefs.get(author.getIdAsString() + book + " wc", "0").split(" ");

		try {
			Month.valueOf(month.toUpperCase());
		} catch (IllegalArgumentException e) {
			validMonth = false;
		}

		if (validMonth) {
			for (int i = 0; i< dates.length; i++) {
				if (LocalDate.parse(dates[i]).getMonth().compareTo(Month.valueOf(month.toUpperCase()))==0 && startSet) {
					counter++;
				}
				else if (LocalDate.parse(dates[i]).getMonth().compareTo(Month.valueOf(month.toUpperCase()))==0){
					start = i;
					startSet = true;
				}
			}

			for (int i = start; i < start + counter + 1; i++) {
				//System.out.println(words[i]);
				if (!words[i].chars().allMatch(Character::isWhitespace)) {
					String temp = words[i].replace(" ", "");
					sum += Long.parseLong(temp);
				}
			}

			if (start == -1){
				sum = -18;
			}
		}

		else{
			sum = -18;
		}
	}


	return sum;
}

public static void sum(String[] message, TextChannel currentChannel, long ID, User author) {
	//wc.total book1 month or wc.check book2 month
	int sum = 0;
	String book = message[1];
	if (prefs.getBoolean(author.getIdAsString() + "isOnBreak", false)) 
	{currentChannel.sendMessage("You're already on a break. Use wc.back to join the rat race");}
	
	else if (book.equalsIgnoreCase("book1")||book.equalsIgnoreCase("book2")) {
		sum = total(message, currentChannel, ID, author, "total");
		currentChannel.sendMessage("Total words written - " + sum);
	}
	
	else {
		currentChannel.sendMessage("Please specify whether the changes are for book1 or book2");
	}
	
}

public static void reset(String[] message, TextChannel currentChannel, long ID, User author) {
	//wc.reset book1 month or wc.reset book2 month
	String book = message[1];
	if (prefs.getBoolean(author.getIdAsString() + "isOnBreak", false)) 
	{currentChannel.sendMessage("You're already on a break. Use wc.back to join the rat race");}
	
	else if (book.equalsIgnoreCase("book1")||book.equalsIgnoreCase("book2")) {
		if (prefs.getBoolean(author.getIdAsString() + book, false)) {
			prefs.remove(author.getIdAsString() + book + " name");
			prefs.remove(author.getIdAsString() + book + " wc");
			prefs.remove(author.getIdAsString() + book);
			currentChannel.sendMessage(book +" removed");
		}
	}
	
	else {
		currentChannel.sendMessage("Please specify if the changes are for book1 or book2");
	}
}

public static void goOnBreak(String[] message, TextChannel currentChannel, long ID, User author) throws ExecutionException, InterruptedException {
	LocalDate breakStartsfrom = LocalDate.now();
	if (prefs.getBoolean(author.getIdAsString() + "isOnBreak", false)) 
		{currentChannel.sendMessage("You're already on a break. Use wc.back to join the rat race");}
	else
		{prefs.putBoolean(author.getIdAsString() + "isOnBreak", true);
		prefs.put(author.getIdAsString() + "break","From : " + LocalDate.now().toString());
		currentChannel.getMessageById(ID).get().addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
		Organization.deleteInTime(currentChannel, ID, 1);
		Main.api.getTextChannelById("704993381641748542").get().sendMessage(author.getDiscriminatedName() + " (ID - " + author.getIdAsString() + ") is now on a break");
		}
	}

public static void isBack(String[] message, TextChannel currentChannel, long ID, User author) throws ExecutionException, InterruptedException {
	if (!prefs.getBoolean(author.getIdAsString() + "isOnBreak", false)) 
		{currentChannel.sendMessage("You were never on a break. Use wc.break to quit on us like a damn pussy");}
	else
		{prefs.putBoolean(author.getIdAsString() + "isOnBreak", false);
		//prefs.put(author.getIdAsString() + "break", LocalDate.now().toString());
		String breakstats = prefs.get(author.getIdAsString() + "breakstats", "");
		breakstats+=prefs.get(author.getIdAsString() + "break", "");
		breakstats+=" To : ";
		breakstats+=LocalDate.now();
		breakstats+=",";
		prefs.put(author.getIdAsString() + "breakstats", breakstats);
		currentChannel.getMessageById(ID).get().addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
		Organization.deleteInTime(currentChannel, ID, 1);
		}
}

public static void checkStats(String[] message, TextChannel currentChannel, long ID, User author) throws InterruptedException, ExecutionException {
	if (message.length>1) {
		if (Organization.checkPerms(author)) {
			boolean breakexists = true;
			User subject = Main.api.getUserById(message[2].substring(2, 21)).get();
			String[] breaks = prefs.get(subject.getIdAsString() + "breakstats", "").split(",");
			String breakstats = "";

			if (breaks.length==1&&breaks[0].isEmpty())
				breakexists = false;
			if (breakexists){
				for (int i=0; i< breaks.length; i++){
					breakstats += breaks[i];
					String[] toandfrom = breaks[i].split("-");
					breakstats += String.valueOf(Math.abs(ChronoUnit.DAYS.between(LocalDate.parse(toandfrom[1]), LocalDate.parse(toandfrom[3])))) + " Days";
					breakstats += System.lineSeparator();
				}}
			else
			{breakstats = "No breaks taken gg";}

			String bookname1 = prefs.get(subject.getIdAsString() + "book1 name", "No name set");
			String bookname2 = prefs.get(subject.getIdAsString() + "book2 name", "No name set");
			int bookwords1 = total(new String[] {"bla", "book1"}, currentChannel, ID, subject, "total");
			int bookwords2 = total(new String[] {"bla", "book2"}, currentChannel, ID, subject, "total");
			EmbedBuilder eb = new EmbedBuilder();
			eb.setAuthor("Writing Stats");
			eb.addField(bookname1, Integer.toString(bookwords1));
			eb.addField(bookname2, Integer.toString(bookwords2));
			eb.addField("Breakstats", breakstats);
		}

		else
		{currentChannel.sendMessage("Use wc.checkstats <month> to check your own stats");}
	}

	else {
		Boolean breakexists = true;
		String breaks[] = prefs.get(author.getIdAsString() + "breakstats", "").split(",");
		String breakstats = "";

		if (breaks.length==1&&breaks[0].isEmpty())
			breakexists = false;

		if (breakexists){
			for (int i=0; i< breaks.length; i++){
				System.out.println("break - " + breaks[i]);
				breakstats += breaks[i];
				breakstats += "; " + String.valueOf(Math.abs(ChronoUnit.DAYS.between(LocalDate.parse(breaks[i].substring(7,17)), LocalDate.parse(breaks[i].substring(23,33))))) + " Days";
				breakstats += System.lineSeparator();
			}}
		else
		{breakstats = "No breaks taken gg";}

		String bookname1 = prefs.get(author.getIdAsString() + "book1 name", "No name set");
		String bookname2 = prefs.get(author.getIdAsString() + "book2 name", "No name set");
		int bookwords1 = total(new String[] {"bla", "book1"}, currentChannel, ID, author, "total");
		int bookwords2 = total(new String[] {"bla", "book2"}, currentChannel, ID, author, "total");
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("Writing Stats");
		eb.addField(bookname1, "Words written - " + Integer.toString(bookwords1));
		eb.addField(bookname2, "Words written - " +  Integer.toString(bookwords2));
		eb.addField("Breakstats", breakstats);
		//System.out.println("EUGIWYDUX");
		author.sendMessage(eb);
	}
}

public static void checkMonthlyStats(String[] message, TextChannel currentChannel, long ID, User author) throws InterruptedException, ExecutionException {
	//wc.checkmonthlystats month member
	if (message.length>2) {
		if (Organization.checkPerms(author)) {
			boolean breakexists = true;
			User subject = Main.api.getUserById(message[2].substring(2, 21)).get();
			String[] breaks = prefs.get(subject.getIdAsString() + "breakstats", "").split(",");
			String breakstats = "";

			if (breaks.length==1&&breaks[0].isEmpty())
				breakexists = false;
			if (breakexists){
			for (int i=0; i< breaks.length; i++){
				breakstats += breaks[i];
				String[] toandfrom = breaks[i].split("-");
				breakstats += String.valueOf(Math.abs(ChronoUnit.DAYS.between(LocalDate.parse(toandfrom[1]), LocalDate.parse(toandfrom[3])))) + " Days";
				breakstats += System.lineSeparator();
			}}
			else
			{breakstats = "No breaks taken gg";}

			String bookname1 = prefs.get(subject.getIdAsString() + "book1 name", "No name set");
			String bookname2 = prefs.get(subject.getIdAsString() + "book2 name", "No name set");
			int bookwords1 = total(new String[] {"bla", "book1"}, currentChannel, ID, subject, message[1]);
			int bookwords2 = total(new String[] {"bla", "book2"}, currentChannel, ID, subject, message[1]);
			EmbedBuilder eb = new EmbedBuilder();
			eb.setAuthor("Writing Stats");
			eb.addField(bookname1, Integer.toString(bookwords1));
			eb.addField(bookname2, Integer.toString(bookwords2));
			eb.addField("Breakstats", breakstats);
			if (bookwords1==-18||bookwords2==-18)
				{author.sendMessage("The month you entered couldn't be recognized or you don't have words for mentioned month. Try again with a valid month.");}
			else
				{author.sendMessage(eb);}
		}

		else
		{currentChannel.sendMessage("Use wc.checkmonthlystats <month> to check your own stats");}
	}

	else {
		Boolean breakexists = true;
		String breaks[] = prefs.get(author.getIdAsString() + "breakstats", "").split(",");
		String breakstats = "";

		if (breaks.length==1&&breaks[0].isEmpty())
			breakexists = false;

		if (breakexists){
		for (int i=0; i< breaks.length; i++){
			System.out.println("break - " + breaks[i]);
			breakstats += breaks[i];
			breakstats += "; " + String.valueOf(Math.abs(ChronoUnit.DAYS.between(LocalDate.parse(breaks[i].substring(7,17)), LocalDate.parse(breaks[i].substring(23,33))))) + " Days";
			breakstats += System.lineSeparator();
		}}
		else
		{breakstats = "No breaks taken gg";}

		String bookname1 = prefs.get(author.getIdAsString() + "book1 name", "No name set");
		String bookname2 = prefs.get(author.getIdAsString() + "book2 name", "No name set");
		int bookwords1 = total(new String[] {"bla", "book1"}, currentChannel, ID, author, message[1]);
		int bookwords2 = total(new String[] {"bla", "book2"}, currentChannel, ID, author, message[1]);
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("Writing Stats");
		eb.addField(bookname1, "Words written - " + Integer.toString(bookwords1));
		eb.addField(bookname2, "Words written - " +  Integer.toString(bookwords2));
		eb.addField("Breakstats", breakstats);

		if (bookwords1==-18||bookwords2==-18)
			{author.sendMessage("The month you entered couldn't be recognized or you don't have words for mentioned month. Try again with a valid month.");}
		else
			{author.sendMessage(eb);}
	}
}

public static void resetBreakStats(String[] message, TextChannel currentChannel, long ID, User author) {
	//wc.resetBreak
	prefs.remove(author.getIdAsString() + "breakstats");
	}

public static void scan(String[] message, TextChannel currentChannel, long ID, User author) throws ExecutionException, InterruptedException {
if (Organization.checkPerms(author))
	{String mID = message[1];
	System.out.println("I'm working, ID is'" + mID);
	MessageSet messages = currentChannel.getMessagesBetween(Long.parseLong(mID), ID).get();
	messages.forEach(msg -> {
		try {
			Main.assign(msg.getContent(), msg.getChannel(), msg.getId(), msg.getAuthor().asUser().get());
		} catch (InterruptedException |  ExecutionException | IOException e) {
			e.printStackTrace();
		}});
	}
else
	{author.sendMessage("Only Mods can use this method");}
	}

public static void memberList(String[] message, TextChannel currentChannel, long ID, User author) throws ExecutionException, InterruptedException {
	String[]members = prefs.get("members", "").split(" ");
	System.out.println(prefs.get("members", ""));
	LinkedList allstats = new LinkedList();
	for(int i=0; i< members.length; i++){
		if (!members[i].isEmpty())
		{long mID = Long.parseLong(members[i]);
		String[]temp1 = prefs.get(author.getIdAsString() + "book1 wc", "0").split(" ");
		String[]temp2 = prefs.get(author.getIdAsString() + "book2 wc", "0").split(" ");
		int numberOfUpdates = temp1.length + temp2.length;
		int bookwords1 = total(new String[] {"bla", "book1"}, currentChannel, ID, Main.api.getUserById(mID).get(), "total");
		int bookwords2 = total(new String[] {"bla", "book2"}, currentChannel, ID, Main.api.getUserById(mID).get(), "total");
		int totalWords = bookwords1 + bookwords2;
		String stats = "Member - " + Main.api.getUserById(mID).get().getDiscriminatedName() + "\n" + "Number of times updated - " + numberOfUpdates + "\n" + "Words written - " + totalWords;
		allstats.add(stats);}
	}
	MessageBuilder mb = new MessageBuilder();
	for (int i=0; i< allstats.size(); i++){
		mb.append(allstats.get(i));
		mb.appendNewLine();
		mb.append("---------------");
	}
	mb.send(currentChannel);
}

	//method for member list
	public static void memberAdd(String[] message, TextChannel currentChannel, long ID, User author){
		boolean exists = false;
		System.out.print(prefs.get("members", ""));
		if (!prefs.get("members", "").equalsIgnoreCase(""))
		{String[] members = prefs.get("members", "").split(" ");
			for (int i=1; i< members.length; i++){
				if (Long.parseLong(members[i])== author.getId()){
					exists = true;
				}
			}}

		if (!exists) {
			String temp = prefs.get("members", "") + " " + author.getIdAsString();
			prefs.put("members", temp);
		}
	}

	public static void letsGetThatBread(String[] message, TextChannel currentChannel, long ID, User author) throws ExecutionException, InterruptedException {
        try (Stream<Message> messageStream = currentChannel.getMessagesBeforeAsStream(ID)) {
        map = messageStream.collect(Collectors.groupingBy(Message::getAuthor));
        System.out.println("I'm done searching");
        }
    }

    public static void showmap(String[] message, TextChannel currentChannel, long ID, User author){
    int number = Integer.parseInt(message[1]);
    Object key = map.keySet().toArray()[number];
    List<Message> allMessages = map.get(key);
    final String[] messages = {""};
    allMessages.forEach(i -> {String[]content = i.getContent().split("content:");
    							messages[0] += content[0];
								messages[0] += ", ";});
    System.out.println(number + " " + key);
    System.out.println(messages[0]);
    currentChannel.sendMessage(key.toString());

    if (messages[0].length()>2000){
    for (int i=0; i<(messages[0].length()/2000 + 1); i++){
        currentChannel.sendMessage(messages[0].substring(2000*i, 2000*(i+1)));
    }
    }
    else
    {currentChannel.sendMessage(messages[0]);}
    }

    public static void status(String[] message, TextChannel currentChannel, long ID, User author){
		String mID = message[1];
		try{
		Main.api.getUserById(mID).get();
		}
		catch (NoSuchElementException | InterruptedException | ExecutionException e){
			currentChannel.sendMessage("This member isn't in the Server anymore.");
		}
	}
    public static void updateMap(String message, TextChannel currentChannel, long ID, User author){
        //wc.update ID book - Text
        long mID = 0;
		if (!message.split(" ")[1].chars().allMatch(Character::isWhitespace)) {
			String temp = message.split(" ")[1].replace(" ", "");
			mID = Long.parseLong(temp);
		}
        String book = message.split(" ")[2];
        if (!book.chars().allMatch(Character::isWhitespace)) {
            String temp = book.replace(" ", "");
            book = temp;
        }
        if (book!="book1"||book!="book2"){
            currentChannel.sendMessage("Specify a book");
        }
		int sum = prefs.getInt(mID + " sum " + book, 0);

        String[]words = message.split("-")[1].split(",");
        for (int i=0; i<words.length; i++){
            if (!words[i].chars().allMatch(Character::isWhitespace)) {
                String temp = words[i].replace(" ", "");
                sum += Long.parseLong(temp);
            }
        }
        currentChannel.sendMessage("sum - " + sum);
        prefs.putInt(mID + " sum " + book, sum);
    }

}

