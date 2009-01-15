package it.polito.util;

public interface HumanInput {
	public boolean waitForStart(boolean locking);
	public boolean waitForMove(boolean locking);
	public void init();
	public void destroy();
}
