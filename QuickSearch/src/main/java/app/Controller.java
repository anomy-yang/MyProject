package app;
import dao.FileOperatorDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import task.DBInit;
import task.FileOperatorTask;
import task.FileScanCallback;
import task.FileScanTask;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<FileMeta> fileTable;

    @FXML
    private Label srcDirectory;

    private Thread t;

    public void initialize(URL location, ResourceBundle resources) {
        DBInit.init();
        // 添加搜索框监听器，内容改变时执行监听事件
        searchField.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                freshTable(newValue);
            }
        });
    }

    public void choose(Event event) {
        // 选择文件目录
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Window window = rootPane.getScene().getWindow();
        File file = directoryChooser.showDialog(window);
        if(file == null)
            return;
        // 获取选择的目录路径，并显示
        String path = file.getPath();
        // TODO
        System.out.println(path);
        srcDirectory.setText(path);

        if(t != null) {
            t.interrupt();
        }
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileScanCallback callback = new FileOperatorTask();
                    FileScanTask task = new FileScanTask(callback);
                    task.startScan(file);
                    //等待task执行完成
                    task.waitFinish();
                    System.out.println("执行完毕");
                    freshTable(srcDirectory.getText());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

    }


    // 刷新表格数据
    private void freshTable(String text){
        ObservableList<FileMeta> metas = fileTable.getItems();
        metas.clear();
        // TODO

        List<FileMeta> datas = FileOperatorDAO.search(srcDirectory.getText(),searchField.getText());
        metas.addAll(datas);

    }
}