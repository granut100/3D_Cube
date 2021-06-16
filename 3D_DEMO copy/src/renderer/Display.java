package renderer;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Display extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private Thread thread;
	private JFrame frame;
	private static String title = "3D Renderer";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static boolean running = false;
	
	private Polyhedron tetra;
	
	public Display() {
		this.frame = new JFrame();
		
		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setPreferredSize(size);
	}
	
	public static void main(String[] args) {
		Display display = new Display();
		display.frame.setTitle(title);
		display.frame.add(display);
		display.frame.pack();
		display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.frame.setLocationRelativeTo(null);
		display.frame.setResizable(false);
		display.frame.setVisible(true);
		
		display.start();
	}
	
	public synchronized void start() {
		running = true;
		this.thread = new Thread(this, "Display");
		this.thread.start();
	}
	
	public synchronized void stop() {
		try {
			this.thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60;
		double delta = 0;
		int frames = 0;
		
		init();
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				delta--;
				render();
				frames++;
			}
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				this.frame.setTitle(title + " | " + frames + " fps ");
				frames = 0;
			}
		}
		stop();
	}
	
	private void init() {
		int s = 100;
		MyPoint p1 = new MyPoint(s/2, -s/2, -s/2);
		MyPoint p2 = new MyPoint(s/2, s/2, -s/2);
		MyPoint p3 = new MyPoint(s/2, s/2, s/2);
		MyPoint p4 = new MyPoint(s/2, -s/2, s/2);
		MyPoint p5 = new MyPoint(-s/2, -s/2, -s/2);
		MyPoint p6 = new MyPoint(-s/2, s/2, -s/2);
		MyPoint p7 = new MyPoint(-s/2, s/2, s/2);
		MyPoint p8 = new MyPoint(-s/2, -s/2, s/2);
		
		this.tetra = new Polyhedron(
				new MyPolygon(Color.blue,p5,p6,p7,p8),
				new MyPolygon(Color.white,p1,p2,p6,p5),
				new MyPolygon(Color.yellow,p1,p5,p8,p4),
				new MyPolygon(Color.green,p2,p6,p7,p3),
				new MyPolygon(Color.orange,p4,p3,p7,p8),
				new MyPolygon(Color.red,p1,p2,p3,p4)
				);
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		
		tetra.render(g);
		
		g.dispose();
		bs.show();
	}
	
	private void update() {
		this.tetra.rotate(true, 0.5, 0.5, 0.5);
	}

}
