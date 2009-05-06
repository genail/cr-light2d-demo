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

import java.util.LinkedList;
import java.util.List;

import pl.graniec.coralreef.geometry.Geometry;
import pl.graniec.coralreef.geometry.Point2;
import pl.graniec.coralreef.light2d.LightResistor;
import pl.graniec.coralreef.light2d.LightSource;
import pl.graniec.coralreef.light2d.SimpleLightAlgorithm;
import pl.graniec.coralreef.pulpcore.desktop.CoreApplication;
import pulpcore.Input;
import pulpcore.Stage;
import pulpcore.image.CoreGraphics;
import pulpcore.scene.Scene;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class CirclesScene extends Scene {

	final List<Shape> shapes = new LinkedList<Shape>();
	private LightSource source;
	
	SimpleLightAlgorithm algorithm;
	private Geometry rays;
	
	public static void main(String[] args) {
		final CoreApplication app = new CoreApplication(CirclesScene.class);
		app.run();
	}
	
	@Override
	public void load() {
		
		final int rows = 4;
		final int cols = 4;
		final int circleRadius = 20;
		final int circleParts = 8;
		
		algorithm = new SimpleLightAlgorithm();
		
		for (int x = 0; x < cols; ++x) {
			for (int y = 0; y < rows; ++y) {
				final int posX = Stage.getWidth() / rows * x + Stage.getWidth() / (rows * 2);
				final int posY = Stage.getHeight() / cols * y + Stage.getHeight() / (cols * 2);
				
				final Shape shape = buildCircle(circleParts, circleRadius);
				
				shape.translate(posX, posY);
				shapes.add(shape);
				
				algorithm.addLightResistor(new LightResistor(shape));
			}
		}
		
		source = new LightSource(Stage.getWidth() / 2, Stage.getHeight() / 2, 500);
		
	}
	
	private Shape buildCircle(final int parts, final int radius) {
		final Shape shape = new Shape();
		
		float delta = 360.0f / parts;
		
		for (int i = 0; i < parts; ++i) {
			final float angle = i * delta;
			
			final float x = (float) Math.sin(Math.toRadians(angle)) * radius;
			final float y = (float) Math.cos(Math.toRadians(angle)) * radius;
			
			shape.addVerticle(new Point2(x, y));
		}
		
		return shape;
	}

	@Override
	public void drawScene(CoreGraphics g) {
		g.setColor(0xFF000000);
		g.fill();
		
		for (final Shape shape : shapes) {
			shape.draw(g);
		}
		
		// draw rays
		g.setColor(0xFFFFFF00);
		
		final Point2[] points = rays.getVerticles();
		
		Point2 prev, next = null, first = null;
		
		for (int i = 0; i < points.length; ++i) {
			
			prev = next;
			next = points[i];
			
			if (first == null) {
				first = points[i];
			}
			
			if (prev != null) {
				g.drawLine(prev.x, prev.y, next.x, next.y);
			}
		}
		
		if (first != null && first != next) {
			g.drawLine(first.x, first.y, next.x, next.y);
		}
	}

	@Override
	public void updateScene(int elapsedTime) {
		
		source.x = Input.getMouseX();
		source.y = Input.getMouseY();
		
		rays = algorithm.createRays(source);
	}

}
