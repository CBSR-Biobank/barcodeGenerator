package edu.ualberta.med.biobank.barcodegenerator.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import javax.swing.JTable;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.TableEditor;

import edu.ualberta.med.biobank.barcodegenerator.Activator;
import edu.ualberta.med.biobank.barcodegenerator.preferences.PreferenceConstants;
import edu.ualberta.med.biobank.barcodegenerator.preferences.PreferenceInitializer;
import edu.ualberta.med.biobank.barcodegenerator.template.configuration.Configuration;
import edu.ualberta.med.biobank.barcodegenerator.template.jasper.JasperFiller;
import edu.ualberta.med.biobank.barcodegenerator.template.jasper.JasperOutline;
import edu.ualberta.med.biobank.barcodegenerator.template.jasper.containers.BarcodeImage;
import edu.ualberta.med.biobank.barcodegenerator.template.jasper.containers.PatientInfo;
import edu.ualberta.med.biobank.barcodegenerator.template.jasper.element.FieldGenerator;
import edu.ualberta.med.biobank.barcodegenerator.template.jasper.element.barcodes.Barcode1D;
import edu.ualberta.med.biobank.barcodegenerator.template.jasper.element.barcodes.Barcode2D;
import edu.ualberta.med.biobank.barcodegenerator.template.presets.CBSRData;
import edu.ualberta.med.biobank.barcodegenerator.template.presets.CBSRTemplate;

public class BarcodeView extends ViewPart {

	public static final String ID = "edu.ualberta.med.biobank.barcodegenerator.views.BarcodeView";
	private Composite top = null;
	private Composite composite = null;
	private Composite composite1 = null;
	private Composite composite2 = null;
	private Composite composite3 = null;
	private Composite composite4 = null;
	private Label label = null;
	private Text projectTitleText = null;
	private Label label1 = null;
	private Text logoText = null;
	private Button logoButton = null;
	private Group group = null;
	private Canvas logoCanvas = null;
	private Group group1 = null;
	private Composite composite5 = null;
	private Label label2 = null;
	private Label label3 = null;
	private Label label4 = null;
	private Label label5 = null;
	private Label label6 = null;
	private Text label1Text = null;
	private Button value1Checkbox = null;
	private Button label1Checkbox = null;
	private Button printBarcode1Checkbox = null;
	private Text value1Text = null;
	private Composite composite6 = null;
	private Label label7 = null;
	private Text patientIDText = null;
	private Button label2Checkbox = null;
	private Text label2Text = null;
	private Button value2Checkbox = null;
	private Text value2Text = null;
	private Button printBarcode2Checkbox = null;
	private Button label3Checkbox = null;
	private Text label3Text = null;
	private Button value3Checkbox = null;
	private Text value3Text = null;
	private Button printBarcode3Checkbox = null;
	private Group group2 = null;
	private Composite composite7 = null;
	private Label label8 = null;
	private Button sampleTypeCheckbox = null;
	private Text sampleTypeText = null;
	private Label label9 = null;
	private Text templateText = null;
	private Button templateButton = null;
	private Composite composite8 = null;
	private Group group3 = null;
	private Group group4 = null;
	private Button exitButton = null;
	private Button printButton = null;
	private Composite composite9 = null;
	private CLabel cLabel = null;
	private TableViewer tableViewer = null;
	private Table configTable = null;
	private TableViewer configTableViewer = null;
	private Shell shell;
	private IPreferenceStore store;

