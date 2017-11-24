package Gui;

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

	ComboBoxModel<String> preqNumber;
	ComboBoxModel<String> coursespreqd;
	HashMap<String,String[]> preqdCourses= new HashMap<String,String[]>();
	JComboBox<String> comboBox_1 = new JComboBox<String>();

	JComboBox<String> comboBox_2;
	JTextArea result ;
	public JFrame frame;
	private JTextField fname;
	private JTextField minit;
	private JTextField lname;
	private JTextArea Selected;
	private JTextField txtYear;
	private JTextField textField;
	private JTextField bolumKatsayisi;
	private JTextField dersKatsayisi;
	private JTextField toplulukKatsayisi;
	private JTextField subeKatsayisi;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SettingsGui sg = new SettingsGui();
		sg.setVisible(true);
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
		frame.setBounds(100, 100, 679, 577);
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
		preqdCourses.put("BİL113", new String[] {"BİL211"});
		preqdCourses.put("BİL361", new String[] {"BİL461"});
		preqdCourses.put("BİL214", new String[] {"BİL461"});
		//preqdCourses.put("B?L214", "B?L395");
		preqdCourses.put("BİL264", new String[] {"BİL461","BİL361"});
		preqdCourses.put("BİL481", new String[] {"BİL495"});
		preqdCourses.put("BİL212", new String[] {"BİL372","BİL331"});
		preqdCourses.put("BİL133", new String[] {"BİL331"});
		frame.getContentPane().setLayout(new MigLayout("", "[26px][31px,grow][26px][][][26px][27px][][26px][27px][248px,grow][51px][26px]", "[18px][3px][15px][6px][14px][3px][20px][17px][20px][6px][20px][23px][][][50px][][38px][][][][][][][][][][][][][][36px][][grow][3px][grow][23px][59px]"));
		frame.pack();
		JLabel label = new JLabel("");
		frame.getContentPane().add(label, "cell 0 0 1 7,grow");

		JLabel label_1 = new JLabel("");
		frame.getContentPane().add(label_1, "cell 1 0 1 3,grow");

		JLabel label_2 = new JLabel("");
		frame.getContentPane().add(label_2, "cell 2 0 1 7,grow");

		JLabel label_3 = new JLabel("");
		frame.getContentPane().add(label_3, "cell 5 0 1 7,grow");

		JLabel label_4 = new JLabel("");
		frame.getContentPane().add(label_4, "cell 6 0 1 7,grow");

		JLabel label_5 = new JLabel("");
		frame.getContentPane().add(label_5, "cell 8 0 1 7,grow");

		JLabel label_6 = new JLabel("");
		frame.getContentPane().add(label_6, "cell 9 0 1 7,grow");

		JLabel label_7 = new JLabel("");
		frame.getContentPane().add(label_7, "cell 10 0,grow");

		JLabel label_8 = new JLabel("");
		frame.getContentPane().add(label_8, "cell 11 0,grow");

		JLabel label_9 = new JLabel("");
		frame.getContentPane().add(label_9, "cell 12 0 1 7,grow");

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
		frame.getContentPane().add(btnEkle, "cell 11 7,grow");

		JLabel label_10 = new JLabel("");
		frame.getContentPane().add(label_10, "cell 0 8 1 9,grow");

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
		frame.getContentPane().add(staffCombo, "cell 10 11,alignx left,aligny top");

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
		frame.getContentPane().add(addStaff, "cell 11 11,alignx left,aligny top");

		JLabel label_29 = new JLabel("Giris Yili: (Iki Haneli: 17 gibi)");
		frame.getContentPane().add(label_29, "cell 1 12");

		deptCombo = new JComboBox<String>();
		deptCombo.setModel(depts);
		frame.getContentPane().add(deptCombo, "cell 10 12,alignx left,aligny bottom");

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
		frame.getContentPane().add(deptAdd, "cell 11 12,alignx left,aligny top");

		txtYear = new JTextField();
		txtYear.setText("");
		frame.getContentPane().add(txtYear, "cell 1 13,growx");
		txtYear.setColumns(10);

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
				Selected.setText("Ogrenci Bilgisi:\nAd:" + "\nIkinci Ad:"
						+"\nSoyad: " + "\nGiris Yili:"
						+"\nDersler: " + "\nKulupler: "
						+ "\nHocalar: " + "\nBolum: ");
			}
		});
		frame.getContentPane().add(btnSonucuGetir, "cell 10 13,growx,aligny top");

		Selected = new JTextArea();
		JScrollPane scroll = new JScrollPane();
		Selected.setEditable(false);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Selected.add(scroll);
		Selected.setText("Ogrenci Bilgisi:\nAd:\nIkinci Ad:" +
				"\nSoyad: "  +"\nDersler: "  + "\nKulupler: "
				+ "\nHocalar: " + "\nBolum: " );


		frame.getContentPane().add(Selected, "cell 1 14,growx");
		Selected.setColumns(10);

		JLabel label_11 = new JLabel("");
		frame.getContentPane().add(label_11, "cell 1 16,grow");

		JLabel label_12 = new JLabel("");
		frame.getContentPane().add(label_12, "cell 2 8 1 9,grow");

		JLabel label_13 = new JLabel("");
		frame.getContentPane().add(label_13, "cell 5 8 1 9,grow");

		JLabel label_14 = new JLabel("");
		frame.getContentPane().add(label_14, "cell 6 8 1 9,grow");

		JLabel label_15 = new JLabel("");
		frame.getContentPane().add(label_15, "cell 8 8 1 9,grow");

		JLabel label_16 = new JLabel("");
		frame.getContentPane().add(label_16, "cell 9 8 1 9,grow");
		studentClubCombo = new JComboBox<String>();
		studentClubCombo.setModel(clubs);
		frame.getContentPane().add(studentClubCombo, "cell 10 7 1 4,grow");

		JLabel label_17 = new JLabel("");
		frame.getContentPane().add(label_17, "cell 11 8,grow");
		String[] pCourse = {"BİL361","BİL214","BİL264","BİL113","BİL481","BİL212","BİL133"};
		ComboBoxModel<String> preqCourse = new DefaultComboBoxModel<String>(pCourse);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(preqCourse);
		frame.getContentPane().add(comboBox, "cell 1 18,growx");
		//preqNumber = new DefaultComboBoxModel<String>(OtherFunctions.getStudentsTakeCourse((String)comboBox.getSelectedItem()));
		//comboBox_1.setModel(preqNumber);

		JButton btnEkle_1 = new JButton("Ekle");
		btnEkle_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] b = OtherFunctions.getStudentsTakeCourse((String)comboBox.getSelectedItem());
				System.out.println(b.length);
				preqNumber = new DefaultComboBoxModel<String>(b);
				comboBox_1.setModel(preqNumber);
				String[] s = preqdCourses.get((String)comboBox.getSelectedItem());
				coursespreqd = new DefaultComboBoxModel<String>(s);
				comboBox_2.setModel(coursespreqd);
			}
		});
		frame.getContentPane().add(btnEkle_1, "cell 3 18");

		JLabel lblThreshold = new JLabel("Threshold");
		frame.getContentPane().add(lblThreshold, "cell 7 18");



		frame.getContentPane().add(comboBox_1, "cell 1 19,growx");

		JLabel lblBolumKatsayisi = new JLabel("Bolum Katsayisi");
		frame.getContentPane().add(lblBolumKatsayisi, "cell 7 19");

		bolumKatsayisi = new JTextField(); //bolum
		frame.getContentPane().add(bolumKatsayisi, "cell 10 19,growx");
		bolumKatsayisi.setColumns(10);
		bolumKatsayisi.setText("0.6");
		comboBox_2 = new JComboBox<String>();


		frame.getContentPane().add(comboBox_2, "cell 1 20,growx");

		JLabel lblDersKatsayisi = new JLabel("Ders Katsayisi");
		frame.getContentPane().add(lblDersKatsayisi, "cell 7 20");

		dersKatsayisi = new JTextField(); //ders
		dersKatsayisi.setText("0.6");
		frame.getContentPane().add(dersKatsayisi, "cell 10 20,growx");
		dersKatsayisi.setColumns(10);

		JLabel lblToplulukKatsayisi = new JLabel("Topluluk Katsayisi");
		frame.getContentPane().add(lblToplulukKatsayisi, "cell 7 21");

		toplulukKatsayisi = new JTextField(); //topluluk
		toplulukKatsayisi.setText("0.5");
		frame.getContentPane().add(toplulukKatsayisi, "cell 10 21,growx");
		toplulukKatsayisi.setColumns(10);

		JLabel lblubeKatsayisi = new JLabel("\u015Eube Katsayisi");
		frame.getContentPane().add(lblubeKatsayisi, "cell 7 22");

		subeKatsayisi = new JTextField(); //sube
		subeKatsayisi.setText("0.8");
		frame.getContentPane().add(subeKatsayisi, "cell 10 22,growx");
		subeKatsayisi.setColumns(10);

		JTextArea textArea = new JTextArea();
		frame.getContentPane().add(textArea, "cell 10 27");

		JButton button = new JButton("Yakinlik Ara");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Neo4jDemo n = new Neo4jDemo();
				String course = (String)comboBox_2.getSelectedItem();
				String[] students = OtherFunctions.getStudentsTakeCourse(course);
				double max = 0;
				String number="";
				double gpa_max = 0;
				int sum =0;
				HashMap<Double, String> secenek = new HashMap<>();
				String neo4jQuery="MATCH (s:Student{student_id:\"" + comboBox_1.getSelectedItem().toString().trim() + "\"}),";
				/*for (int i=0;i<students.length;i++){
					neo4jQuery+= "(s"+i+":Student{student_id:\"" + students[i].trim() + "\"}),";
					neo4jQuery+= "p" +i +"=(s)-[r"+i+":DersIliskisi]-(s"+i+"),\n";
				}*/

				int ccount = 0;

				for(int i=0;i<students.length;i++) {
					String ilkNo = (String)comboBox_1.getSelectedItem();
					String sonNo = students[i];
					ilkNo = ilkNo.trim();
					sonNo = sonNo.trim();
					System.out.println(ilkNo + "-" +sonNo);
					if(ilkNo.equalsIgnoreCase(sonNo)) continue;
					double d = n.bolumIliskisi(ilkNo, sonNo);
					double d1 = n.dersIliskisi(ilkNo, sonNo);
					double d2 = n.kulupIliskisi(ilkNo, sonNo);
					double d3 = n.sectionIliskisi(ilkNo, sonNo);

					if(!bolumKatsayisi.getText().equals(""))
						d= d/0.6 * Double.parseDouble(bolumKatsayisi.getText());
					if(!dersKatsayisi.getText().equals(""))
						d1= d1/0.6 * Double.parseDouble(dersKatsayisi.getText());
					if(!toplulukKatsayisi.getText().equals(""))
						d2= d2/0.5 * Double.parseDouble(toplulukKatsayisi.getText());
					if(!subeKatsayisi.getText().equals(""))
						d3= d3/0.8 * Double.parseDouble(subeKatsayisi.getText());

					double ogrenci2Gpa = n.ogrenciGpa(sonNo);
					double toplam = d+d1+d2+d3;
					sum+=toplam;

					if(toplam>0){
						ccount++;
						neo4jQuery+= "(s"+i+":Student{student_id:\"" + students[i].trim() + "\"}),";
						if(d1>0)
							neo4jQuery+= "p" +i +"=(s)-[r"+i+":DersIliskisi]-(s"+i+"),\n";
						else if(d>0)
							neo4jQuery+= "p" +i +"=(s)-[r"+i+":BolumIliskisi]-(s"+i+"),\n";
						else if(d2>0)
							neo4jQuery+= "p" +i +"=(s)-[r"+i+":ToplulukIliskisi]-(s"+i+"),\n";
					}

					if(!textField.getText().equals("")  && toplam<Double.parseDouble(textField.getText())) continue;

					if(secenek.containsKey(toplam))
					{
						String val = secenek.get(toplam);
						String[] arr = val.split("-");
						int index = arr.length - 1;
						String realVal = arr[index];
						Double gpaOld = Double.parseDouble(realVal.split(" ")[1]);
						String newVal = val;
						if(ogrenci2Gpa>gpaOld)
							newVal = sonNo + " " + ogrenci2Gpa + " " + OtherFunctions.getStudentName(sonNo) + "-" + val;
						else
							newVal = val + "-" + sonNo + " " + ogrenci2Gpa + " " + OtherFunctions.getStudentName(sonNo);
						secenek.put(toplam, newVal);
					}
					else
					{
						secenek.put(toplam, sonNo + " " + ogrenci2Gpa + " " + OtherFunctions.getStudentName(sonNo));
					}
				}

				neo4jQuery = neo4jQuery.substring(0,neo4jQuery.length()-2)+ "\nRETURN s,";
				for(int i=0;i<ccount;i++){
					neo4jQuery += "s" +i +",";
				}

				neo4jQuery=neo4jQuery.substring(0,neo4jQuery.length()-1);
				System.out.println(neo4jQuery);
				OtherFunctions.writeToFile(neo4jQuery);


				System.out.println("BENIM YERIM");
				for(Double doble : secenek.keySet())
					System.out.println(doble + " -> " + secenek.get(doble));
				Double[] sorted = secenek.keySet().toArray(new Double[secenek.keySet().size()]);
				Arrays.sort(sorted);

				int count = 0;
				String[] outs = new String[3];
				boolean finito = false;
				for(int i = sorted.length-1 ; i > -1 ; i--)
				{
					String s = secenek.get(sorted[i]);
					System.out.println("Key:" + sorted[i] + " Value: "+ s);
					String[] tokens = s.split("-");
					for(int j = 0 ; j < tokens.length ; j++)
					{
						if(!finito)
							outs[count++] =  sorted[i]+" "+ tokens[j];
						if(count>2)
							finito = true;
					}
					if(finito)
						break;
				}

				textArea.setText("Puan Numara GPA\n"
						+ "_ _ _ _ _ _ _ _ _ _ _\n"+
						"En iyi secenek:\n"+outs[0]+"\nDiger secenekler:\n"+outs[1]+"\n"+outs[2]+"\nThreshold Onerisi: " + (sum/(double)students.length)
				+"\n Neo4j sorgusu neo4jquery.txt dosyasina kaydedilmistir.");
				n.closeDriver();

			}
		});
		frame.getContentPane().add(button, "cell 3 28");
		//frame.getContentPane().add(result, "cell 7 16,grow");

		JLabel label_18 = new JLabel("");
		frame.getContentPane().add(label_18, "cell 0 30 1 6,grow");

		JLabel label_20 = new JLabel("");
		frame.getContentPane().add(label_20, "cell 2 30 1 6,grow");

		JLabel label_21 = new JLabel("");
		frame.getContentPane().add(label_21, "cell 5 30 1 6,grow");

		JLabel label_22 = new JLabel("");
		frame.getContentPane().add(label_22, "cell 6 30 1 6,grow");

		JLabel label_23 = new JLabel("");
		frame.getContentPane().add(label_23, "cell 8 30 1 6,grow");

		JLabel label_24 = new JLabel("");
		frame.getContentPane().add(label_24, "cell 9 30 1 6,grow");

		JLabel label_25 = new JLabel("");
		frame.getContentPane().add(label_25, "cell 10 30,grow");

		JLabel label_26 = new JLabel("");
		frame.getContentPane().add(label_26, "cell 11 30,grow");

		JLabel label_28 = new JLabel("");
		frame.getContentPane().add(label_28, "cell 0 36,grow");

		JLabel label_27 = new JLabel("");
		frame.getContentPane().add(label_27, "cell 12 36,grow");
		courseCombo = new JComboBox<String>();
		courseCombo.setModel(courses);
		frame.getContentPane().add(courseCombo, "cell 10 2 1 5,grow");
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
		frame.getContentPane().add(co_add, "cell 11 2 1 5,grow");
		fname= new JTextField();
		frame.getContentPane().add(fname, "cell 1 6,growx,aligny top");
		fname.setColumns(10);

		result = new JTextArea();
		JScrollPane sp = new JScrollPane(result);
		result.setEditable(false);
		frame.getContentPane().add(sp, "cell 10 14,grow");


		textField = new JTextField();
		frame.getContentPane().add(textField, "cell 10 18,growx");
		textField.setColumns(10);


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
		String select = "Ogrenci Bilgisi:\nAd:" + fname.getText() + "\nIkinci Ad:" + minit.getText()
				+"\nSoyad: " + lname.getText() + "\nGiris Yili:" + txtYear.getText()
				+"\nDersler: " + courseSelected.toString() + "\nKulupler: "
				+ studentClubSelected.toString() + "\nHocalar: " + staff + "\nBolum: "
				+ deptsSelected.toString();
		Selected.setText(select);
	}
}
