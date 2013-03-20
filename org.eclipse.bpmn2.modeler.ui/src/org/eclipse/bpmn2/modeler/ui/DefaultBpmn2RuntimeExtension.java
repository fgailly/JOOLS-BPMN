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
package org.eclipse.bpmn2.modeler.ui;
import org.eclipse.bpmn2.modeler.core.IBpmn2RuntimeExtension;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;


public class DefaultBpmn2RuntimeExtension implements IBpmn2RuntimeExtension {

	public DefaultBpmn2RuntimeExtension() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isContentForRuntime(IEditorInput input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTargetNamespace(Bpmn2DiagramType diagramType){
		String type = "";
		switch (diagramType) {
		case PROCESS:
			type = "/process";
			break;
		case COLLABORATION:
			type = "/collaboration";
			break;
		case CHOREOGRAPHY:
			type = "/choreography";
			break;
		}
		return "http://sample.bpmn2.org/bpmn2/sample" + type;
	}

	@Override
	public void initialize(DiagramEditor editor) {
		// TODO Auto-generated method stub
	}

	@Override
	public Composite getPreferencesComposite(Composite parent, Bpmn2Preferences preferences) {
		return null;
	}
}
