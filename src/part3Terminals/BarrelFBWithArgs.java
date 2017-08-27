package part3Terminals;

import java.util.Scanner;
import java.util.regex.Pattern;

import mainProgram.ParserFailureException;
import mainProgram.Robot;
import part2Terminals.AddOp;
import part2Terminals.DivOp;
import part2Terminals.MulOp;
import part2Terminals.Number;
import part2Terminals.Operation;
import part2Terminals.SubOp;

public class BarrelFBWithArgs implements Operation {
	private Operation value1 ;
	private String sens1 = "";
	

	public BarrelFBWithArgs(Scanner s) {
		// TODO Auto-generated constructor stub
		if (!parseLeft(s)) {
			throw new ParserFailureException("Failed parsing BarrelFB Argument"); 
		}
	}

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		int num1 = 0;
		
		if (sens1.equals("")) {	//left, ... operation or number
			num1 = value1.evaluate(robot);
		} else {
			switch (sens1) {
				case "fuelLeft": num1 = robot.getFuel(); break;
				case "oppLR":  num1 = robot.getOpponentLR(); break;
				case "oppFB": num1 = robot.getOpponentFB(); break;
				case "numBarrels": num1 = robot.numBarrels(); break;
				case "barrelLR": num1 = robot.getClosestBarrelLR(); break;
				case "barrelFB":  num1 = robot.getClosestBarrelFB(); break;
				case "wallDist": num1 = robot.getDistanceToWall(); break;
			}
			 
		}
		
		if (value1 instanceof BarrelFBWithArgs) {
			num1 = robot.getBarrelFB(value1.evaluate(robot));
		} else if (value1 instanceof BarrelLRWithArgs) {
			num1 = robot.getBarrelLR(value1.evaluate(robot));
		}
		
		return num1;
	}

	@Override
	public boolean parseLeft(Scanner s) {
		// TODO Auto-generated method stub
		if (s.hasNext(Pattern.compile("-?\\d+"))) {
			//integer
			value1 = new Number(s.next());
			return true;
		}
		
		if (s.hasNext(Pattern.compile("add|sub|mul|div"))) {
			//operation
			String word = s.next();
			if (word.equals("add") ) {
				return parseAdd(s);
			} else if (word.equals("sub") ) {
				return parseSub(s);
			} else if (word.equals("mul") ) {
				return parseMul(s);
			} else if (word.equals("div") ) {
				return parseDiv(s);
			}
		}
		
		String word = s.next();
		if (word.equals("fuelLeft") || word.equals("oppLR") || word.equals("oppFB") || word.equals("numBarrels") ||
				word.equals("barrelLR") || word.equals("barrelFB") || word.equals("wallDist")) {
			
			if (word.equals("barrelLR")  && s.hasNext(Pattern.compile("\\("))) {
				value1 = parseBLR(s);
				if (value1 == null) return false;
				else return true;
			} else if (word.equals("barrelFB") && s.hasNext(Pattern.compile("\\("))) {
				value1 = parseBFB(s);
				if (value1 == null) return false;
				else return true;
			}
			
			//sensor	
			sens1 = word;
			return true;
		} 
		return false;
	}

	@Override
	public boolean parseRight(Scanner s) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private boolean parseDiv(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new DivOp(s);
			
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	private boolean parseMul(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new MulOp(s);
			
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	private boolean parseSub(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new SubOp(s);
			
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	private boolean parseAdd(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new AddOp(s);
			
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private Operation parseBFB(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			BarrelFBWithArgs temp = new BarrelFBWithArgs(s);
			
			if (s.next().equals(")")) {
				return null;
			} else {
				return temp;
			}
		}
		return null;
	}

	private Operation parseBLR(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			BarrelLRWithArgs temp = new BarrelLRWithArgs(s);
			
			if (s.next().equals(")")) {
				return null;
			} else {
				return temp;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("barrelFB (");
		if (value1 != null) {
			builder.append(value1.toString());
		} else {
			builder.append(sens1);
		}
		builder.append(")");
		return builder.toString();
	}

}
