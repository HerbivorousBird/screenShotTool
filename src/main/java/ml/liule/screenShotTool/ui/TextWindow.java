/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.liule.screenShotTool.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author liu
 */
public class TextWindow extends javax.swing.JFrame {

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea;

    /**
     * Creates new form TextWindow
     */
    public TextWindow(String text) {
        initComponents();
        jTextArea.setText(text);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public TextWindow() {
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void setText(String t) {
        jTextArea.setText(t);
    }

    private void initComponents() {
        jScrollPane1 = new JScrollPane();
        jTextArea = new JTextArea();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OCR或QRScan结果(右键复制到剪贴板)");
        setIconImage(MainWindow.icon);
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(255, 255, 255));
        jTextArea.setFont(new java.awt.Font(null, 0, 18));

        jTextArea.setToolTipText("右键复制文本");
        jTextArea.setText("OCR需要一定时间，请等待。。。");
        jTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 3) {
                    copyToClipboard();
                }
            }
        });
        jScrollPane1.setViewportView(jTextArea);
        setPreferredSize(new java.awt.Dimension(600, 400));
        this.add(jScrollPane1);

        pack();
    }

    private void copyToClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(jTextArea.getText());
        clipboard.setContents(selection, null);
    }
}
