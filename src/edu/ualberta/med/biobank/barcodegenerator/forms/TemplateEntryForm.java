package edu.ualberta.med.biobank.barcodegenerator.forms;

import java.net.URL;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISourceProviderListener;
import org.eclipse.ui.PlatformUI;

import edu.ualberta.med.biobank.SessionManager;
import edu.ualberta.med.biobank.barcodegenerator.dialogs.ComboInputDialog;
import edu.ualberta.med.biobank.barcodegenerator.dialogs.StringInputDialog;
import edu.ualberta.med.biobank.barcodegenerator.template.Template;
import edu.ualberta.med.biobank.barcodegenerator.template.TemplateStore;
import edu.ualberta.med.biobank.barcodegenerator.template.presets.cbsr.CBSRLabelMaker;
import edu.ualberta.med.biobank.barcodegenerator.trees.ConfigurationTree;
import edu.ualberta.med.biobank.barcodegenerator.trees.TreeException;
import edu.ualberta.med.biobank.common.wrappers.JasperTemplateWrapper;
import edu.ualberta.med.biobank.gui.common.BgcPlugin;
import edu.ualberta.med.biobank.gui.common.BgcSessionState;
import edu.ualberta.med.biobank.gui.common.forms.BgcFormBase;
import gov.nih.nci.system.applicationservice.ApplicationException;

/**
 * 
 * View for making templates. Consists of a configuration tree editor to edit
 * jasper maker specific configuration settings.
 * 
 * @author Thomas Polasek 2011
 * 
 */

// FIXME add form close listener
public class TemplateEntryForm extends BgcFormBase {

    public static final String ID = "edu.ualberta.med.biobank.barcodegenerator.forms.TemplateEntryForm";
    private Composite composite4 = null;
    private Button deleteButton = null;
    private Button copyButton = null;
    private Button newButton = null;
    private Button helpButton = null;
    private Button saveButton = null;
    private Text templateNameText = null;
    private Text printerNameText = null;
    private Text jasperConfigText = null;

    private List templateNamesList = null;
    private ConfigurationTree configTree = null;
    private String prevTemplateName = null;

    private Shell shell;

    boolean templateDirty = false;
    private TemplateStore templateStore;

    private boolean loggedIn = false;

    // constants
    final private String HELP_URL = "http://www.example.com";

    @Override
    protected void init() throws Exception {
        setPartName("Label Templates");
    }

    @Override
    protected void performDoubleClick(DoubleClickEvent event) {
        // do nothing
    }

    @Override
    protected Image getFormImage() {
        // TODO: select an image for this form
        return null;
    }

