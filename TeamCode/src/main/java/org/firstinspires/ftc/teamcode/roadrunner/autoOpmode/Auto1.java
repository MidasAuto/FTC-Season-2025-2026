package org.firstinspires.ftc.teamcode.roadrunner.autoOpmode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.ftc.Actions;
import org.firstinspires.ftc.teamcode.roadrunner.PinpointDrive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Autonomous


public class Auto1 extends LinearOpMode {

    List<Integer> sorter = new ArrayList<>(Arrays.asList(0,0,0));
    // The order has list pos "0" as the first slot in the clockwise direction of the intake postion, the intake in the lost pos is 2
    // this ignores half steps
    // Nothing = value "0", green = value "1", purple = value "2"

    public class CheckColor {
        ColorSensor checkColorSensor;
        double blueValue = checkColorSensor.blue();
        double greenValue = checkColorSensor.green();
        double targetValue;
        boolean greenTrue, purpleTrue, ballThere = false;
        public CheckColor(HardwareMap hardwareMap) {
            checkColorSensor = hardwareMap.get(ColorSensor.class, "checkColorSensor");

            if (greenValue > 100) {
                ballThere = true;
            }

            if (blueValue > greenValue && ballThere) {
                purpleTrue = true;
                greenTrue = false;
                sorter.set(2,1);
            } else if (blueValue < greenValue && ballThere) {
                greenTrue = true;
                purpleTrue = false;
                sorter.set(2,2);
            }
        }

    }

    /// Intake Actions

    // Intake Start
    public class Intake {
        DcMotor intakeMotor;
        DcMotor sorterMotor;
        ColorSensor checkColorSensor;
        double targetValue;

        public Intake(HardwareMap hardwareMap) {
            intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
            sorterMotor = hardwareMap.get(DcMotor.class, "spedMotor");
            checkColorSensor = hardwareMap.get(ColorSensor.class, "checkColorSensor");
        }

        public class IntakeAction implements Action {
            public boolean run(@NonNull TelemetryPacket packet) {

                intakeMotor.setPower(1);

                if (checkColorSensor.alpha() > 100) {

                }

                return false;
            }
        }

        public Action intakeAction(){
            return new IntakeAction();
        }

    }

    // IntakeStop

    public class IntakeStop {
        DcMotor intakeMotor;

        public IntakeStop(HardwareMap hardwareMap) {
            intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        }

        public class IntakeStopAction implements Action {
            public boolean run(@NonNull TelemetryPacket packet) {

                intakeMotor.setPower(0);

                return false;
            }
        }

        public Action intakeStopAction(){
            return new IntakeStopAction();
        }

    }

    /// SorterMoveAction

    //Sorter


    public class SorterMove {

        double pattern;
        DcMotor sorterMotor;


        public SorterMove(HardwareMap hardwareMap) {
            sorterMotor = hardwareMap.get(DcMotor.class, "spedMotor");
        }

        public class SorterMoveAction implements Action {
            public boolean run(@NonNull TelemetryPacket packet) {

                if (pattern == 21) {

                }

                return false;
            }
        }

        public Action sorterMoveAction(){
            return new SorterMoveAction();
        }

    }

    /// ShootAction



    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(-39,132, Math.toRadians(180));
        PinpointDrive drive = new PinpointDrive(hardwareMap, startPose);
        Intake intake = new Intake(hardwareMap);
        IntakeStop intakeStop = new IntakeStop(hardwareMap);
        SorterMove sorterMove = new SorterMove(hardwareMap);

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(startPose)
                        .setTangent(Math.toRadians(0))
                        .splineToLinearHeading(new Pose2d(-13, 84.5, Math.toRadians(135)), Math.toRadians(315))
                        .stopAndAdd(intake.intakeAction())
                        .stopAndAdd(intakeStop.intakeStopAction())
                        .build()


        );
    }
}