	@Override
	public void createPartControl(Composite parent) {

		store = null;

		if (Activator.getDefault() != null)
			store = Activator.getDefault().getPreferenceStore();

		// TODO remove store hack
		if (store == null) {
			System.err.println("WARNING: preference store was NULL!");
			store = new PreferenceStore("barcodegen.properties");
			PreferenceInitializer.setDefaults(store);
		}

		shell = parent.getShell();
		// TODO Auto-generated method stub
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = org.eclipse.swt.SWT.VERTICAL;
		rowLayout.fill = true;
		top = new Composite(parent, SWT.NONE);
		top.setBackground(new Color(Display.getCurrent(), 237, 236, 235));
		createGroup3();
		createComposite1();
		top.setLayout(rowLayout);
		createComposite2();
		createComposite8();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		composite = new Composite(group3, SWT.NONE);
		composite.setBackground(new Color(Display.getCurrent(), 237, 56, 235));
		composite.setLayout(new FillLayout());
		composite.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
		createComposite3();
		createComposite4();
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {
		composite1 = new Composite(top, SWT.NONE);
		composite1.setLayout(new FillLayout());
		createGroup1();
	}

	/**
	 * This method initializes composite2
	 * 
	 */
	private void createComposite2() {
		composite2 = new Composite(top, SWT.NONE);
		createGroup2();
		composite2.setLayout(new FillLayout());
	}

	/**
	 * This method initializes composite3
	 * 
	 */
	private void createComposite3() {
		GridData gridData21 = new GridData();
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.verticalAlignment = GridData.CENTER;
		gridData21.horizontalAlignment = GridData.FILL;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.grabExcessHorizontalSpace = true;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = false;
		composite3 = new Composite(composite, SWT.NONE);
		composite3.setLayout(gridLayout);
		label = new Label(composite3, SWT.NONE);
		label.setText("Project Title:");
		projectTitleText = new Text(composite3, SWT.BORDER);
		projectTitleText.setLayoutData(gridData);
		projectTitleText.setTextLimit(12);
		projectTitleText.setText(store
				.getString(PreferenceConstants.PROJECT_TITLE));

		Label filler = new Label(composite3, SWT.NONE);
		label1 = new Label(composite3, SWT.NONE);
		label1.setText("Logo:");
		logoText = new Text(composite3, SWT.BORDER);
		logoText.setEditable(false);
		logoText.setLayoutData(gridData1);
		logoButton = new Button(composite3, SWT.NONE);
		logoButton.setText("Browse...");
		logoButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				fd.setText("Select Logo");
				String[] filterExt = { "*.png" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				if (selected != null) {
					logoText.setText(selected);
					logoCanvas.redraw();
				}

			}

			public void widgetDefaultSelected(SelectionEvent event) {
				widgetSelected(event);
			}
		});
		label9 = new Label(composite3, SWT.NONE);
		label9.setText("Template:");
		templateText = new Text(composite3, SWT.BORDER);
		templateText.setEditable(false);
		templateText.setLayoutData(gridData21);
		templateButton = new Button(composite3, SWT.NONE);
		templateButton.setText("Browse...");
		templateButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				fd.setText("Select Template");
				String[] filterExt = { "*.jrxml", "*.*" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				if (selected != null)
					templateText.setText(selected);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				widgetSelected(event);
			}
		});
	}

	/**
	 * This method initializes composite4
	 * 
	 */
	private void createComposite4() {
		GridLayout gridLayout1 = new GridLayout();
		composite4 = new Composite(composite, SWT.NONE);
		composite4.setLayout(gridLayout1);
		createGroup();
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = GridData.FILL;
		group = new Group(composite4, SWT.NONE);
		group.setLayout(new GridLayout());
		group.setText("Logo");
		createLogoCanvas();
		group.setLayoutData(gridData2);
	}

	/**
	 * This method initializes logoCanvas
	 * 
	 */
	private void createLogoCanvas() {
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.verticalAlignment = GridData.FILL;
		gridData3.grabExcessVerticalSpace = true;
		logoCanvas = new Canvas(group, SWT.NONE);
		logoCanvas
				.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		logoCanvas.setLayoutData(gridData3);
		logoCanvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				if (new File(logoText.getText()).exists()) {
					Image image = new Image(shell.getDisplay(), logoText
							.getText());
					if (image != null) {
						e.gc.drawImage(image, 0, 0, image.getBounds().width,
								image.getBounds().height, 0, 0,
								logoCanvas.getBounds().width,
								logoCanvas.getBounds().height);
						return;
					}

				}
				e.gc.drawString("No logo", 0, 0);
			}

		});
	}

	/**
	 * This method initializes group1
	 * 
	 */
	private void createGroup1() {
		group1 = new Group(composite1, SWT.NONE);
		group1.setText(" Info Fields");
		group1.setLayout(new GridLayout());
		createComposite6();
		createComposite5();
	}

	/**
	 * This method initializes composite5
	 * 
	 */
	private void createComposite5() {
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.BEGINNING;
		gridData11.verticalAlignment = GridData.CENTER;
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = GridData.FILL;
		gridData10.verticalAlignment = GridData.CENTER;
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = GridData.FILL;
		gridData9.verticalAlignment = GridData.CENTER;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.FILL;
		gridData8.verticalAlignment = GridData.CENTER;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.verticalAlignment = GridData.CENTER;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.verticalAlignment = GridData.FILL;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.grabExcessVerticalSpace = false;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.verticalAlignment = GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 5;
		gridLayout2.makeColumnsEqualWidth = false;
		composite5 = new Composite(group1, SWT.NONE);
		composite5.setLayout(gridLayout2);
		label2 = new Label(composite5, SWT.NONE);
		label2.setText("Enable:");
		label3 = new Label(composite5, SWT.NONE);
		label3.setText("Label (Patient Name/PHN/etc):");
		label4 = new Label(composite5, SWT.NONE);
		label4.setText("Enable:");
		label5 = new Label(composite5, SWT.NONE);
		label5.setText("Value (eg BOB MARLEY):");
		label6 = new Label(composite5, SWT.NONE);
		label6.setText("Print Barcode:");

		label1Checkbox = new Button(composite5, SWT.CHECK);
		label1Checkbox.setSelection(store
				.getBoolean(PreferenceConstants.LABEL_CHECKBOX_1));
		label1Text = new Text(composite5, SWT.BORDER);
		label1Text.setLayoutData(gridData6);
		label1Text.setTextLimit(12);
		label1Text.setText(store.getString(PreferenceConstants.LABEL_TEXT_1));

		value1Checkbox = new Button(composite5, SWT.CHECK);
		value1Checkbox.setSelection(store
				.getBoolean(PreferenceConstants.VALUE_CHECKBOX_1));
		value1Text = new Text(composite5, SWT.BORDER);
		value1Text.setLayoutData(gridData7);
		value1Text.setTextLimit(24);

		printBarcode1Checkbox = new Button(composite5, SWT.CHECK);
		printBarcode1Checkbox.setLayoutData(gridData11);
		printBarcode1Checkbox.setSelection(store
				.getBoolean(PreferenceConstants.BARCODE_CHECKBOX_1));

		label2Checkbox = new Button(composite5, SWT.CHECK);
		label2Checkbox.setSelection(store
				.getBoolean(PreferenceConstants.LABEL_CHECKBOX_2));
		label2Text = new Text(composite5, SWT.BORDER);
		label2Text.setLayoutData(gridData8);
		label2Text.setTextLimit(12);
		label2Text.setText(store.getString(PreferenceConstants.LABEL_TEXT_2));

		value2Checkbox = new Button(composite5, SWT.CHECK);
		value2Checkbox.setSelection(store
				.getBoolean(PreferenceConstants.VALUE_CHECKBOX_2));
		value2Text = new Text(composite5, SWT.BORDER);
		value2Text.setLayoutData(gridData5);
		value2Text.setTextLimit(24);
		printBarcode2Checkbox = new Button(composite5, SWT.CHECK);
		printBarcode2Checkbox.setSelection(store
				.getBoolean(PreferenceConstants.BARCODE_CHECKBOX_2));

		label3Checkbox = new Button(composite5, SWT.CHECK);
		label3Checkbox.setSelection(store
				.getBoolean(PreferenceConstants.LABEL_CHECKBOX_3));
		label3Text = new Text(composite5, SWT.BORDER);
		label3Text.setLayoutData(gridData10);
		label3Text.setTextLimit(12);
		label3Text.setText(store.getString(PreferenceConstants.LABEL_TEXT_3));
		value3Checkbox = new Button(composite5, SWT.CHECK);
		value3Checkbox.setSelection(store
				.getBoolean(PreferenceConstants.VALUE_CHECKBOX_3));
		value3Text = new Text(composite5, SWT.BORDER);
		value3Text.setLayoutData(gridData9);
		value3Text.setTextLimit(24);
		printBarcode3Checkbox = new Button(composite5, SWT.CHECK);
		printBarcode3Checkbox.setSelection(store
				.getBoolean(PreferenceConstants.BARCODE_CHECKBOX_3));
	}

	/**
	 * This method initializes composite6
	 * 
	 */
	private void createComposite6() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.grabExcessHorizontalSpace = false;
		gridData4.horizontalSpan = 4;
		gridData4.horizontalIndent = 9;
		gridData4.widthHint = 150;
		gridData4.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.verticalSpacing = 2;
		gridLayout3.numColumns = 5;
		composite6 = new Composite(group1, SWT.NONE);
		composite6.setLayout(gridLayout3);
		label7 = new Label(composite6, SWT.NONE);
		label7.setText("Patient ID:");
		patientIDText = new Text(composite6, SWT.BORDER);
		patientIDText.setLayoutData(gridData4);
		patientIDText.setTextLimit(12);
		patientIDText.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				if (!e.text.matches("[{a-zA-Z0-9}]*")) {
					e.doit = false;
					return;
				}
			}
		});
	}

	/**
	 * This method initializes group2
	 * 
	 */
	private void createGroup2() {
		RowLayout rowLayout2 = new RowLayout();
		rowLayout2.type = org.eclipse.swt.SWT.VERTICAL;
		rowLayout2.fill = true;
		group2 = new Group(composite2, SWT.NONE);
		createComposite7();
		createComposite9();
		group2.setLayout(rowLayout2);
		group2.setText("Additonal Configuration");

	}

	/**
	 * This method initializes composite7
	 * 
	 */
	private void createComposite7() {
		composite7 = new Composite(group2, SWT.NONE);
		composite7.setLayout(new FillLayout());
		sampleTypeCheckbox = new Button(composite7, SWT.CHECK | SWT.LEFT);
		sampleTypeCheckbox.setText("Enable");
		sampleTypeCheckbox.setSelection(store
				.getBoolean(PreferenceConstants.SAMPLETYPE_CHECKBOX));
		cLabel = new CLabel(composite7, SWT.NONE);
		cLabel.setText("Sample Type (on labels):");
		sampleTypeText = new Text(composite7, SWT.BORDER | SWT.V_SCROLL
				| SWT.SINGLE);
		sampleTypeText.setText(store
				.getString(PreferenceConstants.SAMPLETYPE_TEXT));
		sampleTypeText.setTextLimit(15);
		label8 = new Label(composite7, SWT.LEFT | SWT.HORIZONTAL);
		label8.setText("");
		Label filler61 = new Label(composite7, SWT.NONE);
	}

	/**
	 * This method initializes composite8
	 * 
	 */
	private void createComposite8() {
		composite8 = new Composite(top, SWT.NONE);
		composite8.setLayout(new FillLayout());
		createGroup4();
	}

	/**
	 * This method initializes group3
	 * 
	 */
	private void createGroup3() {
		group3 = new Group(top, SWT.NONE);
		group3.setLayout(new FillLayout());
		group3.setText("Branding");
		createComposite();
	}

	/**
	 * This method initializes group4
	 * 
	 */
	private void createGroup4() {
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 3;
		group4 = new Group(composite8, SWT.NONE);
		group4.setText("Actions");
		group4.setLayout(gridLayout5);
		exitButton = new Button(group4, SWT.NONE);
		exitButton.setText("Exit Label Maker");
		exitButton.addSelectionListener(exitButtonListener);
		printButton = new Button(group4, SWT.NONE);
		printButton.setText("Print Label Sheet");
		printButton.addSelectionListener(printButtonListener);
	}

	/**
	 * This method initializes composite9
	 * 
	 */
	private void createComposite9() {

		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = GridData.FILL;
		gridData12.grabExcessHorizontalSpace = true;
		gridData12.grabExcessVerticalSpace = true;
		gridData12.heightHint = 300;
		gridData12.verticalAlignment = GridData.FILL;
		composite9 = new Composite(group2, SWT.NONE);
		composite9.setLayout(new GridLayout());
		createTable(composite9);
	}

	//TODO sort first column when pressed.
	private void createTable(final Composite c) {
		GridData gridData13 = new GridData();
		gridData13.grabExcessHorizontalSpace = true;
		gridData13.horizontalAlignment = GridData.FILL;
		gridData13.verticalAlignment = GridData.CENTER;
		gridData13.heightHint = 150;
		gridData13.grabExcessVerticalSpace = true;

		configTable = new Table(c, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		configTable.setHeaderVisible(true);
		configTable.setLayoutData(gridData13);
		configTable.setLinesVisible(true);

		final TableEditor editor = new TableEditor(configTable);
		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;
		// editing the second column
		final int EDITABLECOLUMN = 1;

		configTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Clean up any previous editor control
				Control oldEditor = editor.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();

				// Identify the selected row
				final TableItem item = (TableItem) e.item;
				
				if (item == null)
					return;
				// The control that will be the editor must be a child of the
				// Table
				Text newEditor = new Text(configTable, SWT.NONE);
				newEditor.setText(item.getText(EDITABLECOLUMN));

				newEditor.addListener(SWT.Verify, new Listener() {
					public void handleEvent(Event e) {
						
						if (!e.text.matches("[{0-9,}]*")) {
							e.doit = false;
							return;
						}
					}
				});
				newEditor.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						Text text = (Text) editor.getEditor();

						editor.getItem()
								.setText(EDITABLECOLUMN, text.getText());

						boolean valid = true;
						if (item != null && item.getText(1) != null
								&& item.getText(1).split(",").length == 4)
							for (String s : item.getText(1).split(",")) {
								if (s.length() < 1 || s.length() > 3)
									valid = false;
							}
						else
							valid = false;

						item.setForeground(valid ? new Color(
								shell.getDisplay(), 0, 0, 0) : new Color(shell
								.getDisplay(), 255, 0, 0));

					}
				});

				newEditor.selectAll();
				newEditor.setFocus();
				editor.setEditor(newEditor, item, EDITABLECOLUMN);
			}
		});

		// create columns

		String[] columnNames = { "Variable", "Value" };

		TableColumn[] column = new TableColumn[2];
		column[0] = new TableColumn(configTable, SWT.LEFT);
		column[0].setText(columnNames[0]);

		column[1] = new TableColumn(configTable, SWT.LEFT);
		column[1].setText(columnNames[1]);
		column[1].setWidth(100);

		for (int i = 0, n = column.length; i < n; i++) {
			column[i].pack();
		}

		// TODO move and improve this. populate table
		//populateTable(configTable, new CBSRTemplate.Settings().getData());

	}

	private HashMap<String, Rectangle> getTableData(Table t)  {
		HashMap<String, Rectangle> tableData = new HashMap<String, Rectangle>();
		for (TableItem ti : t.getItems())
			tableData.put(ti.getText(0), String2Rect(ti.getText(1)));

		return tableData;
	}

	private void populateTable(Table t, Map<String, Rectangle> data) {

		for (Entry<String, Rectangle> e : data.entrySet()) {

			TableItem item = new TableItem(t, SWT.NONE);
			item.setText(new String[] { e.getKey(),rect2String(e.getValue()) });
		}
	}
	
	private static String rect2String(Rectangle r) {
		return r.x + "," + r.y + "," + r.width + "," + r.height;
	}
	
	private static Rectangle String2Rect(String s) {

		String[] parts = s.split(",");

		Rectangle r = null;
		
		if(parts.length == 4){
			try{
			r = new Rectangle(Integer.parseInt(parts[0]),
					Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
					Integer.parseInt(parts[3]));
			}
			catch(NumberFormatException nfe){
				throw new RuntimeException("Failed to parse integers in string to rect converstion.");
			}
		}
		else{
			throw new RuntimeException("Invalid number of items  in string to rect converstion. ");
		}
		return r;

	}
	

	private void updateSavePreferences() {

		store.setValue(PreferenceConstants.LOGO_FILE_LOCATION,
				logoText.getText());
		store.setValue(PreferenceConstants.PROJECT_TITLE,
				projectTitleText.getText());

		store.setValue(PreferenceConstants.LABEL_CHECKBOX_1,
				label1Checkbox.getSelection());
		store.setValue(PreferenceConstants.LABEL_CHECKBOX_2,
				label2Checkbox.getSelection());
		store.setValue(PreferenceConstants.LABEL_CHECKBOX_3,
				label3Checkbox.getSelection());

		store.setValue(PreferenceConstants.LABEL_TEXT_1, label1Text.getText());
		store.setValue(PreferenceConstants.LABEL_TEXT_2, label2Text.getText());
		store.setValue(PreferenceConstants.LABEL_TEXT_3, label3Text.getText());

		store.setValue(PreferenceConstants.VALUE_CHECKBOX_1,
				value1Checkbox.getSelection());
		store.setValue(PreferenceConstants.VALUE_CHECKBOX_2,
				value2Checkbox.getSelection());
		store.setValue(PreferenceConstants.VALUE_CHECKBOX_3,
				value3Checkbox.getSelection());

		store.setValue(PreferenceConstants.BARCODE_CHECKBOX_1,
				printBarcode1Checkbox.getSelection());
		store.setValue(PreferenceConstants.BARCODE_CHECKBOX_2,
				printBarcode2Checkbox.getSelection());
		store.setValue(PreferenceConstants.BARCODE_CHECKBOX_3,
				printBarcode3Checkbox.getSelection());

		store.setValue(PreferenceConstants.SAMPLETYPE_CHECKBOX,
				sampleTypeCheckbox.getSelection());
		store.setValue(PreferenceConstants.SAMPLETYPE_TEXT,
				sampleTypeText.getText());
	}

	public class BarcodeViewGuiData extends CBSRData{

		
		// TODO test exceptions
		public class GuiInputException extends Exception {

			private static final long serialVersionUID = 4349271754734681511L;

			public GuiInputException(String title, String message) {
				this(title + " : " + message);
			}

			public GuiInputException(String message) {
				super(message);
			}
		};
		
		public BarcodeViewGuiData() throws GuiInputException {

			projectTileStr = projectTitleText.getText();

			if (projectTileStr == null || projectTileStr.length() == 0) {
				throw new GuiInputException("Incorrect Title",
						"A valid title is required.");
			}

			ByteArrayInputStream bis = null;
			try {
				BufferedImage logoImage;

				logoImage = ImageIO.read(new File(logoText.getText()));
				ByteArrayOutputStream binaryOutputStream = new ByteArrayOutputStream();
				if (logoImage != null) {
					ImageIO.write(logoImage, "PNG", binaryOutputStream);
					bis = new ByteArrayInputStream(
							binaryOutputStream.toByteArray());
				} else {
					bis = null;
				}

			} catch (IOException e) {
				bis = null;
			}
			logoStream = bis;

			/*
			 * templateFile = new File(templateText.getText()); if
			 * (!templateFile.exists()) {
			 * Activator.openAsyncError("Error: Could Not Find Template",
			 * "A valid template file location is required."); return; }
			 */

			patientIdStr = patientIDText.getText();
			if (patientIdStr == null || patientIdStr.length() == 0) {
				throw new GuiInputException("Incorrect PatientID",
						"A valid patient Id is required.");

			}
			// ------------ patient info start-----------------
			label1Str = null;
			if (label1Checkbox.getSelection()) {
				label1Str = label1Text.getText();
			}
			value1Str = null;
			barcode1Print = false;
			if (value1Checkbox.getSelection()) {
				value1Str = value1Text.getText();
				barcode1Print = printBarcode1Checkbox.getSelection();
			}

			label2Str = null;
			if (label2Checkbox.getSelection()) {
				label2Str = label2Text.getText();
			}
			value2Str = null;
			barcode2Print = false;
			if (value2Checkbox.getSelection()) {
				value2Str = value2Text.getText();
				barcode2Print = printBarcode2Checkbox.getSelection();
			}

			label3Str = null;
			if (label3Checkbox.getSelection()) {
				label3Str = label3Text.getText();
			}
			value3Str = null;
			barcode3Print = false;
			if (value3Checkbox.getSelection()) {
				value3Str = value3Text.getText();
				barcode3Print = printBarcode3Checkbox.getSelection();
			}
			// ------------ patient info end-----------------

			sampleTypeStr = null;
			if (sampleTypeCheckbox.getSelection()) {
				sampleTypeStr = sampleTypeText.getText();
			}
		}
	};


	private static String randString() {
		return UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "")
				.substring(0, 6)
				+ UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "")
						.toUpperCase().substring(0, 6);
	}

	// TODO remove randStringArray
	private static ArrayList<String> randStringArray() {

		ArrayList<String> l = new ArrayList<String>();

		for (int i = 0; i < 32; i++) {

			l.add(randString());
		}
		return l;
	}

	private SelectionListener printButtonListener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			System.out.println("Print pressed!");

			BarcodeViewGuiData guiData = null;
			byte[] pdfdata = null;
			try {
				guiData = new BarcodeViewGuiData();
			} catch (BarcodeViewGuiData.GuiInputException e1) {
				System.err.println(e1.getMessage());//TODO dialog error
			}

			if (guiData != null) {
				try {
					//TODO load CBSRTemplate from the GUI combox.
					CBSRTemplate ct = new CBSRTemplate();
					ct.setJasperStream(null);
					ct.setDefaultConfiguration();
					
					pdfdata = ct.generatePdfCBSR(guiData,randStringArray());
				} catch (Exception e1) {
					System.err.println(e1.getMessage());//TODO dialog error
				}
				if (pdfdata != null) {
					FileOutputStream fos;
					try {
						fos = new FileOutputStream("simple_report.pdf");
						fos.write(pdfdata);
						fos.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();//TODO dialog error
					} catch (IOException ee) {
						ee.printStackTrace();//TODO dialog error
					}
				}

			}
			updateSavePreferences();
			System.out.println("Print done.");

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);

		}
	};

	private SelectionListener exitButtonListener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// shell.getDisplay().getActiveShell().close(); TODO close

			try {
				for (Entry<String, Rectangle> entry : getTableData(configTable)
						.entrySet()) {
					System.out.println(entry.getKey() + ":" + entry.getValue());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);

		}
	};
}
