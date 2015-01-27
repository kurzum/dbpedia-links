package com.services.sameas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.*;
import javax.sql.*;

public class SameAsWS {
	
	/*
	 * User provides an URI and the
	 * API will return the DBpedia URI that is owl:sameAs with the one the user
	 * provided. Example: getSameAsURI("http://rdf.freebase.com/ns/m.015fr");
	 * return: "http://dbpedia.org/resource/Brazil"
	 * @param pURI URI from Freebase
	 * @return 
	 */
	public String getSameAsURI(String pURI, boolean pNormalize) {
		String sRet = "[";
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
        	
        	Context ctx = new InitialContext();   
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/postgres");
          
            
            if (ds != null) 
            {
                con = ds.getConnection();
            }
            //con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            String sQuery = "Select \"URI_DBpedia\" from \"TBL_URI\" where \"URI_FreeBase\" = '"+pURI+"'";
            rs = st.executeQuery(sQuery);

            while (rs.next()) {
            	if(pNormalize)
            	{
            		sRet = getRedirectURI(rs.getString(1));
            		if(sRet != null)
            			break;
            	}
            	else
            		sRet += "{\"uri\":\"" + rs.getString(1) + "\"},";
            }

        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(SameAsWS.class.getName());
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
                Logger lgr = Logger.getLogger(SameAsWS.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        if(!pNormalize)
        	sRet = sRet.substring(0, sRet.length()-1) + "]";
		return sRet;
	}

	private String getRedirectURI(String pURI) {
		String sRet = null;
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
        	
        	Context ctx = new InitialContext();
    
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/postgres");
          
            
            if (ds != null) 
            {
                con = ds.getConnection();
            }
            //con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            String sQuery = "Select \"To\" from \"TBL_REDIRECT\" where \"From\" = '"+pURI+"'";
            rs = st.executeQuery(sQuery);

            while (rs.next()) {
            	sRet = rs.getString(1);
            	if(sRet != null) break;
            }

        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(SameAsWS.class.getName());
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
                Logger lgr = Logger.getLogger(SameAsWS.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        
		return sRet;
	}
}
