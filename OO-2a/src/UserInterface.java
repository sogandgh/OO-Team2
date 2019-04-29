import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class UserInterface {
	private List<Instruction> instructions;
	private Processor processor;
	private GUIListener listener;
	private JList instructionList;
	private JList registerList;
	private JList memoryList;
	private String filename;
	
	private DefaultListModel instructionModel;
	private DefaultListModel registerModel;
	private DefaultListModel memoryModel;

	public UserInterface() {  
		JFrame f;
		JButton b1,b2, b3,b4, b5, b6, b7;
		JPanel buttonPanel, panel, TextPanel, LabelPanel, ButtomLabelPanel;
		JPanel PicturePanel;
		JTextArea area;
		JLabel l, picLabel = null ;
		BufferedImage myPicture = null;

		
		
		f=new JFrame("User Interface");
	    f.setSize(550, 550); 
	    f.setLayout(new BorderLayout());
	    
		instructionList = new JList();
		JScrollPane instructionPane = new JScrollPane(instructionList);
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(instructionPane);
		
		registerList = new JList();
		memoryList = new JList();
		JScrollPane registerPane = new JScrollPane(registerList);
		JScrollPane dataPane = new JScrollPane(memoryList);
		JPanel rightPanel = new JPanel(new BorderLayout());
		//rightPanel.add(dataPane, BorderLayout.EAST);
		rightPanel.add(registerPane, BorderLayout.CENTER);
		
		
	    panel = new JPanel();
	    buttonPanel = new JPanel();
	    TextPanel = new JPanel();
	    LabelPanel = new JPanel();
	    ButtomLabelPanel = new JPanel();
	    PicturePanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxlayout);
        PicturePanel.setLayout(new BorderLayout());
	    panel.setBackground(new java.awt.Color(97, 87, 117));
		PicturePanel.setBackground(new java.awt.Color(97, 87, 117));
        buttonPanel.setBackground(new java.awt.Color(97, 87, 117));
        TextPanel.setBackground(new java.awt.Color(97, 87, 117));
        LabelPanel.setBackground(new java.awt.Color(97, 87, 117));
        ButtomLabelPanel.setBackground(new java.awt.Color(97, 87, 117));
        LabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

	    area=new JTextArea(10,30);  
		area.setBackground(new java.awt.Color(145, 142, 150));
		
	    
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select an assembly file ");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Assembly Files", "asm");
		jfc.addChoosableFileFilter(filter);

	    
	    try {
			myPicture = ImageIO.read(new File("images/logo.png"));

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    picLabel = new JLabel(new ImageIcon(myPicture));
	    picLabel.setPreferredSize(new Dimension(100, 100));

	    int x = (PicturePanel.getWidth() - picLabel.getWidth()) / 2;
	    int y = (PicturePanel.getHeight() - picLabel.getHeight()) / 2;

		picLabel.setLocation(x, y);
		
	    PicturePanel.add(picLabel, BorderLayout.CENTER);

	    b1=new JButton("Browse ");  
	    b2=new JButton("Run");  
	    b3=new JButton("Step");  
	    b4=new JButton("Reset");  
	    b5=new JButton("Stop");  
	    b7=new JButton("Load");  

	    l = new JLabel("Console");
	    
	    //area.setBounds(50, 90 , 20, 40);
	    b1.addActionListener(new ActionListener(){  
	public void actionPerformed(ActionEvent e){  
	            area.setText("Browse File Started...");  
	            int returnValue = jfc.showOpenDialog(null);
	    		if (returnValue == JFileChooser.APPROVE_OPTION) {
	    			area.setText("File added successfully: " + jfc.getSelectedFile().getPath());
					filename = jfc.getSelectedFile().getPath();
	    		}
	    		}  
	    });  
	    
	    b2.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	    	            area.setText("Run File Started successfully");  
	    				listener.onRun();

	    	        }  
	    	    });
	    
	    b3.addActionListener(new ActionListener(){  
	    	@Override
			public void actionPerformed(ActionEvent e) {
				listener.onStep();
			}
		});
	    
		b4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listener.onReset();	
			}
		});
	    
	  
	    b5.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	            area.setText("Paused successfully.");  
				listener.onStop();	


	    	        }  
	    	    });
	    b7.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(filename != null){
					listener.onLoad(filename);
				}
			}
		});
	    buttonPanel.add(b1);
	    buttonPanel.add(b7);
	    buttonPanel.add(b4);
	    ButtomLabelPanel.add(b2);
	    ButtomLabelPanel.add(b3);
	    ButtomLabelPanel.add(b5);
	    
	    
	    TextPanel.add(area);
	    LabelPanel.add(l);
	    panel.add(buttonPanel);
	    panel.add(LabelPanel);
	    panel.add(TextPanel);
	    panel.add(ButtomLabelPanel);
	    //f.add(buttonPanel);  
	    f.add(panel, BorderLayout.EAST);
	    f.add(PicturePanel, BorderLayout.WEST);
	  //  f.add(leftPanel, BorderLayout.WEST);
	    f.add(rightPanel, BorderLayout.SOUTH);


        f.pack();


	    f.setVisible(true);   

	}  
	
	private void load(String filename) {
		String line;
		BufferedReader reader = null;
		instructions = new ArrayList<Instruction>();


		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			int i = 0;
			while((line = reader.readLine()) != null){
				i++;
				if(line.length() == 0) {
					continue;
				}
				try {
					Instruction instruction = new Instruction(line);
					instructions.add(instruction);
				} catch (Exception e) {
					System.out.printf("Invalid instruction '%s' on line %d\n", line, i);
				}
			}
		} catch (IOException e) {
			System.out.printf("File reading error: %s \n", e.getMessage());
		}
		processor.setInstructionSet(instructions);
		
	}
	public interface GUIListener {
		public void onLoad(String filename);
		public void onStep();
		public void onRun();
		public void onStop();
		public void onReset();
		public void onHex();
		public void onDec();
	}

	public void setGUIListener(GUIListener listener) {
		this.listener = listener;
	}

	public void setInstructionListModel(ListModel model){
		instructionList.setModel(model);
	}

	public void setRegisterListModel(ListModel model){
		registerList.setModel(model);
	}

	public void setMemoryListModel(ListModel model){
		memoryList.setModel(model);
	}

	
	public void selectInstruction(int index) {
		instructionList.setSelectedIndex(index);
	}

	public void clearInstructionSelection() {
		instructionList.clearSelection();
	}


	}  


