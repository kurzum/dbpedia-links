package com.sameas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImportTriples {

	public static void main(String[] args) {
		ImportTriples imp = new ImportTriples();
		imp.insertTriplesFile("/home/valdestilhas/Downloads/SameAsPaperDimitris/DataBaseDimitris/testScriptSebastian/inserts.sql");

	}

	private void insertTriplesFile(String pFile) {
		int count = 0;
		int countGeneral = 0;
		// Read the file
		try {
			System.out.println("Starting...");
			long startTime = System.currentTimeMillis();
			String url = "jdbc:postgresql://localhost/dbSameAs";
			String user = "postgres";
			String password = "aux123";
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement prep = conn
					.prepareStatement("INSERT INTO RAW_SAMEAS (dbpedia_uri, link_target) VALUES (?, ?);");

			BufferedReader reader = new BufferedReader(new FileReader(pFile));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("(")) {
					String sLine[] = line.split(" , ");
					String from = sLine[0].substring(2, sLine[0].length() - 1);
					String to = sLine[1].substring(1, sLine[1].length() - 3);
					if (from.startsWith("eraseThis"))
						continue;

					prep.setString(1, from);
					prep.setString(2, to);
					prep.addBatch();
					count++;
					conn.setAutoCommit(false);
					try {
						prep.executeBatch();
					} catch (SQLException exp) {
						System.out.println("Register number: " + count);
						System.out.println("Error: " + exp.getMessage());
					}
					conn.setAutoCommit(true);
				}
			}
			reader.close();
			conn.close();

			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("Total time in millis: " + totalTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
