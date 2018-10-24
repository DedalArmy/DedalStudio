package fr.ema.dedal.rcp;

import java.util.MissingResourceException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import dedal.presentation.DedalEditorPlugin;
import dedal.provider.DedalEditPlugin;

/**
 * This is the page where the type of object to create is selected.
 */
public class DedalWizardPage extends WizardPage {

	protected Text fileField;
	protected Text projectField;

	/**
	 * Pass in the selection.
	 */
	public DedalWizardPage(String pageId) {
		super(pageId);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
			layout.verticalSpacing = 12;
			composite.setLayout(layout);

			GridData data = new GridData();
			data.verticalAlignment = GridData.FILL;
			data.grabExcessVerticalSpace = true;
			data.horizontalAlignment = GridData.FILL;
			composite.setLayoutData(data);
		}

		Label projectURILabel = new Label(composite, SWT.LEFT);
		{
			projectURILabel.setText(DedalEditorPlugin.INSTANCE.getString("_UI_DedalModelWizard_project_label"));

			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			projectURILabel.setLayoutData(data);
		}
		projectField = new Text(composite, SWT.BORDER);
		GridData gd_projectField = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		projectField.setLayoutData(gd_projectField);
		{
			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.grabExcessHorizontalSpace = true;
			data.horizontalSpan = 1;
			projectField.setLayoutData(data);
		}

		projectField.addModifyListener(validator);

		Label resourceURILabel = new Label(composite, SWT.LEFT);
		{
			resourceURILabel.setText(DedalEditorPlugin.INSTANCE.getString("_UI_DedalModelWizard_file_label"));

			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			resourceURILabel.setLayoutData(data);
		}

		fileField = new Text(composite, SWT.BORDER);
		{
			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.grabExcessHorizontalSpace = true;
			data.horizontalSpan = 1;
			fileField.setLayoutData(data);
		}
		new Label(composite, SWT.NONE);

		fileField.addModifyListener(validator);

		setPageComplete(validatePage());
		setControl(composite);
	}

	protected ModifyListener validator = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			setPageComplete(validatePage());
		}
	};

	protected boolean validatePage() {
		if (projectField.getText().isEmpty()) {
			return false;
		}
		URI fileURI = getFileURI();
		if (fileURI == null || fileURI.isEmpty()) {
			setErrorMessage(null);
			return false;
		}
		setErrorMessage(null);
		return true;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			fileField.setFocus();
		}
	}

	public URI getFileURI() {
		try { 
			return URI.createFileURI(getProjectPath() + "/" + fileField.getText() + ".dedaladl");
		} catch (Exception exception) {
			// Ignore
		}
		return null;
	}
	
	public String getProjectPath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + "/" + getProjectName();
	}
	
	public String getProjectName() {
		return projectField.getText();
	}

	public void selectFileField() {
		fileField.selectAll();
		fileField.setFocus();
	}

	/**
	 * Returns the label for the specified type name.
	 */
	protected String getLabel(String typeName) {
		try {
			return DedalEditPlugin.INSTANCE.getString("_UI_" + typeName + "_type");
		} catch (MissingResourceException mre) {
			DedalEditorPlugin.INSTANCE.log(mre);
		}
		return typeName;
	}
}
