/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.liule.screenShotTool.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author liu
 */
public class ChosenRectangle extends Rectangle {

    public static final int OUT_E = 1;
    public static final int OUT_NE = 2;
    public static final int OUT_N = 3;
    public static final int OUT_NW = 4;
    public static final int OUT_W = 5;
    public static final int OUT_SW = 6;
    public static final int OUT_S = 7;
    public static final int OUT_SE = 8;
    public static final int IN = 0;

    /**
     *
     */
    public ChosenRectangle() {
        super();
    }

    public ChosenRectangle(int i, int i0, int i1, int i2) {
        super(i, i0, i1, i2);
    }

    public void panInRec(Point startPoint, Point endPoint, Rectangle rec) {
        if (!rec.contains(this)) {
            return;
        }
        x += (endPoint.x - startPoint.x);
        y += (endPoint.y - startPoint.y);
        if (x < rec.x) {
            x = rec.x;
        } else if (x + width > rec.x + rec.width) {
            x = rec.x + rec.width - width;
        }
        if (y < rec.y) {
            y = rec.y;
        } else if (y + height > rec.y + rec.height) {
            y = rec.y + rec.height - height;
        }
    }

    /**
     *
     * 该函数用于根据两个点和一个方向调整矩形大小
     * @param start 起点与终点配合用于计算偏移量
     * @param end 与起点配合用于计算偏移量
     * @param dir 从哪个方向进行调整
     */
    public void reSizeBy(Point start, Point end, int dir) {
        if (dir == IN) {
            return;
        }
        int xOff = end.x - start.x;
        int yOff = end.y - start.y;
        switch (dir) {
            case OUT_E:
                width += xOff;
                break;
            case OUT_SE:
                width += xOff;
                height += yOff;
                break;
            case OUT_S:
                height += yOff;
                break;
            case OUT_SW:
                width -= xOff;
                height += yOff;
                x += xOff;
                break;
            case OUT_W:
                width -= xOff;
                x += xOff;
                break;
            case OUT_NW:
                width -= xOff;
                height -= yOff;
                x += xOff;
                y += yOff;
                break;
            case OUT_N:
                height -= yOff;
                y += yOff;
                break;
            case OUT_NE:
                width += xOff;
                height -= yOff;
                y += yOff;
                break;
            default:
                break;
        }
        if (width < 1) {
            if (xOff > 0) {
                x += width - 1;
            }
            width = 1;
        }
        if (height < 1) {
            if (yOff > 0) {
                y += height - 1;
            }
            height = 1;
        }
    }
    
    public void reSizeBy(Point p){
        if(p.x>x){
            width=p.x-x;
        }else{
            width=x-p.x;
            x=p.x;
        }
        if(p.y>y){
            height=p.y-y;
        }else{
            height=y-p.y;
            y=p.y;
        }
        width=width==0?1:width;
        height=height==0?1:height;
    }

    @Override
    public boolean contains(Point p) {
        return outcode(p) == IN; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "(" + x + ',' + y + ',' + width + ',' + height + ")";
    }

    public void fromString(String str) {
        String substr = str.substring(1, str.length() - 1);
        String[] xywhStrings = substr.split(",");
        x = Integer.parseInt(xywhStrings[0]);
        y = Integer.parseInt(xywhStrings[1]);
        width = Integer.parseInt(xywhStrings[2]);
        height = Integer.parseInt(xywhStrings[3]);
    }

    public int outcode(Point p) {
        int dir = IN;
        int x2 = x + width;
        int y2 = y + height;
        if (p.x >= x2 - 1) {
            if (p.y >= y2 - 1) {
                dir = OUT_SE;
            } else if (p.y <= y) {
                dir = OUT_NE;
            } else {
                dir = OUT_E;
            }
        } else if (p.x <= x) {
            if (p.y >= y2 - 1) {
                dir = OUT_SW;
            } else if (p.y <= y) {
                dir = OUT_NW;
            } else {
                dir = OUT_W;
            }
        } else {
            if (p.y >= y2 - 1) {
                dir = OUT_S;
            } else if (p.y <= y) {
                dir = OUT_N;
            } else {
                dir = IN;
            }
        }
        return dir;
    }

    public Cursor getDirectionCursor(Point endPoint) {
        Cursor cursor = null;
        switch (this.outcode(endPoint)) {
            case ChosenRectangle.OUT_N:
                cursor = new Cursor(Cursor.N_RESIZE_CURSOR);
                break;
            case ChosenRectangle.OUT_S:
                cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
                break;
            case ChosenRectangle.OUT_W:
                cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
                break;
            case ChosenRectangle.OUT_E:
                cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
                break;
            case ChosenRectangle.OUT_SW:
                cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
                break;
            case ChosenRectangle.OUT_SE:
                cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
                break;
            case ChosenRectangle.OUT_NE:
                cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
                break;
            case ChosenRectangle.OUT_NW:
                cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
                break;
            default:
                cursor = Cursor.getDefaultCursor();
                break;
        }
        return cursor;
    }
}
