import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;


public class BattleShipGUI
{
  private JFrame f;
  private JPanel panel;
  private JPanel panelCenter;
  private JLabel l;
  private JComboBox<String> comboBox;
  private JButton[][] buttons;
  private JButton rightOrDown, enter;
  private PlayerInfo p1, p2;
  private boolean goRight, noWinner, p1Turn, haveInput, readIn, turnOver;
  private int round, currentX, currentY;
  private String result;

  public BattleShipGUI()
  {
    f = new JFrame();
    panel = new JPanel();
    panelCenter = new JPanel();                                                                                                                     //mid
    l = new JLabel("                                                                                                                  Can't believe I get it work!   \\（￣︶￣）/                                                                             ");
    comboBox = new JComboBox<String>();
    buttons = new JButton[10][10];
    rightOrDown = new JButton(new ImageIcon("R.png"));
    enter = new JButton(new ImageIcon("enter.png"));
    goRight = true;
    noWinner = false;
    p1Turn = true;
    haveInput = false;
    readIn = true;
    turnOver = false;
    round = 0;

    for(int i=0;i<10;i++)
      for(int j=0;j<10;j++)
      {
        buttons[i][j] = new JButton(new ImageIcon("blank.png"));
        panel.add(buttons[i][j]);
      }

    panel.setLayout(new GridLayout(10,10));
    panel.setSize(700,700);
    f.setSize(850,850);
    f.add(panel, BorderLayout.CENTER);
    f.setTitle("TEST");
    f.setVisible(true);
    f.add(comboBox, BorderLayout.PAGE_START);
    f.add(l, BorderLayout.PAGE_END);
    f.add(rightOrDown, BorderLayout.EAST);
    f.add(enter, BorderLayout.WEST);

    addListener();
    addListenerButtons();
    comboBox.addItem("                                                                                                       --------Click check mark to start a new game-------                                           ");
    JOptionPane.showMessageDialog(f,"This is the GUI of battle ship\n*Click check mark on left side to start a new game\n*check mark also use to enter the input\n*The swich on right side used to swich derection while read in ship positions\n*And there is a tiny label at bottom will print text (ues to show result of each move)\n*I can't make it bigger 不会弄 ╮（╯_╰）╭\n\n Have FUN! ^_^       --- Honghui");
  }

  public void chooseButton(int x, int y)
  {
    if(noWinner)
    {
    if(p1Turn)
      p1.chooseButton(x,y,f);
    else
      p2.chooseButton(x,y,f);

    haveInput = PlayerInfo.haveInput;
    if(p1Turn && haveInput)
    {
      currentX = x;
      currentY = y;
    }
    else if(haveInput)
    {
      currentX = x;
      currentY = y;
    }
    updateInfo();
    }
  }

  public void enter()
  {
    if(!noWinner)
      startNewGame();
    else if(readIn && haveInput)
    {
      if(p1Turn){
        p1.readInShip();
        if(p1.shipIndex >= 5)//p1 finish read in
        {
          p1Turn = false;
          p1.inGame();
          updateInfo();
          JOptionPane.showMessageDialog(f,p2.getName()+" place ships!\n*click check mark to enter a ship position");
        }
      }
      else{
        p2.currentX = currentX;
        p2.currentY = currentY;
        p2.readInShip();
        if(p2.shipIndex >= 5)//p2 finish read in
        {
          p1Turn = false;
          readIn = false;
          PlayerInfo.readIn = false;
          p2.inGame();
          updateInfo();
          JOptionPane.showMessageDialog(f,p1.getName()+" go first!");
        }
      }
    }
    else if(turnOver)
    {
      haveInput = false;
      p1Turn = !p1Turn;
      turnOver = false;
      round++;
    }
    else if(haveInput)// in game
    {
      if(p1Turn)
      {
        result = p1.otherPlayersTurn(p2.getName(),f);
      }
      else
      {
        result = p2.otherPlayersTurn(p1.getName(),f);
      }



      changesLable();
    }
    else//no Input
      JOptionPane.showMessageDialog(f,"No Input");

    noWinner = PlayerInfo.noWinner;
    haveInput = false;
    PlayerInfo.resetInput();
    updateInfo();
  }

