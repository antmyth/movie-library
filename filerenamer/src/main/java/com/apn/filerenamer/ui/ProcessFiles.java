package com.apn.filerenamer.ui;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;

import static com.apn.filerenamer.ui.Utils.*;

public class ProcessFiles extends JDialog {
    public static final String[] FILE_TYPES = new String[]{"cd","scn",""};

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    public JTextField newDirName;
    private JComboBox fileType;
    protected JLabel newDirNameLabel;

    public void setFilesToProcess(File[] filesToProcess) {
        this.filesToProcess = filesToProcess;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    private File[] filesToProcess;
    private String baseDir;
    private FileRenamer owner;

    public static void main(String[] args) {
        ProcessFiles dialog = new ProcessFiles(null, null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public ProcessFiles(File[] filesToProcess, String baseDir) {
        this.filesToProcess = filesToProcess;
        this.baseDir = baseDir;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeProcessWindow();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeProcessWindow();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        newDirName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == RETURN_KEYCODE) {
                    renameAndMoveFiles(ProcessFiles.this.newDirName.getText());
                }
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                renameAndMoveFiles(newDirName.getText());
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeProcessWindow();
            }
        });
    }

    private void renameAndMoveFiles(String newDirName) {
        validateNewDirName(newDirName);

        Path newBaseDir = createDirectory(newDirName, baseDir);
        renameFile(newDirName, newBaseDir, fileType.getSelectedItem().toString(), filesToProcess);
        owner.refreshTree();
        dispose();
    }

    private void validateNewDirName(String newDirName) {
        if (newDirName == null || newDirName.trim().length() <= 0) {
            throw new IllegalArgumentException("");
        }
    }

    private void closeProcessWindow() {
        dispose();
    }

    @SuppressWarnings("unchecked")
    private void createUIComponents() {
        fileType = new JComboBox(FILE_TYPES);
    }

    public void setOwner(FileRenamer owner) {
        this.owner = owner;
    }
}
