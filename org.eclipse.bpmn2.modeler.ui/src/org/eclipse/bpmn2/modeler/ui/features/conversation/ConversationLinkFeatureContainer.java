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
package org.eclipse.bpmn2.modeler.ui.features.conversation;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Conversation;
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.BaseElementConnectionFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractAddFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractReconnectFlowFeature;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Connection;

public class ConversationLinkFeatureContainer extends BaseElementConnectionFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof ConversationLink;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddFlowFeature<ConversationLink>(fp) {

			@Override
			protected Polyline createConnectionLine(Connection connection) {
				Polyline connectionLine = super.createConnectionLine(connection);
				connectionLine.setLineWidth(3);
				return connectionLine;
			}

			@Override
			protected Class<? extends BaseElement> getBoClass() {
				return ConversationLink.class;
			}
		};
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
		return new CreateConversationLinkFeature(fp);
	}

	@Override
	public IReconnectionFeature getReconnectionFeature(IFeatureProvider fp) {
		return new ReconnectConversationLinkFeature(fp);
	}

	public static class CreateConversationLinkFeature extends AbstractCreateFlowFeature<ConversationLink, Participant, Conversation> {

		public CreateConversationLinkFeature(IFeatureProvider fp) {
			super(fp, "Conversation Link", "Connects Conversation nodes to and from Participants");
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_CONVERSATION_LINK;
		}

		@Override
		protected Class<Participant> getSourceClass() {
			return Participant.class;
		}

		@Override
		protected Class<Conversation> getTargetClass() {
			return Conversation.class;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateConnectionFeature#getBusinessObjectClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getConversationLink();
		}

		@Override
		public ConversationLink createBusinessObject(ICreateConnectionContext context) {
			ConversationLink bo = null;
			try {
				ModelHandler mh = ModelHandler.getInstance(getDiagram());
				Participant source = getSourceBo(context);
				Conversation target = getTargetBo(context);
				bo = mh.createConversationLink(source, target);
				bo.setName("Conversation Link");
				putBusinessObject(context, bo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bo;
		}
	}
	public static class ReconnectConversationLinkFeature extends AbstractReconnectFlowFeature {

		public ReconnectConversationLinkFeature(IFeatureProvider fp) {
			super(fp);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Class<? extends EObject> getTargetClass() {
			return Conversation.class;
		}

		@Override
		protected Class<? extends EObject> getSourceClass() {
			return Participant.class;
		}

	}
	
}