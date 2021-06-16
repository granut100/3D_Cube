package renderer;

import java.awt.Color;
import java.awt.Graphics;

public class Polyhedron {
	private MyPolygon[] polygons;
	private Color color;
	
	public Polyhedron(Color color, MyPolygon...polygons) {
		this.color = color;
		this.polygons = polygons;
		this.setPolygonColor();
	}
	
	public Polyhedron(MyPolygon...polygons) {
		this.color = Color.WHITE;
		this.polygons = polygons;
	}
	
	public void render(Graphics g) {
		for(MyPolygon poly : this.polygons) {
			poly.render(g);
		}
	}
	
	public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
		for(MyPolygon p : this.polygons) {
			p.rotate(CW, xDegrees, yDegrees, zDegrees);
		}
		this.sortPolygons();
	}
	
	private void sortPolygons() {
		MyPolygon.sortPoligons(polygons);
	}
	
	private void setPolygonColor() {
		for(MyPolygon poly : this.polygons) {
			poly.setColor(this.color);
		}
	}
 }
