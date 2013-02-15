package com.apn.filerenamer.ui;

import com.apn.filerenamer.entities.Movie;
import com.apn.filerenamer.persistence.MorphiaDb;
import com.apn.filerenamer.persistence.MovieLibrary;
import com.apn.filerenamer.persistence.PersistentMovieLibrary;
import com.google.common.collect.Ordering;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.apn.filerenamer.ui.FileFilters.newDirectoryFilter;
import static com.apn.filerenamer.ui.FileFilters.newFileFilter;
import static com.apn.filerenamer.ui.Utils.*;
import static org.apache.commons.lang.StringUtils.isBlank;

public class FileRenamer {
    private JButton selectRootFolderButton;
    private JTree dirTree;
    private JButton exitButton;
    private JPanel topPanel;
    private JButton processFilesButton;
    private JButton refreshButton;
    private JButton import2DBButton;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private Path baseDir = Paths.get("");
    private ProcessFiles processDialog;
    private String defaultLocation = "drobo";

    public FileRenamer() {
        processDialog = new ProcessFiles(null, null);
        processDialog.setOwner(this);
        processDialog.setSize(600, 200);

        exitButton.addActionListener(new ExitActionListener());
        selectRootFolderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFileChooserDialog();
            }
        });
        processFilesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openProcessDialogIfSelectedFromTree();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTree();
            }
        });
        dirTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == RETURN_KEYCODE) {
                    openProcessDialogIfSelectedFromTree();
                }
            }
        });
        import2DBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importMoviesToLibrary();
            }
        });
    }

    private void importMoviesToLibrary() {
        try {
            validateSelectedDirsFromTree();
            String[] names = fileNamesArrayFrom(dirTree.getSelectionPaths());
            //validate names
            defaultLocation = getLocationFromUser(names.length);
            System.out.println("location = " + defaultLocation);
            MovieLibrary movieLibrary = new PersistentMovieLibrary(new MorphiaDb().getDatastore("movie_db"));
            for (String movieName : names) {
                Movie movie = new Movie(movieName, defaultLocation);
                System.out.println("movie = " + movie);
                movieLibrary.addMovie(movie);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(import2DBButton,
                e.getMessage(),
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private String getLocationFromUser(int length) {
        String location = (String) JOptionPane.showInputDialog(
                import2DBButton,
                String.format("%s movies will be imported.\n Define Location:",length),
                "Import movies location",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                defaultLocation);
        if(isBlank(location)){
            throw new IllegalArgumentException("No location was provided!");
        }
        return location;
    }

    private void validateSelectedDirsFromTree() {
        if (dirTree.getSelectionCount() < 1) {
            throw new IllegalArgumentException("No files selected from the tree!");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FileRenamer");
        frame.setContentPane(new FileRenamer().topPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void openFileChooserDialog() {
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(baseDir.toFile());
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //In response to a button click:
        if (fc.showOpenDialog(selectRootFolderButton) == 0) {
            buildFileStructure(fc.getSelectedFile().toPath());
            baseDir = fc.getSelectedFile().toPath();
        }
    }

    private void openProcessDialogIfSelectedFromTree() {
        validateSelectedFromTree();

        openProcessDialog();
    }

    private void openProcessDialog() {
        processDialog.setBaseDir(rootNode.toString());
        processDialog.setFilesToProcess(buildFileArrayToProcess());
        processDialog.setVisible(true);
        processDialog.newDirName.selectAll();
    }

    private void validateSelectedFromTree() {
        if (dirTree.getSelectionCount() < 1) {
            throw new IllegalArgumentException("No files selected from the tree!");
        }
    }

    private File[] buildFileArrayToProcess() {
        return buildFileArrayFrom(
                rootNode.toString(),
                fileNamesArrayFrom(dirTree.getSelectionPaths())
        );
    }

    public void refreshTree() {
        buildFileStructure((Path) rootNode.getUserObject());
    }

    public void buildFileStructure(Path rootDir) {
        validateRootDir(rootDir);
        resetTree(rootDir);
        DefaultMutableTreeNode parentNode = rootNode;

        addDirectoriesTo(parentNode, rootDir);
        addFilesTo(parentNode, rootDir);

        treeModel.reload();
    }

    private void addDirectoriesTo(DefaultMutableTreeNode parentNode, Path rootDir) {
        addElementsToTree(parentNode, rootDir, newDirectoryFilter, true);
    }

    private void addFilesTo(DefaultMutableTreeNode parentNode, Path rootDir) {
        addElementsToTree(parentNode, rootDir, newFileFilter, false);
    }

    private void addElementsToTree(DefaultMutableTreeNode parentNode, Path rootDir, DirectoryStream.Filter<Path> elementFilter, boolean subProcess) {

        try (DirectoryStream<Path> elements = Files.newDirectoryStream(rootDir, elementFilter)) {
            List<Path> sorted = Ordering.natural().sortedCopy(elements);
            for (Path path : sorted) {
                DefaultMutableTreeNode dirNode = addObject(parentNode, path.getName(path.getNameCount() - 1), true);
                if (subProcess) {
                    addDirectoriesTo(dirNode, path);
                    addFilesTo(dirNode, path);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error building file tree:", e);
        }
    }

    private void resetTree(Path rootDir) {
        rootNode.removeAllChildren();
        rootNode.setUserObject(rootDir);
    }

    private void validateRootDir(Path rootDir) {
        if (rootDir == null || !Files.exists(rootDir))
            throw new IllegalArgumentException("Selected directory is not valid");
    }

    private DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                             Object child,
                                             boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode =
                new DefaultMutableTreeNode(child);
        treeModel.insertNodeInto(childNode, parent,
                parent.getChildCount());

        if (shouldBeVisible) {
            dirTree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    private void createUIComponents() {
        rootNode = new DefaultMutableTreeNode("RootNode");
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new CustomModelListener());

        dirTree = new JTree(treeModel);
        dirTree.setEditable(true);
        dirTree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        dirTree.setShowsRootHandles(false);
    }

}

