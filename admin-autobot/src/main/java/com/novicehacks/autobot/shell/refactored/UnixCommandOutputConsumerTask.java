package com.novicehacks.autobot.shell.refactored;

import java.io.OutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnixCommandOutputConsumerTask implements Runnable {

	Lock			shellLock;
	Condition		SendInput;
	Condition		InputComplete;
	OutputStream	outputStream;
	StringBuilder	commandOutput;
	private Logger	logger	= LogManager.getLogger ();

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
