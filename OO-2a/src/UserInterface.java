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
import java.io.File;
import java.io.IOException;  

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class UserInterface {
	public static void main(String[] args) {  
		JFrame f;
		JButton b1,b2, b3,b4, b5, b6;
		JPanel buttonPanel, panel, TextPanel, LabelPanel, ButtomLabelPanel;
		JPanel PicturePanel;
		JTextArea area;
		JLabel l, picLabel = null ;
		BufferedImage myPicture = null;
		f=new JFrame("User Interface");
	    f.setSize(550, 550); 
	    f.setLayout(new BorderLayout());
	    
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

	    b1=new JButton("Browse File");  
	    b2=new JButton("Run File");  
	    b3=new JButton("Run File in One Step");  
	    b4=new JButton("View Registers");  
	    b5=new JButton("PAUSE");  

	    l = new JLabel("Console");
	    
	    //area.setBounds(50, 90 , 20, 40);
	    b1.addActionListener(new ActionListener(){  
	public void actionPerformed(ActionEvent e){  
	            area.setText("Browse File Started...");  
	            int returnValue = jfc.showOpenDialog(null);
	    		if (returnValue == JFileChooser.APPROVE_OPTION) {
	    			area.setText("File added successfully: " + jfc.getSelectedFile().getPath());
	    		}
	    		}  
	    });  
	    
	    b2.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	    	            area.setText("Run File Started successfully");  

	    	        }  
	    	    });
	    
	    b3.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	            area.setText("The single step operation was successful");  


	    	        }  
	    	    });
	    b4.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	            area.setText("Showed register contents successfully");  


	    	        }  
	    	    });
	    b5.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	            area.setText("Paused successfully.");  


	    	        }  
	    	    });
	    
	    buttonPanel.add(b1);
	    buttonPanel.add(b2);
	    buttonPanel.add(b3);
	    ButtomLabelPanel.add(b4);
	    ButtomLabelPanel.add(b5);
	    
	    TextPanel.add(area);
	    LabelPanel.add(l);
	    panel.add(buttonPanel);
	    panel.add(LabelPanel);
	    panel.add(TextPanel);
	    panel.add(ButtomLabelPanel);
	    //f.add(buttonPanel);  
	    f.add(panel, BorderLayout.WEST);
	    f.add(PicturePanel, BorderLayout.EAST);
        f.pack();


	    f.setVisible(true);   
	}  
	}  


