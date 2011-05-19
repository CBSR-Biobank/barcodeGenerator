package edu.ualberta.med.biobank.barcodegenerator.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.layout.RowLayout;

import edu.ualberta.med.biobank.barcodegenerator.template.Template;
import edu.ualberta.med.biobank.barcodegenerator.template.TemplateStore;
import edu.ualberta.med.biobank.barcodegenerator.template.presets.CBSRTemplate;

public class BarcodeSelector extends ViewPart {

	public static final String ID = "edu.ualberta.med.biobank.barcodegenerator.views.BarcodeSelector"; // TODO Needs to be whatever is mentioned in plugin.xml
	private Composite top = null;
	private Group group = null;
	private Composite composite = null;
	private Composite composite1 = null;
	private Composite composite2 = null;
	private Composite composite3 = null;
	private Group group1 = null;
	private Composite composite4 = null;
	private Button button = null;
	private Button button1 = null;
	private Button button2 = null;
	private Button cancleButton = null;
	private Button saveAllButton = null;
	private Composite composite5 = null;
	private Label label = null;
	private Text text = null;
	private Label label1 = null;
	private Text text1 = null;
	private Button button5 = null;
	private Group group2 = null;
	private Table table = null;
	private List list = null;
	private Group composite6 = null;
	private Table table1 = null;
	private Shell shell;	
	
	
	private TemplateStore templateStore  = new TemplateStore();
	private Template templateSelected = null;
	
	@Override
	public void createPartControl(Composite parent) {
		
		shell = parent.getShell();
		
		//TODO load store from proper location
		try {
			templateStore.loadStore(new File("Store.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout());
		createGroup();
	}

	@Override
	public void setFocus() {
	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		group = new Group(top, SWT.NONE);
		group.setText("Templates Editor");
		group.setLayoutData(gridData);
		createComposite();
		group.setLayout(new GridLayout());
		createComposite1();
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = GridData.FILL;
		composite = new Composite(group, SWT.NONE);
		createComposite2();
		composite.setLayoutData(gridData1);
		composite.setLayout(gridLayout);
		Label filler = new Label(composite, SWT.NONE);
		createComposite3();
	}

	/**
	 * This method initializes composite1	
	 *
	 */
	private void createComposite1() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = false;
		gridData2.verticalAlignment = GridData.CENTER;
		composite1 = new Composite(group, SWT.NONE);
		composite1.setLayoutData(gridData2);
		composite1.setLayout(new FillLayout());
		cancleButton = new Button(composite1, SWT.NONE);
		cancleButton.setText("Cancle");
		cancleButton.addSelectionListener(cancleListener);
		Label filler21 = new Label(composite1, SWT.NONE);
		Label filler22 = new Label(composite1, SWT.NONE);
		Label filler2 = new Label(composite1, SWT.NONE);
		saveAllButton = new Button(composite1, SWT.NONE);
		saveAllButton.setText("Save All ");
		saveAllButton.addSelectionListener(saveAllListener);
	}

	/**
	 * This method initializes composite2	
	 *
	 */
	private void createComposite2() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.BEGINNING;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.grabExcessHorizontalSpace = false;
		gridData3.verticalAlignment = GridData.FILL;
		composite2 = new Composite(composite, SWT.NONE);
		createGroup1();
		composite2.setLayout(new GridLayout());
		composite2.setLayoutData(gridData3);
		createComposite4();
	}

	/**
	 * This method initializes composite3	
	 *
	 */
	private void createComposite3() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = GridData.FILL;
		composite3 = new Composite(composite, SWT.NONE);
		createComposite5();
		composite3.setLayoutData(gridData4);
		createComposite62();
		composite3.setLayout(new GridLayout());
	}

	/**
	 * This method initializes group1	
	 *
	 */
	private void createGroup1() {
		GridData gridData6 = new GridData();
		gridData6.grabExcessVerticalSpace = true;
		gridData6.verticalAlignment = GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalAlignment = GridData.FILL;
		FillLayout fillLayout1 = new FillLayout();
		fillLayout1.type = org.eclipse.swt.SWT.VERTICAL;
		group1 = new Group(composite2, SWT.NONE);
		group1.setText("Templates");
		group1.setLayoutData(gridData6);
		group1.setLayout(fillLayout1);
		list = new List(group1, SWT.BORDER | SWT.V_SCROLL);
		list.addSelectionListener(listListener);
		syncListWithStore();
	}
	
