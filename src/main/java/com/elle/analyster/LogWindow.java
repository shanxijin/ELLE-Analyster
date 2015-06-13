/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.elle.analyster;

/**
 *
 * @author Tina
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogWindow {

	private final JFrame frame;
	private final JScrollPane scrollPane;
	private final TextArea logText;
	private final String FILENAME;
        private final ArrayList<String> messages = new ArrayList<>();

	public LogWindow() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss a");
		FILENAME = "log.txt";
		frame = new JFrame("Log Window");
		ImageIcon imag = new ImageIcon(
				"Images/elle gui image.jpg");
		frame.setIconImage(imag.getImage());

		logText = new TextArea(5, 30);
		logText.setEditable(false);
		scrollPane = new JScrollPane(logText);
		
		writeToTextFile("-------------------------" + dateFormat.format(date)
				+ "-------------------------");
		readMessages();
                
                
                // change layout of frame
                frame.setLayout(new GridBagLayout());
                
                // set constraints for the scroll panel
                GridBagConstraints scrollPanelConstraints = new GridBagConstraints();
                scrollPanelConstraints.fill = GridBagConstraints.BOTH;
                scrollPanelConstraints.weightx = 1; // takes up whole x axis
                scrollPanelConstraints.weighty = 1; // takes up most y axis with room for buttons
                scrollPanelConstraints.gridx = 0; // first col cell
                scrollPanelConstraints.gridy = 0; // first row cell
                
                // add scroll panel to frame
		frame.add(scrollPane, scrollPanelConstraints);
                
                // create a panel for buttons
                JPanel jPanelLogWindowButtons = new JPanel();
                
                // create buttons 
                JButton jBtnClearAll = new JButton("Clear All");
                jBtnClearAll.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jBtnClearAllActionPerformed(evt);
                    }
                });
                JButton jBtnClearAllButToday = new JButton("Clear All But Today");
                jBtnClearAllButToday.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jBtnClearAllButTodayActionPerformed(evt);
                    }
                });
                JCheckBox jCheckBoxOrder = new JCheckBox();
                jCheckBoxOrder.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBoxOrderActionPerformed(evt);
                    }
                });
                JLabel jLabelOrder = new JLabel("Order");
                
                // add buttons to panel
                jPanelLogWindowButtons.add(jBtnClearAll);
                jPanelLogWindowButtons.add(jBtnClearAllButToday);
                jPanelLogWindowButtons.add(jCheckBoxOrder);
                jPanelLogWindowButtons.add(jLabelOrder);
                
                // set constraints for the buttons panel
                GridBagConstraints buttonsPanelConstraints = new GridBagConstraints();
                buttonsPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
                buttonsPanelConstraints.weightx = 1; // takes up whole x axis
                buttonsPanelConstraints.weighty = 0; // takes up enough y axis just for buttons
                buttonsPanelConstraints.gridx = 0; // first col cell
                buttonsPanelConstraints.gridy = 1; // second row cell
                
                // add panel to the frame
                frame.add(jPanelLogWindowButtons,buttonsPanelConstraints);
                
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.pack();
		frame.setVisible(false);
	}

	public String fillSQLCommand(String str) {

		String output;
		if (str.startsWith("#")) {
			output = "SELECT *\nFROM trades\nWHERE Symbol = " + "'"
					+ str.substring(1) + "'";
		} else {
			String[] lst = str.split(" ");
			output = "SELECT *\nFROM trades\nWHERE Trade_Time BETWEEN " + "'"
					+ lst[0].substring(1) + "' AND '" + lst[1] + "'";
		}
		return output;
	}

	public void sendMessages(String str) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss a");
		writeToTextFile(dateFormat.format(date) + ": " + str);
		readCurrentMessages(dateFormat.format(date) + ": " + str);
	}

	public void readCurrentMessages(String str) {
		logText.append(str);
		logText.append("\n");
	}

	public void readMessages() {
		String line = "";
		try {
			FileReader fileReader = new FileReader(FILENAME);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			line = bufferedReader.readLine();
			while (line != null) {
				logText.append(line);
				logText.append("\n");
                                messages.add(line); // store messages in an array for ordering
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(frame,
					"Error: Fail to read the log file");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frame, "Unknown error");
		}
	}

	public void writeToTextFile(String str) {
		File file = new File(FILENAME);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(FILENAME, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(str);
			bufferedWriter.newLine();
			if (str.endsWith("\n"))
				bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(frame,
					"Error: Fail to write the log file");
		} catch (Exception ex) {
			JOptionPane.showConfirmDialog(frame, "Unknow error");
		}
	}

	public void showLogWindow() {
		frame.setVisible(true);
	}
        
        /**
         * hides log window by setting visible to false
         */
        public void hideLogWindow() {
		frame.setVisible(false);
	}
        
        /**
         * listens if window was closed and removes check from check-box of log menu item
         * @param JCheckBoxMenuItem
         */
        public void removeCheckOnHideLogWindow(JCheckBoxMenuItem checkMenuBox) {
		frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e){
                        checkMenuBox.setSelected(false);
                    }
                });
	}
        
        /**
         * Clear all button: When the Clear all button is clicked, 
         * all the messages are removed from the scroll pane text box.
         */
        private void jBtnClearAllActionPerformed(ActionEvent evt) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        /**
         * Clear all but today button action performed: 
         * When the Clear all but today button is clicked, 
         * all the messages are removed from the scroll pane text box,
         * except todays.
         */
        private void jBtnClearAllButTodayActionPerformed(ActionEvent evt) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        /**
         * Order check box: When the order check box is checked, 
         * all the messages are reversed in order in the scroll pane text box.
         */
        private void jCheckBoxOrderActionPerformed(ActionEvent evt) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
}

