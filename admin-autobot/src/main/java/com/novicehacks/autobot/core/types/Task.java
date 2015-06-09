package com.novicehacks.autobot.core.types;

/**
 * A task is a small peice of code that runs on a thread, and set the flag when
 * it is started.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public interface Task {

	public boolean isThreadStarted();
}
