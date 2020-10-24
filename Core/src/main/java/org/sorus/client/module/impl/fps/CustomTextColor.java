/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.sorus.client.module.impl.fps;

import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.settings.Configurable;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.settings.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomTextColor extends Configurable {

    private final Setting<List<Pair<String, Color>>> setting;
    private final List<CustomText> customTexts = new ArrayList<>();
    private final Collection collection;

    private CustomText draggedText;
    private double originalMouseX, originalMouseY;
    private double originalX, originalY;

    public CustomTextColor(Setting<List<Pair<String, Color>>> setting, String description) {
        this.setting = setting;
        this.add(collection = new Collection().position(30, 70));
        for(Pair<String, Color> pair : this.setting.getValue()) {
            this.addCustomText(pair.getKey(), pair.getValue());
        }
        collection.add(new NewButton().position(0, 95));
        collection.add(new RemoveButton().position(60, 95));
        this.add(
                new Text()
                        .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                        .text(description)
                        .scale(3.5, 3.5)
                        .position(30, 30)
                        .color(new Color(170, 170, 170)));
        Sorus.getSorus().getEventManager().register(this);
    }

    private CustomText addCustomText(String string, Color color) {
        return this.addCustomText(customTexts.size(), string, color);
    }

    private CustomText addCustomText(int index, String string, Color color) {
        CustomText customText = new CustomText(string, color);
        this.customTexts.add(index, customText);
        collection.add(customText);
        return customText;
    }

    private void update() {
        List<Pair<String, Color>> list = new ArrayList<>();
        for(CustomText customText : this.customTexts) {
            list.add(Pair.of(customText.textBoxInner.message, customText.color));
        }
        this.setting.setValue(list);
    }

    @Override
    public void onRender() {
        int x = 0;
        double test = draggedText == null ? 0 : draggedText.absoluteX();
        boolean continuing = true;
        int index = 0;
        if(customTexts.size() == 0) {
            continuing = false;
        }
        while(continuing) {
            CustomText customText = this.customTexts.get(index);
            customText.position(x, 0);
            if(Math.abs(customText.absoluteX() - test) < customText.getWidth() / 2 * this.absoluteXScale()) {
                x += draggedText.getWidth() + 10;
                this.draggedText.index = index;
            } else {
                index++;
                x += customText.getWidth() + 10;
            }
            if(index == this.customTexts.size()) {
                continuing = false;
            }
        }
        if(test > (this.collection.absoluteX() + x * this.collection.absoluteXScale() - (this.customTexts.get(this.customTexts.size() - 1).getWidth() / 2 * this.absoluteXScale())) && draggedText != null) {
            this.draggedText.index = customTexts.size();
        }
        if(this.draggedText != null) {
            double mouseX = Sorus.getSorus().getVersion().getInput().getMouseX();
            double mouseY = Sorus.getSorus().getVersion().getInput().getMouseY();
            this.draggedText.position((mouseX - originalMouseX) * 4 + originalX, (mouseY - originalMouseY) * 4 + originalY);
        }
        super.onRender();
    }

    @Override
    public void onRemove() {
        Sorus.getSorus().getEventManager().unregister(this);
        super.onRemove();
    }

    @EventInvoked
    public void onMouseRelease(MouseReleaseEvent e) {
        if(this.draggedText != null) {
            this.collection.remove(draggedText);
            CustomText customText = this.addCustomText(draggedText.index, draggedText.textBoxInner.message, draggedText.color);
            customText.selected = true;
            this.draggedText = null;
            CustomTextColor.this.update();
        }
    }

    public class NewButton extends Collection {

        public NewButton() {
            this.add(new Rectangle().size(50, 50).color(DefaultTheme.getMedforegroundLayerColor()));
            this.add(new Rectangle().size(10, 40).position(20, 5).color(DefaultTheme.getForegroundLayerColor()));
            this.add(new Rectangle().size(40, 10).position(5, 20).color(DefaultTheme.getForegroundLayerColor()));
            Sorus.getSorus().getEventManager().register(this);
        }

        @Override
        public void onRemove() {
            Sorus.getSorus().getEventManager().unregister(this);
            super.onRemove();
        }

        @EventInvoked
        public void onClick(MousePressEvent e) {
            if(this.getContainer().isInteractContainer()) {
                if(e.getX() > this.absoluteX() && e.getX() < this.absoluteX() + 50 * this.absoluteXScale() && e.getY() > this.absoluteY() && e.getY() < this.absoluteY() + 50 * this.absoluteYScale()) {
                    CustomText customText = CustomTextColor.this.addCustomText("", Color.WHITE);
                    customText.selected = true;
                    customText.textBoxInner.selected = true;
                }
            }
        }
    }

    public class RemoveButton extends Collection {

        public RemoveButton() {
            this.add(new Rectangle().size(50, 50).color(DefaultTheme.getMedforegroundLayerColor()));
            this.add(new Rectangle().size(40, 10).position(5, 20).color(DefaultTheme.getForegroundLayerColor()));
            Sorus.getSorus().getEventManager().register(this);
        }

        @Override
        public void onRemove() {
            Sorus.getSorus().getEventManager().unregister(this);
            super.onRemove();
        }

        @EventInvoked
        public void onClick(MousePressEvent e) {
            if(this.getContainer().isInteractContainer()) {
                if(e.getX() > this.absoluteX() && e.getX() < this.absoluteX() + 50 * this.absoluteXScale() && e.getY() > this.absoluteY() && e.getY() < this.absoluteY() + 50 * this.absoluteYScale()) {
                    for(CustomText customText : new ArrayList<>(CustomTextColor.this.customTexts)) {
                        if(customText.selected) {
                            CustomTextColor.this.customTexts.remove(customText);
                            CustomTextColor.this.collection.remove(customText);
                            CustomTextColor.this.update();
                        }
                    }
                }
            }
        }

    }

    public class CustomText extends Collection {

        private final TextBoxInner textBoxInner;

        private final Rectangle rectangle;
        private final HollowRectangle hollowRectangle;

        private Color color;

        private boolean selected;

        private int index;

        public CustomText(String string, Color color) {
            this.add(rectangle = new Rectangle().color(DefaultTheme.getMedforegroundLayerColor()));
            this.add(hollowRectangle = new HollowRectangle());
            this.add(textBoxInner = new TextBoxInner(string));
            this.add(new ColorSelect().position(7.5, 47));
            this.color = color;
            Sorus.getSorus().getEventManager().register(this);
        }

        @Override
        public void onRender() {
            rectangle.size(this.getWidth(), 75);
            hollowRectangle.size(this.getWidth(), 75);
            if (selected) {
                hollowRectangle.color(DefaultTheme.getForegroundLayerColor());
            } else {
                hollowRectangle.color(new Color(0, 0, 0, 0));
            }
            super.onRender();
        }

        @Override
        public void onRemove() {
            Sorus.getSorus().getEventManager().unregister(this);
            super.onRemove();
        }

        @EventInvoked
        public void onClick(MousePressEvent e) {
            if(this.getContainer().isInteractContainer()) {
                selected = e.getX() > this.absoluteX() && e.getX() < this.absoluteX() + this.getWidth() * this.absoluteXScale() && e.getY() > this.absoluteY() && e.getY() < this.absoluteY() + 75 * this.absoluteYScale();
                if(selected && e.getX() > this.absoluteX() + (this.getWidth() / 2) * this.absoluteXScale() && e.getY() > this.absoluteY() + 35 * this.absoluteYScale()) {
                    CustomTextColor.this.customTexts.remove(this);
                    CustomTextColor.this.draggedText = this;
                    originalMouseX = e.getX();
                    originalMouseY = e.getY();
                    originalX = this.x;
                    originalY = this.y;
                }
            }
        }

        public double getWidth() {
            return this.textBoxInner.getWidth();
        }

        public class TextBoxInner extends Collection {

            private final Text text;

            private boolean selected;

            private String message;

            public TextBoxInner(String string) {
                this.add(text = new Text().fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer()).scale(4, 4).color(DefaultTheme.getForegroundLayerColor()));
                this.message = string;
                Sorus.getSorus().getEventManager().register(this);
            }

            @Override
            public void onRender() {
                this.updateButton(message);
                //Scissor.beginScissor(this.absoluteX() + 10 * this.absoluteXScale(), this.absoluteY(), 240 * this.absoluteXScale(), 50 * this.absoluteYScale());
                this.text.onRender();
                //Scissor.endScissor();
            }

            public double getWidth() {
                if(selected) {
                    return 40 + Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer().getStringWidth(this.message) * 4;
                } else {
                    return 20 + Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer().getStringWidth(this.message) * 4;
                }
            }

            @Override
            public void onRemove() {
                Sorus.getSorus().getEventManager().unregister(this);
            }

            @EventInvoked
            public void onClick(MousePressEvent e) {
                if(this.getContainer().isInteractContainer()) {
                    selected = e.getX() > this.absoluteX() && e.getX() < this.absoluteX() + this.getWidth() * this.absoluteXScale() && e.getY() > this.absoluteY() && e.getY() < this.absoluteY() + 40 * this.absoluteYScale();
                }
            }

            @EventInvoked
            public void keyPressed(KeyPressEvent e) {
                if(this.getContainer().isInteractContainer()) {
                    if(selected) {
                        char character = e.getCharacter();
                        switch(e.getKey()) {
                            case SHIFT_LEFT:
                            case SHIFT_RIGHT:
                                return;
                            case BACKSPACE:
                                if(!message.isEmpty()) {
                                    this.message = message.substring(0, message.length() - 1);
                                }
                                break;
                            default:
                                this.message = message + character;
                                break;
                        }
                        CustomTextColor.this.update();
                    }
                }
            }

            private void updateButton(String string) {
                this.text.text(string + " ");
                this.text.position(10, 25 - this.text.height() / 2 * 4);
                if(selected) {
                    this.text.text(string + (System.currentTimeMillis() % 1000 > 500 ? "_" : ""));
                }
            }
        }

        public class ColorSelect extends Collection {

            private final Rectangle rectangle;

            public ColorSelect() {
                this.add(rectangle = new Rectangle());
                Sorus.getSorus().getEventManager().register(this);
            }

            @Override
            public void onRender() {
                rectangle.size(CustomText.this.getWidth() / 2 - 7.5, 22).color(CustomText.this.color);
                super.onRender();
            }

            @Override
            public void onRemove() {
                Sorus.getSorus().getEventManager().unregister(this);
                super.onRemove();
            }

            @EventInvoked
            public void onClick(MousePressEvent e) {
                if(this.getContainer().isInteractContainer()) {
                    if(e.getX() > this.absoluteX() && e.getX() < this.absoluteX() + (CustomText.this.getWidth() / 2 - 20) * this.absoluteXScale() && e.getY() > this.absoluteY() && e.getY() < this.absoluteY() + 30 * this.absoluteYScale()) {
                        Sorus.getSorus().getGUIManager().open(new ColorPicker.ColorPickerScreen(CustomText.this.color, new ColorCallback()));
                    }
                }
            }

            public class ColorCallback implements Callback<Color> {

                @Override
                public void call(Color selected) {
                    CustomText.this.color = selected;
                    CustomTextColor.this.update();
                }

                @Override
                public void cancel() {

                }
            }

        }

    }

    @Override
    public double getHeight() {
        return 205;
    }

}
