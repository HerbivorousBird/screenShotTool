/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.liule.screenShotTool.graelement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.StringJoiner;
import org.json.JSONObject;

/**
 *
 * @author liu
 */
public class MyPolyline extends GraElement {
    
    private ArrayList<Point> points = new ArrayList<>();
    private Point rubberPoint = null;
    
    public MyPolyline(Color color, float weight) {
        this.color = color;
        this.weight = weight;
        this.type = LINE;
    }
    
    public MyPolyline(JSONObject json) {
        this.type = LINE;
        fromJson(json);
    }
    
    public void addPoint(Point p) {
        points.add(p);
        rubberPoint = null;
    }
    
    public void setRubberPoint(Point p) {
        this.rubberPoint = p;
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Type", type);
        json.put("Color", color.getRGB());
        json.put("Weight", weight);
        json.put("Content", polyline2WKT());
        return json;
    }
    
    @Override
    public void fromJson(JSONObject json) {
        this.color = new Color(json.getInt("Color"));
        this.weight = (float) json.getDouble("Weight");
        this.points = new ArrayList<>();
        String WKT = json.getString("Content");
        WKT2polyline(WKT);
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        this.setPen(g2d);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
        int ptcount = points.size();
        int[] xarr = new int[ptcount];
        int[] yarr = new int[ptcount];
        
        for (int i = 0; i < ptcount; i++) {
            Point pt = points.get(i);
            xarr[i] = (int) pt.getX();
            yarr[i] = (int) pt.getY();
        }
        g2d.drawPolyline(xarr, yarr, ptcount);
        drawRubberBand(g2d);
    }
    
    private void drawRubberBand(Graphics2D g2d) {
        if (rubberPoint == null) {
            return;
        }
        Point p1 = points.get(points.size() - 1);
        this.setPen(g2d);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
        g2d.drawLine(p1.x, p1.y, rubberPoint.x, rubberPoint.y);
    }
    
    private String polyline2WKT() {
        int pCount = points.size();
        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (int i = 0; i < pCount; i++) {
            sj.add(point2WKT(points.get(i)));
        }
        return sj.toString();
    }
    
    private void WKT2polyline(String WKT) {
        String subWKT = WKT.substring(1, WKT.length() - 1);
        String[] pointWKTs = subWKT.split(",");
        for (String wkt : pointWKTs) {
            points.add(WKT2Point(wkt));
        }
    }
    
}
