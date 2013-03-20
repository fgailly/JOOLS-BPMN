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

import org.eclipse.bpmn2.modeler.core.IBpmn2RuntimeExtension;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

/**
 * @author Bob Brodt
 *
 */
public class SampleRuntimeExtension implements IBpmn2RuntimeExtension {

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.core.IBpmn2RuntimeExtension#getTargetNamespace(org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType)
	 */
	@Override
	public String getTargetNamespace(Bpmn2DiagramType diagramType) {
		return "http://org.eclipse.bpmn2.modeler.runtime.example";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.core.IBpmn2RuntimeExtension#initialize()
	 */
	@Override
	public void initialize(DiagramEditor editor) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.core.IBpmn2RuntimeExtension#isContentForRuntime(org.eclipse.bpmn2.modeler.core.IFile)
	 */
	@Override
	public boolean isContentForRuntime(IEditorInput input) {
		// IMPORTANT: The plugin is responsible for inspecting the file contents!
		// Unless you are absolutely sure that the file is targeted for this runtime
		// (by, e.g. looking at the targetNamespace or some other feature) then this
		// method must return FALSE.
		return false;
	}

	@Override
	public Composite getPreferencesComposite(Composite parent,
			Bpmn2Preferences preferences) {
		return null;
	}
}
