package com.novicehacks.autobot.core;

import java.util.concurrent.Callable;

public interface CallableTask<V> extends Callable<V>, Task {

}
