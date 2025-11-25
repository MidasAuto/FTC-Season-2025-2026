package org.firstinspires.ftc.teamcode.roadrunner.autoOpmode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.PinpointEncoder;
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


    public static class PoseStorage {
        // See this static keyword? That's what lets us share the data between opmodes.
        public static Pose2d currentPose = new Pose2d(null, null);

        public PoseStorage(Pose2d currentPose) {

        }

        public void getClass(Pose2d currentPose) {

        }
    }

    boolean intakeTest = false;

    List<Integer> sorter = new ArrayList<>(Arrays.asList(0,0,0));
    // The order has list pos "0" as the first slot in the clockwise direction of the intake position, the intake in the lost pos is 2
    // this ignores half steps
    // Nothing = value "0", green = value "1", purple = value "2"

    /// This class detects color and sets the first position in the list
    public class CheckColor {
        ColorSensor checkColorSensor;
        double targetValue;
        boolean greenTrue, purpleTrue, ballThere = false, sorterMoving = false;
        public CheckColor(HardwareMap hardwareMap) {
            checkColorSensor = hardwareMap.get(ColorSensor.class, "checkColorSensor");
        }

        public void checkBall() {
            double blueValue = checkColorSensor.blue();
            double greenValue = checkColorSensor.green();
            if (greenValue > 100) {
                ballThere = true;
            }
            if (blueValue > greenValue && ballThere && !sorterMoving) {
                purpleTrue = true;
                greenTrue = false;
                sorter.set(0,1);
                targetValue += 180;
            } else if (blueValue < greenValue && ballThere && !sorterMoving) {
                greenTrue = true;
                purpleTrue = false;
                sorter.set(0,2);
                targetValue += 180;
            } else {
                greenTrue = false;
                purpleTrue = false;
            }
        }

    }

    /// Intake Actions

    // Intake Start
    public class Intake {
        DcMotor intakeMotor;

        public Intake(HardwareMap hardwareMap) {
            intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
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

                double currPos = sorterMotor.getCurrentPosition(), greenPosNeed = 0;

                if (pattern == 1) {
                    greenPosNeed = 0;
                } else if (pattern == 2) {
                    greenPosNeed = 1;
                } else if (pattern == 3) {
                    greenPosNeed = 2;
                }

                if (greenPosNeed < sorter.indexOf(1)) {
                    checkColor.targetValue += 180;
                    sorter.set(sorter.indexOf(1), 2);
                    sorter.set(sorter.indexOf(1)+1, 1);
                } else if (greenPosNeed > sorter.indexOf(1)) {
                    checkColor.targetValue -= 180;
                    sorter.set(sorter.indexOf(1), 2);
                    sorter.set(sorter.indexOf(1)-1, 1);
                }

                if (currPos <= checkColor.targetValue-7 || currPos >= checkColor.targetValue+7) {
                    if (currPos < checkColor.targetValue) {
                        sorterMotor.setPower(0.3);
                        checkColor.sorterMoving = true;
                    }
                    else if (currPos > checkColor.targetValue) {
                        sorterMotor.setPower(-0.3);
                        checkColor.sorterMoving = true;
                    }
                }
                else if (currPos >= checkColor.targetValue-7 && currPos <= checkColor.targetValue+7){
                    sorterMotor.setPower(0);
                }
                return true;
            }
        }

        public Action sorterMoveAction(){
            return new SorterMoveAction();
        }

    }

    public void telemetry() {

        telemetry.addData("Pos0", sorter.get(0));
        telemetry.addData("Pos1", sorter.get(1));
        telemetry.addData("Pos2", sorter.get(2));

        telemetry.update();
    }

    /// ShootAction

    public class Shoot {
        DcMotor launch1;
        DcMotor launch2;

        public Shoot(HardwareMap hardwareMap) {
            launch1 = hardwareMap.get(DcMotor.class, "launch1");
            launch2 = hardwareMap.get(DcMotor.class, "launch2");
        }

        public class ShootAction implements Action {
            public boolean run(@NonNull TelemetryPacket packet) {

               double shootAlgy;



                return false;
            }
        }

        public Action shootAction(){
            return new ShootAction();
        }

    }

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

        PoseStorage.currentPose = drive.getPose();
    }
}
