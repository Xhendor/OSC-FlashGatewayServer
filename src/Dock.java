
import com.explodingpixels.macwidgets.*;
import com.explodingpixels.widgets.PopupMenuCustomizer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;
import javax.swing.*;

/*
 * Copyright (C) 2012 rosendorsc
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 */
/**
 *
 * @author rosendorsc
 */
public class Dock extends JFrame implements ActionListener, ComponentListener {

    private JButton button;
    private boolean on = false;
    private Gateway myGateway;
    private MenuItem exitItem;
    private MenuItem hide;
    private final JMenuItem changuePorts;
    private JTextField portFlash;
    private JTextField portOSC;
    private JLabel portlabel;
    private JLabel portFlashlabel;
    private JLabel portOSCLabel;
    private JButton saveButton;
    private HudWindow hud;
    private int portNumFlash = 3000;
    private int portNumOSC = 3333;

    public Dock() {

        this.setSize(200, 150);
        myGateway = new Gateway(3333, 3000);
        this.setResizable(false);
        MacUtils.makeWindowLeopardStyle(this.getRootPane());
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.addComponentListener(this);
        UnifiedToolBar toolBar = new UnifiedToolBar();
        button = new JButton("Start servers");
        button.addActionListener(this);
        button.putClientProperty("JButton.buttonType", "textured");
        toolBar.addComponentToLeft(button);
        SourceListControlBar controlBar = new SourceListControlBar();
        changuePorts = new JMenuItem("Change ports");
        changuePorts.addActionListener(this);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        controlBar.createAndAddPopdownButton(MacIcons.GEAR,
                new PopupMenuCustomizer() {

                    @Override
                    public void customizePopup(JPopupMenu popup) {
                        popup.removeAll();
                        popup.add(changuePorts);
                    }
                });
        this.add(toolBar.getComponent(), BorderLayout.NORTH);
        this.add(controlBar.getComponent(), BorderLayout.SOUTH);
        this.setVisible(true);
        trayIcon();
        this.pack();
    }

    private void trayIcon() {
        PopupMenu popup = new PopupMenu();
        URL url = getMyGateway().getClass().getResource("bulb.png");
        Image icon = Toolkit.getDefaultToolkit().getImage(url);
        TrayIcon trayIcon = new TrayIcon(icon);
        SystemTray tray = SystemTray.getSystemTray();
        MenuItem aboutItem = new MenuItem("About");
        hide = new MenuItem("Hide");
        hide.addActionListener(this);
        exitItem = new MenuItem("Exit");
        exitItem.addActionListener(this);
        popup.addSeparator();

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(hide);
        popup.addSeparator();
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    protected static Image createImage(String path, String description) {

        if (path == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(path, description)).getImage();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println(Dock.class.getResource("/bulb.png"));


        if (ae.getSource() == button && !on) {
            getMyGateway().startServers();
            on = true;
            button.setText("Stop Servers");
        } else if (ae.getSource() == button && on) {
            getMyGateway().stopServers();
            button.setText("Start Servers");
            on = false;

        } else if (ae.getSource() == exitItem) {
            if (on) {
                getMyGateway().stopServers();
                System.exit(0);
            } else {
                System.exit(0);
            }

        } else if (ae.getSource() == hide) {
            if (this.isVisible()) {
                hide.setLabel("Show");
                this.setVisible(false);
            } else {
                hide.setLabel("Hide");

                this.setVisible(true);

            }
        } else if (ae.getSource() == changuePorts) {
            hud = new HudWindow("Configuracion");
            hud.getJDialog().setSize(150, 250);
            hud.getJDialog().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            portFlashlabel = new JLabel("Flash port:");
            portFlashlabel.setForeground(Color.white);
            portOSCLabel = new JLabel("OSC Port:");

            portOSCLabel.setForeground(Color.white);


            portFlash = new JTextField("" + portNumFlash);
            portOSC = new JTextField("" + portNumOSC);
            hud.getJDialog().setLayout(new GridLayout(0, 1));
            hud.getJDialog().add(portFlashlabel);
            hud.getJDialog().add(portFlash);
            hud.getJDialog().add(portOSCLabel);
            hud.getJDialog().add(portOSC);
            hud.getJDialog().add(saveButton);

            hud.getJDialog().setVisible(true);

        } else if (ae.getSource() == saveButton) {
            int portF = Integer.parseInt(portFlash.getText());
            int portO = Integer.parseInt(portOSC.getText());
            portNumFlash = portF;
            portNumOSC = portO;

            if (on) {

                try {


                    getMyGateway().stopServers();
                    button.setText("Star servers");
                    on = false;
                    myGateway = new Gateway(portO, portF);
                    hud.getJDialog().setVisible(false);

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(rootPane, null, "Only numbers!!!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                try {

                    myGateway = new Gateway(portO, portF);
                    hud.getJDialog().setVisible(false);

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(rootPane, null, "Only numbers!!!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    @Override
    public void componentResized(ComponentEvent ce) {
    }

    @Override
    public void componentMoved(ComponentEvent ce) {
    }

    @Override
    public void componentShown(ComponentEvent ce) {
    }

    @Override
    public void componentHidden(ComponentEvent ce) {
        hide.setLabel("Show");
    }

    /**
     * @return the myGateway
     */
    public Gateway getMyGateway() {
        return myGateway;
    }
    
    
}
