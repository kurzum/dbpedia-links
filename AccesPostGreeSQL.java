package com.sameas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccesPostGreeSQL {
	
    public static void main(String[] args) {
    	AccesPostGreeSQL ac = new AccesPostGreeSQL();
    	ac.insertTriple("<nada3>", "<sameas>", "<tudo3>", "TBL_URI");
       
    }
    
    public static void truncateTable(String pTable) {
    	
		Connection con = null;
        ResultSet rs = null;
        Statement st = null;

        String url = "jdbc:postgresql://localhost/dbSameAs";
        String user = "postgres";
        String password = "aux123";

        try {
            con = DriverManager.getConnection(url, user, password);
            
            st = con.createStatement();
            String sql = "Truncate table \""+pTable+"\"";
            st.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(AccesPostGreeSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(AccesPostGreeSQL.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
		
	}
	
    public String insertTriple(String pURIFreeBase, String pSAMEAS,
			String pURIDBpedia, String pTableName) {
    	
    	String ret = null;
		Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        String url = "jdbc:postgresql://localhost/dbSameAs";
        String user = "postgres";
        String password = "aux123";

        try {
            con = DriverManager.getConnection(url, user, password);
            
            pst = con.prepareStatement("INSERT INTO \""+pTableName+"\"(\"URI_FreeBase\", \"SAME_AS\", \"URI_DBpedia\") VALUES(?,?,?)");
            pst.setString(1, pURIFreeBase);
            pst.setString(2, pSAMEAS);
            pst.setString(3, pURIDBpedia);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(AccesPostGreeSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(AccesPostGreeSQL.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
                ret = "Error";
            }
        }
		return ret;
		
	}
    
    public String insertTripleRedirect(String pFrom, String pTo,
			 String pTableName) {
    	
    	String ret = null;
		Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        String url = "jdbc:postgresql://localhost/dbSameAs";
        String user = "postgres";
        String password = "aux123";

        try {
            con = DriverManager.getConnection(url, user, password);
            
            pst = con.prepareStatement("INSERT INTO \""+pTableName+"\"(\"From\", \"To\") VALUES(?,?)");
            pst.setString(1, pFrom);
            pst.setString(2, pTo);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(AccesPostGreeSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(AccesPostGreeSQL.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
                ret = "Error";
            }
        }
		return ret;
		
	}
}