    @Override
    protected void createFormContent() throws Exception {
        form.setText("Label Templates");
        form.setMessage(getOkMessage(), IMessageProvider.NONE);
        page.setLayout(new GridLayout(1, false));

        BgcSessionState sessionSourceProvider = BgcPlugin
            .getSessionStateSourceProvider();

        loggedIn = sessionSourceProvider.getCurrentState()
            .get(BgcSessionState.SESSION_STATE_SOURCE_NAME)
            .equals(BgcSessionState.LOGGED_IN);

        shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

        Composite composite = toolkit.createComposite(page);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, true));

        createComposite2(composite);
        createFormButtons();

        sessionSourceProvider
            .addSourceProviderListener(new ISourceProviderListener() {
                @Override
                public void sourceChanged(int sourcePriority,
                    String sourceName, Object sourceValue) {
                    if (sourceValue != null) {
                        loggedIn = sourceValue
                            .equals(BgcSessionState.LOGGED_IN);
                        updateForm();
                    }
                }

                @Override
                public void sourceChanged(int sourcePriority,
                    @SuppressWarnings("rawtypes") Map sourceValuesByName) {
                }
            });
        updateForm();
    }

    protected String getOkMessage() {
        return "Used to create a new template for a printer label";
    }

    @Override
    public void setFocus() {
    }

    private void createComposite2(Composite parent) throws ApplicationException {
        Composite composite2 = toolkit.createComposite(parent);
        composite2.setLayout(new GridLayout(1, false));
        composite2.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, true));

        createGroup1(composite2);
        createComposite3(parent);
        toolkit.paintBordersFor(composite2);
    }

    private void createGroup1(Composite composite) throws ApplicationException {
        Composite client = createSectionWithClient("Label Templates");
        client.setLayout(new GridLayout());
        client.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
            true));
        templateNamesList = new List(client, SWT.BORDER | SWT.V_SCROLL);
        templateNamesList.addSelectionListener(listListener);
        templateNamesList.setLayoutData(new GridData(GridData.FILL,
            GridData.FILL, true, true));

        composite4 = new Composite(client, SWT.NONE);
        composite4.setLayout(new GridLayout(3, true));

        newButton = toolkit.createButton(composite4, "New", SWT.PUSH);
        newButton.addSelectionListener(newListener);
        newButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, true));

        copyButton = toolkit.createButton(composite4, "Copy", SWT.PUSH);
        copyButton.addSelectionListener(copyListener);
        copyButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, true));

        deleteButton = toolkit.createButton(composite4, "Delete", SWT.PUSH);
        deleteButton.addSelectionListener(deleteListener);
        deleteButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, true));
    }

    private void createComposite3(Composite parent) {
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL;
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.grabExcessVerticalSpace = true;
        gridData4.verticalAlignment = GridData.FILL;
        Composite composite3 = new Composite(parent, SWT.NONE);
        composite3.setLayout(new GridLayout());
        composite3.setLayoutData(gridData4);

        createComposite5(composite3);
        createComposite62(composite3);

    }

    private void createComposite62(Composite composite) {
        GridData gridData10 = new GridData();
        gridData10.horizontalAlignment = GridData.FILL;
        gridData10.grabExcessVerticalSpace = true;
        gridData10.verticalAlignment = GridData.FILL;
        GridData gridData9 = new GridData();
        gridData9.grabExcessHorizontalSpace = true;
        gridData9.verticalAlignment = GridData.FILL;
        gridData9.grabExcessVerticalSpace = true;
        gridData9.widthHint = -1;
        gridData9.horizontalAlignment = GridData.FILL;

        Group g6 = new Group(composite, SWT.NONE);
        g6.setLayout(new GridLayout());
        g6.setLayoutData(gridData10);
        g6.setText("Configuration");

        configTree = new ConfigurationTree(g6, SWT.NONE);
    }

    private void createComposite5(Composite composite) {
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.FILL;
        gridData11.grabExcessHorizontalSpace = true;
        gridData11.verticalAlignment = GridData.CENTER;

        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 3;

        Composite composite5 = new Composite(composite, SWT.NONE);
        composite5.setLayout(gridLayout2);
        composite5.setLayoutData(gridData11);

        new Label(composite5, SWT.NONE).setText("Template Name:");

        templateNameText = new Text(composite5, SWT.BORDER);
        templateNameText.setLayoutData(gridData11);
        templateNameText.setEditable(false);
        templateNameText.setEnabled(true);
        // TODO color

        new Label(composite5, SWT.NONE);
        new Label(composite5, SWT.NONE).setText("Jasper Configuration:");

        jasperConfigText = new Text(composite5, SWT.BORDER);
        jasperConfigText.setLayoutData(gridData11);
        jasperConfigText.setEditable(false);
        jasperConfigText.setEnabled(true);

        new Label(composite5, SWT.NONE);
        new Label(composite5, SWT.NONE).setText("Intended Printer:");

        printerNameText = new Text(composite5, SWT.BORDER);
        printerNameText.setEditable(true);
        printerNameText.setLayoutData(gridData11);
        printerNameText.addModifyListener(printerNameModifyListener);

    }

    private void createFormButtons() {
        Composite composite = new Composite(page, SWT.NONE);
        composite.setLayout(new GridLayout(5, true));
        composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
            true, true));

        helpButton = new Button(composite, SWT.NONE);
        helpButton.setText("Help");
        helpButton.addSelectionListener(helpListener);
        helpButton.setLayoutData(new GridData(GridData.FILL,
            GridData.BEGINNING, true, true));

        Composite spacer = new Composite(composite, SWT.NONE);
        GridData gd = new GridData(GridData.FILL, GridData.BEGINNING, true,
            true);
        gd.horizontalSpan = 3;
        spacer.setLayoutData(gd);

        saveButton = new Button(composite, SWT.NONE);
        saveButton.setText("Save Template");
        saveButton.addSelectionListener(saveAllListener);
        saveButton.setLayoutData(new GridData(GridData.FILL,
            GridData.BEGINNING, true, true));
    }

    private void setEnable(boolean enable) {
        configTree.setEnabled(enable);
        deleteButton.setEnabled(enable);
        copyButton.setEnabled(enable);
        newButton.setEnabled(enable);
        helpButton.setEnabled(enable);
        saveButton.setEnabled(enable);
        printerNameText.setEnabled(enable);
    }

    /**
     * 
     * Updates the template name list and jasper file combo.
     * 
     */
    private void updateForm() {
        try {
            if (loggedIn) {
                if (templateStore == null) {
                    templateStore = new TemplateStore();
                }

                setEnable(true);

                for (String s : templateStore.getTemplateNames())
                    templateNamesList.add(s);

                templateNamesList.redraw();

            } else {
                setEnable(false);
                templateNamesList.removeAll();
                templateNamesList.redraw();

            }
        } catch (ApplicationException e) {
            BgcPlugin.openAsyncError("Database Error",
                "Error while updating form", e);
        }
    }

    // if (templateDirty || configTree.isDirty()) {

    private SelectionListener listListener = new SelectionListener() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            String[] selectedItems = templateNamesList.getSelection();
            if (selectedItems.length == 1) {

                // TODO call resetEditor in the appropiate locations
                try {
                    configTree.resetEditor();
                } catch (TreeException e1) {
                    BgcPlugin.openAsyncError("Tree Editor",
                        "Failed to reset tree editor");
                    return;
                }

                try {
                    saveCurrentTemplate();
                } catch (Exception e1) {
                    BgcPlugin.openAsyncError("Template Saving",
                        "Failed to save template: " + e1.getMessage());
                    return;
                }

                if (selectedItems[0] != null) {
                    Template selectedTemplate;
                    try {
                        selectedTemplate = templateStore
                            .getTemplate(selectedItems[0]);
                    } catch (Exception e1) {
                        BgcPlugin.openAsyncError("Template Selection",
                            "Failed to load the selected template");
                        return;
                    }

                    // TODO check that template name matches selectedItem

                    templateNameText.setText(selectedTemplate.getName());
                    prevTemplateName = selectedTemplate.getName();

                    printerNameText.setText(selectedTemplate.getPrinterName());

                    try {
                        jasperConfigText.setText(selectedTemplate
                            .getJasperTemplateName());
                    } catch (Exception e1) {
                        BgcPlugin.openAsyncError("Template Selection",
                            "Failed to find the jasper configuration name.");
                    }
                    printerNameText.setEnabled(true);

                    // TODO handle errors with correct branching.

                    try {
                        configTree.populateTree(selectedTemplate
                            .getConfiguration());
                    } catch (Exception ee) {
                        BgcPlugin.openAsyncError(
                            "Error: Could not set Template Configuration Tree",
                            ee.getMessage());
                        return;
                    }
                    templateDirty = selectedTemplate.isNew();

                } else {
                    prevTemplateName = null;
                    templateNameText.setText("Please select a template");
                    printerNameText.setText("");
                    printerNameText.setEnabled(false);
                    jasperConfigText.setText("");
                    try {
                        configTree.populateTree(null);
                    } catch (TreeException e1) {
                        BgcPlugin
                            .openAsyncError(
                                "Error: Could not clear the Template Configuration Tree",
                                e1.getError());
                    }
                    templateDirty = false;

                }

            } else {
                BgcPlugin.openAsyncError("Selection Listener Error",
                    "invalid selected items length: " + selectedItems.length);
                return;
            }

        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);

        }
    };

    // FIXME get the selected jaspertemplatewrapper correctly.
    private JasperTemplateWrapper getJasperTemplateWrapper(String name)
        throws ApplicationException {

        for (JasperTemplateWrapper t : JasperTemplateWrapper
            .getAllTemplates(SessionManager.getAppService())) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    private void saveCurrentTemplate() throws Exception {

        if (prevTemplateName != null) {

            if (templateDirty || configTree.isDirty()) {
                if (BgcPlugin
                    .openConfirm("Template Saving",
                        "Template has been modified, do you want to save your changes?")) {

                    Template selectedTemplate = templateStore
                        .getTemplate(prevTemplateName);

                    String printerName = printerNameText.getText();
                    if (printerName == null || printerName.length() == 0) {
                        selectedTemplate.setPrinterName("default");
                    } else {
                        if (!printerName.equals(selectedTemplate
                            .getPrinterName())) {
                            selectedTemplate.setPrinterName(printerName);
                        }
                    }

                    if (configTree.isDirty()) {
                        selectedTemplate.setConfiguration(configTree
                            .getConfiguration());
                    }
                    selectedTemplate.persist();

                }

            }
            templateDirty = false;
            configTree.unDirty();

        }
    }

    private SelectionListener newListener = new SelectionListener() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            try {

                StringInputDialog dialog = new StringInputDialog(
                    "New Template",
                    "Please enter the name for the new template", "Name", shell);
                if (dialog.open() == Dialog.OK) {

                    String newTemplateName = dialog.getValue();

                    if (!templateStore.getTemplateNames().contains(
                        newTemplateName)) {

                        // jasper config combo selection
                        ComboInputDialog jasperComboDialog = new ComboInputDialog(
                            "Jasper Configuration Selection",
                            "Please select a Jasper Configuration that you would like to base this template on.",
                            JasperTemplateWrapper
                                .getTemplateNames(SessionManager
                                    .getAppService()), null, shell);
                        jasperComboDialog.open();

                        String selectedJasperConfig = jasperComboDialog
                            .getValue();

                        if (selectedJasperConfig == null
                            || selectedJasperConfig.length() == 0)
                            return;

                        Template ct = new Template();

                        ct.setName(newTemplateName);
                        ct.setPrinterName("default");

                        ct.setJasperTemplate(getJasperTemplateWrapper(selectedJasperConfig));

                        if (selectedJasperConfig.equals("CBSR")) {
                            ct.setConfiguration(CBSRLabelMaker
                                .getDefaultConfiguration());

                        } else {
                            ct.setConfiguration(CBSRLabelMaker
                                .getDefaultConfiguration());
                        }

                        templateStore.addTemplate(ct);
                        templateNamesList.add(ct.getName());
                        templateNamesList.redraw();
                    } else {
                        BgcPlugin.openAsyncError("Template Exists",
                            "Your new template must have a unique name.");
                        return;
                    }
                }
            } catch (Exception e1) {
                BgcPlugin.openAsyncError("Template Create Error",
                    "Could not create template", e1);
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    };

    // FIXME make cloning work.
    private SelectionListener copyListener = new SelectionListener() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            try {
                if (prevTemplateName == null)
                    return;

                StringInputDialog dialog = new StringInputDialog(
                    "Cloned Template Name",
                    "What is the name of the cloned template?", "Name", shell);
                dialog.setValue(prevTemplateName + " copy");

                if (dialog.open() == Dialog.OK) {

                    Template selectedTemplate = templateStore
                        .getTemplate(prevTemplateName);

                    String newTemplateName = dialog.getValue();

                    if (!templateStore.getTemplateNames().contains(
                        newTemplateName)) {

                        Template cloned = selectedTemplate.clone();

                        if (cloned != null) {

                            cloned.setName(newTemplateName);
                            cloned.persist();

                            templateStore.addTemplate(cloned);
                            templateNamesList.add(newTemplateName);
                            templateNamesList.redraw();
                        } else {
                            BgcPlugin.openAsyncError("Copy Template Error",
                                "Could not copy template. An error occured.");
                            return;
                        }
                    } else {
                        BgcPlugin.openAsyncError("Template Exists",
                            "Your new template must have a unique name.");
                        return;
                    }
                }
            } catch (Exception e1) {
                BgcPlugin.openAsyncError("Template Create Error",
                    "Could not create template", e1);
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    };

    private SelectionListener deleteListener = new SelectionListener() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            try {

                if (prevTemplateName != null) {
                    MessageBox messageBox = new MessageBox(shell,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    messageBox.setMessage("Are you sure you want to delete "
                        + prevTemplateName + "?");
                    messageBox.setText("Deleting Template");

                    int response = messageBox.open();
                    if (response == SWT.YES) {

                        Template selectedTemplate = templateStore
                            .getTemplate(prevTemplateName);

                        templateStore.deleteTemplate(prevTemplateName);
                        templateNamesList.remove(selectedTemplate.getName());

                        if (!selectedTemplate.isNew()) {
                            selectedTemplate.delete();
                        }

                        templateNameText.setText("Please select a template");

                        templateNamesList.deselectAll();
                        templateNamesList.redraw();

                        prevTemplateName = null;
                        printerNameText.setText("");
                        printerNameText.setEnabled(false);

                        jasperConfigText.setText("");

                        configTree.populateTree(null);

                    }
                }
            } catch (Exception e1) {
                BgcPlugin.openAsyncError("Template Delete Error",
                    "Could not delete template", e1);
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    };

    private ModifyListener printerNameModifyListener = new ModifyListener() {

        @Override
        public void modifyText(ModifyEvent e) {
            templateDirty = true;
        }
    };

    private SelectionListener saveAllListener = new SelectionListener() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            try {
                saveCurrentTemplate();
            } catch (Exception e1) {
                BgcPlugin.openAsyncError("Template Save Error",
                    "Could not save the template to the database", e1);
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);

        }
    };

    private SelectionListener helpListener = new SelectionListener() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            try {
                // TODO make a valid documentation page for help.
                PlatformUI.getWorkbench().getBrowserSupport()
                    .getExternalBrowser().openURL(new URL(HELP_URL));
            } catch (Exception e1) {
                BgcPlugin.openAsyncError("Open URL Problem",
                    "Could not open help url.\n\n" + e1.getMessage());
                return;
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    };

}