  public void updateInfo()
  {
    if(readIn)
    {
      if(p1Turn)
      {
        for(int i=0;i<10;i++)
          for(int j=0;j<10;j++)
            buttons[i][j].setIcon(new ImageIcon(p1.transitionMap.getMap()[i][j]+".png"));
      }
      else
      {
        for(int i=0;i<10;i++)
          for(int j=0;j<10;j++)
            buttons[i][j].setIcon(new ImageIcon(p2.transitionMap.getMap()[i][j]+".png"));
      }
    }
    else
    {
      if(p1Turn)
      {
        for(int i=0;i<10;i++)
          for(int j=0;j<10;j++)
            buttons[i][j].setIcon(new ImageIcon(p1.transitionMap.getMap()[i][j]+".png"));
      }
      else
      {
        for(int i=0;i<10;i++)
          for(int j=0;j<10;j++)
            buttons[i][j].setIcon(new ImageIcon(p2.transitionMap.getMap()[i][j]+".png"));
      }
    }

    if(!readIn && noWinner)//in game
    {
      comboBox.removeAllItems();
      if(p1Turn)
        comboBox.addItem(p2.getName()+"'s Turn                                                                                                                  --------Round: "+round/2+"-------                                      (click to see how many ship left)");
      else
        comboBox.addItem(p1.getName()+"'s Turn                                                                                                                  --------Round: "+round/2+"-------                                      (click to see how many ship left)");
      comboBox.addItem("*"+p1.getName()+"*  ------------------"+p1.numOfShipLeft());
      comboBox.addItem("*"+p2.getName()+"*  ------------------"+p2.numOfShipLeft());
    }
    else if(!readIn && !noWinner)
    {
      comboBox.removeAllItems();
      comboBox.addItem("                                                                                                                       --------Click to review board-------                                                                           ");
      comboBox.addItem("*"+p1.getName()+"'s board");
      comboBox.addItem("*"+p2.getName()+"'s board");
    }
  }

  public void changesLable()
  {
    if(result.equals("hit"))
      l.setText("                                                                                                       You hit it, take an extra hit! (^_^)");
    else if(result.equals("sink"))
      l.setText("                                                                                              You hit it, and you sink a ship! Take an extra hit! \\(^o^)/");
    else if(result.equals("win"))
    {
      l.setText("                                                                                                    ( ＾∀＾）／ You win!!! ＼( ＾∀＾）                                    (Click check mark to start a New Game)");
      JOptionPane.showMessageDialog(f,"You win!\n*"+PlayerInfo.winnerName+" beat "+PlayerInfo.LoserName+" in "+round/2+" rounds!\n\n*(use comboBox on top to review you boards)\n*(click check mark on left to start a new game)");
      JOptionPane.showMessageDialog(f,"Hope you have fun! ^_^");
      noWinner = false;
      turnOver = true;
    }
    else if(result.equals("miss"))
    {
      turnOver = true;
      l.setText("                                                                                                    Not there?  Σ（ ° △ °|||) OK,now swich turn  ╮（╯_╰）╭                                            (click check mark)");
      JOptionPane.showMessageDialog(f,"You miss, now swich turn!\n*(click check mark on left to swich turn)");
    }
  }

  public void startNewGame()
  {
    noWinner = true;
    round = 0;
    p1Turn = true;
    haveInput = false;
    readIn = true;
    JOptionPane.showMessageDialog(f,"New Game Started!");
    p1 = new PlayerInfo(JOptionPane.showInputDialog(f,"Enter 1st player's name")+"");
    p2 = new PlayerInfo(JOptionPane.showInputDialog(f,"Enter 2nd player's name")+"");
    JOptionPane.showMessageDialog(f,p1.getName()+" place ships!\n*click check mark to enter a ship position");
    comboBox.addItem("                                                                                                                            --------Read In-------");
    updateInfo();
  }

