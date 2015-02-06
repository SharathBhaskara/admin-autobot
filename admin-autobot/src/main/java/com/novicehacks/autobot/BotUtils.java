package com.novicehacks.autobot;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.novicehacks.autobot.types.Mappable;

/**
 * Utility Methods, that are frequently used are grouped together in BotUtils.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class BotUtils {

	/**
	 * Will check that none of the parameters are having null values. If even
	 * one param is having null then it will return a false.
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
		for (Object object : params) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Will create a Map from the Collection of Mappable Objects. The key of the
	 * map will be the <em>mapKey</em> of the object.
	 * 
	 * @param collection
	 * @return
	 */
	public static Map<String, Mappable>
			CreateMap(Collection<? extends Mappable> collection) {
		Map<String, Mappable> finalMap;
		finalMap = new TreeMap<String, Mappable> ();
		if (collection == null || collection.size () == 0) {
			return finalMap;
		}
		for (Mappable mappable : collection) {
			finalMap.put (mappable.mapKey (), mappable);
		}
		return finalMap;
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
	public static <T> Map<String, T>
			CreateMap(Collection<? extends Mappable> collection, T type) {
		Map<String, T> finalMap;
		finalMap = new TreeMap<String, T> ();
		if (collection == null || collection.size () == 0) {
			return finalMap;
		}
		for (Mappable mappable : collection) {
			@SuppressWarnings ("unchecked")
			T object = (T) mappable;
			finalMap.put (mappable.mapKey (), object);
		}
		return finalMap;
	}

	/**
	 * Propogates the Interrupted Exception if the exception is
	 * InterruptedException or Thread is interrupted.
	 * 
	 * @param ex
	 */
	public static void propogateInterruptIfExist(Exception exception) {
		if (exception instanceof InterruptedException || Thread.interrupted ()) {
			Thread.currentThread ().interrupt ();
		}
	}
}
