package org.firstinspires.ftc.teamcode.roadrunner.autoOpmode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.ftc.Actions;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.PinpointDrive;


@Autonomous

public class Auto1 extends LinearOpMode {


    public class Intake {
        DcMotor intakeMotor;

        public Intake(HardwareMap hardwareMap) {
            intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        }

        public class IntakeAction implements Action {
            public boolean run(@NonNull TelemetryPacket packet) {
                intakeMotor.setPower(1);

                sleep(3000);

                intakeMotor.setPower(0);

                return false;
            }
        }

        public Action intakeAction(){
            return new IntakeAction();
        }

    }




    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(-39,132, Math.toRadians(180));
        PinpointDrive drive = new PinpointDrive(hardwareMap, startPose);
        Intake intake = new Intake(hardwareMap);

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(startPose)
                        .setTangent(Math.toRadians(0))
                        .splineToLinearHeading(new Pose2d(-13, 84.5, Math.toRadians(135)), Math.toRadians(315))
                        .stopAndAdd(intake.intakeAction())
                        .build()


        );
    }
}
