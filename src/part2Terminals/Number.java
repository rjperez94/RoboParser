package part2Terminals;

import java.util.Scanner;

import mainProgram.Robot;

public class Number implements Operation {
	int value;
	
	/**
	 * @param word
	 */
	public Number(String word) {
		this.value = Integer.parseInt(word);
	}

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public boolean parseLeft(Scanner s) {
		// does nothing
		return true;
	}

	@Override
	public boolean parseRight(Scanner s) {
		// does nothing
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ""+value;
	}
	
	
}
