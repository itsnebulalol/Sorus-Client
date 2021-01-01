package org.sorus.client.gui.theme.defaultTheme.settings.components;

import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.modifier.impl.Recolor;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.settings.ConfigurableThemeBase;
import org.sorus.client.gui.screen.settings.components.CustomTextColor;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.IInput;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCustomTextColor extends ConfigurableThemeBase<CustomTextColor> {

    private final Collection main;
    private final Setting<List<List<Pair<String, Color>>>> setting;
    private final List<CustomTextLine> customTextLines = new ArrayList<>();

    private CustomTextLine.CustomText draggedText;
    private double originalMouseX, originalMouseY;
    private double originalX, originalY;
    private final Collection collection;
    private final Component newButton, newLineButton, removeButton;

    private final Map<CustomTextLine, Rectangle> backgrounds = new HashMap<>();

    private final Rectangle outer;

    public DefaultCustomTextColor(Collection main, Setting<List<List<Pair<String, Color>>>> setting, String description) {
        this.main = main;
        this.setting = setting;
        main.add(outer = new Rectangle().smooth(5).position(30, 70).color(DefaultTheme.getElementBackgroundColorNew()));
        main.add(collection = new Collection().position(30, 70));
        this.refresh();
        main.add(newButton = new NewButton());
        main.add(newLineButton = new NewLineButton());
        main.add(removeButton = new RemoveButton());
        main.add(
                new Text()
                        .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                        .text(description)
                        .scale(3.5, 3.5)
                        .position(30, 30)
                        .color(DefaultTheme.getElementColorNew()));
        Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void update() {
        main.onUpdate();
    }

    @Override
    public void render() {
        this.newButton.position(330, 80 + 100 * this.customTextLines.size());
        this.removeButton.position(400, 80 + 100 * this.customTextLines.size());
        this.newLineButton.position(755, 80 + 100 * this.customTextLines.size());
        if (this.draggedText != null) {
            double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
            double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
            this.draggedText.position(
                    (mouseX - originalMouseX) * 4 + originalX, (mouseY - originalMouseY) * 4 + originalY);
        }
        outer.size(790, this.customTextLines.size() * 100 + 70);
        main.onRender();
    }

    @Override
    public void exit() {
        Sorus.getSorus().getEventManager().unregister(this);
        main.onRemove();
    }

    @EventInvoked
    public void onMouseRelease(MouseReleaseEvent e) {
        if (DefaultCustomTextColor.this.draggedText != null) {
            draggedText.getParent().remove(draggedText);
            for (Component component : this.collection.getChildren()) {
                if (component instanceof CustomTextLine) {
                    if (Math.abs(
                            (((CustomTextLine) component).collection.absoluteY()) - draggedText.absoluteY())
                            < 37.5 * draggedText.absoluteYScale()) {
                        ((CustomTextLine) component).onDropCustomText();
                    }
                }
            }
            draggedText = null;
        }
    }

    private void refresh() {
        this.collection.clear();
        this.customTextLines.clear();
        this.backgrounds.clear();
        int i = 0;
        for (List<Pair<String, Color>> list : this.setting.getValue()) {
            CustomTextLine customTextLine = new CustomTextLine().position(10, 10 + 100 * i);
            for (Pair<String, Color> pair : list) {
                customTextLine.addCustomText(pair.getKey(), pair.getValue());
            }
            this.customTextLines.add(customTextLine);
            this.collection.add(customTextLine);
            Rectangle rectangle =
                    new Rectangle()
                            .size(700, 90)
                            .smooth(5)
                            .position(10, 10 + 100 * i)
                            .color(DefaultTheme.getElementMedgroundColorNew());
            this.collection.addAtFront(rectangle);
            this.backgrounds.put(customTextLine, rectangle);
            i++;
        }
    }

    private void update2() {
        List<List<Pair<String, Color>>> list = new ArrayList<>();
        for (CustomTextLine customTextLine : this.customTextLines) {
            List<Pair<String, Color>> lineList = new ArrayList<>();
            for (CustomTextLine.CustomText customText : customTextLine.customTexts) {
                lineList.add(Pair.of(customText.textBoxInner.message, customText.color));
            }
            list.add(lineList);
        }
        DefaultCustomTextColor.this.setting.setValue(list);
    }

    private void removeCustomTextLine(CustomTextLine customTextLine) {
        this.collection.remove(customTextLine);
        this.customTextLines.remove(customTextLine);
        this.collection.remove(backgrounds.get(customTextLine));
        this.update2();
        this.refresh();
    }

    public class NewButton extends Collection {

        public NewButton() {
            this.add(
                    new Rectangle()
                            .size(7.5, 40)
                            .position(21.25, 5));
            this.add(
                    new Rectangle()
                            .size(40, 7.5)
                            .position(5, 21.25));
            this.attach(new Recolor(100, 50, 50, DefaultTheme.getElementSecondColorNew(), DefaultTheme.getElementColorNew()));
            Sorus.getSorus().getEventManager().register(this);
        }

        @Override
        public void onRemove() {
            Sorus.getSorus().getEventManager().unregister(this);
            super.onRemove();
        }

        @EventInvoked
        public void onClick(MousePressEvent e) {
            if (this.getContainer().isTopLevel()) {
                if (e.getX() > this.absoluteX()
                        && e.getX() < this.absoluteX() + 50 * this.absoluteXScale()
                        && e.getY() > this.absoluteY()
                        && e.getY() < this.absoluteY() + 50 * this.absoluteYScale()) {
                    CustomTextLine.CustomText customText =
                            DefaultCustomTextColor.this.customTextLines.get(0).addCustomText("", Color.WHITE);
                    customText.selected = true;
                    customText.textBoxInner.selected = true;
                    DefaultCustomTextColor.this.update2();
                }
            }
        }
    }

    public class NewLineButton extends Collection {

        public NewLineButton() {
            this.add(
                    new Rectangle()
                            .size(7.5, 40)
                            .position(21.25, 5));
            this.add(
                    new Rectangle()
                            .size(40, 7.5)
                            .position(5, 21.25));
            this.attach(new Recolor(100, 50, 50, DefaultTheme.getElementSecondColorNew(), DefaultTheme.getElementColorNew()));
            Sorus.getSorus().getEventManager().register(this);
        }

        @Override
        public void onRemove() {
            Sorus.getSorus().getEventManager().unregister(this);
            super.onRemove();
        }

        @EventInvoked
        public void onClick(MousePressEvent e) {
            if (this.getContainer().isTopLevel()) {
                if (e.getX() > this.absoluteX()
                        && e.getX() < this.absoluteX() + 50 * this.absoluteXScale()
                        && e.getY() > this.absoluteY()
                        && e.getY() < this.absoluteY() + 50 * this.absoluteYScale()) {
                    CustomTextLine customTextLine =
                            new CustomTextLine().position(10, 10 + DefaultCustomTextColor.this.customTextLines.size() * 100);
                    DefaultCustomTextColor.this.collection.add(customTextLine);
                    Rectangle rectangle =
                            new Rectangle()
                                    .size(700, 90)
                                    .smooth(5)
                                    .position(10, 10 + 100 * DefaultCustomTextColor.this.customTextLines.size())
                                    .color(DefaultTheme.getElementMedgroundColorNew());
                    DefaultCustomTextColor.this.collection.addAtFront(rectangle);
                    DefaultCustomTextColor.this.customTextLines.add(customTextLine);
                    customTextLine.addCustomText("Test", Color.WHITE);
                    DefaultCustomTextColor.this.backgrounds.put(customTextLine, rectangle);
                    DefaultCustomTextColor.this.update2();
                }
            }
        }
    }

    public class RemoveButton extends Collection {

        public RemoveButton() {
            this.add(
                    new Rectangle()
                            .size(40, 7.5)
                            .position(5, 21.25));
            this.attach(new Recolor(100, 50, 50, DefaultTheme.getElementSecondColorNew(), DefaultTheme.getElementColorNew()));
            Sorus.getSorus().getEventManager().register(this);
        }

        @Override
        public void onRemove() {
            Sorus.getSorus().getEventManager().unregister(this);
            super.onRemove();
        }

        @EventInvoked
        public void onClick(MousePressEvent e) {
            if (this.getContainer().isTopLevel()) {
                if (e.getX() > this.absoluteX()
                        && e.getX() < this.absoluteX() + 50 * this.absoluteXScale()
                        && e.getY() > this.absoluteY()
                        && e.getY() < this.absoluteY() + 50 * this.absoluteYScale()) {
                    for (CustomTextLine customTextLine :
                            new ArrayList<>(DefaultCustomTextColor.this.customTextLines)) {
                        for (CustomTextLine.CustomText customText :
                                new ArrayList<>(customTextLine.customTexts)) {
                            if (customText.selected) {
                                customTextLine.customTexts.remove(customText);
                                customTextLine.collection.remove(customText);
                                DefaultCustomTextColor.this.update2();
                            }
                        }
                    }
                    DefaultCustomTextColor.this.update2();
                }
            }
        }
    }

    public class CustomTextLine extends Collection {

        private final List<CustomText> customTexts = new ArrayList<>();
        private final Collection collection;

        public CustomTextLine() {
            this.add(collection = new Collection());
            this.add(new RemoveButton().position(715, 20));
            Sorus.getSorus().getEventManager().register(this);
        }

        @Override
        public void onRender() {
            int x = 10;
            double test =
                    draggedText == null
                            || Math.abs(draggedText.absoluteY() - collection.absoluteY())
                            > 37.5 * this.absoluteYScale()
                            ? 0
                            : draggedText.absoluteX();
            boolean continuing = true;
            int index = 0;
            if (customTexts.size() == 0) {
                continuing = false;
            }
            while (continuing) {
                CustomTextLine.CustomText customText = this.customTexts.get(index);
                customText.position(x, 10);
                if (Math.abs(customText.absoluteX() - test)
                        < customText.getWidth() / 2 * this.absoluteXScale()) {
                    x += draggedText.getWidth() + 10;
                } else {
                    index++;
                    x += customText.getWidth() + 10;
                }
                if (index == this.customTexts.size()) {
                    continuing = false;
                }
            }
            super.onRender();
        }

        @Override
        public void onRemove() {
            Sorus.getSorus().getEventManager().unregister(this);
            super.onRemove();
        }

        private CustomTextLine.CustomText addCustomText(String string, Color color) {
            return this.addCustomText(customTexts.size(), string, color);
        }

        private CustomTextLine.CustomText addCustomText(int index, String string, Color color) {
            CustomTextLine.CustomText customText = new CustomTextLine.CustomText(string, color);
            this.customTexts.add(index, customText);
            collection.add(customText);
            customText.line = this;
            return customText;
        }

        public void onDropCustomText() {
            double test = draggedText.absoluteX();
            boolean continuing = true;
            int index = 0;
            int i = 0;
            if (customTexts.size() == 0) {
                continuing = false;
            }
            while (continuing) {
                CustomTextLine.CustomText customText = this.customTexts.get(i);
                if (Math.abs(customText.absoluteX() - test)
                        < customText.getWidth() / 2 * this.absoluteXScale()) {
                    index = i;
                } else {
                    i++;
                }
                if (i == this.customTexts.size()) {
                    continuing = false;
                }
            }
            if (this.customTexts.size() > 0
                    && test > this.customTexts.get(this.customTexts.size() - 1).absoluteX()) {
                index = this.customTexts.size();
            }
            CustomTextLine.CustomText customText =
                    this.addCustomText(index, draggedText.textBoxInner.message, draggedText.color);
            customText.selected = true;
            DefaultCustomTextColor.this.update2();
        }

        public class RemoveButton extends Collection {

            public RemoveButton() {
                this.add(
                        new Rectangle()
                                .size(40, 7.5)
                                .position(5, 21.25));
                this.attach(new Recolor(100, 50, 50, DefaultTheme.getElementSecondColorNew(), DefaultTheme.getElementColorNew()));
                Sorus.getSorus().getEventManager().register(this);
            }

            @Override
            public void onRemove() {
                Sorus.getSorus().getEventManager().unregister(this);
                super.onRemove();
            }

            @EventInvoked
            public void onClick(MousePressEvent e) {
                if (this.getContainer().isTopLevel()) {
                    if (e.getX() > this.absoluteX()
                            && e.getX() < this.absoluteX() + 50 * this.absoluteXScale()
                            && e.getY() > this.absoluteY()
                            && e.getY() < this.absoluteY() + 50 * this.absoluteYScale()) {
                        DefaultCustomTextColor.this.removeCustomTextLine(CustomTextLine.this);
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

            public CustomTextLine line;

            public CustomText(String string, Color color) {
                this.add(rectangle = new Rectangle().smooth(5).color(DefaultTheme.getElementBackgroundColorNew()));
                this.add(hollowRectangle = new HollowRectangle().thickness(2).smooth(5));
                this.add(textBoxInner = new TextBoxInner(string));
                this.add(new ColorSelect().position(7.5, 43));
                this.color = color;
                Sorus.getSorus().getEventManager().register(this);
            }

            @Override
            public void onRender() {
                rectangle.size(this.getWidth(), 70);
                hollowRectangle.size(this.getWidth(), 70);
                if (selected) {
                    hollowRectangle.color(DefaultTheme.getElementForegroundColorNew());
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
                if (this.getContainer().isTopLevel()) {
                    selected =
                            e.getX() > this.absoluteX()
                                    && e.getX() < this.absoluteX() + this.getWidth() * this.absoluteXScale()
                                    && e.getY() > this.absoluteY()
                                    && e.getY() < this.absoluteY() + 90 * this.absoluteYScale();
                    if (selected
                            && e.getX() > this.absoluteX() + (this.getWidth() / 2) * this.absoluteXScale()
                            && e.getY() > this.absoluteY() + 35 * this.absoluteYScale()) {
                        CustomTextLine.this.customTexts.remove(this);
                        CustomTextLine.this.collection.remove(this);
                        DefaultCustomTextColor.this.collection.add(this);
                        DefaultCustomTextColor.this.draggedText = this;
                        originalMouseX = e.getX();
                        originalMouseY = e.getY();
                        originalX = this.x + 10;
                        originalY = this.y + CustomTextLine.this.y;
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
                    this.add(
                            text =
                                    new Text()
                                            .fontRenderer(
                                                    Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                                            .scale(3, 3)
                                            .color(DefaultTheme.getForegroundLayerColor()));
                    this.message = string;
                    Sorus.getSorus().getEventManager().register(this);
                }

                @Override
                public void onRender() {
                    this.updateButton(message);
                    // Scissor.beginScissor(this.absoluteX() + 10 * this.absoluteXScale(), this.absoluteY(),
                    // 240 * this.absoluteXScale(), 50 * this.absoluteYScale());
                    this.text.onRender();
                    // Scissor.endScissor();
                }

                public double getWidth() {
                    if (selected) {
                        return 40
                                + Sorus.getSorus()
                                .getGUIManager()
                                .getRenderer()
                                .getRubikFontRenderer()
                                .getStringWidth(this.message)
                                * 3;
                    } else {
                        return 20
                                + Sorus.getSorus()
                                .getGUIManager()
                                .getRenderer()
                                .getRubikFontRenderer()
                                .getStringWidth(this.message)
                                * 3;
                    }
                }

                @Override
                public void onRemove() {
                    Sorus.getSorus().getEventManager().unregister(this);
                }

                @EventInvoked
                public void onClick(MousePressEvent e) {
                    if (this.getContainer().isTopLevel()) {
                        selected =
                                e.getX() > this.absoluteX()
                                        && e.getX() < this.absoluteX() + this.getWidth() * this.absoluteXScale()
                                        && e.getY() > this.absoluteY()
                                        && e.getY() < this.absoluteY() + 40 * this.absoluteYScale();
                    }
                }

                @EventInvoked
                public void keyPressed(KeyPressEvent e) {
                    if (this.getContainer().isTopLevel()) {
                        if (selected) {
                            char character = e.getCharacter();
                            switch (e.getKey()) {
                                case SHIFT_LEFT:
                                case SHIFT_RIGHT:
                                    return;
                                case BACKSPACE:
                                    if (!message.isEmpty()) {
                                        this.message = message.substring(0, message.length() - 1);
                                    }
                                    break;
                                default:
                                    this.message = message + character;
                                    break;
                            }
                            DefaultCustomTextColor.this.update2();
                        }
                    }
                }

                private void updateButton(String string) {
                    this.text.text(string + " ");
                    this.text.position(10, 25 - this.text.height() / 2 * 3);
                    if (selected) {
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
                    if (this.getContainer().isTopLevel()) {
                        if (e.getX() > this.absoluteX()
                                && e.getX()
                                < this.absoluteX()
                                + (CustomText.this.getWidth() / 2 - 20) * this.absoluteXScale()
                                && e.getY() > this.absoluteY()
                                && e.getY() < this.absoluteY() + 30 * this.absoluteYScale()) {
                            Sorus.getSorus()
                                    .getGUIManager()
                                    .open(
                                            new DefaultColorPicker.ColorPickerScreen(
                                                    CustomText.this.color, new ColorCallback()));
                        }
                    }
                }

                public class ColorCallback implements Callback<Color> {

                    @Override
                    public void call(Color selected) {
                        CustomText.this.color = selected;
                        DefaultCustomTextColor.this.update2();
                    }

                    @Override
                    public void cancel() {}
                }
            }
        }
    }

    @Override
    public double getHeight() {
        return 170 + 100 * this.customTextLines.size();
    }

}
