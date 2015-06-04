package com.novicehacks.autobot.core.types;

import com.novicehacks.autobot.core.annotations.Incomplete;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * 
 * @incomplete For future use, under development and research
 *
 */
@Incomplete
@Deprecated
public class SQLCommand implements Command {

	@Override
	public String commandTxt() {
		return null;
	}

	@Override
	public String id() {
		return null;
	}

	@Override
	public String description() {
		return null;
	}

	@Override
	public int compareTo(Command o) {
		return 0;
	}

	@Override
	public String mapKey() {
		return null;
	}

}
