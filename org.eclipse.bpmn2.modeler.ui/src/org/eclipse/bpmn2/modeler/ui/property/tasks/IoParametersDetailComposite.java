/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/


package org.eclipse.bpmn2.modeler.ui.property.tasks;


import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

/**
 * This class renders the property sheet tab for I/O Parameters
 * defined in Activities, CallableElements and ThrowEvents.
 * 
 * TODO: handle ThrowEvent parameters
 */
public class IoParametersDetailComposite extends AbstractDetailComposite {

	AbstractListComposite inputSetsTable;
	AbstractListComposite dataInputsTable;
	AbstractListComposite outputSetsTable;
	AbstractListComposite dataOutputsTable;
	
	public IoParametersDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public IoParametersDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public void cleanBindings() {
		super.cleanBindings();
		inputSetsTable = null;
		dataInputsTable = null;
		outputSetsTable = null;
		dataOutputsTable = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2DetailComposite
	 * #createBindings(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void createBindings(final EObject be) {
		final EStructuralFeature ioSpecificationFeature = be.eClass().getEStructuralFeature("ioSpecification");
		if (ioSpecificationFeature != null) {
			// the control parameter must be an Activity or CallableElement (i.e. a Process or GlobalTask)
			InputOutputSpecification ioSpecification = (InputOutputSpecification)be.eGet(ioSpecificationFeature);
			if (ioSpecification==null) {
				ioSpecification = (InputOutputSpecification) FACTORY.createInputOutputSpecification();
				InsertionAdapter.add(be, ioSpecificationFeature, ioSpecification);
			}

			EStructuralFeature inputSetsFeature = getFeature(ioSpecification, "inputSets");
			inputSetsTable = new IoSetsListComposite(this, be, ioSpecification, inputSetsFeature);
			inputSetsTable.bindList(ioSpecification, inputSetsFeature);
			inputSetsTable.setTitle("Input Sets");

			EStructuralFeature dataInputsFeature = getFeature(ioSpecification, "dataInputs");
			dataInputsTable = new IoParametersListComposite(this, be, ioSpecification, dataInputsFeature);
			dataInputsTable.bindList(ioSpecification, dataInputsFeature);
			dataInputsTable.setTitle("Input Parameters");


			EStructuralFeature outputSetsFeature = getFeature(ioSpecification, "outputSets");
			outputSetsTable = new IoSetsListComposite(this, be, ioSpecification, outputSetsFeature);
			outputSetsTable.bindList(ioSpecification, outputSetsFeature);
			outputSetsTable.setTitle("Output Sets");
			
			EStructuralFeature dataOutputsFeature = getFeature(ioSpecification, "dataOutputs");
			dataOutputsTable = new IoParametersListComposite(this, be, ioSpecification, dataOutputsFeature);
			dataOutputsTable.bindList(ioSpecification, dataOutputsFeature);
			dataOutputsTable.setTitle("Output Parameters");
		}
		else {
			// the control is a ThrowEvent
		}
	}
}