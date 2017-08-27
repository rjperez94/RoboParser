package part1Terminals;

import java.util.NoSuchElementException;
import java.util.Scanner;

import part2Terminals.AndOp;
import part2Terminals.NotOp;
import part2Terminals.Operation;
import part2Terminals.OrOp;
import mainProgram.Robot;

public class Condition {
	private Operation operation = null;

	public boolean evaluate(Robot robot) {
		return operation.evaluate(robot) > 0;	//1 == true, 0 == false
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return operation.toString();
	}
	
	public boolean validCondition(Scanner s) {
		// TODO Auto-generated method stub
		try {
			//set operator
			String keyword = s.next();
			//System.out.println(keyword);
			
			if (keyword.equals("gt")) {
				return parseGT(s);
			} else if (keyword.equals("lt")) {
				return parseLT(s);
			} else if (keyword.equals("eq")) {
				return parseEQ(s);
			} else if (keyword.equals("and")) {
				return parseAnd(s);
			} else if (keyword.equals("or")) {
				return parseOr(s);
			} else if (keyword.equals("not")) {
				return parseNot(s);
			} else {
				return false;
			}
		
		//if it gets here, invalid syntax
		} catch (NoSuchElementException e) {return false;}
	}
	
	private boolean parseGT(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new GreaterThan(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseLT(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new LessThan(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseEQ(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new EqualTo(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseAnd(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new AndOp(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseOr(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new OrOp(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseNot(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			operation = new NotOp(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
}
