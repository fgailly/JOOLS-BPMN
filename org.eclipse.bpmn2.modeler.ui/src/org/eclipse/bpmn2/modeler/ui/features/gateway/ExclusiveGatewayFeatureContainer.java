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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.features.gateway;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.features.gateway.AbstractCreateGatewayFeature;
import org.eclipse.bpmn2.modeler.core.features.gateway.AddGatewayFeature;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil.DiagonalCross;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class ExclusiveGatewayFeatureContainer extends AbstractGatewayFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof ExclusiveGateway;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddGatewayFeature<ExclusiveGateway>(fp) {
			@Override
			protected void decorateGateway(ContainerShape container, BPMNShape bpmnShape) {
				// TODO: handle showExclusiveGatewayMarker property change event in BPMN2Editor
				// and override the default gateway UpdateFeature to show/hide the "X" marker.
				if (bpmnShape.isIsMarkerVisible()) {
					GraphicsUtil.createGatewayDiagonalCross(container);
				}
			}
		};
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateExclusiveGatewayFeature(fp);
	}

	public static class CreateExclusiveGatewayFeature extends AbstractCreateGatewayFeature<ExclusiveGateway> {

		public CreateExclusiveGatewayFeature(IFeatureProvider fp) {
			super(fp, "Exclusive Gateway", "Exclusive decision and merging");
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_EXCLUSIVE_GATEWAY;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getExclusiveGateway();
		}
	}
}