package game;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import game.YahtzeeFrame.getPlayerListener;

import javax.swing.Timer;

public class YahtzeeFrame extends JFrame {
	private ImagePanel diceLabel1, diceLabel2,diceLabel3,diceLabel4,diceLabel5;
	private Timer timer;
	private int delay=100;
		
	private JLabel name;
	private JTextField nameTxt;
	
	private  JLabel txtAces, txtTwos, txtThrees, txtFours, txtFives,txtSixs;
	private JButton acesBtn, twosBtn,threesBtn,foursBtn,fivesBtn,sixsBtn;
   
   
    private JLabel txtThreek, txtFourk, txtFull, txtSmall, txtLarge,txtYhz,txtChance;
	private JButton threekBtn, fourkBtn,fullBtn,smallsBtn,largesBtn,yhzBtn,chanceBtn;
    
   
    private JLabel upperSec,lowerSec;
    
    private JLabel upperScore, upperBonus, upperTotal;
    private JLabel upScrTxt,upBnsTxt,upTotalTxt;
    
    private JLabel yhzBns,lowerTotal, allTotal;
    private JLabel yhzBnsTxt,loTotalTxt,allTotalTxt;
    
    private JLabel Turn,Roll;
    private JButton roll;
    private JCheckBox cb1,cb2,cb3,cb4,cb5;
    
    private int count = (int)(10*Math.random())+5;
    private int turnNum = 1;
    private int rollNum =0;
    //save the number of one dice
    private List<Integer> nums;
    private int num1=1,num2=1,num3=1,num4=1,num5=1;
    //save the sum of five dice
    private int sum=0;
    //save upper and lower score
    private int upperScr=0,lowerScr=0;
    //let each button can only be pressed once
    private List<Boolean> able_flag;
    //only three rolls in one turn
    private boolean turn_flag=false;
    
    // build database
    private Connection conn;
    private PreparedStatement insertStatement;
    
    private player_info player= new player_info();
    private JTextArea playerLists;
	private JTextField inputPlayerName;
	private JButton getPlayer;
	private Border line;
    
	
	public YahtzeeFrame() {	
	/*initialize dice array*/
		nums = new ArrayList<Integer>();
		nums.add(0,num1);
		nums.add(1,num2);
		nums.add(2,num3);
		nums.add(3,num4);
		nums.add(4,num5);
	/*initialize button press ability*/
		able_flag = new ArrayList<Boolean>();
		for(int i=0;i<13;i++) {
			able_flag.add(i,true);
		}	
	/*menu bar setting*/
		JMenuBar mb = new JMenuBar();
		JMenu game = new JMenu("Game");
		game.add(createSave());
		game.add(createLoad());
		game.add(createExit());
		mb.add(game);
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.LIGHT_GRAY);
		this.setSize(500,700);
		setLayout(new BorderLayout());
		this.setJMenuBar(mb);
		
	/*right part*/
		diceLabel1= new ImagePanel("die1.png");
		diceLabel2= new ImagePanel("die1.png");
		diceLabel3= new ImagePanel("die1.png");
		diceLabel4= new ImagePanel("die1.png");
		diceLabel5= new ImagePanel("die1.png");
		
	
		diceLabel1.scaleImage(0.5);
		diceLabel2.scaleImage(0.5);
		diceLabel3.scaleImage(0.5);
		diceLabel4.scaleImage(0.5);
		diceLabel5.scaleImage(0.5);
		
		
		cb1 = new JCheckBox("Keep",false);
		cb2 = new JCheckBox("Keep",false);
		cb3 = new JCheckBox("Keep",false);
		cb4 = new JCheckBox("Keep",false);
		cb5 = new JCheckBox("Keep",false);
	
		
		JPanel dicePanel1 = new JPanel();
		JPanel dicePanel2 = new JPanel();
		JPanel dicePanel3 = new JPanel();
		JPanel dicePanel4 = new JPanel();
		JPanel dicePanel5 = new JPanel();
		
		
		
		JPanel dicePanel = new JPanel(new GridLayout(6,1));
		dicePanel.setSize(100,200);
		
		dicePanel1.add(diceLabel1);
		dicePanel1.add(cb1);
		
		dicePanel2.add(diceLabel2,BorderLayout.NORTH);
		dicePanel2.add(cb2,BorderLayout.SOUTH);
		
		dicePanel3.add(diceLabel3);
		dicePanel3.add(cb3);
		
		dicePanel4.add(diceLabel4);
		dicePanel4.add(cb4);
		
		dicePanel5.add(diceLabel5);
		dicePanel5.add(cb5);
	
