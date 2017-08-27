package part1Terminals;

import java.util.ArrayList;
import java.util.List;

import part0Terminals.RootNode;
import part2Terminals.Else;
import mainProgram.BlockNode;
import mainProgram.Robot;
import mainProgram.RobotProgramNode;

public class IfCondition extends RootNode implements BlockNode {
	public Condition cond = new Condition();
	public List<IfCondition> elseIfs = new ArrayList<IfCondition>();
	public Else elseBlock = null;
	
	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		if (cond.evaluate(robot)) {
			for (RobotProgramNode rNode: children) {
				rNode.execute(robot);
			} 
		} else if (!elseIfs.isEmpty()) {
			for (IfCondition block: elseIfs) {
				if (block.cond.evaluate(robot)) {
					block.execute(robot);
					return;
				}
			}
		} else {
			if (elseBlock != null) {
				for (RobotProgramNode elseNode: elseBlock.children) {
					elseNode.execute(robot);
				}
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(int depth) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth; i++)
			builder.append("  ");
		
		builder.append("if (");
		builder.append(cond.toString());
		builder.append(") {\n");
		for (RobotProgramNode rNode: children) {
			for (int i = 0; i < depth; i++)
				builder.append("  ");
			builder.append(rNode.toString(depth+1));
		}
		for (int i = -1; i < depth; i++)
			builder.append("   ");
		
		int counter = 0;
		for (IfCondition block: elseIfs) {
			counter++;
			builder.append("\nelse "+block.toString(depth));
			
			if (counter < elseIfs.size()) builder.append("}");
		}
		
		if (elseBlock == null) {
			builder.append("\n");
		} else {
			builder.append("}\n"+elseBlock.toString(depth));
		}
		
		return builder.toString();
	}

	
}
