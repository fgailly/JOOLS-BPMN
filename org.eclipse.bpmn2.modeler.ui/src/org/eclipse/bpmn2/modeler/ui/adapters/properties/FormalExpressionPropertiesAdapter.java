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
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author Bob Brodt
 *
 */
public class FormalExpressionPropertiesAdapter extends ExtendedPropertiesAdapter<FormalExpression> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public FormalExpressionPropertiesAdapter(AdapterFactory adapterFactory, FormalExpression object) {
		super(adapterFactory, object);

    	final EStructuralFeature body = Bpmn2Package.eINSTANCE.getFormalExpression_Body();
    	setFeatureDescriptor(body,
			new FeatureDescriptor<FormalExpression>(adapterFactory,object,body) {
    		
    			@Override
    			
    			public void setValue(Object context, final Object value) {
    				final FormalExpression formalExpression = adopt(context);
    				InsertionAdapter.executeIfNeeded(formalExpression);
    				TransactionalEditingDomain editingDomain = getEditingDomain(formalExpression);
					if (editingDomain == null) {
	    				formalExpression.setBody(value.toString());
					} else {
						editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
							@Override
							protected void doExecute() {
			    				formalExpression.setBody(value.toString());
							}
						});
					}
    			}
    			
	    		@Override
	    		public String getDisplayName(Object context) {
					FormalExpression expression = adopt(context);
					if (expression.getBody()==null)
						return "";
					return expression.getBody();
	    		}
	    		
				@Override
				public String getLabel(Object context) {
					FormalExpression expression = adopt(context);
					if (expression.eContainer() instanceof SequenceFlow)
						return "Constraint";
					return super.getLabel(context);
				}

				@Override
				public boolean isMultiLine(Object context) {
					// formal expression body is always a multiline text field
					return true;
				}
			}
    	);
		setObjectDescriptor(new ObjectDescriptor<FormalExpression>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				return getFeatureDescriptor(body).getDisplayName(context);
			}
		});
	}

}
