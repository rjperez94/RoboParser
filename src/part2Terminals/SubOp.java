package part2Terminals;

import java.util.Scanner;
import java.util.regex.Pattern;

import part3Terminals.BarrelFBWithArgs;
import part3Terminals.BarrelLRWithArgs;
import mainProgram.ParserFailureException;
import mainProgram.Robot;

public class SubOp implements Operation {
	private Operation value1 ;
	private Operation value2 ;
	private String sens1 = "";
	private String sens2 = "";
	

	public SubOp(Scanner s) {
		// TODO Auto-generated constructor stub
		if (!parseLeft(s) || !s.next().equals(",") || !parseRight(s)) {
			throw new ParserFailureException("Failed parsing Add Operation"); 
		}
	}

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		int num1 = 0;
		int num2 = 0;
		
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
		
		if (sens2.equals("")) {	//right, ... operation or number
			num2 = value2.evaluate(robot);
		} else {
			switch (sens2) {
				case "fuelLeft": num2 = robot.getFuel(); break;
				case "oppLR":  num2 = robot.getOpponentLR(); break;
				case "oppFB": num2 = robot.getOpponentFB(); break;
				case "numBarrels": num2 = robot.numBarrels(); break;
				case "barrelLR": num2 = robot.getClosestBarrelLR(); break;
				case "barrelFB":  num2 = robot.getClosestBarrelFB(); break;
				case "wallDist": num2 = robot.getDistanceToWall(); break;
			}
			 
		}
		
		if (value2 instanceof BarrelFBWithArgs) {
			num2 = robot.getBarrelFB(value2.evaluate(robot));
		} else if (value2 instanceof BarrelLRWithArgs) {
			num2 = robot.getBarrelLR(value2.evaluate(robot));
		}
		
		return  num1 - num2;
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
				return parseAdd(s, 1);
			} else if (word.equals("sub") ) {
				return parseSub(s, 1);
			} else if (word.equals("mul") ) {
				return parseMul(s, 1);
			} else if (word.equals("div") ) {
				return parseDiv(s, 1);
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
		if (s.hasNext(Pattern.compile("-?\\d+"))) {
			//integer
			value2 = new Number(s.next());
			return true;
		}
		
		if (s.hasNext(Pattern.compile("add|sub|mul|div"))) {
			//operation
			String word = s.next();
			if (word.equals("add") ) {
				return parseAdd(s, 2);
			} else if (word.equals("sub") ) {
				return parseSub(s, 2);
			} else if (word.equals("mul") ) {
				return parseMul(s, 2);
			} else if (word.equals("div") ) {
				return parseDiv(s, 2);
			}
		}
		
		String word = s.next();
		if (word.equals("fuelLeft") || word.equals("oppLR") || word.equals("oppFB") || word.equals("numBarrels") ||
				word.equals("barrelLR") || word.equals("barrelFB") || word.equals("wallDist")) {
			
			if (word.equals("barrelLR")  && s.hasNext(Pattern.compile("\\("))) {
				value2 = parseBLR(s);
				if (value2 == null) return false;
				else return true;
			} else if (word.equals("barrelFB") && s.hasNext(Pattern.compile("\\("))) {
				value2 = parseBFB(s);
				if (value2 == null) return false;
				else return true;
			}
			
			//sensor	
			sens2 = word;
			return true;
		} 
		return false;
	}
	
	private Operation parseBFB(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			BarrelFBWithArgs temp = new BarrelFBWithArgs(s);
			
			if (!s.next().equals(")")) {
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
			
			if (!s.next().equals(")")) {
				return null;
			} else {
				return temp;
			}
		}
		return null;
	}
	
	private boolean parseDiv(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new DivOp(s);
			else this.value2 = new DivOp(s);
			
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	private boolean parseMul(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new MulOp(s);
			else this.value2 = new MulOp(s);
			
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	private boolean parseSub(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new SubOp(s);
			else this.value2 = new SubOp(s);
			
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	private boolean parseAdd(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new AddOp(s);
			else this.value2 = new AddOp(s);
			
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("sub( ");
		if (value1 != null) {
			builder.append(value1.toString());
		} else {
			builder.append(sens1);
		}
		builder.append(", ");
		if (value2 != null) {
			builder.append(value2.toString());
		} else {
			builder.append(sens2);
		}
		builder.append(")");
		return builder.toString();
	}
}
