package test.rcp.action;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

public class ComponentInspectorDialogHandler {
    @Execute
    public void execute(final Shell shell) {
    	InspectorDialog dialog = new InspectorDialog(shell);
    	dialog.open();
    }
}