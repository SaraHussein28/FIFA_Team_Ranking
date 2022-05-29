package Algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    Team team;
    static double points = 1500.5;
    @BeforeEach
    void setup(){
        team = new Team("Egypt",points,3);
    }
    @ParameterizedTest
    @DisplayName("Test Win Diff")
    @MethodSource("calc_new_points_arguments")
    void calculateNewPoints(int importance,double win, double expectedWin, boolean immunity, double expected) {
        team.calculateNewPoints(importance,win,expectedWin,immunity);
        assertEquals(expected,team.getPoints());
    }

    private static Stream<Arguments> calc_new_points_arguments(){
        return Stream.of(
                // Format: importance, win, expectedWin, immunity, expected new points

                //Tests with immunity, the points shouldn't decrease if the win is less than the expected win
                Arguments.of(10,0,0.7,true,points),
                Arguments.of(10,0.5,0.8,true,points),
                Arguments.of(10,0,0.7,true,points),
                Arguments.of(10,1,0.7,true,1503.5),
                //Tests without immunity
                Arguments.of(10,1,0.7,false,1503.5),
                Arguments.of(10,0,0.7,false,1493.5),
                Arguments.of(10,0.5,0.8,false,1497.5)
        );
    }


    @ParameterizedTest
    @DisplayName("Test Win Diff")
    @MethodSource("WinDiff_arguments")
    void calcWinDiff(double win, double expectedWin, double expected_result) {
        double result = team.calcWinDiff(win,expectedWin);
        assertEquals(expected_result,result,1e-15);
    }

    private static Stream<Arguments> WinDiff_arguments(){
        return Stream.of(
                // Format: W,We,expected answer
                Arguments.of(0.0,0.0,0.0),
                Arguments.of(0.0,0.25,-0.25),
                Arguments.of(0.0,0.5,-0.5),
                Arguments.of(0.0,0.65,-0.65),
                Arguments.of(0.0,0.75,-0.75),
                Arguments.of(0.0,0.9,0.-0.9),
                Arguments.of(0.0,1.0,-1.0),
                Arguments.of(0.5,0.0,0.5),
                Arguments.of(0.5,0.25,0.25),
                Arguments.of(0.5,0.5,0.0),
                Arguments.of(0.5,0.65,-0.15),
                Arguments.of(0.5,0.75,-0.25),
                Arguments.of(0.5,0.9,0.-0.4),
                Arguments.of(0.5,1.0,-0.5),
                Arguments.of(0.75,0.0,0.75),
                Arguments.of(0.75,0.25,0.5),
                Arguments.of(0.75,0.5,0.25),
                Arguments.of(0.75,0.65,0.1),
                Arguments.of(0.75,0.75,0.0),
                Arguments.of(0.75,0.9,0.-0.15),
                Arguments.of(0.75,1.0,-0.25),
                Arguments.of(1.0,0.0,1.0),
                Arguments.of(1.0,0.25,0.75),
                Arguments.of(1.0,0.5,0.5),
                Arguments.of(1.0,0.65,0.35),
                Arguments.of(1.0,0.75,0.25),
                Arguments.of(1.0,0.9,0.1),
                Arguments.of(1.0,1.0,0)
        );
    }
}