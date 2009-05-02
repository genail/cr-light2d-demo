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

import pl.graniec.coralreef.geometry.Geometry;
import pl.graniec.coralreef.geometry.Point2;
import pulpcore.image.CoreGraphics;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class Shape extends Geometry {
	
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