		dicePanel.add(dicePanel1);
		dicePanel.add(dicePanel2);
		dicePanel.add(dicePanel3);
		dicePanel.add(dicePanel4);
		dicePanel.add(dicePanel5);
			
		Turn = new JLabel("Turn:1");
		Roll = new JLabel("Roll:0");
		roll = new JButton("Roll");
	
		
		JPanel curStatus = new JPanel(new GridLayout(3,1));
		curStatus.add(Turn);
		curStatus.add(Roll);
		curStatus.add(roll);
		
		dicePanel.add(curStatus);
		
	/*North part: input name*/
		name = new JLabel("Player Name:");
		nameTxt  =new JTextField(20);
		JPanel pnlInput = new JPanel();
		pnlInput.add(name);
		pnlInput.add(nameTxt);
		
	/*-----finish-----*/
		
	/*left upper part*/
		Border line = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		
		upperSec = new JLabel("Upper Section");
		JPanel upperPnl = new JPanel();
		upperPnl.add(upperSec);
		
		JPanel upperChart = new JPanel(new GridLayout(1,2));
		JPanel upBtnPanel = new JPanel(new GridLayout(9,1));
		JPanel upScrPanel = new JPanel(new GridLayout(9,1));
		
		initialTxt();
	    
		upScrPanel.add(txtAces);
		upScrPanel.add(txtTwos);
		upScrPanel.add(txtThrees);
		upScrPanel.add(txtFours);
		upScrPanel.add(txtFives);
		upScrPanel.add(txtSixs);
		upScrPanel.add(upScrTxt);		
		upScrPanel.add(upBnsTxt);		
		upScrPanel.add(upTotalTxt); 
		
		acesBtn = new JButton("Aces");
		twosBtn = new JButton("Twos");
		threesBtn = new JButton("Threes");
		foursBtn = new JButton("Fours");
		fivesBtn = new JButton("Fives");
		sixsBtn = new JButton("Sixs");
		upperScore = new JLabel("Score Subtotal");
		upperBonus = new JLabel("Bonus");
		upperTotal = new JLabel("Grand Total");
		
		upBtnPanel.add(acesBtn);
		upBtnPanel.add(twosBtn);
		upBtnPanel.add(threesBtn);
		upBtnPanel.add(foursBtn);
		upBtnPanel.add(fivesBtn);
		upBtnPanel.add(sixsBtn);
		upBtnPanel.add(upperScore);
		upBtnPanel.add(upperBonus);
		upBtnPanel.add(upperTotal);
		
		
		
		upperChart.add(upBtnPanel);
		upperChart.add(upScrPanel);
		
		JPanel upper = new JPanel(new BorderLayout());
		upper.add(upperPnl,BorderLayout.BEFORE_LINE_BEGINS);
		upper.add(upperChart,BorderLayout.SOUTH);
		upper.setBorder(line);
			
	/*left lower part */
		lowerSec = new JLabel("Lower Section");
		JPanel lowerPnl = new JPanel();
		lowerPnl.add(lowerSec);
		
		JPanel lowerChart = new JPanel(new GridLayout(1,2));
		JPanel loBtnPanel = new JPanel(new GridLayout(10,1));
		JPanel loScrPanel = new JPanel(new GridLayout(10,1));
		
		

		loScrPanel.add(txtThreek);
		loScrPanel.add(txtFourk);
		loScrPanel.add(txtFull);
		loScrPanel.add(txtSmall);
		loScrPanel.add(txtLarge);
		loScrPanel.add(txtYhz);
		loScrPanel.add(txtChance);
		loScrPanel.add(yhzBnsTxt);
		loScrPanel.add(loTotalTxt);
		loScrPanel.add(allTotalTxt);

		threekBtn = new JButton("3 of a kind");
		fourkBtn = new JButton("4 of a kind");
		fullBtn = new JButton("Full House");
		smallsBtn = new JButton("Small Straight");
		largesBtn = new JButton("Large Straight");
		yhzBtn = new JButton("Yahtzee");
		chanceBtn = new JButton("Chance");
		yhzBns = new JLabel("Yahtzee Bonus");
		lowerTotal = new JLabel("Total of lower section:");
		allTotal = new JLabel("Grand Total:");
		
		loBtnPanel.add(threekBtn);
		loBtnPanel.add(fourkBtn);
		loBtnPanel.add(fullBtn);
		loBtnPanel.add(smallsBtn);
		loBtnPanel.add(largesBtn);
		loBtnPanel.add(yhzBtn);
		loBtnPanel.add(chanceBtn);
		loBtnPanel.add(yhzBns);
		loBtnPanel.add(lowerTotal);
		loBtnPanel.add(allTotal);
		

		

