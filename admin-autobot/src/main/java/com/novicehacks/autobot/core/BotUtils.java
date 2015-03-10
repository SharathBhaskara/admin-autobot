package com.novicehacks.autobot.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.novicehacks.autobot.core.types.Mappable;

/**
 * Utility Methods, that are frequently used are grouped together in BotUtils.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class BotUtils {

	/**
	 * Returns true if any of the parameter passed has a null reference to it.
	 * 
	 * If any array or array objects is passed when will not validate, the
	 * contents of that array for null values.
	 * 
	 * @param params
	 * @return <p>
	 *         true if no objects have null value
	 *         </p>
	 *         <p>
	 *         false if any object has null value
	 *         </p>
	 */
	public static boolean HasNullReferences(Object... params) {
		if (params == null)
			return true;
		else
			return HasNullReferencesInArray (params);
	}

	private static boolean HasNullReferencesInArray(Object[] params) {
		for (Object object : params) {
			if (object == null)
				return true;
		}
		return false;
	}

	/**
	 * Will create a Map from the Collection of Mappable Objects. The key of the
	 * map will be the <em>mapKey</em> of the object.
	 * 
	 * @param collection
	 *        collection of objects from which the map is created
	 * @param type
	 *        reference to tell which type of objects are returned, no
	 *        implication in the map creation logic.
	 * @return Map of the objects in collection with the key as given in
	 *         Mappable.mapKey element
	 * @throws ClassCastException
	 *         if the object cannot be casted to the Type specified in T.
	 */
	public static <T extends Mappable> Map<String, T> CreateMap(Collection<T> collection) {
		Map<String, T> finalMap;
		finalMap = new HashMap<String, T> ();

		if (collection == null || collection.size () == 0)
			return finalMap;

		for (T mappable : collection) {
			finalMap.put (mappable.mapKey (), mappable);
		}
		return finalMap;
	}

	/**
	 * Propogates the Interrupted Exception if the exception is
	 * InterruptedException or Thread is interrupted.
	 * 
	 * @param ex
	 */
	public static void PropogateInterruptIfExist(Exception exception) {
		if (exception instanceof InterruptedException || Thread.interrupted ()) {
			Thread.currentThread ().getThreadGroup ().interrupt ();
		}
	}

	/**
	 * Will not propagate interrupt but will set the interrupt status on the
	 * same thread.
	 * 
	 * @param exception
	 */
	public static void DoNotPropogateInterrupt(InterruptedException exception) {
		if (exception instanceof InterruptedException || Thread.interrupted ()) {
			Thread.currentThread ().interrupt ();
		}
	}

	public static String newLine() {
		return System.lineSeparator ();
	}

	/**
	 * Returns true if any of the string is null or has empty strings
	 * <p>
	 * <strong> <em>Empty Strings</em> </strong> are strings of any length
	 * containing only spaces and no data.
	 * </p>
	 * 
	 * @param strings
	 * @return
	 */
	public static boolean HasEmptyStrings(String... strings) {
		if (strings == null)
			return true;
		else
			return HasEmptyStringsInArray (strings);
	}

	private static boolean HasEmptyStringsInArray(String[] strings) {
		for (String string : strings)
			if (string == null || string.trim ().equals (""))
				return true;

		return false;
	}

}
