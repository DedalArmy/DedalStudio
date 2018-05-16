package test.rcp.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


public class ComponentInspectorAction {
	
	private Text text;
    private Browser browser;

    @PostConstruct
    public void createControls(Composite parent) {
        parent.setLayout(new GridLayout(2, false));

        text = new Text(parent, SWT.BORDER);
        text.setMessage("Enter Projects Folder Location");
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Button button = new Button(parent, SWT.PUSH);
        button.setText("Search");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String uri = text.getText();
                String[] tab = {uri};
                if (uri.isEmpty()) {
                    return;
                }
                fr.ema.dedal.componentinspector.main.Main.main(tab);
            }
        });

        browser = new Browser(parent, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

    }

    @Focus
    public void onFocus() {
        text.setFocus();
    }

	
}
