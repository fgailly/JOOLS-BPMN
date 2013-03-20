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

package org.eclipse.bpmn2.modeler.core.merrimac.dialogs;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ObjectEditingDialog extends AbstractObjectEditingDialog {

	protected EClass eclass;

	public ObjectEditingDialog(DiagramEditor editor, EObject object) {
		this(editor,object,object.eClass());
	}
	
	public ObjectEditingDialog(DiagramEditor editor, EObject object, EClass eclass) {
		super(editor, object);
		this.eclass = eclass;
	}
	
	protected Composite createDialogContent(Composite parent) {
		Composite content = PropertiesCompositeFactory.createDialogComposite(
				eclass, parent, SWT.NONE);
		return content;
	}

	@Override
	protected String getPreferenceKey() {
		return eclass.getName();
	}
	
	@Override
	public int open() {
		title = null;
		if (object!=null)
			title = "Edit " + ModelUtil.getLabel(object);
		create();
		if (cancel)
			return Window.CANCEL;
		if (title==null)
			title = "Create New " + ModelUtil.getLabel(object);
		return super.open();
	}
}