		lowerChart.add(loBtnPanel,BorderLayout.WEST);
		lowerChart.add(loScrPanel,BorderLayout.EAST);
		
		JPanel lower = new JPanel(new BorderLayout());
		lower.add(lowerPnl,BorderLayout.BEFORE_LINE_BEGINS);
		lower.add(lowerChart,BorderLayout.SOUTH);
		lower.setBorder(line);
	/*--left lower part finish--*/
		
		JPanel left = new JPanel(new GridLayout(2,1));
		left.add(upper);
		left.add(lower);
		
		add(pnlInput,BorderLayout.NORTH);	
		add(left,BorderLayout.WEST);
		add(dicePanel,BorderLayout.EAST);
	
  /*roll dice button setting*/	
		timer = new Timer(delay,action);
		roll.addActionListener(new MouseEvent());
/*---determine section final score---*/
		txtAces.addPropertyChangeListener(new action1());
		txtTwos.addPropertyChangeListener(new action1());
		txtThrees.addPropertyChangeListener(new action1());
		txtFours.addPropertyChangeListener(new action1());
		txtFives.addPropertyChangeListener(new action1());
		txtSixs.addPropertyChangeListener(new action1());
		
		txtThreek.addPropertyChangeListener(new action1());
		txtFourk.addPropertyChangeListener(new action1());
		txtFull.addPropertyChangeListener(new action1());
		txtSmall.addPropertyChangeListener(new action1());
		txtLarge.addPropertyChangeListener(new action1());
		txtYhz.addPropertyChangeListener(new action1());
		txtChance.addPropertyChangeListener(new action1());
		yhzBnsTxt.addPropertyChangeListener(new action1());	
/*---finish ---*/
		
/*---input the score to database---*/
	//	allTotalTxt.addPropertyChangeListener(new action2());

/*click the button, input sum of dice*/
		
		acesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(0)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
								
