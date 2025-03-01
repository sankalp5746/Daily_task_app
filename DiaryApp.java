import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

class DiaryApp {
    private static final String FILE_NAME = "diary.txt";
    private JFrame frame;
    private DefaultListModel<String> listModel;
    private JList<String> noteList;
    private JTextArea noteTextArea;
    private JButton addButton, editButton, deleteButton;
    private java.util.List<String> notes = new ArrayList<>();


    public DiaryApp() {
        frame = new JFrame("Diary Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        noteList = new JList<>(listModel);
        noteTextArea = new JTextArea();
        loadNotes();

        addButton = new JButton("Add Note");
        editButton = new JButton("Edit Note");
        deleteButton = new JButton("Delete Note");

        addButton.addActionListener(e -> addNote());
        editButton.addActionListener(e -> editNote());
        deleteButton.addActionListener(e -> deleteNote());

	noteList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && noteList.getSelectedIndex() != -1) {
            noteTextArea.setText(noteList.getSelectedValue());
         }
    });

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(noteList), BorderLayout.CENTER);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Notes"));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(noteTextArea), BorderLayout.CENTER);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Write/Edit Note"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void addNote() {
        String note = noteTextArea.getText().trim();
        if (!note.isEmpty()) {
            listModel.addElement(note);
            notes.add(note);
            saveNotes();
            noteTextArea.setText("");
        }
    }

    private void editNote() {
        int index = noteList.getSelectedIndex();
        if (index != -1) {
            String newText = noteTextArea.getText().trim();
            if (!newText.isEmpty()) {
                listModel.set(index, newText);
                notes.set(index, newText);
                saveNotes();
            }
        }
    }

    private void deleteNote() {
        int index = noteList.getSelectedIndex();
        if (index != -1) {
            listModel.remove(index);
            notes.remove(index);
            saveNotes();
            noteTextArea.setText("");
        }
    }

    private void loadNotes() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                listModel.addElement(line);
                notes.add(line);
            }
        } catch (IOException ignored) {}
    }

    private void saveNotes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String note : notes) {
                writer.write(note);
                writer.newLine();
            }
        } catch (IOException ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DiaryApp::new);
    }
} 