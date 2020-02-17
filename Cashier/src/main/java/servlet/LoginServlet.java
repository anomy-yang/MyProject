package servlet;

import entity.Account;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //拿到输入的username和password
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "select id,username,password from account where username=?" +
                    " and password=?";
            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            resultSet = preparedStatement.executeQuery();

            Account account = new Account();
            //不为空，说明找到了用户
            if(resultSet.next()){
                //问题：
                account.setId(resultSet.getInt("id"));
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));
            }

            Writer writer = resp.getWriter();
            if(account.getId() == null){
                //System.out.println("用户名或密码错误");
                writer.write("<h2> 用户名或者密码错误:" + username + "</h2>");
            }else{
                HttpSession session = req.getSession(true);
                //若session不存在，则创建一个session对象
                session.setAttribute("user",account);
                resp.sendRedirect("index.html");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
    }
}
