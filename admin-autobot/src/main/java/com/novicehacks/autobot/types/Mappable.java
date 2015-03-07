package com.novicehacks.autobot.types;

import com.novicehacks.autobot.core.BotUtils;

/**
 * 
 * Mappable objects can be converted from A Collection object to a Map object.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see BotUtils#CreateMap(java.util.Collection)
 */
public interface Mappable {

	public String mapKey();

}
