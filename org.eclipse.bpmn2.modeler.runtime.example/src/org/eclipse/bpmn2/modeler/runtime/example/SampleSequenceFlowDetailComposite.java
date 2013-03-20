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

package org.eclipse.bpmn2.modeler.runtime.example;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.bpmn2.modeler.runtime.example.SampleModel.SampleModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.connectors.SequenceFlowDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class SampleSequenceFlowDetailComposite extends SequenceFlowDetailComposite {

	/**
	 * @param section
	 */
	public SampleSequenceFlowDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public SampleSequenceFlowDetailComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	public void createBindings(EObject be) {
		EStructuralFeature attr = SampleModelPackage.eINSTANCE.getDocumentRoot_SampleCustomFlowValue();
		ObjectEditor editor = new TextObjectEditor(this,be,attr);
		editor.createControl( getAttributesParent(), "Flow Value");
		super.createBindings(be);
	}
}
