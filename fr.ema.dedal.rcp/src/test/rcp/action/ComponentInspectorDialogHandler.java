package test.rcp.action;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ComponentInspectorDialogHandler {
	@Execute
	public void execute(final Shell shell) {
		InspectorDialog dialog = new InspectorDialog(shell);
		IProject project = dialog.getSelectedProject();
		if (project != null) {
			dialog.open();
		} else {
			MessageDialog.openError(shell, "Error", "A project's root must be selected");
		}
	}
}