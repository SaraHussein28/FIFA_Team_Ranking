package Database;

import Algorithms.Team;

import java.sql.*;
import java.util.Objects;

public class Queries {

    public Boolean GetAllTeams(){
        Connection conn = MySQL_Connector.ConnectDB();


        try{
            Statement stmt = Objects.requireNonNull(conn).createStatement();
            ResultSet rs = stmt.executeQuery("select * from Teams");

            while (rs.next()){
                String name = rs.getString("Name");
                double score = rs.getDouble("Score");
                int rank = rs.getInt("Rank");

            }
            return true;
        }catch(SQLException e){
            return false;
        }

    }

    public static ResultSet  getTeams(Team team)throws SQLException{
        Connection conn = MySQL_Connector.ConnectDB();


        try{
            PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement
                    ("select * from Teams where Name = ?");
            pstmt.setString(1,team.getName());
            ResultSet rs = pstmt.executeQuery();

            return rs;
        }catch(SQLException e){
            throw new SQLException();
        }

    }

    public Boolean addMatch(String team1Name, String team2Name, String competitionType, String round,
    int team1score, int team2score, int importance,boolean PSO){


        Connection conn = MySQL_Connector.ConnectDB();
        try{
            PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement
                    ("INSERT INTO Matches(Team1, Team2, Importance, Competition_type, Round, Team1_Score, Team2_Score, PSO)" +
                            " VALUES(?, ?, ?,?,?,?,?,?)");
            pstmt.setString(1, team1Name);
            pstmt.setString(2, team2Name);
            pstmt.setInt(3, importance);
            pstmt.setString(4, competitionType);
            pstmt.setString(5, round);
            pstmt.setInt(6, team1score);
            pstmt.setInt(7, team2score);
            pstmt.setBoolean(8, PSO);
            pstmt.execute();
            return true;
        }catch (SQLException e){
            return false;
        }

    }

