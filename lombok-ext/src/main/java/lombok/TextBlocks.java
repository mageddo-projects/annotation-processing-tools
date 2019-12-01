package lombok;

public final class TextBlocks {

	private TextBlocks() {
	}

	/**
	 * This method requires RSL to work otherwise an exception will be thrown
	 *
	 * @throws IllegalStateException if RSL didn't inject the variable
	 * @return Throws an exception if called
	 */
	public static String lazyInit(){
		throw new IllegalStateException("RSL were supposed to inject this variable");
	}
}
