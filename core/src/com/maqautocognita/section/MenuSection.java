package com.maqautocognita.section;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.Texture;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This is mainly show in the left side of the alphabet and phonic screen
 *
 * @author csc
 */
public class MenuSection extends AbstractAutoCognitaSection {


    public MenuItem[] menuItems;
    public MenuItem helpMenuItem;
    private IMenuSelectListener menuSelectListener;

    public MenuSection(AbstractAutoCognitaScreen autoCognitaScreen) {
        super(autoCognitaScreen, null);

    }

    public void setMenuSelectListener(IMenuSelectListener menuSelectListener) {
        this.menuSelectListener = menuSelectListener;
    }

    @Override
    public void render() {
        initMenuItems();

        batch.begin();

        for (MenuItem menuItem : menuItems) {
            if (menuItem.isShow()) {
                AutoCognitaTextureRegion autoCognitaTextureRegion;
                if (menuItem.isHighLighted()) {
                    autoCognitaTextureRegion = menuItem.getHighlightedAutoCognitaTextureRegion();
                } else if (menuItem.isSelected()) {
                    autoCognitaTextureRegion = menuItem.getSelectedAutoCognitaTextureRegion();
                } else {
                    autoCognitaTextureRegion = menuItem.getUnselectedAutoCognitaTextureRegion();
                }

                drawMenu(autoCognitaTextureRegion, menuItem.getMenuItemEnum().iconPosition);
            }
        }

        batch.end();

    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return new String[]{AssetManagerUtils.ICONS};
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        menuItems = null;
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        for (MenuItemEnum item : MenuItemEnum.values()) {
            if (TouchUtils.isTouched(item.iconPosition, screenX, screenY)) {

                if (MenuItemEnum.HELP.equals(item)) {
                    //only help button has click action
                    callMenuChangeListener(item);
                }
                break;
            }
        }
    }

    private void callMenuChangeListener(MenuItemEnum item) {
        if (null != menuSelectListener) {
            menuSelectListener.onHelpSelected(item);
        }
    }

    private void initMenuItems() {
        if (null == menuItems) {
            menuItems = new MenuItem[MenuItemEnum.values().length];
            for (int i = 0; i < MenuItemEnum.values().length; i++) {
                menuItems[i] = new MenuItem(MenuItemEnum.values()[i], false);

                if (MenuItemEnum.HELP.equals(MenuItemEnum.values()[i])) {
                    helpMenuItem = menuItems[i];
                }
            }

            Texture iconTexture = AssetManagerUtils.getTexture(AssetManagerUtils.ICONS);

            for (MenuItem menuItem : menuItems) {
                switch (menuItem.getMenuItemEnum()) {
                    case HELP:
                        menuItem.setSelectedAutoCognitaTextureRegion(new AutoCognitaTextureRegion(iconTexture, 400, 0, (int) MenuItemEnum.HELP.iconPosition.width, (int) MenuItemEnum.HELP.iconPosition.height));
                        menuItem.setUnselectedAutoCognitaTextureRegion(new AutoCognitaTextureRegion(iconTexture, 400, 100, (int) MenuItemEnum.HELP.iconPosition.width, (int) MenuItemEnum.HELP.iconPosition.height));
                        menuItem.setHighlightedAutoCognitaTextureRegion(new AutoCognitaTextureRegion(iconTexture, 400, 200, (int) MenuItemEnum.HELP.iconPosition.width, (int) MenuItemEnum.HELP.iconPosition.height));
                        //the help icon is always in selected state
                        menuItem.setSelectedWithoutHighLight(true);
                        break;
                }
            }
        }
    }

    private void drawMenu(AutoCognitaTextureRegion menu, IconPosition position) {
        batch.draw(menu, (int) position.x, (int) position.y);
    }

    public enum MenuItemEnum {
        HELP(new IconPosition(50, 50, 100, 100));

        public IconPosition iconPosition;

        MenuItemEnum(IconPosition iconPosition) {
            this.iconPosition = iconPosition;
        }

    }

    public interface IMenuSelectListener {
        void onHelpSelected(MenuItemEnum selectedMenuItemEnum);
    }

    public class MenuItem {
        private MenuItemEnum menuItemEnum;
        private boolean show = true;
        private boolean selected;
        private boolean highLighted;
        private AutoCognitaTextureRegion selectedAutoCognitaTextureRegion;
        private AutoCognitaTextureRegion unselectedAutoCognitaTextureRegion;
        private AutoCognitaTextureRegion highlightedAutoCognitaTextureRegion;

        public MenuItem(MenuItemEnum menuItemEnum, boolean selected) {
            this.menuItemEnum = menuItemEnum;
            this.selected = selected;
        }

        public MenuItemEnum getMenuItemEnum() {
            return menuItemEnum;
        }

        public boolean isShow() {
            return show;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                setHighLighted(true);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        setHighLighted(false);
                    }
                }, 1000);

            }
        }

        public void setSelectedWithoutHighLight(boolean selected) {
            this.selected = selected;
        }

        public AutoCognitaTextureRegion getSelectedAutoCognitaTextureRegion() {
            return selectedAutoCognitaTextureRegion;
        }

        public void setSelectedAutoCognitaTextureRegion(AutoCognitaTextureRegion selectedAutoCognitaTextureRegion) {
            this.selectedAutoCognitaTextureRegion = selectedAutoCognitaTextureRegion;
        }

        public AutoCognitaTextureRegion getUnselectedAutoCognitaTextureRegion() {
            return unselectedAutoCognitaTextureRegion;
        }

        public void setUnselectedAutoCognitaTextureRegion(AutoCognitaTextureRegion unselectedAutoCognitaTextureRegion) {
            this.unselectedAutoCognitaTextureRegion = unselectedAutoCognitaTextureRegion;
        }

        public AutoCognitaTextureRegion getHighlightedAutoCognitaTextureRegion() {
            return highlightedAutoCognitaTextureRegion;
        }

        public void setHighlightedAutoCognitaTextureRegion(AutoCognitaTextureRegion highlightedAutoCognitaTextureRegion) {
            this.highlightedAutoCognitaTextureRegion = highlightedAutoCognitaTextureRegion;
        }

        public boolean isHighLighted() {
            return highLighted;
        }

        public void setHighLighted(boolean highLighted) {
            this.highLighted = highLighted;
        }
    }
}
