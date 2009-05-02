/**
 * Copyright (c) 2009, Coral Reef Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of the Coral Reef Project nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package pl.graniec.coralreef.light2d.demo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pl.graniec.coralreef.geometry.Geometry;
import pl.graniec.coralreef.geometry.Point2;
import pl.graniec.coralreef.light2d.LightResistor;
import pl.graniec.coralreef.light2d.LightSource;
import pl.graniec.coralreef.light2d.SimpleLightAlgorithm;
import pl.graniec.coralreef.pulpcore.desktop.CoreApplication;
import pulpcore.Stage;
import pulpcore.image.CoreGraphics;
import pulpcore.scene.Scene;
import pulpcore.sprite.FilledSprite;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class MainScene extends Scene {

	final List<Shape> shapes = new LinkedList<Shape>();

	Shape rotatingShapeOrigin, rotatingShape;
	FilledSprite lightSprite;
	
	LightSource source;
	Geometry rays;
	float angle = 0;
	
	float fps;
	int timePassed;
	
	@Override
	public void load() {
		rotatingShapeOrigin = new Shape();
		rotatingShapeOrigin.addVerticle(new Point2(300, 250));
		rotatingShapeOrigin.addVerticle(new Point2(350, 300));
		rotatingShapeOrigin.addVerticle(new Point2(300, 350));
		rotatingShapeOrigin.addVerticle(new Point2(250, 300));
		
//		shapes.add(shape);
		
		lightSprite = new FilledSprite(0xFFFFFFFF);
		lightSprite.setSize(10, 10);
		lightSprite.anchorX.set(0.5f);
		lightSprite.anchorY.set(0.5f);
		lightSprite.x.set(Stage.getWidth() / 2);
		lightSprite.y.set(Stage.getHeight() / 2);
		
//		add(lightSprite);
		
		System.out.println(rays);
		
	}
	
	/*
	 * @see pulpcore.scene.Scene2D#drawScene(pulpcore.image.CoreGraphics)
	 */
	@Override
	public void drawScene(CoreGraphics g) {
		g.setColor(0xFF000000);
		g.fill();
		
		lightSprite.draw(g);
		
		for (final Shape shape : shapes) {
			shape.draw(g);
		}
		
		// draw rays
		g.setColor(0xFFFFFF00);
		
		final Point2[] points = rays.getVerticles();
		for (int i = 0; i < points.length; ++i) {
			g.drawLine(source.x, source.y, points[i].x, points[i].y);
		}
	}
	
	@Override
	public void updateScene(int elapsedTime) {
		angle += 45f * (elapsedTime / 1000f);
		
		final float xtrans = (float) Math.sin(Math.toRadians(angle)) * 150;
		final float ytrans = (float) Math.cos(Math.toRadians(angle)) * 150;
		
//		System.out.println(angle + " x=" + xtrans);
		
		if (rotatingShape != null) {
			shapes.remove(rotatingShape);
		}
		
		rotatingShape = new Shape(rotatingShapeOrigin);
		rotatingShape.translate(xtrans, ytrans);
		
		shapes.add(rotatingShape);
		
		
		final SimpleLightAlgorithm algorithm = new SimpleLightAlgorithm();
		
		final LightResistor resistor = new LightResistor(rotatingShape);
		source = new LightSource(Stage.getWidth() / 2, Stage.getHeight() / 2, 300);
		
		algorithm.addLightResistor(resistor);
		rays = algorithm.createRays(source);
		
		// fps
		++fps;
		timePassed += elapsedTime;
		
		if (timePassed >= 1000) {
			final float realFps = fps * (1000f / timePassed);
			System.out.println("fps: " + realFps);
			
			fps -= realFps;
			timePassed %= 1000;
		}
	}
	
	
	
	public static void main(String[] args) {
		final CoreApplication app = new CoreApplication(MainScene.class);
		app.run();
	}
	
}



class Shape extends Geometry {
	
	public Shape() {
	}
	
	public Shape(final Shape other) {
		super(other);
	}
	
	public void draw(final CoreGraphics g) {
		
		Point2 first = null, prev, next = null;
		
		for (final Iterator<?> itor = verticles.iterator(); itor.hasNext();) {
			
			final Point2 point = (Point2) itor.next();
			
			if (first == null) {
				first = point;
			}
			
			prev = next;
			next = point;
			
			if (prev == null) {
				continue;
			}
			
			drawLine(g, prev, next);
		}
		
		if (first != null && next != null) {
			drawLine(g, next, first);
		}
	}
	
	private void drawLine(final CoreGraphics g, final Point2 a, final Point2 b) {
		g.setColor(0xFFFFFFFF);
		g.drawLine(a.x, a.y, b.x, b.y);
	}
}