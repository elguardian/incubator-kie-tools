/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License. 
 */

package org.kie.lienzo.client;

import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.shared.core.types.Color;

public class RectangleExample extends BaseShapesExample<Rectangle> implements Example {

    public RectangleExample(String title) {
        super(title);
        this.numberOfShapes = 30;
        this.setPaddings(5, 20, 5, 100);
        shapes = new Rectangle[numberOfShapes];
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfShapes; i++) {
            final int strokeWidth = 1;
            shapes[i] = new Rectangle(Math.random() * 220, Math.random() * 160)
                    .setStrokeColor(Color.getRandomHexColor()).setStrokeWidth(strokeWidth).setFillColor(Color.getRandomHexColor()).setDraggable(true);
            layer.add(shapes[i]);
        }
        setLocation();
        layer.draw();
    }
}
