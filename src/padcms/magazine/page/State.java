package padcms.magazine.page;

public enum State {
	ACTIVE(0), DISACTIVE(1), RELEASE(2), DOWNLOAD(3), EXTRA_ACTIVE(4);
	private int state;

	private State(int state) {
		this.state = state;
	}

	public int getCode() {
		return this.state;
	}

	/**
	 * @param val
	 *            - int value
	 * @return Instance of this enum or null if no instance for <code>val</code>
	 */
	static public State getTypeValueOf(int val) {
		for (State typeValue : State.values()) {
			if (val == typeValue.getCode()) {
				return typeValue;
			}
		}
		return null;
	}
}