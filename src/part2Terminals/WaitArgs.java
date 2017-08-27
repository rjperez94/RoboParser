package part2Terminals;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import part3Terminals.BarrelFBWithArgs;
import part3Terminals.BarrelLRWithArgs;
import mainProgram.ParserFailureException;
import mainProgram.Robot;
import mainProgram.RobotProgramNode;

public class WaitArgs  implements RobotProgramNode {
	private int iterations = 0;
	
	//for 'first layer' only
	private String sensor = "";
	private Operation operation = null;
	private boolean isSen;
	
	public WaitArgs (Scanner s) {
		try {
			if (s.hasNext(Pattern.compile("-?\\d+"))) {
				this.iterations = Integer.parseInt(s.next());
			} else {
				if (!parseSenOrOp(s)) {
					throw new ParserFailureException("Failed at parsing optional argument sensor/operation for wait");
				}
			}
		} catch (NoSuchElementException e) {
			throw new ParserFailureException("Failed at parsing optional argument for wait");
		}
	}
	
	private boolean parseSenOrOp(Scanner s) {
		// TODO Auto-generated method stub
		if (s.hasNext(Pattern.compile("add|sub|mul|div"))) {	//inspect only
			return parseOp (s);
		} else {
			isSen = true;
			return parseSen (s);
		}
	}

	private boolean parseSen(Scanner s) {
		// TODO Auto-generated method stub
		String sen = s.next();
		if (sen.equals("fuelLeft") || sen.equals("oppLR") || sen.equals("oppFB") || sen.equals("numBarrels") ||
				sen.equals("barrelLR") || sen.equals("barrelFB") || sen.equals("wallDist")) {
			
			if (sen.equals("barrelLR")  && s.hasNext(Pattern.compile("\\("))) {
				operation = parseBLR(s);
				if (operation == null) return false;
				else return true;
			} else if (sen.equals("barrelFB") && s.hasNext(Pattern.compile("\\("))) {
				operation = parseBFB(s);
				if (operation == null) return false;
				else return true;
			}
			
			sensor = sen;
			return true;
		}
		return false;
	}

	private boolean parseOp(Scanner s) {
		// TODO Auto-generated method stub
		String oper = s.next();
		if (oper.equals("add") ) {
			return parseAdd(s);
		} else if (oper.equals("sub") ) {
			return parseSub(s);
		} else if (oper.equals("mul") ) {
			return parseMul(s);
		} else if (oper.equals("div") ) {
			return parseDiv(s);
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

	private boolean parseDiv(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new DivOp(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	private boolean parseMul(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new MulOp(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	private boolean parseSub(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new SubOp(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	private boolean parseAdd(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new AddOp(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		if (isSen) {
			switch (sensor) {
				case "fuelLeft": iterations = robot.getFuel(); break;
				case "oppLR":  iterations = robot.getOpponentLR(); break;
				case "oppFB": iterations = robot.getOpponentFB(); break;
				case "numBarrels": iterations = robot.numBarrels(); break;
				case "barrelLR": iterations = robot.getClosestBarrelLR(); break;
				case "barrelFB":  iterations = robot.getClosestBarrelFB(); break;
				case "wallDist": iterations = robot.getDistanceToWall(); break;
			}
			
		} else {
			if (operation != null) iterations = operation.evaluate(robot);
		}
		
		if (operation instanceof BarrelFBWithArgs) {
			iterations = robot.getBarrelFB(operation.evaluate(robot));
		} else if (operation instanceof BarrelLRWithArgs) {
			iterations = robot.getBarrelLR(operation.evaluate(robot));
		}
		
		for (int i = 0; i < iterations; i++) {
			robot.idleWait();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(int depth) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth; i++)
			builder.append("  ");
		if (sensor.equals("") && operation == null) {
			builder.append("wait("+iterations+")\n");
		} else {
			if (isSen) {
				builder.append("wait("+sensor+")\n");
			} else {
				builder.append("wait("+operation.toString()+")\n");
			}
		}
		return builder.toString();
	}

}
