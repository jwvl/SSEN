package graph.explorer;

import forms.Form;
import forms.GraphForm;

import java.util.List;
import java.util.Stack;

/**
 * Created by janwillem on 29/03/16.
 */
public class Visitor {
    private Stack<VisitNode> nodesVisited;
    private VisitationGraph graph;
    private final Form goalForm;
    private VisitNode nowVisiting;

    public Visitor(VisitationGraph graph, Form goalForm) {
        this.nodesVisited = new Stack<>();
        this.graph = graph;
        this.goalForm = goalForm;
    }

    public void init(Form startForm) {
        VisitNode firstNode = new VisitNode(startForm);
        nodesVisited.push(firstNode);
        nowVisiting = firstNode;
        traverse();
    }

    private void traverse() {
        while (!nodesVisited.isEmpty()) {
            explore();
        }
    }


    public void explore() {
        Form currentForm = nowVisiting.getForm();
        if (currentForm.equals(goalForm)) {
            backtrack(VisitState.VISITED_GREEN);
        } else if (isDeadEnd(currentForm)) {
            graph.markRed(currentForm);
            backtrack(VisitState.VISITED_RED);
        } else {
            List<Form> successors = graph.getSuccessors(currentForm);
            Form unexplored = null;
            boolean unvisitedFound = false;
            while (!unvisitedFound && nowVisiting.getNowAt() < successors.size()) {
                unexplored = successors.get(nowVisiting.getNowAt());
                unvisitedFound = !(graph.wasVisited(unexplored));
                nowVisiting.increaseNowAt();
            }
            if (unvisitedFound) {
                forward(unexplored);
            } else {
                VisitState state = markNodeRedOrGreen(successors);
                backtrack(state);
            }
        }

    }

    private boolean isDeadEnd(Form currentForm) {
        if (currentForm.equals(GraphForm.getSinkInstance())) {
            return true;
        } else if (currentForm.getLevel().myIndex() >= goalForm.getLevel().myIndex()) {
            return true;
        }
        return false;
    }

    private void forward(Form unexplored) {
        VisitNode forwardNode = new VisitNode(unexplored);
        nodesVisited.push(nowVisiting);
        nowVisiting = forwardNode;

    }

    private VisitState markNodeRedOrGreen(List<Form> successors) {
        VisitState stateToMark = VisitState.VISITED_RED;
        for (Form f : successors) {
            if (graph.getState(f) == VisitState.VISITED_GREEN) {
                stateToMark = VisitState.VISITED_GREEN;
            }
        }
        return stateToMark;
    }

    private void backtrack(VisitState markState) {
        Form currentForm = nowVisiting.getForm();
        VisitNode backTrackingTo = nodesVisited.pop();


        if (markState == VisitState.VISITED_GREEN) {
            Form previous = backTrackingTo.getForm();
            graph.markGreen(currentForm, previous);
        } else if (markState == VisitState.VISITED_RED) {
            graph.markRed(currentForm);
        }
        nowVisiting = backTrackingTo;

    }

}

