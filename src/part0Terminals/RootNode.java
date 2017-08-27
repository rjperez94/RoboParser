package part0Terminals;

import java.util.ArrayList;
import java.util.List;

import mainProgram.BlockNode;
import mainProgram.Robot;
import mainProgram.RobotProgramNode;

public class RootNode implements BlockNode {
	public List<RobotProgramNode> children = new ArrayList<RobotProgramNode>();
	
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
	@Override
	public String toString() {
		//System.out.print("Root size "+children.size());
		StringBuilder builder = new StringBuilder();
		builder.append("Root { \n");
		for (RobotProgramNode rNode: children) {
			builder.append(rNode.toString(1));
		}
		builder.append("}\n");
		return builder.toString();
	}

	@Override
	//fullfils interface requirements but does nothing
	public String toString(int depth) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
