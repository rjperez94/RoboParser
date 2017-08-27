package part0Terminals;

import mainProgram.Robot;
import mainProgram.RobotProgramNode;

public class TurnLeft implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		robot.turnLeft();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(int depth) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth; i++)
			builder.append("  ");
		builder.append("turnL\n");
		return builder.toString();
	}
}
