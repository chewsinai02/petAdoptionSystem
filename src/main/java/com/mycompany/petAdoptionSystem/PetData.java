package com.mycompany.petAdoptionSystem;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class PetData {

  public static String displayPets() {
    String SCHEMA_NAME = null;  // Set schema name if required
    StringBuilder builder = new StringBuilder();

    try (Connection connection = DatabaseConnection.getConnection()) {
        DatabaseMetaData metaData = connection.getMetaData();
        String[] tableType = {"TABLE"};

        try (ResultSet tables = metaData.getTables(null, SCHEMA_NAME, null, tableType)) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                
                if(!tableName.equalsIgnoreCase("pet")) {  // Changed from "pet_adoption" to "pet"
                    continue;
                }

                builder.append("Table: ").append(tableName).append("\n");

                try (Statement stmt = connection.createStatement();
                     ResultSet data = stmt.executeQuery("SELECT * FROM " + tableName)) {

                    int columnCount = data.getMetaData().getColumnCount();
                    boolean hasData = false;

                    while (data.next()) {
                        hasData = true;
                        for (int i = 1; i <= columnCount; i++) {
                            builder.append(data.getString(i)).append("\t");
                        }
                        builder.append("\n");
                    }

                    if (!hasData) {
                        builder.append("No data found.\n");
                    }
                } catch (Exception e) {
                    builder.append("Error retrieving data from ").append(tableName)
                           .append(": ").append(e.getMessage()).append("\n");
                }
            }
        }
        System.out.println(builder.toString());
    } catch (Exception e) {
        System.out.println("Error accessing database: " + e.getMessage());
    }
    return builder.toString();
}
}

