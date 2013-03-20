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

package org.eclipse.bpmn2.modeler.ui.adapters.properties;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class ServiceTaskPropertiesAdapter extends TaskPropertiesAdapter<ServiceTask> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ServiceTaskPropertiesAdapter(AdapterFactory adapterFactory, ServiceTask object) {
		super(adapterFactory, object);
    	setProperty(Bpmn2Package.eINSTANCE.getServiceTask_OperationRef(), UI_CAN_CREATE_NEW, Boolean.FALSE);
    	setProperty(Bpmn2Package.eINSTANCE.getServiceTask_OperationRef(), UI_CAN_EDIT, Boolean.FALSE);

    	EStructuralFeature ref = Bpmn2Package.eINSTANCE.getServiceTask_OperationRef();
    	setFeatureDescriptor(ref, new RootElementRefFeatureDescriptor<ServiceTask>(adapterFactory,object,ref));
	}

}
