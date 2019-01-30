/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import Beans.Player;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author michi
 */
public class LobbyTableModel extends AbstractTableModel
{
        private List<Player> playerList = new LinkedList<>();
        private String colNames[] = {"Name","Schiff","Bereit?"};
        
        public void setPlayerList(List<Player> list)
        {
            this.playerList = list;
            
            this.fireTableDataChanged();
        }
        
        public void addPlayer(Player p )
        {
            this.playerList.add(p);
            this.fireTableDataChanged();
        }

    @Override
    public int getRowCount() {
       return playerList.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        Player p = playerList.get(rowIndex);
        
        switch(columnIndex)
        {
            case 0: return p.getName();
            case 1: return p.getSchiffArt();
            case 2: return p.isBereit();
            default: return null;
        }
    }


}
