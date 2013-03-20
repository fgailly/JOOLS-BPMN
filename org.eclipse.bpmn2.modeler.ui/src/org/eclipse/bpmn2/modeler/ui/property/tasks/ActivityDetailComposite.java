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

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.LoopCharacteristics;
import org.eclipse.bpmn2.MultiInstanceLoopCharacteristics;
import org.eclipse.bpmn2.StandardLoopCharacteristics;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ActivityDetailComposite extends DefaultDetailComposite {

	private Button noneButton;
	private Button addStandardLoopButton;
	private Button addMultiLoopButton;
	protected AbstractDetailComposite loopCharacteristicsComposite;
	
	public ActivityDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public ActivityDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public void cleanBindings() {
		super.cleanBindings();
		noneButton = null;
		addStandardLoopButton = null;
		addMultiLoopButton = null;
		loopCharacteristicsComposite = null;
	}
	
	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"anyAttribute",
						"calledElementRef", // only used in CallActivity
						"calledChoreographyRef", // only used in CallChoreography
						"startQuantity",
						"completionQuantity",
						"completionCondition",
						"isForCompensation",
						"triggeredByEvent",
						"cancelRemainingInstances",
						"loopCharacteristics",
						"properties",
						"resources",
						"method",
						"ordering",
						"protocol",
				};
				
				@Override
				public String[] getProperties() {
					return properties; 
				}
			};
		}
		return propertiesProvider;
	}

	protected void bindReference(Composite parent, EObject object, EReference reference) {
		if ("loopCharacteristics".equals(reference.getName())) {

			final Activity activity = (Activity) businessObject;
			LoopCharacteristics loopCharacteristics = (LoopCharacteristics) activity.getLoopCharacteristics();
				
			Composite composite = getAttributesParent();

			createLabel(composite, "Loop Characteristics");
			
			Composite buttonComposite = toolkit.createComposite(composite);
			buttonComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
			FillLayout layout = new FillLayout();
			layout.marginWidth = 20;
			buttonComposite.setLayout(layout);
			
			noneButton = toolkit.createButton(buttonComposite, "None", SWT.RADIO);
			noneButton.setSelection(loopCharacteristics == null);
			noneButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					if (noneButton.getSelection()) {
						@SuppressWarnings("restriction")
						TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
						domain.getCommandStack().execute(new RecordingCommand(domain) {
							@Override
							protected void doExecute() {
								if (activity.getLoopCharacteristics() !=null)
									activity.setLoopCharacteristics(null);
								setBusinessObject(activity);
							}
						});
					}
				}
			});
			
			addStandardLoopButton = toolkit.createButton(buttonComposite, "Standard", SWT.RADIO);
			addStandardLoopButton.setSelection(loopCharacteristics instanceof StandardLoopCharacteristics);
			addStandardLoopButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					if (addStandardLoopButton.getSelection()) {
						@SuppressWarnings("restriction")
						TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
						domain.getCommandStack().execute(new RecordingCommand(domain) {
							@Override
							protected void doExecute() {
								StandardLoopCharacteristics loopChar = FACTORY.createStandardLoopCharacteristics();
								activity.setLoopCharacteristics(loopChar);
								ModelUtil.setID(loopChar);
								setBusinessObject(activity);
							}
						});
					}
				}
			});

			addMultiLoopButton = toolkit.createButton(buttonComposite, "Multi-Instance", SWT.RADIO);
			addMultiLoopButton.setSelection(loopCharacteristics instanceof MultiInstanceLoopCharacteristics);
			addMultiLoopButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					if (addMultiLoopButton.getSelection()) {
						@SuppressWarnings("restriction")
						TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
						domain.getCommandStack().execute(new RecordingCommand(domain) {
							@Override
							protected void doExecute() {
								MultiInstanceLoopCharacteristics loopChar = FACTORY.createMultiInstanceLoopCharacteristics();
								activity.setLoopCharacteristics(loopChar);
								ModelUtil.setID(loopChar);
								setBusinessObject(activity);
							}
						});
					}
				}
			});
			
			if (loopCharacteristics != null) {
				loopCharacteristicsComposite = PropertiesCompositeFactory.createDetailComposite(
						loopCharacteristics.eClass().getInstanceClass(), composite, SWT.NONE);
				loopCharacteristicsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
				loopCharacteristicsComposite.setBusinessObject(loopCharacteristics);
				loopCharacteristicsComposite.setTitle(loopCharacteristics instanceof StandardLoopCharacteristics ?
						"Standard Loop Characteristics" : "Multi-Instance Loop Characteristics");
			}
			else if (loopCharacteristicsComposite!=null) {
				loopCharacteristicsComposite.dispose();
				loopCharacteristicsComposite = null;
			}

		}
		else
			super.bindReference(parent, object, reference);
		
		redrawPage();
	}
}