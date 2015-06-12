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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogWindow extends JPanel {

	private final JFrame frame;
	private final JScrollPane scrollPane;
	private final TextArea logText;
	private final String FILENAME;

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
		scrollPane.setPreferredSize(new Dimension(450, 110));
		writeToTextFile("-------------------------" + dateFormat.format(date)
				+ "-------------------------");
		readMessages();

		frame.add(scrollPane);
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
         * listens if window was closed and removes check from checkbox of log menu item
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
}

