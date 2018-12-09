package zw.home.utils;

import java.sql.*;
import  org.sqlite.SQLiteConfig;
import zw.home.pojos.WordMeanings;

import javax.swing.*;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.Lists;
import static zw.home.utils.Strings.PATH_INJAR_DB;

public class SqliteDB {

    private  Connection connection(){

        SQLiteConfig config = new SQLiteConfig(  );
        config.setReadOnly( true );
       try( Connection connection = DriverManager.getConnection( PATH_INJAR_DB )){
           return  connection;
       }catch ( SQLException ex ){
           JOptionPane.showMessageDialog(null , "Failed to Access Database , Please restart App !" , "Startup Error ." , JOptionPane.ERROR_MESSAGE);
           return null;
       }

    }

    public ImmutableList< WordMeanings > getAllWords(){
        MutableList<WordMeanings> returnVal = Lists.mutable.empty();
        final var sql = "SELECT * FROM dictionary LIMIT 2000 ";
        try (Connection conn = this.connection(); Statement stmt  = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {

                returnVal.add( new WordMeanings(
                        rs.getInt("id"),
                        rs.getString("word") ,
                        rs.getString("definitions")
                        )
                );
            }
        } catch ( SQLException ex ){
            JOptionPane.showMessageDialog(null , "Failed to Read Database , Please restart App !" , "Read Error ." , JOptionPane.ERROR_MESSAGE);
        }
return returnVal.toImmutable();

    }

}
