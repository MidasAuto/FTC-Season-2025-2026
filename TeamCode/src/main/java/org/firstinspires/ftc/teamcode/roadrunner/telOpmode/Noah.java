package org.firstinspires.ftc.teamcode.roadrunner.telOpmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Noah")
public class Noah extends OpMode {

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
        // keep your original stick mapping
        double drive  = gamepad1.left_stick_y; // forward/back
        double strafe = gamepad1.left_stick_x;  // left/right
        double turn   = -gamepad1.right_stick_x;   // rotation

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

        // apply power
        frontRightMotor.setPower(fRightPower);
        frontLeftMotor.setPower(1);
        backRightMotor.setPower(bRightPower);
        backLeftMotor.setPower(bLeftPower);

        // quick debug telemetry
        telemetry.addData("fR", fRightPower);
        telemetry.addData("fL", fLeftPower);
        telemetry.addData("bR", bRightPower);
        telemetry.addData("bL", bLeftPower);
        telemetry.update();
    }
}