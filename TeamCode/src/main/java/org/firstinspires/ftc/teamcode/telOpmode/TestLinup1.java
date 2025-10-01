package org.firstinspires.ftc.teamcode.telOpmode;

import static java.lang.Math.tan;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "LinupTest")
public class TestLinup1 extends OpMode {

    DcMotor frontRightMotor;
    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor backLeftMotor;

    // Tweak this slightly if the left side still feels faster (ex: 0.95 -> 0.92)
    double leftMotorSpeedFactor = 0.95;

    @Override
    public void init() {
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontLeftMotor  = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor   = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRightMotor  = hardwareMap.get(DcMotor.class, "backRightMotor");

        // ORIGINAL: only frontLeft reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        // backLeftMotor left as default (no reverse)

        // Brake so robot stops instead of coasting
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Use encoders like your original code
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {

        double drive  = gamepad1.left_stick_y; // forward/back
        double strafe = gamepad1.left_stick_x;  // left/right
        double turn   = -gamepad1.right_stick_x;   // rotation


        boolean shotLinup = gamepad1.a;


        if ((shotLinup) && (gamepad1.left_stick_y != 0 || gamepad1.left_stick_x != 0)) {
            double drive_angle = Math.atan2(gamepad1.left_stick_y,gamepad1.left_stick_x);

            double r = Math.sqrt(gamepad1.left_stick_x*gamepad1.left_stick_x + gamepad1.left_stick_x*gamepad1.left_stick_x); // sqrt(2) ≈ 1.414


            drive = r * Math.cos(drive_angle);
            strafe = r * Math.sin(drive_angle);

        }
        // keep your original stick mapping

        // motor power calc with left-side compensation
        double fRightPower = drive + turn + strafe;
        double fLeftPower  = (drive - turn - strafe) * leftMotorSpeedFactor;
        double bRightPower = drive + turn - strafe;
        double bLeftPower  = (drive - turn + strafe) * leftMotorSpeedFactor;

        // normalize so no value exceeds ±1
        double max = Math.max(1.0,
                Math.max(Math.abs(fRightPower),
                        Math.max(Math.abs(fLeftPower),
                                Math.max(Math.abs(bRightPower), Math.abs(bLeftPower)))));

        fRightPower /= max;
        fLeftPower  /= max;
        bRightPower /= max;
        bLeftPower  /= max;

        double oppositeSide = 5.0; // Example value
        double adjacentSide = 12.0; // Example value

        // Calculate the tangent ratio
        double tangentRatio = oppositeSide / adjacentSide;

        // Calculate the angle in radians using Math.atan()
        double angleInRadians = Math.atan(tangentRatio);

        // Convert the angle from radians to degrees (optional, for readability)
        double angleInDegrees = Math.toDegrees(angleInRadians);

        // apply power
        frontRightMotor.setPower(fRightPower);
        frontLeftMotor.setPower(fLeftPower);
        backRightMotor.setPower(bRightPower);
        backLeftMotor.setPower(bLeftPower);

        // quick debug telemetry

    }
}