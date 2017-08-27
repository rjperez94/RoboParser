package part1Terminals;

import part0Terminals.RootNode;
import mainProgram.BlockNode;
import mainProgram.Robot;
import mainProgram.RobotProgramNode;

public class WhileCondition extends RootNode implements BlockNode {
	public Condition cond = new Condition();
	
	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		while (cond.evaluate(robot)) {
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
	@Override
	public String toString(int depth) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth; i++)
			builder.append("  ");
		
		builder.append("while (");
		builder.append(cond.toString());
		builder.append(") {\n");
		for (RobotProgramNode rNode: children) {
			for (int i = 0; i < depth; i++)
				builder.append("  ");
			builder.append(rNode.toString(depth+1));
		}
		for (int i = -1; i < depth; i++)
			builder.append("  ");
		builder.append("}\n");
		return builder.toString();
	}
	
}
