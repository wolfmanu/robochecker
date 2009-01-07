
public class cantMoveException extends Exception {
	private static final long serialVersionUID = 833927490028484483L;

	public cantMoveException() {
	}

	public cantMoveException(String message) {
		super(message);
	}

	public cantMoveException(Throwable cause) {
		super(cause);
	}

	public cantMoveException(String message, Throwable cause) {
		super(message, cause);
	}

}
