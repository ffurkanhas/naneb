package Gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI {
	public HashMap<String, Integer> staffMap;
	public JComboBox<String> studentClubCombo;
	public ArrayList<String> studentClubSelected = new ArrayList<String>();

	public JComboBox<String> courseCombo;
	public ArrayList<String> courseSelected = new ArrayList<String>();
	
	public JComboBox<String> staffCombo;
	public ArrayList<Integer> staffSelected = new ArrayList<Integer>();
	
	public JComboBox<String> deptCombo;
	public ArrayList<String> deptsSelected = new ArrayList<String>();
	JTextArea result ;
	private JFrame frame;
	private JTextField fname;
	private JTextField minit;
	private JTextField lname;
	private JTextArea Selected;
	private JTextField txtYear;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 576, 475);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String[] clubs_str = OtherFunctions.getStudentClubs();
		String[] course_str = OtherFunctions.getCourses();
		String[] dept_str = OtherFunctions.getDept();
		staffMap = OtherFunctions.getAcademic();
		String[] aStaff_str = new String[staffMap.size()];
		Iterator it = staffMap.entrySet().iterator();
		int i=0;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        aStaff_str[i]=(String)pair.getKey();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        i++;
	    }
		Arrays.sort(aStaff_str);
		ComboBoxModel<String> clubs = new DefaultComboBoxModel<String>(clubs_str);
		ComboBoxModel<String> courses = new DefaultComboBoxModel<String>(course_str);
		ComboBoxModel<String> staff = new DefaultComboBoxModel<String>(aStaff_str);
		ComboBoxModel<String> depts = new DefaultComboBoxModel<String>(dept_str);
		frame.getContentPane().setLayout(new MigLayout("", "[26px][31px,grow][26px][26px][27px][26px][27px][248px,grow][51px][26px]", "[18px][3px][15px][6px][14px][3px][20px][17px][20px][6px][20px][23px][][][50px][38px][][36px][grow][3px][23px][59px]"));
		
		JLabel label = new JLabel("");
		frame.getContentPane().add(label, "cell 0 0 1 7,grow");
		
		JLabel label_1 = new JLabel("");
		frame.getContentPane().add(label_1, "cell 1 0 1 3,grow");
		
		JLabel label_2 = new JLabel("");
		frame.getContentPane().add(label_2, "cell 2 0 1 7,grow");
		
		JLabel label_3 = new JLabel("");
		frame.getContentPane().add(label_3, "cell 3 0 1 7,grow");
		
		JLabel label_4 = new JLabel("");
		frame.getContentPane().add(label_4, "cell 4 0 1 7,grow");
		
		JLabel label_5 = new JLabel("");
		frame.getContentPane().add(label_5, "cell 5 0 1 7,grow");
		
		JLabel label_6 = new JLabel("");
		frame.getContentPane().add(label_6, "cell 6 0 1 7,grow");
		
		JLabel label_7 = new JLabel("");
		frame.getContentPane().add(label_7, "cell 7 0,grow");
		
		JLabel label_8 = new JLabel("");
		frame.getContentPane().add(label_8, "cell 8 0,grow");
		
		JLabel label_9 = new JLabel("");
		frame.getContentPane().add(label_9, "cell 9 0 1 7,grow");
		
		JLabel lblIlkAd = new JLabel("\u0130lk Ad");
		frame.getContentPane().add(lblIlkAd, "cell 1 4,growx,aligny top");
		
		JLabel lblIkinciAd = new JLabel("\u0130kinci Ad");
		frame.getContentPane().add(lblIkinciAd, "cell 1 7,growx,aligny top");
		
		JButton btnEkle = new JButton("Ekle");
		btnEkle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String club_name = studentClubCombo.getSelectedItem().toString();
				System.out.println(club_name);
				if(!studentClubSelected.contains(club_name) && club_name.length()>1)
					studentClubSelected.add(club_name);
				updateSelected();
			}
		});
		frame.getContentPane().add(btnEkle, "cell 8 7,grow");
		
		JLabel label_10 = new JLabel("");
		frame.getContentPane().add(label_10, "cell 0 8 1 8,grow");
		
		minit = new JTextField();
		frame.getContentPane().add(minit, "cell 1 8,growx,aligny top");
		minit.setColumns(10);
		
		JLabel lblSoyad = new JLabel("Soyad");
		frame.getContentPane().add(lblSoyad, "cell 1 10,alignx left,aligny top");
		
		lname = new JTextField();
		frame.getContentPane().add(lname, "cell 1 11,growx,aligny top");
		lname.setColumns(10);
		
		staffCombo = new JComboBox<String>();
		staffCombo.setModel(staff);
		frame.getContentPane().add(staffCombo, "cell 7 11,alignx left,aligny top");
		
		JButton addStaff = new JButton("Ekle");
		addStaff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String staff_name = staffCombo.getSelectedItem().toString();
				System.out.println(staffMap.get(staff_name));
				System.out.println(staff_name);
				if(!staffSelected.contains(staffMap.get(staff_name))) {
					staffSelected.add(staffMap.get(staff_name));
					System.out.println("lol");
				}
				updateSelected();
			}
		});
		frame.getContentPane().add(addStaff, "cell 8 11,alignx left,aligny top");
		
		JLabel label_29 = new JLabel("Giriþ Yýlý: (Ýki Haneli: 17 gibi)");
		frame.getContentPane().add(label_29, "cell 1 12");
		
		txtYear = new JTextField();
		txtYear.setText("");
		frame.getContentPane().add(txtYear, "cell 1 13,growx");
		txtYear.setColumns(10);
		
		deptCombo = new JComboBox<String>();
		deptCombo.setModel(depts);
		frame.getContentPane().add(deptCombo, "cell 7 13,alignx left,aligny bottom");
		
		JButton deptAdd = new JButton("Ekle");
		deptAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String dept_name = deptCombo.getSelectedItem().toString();
				System.out.println(dept_name);
				if(!deptsSelected.contains(dept_name))
					deptsSelected.add(dept_name);
				updateSelected();
			}
		});
		frame.getContentPane().add(deptAdd, "cell 8 13,alignx left,aligny top");
		
		Selected = new JTextArea();
		JScrollPane scroll = new JScrollPane();
		Selected.setEditable(false);
		 scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		 Selected.add(scroll);
		 Selected.setText("Öðrenci Bilgisi:\nAd:\nÝkinci Ad:" +
		"\nSoyad: "  +"\nDersler: "  + "\nKulüpler: "
		+ "\nHocalar: " + "\nBölüm: " );
		 
		
		frame.getContentPane().add(Selected, "cell 7 14,growx");
		Selected.setColumns(10);
		
		JLabel label_11 = new JLabel("");
		frame.getContentPane().add(label_11, "cell 1 15,grow");
		
		JLabel label_12 = new JLabel("");
		frame.getContentPane().add(label_12, "cell 2 8 1 8,grow");
		
		JLabel label_13 = new JLabel("");
		frame.getContentPane().add(label_13, "cell 3 8 1 8,grow");
		
		JLabel label_14 = new JLabel("");
		frame.getContentPane().add(label_14, "cell 4 8 1 8,grow");
		
		JLabel label_15 = new JLabel("");
		frame.getContentPane().add(label_15, "cell 5 8 1 8,grow");
		
		JLabel label_16 = new JLabel("");
		frame.getContentPane().add(label_16, "cell 6 8 1 8,grow");
		studentClubCombo = new JComboBox<String>();
		studentClubCombo.setModel(clubs);
		frame.getContentPane().add(studentClubCombo, "cell 7 7 1 4,grow");
		
		JLabel label_17 = new JLabel("");
		frame.getContentPane().add(label_17, "cell 8 8,grow");
		
		JButton btnSonucuGetir = new JButton("Sonucu Getir");
		btnSonucuGetir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String f_name = fname.getText();
				String m_init = minit.getText();
				String l_name = lname.getText();
				String year = txtYear.getText();
				
				String rs = OtherFunctions.getFinalResult(f_name,m_init,l_name,year,deptsSelected, courseSelected, staffSelected, studentClubSelected);
				 
				 System.out.println(rs);
				 result.setText(rs);
				studentClubSelected.clear();
				deptsSelected.clear();
				courseSelected.clear();
				staffSelected.clear();
				fname.setText("");
				minit.setText("");
				lname.setText("");
				txtYear.setText("");
				Selected.setText("Öðrenci Bilgisi:\nAd:" + "\nÝkinci Ad:"
				+"\nSoyad: " + "\nGiriþ Yýlý:" 
				+"\nDersler: " + "\nKulüpler: "
				+ "\nHocalar: " + "\nBölüm: ");
			}
		});
		frame.getContentPane().add(btnSonucuGetir, "cell 7 15,growx,aligny top");
		
		result = new JTextArea();
		JScrollPane sp = new JScrollPane(result);  
		result.setEditable(false);
		frame.getContentPane().add(sp, "cell 7 16,grow");
		//frame.getContentPane().add(result, "cell 7 16,grow");
		
		JLabel label_18 = new JLabel("");
		frame.getContentPane().add(label_18, "cell 0 17 1 4,grow");
		
		JLabel label_19 = new JLabel("");
		frame.getContentPane().add(label_19, "cell 1 17 1 4,grow");
		
		JLabel label_20 = new JLabel("");
		frame.getContentPane().add(label_20, "cell 2 17 1 4,grow");
		
		JLabel label_21 = new JLabel("");
		frame.getContentPane().add(label_21, "cell 3 17 1 4,grow");
		
		JLabel label_22 = new JLabel("");
		frame.getContentPane().add(label_22, "cell 4 17 1 4,grow");
		
		JLabel label_23 = new JLabel("");
		frame.getContentPane().add(label_23, "cell 5 17 1 4,grow");
		
		JLabel label_24 = new JLabel("");
		frame.getContentPane().add(label_24, "cell 6 17 1 4,grow");
		
		JTextArea textArea = new JTextArea();
		frame.getContentPane().add(textArea, "flowy,cell 7 17");
		
		JLabel label_25 = new JLabel("");
		frame.getContentPane().add(label_25, "cell 7 17,grow");
		
		JLabel label_26 = new JLabel("");
		frame.getContentPane().add(label_26, "cell 8 17,grow");
		
		JLabel label_28 = new JLabel("");
		frame.getContentPane().add(label_28, "cell 0 21,grow");
		
		JLabel label_27 = new JLabel("");
		frame.getContentPane().add(label_27, "cell 9 21,grow");
		courseCombo = new JComboBox<String>();
		courseCombo.setModel(courses);
		frame.getContentPane().add(courseCombo, "cell 7 2 1 5,grow");
		JButton co_add = new JButton("Ekle");
		co_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String course_code = courseCombo.getSelectedItem().toString();
				System.out.println(course_code);
				if(!courseSelected.contains(course_code))
					courseSelected.add(course_code);
				updateSelected();
			}
		});
		frame.getContentPane().add(co_add, "cell 8 2 1 5,grow");
		fname= new JTextField();
		frame.getContentPane().add(fname, "cell 1 6,growx,aligny top");
		fname.setColumns(10);
		
		
	}

private void updateSelected() {
	String staff = "[";
	Iterator<Integer> it = staffSelected.iterator();
	while(it.hasNext()) {
		int id = it.next();
		Iterator it2 = staffMap.entrySet().iterator();
		int i=0;
	    while (it2.hasNext()) {
	        Map.Entry pair = (Map.Entry)it2.next();
	        if(pair.getValue().equals(id)) 
	        	staff += pair.getKey();
	    }
	    if(it2.hasNext()) staff +=",";
	}
	 staff += "]";
	String select = "Öðrenci Bilgisi:\nAd:" + fname.getText() + "\nÝkinci Ad:" + minit.getText()
		+"\nSoyad: " + lname.getText() + "\nGiriþ Yýlý:" + txtYear.getText()
		+"\nDersler: " + courseSelected.toString() + "\nKulüpler: "
		+ studentClubSelected.toString() + "\nHocalar: " + staff + "\nBölüm: " 
		+ deptsSelected.toString();
	Selected.setText(select);
	staff="";
}
/*	private class SwingAction extends AbstractAction {
		public SwingAction() {
			//putValue(NAME, "SwingAction");
			//putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}*/
}
