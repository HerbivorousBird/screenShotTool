/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.liule.screenShotTool.graelement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import org.json.JSONObject;

/**
 *
 * @author liu
 */
public abstract class GraElement {

    public static final int LINE = 0;
    public static final int STR = 1;
    public static final int RECT = 2;

    protected float weight = 4;
    protected Color color = Color.BLACK;
    protected int type = -1;

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public abstract JSONObject toJson();

    protected abstract void fromJson(JSONObject json);

    public abstract void draw(Graphics2D g2d);

    protected void setPen(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(weight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(color);
    }

    protected String point2WKT(Point p) {
        return "(" + p.x + ' ' + p.y + ")";
    }

    protected Point WKT2Point(String WKT) {
        Point p = new Point();
        String rawWKT = WKT.substring(1, WKT.length() - 1);
        String[] xyStrings = rawWKT.split(" ");
        p.x = Integer.parseInt(xyStrings[0]);
        p.y = Integer.parseInt(xyStrings[1]);
        return p;
    }
}