					System.out.println("coming to button1");
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					turn_flag = true;
					Roll.setText("Roll: "+String.valueOf(0));
					int count=0;
					for(int i=0;i<5;i++) {
						if(nums.get(i)==1) {
							count+=1;
						}
					}				
					txtAces.setText(String.valueOf(count));
					player.setacesTxt(count);
					upperScr+=count;
					able_flag.set(0,false);
					player.setableflag(0,false);
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
				}
			}	
		});
		twosBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(1)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					
					System.out.println("coming to button2");
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					int count=0;
					for(int i=0;i<5;i++) {
						if(nums.get(i)==2) {
							count+=1;
						}
					}				
					txtTwos.setText(String.valueOf(count*2));
					player.settwosTxt(count*2);
					upperScr+=(count*2);
					able_flag.set(1,false);
					player.setableflag(1, false);
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
				}
			}
		});
		threesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(2)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button3");
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					int count=0;
					for(int i=0;i<5;i++) {
						if(nums.get(i)==3) {
							count+=1;
						}
					}				
					txtThrees.setText(String.valueOf(count*3));
					player.setthreesTxt(count*3);
					upperScr+=(count*3);
					able_flag.set(2,false);
					player.setableflag(2,false);
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
				}
			}	
		});
		foursBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(3)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button4");
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					int count=0;
					for(int i=0;i<5;i++) {
						if(nums.get(i)==4) {
							count+=1;
						}
					}				
					txtFours.setText(String.valueOf(count*4));
					player.setfoursTxt(count*4);
					upperScr+=(count*4);
					able_flag.set(3,false);
					player.setableflag(3,false);
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
				}
			}	
		});
		fivesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(4)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button5");
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					int count=0;
					for(int i=0;i<5;i++) {
						if(nums.get(i)==5) {
							count+=1;
						}
					}				
					txtFives.setText(String.valueOf(count*5));
					player.setfivesTxt(count*5);
					upperScr+=(count*5);
					able_flag.set(4,false);
					player.setableflag(4,false);
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
				}
			}	
		});
		sixsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(5)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button6");
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
					int count=0;
					for(int i=0;i<5;i++) {
						if(nums.get(i)==6) {
							count+=1;
						}
					}				
					txtSixs.setText(String.valueOf(count*6));
					player.setsixsTxt(count*6);
					upperScr+=(count*6);
					able_flag.set(5,false);
					player.setableflag(5,false);
				}
			}	
		});
		threekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(6)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button7");
					able_flag.set(6,false);
					player.setableflag(6,false);
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
					int count=0;
					HashMap<Integer,Integer> map= new HashMap<Integer,Integer>();
					for(int i :nums) {
						if(map.containsKey(i)) {
							map.put(i, map.get(i)+1);
						}
						else {
							map.put(i, 1);
						}
					}
					boolean flag=false;
					for(int i:nums) {
						if(map.get(i)>=3) {
							flag = true;
							break;
						}
					}
					if (flag) {
						txtThreek.setText(String.valueOf(sum));
						player.setthreekTxt(sum);
						lowerScr+=sum;
					}
					else {
						txtThreek.setText(String.valueOf(0));
						player.setthreekTxt(sum);
					}
				}
				
				
					
			}	
		});
		fourkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(7)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button8");
					able_flag.set(7,false);
					player.setableflag(7,false);
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
					HashMap<Integer,Integer> map= new HashMap<Integer,Integer>();
					for(int i :nums) {
						if(map.containsKey(i)) {
							map.put(i, map.get(i)+1);
						}
						else {
							map.put(i, 1);
						}
					}
					boolean flag=false;
					for(int i:nums) {
						if(map.get(i)>=4) {
							flag = true;
							break;
						}
					}
					if (flag) {
						txtFourk.setText(String.valueOf(sum));
						player.setfourkTxt(sum);
						lowerScr+=sum;
						
					}
					else {
						txtFourk.setText(String.valueOf(0));
						player.setfourkTxt(sum);
					}
				}
				
				
			}
		});
		fullBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(8)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button9");
					able_flag.set(8,false);
					player.setableflag(8,false);
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
					int count=0;
					HashMap<Integer,Integer> map= new HashMap<Integer,Integer>();
					for(int i :nums) {
						if(map.containsKey(i)) {
							map.put(i, map.get(i)+1);
						}
						else {
							map.put(i, 1);
						}
					}
					boolean flag=false;
					if(map.size()!=2) {
						flag = false;
					}
					else {	
						List<Integer> value = new ArrayList(map.keySet());
						System.out.println(value);
						if(map.get(value.get(0))==3 || map.get(value.get(1))==3) {
							if(map.get(value.get(0))==2 ||map.get(value.get(1))==2) {
								flag=true;
							}
						}
					}
					if (flag) {
						txtFull.setText(String.valueOf(25));
						player.setfullTxt(25);
						lowerScr+=sum;
					}
					else {
						txtFull.setText(String.valueOf(0));
						player.setfullTxt(0);
					}
				}
				
			
			}	
		});
		smallsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(9)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button10");
					able_flag.set(9,false);
					player.setableflag(9,false);
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
					HashMap<Integer,Integer> map= new HashMap<Integer,Integer>();
					for(int i :nums) {
						if(map.containsKey(i)) {
							map.put(i, map.get(i)+1);
						}
						else {
							map.put(i, 1);
						}
					}
					txtSmall.setText(String.valueOf(0));
					player.setsmallTxt(0);
					if(map.size()>=4) {
						List<Integer> value = new ArrayList(map.keySet());
						if(value.contains(3)&&value.contains(4)) {
							if(value.contains(1)&&value.contains(2)
									||value.contains(2)&&value.contains(5)
									||value.contains(5)&&value.contains(6)) {
								txtSmall.setText(String.valueOf(30));
								player.setsmallTxt(30);
								lowerScr+=30;
							}
						}	
					}
				}
				
				
			}
		});
		largesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(10)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button11");
					able_flag.set(10,false);
					player.setableflag(10,false);
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
					if(nums.contains(1)&&nums.contains(2)&&nums.contains(3)
							&&nums.contains(4)&&nums.contains(5)
							||nums.contains(6)&&nums.contains(2)&&nums.contains(3)
							&&nums.contains(4)&&nums.contains(5)) {
						txtLarge.setText(String.valueOf(40));
						player.setlargeTxt(40);
						lowerScr+=40;
					}
					else {
						txtLarge.setText(String.valueOf(0));
						player.setlargeTxt(0);
					}
				}
				
			}	
		});
		yhzBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(11)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button12");
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
					able_flag.set(11, false);
					player.setableflag(11,false);
					boolean flag=true;
					Integer j = nums.get(0);
					for(Integer i:nums) {
						if(!i.equals(j)) {
							flag=false;
							break;
						}	
					}
					if(flag) {
						txtYhz.setText(String.valueOf(50));
						player.setyhzTxt(50);
						lowerScr+=50;
					}
					else {
						txtYhz.setText(String.valueOf(0));
						player.setyhzTxt(0);
					}
				}
				
				
			}	
		});
		chanceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(able_flag.get(12)&&!turn_flag&&!(turnNum==1&&rollNum==0)) {
					cb1.setSelected(false);
					cb2.setSelected(false);
					cb3.setSelected(false);
					cb4.setSelected(false);
					cb5.setSelected(false);
					System.out.println("coming to button13");
					count = (int)(10*Math.random())+5;
					rollNum=0;
					player.setrollNum(0);
					Roll.setText("Roll: "+String.valueOf(0));
					turn_flag = true;
					turnNum+=1;
					player.setturnNum(turnNum);
					Turn.setText("Turn:"+String.valueOf(turnNum));
					txtChance.setText(String.valueOf(sum));
					player.setchanceTxt(sum);
					lowerScr+=sum;
					able_flag.set(12,false);
					player.setableflag(12,false);
				}
				
				
			}	
		});
		
		this.setVisible(true);
	}
	
	public class action1 implements PropertyChangeListener{

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			if(!(txtAces.getText().equals("")
					||txtTwos.getText().equals("")
					||txtThrees.getText().equals("")
					||txtFours.getText().equals("")
					||txtFives.getText().equals("")
					||txtSixs.getText().equals(""))) {
				upScrTxt.setText(String.valueOf(upperScr));
				if(upperScr>=63) {
					upperScr+=35;
					upBnsTxt.setText(String.valueOf(35));
				}	
				else {
					upBnsTxt.setText(String.valueOf(0));
				}
				upTotalTxt.setText(String.valueOf(upperScr));
			}
			if(!(txtThreek.getText().equals("")
					||txtFourk.getText().equals("")
					||txtFull.getText().equals("")
					||txtSmall.getText().equals("")
					||txtLarge.getText().equals("")
					||txtYhz.getText().equals("")
					||txtChance.getText().equals("")
					||upTotalTxt.getText().equals(""))) {
				yhzBnsTxt.setText("0");
				player.setyhzBns(0);
				loTotalTxt.setText(String.valueOf(lowerScr));
				allTotalTxt.setText(String.valueOf(upperScr+lowerScr));
				Turn.setText("Turn:"+String.valueOf(0));
				}
		}
	}
	/*
	public class action2 implements PropertyChangeListener{

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			
			try {
				insertStatement.setString(1, nameTxt.getText());
				insertStatement.setString(2, txtAces.getText());
				insertStatement.setString(3, txtTwos.getText());
				insertStatement.setString(4, txtThrees.getText());
				insertStatement.setString(5, txtFours.getText());
				insertStatement.setString(6, txtFives.getText());
				insertStatement.setString(7, txtSixs.getText());
				insertStatement.setString(8, txtThreek.getText());
				insertStatement.setString(9, txtFourk.getText());
				insertStatement.setString(10, txtFull.getText());
				insertStatement.setString(11, txtSmall.getText());
				insertStatement.setString(12, txtLarge.getText());
				insertStatement.setString(13, txtYhz.getText());
				insertStatement.setString(14, txtChance.getText());
				insertStatement.setString(15, upBnsTxt.getText());
				insertStatement.setString(16, yhzBnsTxt.getText());
				insertStatement.setString(17, allTotalTxt.getText());
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	public void Database() {
		//database connection
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:yahtzee.db");
			insertStatement = conn.prepareStatement("Insert Into People(name,aces,twos,threes,fours,fives,sixs,threek,fourk,full,small,large,yhz,chance,upbns,yhzbns,total)"
			+"Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		}catch (SQLException e) {
			System.err.println("Connection error: " + e);
			System.exit(1);
		}
	
		
		
	}*/
	public class MouseEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(rollNum<3) {
				timer.start();
			}
		}
	}
	ActionListener action = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(turnNum==14) {
				if (yhzBnsTxt.getText().equals("")) {
					yhzBnsTxt.setText(String.valueOf(0));
					player.setyhzBns(0);
					
					
					}
			}
			if(turn_flag||rollNum<3&&turnNum<14) {
				if (count == 0) {
					sum = num1+num2+num3+num4+num5;
					nums.set(0,num1);
					nums.set(1,num2);
					nums.set(2,num3);
					nums.set(3,num4);
					nums.set(4,num5);
					//whether we can add yha bonus
					if(!txtYhz.getText().equals("")
							&&!txtYhz.getText().equals("0")
							&&!txtAces.getText().equals("")
							&&!txtTwos.getText().equals("")
							&&!txtThrees.getText().equals("")
							&&!txtFours.getText().equals("")
							&&!txtSixs.getText().equals("")) {
							boolean flag=true;
							Integer j = nums.get(0);
							for(Integer i:nums) {
								if(!i.equals(j)) {
									flag=false;
									break;
								}	
							}
							if(flag) {
								yhzBnsAdd();
								lowerScr+=100;
							}
						}
				/*---finish yhz bonus----*/
					count = (int)(10*Math.random())+5;	
					rollNum++;
					player.setrollNum(rollNum);
					Roll.setText("Roll: "+rollNum);
					turn_flag = false;
					timer.stop();
				}
				else {
					//each turn's beginning, user is unable to selected.
					if(rollNum==0) {
						cb1.setSelected(false);
						cb2.setSelected(false);
						cb3.setSelected(false);
						cb4.setSelected(false);
						cb5.setSelected(false);
					}
					if(!cb1.isSelected()){
						num1 = (int)(6*Math.random())+1;
						diceLabel1.setImage("die"+num1+".png");
						diceLabel1.scaleImage(0.5);				
					}
					if(!cb2.isSelected()){
						num2 = (int)(6*Math.random())+1;
						diceLabel2.setImage("die"+num2+".png");
						diceLabel2.scaleImage(0.5);					
					}
					if(!cb3.isSelected()){
						num3 = (int)(6*Math.random())+1;
						diceLabel3.setImage("die"+num3+".png");
						diceLabel3.scaleImage(0.5);					
					}
					if(!cb4.isSelected()){
						num4 = (int)(6*Math.random())+1;
						diceLabel4.setImage("die"+num4+".png");
						diceLabel4.scaleImage(0.5);					
					}
					if(!cb5.isSelected()){
						num5 = (int)(6*Math.random())+1;
						diceLabel5.setImage("die"+num5+".png");
						diceLabel5.scaleImage(0.5);	
					}
					count--;	
				}
			}
		}
	};
	public void yhzBnsAdd() {
		yhzBnsTxt.setText(String.valueOf(100));
		player.setyhzBns(100);
	}
