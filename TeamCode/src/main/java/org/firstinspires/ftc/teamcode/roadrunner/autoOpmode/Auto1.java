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

    boolean intakeTest = false;

    List<Integer> sorter = new ArrayList<>(Arrays.asList(0,0,0));
    // The order has list pos "0" as the first slot in the clockwise direction of the intake position, the intake in the lost pos is 2
    // this ignores half steps
    // Nothing = value "0", green = value "1", purple = value "2"

    /// This class detects color and sets the first position in the list
    public class CheckColor {
        ColorSensor checkColorSensor;
        double targetValue;
        boolean greenTrue, purpleTrue, ballThere = false;
        public CheckColor(HardwareMap hardwareMap) {
            checkColorSensor = hardwareMap.get(ColorSensor.class, "checkColorSensor");
        }

        public void checkBall() {
            double blueValue = checkColorSensor.blue();
            double greenValue = checkColorSensor.green();
            if (greenValue > 100) {
                ballThere = true;
            }
            if (blueValue > greenValue && ballThere) {
                purpleTrue = true;
                greenTrue = false;
                sorter.set(2,1);
                targetValue += 180;
            } else if (blueValue < greenValue && ballThere) {
                greenTrue = true;
                purpleTrue = false;
                sorter.set(2,2);
                targetValue += 180;
            } else {
                greenTrue = false;
                purpleTrue = false;
            }
        }

    }

    public void getCheckColor() {
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
        CheckColor checkColor;



        public SorterMove(HardwareMap hardwareMap) {
            sorterMotor = hardwareMap.get(DcMotor.class, "spedMotor");
            sorterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }



        public class SorterMoveAction implements Action {
            public boolean run(@NonNull TelemetryPacket packet) {

                double currPos = sorterMotor.getCurrentPosition();

                if (pattern == 1) {
                    sorter.indexOf(1);
                }

                if (pattern == 2) {

                }

                if (pattern == 3) {

                }

                if (currPos <= checkColor.targetValue-7 || currPos >= checkColor.targetValue+7) {
                    if (currPos < checkColor.targetValue) {
                        sorterMotor.setPower(0.3);
                    }
                    else if (currPos > checkColor.targetValue) {
                        sorterMotor.setPower(-0.3);
                    }
                }
                else if (currPos >= checkColor.targetValue-7 && currPos <= checkColor.targetValue+7){
                    sorterMotor.setPower(0);
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
        CheckColor checkColor = new CheckColor(hardwareMap);


        waitForStart();
        new Thread(() -> {
            while (opModeIsActive()) {
                checkColor.checkBall();
            }
        }).start();


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
