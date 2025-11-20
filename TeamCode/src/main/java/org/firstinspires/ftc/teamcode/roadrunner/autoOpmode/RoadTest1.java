package org.firstinspires.ftc.teamcode.roadrunner.autoOpmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.roadrunner.PinpointDrive;
import com.acmerobotics.roadrunner.ftc.Actions;

@Autonomous(name = "RoadrunnerTest1")



public class RoadTest1 extends OpMode {

    DcMotor frontRightMotor;
    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor backLeftMotor;
    DcMotor sorterMotor;
    DcMotor intakeMoter;
    DcMotor launch1;
    DcMotor launch2;
    ColorSensor checkColorSensor;
    Servo launchServo;
    Servo rampAngle;

    double posX, posy;

    Vector2d vector1 = new Vector2d(10, -5);
    Pose2d startPos = new Pose2d(posX, posy, 0);

    @Override
    public void init() {
        frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "leftFront");
        backLeftMotor = hardwareMap.get(DcMotor.class, "leftBack");
        backRightMotor = hardwareMap.get(DcMotor.class, "rightBack");
        sorterMotor = hardwareMap.get(DcMotor.class, "spedMotor");
        intakeMoter = hardwareMap.get(DcMotor.class, "intakeMotor");
        launch1 = hardwareMap.get(DcMotor.class, "launch1");
        launch2 = hardwareMap.get(DcMotor.class, "launch2");
        ///servos
        launchServo = hardwareMap.get(Servo.class, "launchServo");
        rampAngle = hardwareMap.get(Servo.class, "rampAngle");
        ///Color sensor
        checkColorSensor = hardwareMap.get(ColorSensor.class, "checkColorSensor");

        /// ORIGINAL: only frontLeft reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        /// backLeftMotor left as default (no reverse)

        /// Brake so robot stops instead of coasting
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sorterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        /// Use encoders like your original code
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //sorterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }



    @Override
    public void loop() {
        /*
        Trajectory myTrajectory = drive.trajectoryBuilder(new Pose2d(posX, posy, 0))
                .strafeRight(10)
                .forward(5)
                .build();

         */
    }
        /*
    @Override
    public void runOpMode() {

    }

     */
}
