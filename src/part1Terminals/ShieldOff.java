package part1Terminals;

import mainProgram.Robot;
import mainProgram.RobotProgramNode;

public class ShieldOff implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		robot.setShield(false);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(int depth) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth; i++)
			builder.append("  ");
		builder.append("shieldOff\n");
		return builder.toString();
	}
}
