/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.liule.screenShotTool.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import javax.swing.JWindow;
import ml.liule.screenShotTool.graelement.MyPolyline;
import ml.liule.screenShotTool.graelement.MyRectangle;
import ml.liule.screenShotTool.graelement.MyStringGra;
import org.json.JSONObject;

/**
 *
 * @author liu
 */
public class ScrShotWindow extends JWindow {

    public static final int SELECT = 0;
    public static final int SELECTED = 1;
    public static final int CHAR = 2;
    public static final int LINE = 3;
    public static final int DRAW = 4;
    public static final int REC = 5;

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final ControlWindow control;
    private boolean isSaveToDB;

    private Point lastPoint = null;
    private Point curPoint = null;
    private int reSizeRecDir = ChosenRectangle.IN;
    private boolean isPanRec = false;

    private int curOptions = SELECT;
    private Color curColor = Color.BLACK;
    private float curWeight = 4;
    private String curString = null;

    public ScrShotWindow() throws AWTException {
        initWindow();
        isSaveToDB = true;
        Robot robot = new Robot();
        BufferedImage shotImage = robot.createScreenCapture(new Rectangle(0, 0, screenSize.width, screenSize.height));
        control = new ControlWindow(this, shotImage);
    }

    public ScrShotWindow(JSONObject json, BufferedImage shotImage) {
        initWindow();
        curOptions = SELECTED;
        isSaveToDB = false;
        control = new ControlWindow(this, shotImage);
        control.fromJson(json);
        control.resetLocation();
        control.setVisible(true);
        control.refreshButtonState();
    }

    public void setCurOptions(int ops) {
        curOptions = ops;
        control.tempGra = null;
        control.highLightOptions();
        control.draw();
    }

    public Color getCurColor() {
        return curColor;
    }

    public boolean getIsSaveToDB() {
        return isSaveToDB;
    }

    public void setCurColor(Color curColor) {
        this.curColor = curColor;
    }

    public int getCurOptions() {
        return curOptions;
    }

    public void setCurString(String curString) {
        this.curString = curString;
    }

