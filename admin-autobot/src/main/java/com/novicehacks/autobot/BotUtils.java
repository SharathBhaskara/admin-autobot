package com.novicehacks.autobot;

/**
 * Utility Methods, that are frequently used are grouped together in BotUtils.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class BotUtils {

	/**
	 * Will check that none of the parameters are having null values. If any one
	 * params is having null then it will return a false.
	 * 
	 * @param params
	 * @return <p>
	 *         true if no objects have null value
	 *         </p>
	 *         <p>
	 *         false if any object has null value
	 *         </p>
	 */
	public static boolean NotNullParams(Object... params) {
		for (Object object : params) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

}