	private SelectionListener listListener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			 String[] selectedItems = list.getSelection();
			if(selectedItems.length == 1){
				Template t = templateStore.getTemplate(selectedItems[0]);

				setSelectedTemplate(t);
			}
			else{
				setSelectedTemplate(null);
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);

		}
	};
	
	private void setSelectedTemplate(Template t){
		templateSelected = t;
		if(t != null){
			text.setText(t.getName());
			populateTable(table1, ((CBSRTemplate)templateSelected).getConfiguration().getSettings());
		}
		else{
			text.setText("Select a template.");
			populateTable(table1,null);
		}
	}

	private void syncListWithStore(){

		this.list.removeAll();
		for(String s : templateStore.getTemplateNames())
			this.list.add(s);

		if (templateSelected != null) {
			setSelectedTemplate(templateStore.getTemplate(templateSelected
					.getName()));
			
			this.list.deselectAll();
		}
		
		this.list.redraw();
	}

	/**
	 * This method initializes composite4	
	 *
	 */
	private void createComposite4() {
		composite4 = new Composite(composite2, SWT.NONE);
		composite4.setLayout(new RowLayout());
		button = new Button(composite4, SWT.NONE);
		button.setText("Delete ");
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(templateSelected != null){
					if(templateStore.removeTemplate(templateSelected)){
						list.remove(templateSelected.getName());
						setSelectedTemplate(null);
					}
					else{
						//TODO warn user dialog against duplicates
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		button1 = new Button(composite4, SWT.NONE);
		button1.setText("Copy ");
		button1.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(templateSelected != null){
					
					Template clone = new CBSRTemplate();
					Template.Clone(templateSelected, clone);
					clone.setName(clone.getName() + " copy");
					
					
					if(templateStore.addTemplate(clone)){
						list.add(clone.getName());
						list.redraw();
					}
					else{
						//TODO warn user dialog against duplicates
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		button2 = new Button(composite4, SWT.NONE);
		button2.setText("New");
		button2.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO make dialog
				CBSRTemplate ct = new CBSRTemplate();
				ct.setJasperFileData(null);
				ct.setDefaultConfiguration();
				ct.setName(BarcodeView.randString());

				if(templateStore.addTemplate(ct)){
					list.add(ct.getName());
					list.redraw();
				}
				else{
					//TODO warn user dialog against duplicates
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	/**
	 * This method initializes composite5	
	 *
	 */
	private void createComposite5() {
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.verticalAlignment = GridData.CENTER;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.verticalAlignment = GridData.CENTER;
		GridData gridData7 = new GridData();
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.verticalAlignment = GridData.CENTER;
		gridData7.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 3;
		composite5 = new Composite(composite3, SWT.NONE);
		composite5.setLayout(gridLayout2);
		composite5.setLayoutData(gridData11);
		label = new Label(composite5, SWT.NONE);
		label.setText("Template Name:");
		text = new Text(composite5, SWT.BORDER);
		text.setEditable(false);
		text.setLayoutData(gridData7);
		Label filler7 = new Label(composite5, SWT.NONE);
		label1 = new Label(composite5, SWT.NONE);
		label1.setText("Jasper File:");
		text1 = new Text(composite5, SWT.BORDER);
		text1.setEditable(false);
		text1.setLayoutData(gridData8);
		button5 = new Button(composite5, SWT.NONE);
		button5.setText("Browse...");
	}

	/**
	 * This method initializes composite6	
	 *
	 */
	private void createComposite6() {
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.verticalAlignment = GridData.FILL;
	}

	/**
	 * This method initializes composite6	
	 *
	 */
	private void createComposite62() {
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
		composite6 = new Group(composite3, SWT.NONE);
		composite6.setLayout(new GridLayout());
		composite6.setText("Configuration");
		composite6.setLayoutData(gridData10);
		createTable(composite6);
	}
	
	// TODO sort first column when pressed.
	private void createTable(final Composite c) {
		GridData gridData9 = new GridData();
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.verticalAlignment = GridData.FILL;
		gridData9.grabExcessVerticalSpace = true;
		gridData9.widthHint = -1;
		gridData9.horizontalAlignment = GridData.FILL;

		table1 = new Table(c, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table1.setHeaderVisible(true);
		table1.setLayoutData(gridData9);
		table1.setLinesVisible(true);

		final TableEditor editor = new TableEditor(table1);
		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;
		// editing the second column
		final int EDITABLECOLUMN = 1;

		table1.addSelectionListener(new SelectionAdapter() {
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
				Text newEditor = new Text(table1, SWT.NONE);
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
		column[0] = new TableColumn(table1, SWT.LEFT);
		column[0].setText(columnNames[0]);

		column[1] = new TableColumn(table1, SWT.LEFT);
		column[1].setText(columnNames[1]);
		column[1].setWidth(100);

		for (int i = 0, n = column.length; i < n; i++) {
			column[i].pack();
		}
	}

	private HashMap<String, Rectangle> getTableData(Table t) {
		HashMap<String, Rectangle> tableData = new HashMap<String, Rectangle>();
		for (TableItem ti : t.getItems())
			tableData.put(ti.getText(0), String2Rect(ti.getText(1)));

		return tableData;
	}

	private void populateTable(Table t, Map<String, Rectangle> data) {

		if(data == null){
			t.removeAll();
			return;
		}
		
		for (Entry<String, Rectangle> e : data.entrySet()) {

			TableItem item = new TableItem(t, SWT.NONE);
			item.setText(new String[] { e.getKey(), rect2String(e.getValue()) });
		}
	}

	private static String rect2String(Rectangle r) {
		return r.x + "," + r.y + "," + r.width + "," + r.height;
	}

	private static Rectangle String2Rect(String s) {

		String[] parts = s.split(",");

		Rectangle r = null;

		if (parts.length == 4) {
			try {
				r = new Rectangle(Integer.parseInt(parts[0]),
						Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3]));
			} catch (NumberFormatException nfe) {
				throw new RuntimeException(
						"Failed to parse integers in string to rect converstion.");
			}
		} else {
			throw new RuntimeException(
					"Invalid number of items  in string to rect converstion. ");
		}
		return r;

	}
	
	private SelectionListener cancleListener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {

			//TODO exit dialog
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);

		}
	};

	private SelectionListener saveAllListener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// shell.getDisplay().getActiveShell().close(); TODO close
			try {
				templateStore.saveStore(new File("Store.dat"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//TODO exit dialog
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);

		}
	};


}  //  @jve:decl-index=0:visual-constraint="10,10,559,504"