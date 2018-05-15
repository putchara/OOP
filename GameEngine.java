package f2.spw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();	
	private SpaceShip v1;	
	private SpaceShip v2;

	private Timer timer1,timer2;
	
	private long score1 = 0,score2 = 0;
	private int level = 0;
	private int defultScore1 = 100;
	private int defultScore2 = 100;
	
	private double difficulty = 0.1;
	private int p1_Life = 1;
	private int p2_Life = 1;

	
	public GameEngine(GamePanel gp, SpaceShip v1, SpaceShip v2) {
		this.gp = gp;

		this.v1 = v1;
		this.v2 = v2;			
		
		gp.sprites.add(v1);
		gp.sprites.add(v2);
		timer2 = new Timer(10000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				difficulty += 0.2;
				level += 1;

			}
		});
		
		timer1 = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();

			}
		});
		timer1.setRepeats(true);
		timer2.setRepeats(true);
		
	}
	
	public void start(){
		timer1.start();
		timer2.start();
	}
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*790), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}
		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
							
				score1 += defultScore1;
				score2 += defultScore2;
			}
		}
		
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr1 = v1.getRectangle();
		Rectangle2D.Double vr2 = v2.getRectangle();
		Rectangle2D.Double er;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr1)){
				//Action When Player1 OVER!
				v1.setMove(0);
				defultScore1 = 0;
				p1_Life = 0;
				return;
			}else if(er.intersects(vr2)){
				//Action When Player2 OVER!
				v2.setMove(0);
				defultScore2 = 0;
				p2_Life = 0;
				return;
			}
			if (p1_Life == 0  &&  p2_Life == 0) {
				if(score1 > score2)
					gp.setWinner(1);
				else if(score2 > score1)
					gp.setWinner(2);
				else
					gp.setWinner(3);

				gp.updateGameUI(this);
				die();				
			}
		}
		
	}
	
	public void die(){
		timer1.stop();
		timer2.stop();
	}
	
	void controlVehicleForP1(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				v1.move(-1);
				break;
			case KeyEvent.VK_D:
				v1.move(1);
				break;
		}
	}

	void controlVehicleForP2(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				v2.move(-1);
				break;
			case KeyEvent.VK_RIGHT:
				v2.move(1);
				break;
		}
	}

	public long getScoreP1(){
		return score1;
	}
	
	public long getScoreP2(){
		return score2;
	}

	public int getLevel(){
		return level;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		controlVehicleForP1(e);
		controlVehicleForP2(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		controlVehicleForP1(e);
		controlVehicleForP2(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}
}
