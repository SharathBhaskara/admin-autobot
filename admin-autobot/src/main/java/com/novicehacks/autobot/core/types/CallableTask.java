package com.novicehacks.autobot.core.types;

import java.util.concurrent.Callable;
/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * 
 * @param <V>
 */
public interface CallableTask<V> extends Callable<V>, Task {

}
