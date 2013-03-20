package org.eclipse.bpmn2.modeler.ui.views.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class BPMNShapeTreeEditPart extends AbstractGraphicsTreeEditPart {

	public BPMNShapeTreeEditPart(DiagramTreeEditPart dep, BPMNShape bpmnShape) {
		super(dep, bpmnShape);
	}

	public BPMNShape getBPMNShape() {
		return (BPMNShape) getBpmnModel();
	}

	// ======================= overwriteable behaviour ========================

	/**
	 * Creates the EditPolicies of this EditPart. Subclasses often overwrite
	 * this method to change the behaviour of the editpart.
	 */
	@Override
	protected void createEditPolicies() {
	}

	@Override
	protected List<Object> getModelChildren() {
		List<Object> retList = new ArrayList<Object>();
		BPMNShape bpmnShape = getBPMNShape();
		// TODO
		return retList;
	}
	
	@Override
	protected String getText() {
		BPMNShape bpmnShape = getBPMNShape();
		if (bpmnShape.getBpmnElement()==null)
			return super.getText(bpmnShape);
		return super.getText(bpmnShape.getBpmnElement());
	}
}