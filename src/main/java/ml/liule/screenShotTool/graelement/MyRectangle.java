/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.liule.screenShotTool.graelement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import org.json.JSONObject;

/**
 *
 * @author liu
 */
public class MyRectangle extends GraElement {

    private Point startP = null;
    private Point endP = null;

    /**
     *
     * @param startP
     * @param color
     * @param weight
     */
    public MyRectangle(Point startP, Color color, float weight) {
        this.startP = startP;
        this.color = color;
        this.weight = weight;
        this.type = RECT;
    }

    public MyRectangle(JSONObject json) {
        this.type = RECT;
        fromJson(json);
    }

    public void setEndP(Point endP) {
        this.endP = endP;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Type", type);
        json.put("Color", color.getRGB());
        json.put("Weight", weight);
        JSONObject content = new JSONObject();
        content.put("StartPoint", point2WKT(startP));
        content.put("EndPoint", point2WKT(endP));
        json.put("Content", content);
        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        this.color = new Color(json.getInt("Color"));
        this.weight = (float) json.getDouble("Weight");
        JSONObject content = json.getJSONObject("Content");
        this.startP = WKT2Point(content.getString("StartPoint"));
        this.endP = WKT2Point(content.getString("EndPoint"));
    }

    @Override
    public void draw(Graphics2D g2d) {
        this.setPen(g2d);
        Rectangle rec = new Rectangle();
        rec.setLocation(startP);
        rec.add(endP);
        g2d.draw(rec);
    }
}
