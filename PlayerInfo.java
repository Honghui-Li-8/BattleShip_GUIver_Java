import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;


public class PlayerInfo
{
  public String name;
  public Map map;
  public Map transitionMap;// transitionMap is the one  that been print out
  public Map mapShown;
  public Map mapCheck;
  public boolean isOtherPlayersTurn, available;
  public static boolean readIn, goRight, haveInput, noWinner;
  public int[][] ships;
  public int[] shipCountDown = {5,4,3,3,2};
  public int shipIndex;
  public int currentX, currentY, currentShipLength;
  public static String winnerName, LoserName;

  public PlayerInfo(String name)
  {
    this.name = name;
    map = new Map();
    mapShown = new Map();
    mapCheck = new Map("checkMap");
    transitionMap = new Map();
    ships = new int[5][4];
    shipIndex =  0;
    currentShipLength = 5;
    noWinner = true;
    winnerName = "";
    readIn = true;
    goRight = true;
  }

  public void chooseButton(int x, int y, JFrame f)
  {
    currentShipLength = 5 - shipIndex;
    if(currentShipLength < 3)
      currentShipLength++;
    if(readIn)
    {
      transitionMap.copyMap(map);
      available = transitionMap.checkAvailable(currentShipLength,x,y,goRight);
      if(available)
      {
        haveInput = true;
        transitionMap.markMap(currentShipLength,x,y,goRight);
        currentX = x;
        currentY = y;
      }
    }
    else//is in game
    {
      transitionMap.copyMap(mapShown);
      available = mapCheck.checkPoint(x,y);
      if(available)
      {
        haveInput = true;
        transitionMap.markMap(x,y);
        currentX = x;
        currentY = y;
      }
    }
  }

  public void readInShip()
  {
    ships[shipIndex][0] = currentX;
    ships[shipIndex][1] = currentY;

    if(goRight)
      ships[shipIndex][2] = 0;//use interher 0 for right, keep data in the same array
    else
      ships[shipIndex][2] = 1;//1 for left

    ships[shipIndex][3] = currentShipLength;

    mapCheck.markCheckMap(shipIndex,currentX,currentY,goRight);
    map.copyMap(transitionMap);
    shipIndex++;
  }

  public String otherPlayersTurn(String opName, JFrame f)// other player's name
  {
    int x = currentX;
    int y = currentY;
    String result = "";
    if(checkResult(x,y))//check it do hit or not
    {
      if(checkSink(mapCheck.getMap()[x][y]))
      {
        result = "sink";
        //showSinkShip(mapCheck.getMap()[x][y]);
        if(noShipLeft())
        {
          result = "win";
          haveWinner();
          addWinner(opName, this.name);
        }
      }
      else
      {
        mapShown.showHit(x,y);
        result = "hit";
      }
    }
    else
    {
      mapShown.showMiss(x,y);
      result = "miss";
    }

    transitionMap.copyMap(mapShown);
    return result;
  }

  public void showSinkShip(int shipIndex)
  {
    int x = ships[shipIndex][0];
    int y = ships[shipIndex][1];

    if(ships[shipIndex][2] == 0)
    {
      for(int i = 0;i<ships[shipIndex][3];i++)
        mapShown.getMap()[x][y+i]= map.getMap()[x][y+i];
    }
    else
    {
      for(int i = 0;i<ships[shipIndex][3];i++)
        mapShown.getMap()[x+i][y]= map.getMap()[x+i][y];
    }

    transitionMap.copyMap(mapShown);
  }

  public void shipCountDown(int i)//-------------------?????
  {
    shipCountDown[i]--;

    if(shipCountDown[i] == 0)
    {
     showSinkShip(i);
     //JOptionPane.showMessageDialog(f,"You just sink a Ship");
    }
  }

  public boolean checkResult(int x, int y)
  {
    String list ="abcde";
    if(!mapCheck.getMap()[x][y].equals(" "))
    {
      mapCheck.getMap()[x][y] = mapCheck.getMap()[x][y].toLowerCase();
      shipIndex = list.indexOf(mapCheck.getMap()[x][y]);
      shipCountDown(shipIndex);
      return true;
    }
    else
      return false;
  }

  public boolean checkSink(String letter)
  {
    String list = "abcde";
    shipIndex = list.indexOf(letter);

    if(shipCountDown[shipIndex] == 0)
      return true;
    else
      return false;
  }

  public boolean noShipLeft()
  {
    for(int i=0; i<5; i++)//5 ships total
    {
      if(shipCountDown[i] != 0)//if any ship do not be sink return false
        return false;
    }

    return true;
  }

  public String numOfShipLeft()
  {
    String ans = "    length: ";
    int left = 0;
    int length;
    for(int i=0;i<5;i++)
    {
      length = i+1;
      if(length<3)
        length++;

      if(shipCountDown[i] > 0)
      {
        left++;
        ans += length+" ";
      }
    }

    return "ship left: "+left+ans;
  }

  public static void addWinner(String winner, String loser)
  {
    if(winnerName.equals(""))
    {
      winnerName += winner;
      LoserName = loser;
    }
    else
    {
      winnerName = "Both "+winnerName+" and "+winner;
      LoserName = "";
    }
  }

  public static void haveWinner()
  { noWinner = false;}

  public static void resetInput()
  { haveInput = false;}

  public String getName()
  { return name;}

  public void inGame()
  {
    transitionMap.copyMap(mapShown);
  }
}
