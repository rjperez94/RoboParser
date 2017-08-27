package part0Terminals;

import java.util.ArrayList;
import java.util.List;

import mainProgram.BlockNode;
import mainProgram.Robot;
import mainProgram.RobotProgramNode;

public class Loop extends RootNode implements BlockNode {
	public List<RobotProgramNode> children = new ArrayList<RobotProgramNode>();

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		while (true) {
			for (RobotProgramNode rNode: children) {
				rNode.execute(robot);
			}
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
		
		builder.append("loop {\n");
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
