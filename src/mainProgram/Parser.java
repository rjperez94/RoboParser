package mainProgram;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;

import javax.swing.JFileChooser;

import part0Terminals.*;
import part1Terminals.*;
import part2Terminals.*;

/** The parser and interpreter.
    The top level parse function, a main method for testing, and several
    utility methods are provided.
    You need to implement parseProgram and all the rest of the parser.
 */

public class Parser {
	/**
	 * Top level parse method, called by the World
	 */
	static RobotProgramNode parseFile(File code){
		Scanner scan = null;
		try {
			scan = new Scanner(code);

			// the only time tokens can be next to each other is
			// when one of them is one of (){},;
			scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

			RobotProgramNode n = parseProgram(scan);  // You need to implement this!!!

			scan.close();
			return n;
		} catch (FileNotFoundException e) {
			System.out.println("Robot program source file not found");
		} catch (ParserFailureException e) {
			System.out.println("Parser error:");
			System.out.println(e.getMessage());
			scan.close();
		}
		return null;
	}

	/** For testing the parser without requiring the world */

	public static void main(String[] args){
		if (args.length>0){
			for (String arg : args){
				File f = new File(arg);
				if (f.exists()){
					System.out.println("Parsing '"+ f+"'");
					RobotProgramNode prog = parseFile(f);
					System.out.println("Parsing completed ");
					if (prog!=null){
						System.out.println("================\nProgram:");
						System.out.println(prog);}
					System.out.println("=================");
				}
				else {System.out.println("Can't find file '"+f+"'");}
			}
		} else {
			while (true){
				JFileChooser chooser = new JFileChooser(".");//System.getProperty("user.dir"));
				int res = chooser.showOpenDialog(null);
				if(res != JFileChooser.APPROVE_OPTION){ break;}
				RobotProgramNode prog = parseFile(chooser.getSelectedFile());
				System.out.println("Parsing completed");
				if (prog!=null){
					System.out.println("Program: \n"+prog);
				}
				System.out.println("=================");
			}
		}
		System.out.println("Done");
	}

	// Useful Patterns
	//private static Pattern NUMPAT = Pattern.compile("-?\\d+");  //("-?(0|[1-9][0-9]*)");
	private static Pattern OPENPAREN = Pattern.compile("\\(");
	private static Pattern CLOSEPAREN = Pattern.compile("\\)");
	private static Pattern OPENBRACE = Pattern.compile("\\{");
	private static Pattern CLOSEBRACE = Pattern.compile("\\}");
	private static Pattern ELSEPAT = Pattern.compile("else");
	private static Pattern ELSEIFPAT = Pattern.compile("elif");

	/**    PROG  ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s){
		//THE PARSER GOES HERE!
		/*while (s.hasNext()) {
			System.out.println(s.next());
		}*/
		RootNode root = new RootNode();

		while (s.hasNext()) {
			String keyword = s.next();
			//System.out.println(keyword);

			if (keyword.equals("move")) {
				if (gobble(OPENPAREN, s)) {
					addMoveWithArgs(root, s);
				} else {
					addMove(root, s);
				}
			} else if (keyword.equals("turnL")) {
				addTurnLeft(root, s);
			} else if (keyword.equals("turnR")) {
				addTurnRight(root, s);
			} else if (keyword.equals("turnAround")) {
				addTurnAround(root, s);
			} else if (keyword.equals("shieldOn")) {
				addShieldOn(root, s);
			} else if (keyword.equals("shieldOff")) {
				addShieldOff(root, s);
			} else if (keyword.equals("takeFuel")) {
				addTakeFuel(root, s);
			} else if (keyword.equals("wait")) {
				if (gobble(OPENPAREN, s)) {
					addWaitWithArgs(root, s);
				} else {
					addWait(root, s);
				}

			} else if (keyword.equals("while")) {
				if (!addBlock(root, s, 0)) {
					fail("Failed at adding while at root", s);
				}
			} else if (keyword.equals("if")) {
				if (!addBlock(root, s , 1)) {
					fail("Failed at adding if at root", s);
				}
			} else if (keyword.equals("loop")) {
				if (!addBlock(root, s, 2)) {
					fail("Failed at adding loop at root", s);
				}
			} else {
				fail("Failed: Invalid terminal at root", s);
			}

		}

