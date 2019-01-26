/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import Beans.Player;
import java.util.LinkedList;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author michi
 */
public class SpielerListModel extends AbstractListModel
{
        private LinkedList<Player> playerList = new LinkedList<>();


    public void addPlayer(Player player) {
        if (!(playerList.contains(player))) {
            playerList.add(player);
            updateList();
        } else {
            JOptionPane.showMessageDialog(null, "so nicht");
        }
    }

    public void delPlayer(Player player) {
        if (playerList.contains(player)) {
            playerList.remove(player);
            updateList();
        } else {
            JOptionPane.showMessageDialog(null, "so nicht");
        }
    }

    @Override
    public int getSize()
    {
        return playerList.size();
    }

    @Override
    public Object getElementAt(int i)
    {
        return playerList.get(i);
    }

    public void updateList() {
        super.fireContentsChanged(this, 0, playerList.size());
    }

}
