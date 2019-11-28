package gui;

import consistency.mhsAlgs.RcTree;
import consistency.CbModel;
import util.Util;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConsistencyModeling {
    public JPanel panel;
    private JTextArea cnfModelArea;
    private JTextField observationArea;
    private JButton diagnoseObservationButton;
    private JTextArea propLogModelArea;
    private JButton exportCNFModelButton;
    private JButton convertToCNFButton;
    private JTextArea diagnosisArea;
    private JButton exportModelButton;
    private CbModel cbModel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("FmiDataExtractor");
        frame.setContentPane(new ConsistencyModeling().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ConsistencyModeling() {
        diagnoseObservationButton.addActionListener(e -> {
            diagnosisArea.setText(null);
            String obsStr = observationArea.getText();
            if(obsStr.isEmpty() || cbModel == null)
                return;
            obsStr = obsStr.replaceAll("\\s+","");
            List<String> obs = Arrays.asList(obsStr.split(","));
            RcTree rcTree = new RcTree(cbModel, cbModel.observationToInt(obs));
            try {
                for(List<Integer> mhs  : rcTree.getDiagnosis()) {
                    List<String> diag = cbModel.diagnosisToComponentNames(mhs);
                    diagnosisArea.append(String.join(", ", diag) + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        convertToCNFButton.addActionListener(e -> {
            cnfModelArea.setText(null);
            String model = propLogModelArea.getText();
            if(model.isEmpty())
                return;
            cbModel = new CbModel();
            cbModel.modelToCNF(model);
            cbModel.setNumOfDistinct(cbModel.getPredicates().getSize());
            for(List<String> line : cbModel.modelToString())
                cnfModelArea.append(String.join(", ", line) + "\n");

        });

        exportCNFModelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(Util.getCurrentDir());
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileWriter fw = new FileWriter(file);
                    fw.write(cnfModelArea.getText());
                    fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        exportModelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(Util.getCurrentDir());
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileWriter fw = new FileWriter(file);
                    fw.write(propLogModelArea.getText());
                    fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

}
