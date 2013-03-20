package org.eclipse.bpmn2.modeler.ui.property.tasks;

import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.Expression;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.MultiInstanceBehavior;
import org.eclipse.bpmn2.MultiInstanceLoopCharacteristics;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.FeatureEditingDialog;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditingDialog;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextAndButtonObjectEditor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class MultiInstanceLoopCharacteristicsDetailComposite extends DefaultDetailComposite {

	ObjectEditor noneBehaviorEventEditor;
	ObjectEditor oneBehaviorEventEditor;
	AbstractListComposite complexBehaviorList;

	public MultiInstanceLoopCharacteristicsDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	public MultiInstanceLoopCharacteristicsDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}
	
	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"anyAttribute",
						"isSequential",
						"inputDataItem",
						"outputDataItem",
						"loopDataInputRef",
						"loopDataOutputRef",
						"completionCondition",
						"loopCardinality",
						"behavior",
						"noneBehaviorEventRef",
						"oneBehaviorEventRef",
						"complexBehaviorDefinition",
				};
				
				@Override
				public String[] getProperties() {
					return properties; 
				}
			};
		}
		return propertiesProvider;
	}

	@Override
	protected void cleanBindings() {
		super.cleanBindings();
		noneBehaviorEventEditor = null;
		oneBehaviorEventEditor = null;
		complexBehaviorList = null;
	}

	public void createBindings(EObject be) {
		super.createBindings(be);
	}
	
	protected void bindAttribute(Composite parent, EObject object, EAttribute attribute, String label) {
		if (attribute.getName().equals("behavior")) {
			ObjectEditor editor = new ComboObjectEditor(this,object,attribute) {

				@Override
				protected boolean updateObject(Object result) {
					MultiInstanceLoopCharacteristics lc = (MultiInstanceLoopCharacteristics)object;
					boolean updated = super.updateObject(result);
					if (updated) {
						switch (lc.getBehavior()) {
						case ALL:
							if (noneBehaviorEventEditor!=null) {
								noneBehaviorEventEditor.setVisible(false);
							}
							if (oneBehaviorEventEditor!=null) {
								oneBehaviorEventEditor.setVisible(false);
							}
							if (complexBehaviorList!=null) {
								complexBehaviorList.setVisible(false);
							}
							break;
						case NONE:
							if (noneBehaviorEventEditor!=null) {
								noneBehaviorEventEditor.setVisible(true);
							}
							if (oneBehaviorEventEditor!=null) {
								oneBehaviorEventEditor.setVisible(false);
							}
							if (complexBehaviorList!=null) {
								complexBehaviorList.setVisible(false);
							}
							break;
						case ONE:
							if (noneBehaviorEventEditor!=null) {
								noneBehaviorEventEditor.setVisible(false);
							}
							if (oneBehaviorEventEditor!=null) {
								oneBehaviorEventEditor.setVisible(true);
							}
							if (complexBehaviorList!=null) {
								complexBehaviorList.setVisible(false);
							}
							break;
						case COMPLEX:
							if (noneBehaviorEventEditor!=null) {
								noneBehaviorEventEditor.setVisible(false);
							}
							if (oneBehaviorEventEditor!=null) {
								oneBehaviorEventEditor.setVisible(false);
							}
							if (complexBehaviorList!=null) {
								complexBehaviorList.setVisible(true);
							}
							break;
						}
						Display.getDefault().asyncExec( new Runnable() {

							@Override
							public void run() {
								redrawPage();
							}
							
						});
					}
					return updated;
				}
			};
			editor.createControl(parent,label);
		}
		else
			super.bindAttribute(parent, object, attribute, label);
	}
	
	protected void bindReference(Composite parent, EObject object, EReference reference) {
		MultiInstanceLoopCharacteristics lc = (MultiInstanceLoopCharacteristics)object;
		EStructuralFeature f;

		if (reference == PACKAGE.getMultiInstanceLoopCharacteristics_InputDataItem()) {
			f = Bpmn2Package.eINSTANCE.getMultiInstanceLoopCharacteristics_InputDataItem();
			if (isModelObjectEnabled(lc.eClass(), f)) {
				DataIoObjectEditor inputDataItemEditor = new DataIoObjectEditor(lc,f);
				inputDataItemEditor.createControl(getAttributesParent(), "Input Paramter");
			}
		}
		else if (reference == PACKAGE.getMultiInstanceLoopCharacteristics_OutputDataItem()) {
			f = Bpmn2Package.eINSTANCE.getMultiInstanceLoopCharacteristics_OutputDataItem();
			if (isModelObjectEnabled(lc.eClass(), f)) {
				DataIoObjectEditor outputDataItemEditor = new DataIoObjectEditor(lc,f);
				outputDataItemEditor.createControl(getAttributesParent(), "Output Paramter");
			}
		}
		else if (reference == PACKAGE.getMultiInstanceLoopCharacteristics_NoneBehaviorEventRef()) {
			String displayName = ModelUtil.getLabel(object, reference);
			noneBehaviorEventEditor = new ComboObjectEditor(this,object,reference);
			noneBehaviorEventEditor.createControl(parent,displayName);
			noneBehaviorEventEditor.setVisible( lc.getBehavior() == MultiInstanceBehavior.NONE );
				
		}		
		else if (reference == PACKAGE.getMultiInstanceLoopCharacteristics_OneBehaviorEventRef()) {
			String displayName = ModelUtil.getLabel(object, reference);
			oneBehaviorEventEditor = new ComboObjectEditor(this,object,reference);
			oneBehaviorEventEditor.createControl(parent,displayName);
			oneBehaviorEventEditor.setVisible( lc.getBehavior() == MultiInstanceBehavior.ONE );
		}		
		else
			super.bindReference(parent, object, reference);
	}
	
	protected AbstractListComposite bindList(EObject object, EStructuralFeature feature, EClass listItemClass) {
		MultiInstanceLoopCharacteristics lc = (MultiInstanceLoopCharacteristics)object;
		if (feature.getName().equals("complexBehaviorDefinition")) {
			complexBehaviorList = super.bindList(getAttributesParent(), object, feature, listItemClass);
			complexBehaviorList.setVisible( lc.getBehavior() == MultiInstanceBehavior.COMPLEX );
			return complexBehaviorList;
		}
		else
			return super.bindList(object, feature, listItemClass);
	}
	
	private class DataIoObjectEditor extends TextAndButtonObjectEditor {

		public DataIoObjectEditor(MultiInstanceLoopCharacteristics object, EStructuralFeature feature) {
			super(MultiInstanceLoopCharacteristicsDetailComposite.this, object, feature);
		}

		@Override
		protected void buttonClicked(int buttonId) {
			if (buttonId==ID_DEFAULT_BUTTON) {
				FeatureEditingDialog dlg = new FeatureEditingDialog(getDiagramEditor(), object, feature);
				dlg.open();
				updateText();
			}
			else if (buttonId==ID_REMOVE_BUTTON) {
				final EObject value = (EObject) object.eGet(feature);
				if (value!=null) {
					TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							object.eUnset(feature);
							EcoreUtil.delete(value);
						}
					});
				}
			}
		}
		
		@Override
		protected boolean updateObject(final Object result) {
			return true;
		}
		
		protected void updateText() {
			// update the read-only text for this DataInput or DataOutput editor:
			// this will be in the form "InputOutputParameterName" -> "MappedObject"
			// the MappedObject will be found in the Data Input/Output object's parent
			// dataInputAssociations or dataOutputAssociations list
			// @see also DataAssociationDetailComposite which does something similar
			BaseElement dataIoObject = (BaseElement) object.eGet(feature);
			String newText = "";
			if (dataIoObject!=null) {
				boolean isInput = (dataIoObject instanceof DataInput);
				DataAssociation association = null;
				EObject container = dataIoObject.eContainer();
				while (container!=null && association==null) {
					EStructuralFeature f;
					List<? extends DataAssociation> associations = null;
					if (container instanceof Activity) {
						if (isInput) {
							f = container.eClass().getEStructuralFeature("dataInputAssociations");
							if (f!=null)
								associations = (List<DataAssociation>)container.eGet(f);
						}
						else {
							f = container.eClass().getEStructuralFeature("dataOutputAssociations");
							if (f!=null)
								associations = (List<DataAssociation>)container.eGet(f);
						}
					}
					else if (container instanceof ThrowEvent && isInput) {
						associations = ((ThrowEvent)container).getDataInputAssociation();
					}
					else if (container instanceof CatchEvent && !isInput) {
						associations = ((CatchEvent)container).getDataOutputAssociation();
					}
					if (associations!=null) {
						for (DataAssociation a : associations) {
							if (isInput) {
								if (a.getTargetRef() == dataIoObject) {
									association = a;
									break;
								}
							}
							else
							{
								for (ItemAwareElement e : a.getSourceRef()) {
									if (e == dataIoObject) {
										association = a;
										break;
									}
								}
								if (association!=null)
									break;
							}
						}
					}
					container = container.eContainer();
				}
	
				newText = ModelUtil.getName(dataIoObject);
				String mappedTo = null;
				if (association!=null) {
					if (isInput) {
						if (association.getSourceRef().size()>0)
							mappedTo = ModelUtil.getDisplayName(association.getSourceRef().get(0));
						else if (association.getAssignment().size()>0)
							mappedTo = "[Expression]";
						else if (association.getTransformation()!=null)
							mappedTo = "[Transformation]";
					}
					else {
						if (association.getTargetRef()!=null)
							mappedTo = ModelUtil.getDisplayName(association.getTargetRef());
						else if (association.getAssignment().size()>0)
							mappedTo = "[Expression]";
						else if (association.getTransformation()!=null)
							mappedTo = "[Transformation]";
					}
				}
				if (mappedTo!=null)
					newText += " -> " + mappedTo;
			}
			
			if (!text.getText().equals(newText)) {
				setText(newText);
			}
		}
		@Override
		protected boolean canRemove() {
			return true;
		}
	}
}
