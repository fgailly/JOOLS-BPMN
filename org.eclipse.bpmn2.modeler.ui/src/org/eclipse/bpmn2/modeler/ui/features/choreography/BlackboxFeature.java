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

package org.eclipse.bpmn2.modeler.ui.features.choreography;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;

/**
 * @author Bob Brodt
 *
 */
public class BlackboxFeature extends AbstractCustomFeature {

	/**
	 * @param fp
	 */
	public BlackboxFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
	    return "Blackbox";
	}
	
	@Override
	public String getDescription() {
	    return "Delete the Diagram for this Participant";
	}

	@Override
	public String getImageId() {
		return ImageProvider.IMG_16_BLACKBOX;
	}

	@Override
	public boolean isAvailable(IContext context) {
		return true;
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (bo instanceof Participant) {
				Participant participant = (Participant)bo;
				Process process = participant.getProcessRef();
				if (process!=null) {
					try {
						ModelHandler mh = ModelHandlerLocator.getModelHandler(participant.eResource());
						DiagramElement de = mh.findDIElement(process);
						return de!=null;
					}
					catch (Exception e){
					}
				}
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.custom.ICustomFeature#execute(org.eclipse.graphiti.features.context.ICustomContext)
	 */
	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (bo instanceof Participant) {
				Participant participant = (Participant)bo;
				Process process = participant.getProcessRef();

				if (process!=null) {
					participant.setProcessRef(null);
					
					// can only delete this Process and BPMNDiagram if the Participant is not
					// a Participant Band of a ChoreographyTask because the Process may still
					// be used (or reused) by other Participants.
					if (!ChoreographyUtil.isChoreographyParticipantBand(pe)) {
						try {
							boolean canDeleteProcess = true;
							ModelHandler mh = ModelHandlerLocator.getModelHandler(participant.eResource());
							List<Participant> participants = mh.getAll(Participant.class);
							for (Participant p : participants) {
								if (p.getProcessRef() == process) {
									canDeleteProcess = false;
									break;
								}
							}
		
							if (canDeleteProcess) {
								DiagramElement de = mh.findDIElement(process);
								if (de.eContainer() instanceof BPMNDiagram) {
									BPMNDiagram bpmnDiagram = (BPMNDiagram)de.eContainer();
									DIUtils.deleteDiagram(getDiagramEditor(), bpmnDiagram);
								}
								
								EcoreUtil.delete(process);
							}
						}
						catch (Exception e){
						}
					}
				}
			}
		}
	}
}