/*for server
	public String getAces() {
		return this.txtAces.getText();
	}
	public String getTwos() {
		return this.txtTwos.getText();
	}
	public String getThrees() {
		return this.txtThrees.getText();
	}
	public String getFours() {
		return this.txtFours.getText();
	}
	public String getFives() {
		return this.txtFives.getText();
	}
	public String getSixs() {
		return this.txtSixs.getText();
	}
	public String getThreek() {
		return this.txtThreek.getText();
	}
	public String getFourk() {
		return this.txtFourk.getText();
	}
	public String getFull() {
		return this.txtFull.getText();
	}
	public String getSmall() {
		return this.txtSmall.getText();
	}
	public String getLarge() {
		return this.txtLarge.getText();
	}
	public String getYhz() {
		return this.txtYhz.getText();
	}
	public String getChance() {
		return this.txtChance.getText();
	}
	public String getUpBns() {
		return this.upBnsTxt.getText();
	}
	
	public String getYhzBns() {
		return this.yhzBnsTxt.getText();
	}
	public String getTotal() {
		return this.allTotalTxt.getText();
	}
*/
	private JMenuItem createSave() {
		JMenuItem item = new JMenuItem("Save Game");
		class SaveGameListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				try {
					if(nameTxt.getText().equals("")) {
						createMessageBox("Please enter your name:)");
						System.out.print("snkcfjd");
					}
					else {
						Socket socket = new Socket("localhost",8000);
						System.out.print("snkcfjd");
						player.setName(nameTxt.getText());
						ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
						player_info toServPlayer = new player_info();
						toServPlayer = player;
						toServer.writeObject(toServPlayer);
						
						
						System.out.print("snkcfjd");
						
					}
				}catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		item.addActionListener(new SaveGameListener());
		return item;
	}
	private JMenuItem createLoad() {
		JMenuItem item = new JMenuItem("Load Game");
		class LoadGameListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {			
				
				loadList();
				
			}
			
		}
		item.addActionListener(new LoadGameListener());
		return item;
	}
	private JMenuItem createExit() {
		JMenuItem item = new JMenuItem("Exit");
		class ExitItemListener implements ActionListener
	      {
	         public void actionPerformed(ActionEvent event)
	         {
	            System.exit(0);
	         }
	      }
		item.addActionListener(new ExitItemListener());
		return item;
	}
	public void createMessageBox(String msg){
		JFrame frame = new JFrame("Error:");
		JLabel lbl = new JLabel(msg);
		frame.add(lbl);
		frame.setSize(200,200);
		frame.setVisible(true);
	}
	public void loadList() {
		Socket socket;
		try {
			socket = new Socket("localhost",8000);
			ObjectOutputStream toServer =
			          new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
			String loadGame = "Load the game";
			toServer.writeObject((Object)loadGame);
			JFrame frame = new JFrame("Load Game Lists");
			inputPlayerName = new JTextField(20);
			JLabel inputPlayer = new JLabel("Please input player name:");
			getPlayer = new JButton("Get player");
			getPlayer.addActionListener(new getPlayerListener());
			JPanel upperPanel = new JPanel();
			upperPanel.add(inputPlayer);
			upperPanel.add(inputPlayerName);
			upperPanel.add(getPlayer);
			frame.add(upperPanel,BorderLayout.NORTH);
			playerLists = new JTextArea(35,10);

			JScrollPane listScroller = new JScrollPane(playerLists);
			frame.add(listScroller, BorderLayout.CENTER);
			listScroller.setPreferredSize(new Dimension(250, 80));
			frame.setSize(600,400);
			frame.setVisible(true);
				
			Object object = fromServer.readObject();
			String rs = (String)object;
			playerLists.append(rs);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	class getPlayerListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Socket socket = new Socket("localhost",8000);
				ObjectOutputStream toServer =
				          new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
				toServer.writeObject((Object)inputPlayerName.getText());
				toServer.flush();
				
				Object object = fromServer.readObject();
				if (object instanceof player_info) {
					player = (player_info)object;
					Turn.setText("Turn:"+player.getTurnNum());  
					turnNum = player.getTurnNum();
					Roll.setText("Roll:"+player.getRollNum());
					rollNum = player.getRollNum();
					
					if(player.getacesTxt()>=0) {
						txtAces.setText(Integer.toString(player.getacesTxt()));
						able_flag.set(0, false);
					}
					else {
						txtAces.setText("");
						able_flag.set(0, true);
					}
					if(player.gettwosTxt()>=0) {
						txtTwos.setText(Integer.toString(player.gettwosTxt()));
						able_flag.set(1, false);
					}
					else {
						txtTwos.setText("");
						able_flag.set(1, true);
					}
					if(player.getthreesTxt()>=0) {
						txtThrees.setText(Integer.toString(player.getthreesTxt()));
						able_flag.set(2, false);
					}
					else {
						txtThrees.setText("");
						able_flag.set(2, true);
					}
					if(player.getfoursTxt()>=0) {
						txtFours.setText(Integer.toString(player.getfoursTxt()));
						able_flag.set(3, false);
					}
					else {
						txtFours.setText("");
						able_flag.set(3, true);
					}
					if(player.getfivesTxt()>=0) {
						txtFives.setText(Integer.toString(player.getfivesTxt()));
						able_flag.set(4, false);
					}
					else {
						txtFives.setText("");
						able_flag.set(4, true);
					}
					if(player.getsixsTxt()>=0) {
						txtSixs.setText(Integer.toString(player.getsixsTxt()));
						able_flag.set(5, false);
					}
					else {
						txtSixs.setText("");
						able_flag.set(5, true);
					}
					if(player.getthreekTxt()>=0) {
						txtThreek.setText(Integer.toString(player.getthreekTxt()));
						able_flag.set(6, false);
					}
					else {
						txtThreek.setText("");
						able_flag.set(6, true);
					}
					if(player.getfourkTxt()>=0) {
						txtFourk.setText(Integer.toString(player.getfourkTxt()));
						able_flag.set(7, false);
					}
					else {
						txtFourk.setText("");
						able_flag.set(7, true);
					}
					if(player.getfullTxt()>=0) {
						txtFull.setText(Integer.toString(player.getfullTxt()));
						able_flag.set(8, false);
					}
					else {
						txtFull.setText("");
						able_flag.set(8, true);
					}
					if(player.getsmallTxt()>=0) {
						txtSmall.setText(Integer.toString(player.getsmallTxt()));
						able_flag.set(9, false);
					}
					else {
						txtSmall.setText("");
						able_flag.set(9, true);
					}
					if(player.getlargeTxt()>=0) {
						txtLarge.setText(Integer.toString(player.getlargeTxt()));
						able_flag.set(10, false);
					}
					else {
						txtLarge.setText("");
						able_flag.set(10, true);
					}
					if(player.getchanceTxt()>=0) {
						txtChance.setText(Integer.toString(player.getchanceTxt()));
						able_flag.set(11, false);
					}
					else {
						txtChance.setText("");
						able_flag.set(11, true);
					}
					if(player.getyhzTxt()>=0) {
						txtYhz.setText(Integer.toString(player.getyhzTxt()));
						able_flag.set(12, false);
						
					}
					else {
						txtYhz.setText("");
						able_flag.set(12, true);
					}
					if(player.getyhzbnsTxt()>=0) {
						yhzBnsTxt.setText(Integer.toString(player.getyhzbnsTxt()));
					}
					else {
						yhzBnsTxt.setText("");
					}
				}
				else {
					createMessageBox("No this player in current database!");
				}
			}catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	public void initialTxt() {
		txtAces = new JLabel("");		
		txtAces.setOpaque(true);
		txtAces.setBackground(Color.white);
		txtAces.setPreferredSize(new Dimension(100,25));
		txtAces.setBorder(line);
		
		txtTwos =new JLabel("");
		txtTwos.setOpaque(true);
		txtTwos.setBackground(Color.white);
		txtTwos.setBorder(line);
		
		txtThrees =new JLabel("");
		txtThrees.setOpaque(true);
		txtThrees.setBackground(Color.white);
		txtThrees.setBorder(line);
		
		
		txtFours=new JLabel("");
		txtFours.setOpaque(true);
		txtFours.setBackground(Color.white);
		txtFours.setBorder(line);
		
		txtFives=new JLabel("");
		txtFives.setOpaque(true);
		txtFives.setBackground(Color.white);
		txtFives.setBorder(line);
		
		txtSixs=new JLabel();
		txtSixs.setOpaque(true);
		txtSixs.setBackground(Color.white);
		txtSixs.setBorder(line);
		
		upScrTxt = new JLabel("");
		upScrTxt.setOpaque(true);
		upScrTxt.setBackground(Color.white);
		upScrTxt.setBorder(line);
		
		upBnsTxt = new JLabel("");
		upBnsTxt.setOpaque(true);
		upBnsTxt.setBackground(Color.white);
		upBnsTxt.setBorder(line);
		
		upTotalTxt= new JLabel("");
		upTotalTxt.setOpaque(true);
		upTotalTxt.setBackground(Color.white);
		upTotalTxt.setBorder(line);
		
		txtThreek = new JLabel("");
		txtThreek.setOpaque(true);
		txtThreek.setBackground(Color.white);
		txtThreek.setPreferredSize(new Dimension(100,25));
		txtThreek.setBorder(line);
		
		txtFourk = new JLabel("");
		txtFourk.setOpaque(true);
		txtFourk.setBackground(Color.white);
		txtFourk.setBorder(line);
		
		txtFull = new JLabel("");
		txtFull.setOpaque(true);
		txtFull.setBackground(Color.white);
		txtFull.setBorder(line);
		
		txtSmall = new JLabel("");
		txtSmall.setOpaque(true);
		txtSmall.setBackground(Color.white);
		txtSmall.setBorder(line);
		
		txtLarge = new JLabel("");
		txtLarge.setOpaque(true);
		txtLarge.setBackground(Color.white);
		txtLarge.setBorder(line);
		
		txtYhz = new JLabel("");
		txtYhz.setOpaque(true);
		txtYhz.setBackground(Color.white);
		txtYhz.setBorder(line);
		
		txtChance = new JLabel("");
		txtChance.setOpaque(true);
		txtChance.setBackground(Color.white);
		txtChance.setBorder(line);
		
		yhzBnsTxt = new JLabel("");
		yhzBnsTxt.setOpaque(true);
		yhzBnsTxt.setBackground(Color.white);
		yhzBnsTxt.setBorder(line);
		
		loTotalTxt = new JLabel("");
		loTotalTxt.setOpaque(true);
		loTotalTxt.setBackground(Color.white);
		loTotalTxt.setBorder(line);
		
		allTotalTxt = new JLabel("");
		allTotalTxt.setOpaque(true);
		allTotalTxt.setBackground(Color.white);
		allTotalTxt.setBorder(line);
		
	}
	public void guiRestart(){
		turnNum=1;
		rollNum =0;
		player.setrollNum(rollNum);
		diceLabel1= new ImagePanel("die1.png");
		diceLabel2= new ImagePanel("die1.png");
		diceLabel3= new ImagePanel("die1.png");
		diceLabel4= new ImagePanel("die1.png");
		diceLabel5= new ImagePanel("die1.png");
		sum=0;
		upperScr=0;
		lowerScr = 0;
		turn_flag = false;
		able_flag = new ArrayList<Boolean>();
			for(int i=0;i<13;i++) {
				able_flag.add(i,true);
			}

		cb1 = new JCheckBox("Keep",false);
		cb2 = new JCheckBox("Keep",false);
		cb3 = new JCheckBox("Keep",false);
		cb4 = new JCheckBox("Keep",false);
		cb5 = new JCheckBox("Keep",false);
		
		initialTxt();


	}
				
	
	public static void main(String args[]) {
		YahtzeeFrame yahtzee = new YahtzeeFrame();
		
	}
}
