package com.motionpoint.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.motionpoint.demo.model.Message;
import com.motionpoint.demo.model.Persistent;
import com.motionpoint.demo.util.IntegerUtil;

import de.svenjacobs.loremipsum.LoremIpsum;

public class RunExample {

	private List<Persistent> persistentlist = new ArrayList<Persistent>();
	private List<Persistent> loadedPersistentlist = new ArrayList<Persistent>();
	private List<Integer> integerKeeperIdList = new ArrayList<Integer>();
	private List<Integer> consoleMessageIdList = new ArrayList<Integer>();

	public void runExample() {
		build();
		store();
		send();
		load();
		System.out.println("All done!");
	}

	private void load() {
		System.out.println("Loading items...");
		
		for (Integer keeperId : integerKeeperIdList) {
			IntegerKeeper keeper = new IntegerKeeper();
			keeper.setId(keeperId);
			System.out.println("Loading integerKeeper: id=" + keeper.getId());
			keeper.load();
			loadedPersistentlist.add(keeper);
		}
		
		for (Integer msgId : consoleMessageIdList) {
			ConsoleMessage msg = new ConsoleMessage();
			msg.setId(msgId);
			System.out.println("Loading consoleMessage: id=" + msg.getId());
			msg.load();
			loadedPersistentlist.add(msg);
		}
		
	}

	private void store() {
		System.out.println("Storing items...");
		for (Iterator<Persistent> iterator = persistentlist.iterator(); iterator.hasNext();) {
			Persistent persistent = (Persistent) iterator.next();
			if (persistent.store()) {
				System.out.println("Stored:\n" + persistent.getContent());
			} else {
				System.out.println("Error storing:\n" + persistent.getContent());
			}
		}
	}
	private void send() {
		System.out.println("Sending messages...");
		for (Iterator<Persistent> iterator = persistentlist.iterator(); iterator.hasNext();) {
			Persistent persistent = (Persistent) iterator.next();
			if (persistent instanceof Message) {
				Message msg = (Message) persistent;
				System.out.println("Sending message: id=" + msg.getId());
				msg.send();
			}
		}
	}

	private void build() {
		System.out.println("Building lists...");

		persistentlist.clear();
		integerKeeperIdList.clear();
		consoleMessageIdList.clear();

		int keeperCount = IntegerUtil.getRandom(0, 5);
		int msgCount = IntegerUtil.getRandom(0, 5);

		System.out.println(String.format("Creating %d IntegerKeepers", keeperCount));
		// Insert integer keepers
		for (int i = 0; i < keeperCount; i++) {
			IntegerKeeper integerKeeper = new IntegerKeeper();
			integerKeeper.setId(IntegerUtil.getRandom(1, Integer.MAX_VALUE));
			integerKeeper.setGuest(IntegerUtil.getRandom(1, Integer.MAX_VALUE));
			System.out.println("Adding integerKeeper: id=" + integerKeeper.getId());
			persistentlist.add(integerKeeper);
			integerKeeperIdList.add(integerKeeper.getId());
		}
		// insert console messages
		String sender = System.getProperty("user.name");
		LoremIpsum loremIpsum = new LoremIpsum();
		System.out.println(String.format("Creating %d ConsoleMessages", msgCount));
		for (int i = 0; i < msgCount; i++) {
			ConsoleMessage consoleMessage = new ConsoleMessage();
			consoleMessage.setId(IntegerUtil.getRandom(1, Integer.MAX_VALUE));
			consoleMessage.setSender(sender);
			consoleMessage.setText(loremIpsum.getWords(IntegerUtil.getRandom(5, 10)));
			System.out.println("Adding consoleMessage: id=" + consoleMessage.getId());
			persistentlist.add(consoleMessage);
			consoleMessageIdList.add(consoleMessage.getId());
		}
	}

	public static void main(String[] args) {
		RunExample runExample = new RunExample();
		runExample.runExample();

	}

}
