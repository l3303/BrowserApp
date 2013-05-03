package ken.server.environment;

public enum ServerProtocol {

	/**No encryption.*/
	HTTP("http://"),
	/**Encryption.*/
	HTTPS("https://");

	/**The used transport protocol.*/
	private final String mProtocol;

	/**Initializes the enum.
	 * @param pProtocol The used transport protocol.
	 * */
	private ServerProtocol(final String pProtocol) {
		mProtocol = pProtocol;
	}

	/**Delivers the used transport protocol.
	 * @return The used protocol.
	 * */
	public String getProtocol() {
		return mProtocol;
	}

	@Override
	public String toString() {
		return getProtocol();
	}
}