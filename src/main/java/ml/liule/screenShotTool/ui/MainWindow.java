/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.liule.screenShotTool.ui;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author liu
 */
public class MainWindow extends javax.swing.JWindow {

    /**
     *
     * 保存数据库的连接，供后续向数据库读写数据
     */
    public static Connection dbConnection = null;

    /**
     *
     * 截图记录文件以及SQLite3数据库文件的存储路径
     */
    public static String recordsPath = getProjectPath() + "/records/";

    /**
     *
     * 程序窗口的图标
     */
    public static BufferedImage icon = null;

    private static String getProjectPath() {
        String path = null;
        try {
            path = new File("").getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return path;
    }

    private Color yinColor = Color.BLACK;
    private Color yangColor = Color.WHITE;
    private Area yinArea = null;
    private Area yangArea = null;
    private int curHighlight = 0;
    private Point curPoint = null;
    private Point lastPoint = null;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        initTray();
        initTaiChiArea();
        initFile();
    }

    private void initComponents() {
        setAlwaysOnTop(true);
        setPreferredSize(new java.awt.Dimension(192, 192));
        setLocationRelativeTo(null);   //设置窗口居中
        setOpacity((float) (0.80));  //设置窗口透明
        try {
            icon = ImageIO.read(getClass().getResource("/ml/liule/screenShotTool/res/icon.png"));
            setIconImage(icon);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        JPanel panel = new JPanel();  //不添加Jpanel，只有JWindow的话，似乎没法处理mouseExit事件。
        this.add(panel);
        panel.setToolTipText("单击白色区域截图，黑色区域查看记录;拖拽窗口移动，右键窗口隐藏");
        pack();
        setShape(new Ellipse2D.Float(0, 0, this.getWidth(), this.getWidth()));  //设置圆形窗口
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                onMouseClicked(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                yangColor = Color.WHITE;
                yinColor = Color.BLACK;
                curHighlight = 0;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
            }

            private void onMouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    setVisible(false);
                    return;
                }
                if (yangArea.contains(e.getPoint())) {
                    startShot();
                } else {
                    showRecords();
                }
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                if (yangArea.contains(e.getPoint()) && curHighlight != 1) {
                    yangColor = Color.DARK_GRAY;
                    yinColor = Color.BLACK;
                    curHighlight = 1;
                    repaint();
                } else if (yinArea.contains(e.getPoint()) && curHighlight != 2) {
                    yangColor = Color.WHITE;
                    yinColor = Color.LIGHT_GRAY;
                    curHighlight = 2;
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseDragged(e);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Meta".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
////            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    private void onMouseDragged(MouseEvent e) {
        curPoint = e.getPoint();
        int xOff = curPoint.x - lastPoint.x;
        int yOff = curPoint.y - lastPoint.y;
        this.setLocation(this.getX() + xOff, this.getY() + yOff);
    }

    private void startShot() {
        this.setVisible(false);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ScrShotWindow shot = new ScrShotWindow();
            shot.setVisible(true);
        } catch (AWTException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showRecords() {
        RecordsWindow records = new RecordsWindow();
        records.setVisible(true);
    }

    private void connectToDB() throws SQLException {
        String createTableSql = "CREATE TABLE records("
                + "id INTEGER PRIMARY KEY autoincrement,"
                + "time INTEGER ,"
                + "result TEXT"
                + ")";
        dbConnection = DriverManager.getConnection("jdbc:sqlite:" + recordsPath + "records.db");
        Statement statement = (Statement) dbConnection.createStatement();
        try {
            statement.execute(createTableSql);
        } catch (SQLException e) {
        }
    }

    private void initTray() {
        if (!SystemTray.isSupported()) {
            return;
        }
        MainWindow mainWindow = this;
        SystemTray tray = SystemTray.getSystemTray();  //获取系统托盘
        PopupMenu pop = new PopupMenu(); // 构造一个右键弹出式菜单
        MenuItem showMain = new MenuItem("打开主窗口");
        MenuItem showRecordsMenu = new MenuItem("查看记录");
        MenuItem showAboutMenu = new MenuItem("关于程序");
        MenuItem exit = new MenuItem("退出程序");
        showMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.setVisible(true);
            }
        });
        showRecordsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRecords();
            }
        });
        showAboutMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            }
        });
        pop.add(showMain);
        pop.add(showRecordsMenu);
        pop.add(showAboutMenu);
        pop.add(exit);
        TrayIcon trayIcon = new TrayIcon(icon, "左键单击截图,右键单击菜单", pop);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 1) {
                    startShot();
                }
            }
        });
        
        try {
            tray.add(trayIcon);
        } catch (AWTException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paint(Graphics g) {
        paintTaiChi(g);
    }

    private void initTaiChiArea() {
        int r = this.getWidth() / 2 - 1;
        int d = this.getWidth() - 1;
        Arc2D halfYin = new Arc2D.Float(0, 0, d, d, 90, -180, Arc2D.PIE);
        Arc2D halfYang = new Arc2D.Float(0, 0, d, d, 90, 180, Arc2D.PIE);
        Arc2D quarYin = new Arc2D.Float(r / 2 + 2, r + 1, r, r, 90, 180, Arc2D.PIE);
        Arc2D quarYang = new Arc2D.Float(r / 2, 0, r, r, 90, -180, Arc2D.PIE);
        Ellipse2D tinyYin = new Ellipse2D.Float(r - r / 8, r / 2 - r / 8, r / 4, r / 4);
        Ellipse2D tinyYang = new Ellipse2D.Float(r - r / 8, 3 * r / 2 - r / 8, r / 4, r / 4);

        yinArea = new Area(halfYin);
        yinArea.add(new Area(quarYin));
        yinArea.subtract(new Area(quarYang));
        yinArea.subtract(new Area(tinyYang));
        yinArea.add(new Area(tinyYin));

        yangArea = new Area(halfYang);
        yangArea.add(new Area(quarYang));
        yangArea.subtract(new Area(quarYin));
        yangArea.subtract(new Area(tinyYin));
        yangArea.add(new Area(tinyYang));
    }

    private void paintTaiChi(Graphics g1) {
        BufferedImage temp = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) temp.getGraphics();
        int r = this.getWidth() / 2 - 1;
        int d = this.getWidth() - 1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(yangColor);
        g.fill(yangArea);
        g.setColor(yinColor);
        g.fill(yinArea);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke((float) 6.0));
        g.drawOval(0, 0, d, d);
        g1.drawImage(temp, 0, 0, null);
    }

    private void showAbout() {
        String about = "本工具简陋的介绍\n"
                + "建议在windows下运行本程序,因为在我的黑MAC下测试时出现\n"
                + "工具条Color面板底色不显示(主题原因)以及偶现截图白屏BUG\n"
                + "待您关闭该窗口后，将出现一个‘太极’窗口，同时在托盘显示太极图标\n"
                + "单击太极白色区域，进行截图；单击黑色区域，打开历史记录\n"
                + "拖拽太极移动窗口，右键隐藏窗口到托盘\n"
                + "托盘左键截图，右键菜单\n"
                + "软件记录的数据在项目文件夹下的records目录\n"
                + "在查看记录面板双击记录时间，可以还原当时的截图场景\n"
                + "OCR采用百度的接口，不对其隐私性及准确性负责\n"
                + "截图界面部分UI逻辑受Snipaste启发\n"
                + "滚轮可以调整画笔粗细、字号大小\n"
                + "祝您使用愉快！\n";
        TextWindow textWindow = new TextWindow(about);
        textWindow.setTitle("关于程序： made by liule");
    }

    private void initFile() {
        File r = new File(recordsPath);
        if (!r.exists()) {
            r.mkdir();
            showAbout();
        }
        r = new File(recordsPath + "/file");
        if (!r.exists()) {
            r.mkdir();
        }
        try {
            connectToDB();
        } catch (SQLException e) {
            System.err.println("数据库连接失败");
            System.exit(1);
        }
    }
}
