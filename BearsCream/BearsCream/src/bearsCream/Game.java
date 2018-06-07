package bearsCream;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread{
	public Image judgment = null;
	ArrayList<IceCream> iList=new ArrayList<IceCream>(5);
	public Image resultBear = new ImageIcon(this.getClass().getResource("/result_Bear_normal.png")).getImage();
	public Image scoreItem1=new ImageIcon(this.getClass().getResource("/1.png")).getImage();
	public Image scoreItem2=new ImageIcon(this.getClass().getResource("/2.png")).getImage();
	public Image scoreItem3=new ImageIcon(this.getClass().getResource("/3.png")).getImage();
	public Image scoreItem4=new ImageIcon(this.getClass().getResource("/4.png")).getImage();
	public Image scoreItem5=new ImageIcon(this.getClass().getResource("/5.png")).getImage();
	public Image scoreItem6=new ImageIcon(this.getClass().getResource("/6.png")).getImage();

	IceCream icc;
	Bear b=new Bear();
	
	public static Cornet c=new Cornet(430,660);
	private int size=100;
	private int speed=100;
	
	private int before_x1,before_x2,before_y1;
	
	private int i_cnt=0;
	
	public static int score;
	private boolean flag=true;
	
	public Game(){
		c.x=430;
	}
	
	public void screenDraw(Graphics g){
		b.BearDraw(g);
		switch(score/3){
		case 5:
			g.drawImage(scoreItem1, 733, 222, 127,248, null);
			g.drawImage(scoreItem2, 400, 100, 550, 555, null);
			g.drawImage(scoreItem4, -30, 300, 331, 232, null);
			g.drawImage(scoreItem6, 100, 20, 777, 108, null);
			g.drawImage(scoreItem3, 600, 400, 217, 277, null);
			break;
		case 4:
			g.drawImage(scoreItem1, 733, 222, 127,248, null);
			g.drawImage(scoreItem2, 400, 100, 550, 555, null);
			g.drawImage(scoreItem4, -30, 300, 331, 232, null);
			g.drawImage(scoreItem6, 100, 20, 777, 108, null);
			break;
		case 3:
			g.drawImage(scoreItem1, 733, 222, 127,248, null);
			g.drawImage(scoreItem4, -30, 300, 331, 232, null);
			g.drawImage(scoreItem6, 100, 20, 777, 108, null);
			break;
		case 2:
			g.drawImage(scoreItem4, -30, 300, 331, 232, null);
			g.drawImage(scoreItem6, 100, 20, 777, 108, null);
			break;
		case 1:
			g.drawImage(scoreItem6, 100, 20, 777, 108, null);
			break;
		default:
			if((score/3)>5){
				g.drawImage(scoreItem1, 733, 222, 127,248, null);
				g.drawImage(scoreItem2, 400, 100, 550, 555, null);
				g.drawImage(scoreItem4, -30, 300, 331, 232, null);
				g.drawImage(scoreItem6, 100, 20, 777, 108, null);
				g.drawImage(scoreItem3, 600, 400, 217, 277, null);
				g.drawImage(scoreItem5, 290, 400, 109, 139, null);
			}
		}
		g.drawImage(judgment, 323, 250, 354, 118,null);
		for(int i=0;i<iList.size();i++){
			IceCream ic=iList.get(i);
			if(i==0){
				before_y1=c.y;
				before_x1=c.x;
				before_x2=c.x+92;
			}else{
				before_y1=iList.get(i-1).y1;
				before_x1=iList.get(i-1).x1;
				before_x2=iList.get(i-1).x2;
			}
			if(ic.isCatch(before_y1,before_x1,before_x2)){
				if(i==0)
					ic.y1=660-(ic.size/5*4);
				else
					ic.y1=iList.get(i-1).y1-(ic.size/5*4);
				
				ic.iceCreamDraw(g);
				
				if(i_cnt>=5){//5개면 삭제
					b.look++;
					judgment=new ImageIcon(this.getClass().getResource("/catch.png")).getImage();
					try {
						Thread.sleep(200); //5개 만들어지면 잠깐 기다리고 삭제
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int j=0;j<5;j++)
						iList.remove(0); //5개되면 삭제
					i_cnt-=5;
					score++;
				}
				if(i>=i_cnt)
					i_cnt++;
			}else{
				ic.iceCreamDraw(g);
				if(ic.y2>before_y1&&iList.size()>=5){//이전꺼의 위 y좌표보다 아래로 내려왔을때, 5개일때(5개단위로 삭제해야하기 때문)
					b.look--;
					judgment=new ImageIcon(this.getClass().getResource("/miss.png")).getImage();
					if(b.look==-2)
						judgment=new ImageIcon(this.getClass().getResource("/gameover.png")).getImage();//게임오버
					for(int j=0;j<5;j++){
						iList.remove(0);
					}
				}
			}
		}
		g.drawImage(c.cornet,c.x,c.y,92,70,null);
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.drawString(String.valueOf(score), 920, 45);
	}
	
	public void pressLeft(){
		c.Left();
		for(int i=0;i<iList.size();i++){
			if(!iList.get(i).isProceeded()){
				if(i==0)//같이 움직이게 이전거+이전거와의 거리
					iList.get(i).x1=c.x+iList.get(i).x_distance;
				else
					iList.get(i).x1=iList.get(i-1).x1+iList.get(i).x_distance;
				iList.get(i).x2=iList.get(i).x1+iList.get(i).size;
			}
		}
	}
	
	public void pressRight(){
		c.Right();
		for(int i=0;i<iList.size();i++){
			if(!iList.get(i).isProceeded()){
				if(i==0)//같이 움직이게 이전거+이전거와의 거리
					iList.get(i).x1=c.x+iList.get(i).x_distance;
				else
					iList.get(i).x1=iList.get(i-1).x1+iList.get(i).x_distance;
				iList.get(i).x2=iList.get(i).x1+iList.get(i).size;
			}
		}
	}
	public void run(){
		try {
			while(flag){
				if(b.look<=-2){
					flag=false;
					break;
				}
				for(int i=0;i<5;i++){
					judgment=null;
					if(iList.size()<=4){
						icc=new IceCream(size,speed);
						iList.add(icc);
						size-=10;
						icc.start();
						Thread.sleep(speed*25);//1개 만들고 쉬기
					}
				}
				Thread.sleep(speed*30);//5개 만들고 쉬기
				size=100;
				if(speed>60)speed-=5;
				else speed-=1;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(score/10==0){//점수가 한자리 수일 떄 우는 곰
			resultBear = new ImageIcon(this.getClass().getResource("/result_Bear_sad.png")).getImage();
		}
		Result r=new Result(score);
		r.readFile();
		if(r.compareRank()){//랭킹에 등록했을 때 행복한 곰
			resultBear = new ImageIcon(this.getClass().getResource("/result_Bear_happy.png")).getImage();
			r.inputName();
		}
	}
	
	public void interruptThread(){
		interrupt();
	}
}
