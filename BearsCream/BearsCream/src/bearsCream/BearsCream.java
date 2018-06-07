package bearsCream;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BearsCream extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JScrollPane scrollPane;
	ArrayList<JLabel> scoreList=new ArrayList<JLabel>(10);

	// 이미지
	private Image bgimg = new ImageIcon(this.getClass().getResource("/intro_Background.jpg")).getImage();
	private ImageIcon startButtonEnteredImg = new ImageIcon(this.getClass().getResource("/startButtonEntered.png"));
	private ImageIcon startButtonBasicImg = new ImageIcon(this.getClass().getResource("/startButtonBasic.png"));
	private ImageIcon explanationButtonEnteredImg = new ImageIcon(this.getClass().getResource("/explanationButtonEntered.png"));
	private ImageIcon explanationButtonBasicImg = new ImageIcon(this.getClass().getResource("/explanationButtonBasic.png"));
	private ImageIcon rankButtonEnteredImg = new ImageIcon(this.getClass().getResource("/rankButtonEntered.png"));
	private ImageIcon rankButtonBasicImg = new ImageIcon(this.getClass().getResource("/rankButtonBasic.png"));
	private ImageIcon backButtonImg = new ImageIcon(this.getClass().getResource("/back.png"));
	private ImageIcon pauseButtonImg = new ImageIcon(this.getClass().getResource("/pause.png"));
	private ImageIcon restartButtonImg = new ImageIcon(this.getClass().getResource("/restart.png"));

	// 버튼
    private JButton startButton = new JButton(startButtonBasicImg);
	private JButton explanationButton = new JButton(explanationButtonBasicImg);
	private JButton rankButton = new JButton(rankButtonBasicImg);
	private JButton backButton=new JButton(backButtonImg);
	private JButton pauseButton = new JButton(pauseButtonImg);
	private JButton restartButton=new JButton(restartButtonImg);
	
	//음악
	Music music=new Music("music/introBackgroundMusic.mp3",true);
	
	int nowScreen=0; // 0은 메인 1은 설명 2는 게임 시작 3은 랭킹 4는 게임오버
	static boolean pause=false;
	
	public static Game game;
	
	public BearsCream() {
		setTitle("Bear's Cream"); //제목
		setLayout(null);
		setVisible(true);
		setResizable(false); // 창크기 바뀌지 않게
		
		JPanel background = new JPanel() {
	           /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
	        	   if(nowScreen==2&&!game.isAlive()){//게임화면인데 스레드가 죽어있을 때 결과화면으로
	        		   nowScreen=4;
	        		   bgimg=new ImageIcon(this.getClass().getResource("/result_Background.jpg")).getImage();
	        	   }
	               g.drawImage(bgimg, 0, 0, null);
	               if(nowScreen==2)
	            	   game.screenDraw(g);
	               else if(nowScreen==4){
	   				   backButton.setBounds(20,20,72,72);
	            	   pauseButton.setVisible(false);
	            	   backButton.setVisible(true);
	        		   g.setColor(Color.white);
	        		   g.setFont(new Font("Arial",Font.BOLD,100));
	        		   g.drawString(String.valueOf(Game.score), 470, 300);
	        		   g.drawImage(game.resultBear,306,380,388,319,null);
	               }
	               setOpaque(false); //그림을 표시하게 설정,투명하게 조절
	               super.paintComponent(g);
	               this.repaint();
	           }
	     };
	     background.setLayout(null);
	     
	     addKeyListener(new KeyListener());
	     
	     music.start();
	     
	     explanationButton.setBounds(120,460,300,120);
	     explanationButton.setBorderPainted(false); // 버튼 테두리 없애기
	     explanationButton.setContentAreaFilled(false);// 버튼 배경없애기
	     explanationButton.setFocusPainted(false); // 버튼 안에 이미지 테두리 없애기
	     background.add(explanationButton);
	     scrollPane = new JScrollPane(background);
	     setContentPane(scrollPane);
	     explanationButton.addMouseListener(new MouseAdapter() {
			@Override
			// 버튼에 마우스를 올렸을 때
			public void mouseEntered(MouseEvent e) {
				explanationButton.setIcon(explanationButtonEnteredImg);
			}

			@Override
			// 버튼에 마우스를 뗐을 때
			public void mouseExited(MouseEvent e) {
				explanationButton.setIcon(explanationButtonBasicImg);
			}

			@Override
			// 버튼을 눌렀을 때
			public void mousePressed(MouseEvent e) {
				startButton.setVisible(false);
				explanationButton.setVisible(false);
				rankButton.setVisible(false);
				bgimg = new ImageIcon(this.getClass().getResource("/explain_Background.jpg")).getImage();
				backButton.setVisible(true);
			}
		});

	     startButton.setBounds(340,460,300,120);
	     startButton.setBorderPainted(false); // 버튼 테두리 없애기
	     startButton.setContentAreaFilled(false);// 버튼 배경없애기
	     startButton.setFocusPainted(false); // 버튼 안에 이미지 테두리 없애기
	     background.add(startButton);
	     scrollPane = new JScrollPane(background);
	     setContentPane(scrollPane);
	     startButton.addMouseListener(new MouseAdapter() {
			@Override
			// 버튼에 마우스를 올렸을 때
			public void mouseEntered(MouseEvent e) {
				startButton.setIcon(startButtonEnteredImg);
			}

			@Override
			// 버튼에 마우스를 뗐을 때
			public void mouseExited(MouseEvent e) {
				startButton.setIcon(startButtonBasicImg);
			}

			@Override
			// 버튼을 눌렀을 때
			public void mousePressed(MouseEvent e) {
				startButton.setVisible(false);
				explanationButton.setVisible(false);
				rankButton.setVisible(false);
				bgimg = new ImageIcon(this.getClass().getResource("/game_Background.jpg")).getImage();
				backButton.setIcon(new ImageIcon(this.getClass().getResource("/home.png")));
				game=new Game();
				music.close();
				music=new Music("music/gameBackgroundMusic.mp3",true);
				music.start();
				pauseButton.setVisible(true);
				nowScreen=2;
				Game.score=0;
				game.start();
			}
		});
	     
	     rankButton.setBounds(560,460,300,120);
	     rankButton.setBorderPainted(false); // 버튼 테두리 없애기
	     rankButton.setContentAreaFilled(false);// 버튼 배경없애기
	     rankButton.setFocusPainted(false); // 버튼 안에 이미지 테두리 없애기
	     background.add(rankButton);
	     scrollPane = new JScrollPane(background);
	     setContentPane(scrollPane);
	     rankButton.addMouseListener(new MouseAdapter() {
			@Override
			// 버튼에 마우스를 올렸을 때
			public void mouseEntered(MouseEvent e) {
				rankButton.setIcon(rankButtonEnteredImg);
			}

			@Override
			// 버튼에 마우스를 뗐을 때
			public void mouseExited(MouseEvent e) {
				rankButton.setIcon(rankButtonBasicImg);
			}

			@Override
			// 버튼을 눌렀을 때
			public void mousePressed(MouseEvent e) {
				startButton.setVisible(false);
				explanationButton.setVisible(false);
				rankButton.setVisible(false);
				bgimg = new ImageIcon(this.getClass().getResource("/rank_Background.jpg")).getImage();
				backButton.setVisible(true);

				rankRead();
				for(int i=0;i<10;i++){
					scoreList.get(i).setFont(new Font("돋움", Font.PLAIN, 20));
					scoreList.get(i).setBounds(480, 165+(i*48), 250, 25);
					background.add(scoreList.get(i));
					scoreList.get(i).setVisible(true);
				}
				nowScreen=3;
			}
		}); 
	     
	     backButton.setBounds(20,20,72,72);
	     backButton.setBorderPainted(false); // 버튼 테두리 없애기
	     backButton.setContentAreaFilled(false);// 버튼 배경없애기
	     backButton.setFocusPainted(false); // 버튼 안에 이미지 테두리 없애기
	     background.add(backButton);
	     backButton.setVisible(false);
	     scrollPane = new JScrollPane(background);
	     setContentPane(scrollPane);
	     backButton.addMouseListener(new MouseAdapter() {
			// 버튼을 눌렀을 때
			public void mousePressed(MouseEvent e) {
				backMain();
			}
		});
	     
	     pauseButton.setBounds(20,20,72,72);
	     pauseButton.setBorderPainted(false); // 버튼 테두리 없애기
	     pauseButton.setContentAreaFilled(false);// 버튼 배경없애기
	     pauseButton.setFocusPainted(false); // 버튼 안에 이미지 테두리 없애기
	     background.add(pauseButton);
	     pauseButton.setVisible(false);
	     scrollPane = new JScrollPane(background);
	     setContentPane(scrollPane);
	     pauseButton.addMouseListener(new MouseAdapter() {
			// 버튼을 눌렀을 때
			@SuppressWarnings("deprecation")
			public void mousePressed(MouseEvent e) {
				backButton.setBounds(408,324,72,72);
				backButton.setVisible(true);
				restartButton.setVisible(true);
				pauseButton.setVisible(false);
				pause=true;
				game.suspend();
				for(int i=0;i<game.iList.size();i++){
					game.iList.get(i).suspend();
				}
			}
		});
		 
	     restartButton.setBounds(500,324,72,72);
	     restartButton.setBorderPainted(false); // 버튼 테두리 없애기
	     restartButton.setContentAreaFilled(false);// 버튼 배경없애기
	     restartButton.setFocusPainted(false); // 버튼 안에 이미지 테두리 없애기
	     background.add(restartButton);
	     restartButton.setVisible(false);
	     scrollPane = new JScrollPane(background);
	     setContentPane(scrollPane);
	     restartButton.addMouseListener(new MouseAdapter() {
			// 버튼을 눌렀을 때
			@SuppressWarnings("deprecation")
			public void mousePressed(MouseEvent e){
				backButton.setVisible(false);
				restartButton.setVisible(false);
				pauseButton.setVisible(true);
				pause=false;
				game.resume();
				for(int i=0;i<game.iList.size();i++){
					//오류
					game.iList.get(i).resume();
				}
			}
		});
	     
	}
	
	public void backMain(){
		pause=false;
		backButton.setBounds(20,20,72,72);
		startButton.setVisible(true);
		explanationButton.setVisible(true);
		rankButton.setVisible(true);
		restartButton.setVisible(false);
		bgimg = new ImageIcon(this.getClass().getResource("/intro_Background.jpg")).getImage();
		backButton.setIcon(backButtonImg);
		backButton.setVisible(false);
		if(nowScreen==2||nowScreen==4){
			music.close();
			music=new Music("music/introBackgroundMusic.mp3",true);
			music.start();
			game.interruptThread();
		}
		if(nowScreen==3){
			for(int i=0;i<10;i++){
				scoreList.get(0).setVisible(false);
				scoreList.remove(0);
			}
		}
		nowScreen=0;
	}
	
	public void rankRead(){
		File file=new File("score.txt");
		Scanner scan;
		try {
			scan = new Scanner(file);
			String line="";
			
			while(scan.hasNextLine()){
				line =scan.nextLine()+"개";
				scoreList.add(new JLabel(line));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
