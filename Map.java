import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;



public class Map
{
  private String[][] map;

  public Map()
  {
    map = new String[10][10];

    for(int i=0;i<10;i++)
      for(int j=0;j<10;j++)
        map[i][j] = "blank";
  }

  public Map(String checkMap)
  {
    map = new String[10][10];

    for(int i=0;i<10;i++)
      for(int j=0;j<10;j++)
        map[i][j] = " ";
  }

  public String[][] getMap()
  { return map;}


  public void copyMap(Map beenCopyMap)
  {
    String[][] beenCopy = beenCopyMap.getMap();
    for(int i=0;i<beenCopy.length;i++)
      for(int j=0;j<beenCopy[i].length;j++)
        map[i][j] = beenCopy[i][j];
  }

  public void markMap(int length, int x, int y, boolean goRight)
  {
      String[][] R = {{"R-2","R-0"},{"R-2","R-1","R-0"},{"R-2","R-1","R-1","R-0"},{"R-2","R-1","R-1","R-1","R-0"}};
      String[][] D = {{"D-0","D-2"},{"D-0","D-1","D-2"},{"D-0","D-1","D-1","D-2"},{"D-0","D-1","D-1","D-1","D-2"}};
      String[] ship;
      if(goRight)
        ship = R[length-2];
      else
        ship = D[length-2];

    if(goRight)
    {
      for(int i = 0;i<length;i++)
        map[x][y+i] = ship[i];
    }
    else
    {
      for(int i = 0;i<length;i++)
        map[x+i][y]= ship[i];
    }
  }

  public void markCheckMap(int shipIndex, int x, int y, boolean goRight)
  {
    String[] letters = {"A","B","C","D","E"};
    int length = 5 - shipIndex;
    if(length<3)
      length++;

    if(goRight)
    {
      for(int i = 0;i<length;i++)
        map[x][y+i] = letters[shipIndex];
    }
    else
    {
      for(int i = 0;i<length;i++)
      map[x+i][y]= letters[shipIndex];
    }
  }

  public boolean checkAvailable(int length, int x, int y, boolean goRight)
  {
    if(goRight)
    {
      for(int i = 0;i<length;i++)
        if(!map[x][y+i].equals("blank"))
          return false;
    }
    //gose down
    else
    {
      for(int i = 0;i<length;i++)
        if(!map[x+i][y].equals("blank"))
          return false;
    }

    return true;
  }

  public boolean checkPoint(int x,int y)
  {
    String list = "ABCDE ";// if at that point is ABCDE and blank
    //blank contain b & a
    if(list.indexOf(map[x][y]) == -1)
      return false;
    else
      return true;
  }

  public void markMap(int x,int y)
  {
    map[x][y] = "questionMark";
  }

  public void showMiss(int x,int y)
  {
    map[x][y] = "X";
  }

  public void showHit(int x, int y)
  {
    map[x][y]="O";
  }
}
