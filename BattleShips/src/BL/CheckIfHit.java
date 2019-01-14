/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BL;

import Beans.Kugel;
import Beans.Player;
import Beans.Treffer;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 *
 * @author Tobias und Michael
 * Erstellt am 6.6.2018
 * Begeistert
 */
public class CheckIfHit // Collision Detection Klasse
{

    private LinkedList<Kugel> kugelListe;
    private LinkedList<Player> schiffListe;


    public CheckIfHit(LinkedList<Kugel> kugeln, LinkedList<Player> schiffe)
    {
        kugelListe = kugeln;
        schiffListe = schiffe;

    }
    
    public boolean checkCollision()
    {
        Player p1 = schiffListe.get(0);
        Player p2 = schiffListe.get(1);

        Rectangle hitbox1 = p1.getHitbox();
        Rectangle hitbox2 = p2.getHitbox();

        //Schiffe fahren Zusammen
        if(hitbox1.intersects(hitbox2))
        {
            return true;
        }
        
        return false;
    }
    
    public Treffer checkIfHit()
    {        
        
        Player p1 = schiffListe.get(0);
        Player p2 = schiffListe.get(1);

        Rectangle hitbox1 = p1.getHitbox();
        Rectangle hitbox2 = p2.getHitbox();
        
        //Treffer mit KanonenKugel
        for (Kugel k : kugelListe) {
            
            Rectangle rectK = new Rectangle(k.getPos().getXInt(),k.getPos().getYInt(), k.getGroesse(),k.getGroesse());
            
            if(k.getPlayernumber() == 1)
            {
                if(rectK.intersects(hitbox2))
                {
                    p2.setLeben(p2.getLeben()-2);
                    Treffer t = new Treffer(2,kugelListe.indexOf(k));
                    return t;
                }
            }
            else if(k.getPlayernumber() == 2)
            {
                if(rectK.intersects(hitbox1))
                {
                    p1.setLeben(p1.getLeben()-2);
                    Treffer t = new Treffer(1,kugelListe.indexOf(k));
                    return t;
                }
            }
            
        }

        return null;
    }

}