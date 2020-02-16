package util;



import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    //初始化数据库
    /**
     * 获取数据库连接
     * 1.Class.forName("驱动类全名")加载驱，DriverManager.getConnection()
     * 2.DataSource
     * @return
     */
    private static volatile DataSource DATA_SOURCE;
    //volatile禁止指令重排序
    //同步代码块中的代码和之外的代码同时执行，为new代码的执行建立内存屏障
    //保证可见性

    private DBUtil(){

    }

    /**
     * 获取SQLite数据库本地文件路径()
     * @return
     * @throws URISyntaxException
     */
    private static String getUrl() throws URISyntaxException {
        String dbName = "everything.db";

        //classLoader类加载器；getClassLoader(),使用类加载器获取对象
        URL url = DBUtil.class.getClassLoader().getResource(".");

        return "jdbc:sqlite://" + new File(url.toURI()).getParentFile()
                + File.separator + dbName;
                 //File.separator文件分割符
    }

    /**
     * 获取数据库连接池
     * @return
     * @throws URISyntaxException
     */
    private static DataSource getDataSource() throws URISyntaxException {
        if(DATA_SOURCE == null){
            synchronized (DBUtil.class){
                if(DATA_SOURCE == null){
                    //日期格式：mysql: yyyy-MM-dd HH:mm:ss
                    //sqlite日期格式： yyyy-MM-dd HH:mm:ss:SSS
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATE_PATTERN);
                    DATA_SOURCE = new SQLiteDataSource();
                    ((SQLiteDataSource)DATA_SOURCE).setUrl(getUrl());
                }
            }
        }
        return DATA_SOURCE;
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接获取失败");

        }
    }


    public static void close(Connection connection, Statement statement, ResultSet resultSet){
        try {
            if (resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("释放数据库资源错误");
        }
    }

    public static void close(Connection connection,Statement statement){

    }

    public static void main(String[] args) throws URISyntaxException {

       Connection connection = getConnection();
    }
}
