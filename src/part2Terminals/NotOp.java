package part2Terminals;

import java.util.Scanner;

import part1Terminals.EqualTo;
import part1Terminals.GreaterThan;
import part1Terminals.LessThan;
import mainProgram.ParserFailureException;
import mainProgram.Robot;

public class NotOp implements Operation {
	private Operation value1 ;

	public NotOp(Scanner s) {
		// TODO Auto-generated constructor stub
		if (!parseLeft(s)) {
			throw new ParserFailureException("Failed parsing Not Condition"); 
		}
	}

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		if (value1.evaluate(robot) <= 0) return 1;
		else return -1;
	}

	@Override
	public boolean parseLeft(Scanner s) {
		// TODO Auto-generated method stub
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
		}
		return false;
	}

	@Override
	public boolean parseRight(Scanner s) {
		// TODO Auto-generated method stub
		return true;		//does nothing
	}

	private boolean parseGT(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new GreaterThan(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseLT(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new LessThan(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseEQ(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new EqualTo(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseAnd(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new AndOp(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseOr(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new OrOp(s);
			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseNot(Scanner s) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			this.value1 = new NotOp(s);
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
		builder.append("not( ");
		builder.append(value1.toString());
		builder.append(")");
		return builder.toString();
	}
}
