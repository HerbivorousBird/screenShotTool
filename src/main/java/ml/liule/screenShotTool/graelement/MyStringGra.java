/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.liule.screenShotTool.graelement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import org.json.JSONObject;

/**
 *
 * @author liu
 */
public class MyStringGra extends GraElement {
    
    String str = null;
    Point position = null;
    
    public MyStringGra(String str, Color color, float weight) {
        this.color = color;
        this.str = str;
        this.type = STR;
        this.weight = weight * 8;//weight字段在此表示字号，并不表示线宽
    }
    
    public MyStringGra(JSONObject graJson) {
        this.type = STR;
        this.fromJson(graJson);
    }
    
    public void setPosition(Point position) {
        this.position = position;
    }
    
    @Override
    public void setWeight(float weight) {
        this.weight = 8 * weight;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        Font font = new Font(null, Font.PLAIN, (int) weight);
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
        g2d.drawString(str, position.x, position.y);
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Type",type);
        json.put("Color", color.getRGB());
        json.put("Weight", weight);
        JSONObject content = new JSONObject();
        content.put("String", str);
        content.put("Position", point2WKT(position));
        json.put("Content", content);
        return json;
    }
    
    @Override
    protected void fromJson(JSONObject json) {
        this.color = new Color(json.getInt("Color"));
        this.weight = (float) json.getDouble("Weight");
        JSONObject content = json.getJSONObject("Content");
        this.str = content.getString("String");
        this.position = WKT2Point(content.getString("Position"));
    }
    
}
