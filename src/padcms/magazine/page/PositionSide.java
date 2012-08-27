package padcms.magazine.page;


//Connection
public enum PositionSide {
	LEFT(10), RIGHT(11), TOP(12), BOTTOM(13);
	private int position;
	
	private PositionSide(int position) {
		this.position = position;
	}
	
	public int getCode() {
		return position;
	}
	
	/**
	 * @param val - int value
	 * @return Instance of this enum or null if no instance for <code>val</code>
	 */
	static public PositionSide getTypeValueOf(int val) {
		for (PositionSide typeValue : PositionSide.values()) {
			if (val == typeValue.getCode()) {
				return typeValue;
			}
		}
		return null;
	}
}
