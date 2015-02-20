package test.com.novicehacks.autobot.core.stubs;

import com.novicehacks.autobot.types.Mappable;

public class MappableStub implements Mappable {
	private String	key;

	public MappableStub (String k) {
		this.key = k;
	}

	@Override
	public String mapKey() {
		return this.key;
	}

}
