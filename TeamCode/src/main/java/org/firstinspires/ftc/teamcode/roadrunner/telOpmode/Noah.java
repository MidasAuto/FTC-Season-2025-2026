package org.firstinspires.ftc.teamcode.roadrunner.telOpmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Noah")
public class Noah extends OpMode {

    DcMotor frontRightMotor;
    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor backLeftMotor;
    DcMotor intakeMotor;
    DcMotor launch1;
    DcMotor launch2;
    Servo rampAngle;

    // Tweak this slightly if the left side still feels faster (ex: 0.95 -> 0.92)

    @Override
    public void init() {
        frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
        frontLeftMotor  = hardwareMap.get(DcMotor.class, "leftFront");
        backLeftMotor   = hardwareMap.get(DcMotor.class, "leftBack");
        backRightMotor  = hardwareMap.get(DcMotor.class, "rightBack");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        launch1 = hardwareMap.get(DcMotor.class, "launch1");
        launch2 = hardwareMap.get(DcMotor.class, "launch2");
        rampAngle = hardwareMap.get(Servo.class, "rampAngle");

        // ORIGINAL: only frontLeft reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        launch2.setDirection(DcMotorSimple.Direction.REVERSE);
        // backLeftMotor left as default (no reverse)

        // Brake so robot stops instead of coasting
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Use encoders like your original code
        /*
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotoaz`r.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

         */
    }

    @Override
    public void loop() {
        // keep your original stick mapping
        double drive  = gamepad1.left_stick_y; // forward/back
        double strafe = gamepad1.left_stick_x;  // left/right
        double turn   = -gamepad1.right_stick_x;   // rotation
        double rampAngle1 = gamepad2.left_stick_y/1.85-0.35;

        double fRightPower;
        double fLeftPower;
        double bRightPower;
        double bLeftPower;

        // motor power calc with left-side compensation
        if (gamepad2.left_bumper && gamepad2.right_bumper) {
            fRightPower = 0;
            fLeftPower = 0;
            bRightPower = 0;
            bLeftPower = 0;
        } else {
            fRightPower = drive + turn + strafe;
            fLeftPower  = (drive - turn - strafe);
            bRightPower = drive + turn - strafe;
            bLeftPower  = (drive - turn + strafe);
        }


        // normalize so no value exceeds ±1
        // apply power
        frontRightMotor.setPower(fRightPower);
        frontLeftMotor.setPower(fLeftPower);
        backRightMotor.setPower(bRightPower);
        backLeftMotor.setPower(bLeftPower);

        // quick debug telemetry
        telemetry.addData("fR", fRightPower);
        telemetry.addData("fL", fLeftPower);
        telemetry.addData("bR", bRightPower);
        telemetry.addData("bL", bLeftPower);
        telemetry.addData("Servo", rampAngle1);
        telemetry.update();

        if (gamepad2.left_trigger > 0.1) {
            intakeMotor.setPower(1);
        } else {
            intakeMotor.setPower(0);
        }

        if (gamepad2.left_bumper && gamepad2.right_trigger != 0) {
            launch1.setPower(gamepad2.right_trigger);
            launch2.setPower(gamepad2.right_trigger);
        } else if (gamepad2.left_bumper) {
            launch1.setPower(0);
            launch2.setPower(0);
        }

        if (gamepad2.right_bumper && gamepad2.left_bumper) {
            rampAngle.setPosition(rampAngle1);
        }

    /*qa
        if (gamepad2.left_bumper && gamepad2.right_bumper && gamepad2.left_stick_y != 0) {
            rampAngle.setPosition(rampAngle1);
        }
        */

    }
}