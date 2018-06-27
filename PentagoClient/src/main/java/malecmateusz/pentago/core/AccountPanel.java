package malecmateusz.pentago.core;

import com.google.gson.Gson;
import malecmateusz.pentago.components.highscore.HighscoreBox;
import malecmateusz.pentago.config.ConfigManager;
import malecmateusz.pentago.lang.LangBean;
import malecmateusz.pentago.lang.LangController;
import malecmateusz.pentago.lang.LangEvent;
import malecmateusz.pentago.lang.LangListener;
import malecmateusz.pentago.net.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*
Kontroler panelu statystyk, wyświetla wykres statystyk rozegranych meczów,
oraz listę 10 najlepszych graczy
 */

public class AccountPanel extends StackPane implements Initializable, LangListener, MainMenuListener, DataListener{
    @FXML
    private GridPane grid;

    private PieChart pieChart;
    private ConfigManager config;
    private HighscoreBox highscoreBox;
    private NetworkController net;
    private volatile LangBean langBean;
    private AccountDTO accountDTO;

    public AccountPanel(ConfigManager config){
        this.config = config;

        pieChart = new PieChart();
        net = NetworkController.getInstance();
        net.addDataListener(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AccountPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new FailedToLoadViewException(this);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));
        pieChart.setClockwise(true);
        langBean = LangController.getInstance().getActiveBean();
        accountDTO = new AccountDTO();
        highscoreBox = new HighscoreBox();

        createStartPie();
        createStartHighscoreList();

        grid.add(pieChart, 0, 0);
        grid.add(highscoreBox, 1,0);
        this.getChildren().add(grid);
        pieChart.getStyleClass().add("pie");
    }

    private void createPie(){
        StringBuilder sb = new StringBuilder(Window.nickname).append(" ");

        sb.append(langBean.getWins()).append(": ").append(accountDTO.getWins()).append(" ")
                .append(langBean.getLoses()).append(": ").append(accountDTO.getLoses()).append(" ")
                .append(langBean.getLeaves()).append(": ").append(accountDTO.getLeaves());
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data(langBean.getWins(), accountDTO.getWins()),
                        new PieChart.Data(langBean.getLoses(), accountDTO.getLoses()),
                        new PieChart.Data(langBean.getLeaves(), accountDTO.getLeaves()));
        pieChart.setTitle(sb.toString());
        pieChart.setData(pieChartData);

    }

    private synchronized void createStartHighscoreList(){
        highscoreBox.clear();
        highscoreBox.add("AAAA", 10);
        highscoreBox.add("AAAA", 10);
        highscoreBox.add("AAAA", 10);
        highscoreBox.add("AAAA", 10);
        highscoreBox.add("AAAA", 10);
        highscoreBox.add("AAAA", 10);
        highscoreBox.add("AAAA", 10);
        highscoreBox.add("AAAA", 10);
        highscoreBox.add("AAAA", 10);
        highscoreBox.add("AAAA", 10);
        highscoreBox.setTitle(langBean.getHighscoreTitle());
    }

    private void createStartPie(){
        StringBuilder sb = new StringBuilder(Window.nickname).append(" ");

        sb.append(langBean.getWins()).append("0 ")
                .append(langBean.getLoses()).append("0 ")
                .append(langBean.getLeaves()).append(0);
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data(langBean.getWins(), 1),
                        new PieChart.Data(langBean.getLoses(), 1),
                        new PieChart.Data(langBean.getLeaves(),1));
        pieChart.setTitle(sb.toString());
        pieChart.setData(pieChartData);

    }

    private void createHighscoreList(){
        highscoreBox.clear();
        highscoreBox.setTitle(langBean.getHighscoreTitle());
        for(HighscoreDTO h : accountDTO.getHighscores()){
            highscoreBox.add(h.getUsername(), h.getWins());
        }
    }

    @Override
    public void loadLang(LangEvent event) {
        langBean = event.getLangBean();
        createPie();
        createHighscoreList();
    }

    @Override
    public void onClick() {
        net.sendData(new DataProtocol(DataProtocol.HIGHSCORES_REQUEST, null));
    }


    @Override
    public void onDataRecieved(final DataProtocol data) {
        if(data.getMetadata() == DataProtocol.HIGHSCORES_RESPONSE){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    accountDTO = gson.fromJson((String)data.getData(), AccountDTO.class);
                    createPie();
                    createHighscoreList();
                }
            });
        }
    }
}
