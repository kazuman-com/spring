package hello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

@Component
public class MyDiff implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		final String control = "<a><b attr=\"abc\"></b><aaa k=\"zz\"/></a>";
		final String test = "<a><b attr=\"xyz\"></b></a>";

		Diff myDiff = DiffBuilder.compare(control).withTest(test)
				.withDifferenceEvaluator(new IgnoreAttributeDifferenceEvaluator("attr")).checkForSimilar().build();

		for (Difference d : myDiff.getDifferences()) {
			Comparison c = d.getComparison();
			Node node = c.getControlDetails().getTarget();

			for (Node n: this.getPath(node)) {
				System.out.println(n.getNodeName());
			}
			
//			System.out.println("> " + c.getControlDetails().getValue());
//			System.out.println(node.toString());
		}

		return null;
	}

	private List<Node> getPath(Node node) {
		// 最下層
		if (node.getFirstChild() != null) {
			return new ArrayList<Node>();
		} else {
			return this.getParentPath(node);
		}
	}
	
	private List<Node> getParentPath(Node node) {
		if (node.getParentNode() != null) {
			List<Node> nodeList = this.getParentPath(node.getParentNode());
			nodeList.add(node);
			return nodeList;
		} else {
			List<Node> ret = new ArrayList<>();
			ret.add(node);
			return ret;
		}
	}
}