    public Boolean getAllMatches() {
        Connection conn = MySQL_Connector.ConnectDB();
        try{
            Statement stmt = Objects.requireNonNull(conn).createStatement();
            ResultSet rs = stmt.executeQuery("select * from Matches");

            while(rs.next()){
                int id = rs.getInt("Id");
                String team1 = rs.getString("Team1");
                String team2 = rs.getString("Team2");
                int imp = rs.getInt("Importance");
                String comp_type = rs.getString("Competition_type");
                String round = rs.getString("Round");
                int team1_score = rs.getInt("Team1_Score");
                int team2_score = rs.getInt("Team2_Score");
                int PSO = rs.getInt("PSO");

                System.out.println("Match " + id + ":");
                System.out.println("Team1 : " + team1 + " , Team1 score: " + team1_score
                        + " Team 2 : " + team2 + " , Team2 score " + team2_score);
            }
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public Boolean deleteAllMatches(){
        Connection conn = MySQL_Connector.ConnectDB();
        try{
            Statement stmt = Objects.requireNonNull(conn).createStatement();
            stmt.executeUpdate("DELETE FROM Matches");
            return true;
        }catch (SQLException e){
            return false;
        }
    }
    public static ResultSet getLastMatch() throws SQLException {
        Connection conn = MySQL_Connector.ConnectDB();
        Statement stmt = Objects.requireNonNull(conn).createStatement();
        ResultSet rs = stmt.executeQuery("select * from Matches order by ID desc limit 1");
        rs.next();

        /*System.out.println("Last match data:");
        System.out.println("Match id : " + rs.getInt("Id"));
        System.out.println("Team1 : " + rs.getString("Team1") + " , Team1 score: " + rs.getString("Team1_Score")
                + " Team 2 : " + rs.getString("Team2") + " , Team2 score " + rs.getString("Team2_Score"));*/
        return rs;

    }
    public Boolean deleteTeam(String TeamName) throws SQLException {
        Connection conn = MySQL_Connector.ConnectDB();
        try {
            PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(
                    "delete from Teams where Name = ?");
            pstmt.setString(1, TeamName);
            pstmt.execute();
            return true;
        }catch(SQLException e){
                return false;
        }
    }
    public Boolean addTeam(String teamName, double score, int rank) throws SQLException {
        Connection conn = MySQL_Connector.ConnectDB();
        try {
            PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(
                    "Insert into Teams (Name, Score, Rank) VALUES (?, ?, ?)");
            pstmt.setString(1, teamName);
            pstmt.setDouble(2, score);
            pstmt.setInt(3, rank);
            pstmt.execute();
            return true;
        } catch(SQLException e){
            return false;
        }
    }
    public Boolean addTeamMissingField(String teamName, double score) throws SQLException {
        Connection conn = MySQL_Connector.ConnectDB();
        try {
            PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(
                    "Insert into Teams VALUES (?, ?)");
            pstmt.setString(1, teamName);
            pstmt.setDouble(2, score);
            pstmt.execute();
            return true;
        } catch(SQLException e){
            return false;
        }
    }

    public static Boolean getScoreFromHistory(String TeamName, int month, int year) throws SQLException {
        Connection conn = MySQL_Connector.ConnectDB();
        PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(
                "select Score from score_history where TeamName = ? and Month = ? and Year = ? order by MatchId desc limit 1");
        pstmt.setString(1, TeamName);
        pstmt.setInt(2, month);
        pstmt.setInt(3, year);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println(rs.getDouble("Score"));
            return true;
        } else {
            pstmt = Objects.requireNonNull(conn).prepareStatement(
                    "select Score from score_history where TeamName = ? and Month < ? and Year = ? order by Month desc, MatchId desc limit 1");
            pstmt.setString(1, TeamName);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println(rs.getDouble("Score"));
                return true;
            } else {
                pstmt = Objects.requireNonNull(conn).prepareStatement(
                        "select Score from score_history where TeamName = ? and Year < ? order by Year desc, Month desc, MatchId desc limit 1");
                pstmt.setString(1, TeamName);
                pstmt.setInt(2, year);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println(rs.getDouble("Score"));
                    return true;
                } else return false;
            }
        }
    }

    private static void clearTables() throws SQLException{
        Connection conn = MySQL_Connector.ConnectDB();
        try {
            PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement
                    ("DELETE FROM score_history");
            pstmt.execute();
            pstmt = Objects.requireNonNull(conn).prepareStatement
                    ("DELETE FROM matches");
            pstmt.execute();
            pstmt = Objects.requireNonNull(conn).prepareStatement
                    ("DELETE FROM teams");
            pstmt.execute();
        } catch(SQLException e){
            throw new SQLException();
        }
    }
    private static void insertDummyTeams()throws SQLException{
        Connection conn = MySQL_Connector.ConnectDB();
        try {
            PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(
                    "Insert into Teams (`Name`, `Score`, `Rank`) VALUES (?, ?, ?)");
            pstmt.setString(1, "Austria");
            pstmt.setDouble(2, 1500);
            pstmt.setInt(3, 0);
            pstmt.addBatch();
            pstmt.setString(1, "Egypt");
            pstmt.setDouble(2, 1500);
            pstmt.setInt(3, 0);
            pstmt.addBatch();
            pstmt.setString(1, "Germany");
            pstmt.setDouble(2, 1500);
            pstmt.setInt(3, 0);
            pstmt.addBatch();
            pstmt.executeBatch();

        } catch(SQLException e){
            throw new SQLException();
        }
    }
    private static void insertDummyMatches()throws SQLException{
        Connection conn = MySQL_Connector.ConnectDB();
        try {
            PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(
                    "INSERT INTO Matches (`Id`,`Team1`,`Team2`,`Importance`,`Competition_type`,`Round`,`Team1_Score`,`Team2_Score`,`PSO`) VALUES (?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1,1);
            pstmt.setString(2, "Austria");
            pstmt.setString(3,"Egypt");
            pstmt.setInt(4,5);
            pstmt.setString(5,"Friendly Match");
            pstmt.setString(6,"Group Stage");
            pstmt.setInt(7, 0);
            pstmt.setInt(8, 0);
            pstmt.setInt(9, 0);
            pstmt.addBatch();
            pstmt.setInt(1,2);
            pstmt.setString(2, "Germany");
            pstmt.setString(3,"Egypt");
            pstmt.setInt(4,5);
            pstmt.setString(5,"Friendly Match");
            pstmt.setString(6,"Group Stage");
            pstmt.setInt(7, 0);
            pstmt.setInt(8, 0);
            pstmt.setInt(9, 0);
            pstmt.addBatch();
            pstmt.executeBatch();

        } catch(SQLException e){
            throw new SQLException();
        }
    }

    private static void insertDummyHistory()throws SQLException{
        Connection conn = MySQL_Connector.ConnectDB();
        try {
            PreparedStatement pstmt = Objects.requireNonNull(conn).prepareStatement(
                    "INSERT INTO score_history (`TeamName`,`MatchID`,`Month`,`Year`,`Score`) VALUES (?,?,?,?,?)");
            pstmt.setString(1,"Austria");
            pstmt.setInt(2,1);
            pstmt.setInt(3,1);
            pstmt.setInt(4,2021);
            pstmt.setDouble(5,1500);
            pstmt.addBatch();
            pstmt.setString(1,"Egypt");
            pstmt.setInt(2,1);
            pstmt.setInt(3,1);
            pstmt.setInt(4,2021);
            pstmt.setDouble(5,1500);
            pstmt.addBatch();
            pstmt.setString(1,"Egypt");
            pstmt.setInt(2,2);
            pstmt.setInt(3,1);
            pstmt.setInt(4,2021);
            pstmt.setDouble(5,1500);
            pstmt.addBatch();
            pstmt.setString(1,"Germany");
            pstmt.setInt(2,2);
            pstmt.setInt(3,1);
            pstmt.setInt(4,2021);
            pstmt.setDouble(5,1500);
            pstmt.addBatch();
            pstmt.executeBatch();

        } catch(SQLException e){
            throw new SQLException();
        }
    }

    public static void resetDB() throws SQLException {
        clearTables();
        insertDummyTeams();
        insertDummyMatches();
        insertDummyHistory();
    }
}