		return root;
		//return null;     // just so it will compile!!
	}

	private static void addWaitWithArgs(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new WaitArgs(s));
		if (!gobble(CLOSEPAREN, s)) {
			fail("No closing parenthisis present for optional wait argment", s);
		}
		terminator(s);
	}

	private static void addMoveWithArgs(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new MoveArgs(s));
		if (!gobble(CLOSEPAREN, s)) {
			fail("No closing parenthisis present for optional move argment", s);
		}
		terminator(s);
	}

	private static boolean addBlock (RootNode root, Scanner s, int classifier) {
		// TODO Auto-generated method stub
		IfCondition ifCond = null;
		WhileCondition whCond = null;
		Loop lp = null;
		if (classifier == 1) {
			ifCond = new IfCondition();
			root.addAsChild(ifCond);
		} else if (classifier == 2) {
			lp = new Loop();
			root.addAsChild(lp);
		} else {
			whCond = new WhileCondition();
			root.addAsChild(whCond);
		}

		boolean hasSTMT = false;
		//for if-else only
		boolean hasElif = false;
		boolean validElif = false;

		//validate and update condition for if and while only
		if (classifier == 1) checkCondition(s, ifCond, classifier);
		else if (classifier == 0) checkCondition(s, whCond, classifier);

		if (gobble(OPENBRACE, s)) {
			while (s.hasNext()) {
				if (gobble(CLOSEBRACE, s)) {
					if (hasSTMT) {
						if (classifier == 1) {
							while (gobble(ELSEIFPAT, s)) {
								hasElif = true;
								validElif = parseElseIf(s, ifCond);
							}
							
							if (gobble(ELSEPAT, s)) {
								return parseElse(s, ifCond);
							} else {
								if (hasElif) {
									if (validElif) return true;
									return false;
								}
								return true;
							}
						}
						return true;
					} else {
						return false;
					}
				}

				String keyword = s.next();
				//System.out.println(keyword);

				if (keyword.equals("move")) {
					hasSTMT = true;
					if (classifier == 1) {
						if (gobble(OPENPAREN, s)) {
							addMoveWithArgs(ifCond, s);
						} else {
							addMove(ifCond, s);
						}

					} else if (classifier == 2) {
						if (gobble(OPENPAREN, s)) {
							addMoveWithArgs(lp, s);
						} else {
							addMove(lp, s);
						}
					} else {
						if (gobble(OPENPAREN, s)) {
							addMoveWithArgs(whCond, s);
						} else {
							addMove(whCond, s);
						}
					}
				} else if (keyword.equals("turnL")) {
					hasSTMT = true;
					if (classifier == 1) addTurnLeft(ifCond, s);
					else if (classifier == 2) addTurnLeft(lp, s);
					else addTurnLeft(whCond, s);
				} else if (keyword.equals("turnR")) {
					hasSTMT = true;
					if (classifier == 1) addTurnRight(ifCond, s);
					else if (classifier == 2) addTurnRight(lp, s);
					else addTurnRight(whCond, s);
				} else if (keyword.equals("turnAround")) {
					hasSTMT = true;
					if (classifier == 1) addTurnAround(ifCond, s);
					else if (classifier == 2) addTurnAround(lp, s);
					else addTurnAround(whCond, s);
				} else if (keyword.equals("shieldOn")) {
					hasSTMT = true;
					if (classifier == 1) addShieldOn(ifCond, s);
					else if (classifier == 2) addShieldOn(lp, s);
					else addShieldOn(whCond, s);
				} else if (keyword.equals("shieldOff")) {
					hasSTMT = true;
					if (classifier == 1) addShieldOff(ifCond, s);
					else if (classifier == 2) addShieldOff(lp, s);
					else addShieldOff(whCond, s);
				} else if (keyword.equals("takeFuel")) {
					hasSTMT = true;
					if (classifier == 1) addTakeFuel(ifCond, s);
					else if (classifier == 2) addTakeFuel(lp, s);
					else addTakeFuel(whCond, s);
				} else if (keyword.equals("wait")) {
					hasSTMT = true;
					if (classifier == 1) {
						if (gobble(OPENPAREN, s)) {
							addWaitWithArgs(ifCond, s);
						} else {
							addWait(ifCond, s);
						}
					} else if (classifier == 2) {
						if (gobble(OPENPAREN, s)) {
							addWaitWithArgs(lp, s);
						} else {
							addWait(lp, s);
						}
					} else {
						if (gobble(OPENPAREN, s)) {
							addWaitWithArgs(whCond, s);
						} else {
							addWait(whCond, s);
						}
					}
				} else if (keyword.equals("while")) {
					hasSTMT = true;
					if (classifier == 1) {
						if (!addBlock(ifCond, s, 0)) {
							fail("Failed at adding while as child of if condition", s);
						}
					} else if (classifier == 2) {
						if (!addBlock(lp, s, 0)) {
							fail("Failed at adding loop as child of if condition", s);
						}
					} else {
						if (!addBlock(whCond, s, 0)) {
							fail("Failed at adding while as child of while condition", s);
						}
					}
				} else if (keyword.equals("if")) {
					hasSTMT = true;
					if (classifier == 1) {
						if (!addBlock(ifCond, s, 1)) {
							fail("Failed at adding if as child of if condition", s);
						}
					} else if (classifier == 2) {
						if (!addBlock(lp, s, 1)) {
							fail("Failed at adding loop as child of if condition", s);
						}
					} else {
						if (!addBlock(whCond, s, 1)) {
							fail("Failed at adding if as child of while condition", s);
						}
					}
				} else if (keyword.equals("loop")) {
					hasSTMT = true;
					if (classifier == 1) {
						if (!addBlock(ifCond, s, 2)) {
							fail("Failed at adding loop as child of if condition", s);
						}
					} else if (classifier == 2) {
						if (!addBlock(lp, s, 2)) {
							fail("Failed at adding loop as child of loop condition", s);
						}
					} else {
						if (!addBlock(whCond, s, 2)) {
							fail("Failed at adding loop as child of while condition", s);
						}
					}
				} else {
					if (classifier == 1) fail("Failed: Invalid terminal at if", s);
					else if (classifier == 2) fail("Failed: Invalid terminal at loop", s);
					else fail("Failed: Invalid terminal at while condition", s);
				}
			}
		} else {
			if (classifier == 1) fail("Failed: Invalid syntax inside if", s);
			else if (classifier == 2) fail("Failed: Invalid syntax inside loop", s);
			else fail("Failed: Invalid syntax inside while", s);
		}

		return false;
	}

	private static boolean parseElseIf(Scanner s, IfCondition ifCond) {
		// TODO Auto-generated method stub
		IfCondition newIfCond = new IfCondition();
		ifCond.elseIfs.add(newIfCond);
		
		boolean hasSTMT = false;
		checkCondition(s, newIfCond, 1);	//method requires classifier argument, if condition === classifier 1

		if (gobble(OPENBRACE, s)) {
			while (s.hasNext()) {
				if (gobble(CLOSEBRACE, s)) {
					if (hasSTMT) {
						return true;
					} else {
						return false;
					}
				}

				String keyword = s.next();
				//System.out.println(keyword);

				if (keyword.equals("move")) {
					hasSTMT = true;
					if (gobble(OPENPAREN, s)) {
						addMoveWithArgs(newIfCond, s);
					} else {
						addMove(newIfCond, s);
					}
				} else if (keyword.equals("turnL")) {
					hasSTMT = true;
					addTurnLeft(newIfCond, s);
				} else if (keyword.equals("turnR")) {
					hasSTMT = true;
					addTurnRight(newIfCond, s);
				} else if (keyword.equals("turnAround")) {
					hasSTMT = true;
					addTurnAround(newIfCond, s);
				} else if (keyword.equals("shieldOn")) {
					hasSTMT = true;
					addShieldOn(newIfCond, s);
				} else if (keyword.equals("shieldOff")) {
					hasSTMT = true;
					addShieldOff(newIfCond, s);
				} else if (keyword.equals("takeFuel")) {
					hasSTMT = true;
					addTakeFuel(newIfCond, s);
				} else if (keyword.equals("wait")) {
					hasSTMT = true;
					if (gobble(OPENPAREN, s)) {
						addWaitWithArgs(newIfCond, s);
					} else {
						addWait(newIfCond, s);
					}
				} else if (keyword.equals("while")) {
					hasSTMT = true;
					if (!addBlock(newIfCond, s, 0)) {
						fail("Failed at adding while as child of else if clause", s);
					}
				} else if (keyword.equals("if")) {
					hasSTMT = true;
					if (!addBlock(newIfCond, s, 1)) {
						fail("Failed at adding if as child of else if clause", s);
					}
				} else if (keyword.equals("loop")) {
					hasSTMT = true;
					if (!addBlock(newIfCond, s, 2)) {
						fail("Failed at adding loop as child of else if clause", s);
					}
				} else {
					fail("Failed: Invalid terminal at else if clause", s);
				}
			}
		} else {
			fail("Failed: Invalid syntax inside else if clause", s);
		}

		return false;
	}

	private static boolean parseElse(Scanner s, IfCondition ifCond) {
		ifCond.elseBlock = new Else();

		boolean hasSTMT = false;

		if (gobble(OPENBRACE, s)) {
			while (s.hasNext()) {
				if (gobble(CLOSEBRACE, s)) {
					if (hasSTMT) {
						return true;
					} else {
						return false;
					}
				}

				String keyword = s.next();
				//System.out.println(keyword);

				if (keyword.equals("move")) {
					hasSTMT = true;
					if (gobble(OPENPAREN, s)) {
						addMoveWithArgs(ifCond.elseBlock, s);
					} else {
						addMove(ifCond.elseBlock, s);
					}
				} else if (keyword.equals("turnL")) {
					hasSTMT = true;
					addTurnLeft(ifCond.elseBlock, s);
				} else if (keyword.equals("turnR")) {
					hasSTMT = true;
					addTurnRight(ifCond.elseBlock, s);
				} else if (keyword.equals("turnAround")) {
					hasSTMT = true;
					addTurnAround(ifCond.elseBlock, s);
				} else if (keyword.equals("shieldOn")) {
					hasSTMT = true;
					addShieldOn(ifCond.elseBlock, s);
				} else if (keyword.equals("shieldOff")) {
					hasSTMT = true;
					addShieldOff(ifCond.elseBlock, s);
				} else if (keyword.equals("takeFuel")) {
					hasSTMT = true;
					addTakeFuel(ifCond.elseBlock, s);
				} else if (keyword.equals("wait")) {
					hasSTMT = true;
					if (gobble(OPENPAREN, s)) {
						addWaitWithArgs(ifCond.elseBlock, s);
					} else {
						addWait(ifCond.elseBlock, s);
					}
				} else if (keyword.equals("while")) {
					hasSTMT = true;
					if (!addBlock(ifCond.elseBlock, s, 0)) {
						fail("Failed at adding while as child of else clause", s);
					}
				} else if (keyword.equals("if")) {
					hasSTMT = true;
					if (!addBlock(ifCond.elseBlock, s, 1)) {
						fail("Failed at adding if as child of else clause", s);
					}
				} else if (keyword.equals("loop")) {
					hasSTMT = true;
					if (!addBlock(ifCond.elseBlock, s, 2)) {
						fail("Failed at adding loop as child of else clause", s);
					}
				} else {
					fail("Failed: Invalid terminal at else clause", s);
				}
			}
		} else {
			fail("Failed: Invalid syntax inside else clause", s);
		}

		return false;
	}

	private static void checkCondition(Scanner s, RootNode parent, int classifier) {
		if (!gobble(OPENPAREN, s)) {	//opening of cond
			fail("Failed: Invalid condition construct", s);
		}

		if (classifier == 1) {
			IfCondition ifCond = (IfCondition) parent;
			if (!ifCond.cond.validCondition(s)) {
				fail("Failed: Invalid condition construct", s);
			}
		} else {
			WhileCondition whCond = (WhileCondition) parent;
			if (!whCond.cond.validCondition(s)) {
				fail("Failed: Invalid condition construct", s);
			}
		}

		if (!gobble(CLOSEPAREN, s)) {		//closing while cond
			fail("Failed: Invalid condition construct", s);
		}
	}

	private static void addShieldOff(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new ShieldOff());
		terminator(s);
	}

	private static void addShieldOn(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new ShieldOn());
		terminator(s);
	}

	private static void addTurnAround(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new TurnAround());
		terminator(s);
	}

	private static void addWait(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new Wait());
		terminator(s);
	}

	private static void addTakeFuel(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new TakeFuel());
		terminator(s);
	}

	private static void addTurnRight(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new TurnRight());
		terminator(s);
	}

	private static void addTurnLeft(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new TurnLeft());
		terminator(s);
	}

	private static void addMove(RootNode root, Scanner s) {
		// TODO Auto-generated method stub
		root.addAsChild(new Move());
		terminator(s);
	}

	private static void terminator(Scanner s) {
		// TODO Auto-generated method stub
		String term = s.next();
		//System.out.println(term);
		if (!term.equals(";")) {
			//System.out.println("GETS HERE");
			fail("Failed at ", s);
		}
	}

	//utility methods for the parser
	/**
	 * Report a failure in the parser.
	 */
	static void fail(String message, Scanner s){
		String msg = message + "\n   @ ...";
		for (int i=0; i<5 && s.hasNext(); i++){
			msg += " " + s.next();
		}
		throw new ParserFailureException(msg+"...");
	}

	/**
       If the next token in the scanner matches the specified pattern,
       consume the token and return true. Otherwise return false without
       consuming anything.
       Useful for dealing with the syntactic elements of the language
       which do not have semantic content, and are there only to
       make the language parsable.
	 */
	static boolean gobble(String p, Scanner s){
		if (s.hasNext(p)) { s.next(); return true;} 
		else { return false; } 
	}
	static boolean gobble(Pattern p, Scanner s){
		if (s.hasNext(p)) { s.next(); return true;} 
		else { return false; } 
	}
}

// You could add the node classes here, as long as they are not declared public (or private)
