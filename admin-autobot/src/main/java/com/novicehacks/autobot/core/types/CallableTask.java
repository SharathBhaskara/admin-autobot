package com.novicehacks.autobot.core.types;

import java.util.concurrent.Callable;

public interface CallableTask<V> extends Callable<V>, Task {

}