  public void addListener()
  {
    enter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        enter();
      }
    });

    rightOrDown.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        if(readIn)
        {
          goRight = !goRight;
          PlayerInfo.goRight = goRight;
          if(goRight)
            rightOrDown.setIcon(new ImageIcon("R.png"));
          else
            rightOrDown.setIcon(new ImageIcon("D.png"));

          chooseButton(currentX, currentY);
        }
        else if(turnOver)
        {
          turnOver = false;
          p1Turn = !p1Turn;
          haveInput = false;
          updateInfo();
        }

      }
    });

    comboBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        int currentPlayer = comboBox.getSelectedIndex();

        if(!readIn && !noWinner)
        {
          if(currentPlayer == 1)
            p1Turn = true;
          else if(currentPlayer ==  2)
            p1Turn = false;
          updateInfo();
        }
      }
    });
  }

  public void addListenerButtons()
  {
    buttons[0][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,0);
      }
    });

    buttons[0][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,1);
      }
    });

    buttons[0][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,2);
      }
    });

    buttons[0][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,3);
      }
    });

    buttons[0][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,4);
      }
    });

    buttons[0][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,5);
      }
    });

    buttons[0][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,6);
      }
    });

    buttons[0][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,7);
      }
    });

    buttons[0][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,8);
      }
    });

    buttons[0][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(0,9);
      }
    });

    buttons[1][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,0);
      }
    });

    buttons[1][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,1);
      }
    });

    buttons[1][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,2);
      }
    });

    buttons[1][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,3);
      }
    });

    buttons[1][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,4);
      }
    });

    buttons[1][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,5);
      }
    });

    buttons[1][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,6);
      }
    });

    buttons[1][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,7);
      }
    });

    buttons[1][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,8);
      }
    });

    buttons[1][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(1,9);
      }
    });

    buttons[2][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,0);
      }
    });

    buttons[2][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,1);
      }
    });

    buttons[2][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,2);
      }
    });

    buttons[2][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,3);
      }
    });

    buttons[2][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,4);
      }
    });

    buttons[2][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,5);
      }
    });

    buttons[2][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,6);
      }
    });

    buttons[2][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,7);
      }
    });

    buttons[2][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,8);
      }
    });

    buttons[2][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(2,9);
      }
    });

    buttons[3][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,0);
      }
    });

    buttons[3][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,1);
      }
    });

    buttons[3][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,2);
      }
    });

    buttons[3][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,3);
      }
    });

    buttons[3][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,4);
      }
    });

    buttons[3][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,5);
      }
    });

    buttons[3][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,6);
      }
    });

    buttons[3][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,7);
      }
    });

    buttons[3][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,8);
      }
    });

    buttons[3][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(3,9);
      }
    });

    buttons[4][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,0);
      }
    });

    buttons[4][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,1);
      }
    });

    buttons[4][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,2);
      }
    });

    buttons[4][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,3);
      }
    });

    buttons[4][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,4);
      }
    });

    buttons[4][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,5);
      }
    });

    buttons[4][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,6);
      }
    });

    buttons[4][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,7);
      }
    });

    buttons[4][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,8);
      }
    });

    buttons[4][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(4,9);
      }
    });

    buttons[5][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,0);
      }
    });

    buttons[5][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,1);
      }
    });

    buttons[5][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,2);
      }
    });

    buttons[5][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,3);
      }
    });

    buttons[5][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,4);
      }
    });

    buttons[5][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,5);
      }
    });

    buttons[5][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,6);
      }
    });

    buttons[5][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,7);
      }
    });

    buttons[5][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,8);
      }
    });

    buttons[5][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(5,9);
      }
    });

    buttons[6][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,0);
      }
    });

    buttons[6][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,1);
      }
    });

    buttons[6][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,2);
      }
    });

    buttons[6][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,3);
      }
    });

    buttons[6][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,4);
      }
    });

    buttons[6][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,5);
      }
    });

    buttons[6][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,6);
      }
    });

    buttons[6][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,7);
      }
    });

    buttons[6][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,8);
      }
    });

    buttons[6][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(6,9);
      }
    });

    buttons[7][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,0);
      }
    });

    buttons[7][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,1);
      }
    });

    buttons[7][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,2);
      }
    });

    buttons[7][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,3);
      }
    });

    buttons[7][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,4);
      }
    });

    buttons[7][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,5);
      }
    });

    buttons[7][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,6);
      }
    });

    buttons[7][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,7);
      }
    });

    buttons[7][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,8);
      }
    });

    buttons[7][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(7,9);
      }
    });

    buttons[8][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,0);
      }
    });

    buttons[8][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,1);
      }
    });

    buttons[8][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,2);
      }
    });

    buttons[8][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,3);
      }
    });

    buttons[8][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,4);
      }
    });

    buttons[8][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,5);
      }
    });

    buttons[8][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,6);
      }
    });

    buttons[8][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,7);
      }
    });

    buttons[8][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,8);
      }
    });

    buttons[8][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(8,9);
      }
    });

    buttons[9][0].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,0);
      }
    });

    buttons[9][1].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,1);
      }
    });

    buttons[9][2].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,2);
      }
    });

    buttons[9][3].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,3);
      }
    });

    buttons[9][4].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,4);
      }
    });

    buttons[9][5].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,5);
      }
    });

    buttons[9][6].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,6);
      }
    });

    buttons[9][7].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,7);
      }
    });

    buttons[9][8].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,8);
      }
    });

    buttons[9][9].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        chooseButton(9,9);
      }
    });


  }
}
