package part2Terminals;

import mainProgram.BlockNode;
import mainProgram.Robot;
import mainProgram.RobotProgramNode;
import part0Terminals.RootNode;

public class Else extends RootNode implements BlockNode {
	
	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		for (RobotProgramNode rNode: children) {
			rNode.execute(robot);
		}
	}

	@Override
	public void addAsChild(RobotProgramNode child) {
		// TODO Auto-generated method stub
		children.add(child);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(int depth) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth; i++)
			builder.append("  ");
		
		builder.append("else {\n");
		for (RobotProgramNode rNode: children) {
			for (int i = 0; i < depth; i++)
				builder.append("  ");
			builder.append(rNode.toString(depth+1));
		}
		
		for (int i = -1; i < depth; i++)
			builder.append("  ");
		builder.append("}\n ");
		return builder.toString();
	}
}
