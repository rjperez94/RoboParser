package part2Terminals;

import java.util.Scanner;

import mainProgram.Robot;

public interface Operation {
	
	public int evaluate(Robot robot);
	
	public boolean parseLeft(Scanner s);
	
	public boolean parseRight(Scanner s);
	
	public String toString();
}
