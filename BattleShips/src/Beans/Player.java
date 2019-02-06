/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 *
 * @author Team
 * Erstellt am 11.4.2018
 */
public class Player implements Serializable{
    
    private String name;
    private Color c;
    private transient Image schiff;
    private int leben;
    private int munition;
    private int index;
    private Position p;
    private String schiffArt;
    private double currentAngle;
    private EinheitsVektor direction;
    private double speed;
    private int width, height;
    private int rotation;
    //Neu mit Server
    private Position startPos;
    private boolean bereit;

    public Player()
    {
    }
 

    public Player(String name, Color c, Image schiff, int leben, int munition, int index, Position p, String schiffArt, double currentAngle, EinheitsVektor direction, double speed, int rotation, Position startPos, boolean bereit) 
    {
        this.name = name;
        this.c = c;
        this.schiff = schiff;
        this.leben = leben;
        this.munition = munition;
        this.index = index;
        this.p = p;
        this.schiffArt = schiffArt;
        this.currentAngle = currentAngle;
        this.direction = direction;
        this.speed = speed;
        this.width = schiff.getWidth(null);
        this.height = schiff.getHeight(null);
        this.rotation = rotation;
        this.startPos = startPos;
        this.bereit = bereit;
    }

       public int getWidth() {
        return width;
    }
       
       public void setToStartPos()
       {
           this.p = startPos;
       }
       public Rectangle getHitbox()
       {
           Rectangle hitbox = null;
           
           if((currentAngle > 45 && currentAngle < 135 || currentAngle > 225 && currentAngle < 315) || (currentAngle < -45 && currentAngle > -135 || currentAngle < -225 && currentAngle > -315))
            {
               hitbox = new Rectangle(p.getXInt() -(width/4), p.getYInt()+(width/4), height, width); 
            }
            else
            {
                hitbox = new Rectangle(p.getXInt(), p.getYInt(), width, height);
            }
          
           return hitbox;
       }

       public void setToStartPosition()
       {
           this.p = this.startPos;
       }
    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
       
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public double getSpeed() {
        return speed;
    }

    public boolean isBereit() {
        return bereit;
    }

    public void setBereit(boolean bereit) {
        this.bereit = bereit;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    public EinheitsVektor getDirection() {
        return direction;
    }

    public void setDirection(EinheitsVektor direction) {
        this.direction = direction;
    }

    public String getSchiffArt() {
        return schiffArt;
    }

    public void setSchiffArt(String schiffArt) {
        this.schiffArt = schiffArt;
    }

    public double getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(double currentAngle) {
        this.currentAngle = currentAngle;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }

    public Image getSchiff() {
        return schiff;
    }

    public void setSchiff(Image schiff) {
        this.schiff = schiff;
    }

    public int getLeben() {
        return leben;
    }

    public void setLeben(int leben) {
        this.leben = leben;
    }

    public int getMunition() {
        return munition;
    }

    public void setMunition(int munition) {
        this.munition = munition;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Position getP() {
        return p;
    }

    public void setP(Position p) {
        this.p = p;
    }

    public Position getStartPos() {
        return startPos;
    }

    public void setStartPos(Position startPos) {
        this.startPos = startPos;
        this.p = startPos;
    }

    @Override
    public String toString() {
        int n = 1;
        return "Player{" + "name=" + name + ", c=" + c + ", schiff=" + schiff + ", leben=" + leben + ", munition=" + munition + ", index=" + index + ", p=" + p + ", schiffArt=" + schiffArt + ", currentAngle=" + currentAngle + ", direction=" + direction + ", speed=" + speed + ", width=" + width + ", height=" + height + ", rotation=" + rotation + ", startPos=" + startPos + ", bereit=" + bereit + '}';
    }



    
    
}
