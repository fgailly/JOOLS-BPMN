package org.eclipse.bpmn2.modeler.runtime.example;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.example.SampleModel.SampleModelPackage;
import org.eclipse.bpmn2.modeler.ui.features.flow.SequenceFlowFeatureContainer;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Connection;

public class SampleSequenceFlowFeatureContainer extends SequenceFlowFeatureContainer {

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		// TODO Auto-generated method stub
		return super.getCreateFeature(fp);
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
		return new CreateSequenceFlowFeature(fp) {

			@Override
			public Connection create(ICreateConnectionContext context) {
				// TODO Auto-generated method stub
				return super.create(context);
			}

			@Override
			public SequenceFlow createBusinessObject(ICreateConnectionContext context) {
				SequenceFlow sf = super.createBusinessObject(context);
				Definitions defs = ModelUtil.getDefinitions(sf);
				EStructuralFeature attr = SampleModelPackage.eINSTANCE.getDocumentRoot_SampleCustomFlowValue();
				sf.eSet(attr, "50");
				return sf;
			}
			
		};
	}

}
