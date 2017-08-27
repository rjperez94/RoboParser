package part2Terminals;

import java.util.Scanner;

import part1Terminals.EqualTo;
import part1Terminals.GreaterThan;
import part1Terminals.LessThan;
import mainProgram.ParserFailureException;
import mainProgram.Robot;

public class OrOp implements Operation {
	private Operation value1 ;
	private Operation value2 ;
	
	public OrOp(Scanner s) {
		// TODO Auto-generated constructor stub
		if (!parseLeft(s) || !s.next().equals(",") || !parseRight(s)) {
			throw new ParserFailureException("Failed parsing Or Condition"); 
		}
	}

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		if (value1.evaluate(robot) > 0 || value2.evaluate(robot) > 0) return 1;
		else return -1;
	}

	@Override
	public boolean parseLeft(Scanner s) {
		// TODO Auto-generated method stub
		//set operator
		String keyword = s.next();
		//System.out.println(keyword);

		if (keyword.equals("gt")) {
			return parseGT(s, 1);
		} else if (keyword.equals("lt")) {
			return parseLT(s, 1);
		} else if (keyword.equals("eq")) {
			return parseEQ(s, 1);
		} else if (keyword.equals("and")) {
			return parseAnd(s, 1);
		} else if (keyword.equals("or")) {
			return parseOr(s, 1);
		} else if (keyword.equals("not")) {
			return parseNot(s, 1);
		}
		return false;
	}

	@Override
	public boolean parseRight(Scanner s) {
		// TODO Auto-generated method stub
		//set operator
		String keyword = s.next();
		//System.out.println(keyword);

		if (keyword.equals("gt")) {
			return parseGT(s, 2);
		} else if (keyword.equals("lt")) {
			return parseLT(s, 2);
		} else if (keyword.equals("eq")) {
			return parseEQ(s, 2);
		} else if (keyword.equals("and")) {
			return parseAnd(s, 2);
		} else if (keyword.equals("or")) {
			return parseOr(s, 2);
		} else if (keyword.equals("not")) {
			return parseNot(s, 2);
		}
		return false;
	}
	
	private boolean parseGT(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new GreaterThan(s);
			else this.value2 = new GreaterThan(s);

			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseLT(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new LessThan(s);
			else this.value2 = new LessThan(s);

			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseEQ(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new EqualTo(s);
			else this.value2 = new EqualTo(s);

			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseAnd(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new AndOp(s);
			else this.value2 = new AndOp(s);

			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseOr(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new OrOp(s);
			else this.value2 = new OrOp(s);

			if (s.next().equals(")")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parseNot(Scanner s, int classifier) {
		// TODO Auto-generated method stub
		if (s.next().equals("(")) {
			if (classifier == 1) this.value1 = new NotOp(s);
			else this.value2 = new NotOp(s);

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
		builder.append("or( ");
		builder.append(value1.toString());
		builder.append(", ");
		builder.append(value2.toString());
		builder.append(")");
		return builder.toString();
	}
}
