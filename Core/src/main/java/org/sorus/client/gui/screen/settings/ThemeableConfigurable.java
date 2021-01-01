package org.sorus.client.gui.screen.settings;

public abstract class ThemeableConfigurable extends Configurable {

    private final ConfigurableThemeBase<?> theme;

    public ThemeableConfigurable(ConfigurableThemeBase<ThemeableConfigurable> theme) {
        this.theme = theme;
        theme.setParent(this);
        theme.init();
    }

    @Override
    public void onUpdate() {
        theme.update();
    }

    @Override
    public void onRender() {
        theme.render();
    }

    @Override
    public void onRemove() {
        theme.exit();
    }

    @Override
    public double getHeight() {
        return this.getTheme().getHeight();
    }

    public ConfigurableThemeBase<?> getTheme() {
        return theme;
    }

}
