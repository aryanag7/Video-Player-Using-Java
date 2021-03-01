package sample;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.crypto.spec.PSource;
import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    MediaPlayer player;
    @FXML
    private BorderPane bp;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button playBtn;

    @FXML
    private Button prevBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Slider timeSlider;
    @FXML
    private Slider volumeslider;
    @FXML
    private Button muteBtn;
    @FXML
    private Button ssBtn;

    private double diff=10;






    @FXML
    void openVideomenu(ActionEvent event) {
        try {
            System.out.println("Open song clicked");
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);

            Media m = new Media(file.toURI().toURL().toString());

            if(player!= null){
                player.dispose();
            }

            player = new MediaPlayer(m);
            mediaView.setMediaPlayer(player);
            mediaView.setFitWidth(2000);
            mediaView.setFitHeight(800);

            ///volumeslider
            volumeslider.setValue(player.getVolume()*100);
            volumeslider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    player.setVolume(volumeslider.getValue()/100);
                    try {
                        if ((volumeslider.getValue() / 100) == 0)
                            muteBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/soundofficon.png"))));
                        else
                            muteBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/soundonicon.png"))));
                    }
                    catch ( FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });


            ///time slider


            player.setOnReady(()->{
                timeSlider.setMin(0);
                timeSlider.setMax(player.getMedia().getDuration().toMinutes());


                timeSlider.setValue(0);
                try {
                    playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            });


            ///listener on player

            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    //slider
                   Duration d= player.getCurrentTime();
                   timeSlider.setValue(d.toMinutes());


                }
            });
            ///for slider forward

            timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if(timeSlider.isPressed()){
                        double val=timeSlider.getValue();
                        player.seek(new Duration(val*60*1000));
                    }
                }
            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void play(ActionEvent event) {

        try{
            MediaPlayer.Status status=player.getStatus();

            if(status==MediaPlayer.Status.PLAYING)
            {
                player.pause();
                ///playBtn.setText("Play");
                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));

            }
            else
            {
                player.play();
                ///playBtn.setText("Pause");
                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pause.png"))));

            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
            prevBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/previous.png"))));
            nextBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/next.png"))));
            muteBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/soundonicon.png"))));
            muteBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/soundofficon.png"))));
            ssBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/snap.png"))));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void playBtnclick(ActionEvent event) {
        double d= player.getCurrentTime().toSeconds();
        d=d+diff;
        player.seek(new Duration(d*1000));

    }
    @FXML
    void rotateBtn(ActionEvent event) {
        double  currentR = mediaView.getRotate();
        mediaView.setRotate(currentR+ 90);

    }

    @FXML
    void exit(ActionEvent event) {
        int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to exit?");
        if(result==JOptionPane.YES_OPTION)
            System.exit(0);
    }

    @FXML
    void prevBtnclick(ActionEvent event) {
        double d = player.getCurrentTime().toSeconds();
        d=d-diff;
        player.seek(new Duration(d*1000));
    }



    @FXML
    void settingsMenu(ActionEvent event) {
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(new EmptyBorder(2, 3, 2, 3));
        JPanel layout = new JPanel(new GridBagLayout());
        JButton J1 = new JButton("1. Change Skip By Speed");
        JButton J2 = new JButton("2. Go Fullscreen");
        J1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                diff = Double.parseDouble(JOptionPane.showInputDialog(null,"Enter New Skip Speed in Seconds [ DEFAULT : 10 ]"));
            }
        });
        J2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Stage stageC = (Stage) bp.getScene().getWindow();
                    stageC.setFullScreen(true);
                }
            });
        layout.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel btnPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        btnPanel.add(J1);
        btnPanel.add(J2);
        layout.add(btnPanel);
        p1.add(layout, BorderLayout.CENTER);
        JFrame frame = new JFrame("Settings");
        frame.add(p1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setSize(720, 480);
        frame.setVisible(true);
    }


    @FXML
    void snapshot(ActionEvent event) throws IOException {
        String s = ":";
        String path = "src/screenshots/IMG " + new Date() + ".png";
        String newPath = path.replaceAll(s,".");
        System.out.println("User asks snap.");
        WritableImage img = mediaView.snapshot(new SnapshotParameters(), null);
        BufferedImage bufImg = SwingFXUtils.fromFXImage(img, null);
        ImageIO.write(bufImg, "png", new File(newPath));
    }


    @FXML
    void mute(ActionEvent event) {
        try {
            double x = player.getVolume();
            if (x != 0) {
                player.setVolume(0);
                muteBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/soundofficon.png"))));


            } else {
                player.setVolume(75);
                muteBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/soundonicon.png"))));

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }





}

