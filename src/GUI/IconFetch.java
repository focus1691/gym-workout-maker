package GUI;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class IconFetch {

    private static IconFetch instance;

    private IconFetch(){
    }

    public static IconFetch getInstance() {
        if (instance == null)
            instance = new IconFetch();
        return instance;
    }

    public ImageIcon getIcon(String iconName) {
        URL imgUrl = getClass().getResource(iconName);
        if (imgUrl != null) {
            return new ImageIcon(new ImageIcon(imgUrl).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        } else {
        	System.err.println("Icon " + iconName + " does not exist");
        }
        return null;
    }
}