    private void initWindow() {
        this.setBounds(0, 0, screenSize.width, screenSize.height);
        this.setAlwaysOnTop(true);
        this.setVisible(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                onMouseReleased(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onDoubleClicked(e);
                } else if (e.getClickCount() == 1) {
                    onMouseClicked(e);
                }
            }

        });

        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseDraggde(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseMoved(e);
            }
        });

        this.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                onMouseWheelMoved(e);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        control.draw();
    }

    private void onMousePressed(MouseEvent e) {
        lastPoint = e.getPoint();
        switch (curOptions) {
            case SELECT:
                control.chosenRec = new ChosenRectangle(0, 0, 1, 1);
                control.chosenRec.setLocation(lastPoint);
                break;
            case SELECTED:
                if (control.chosenRec.contains(lastPoint)) {
                    isPanRec = true;
                } else {
                    isPanRec = false;
                    reSizeRecDir = control.chosenRec.outcode(lastPoint);
                    control.chosenRec.add(lastPoint);
                    control.resetLocation();
                }
                break;
            case CHAR:
                break;
            case LINE:
                break;
            case DRAW:
                break;
            case REC:
                break;
            default:
                break;
        }
        repaint();
    }

    private void onMouseReleased(MouseEvent e) {
        switch (curOptions) {
            case SELECT:
                setCurOptions(SELECTED);
                if (control.chosenRec.width == 1 && control.chosenRec.height == 1) {
                    control.chosenRec.setLocation(0, 0);
                    control.chosenRec.setSize(screenSize);
                }
                control.resetLocation();
                control.setVisible(true);
                break;
            case SELECTED:
                isPanRec = false;
                break;
            case CHAR:
                break;
            case LINE:
                break;
            case DRAW:
                control.addTempGra();
                control.refreshButtonState();
                break;
            case REC:
                control.addTempGra();
                control.refreshButtonState();
                break;
            default:
                break;
        }
        repaint();
    }

    private void onMouseDraggde(MouseEvent e) {
        curPoint = e.getPoint();
        switch (curOptions) {
            case SELECT:
                control.chosenRec.reSizeBy(curPoint);
                break;
            case SELECTED:
                if (isPanRec) {
                    control.chosenRec.panInRec(lastPoint, curPoint, new Rectangle(0, 0, screenSize.width, screenSize.height));
                    lastPoint = curPoint;
                } else {
                    control.chosenRec.reSizeBy(lastPoint, curPoint, reSizeRecDir);
                    lastPoint = curPoint;
                }
                control.resetLocation();
                break;
            case CHAR:
                break;
            case LINE:
                if (control.tempGra == null) {
                    setCurOptions(DRAW);
                }
                break;
            case DRAW:
                if (control.tempGra == null) {
                    control.tempGra = new MyPolyline(curColor, curWeight);
                }
                MyPolyline tempPoly = (MyPolyline) control.tempGra;
                tempPoly.addPoint(e.getPoint());
                break;
            case REC:
                if (control.tempGra == null) {
                    control.tempGra = new MyRectangle(lastPoint, curColor, curWeight);
                }
                MyRectangle tempRec = (MyRectangle) control.tempGra;
                tempRec.setEndP(e.getPoint());
                break;
            default:
                break;
        }
        repaint();
    }

    private void onMouseClicked(MouseEvent e) {
        switch (curOptions) {
            case SELECT:
                break;
            case SELECTED:
                break;
            case CHAR:
                control.addTempGra();
                setCurOptions(SELECTED);
                control.refreshButtonState();
                break;
            case LINE:
                if (control.tempGra == null) {
                    control.tempGra = new MyPolyline(curColor, curWeight);
                }
                MyPolyline tempPoly = (MyPolyline) control.tempGra;
                tempPoly.addPoint(e.getPoint());
                break;
            case DRAW:
                if (control.tempGra == null) {
                    setCurOptions(LINE);
                    control.tempGra = new MyPolyline(curColor, curWeight);
                    tempPoly = (MyPolyline) control.tempGra;
                    tempPoly.addPoint(e.getPoint());
                }
                break;
            case REC:
                break;
            default:
                break;
        }
    }

    private void onDoubleClicked(MouseEvent e) {
        switch (curOptions) {
            case SELECT:
                break;
            case SELECTED:
                break;
            case CHAR:
                break;
            case LINE:
                control.addTempGra();
                control.refreshButtonState();
                break;
            case DRAW:
                break;
            case REC:
                break;
            default:
                break;
        }
    }

    private void onMouseMoved(MouseEvent e) {
        curPoint = e.getPoint();
        switch (curOptions) {
            case SELECT:
                break;
            case SELECTED:
                if (control.chosenRec.contains(curPoint)) {
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                } else {
                    setCursor(control.chosenRec.getDirectionCursor(curPoint));
                }
                break;
            case CHAR:
                if (control.chosenRec.contains(curPoint)) {
                    setCursor(new Cursor(Cursor.TEXT_CURSOR));
                    {
                        if (control.tempGra == null) {
                            control.tempGra = new MyStringGra(curString, curColor, curWeight);
                        }
                        MyStringGra tempString = (MyStringGra) control.tempGra;
                        tempString.setPosition(curPoint);
                        repaint();
                    }
                } else {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                break;
            case LINE:
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                if (control.tempGra != null) {
                    MyPolyline tempPoly = (MyPolyline) control.tempGra;
                    tempPoly.setRubberPoint(e.getPoint());
                    repaint();
                }
                break;
            case DRAW:
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                break;
            case REC:
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                break;
            default:
                break;
        }
    }

    private void onMouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() == -1) {
            if (curWeight < 30) {
                curWeight += 0.5;
            }
        } else if (e.getWheelRotation() == 1) {
            if (curWeight > 1) {
                curWeight -= 0.5;
            }
        }
        if (control.tempGra == null) {
            return;
        }
        control.tempGra.setWeight(curWeight);
        repaint();
    }
}
